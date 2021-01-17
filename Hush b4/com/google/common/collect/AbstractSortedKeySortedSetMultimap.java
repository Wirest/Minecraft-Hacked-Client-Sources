// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Set;
import java.util.SortedSet;
import java.util.Map;
import java.util.Collection;
import java.util.SortedMap;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract class AbstractSortedKeySortedSetMultimap<K, V> extends AbstractSortedSetMultimap<K, V>
{
    AbstractSortedKeySortedSetMultimap(final SortedMap<K, Collection<V>> map) {
        super(map);
    }
    
    @Override
    public SortedMap<K, Collection<V>> asMap() {
        return (SortedMap<K, Collection<V>>)(SortedMap)super.asMap();
    }
    
    @Override
    SortedMap<K, Collection<V>> backingMap() {
        return (SortedMap<K, Collection<V>>)(SortedMap)super.backingMap();
    }
    
    @Override
    public SortedSet<K> keySet() {
        return (SortedSet<K>)(SortedSet)super.keySet();
    }
}
