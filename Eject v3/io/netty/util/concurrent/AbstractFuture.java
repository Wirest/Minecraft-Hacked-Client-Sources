package io.netty.util.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class AbstractFuture<V>
        implements Future<V> {
    public V get()
            throws InterruptedException, ExecutionException {
        await();
        Throwable localThrowable = cause();
        if (localThrowable == null) {
            return (V) getNow();
        }
        throw new ExecutionException(localThrowable);
    }

    public V get(long paramLong, TimeUnit paramTimeUnit)
            throws InterruptedException, ExecutionException, TimeoutException {
        if (await(paramLong, paramTimeUnit)) {
            Throwable localThrowable = cause();
            if (localThrowable == null) {
                return (V) getNow();
            }
            throw new ExecutionException(localThrowable);
        }
        throw new TimeoutException();
    }
}




