package io.netty.channel;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;

public abstract interface ChannelPromise
        extends ChannelFuture, Promise<Void> {
    public abstract Channel channel();

    public abstract ChannelPromise setSuccess(Void paramVoid);

    public abstract ChannelPromise setSuccess();

    public abstract boolean trySuccess();

    public abstract ChannelPromise setFailure(Throwable paramThrowable);

    public abstract ChannelPromise addListener(GenericFutureListener<? extends Future<? super Void>> paramGenericFutureListener);

    public abstract ChannelPromise addListeners(GenericFutureListener<? extends Future<? super Void>>... paramVarArgs);

    public abstract ChannelPromise removeListener(GenericFutureListener<? extends Future<? super Void>> paramGenericFutureListener);

    public abstract ChannelPromise removeListeners(GenericFutureListener<? extends Future<? super Void>>... paramVarArgs);

    public abstract ChannelPromise sync()
            throws InterruptedException;

    public abstract ChannelPromise syncUninterruptibly();

    public abstract ChannelPromise await()
            throws InterruptedException;

    public abstract ChannelPromise awaitUninterruptibly();
}




