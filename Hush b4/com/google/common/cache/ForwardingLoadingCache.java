// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.cache;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.ExecutionException;
import com.google.common.annotations.Beta;

@Beta
public abstract class ForwardingLoadingCache<K, V> extends ForwardingCache<K, V> implements LoadingCache<K, V>
{
    protected ForwardingLoadingCache() {
    }
    
    @Override
    protected abstract LoadingCache<K, V> delegate();
    
    @Override
    public V get(final K key) throws ExecutionException {
        return this.delegate().get(key);
    }
    
    @Override
    public V getUnchecked(final K key) {
        return this.delegate().getUnchecked(key);
    }
    
    @Override
    public ImmutableMap<K, V> getAll(final Iterable<? extends K> keys) throws ExecutionException {
        return this.delegate().getAll(keys);
    }
    
    @Override
    public V apply(final K key) {
        return this.delegate().apply(key);
    }
    
    @Override
    public void refresh(final K key) {
        this.delegate().refresh(key);
    }
    
    @Beta
    public abstract static class SimpleForwardingLoadingCache<K, V> extends ForwardingLoadingCache<K, V>
    {
        private final LoadingCache<K, V> delegate;
        
        protected SimpleForwardingLoadingCache(final LoadingCache<K, V> delegate) {
            this.delegate = Preconditions.checkNotNull(delegate);
        }
        
        @Override
        protected final LoadingCache<K, V> delegate() {
            return this.delegate;
        }
    }
}
