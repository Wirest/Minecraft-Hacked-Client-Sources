// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import com.google.common.collect.ForwardingObject;

public abstract class ForwardingFuture<V> extends ForwardingObject implements Future<V>
{
    protected ForwardingFuture() {
    }
    
    @Override
    protected abstract Future<V> delegate();
    
    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        return this.delegate().cancel(mayInterruptIfRunning);
    }
    
    @Override
    public boolean isCancelled() {
        return this.delegate().isCancelled();
    }
    
    @Override
    public boolean isDone() {
        return this.delegate().isDone();
    }
    
    @Override
    public V get() throws InterruptedException, ExecutionException {
        return this.delegate().get();
    }
    
    @Override
    public V get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.delegate().get(timeout, unit);
    }
    
    public abstract static class SimpleForwardingFuture<V> extends ForwardingFuture<V>
    {
        private final Future<V> delegate;
        
        protected SimpleForwardingFuture(final Future<V> delegate) {
            this.delegate = Preconditions.checkNotNull(delegate);
        }
        
        @Override
        protected final Future<V> delegate() {
            return this.delegate;
        }
    }
}
