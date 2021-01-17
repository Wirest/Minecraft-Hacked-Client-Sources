// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.concurrent;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Callable;

public abstract class AbstractEventExecutorGroup implements EventExecutorGroup
{
    @Override
    public Future<?> submit(final Runnable task) {
        return this.next().submit(task);
    }
    
    @Override
    public <T> Future<T> submit(final Runnable task, final T result) {
        return this.next().submit(task, result);
    }
    
    @Override
    public <T> Future<T> submit(final Callable<T> task) {
        return this.next().submit(task);
    }
    
    @Override
    public ScheduledFuture<?> schedule(final Runnable command, final long delay, final TimeUnit unit) {
        return this.next().schedule(command, delay, unit);
    }
    
    @Override
    public <V> ScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit unit) {
        return this.next().schedule(callable, delay, unit);
    }
    
    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit) {
        return this.next().scheduleAtFixedRate(command, initialDelay, period, unit);
    }
    
    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit) {
        return this.next().scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }
    
    @Override
    public Future<?> shutdownGracefully() {
        return this.shutdownGracefully(2L, 15L, TimeUnit.SECONDS);
    }
    
    @Deprecated
    @Override
    public abstract void shutdown();
    
    @Deprecated
    @Override
    public List<Runnable> shutdownNow() {
        this.shutdown();
        return Collections.emptyList();
    }
    
    @Override
    public <T> List<java.util.concurrent.Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return this.next().invokeAll(tasks);
    }
    
    @Override
    public <T> List<java.util.concurrent.Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.next().invokeAll(tasks, timeout, unit);
    }
    
    @Override
    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return this.next().invokeAny(tasks);
    }
    
    @Override
    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.next().invokeAny(tasks, timeout, unit);
    }
    
    @Override
    public void execute(final Runnable command) {
        this.next().execute(command);
    }
}
