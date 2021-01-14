package com.google.common.util.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public abstract interface ListenableFuture<V>
        extends Future<V> {
    public abstract void addListener(Runnable paramRunnable, Executor paramExecutor);
}




