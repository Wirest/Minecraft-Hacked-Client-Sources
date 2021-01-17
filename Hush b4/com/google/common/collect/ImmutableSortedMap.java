// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.NavigableSet;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.SortedMap;
import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;
import java.util.NavigableMap;

@GwtCompatible(serializable = true, emulated = true)
public abstract class ImmutableSortedMap<K, V> extends ImmutableSortedMapFauxverideShim<K, V> implements NavigableMap<K, V>
{
    private static final Comparator<Comparable> NATURAL_ORDER;
    private static final ImmutableSortedMap<Comparable, Object> NATURAL_EMPTY_MAP;
    private transient ImmutableSortedMap<K, V> descendingMap;
    private static final long serialVersionUID = 0L;
    
    static <K, V> ImmutableSortedMap<K, V> emptyMap(final Comparator<? super K> comparator) {
        if (Ordering.natural().equals(comparator)) {
            return of();
        }
        return new EmptyImmutableSortedMap<K, V>(comparator);
    }
    
    static <K, V> ImmutableSortedMap<K, V> fromSortedEntries(final Comparator<? super K> comparator, final int size, final Map.Entry<K, V>[] entries) {
        if (size == 0) {
            return emptyMap(comparator);
        }
        final ImmutableList.Builder<K> keyBuilder = ImmutableList.builder();
        final ImmutableList.Builder<V> valueBuilder = ImmutableList.builder();
        for (final Map.Entry<K, V> entry : entries) {
            keyBuilder.add(entry.getKey());
            valueBuilder.add(entry.getValue());
        }
        return new RegularImmutableSortedMap<K, V>(new RegularImmutableSortedSet<K>(keyBuilder.build(), comparator), valueBuilder.build());
    }
    
    static <K, V> ImmutableSortedMap<K, V> from(final ImmutableSortedSet<K> keySet, final ImmutableList<V> valueList) {
        if (keySet.isEmpty()) {
            return emptyMap(keySet.comparator());
        }
        return new RegularImmutableSortedMap<K, V>((RegularImmutableSortedSet)keySet, valueList);
    }
    
    public static <K, V> ImmutableSortedMap<K, V> of() {
        return (ImmutableSortedMap<K, V>)ImmutableSortedMap.NATURAL_EMPTY_MAP;
    }
    
    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(final K k1, final V v1) {
        return from((ImmutableSortedSet<K>)ImmutableSortedSet.of((K)k1), (ImmutableList<V>)ImmutableList.of((V)v1));
    }
    
    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(final K k1, final V v1, final K k2, final V v2) {
        return fromEntries(Ordering.natural(), false, 2, (Map.Entry<K, V>[])new Map.Entry[] { ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2) });
    }
    
    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3) {
        return fromEntries(Ordering.natural(), false, 3, (Map.Entry<K, V>[])new Map.Entry[] { ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3) });
    }
    
    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4) {
        return fromEntries(Ordering.natural(), false, 4, (Map.Entry<K, V>[])new Map.Entry[] { ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4) });
    }
    
    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5) {
        return fromEntries(Ordering.natural(), false, 5, (Map.Entry<K, V>[])new Map.Entry[] { ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4), ImmutableMap.entryOf(k5, v5) });
    }
    
    public static <K, V> ImmutableSortedMap<K, V> copyOf(final Map<? extends K, ? extends V> map) {
        final Ordering<K> naturalOrder = Ordering.natural();
        return copyOfInternal(map, (Comparator<? super K>)naturalOrder);
    }
    
    public static <K, V> ImmutableSortedMap<K, V> copyOf(final Map<? extends K, ? extends V> map, final Comparator<? super K> comparator) {
        return (ImmutableSortedMap<K, V>)copyOfInternal((Map<?, ?>)map, (Comparator<? super Object>)Preconditions.checkNotNull(comparator));
    }
    
    public static <K, V> ImmutableSortedMap<K, V> copyOfSorted(final SortedMap<K, ? extends V> map) {
        Comparator<? super K> comparator = map.comparator();
        if (comparator == null) {
            comparator = (Comparator<? super K>)ImmutableSortedMap.NATURAL_ORDER;
        }
        return copyOfInternal((Map<? extends K, ? extends V>)map, comparator);
    }
    
    private static <K, V> ImmutableSortedMap<K, V> copyOfInternal(final Map<? extends K, ? extends V> map, final Comparator<? super K> comparator) {
        boolean sameComparator = false;
        if (map instanceof SortedMap) {
            final SortedMap<?, ?> sortedMap = (SortedMap<?, ?>)(SortedMap)map;
            final Comparator<?> comparator2 = sortedMap.comparator();
            sameComparator = ((comparator2 == null) ? (comparator == ImmutableSortedMap.NATURAL_ORDER) : comparator.equals(comparator2));
        }
        if (sameComparator && map instanceof ImmutableSortedMap) {
            final ImmutableSortedMap<K, V> kvMap = (ImmutableSortedMap<K, V>)(ImmutableSortedMap)map;
            if (!kvMap.isPartialView()) {
                return kvMap;
            }
        }
        final Map.Entry<K, V>[] entries = map.entrySet().toArray(new Map.Entry[0]);
        return fromEntries(comparator, sameComparator, entries.length, entries);
    }
    
    static <K, V> ImmutableSortedMap<K, V> fromEntries(final Comparator<? super K> comparator, final boolean sameComparator, final int size, final Map.Entry<K, V>... entries) {
        for (int i = 0; i < size; ++i) {
            final Map.Entry<K, V> entry = entries[i];
            entries[i] = ImmutableMap.entryOf(entry.getKey(), entry.getValue());
        }
        if (!sameComparator) {
            sortEntries(comparator, size, entries);
            validateEntries(size, entries, comparator);
        }
        return fromSortedEntries(comparator, size, entries);
    }
    
    private static <K, V> void sortEntries(final Comparator<? super K> comparator, final int size, final Map.Entry<K, V>[] entries) {
        Arrays.sort(entries, 0, size, (Comparator<? super Map.Entry<K, V>>)Ordering.from(comparator).onKeys());
    }
    
    private static <K, V> void validateEntries(final int size, final Map.Entry<K, V>[] entries, final Comparator<? super K> comparator) {
        for (int i = 1; i < size; ++i) {
            ImmutableMap.checkNoConflict(comparator.compare((Object)entries[i - 1].getKey(), (Object)entries[i].getKey()) != 0, "key", entries[i - 1], entries[i]);
        }
    }
    
    public static <K extends Comparable<?>, V> Builder<K, V> naturalOrder() {
        return new Builder<K, V>(Ordering.natural());
    }
    
    public static <K, V> Builder<K, V> orderedBy(final Comparator<K> comparator) {
        return new Builder<K, V>(comparator);
    }
    
    public static <K extends Comparable<?>, V> Builder<K, V> reverseOrder() {
        return new Builder<K, V>(Ordering.natural().reverse());
    }
    
    ImmutableSortedMap() {
    }
    
    ImmutableSortedMap(final ImmutableSortedMap<K, V> descendingMap) {
        this.descendingMap = descendingMap;
    }
    
    @Override
    public int size() {
        return this.values().size();
    }
    
    @Override
    public boolean containsValue(@Nullable final Object value) {
        return this.values().contains(value);
    }
    
    @Override
    boolean isPartialView() {
        return this.keySet().isPartialView() || this.values().isPartialView();
    }
    
    @Override
    public ImmutableSet<Map.Entry<K, V>> entrySet() {
        return super.entrySet();
    }
    
    @Override
    public abstract ImmutableSortedSet<K> keySet();
    
    @Override
    public abstract ImmutableCollection<V> values();
    
    @Override
    public Comparator<? super K> comparator() {
        return this.keySet().comparator();
    }
    
    @Override
    public K firstKey() {
        return this.keySet().first();
    }
    
    @Override
    public K lastKey() {
        return this.keySet().last();
    }
    
    @Override
    public ImmutableSortedMap<K, V> headMap(final K toKey) {
        return this.headMap(toKey, false);
    }
    
    @Override
    public abstract ImmutableSortedMap<K, V> headMap(final K p0, final boolean p1);
    
    @Override
    public ImmutableSortedMap<K, V> subMap(final K fromKey, final K toKey) {
        return this.subMap(fromKey, true, toKey, false);
    }
    
    @Override
    public ImmutableSortedMap<K, V> subMap(final K fromKey, final boolean fromInclusive, final K toKey, final boolean toInclusive) {
        Preconditions.checkNotNull(fromKey);
        Preconditions.checkNotNull(toKey);
        Preconditions.checkArgument(this.comparator().compare((Object)fromKey, (Object)toKey) <= 0, "expected fromKey <= toKey but %s > %s", fromKey, toKey);
        return this.headMap(toKey, toInclusive).tailMap(fromKey, fromInclusive);
    }
    
    @Override
    public ImmutableSortedMap<K, V> tailMap(final K fromKey) {
        return this.tailMap(fromKey, true);
    }
    
    @Override
    public abstract ImmutableSortedMap<K, V> tailMap(final K p0, final boolean p1);
    
    @Override
    public Map.Entry<K, V> lowerEntry(final K key) {
        return this.headMap(key, false).lastEntry();
    }
    
    @Override
    public K lowerKey(final K key) {
        return Maps.keyOrNull((Map.Entry<K, ?>)this.lowerEntry((K)key));
    }
    
    @Override
    public Map.Entry<K, V> floorEntry(final K key) {
        return this.headMap(key, true).lastEntry();
    }
    
    @Override
    public K floorKey(final K key) {
        return Maps.keyOrNull((Map.Entry<K, ?>)this.floorEntry((K)key));
    }
    
    @Override
    public Map.Entry<K, V> ceilingEntry(final K key) {
        return this.tailMap(key, true).firstEntry();
    }
    
    @Override
    public K ceilingKey(final K key) {
        return Maps.keyOrNull((Map.Entry<K, ?>)this.ceilingEntry((K)key));
    }
    
    @Override
    public Map.Entry<K, V> higherEntry(final K key) {
        return this.tailMap(key, false).firstEntry();
    }
    
    @Override
    public K higherKey(final K key) {
        return Maps.keyOrNull((Map.Entry<K, ?>)this.higherEntry((K)key));
    }
    
    @Override
    public Map.Entry<K, V> firstEntry() {
        return this.isEmpty() ? null : this.entrySet().asList().get(0);
    }
    
    @Override
    public Map.Entry<K, V> lastEntry() {
        return this.isEmpty() ? null : this.entrySet().asList().get(this.size() - 1);
    }
    
    @Deprecated
    @Override
    public final Map.Entry<K, V> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public final Map.Entry<K, V> pollLastEntry() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ImmutableSortedMap<K, V> descendingMap() {
        ImmutableSortedMap<K, V> result = this.descendingMap;
        if (result == null) {
            final ImmutableSortedMap<K, V> descendingMap = this.createDescendingMap();
            this.descendingMap = descendingMap;
            result = descendingMap;
        }
        return result;
    }
    
    abstract ImmutableSortedMap<K, V> createDescendingMap();
    
    @Override
    public ImmutableSortedSet<K> navigableKeySet() {
        return this.keySet();
    }
    
    @Override
    public ImmutableSortedSet<K> descendingKeySet() {
        return this.keySet().descendingSet();
    }
    
    @Override
    Object writeReplace() {
        return new SerializedForm(this);
    }
    
    static {
        NATURAL_ORDER = Ordering.natural();
        NATURAL_EMPTY_MAP = new EmptyImmutableSortedMap<Comparable, Object>(ImmutableSortedMap.NATURAL_ORDER);
    }
    
    public static class Builder<K, V> extends ImmutableMap.Builder<K, V>
    {
        private final Comparator<? super K> comparator;
        
        public Builder(final Comparator<? super K> comparator) {
            this.comparator = Preconditions.checkNotNull(comparator);
        }
        
        @Override
        public Builder<K, V> put(final K key, final V value) {
            super.put(key, value);
            return this;
        }
        
        @Override
        public Builder<K, V> put(final Map.Entry<? extends K, ? extends V> entry) {
            super.put(entry);
            return this;
        }
        
        @Override
        public Builder<K, V> putAll(final Map<? extends K, ? extends V> map) {
            super.putAll(map);
            return this;
        }
        
        @Override
        public ImmutableSortedMap<K, V> build() {
            return ImmutableSortedMap.fromEntries(this.comparator, false, this.size, (Map.Entry<K, V>[])this.entries);
        }
    }
    
    private static class SerializedForm extends ImmutableMap.SerializedForm
    {
        private final Comparator<Object> comparator;
        private static final long serialVersionUID = 0L;
        
        SerializedForm(final ImmutableSortedMap<?, ?> sortedMap) {
            super(sortedMap);
            this.comparator = (Comparator<Object>)sortedMap.comparator();
        }
        
        @Override
        Object readResolve() {
            final ImmutableSortedMap.Builder<Object, Object> builder = new ImmutableSortedMap.Builder<Object, Object>(this.comparator);
            return this.createMap(builder);
        }
    }
}
