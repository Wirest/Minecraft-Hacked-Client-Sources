// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.RunnableFuture;
import java.util.concurrent.Future;
import javax.annotation.Nullable;
import java.util.concurrent.Callable;
import com.google.common.annotations.Beta;
import java.util.concurrent.AbstractExecutorService;

@Beta
public abstract class AbstractListeningExecutorService extends AbstractExecutorService implements ListeningExecutorService
{
    @Override
    protected final <T> ListenableFutureTask<T> newTaskFor(final Runnable runnable, final T value) {
        return ListenableFutureTask.create(runnable, value);
    }
    
    @Override
    protected final <T> ListenableFutureTask<T> newTaskFor(final Callable<T> callable) {
        return ListenableFutureTask.create(callable);
    }
    
    @Override
    public ListenableFuture<?> submit(final Runnable task) {
        return (ListenableFuture<?>)(ListenableFuture)super.submit(task);
    }
    
    @Override
    public <T> ListenableFuture<T> submit(final Runnable task, @Nullable final T result) {
        return (ListenableFuture<T>)(ListenableFuture)super.submit(task, result);
    }
    
    @Override
    public <T> ListenableFuture<T> submit(final Callable<T> task) {
        return (ListenableFuture<T>)(ListenableFuture)super.submit(task);
    }
}
