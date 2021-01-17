// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.cache;

import com.google.common.base.Preconditions;
import java.util.concurrent.ConcurrentMap;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Callable;
import javax.annotation.Nullable;
import com.google.common.annotations.Beta;
import com.google.common.collect.ForwardingObject;

@Beta
public abstract class ForwardingCache<K, V> extends ForwardingObject implements Cache<K, V>
{
    protected ForwardingCache() {
    }
    
    @Override
    protected abstract Cache<K, V> delegate();
    
    @Nullable
    @Override
    public V getIfPresent(final Object key) {
        return this.delegate().getIfPresent(key);
    }
    
    @Override
    public V get(final K key, final Callable<? extends V> valueLoader) throws ExecutionException {
        return this.delegate().get(key, valueLoader);
    }
    
    @Override
    public ImmutableMap<K, V> getAllPresent(final Iterable<?> keys) {
        return this.delegate().getAllPresent(keys);
    }
    
    @Override
    public void put(final K key, final V value) {
        this.delegate().put(key, value);
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        this.delegate().putAll(m);
    }
    
    @Override
    public void invalidate(final Object key) {
        this.delegate().invalidate(key);
    }
    
    @Override
    public void invalidateAll(final Iterable<?> keys) {
        this.delegate().invalidateAll(keys);
    }
    
    @Override
    public void invalidateAll() {
        this.delegate().invalidateAll();
    }
    
    @Override
    public long size() {
        return this.delegate().size();
    }
    
    @Override
    public CacheStats stats() {
        return this.delegate().stats();
    }
    
    @Override
    public ConcurrentMap<K, V> asMap() {
        return this.delegate().asMap();
    }
    
    @Override
    public void cleanUp() {
        this.delegate().cleanUp();
    }
    
    @Beta
    public abstract static class SimpleForwardingCache<K, V> extends ForwardingCache<K, V>
    {
        private final Cache<K, V> delegate;
        
        protected SimpleForwardingCache(final Cache<K, V> delegate) {
            this.delegate = Preconditions.checkNotNull(delegate);
        }
        
        @Override
        protected final Cache<K, V> delegate() {
            return this.delegate;
        }
    }
}
