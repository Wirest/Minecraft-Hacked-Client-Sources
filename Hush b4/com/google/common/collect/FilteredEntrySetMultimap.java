// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Collection;
import java.util.Set;
import java.util.Map;
import com.google.common.base.Predicate;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
final class FilteredEntrySetMultimap<K, V> extends FilteredEntryMultimap<K, V> implements FilteredSetMultimap<K, V>
{
    FilteredEntrySetMultimap(final SetMultimap<K, V> unfiltered, final Predicate<? super Map.Entry<K, V>> predicate) {
        super(unfiltered, predicate);
    }
    
    @Override
    public SetMultimap<K, V> unfiltered() {
        return (SetMultimap<K, V>)(SetMultimap)this.unfiltered;
    }
    
    @Override
    public Set<V> get(final K key) {
        return (Set<V>)(Set)super.get(key);
    }
    
    @Override
    public Set<V> removeAll(final Object key) {
        return (Set<V>)(Set)super.removeAll(key);
    }
    
    @Override
    public Set<V> replaceValues(final K key, final Iterable<? extends V> values) {
        return (Set<V>)(Set)super.replaceValues(key, values);
    }
    
    @Override
    Set<Map.Entry<K, V>> createEntries() {
        return Sets.filter(this.unfiltered().entries(), this.entryPredicate());
    }
    
    @Override
    public Set<Map.Entry<K, V>> entries() {
        return (Set<Map.Entry<K, V>>)(Set)super.entries();
    }
}
