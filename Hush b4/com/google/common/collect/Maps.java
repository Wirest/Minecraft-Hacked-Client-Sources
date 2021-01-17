// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.io.Serializable;
import com.google.common.base.Objects;
import com.google.common.base.Predicates;
import com.google.common.base.Predicate;
import com.google.common.base.Converter;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Collection;
import com.google.common.annotations.GwtIncompatible;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.Set;
import java.util.Collections;
import com.google.common.base.Equivalence;
import java.util.IdentityHashMap;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;
import java.util.LinkedHashMap;
import java.util.HashMap;
import com.google.common.annotations.Beta;
import java.util.EnumMap;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.Map;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class Maps
{
    static final Joiner.MapJoiner STANDARD_JOINER;
    
    private Maps() {
    }
    
    static <K> Function<Map.Entry<K, ?>, K> keyFunction() {
        return (Function<Map.Entry<K, ?>, K>)EntryFunction.KEY;
    }
    
    static <V> Function<Map.Entry<?, V>, V> valueFunction() {
        return (Function<Map.Entry<?, V>, V>)EntryFunction.VALUE;
    }
    
    static <K, V> Iterator<K> keyIterator(final Iterator<Map.Entry<K, V>> entryIterator) {
        return Iterators.transform(entryIterator, (Function<? super Map.Entry<K, V>, ? extends K>)keyFunction());
    }
    
    static <K, V> Iterator<V> valueIterator(final Iterator<Map.Entry<K, V>> entryIterator) {
        return Iterators.transform(entryIterator, (Function<? super Map.Entry<K, V>, ? extends V>)valueFunction());
    }
    
    static <K, V> UnmodifiableIterator<V> valueIterator(final UnmodifiableIterator<Map.Entry<K, V>> entryIterator) {
        return new UnmodifiableIterator<V>() {
            @Override
            public boolean hasNext() {
                return entryIterator.hasNext();
            }
            
            @Override
            public V next() {
                return ((Map.Entry)entryIterator.next()).getValue();
            }
        };
    }
    
    @GwtCompatible(serializable = true)
    @Beta
    public static <K extends Enum<K>, V> ImmutableMap<K, V> immutableEnumMap(final Map<K, ? extends V> map) {
        if (map instanceof ImmutableEnumMap) {
            final ImmutableEnumMap<K, V> result = (ImmutableEnumMap<K, V>)(ImmutableEnumMap)map;
            return result;
        }
        if (map.isEmpty()) {
            return ImmutableMap.of();
        }
        for (final Map.Entry<K, ? extends V> entry : map.entrySet()) {
            Preconditions.checkNotNull(entry.getKey());
            Preconditions.checkNotNull(entry.getValue());
        }
        return ImmutableEnumMap.asImmutable(new EnumMap<K, V>(map));
    }
    
    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<K, V>();
    }
    
    public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(final int expectedSize) {
        return new HashMap<K, V>(capacity(expectedSize));
    }
    
    static int capacity(final int expectedSize) {
        if (expectedSize < 3) {
            CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
            return expectedSize + 1;
        }
        if (expectedSize < 1073741824) {
            return expectedSize + expectedSize / 3;
        }
        return Integer.MAX_VALUE;
    }
    
    public static <K, V> HashMap<K, V> newHashMap(final Map<? extends K, ? extends V> map) {
        return new HashMap<K, V>(map);
    }
    
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        return new LinkedHashMap<K, V>();
    }
    
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(final Map<? extends K, ? extends V> map) {
        return new LinkedHashMap<K, V>(map);
    }
    
    public static <K, V> ConcurrentMap<K, V> newConcurrentMap() {
        return new MapMaker().makeMap();
    }
    
    public static <K extends Comparable, V> TreeMap<K, V> newTreeMap() {
        return new TreeMap<K, V>();
    }
    
    public static <K, V> TreeMap<K, V> newTreeMap(final SortedMap<K, ? extends V> map) {
        return new TreeMap<K, V>(map);
    }
    
    public static <C, K extends C, V> TreeMap<K, V> newTreeMap(@Nullable final Comparator<C> comparator) {
        return new TreeMap<K, V>(comparator);
    }
    
    public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(final Class<K> type) {
        return new EnumMap<K, V>(Preconditions.checkNotNull(type));
    }
    
    public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(final Map<K, ? extends V> map) {
        return new EnumMap<K, V>(map);
    }
    
    public static <K, V> IdentityHashMap<K, V> newIdentityHashMap() {
        return new IdentityHashMap<K, V>();
    }
    
    public static <K, V> MapDifference<K, V> difference(final Map<? extends K, ? extends V> left, final Map<? extends K, ? extends V> right) {
        if (left instanceof SortedMap) {
            final SortedMap<K, ? extends V> sortedLeft = (SortedMap<K, ? extends V>)(SortedMap)left;
            final SortedMapDifference<K, V> result = difference(sortedLeft, right);
            return result;
        }
        return difference(left, right, (Equivalence<? super V>)Equivalence.equals());
    }
    
    @Beta
    public static <K, V> MapDifference<K, V> difference(final Map<? extends K, ? extends V> left, final Map<? extends K, ? extends V> right, final Equivalence<? super V> valueEquivalence) {
        Preconditions.checkNotNull(valueEquivalence);
        final Map<K, V> onlyOnLeft = (Map<K, V>)newHashMap();
        final Map<K, V> onlyOnRight = new HashMap<K, V>(right);
        final Map<K, V> onBoth = (Map<K, V>)newHashMap();
        final Map<K, MapDifference.ValueDifference<V>> differences = (Map<K, MapDifference.ValueDifference<V>>)newHashMap();
        doDifference(left, right, valueEquivalence, onlyOnLeft, onlyOnRight, onBoth, differences);
        return new MapDifferenceImpl<K, V>(onlyOnLeft, onlyOnRight, onBoth, differences);
    }
    
    private static <K, V> void doDifference(final Map<? extends K, ? extends V> left, final Map<? extends K, ? extends V> right, final Equivalence<? super V> valueEquivalence, final Map<K, V> onlyOnLeft, final Map<K, V> onlyOnRight, final Map<K, V> onBoth, final Map<K, MapDifference.ValueDifference<V>> differences) {
        for (final Map.Entry<? extends K, ? extends V> entry : left.entrySet()) {
            final K leftKey = (K)entry.getKey();
            final V leftValue = (V)entry.getValue();
            if (right.containsKey(leftKey)) {
                final V rightValue = onlyOnRight.remove(leftKey);
                if (valueEquivalence.equivalent((Object)leftValue, (Object)rightValue)) {
                    onBoth.put(leftKey, leftValue);
                }
                else {
                    differences.put(leftKey, ValueDifferenceImpl.create(leftValue, rightValue));
                }
            }
            else {
                onlyOnLeft.put(leftKey, leftValue);
            }
        }
    }
    
    private static <K, V> Map<K, V> unmodifiableMap(final Map<K, V> map) {
        if (map instanceof SortedMap) {
            return (Map<K, V>)Collections.unmodifiableSortedMap((SortedMap<Object, ?>)(SortedMap)map);
        }
        return Collections.unmodifiableMap((Map<? extends K, ? extends V>)map);
    }
    
    public static <K, V> SortedMapDifference<K, V> difference(final SortedMap<K, ? extends V> left, final Map<? extends K, ? extends V> right) {
        Preconditions.checkNotNull(left);
        Preconditions.checkNotNull(right);
        final Comparator<? super K> comparator = orNaturalOrder((Comparator<? super Object>)left.comparator());
        final SortedMap<K, V> onlyOnLeft = (SortedMap<K, V>)newTreeMap(comparator);
        final SortedMap<K, V> onlyOnRight = (SortedMap<K, V>)newTreeMap(comparator);
        onlyOnRight.putAll((Map<?, ?>)right);
        final SortedMap<K, V> onBoth = (SortedMap<K, V>)newTreeMap(comparator);
        final SortedMap<K, MapDifference.ValueDifference<V>> differences = (SortedMap<K, MapDifference.ValueDifference<V>>)newTreeMap(comparator);
        doDifference((Map<? extends K, ? extends V>)left, right, Equivalence.equals(), onlyOnLeft, onlyOnRight, onBoth, differences);
        return new SortedMapDifferenceImpl<K, V>(onlyOnLeft, onlyOnRight, onBoth, differences);
    }
    
    static <E> Comparator<? super E> orNaturalOrder(@Nullable final Comparator<? super E> comparator) {
        if (comparator != null) {
            return comparator;
        }
        return (Comparator<? super E>)Ordering.natural();
    }
    
    @Beta
    public static <K, V> Map<K, V> asMap(final Set<K> set, final Function<? super K, V> function) {
        if (set instanceof SortedSet) {
            return (Map<K, V>)asMap((SortedSet<Object>)(SortedSet)set, (Function<? super Object, V>)function);
        }
        return new AsMapView<K, V>(set, function);
    }
    
    @Beta
    public static <K, V> SortedMap<K, V> asMap(final SortedSet<K> set, final Function<? super K, V> function) {
        return Platform.mapsAsMapSortedSet(set, function);
    }
    
    static <K, V> SortedMap<K, V> asMapSortedIgnoreNavigable(final SortedSet<K> set, final Function<? super K, V> function) {
        return new SortedAsMapView<K, V>(set, function);
    }
    
    @Beta
    @GwtIncompatible("NavigableMap")
    public static <K, V> NavigableMap<K, V> asMap(final NavigableSet<K> set, final Function<? super K, V> function) {
        return new NavigableAsMapView<K, V>(set, function);
    }
    
    static <K, V> Iterator<Map.Entry<K, V>> asMapEntryIterator(final Set<K> set, final Function<? super K, V> function) {
        return new TransformedIterator<K, Map.Entry<K, V>>(set.iterator()) {
            @Override
            Map.Entry<K, V> transform(final K key) {
                return Maps.immutableEntry(key, function.apply(key));
            }
        };
    }
    
    private static <E> Set<E> removeOnlySet(final Set<E> set) {
        return new ForwardingSet<E>() {
            @Override
            protected Set<E> delegate() {
                return set;
            }
            
            @Override
            public boolean add(final E element) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public boolean addAll(final Collection<? extends E> es) {
                throw new UnsupportedOperationException();
            }
        };
    }
    
    private static <E> SortedSet<E> removeOnlySortedSet(final SortedSet<E> set) {
        return new ForwardingSortedSet<E>() {
            @Override
            protected SortedSet<E> delegate() {
                return set;
            }
            
            @Override
            public boolean add(final E element) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public boolean addAll(final Collection<? extends E> es) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public SortedSet<E> headSet(final E toElement) {
                return (SortedSet<E>)removeOnlySortedSet((SortedSet<Object>)super.headSet(toElement));
            }
            
            @Override
            public SortedSet<E> subSet(final E fromElement, final E toElement) {
                return (SortedSet<E>)removeOnlySortedSet((SortedSet<Object>)super.subSet(fromElement, toElement));
            }
            
            @Override
            public SortedSet<E> tailSet(final E fromElement) {
                return (SortedSet<E>)removeOnlySortedSet((SortedSet<Object>)super.tailSet(fromElement));
            }
        };
    }
    
    @GwtIncompatible("NavigableSet")
    private static <E> NavigableSet<E> removeOnlyNavigableSet(final NavigableSet<E> set) {
        return new ForwardingNavigableSet<E>() {
            @Override
            protected NavigableSet<E> delegate() {
                return set;
            }
            
            @Override
            public boolean add(final E element) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public boolean addAll(final Collection<? extends E> es) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public SortedSet<E> headSet(final E toElement) {
                return (SortedSet<E>)removeOnlySortedSet((SortedSet<Object>)super.headSet(toElement));
            }
            
            @Override
            public SortedSet<E> subSet(final E fromElement, final E toElement) {
                return (SortedSet<E>)removeOnlySortedSet((SortedSet<Object>)super.subSet(fromElement, toElement));
            }
            
            @Override
            public SortedSet<E> tailSet(final E fromElement) {
                return (SortedSet<E>)removeOnlySortedSet((SortedSet<Object>)super.tailSet(fromElement));
            }
            
            @Override
            public NavigableSet<E> headSet(final E toElement, final boolean inclusive) {
                return (NavigableSet<E>)removeOnlyNavigableSet((NavigableSet<Object>)super.headSet(toElement, inclusive));
            }
            
            @Override
            public NavigableSet<E> tailSet(final E fromElement, final boolean inclusive) {
                return (NavigableSet<E>)removeOnlyNavigableSet((NavigableSet<Object>)super.tailSet(fromElement, inclusive));
            }
            
            @Override
            public NavigableSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
                return (NavigableSet<E>)removeOnlyNavigableSet((NavigableSet<Object>)super.subSet(fromElement, fromInclusive, toElement, toInclusive));
            }
            
            @Override
            public NavigableSet<E> descendingSet() {
                return (NavigableSet<E>)removeOnlyNavigableSet((NavigableSet<Object>)super.descendingSet());
            }
        };
    }
    
    @Beta
    public static <K, V> ImmutableMap<K, V> toMap(final Iterable<K> keys, final Function<? super K, V> valueFunction) {
        return toMap(keys.iterator(), valueFunction);
    }
    
    @Beta
    public static <K, V> ImmutableMap<K, V> toMap(final Iterator<K> keys, final Function<? super K, V> valueFunction) {
        Preconditions.checkNotNull(valueFunction);
        final Map<K, V> builder = (Map<K, V>)newLinkedHashMap();
        while (keys.hasNext()) {
            final K key = keys.next();
            builder.put(key, valueFunction.apply((Object)key));
        }
        return ImmutableMap.copyOf((Map<? extends K, ? extends V>)builder);
    }
    
    public static <K, V> ImmutableMap<K, V> uniqueIndex(final Iterable<V> values, final Function<? super V, K> keyFunction) {
        return uniqueIndex(values.iterator(), keyFunction);
    }
    
    public static <K, V> ImmutableMap<K, V> uniqueIndex(final Iterator<V> values, final Function<? super V, K> keyFunction) {
        Preconditions.checkNotNull(keyFunction);
        final ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
        while (values.hasNext()) {
            final V value = values.next();
            builder.put(keyFunction.apply((Object)value), value);
        }
        return builder.build();
    }
    
    @GwtIncompatible("java.util.Properties")
    public static ImmutableMap<String, String> fromProperties(final Properties properties) {
        final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        final Enumeration<?> e = properties.propertyNames();
        while (e.hasMoreElements()) {
            final String key = (String)e.nextElement();
            builder.put(key, properties.getProperty(key));
        }
        return builder.build();
    }
    
    @GwtCompatible(serializable = true)
    public static <K, V> Map.Entry<K, V> immutableEntry(@Nullable final K key, @Nullable final V value) {
        return new ImmutableEntry<K, V>(key, value);
    }
    
    static <K, V> Set<Map.Entry<K, V>> unmodifiableEntrySet(final Set<Map.Entry<K, V>> entrySet) {
        return new UnmodifiableEntrySet<K, V>(Collections.unmodifiableSet((Set<? extends Map.Entry<K, V>>)entrySet));
    }
    
    static <K, V> Map.Entry<K, V> unmodifiableEntry(final Map.Entry<? extends K, ? extends V> entry) {
        Preconditions.checkNotNull(entry);
        return new AbstractMapEntry<K, V>() {
            @Override
            public K getKey() {
                return entry.getKey();
            }
            
            @Override
            public V getValue() {
                return entry.getValue();
            }
        };
    }
    
    @Beta
    public static <A, B> Converter<A, B> asConverter(final BiMap<A, B> bimap) {
        return new BiMapConverter<A, B>(bimap);
    }
    
    public static <K, V> BiMap<K, V> synchronizedBiMap(final BiMap<K, V> bimap) {
        return Synchronized.biMap(bimap, null);
    }
    
    public static <K, V> BiMap<K, V> unmodifiableBiMap(final BiMap<? extends K, ? extends V> bimap) {
        return new UnmodifiableBiMap<K, V>(bimap, null);
    }
    
    public static <K, V1, V2> Map<K, V2> transformValues(final Map<K, V1> fromMap, final Function<? super V1, V2> function) {
        return transformEntries(fromMap, (EntryTransformer<? super K, ? super V1, V2>)asEntryTransformer((Function<? super V1, V2>)function));
    }
    
    public static <K, V1, V2> SortedMap<K, V2> transformValues(final SortedMap<K, V1> fromMap, final Function<? super V1, V2> function) {
        return transformEntries(fromMap, (EntryTransformer<? super K, ? super V1, V2>)asEntryTransformer((Function<? super V1, V2>)function));
    }
    
    @GwtIncompatible("NavigableMap")
    public static <K, V1, V2> NavigableMap<K, V2> transformValues(final NavigableMap<K, V1> fromMap, final Function<? super V1, V2> function) {
        return transformEntries(fromMap, (EntryTransformer<? super K, ? super V1, V2>)asEntryTransformer((Function<? super V1, V2>)function));
    }
    
    public static <K, V1, V2> Map<K, V2> transformEntries(final Map<K, V1> fromMap, final EntryTransformer<? super K, ? super V1, V2> transformer) {
        if (fromMap instanceof SortedMap) {
            return (Map<K, V2>)transformEntries((SortedMap<Object, Object>)(SortedMap)fromMap, (EntryTransformer<? super Object, ? super Object, V2>)transformer);
        }
        return (Map<K, V2>)new TransformedEntriesMap((Map<Object, Object>)fromMap, (EntryTransformer<? super Object, ? super Object, Object>)transformer);
    }
    
    public static <K, V1, V2> SortedMap<K, V2> transformEntries(final SortedMap<K, V1> fromMap, final EntryTransformer<? super K, ? super V1, V2> transformer) {
        return Platform.mapsTransformEntriesSortedMap(fromMap, transformer);
    }
    
    @GwtIncompatible("NavigableMap")
    public static <K, V1, V2> NavigableMap<K, V2> transformEntries(final NavigableMap<K, V1> fromMap, final EntryTransformer<? super K, ? super V1, V2> transformer) {
        return new TransformedEntriesNavigableMap<K, Object, V2>(fromMap, transformer);
    }
    
    static <K, V1, V2> SortedMap<K, V2> transformEntriesIgnoreNavigable(final SortedMap<K, V1> fromMap, final EntryTransformer<? super K, ? super V1, V2> transformer) {
        return new TransformedEntriesSortedMap<K, Object, V2>(fromMap, transformer);
    }
    
    static <K, V1, V2> EntryTransformer<K, V1, V2> asEntryTransformer(final Function<? super V1, V2> function) {
        Preconditions.checkNotNull(function);
        return new EntryTransformer<K, V1, V2>() {
            @Override
            public V2 transformEntry(final K key, final V1 value) {
                return function.apply(value);
            }
        };
    }
    
    static <K, V1, V2> Function<V1, V2> asValueToValueFunction(final EntryTransformer<? super K, V1, V2> transformer, final K key) {
        Preconditions.checkNotNull(transformer);
        return new Function<V1, V2>() {
            @Override
            public V2 apply(@Nullable final V1 v1) {
                return transformer.transformEntry(key, v1);
            }
        };
    }
    
    static <K, V1, V2> Function<Map.Entry<K, V1>, V2> asEntryToValueFunction(final EntryTransformer<? super K, ? super V1, V2> transformer) {
        Preconditions.checkNotNull(transformer);
        return new Function<Map.Entry<K, V1>, V2>() {
            @Override
            public V2 apply(final Map.Entry<K, V1> entry) {
                return transformer.transformEntry(entry.getKey(), entry.getValue());
            }
        };
    }
    
    static <V2, K, V1> Map.Entry<K, V2> transformEntry(final EntryTransformer<? super K, ? super V1, V2> transformer, final Map.Entry<K, V1> entry) {
        Preconditions.checkNotNull(transformer);
        Preconditions.checkNotNull(entry);
        return new AbstractMapEntry<K, V2>() {
            @Override
            public K getKey() {
                return entry.getKey();
            }
            
            @Override
            public V2 getValue() {
                return transformer.transformEntry(entry.getKey(), entry.getValue());
            }
        };
    }
    
    static <K, V1, V2> Function<Map.Entry<K, V1>, Map.Entry<K, V2>> asEntryToEntryFunction(final EntryTransformer<? super K, ? super V1, V2> transformer) {
        Preconditions.checkNotNull(transformer);
        return new Function<Map.Entry<K, V1>, Map.Entry<K, V2>>() {
            @Override
            public Map.Entry<K, V2> apply(final Map.Entry<K, V1> entry) {
                return Maps.transformEntry(transformer, entry);
            }
        };
    }
    
    static <K> Predicate<Map.Entry<K, ?>> keyPredicateOnEntries(final Predicate<? super K> keyPredicate) {
        return Predicates.compose(keyPredicate, (Function<Map.Entry<K, ?>, ? extends K>)keyFunction());
    }
    
    static <V> Predicate<Map.Entry<?, V>> valuePredicateOnEntries(final Predicate<? super V> valuePredicate) {
        return Predicates.compose(valuePredicate, (Function<Map.Entry<?, V>, ? extends V>)valueFunction());
    }
    
    public static <K, V> Map<K, V> filterKeys(final Map<K, V> unfiltered, final Predicate<? super K> keyPredicate) {
        if (unfiltered instanceof SortedMap) {
            return (Map<K, V>)filterKeys((SortedMap<Object, Object>)(SortedMap)unfiltered, (Predicate<? super Object>)keyPredicate);
        }
        if (unfiltered instanceof BiMap) {
            return (Map<K, V>)filterKeys((BiMap<Object, Object>)(BiMap)unfiltered, (Predicate<? super Object>)keyPredicate);
        }
        Preconditions.checkNotNull(keyPredicate);
        final Predicate<Map.Entry<K, ?>> entryPredicate = keyPredicateOnEntries(keyPredicate);
        return (unfiltered instanceof AbstractFilteredMap) ? filterFiltered((AbstractFilteredMap<K, V>)(AbstractFilteredMap)unfiltered, entryPredicate) : new FilteredKeyMap<K, V>(Preconditions.checkNotNull(unfiltered), keyPredicate, entryPredicate);
    }
    
    public static <K, V> SortedMap<K, V> filterKeys(final SortedMap<K, V> unfiltered, final Predicate<? super K> keyPredicate) {
        return (SortedMap<K, V>)filterEntries((SortedMap<Object, Object>)unfiltered, keyPredicateOnEntries((Predicate<? super Object>)keyPredicate));
    }
    
    @GwtIncompatible("NavigableMap")
    public static <K, V> NavigableMap<K, V> filterKeys(final NavigableMap<K, V> unfiltered, final Predicate<? super K> keyPredicate) {
        return (NavigableMap<K, V>)filterEntries((NavigableMap<Object, Object>)unfiltered, keyPredicateOnEntries((Predicate<? super Object>)keyPredicate));
    }
    
    public static <K, V> BiMap<K, V> filterKeys(final BiMap<K, V> unfiltered, final Predicate<? super K> keyPredicate) {
        Preconditions.checkNotNull(keyPredicate);
        return (BiMap<K, V>)filterEntries((BiMap<Object, Object>)unfiltered, keyPredicateOnEntries((Predicate<? super Object>)keyPredicate));
    }
    
    public static <K, V> Map<K, V> filterValues(final Map<K, V> unfiltered, final Predicate<? super V> valuePredicate) {
        if (unfiltered instanceof SortedMap) {
            return (Map<K, V>)filterValues((SortedMap<Object, Object>)(SortedMap)unfiltered, (Predicate<? super Object>)valuePredicate);
        }
        if (unfiltered instanceof BiMap) {
            return (Map<K, V>)filterValues((BiMap<Object, Object>)(BiMap)unfiltered, (Predicate<? super Object>)valuePredicate);
        }
        return (Map<K, V>)filterEntries((Map<Object, Object>)unfiltered, valuePredicateOnEntries((Predicate<? super Object>)valuePredicate));
    }
    
    public static <K, V> SortedMap<K, V> filterValues(final SortedMap<K, V> unfiltered, final Predicate<? super V> valuePredicate) {
        return (SortedMap<K, V>)filterEntries((SortedMap<Object, Object>)unfiltered, valuePredicateOnEntries((Predicate<? super Object>)valuePredicate));
    }
    
    @GwtIncompatible("NavigableMap")
    public static <K, V> NavigableMap<K, V> filterValues(final NavigableMap<K, V> unfiltered, final Predicate<? super V> valuePredicate) {
        return (NavigableMap<K, V>)filterEntries((NavigableMap<Object, Object>)unfiltered, valuePredicateOnEntries((Predicate<? super Object>)valuePredicate));
    }
    
    public static <K, V> BiMap<K, V> filterValues(final BiMap<K, V> unfiltered, final Predicate<? super V> valuePredicate) {
        return (BiMap<K, V>)filterEntries((BiMap<Object, Object>)unfiltered, valuePredicateOnEntries((Predicate<? super Object>)valuePredicate));
    }
    
    public static <K, V> Map<K, V> filterEntries(final Map<K, V> unfiltered, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
        if (unfiltered instanceof SortedMap) {
            return (Map<K, V>)filterEntries((SortedMap<Object, Object>)(SortedMap)unfiltered, (Predicate<? super Map.Entry<Object, Object>>)entryPredicate);
        }
        if (unfiltered instanceof BiMap) {
            return (Map<K, V>)filterEntries((BiMap<Object, Object>)(BiMap)unfiltered, (Predicate<? super Map.Entry<Object, Object>>)entryPredicate);
        }
        Preconditions.checkNotNull(entryPredicate);
        return (unfiltered instanceof AbstractFilteredMap) ? filterFiltered((AbstractFilteredMap<K, V>)(AbstractFilteredMap)unfiltered, entryPredicate) : new FilteredEntryMap<K, V>(Preconditions.checkNotNull(unfiltered), entryPredicate);
    }
    
    public static <K, V> SortedMap<K, V> filterEntries(final SortedMap<K, V> unfiltered, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
        return Platform.mapsFilterSortedMap(unfiltered, entryPredicate);
    }
    
    static <K, V> SortedMap<K, V> filterSortedIgnoreNavigable(final SortedMap<K, V> unfiltered, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
        Preconditions.checkNotNull(entryPredicate);
        return (unfiltered instanceof FilteredEntrySortedMap) ? filterFiltered((FilteredEntrySortedMap<K, V>)(FilteredEntrySortedMap)unfiltered, entryPredicate) : new FilteredEntrySortedMap<K, V>(Preconditions.checkNotNull(unfiltered), entryPredicate);
    }
    
    @GwtIncompatible("NavigableMap")
    public static <K, V> NavigableMap<K, V> filterEntries(final NavigableMap<K, V> unfiltered, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
        Preconditions.checkNotNull(entryPredicate);
        return (unfiltered instanceof FilteredEntryNavigableMap) ? filterFiltered((FilteredEntryNavigableMap<K, V>)(FilteredEntryNavigableMap)unfiltered, entryPredicate) : new FilteredEntryNavigableMap<K, V>(Preconditions.checkNotNull(unfiltered), entryPredicate);
    }
    
    public static <K, V> BiMap<K, V> filterEntries(final BiMap<K, V> unfiltered, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
        Preconditions.checkNotNull(unfiltered);
        Preconditions.checkNotNull(entryPredicate);
        return (unfiltered instanceof FilteredEntryBiMap) ? filterFiltered((FilteredEntryBiMap<K, V>)(FilteredEntryBiMap)unfiltered, entryPredicate) : new FilteredEntryBiMap<K, V>(unfiltered, entryPredicate);
    }
    
    private static <K, V> Map<K, V> filterFiltered(final AbstractFilteredMap<K, V> map, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
        return new FilteredEntryMap<K, V>(map.unfiltered, Predicates.and(map.predicate, entryPredicate));
    }
    
    private static <K, V> SortedMap<K, V> filterFiltered(final FilteredEntrySortedMap<K, V> map, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
        final Predicate<Map.Entry<K, V>> predicate = Predicates.and(map.predicate, entryPredicate);
        return new FilteredEntrySortedMap<K, V>(map.sortedMap(), predicate);
    }
    
    @GwtIncompatible("NavigableMap")
    private static <K, V> NavigableMap<K, V> filterFiltered(final FilteredEntryNavigableMap<K, V> map, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
        final Predicate<Map.Entry<K, V>> predicate = Predicates.and((Predicate<? super Map.Entry<K, V>>)((FilteredEntryNavigableMap<Object, Object>)map).entryPredicate, entryPredicate);
        return new FilteredEntryNavigableMap<K, V>(((FilteredEntryNavigableMap<Object, Object>)map).unfiltered, predicate);
    }
    
    private static <K, V> BiMap<K, V> filterFiltered(final FilteredEntryBiMap<K, V> map, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
        final Predicate<Map.Entry<K, V>> predicate = Predicates.and(map.predicate, entryPredicate);
        return new FilteredEntryBiMap<K, V>(map.unfiltered(), predicate);
    }
    
    @GwtIncompatible("NavigableMap")
    public static <K, V> NavigableMap<K, V> unmodifiableNavigableMap(final NavigableMap<K, V> map) {
        Preconditions.checkNotNull(map);
        if (map instanceof UnmodifiableNavigableMap) {
            return map;
        }
        return new UnmodifiableNavigableMap<K, V>(map);
    }
    
    @Nullable
    private static <K, V> Map.Entry<K, V> unmodifiableOrNull(@Nullable final Map.Entry<K, V> entry) {
        return (Map.Entry<K, V>)((entry == null) ? null : unmodifiableEntry((Map.Entry<?, ?>)entry));
    }
    
    @GwtIncompatible("NavigableMap")
    public static <K, V> NavigableMap<K, V> synchronizedNavigableMap(final NavigableMap<K, V> navigableMap) {
        return Synchronized.navigableMap(navigableMap);
    }
    
    static <V> V safeGet(final Map<?, V> map, @Nullable final Object key) {
        Preconditions.checkNotNull(map);
        try {
            return map.get(key);
        }
        catch (ClassCastException e) {
            return null;
        }
        catch (NullPointerException e2) {
            return null;
        }
    }
    
    static boolean safeContainsKey(final Map<?, ?> map, final Object key) {
        Preconditions.checkNotNull(map);
        try {
            return map.containsKey(key);
        }
        catch (ClassCastException e) {
            return false;
        }
        catch (NullPointerException e2) {
            return false;
        }
    }
    
    static <V> V safeRemove(final Map<?, V> map, final Object key) {
        Preconditions.checkNotNull(map);
        try {
            return map.remove(key);
        }
        catch (ClassCastException e) {
            return null;
        }
        catch (NullPointerException e2) {
            return null;
        }
    }
    
    static boolean containsKeyImpl(final Map<?, ?> map, @Nullable final Object key) {
        return Iterators.contains(keyIterator(map.entrySet().iterator()), key);
    }
    
    static boolean containsValueImpl(final Map<?, ?> map, @Nullable final Object value) {
        return Iterators.contains(valueIterator(map.entrySet().iterator()), value);
    }
    
    static <K, V> boolean containsEntryImpl(final Collection<Map.Entry<K, V>> c, final Object o) {
        return o instanceof Map.Entry && c.contains(unmodifiableEntry((Map.Entry<?, ?>)o));
    }
    
    static <K, V> boolean removeEntryImpl(final Collection<Map.Entry<K, V>> c, final Object o) {
        return o instanceof Map.Entry && c.remove(unmodifiableEntry((Map.Entry<?, ?>)o));
    }
    
    static boolean equalsImpl(final Map<?, ?> map, final Object object) {
        if (map == object) {
            return true;
        }
        if (object instanceof Map) {
            final Map<?, ?> o = (Map<?, ?>)object;
            return map.entrySet().equals(o.entrySet());
        }
        return false;
    }
    
    static String toStringImpl(final Map<?, ?> map) {
        final StringBuilder sb = Collections2.newStringBuilderForCollection(map.size()).append('{');
        Maps.STANDARD_JOINER.appendTo(sb, map);
        return sb.append('}').toString();
    }
    
    static <K, V> void putAllImpl(final Map<K, V> self, final Map<? extends K, ? extends V> map) {
        for (final Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            self.put((K)entry.getKey(), (V)entry.getValue());
        }
    }
    
    @Nullable
    static <K> K keyOrNull(@Nullable final Map.Entry<K, ?> entry) {
        return (entry == null) ? null : entry.getKey();
    }
    
    @Nullable
    static <V> V valueOrNull(@Nullable final Map.Entry<?, V> entry) {
        return (entry == null) ? null : entry.getValue();
    }
    
    static {
        STANDARD_JOINER = Collections2.STANDARD_JOINER.withKeyValueSeparator("=");
    }
    
    private enum EntryFunction implements Function<Map.Entry<?, ?>, Object>
    {
        KEY {
            @Nullable
            @Override
            public Object apply(final Map.Entry<?, ?> entry) {
                return entry.getKey();
            }
        }, 
        VALUE {
            @Nullable
            @Override
            public Object apply(final Map.Entry<?, ?> entry) {
                return entry.getValue();
            }
        };
    }
    
    static class MapDifferenceImpl<K, V> implements MapDifference<K, V>
    {
        final Map<K, V> onlyOnLeft;
        final Map<K, V> onlyOnRight;
        final Map<K, V> onBoth;
        final Map<K, ValueDifference<V>> differences;
        
        MapDifferenceImpl(final Map<K, V> onlyOnLeft, final Map<K, V> onlyOnRight, final Map<K, V> onBoth, final Map<K, ValueDifference<V>> differences) {
            this.onlyOnLeft = (Map<K, V>)unmodifiableMap((Map<Object, Object>)onlyOnLeft);
            this.onlyOnRight = (Map<K, V>)unmodifiableMap((Map<Object, Object>)onlyOnRight);
            this.onBoth = (Map<K, V>)unmodifiableMap((Map<Object, Object>)onBoth);
            this.differences = (Map<K, ValueDifference<V>>)unmodifiableMap((Map<Object, Object>)differences);
        }
        
        @Override
        public boolean areEqual() {
            return this.onlyOnLeft.isEmpty() && this.onlyOnRight.isEmpty() && this.differences.isEmpty();
        }
        
        @Override
        public Map<K, V> entriesOnlyOnLeft() {
            return this.onlyOnLeft;
        }
        
        @Override
        public Map<K, V> entriesOnlyOnRight() {
            return this.onlyOnRight;
        }
        
        @Override
        public Map<K, V> entriesInCommon() {
            return this.onBoth;
        }
        
        @Override
        public Map<K, ValueDifference<V>> entriesDiffering() {
            return this.differences;
        }
        
        @Override
        public boolean equals(final Object object) {
            if (object == this) {
                return true;
            }
            if (object instanceof MapDifference) {
                final MapDifference<?, ?> other = (MapDifference<?, ?>)object;
                return this.entriesOnlyOnLeft().equals(other.entriesOnlyOnLeft()) && this.entriesOnlyOnRight().equals(other.entriesOnlyOnRight()) && this.entriesInCommon().equals(other.entriesInCommon()) && this.entriesDiffering().equals(other.entriesDiffering());
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(this.entriesOnlyOnLeft(), this.entriesOnlyOnRight(), this.entriesInCommon(), this.entriesDiffering());
        }
        
        @Override
        public String toString() {
            if (this.areEqual()) {
                return "equal";
            }
            final StringBuilder result = new StringBuilder("not equal");
            if (!this.onlyOnLeft.isEmpty()) {
                result.append(": only on left=").append(this.onlyOnLeft);
            }
            if (!this.onlyOnRight.isEmpty()) {
                result.append(": only on right=").append(this.onlyOnRight);
            }
            if (!this.differences.isEmpty()) {
                result.append(": value differences=").append(this.differences);
            }
            return result.toString();
        }
    }
    
    static class ValueDifferenceImpl<V> implements MapDifference.ValueDifference<V>
    {
        private final V left;
        private final V right;
        
        static <V> MapDifference.ValueDifference<V> create(@Nullable final V left, @Nullable final V right) {
            return new ValueDifferenceImpl<V>(left, right);
        }
        
        private ValueDifferenceImpl(@Nullable final V left, @Nullable final V right) {
            this.left = left;
            this.right = right;
        }
        
        @Override
        public V leftValue() {
            return this.left;
        }
        
        @Override
        public V rightValue() {
            return this.right;
        }
        
        @Override
        public boolean equals(@Nullable final Object object) {
            if (object instanceof MapDifference.ValueDifference) {
                final MapDifference.ValueDifference<?> that = (MapDifference.ValueDifference<?>)object;
                return Objects.equal(this.left, that.leftValue()) && Objects.equal(this.right, that.rightValue());
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(this.left, this.right);
        }
        
        @Override
        public String toString() {
            return "(" + this.left + ", " + this.right + ")";
        }
    }
    
    static class SortedMapDifferenceImpl<K, V> extends MapDifferenceImpl<K, V> implements SortedMapDifference<K, V>
    {
        SortedMapDifferenceImpl(final SortedMap<K, V> onlyOnLeft, final SortedMap<K, V> onlyOnRight, final SortedMap<K, V> onBoth, final SortedMap<K, MapDifference.ValueDifference<V>> differences) {
            super(onlyOnLeft, onlyOnRight, onBoth, differences);
        }
        
        @Override
        public SortedMap<K, MapDifference.ValueDifference<V>> entriesDiffering() {
            return (SortedMap<K, MapDifference.ValueDifference<V>>)(SortedMap)super.entriesDiffering();
        }
        
        @Override
        public SortedMap<K, V> entriesInCommon() {
            return (SortedMap<K, V>)(SortedMap)super.entriesInCommon();
        }
        
        @Override
        public SortedMap<K, V> entriesOnlyOnLeft() {
            return (SortedMap<K, V>)(SortedMap)super.entriesOnlyOnLeft();
        }
        
        @Override
        public SortedMap<K, V> entriesOnlyOnRight() {
            return (SortedMap<K, V>)(SortedMap)super.entriesOnlyOnRight();
        }
    }
    
    private static class AsMapView<K, V> extends ImprovedAbstractMap<K, V>
    {
        private final Set<K> set;
        final Function<? super K, V> function;
        
        Set<K> backingSet() {
            return this.set;
        }
        
        AsMapView(final Set<K> set, final Function<? super K, V> function) {
            this.set = Preconditions.checkNotNull(set);
            this.function = Preconditions.checkNotNull(function);
        }
        
        public Set<K> createKeySet() {
            return (Set<K>)removeOnlySet((Set<Object>)this.backingSet());
        }
        
        @Override
        Collection<V> createValues() {
            return Collections2.transform(this.set, this.function);
        }
        
        @Override
        public int size() {
            return this.backingSet().size();
        }
        
        @Override
        public boolean containsKey(@Nullable final Object key) {
            return this.backingSet().contains(key);
        }
        
        @Override
        public V get(@Nullable final Object key) {
            if (Collections2.safeContains(this.backingSet(), key)) {
                final K k = (K)key;
                return this.function.apply((Object)k);
            }
            return null;
        }
        
        @Override
        public V remove(@Nullable final Object key) {
            if (this.backingSet().remove(key)) {
                final K k = (K)key;
                return this.function.apply((Object)k);
            }
            return null;
        }
        
        @Override
        public void clear() {
            this.backingSet().clear();
        }
        
        protected Set<Map.Entry<K, V>> createEntrySet() {
            return (Set<Map.Entry<K, V>>)new EntrySet<K, V>() {
                @Override
                Map<K, V> map() {
                    return (Map<K, V>)AsMapView.this;
                }
                
                @Override
                public Iterator<Map.Entry<K, V>> iterator() {
                    return Maps.asMapEntryIterator(AsMapView.this.backingSet(), AsMapView.this.function);
                }
            };
        }
    }
    
    private static class SortedAsMapView<K, V> extends AsMapView<K, V> implements SortedMap<K, V>
    {
        SortedAsMapView(final SortedSet<K> set, final Function<? super K, V> function) {
            super(set, function);
        }
        
        @Override
        SortedSet<K> backingSet() {
            return (SortedSet<K>)(SortedSet)super.backingSet();
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return this.backingSet().comparator();
        }
        
        @Override
        public Set<K> keySet() {
            return (Set<K>)removeOnlySortedSet((SortedSet<Object>)this.backingSet());
        }
        
        @Override
        public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
            return Maps.asMap(this.backingSet().subSet(fromKey, toKey), this.function);
        }
        
        @Override
        public SortedMap<K, V> headMap(final K toKey) {
            return Maps.asMap(this.backingSet().headSet(toKey), this.function);
        }
        
        @Override
        public SortedMap<K, V> tailMap(final K fromKey) {
            return Maps.asMap(this.backingSet().tailSet(fromKey), this.function);
        }
        
        @Override
        public K firstKey() {
            return this.backingSet().first();
        }
        
        @Override
        public K lastKey() {
            return this.backingSet().last();
        }
    }
    
    @GwtIncompatible("NavigableMap")
    private static final class NavigableAsMapView<K, V> extends AbstractNavigableMap<K, V>
    {
        private final NavigableSet<K> set;
        private final Function<? super K, V> function;
        
        NavigableAsMapView(final NavigableSet<K> ks, final Function<? super K, V> vFunction) {
            this.set = Preconditions.checkNotNull(ks);
            this.function = Preconditions.checkNotNull(vFunction);
        }
        
        @Override
        public NavigableMap<K, V> subMap(final K fromKey, final boolean fromInclusive, final K toKey, final boolean toInclusive) {
            return Maps.asMap(this.set.subSet(fromKey, fromInclusive, toKey, toInclusive), this.function);
        }
        
        @Override
        public NavigableMap<K, V> headMap(final K toKey, final boolean inclusive) {
            return Maps.asMap(this.set.headSet(toKey, inclusive), this.function);
        }
        
        @Override
        public NavigableMap<K, V> tailMap(final K fromKey, final boolean inclusive) {
            return Maps.asMap(this.set.tailSet(fromKey, inclusive), this.function);
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return this.set.comparator();
        }
        
        @Nullable
        @Override
        public V get(@Nullable final Object key) {
            if (Collections2.safeContains(this.set, key)) {
                final K k = (K)key;
                return this.function.apply((Object)k);
            }
            return null;
        }
        
        @Override
        public void clear() {
            this.set.clear();
        }
        
        @Override
        Iterator<Map.Entry<K, V>> entryIterator() {
            return Maps.asMapEntryIterator(this.set, this.function);
        }
        
        @Override
        Iterator<Map.Entry<K, V>> descendingEntryIterator() {
            return (Iterator<Map.Entry<K, V>>)this.descendingMap().entrySet().iterator();
        }
        
        @Override
        public NavigableSet<K> navigableKeySet() {
            return (NavigableSet<K>)removeOnlyNavigableSet((NavigableSet<Object>)this.set);
        }
        
        @Override
        public int size() {
            return this.set.size();
        }
        
        @Override
        public NavigableMap<K, V> descendingMap() {
            return Maps.asMap(this.set.descendingSet(), this.function);
        }
    }
    
    static class UnmodifiableEntries<K, V> extends ForwardingCollection<Map.Entry<K, V>>
    {
        private final Collection<Map.Entry<K, V>> entries;
        
        UnmodifiableEntries(final Collection<Map.Entry<K, V>> entries) {
            this.entries = entries;
        }
        
        @Override
        protected Collection<Map.Entry<K, V>> delegate() {
            return this.entries;
        }
        
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            final Iterator<Map.Entry<K, V>> delegate = super.iterator();
            return new UnmodifiableIterator<Map.Entry<K, V>>() {
                @Override
                public boolean hasNext() {
                    return delegate.hasNext();
                }
                
                @Override
                public Map.Entry<K, V> next() {
                    return Maps.unmodifiableEntry((Map.Entry<? extends K, ? extends V>)delegate.next());
                }
            };
        }
        
        @Override
        public Object[] toArray() {
            return this.standardToArray();
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            return this.standardToArray(array);
        }
    }
    
    static class UnmodifiableEntrySet<K, V> extends UnmodifiableEntries<K, V> implements Set<Map.Entry<K, V>>
    {
        UnmodifiableEntrySet(final Set<Map.Entry<K, V>> entries) {
            super(entries);
        }
        
        @Override
        public boolean equals(@Nullable final Object object) {
            return Sets.equalsImpl(this, object);
        }
        
        @Override
        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
    }
    
    private static final class BiMapConverter<A, B> extends Converter<A, B> implements Serializable
    {
        private final BiMap<A, B> bimap;
        private static final long serialVersionUID = 0L;
        
        BiMapConverter(final BiMap<A, B> bimap) {
            this.bimap = Preconditions.checkNotNull(bimap);
        }
        
        @Override
        protected B doForward(final A a) {
            return convert(this.bimap, a);
        }
        
        @Override
        protected A doBackward(final B b) {
            return convert(this.bimap.inverse(), b);
        }
        
        private static <X, Y> Y convert(final BiMap<X, Y> bimap, final X input) {
            final Y output = bimap.get(input);
            Preconditions.checkArgument(output != null, "No non-null mapping present for input: %s", input);
            return output;
        }
        
        @Override
        public boolean equals(@Nullable final Object object) {
            if (object instanceof BiMapConverter) {
                final BiMapConverter<?, ?> that = (BiMapConverter<?, ?>)object;
                return this.bimap.equals(that.bimap);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.bimap.hashCode();
        }
        
        @Override
        public String toString() {
            return "Maps.asConverter(" + this.bimap + ")";
        }
    }
    
    private static class UnmodifiableBiMap<K, V> extends ForwardingMap<K, V> implements BiMap<K, V>, Serializable
    {
        final Map<K, V> unmodifiableMap;
        final BiMap<? extends K, ? extends V> delegate;
        BiMap<V, K> inverse;
        transient Set<V> values;
        private static final long serialVersionUID = 0L;
        
        UnmodifiableBiMap(final BiMap<? extends K, ? extends V> delegate, @Nullable final BiMap<V, K> inverse) {
            this.unmodifiableMap = Collections.unmodifiableMap((Map<? extends K, ? extends V>)delegate);
            this.delegate = delegate;
            this.inverse = inverse;
        }
        
        @Override
        protected Map<K, V> delegate() {
            return this.unmodifiableMap;
        }
        
        @Override
        public V forcePut(final K key, final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public BiMap<V, K> inverse() {
            final BiMap<V, K> result = this.inverse;
            return (result == null) ? (this.inverse = (BiMap<V, K>)new UnmodifiableBiMap(this.delegate.inverse(), (BiMap<Object, Object>)this)) : result;
        }
        
        @Override
        public Set<V> values() {
            final Set<V> result = this.values;
            return (result == null) ? (this.values = Collections.unmodifiableSet(this.delegate.values())) : result;
        }
    }
    
    static class TransformedEntriesMap<K, V1, V2> extends ImprovedAbstractMap<K, V2>
    {
        final Map<K, V1> fromMap;
        final EntryTransformer<? super K, ? super V1, V2> transformer;
        
        TransformedEntriesMap(final Map<K, V1> fromMap, final EntryTransformer<? super K, ? super V1, V2> transformer) {
            this.fromMap = Preconditions.checkNotNull(fromMap);
            this.transformer = Preconditions.checkNotNull(transformer);
        }
        
        @Override
        public int size() {
            return this.fromMap.size();
        }
        
        @Override
        public boolean containsKey(final Object key) {
            return this.fromMap.containsKey(key);
        }
        
        @Override
        public V2 get(final Object key) {
            final V1 value = this.fromMap.get(key);
            return (value != null || this.fromMap.containsKey(key)) ? this.transformer.transformEntry((Object)key, (Object)value) : null;
        }
        
        @Override
        public V2 remove(final Object key) {
            return this.fromMap.containsKey(key) ? this.transformer.transformEntry((Object)key, (Object)this.fromMap.remove(key)) : null;
        }
        
        @Override
        public void clear() {
            this.fromMap.clear();
        }
        
        @Override
        public Set<K> keySet() {
            return this.fromMap.keySet();
        }
        
        protected Set<Map.Entry<K, V2>> createEntrySet() {
            return (Set<Map.Entry<K, V2>>)new EntrySet<K, V2>() {
                @Override
                Map<K, V2> map() {
                    return (Map<K, V2>)TransformedEntriesMap.this;
                }
                
                @Override
                public Iterator<Map.Entry<K, V2>> iterator() {
                    return Iterators.transform(TransformedEntriesMap.this.fromMap.entrySet().iterator(), (Function<? super Map.Entry<K, V1>, ? extends Map.Entry<K, V2>>)Maps.asEntryToEntryFunction((EntryTransformer<? super Object, ? super Object, V2>)TransformedEntriesMap.this.transformer));
                }
            };
        }
    }
    
    static class TransformedEntriesSortedMap<K, V1, V2> extends TransformedEntriesMap<K, V1, V2> implements SortedMap<K, V2>
    {
        protected SortedMap<K, V1> fromMap() {
            return (SortedMap<K, V1>)(SortedMap)this.fromMap;
        }
        
        TransformedEntriesSortedMap(final SortedMap<K, V1> fromMap, final EntryTransformer<? super K, ? super V1, V2> transformer) {
            super(fromMap, transformer);
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return this.fromMap().comparator();
        }
        
        @Override
        public K firstKey() {
            return this.fromMap().firstKey();
        }
        
        @Override
        public SortedMap<K, V2> headMap(final K toKey) {
            return Maps.transformEntries(this.fromMap().headMap(toKey), this.transformer);
        }
        
        @Override
        public K lastKey() {
            return this.fromMap().lastKey();
        }
        
        @Override
        public SortedMap<K, V2> subMap(final K fromKey, final K toKey) {
            return Maps.transformEntries(this.fromMap().subMap(fromKey, toKey), this.transformer);
        }
        
        @Override
        public SortedMap<K, V2> tailMap(final K fromKey) {
            return Maps.transformEntries(this.fromMap().tailMap(fromKey), this.transformer);
        }
    }
    
    @GwtIncompatible("NavigableMap")
    private static class TransformedEntriesNavigableMap<K, V1, V2> extends TransformedEntriesSortedMap<K, V1, V2> implements NavigableMap<K, V2>
    {
        TransformedEntriesNavigableMap(final NavigableMap<K, V1> fromMap, final EntryTransformer<? super K, ? super V1, V2> transformer) {
            super(fromMap, transformer);
        }
        
        @Override
        public Map.Entry<K, V2> ceilingEntry(final K key) {
            return this.transformEntry(this.fromMap().ceilingEntry(key));
        }
        
        @Override
        public K ceilingKey(final K key) {
            return this.fromMap().ceilingKey(key);
        }
        
        @Override
        public NavigableSet<K> descendingKeySet() {
            return this.fromMap().descendingKeySet();
        }
        
        @Override
        public NavigableMap<K, V2> descendingMap() {
            return Maps.transformEntries(this.fromMap().descendingMap(), this.transformer);
        }
        
        @Override
        public Map.Entry<K, V2> firstEntry() {
            return this.transformEntry(this.fromMap().firstEntry());
        }
        
        @Override
        public Map.Entry<K, V2> floorEntry(final K key) {
            return this.transformEntry(this.fromMap().floorEntry(key));
        }
        
        @Override
        public K floorKey(final K key) {
            return this.fromMap().floorKey(key);
        }
        
        @Override
        public NavigableMap<K, V2> headMap(final K toKey) {
            return this.headMap(toKey, false);
        }
        
        @Override
        public NavigableMap<K, V2> headMap(final K toKey, final boolean inclusive) {
            return Maps.transformEntries(this.fromMap().headMap(toKey, inclusive), this.transformer);
        }
        
        @Override
        public Map.Entry<K, V2> higherEntry(final K key) {
            return this.transformEntry(this.fromMap().higherEntry(key));
        }
        
        @Override
        public K higherKey(final K key) {
            return this.fromMap().higherKey(key);
        }
        
        @Override
        public Map.Entry<K, V2> lastEntry() {
            return this.transformEntry(this.fromMap().lastEntry());
        }
        
        @Override
        public Map.Entry<K, V2> lowerEntry(final K key) {
            return this.transformEntry(this.fromMap().lowerEntry(key));
        }
        
        @Override
        public K lowerKey(final K key) {
            return this.fromMap().lowerKey(key);
        }
        
        @Override
        public NavigableSet<K> navigableKeySet() {
            return this.fromMap().navigableKeySet();
        }
        
        @Override
        public Map.Entry<K, V2> pollFirstEntry() {
            return this.transformEntry(this.fromMap().pollFirstEntry());
        }
        
        @Override
        public Map.Entry<K, V2> pollLastEntry() {
            return this.transformEntry(this.fromMap().pollLastEntry());
        }
        
        @Override
        public NavigableMap<K, V2> subMap(final K fromKey, final boolean fromInclusive, final K toKey, final boolean toInclusive) {
            return Maps.transformEntries(this.fromMap().subMap(fromKey, fromInclusive, toKey, toInclusive), this.transformer);
        }
        
        @Override
        public NavigableMap<K, V2> subMap(final K fromKey, final K toKey) {
            return this.subMap(fromKey, true, toKey, false);
        }
        
        @Override
        public NavigableMap<K, V2> tailMap(final K fromKey) {
            return this.tailMap(fromKey, true);
        }
        
        @Override
        public NavigableMap<K, V2> tailMap(final K fromKey, final boolean inclusive) {
            return Maps.transformEntries(this.fromMap().tailMap(fromKey, inclusive), this.transformer);
        }
        
        @Nullable
        private Map.Entry<K, V2> transformEntry(@Nullable final Map.Entry<K, V1> entry) {
            return (entry == null) ? null : Maps.transformEntry(this.transformer, entry);
        }
        
        @Override
        protected NavigableMap<K, V1> fromMap() {
            return (NavigableMap<K, V1>)(NavigableMap)super.fromMap();
        }
    }
    
    private abstract static class AbstractFilteredMap<K, V> extends ImprovedAbstractMap<K, V>
    {
        final Map<K, V> unfiltered;
        final Predicate<? super Map.Entry<K, V>> predicate;
        
        AbstractFilteredMap(final Map<K, V> unfiltered, final Predicate<? super Map.Entry<K, V>> predicate) {
            this.unfiltered = unfiltered;
            this.predicate = predicate;
        }
        
        boolean apply(@Nullable final Object key, @Nullable final V value) {
            final K k = (K)key;
            return this.predicate.apply(Maps.immutableEntry(k, value));
        }
        
        @Override
        public V put(final K key, final V value) {
            Preconditions.checkArgument(this.apply(key, value));
            return this.unfiltered.put(key, value);
        }
        
        @Override
        public void putAll(final Map<? extends K, ? extends V> map) {
            for (final Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
                Preconditions.checkArgument(this.apply(entry.getKey(), entry.getValue()));
            }
            this.unfiltered.putAll(map);
        }
        
        @Override
        public boolean containsKey(final Object key) {
            return this.unfiltered.containsKey(key) && this.apply(key, this.unfiltered.get(key));
        }
        
        @Override
        public V get(final Object key) {
            final V value = this.unfiltered.get(key);
            return (value != null && this.apply(key, value)) ? value : null;
        }
        
        @Override
        public boolean isEmpty() {
            return this.entrySet().isEmpty();
        }
        
        @Override
        public V remove(final Object key) {
            return this.containsKey(key) ? this.unfiltered.remove(key) : null;
        }
        
        @Override
        Collection<V> createValues() {
            return (Collection<V>)new FilteredMapValues((Map<Object, Object>)this, (Map<Object, Object>)this.unfiltered, (Predicate<? super Map.Entry<Object, Object>>)this.predicate);
        }
    }
    
    private static final class FilteredMapValues<K, V> extends Values<K, V>
    {
        Map<K, V> unfiltered;
        Predicate<? super Map.Entry<K, V>> predicate;
        
        FilteredMapValues(final Map<K, V> filteredMap, final Map<K, V> unfiltered, final Predicate<? super Map.Entry<K, V>> predicate) {
            super(filteredMap);
            this.unfiltered = unfiltered;
            this.predicate = predicate;
        }
        
        @Override
        public boolean remove(final Object o) {
            return Iterables.removeFirstMatching(this.unfiltered.entrySet(), Predicates.and(this.predicate, (Predicate<? super Map.Entry<K, V>>)Maps.valuePredicateOnEntries(Predicates.equalTo(o)))) != null;
        }
        
        private boolean removeIf(final Predicate<? super V> valuePredicate) {
            return Iterables.removeIf(this.unfiltered.entrySet(), Predicates.and(this.predicate, (Predicate<? super Map.Entry<K, V>>)Maps.valuePredicateOnEntries((Predicate<? super Object>)valuePredicate)));
        }
        
        @Override
        public boolean removeAll(final Collection<?> collection) {
            return this.removeIf(Predicates.in((Collection<? extends V>)collection));
        }
        
        @Override
        public boolean retainAll(final Collection<?> collection) {
            return this.removeIf(Predicates.not(Predicates.in((Collection<? extends V>)collection)));
        }
        
        @Override
        public Object[] toArray() {
            return Lists.newArrayList(this.iterator()).toArray();
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            return Lists.newArrayList(this.iterator()).toArray(array);
        }
    }
    
    private static class FilteredKeyMap<K, V> extends AbstractFilteredMap<K, V>
    {
        Predicate<? super K> keyPredicate;
        
        FilteredKeyMap(final Map<K, V> unfiltered, final Predicate<? super K> keyPredicate, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
            super(unfiltered, entryPredicate);
            this.keyPredicate = keyPredicate;
        }
        
        protected Set<Map.Entry<K, V>> createEntrySet() {
            return Sets.filter(this.unfiltered.entrySet(), this.predicate);
        }
        
        @Override
        Set<K> createKeySet() {
            return Sets.filter(this.unfiltered.keySet(), this.keyPredicate);
        }
        
        @Override
        public boolean containsKey(final Object key) {
            return this.unfiltered.containsKey(key) && this.keyPredicate.apply((Object)key);
        }
    }
    
    static class FilteredEntryMap<K, V> extends AbstractFilteredMap<K, V>
    {
        final Set<Map.Entry<K, V>> filteredEntrySet;
        
        FilteredEntryMap(final Map<K, V> unfiltered, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
            super(unfiltered, entryPredicate);
            this.filteredEntrySet = Sets.filter(unfiltered.entrySet(), this.predicate);
        }
        
        protected Set<Map.Entry<K, V>> createEntrySet() {
            return new EntrySet();
        }
        
        @Override
        Set<K> createKeySet() {
            return (Set<K>)new KeySet();
        }
        
        private class EntrySet extends ForwardingSet<Map.Entry<K, V>>
        {
            @Override
            protected Set<Map.Entry<K, V>> delegate() {
                return FilteredEntryMap.this.filteredEntrySet;
            }
            
            @Override
            public Iterator<Map.Entry<K, V>> iterator() {
                return new TransformedIterator<Map.Entry<K, V>, Map.Entry<K, V>>(FilteredEntryMap.this.filteredEntrySet.iterator()) {
                    @Override
                    Map.Entry<K, V> transform(final Map.Entry<K, V> entry) {
                        return new ForwardingMapEntry<K, V>() {
                            @Override
                            protected Map.Entry<K, V> delegate() {
                                return entry;
                            }
                            
                            @Override
                            public V setValue(final V newValue) {
                                Preconditions.checkArgument(FilteredEntryMap.this.apply(((ForwardingMapEntry<Object, V>)this).getKey(), newValue));
                                return super.setValue(newValue);
                            }
                        };
                    }
                };
            }
        }
        
        class KeySet extends Maps.KeySet<K, V>
        {
            KeySet() {
                super(FilteredEntryMap.this);
            }
            
            @Override
            public boolean remove(final Object o) {
                if (FilteredEntryMap.this.containsKey(o)) {
                    FilteredEntryMap.this.unfiltered.remove(o);
                    return true;
                }
                return false;
            }
            
            private boolean removeIf(final Predicate<? super K> keyPredicate) {
                return Iterables.removeIf(FilteredEntryMap.this.unfiltered.entrySet(), Predicates.and(FilteredEntryMap.this.predicate, (Predicate<? super Map.Entry<K, V>>)Maps.keyPredicateOnEntries((Predicate<? super Object>)keyPredicate)));
            }
            
            @Override
            public boolean removeAll(final Collection<?> c) {
                return this.removeIf(Predicates.in((Collection<? extends K>)c));
            }
            
            @Override
            public boolean retainAll(final Collection<?> c) {
                return this.removeIf(Predicates.not(Predicates.in((Collection<? extends K>)c)));
            }
            
            @Override
            public Object[] toArray() {
                return Lists.newArrayList(((Maps.KeySet<?, V>)this).iterator()).toArray();
            }
            
            @Override
            public <T> T[] toArray(final T[] array) {
                return Lists.newArrayList(((Maps.KeySet<?, V>)this).iterator()).toArray(array);
            }
        }
    }
    
    private static class FilteredEntrySortedMap<K, V> extends FilteredEntryMap<K, V> implements SortedMap<K, V>
    {
        FilteredEntrySortedMap(final SortedMap<K, V> unfiltered, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
            super(unfiltered, entryPredicate);
        }
        
        SortedMap<K, V> sortedMap() {
            return (SortedMap<K, V>)(SortedMap)this.unfiltered;
        }
        
        @Override
        public SortedSet<K> keySet() {
            return (SortedSet<K>)(SortedSet)super.keySet();
        }
        
        @Override
        SortedSet<K> createKeySet() {
            return new SortedKeySet();
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return this.sortedMap().comparator();
        }
        
        @Override
        public K firstKey() {
            return this.keySet().iterator().next();
        }
        
        @Override
        public K lastKey() {
            SortedMap<K, V> headMap = this.sortedMap();
            K key;
            while (true) {
                key = headMap.lastKey();
                if (this.apply(key, this.unfiltered.get(key))) {
                    break;
                }
                headMap = this.sortedMap().headMap(key);
            }
            return key;
        }
        
        @Override
        public SortedMap<K, V> headMap(final K toKey) {
            return new FilteredEntrySortedMap((SortedMap<Object, Object>)this.sortedMap().headMap(toKey), (Predicate<? super Map.Entry<Object, Object>>)this.predicate);
        }
        
        @Override
        public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
            return new FilteredEntrySortedMap((SortedMap<Object, Object>)this.sortedMap().subMap(fromKey, toKey), (Predicate<? super Map.Entry<Object, Object>>)this.predicate);
        }
        
        @Override
        public SortedMap<K, V> tailMap(final K fromKey) {
            return new FilteredEntrySortedMap((SortedMap<Object, Object>)this.sortedMap().tailMap(fromKey), (Predicate<? super Map.Entry<Object, Object>>)this.predicate);
        }
        
        class SortedKeySet extends FilteredEntryMap.KeySet implements SortedSet<K>
        {
            @Override
            public Comparator<? super K> comparator() {
                return FilteredEntrySortedMap.this.sortedMap().comparator();
            }
            
            @Override
            public SortedSet<K> subSet(final K fromElement, final K toElement) {
                return (SortedSet<K>)(SortedSet)FilteredEntrySortedMap.this.subMap(fromElement, toElement).keySet();
            }
            
            @Override
            public SortedSet<K> headSet(final K toElement) {
                return (SortedSet<K>)(SortedSet)FilteredEntrySortedMap.this.headMap(toElement).keySet();
            }
            
            @Override
            public SortedSet<K> tailSet(final K fromElement) {
                return (SortedSet<K>)(SortedSet)FilteredEntrySortedMap.this.tailMap(fromElement).keySet();
            }
            
            @Override
            public K first() {
                return FilteredEntrySortedMap.this.firstKey();
            }
            
            @Override
            public K last() {
                return FilteredEntrySortedMap.this.lastKey();
            }
        }
    }
    
    @GwtIncompatible("NavigableMap")
    private static class FilteredEntryNavigableMap<K, V> extends AbstractNavigableMap<K, V>
    {
        private final NavigableMap<K, V> unfiltered;
        private final Predicate<? super Map.Entry<K, V>> entryPredicate;
        private final Map<K, V> filteredDelegate;
        
        FilteredEntryNavigableMap(final NavigableMap<K, V> unfiltered, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
            this.unfiltered = Preconditions.checkNotNull(unfiltered);
            this.entryPredicate = entryPredicate;
            this.filteredDelegate = new FilteredEntryMap<K, V>(unfiltered, entryPredicate);
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return this.unfiltered.comparator();
        }
        
        @Override
        public NavigableSet<K> navigableKeySet() {
            return new NavigableKeySet<K, V>(this) {
                @Override
                public boolean removeAll(final Collection<?> c) {
                    return Iterators.removeIf((Iterator<Object>)FilteredEntryNavigableMap.this.unfiltered.entrySet().iterator(), Predicates.and(FilteredEntryNavigableMap.this.entryPredicate, (Predicate<? super Object>)Maps.keyPredicateOnEntries(Predicates.in(c))));
                }
                
                @Override
                public boolean retainAll(final Collection<?> c) {
                    return Iterators.removeIf((Iterator<Object>)FilteredEntryNavigableMap.this.unfiltered.entrySet().iterator(), Predicates.and(FilteredEntryNavigableMap.this.entryPredicate, (Predicate<? super Object>)Maps.keyPredicateOnEntries(Predicates.not(Predicates.in((Collection<? extends K>)c)))));
                }
            };
        }
        
        @Override
        public Collection<V> values() {
            return (Collection<V>)new FilteredMapValues((Map<Object, Object>)this, (Map<Object, Object>)this.unfiltered, (Predicate<? super Map.Entry<Object, Object>>)this.entryPredicate);
        }
        
        @Override
        Iterator<Map.Entry<K, V>> entryIterator() {
            return (Iterator<Map.Entry<K, V>>)Iterators.filter(this.unfiltered.entrySet().iterator(), (Predicate<? super Map.Entry<Object, Object>>)this.entryPredicate);
        }
        
        @Override
        Iterator<Map.Entry<K, V>> descendingEntryIterator() {
            return (Iterator<Map.Entry<K, V>>)Iterators.filter(this.unfiltered.descendingMap().entrySet().iterator(), (Predicate<? super Map.Entry<Object, Object>>)this.entryPredicate);
        }
        
        @Override
        public int size() {
            return this.filteredDelegate.size();
        }
        
        @Nullable
        @Override
        public V get(@Nullable final Object key) {
            return this.filteredDelegate.get(key);
        }
        
        @Override
        public boolean containsKey(@Nullable final Object key) {
            return this.filteredDelegate.containsKey(key);
        }
        
        @Override
        public V put(final K key, final V value) {
            return this.filteredDelegate.put(key, value);
        }
        
        @Override
        public V remove(@Nullable final Object key) {
            return this.filteredDelegate.remove(key);
        }
        
        @Override
        public void putAll(final Map<? extends K, ? extends V> m) {
            this.filteredDelegate.putAll(m);
        }
        
        @Override
        public void clear() {
            this.filteredDelegate.clear();
        }
        
        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            return this.filteredDelegate.entrySet();
        }
        
        @Override
        public Map.Entry<K, V> pollFirstEntry() {
            return Iterables.removeFirstMatching((Iterable<Map.Entry<K, V>>)this.unfiltered.entrySet(), this.entryPredicate);
        }
        
        @Override
        public Map.Entry<K, V> pollLastEntry() {
            return Iterables.removeFirstMatching((Iterable<Map.Entry<K, V>>)this.unfiltered.descendingMap().entrySet(), this.entryPredicate);
        }
        
        @Override
        public NavigableMap<K, V> descendingMap() {
            return Maps.filterEntries(this.unfiltered.descendingMap(), this.entryPredicate);
        }
        
        @Override
        public NavigableMap<K, V> subMap(final K fromKey, final boolean fromInclusive, final K toKey, final boolean toInclusive) {
            return Maps.filterEntries(this.unfiltered.subMap(fromKey, fromInclusive, toKey, toInclusive), this.entryPredicate);
        }
        
        @Override
        public NavigableMap<K, V> headMap(final K toKey, final boolean inclusive) {
            return Maps.filterEntries(this.unfiltered.headMap(toKey, inclusive), this.entryPredicate);
        }
        
        @Override
        public NavigableMap<K, V> tailMap(final K fromKey, final boolean inclusive) {
            return Maps.filterEntries(this.unfiltered.tailMap(fromKey, inclusive), this.entryPredicate);
        }
    }
    
    static final class FilteredEntryBiMap<K, V> extends FilteredEntryMap<K, V> implements BiMap<K, V>
    {
        private final BiMap<V, K> inverse;
        
        private static <K, V> Predicate<Map.Entry<V, K>> inversePredicate(final Predicate<? super Map.Entry<K, V>> forwardPredicate) {
            return new Predicate<Map.Entry<V, K>>() {
                @Override
                public boolean apply(final Map.Entry<V, K> input) {
                    return forwardPredicate.apply(Maps.immutableEntry(input.getValue(), input.getKey()));
                }
            };
        }
        
        FilteredEntryBiMap(final BiMap<K, V> delegate, final Predicate<? super Map.Entry<K, V>> predicate) {
            super(delegate, predicate);
            this.inverse = (BiMap<V, K>)new FilteredEntryBiMap((BiMap<Object, Object>)delegate.inverse(), inversePredicate((Predicate<? super Map.Entry<Object, Object>>)predicate), (BiMap<Object, Object>)this);
        }
        
        private FilteredEntryBiMap(final BiMap<K, V> delegate, final Predicate<? super Map.Entry<K, V>> predicate, final BiMap<V, K> inverse) {
            super(delegate, predicate);
            this.inverse = inverse;
        }
        
        BiMap<K, V> unfiltered() {
            return (BiMap<K, V>)(BiMap)this.unfiltered;
        }
        
        @Override
        public V forcePut(@Nullable final K key, @Nullable final V value) {
            Preconditions.checkArgument(this.apply(key, value));
            return this.unfiltered().forcePut(key, value);
        }
        
        @Override
        public BiMap<V, K> inverse() {
            return this.inverse;
        }
        
        @Override
        public Set<V> values() {
            return this.inverse.keySet();
        }
    }
    
    @GwtIncompatible("NavigableMap")
    static class UnmodifiableNavigableMap<K, V> extends ForwardingSortedMap<K, V> implements NavigableMap<K, V>, Serializable
    {
        private final NavigableMap<K, V> delegate;
        private transient UnmodifiableNavigableMap<K, V> descendingMap;
        
        UnmodifiableNavigableMap(final NavigableMap<K, V> delegate) {
            this.delegate = delegate;
        }
        
        UnmodifiableNavigableMap(final NavigableMap<K, V> delegate, final UnmodifiableNavigableMap<K, V> descendingMap) {
            this.delegate = delegate;
            this.descendingMap = descendingMap;
        }
        
        @Override
        protected SortedMap<K, V> delegate() {
            return Collections.unmodifiableSortedMap((SortedMap<K, ? extends V>)this.delegate);
        }
        
        @Override
        public Map.Entry<K, V> lowerEntry(final K key) {
            return (Map.Entry<K, V>)unmodifiableOrNull((Map.Entry<Object, Object>)this.delegate.lowerEntry(key));
        }
        
        @Override
        public K lowerKey(final K key) {
            return this.delegate.lowerKey(key);
        }
        
        @Override
        public Map.Entry<K, V> floorEntry(final K key) {
            return (Map.Entry<K, V>)unmodifiableOrNull((Map.Entry<Object, Object>)this.delegate.floorEntry(key));
        }
        
        @Override
        public K floorKey(final K key) {
            return this.delegate.floorKey(key);
        }
        
        @Override
        public Map.Entry<K, V> ceilingEntry(final K key) {
            return (Map.Entry<K, V>)unmodifiableOrNull((Map.Entry<Object, Object>)this.delegate.ceilingEntry(key));
        }
        
        @Override
        public K ceilingKey(final K key) {
            return this.delegate.ceilingKey(key);
        }
        
        @Override
        public Map.Entry<K, V> higherEntry(final K key) {
            return (Map.Entry<K, V>)unmodifiableOrNull((Map.Entry<Object, Object>)this.delegate.higherEntry(key));
        }
        
        @Override
        public K higherKey(final K key) {
            return this.delegate.higherKey(key);
        }
        
        @Override
        public Map.Entry<K, V> firstEntry() {
            return (Map.Entry<K, V>)unmodifiableOrNull((Map.Entry<Object, Object>)this.delegate.firstEntry());
        }
        
        @Override
        public Map.Entry<K, V> lastEntry() {
            return (Map.Entry<K, V>)unmodifiableOrNull((Map.Entry<Object, Object>)this.delegate.lastEntry());
        }
        
        @Override
        public final Map.Entry<K, V> pollFirstEntry() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public final Map.Entry<K, V> pollLastEntry() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public NavigableMap<K, V> descendingMap() {
            final UnmodifiableNavigableMap<K, V> result = this.descendingMap;
            return (result == null) ? (this.descendingMap = new UnmodifiableNavigableMap<K, V>(this.delegate.descendingMap(), this)) : result;
        }
        
        @Override
        public Set<K> keySet() {
            return this.navigableKeySet();
        }
        
        @Override
        public NavigableSet<K> navigableKeySet() {
            return Sets.unmodifiableNavigableSet(this.delegate.navigableKeySet());
        }
        
        @Override
        public NavigableSet<K> descendingKeySet() {
            return Sets.unmodifiableNavigableSet(this.delegate.descendingKeySet());
        }
        
        @Override
        public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
            return this.subMap(fromKey, true, toKey, false);
        }
        
        @Override
        public SortedMap<K, V> headMap(final K toKey) {
            return this.headMap(toKey, false);
        }
        
        @Override
        public SortedMap<K, V> tailMap(final K fromKey) {
            return this.tailMap(fromKey, true);
        }
        
        @Override
        public NavigableMap<K, V> subMap(final K fromKey, final boolean fromInclusive, final K toKey, final boolean toInclusive) {
            return Maps.unmodifiableNavigableMap(this.delegate.subMap(fromKey, fromInclusive, toKey, toInclusive));
        }
        
        @Override
        public NavigableMap<K, V> headMap(final K toKey, final boolean inclusive) {
            return Maps.unmodifiableNavigableMap(this.delegate.headMap(toKey, inclusive));
        }
        
        @Override
        public NavigableMap<K, V> tailMap(final K fromKey, final boolean inclusive) {
            return Maps.unmodifiableNavigableMap(this.delegate.tailMap(fromKey, inclusive));
        }
    }
    
    @GwtCompatible
    abstract static class ImprovedAbstractMap<K, V> extends AbstractMap<K, V>
    {
        private transient Set<Map.Entry<K, V>> entrySet;
        private transient Set<K> keySet;
        private transient Collection<V> values;
        
        abstract Set<Map.Entry<K, V>> createEntrySet();
        
        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            final Set<Map.Entry<K, V>> result = this.entrySet;
            return (result == null) ? (this.entrySet = this.createEntrySet()) : result;
        }
        
        @Override
        public Set<K> keySet() {
            final Set<K> result = this.keySet;
            return (result == null) ? (this.keySet = this.createKeySet()) : result;
        }
        
        Set<K> createKeySet() {
            return (Set<K>)new KeySet((Map<Object, Object>)this);
        }
        
        @Override
        public Collection<V> values() {
            final Collection<V> result = this.values;
            return (result == null) ? (this.values = this.createValues()) : result;
        }
        
        Collection<V> createValues() {
            return (Collection<V>)new Values((Map<Object, Object>)this);
        }
    }
    
    static class KeySet<K, V> extends Sets.ImprovedAbstractSet<K>
    {
        final Map<K, V> map;
        
        KeySet(final Map<K, V> map) {
            this.map = Preconditions.checkNotNull(map);
        }
        
        Map<K, V> map() {
            return this.map;
        }
        
        @Override
        public Iterator<K> iterator() {
            return Maps.keyIterator(this.map().entrySet().iterator());
        }
        
        @Override
        public int size() {
            return this.map().size();
        }
        
        @Override
        public boolean isEmpty() {
            return this.map().isEmpty();
        }
        
        @Override
        public boolean contains(final Object o) {
            return this.map().containsKey(o);
        }
        
        @Override
        public boolean remove(final Object o) {
            if (this.contains(o)) {
                this.map().remove(o);
                return true;
            }
            return false;
        }
        
        @Override
        public void clear() {
            this.map().clear();
        }
    }
    
    static class SortedKeySet<K, V> extends KeySet<K, V> implements SortedSet<K>
    {
        SortedKeySet(final SortedMap<K, V> map) {
            super(map);
        }
        
        @Override
        SortedMap<K, V> map() {
            return (SortedMap<K, V>)(SortedMap)super.map();
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return this.map().comparator();
        }
        
        @Override
        public SortedSet<K> subSet(final K fromElement, final K toElement) {
            return new SortedKeySet<K, Object>(this.map().subMap(fromElement, toElement));
        }
        
        @Override
        public SortedSet<K> headSet(final K toElement) {
            return new SortedKeySet<K, Object>(this.map().headMap(toElement));
        }
        
        @Override
        public SortedSet<K> tailSet(final K fromElement) {
            return new SortedKeySet<K, Object>(this.map().tailMap(fromElement));
        }
        
        @Override
        public K first() {
            return this.map().firstKey();
        }
        
        @Override
        public K last() {
            return this.map().lastKey();
        }
    }
    
    @GwtIncompatible("NavigableMap")
    static class NavigableKeySet<K, V> extends SortedKeySet<K, V> implements NavigableSet<K>
    {
        NavigableKeySet(final NavigableMap<K, V> map) {
            super(map);
        }
        
        @Override
        NavigableMap<K, V> map() {
            return (NavigableMap<K, V>)(NavigableMap)this.map;
        }
        
        @Override
        public K lower(final K e) {
            return this.map().lowerKey(e);
        }
        
        @Override
        public K floor(final K e) {
            return this.map().floorKey(e);
        }
        
        @Override
        public K ceiling(final K e) {
            return this.map().ceilingKey(e);
        }
        
        @Override
        public K higher(final K e) {
            return this.map().higherKey(e);
        }
        
        @Override
        public K pollFirst() {
            return Maps.keyOrNull(this.map().pollFirstEntry());
        }
        
        @Override
        public K pollLast() {
            return Maps.keyOrNull(this.map().pollLastEntry());
        }
        
        @Override
        public NavigableSet<K> descendingSet() {
            return this.map().descendingKeySet();
        }
        
        @Override
        public Iterator<K> descendingIterator() {
            return this.descendingSet().iterator();
        }
        
        @Override
        public NavigableSet<K> subSet(final K fromElement, final boolean fromInclusive, final K toElement, final boolean toInclusive) {
            return this.map().subMap(fromElement, fromInclusive, toElement, toInclusive).navigableKeySet();
        }
        
        @Override
        public NavigableSet<K> headSet(final K toElement, final boolean inclusive) {
            return this.map().headMap(toElement, inclusive).navigableKeySet();
        }
        
        @Override
        public NavigableSet<K> tailSet(final K fromElement, final boolean inclusive) {
            return this.map().tailMap(fromElement, inclusive).navigableKeySet();
        }
        
        @Override
        public SortedSet<K> subSet(final K fromElement, final K toElement) {
            return this.subSet(fromElement, true, toElement, false);
        }
        
        @Override
        public SortedSet<K> headSet(final K toElement) {
            return this.headSet(toElement, false);
        }
        
        @Override
        public SortedSet<K> tailSet(final K fromElement) {
            return this.tailSet(fromElement, true);
        }
    }
    
    static class Values<K, V> extends AbstractCollection<V>
    {
        final Map<K, V> map;
        
        Values(final Map<K, V> map) {
            this.map = Preconditions.checkNotNull(map);
        }
        
        final Map<K, V> map() {
            return this.map;
        }
        
        @Override
        public Iterator<V> iterator() {
            return Maps.valueIterator(this.map().entrySet().iterator());
        }
        
        @Override
        public boolean remove(final Object o) {
            try {
                return super.remove(o);
            }
            catch (UnsupportedOperationException e) {
                for (final Map.Entry<K, V> entry : this.map().entrySet()) {
                    if (Objects.equal(o, entry.getValue())) {
                        this.map().remove(entry.getKey());
                        return true;
                    }
                }
                return false;
            }
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            try {
                return super.removeAll(Preconditions.checkNotNull(c));
            }
            catch (UnsupportedOperationException e) {
                final Set<K> toRemove = (Set<K>)Sets.newHashSet();
                for (final Map.Entry<K, V> entry : this.map().entrySet()) {
                    if (c.contains(entry.getValue())) {
                        toRemove.add(entry.getKey());
                    }
                }
                return this.map().keySet().removeAll(toRemove);
            }
        }
        
        @Override
        public boolean retainAll(final Collection<?> c) {
            try {
                return super.retainAll(Preconditions.checkNotNull(c));
            }
            catch (UnsupportedOperationException e) {
                final Set<K> toRetain = (Set<K>)Sets.newHashSet();
                for (final Map.Entry<K, V> entry : this.map().entrySet()) {
                    if (c.contains(entry.getValue())) {
                        toRetain.add(entry.getKey());
                    }
                }
                return this.map().keySet().retainAll(toRetain);
            }
        }
        
        @Override
        public int size() {
            return this.map().size();
        }
        
        @Override
        public boolean isEmpty() {
            return this.map().isEmpty();
        }
        
        @Override
        public boolean contains(@Nullable final Object o) {
            return this.map().containsValue(o);
        }
        
        @Override
        public void clear() {
            this.map().clear();
        }
    }
    
    abstract static class EntrySet<K, V> extends Sets.ImprovedAbstractSet<Map.Entry<K, V>>
    {
        abstract Map<K, V> map();
        
        @Override
        public int size() {
            return this.map().size();
        }
        
        @Override
        public void clear() {
            this.map().clear();
        }
        
        @Override
        public boolean contains(final Object o) {
            if (o instanceof Map.Entry) {
                final Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
                final Object key = entry.getKey();
                final V value = Maps.safeGet(this.map(), key);
                return Objects.equal(value, entry.getValue()) && (value != null || this.map().containsKey(key));
            }
            return false;
        }
        
        @Override
        public boolean isEmpty() {
            return this.map().isEmpty();
        }
        
        @Override
        public boolean remove(final Object o) {
            if (this.contains(o)) {
                final Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
                return this.map().keySet().remove(entry.getKey());
            }
            return false;
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            try {
                return super.removeAll(Preconditions.checkNotNull(c));
            }
            catch (UnsupportedOperationException e) {
                return Sets.removeAllImpl(this, c.iterator());
            }
        }
        
        @Override
        public boolean retainAll(final Collection<?> c) {
            try {
                return super.retainAll(Preconditions.checkNotNull(c));
            }
            catch (UnsupportedOperationException e) {
                final Set<Object> keys = Sets.newHashSetWithExpectedSize(c.size());
                for (final Object o : c) {
                    if (this.contains(o)) {
                        final Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
                        keys.add(entry.getKey());
                    }
                }
                return this.map().keySet().retainAll(keys);
            }
        }
    }
    
    @GwtIncompatible("NavigableMap")
    abstract static class DescendingMap<K, V> extends ForwardingMap<K, V> implements NavigableMap<K, V>
    {
        private transient Comparator<? super K> comparator;
        private transient Set<Map.Entry<K, V>> entrySet;
        private transient NavigableSet<K> navigableKeySet;
        
        abstract NavigableMap<K, V> forward();
        
        @Override
        protected final Map<K, V> delegate() {
            return this.forward();
        }
        
        @Override
        public Comparator<? super K> comparator() {
            Comparator<? super K> result = this.comparator;
            if (result == null) {
                Comparator<? super K> forwardCmp = this.forward().comparator();
                if (forwardCmp == null) {
                    forwardCmp = (Comparator<? super K>)Ordering.natural();
                }
                final Ordering<? super K> reverse = reverse(forwardCmp);
                this.comparator = reverse;
                result = reverse;
            }
            return result;
        }
        
        private static <T> Ordering<T> reverse(final Comparator<T> forward) {
            return Ordering.from(forward).reverse();
        }
        
        @Override
        public K firstKey() {
            return this.forward().lastKey();
        }
        
        @Override
        public K lastKey() {
            return this.forward().firstKey();
        }
        
        @Override
        public Map.Entry<K, V> lowerEntry(final K key) {
            return this.forward().higherEntry(key);
        }
        
        @Override
        public K lowerKey(final K key) {
            return this.forward().higherKey(key);
        }
        
        @Override
        public Map.Entry<K, V> floorEntry(final K key) {
            return this.forward().ceilingEntry(key);
        }
        
        @Override
        public K floorKey(final K key) {
            return this.forward().ceilingKey(key);
        }
        
        @Override
        public Map.Entry<K, V> ceilingEntry(final K key) {
            return this.forward().floorEntry(key);
        }
        
        @Override
        public K ceilingKey(final K key) {
            return this.forward().floorKey(key);
        }
        
        @Override
        public Map.Entry<K, V> higherEntry(final K key) {
            return this.forward().lowerEntry(key);
        }
        
        @Override
        public K higherKey(final K key) {
            return this.forward().lowerKey(key);
        }
        
        @Override
        public Map.Entry<K, V> firstEntry() {
            return this.forward().lastEntry();
        }
        
        @Override
        public Map.Entry<K, V> lastEntry() {
            return this.forward().firstEntry();
        }
        
        @Override
        public Map.Entry<K, V> pollFirstEntry() {
            return this.forward().pollLastEntry();
        }
        
        @Override
        public Map.Entry<K, V> pollLastEntry() {
            return this.forward().pollFirstEntry();
        }
        
        @Override
        public NavigableMap<K, V> descendingMap() {
            return this.forward();
        }
        
        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            final Set<Map.Entry<K, V>> result = this.entrySet;
            return (result == null) ? (this.entrySet = this.createEntrySet()) : result;
        }
        
        abstract Iterator<Map.Entry<K, V>> entryIterator();
        
        Set<Map.Entry<K, V>> createEntrySet() {
            return (Set<Map.Entry<K, V>>)new EntrySet<K, V>() {
                @Override
                Map<K, V> map() {
                    return (Map<K, V>)DescendingMap.this;
                }
                
                @Override
                public Iterator<Map.Entry<K, V>> iterator() {
                    return DescendingMap.this.entryIterator();
                }
            };
        }
        
        @Override
        public Set<K> keySet() {
            return this.navigableKeySet();
        }
        
        @Override
        public NavigableSet<K> navigableKeySet() {
            final NavigableSet<K> result = this.navigableKeySet;
            return (result == null) ? (this.navigableKeySet = new NavigableKeySet<K, Object>(this)) : result;
        }
        
        @Override
        public NavigableSet<K> descendingKeySet() {
            return this.forward().navigableKeySet();
        }
        
        @Override
        public NavigableMap<K, V> subMap(final K fromKey, final boolean fromInclusive, final K toKey, final boolean toInclusive) {
            return this.forward().subMap(toKey, toInclusive, fromKey, fromInclusive).descendingMap();
        }
        
        @Override
        public NavigableMap<K, V> headMap(final K toKey, final boolean inclusive) {
            return this.forward().tailMap(toKey, inclusive).descendingMap();
        }
        
        @Override
        public NavigableMap<K, V> tailMap(final K fromKey, final boolean inclusive) {
            return this.forward().headMap(fromKey, inclusive).descendingMap();
        }
        
        @Override
        public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
            return this.subMap(fromKey, true, toKey, false);
        }
        
        @Override
        public SortedMap<K, V> headMap(final K toKey) {
            return this.headMap(toKey, false);
        }
        
        @Override
        public SortedMap<K, V> tailMap(final K fromKey) {
            return this.tailMap(fromKey, true);
        }
        
        @Override
        public Collection<V> values() {
            return (Collection<V>)new Values((Map<Object, Object>)this);
        }
        
        @Override
        public String toString() {
            return this.standardToString();
        }
    }
    
    public interface EntryTransformer<K, V1, V2>
    {
        V2 transformEntry(@Nullable final K p0, @Nullable final V1 p1);
    }
}
