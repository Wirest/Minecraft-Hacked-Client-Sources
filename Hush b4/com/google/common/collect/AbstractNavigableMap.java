// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Set;
import java.util.NavigableSet;
import java.util.SortedMap;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;
import java.util.NavigableMap;
import java.util.AbstractMap;

abstract class AbstractNavigableMap<K, V> extends AbstractMap<K, V> implements NavigableMap<K, V>
{
    @Nullable
    @Override
    public abstract V get(@Nullable final Object p0);
    
    @Nullable
    @Override
    public Map.Entry<K, V> firstEntry() {
        return Iterators.getNext(this.entryIterator(), (Map.Entry<K, V>)null);
    }
    
    @Nullable
    @Override
    public Map.Entry<K, V> lastEntry() {
        return Iterators.getNext(this.descendingEntryIterator(), (Map.Entry<K, V>)null);
    }
    
    @Nullable
    @Override
    public Map.Entry<K, V> pollFirstEntry() {
        return Iterators.pollNext(this.entryIterator());
    }
    
    @Nullable
    @Override
    public Map.Entry<K, V> pollLastEntry() {
        return Iterators.pollNext(this.descendingEntryIterator());
    }
    
    @Override
    public K firstKey() {
        final Map.Entry<K, V> entry = this.firstEntry();
        if (entry == null) {
            throw new NoSuchElementException();
        }
        return entry.getKey();
    }
    
    @Override
    public K lastKey() {
        final Map.Entry<K, V> entry = this.lastEntry();
        if (entry == null) {
            throw new NoSuchElementException();
        }
        return entry.getKey();
    }
    
    @Nullable
    @Override
    public Map.Entry<K, V> lowerEntry(final K key) {
        return this.headMap(key, false).lastEntry();
    }
    
    @Nullable
    @Override
    public Map.Entry<K, V> floorEntry(final K key) {
        return this.headMap(key, true).lastEntry();
    }
    
    @Nullable
    @Override
    public Map.Entry<K, V> ceilingEntry(final K key) {
        return this.tailMap(key, true).firstEntry();
    }
    
    @Nullable
    @Override
    public Map.Entry<K, V> higherEntry(final K key) {
        return this.tailMap(key, false).firstEntry();
    }
    
    @Override
    public K lowerKey(final K key) {
        return Maps.keyOrNull((Map.Entry<K, ?>)this.lowerEntry((K)key));
    }
    
    @Override
    public K floorKey(final K key) {
        return Maps.keyOrNull((Map.Entry<K, ?>)this.floorEntry((K)key));
    }
    
    @Override
    public K ceilingKey(final K key) {
        return Maps.keyOrNull((Map.Entry<K, ?>)this.ceilingEntry((K)key));
    }
    
    @Override
    public K higherKey(final K key) {
        return Maps.keyOrNull((Map.Entry<K, ?>)this.higherEntry((K)key));
    }
    
    abstract Iterator<Map.Entry<K, V>> entryIterator();
    
    abstract Iterator<Map.Entry<K, V>> descendingEntryIterator();
    
    @Override
    public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
        return (SortedMap<K, V>)this.subMap(fromKey, true, toKey, false);
    }
    
    @Override
    public SortedMap<K, V> headMap(final K toKey) {
        return (SortedMap<K, V>)this.headMap(toKey, false);
    }
    
    @Override
    public SortedMap<K, V> tailMap(final K fromKey) {
        return (SortedMap<K, V>)this.tailMap(fromKey, true);
    }
    
    @Override
    public NavigableSet<K> navigableKeySet() {
        return new Maps.NavigableKeySet<K, Object>(this);
    }
    
    @Override
    public Set<K> keySet() {
        return this.navigableKeySet();
    }
    
    @Override
    public abstract int size();
    
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return (Set<Map.Entry<K, V>>)new Maps.EntrySet<K, V>() {
            @Override
            Map<K, V> map() {
                return (Map<K, V>)AbstractNavigableMap.this;
            }
            
            @Override
            public Iterator<Map.Entry<K, V>> iterator() {
                return AbstractNavigableMap.this.entryIterator();
            }
        };
    }
    
    @Override
    public NavigableSet<K> descendingKeySet() {
        return this.descendingMap().navigableKeySet();
    }
    
    @Override
    public NavigableMap<K, V> descendingMap() {
        return new DescendingMap();
    }
    
    private final class DescendingMap extends Maps.DescendingMap<K, V>
    {
        @Override
        NavigableMap<K, V> forward() {
            return (NavigableMap<K, V>)AbstractNavigableMap.this;
        }
        
        @Override
        Iterator<Map.Entry<K, V>> entryIterator() {
            return AbstractNavigableMap.this.descendingEntryIterator();
        }
    }
}
