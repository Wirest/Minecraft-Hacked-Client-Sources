// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

public abstract class SoftCache<K, V, D> extends CacheBase<K, V, D>
{
    private ConcurrentHashMap<K, SettableSoftReference<V>> map;
    
    public SoftCache() {
        this.map = new ConcurrentHashMap<K, SettableSoftReference<V>>();
    }
    
    @Override
    public final V getInstance(final K key, final D data) {
        SettableSoftReference<V> valueRef = this.map.get(key);
        if (valueRef != null) {
            synchronized (valueRef) {
                V value = ((SettableSoftReference<Object>)valueRef).ref.get();
                if (value != null) {
                    return value;
                }
                value = this.createInstance(key, data);
                if (value != null) {
                    ((SettableSoftReference<Object>)valueRef).ref = (SoftReference<Object>)new SoftReference((T)value);
                }
                return value;
            }
        }
        V value = this.createInstance(key, data);
        if (value == null) {
            return null;
        }
        valueRef = this.map.putIfAbsent(key, new SettableSoftReference<V>((Object)value));
        if (valueRef == null) {
            return value;
        }
        return (V)((SettableSoftReference<Object>)valueRef).setIfAbsent(value);
    }
    
    private static final class SettableSoftReference<V>
    {
        private SoftReference<V> ref;
        
        private SettableSoftReference(final V value) {
            this.ref = new SoftReference<V>(value);
        }
        
        private synchronized V setIfAbsent(final V value) {
            final V oldValue = this.ref.get();
            if (oldValue == null) {
                this.ref = new SoftReference<V>(value);
                return value;
            }
            return oldValue;
        }
    }
}
