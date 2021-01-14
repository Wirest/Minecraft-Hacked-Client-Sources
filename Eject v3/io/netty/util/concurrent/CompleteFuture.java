package io.netty.util.concurrent;

import java.util.concurrent.TimeUnit;

public abstract class CompleteFuture<V>
        extends AbstractFuture<V> {
    private final EventExecutor executor;

    protected CompleteFuture(EventExecutor paramEventExecutor) {
        this.executor = paramEventExecutor;
    }

    protected EventExecutor executor() {
        return this.executor;
    }

    public Future<V> addListener(GenericFutureListener<? extends Future<? super V>> paramGenericFutureListener) {
        if (paramGenericFutureListener == null) {
            throw new NullPointerException("listener");
        }
        DefaultPromise.notifyListener(executor(), this, paramGenericFutureListener);
        return this;
    }

    public Future<V> addListeners(GenericFutureListener<? extends Future<? super V>>... paramVarArgs) {
        if (paramVarArgs == null) {
            throw new NullPointerException("listeners");
        }
        for (GenericFutureListener<? extends Future<? super V>> localGenericFutureListener : paramVarArgs) {
            if (localGenericFutureListener == null) {
                break;
            }
            DefaultPromise.notifyListener(executor(), this, localGenericFutureListener);
        }
        return this;
    }

    public Future<V> removeListener(GenericFutureListener<? extends Future<? super V>> paramGenericFutureListener) {
        return this;
    }

    public Future<V> removeListeners(GenericFutureListener<? extends Future<? super V>>... paramVarArgs) {
        return this;
    }

    public Future<V> await()
            throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        return this;
    }

    public boolean await(long paramLong, TimeUnit paramTimeUnit)
            throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        return true;
    }

    public Future<V> sync()
            throws InterruptedException {
        return this;
    }

    public Future<V> syncUninterruptibly() {
        return this;
    }

    public boolean await(long paramLong)
            throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        return true;
    }

    public Future<V> awaitUninterruptibly() {
        return this;
    }

    public boolean awaitUninterruptibly(long paramLong, TimeUnit paramTimeUnit) {
        return true;
    }

    public boolean awaitUninterruptibly(long paramLong) {
        return true;
    }

    public boolean isDone() {
        return true;
    }

    public boolean isCancellable() {
        return false;
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean cancel(boolean paramBoolean) {
        return false;
    }
}




