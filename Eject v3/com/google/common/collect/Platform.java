package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Predicate;

import java.lang.reflect.Array;
import java.util.*;
import java.util.Map.Entry;

@GwtCompatible(emulated = true)
final class Platform {
    static <T> T[] newArray(T[] paramArrayOfT, int paramInt) {
        Class localClass = paramArrayOfT.getClass().getComponentType();
        Object[] arrayOfObject = (Object[]) Array.newInstance(localClass, paramInt);
        return arrayOfObject;
    }

    static <E> Set<E> newSetFromMap(Map<E, Boolean> paramMap) {
        return Collections.newSetFromMap(paramMap);
    }

    static MapMaker tryWeakKeys(MapMaker paramMapMaker) {
        return paramMapMaker.weakKeys();
    }

    static <K, V1, V2> SortedMap<K, V2> mapsTransformEntriesSortedMap(SortedMap<K, V1> paramSortedMap, Maps.EntryTransformer<? super K, ? super V1, V2> paramEntryTransformer) {
        return (paramSortedMap instanceof NavigableMap) ? Maps.transformEntries((NavigableMap) paramSortedMap, paramEntryTransformer) : Maps.transformEntriesIgnoreNavigable(paramSortedMap, paramEntryTransformer);
    }

    static <K, V> SortedMap<K, V> mapsAsMapSortedSet(SortedSet<K> paramSortedSet, Function<? super K, V> paramFunction) {
        return (paramSortedSet instanceof NavigableSet) ? Maps.asMap((NavigableSet) paramSortedSet, paramFunction) : Maps.asMapSortedIgnoreNavigable(paramSortedSet, paramFunction);
    }

    static <E> SortedSet<E> setsFilterSortedSet(SortedSet<E> paramSortedSet, Predicate<? super E> paramPredicate) {
        return (paramSortedSet instanceof NavigableSet) ? Sets.filter((NavigableSet) paramSortedSet, paramPredicate) : Sets.filterSortedIgnoreNavigable(paramSortedSet, paramPredicate);
    }

    static <K, V> SortedMap<K, V> mapsFilterSortedMap(SortedMap<K, V> paramSortedMap, Predicate<? super Map.Entry<K, V>> paramPredicate) {
        return (paramSortedMap instanceof NavigableMap) ? Maps.filterEntries((NavigableMap) paramSortedMap, paramPredicate) : Maps.filterSortedIgnoreNavigable(paramSortedMap, paramPredicate);
    }
}




