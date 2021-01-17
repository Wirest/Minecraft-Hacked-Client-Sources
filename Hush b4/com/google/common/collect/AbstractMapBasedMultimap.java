// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.NavigableMap;
import java.util.ListIterator;
import com.google.common.annotations.GwtIncompatible;
import java.util.NavigableSet;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.AbstractCollection;
import java.util.SortedMap;
import java.util.RandomAccess;
import java.util.List;
import java.util.Set;
import java.util.Collections;
import java.util.SortedSet;
import javax.annotation.Nullable;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(emulated = true)
abstract class AbstractMapBasedMultimap<K, V> extends AbstractMultimap<K, V> implements Serializable
{
    private transient Map<K, Collection<V>> map;
    private transient int totalSize;
    private static final long serialVersionUID = 2447537837011683357L;
    
    protected AbstractMapBasedMultimap(final Map<K, Collection<V>> map) {
        Preconditions.checkArgument(map.isEmpty());
        this.map = map;
    }
    
    final void setMap(final Map<K, Collection<V>> map) {
        this.map = map;
        this.totalSize = 0;
        for (final Collection<V> values : map.values()) {
            Preconditions.checkArgument(!values.isEmpty());
            this.totalSize += values.size();
        }
    }
    
    Collection<V> createUnmodifiableEmptyCollection() {
        return this.unmodifiableCollectionSubclass(this.createCollection());
    }
    
    abstract Collection<V> createCollection();
    
    Collection<V> createCollection(@Nullable final K key) {
        return this.createCollection();
    }
    
    Map<K, Collection<V>> backingMap() {
        return this.map;
    }
    
    @Override
    public int size() {
        return this.totalSize;
    }
    
    @Override
    public boolean containsKey(@Nullable final Object key) {
        return this.map.containsKey(key);
    }
    
    @Override
    public boolean put(@Nullable final K key, @Nullable final V value) {
        Collection<V> collection = this.map.get(key);
        if (collection == null) {
            collection = this.createCollection(key);
            if (collection.add(value)) {
                ++this.totalSize;
                this.map.put(key, collection);
                return true;
            }
            throw new AssertionError((Object)"New Collection violated the Collection spec");
        }
        else {
            if (collection.add(value)) {
                ++this.totalSize;
                return true;
            }
            return false;
        }
    }
    
    private Collection<V> getOrCreateCollection(@Nullable final K key) {
        Collection<V> collection = this.map.get(key);
        if (collection == null) {
            collection = this.createCollection(key);
            this.map.put(key, collection);
        }
        return collection;
    }
    
    @Override
    public Collection<V> replaceValues(@Nullable final K key, final Iterable<? extends V> values) {
        final Iterator<? extends V> iterator = values.iterator();
        if (!iterator.hasNext()) {
            return this.removeAll(key);
        }
        final Collection<V> collection = this.getOrCreateCollection(key);
        final Collection<V> oldValues = this.createCollection();
        oldValues.addAll((Collection<? extends V>)collection);
        this.totalSize -= collection.size();
        collection.clear();
        while (iterator.hasNext()) {
            if (collection.add((V)iterator.next())) {
                ++this.totalSize;
            }
        }
        return this.unmodifiableCollectionSubclass(oldValues);
    }
    
    @Override
    public Collection<V> removeAll(@Nullable final Object key) {
        final Collection<V> collection = this.map.remove(key);
        if (collection == null) {
            return this.createUnmodifiableEmptyCollection();
        }
        final Collection<V> output = this.createCollection();
        output.addAll((Collection<? extends V>)collection);
        this.totalSize -= collection.size();
        collection.clear();
        return this.unmodifiableCollectionSubclass(output);
    }
    
    Collection<V> unmodifiableCollectionSubclass(final Collection<V> collection) {
        if (collection instanceof SortedSet) {
            return (Collection<V>)Collections.unmodifiableSortedSet((SortedSet<Object>)(SortedSet)collection);
        }
        if (collection instanceof Set) {
            return (Collection<V>)Collections.unmodifiableSet((Set<?>)(Set)collection);
        }
        if (collection instanceof List) {
            return (Collection<V>)Collections.unmodifiableList((List<?>)(List)collection);
        }
        return Collections.unmodifiableCollection((Collection<? extends V>)collection);
    }
    
    @Override
    public void clear() {
        for (final Collection<V> collection : this.map.values()) {
            collection.clear();
        }
        this.map.clear();
        this.totalSize = 0;
    }
    
    @Override
    public Collection<V> get(@Nullable final K key) {
        Collection<V> collection = this.map.get(key);
        if (collection == null) {
            collection = this.createCollection(key);
        }
        return this.wrapCollection(key, collection);
    }
    
    Collection<V> wrapCollection(@Nullable final K key, final Collection<V> collection) {
        if (collection instanceof SortedSet) {
            return new WrappedSortedSet(key, (SortedSet)collection, null);
        }
        if (collection instanceof Set) {
            return new WrappedSet(key, (Set)collection);
        }
        if (collection instanceof List) {
            return this.wrapList(key, (List)collection, null);
        }
        return new WrappedCollection(key, collection, null);
    }
    
    private List<V> wrapList(@Nullable final K key, final List<V> list, @Nullable final WrappedCollection ancestor) {
        return (list instanceof RandomAccess) ? new RandomAccessWrappedList(key, list, ancestor) : new WrappedList(key, list, ancestor);
    }
    
    private Iterator<V> iteratorOrListIterator(final Collection<V> collection) {
        return (collection instanceof List) ? ((List)collection).listIterator() : collection.iterator();
    }
    
    @Override
    Set<K> createKeySet() {
        return (Set<K>)((this.map instanceof SortedMap) ? new SortedKeySet((SortedMap)this.map) : new KeySet(this.map));
    }
    
    private int removeValuesForKey(final Object key) {
        final Collection<V> collection = Maps.safeRemove(this.map, key);
        int count = 0;
        if (collection != null) {
            count = collection.size();
            collection.clear();
            this.totalSize -= count;
        }
        return count;
    }
    
    @Override
    public Collection<V> values() {
        return super.values();
    }
    
    @Override
    Iterator<V> valueIterator() {
        return new Itr<V>() {
            @Override
            V output(final K key, final V value) {
                return value;
            }
        };
    }
    
    @Override
    public Collection<Map.Entry<K, V>> entries() {
        return super.entries();
    }
    
    @Override
    Iterator<Map.Entry<K, V>> entryIterator() {
        return new Itr<Map.Entry<K, V>>() {
            @Override
            Map.Entry<K, V> output(final K key, final V value) {
                return Maps.immutableEntry(key, value);
            }
        };
    }
    
    @Override
    Map<K, Collection<V>> createAsMap() {
        return (this.map instanceof SortedMap) ? new SortedAsMap((SortedMap)this.map) : new AsMap(this.map);
    }
    
    private class WrappedCollection extends AbstractCollection<V>
    {
        final K key;
        Collection<V> delegate;
        final WrappedCollection ancestor;
        final Collection<V> ancestorDelegate;
        
        WrappedCollection(final K key, @Nullable final Collection<V> delegate, final WrappedCollection ancestor) {
            this.key = key;
            this.delegate = delegate;
            this.ancestor = ancestor;
            this.ancestorDelegate = ((ancestor == null) ? null : ancestor.getDelegate());
        }
        
        void refreshIfEmpty() {
            if (this.ancestor != null) {
                this.ancestor.refreshIfEmpty();
                if (this.ancestor.getDelegate() != this.ancestorDelegate) {
                    throw new ConcurrentModificationException();
                }
            }
            else if (this.delegate.isEmpty()) {
                final Collection<V> newDelegate = AbstractMapBasedMultimap.this.map.get(this.key);
                if (newDelegate != null) {
                    this.delegate = newDelegate;
                }
            }
        }
        
        void removeIfEmpty() {
            if (this.ancestor != null) {
                this.ancestor.removeIfEmpty();
            }
            else if (this.delegate.isEmpty()) {
                AbstractMapBasedMultimap.this.map.remove(this.key);
            }
        }
        
        K getKey() {
            return this.key;
        }
        
        void addToMap() {
            if (this.ancestor != null) {
                this.ancestor.addToMap();
            }
            else {
                AbstractMapBasedMultimap.this.map.put(this.key, this.delegate);
            }
        }
        
        @Override
        public int size() {
            this.refreshIfEmpty();
            return this.delegate.size();
        }
        
        @Override
        public boolean equals(@Nullable final Object object) {
            if (object == this) {
                return true;
            }
            this.refreshIfEmpty();
            return this.delegate.equals(object);
        }
        
        @Override
        public int hashCode() {
            this.refreshIfEmpty();
            return this.delegate.hashCode();
        }
        
        @Override
        public String toString() {
            this.refreshIfEmpty();
            return this.delegate.toString();
        }
        
        Collection<V> getDelegate() {
            return this.delegate;
        }
        
        @Override
        public Iterator<V> iterator() {
            this.refreshIfEmpty();
            return new WrappedIterator();
        }
        
        @Override
        public boolean add(final V value) {
            this.refreshIfEmpty();
            final boolean wasEmpty = this.delegate.isEmpty();
            final boolean changed = this.delegate.add(value);
            if (changed) {
                AbstractMapBasedMultimap.this.totalSize++;
                if (wasEmpty) {
                    this.addToMap();
                }
            }
            return changed;
        }
        
        WrappedCollection getAncestor() {
            return this.ancestor;
        }
        
        @Override
        public boolean addAll(final Collection<? extends V> collection) {
            if (collection.isEmpty()) {
                return false;
            }
            final int oldSize = this.size();
            final boolean changed = this.delegate.addAll(collection);
            if (changed) {
                final int newSize = this.delegate.size();
                AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
                if (oldSize == 0) {
                    this.addToMap();
                }
            }
            return changed;
        }
        
        @Override
        public boolean contains(final Object o) {
            this.refreshIfEmpty();
            return this.delegate.contains(o);
        }
        
        @Override
        public boolean containsAll(final Collection<?> c) {
            this.refreshIfEmpty();
            return this.delegate.containsAll(c);
        }
        
        @Override
        public void clear() {
            final int oldSize = this.size();
            if (oldSize == 0) {
                return;
            }
            this.delegate.clear();
            AbstractMapBasedMultimap.this.totalSize -= oldSize;
            this.removeIfEmpty();
        }
        
        @Override
        public boolean remove(final Object o) {
            this.refreshIfEmpty();
            final boolean changed = this.delegate.remove(o);
            if (changed) {
                AbstractMapBasedMultimap.this.totalSize--;
                this.removeIfEmpty();
            }
            return changed;
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            if (c.isEmpty()) {
                return false;
            }
            final int oldSize = this.size();
            final boolean changed = this.delegate.removeAll(c);
            if (changed) {
                final int newSize = this.delegate.size();
                AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
                this.removeIfEmpty();
            }
            return changed;
        }
        
        @Override
        public boolean retainAll(final Collection<?> c) {
            Preconditions.checkNotNull(c);
            final int oldSize = this.size();
            final boolean changed = this.delegate.retainAll(c);
            if (changed) {
                final int newSize = this.delegate.size();
                AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
                this.removeIfEmpty();
            }
            return changed;
        }
        
        class WrappedIterator implements Iterator<V>
        {
            final Iterator<V> delegateIterator;
            final Collection<V> originalDelegate;
            
            WrappedIterator() {
                this.originalDelegate = WrappedCollection.this.delegate;
                this.delegateIterator = (Iterator<V>)AbstractMapBasedMultimap.this.iteratorOrListIterator(WrappedCollection.this.delegate);
            }
            
            WrappedIterator(final Iterator<V> delegateIterator) {
                this.originalDelegate = WrappedCollection.this.delegate;
                this.delegateIterator = delegateIterator;
            }
            
            void validateIterator() {
                WrappedCollection.this.refreshIfEmpty();
                if (WrappedCollection.this.delegate != this.originalDelegate) {
                    throw new ConcurrentModificationException();
                }
            }
            
            @Override
            public boolean hasNext() {
                this.validateIterator();
                return this.delegateIterator.hasNext();
            }
            
            @Override
            public V next() {
                this.validateIterator();
                return this.delegateIterator.next();
            }
            
            @Override
            public void remove() {
                this.delegateIterator.remove();
                AbstractMapBasedMultimap.this.totalSize--;
                WrappedCollection.this.removeIfEmpty();
            }
            
            Iterator<V> getDelegateIterator() {
                this.validateIterator();
                return this.delegateIterator;
            }
        }
    }
    
    private class WrappedSet extends WrappedCollection implements Set<V>
    {
        WrappedSet(final K key, final Set<V> delegate) {
            super(key, delegate, null);
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            if (c.isEmpty()) {
                return false;
            }
            final int oldSize = this.size();
            final boolean changed = Sets.removeAllImpl((Set)this.delegate, c);
            if (changed) {
                final int newSize = this.delegate.size();
                AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
                this.removeIfEmpty();
            }
            return changed;
        }
    }
    
    private class WrappedSortedSet extends WrappedCollection implements SortedSet<V>
    {
        WrappedSortedSet(final K key, @Nullable final SortedSet<V> delegate, final WrappedCollection ancestor) {
            super(key, delegate, ancestor);
        }
        
        SortedSet<V> getSortedSetDelegate() {
            return (SortedSet<V>)(SortedSet)this.getDelegate();
        }
        
        @Override
        public Comparator<? super V> comparator() {
            return this.getSortedSetDelegate().comparator();
        }
        
        @Override
        public V first() {
            this.refreshIfEmpty();
            return this.getSortedSetDelegate().first();
        }
        
        @Override
        public V last() {
            this.refreshIfEmpty();
            return this.getSortedSetDelegate().last();
        }
        
        @Override
        public SortedSet<V> headSet(final V toElement) {
            this.refreshIfEmpty();
            return new WrappedSortedSet(this.getKey(), this.getSortedSetDelegate().headSet(toElement), (this.getAncestor() == null) ? this : this.getAncestor());
        }
        
        @Override
        public SortedSet<V> subSet(final V fromElement, final V toElement) {
            this.refreshIfEmpty();
            return new WrappedSortedSet(this.getKey(), this.getSortedSetDelegate().subSet(fromElement, toElement), (this.getAncestor() == null) ? this : this.getAncestor());
        }
        
        @Override
        public SortedSet<V> tailSet(final V fromElement) {
            this.refreshIfEmpty();
            return new WrappedSortedSet(this.getKey(), this.getSortedSetDelegate().tailSet(fromElement), (this.getAncestor() == null) ? this : this.getAncestor());
        }
    }
    
    @GwtIncompatible("NavigableSet")
    class WrappedNavigableSet extends WrappedSortedSet implements NavigableSet<V>
    {
        WrappedNavigableSet(final K key, @Nullable final NavigableSet<V> delegate, final WrappedCollection ancestor) {
            super(key, delegate, ancestor);
        }
        
        @Override
        NavigableSet<V> getSortedSetDelegate() {
            return (NavigableSet<V>)(NavigableSet)super.getSortedSetDelegate();
        }
        
        @Override
        public V lower(final V v) {
            return this.getSortedSetDelegate().lower(v);
        }
        
        @Override
        public V floor(final V v) {
            return this.getSortedSetDelegate().floor(v);
        }
        
        @Override
        public V ceiling(final V v) {
            return this.getSortedSetDelegate().ceiling(v);
        }
        
        @Override
        public V higher(final V v) {
            return this.getSortedSetDelegate().higher(v);
        }
        
        @Override
        public V pollFirst() {
            return Iterators.pollNext(this.iterator());
        }
        
        @Override
        public V pollLast() {
            return Iterators.pollNext(this.descendingIterator());
        }
        
        private NavigableSet<V> wrap(final NavigableSet<V> wrapped) {
            return new WrappedNavigableSet(this.key, wrapped, (this.getAncestor() == null) ? this : this.getAncestor());
        }
        
        @Override
        public NavigableSet<V> descendingSet() {
            return this.wrap(this.getSortedSetDelegate().descendingSet());
        }
        
        @Override
        public Iterator<V> descendingIterator() {
            return new WrappedIterator(this.getSortedSetDelegate().descendingIterator());
        }
        
        @Override
        public NavigableSet<V> subSet(final V fromElement, final boolean fromInclusive, final V toElement, final boolean toInclusive) {
            return this.wrap(this.getSortedSetDelegate().subSet(fromElement, fromInclusive, toElement, toInclusive));
        }
        
        @Override
        public NavigableSet<V> headSet(final V toElement, final boolean inclusive) {
            return this.wrap(this.getSortedSetDelegate().headSet(toElement, inclusive));
        }
        
        @Override
        public NavigableSet<V> tailSet(final V fromElement, final boolean inclusive) {
            return this.wrap(this.getSortedSetDelegate().tailSet(fromElement, inclusive));
        }
    }
    
    private class WrappedList extends WrappedCollection implements List<V>
    {
        WrappedList(final K key, @Nullable final List<V> delegate, final WrappedCollection ancestor) {
            super(key, delegate, ancestor);
        }
        
        List<V> getListDelegate() {
            return (List<V>)(List)this.getDelegate();
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends V> c) {
            if (c.isEmpty()) {
                return false;
            }
            final int oldSize = this.size();
            final boolean changed = this.getListDelegate().addAll(index, c);
            if (changed) {
                final int newSize = this.getDelegate().size();
                AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
                if (oldSize == 0) {
                    this.addToMap();
                }
            }
            return changed;
        }
        
        @Override
        public V get(final int index) {
            this.refreshIfEmpty();
            return this.getListDelegate().get(index);
        }
        
        @Override
        public V set(final int index, final V element) {
            this.refreshIfEmpty();
            return this.getListDelegate().set(index, element);
        }
        
        @Override
        public void add(final int index, final V element) {
            this.refreshIfEmpty();
            final boolean wasEmpty = this.getDelegate().isEmpty();
            this.getListDelegate().add(index, element);
            AbstractMapBasedMultimap.this.totalSize++;
            if (wasEmpty) {
                this.addToMap();
            }
        }
        
        @Override
        public V remove(final int index) {
            this.refreshIfEmpty();
            final V value = this.getListDelegate().remove(index);
            AbstractMapBasedMultimap.this.totalSize--;
            this.removeIfEmpty();
            return value;
        }
        
        @Override
        public int indexOf(final Object o) {
            this.refreshIfEmpty();
            return this.getListDelegate().indexOf(o);
        }
        
        @Override
        public int lastIndexOf(final Object o) {
            this.refreshIfEmpty();
            return this.getListDelegate().lastIndexOf(o);
        }
        
        @Override
        public ListIterator<V> listIterator() {
            this.refreshIfEmpty();
            return new WrappedListIterator();
        }
        
        @Override
        public ListIterator<V> listIterator(final int index) {
            this.refreshIfEmpty();
            return new WrappedListIterator(index);
        }
        
        @Override
        public List<V> subList(final int fromIndex, final int toIndex) {
            this.refreshIfEmpty();
            return AbstractMapBasedMultimap.this.wrapList(this.getKey(), this.getListDelegate().subList(fromIndex, toIndex), (this.getAncestor() == null) ? this : this.getAncestor());
        }
        
        private class WrappedListIterator extends WrappedIterator implements ListIterator<V>
        {
            WrappedListIterator() {
            }
            
            public WrappedListIterator(final int index) {
                super(WrappedList.this.getListDelegate().listIterator(index));
            }
            
            private ListIterator<V> getDelegateListIterator() {
                return (ListIterator<V>)(ListIterator)this.getDelegateIterator();
            }
            
            @Override
            public boolean hasPrevious() {
                return this.getDelegateListIterator().hasPrevious();
            }
            
            @Override
            public V previous() {
                return this.getDelegateListIterator().previous();
            }
            
            @Override
            public int nextIndex() {
                return this.getDelegateListIterator().nextIndex();
            }
            
            @Override
            public int previousIndex() {
                return this.getDelegateListIterator().previousIndex();
            }
            
            @Override
            public void set(final V value) {
                this.getDelegateListIterator().set(value);
            }
            
            @Override
            public void add(final V value) {
                final boolean wasEmpty = WrappedList.this.isEmpty();
                this.getDelegateListIterator().add(value);
                AbstractMapBasedMultimap.this.totalSize++;
                if (wasEmpty) {
                    WrappedList.this.addToMap();
                }
            }
        }
    }
    
    private class RandomAccessWrappedList extends WrappedList implements RandomAccess
    {
        RandomAccessWrappedList(final K key, @Nullable final List<V> delegate, final WrappedCollection ancestor) {
            super(key, delegate, ancestor);
        }
    }
    
    private class KeySet extends Maps.KeySet<K, Collection<V>>
    {
        KeySet(final Map<K, Collection<V>> subMap) {
            super(subMap);
        }
        
        @Override
        public Iterator<K> iterator() {
            final Iterator<Map.Entry<K, Collection<V>>> entryIterator = this.map().entrySet().iterator();
            return new Iterator<K>() {
                Map.Entry<K, Collection<V>> entry;
                
                @Override
                public boolean hasNext() {
                    return entryIterator.hasNext();
                }
                
                @Override
                public K next() {
                    this.entry = entryIterator.next();
                    return this.entry.getKey();
                }
                
                @Override
                public void remove() {
                    CollectPreconditions.checkRemove(this.entry != null);
                    final Collection<V> collection = this.entry.getValue();
                    entryIterator.remove();
                    AbstractMapBasedMultimap.this.totalSize -= collection.size();
                    collection.clear();
                }
            };
        }
        
        @Override
        public boolean remove(final Object key) {
            int count = 0;
            final Collection<V> collection = this.map().remove(key);
            if (collection != null) {
                count = collection.size();
                collection.clear();
                AbstractMapBasedMultimap.this.totalSize -= count;
            }
            return count > 0;
        }
        
        @Override
        public void clear() {
            Iterators.clear(this.iterator());
        }
        
        @Override
        public boolean containsAll(final Collection<?> c) {
            return this.map().keySet().containsAll(c);
        }
        
        @Override
        public boolean equals(@Nullable final Object object) {
            return this == object || this.map().keySet().equals(object);
        }
        
        @Override
        public int hashCode() {
            return this.map().keySet().hashCode();
        }
    }
    
    private class SortedKeySet extends KeySet implements SortedSet<K>
    {
        SortedKeySet(final SortedMap<K, Collection<V>> subMap) {
            super(subMap);
        }
        
        SortedMap<K, Collection<V>> sortedMap() {
            return (SortedMap<K, Collection<V>>)(SortedMap)super.map();
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return this.sortedMap().comparator();
        }
        
        @Override
        public K first() {
            return this.sortedMap().firstKey();
        }
        
        @Override
        public SortedSet<K> headSet(final K toElement) {
            return new SortedKeySet(this.sortedMap().headMap(toElement));
        }
        
        @Override
        public K last() {
            return this.sortedMap().lastKey();
        }
        
        @Override
        public SortedSet<K> subSet(final K fromElement, final K toElement) {
            return new SortedKeySet(this.sortedMap().subMap(fromElement, toElement));
        }
        
        @Override
        public SortedSet<K> tailSet(final K fromElement) {
            return new SortedKeySet(this.sortedMap().tailMap(fromElement));
        }
    }
    
    @GwtIncompatible("NavigableSet")
    class NavigableKeySet extends SortedKeySet implements NavigableSet<K>
    {
        NavigableKeySet(final NavigableMap<K, Collection<V>> subMap) {
            super(subMap);
        }
        
        @Override
        NavigableMap<K, Collection<V>> sortedMap() {
            return (NavigableMap<K, Collection<V>>)(NavigableMap)super.sortedMap();
        }
        
        @Override
        public K lower(final K k) {
            return this.sortedMap().lowerKey(k);
        }
        
        @Override
        public K floor(final K k) {
            return this.sortedMap().floorKey(k);
        }
        
        @Override
        public K ceiling(final K k) {
            return this.sortedMap().ceilingKey(k);
        }
        
        @Override
        public K higher(final K k) {
            return this.sortedMap().higherKey(k);
        }
        
        @Override
        public K pollFirst() {
            return Iterators.pollNext(this.iterator());
        }
        
        @Override
        public K pollLast() {
            return Iterators.pollNext(this.descendingIterator());
        }
        
        @Override
        public NavigableSet<K> descendingSet() {
            return new NavigableKeySet(this.sortedMap().descendingMap());
        }
        
        @Override
        public Iterator<K> descendingIterator() {
            return this.descendingSet().iterator();
        }
        
        @Override
        public NavigableSet<K> headSet(final K toElement) {
            return this.headSet(toElement, false);
        }
        
        @Override
        public NavigableSet<K> headSet(final K toElement, final boolean inclusive) {
            return new NavigableKeySet(this.sortedMap().headMap(toElement, inclusive));
        }
        
        @Override
        public NavigableSet<K> subSet(final K fromElement, final K toElement) {
            return this.subSet(fromElement, true, toElement, false);
        }
        
        @Override
        public NavigableSet<K> subSet(final K fromElement, final boolean fromInclusive, final K toElement, final boolean toInclusive) {
            return new NavigableKeySet(this.sortedMap().subMap(fromElement, fromInclusive, toElement, toInclusive));
        }
        
        @Override
        public NavigableSet<K> tailSet(final K fromElement) {
            return this.tailSet(fromElement, true);
        }
        
        @Override
        public NavigableSet<K> tailSet(final K fromElement, final boolean inclusive) {
            return new NavigableKeySet(this.sortedMap().tailMap(fromElement, inclusive));
        }
    }
    
    private abstract class Itr<T> implements Iterator<T>
    {
        final Iterator<Map.Entry<K, Collection<V>>> keyIterator;
        K key;
        Collection<V> collection;
        Iterator<V> valueIterator;
        
        Itr() {
            this.keyIterator = AbstractMapBasedMultimap.this.map.entrySet().iterator();
            this.key = null;
            this.collection = null;
            this.valueIterator = Iterators.emptyModifiableIterator();
        }
        
        abstract T output(final K p0, final V p1);
        
        @Override
        public boolean hasNext() {
            return this.keyIterator.hasNext() || this.valueIterator.hasNext();
        }
        
        @Override
        public T next() {
            if (!this.valueIterator.hasNext()) {
                final Map.Entry<K, Collection<V>> mapEntry = this.keyIterator.next();
                this.key = mapEntry.getKey();
                this.collection = mapEntry.getValue();
                this.valueIterator = this.collection.iterator();
            }
            return this.output(this.key, this.valueIterator.next());
        }
        
        @Override
        public void remove() {
            this.valueIterator.remove();
            if (this.collection.isEmpty()) {
                this.keyIterator.remove();
            }
            AbstractMapBasedMultimap.this.totalSize--;
        }
    }
    
    private class AsMap extends Maps.ImprovedAbstractMap<K, Collection<V>>
    {
        final transient Map<K, Collection<V>> submap;
        
        AsMap(final Map<K, Collection<V>> submap) {
            this.submap = submap;
        }
        
        protected Set<Map.Entry<K, Collection<V>>> createEntrySet() {
            return (Set<Map.Entry<K, Collection<V>>>)new AsMapEntries();
        }
        
        @Override
        public boolean containsKey(final Object key) {
            return Maps.safeContainsKey(this.submap, key);
        }
        
        @Override
        public Collection<V> get(final Object key) {
            final Collection<V> collection = Maps.safeGet(this.submap, key);
            if (collection == null) {
                return null;
            }
            final K k = (K)key;
            return AbstractMapBasedMultimap.this.wrapCollection(k, collection);
        }
        
        @Override
        public Set<K> keySet() {
            return AbstractMapBasedMultimap.this.keySet();
        }
        
        @Override
        public int size() {
            return this.submap.size();
        }
        
        @Override
        public Collection<V> remove(final Object key) {
            final Collection<V> collection = this.submap.remove(key);
            if (collection == null) {
                return null;
            }
            final Collection<V> output = AbstractMapBasedMultimap.this.createCollection();
            output.addAll((Collection<? extends V>)collection);
            AbstractMapBasedMultimap.this.totalSize -= collection.size();
            collection.clear();
            return output;
        }
        
        @Override
        public boolean equals(@Nullable final Object object) {
            return this == object || this.submap.equals(object);
        }
        
        @Override
        public int hashCode() {
            return this.submap.hashCode();
        }
        
        @Override
        public String toString() {
            return this.submap.toString();
        }
        
        @Override
        public void clear() {
            if (this.submap == AbstractMapBasedMultimap.this.map) {
                AbstractMapBasedMultimap.this.clear();
            }
            else {
                Iterators.clear(new AsMapIterator());
            }
        }
        
        Map.Entry<K, Collection<V>> wrapEntry(final Map.Entry<K, Collection<V>> entry) {
            final K key = entry.getKey();
            return Maps.immutableEntry(key, AbstractMapBasedMultimap.this.wrapCollection(key, entry.getValue()));
        }
        
        class AsMapEntries extends Maps.EntrySet<K, Collection<V>>
        {
            @Override
            Map<K, Collection<V>> map() {
                return AsMap.this;
            }
            
            @Override
            public Iterator<Map.Entry<K, Collection<V>>> iterator() {
                return new AsMapIterator();
            }
            
            @Override
            public boolean contains(final Object o) {
                return Collections2.safeContains(AsMap.this.submap.entrySet(), o);
            }
            
            @Override
            public boolean remove(final Object o) {
                if (!this.contains(o)) {
                    return false;
                }
                final Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
                AbstractMapBasedMultimap.this.removeValuesForKey(entry.getKey());
                return true;
            }
        }
        
        class AsMapIterator implements Iterator<Map.Entry<K, Collection<V>>>
        {
            final Iterator<Map.Entry<K, Collection<V>>> delegateIterator;
            Collection<V> collection;
            
            AsMapIterator() {
                this.delegateIterator = AsMap.this.submap.entrySet().iterator();
            }
            
            @Override
            public boolean hasNext() {
                return this.delegateIterator.hasNext();
            }
            
            @Override
            public Map.Entry<K, Collection<V>> next() {
                final Map.Entry<K, Collection<V>> entry = this.delegateIterator.next();
                this.collection = entry.getValue();
                return AsMap.this.wrapEntry(entry);
            }
            
            @Override
            public void remove() {
                this.delegateIterator.remove();
                AbstractMapBasedMultimap.this.totalSize -= this.collection.size();
                this.collection.clear();
            }
        }
    }
    
    private class SortedAsMap extends AsMap implements SortedMap<K, Collection<V>>
    {
        SortedSet<K> sortedKeySet;
        
        SortedAsMap(final SortedMap<K, Collection<V>> submap) {
            super(submap);
        }
        
        SortedMap<K, Collection<V>> sortedMap() {
            return (SortedMap<K, Collection<V>>)(SortedMap)this.submap;
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return this.sortedMap().comparator();
        }
        
        @Override
        public K firstKey() {
            return this.sortedMap().firstKey();
        }
        
        @Override
        public K lastKey() {
            return this.sortedMap().lastKey();
        }
        
        @Override
        public SortedMap<K, Collection<V>> headMap(final K toKey) {
            return new SortedAsMap(this.sortedMap().headMap(toKey));
        }
        
        @Override
        public SortedMap<K, Collection<V>> subMap(final K fromKey, final K toKey) {
            return new SortedAsMap(this.sortedMap().subMap(fromKey, toKey));
        }
        
        @Override
        public SortedMap<K, Collection<V>> tailMap(final K fromKey) {
            return new SortedAsMap(this.sortedMap().tailMap(fromKey));
        }
        
        @Override
        public SortedSet<K> keySet() {
            final SortedSet<K> result = this.sortedKeySet;
            return (result == null) ? (this.sortedKeySet = this.createKeySet()) : result;
        }
        
        @Override
        SortedSet<K> createKeySet() {
            return new AbstractMapBasedMultimap.SortedKeySet(this.sortedMap());
        }
    }
    
    @GwtIncompatible("NavigableAsMap")
    class NavigableAsMap extends SortedAsMap implements NavigableMap<K, Collection<V>>
    {
        NavigableAsMap(final NavigableMap<K, Collection<V>> submap) {
            super(submap);
        }
        
        @Override
        NavigableMap<K, Collection<V>> sortedMap() {
            return (NavigableMap<K, Collection<V>>)(NavigableMap)super.sortedMap();
        }
        
        @Override
        public Map.Entry<K, Collection<V>> lowerEntry(final K key) {
            final Map.Entry<K, Collection<V>> entry = this.sortedMap().lowerEntry(key);
            return (entry == null) ? null : this.wrapEntry(entry);
        }
        
        @Override
        public K lowerKey(final K key) {
            return this.sortedMap().lowerKey(key);
        }
        
        @Override
        public Map.Entry<K, Collection<V>> floorEntry(final K key) {
            final Map.Entry<K, Collection<V>> entry = this.sortedMap().floorEntry(key);
            return (entry == null) ? null : this.wrapEntry(entry);
        }
        
        @Override
        public K floorKey(final K key) {
            return this.sortedMap().floorKey(key);
        }
        
        @Override
        public Map.Entry<K, Collection<V>> ceilingEntry(final K key) {
            final Map.Entry<K, Collection<V>> entry = this.sortedMap().ceilingEntry(key);
            return (entry == null) ? null : this.wrapEntry(entry);
        }
        
        @Override
        public K ceilingKey(final K key) {
            return this.sortedMap().ceilingKey(key);
        }
        
        @Override
        public Map.Entry<K, Collection<V>> higherEntry(final K key) {
            final Map.Entry<K, Collection<V>> entry = this.sortedMap().higherEntry(key);
            return (entry == null) ? null : this.wrapEntry(entry);
        }
        
        @Override
        public K higherKey(final K key) {
            return this.sortedMap().higherKey(key);
        }
        
        @Override
        public Map.Entry<K, Collection<V>> firstEntry() {
            final Map.Entry<K, Collection<V>> entry = this.sortedMap().firstEntry();
            return (entry == null) ? null : this.wrapEntry(entry);
        }
        
        @Override
        public Map.Entry<K, Collection<V>> lastEntry() {
            final Map.Entry<K, Collection<V>> entry = this.sortedMap().lastEntry();
            return (entry == null) ? null : this.wrapEntry(entry);
        }
        
        @Override
        public Map.Entry<K, Collection<V>> pollFirstEntry() {
            return this.pollAsMapEntry(this.entrySet().iterator());
        }
        
        @Override
        public Map.Entry<K, Collection<V>> pollLastEntry() {
            return (Map.Entry<K, Collection<V>>)this.pollAsMapEntry((Iterator<Map.Entry<Object, Collection<V>>>)this.descendingMap().entrySet().iterator());
        }
        
        Map.Entry<K, Collection<V>> pollAsMapEntry(final Iterator<Map.Entry<K, Collection<V>>> entryIterator) {
            if (!entryIterator.hasNext()) {
                return null;
            }
            final Map.Entry<K, Collection<V>> entry = entryIterator.next();
            final Collection<V> output = AbstractMapBasedMultimap.this.createCollection();
            output.addAll((Collection<? extends V>)entry.getValue());
            entryIterator.remove();
            return Maps.immutableEntry(entry.getKey(), AbstractMapBasedMultimap.this.unmodifiableCollectionSubclass(output));
        }
        
        @Override
        public NavigableMap<K, Collection<V>> descendingMap() {
            return new NavigableAsMap(this.sortedMap().descendingMap());
        }
        
        @Override
        public NavigableSet<K> keySet() {
            return (NavigableSet<K>)(NavigableSet)super.keySet();
        }
        
        @Override
        NavigableSet<K> createKeySet() {
            return new AbstractMapBasedMultimap.NavigableKeySet(this.sortedMap());
        }
        
        @Override
        public NavigableSet<K> navigableKeySet() {
            return this.keySet();
        }
        
        @Override
        public NavigableSet<K> descendingKeySet() {
            return this.descendingMap().navigableKeySet();
        }
        
        @Override
        public NavigableMap<K, Collection<V>> subMap(final K fromKey, final K toKey) {
            return this.subMap(fromKey, true, toKey, false);
        }
        
        @Override
        public NavigableMap<K, Collection<V>> subMap(final K fromKey, final boolean fromInclusive, final K toKey, final boolean toInclusive) {
            return new NavigableAsMap(this.sortedMap().subMap(fromKey, fromInclusive, toKey, toInclusive));
        }
        
        @Override
        public NavigableMap<K, Collection<V>> headMap(final K toKey) {
            return this.headMap(toKey, false);
        }
        
        @Override
        public NavigableMap<K, Collection<V>> headMap(final K toKey, final boolean inclusive) {
            return new NavigableAsMap(this.sortedMap().headMap(toKey, inclusive));
        }
        
        @Override
        public NavigableMap<K, Collection<V>> tailMap(final K fromKey) {
            return this.tailMap(fromKey, true);
        }
        
        @Override
        public NavigableMap<K, Collection<V>> tailMap(final K fromKey, final boolean inclusive) {
            return new NavigableAsMap(this.sortedMap().tailMap(fromKey, inclusive));
        }
    }
}
