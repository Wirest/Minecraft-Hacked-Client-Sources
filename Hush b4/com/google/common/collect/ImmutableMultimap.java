// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import java.util.Collections;
import java.util.List;
import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;
import java.util.Collection;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(emulated = true)
public abstract class ImmutableMultimap<K, V> extends AbstractMultimap<K, V> implements Serializable
{
    final transient ImmutableMap<K, ? extends ImmutableCollection<V>> map;
    final transient int size;
    private static final long serialVersionUID = 0L;
    
    public static <K, V> ImmutableMultimap<K, V> of() {
        return (ImmutableMultimap<K, V>)ImmutableListMultimap.of();
    }
    
    public static <K, V> ImmutableMultimap<K, V> of(final K k1, final V v1) {
        return ImmutableListMultimap.of(k1, v1);
    }
    
    public static <K, V> ImmutableMultimap<K, V> of(final K k1, final V v1, final K k2, final V v2) {
        return ImmutableListMultimap.of(k1, v1, k2, v2);
    }
    
    public static <K, V> ImmutableMultimap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3) {
        return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3);
    }
    
    public static <K, V> ImmutableMultimap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4) {
        return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4);
    }
    
    public static <K, V> ImmutableMultimap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5) {
        return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
    }
    
    public static <K, V> Builder<K, V> builder() {
        return new Builder<K, V>();
    }
    
    public static <K, V> ImmutableMultimap<K, V> copyOf(final Multimap<? extends K, ? extends V> multimap) {
        if (multimap instanceof ImmutableMultimap) {
            final ImmutableMultimap<K, V> kvMultimap = (ImmutableMultimap<K, V>)(ImmutableMultimap)multimap;
            if (!kvMultimap.isPartialView()) {
                return kvMultimap;
            }
        }
        return (ImmutableMultimap<K, V>)ImmutableListMultimap.copyOf((Multimap<?, ?>)multimap);
    }
    
    ImmutableMultimap(final ImmutableMap<K, ? extends ImmutableCollection<V>> map, final int size) {
        this.map = map;
        this.size = size;
    }
    
    @Deprecated
    @Override
    public ImmutableCollection<V> removeAll(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public ImmutableCollection<V> replaceValues(final K key, final Iterable<? extends V> values) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public abstract ImmutableCollection<V> get(final K p0);
    
    public abstract ImmutableMultimap<V, K> inverse();
    
    @Deprecated
    @Override
    public boolean put(final K key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public boolean putAll(final K key, final Iterable<? extends V> values) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public boolean putAll(final Multimap<? extends K, ? extends V> multimap) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public boolean remove(final Object key, final Object value) {
        throw new UnsupportedOperationException();
    }
    
    boolean isPartialView() {
        return this.map.isPartialView();
    }
    
    @Override
    public boolean containsKey(@Nullable final Object key) {
        return this.map.containsKey(key);
    }
    
    @Override
    public boolean containsValue(@Nullable final Object value) {
        return value != null && super.containsValue(value);
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public ImmutableSet<K> keySet() {
        return this.map.keySet();
    }
    
    @Override
    public ImmutableMap<K, Collection<V>> asMap() {
        return (ImmutableMap<K, Collection<V>>)this.map;
    }
    
    @Override
    Map<K, Collection<V>> createAsMap() {
        throw new AssertionError((Object)"should never be called");
    }
    
    @Override
    public ImmutableCollection<Map.Entry<K, V>> entries() {
        return (ImmutableCollection<Map.Entry<K, V>>)(ImmutableCollection)super.entries();
    }
    
    @Override
    ImmutableCollection<Map.Entry<K, V>> createEntries() {
        return (ImmutableCollection<Map.Entry<K, V>>)new EntryCollection((ImmutableMultimap<Object, Object>)this);
    }
    
    @Override
    UnmodifiableIterator<Map.Entry<K, V>> entryIterator() {
        return new Itr<Map.Entry<K, V>>() {
            @Override
            Map.Entry<K, V> output(final K key, final V value) {
                return Maps.immutableEntry(key, value);
            }
        };
    }
    
    @Override
    public ImmutableMultiset<K> keys() {
        return (ImmutableMultiset<K>)(ImmutableMultiset)super.keys();
    }
    
    @Override
    ImmutableMultiset<K> createKeys() {
        return new Keys();
    }
    
    @Override
    public ImmutableCollection<V> values() {
        return (ImmutableCollection<V>)(ImmutableCollection)super.values();
    }
    
    @Override
    ImmutableCollection<V> createValues() {
        return (ImmutableCollection<V>)new Values((ImmutableMultimap<Object, Object>)this);
    }
    
    @Override
    UnmodifiableIterator<V> valueIterator() {
        return new Itr<V>() {
            @Override
            V output(final K key, final V value) {
                return value;
            }
        };
    }
    
    private static class BuilderMultimap<K, V> extends AbstractMapBasedMultimap<K, V>
    {
        private static final long serialVersionUID = 0L;
        
        BuilderMultimap() {
            super(new LinkedHashMap());
        }
        
        @Override
        Collection<V> createCollection() {
            return (Collection<V>)Lists.newArrayList();
        }
    }
    
    public static class Builder<K, V>
    {
        Multimap<K, V> builderMultimap;
        Comparator<? super K> keyComparator;
        Comparator<? super V> valueComparator;
        
        public Builder() {
            this.builderMultimap = new BuilderMultimap<K, V>();
        }
        
        public Builder<K, V> put(final K key, final V value) {
            CollectPreconditions.checkEntryNotNull(key, value);
            this.builderMultimap.put(key, value);
            return this;
        }
        
        public Builder<K, V> put(final Map.Entry<? extends K, ? extends V> entry) {
            return (Builder<K, V>)this.put(entry.getKey(), entry.getValue());
        }
        
        public Builder<K, V> putAll(final K key, final Iterable<? extends V> values) {
            if (key == null) {
                throw new NullPointerException("null key in entry: null=" + Iterables.toString(values));
            }
            final Collection<V> valueList = this.builderMultimap.get(key);
            for (final V value : values) {
                CollectPreconditions.checkEntryNotNull(key, value);
                valueList.add(value);
            }
            return this;
        }
        
        public Builder<K, V> putAll(final K key, final V... values) {
            return this.putAll(key, (Iterable<? extends V>)Arrays.asList(values));
        }
        
        public Builder<K, V> putAll(final Multimap<? extends K, ? extends V> multimap) {
            for (final Map.Entry<? extends K, ? extends Collection<? extends V>> entry : multimap.asMap().entrySet()) {
                this.putAll(entry.getKey(), (Iterable<? extends V>)entry.getValue());
            }
            return this;
        }
        
        public Builder<K, V> orderKeysBy(final Comparator<? super K> keyComparator) {
            this.keyComparator = Preconditions.checkNotNull(keyComparator);
            return this;
        }
        
        public Builder<K, V> orderValuesBy(final Comparator<? super V> valueComparator) {
            this.valueComparator = Preconditions.checkNotNull(valueComparator);
            return this;
        }
        
        public ImmutableMultimap<K, V> build() {
            if (this.valueComparator != null) {
                for (final Collection<V> values : this.builderMultimap.asMap().values()) {
                    final List<V> list = (List<V>)(List)values;
                    Collections.sort(list, this.valueComparator);
                }
            }
            if (this.keyComparator != null) {
                final Multimap<K, V> sortedCopy = new BuilderMultimap<K, V>();
                final List<Map.Entry<K, Collection<V>>> entries = (List<Map.Entry<K, Collection<V>>>)Lists.newArrayList((Iterable<?>)this.builderMultimap.asMap().entrySet());
                Collections.sort(entries, (Comparator<? super Map.Entry<K, Collection<V>>>)Ordering.from(this.keyComparator).onKeys());
                for (final Map.Entry<K, Collection<V>> entry : entries) {
                    sortedCopy.putAll(entry.getKey(), (Iterable<? extends V>)entry.getValue());
                }
                this.builderMultimap = sortedCopy;
            }
            return ImmutableMultimap.copyOf((Multimap<? extends K, ? extends V>)this.builderMultimap);
        }
    }
    
    @GwtIncompatible("java serialization is not supported")
    static class FieldSettersHolder
    {
        static final Serialization.FieldSetter<ImmutableMultimap> MAP_FIELD_SETTER;
        static final Serialization.FieldSetter<ImmutableMultimap> SIZE_FIELD_SETTER;
        static final Serialization.FieldSetter<ImmutableSetMultimap> EMPTY_SET_FIELD_SETTER;
        
        static {
            MAP_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "map");
            SIZE_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "size");
            EMPTY_SET_FIELD_SETTER = Serialization.getFieldSetter(ImmutableSetMultimap.class, "emptySet");
        }
    }
    
    private static class EntryCollection<K, V> extends ImmutableCollection<Map.Entry<K, V>>
    {
        final ImmutableMultimap<K, V> multimap;
        private static final long serialVersionUID = 0L;
        
        EntryCollection(final ImmutableMultimap<K, V> multimap) {
            this.multimap = multimap;
        }
        
        @Override
        public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
            return this.multimap.entryIterator();
        }
        
        @Override
        boolean isPartialView() {
            return this.multimap.isPartialView();
        }
        
        @Override
        public int size() {
            return this.multimap.size();
        }
        
        @Override
        public boolean contains(final Object object) {
            if (object instanceof Map.Entry) {
                final Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
                return this.multimap.containsEntry(entry.getKey(), entry.getValue());
            }
            return false;
        }
    }
    
    private abstract class Itr<T> extends UnmodifiableIterator<T>
    {
        final Iterator<Map.Entry<K, Collection<V>>> mapIterator;
        K key;
        Iterator<V> valueIterator;
        
        private Itr() {
            this.mapIterator = ImmutableMultimap.this.asMap().entrySet().iterator();
            this.key = null;
            this.valueIterator = (Iterator<V>)Iterators.emptyIterator();
        }
        
        abstract T output(final K p0, final V p1);
        
        @Override
        public boolean hasNext() {
            return this.mapIterator.hasNext() || this.valueIterator.hasNext();
        }
        
        @Override
        public T next() {
            if (!this.valueIterator.hasNext()) {
                final Map.Entry<K, Collection<V>> mapEntry = this.mapIterator.next();
                this.key = mapEntry.getKey();
                this.valueIterator = mapEntry.getValue().iterator();
            }
            return this.output(this.key, this.valueIterator.next());
        }
    }
    
    class Keys extends ImmutableMultiset<K>
    {
        @Override
        public boolean contains(@Nullable final Object object) {
            return ImmutableMultimap.this.containsKey(object);
        }
        
        @Override
        public int count(@Nullable final Object element) {
            final Collection<V> values = (Collection<V>)ImmutableMultimap.this.map.get(element);
            return (values == null) ? 0 : values.size();
        }
        
        @Override
        public Set<K> elementSet() {
            return ImmutableMultimap.this.keySet();
        }
        
        @Override
        public int size() {
            return ImmutableMultimap.this.size();
        }
        
        @Override
        Multiset.Entry<K> getEntry(final int index) {
            final Map.Entry<K, ? extends Collection<V>> entry = ImmutableMultimap.this.map.entrySet().asList().get(index);
            return Multisets.immutableEntry(entry.getKey(), ((Collection)entry.getValue()).size());
        }
        
        @Override
        boolean isPartialView() {
            return true;
        }
    }
    
    private static final class Values<K, V> extends ImmutableCollection<V>
    {
        private final transient ImmutableMultimap<K, V> multimap;
        private static final long serialVersionUID = 0L;
        
        Values(final ImmutableMultimap<K, V> multimap) {
            this.multimap = multimap;
        }
        
        @Override
        public boolean contains(@Nullable final Object object) {
            return this.multimap.containsValue(object);
        }
        
        @Override
        public UnmodifiableIterator<V> iterator() {
            return this.multimap.valueIterator();
        }
        
        @GwtIncompatible("not present in emulated superclass")
        @Override
        int copyIntoArray(final Object[] dst, int offset) {
            for (final ImmutableCollection<V> valueCollection : this.multimap.map.values()) {
                offset = valueCollection.copyIntoArray(dst, offset);
            }
            return offset;
        }
        
        @Override
        public int size() {
            return this.multimap.size();
        }
        
        @Override
        boolean isPartialView() {
            return true;
        }
    }
}
