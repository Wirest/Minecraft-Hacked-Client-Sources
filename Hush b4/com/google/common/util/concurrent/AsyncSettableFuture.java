// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.Future;
import javax.annotation.Nullable;
import com.google.common.base.Preconditions;

final class AsyncSettableFuture<V> extends ForwardingListenableFuture<V>
{
    private final NestedFuture<V> nested;
    private final ListenableFuture<V> dereferenced;
    
    public static <V> AsyncSettableFuture<V> create() {
        return new AsyncSettableFuture<V>();
    }
    
    private AsyncSettableFuture() {
        this.nested = new NestedFuture<V>();
        this.dereferenced = Futures.dereference((ListenableFuture<? extends ListenableFuture<? extends V>>)this.nested);
    }
    
    @Override
    protected ListenableFuture<V> delegate() {
        return this.dereferenced;
    }
    
    public boolean setFuture(final ListenableFuture<? extends V> future) {
        return this.nested.setFuture(Preconditions.checkNotNull(future));
    }
    
    public boolean setValue(@Nullable final V value) {
        return this.setFuture(Futures.immediateFuture(value));
    }
    
    public boolean setException(final Throwable exception) {
        return this.setFuture(Futures.immediateFailedFuture(exception));
    }
    
    public boolean isSet() {
        return this.nested.isDone();
    }
    
    private static final class NestedFuture<V> extends AbstractFuture<ListenableFuture<? extends V>>
    {
        boolean setFuture(final ListenableFuture<? extends V> value) {
            final boolean result = this.set(value);
            if (this.isCancelled()) {
                value.cancel(this.wasInterrupted());
            }
            return result;
        }
    }
}
