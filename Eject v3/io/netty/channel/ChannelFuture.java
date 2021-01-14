package io.netty.channel;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public abstract interface ChannelFuture
        extends Future<Void> {
    public abstract Channel channel();

    public abstract ChannelFuture addListener(GenericFutureListener<? extends Future<? super Void>> paramGenericFutureListener);

    public abstract ChannelFuture addListeners(GenericFutureListener<? extends Future<? super Void>>... paramVarArgs);

    public abstract ChannelFuture removeListener(GenericFutureListener<? extends Future<? super Void>> paramGenericFutureListener);

    public abstract ChannelFuture removeListeners(GenericFutureListener<? extends Future<? super Void>>... paramVarArgs);

    public abstract ChannelFuture sync()
            throws InterruptedException;

    public abstract ChannelFuture syncUninterruptibly();

    public abstract ChannelFuture await()
            throws InterruptedException;

    public abstract ChannelFuture awaitUninterruptibly();
}




