package io.netty.util.concurrent;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class AbstractEventExecutorGroup
        implements EventExecutorGroup {
    public Future<?> submit(Runnable paramRunnable) {
        return next().submit(paramRunnable);
    }

    public <T> Future<T> submit(Runnable paramRunnable, T paramT) {
        return next().submit(paramRunnable, paramT);
    }

    public <T> Future<T> submit(Callable<T> paramCallable) {
        return next().submit(paramCallable);
    }

    public ScheduledFuture<?> schedule(Runnable paramRunnable, long paramLong, TimeUnit paramTimeUnit) {
        return next().schedule(paramRunnable, paramLong, paramTimeUnit);
    }

    public <V> ScheduledFuture<V> schedule(Callable<V> paramCallable, long paramLong, TimeUnit paramTimeUnit) {
        return next().schedule(paramCallable, paramLong, paramTimeUnit);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable paramRunnable, long paramLong1, long paramLong2, TimeUnit paramTimeUnit) {
        return next().scheduleAtFixedRate(paramRunnable, paramLong1, paramLong2, paramTimeUnit);
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable paramRunnable, long paramLong1, long paramLong2, TimeUnit paramTimeUnit) {
        return next().scheduleWithFixedDelay(paramRunnable, paramLong1, paramLong2, paramTimeUnit);
    }

    public Future<?> shutdownGracefully() {
        return shutdownGracefully(2L, 15L, TimeUnit.SECONDS);
    }

    @Deprecated
    public abstract void shutdown();

    @Deprecated
    public List<Runnable> shutdownNow() {
        shutdown();
        return Collections.emptyList();
    }

    public <T> List<java.util.concurrent.Future<T>> invokeAll(Collection<? extends Callable<T>> paramCollection)
            throws InterruptedException {
        return next().invokeAll(paramCollection);
    }

    public <T> List<java.util.concurrent.Future<T>> invokeAll(Collection<? extends Callable<T>> paramCollection, long paramLong, TimeUnit paramTimeUnit)
            throws InterruptedException {
        return next().invokeAll(paramCollection, paramLong, paramTimeUnit);
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> paramCollection)
            throws InterruptedException, ExecutionException {
        return (T) next().invokeAny(paramCollection);
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> paramCollection, long paramLong, TimeUnit paramTimeUnit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return (T) next().invokeAny(paramCollection, paramLong, paramTimeUnit);
    }

    public void execute(Runnable paramRunnable) {
        next().execute(paramRunnable);
    }
}




