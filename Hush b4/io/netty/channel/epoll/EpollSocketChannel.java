// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.epoll;

import io.netty.buffer.ByteBufAllocator;
import java.net.ConnectException;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFutureListener;
import java.util.concurrent.TimeUnit;
import io.netty.channel.ConnectTimeoutException;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.channel.ChannelConfig;
import io.netty.channel.AbstractChannel;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.EventLoop;
import io.netty.channel.ChannelFuture;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.channel.DefaultFileRegion;
import java.io.IOException;
import java.nio.ByteBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.Channel;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ScheduledFuture;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.SocketChannel;

public final class EpollSocketChannel extends AbstractEpollChannel implements SocketChannel
{
    private static final String EXPECTED_TYPES;
    private final EpollSocketChannelConfig config;
    private ChannelPromise connectPromise;
    private ScheduledFuture<?> connectTimeoutFuture;
    private SocketAddress requestedRemoteAddress;
    private volatile InetSocketAddress local;
    private volatile InetSocketAddress remote;
    private volatile boolean inputShutdown;
    private volatile boolean outputShutdown;
    
    EpollSocketChannel(final Channel parent, final int fd) {
        super(parent, fd, 1, true);
        this.config = new EpollSocketChannelConfig(this);
        this.remote = Native.remoteAddress(fd);
        this.local = Native.localAddress(fd);
    }
    
    public EpollSocketChannel() {
        super(Native.socketStreamFd(), 1);
        this.config = new EpollSocketChannelConfig(this);
    }
    
    @Override
    protected AbstractEpollUnsafe newUnsafe() {
        return new EpollSocketUnsafe();
    }
    
    @Override
    protected SocketAddress localAddress0() {
        return this.local;
    }
    
    @Override
    protected SocketAddress remoteAddress0() {
        return this.remote;
    }
    
    @Override
    protected void doBind(final SocketAddress local) throws Exception {
        final InetSocketAddress localAddress = (InetSocketAddress)local;
        Native.bind(this.fd, localAddress.getAddress(), localAddress.getPort());
        this.local = Native.localAddress(this.fd);
    }
    
    private boolean writeBytes(final ChannelOutboundBuffer in, final ByteBuf buf) throws Exception {
        final int readableBytes = buf.readableBytes();
        if (readableBytes == 0) {
            in.remove();
            return true;
        }
        boolean done = false;
        long writtenBytes = 0L;
        if (buf.hasMemoryAddress()) {
            final long memoryAddress = buf.memoryAddress();
            int readerIndex = buf.readerIndex();
            final int writerIndex = buf.writerIndex();
            while (true) {
                final int localFlushedAmount = Native.writeAddress(this.fd, memoryAddress, readerIndex, writerIndex);
                if (localFlushedAmount <= 0) {
                    this.setEpollOut();
                    break;
                }
                writtenBytes += localFlushedAmount;
                if (writtenBytes == readableBytes) {
                    done = true;
                    break;
                }
                readerIndex += localFlushedAmount;
            }
            in.removeBytes(writtenBytes);
            return done;
        }
        if (buf.nioBufferCount() == 1) {
            final int readerIndex2 = buf.readerIndex();
            final ByteBuffer nioBuf = buf.internalNioBuffer(readerIndex2, buf.readableBytes());
            while (true) {
                final int pos = nioBuf.position();
                final int limit = nioBuf.limit();
                final int localFlushedAmount = Native.write(this.fd, nioBuf, pos, limit);
                if (localFlushedAmount <= 0) {
                    this.setEpollOut();
                    break;
                }
                nioBuf.position(pos + localFlushedAmount);
                writtenBytes += localFlushedAmount;
                if (writtenBytes == readableBytes) {
                    done = true;
                    break;
                }
            }
            in.removeBytes(writtenBytes);
            return done;
        }
        final ByteBuffer[] nioBuffers = buf.nioBuffers();
        return this.writeBytesMultiple(in, nioBuffers, nioBuffers.length, readableBytes);
    }
    
    private boolean writeBytesMultiple(final ChannelOutboundBuffer in, final IovArray array) throws IOException {
        long expectedWrittenBytes = array.size();
        int cnt = array.count();
        assert expectedWrittenBytes != 0L;
        assert cnt != 0;
        boolean done = false;
        long writtenBytes = 0L;
        int offset = 0;
        final int end = offset + cnt;
        while (true) {
            long localWrittenBytes = Native.writevAddresses(this.fd, array.memoryAddress(offset), cnt);
            if (localWrittenBytes == 0L) {
                this.setEpollOut();
                break;
            }
            expectedWrittenBytes -= localWrittenBytes;
            writtenBytes += localWrittenBytes;
            if (expectedWrittenBytes == 0L) {
                done = true;
                break;
            }
            do {
                final long bytes = array.processWritten(offset, localWrittenBytes);
                if (bytes == -1L) {
                    break;
                }
                ++offset;
                --cnt;
                localWrittenBytes -= bytes;
            } while (offset < end && localWrittenBytes > 0L);
        }
        in.removeBytes(writtenBytes);
        return done;
    }
    
    private boolean writeBytesMultiple(final ChannelOutboundBuffer in, final ByteBuffer[] nioBuffers, int nioBufferCnt, long expectedWrittenBytes) throws IOException {
        assert expectedWrittenBytes != 0L;
        boolean done = false;
        long writtenBytes = 0L;
        int offset = 0;
        final int end = offset + nioBufferCnt;
        while (true) {
            long localWrittenBytes = Native.writev(this.fd, nioBuffers, offset, nioBufferCnt);
            if (localWrittenBytes == 0L) {
                this.setEpollOut();
                break;
            }
            expectedWrittenBytes -= localWrittenBytes;
            writtenBytes += localWrittenBytes;
            if (expectedWrittenBytes == 0L) {
                done = true;
                break;
            }
            do {
                final ByteBuffer buffer = nioBuffers[offset];
                final int pos = buffer.position();
                final int bytes = buffer.limit() - pos;
                if (bytes > localWrittenBytes) {
                    buffer.position(pos + (int)localWrittenBytes);
                    break;
                }
                ++offset;
                --nioBufferCnt;
                localWrittenBytes -= bytes;
            } while (offset < end && localWrittenBytes > 0L);
        }
        in.removeBytes(writtenBytes);
        return done;
    }
    
    private boolean writeFileRegion(final ChannelOutboundBuffer in, final DefaultFileRegion region) throws Exception {
        final long regionCount = region.count();
        if (region.transfered() >= regionCount) {
            in.remove();
            return true;
        }
        final long baseOffset = region.position();
        boolean done = false;
        long flushedAmount = 0L;
        for (int i = this.config().getWriteSpinCount() - 1; i >= 0; --i) {
            final long offset = region.transfered();
            final long localFlushedAmount = Native.sendfile(this.fd, region, baseOffset, offset, regionCount - offset);
            if (localFlushedAmount == 0L) {
                this.setEpollOut();
                break;
            }
            flushedAmount += localFlushedAmount;
            if (region.transfered() >= regionCount) {
                done = true;
                break;
            }
        }
        if (flushedAmount > 0L) {
            in.progress(flushedAmount);
        }
        if (done) {
            in.remove();
        }
        return done;
    }
    
    @Override
    protected void doWrite(final ChannelOutboundBuffer in) throws Exception {
        while (true) {
            final int msgCount = in.size();
            if (msgCount == 0) {
                this.clearEpollOut();
                break;
            }
            if (msgCount > 1 && in.current() instanceof ByteBuf) {
                if (!this.doWriteMultiple(in)) {
                    break;
                }
                continue;
            }
            else {
                if (!this.doWriteSingle(in)) {
                    break;
                }
                continue;
            }
        }
    }
    
    private boolean doWriteSingle(final ChannelOutboundBuffer in) throws Exception {
        final Object msg = in.current();
        if (msg instanceof ByteBuf) {
            final ByteBuf buf = (ByteBuf)msg;
            if (!this.writeBytes(in, buf)) {
                return false;
            }
        }
        else {
            if (!(msg instanceof DefaultFileRegion)) {
                throw new Error();
            }
            final DefaultFileRegion region = (DefaultFileRegion)msg;
            if (!this.writeFileRegion(in, region)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean doWriteMultiple(final ChannelOutboundBuffer in) throws Exception {
        if (PlatformDependent.hasUnsafe()) {
            final IovArray array = IovArray.get(in);
            final int cnt = array.count();
            if (cnt >= 1) {
                if (!this.writeBytesMultiple(in, array)) {
                    return false;
                }
            }
            else {
                in.removeBytes(0L);
            }
        }
        else {
            final ByteBuffer[] buffers = in.nioBuffers();
            final int cnt = in.nioBufferCount();
            if (cnt >= 1) {
                if (!this.writeBytesMultiple(in, buffers, cnt, in.nioBufferSize())) {
                    return false;
                }
            }
            else {
                in.removeBytes(0L);
            }
        }
        return true;
    }
    
    @Override
    protected Object filterOutboundMessage(final Object msg) {
        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf)msg;
            if (!buf.hasMemoryAddress() && (PlatformDependent.hasUnsafe() || !buf.isDirect())) {
                buf = this.newDirectBuffer(buf);
                assert buf.hasMemoryAddress();
            }
            return buf;
        }
        if (msg instanceof DefaultFileRegion) {
            return msg;
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EpollSocketChannel.EXPECTED_TYPES);
    }
    
    @Override
    public EpollSocketChannelConfig config() {
        return this.config;
    }
    
    @Override
    public boolean isInputShutdown() {
        return this.inputShutdown;
    }
    
    @Override
    public boolean isOutputShutdown() {
        return this.outputShutdown || !this.isActive();
    }
    
    @Override
    public ChannelFuture shutdownOutput() {
        return this.shutdownOutput(this.newPromise());
    }
    
    @Override
    public ChannelFuture shutdownOutput(final ChannelPromise promise) {
        final EventLoop loop = this.eventLoop();
        if (loop.inEventLoop()) {
            try {
                Native.shutdown(this.fd, false, true);
                this.outputShutdown = true;
                promise.setSuccess();
            }
            catch (Throwable t) {
                promise.setFailure(t);
            }
        }
        else {
            loop.execute(new Runnable() {
                @Override
                public void run() {
                    EpollSocketChannel.this.shutdownOutput(promise);
                }
            });
        }
        return promise;
    }
    
    @Override
    public ServerSocketChannel parent() {
        return (ServerSocketChannel)super.parent();
    }
    
    static {
        EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(DefaultFileRegion.class) + ')';
    }
    
    final class EpollSocketUnsafe extends AbstractEpollUnsafe
    {
        private RecvByteBufAllocator.Handle allocHandle;
        
        private void closeOnRead(final ChannelPipeline pipeline) {
            EpollSocketChannel.this.inputShutdown = true;
            if (EpollSocketChannel.this.isOpen()) {
                if (Boolean.TRUE.equals(EpollSocketChannel.this.config().getOption(ChannelOption.ALLOW_HALF_CLOSURE))) {
                    this.clearEpollIn0();
                    pipeline.fireUserEventTriggered(ChannelInputShutdownEvent.INSTANCE);
                }
                else {
                    this.close(this.voidPromise());
                }
            }
        }
        
        private boolean handleReadException(final ChannelPipeline pipeline, final ByteBuf byteBuf, final Throwable cause, final boolean close) {
            if (byteBuf != null) {
                if (byteBuf.isReadable()) {
                    this.readPending = false;
                    pipeline.fireChannelRead(byteBuf);
                }
                else {
                    byteBuf.release();
                }
            }
            pipeline.fireChannelReadComplete();
            pipeline.fireExceptionCaught(cause);
            if (close || cause instanceof IOException) {
                this.closeOnRead(pipeline);
                return true;
            }
            return false;
        }
        
        @Override
        public void connect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
            if (!promise.setUncancellable() || !this.ensureOpen(promise)) {
                return;
            }
            try {
                if (EpollSocketChannel.this.connectPromise != null) {
                    throw new IllegalStateException("connection attempt already made");
                }
                final boolean wasActive = EpollSocketChannel.this.isActive();
                if (this.doConnect((InetSocketAddress)remoteAddress, (InetSocketAddress)localAddress)) {
                    this.fulfillConnectPromise(promise, wasActive);
                }
                else {
                    EpollSocketChannel.this.connectPromise = promise;
                    EpollSocketChannel.this.requestedRemoteAddress = remoteAddress;
                    final int connectTimeoutMillis = EpollSocketChannel.this.config().getConnectTimeoutMillis();
                    if (connectTimeoutMillis > 0) {
                        EpollSocketChannel.this.connectTimeoutFuture = EpollSocketChannel.this.eventLoop().schedule((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                final ChannelPromise connectPromise = EpollSocketChannel.this.connectPromise;
                                final ConnectTimeoutException cause = new ConnectTimeoutException("connection timed out: " + remoteAddress);
                                if (connectPromise != null && connectPromise.tryFailure(cause)) {
                                    EpollSocketUnsafe.this.close(EpollSocketUnsafe.this.voidPromise());
                                }
                            }
                        }, (long)connectTimeoutMillis, TimeUnit.MILLISECONDS);
                    }
                    promise.addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
                        @Override
                        public void operationComplete(final ChannelFuture future) throws Exception {
                            if (future.isCancelled()) {
                                if (EpollSocketChannel.this.connectTimeoutFuture != null) {
                                    EpollSocketChannel.this.connectTimeoutFuture.cancel(false);
                                }
                                EpollSocketChannel.this.connectPromise = null;
                                EpollSocketUnsafe.this.close(EpollSocketUnsafe.this.voidPromise());
                            }
                        }
                    });
                }
            }
            catch (Throwable t) {
                if (t instanceof ConnectException) {
                    final Throwable newT = new ConnectException(t.getMessage() + ": " + remoteAddress);
                    newT.setStackTrace(t.getStackTrace());
                    t = newT;
                }
                this.closeIfClosed();
                promise.tryFailure(t);
            }
        }
        
        private void fulfillConnectPromise(final ChannelPromise promise, final boolean wasActive) {
            if (promise == null) {
                return;
            }
            EpollSocketChannel.this.active = true;
            final boolean promiseSet = promise.trySuccess();
            if (!wasActive && EpollSocketChannel.this.isActive()) {
                EpollSocketChannel.this.pipeline().fireChannelActive();
            }
            if (!promiseSet) {
                this.close(this.voidPromise());
            }
        }
        
        private void fulfillConnectPromise(final ChannelPromise promise, final Throwable cause) {
            if (promise == null) {
                return;
            }
            promise.tryFailure(cause);
            this.closeIfClosed();
        }
        
        private void finishConnect() {
            assert EpollSocketChannel.this.eventLoop().inEventLoop();
            boolean connectStillInProgress = false;
            try {
                final boolean wasActive = EpollSocketChannel.this.isActive();
                if (!this.doFinishConnect()) {
                    connectStillInProgress = true;
                    return;
                }
                this.fulfillConnectPromise(EpollSocketChannel.this.connectPromise, wasActive);
            }
            catch (Throwable t) {
                if (t instanceof ConnectException) {
                    final Throwable newT = new ConnectException(t.getMessage() + ": " + EpollSocketChannel.this.requestedRemoteAddress);
                    newT.setStackTrace(t.getStackTrace());
                    t = newT;
                }
                this.fulfillConnectPromise(EpollSocketChannel.this.connectPromise, t);
            }
            finally {
                if (!connectStillInProgress) {
                    if (EpollSocketChannel.this.connectTimeoutFuture != null) {
                        EpollSocketChannel.this.connectTimeoutFuture.cancel(false);
                    }
                    EpollSocketChannel.this.connectPromise = null;
                }
            }
        }
        
        @Override
        void epollOutReady() {
            if (EpollSocketChannel.this.connectPromise != null) {
                this.finishConnect();
            }
            else {
                super.epollOutReady();
            }
        }
        
        private boolean doConnect(final InetSocketAddress remoteAddress, final InetSocketAddress localAddress) throws Exception {
            if (localAddress != null) {
                AbstractEpollChannel.checkResolvable(localAddress);
                Native.bind(EpollSocketChannel.this.fd, localAddress.getAddress(), localAddress.getPort());
            }
            boolean success = false;
            try {
                AbstractEpollChannel.checkResolvable(remoteAddress);
                final boolean connected = Native.connect(EpollSocketChannel.this.fd, remoteAddress.getAddress(), remoteAddress.getPort());
                EpollSocketChannel.this.remote = remoteAddress;
                EpollSocketChannel.this.local = Native.localAddress(EpollSocketChannel.this.fd);
                if (!connected) {
                    EpollSocketChannel.this.setEpollOut();
                }
                success = true;
                return connected;
            }
            finally {
                if (!success) {
                    EpollSocketChannel.this.doClose();
                }
            }
        }
        
        private boolean doFinishConnect() throws Exception {
            if (Native.finishConnect(EpollSocketChannel.this.fd)) {
                EpollSocketChannel.this.clearEpollOut();
                return true;
            }
            EpollSocketChannel.this.setEpollOut();
            return false;
        }
        
        private int doReadBytes(final ByteBuf byteBuf) throws Exception {
            final int writerIndex = byteBuf.writerIndex();
            int localReadAmount;
            if (byteBuf.hasMemoryAddress()) {
                localReadAmount = Native.readAddress(EpollSocketChannel.this.fd, byteBuf.memoryAddress(), writerIndex, byteBuf.capacity());
            }
            else {
                final ByteBuffer buf = byteBuf.internalNioBuffer(writerIndex, byteBuf.writableBytes());
                localReadAmount = Native.read(EpollSocketChannel.this.fd, buf, buf.position(), buf.limit());
            }
            if (localReadAmount > 0) {
                byteBuf.writerIndex(writerIndex + localReadAmount);
            }
            return localReadAmount;
        }
        
        @Override
        void epollRdHupReady() {
            if (EpollSocketChannel.this.isActive()) {
                this.epollInReady();
            }
            else {
                this.closeOnRead(EpollSocketChannel.this.pipeline());
            }
        }
        
        @Override
        void epollInReady() {
            final ChannelConfig config = EpollSocketChannel.this.config();
            final ChannelPipeline pipeline = EpollSocketChannel.this.pipeline();
            final ByteBufAllocator allocator = config.getAllocator();
            RecvByteBufAllocator.Handle allocHandle = this.allocHandle;
            if (allocHandle == null) {
                allocHandle = (this.allocHandle = config.getRecvByteBufAllocator().newHandle());
            }
            ByteBuf byteBuf = null;
            boolean close = false;
            try {
                int totalReadAmount = 0;
                int writable;
                int localReadAmount;
                do {
                    byteBuf = allocHandle.allocate(allocator);
                    writable = byteBuf.writableBytes();
                    localReadAmount = this.doReadBytes(byteBuf);
                    if (localReadAmount <= 0) {
                        byteBuf.release();
                        close = (localReadAmount < 0);
                        break;
                    }
                    this.readPending = false;
                    pipeline.fireChannelRead(byteBuf);
                    byteBuf = null;
                    if (totalReadAmount >= Integer.MAX_VALUE - localReadAmount) {
                        allocHandle.record(totalReadAmount);
                        totalReadAmount = localReadAmount;
                    }
                    else {
                        totalReadAmount += localReadAmount;
                    }
                } while (localReadAmount >= writable);
                pipeline.fireChannelReadComplete();
                allocHandle.record(totalReadAmount);
                if (close) {
                    this.closeOnRead(pipeline);
                    close = false;
                }
            }
            catch (Throwable t) {
                final boolean closed = this.handleReadException(pipeline, byteBuf, t, close);
                if (!closed) {
                    EpollSocketChannel.this.eventLoop().execute(new Runnable() {
                        @Override
                        public void run() {
                            EpollSocketUnsafe.this.epollInReady();
                        }
                    });
                }
            }
            finally {
                if (!config.isAutoRead() && !this.readPending) {
                    this.clearEpollIn0();
                }
            }
        }
    }
}
