package io.netty.channel;

import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class DefaultChannelPromise
        extends DefaultPromise<Void>
        implements ChannelPromise, ChannelFlushPromiseNotifier.FlushCheckpoint {
    private final Channel channel;
    private long checkpoint;

    public DefaultChannelPromise(Channel paramChannel) {
        this.channel = paramChannel;
    }

    public DefaultChannelPromise(Channel paramChannel, EventExecutor paramEventExecutor) {
        super(paramEventExecutor);
        this.channel = paramChannel;
    }

    protected EventExecutor executor() {
        EventExecutor localEventExecutor = super.executor();
        if (localEventExecutor == null) {
            return channel().eventLoop();
        }
        return localEventExecutor;
    }

    public Channel channel() {
        return this.channel;
    }

    public ChannelPromise setSuccess() {
        return setSuccess(null);
    }

    public ChannelPromise setSuccess(Void paramVoid) {
        super.setSuccess(paramVoid);
        return this;
    }

    public boolean trySuccess() {
        return trySuccess(null);
    }

    public ChannelPromise setFailure(Throwable paramThrowable) {
        super.setFailure(paramThrowable);
        return this;
    }

    public ChannelPromise addListener(GenericFutureListener<? extends Future<? super Void>> paramGenericFutureListener) {
        super.addListener(paramGenericFutureListener);
        return this;
    }

    public ChannelPromise addListeners(GenericFutureListener<? extends Future<? super Void>>... paramVarArgs) {
        super.addListeners(paramVarArgs);
        return this;
    }

    public ChannelPromise removeListener(GenericFutureListener<? extends Future<? super Void>> paramGenericFutureListener) {
        super.removeListener(paramGenericFutureListener);
        return this;
    }

    public ChannelPromise removeListeners(GenericFutureListener<? extends Future<? super Void>>... paramVarArgs) {
        super.removeListeners(paramVarArgs);
        return this;
    }

    public ChannelPromise sync()
            throws InterruptedException {
        super.sync();
        return this;
    }

    public ChannelPromise syncUninterruptibly() {
        super.syncUninterruptibly();
        return this;
    }

    public ChannelPromise await()
            throws InterruptedException {
        super.await();
        return this;
    }

    public ChannelPromise awaitUninterruptibly() {
        super.awaitUninterruptibly();
        return this;
    }

    public long flushCheckpoint() {
        return this.checkpoint;
    }

    public void flushCheckpoint(long paramLong) {
        this.checkpoint = paramLong;
    }

    public ChannelPromise promise() {
        return this;
    }

    protected void checkDeadLock() {
        if (channel().isRegistered()) {
            super.checkDeadLock();
        }
    }
}




