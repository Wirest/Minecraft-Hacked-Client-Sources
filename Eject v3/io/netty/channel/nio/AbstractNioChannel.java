package io.netty.channel.nio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.AbstractChannel.AbstractUnsafe;
import io.netty.channel.Channel.Unsafe;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.OneTimeTask;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketAddress;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class AbstractNioChannel
        extends AbstractChannel {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractNioChannel.class);
    protected final int readInterestOp;
    private final SelectableChannel ch;
    volatile SelectionKey selectionKey;
    private volatile boolean inputShutdown;
    private volatile boolean readPending;
    private ChannelPromise connectPromise;
    private ScheduledFuture<?> connectTimeoutFuture;
    private SocketAddress requestedRemoteAddress;

    protected AbstractNioChannel(Channel paramChannel, SelectableChannel paramSelectableChannel, int paramInt) {
        super(paramChannel);
        this.ch = paramSelectableChannel;
        this.readInterestOp = paramInt;
        try {
            paramSelectableChannel.configureBlocking(false);
        } catch (IOException localIOException1) {
            try {
                paramSelectableChannel.close();
            } catch (IOException localIOException2) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Failed to close a partially initialized socket.", localIOException2);
                }
            }
            throw new ChannelException("Failed to enter non-blocking mode.", localIOException1);
        }
    }

    public boolean isOpen() {
        return this.ch.isOpen();
    }

    public NioUnsafe unsafe() {
        return (NioUnsafe) super.unsafe();
    }

    protected SelectableChannel javaChannel() {
        return this.ch;
    }

    public NioEventLoop eventLoop() {
        return (NioEventLoop) super.eventLoop();
    }

    protected SelectionKey selectionKey() {
        assert (this.selectionKey != null);
        return this.selectionKey;
    }

    protected boolean isReadPending() {
        return this.readPending;
    }

    protected void setReadPending(boolean paramBoolean) {
        this.readPending = paramBoolean;
    }

    protected boolean isInputShutdown() {
        return this.inputShutdown;
    }

    void setInputShutdown() {
        this.inputShutdown = true;
    }

    protected boolean isCompatible(EventLoop paramEventLoop) {
        return paramEventLoop instanceof NioEventLoop;
    }

    protected void doRegister()
            throws Exception {
        int i = 0;
        for (; ; ) {
            try {
                this.selectionKey = javaChannel().register(eventLoop().selector, 0, this);
                return;
            } catch (CancelledKeyException localCancelledKeyException) {
                if (i == 0) {
                    eventLoop().selectNow();
                    i = 1;
                } else {
                    throw localCancelledKeyException;
                }
            }
        }
    }

    protected void doDeregister()
            throws Exception {
        eventLoop().cancel(selectionKey());
    }

    protected void doBeginRead()
            throws Exception {
        if (this.inputShutdown) {
            return;
        }
        SelectionKey localSelectionKey = this.selectionKey;
        if (!localSelectionKey.isValid()) {
            return;
        }
        this.readPending = true;
        int i = localSelectionKey.interestOps();
        if (i >> this.readInterestOp == 0) {
            localSelectionKey.interestOps(i ^ this.readInterestOp);
        }
    }

    protected abstract boolean doConnect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2)
            throws Exception;

    protected abstract void doFinishConnect()
            throws Exception;

    protected final ByteBuf newDirectBuffer(ByteBuf paramByteBuf) {
        int i = paramByteBuf.readableBytes();
        if (i == 0) {
            ReferenceCountUtil.safeRelease(paramByteBuf);
            return Unpooled.EMPTY_BUFFER;
        }
        ByteBufAllocator localByteBufAllocator = alloc();
        if (localByteBufAllocator.isDirectBufferPooled()) {
            localByteBuf = localByteBufAllocator.directBuffer(i);
            localByteBuf.writeBytes(paramByteBuf, paramByteBuf.readerIndex(), i);
            ReferenceCountUtil.safeRelease(paramByteBuf);
            return localByteBuf;
        }
        ByteBuf localByteBuf = ByteBufUtil.threadLocalDirectBuffer();
        if (localByteBuf != null) {
            localByteBuf.writeBytes(paramByteBuf, paramByteBuf.readerIndex(), i);
            ReferenceCountUtil.safeRelease(paramByteBuf);
            return localByteBuf;
        }
        return paramByteBuf;
    }

    protected final ByteBuf newDirectBuffer(ReferenceCounted paramReferenceCounted, ByteBuf paramByteBuf) {
        int i = paramByteBuf.readableBytes();
        if (i == 0) {
            ReferenceCountUtil.safeRelease(paramReferenceCounted);
            return Unpooled.EMPTY_BUFFER;
        }
        ByteBufAllocator localByteBufAllocator = alloc();
        if (localByteBufAllocator.isDirectBufferPooled()) {
            localByteBuf = localByteBufAllocator.directBuffer(i);
            localByteBuf.writeBytes(paramByteBuf, paramByteBuf.readerIndex(), i);
            ReferenceCountUtil.safeRelease(paramReferenceCounted);
            return localByteBuf;
        }
        ByteBuf localByteBuf = ByteBufUtil.threadLocalDirectBuffer();
        if (localByteBuf != null) {
            localByteBuf.writeBytes(paramByteBuf, paramByteBuf.readerIndex(), i);
            ReferenceCountUtil.safeRelease(paramReferenceCounted);
            return localByteBuf;
        }
        if (paramReferenceCounted != paramByteBuf) {
            paramByteBuf.retain();
            ReferenceCountUtil.safeRelease(paramReferenceCounted);
        }
        return paramByteBuf;
    }

    public static abstract interface NioUnsafe
            extends Channel.Unsafe {
        public abstract SelectableChannel ch();

        public abstract void finishConnect();

        public abstract void read();

        public abstract void forceFlush();
    }

    protected abstract class AbstractNioUnsafe
            extends AbstractChannel.AbstractUnsafe
            implements AbstractNioChannel.NioUnsafe {
        protected AbstractNioUnsafe() {
            super();
        }

        protected final void removeReadOp() {
            SelectionKey localSelectionKey = AbstractNioChannel.this.selectionKey();
            if (!localSelectionKey.isValid()) {
                return;
            }
            int i = localSelectionKey.interestOps();
            if (i >> AbstractNioChannel.this.readInterestOp != 0) {
                localSelectionKey.interestOps(i >> AbstractNioChannel.this.readInterestOp + -1);
            }
        }

        public final SelectableChannel ch() {
            return AbstractNioChannel.this.javaChannel();
        }

        public final void connect(final SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2, ChannelPromise paramChannelPromise) {
            if ((!paramChannelPromise.setUncancellable()) || (!ensureOpen(paramChannelPromise))) {
                return;
            }
            try {
                if (AbstractNioChannel.this.connectPromise != null) {
                    throw new IllegalStateException("connection attempt already made");
                }
                boolean bool = AbstractNioChannel.this.isActive();
                if (AbstractNioChannel.this.doConnect(paramSocketAddress1, paramSocketAddress2)) {
                    fulfillConnectPromise(paramChannelPromise, bool);
                } else {
                    AbstractNioChannel.this.connectPromise = paramChannelPromise;
                    AbstractNioChannel.this.requestedRemoteAddress = paramSocketAddress1;
                    int i = AbstractNioChannel.this.config().getConnectTimeoutMillis();
                    if (i > 0) {
                        AbstractNioChannel.this.connectTimeoutFuture = AbstractNioChannel.this.eventLoop().schedule(new OneTimeTask() {
                            public void run() {
                                ChannelPromise localChannelPromise = AbstractNioChannel.this.connectPromise;
                                ConnectTimeoutException localConnectTimeoutException = new ConnectTimeoutException("connection timed out: " + paramSocketAddress1);
                                if ((localChannelPromise != null) && (localChannelPromise.tryFailure(localConnectTimeoutException))) {
                                    AbstractNioChannel.AbstractNioUnsafe.this.close(AbstractNioChannel.AbstractNioUnsafe.this.voidPromise());
                                }
                            }
                        }, i, TimeUnit.MILLISECONDS);
                    }
                    paramChannelPromise.addListener(new ChannelFutureListener() {
                        public void operationComplete(ChannelFuture paramAnonymousChannelFuture)
                                throws Exception {
                            if (paramAnonymousChannelFuture.isCancelled()) {
                                if (AbstractNioChannel.this.connectTimeoutFuture != null) {
                                    AbstractNioChannel.this.connectTimeoutFuture.cancel(false);
                                }
                                AbstractNioChannel.this.connectPromise = null;
                                AbstractNioChannel.AbstractNioUnsafe.this.close(AbstractNioChannel.AbstractNioUnsafe.this.voidPromise());
                            }
                        }
                    });
                }
            } catch (Throwable localThrowable) {
                Object localObject;
                if ((localThrowable instanceof ConnectException)) {
                    ConnectException localConnectException = new ConnectException(localThrowable.getMessage() + ": " + paramSocketAddress1);
                    localConnectException.setStackTrace(localThrowable.getStackTrace());
                    localObject = localConnectException;
                }
                paramChannelPromise.tryFailure((Throwable) localObject);
                closeIfClosed();
            }
        }

        private void fulfillConnectPromise(ChannelPromise paramChannelPromise, boolean paramBoolean) {
            if (paramChannelPromise == null) {
                return;
            }
            boolean bool = paramChannelPromise.trySuccess();
            if ((!paramBoolean) && (AbstractNioChannel.this.isActive())) {
                AbstractNioChannel.this.pipeline().fireChannelActive();
            }
            if (!bool) {
                close(voidPromise());
            }
        }

        private void fulfillConnectPromise(ChannelPromise paramChannelPromise, Throwable paramThrowable) {
            if (paramChannelPromise == null) {
                return;
            }
            paramChannelPromise.tryFailure(paramThrowable);
            closeIfClosed();
        }

        public final void finishConnect() {
            assert (AbstractNioChannel.this.eventLoop().inEventLoop());
            try {
                boolean bool = AbstractNioChannel.this.isActive();
                AbstractNioChannel.this.doFinishConnect();
                fulfillConnectPromise(AbstractNioChannel.this.connectPromise, bool);
            } catch (Throwable localThrowable) {
                Object localObject1;
                if ((localThrowable instanceof ConnectException)) {
                    ConnectException localConnectException = new ConnectException(localThrowable.getMessage() + ": " + AbstractNioChannel.this.requestedRemoteAddress);
                    localConnectException.setStackTrace(localThrowable.getStackTrace());
                    localObject1 = localConnectException;
                }
                fulfillConnectPromise(AbstractNioChannel.this.connectPromise, (Throwable) localObject1);
            } finally {
                if (AbstractNioChannel.this.connectTimeoutFuture != null) {
                    AbstractNioChannel.this.connectTimeoutFuture.cancel(false);
                }
                AbstractNioChannel.this.connectPromise = null;
            }
        }

        protected final void flush0() {
            if (isFlushPending()) {
                return;
            }
            super.flush0();
        }

        public final void forceFlush() {
            super.flush0();
        }

        private boolean isFlushPending() {
            SelectionKey localSelectionKey = AbstractNioChannel.this.selectionKey();
            return (localSelectionKey.isValid()) && (localSelectionKey.interestOps() >> 4 != 0);
        }
    }
}




