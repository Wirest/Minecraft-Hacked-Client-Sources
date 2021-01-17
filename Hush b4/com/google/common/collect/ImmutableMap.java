// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Set;
import java.util.Comparator;
import com.google.common.annotations.Beta;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.EnumMap;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Map;

@GwtCompatible(serializable = true, emulated = true)
public abstract class ImmutableMap<K, V> implements Map<K, V>, Serializable
{
    private static final Entry<?, ?>[] EMPTY_ENTRY_ARRAY;
    private transient ImmutableSet<Entry<K, V>> entrySet;
    private transient ImmutableSet<K> keySet;
    private transient ImmutableCollection<V> values;
    private transient ImmutableSetMultimap<K, V> multimapView;
    
    public static <K, V> ImmutableMap<K, V> of() {
        return (ImmutableMap<K, V>)ImmutableBiMap.of();
    }
    
    public static <K, V> ImmutableMap<K, V> of(final K k1, final V v1) {
        return ImmutableBiMap.of(k1, v1);
    }
    
    public static <K, V> ImmutableMap<K, V> of(final K k1, final V v1, final K k2, final V v2) {
        return new RegularImmutableMap<K, V>((ImmutableMapEntry.TerminalEntry<?, ?>[])new ImmutableMapEntry.TerminalEntry[] { entryOf(k1, v1), entryOf(k2, v2) });
    }
    
    public static <K, V> ImmutableMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3) {
        return new RegularImmutableMap<K, V>((ImmutableMapEntry.TerminalEntry<?, ?>[])new ImmutableMapEntry.TerminalEntry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3) });
    }
    
    public static <K, V> ImmutableMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4) {
        return new RegularImmutableMap<K, V>((ImmutableMapEntry.TerminalEntry<?, ?>[])new ImmutableMapEntry.TerminalEntry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4) });
    }
    
    public static <K, V> ImmutableMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5) {
        return new RegularImmutableMap<K, V>((ImmutableMapEntry.TerminalEntry<?, ?>[])new ImmutableMapEntry.TerminalEntry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5) });
    }
    
    static <K, V> ImmutableMapEntry.TerminalEntry<K, V> entryOf(final K key, final V value) {
        CollectPreconditions.checkEntryNotNull(key, value);
        return new ImmutableMapEntry.TerminalEntry<K, V>(key, value);
    }
    
    public static <K, V> Builder<K, V> builder() {
        return new Builder<K, V>();
    }
    
    static void checkNoConflict(final boolean safe, final String conflictDescription, final Entry<?, ?> entry1, final Entry<?, ?> entry2) {
        if (!safe) {
            throw new IllegalArgumentException("Multiple entries with same " + conflictDescription + ": " + entry1 + " and " + entry2);
        }
    }
    
    public static <K, V> ImmutableMap<K, V> copyOf(final Map<? extends K, ? extends V> map) {
        if (map instanceof ImmutableMap && !(map instanceof ImmutableSortedMap)) {
            final ImmutableMap<K, V> kvMap = (ImmutableMap<K, V>)(ImmutableMap)map;
            if (!kvMap.isPartialView()) {
                return kvMap;
            }
        }
        else if (map instanceof EnumMap) {
            return (ImmutableMap<K, V>)copyOfEnumMapUnsafe((Map<?, ?>)map);
        }
        final Entry<?, ?>[] entries = map.entrySet().toArray(ImmutableMap.EMPTY_ENTRY_ARRAY);
        switch (entries.length) {
            case 0: {
                return of();
            }
            case 1: {
                final Entry<K, V> onlyEntry = (Entry<K, V>)entries[0];
                return of(onlyEntry.getKey(), onlyEntry.getValue());
            }
            default: {
                return new RegularImmutableMap<K, V>(entries);
            }
        }
    }
    
    private static <K, V> ImmutableMap<K, V> copyOfEnumMapUnsafe(final Map<? extends K, ? extends V> map) {
        return (ImmutableMap<K, V>)copyOfEnumMap((Map<Enum, ?>)(EnumMap)map);
    }
    
    private static <K extends Enum<K>, V> ImmutableMap<K, V> copyOfEnumMap(final Map<K, ? extends V> original) {
        final EnumMap<K, V> copy = new EnumMap<K, V>(original);
        for (final Entry<?, ?> entry : copy.entrySet()) {
            CollectPreconditions.checkEntryNotNull(entry.getKey(), entry.getValue());
        }
        return ImmutableEnumMap.asImmutable(copy);
    }
    
    ImmutableMap() {
    }
    
    @Deprecated
    @Override
    public final V put(final K k, final V v) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public final V remove(final Object o) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public final void putAll(final Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public final void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public boolean containsKey(@Nullable final Object key) {
        return this.get(key) != null;
    }
    
    @Override
    public boolean containsValue(@Nullable final Object value) {
        return this.values().contains(value);
    }
    
    @Override
    public abstract V get(@Nullable final Object p0);
    
    @Override
    public ImmutableSet<Entry<K, V>> entrySet() {
        final ImmutableSet<Entry<K, V>> result = this.entrySet;
        return (result == null) ? (this.entrySet = this.createEntrySet()) : result;
    }
    
    abstract ImmutableSet<Entry<K, V>> createEntrySet();
    
    @Override
    public ImmutableSet<K> keySet() {
        final ImmutableSet<K> result = this.keySet;
        return (result == null) ? (this.keySet = this.createKeySet()) : result;
    }
    
    ImmutableSet<K> createKeySet() {
        return (ImmutableSet<K>)new ImmutableMapKeySet((ImmutableMap<Object, Object>)this);
    }
    
    @Override
    public ImmutableCollection<V> values() {
        final ImmutableCollection<V> result = this.values;
        return (result == null) ? (this.values = (ImmutableCollection<V>)new ImmutableMapValues((ImmutableMap<Object, Object>)this)) : result;
    }
    
    @Beta
    public ImmutableSetMultimap<K, V> asMultimap() {
        final ImmutableSetMultimap<K, V> result = this.multimapView;
        return (result == null) ? (this.multimapView = this.createMultimapView()) : result;
    }
    
    private ImmutableSetMultimap<K, V> createMultimapView() {
        final ImmutableMap<K, ImmutableSet<V>> map = this.viewMapValuesAsSingletonSets();
        return new ImmutableSetMultimap<K, V>(map, map.size(), null);
    }
    
    private ImmutableMap<K, ImmutableSet<V>> viewMapValuesAsSingletonSets() {
        return (ImmutableMap<K, ImmutableSet<V>>)new MapViewOfValuesAsSingletonSets((ImmutableMap<Object, Object>)this);
    }
    
    @Override
    public boolean equals(@Nullable final Object object) {
        return Maps.equalsImpl(this, object);
    }
    
    abstract boolean isPartialView();
    
    @Override
    public int hashCode() {
        return this.entrySet().hashCode();
    }
    
    @Override
    public String toString() {
        return Maps.toStringImpl(this);
    }
    
    Object writeReplace() {
        return new SerializedForm(this);
    }
    
    static {
        EMPTY_ENTRY_ARRAY = new Entry[0];
    }
    
    public static class Builder<K, V>
    {
        ImmutableMapEntry.TerminalEntry<K, V>[] entries;
        int size;
        
        public Builder() {
            this(4);
        }
        
        Builder(final int initialCapacity) {
            this.entries = (ImmutableMapEntry.TerminalEntry<K, V>[])new ImmutableMapEntry.TerminalEntry[initialCapacity];
            this.size = 0;
        }
        
        private void ensureCapacity(final int minCapacity) {
            if (minCapacity > this.entries.length) {
                this.entries = ObjectArrays.arraysCopyOf(this.entries, ImmutableCollection.Builder.expandedCapacity(this.entries.length, minCapacity));
            }
        }
        
        public Builder<K, V> put(final K key, final V value) {
            this.ensureCapacity(this.size + 1);
            final ImmutableMapEntry.TerminalEntry<K, V> entry = ImmutableMap.entryOf(key, value);
            this.entries[this.size++] = entry;
            return this;
        }
        
        public Builder<K, V> put(final Entry<? extends K, ? extends V> entry) {
            return (Builder<K, V>)this.put(entry.getKey(), entry.getValue());
        }
        
        public Builder<K, V> putAll(final Map<? extends K, ? extends V> map) {
            this.ensureCapacity(this.size + map.size());
            for (final Entry<? extends K, ? extends V> entry : map.entrySet()) {
                this.put(entry);
            }
            return this;
        }
        
        public ImmutableMap<K, V> build() {
            switch (this.size) {
                case 0: {
                    return ImmutableMap.of();
                }
                case 1: {
                    return ImmutableMap.of(this.entries[0].getKey(), this.entries[0].getValue());
                }
                default: {
                    return new RegularImmutableMap<K, V>(this.size, this.entries);
                }
            }
        }
    }
    
    private static final class MapViewOfValuesAsSingletonSets<K, V> extends ImmutableMap<K, ImmutableSet<V>>
    {
        private final ImmutableMap<K, V> delegate;
        
        MapViewOfValuesAsSingletonSets(final ImmutableMap<K, V> delegate) {
            this.delegate = Preconditions.checkNotNull(delegate);
        }
        
        @Override
        public int size() {
            return this.delegate.size();
        }
        
        @Override
        public boolean containsKey(@Nullable final Object key) {
            return this.delegate.containsKey(key);
        }
        
        @Override
        public ImmutableSet<V> get(@Nullable final Object key) {
            final V outerValue = this.delegate.get(key);
            return (outerValue == null) ? null : ImmutableSet.of(outerValue);
        }
        
        @Override
        boolean isPartialView() {
            return false;
        }
        
        @Override
        ImmutableSet<Entry<K, ImmutableSet<V>>> createEntrySet() {
            return (ImmutableSet<Entry<K, ImmutableSet<V>>>)new ImmutableMapEntrySet<K, ImmutableSet<V>>() {
                @Override
                ImmutableMap<K, ImmutableSet<V>> map() {
                    return (ImmutableMap<K, ImmutableSet<V>>)MapViewOfValuesAsSingletonSets.this;
                }
                
                @Override
                public UnmodifiableIterator<Entry<K, ImmutableSet<V>>> iterator() {
                    final Iterator<Entry<K, V>> backingIterator = (Iterator<Entry<K, V>>)MapViewOfValuesAsSingletonSets.this.delegate.entrySet().iterator();
                    return new UnmodifiableIterator<Entry<K, ImmutableSet<V>>>() {
                        @Override
                        public boolean hasNext() {
                            return backingIterator.hasNext();
                        }
                        
                        @Override
                        public Entry<K, ImmutableSet<V>> next() {
                            final Entry<K, V> backingEntry = backingIterator.next();
                            return new AbstractMapEntry<K, ImmutableSet<V>>() {
                                @Override
                                public K getKey() {
                                    return backingEntry.getKey();
                                }
                                
                                @Override
                                public ImmutableSet<V> getValue() {
                                    return ImmutableSet.of(backingEntry.getValue());
                                }
                            };
                        }
                    };
                }
            };
        }
    }
    
    static class SerializedForm implements Serializable
    {
        private final Object[] keys;
        private final Object[] values;
        private static final long serialVersionUID = 0L;
        
        SerializedForm(final ImmutableMap<?, ?> map) {
            this.keys = new Object[map.size()];
            this.values = new Object[map.size()];
            int i = 0;
            for (final Entry<?, ?> entry : map.entrySet()) {
                this.keys[i] = entry.getKey();
                this.values[i] = entry.getValue();
                ++i;
            }
        }
        
        Object readResolve() {
            final Builder<Object, Object> builder = new Builder<Object, Object>();
            return this.createMap(builder);
        }
        
        Object createMap(final Builder<Object, Object> builder) {
            for (int i = 0; i < this.keys.length; ++i) {
                builder.put(this.keys[i], this.values[i]);
            }
            return builder.build();
        }
    }
}
