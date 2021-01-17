// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Set;
import java.util.Collection;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
public abstract class ImmutableBiMap<K, V> extends ImmutableMap<K, V> implements BiMap<K, V>
{
    private static final Map.Entry<?, ?>[] EMPTY_ENTRY_ARRAY;
    
    public static <K, V> ImmutableBiMap<K, V> of() {
        return (ImmutableBiMap<K, V>)EmptyImmutableBiMap.INSTANCE;
    }
    
    public static <K, V> ImmutableBiMap<K, V> of(final K k1, final V v1) {
        return new SingletonImmutableBiMap<K, V>(k1, v1);
    }
    
    public static <K, V> ImmutableBiMap<K, V> of(final K k1, final V v1, final K k2, final V v2) {
        return new RegularImmutableBiMap<K, V>((ImmutableMapEntry.TerminalEntry<?, ?>[])new ImmutableMapEntry.TerminalEntry[] { ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2) });
    }
    
    public static <K, V> ImmutableBiMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3) {
        return new RegularImmutableBiMap<K, V>((ImmutableMapEntry.TerminalEntry<?, ?>[])new ImmutableMapEntry.TerminalEntry[] { ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3) });
    }
    
    public static <K, V> ImmutableBiMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4) {
        return new RegularImmutableBiMap<K, V>((ImmutableMapEntry.TerminalEntry<?, ?>[])new ImmutableMapEntry.TerminalEntry[] { ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4) });
    }
    
    public static <K, V> ImmutableBiMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5) {
        return new RegularImmutableBiMap<K, V>((ImmutableMapEntry.TerminalEntry<?, ?>[])new ImmutableMapEntry.TerminalEntry[] { ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4), ImmutableMap.entryOf(k5, v5) });
    }
    
    public static <K, V> Builder<K, V> builder() {
        return new Builder<K, V>();
    }
    
    public static <K, V> ImmutableBiMap<K, V> copyOf(final Map<? extends K, ? extends V> map) {
        if (map instanceof ImmutableBiMap) {
            final ImmutableBiMap<K, V> bimap = (ImmutableBiMap<K, V>)(ImmutableBiMap)map;
            if (!bimap.isPartialView()) {
                return bimap;
            }
        }
        final Map.Entry<?, ?>[] entries = map.entrySet().toArray(ImmutableBiMap.EMPTY_ENTRY_ARRAY);
        switch (entries.length) {
            case 0: {
                return of();
            }
            case 1: {
                final Map.Entry<K, V> entry = (Map.Entry<K, V>)entries[0];
                return of(entry.getKey(), entry.getValue());
            }
            default: {
                return new RegularImmutableBiMap<K, V>(entries);
            }
        }
    }
    
    ImmutableBiMap() {
    }
    
    @Override
    public abstract ImmutableBiMap<V, K> inverse();
    
    @Override
    public ImmutableSet<V> values() {
        return (ImmutableSet<V>)this.inverse().keySet();
    }
    
    @Deprecated
    @Override
    public V forcePut(final K key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    Object writeReplace() {
        return new SerializedForm(this);
    }
    
    static {
        EMPTY_ENTRY_ARRAY = new Map.Entry[0];
    }
    
    public static final class Builder<K, V> extends ImmutableMap.Builder<K, V>
    {
        @Override
        public Builder<K, V> put(final K key, final V value) {
            super.put(key, value);
            return this;
        }
        
        @Override
        public Builder<K, V> putAll(final Map<? extends K, ? extends V> map) {
            super.putAll(map);
            return this;
        }
        
        @Override
        public ImmutableBiMap<K, V> build() {
            switch (this.size) {
                case 0: {
                    return ImmutableBiMap.of();
                }
                case 1: {
                    return ImmutableBiMap.of((K)this.entries[0].getKey(), (V)this.entries[0].getValue());
                }
                default: {
                    return new RegularImmutableBiMap<K, V>(this.size, this.entries);
                }
            }
        }
    }
    
    private static class SerializedForm extends ImmutableMap.SerializedForm
    {
        private static final long serialVersionUID = 0L;
        
        SerializedForm(final ImmutableBiMap<?, ?> bimap) {
            super(bimap);
        }
        
        @Override
        Object readResolve() {
            final ImmutableBiMap.Builder<Object, Object> builder = new ImmutableBiMap.Builder<Object, Object>();
            return this.createMap(builder);
        }
    }
}
