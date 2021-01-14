package io.netty.util.concurrent;

public abstract interface Promise<V>
        extends Future<V> {
    public abstract Promise<V> setSuccess(V paramV);

    public abstract boolean trySuccess(V paramV);

    public abstract Promise<V> setFailure(Throwable paramThrowable);

    public abstract boolean tryFailure(Throwable paramThrowable);

    public abstract boolean setUncancellable();

    public abstract Promise<V> addListener(GenericFutureListener<? extends Future<? super V>> paramGenericFutureListener);

    public abstract Promise<V> addListeners(GenericFutureListener<? extends Future<? super V>>... paramVarArgs);

    public abstract Promise<V> removeListener(GenericFutureListener<? extends Future<? super V>> paramGenericFutureListener);

    public abstract Promise<V> removeListeners(GenericFutureListener<? extends Future<? super V>>... paramVarArgs);

    public abstract Promise<V> await()
            throws InterruptedException;

    public abstract Promise<V> awaitUninterruptibly();

    public abstract Promise<V> sync()
            throws InterruptedException;

    public abstract Promise<V> syncUninterruptibly();
}




