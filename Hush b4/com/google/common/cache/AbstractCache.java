// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.cache;

import java.util.concurrent.ConcurrentMap;
import java.util.Iterator;
import java.util.Map;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Callable;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible
public abstract class AbstractCache<K, V> implements Cache<K, V>
{
    protected AbstractCache() {
    }
    
    @Override
    public V get(final K key, final Callable<? extends V> valueLoader) throws ExecutionException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ImmutableMap<K, V> getAllPresent(final Iterable<?> keys) {
        final Map<K, V> result = (Map<K, V>)Maps.newLinkedHashMap();
        for (final Object key : keys) {
            if (!result.containsKey(key)) {
                final K castKey = (K)key;
                result.put(castKey, this.getIfPresent(key));
            }
        }
        return ImmutableMap.copyOf((Map<? extends K, ? extends V>)result);
    }
    
    @Override
    public void put(final K key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        for (final Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }
    
    @Override
    public void cleanUp() {
    }
    
    @Override
    public long size() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void invalidate(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void invalidateAll(final Iterable<?> keys) {
        for (final Object key : keys) {
            this.invalidate(key);
        }
    }
    
    @Override
    public void invalidateAll() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public CacheStats stats() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ConcurrentMap<K, V> asMap() {
        throw new UnsupportedOperationException();
    }
    
    @Beta
    public static final class SimpleStatsCounter implements StatsCounter
    {
        private final LongAddable hitCount;
        private final LongAddable missCount;
        private final LongAddable loadSuccessCount;
        private final LongAddable loadExceptionCount;
        private final LongAddable totalLoadTime;
        private final LongAddable evictionCount;
        
        public SimpleStatsCounter() {
            this.hitCount = LongAddables.create();
            this.missCount = LongAddables.create();
            this.loadSuccessCount = LongAddables.create();
            this.loadExceptionCount = LongAddables.create();
            this.totalLoadTime = LongAddables.create();
            this.evictionCount = LongAddables.create();
        }
        
        @Override
        public void recordHits(final int count) {
            this.hitCount.add(count);
        }
        
        @Override
        public void recordMisses(final int count) {
            this.missCount.add(count);
        }
        
        @Override
        public void recordLoadSuccess(final long loadTime) {
            this.loadSuccessCount.increment();
            this.totalLoadTime.add(loadTime);
        }
        
        @Override
        public void recordLoadException(final long loadTime) {
            this.loadExceptionCount.increment();
            this.totalLoadTime.add(loadTime);
        }
        
        @Override
        public void recordEviction() {
            this.evictionCount.increment();
        }
        
        @Override
        public CacheStats snapshot() {
            return new CacheStats(this.hitCount.sum(), this.missCount.sum(), this.loadSuccessCount.sum(), this.loadExceptionCount.sum(), this.totalLoadTime.sum(), this.evictionCount.sum());
        }
        
        public void incrementBy(final StatsCounter other) {
            final CacheStats otherStats = other.snapshot();
            this.hitCount.add(otherStats.hitCount());
            this.missCount.add(otherStats.missCount());
            this.loadSuccessCount.add(otherStats.loadSuccessCount());
            this.loadExceptionCount.add(otherStats.loadExceptionCount());
            this.totalLoadTime.add(otherStats.totalLoadTime());
            this.evictionCount.add(otherStats.evictionCount());
        }
    }
    
    @Beta
    public interface StatsCounter
    {
        void recordHits(final int p0);
        
        void recordMisses(final int p0);
        
        void recordLoadSuccess(final long p0);
        
        void recordLoadException(final long p0);
        
        void recordEviction();
        
        CacheStats snapshot();
    }
}
