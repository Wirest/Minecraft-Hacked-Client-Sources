package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;

import javax.annotation.Nullable;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;

@Beta
public abstract class AbstractListeningExecutorService
        extends AbstractExecutorService
        implements ListeningExecutorService {
    protected final <T> ListenableFutureTask<T> newTaskFor(Runnable paramRunnable, T paramT) {
        return ListenableFutureTask.create(paramRunnable, paramT);
    }

    protected final <T> ListenableFutureTask<T> newTaskFor(Callable<T> paramCallable) {
        return ListenableFutureTask.create(paramCallable);
    }

    public ListenableFuture<?> submit(Runnable paramRunnable) {
        return (ListenableFuture) super.submit(paramRunnable);
    }

    public <T> ListenableFuture<T> submit(Runnable paramRunnable, @Nullable T paramT) {
        return (ListenableFuture) super.submit(paramRunnable, paramT);
    }

    public <T> ListenableFuture<T> submit(Callable<T> paramCallable) {
        return (ListenableFuture) super.submit(paramCallable);
    }
}




