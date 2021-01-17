// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.ConcurrentModificationException;
import com.google.common.base.Preconditions;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Set;
import java.util.AbstractSequentialList;
import java.util.Collections;
import java.util.ListIterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Iterator;
import javax.annotation.Nullable;
import java.util.HashMap;
import com.google.common.annotations.GwtIncompatible;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(serializable = true, emulated = true)
public class LinkedListMultimap<K, V> extends AbstractMultimap<K, V> implements ListMultimap<K, V>, Serializable
{
    private transient Node<K, V> head;
    private transient Node<K, V> tail;
    private transient Map<K, KeyList<K, V>> keyToKeyList;
    private transient int size;
    private transient int modCount;
    @GwtIncompatible("java serialization not supported")
    private static final long serialVersionUID = 0L;
    
    public static <K, V> LinkedListMultimap<K, V> create() {
        return new LinkedListMultimap<K, V>();
    }
    
    public static <K, V> LinkedListMultimap<K, V> create(final int expectedKeys) {
        return new LinkedListMultimap<K, V>(expectedKeys);
    }
    
    public static <K, V> LinkedListMultimap<K, V> create(final Multimap<? extends K, ? extends V> multimap) {
        return new LinkedListMultimap<K, V>(multimap);
    }
    
    LinkedListMultimap() {
        this.keyToKeyList = (Map<K, KeyList<K, V>>)Maps.newHashMap();
    }
    
    private LinkedListMultimap(final int expectedKeys) {
        this.keyToKeyList = new HashMap<K, KeyList<K, V>>(expectedKeys);
    }
    
    private LinkedListMultimap(final Multimap<? extends K, ? extends V> multimap) {
        this(multimap.keySet().size());
        this.putAll(multimap);
    }
    
    private Node<K, V> addNode(@Nullable final K key, @Nullable final V value, @Nullable final Node<K, V> nextSibling) {
        final Node<K, V> node = new Node<K, V>(key, value);
        if (this.head == null) {
            final Node<K, V> node2 = node;
            this.tail = node2;
            this.head = node2;
            this.keyToKeyList.put(key, new KeyList<K, V>(node));
            ++this.modCount;
        }
        else if (nextSibling == null) {
            this.tail.next = node;
            node.previous = this.tail;
            this.tail = node;
            KeyList<K, V> keyList = this.keyToKeyList.get(key);
            if (keyList == null) {
                this.keyToKeyList.put(key, keyList = new KeyList<K, V>(node));
                ++this.modCount;
            }
            else {
                final KeyList<K, V> list = keyList;
                ++list.count;
                final Node<K, V> keyTail = keyList.tail;
                keyTail.nextSibling = node;
                node.previousSibling = keyTail;
                keyList.tail = node;
            }
        }
        else {
            final KeyList<K, V> list2;
            final KeyList<K, V> keyList = list2 = this.keyToKeyList.get(key);
            ++list2.count;
            node.previous = nextSibling.previous;
            node.previousSibling = nextSibling.previousSibling;
            node.next = nextSibling;
            node.nextSibling = nextSibling;
            if (nextSibling.previousSibling == null) {
                this.keyToKeyList.get(key).head = node;
            }
            else {
                nextSibling.previousSibling.nextSibling = node;
            }
            if (nextSibling.previous == null) {
                this.head = node;
            }
            else {
                nextSibling.previous.next = node;
            }
            nextSibling.previous = node;
            nextSibling.previousSibling = node;
        }
        ++this.size;
        return node;
    }
    
    private void removeNode(final Node<K, V> node) {
        if (node.previous != null) {
            node.previous.next = node.next;
        }
        else {
            this.head = node.next;
        }
        if (node.next != null) {
            node.next.previous = node.previous;
        }
        else {
            this.tail = node.previous;
        }
        if (node.previousSibling == null && node.nextSibling == null) {
            final KeyList<K, V> keyList = this.keyToKeyList.remove(node.key);
            keyList.count = 0;
            ++this.modCount;
        }
        else {
            final KeyList<K, V> list;
            final KeyList<K, V> keyList = list = this.keyToKeyList.get(node.key);
            --list.count;
            if (node.previousSibling == null) {
                keyList.head = node.nextSibling;
            }
            else {
                node.previousSibling.nextSibling = node.nextSibling;
            }
            if (node.nextSibling == null) {
                keyList.tail = node.previousSibling;
            }
            else {
                node.nextSibling.previousSibling = node.previousSibling;
            }
        }
        --this.size;
    }
    
    private void removeAllNodes(@Nullable final Object key) {
        Iterators.clear(new ValueForKeyIterator(key));
    }
    
    private static void checkElement(@Nullable final Object node) {
        if (node == null) {
            throw new NoSuchElementException();
        }
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.head == null;
    }
    
    @Override
    public boolean containsKey(@Nullable final Object key) {
        return this.keyToKeyList.containsKey(key);
    }
    
    @Override
    public boolean containsValue(@Nullable final Object value) {
        return this.values().contains(value);
    }
    
    @Override
    public boolean put(@Nullable final K key, @Nullable final V value) {
        this.addNode(key, value, null);
        return true;
    }
    
    @Override
    public List<V> replaceValues(@Nullable final K key, final Iterable<? extends V> values) {
        final List<V> oldValues = this.getCopy(key);
        final ListIterator<V> keyValues = new ValueForKeyIterator(key);
        final Iterator<? extends V> newValues = values.iterator();
        while (keyValues.hasNext() && newValues.hasNext()) {
            keyValues.next();
            keyValues.set((V)newValues.next());
        }
        while (keyValues.hasNext()) {
            keyValues.next();
            keyValues.remove();
        }
        while (newValues.hasNext()) {
            keyValues.add((V)newValues.next());
        }
        return oldValues;
    }
    
    private List<V> getCopy(@Nullable final Object key) {
        return Collections.unmodifiableList((List<? extends V>)Lists.newArrayList((Iterator<?>)new ValueForKeyIterator(key)));
    }
    
    @Override
    public List<V> removeAll(@Nullable final Object key) {
        final List<V> oldValues = this.getCopy(key);
        this.removeAllNodes(key);
        return oldValues;
    }
    
    @Override
    public void clear() {
        this.head = null;
        this.tail = null;
        this.keyToKeyList.clear();
        this.size = 0;
        ++this.modCount;
    }
    
    @Override
    public List<V> get(@Nullable final K key) {
        return new AbstractSequentialList<V>() {
            @Override
            public int size() {
                final KeyList<K, V> keyList = LinkedListMultimap.this.keyToKeyList.get(key);
                return (keyList == null) ? 0 : keyList.count;
            }
            
            @Override
            public ListIterator<V> listIterator(final int index) {
                return new ValueForKeyIterator(key, index);
            }
        };
    }
    
    @Override
    Set<K> createKeySet() {
        return new Sets.ImprovedAbstractSet<K>() {
            @Override
            public int size() {
                return LinkedListMultimap.this.keyToKeyList.size();
            }
            
            @Override
            public Iterator<K> iterator() {
                return new DistinctKeyIterator();
            }
            
            @Override
            public boolean contains(final Object key) {
                return LinkedListMultimap.this.containsKey(key);
            }
            
            @Override
            public boolean remove(final Object o) {
                return !LinkedListMultimap.this.removeAll(o).isEmpty();
            }
        };
    }
    
    @Override
    public List<V> values() {
        return (List<V>)(List)super.values();
    }
    
    @Override
    List<V> createValues() {
        return new AbstractSequentialList<V>() {
            @Override
            public int size() {
                return LinkedListMultimap.this.size;
            }
            
            @Override
            public ListIterator<V> listIterator(final int index) {
                final NodeIterator nodeItr = new NodeIterator(index);
                return new TransformedListIterator<Map.Entry<K, V>, V>(nodeItr) {
                    @Override
                    V transform(final Map.Entry<K, V> entry) {
                        return entry.getValue();
                    }
                    
                    @Override
                    public void set(final V value) {
                        nodeItr.setValue(value);
                    }
                };
            }
        };
    }
    
    @Override
    public List<Map.Entry<K, V>> entries() {
        return (List<Map.Entry<K, V>>)(List)super.entries();
    }
    
    @Override
    List<Map.Entry<K, V>> createEntries() {
        return new AbstractSequentialList<Map.Entry<K, V>>() {
            @Override
            public int size() {
                return LinkedListMultimap.this.size;
            }
            
            @Override
            public ListIterator<Map.Entry<K, V>> listIterator(final int index) {
                return new NodeIterator(index);
            }
        };
    }
    
    @Override
    Iterator<Map.Entry<K, V>> entryIterator() {
        throw new AssertionError((Object)"should never be called");
    }
    
    @Override
    Map<K, Collection<V>> createAsMap() {
        return (Map<K, Collection<V>>)new Multimaps.AsMap((Multimap<Object, Object>)this);
    }
    
    @GwtIncompatible("java.io.ObjectOutputStream")
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeInt(this.size());
        for (final Map.Entry<K, V> entry : this.entries()) {
            stream.writeObject(entry.getKey());
            stream.writeObject(entry.getValue());
        }
    }
    
    @GwtIncompatible("java.io.ObjectInputStream")
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.keyToKeyList = (Map<K, KeyList<K, V>>)Maps.newLinkedHashMap();
        for (int size = stream.readInt(), i = 0; i < size; ++i) {
            final K key = (K)stream.readObject();
            final V value = (V)stream.readObject();
            this.put(key, value);
        }
    }
    
    private static final class Node<K, V> extends AbstractMapEntry<K, V>
    {
        final K key;
        V value;
        Node<K, V> next;
        Node<K, V> previous;
        Node<K, V> nextSibling;
        Node<K, V> previousSibling;
        
        Node(@Nullable final K key, @Nullable final V value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public K getKey() {
            return this.key;
        }
        
        @Override
        public V getValue() {
            return this.value;
        }
        
        @Override
        public V setValue(@Nullable final V newValue) {
            final V result = this.value;
            this.value = newValue;
            return result;
        }
    }
    
    private static class KeyList<K, V>
    {
        Node<K, V> head;
        Node<K, V> tail;
        int count;
        
        KeyList(final Node<K, V> firstNode) {
            this.head = firstNode;
            this.tail = firstNode;
            firstNode.previousSibling = null;
            firstNode.nextSibling = null;
            this.count = 1;
        }
    }
    
    private class NodeIterator implements ListIterator<Map.Entry<K, V>>
    {
        int nextIndex;
        Node<K, V> next;
        Node<K, V> current;
        Node<K, V> previous;
        int expectedModCount;
        
        NodeIterator(int index) {
            this.expectedModCount = LinkedListMultimap.this.modCount;
            final int size = LinkedListMultimap.this.size();
            Preconditions.checkPositionIndex(index, size);
            if (index >= size / 2) {
                this.previous = LinkedListMultimap.this.tail;
                this.nextIndex = size;
                while (index++ < size) {
                    this.previous();
                }
            }
            else {
                this.next = LinkedListMultimap.this.head;
                while (index-- > 0) {
                    this.next();
                }
            }
            this.current = null;
        }
        
        private void checkForConcurrentModification() {
            if (LinkedListMultimap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
        
        @Override
        public boolean hasNext() {
            this.checkForConcurrentModification();
            return this.next != null;
        }
        
        @Override
        public Node<K, V> next() {
            this.checkForConcurrentModification();
            checkElement(this.next);
            final Node<K, V> next = this.next;
            this.current = next;
            this.previous = next;
            this.next = this.next.next;
            ++this.nextIndex;
            return this.current;
        }
        
        @Override
        public void remove() {
            this.checkForConcurrentModification();
            CollectPreconditions.checkRemove(this.current != null);
            if (this.current != this.next) {
                this.previous = this.current.previous;
                --this.nextIndex;
            }
            else {
                this.next = this.current.next;
            }
            LinkedListMultimap.this.removeNode(this.current);
            this.current = null;
            this.expectedModCount = LinkedListMultimap.this.modCount;
        }
        
        @Override
        public boolean hasPrevious() {
            this.checkForConcurrentModification();
            return this.previous != null;
        }
        
        @Override
        public Node<K, V> previous() {
            this.checkForConcurrentModification();
            checkElement(this.previous);
            final Node<K, V> previous = this.previous;
            this.current = previous;
            this.next = previous;
            this.previous = this.previous.previous;
            --this.nextIndex;
            return this.current;
        }
        
        @Override
        public int nextIndex() {
            return this.nextIndex;
        }
        
        @Override
        public int previousIndex() {
            return this.nextIndex - 1;
        }
        
        @Override
        public void set(final Map.Entry<K, V> e) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final Map.Entry<K, V> e) {
            throw new UnsupportedOperationException();
        }
        
        void setValue(final V value) {
            Preconditions.checkState(this.current != null);
            this.current.value = value;
        }
    }
    
    private class DistinctKeyIterator implements Iterator<K>
    {
        final Set<K> seenKeys;
        Node<K, V> next;
        Node<K, V> current;
        int expectedModCount;
        
        private DistinctKeyIterator() {
            this.seenKeys = (Set<K>)Sets.newHashSetWithExpectedSize(LinkedListMultimap.this.keySet().size());
            this.next = LinkedListMultimap.this.head;
            this.expectedModCount = LinkedListMultimap.this.modCount;
        }
        
        private void checkForConcurrentModification() {
            if (LinkedListMultimap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
        
        @Override
        public boolean hasNext() {
            this.checkForConcurrentModification();
            return this.next != null;
        }
        
        @Override
        public K next() {
            this.checkForConcurrentModification();
            checkElement(this.next);
            this.current = this.next;
            this.seenKeys.add(this.current.key);
            do {
                this.next = this.next.next;
            } while (this.next != null && !this.seenKeys.add(this.next.key));
            return this.current.key;
        }
        
        @Override
        public void remove() {
            this.checkForConcurrentModification();
            CollectPreconditions.checkRemove(this.current != null);
            LinkedListMultimap.this.removeAllNodes(this.current.key);
            this.current = null;
            this.expectedModCount = LinkedListMultimap.this.modCount;
        }
    }
    
    private class ValueForKeyIterator implements ListIterator<V>
    {
        final Object key;
        int nextIndex;
        Node<K, V> next;
        Node<K, V> current;
        Node<K, V> previous;
        
        ValueForKeyIterator(final Object key) {
            this.key = key;
            final KeyList<K, V> keyList = LinkedListMultimap.this.keyToKeyList.get(key);
            this.next = ((keyList == null) ? null : keyList.head);
        }
        
        public ValueForKeyIterator(final Object key, int index) {
            final KeyList<K, V> keyList = LinkedListMultimap.this.keyToKeyList.get(key);
            final int size = (keyList == null) ? 0 : keyList.count;
            Preconditions.checkPositionIndex(index, size);
            if (index >= size / 2) {
                this.previous = ((keyList == null) ? null : keyList.tail);
                this.nextIndex = size;
                while (index++ < size) {
                    this.previous();
                }
            }
            else {
                this.next = ((keyList == null) ? null : keyList.head);
                while (index-- > 0) {
                    this.next();
                }
            }
            this.key = key;
            this.current = null;
        }
        
        @Override
        public boolean hasNext() {
            return this.next != null;
        }
        
        @Override
        public V next() {
            checkElement(this.next);
            final Node<K, V> next = this.next;
            this.current = next;
            this.previous = next;
            this.next = this.next.nextSibling;
            ++this.nextIndex;
            return this.current.value;
        }
        
        @Override
        public boolean hasPrevious() {
            return this.previous != null;
        }
        
        @Override
        public V previous() {
            checkElement(this.previous);
            final Node<K, V> previous = this.previous;
            this.current = previous;
            this.next = previous;
            this.previous = this.previous.previousSibling;
            --this.nextIndex;
            return this.current.value;
        }
        
        @Override
        public int nextIndex() {
            return this.nextIndex;
        }
        
        @Override
        public int previousIndex() {
            return this.nextIndex - 1;
        }
        
        @Override
        public void remove() {
            CollectPreconditions.checkRemove(this.current != null);
            if (this.current != this.next) {
                this.previous = this.current.previousSibling;
                --this.nextIndex;
            }
            else {
                this.next = this.current.nextSibling;
            }
            LinkedListMultimap.this.removeNode(this.current);
            this.current = null;
        }
        
        @Override
        public void set(final V value) {
            Preconditions.checkState(this.current != null);
            this.current.value = value;
        }
        
        @Override
        public void add(final V value) {
            this.previous = (Node<K, V>)LinkedListMultimap.this.addNode(this.key, value, this.next);
            ++this.nextIndex;
            this.current = null;
        }
    }
}
