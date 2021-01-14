package io.netty.channel;

import io.netty.util.concurrent.CompleteFuture;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

abstract class CompleteChannelFuture
        extends CompleteFuture<Void>
        implements ChannelFuture {
    private final Channel channel;

    protected CompleteChannelFuture(Channel paramChannel, EventExecutor paramEventExecutor) {
        super(paramEventExecutor);
        if (paramChannel == null) {
            throw new NullPointerException("channel");
        }
        this.channel = paramChannel;
    }

    protected EventExecutor executor() {
        EventExecutor localEventExecutor = super.executor();
        if (localEventExecutor == null) {
            return channel().eventLoop();
        }
        return localEventExecutor;
    }

    public ChannelFuture addListener(GenericFutureListener<? extends Future<? super Void>> paramGenericFutureListener) {
        super.addListener(paramGenericFutureListener);
        return this;
    }

    public ChannelFuture addListeners(GenericFutureListener<? extends Future<? super Void>>... paramVarArgs) {
        super.addListeners(paramVarArgs);
        return this;
    }

    public ChannelFuture removeListener(GenericFutureListener<? extends Future<? super Void>> paramGenericFutureListener) {
        super.removeListener(paramGenericFutureListener);
        return this;
    }

    public ChannelFuture removeListeners(GenericFutureListener<? extends Future<? super Void>>... paramVarArgs) {
        super.removeListeners(paramVarArgs);
        return this;
    }

    public ChannelFuture syncUninterruptibly() {
        return this;
    }

    public ChannelFuture sync()
            throws InterruptedException {
        return this;
    }

    public ChannelFuture await()
            throws InterruptedException {
        return this;
    }

    public ChannelFuture awaitUninterruptibly() {
        return this;
    }

    public Channel channel() {
        return this.channel;
    }

    public Void getNow() {
        return null;
    }
}




