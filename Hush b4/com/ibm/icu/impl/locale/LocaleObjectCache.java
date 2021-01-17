// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.locale;

import java.lang.ref.SoftReference;
import java.lang.ref.ReferenceQueue;
import java.util.concurrent.ConcurrentHashMap;

public abstract class LocaleObjectCache<K, V>
{
    private ConcurrentHashMap<K, CacheEntry<K, V>> _map;
    private ReferenceQueue<V> _queue;
    
    public LocaleObjectCache() {
        this(16, 0.75f, 16);
    }
    
    public LocaleObjectCache(final int initialCapacity, final float loadFactor, final int concurrencyLevel) {
        this._queue = new ReferenceQueue<V>();
        this._map = new ConcurrentHashMap<K, CacheEntry<K, V>>(initialCapacity, loadFactor, concurrencyLevel);
    }
    
    public V get(K key) {
        V value = null;
        this.cleanStaleEntries();
        CacheEntry<K, V> entry = this._map.get(key);
        if (entry != null) {
            value = (V)entry.get();
        }
        if (value == null) {
            key = this.normalizeKey(key);
            final V newVal = this.createObject(key);
            if (key == null || newVal == null) {
                return null;
            }
            final CacheEntry<K, V> newEntry = new CacheEntry<K, V>(key, newVal, this._queue);
            while (value == null) {
                this.cleanStaleEntries();
                entry = this._map.putIfAbsent(key, newEntry);
                if (entry == null) {
                    value = newVal;
                    break;
                }
                value = (V)entry.get();
            }
        }
        return value;
    }
    
    private void cleanStaleEntries() {
        CacheEntry<K, V> entry;
        while ((entry = (CacheEntry<K, V>)(CacheEntry)this._queue.poll()) != null) {
            this._map.remove(entry.getKey());
        }
    }
    
    protected abstract V createObject(final K p0);
    
    protected K normalizeKey(final K key) {
        return key;
    }
    
    private static class CacheEntry<K, V> extends SoftReference<V>
    {
        private K _key;
        
        CacheEntry(final K key, final V value, final ReferenceQueue<V> queue) {
            super(value, queue);
            this._key = key;
        }
        
        K getKey() {
            return this._key;
        }
    }
}
