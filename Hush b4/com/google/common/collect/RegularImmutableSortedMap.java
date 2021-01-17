// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import java.util.NavigableMap;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
final class RegularImmutableSortedMap<K, V> extends ImmutableSortedMap<K, V>
{
    private final transient RegularImmutableSortedSet<K> keySet;
    private final transient ImmutableList<V> valueList;
    
    RegularImmutableSortedMap(final RegularImmutableSortedSet<K> keySet, final ImmutableList<V> valueList) {
        this.keySet = keySet;
        this.valueList = valueList;
    }
    
    RegularImmutableSortedMap(final RegularImmutableSortedSet<K> keySet, final ImmutableList<V> valueList, final ImmutableSortedMap<K, V> descendingMap) {
        super(descendingMap);
        this.keySet = keySet;
        this.valueList = valueList;
    }
    
    @Override
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        return (ImmutableSet<Map.Entry<K, V>>)new EntrySet();
    }
    
    @Override
    public ImmutableSortedSet<K> keySet() {
        return this.keySet;
    }
    
    @Override
    public ImmutableCollection<V> values() {
        return this.valueList;
    }
    
    @Override
    public V get(@Nullable final Object key) {
        final int index = this.keySet.indexOf(key);
        return (index == -1) ? null : this.valueList.get(index);
    }
    
    private ImmutableSortedMap<K, V> getSubMap(final int fromIndex, final int toIndex) {
        if (fromIndex == 0 && toIndex == this.size()) {
            return this;
        }
        if (fromIndex == toIndex) {
            return ImmutableSortedMap.emptyMap(this.comparator());
        }
        return ImmutableSortedMap.from(this.keySet.getSubSet(fromIndex, toIndex), this.valueList.subList(fromIndex, toIndex));
    }
    
    @Override
    public ImmutableSortedMap<K, V> headMap(final K toKey, final boolean inclusive) {
        return this.getSubMap(0, this.keySet.headIndex(Preconditions.checkNotNull(toKey), inclusive));
    }
    
    @Override
    public ImmutableSortedMap<K, V> tailMap(final K fromKey, final boolean inclusive) {
        return this.getSubMap(this.keySet.tailIndex(Preconditions.checkNotNull(fromKey), inclusive), this.size());
    }
    
    @Override
    ImmutableSortedMap<K, V> createDescendingMap() {
        return new RegularImmutableSortedMap((RegularImmutableSortedSet)this.keySet.descendingSet(), (ImmutableList<Object>)this.valueList.reverse(), (ImmutableSortedMap<Object, Object>)this);
    }
    
    private class EntrySet extends ImmutableMapEntrySet<K, V>
    {
        @Override
        public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
            return (UnmodifiableIterator<Map.Entry<K, V>>)this.asList().iterator();
        }
        
        @Override
        ImmutableList<Map.Entry<K, V>> createAsList() {
            return new ImmutableAsList<Map.Entry<K, V>>() {
                private final ImmutableList<K> keyList = RegularImmutableSortedMap.this.keySet().asList();
                
                @Override
                public Map.Entry<K, V> get(final int index) {
                    return Maps.immutableEntry(this.keyList.get(index), RegularImmutableSortedMap.this.valueList.get(index));
                }
                
                @Override
                ImmutableCollection<Map.Entry<K, V>> delegateCollection() {
                    return (ImmutableCollection<Map.Entry<K, V>>)EntrySet.this;
                }
            };
        }
        
        @Override
        ImmutableMap<K, V> map() {
            return (ImmutableMap<K, V>)RegularImmutableSortedMap.this;
        }
    }
}
