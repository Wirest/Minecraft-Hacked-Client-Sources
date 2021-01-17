// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.base.Predicate;
import java.util.NavigableSet;
import com.google.common.base.Function;
import java.util.SortedSet;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.Collections;
import java.util.Set;
import java.util.Map;
import java.lang.reflect.Array;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
final class Platform
{
    static <T> T[] newArray(final T[] reference, final int length) {
        final Class<?> type = reference.getClass().getComponentType();
        final T[] result = (T[])Array.newInstance(type, length);
        return result;
    }
    
    static <E> Set<E> newSetFromMap(final Map<E, Boolean> map) {
        return Collections.newSetFromMap(map);
    }
    
    static MapMaker tryWeakKeys(final MapMaker mapMaker) {
        return mapMaker.weakKeys();
    }
    
    static <K, V1, V2> SortedMap<K, V2> mapsTransformEntriesSortedMap(final SortedMap<K, V1> fromMap, final Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
        return (fromMap instanceof NavigableMap) ? Maps.transformEntries((NavigableMap<Object, Object>)(NavigableMap)fromMap, (Maps.EntryTransformer<? super Object, ? super Object, V2>)transformer) : Maps.transformEntriesIgnoreNavigable(fromMap, transformer);
    }
    
    static <K, V> SortedMap<K, V> mapsAsMapSortedSet(final SortedSet<K> set, final Function<? super K, V> function) {
        return (set instanceof NavigableSet) ? Maps.asMap((NavigableSet<Object>)(NavigableSet)set, (Function<? super Object, V>)function) : Maps.asMapSortedIgnoreNavigable(set, function);
    }
    
    static <E> SortedSet<E> setsFilterSortedSet(final SortedSet<E> set, final Predicate<? super E> predicate) {
        return (set instanceof NavigableSet) ? Sets.filter((NavigableSet<Object>)(NavigableSet)set, (Predicate<? super Object>)predicate) : Sets.filterSortedIgnoreNavigable(set, predicate);
    }
    
    static <K, V> SortedMap<K, V> mapsFilterSortedMap(final SortedMap<K, V> map, final Predicate<? super Map.Entry<K, V>> predicate) {
        return (map instanceof NavigableMap) ? Maps.filterEntries((NavigableMap<Object, Object>)(NavigableMap)map, (Predicate<? super Map.Entry<Object, Object>>)predicate) : Maps.filterSortedIgnoreNavigable(map, predicate);
    }
    
    private Platform() {
    }
}
