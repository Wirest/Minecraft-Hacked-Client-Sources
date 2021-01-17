// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Map;
import java.util.Set;
import java.util.Collection;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface Multimap<K, V>
{
    int size();
    
    boolean isEmpty();
    
    boolean containsKey(@Nullable final Object p0);
    
    boolean containsValue(@Nullable final Object p0);
    
    boolean containsEntry(@Nullable final Object p0, @Nullable final Object p1);
    
    boolean put(@Nullable final K p0, @Nullable final V p1);
    
    boolean remove(@Nullable final Object p0, @Nullable final Object p1);
    
    boolean putAll(@Nullable final K p0, final Iterable<? extends V> p1);
    
    boolean putAll(final Multimap<? extends K, ? extends V> p0);
    
    Collection<V> replaceValues(@Nullable final K p0, final Iterable<? extends V> p1);
    
    Collection<V> removeAll(@Nullable final Object p0);
    
    void clear();
    
    Collection<V> get(@Nullable final K p0);
    
    Set<K> keySet();
    
    Multiset<K> keys();
    
    Collection<V> values();
    
    Collection<Map.Entry<K, V>> entries();
    
    Map<K, Collection<V>> asMap();
    
    boolean equals(@Nullable final Object p0);
    
    int hashCode();
}
