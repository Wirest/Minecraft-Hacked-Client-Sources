// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.concurrent.Future;
import java.util.Iterator;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import com.google.common.base.Throwables;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import com.google.common.base.Preconditions;
import java.util.concurrent.ExecutorService;

abstract class WrappingExecutorService implements ExecutorService
{
    private final ExecutorService delegate;
    
    protected WrappingExecutorService(final ExecutorService delegate) {
        this.delegate = Preconditions.checkNotNull(delegate);
    }
    
    protected abstract <T> Callable<T> wrapTask(final Callable<T> p0);
    
    protected Runnable wrapTask(final Runnable command) {
        final Callable<Object> wrapped = this.wrapTask((Callable<Object>)Executors.callable(command, (T)null));
        return new Runnable() {
            @Override
            public void run() {
                try {
                    wrapped.call();
                }
                catch (Exception e) {
                    Throwables.propagate(e);
                }
            }
        };
    }
    
    private final <T> ImmutableList<Callable<T>> wrapTasks(final Collection<? extends Callable<T>> tasks) {
        final ImmutableList.Builder<Callable<T>> builder = ImmutableList.builder();
        for (final Callable<T> task : tasks) {
            builder.add(this.wrapTask(task));
        }
        return builder.build();
    }
    
    @Override
    public final void execute(final Runnable command) {
        this.delegate.execute(this.wrapTask(command));
    }
    
    @Override
    public final <T> Future<T> submit(final Callable<T> task) {
        return this.delegate.submit((Callable<T>)this.wrapTask((Callable<T>)Preconditions.checkNotNull((Callable<T>)task)));
    }
    
    @Override
    public final Future<?> submit(final Runnable task) {
        return this.delegate.submit(this.wrapTask(task));
    }
    
    @Override
    public final <T> Future<T> submit(final Runnable task, final T result) {
        return this.delegate.submit(this.wrapTask(task), result);
    }
    
    @Override
    public final <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return this.delegate.invokeAll((Collection<? extends Callable<T>>)this.wrapTasks((Collection<? extends Callable<Object>>)tasks));
    }
    
    @Override
    public final <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.delegate.invokeAll((Collection<? extends Callable<T>>)this.wrapTasks((Collection<? extends Callable<Object>>)tasks), timeout, unit);
    }
    
    @Override
    public final <T> T invokeAny(final Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return this.delegate.invokeAny((Collection<? extends Callable<T>>)this.wrapTasks((Collection<? extends Callable<Object>>)tasks));
    }
    
    @Override
    public final <T> T invokeAny(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.delegate.invokeAny((Collection<? extends Callable<T>>)this.wrapTasks((Collection<? extends Callable<Object>>)tasks), timeout, unit);
    }
    
    @Override
    public final void shutdown() {
        this.delegate.shutdown();
    }
    
    @Override
    public final List<Runnable> shutdownNow() {
        return this.delegate.shutdownNow();
    }
    
    @Override
    public final boolean isShutdown() {
        return this.delegate.isShutdown();
    }
    
    @Override
    public final boolean isTerminated() {
        return this.delegate.isTerminated();
    }
    
    @Override
    public final boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.delegate.awaitTermination(timeout, unit);
    }
}
