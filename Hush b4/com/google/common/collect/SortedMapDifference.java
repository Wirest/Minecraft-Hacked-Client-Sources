// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.SortedMap;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface SortedMapDifference<K, V> extends MapDifference<K, V>
{
    SortedMap<K, V> entriesOnlyOnLeft();
    
    SortedMap<K, V> entriesOnlyOnRight();
    
    SortedMap<K, V> entriesInCommon();
    
    SortedMap<K, ValueDifference<V>> entriesDiffering();
}
