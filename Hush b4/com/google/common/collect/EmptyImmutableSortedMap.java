// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Collection;
import java.util.Set;
import java.util.NavigableMap;
import com.google.common.base.Preconditions;
import java.util.Map;
import javax.annotation.Nullable;
import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
final class EmptyImmutableSortedMap<K, V> extends ImmutableSortedMap<K, V>
{
    private final transient ImmutableSortedSet<K> keySet;
    
    EmptyImmutableSortedMap(final Comparator<? super K> comparator) {
        this.keySet = ImmutableSortedSet.emptySet(comparator);
    }
    
    EmptyImmutableSortedMap(final Comparator<? super K> comparator, final ImmutableSortedMap<K, V> descendingMap) {
        super(descendingMap);
        this.keySet = ImmutableSortedSet.emptySet(comparator);
    }
    
    @Override
    public V get(@Nullable final Object key) {
        return null;
    }
    
    @Override
    public ImmutableSortedSet<K> keySet() {
        return this.keySet;
    }
    
    @Override
    public int size() {
        return 0;
    }
    
    @Override
    public boolean isEmpty() {
        return true;
    }
    
    @Override
    public ImmutableCollection<V> values() {
        return (ImmutableCollection<V>)ImmutableList.of();
    }
    
    @Override
    public String toString() {
        return "{}";
    }
    
    @Override
    boolean isPartialView() {
        return false;
    }
    
    @Override
    public ImmutableSet<Map.Entry<K, V>> entrySet() {
        return ImmutableSet.of();
    }
    
    @Override
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        throw new AssertionError((Object)"should never be called");
    }
    
    @Override
    public ImmutableSetMultimap<K, V> asMultimap() {
        return ImmutableSetMultimap.of();
    }
    
    @Override
    public ImmutableSortedMap<K, V> headMap(final K toKey, final boolean inclusive) {
        Preconditions.checkNotNull(toKey);
        return this;
    }
    
    @Override
    public ImmutableSortedMap<K, V> tailMap(final K fromKey, final boolean inclusive) {
        Preconditions.checkNotNull(fromKey);
        return this;
    }
    
    @Override
    ImmutableSortedMap<K, V> createDescendingMap() {
        return new EmptyImmutableSortedMap(Ordering.from(this.comparator()).reverse(), (ImmutableSortedMap<Object, Object>)this);
    }
}
