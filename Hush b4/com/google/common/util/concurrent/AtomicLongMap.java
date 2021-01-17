// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.Collections;
import com.google.common.collect.Maps;
import com.google.common.base.Function;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ConcurrentHashMap;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public final class AtomicLongMap<K>
{
    private final ConcurrentHashMap<K, AtomicLong> map;
    private transient Map<K, Long> asMap;
    
    private AtomicLongMap(final ConcurrentHashMap<K, AtomicLong> map) {
        this.map = Preconditions.checkNotNull(map);
    }
    
    public static <K> AtomicLongMap<K> create() {
        return new AtomicLongMap<K>(new ConcurrentHashMap<K, AtomicLong>());
    }
    
    public static <K> AtomicLongMap<K> create(final Map<? extends K, ? extends Long> m) {
        final AtomicLongMap<K> result = create();
        result.putAll(m);
        return result;
    }
    
    public long get(final K key) {
        final AtomicLong atomic = this.map.get(key);
        return (atomic == null) ? 0L : atomic.get();
    }
    
    public long incrementAndGet(final K key) {
        return this.addAndGet(key, 1L);
    }
    
    public long decrementAndGet(final K key) {
        return this.addAndGet(key, -1L);
    }
    
    public long addAndGet(final K key, final long delta) {
        while (true) {
            AtomicLong atomic = this.map.get(key);
            if (atomic == null) {
                atomic = this.map.putIfAbsent(key, new AtomicLong(delta));
                if (atomic == null) {
                    return delta;
                }
            }
            while (true) {
                final long oldValue = atomic.get();
                if (oldValue == 0L) {
                    if (this.map.replace(key, atomic, new AtomicLong(delta))) {
                        return delta;
                    }
                    break;
                }
                else {
                    final long newValue = oldValue + delta;
                    if (atomic.compareAndSet(oldValue, newValue)) {
                        return newValue;
                    }
                    continue;
                }
            }
        }
    }
    
    public long getAndIncrement(final K key) {
        return this.getAndAdd(key, 1L);
    }
    
    public long getAndDecrement(final K key) {
        return this.getAndAdd(key, -1L);
    }
    
    public long getAndAdd(final K key, final long delta) {
        while (true) {
            AtomicLong atomic = this.map.get(key);
            if (atomic == null) {
                atomic = this.map.putIfAbsent(key, new AtomicLong(delta));
                if (atomic == null) {
                    return 0L;
                }
            }
            while (true) {
                final long oldValue = atomic.get();
                if (oldValue == 0L) {
                    if (this.map.replace(key, atomic, new AtomicLong(delta))) {
                        return 0L;
                    }
                    break;
                }
                else {
                    final long newValue = oldValue + delta;
                    if (atomic.compareAndSet(oldValue, newValue)) {
                        return oldValue;
                    }
                    continue;
                }
            }
        }
    }
    
    public long put(final K key, final long newValue) {
        while (true) {
            AtomicLong atomic = this.map.get(key);
            if (atomic == null) {
                atomic = this.map.putIfAbsent(key, new AtomicLong(newValue));
                if (atomic == null) {
                    return 0L;
                }
            }
            while (true) {
                final long oldValue = atomic.get();
                if (oldValue == 0L) {
                    if (this.map.replace(key, atomic, new AtomicLong(newValue))) {
                        return 0L;
                    }
                    break;
                }
                else {
                    if (atomic.compareAndSet(oldValue, newValue)) {
                        return oldValue;
                    }
                    continue;
                }
            }
        }
    }
    
    public void putAll(final Map<? extends K, ? extends Long> m) {
        for (final Map.Entry<? extends K, ? extends Long> entry : m.entrySet()) {
            this.put(entry.getKey(), (long)entry.getValue());
        }
    }
    
    public long remove(final K key) {
        final AtomicLong atomic = this.map.get(key);
        if (atomic == null) {
            return 0L;
        }
        long oldValue;
        do {
            oldValue = atomic.get();
        } while (oldValue != 0L && !atomic.compareAndSet(oldValue, 0L));
        this.map.remove(key, atomic);
        return oldValue;
    }
    
    public void removeAllZeros() {
        for (final K key : this.map.keySet()) {
            final AtomicLong atomic = this.map.get(key);
            if (atomic != null && atomic.get() == 0L) {
                this.map.remove(key, atomic);
            }
        }
    }
    
    public long sum() {
        long sum = 0L;
        for (final AtomicLong value : this.map.values()) {
            sum += value.get();
        }
        return sum;
    }
    
    public Map<K, Long> asMap() {
        final Map<K, Long> result = this.asMap;
        return (result == null) ? (this.asMap = this.createAsMap()) : result;
    }
    
    private Map<K, Long> createAsMap() {
        return Collections.unmodifiableMap((Map<? extends K, ? extends Long>)Maps.transformValues((Map<? extends K, AtomicLong>)this.map, (Function<? super AtomicLong, ? extends V>)new Function<AtomicLong, Long>() {
            @Override
            public Long apply(final AtomicLong atomic) {
                return atomic.get();
            }
        }));
    }
    
    public boolean containsKey(final Object key) {
        return this.map.containsKey(key);
    }
    
    public int size() {
        return this.map.size();
    }
    
    public boolean isEmpty() {
        return this.map.isEmpty();
    }
    
    public void clear() {
        this.map.clear();
    }
    
    @Override
    public String toString() {
        return this.map.toString();
    }
    
    long putIfAbsent(final K key, final long newValue) {
        AtomicLong atomic;
        do {
            atomic = this.map.get(key);
            if (atomic == null) {
                atomic = this.map.putIfAbsent(key, new AtomicLong(newValue));
                if (atomic == null) {
                    return 0L;
                }
            }
            final long oldValue = atomic.get();
            if (oldValue == 0L) {
                continue;
            }
            return oldValue;
        } while (!this.map.replace(key, atomic, new AtomicLong(newValue)));
        return 0L;
    }
    
    boolean replace(final K key, final long expectedOldValue, final long newValue) {
        if (expectedOldValue == 0L) {
            return this.putIfAbsent(key, newValue) == 0L;
        }
        final AtomicLong atomic = this.map.get(key);
        return atomic != null && atomic.compareAndSet(expectedOldValue, newValue);
    }
    
    boolean remove(final K key, final long value) {
        final AtomicLong atomic = this.map.get(key);
        if (atomic == null) {
            return false;
        }
        final long oldValue = atomic.get();
        if (oldValue != value) {
            return false;
        }
        if (oldValue == 0L || atomic.compareAndSet(oldValue, 0L)) {
            this.map.remove(key, atomic);
            return true;
        }
        return false;
    }
}
