package com.google.common.util.concurrent;

import javax.annotation.Nullable;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

public class ListenableFutureTask<V>
        extends FutureTask<V>
        implements ListenableFuture<V> {
    private final ExecutionList executionList = new ExecutionList();

    ListenableFutureTask(Callable<V> paramCallable) {
        super(paramCallable);
    }

    ListenableFutureTask(Runnable paramRunnable, @Nullable V paramV) {
        super(paramRunnable, paramV);
    }

    public static <V> ListenableFutureTask<V> create(Callable<V> paramCallable) {
        return new ListenableFutureTask(paramCallable);
    }

    public static <V> ListenableFutureTask<V> create(Runnable paramRunnable, @Nullable V paramV) {
        return new ListenableFutureTask(paramRunnable, paramV);
    }

    public void addListener(Runnable paramRunnable, Executor paramExecutor) {
        this.executionList.add(paramRunnable, paramExecutor);
    }

    protected void done() {
        this.executionList.execute();
    }
}




