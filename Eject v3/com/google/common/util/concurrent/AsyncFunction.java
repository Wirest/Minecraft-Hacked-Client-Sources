package com.google.common.util.concurrent;

public abstract interface AsyncFunction<I, O> {
    public abstract ListenableFuture<O> apply(I paramI)
            throws Exception;
}




