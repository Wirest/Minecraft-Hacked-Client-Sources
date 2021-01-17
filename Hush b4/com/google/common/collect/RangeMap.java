// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Map;
import javax.annotation.Nullable;
import com.google.common.annotations.Beta;

@Beta
public interface RangeMap<K extends Comparable, V>
{
    @Nullable
    V get(final K p0);
    
    @Nullable
    Map.Entry<Range<K>, V> getEntry(final K p0);
    
    Range<K> span();
    
    void put(final Range<K> p0, final V p1);
    
    void putAll(final RangeMap<K, V> p0);
    
    void clear();
    
    void remove(final Range<K> p0);
    
    Map<Range<K>, V> asMapOfRanges();
    
    RangeMap<K, V> subRangeMap(final Range<K> p0);
    
    boolean equals(@Nullable final Object p0);
    
    int hashCode();
    
    String toString();
}
