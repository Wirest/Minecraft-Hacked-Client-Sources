package io.netty.util.concurrent;

import io.netty.util.internal.PlatformDependent;

public final class FailedFuture<V>
        extends CompleteFuture<V> {
    private final Throwable cause;

    public FailedFuture(EventExecutor paramEventExecutor, Throwable paramThrowable) {
        super(paramEventExecutor);
        if (paramThrowable == null) {
            throw new NullPointerException("cause");
        }
        this.cause = paramThrowable;
    }

    public Throwable cause() {
        return this.cause;
    }

    public boolean isSuccess() {
        return false;
    }

    public Future<V> sync() {
        PlatformDependent.throwException(this.cause);
        return this;
    }

    public Future<V> syncUninterruptibly() {
        PlatformDependent.throwException(this.cause);
        return this;
    }

    public V getNow() {
        return null;
    }
}




