// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.nio;

import java.net.ConnectException;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import java.util.concurrent.TimeUnit;
import io.netty.channel.ConnectTimeoutException;
import io.netty.util.internal.OneTimeTask;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.netty.buffer.ByteBuf;
import java.nio.channels.CancelledKeyException;
import io.netty.channel.EventLoop;
import io.netty.channel.ChannelException;
import java.io.IOException;
import io.netty.channel.Channel;
import java.net.SocketAddress;
import java.util.concurrent.ScheduledFuture;
import io.netty.channel.ChannelPromise;
import java.nio.channels.SelectionKey;
import java.nio.channels.SelectableChannel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.AbstractChannel;

public abstract class AbstractNioChannel extends AbstractChannel
{
    private static final InternalLogger logger;
    private final SelectableChannel ch;
    protected final int readInterestOp;
    volatile SelectionKey selectionKey;
    private volatile boolean inputShutdown;
    private volatile boolean readPending;
    private ChannelPromise connectPromise;
    private ScheduledFuture<?> connectTimeoutFuture;
    private SocketAddress requestedRemoteAddress;
    
    protected AbstractNioChannel(final Channel parent, final SelectableChannel ch, final int readInterestOp) {
        super(parent);
        this.ch = ch;
        this.readInterestOp = readInterestOp;
        try {
            ch.configureBlocking(false);
        }
        catch (IOException e3) {
            try {
                ch.close();
            }
            catch (IOException e2) {
                if (AbstractNioChannel.logger.isWarnEnabled()) {
                    AbstractNioChannel.logger.warn("Failed to close a partially initialized socket.", e2);
                }
            }
            throw new ChannelException("Failed to enter non-blocking mode.", e3);
        }
    }
    
    @Override
    public boolean isOpen() {
        return this.ch.isOpen();
    }
    
    @Override
    public NioUnsafe unsafe() {
        return (NioUnsafe)super.unsafe();
    }
    
    protected SelectableChannel javaChannel() {
        return this.ch;
    }
    
    @Override
    public NioEventLoop eventLoop() {
        return (NioEventLoop)super.eventLoop();
    }
    
    protected SelectionKey selectionKey() {
        assert this.selectionKey != null;
        return this.selectionKey;
    }
    
    protected boolean isReadPending() {
        return this.readPending;
    }
    
    protected void setReadPending(final boolean readPending) {
        this.readPending = readPending;
    }
    
    protected boolean isInputShutdown() {
        return this.inputShutdown;
    }
    
    void setInputShutdown() {
        this.inputShutdown = true;
    }
    
    @Override
    protected boolean isCompatible(final EventLoop loop) {
        return loop instanceof NioEventLoop;
    }
    
    @Override
    protected void doRegister() throws Exception {
        boolean selected = false;
        while (true) {
            try {
                this.selectionKey = this.javaChannel().register(this.eventLoop().selector, 0, this);
            }
            catch (CancelledKeyException e) {
                if (!selected) {
                    this.eventLoop().selectNow();
                    selected = true;
                    continue;
                }
                throw e;
            }
            break;
        }
    }
    
    @Override
    protected void doDeregister() throws Exception {
        this.eventLoop().cancel(this.selectionKey());
    }
    
    @Override
    protected void doBeginRead() throws Exception {
        if (this.inputShutdown) {
            return;
        }
        final SelectionKey selectionKey = this.selectionKey;
        if (!selectionKey.isValid()) {
            return;
        }
        this.readPending = true;
        final int interestOps = selectionKey.interestOps();
        if ((interestOps & this.readInterestOp) == 0x0) {
            selectionKey.interestOps(interestOps | this.readInterestOp);
        }
    }
    
    protected abstract boolean doConnect(final SocketAddress p0, final SocketAddress p1) throws Exception;
    
    protected abstract void doFinishConnect() throws Exception;
    
    protected final ByteBuf newDirectBuffer(final ByteBuf buf) {
        final int readableBytes = buf.readableBytes();
        if (readableBytes == 0) {
            ReferenceCountUtil.safeRelease(buf);
            return Unpooled.EMPTY_BUFFER;
        }
        final ByteBufAllocator alloc = this.alloc();
        if (alloc.isDirectBufferPooled()) {
            final ByteBuf directBuf = alloc.directBuffer(readableBytes);
            directBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
            ReferenceCountUtil.safeRelease(buf);
            return directBuf;
        }
        final ByteBuf directBuf = ByteBufUtil.threadLocalDirectBuffer();
        if (directBuf != null) {
            directBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
            ReferenceCountUtil.safeRelease(buf);
            return directBuf;
        }
        return buf;
    }
    
    protected final ByteBuf newDirectBuffer(final ReferenceCounted holder, final ByteBuf buf) {
        final int readableBytes = buf.readableBytes();
        if (readableBytes == 0) {
            ReferenceCountUtil.safeRelease(holder);
            return Unpooled.EMPTY_BUFFER;
        }
        final ByteBufAllocator alloc = this.alloc();
        if (alloc.isDirectBufferPooled()) {
            final ByteBuf directBuf = alloc.directBuffer(readableBytes);
            directBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
            ReferenceCountUtil.safeRelease(holder);
            return directBuf;
        }
        final ByteBuf directBuf = ByteBufUtil.threadLocalDirectBuffer();
        if (directBuf != null) {
            directBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
            ReferenceCountUtil.safeRelease(holder);
            return directBuf;
        }
        if (holder != buf) {
            buf.retain();
            ReferenceCountUtil.safeRelease(holder);
        }
        return buf;
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(AbstractNioChannel.class);
    }
    
    protected abstract class AbstractNioUnsafe extends AbstractUnsafe implements NioUnsafe
    {
        protected final void removeReadOp() {
            final SelectionKey key = AbstractNioChannel.this.selectionKey();
            if (!key.isValid()) {
                return;
            }
            final int interestOps = key.interestOps();
            if ((interestOps & AbstractNioChannel.this.readInterestOp) != 0x0) {
                key.interestOps(interestOps & ~AbstractNioChannel.this.readInterestOp);
            }
        }
        
        @Override
        public final SelectableChannel ch() {
            return AbstractNioChannel.this.javaChannel();
        }
        
        @Override
        public final void connect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
            if (!promise.setUncancellable() || !this.ensureOpen(promise)) {
                return;
            }
            try {
                if (AbstractNioChannel.this.connectPromise != null) {
                    throw new IllegalStateException("connection attempt already made");
                }
                final boolean wasActive = AbstractNioChannel.this.isActive();
                if (AbstractNioChannel.this.doConnect(remoteAddress, localAddress)) {
                    this.fulfillConnectPromise(promise, wasActive);
                }
                else {
                    AbstractNioChannel.this.connectPromise = promise;
                    AbstractNioChannel.this.requestedRemoteAddress = remoteAddress;
                    final int connectTimeoutMillis = AbstractNioChannel.this.config().getConnectTimeoutMillis();
                    if (connectTimeoutMillis > 0) {
                        AbstractNioChannel.this.connectTimeoutFuture = AbstractNioChannel.this.eventLoop().schedule(new OneTimeTask() {
                            @Override
                            public void run() {
                                final ChannelPromise connectPromise = AbstractNioChannel.this.connectPromise;
                                final ConnectTimeoutException cause = new ConnectTimeoutException("connection timed out: " + remoteAddress);
                                if (connectPromise != null && connectPromise.tryFailure(cause)) {
                                    AbstractNioUnsafe.this.close(AbstractNioUnsafe.this.voidPromise());
                                }
                            }
                        }, connectTimeoutMillis, TimeUnit.MILLISECONDS);
                    }
                    promise.addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
                        @Override
                        public void operationComplete(final ChannelFuture future) throws Exception {
                            if (future.isCancelled()) {
                                if (AbstractNioChannel.this.connectTimeoutFuture != null) {
                                    AbstractNioChannel.this.connectTimeoutFuture.cancel(false);
                                }
                                AbstractNioChannel.this.connectPromise = null;
                                AbstractNioUnsafe.this.close(AbstractNioUnsafe.this.voidPromise());
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
                promise.tryFailure(t);
                this.closeIfClosed();
            }
        }
        
        private void fulfillConnectPromise(final ChannelPromise promise, final boolean wasActive) {
            if (promise == null) {
                return;
            }
            final boolean promiseSet = promise.trySuccess();
            if (!wasActive && AbstractNioChannel.this.isActive()) {
                AbstractNioChannel.this.pipeline().fireChannelActive();
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
        
        @Override
        public final void finishConnect() {
            assert AbstractNioChannel.this.eventLoop().inEventLoop();
            try {
                final boolean wasActive = AbstractNioChannel.this.isActive();
                AbstractNioChannel.this.doFinishConnect();
                this.fulfillConnectPromise(AbstractNioChannel.this.connectPromise, wasActive);
            }
            catch (Throwable t) {
                if (t instanceof ConnectException) {
                    final Throwable newT = new ConnectException(t.getMessage() + ": " + AbstractNioChannel.this.requestedRemoteAddress);
                    newT.setStackTrace(t.getStackTrace());
                    t = newT;
                }
                this.fulfillConnectPromise(AbstractNioChannel.this.connectPromise, t);
            }
            finally {
                if (AbstractNioChannel.this.connectTimeoutFuture != null) {
                    AbstractNioChannel.this.connectTimeoutFuture.cancel(false);
                }
                AbstractNioChannel.this.connectPromise = null;
            }
        }
        
        @Override
        protected final void flush0() {
            if (this.isFlushPending()) {
                return;
            }
            super.flush0();
        }
        
        @Override
        public final void forceFlush() {
            super.flush0();
        }
        
        private boolean isFlushPending() {
            final SelectionKey selectionKey = AbstractNioChannel.this.selectionKey();
            return selectionKey.isValid() && (selectionKey.interestOps() & 0x4) != 0x0;
        }
    }
    
    public interface NioUnsafe extends Channel.Unsafe
    {
        SelectableChannel ch();
        
        void finishConnect();
        
        void read();
        
        void forceFlush();
    }
}
