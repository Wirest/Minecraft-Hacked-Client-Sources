// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Set;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Collections;
import java.util.SortedSet;
import java.util.Collection;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract class AbstractSortedSetMultimap<K, V> extends AbstractSetMultimap<K, V> implements SortedSetMultimap<K, V>
{
    private static final long serialVersionUID = 430848587173315748L;
    
    protected AbstractSortedSetMultimap(final Map<K, Collection<V>> map) {
        super(map);
    }
    
    @Override
    abstract SortedSet<V> createCollection();
    
    @Override
    SortedSet<V> createUnmodifiableEmptyCollection() {
        final Comparator<? super V> comparator = this.valueComparator();
        if (comparator == null) {
            return Collections.unmodifiableSortedSet(this.createCollection());
        }
        return (SortedSet<V>)ImmutableSortedSet.emptySet((Comparator<? super Object>)this.valueComparator());
    }
    
    @Override
    public SortedSet<V> get(@Nullable final K key) {
        return (SortedSet<V>)(SortedSet)super.get(key);
    }
    
    @Override
    public SortedSet<V> removeAll(@Nullable final Object key) {
        return (SortedSet<V>)(SortedSet)super.removeAll(key);
    }
    
    @Override
    public SortedSet<V> replaceValues(@Nullable final K key, final Iterable<? extends V> values) {
        return (SortedSet<V>)(SortedSet)super.replaceValues(key, values);
    }
    
    @Override
    public Map<K, Collection<V>> asMap() {
        return super.asMap();
    }
    
    @Override
    public Collection<V> values() {
        return super.values();
    }
}
