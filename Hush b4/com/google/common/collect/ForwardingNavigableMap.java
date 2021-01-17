// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.SortedMap;
import com.google.common.annotations.Beta;
import java.util.NavigableSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Map;
import java.util.NavigableMap;

public abstract class ForwardingNavigableMap<K, V> extends ForwardingSortedMap<K, V> implements NavigableMap<K, V>
{
    protected ForwardingNavigableMap() {
    }
    
    @Override
    protected abstract NavigableMap<K, V> delegate();
    
    @Override
    public Map.Entry<K, V> lowerEntry(final K key) {
        return this.delegate().lowerEntry(key);
    }
    
    protected Map.Entry<K, V> standardLowerEntry(final K key) {
        return this.headMap(key, false).lastEntry();
    }
    
    @Override
    public K lowerKey(final K key) {
        return this.delegate().lowerKey(key);
    }
    
    protected K standardLowerKey(final K key) {
        return Maps.keyOrNull((Map.Entry<K, ?>)this.lowerEntry((K)key));
    }
    
    @Override
    public Map.Entry<K, V> floorEntry(final K key) {
        return this.delegate().floorEntry(key);
    }
    
    protected Map.Entry<K, V> standardFloorEntry(final K key) {
        return this.headMap(key, true).lastEntry();
    }
    
    @Override
    public K floorKey(final K key) {
        return this.delegate().floorKey(key);
    }
    
    protected K standardFloorKey(final K key) {
        return Maps.keyOrNull((Map.Entry<K, ?>)this.floorEntry((K)key));
    }
    
    @Override
    public Map.Entry<K, V> ceilingEntry(final K key) {
        return this.delegate().ceilingEntry(key);
    }
    
    protected Map.Entry<K, V> standardCeilingEntry(final K key) {
        return this.tailMap(key, true).firstEntry();
    }
    
    @Override
    public K ceilingKey(final K key) {
        return this.delegate().ceilingKey(key);
    }
    
    protected K standardCeilingKey(final K key) {
        return Maps.keyOrNull((Map.Entry<K, ?>)this.ceilingEntry((K)key));
    }
    
    @Override
    public Map.Entry<K, V> higherEntry(final K key) {
        return this.delegate().higherEntry(key);
    }
    
    protected Map.Entry<K, V> standardHigherEntry(final K key) {
        return this.tailMap(key, false).firstEntry();
    }
    
    @Override
    public K higherKey(final K key) {
        return this.delegate().higherKey(key);
    }
    
    protected K standardHigherKey(final K key) {
        return Maps.keyOrNull((Map.Entry<K, ?>)this.higherEntry((K)key));
    }
    
    @Override
    public Map.Entry<K, V> firstEntry() {
        return this.delegate().firstEntry();
    }
    
    protected Map.Entry<K, V> standardFirstEntry() {
        return Iterables.getFirst(this.entrySet(), (Map.Entry<K, V>)null);
    }
    
    protected K standardFirstKey() {
        final Map.Entry<K, V> entry = this.firstEntry();
        if (entry == null) {
            throw new NoSuchElementException();
        }
        return entry.getKey();
    }
    
    @Override
    public Map.Entry<K, V> lastEntry() {
        return this.delegate().lastEntry();
    }
    
    protected Map.Entry<K, V> standardLastEntry() {
        return Iterables.getFirst((Iterable<? extends Map.Entry<K, V>>)this.descendingMap().entrySet(), (Map.Entry<K, V>)null);
    }
    
    protected K standardLastKey() {
        final Map.Entry<K, V> entry = this.lastEntry();
        if (entry == null) {
            throw new NoSuchElementException();
        }
        return entry.getKey();
    }
    
    @Override
    public Map.Entry<K, V> pollFirstEntry() {
        return this.delegate().pollFirstEntry();
    }
    
    protected Map.Entry<K, V> standardPollFirstEntry() {
        return Iterators.pollNext(this.entrySet().iterator());
    }
    
    @Override
    public Map.Entry<K, V> pollLastEntry() {
        return this.delegate().pollLastEntry();
    }
    
    protected Map.Entry<K, V> standardPollLastEntry() {
        return Iterators.pollNext((Iterator<Map.Entry<K, V>>)this.descendingMap().entrySet().iterator());
    }
    
    @Override
    public NavigableMap<K, V> descendingMap() {
        return this.delegate().descendingMap();
    }
    
    @Override
    public NavigableSet<K> navigableKeySet() {
        return this.delegate().navigableKeySet();
    }
    
    @Override
    public NavigableSet<K> descendingKeySet() {
        return this.delegate().descendingKeySet();
    }
    
    @Beta
    protected NavigableSet<K> standardDescendingKeySet() {
        return this.descendingMap().navigableKeySet();
    }
    
    @Override
    protected SortedMap<K, V> standardSubMap(final K fromKey, final K toKey) {
        return this.subMap(fromKey, true, toKey, false);
    }
    
    @Override
    public NavigableMap<K, V> subMap(final K fromKey, final boolean fromInclusive, final K toKey, final boolean toInclusive) {
        return this.delegate().subMap(fromKey, fromInclusive, toKey, toInclusive);
    }
    
    @Override
    public NavigableMap<K, V> headMap(final K toKey, final boolean inclusive) {
        return this.delegate().headMap(toKey, inclusive);
    }
    
    @Override
    public NavigableMap<K, V> tailMap(final K fromKey, final boolean inclusive) {
        return this.delegate().tailMap(fromKey, inclusive);
    }
    
    protected SortedMap<K, V> standardHeadMap(final K toKey) {
        return this.headMap(toKey, false);
    }
    
    protected SortedMap<K, V> standardTailMap(final K fromKey) {
        return this.tailMap(fromKey, true);
    }
    
    @Beta
    protected class StandardDescendingMap extends Maps.DescendingMap<K, V>
    {
        public StandardDescendingMap() {
        }
        
        @Override
        NavigableMap<K, V> forward() {
            return (NavigableMap<K, V>)ForwardingNavigableMap.this;
        }
        
        protected Iterator<Map.Entry<K, V>> entryIterator() {
            return new Iterator<Map.Entry<K, V>>() {
                private Map.Entry<K, V> toRemove = null;
                private Map.Entry<K, V> nextOrNull = StandardDescendingMap.this.forward().lastEntry();
                
                @Override
                public boolean hasNext() {
                    return this.nextOrNull != null;
                }
                
                @Override
                public Map.Entry<K, V> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    try {
                        return this.nextOrNull;
                    }
                    finally {
                        this.toRemove = this.nextOrNull;
                        this.nextOrNull = StandardDescendingMap.this.forward().lowerEntry(this.nextOrNull.getKey());
                    }
                }
                
                @Override
                public void remove() {
                    CollectPreconditions.checkRemove(this.toRemove != null);
                    StandardDescendingMap.this.forward().remove(this.toRemove.getKey());
                    this.toRemove = null;
                }
            };
        }
    }
    
    @Beta
    protected class StandardNavigableKeySet extends Maps.NavigableKeySet<K, V>
    {
        public StandardNavigableKeySet() {
            super(ForwardingNavigableMap.this);
        }
    }
}
