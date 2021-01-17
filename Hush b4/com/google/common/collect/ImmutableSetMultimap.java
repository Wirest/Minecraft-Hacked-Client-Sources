// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.List;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Arrays;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import com.google.common.base.Objects;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Collection;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import com.google.common.annotations.GwtIncompatible;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
public class ImmutableSetMultimap<K, V> extends ImmutableMultimap<K, V> implements SetMultimap<K, V>
{
    private final transient ImmutableSet<V> emptySet;
    private transient ImmutableSetMultimap<V, K> inverse;
    private transient ImmutableSet<Map.Entry<K, V>> entries;
    @GwtIncompatible("not needed in emulated source.")
    private static final long serialVersionUID = 0L;
    
    public static <K, V> ImmutableSetMultimap<K, V> of() {
        return (ImmutableSetMultimap<K, V>)EmptyImmutableSetMultimap.INSTANCE;
    }
    
    public static <K, V> ImmutableSetMultimap<K, V> of(final K k1, final V v1) {
        final Builder<K, V> builder = builder();
        builder.put(k1, v1);
        return builder.build();
    }
    
    public static <K, V> ImmutableSetMultimap<K, V> of(final K k1, final V v1, final K k2, final V v2) {
        final Builder<K, V> builder = builder();
        builder.put(k1, v1);
        builder.put(k2, v2);
        return builder.build();
    }
    
    public static <K, V> ImmutableSetMultimap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3) {
        final Builder<K, V> builder = builder();
        builder.put(k1, v1);
        builder.put(k2, v2);
        builder.put(k3, v3);
        return builder.build();
    }
    
    public static <K, V> ImmutableSetMultimap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4) {
        final Builder<K, V> builder = builder();
        builder.put(k1, v1);
        builder.put(k2, v2);
        builder.put(k3, v3);
        builder.put(k4, v4);
        return builder.build();
    }
    
    public static <K, V> ImmutableSetMultimap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5) {
        final Builder<K, V> builder = builder();
        builder.put(k1, v1);
        builder.put(k2, v2);
        builder.put(k3, v3);
        builder.put(k4, v4);
        builder.put(k5, v5);
        return builder.build();
    }
    
    public static <K, V> Builder<K, V> builder() {
        return new Builder<K, V>();
    }
    
    public static <K, V> ImmutableSetMultimap<K, V> copyOf(final Multimap<? extends K, ? extends V> multimap) {
        return copyOf(multimap, (Comparator<? super V>)null);
    }
    
    private static <K, V> ImmutableSetMultimap<K, V> copyOf(final Multimap<? extends K, ? extends V> multimap, final Comparator<? super V> valueComparator) {
        Preconditions.checkNotNull(multimap);
        if (multimap.isEmpty() && valueComparator == null) {
            return of();
        }
        if (multimap instanceof ImmutableSetMultimap) {
            final ImmutableSetMultimap<K, V> kvMultimap = (ImmutableSetMultimap<K, V>)(ImmutableSetMultimap)multimap;
            if (!kvMultimap.isPartialView()) {
                return kvMultimap;
            }
        }
        final ImmutableMap.Builder<K, ImmutableSet<V>> builder = ImmutableMap.builder();
        int size = 0;
        for (final Map.Entry<? extends K, ? extends Collection<? extends V>> entry : multimap.asMap().entrySet()) {
            final K key = (K)entry.getKey();
            final Collection<? extends V> values = (Collection<? extends V>)entry.getValue();
            final ImmutableSet<V> set = valueSet(valueComparator, values);
            if (!set.isEmpty()) {
                builder.put(key, set);
                size += set.size();
            }
        }
        return new ImmutableSetMultimap<K, V>(builder.build(), size, valueComparator);
    }
    
    ImmutableSetMultimap(final ImmutableMap<K, ImmutableSet<V>> map, final int size, @Nullable final Comparator<? super V> valueComparator) {
        super((ImmutableMap<K, ? extends ImmutableCollection<Object>>)map, size);
        this.emptySet = emptySet(valueComparator);
    }
    
    @Override
    public ImmutableSet<V> get(@Nullable final K key) {
        final ImmutableSet<V> set = (ImmutableSet<V>)this.map.get(key);
        return Objects.firstNonNull(set, this.emptySet);
    }
    
    @Override
    public ImmutableSetMultimap<V, K> inverse() {
        final ImmutableSetMultimap<V, K> result = this.inverse;
        return (result == null) ? (this.inverse = this.invert()) : result;
    }
    
    private ImmutableSetMultimap<V, K> invert() {
        final Builder<V, K> builder = builder();
        for (final Map.Entry<K, V> entry : this.entries()) {
            builder.put(entry.getValue(), entry.getKey());
        }
        final ImmutableSetMultimap<V, K> invertedMultimap = builder.build();
        invertedMultimap.inverse = (ImmutableSetMultimap<V, K>)this;
        return invertedMultimap;
    }
    
    @Deprecated
    @Override
    public ImmutableSet<V> removeAll(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public ImmutableSet<V> replaceValues(final K key, final Iterable<? extends V> values) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ImmutableSet<Map.Entry<K, V>> entries() {
        final ImmutableSet<Map.Entry<K, V>> result = this.entries;
        return (result == null) ? (this.entries = (ImmutableSet<Map.Entry<K, V>>)new EntrySet((ImmutableSetMultimap<Object, Object>)this)) : result;
    }
    
    private static <V> ImmutableSet<V> valueSet(@Nullable final Comparator<? super V> valueComparator, final Collection<? extends V> values) {
        return (ImmutableSet<V>)((valueComparator == null) ? ImmutableSet.copyOf((Collection<?>)values) : ImmutableSortedSet.copyOf((Comparator<? super Object>)valueComparator, (Collection<?>)values));
    }
    
    private static <V> ImmutableSet<V> emptySet(@Nullable final Comparator<? super V> valueComparator) {
        return (ImmutableSet<V>)((valueComparator == null) ? ImmutableSet.of() : ImmutableSortedSet.emptySet((Comparator<? super Object>)valueComparator));
    }
    
    @GwtIncompatible("java.io.ObjectOutputStream")
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(this.valueComparator());
        Serialization.writeMultimap((Multimap<Object, Object>)this, stream);
    }
    
    @Nullable
    Comparator<? super V> valueComparator() {
        return (this.emptySet instanceof ImmutableSortedSet) ? ((ImmutableSortedSet)this.emptySet).comparator() : null;
    }
    
    @GwtIncompatible("java.io.ObjectInputStream")
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        final Comparator<Object> valueComparator = (Comparator<Object>)stream.readObject();
        final int keyCount = stream.readInt();
        if (keyCount < 0) {
            throw new InvalidObjectException("Invalid key count " + keyCount);
        }
        final ImmutableMap.Builder<Object, ImmutableSet<Object>> builder = ImmutableMap.builder();
        int tmpSize = 0;
        for (int i = 0; i < keyCount; ++i) {
            final Object key = stream.readObject();
            final int valueCount = stream.readInt();
            if (valueCount <= 0) {
                throw new InvalidObjectException("Invalid value count " + valueCount);
            }
            final Object[] array = new Object[valueCount];
            for (int j = 0; j < valueCount; ++j) {
                array[j] = stream.readObject();
            }
            final ImmutableSet<Object> valueSet = valueSet((Comparator<? super Object>)valueComparator, (Collection<?>)Arrays.asList(array));
            if (valueSet.size() != array.length) {
                throw new InvalidObjectException("Duplicate key-value pairs exist for key " + key);
            }
            builder.put(key, valueSet);
            tmpSize += valueCount;
        }
        ImmutableMap<Object, ImmutableSet<Object>> tmpMap;
        try {
            tmpMap = builder.build();
        }
        catch (IllegalArgumentException e) {
            throw (InvalidObjectException)new InvalidObjectException(e.getMessage()).initCause(e);
        }
        FieldSettersHolder.MAP_FIELD_SETTER.set(this, tmpMap);
        FieldSettersHolder.SIZE_FIELD_SETTER.set(this, tmpSize);
        FieldSettersHolder.EMPTY_SET_FIELD_SETTER.set(this, emptySet(valueComparator));
    }
    
    private static class BuilderMultimap<K, V> extends AbstractMapBasedMultimap<K, V>
    {
        private static final long serialVersionUID = 0L;
        
        BuilderMultimap() {
            super(new LinkedHashMap());
        }
        
        @Override
        Collection<V> createCollection() {
            return (Collection<V>)Sets.newLinkedHashSet();
        }
    }
    
    public static final class Builder<K, V> extends ImmutableMultimap.Builder<K, V>
    {
        public Builder() {
            this.builderMultimap = (Multimap<K, V>)new ImmutableSetMultimap.BuilderMultimap<Object, Object>();
        }
        
        @Override
        public Builder<K, V> put(final K key, final V value) {
            this.builderMultimap.put(Preconditions.checkNotNull(key), Preconditions.checkNotNull(value));
            return this;
        }
        
        @Override
        public Builder<K, V> put(final Map.Entry<? extends K, ? extends V> entry) {
            this.builderMultimap.put(Preconditions.checkNotNull((K)entry.getKey()), Preconditions.checkNotNull((V)entry.getValue()));
            return this;
        }
        
        @Override
        public Builder<K, V> putAll(final K key, final Iterable<? extends V> values) {
            final Collection<V> collection = this.builderMultimap.get(Preconditions.checkNotNull(key));
            for (final V value : values) {
                collection.add(Preconditions.checkNotNull(value));
            }
            return this;
        }
        
        @Override
        public Builder<K, V> putAll(final K key, final V... values) {
            return this.putAll(key, (Iterable<? extends V>)Arrays.asList(values));
        }
        
        @Override
        public Builder<K, V> putAll(final Multimap<? extends K, ? extends V> multimap) {
            for (final Map.Entry<? extends K, ? extends Collection<? extends V>> entry : multimap.asMap().entrySet()) {
                this.putAll(entry.getKey(), (Iterable<? extends V>)entry.getValue());
            }
            return this;
        }
        
        @Override
        public Builder<K, V> orderKeysBy(final Comparator<? super K> keyComparator) {
            this.keyComparator = (Comparator<? super K>)Preconditions.checkNotNull((Comparator<? super K>)keyComparator);
            return this;
        }
        
        @Override
        public Builder<K, V> orderValuesBy(final Comparator<? super V> valueComparator) {
            super.orderValuesBy(valueComparator);
            return this;
        }
        
        @Override
        public ImmutableSetMultimap<K, V> build() {
            if (this.keyComparator != null) {
                final Multimap<K, V> sortedCopy = new ImmutableSetMultimap.BuilderMultimap<K, V>();
                final List<Map.Entry<K, Collection<V>>> entries = (List<Map.Entry<K, Collection<V>>>)Lists.newArrayList((Iterable<?>)this.builderMultimap.asMap().entrySet());
                Collections.sort(entries, (Comparator<? super Map.Entry<K, Collection<V>>>)Ordering.from(this.keyComparator).onKeys());
                for (final Map.Entry<K, Collection<V>> entry : entries) {
                    sortedCopy.putAll(entry.getKey(), (Iterable<? extends V>)entry.getValue());
                }
                this.builderMultimap = sortedCopy;
            }
            return (ImmutableSetMultimap<K, V>)copyOf((Multimap<?, ?>)this.builderMultimap, (Comparator<? super Object>)this.valueComparator);
        }
    }
    
    private static final class EntrySet<K, V> extends ImmutableSet<Map.Entry<K, V>>
    {
        private final transient ImmutableSetMultimap<K, V> multimap;
        
        EntrySet(final ImmutableSetMultimap<K, V> multimap) {
            this.multimap = multimap;
        }
        
        @Override
        public boolean contains(@Nullable final Object object) {
            if (object instanceof Map.Entry) {
                final Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
                return this.multimap.containsEntry(entry.getKey(), entry.getValue());
            }
            return false;
        }
        
        @Override
        public int size() {
            return this.multimap.size();
        }
        
        @Override
        public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
            return this.multimap.entryIterator();
        }
        
        @Override
        boolean isPartialView() {
            return false;
        }
    }
}
