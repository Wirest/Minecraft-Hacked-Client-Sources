package io.netty.channel;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ProgressivePromise;

public abstract interface ChannelProgressivePromise
        extends ProgressivePromise<Void>, ChannelProgressiveFuture, ChannelPromise {
    public abstract ChannelProgressivePromise addListener(GenericFutureListener<? extends Future<? super Void>> paramGenericFutureListener);

    public abstract ChannelProgressivePromise addListeners(GenericFutureListener<? extends Future<? super Void>>... paramVarArgs);

    public abstract ChannelProgressivePromise removeListener(GenericFutureListener<? extends Future<? super Void>> paramGenericFutureListener);

    public abstract ChannelProgressivePromise removeListeners(GenericFutureListener<? extends Future<? super Void>>... paramVarArgs);

    public abstract ChannelProgressivePromise sync()
            throws InterruptedException;

    public abstract ChannelProgressivePromise syncUninterruptibly();

    public abstract ChannelProgressivePromise await()
            throws InterruptedException;

    public abstract ChannelProgressivePromise awaitUninterruptibly();

    public abstract ChannelProgressivePromise setSuccess(Void paramVoid);

    public abstract ChannelProgressivePromise setSuccess();

    public abstract ChannelProgressivePromise setFailure(Throwable paramThrowable);

    public abstract ChannelProgressivePromise setProgress(long paramLong1, long paramLong2);
}




