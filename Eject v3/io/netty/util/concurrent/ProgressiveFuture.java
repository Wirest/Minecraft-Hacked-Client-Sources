package io.netty.util.concurrent;

public abstract interface ProgressiveFuture<V>
        extends Future<V> {
    public abstract ProgressiveFuture<V> addListener(GenericFutureListener<? extends Future<? super V>> paramGenericFutureListener);

    public abstract ProgressiveFuture<V> addListeners(GenericFutureListener<? extends Future<? super V>>... paramVarArgs);

    public abstract ProgressiveFuture<V> removeListener(GenericFutureListener<? extends Future<? super V>> paramGenericFutureListener);

    public abstract ProgressiveFuture<V> removeListeners(GenericFutureListener<? extends Future<? super V>>... paramVarArgs);

    public abstract ProgressiveFuture<V> sync()
            throws InterruptedException;

    public abstract ProgressiveFuture<V> syncUninterruptibly();

    public abstract ProgressiveFuture<V> await()
            throws InterruptedException;

    public abstract ProgressiveFuture<V> awaitUninterruptibly();
}




