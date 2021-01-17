// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.ssl;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFutureListener;
import java.util.concurrent.ScheduledFuture;
import io.netty.util.concurrent.GenericFutureListener;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.ArrayList;
import io.netty.buffer.ByteBufUtil;
import java.util.List;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import io.netty.util.internal.PlatformDependent;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.net.ssl.SSLEngineResult;
import io.netty.buffer.ByteBuf;
import java.net.SocketAddress;
import io.netty.channel.ChannelException;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import io.netty.util.concurrent.ImmediateExecutor;
import io.netty.channel.PendingWriteQueue;
import java.util.concurrent.Executor;
import javax.net.ssl.SSLEngine;
import io.netty.channel.ChannelHandlerContext;
import java.nio.channels.ClosedChannelException;
import javax.net.ssl.SSLException;
import java.util.regex.Pattern;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.handler.codec.ByteToMessageDecoder;

public class SslHandler extends ByteToMessageDecoder implements ChannelOutboundHandler
{
    private static final InternalLogger logger;
    private static final Pattern IGNORABLE_CLASS_IN_STACK;
    private static final Pattern IGNORABLE_ERROR_MESSAGE;
    private static final SSLException SSLENGINE_CLOSED;
    private static final SSLException HANDSHAKE_TIMED_OUT;
    private static final ClosedChannelException CHANNEL_CLOSED;
    private volatile ChannelHandlerContext ctx;
    private final SSLEngine engine;
    private final int maxPacketBufferSize;
    private final Executor delegatedTaskExecutor;
    private final boolean wantsDirectBuffer;
    private final boolean wantsLargeOutboundNetworkBuffer;
    private boolean wantsInboundHeapBuffer;
    private final boolean startTls;
    private boolean sentFirstMessage;
    private boolean flushedBeforeHandshakeDone;
    private PendingWriteQueue pendingUnencryptedWrites;
    private final LazyChannelPromise handshakePromise;
    private final LazyChannelPromise sslCloseFuture;
    private boolean needsFlush;
    private int packetLength;
    private volatile long handshakeTimeoutMillis;
    private volatile long closeNotifyTimeoutMillis;
    
    public SslHandler(final SSLEngine engine) {
        this(engine, false);
    }
    
    public SslHandler(final SSLEngine engine, final boolean startTls) {
        this(engine, startTls, ImmediateExecutor.INSTANCE);
    }
    
    @Deprecated
    public SslHandler(final SSLEngine engine, final Executor delegatedTaskExecutor) {
        this(engine, false, delegatedTaskExecutor);
    }
    
    @Deprecated
    public SslHandler(final SSLEngine engine, final boolean startTls, final Executor delegatedTaskExecutor) {
        this.handshakePromise = new LazyChannelPromise();
        this.sslCloseFuture = new LazyChannelPromise();
        this.handshakeTimeoutMillis = 10000L;
        this.closeNotifyTimeoutMillis = 3000L;
        if (engine == null) {
            throw new NullPointerException("engine");
        }
        if (delegatedTaskExecutor == null) {
            throw new NullPointerException("delegatedTaskExecutor");
        }
        this.engine = engine;
        this.delegatedTaskExecutor = delegatedTaskExecutor;
        this.startTls = startTls;
        this.maxPacketBufferSize = engine.getSession().getPacketBufferSize();
        this.wantsDirectBuffer = (engine instanceof OpenSslEngine);
        this.wantsLargeOutboundNetworkBuffer = !(engine instanceof OpenSslEngine);
    }
    
    public long getHandshakeTimeoutMillis() {
        return this.handshakeTimeoutMillis;
    }
    
    public void setHandshakeTimeout(final long handshakeTimeout, final TimeUnit unit) {
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        this.setHandshakeTimeoutMillis(unit.toMillis(handshakeTimeout));
    }
    
    public void setHandshakeTimeoutMillis(final long handshakeTimeoutMillis) {
        if (handshakeTimeoutMillis < 0L) {
            throw new IllegalArgumentException("handshakeTimeoutMillis: " + handshakeTimeoutMillis + " (expected: >= 0)");
        }
        this.handshakeTimeoutMillis = handshakeTimeoutMillis;
    }
    
    public long getCloseNotifyTimeoutMillis() {
        return this.closeNotifyTimeoutMillis;
    }
    
    public void setCloseNotifyTimeout(final long closeNotifyTimeout, final TimeUnit unit) {
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        this.setCloseNotifyTimeoutMillis(unit.toMillis(closeNotifyTimeout));
    }
    
    public void setCloseNotifyTimeoutMillis(final long closeNotifyTimeoutMillis) {
        if (closeNotifyTimeoutMillis < 0L) {
            throw new IllegalArgumentException("closeNotifyTimeoutMillis: " + closeNotifyTimeoutMillis + " (expected: >= 0)");
        }
        this.closeNotifyTimeoutMillis = closeNotifyTimeoutMillis;
    }
    
    public SSLEngine engine() {
        return this.engine;
    }
    
    public Future<Channel> handshakeFuture() {
        return this.handshakePromise;
    }
    
    public ChannelFuture close() {
        return this.close(this.ctx.newPromise());
    }
    
    public ChannelFuture close(final ChannelPromise future) {
        final ChannelHandlerContext ctx = this.ctx;
        ctx.executor().execute(new Runnable() {
            @Override
            public void run() {
                SslHandler.this.engine.closeOutbound();
                try {
                    SslHandler.this.write(ctx, Unpooled.EMPTY_BUFFER, future);
                    SslHandler.this.flush(ctx);
                }
                catch (Exception e) {
                    if (!future.tryFailure(e)) {
                        SslHandler.logger.warn("flush() raised a masked exception.", e);
                    }
                }
            }
        });
        return future;
    }
    
    public Future<Channel> sslCloseFuture() {
        return this.sslCloseFuture;
    }
    
    public void handlerRemoved0(final ChannelHandlerContext ctx) throws Exception {
        if (!this.pendingUnencryptedWrites.isEmpty()) {
            this.pendingUnencryptedWrites.removeAndFailAll(new ChannelException("Pending write on removal of SslHandler"));
        }
    }
    
    @Override
    public void bind(final ChannelHandlerContext ctx, final SocketAddress localAddress, final ChannelPromise promise) throws Exception {
        ctx.bind(localAddress, promise);
    }
    
    @Override
    public void connect(final ChannelHandlerContext ctx, final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) throws Exception {
        ctx.connect(remoteAddress, localAddress, promise);
    }
    
    @Override
    public void deregister(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        ctx.deregister(promise);
    }
    
    @Override
    public void disconnect(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        this.closeOutboundAndChannel(ctx, promise, true);
    }
    
    @Override
    public void close(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        this.closeOutboundAndChannel(ctx, promise, false);
    }
    
    @Override
    public void read(final ChannelHandlerContext ctx) {
        ctx.read();
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        this.pendingUnencryptedWrites.add(msg, promise);
    }
    
    @Override
    public void flush(final ChannelHandlerContext ctx) throws Exception {
        if (this.startTls && !this.sentFirstMessage) {
            this.sentFirstMessage = true;
            this.pendingUnencryptedWrites.removeAndWriteAll();
            ctx.flush();
            return;
        }
        if (this.pendingUnencryptedWrites.isEmpty()) {
            this.pendingUnencryptedWrites.add(Unpooled.EMPTY_BUFFER, ctx.voidPromise());
        }
        if (!this.handshakePromise.isDone()) {
            this.flushedBeforeHandshakeDone = true;
        }
        this.wrap(ctx, false);
        ctx.flush();
    }
    
    private void wrap(final ChannelHandlerContext ctx, final boolean inUnwrap) throws SSLException {
        ByteBuf out = null;
        ChannelPromise promise = null;
        try {
            while (true) {
                final Object msg = this.pendingUnencryptedWrites.current();
                if (msg == null) {
                    break;
                }
                if (!(msg instanceof ByteBuf)) {
                    this.pendingUnencryptedWrites.removeAndWrite();
                }
                else {
                    final ByteBuf buf = (ByteBuf)msg;
                    if (out == null) {
                        out = this.allocateOutNetBuf(ctx, buf.readableBytes());
                    }
                    final SSLEngineResult result = this.wrap(this.engine, buf, out);
                    if (!buf.isReadable()) {
                        promise = this.pendingUnencryptedWrites.remove();
                    }
                    else {
                        promise = null;
                    }
                    if (result.getStatus() == SSLEngineResult.Status.CLOSED) {
                        this.pendingUnencryptedWrites.removeAndFailAll(SslHandler.SSLENGINE_CLOSED);
                        return;
                    }
                    switch (result.getHandshakeStatus()) {
                        case NEED_TASK: {
                            this.runDelegatedTasks();
                            continue;
                        }
                        case FINISHED: {
                            this.setHandshakeSuccess();
                        }
                        case NOT_HANDSHAKING: {
                            this.setHandshakeSuccessIfStillHandshaking();
                        }
                        case NEED_WRAP: {
                            this.finishWrap(ctx, out, promise, inUnwrap);
                            promise = null;
                            out = null;
                            continue;
                        }
                        case NEED_UNWRAP: {}
                        default: {
                            throw new IllegalStateException("Unknown handshake status: " + result.getHandshakeStatus());
                        }
                    }
                }
            }
        }
        catch (SSLException e) {
            this.setHandshakeFailure(e);
            throw e;
        }
        finally {
            this.finishWrap(ctx, out, promise, inUnwrap);
        }
    }
    
    private void finishWrap(final ChannelHandlerContext ctx, ByteBuf out, final ChannelPromise promise, final boolean inUnwrap) {
        if (out == null) {
            out = Unpooled.EMPTY_BUFFER;
        }
        else if (!out.isReadable()) {
            out.release();
            out = Unpooled.EMPTY_BUFFER;
        }
        if (promise != null) {
            ctx.write(out, promise);
        }
        else {
            ctx.write(out);
        }
        if (inUnwrap) {
            this.needsFlush = true;
        }
    }
    
    private void wrapNonAppData(final ChannelHandlerContext ctx, final boolean inUnwrap) throws SSLException {
        ByteBuf out = null;
        try {
            SSLEngineResult result;
            do {
                if (out == null) {
                    out = this.allocateOutNetBuf(ctx, 0);
                }
                result = this.wrap(this.engine, Unpooled.EMPTY_BUFFER, out);
                if (result.bytesProduced() > 0) {
                    ctx.write(out);
                    if (inUnwrap) {
                        this.needsFlush = true;
                    }
                    out = null;
                }
                switch (result.getHandshakeStatus()) {
                    case FINISHED: {
                        this.setHandshakeSuccess();
                        break;
                    }
                    case NEED_TASK: {
                        this.runDelegatedTasks();
                        break;
                    }
                    case NEED_UNWRAP: {
                        if (!inUnwrap) {
                            this.unwrapNonAppData(ctx);
                            break;
                        }
                        break;
                    }
                    case NEED_WRAP: {
                        break;
                    }
                    case NOT_HANDSHAKING: {
                        this.setHandshakeSuccessIfStillHandshaking();
                        if (!inUnwrap) {
                            this.unwrapNonAppData(ctx);
                            break;
                        }
                        break;
                    }
                    default: {
                        throw new IllegalStateException("Unknown handshake status: " + result.getHandshakeStatus());
                    }
                }
            } while (result.bytesProduced() != 0);
        }
        catch (SSLException e) {
            this.setHandshakeFailure(e);
            throw e;
        }
        finally {
            if (out != null) {
                out.release();
            }
        }
    }
    
    private SSLEngineResult wrap(final SSLEngine engine, final ByteBuf in, final ByteBuf out) throws SSLException {
        ByteBuffer in2 = in.nioBuffer();
        if (!in2.isDirect()) {
            final ByteBuffer newIn0 = ByteBuffer.allocateDirect(in2.remaining());
            newIn0.put(in2).flip();
            in2 = newIn0;
        }
        while (true) {
            final ByteBuffer out2 = out.nioBuffer(out.writerIndex(), out.writableBytes());
            final SSLEngineResult result = engine.wrap(in2, out2);
            in.skipBytes(result.bytesConsumed());
            out.writerIndex(out.writerIndex() + result.bytesProduced());
            switch (result.getStatus()) {
                case BUFFER_OVERFLOW: {
                    out.ensureWritable(this.maxPacketBufferSize);
                    continue;
                }
                default: {
                    return result;
                }
            }
        }
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        this.setHandshakeFailure(SslHandler.CHANNEL_CLOSED);
        super.channelInactive(ctx);
    }
    
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        if (this.ignoreException(cause)) {
            if (SslHandler.logger.isDebugEnabled()) {
                SslHandler.logger.debug("Swallowing a harmless 'connection reset by peer / broken pipe' error that occurred while writing close_notify in response to the peer's close_notify", cause);
            }
            if (ctx.channel().isActive()) {
                ctx.close();
            }
        }
        else {
            ctx.fireExceptionCaught(cause);
        }
    }
    
    private boolean ignoreException(final Throwable t) {
        if (!(t instanceof SSLException) && t instanceof IOException && this.sslCloseFuture.isDone()) {
            final String message = String.valueOf(t.getMessage()).toLowerCase();
            if (SslHandler.IGNORABLE_ERROR_MESSAGE.matcher(message).matches()) {
                return true;
            }
            final StackTraceElement[] arr$;
            final StackTraceElement[] elements = arr$ = t.getStackTrace();
            for (final StackTraceElement element : arr$) {
                final String classname = element.getClassName();
                final String methodname = element.getMethodName();
                if (!classname.startsWith("io.netty.")) {
                    if ("read".equals(methodname)) {
                        if (SslHandler.IGNORABLE_CLASS_IN_STACK.matcher(classname).matches()) {
                            return true;
                        }
                        try {
                            final Class<?> clazz = PlatformDependent.getClassLoader(this.getClass()).loadClass(classname);
                            if (SocketChannel.class.isAssignableFrom(clazz) || DatagramChannel.class.isAssignableFrom(clazz)) {
                                return true;
                            }
                            if (PlatformDependent.javaVersion() >= 7 && "com.sun.nio.sctp.SctpChannel".equals(clazz.getSuperclass().getName())) {
                                return true;
                            }
                        }
                        catch (ClassNotFoundException ex) {}
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean isEncrypted(final ByteBuf buffer) {
        if (buffer.readableBytes() < 5) {
            throw new IllegalArgumentException("buffer must have at least 5 readable bytes");
        }
        return getEncryptedPacketLength(buffer, buffer.readerIndex()) != -1;
    }
    
    private static int getEncryptedPacketLength(final ByteBuf buffer, final int offset) {
        int packetLength = 0;
        boolean tls = false;
        switch (buffer.getUnsignedByte(offset)) {
            case 20:
            case 21:
            case 22:
            case 23: {
                tls = true;
                break;
            }
            default: {
                tls = false;
                break;
            }
        }
        if (tls) {
            final int majorVersion = buffer.getUnsignedByte(offset + 1);
            if (majorVersion == 3) {
                packetLength = buffer.getUnsignedShort(offset + 3) + 5;
                if (packetLength <= 5) {
                    tls = false;
                }
            }
            else {
                tls = false;
            }
        }
        if (!tls) {
            boolean sslv2 = true;
            final int headerLength = ((buffer.getUnsignedByte(offset) & 0x80) != 0x0) ? 2 : 3;
            final int majorVersion2 = buffer.getUnsignedByte(offset + headerLength + 1);
            if (majorVersion2 == 2 || majorVersion2 == 3) {
                if (headerLength == 2) {
                    packetLength = (buffer.getShort(offset) & 0x7FFF) + 2;
                }
                else {
                    packetLength = (buffer.getShort(offset) & 0x3FFF) + 3;
                }
                if (packetLength <= headerLength) {
                    sslv2 = false;
                }
            }
            else {
                sslv2 = false;
            }
            if (!sslv2) {
                return -1;
            }
        }
        return packetLength;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws SSLException {
        final int startOffset = in.readerIndex();
        final int endOffset = in.writerIndex();
        int offset = startOffset;
        int totalLength = 0;
        if (this.packetLength > 0) {
            if (endOffset - startOffset < this.packetLength) {
                return;
            }
            offset += this.packetLength;
            totalLength = this.packetLength;
            this.packetLength = 0;
        }
        boolean nonSslRecord = false;
        while (totalLength < 18713) {
            final int readableBytes = endOffset - offset;
            if (readableBytes < 5) {
                break;
            }
            final int packetLength = getEncryptedPacketLength(in, offset);
            if (packetLength == -1) {
                nonSslRecord = true;
                break;
            }
            assert packetLength > 0;
            if (packetLength > readableBytes) {
                this.packetLength = packetLength;
                break;
            }
            final int newTotalLength = totalLength + packetLength;
            if (newTotalLength > 18713) {
                break;
            }
            offset += packetLength;
            totalLength = newTotalLength;
        }
        if (totalLength > 0) {
            in.skipBytes(totalLength);
            final ByteBuffer inNetBuf = in.nioBuffer(startOffset, totalLength);
            this.unwrap(ctx, inNetBuf, totalLength);
            assert !(!this.engine.isInboundDone());
        }
        if (nonSslRecord) {
            final NotSslRecordException e = new NotSslRecordException("not an SSL/TLS record: " + ByteBufUtil.hexDump(in));
            in.skipBytes(in.readableBytes());
            ctx.fireExceptionCaught(e);
            this.setHandshakeFailure(e);
        }
    }
    
    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
        if (this.needsFlush) {
            this.needsFlush = false;
            ctx.flush();
        }
        super.channelReadComplete(ctx);
    }
    
    private void unwrapNonAppData(final ChannelHandlerContext ctx) throws SSLException {
        this.unwrap(ctx, Unpooled.EMPTY_BUFFER.nioBuffer(), 0);
    }
    
    private void unwrap(final ChannelHandlerContext ctx, ByteBuffer packet, final int initialOutAppBufCapacity) throws SSLException {
        final int oldPos = packet.position();
        ByteBuf newPacket;
        ByteBuffer oldPacket;
        if (this.wantsInboundHeapBuffer && packet.isDirect()) {
            newPacket = ctx.alloc().heapBuffer(packet.limit() - oldPos);
            newPacket.writeBytes(packet);
            oldPacket = packet;
            packet = newPacket.nioBuffer();
        }
        else {
            oldPacket = null;
            newPacket = null;
        }
        boolean wrapLater = false;
        final ByteBuf decodeOut = this.allocate(ctx, initialOutAppBufCapacity);
        try {
            while (true) {
                final SSLEngineResult result = unwrap(this.engine, packet, decodeOut);
                final SSLEngineResult.Status status = result.getStatus();
                final SSLEngineResult.HandshakeStatus handshakeStatus = result.getHandshakeStatus();
                final int produced = result.bytesProduced();
                final int consumed = result.bytesConsumed();
                if (status == SSLEngineResult.Status.CLOSED) {
                    this.sslCloseFuture.trySuccess(ctx.channel());
                    break;
                }
                switch (handshakeStatus) {
                    case NEED_UNWRAP: {
                        break;
                    }
                    case NEED_WRAP: {
                        this.wrapNonAppData(ctx, true);
                        break;
                    }
                    case NEED_TASK: {
                        this.runDelegatedTasks();
                        break;
                    }
                    case FINISHED: {
                        this.setHandshakeSuccess();
                        wrapLater = true;
                        continue;
                    }
                    case NOT_HANDSHAKING: {
                        if (this.setHandshakeSuccessIfStillHandshaking()) {
                            wrapLater = true;
                            continue;
                        }
                        if (this.flushedBeforeHandshakeDone) {
                            this.flushedBeforeHandshakeDone = false;
                            wrapLater = true;
                            break;
                        }
                        break;
                    }
                    default: {
                        throw new IllegalStateException("Unknown handshake status: " + handshakeStatus);
                    }
                }
                if (status == SSLEngineResult.Status.BUFFER_UNDERFLOW) {
                    break;
                }
                if (consumed == 0 && produced == 0) {
                    break;
                }
            }
            if (wrapLater) {
                this.wrap(ctx, true);
            }
        }
        catch (SSLException e) {
            this.setHandshakeFailure(e);
            throw e;
        }
        finally {
            if (newPacket != null) {
                oldPacket.position(oldPos + packet.position());
                newPacket.release();
            }
            if (decodeOut.isReadable()) {
                ctx.fireChannelRead(decodeOut);
            }
            else {
                decodeOut.release();
            }
        }
    }
    
    private static SSLEngineResult unwrap(final SSLEngine engine, final ByteBuffer in, final ByteBuf out) throws SSLException {
        int overflows = 0;
        while (true) {
            final ByteBuffer out2 = out.nioBuffer(out.writerIndex(), out.writableBytes());
            final SSLEngineResult result = engine.unwrap(in, out2);
            out.writerIndex(out.writerIndex() + result.bytesProduced());
            switch (result.getStatus()) {
                case BUFFER_OVERFLOW: {
                    final int max = engine.getSession().getApplicationBufferSize();
                    switch (overflows++) {
                        case 0: {
                            out.ensureWritable(Math.min(max, in.remaining()));
                            continue;
                        }
                        default: {
                            out.ensureWritable(max);
                            continue;
                        }
                    }
                    continue;
                }
                default: {
                    return result;
                }
            }
        }
    }
    
    private void runDelegatedTasks() {
        if (this.delegatedTaskExecutor == ImmediateExecutor.INSTANCE) {
            while (true) {
                final Runnable task = this.engine.getDelegatedTask();
                if (task == null) {
                    break;
                }
                task.run();
            }
        }
        else {
            final List<Runnable> tasks = new ArrayList<Runnable>(2);
            while (true) {
                final Runnable task2 = this.engine.getDelegatedTask();
                if (task2 == null) {
                    break;
                }
                tasks.add(task2);
            }
            if (tasks.isEmpty()) {
                return;
            }
            final CountDownLatch latch = new CountDownLatch(1);
            this.delegatedTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (final Runnable task : tasks) {
                            task.run();
                        }
                    }
                    catch (Exception e) {
                        SslHandler.this.ctx.fireExceptionCaught(e);
                    }
                    finally {
                        latch.countDown();
                    }
                }
            });
            boolean interrupted = false;
            while (latch.getCount() != 0L) {
                try {
                    latch.await();
                }
                catch (InterruptedException e) {
                    interrupted = true;
                }
            }
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    private boolean setHandshakeSuccessIfStillHandshaking() {
        if (!this.handshakePromise.isDone()) {
            this.setHandshakeSuccess();
            return true;
        }
        return false;
    }
    
    private void setHandshakeSuccess() {
        final String cipherSuite = String.valueOf(this.engine.getSession().getCipherSuite());
        if (!this.wantsDirectBuffer && (cipherSuite.contains("_GCM_") || cipherSuite.contains("-GCM-"))) {
            this.wantsInboundHeapBuffer = true;
        }
        if (this.handshakePromise.trySuccess(this.ctx.channel())) {
            if (SslHandler.logger.isDebugEnabled()) {
                SslHandler.logger.debug(this.ctx.channel() + " HANDSHAKEN: " + this.engine.getSession().getCipherSuite());
            }
            this.ctx.fireUserEventTriggered(SslHandshakeCompletionEvent.SUCCESS);
        }
    }
    
    private void setHandshakeFailure(final Throwable cause) {
        this.engine.closeOutbound();
        try {
            this.engine.closeInbound();
        }
        catch (SSLException e) {
            final String msg = e.getMessage();
            if (msg == null || !msg.contains("possible truncation attack")) {
                SslHandler.logger.debug("SSLEngine.closeInbound() raised an exception.", e);
            }
        }
        this.notifyHandshakeFailure(cause);
        this.pendingUnencryptedWrites.removeAndFailAll(cause);
    }
    
    private void notifyHandshakeFailure(final Throwable cause) {
        if (this.handshakePromise.tryFailure(cause)) {
            this.ctx.fireUserEventTriggered(new SslHandshakeCompletionEvent(cause));
            this.ctx.close();
        }
    }
    
    private void closeOutboundAndChannel(final ChannelHandlerContext ctx, final ChannelPromise promise, final boolean disconnect) throws Exception {
        if (!ctx.channel().isActive()) {
            if (disconnect) {
                ctx.disconnect(promise);
            }
            else {
                ctx.close(promise);
            }
            return;
        }
        this.engine.closeOutbound();
        final ChannelPromise closeNotifyFuture = ctx.newPromise();
        this.write(ctx, Unpooled.EMPTY_BUFFER, closeNotifyFuture);
        this.flush(ctx);
        this.safeClose(ctx, closeNotifyFuture, promise);
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        this.pendingUnencryptedWrites = new PendingWriteQueue(ctx);
        if (ctx.channel().isActive() && this.engine.getUseClientMode()) {
            this.handshake();
        }
    }
    
    private Future<Channel> handshake() {
        ScheduledFuture<?> timeoutFuture;
        if (this.handshakeTimeoutMillis > 0L) {
            timeoutFuture = this.ctx.executor().schedule((Runnable)new Runnable() {
                @Override
                public void run() {
                    if (SslHandler.this.handshakePromise.isDone()) {
                        return;
                    }
                    SslHandler.this.notifyHandshakeFailure(SslHandler.HANDSHAKE_TIMED_OUT);
                }
            }, this.handshakeTimeoutMillis, TimeUnit.MILLISECONDS);
        }
        else {
            timeoutFuture = null;
        }
        this.handshakePromise.addListener(new GenericFutureListener<Future<Channel>>() {
            @Override
            public void operationComplete(final Future<Channel> f) throws Exception {
                if (timeoutFuture != null) {
                    timeoutFuture.cancel(false);
                }
            }
        });
        try {
            this.engine.beginHandshake();
            this.wrapNonAppData(this.ctx, false);
            this.ctx.flush();
        }
        catch (Exception e) {
            this.notifyHandshakeFailure(e);
        }
        return this.handshakePromise;
    }
    
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        if (!this.startTls && this.engine.getUseClientMode()) {
            this.handshake().addListener(new GenericFutureListener<Future<Channel>>() {
                @Override
                public void operationComplete(final Future<Channel> future) throws Exception {
                    if (!future.isSuccess()) {
                        SslHandler.logger.debug("Failed to complete handshake", future.cause());
                        ctx.close();
                    }
                }
            });
        }
        ctx.fireChannelActive();
    }
    
    private void safeClose(final ChannelHandlerContext ctx, final ChannelFuture flushFuture, final ChannelPromise promise) {
        if (!ctx.channel().isActive()) {
            ctx.close(promise);
            return;
        }
        ScheduledFuture<?> timeoutFuture;
        if (this.closeNotifyTimeoutMillis > 0L) {
            timeoutFuture = ctx.executor().schedule((Runnable)new Runnable() {
                @Override
                public void run() {
                    SslHandler.logger.warn(ctx.channel() + " last write attempt timed out." + " Force-closing the connection.");
                    ctx.close(promise);
                }
            }, this.closeNotifyTimeoutMillis, TimeUnit.MILLISECONDS);
        }
        else {
            timeoutFuture = null;
        }
        flushFuture.addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture f) throws Exception {
                if (timeoutFuture != null) {
                    timeoutFuture.cancel(false);
                }
                ctx.close(promise);
            }
        });
    }
    
    private ByteBuf allocate(final ChannelHandlerContext ctx, final int capacity) {
        final ByteBufAllocator alloc = ctx.alloc();
        if (this.wantsDirectBuffer) {
            return alloc.directBuffer(capacity);
        }
        return alloc.buffer(capacity);
    }
    
    private ByteBuf allocateOutNetBuf(final ChannelHandlerContext ctx, final int pendingBytes) {
        if (this.wantsLargeOutboundNetworkBuffer) {
            return this.allocate(ctx, this.maxPacketBufferSize);
        }
        return this.allocate(ctx, Math.min(pendingBytes + 2329, this.maxPacketBufferSize));
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(SslHandler.class);
        IGNORABLE_CLASS_IN_STACK = Pattern.compile("^.*(?:Socket|Datagram|Sctp|Udt)Channel.*$");
        IGNORABLE_ERROR_MESSAGE = Pattern.compile("^.*(?:connection.*(?:reset|closed|abort|broken)|broken.*pipe).*$", 2);
        SSLENGINE_CLOSED = new SSLException("SSLEngine closed already");
        HANDSHAKE_TIMED_OUT = new SSLException("handshake timed out");
        CHANNEL_CLOSED = new ClosedChannelException();
        SslHandler.SSLENGINE_CLOSED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
        SslHandler.HANDSHAKE_TIMED_OUT.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
        SslHandler.CHANNEL_CLOSED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
    }
    
    private final class LazyChannelPromise extends DefaultPromise<Channel>
    {
        @Override
        protected EventExecutor executor() {
            if (SslHandler.this.ctx == null) {
                throw new IllegalStateException();
            }
            return SslHandler.this.ctx.executor();
        }
    }
}
