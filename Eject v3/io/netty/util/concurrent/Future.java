package io.netty.util.concurrent;

import java.util.concurrent.TimeUnit;

public abstract interface Future<V>
        extends java.util.concurrent.Future<V> {
    public abstract boolean isSuccess();

    public abstract boolean isCancellable();

    public abstract Throwable cause();

    public abstract Future<V> addListener(GenericFutureListener<? extends Future<? super V>> paramGenericFutureListener);

    public abstract Future<V> addListeners(GenericFutureListener<? extends Future<? super V>>... paramVarArgs);

    public abstract Future<V> removeListener(GenericFutureListener<? extends Future<? super V>> paramGenericFutureListener);

    public abstract Future<V> removeListeners(GenericFutureListener<? extends Future<? super V>>... paramVarArgs);

    public abstract Future<V> sync()
            throws InterruptedException;

    public abstract Future<V> syncUninterruptibly();

    public abstract Future<V> await()
            throws InterruptedException;

    public abstract Future<V> awaitUninterruptibly();

    public abstract boolean await(long paramLong, TimeUnit paramTimeUnit)
            throws InterruptedException;

    public abstract boolean await(long paramLong)
            throws InterruptedException;

    public abstract boolean awaitUninterruptibly(long paramLong, TimeUnit paramTimeUnit);

    public abstract boolean awaitUninterruptibly(long paramLong);

    public abstract V getNow();

    public abstract boolean cancel(boolean paramBoolean);
}




