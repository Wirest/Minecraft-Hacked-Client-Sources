// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.concurrent.Future;
import java.util.concurrent.Executor;

public abstract class ForwardingListenableFuture<V> extends ForwardingFuture<V> implements ListenableFuture<V>
{
    protected ForwardingListenableFuture() {
    }
    
    @Override
    protected abstract ListenableFuture<V> delegate();
    
    @Override
    public void addListener(final Runnable listener, final Executor exec) {
        this.delegate().addListener(listener, exec);
    }
    
    public abstract static class SimpleForwardingListenableFuture<V> extends ForwardingListenableFuture<V>
    {
        private final ListenableFuture<V> delegate;
        
        protected SimpleForwardingListenableFuture(final ListenableFuture<V> delegate) {
            this.delegate = Preconditions.checkNotNull(delegate);
        }
        
        @Override
        protected final ListenableFuture<V> delegate() {
            return this.delegate;
        }
    }
}
