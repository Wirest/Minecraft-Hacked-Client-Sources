// 
// Decompiled by Procyon v0.5.36
// 

package com.google.gson.internal;

import java.util.AbstractSet;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.io.ObjectStreamException;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map;
import java.util.Comparator;
import java.io.Serializable;
import java.util.AbstractMap;

public final class LinkedTreeMap<K, V> extends AbstractMap<K, V> implements Serializable
{
    private static final Comparator<Comparable> NATURAL_ORDER;
    Comparator<? super K> comparator;
    Node<K, V> root;
    int size;
    int modCount;
    final Node<K, V> header;
    private EntrySet entrySet;
    private KeySet keySet;
    
    public LinkedTreeMap() {
        this((Comparator)LinkedTreeMap.NATURAL_ORDER);
    }
    
    public LinkedTreeMap(final Comparator<? super K> comparator) {
        this.size = 0;
        this.modCount = 0;
        this.header = new Node<K, V>();
        this.comparator = (Comparator<? super K>)((comparator != null) ? comparator : LinkedTreeMap.NATURAL_ORDER);
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public V get(final Object key) {
        final Node<K, V> node = this.findByObject(key);
        return (node != null) ? node.value : null;
    }
    
    @Override
    public boolean containsKey(final Object key) {
        return this.findByObject(key) != null;
    }
    
    @Override
    public V put(final K key, final V value) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        final Node<K, V> created = this.find(key, true);
        final V result = created.value;
        created.value = value;
        return result;
    }
    
    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
        ++this.modCount;
        final Node<K, V> header;
        final Node<K, V> node2;
        final Node<K, V> node = node2 = (header = this.header);
        node.prev = node2;
        header.next = node2;
    }
    
    @Override
    public V remove(final Object key) {
        final Node<K, V> node = this.removeInternalByKey(key);
        return (node != null) ? node.value : null;
    }
    
    Node<K, V> find(final K key, final boolean create) {
        final Comparator<? super K> comparator = this.comparator;
        Node<K, V> nearest = this.root;
        int comparison = 0;
        if (nearest != null) {
            final Comparable<Object> comparableKey = (Comparable<Object>)((comparator == LinkedTreeMap.NATURAL_ORDER) ? ((Comparable)key) : null);
            while (true) {
                comparison = ((comparableKey != null) ? comparableKey.compareTo(nearest.key) : comparator.compare((Object)key, (Object)nearest.key));
                if (comparison == 0) {
                    return nearest;
                }
                final Node<K, V> child = (comparison < 0) ? nearest.left : nearest.right;
                if (child == null) {
                    break;
                }
                nearest = child;
            }
        }
        if (!create) {
            return null;
        }
        final Node<K, V> header = this.header;
        Node<K, V> created;
        if (nearest == null) {
            if (comparator == LinkedTreeMap.NATURAL_ORDER && !(key instanceof Comparable)) {
                throw new ClassCastException(key.getClass().getName() + " is not Comparable");
            }
            created = new Node<K, V>(nearest, key, header, header.prev);
            this.root = created;
        }
        else {
            created = new Node<K, V>(nearest, key, header, header.prev);
            if (comparison < 0) {
                nearest.left = created;
            }
            else {
                nearest.right = created;
            }
            this.rebalance(nearest, true);
        }
        ++this.size;
        ++this.modCount;
        return created;
    }
    
    Node<K, V> findByObject(final Object key) {
        try {
            return (Node<K, V>)((key != null) ? this.find(key, false) : null);
        }
        catch (ClassCastException e) {
            return null;
        }
    }
    
    Node<K, V> findByEntry(final Map.Entry<?, ?> entry) {
        final Node<K, V> mine = this.findByObject(entry.getKey());
        final boolean valuesEqual = mine != null && this.equal(mine.value, entry.getValue());
        return valuesEqual ? mine : null;
    }
    
    private boolean equal(final Object a, final Object b) {
        return a == b || (a != null && a.equals(b));
    }
    
    void removeInternal(final Node<K, V> node, final boolean unlink) {
        if (unlink) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        Node<K, V> left = node.left;
        Node<K, V> right = node.right;
        final Node<K, V> originalParent = node.parent;
        if (left != null && right != null) {
            final Node<K, V> adjacent = (left.height > right.height) ? left.last() : right.first();
            this.removeInternal(adjacent, false);
            int leftHeight = 0;
            left = node.left;
            if (left != null) {
                leftHeight = left.height;
                adjacent.left = left;
                left.parent = adjacent;
                node.left = null;
            }
            int rightHeight = 0;
            right = node.right;
            if (right != null) {
                rightHeight = right.height;
                adjacent.right = right;
                right.parent = adjacent;
                node.right = null;
            }
            adjacent.height = Math.max(leftHeight, rightHeight) + 1;
            this.replaceInParent(node, adjacent);
            return;
        }
        if (left != null) {
            this.replaceInParent(node, left);
            node.left = null;
        }
        else if (right != null) {
            this.replaceInParent(node, right);
            node.right = null;
        }
        else {
            this.replaceInParent(node, null);
        }
        this.rebalance(originalParent, false);
        --this.size;
        ++this.modCount;
    }
    
    Node<K, V> removeInternalByKey(final Object key) {
        final Node<K, V> node = this.findByObject(key);
        if (node != null) {
            this.removeInternal(node, true);
        }
        return node;
    }
    
    private void replaceInParent(final Node<K, V> node, final Node<K, V> replacement) {
        final Node<K, V> parent = node.parent;
        node.parent = null;
        if (replacement != null) {
            replacement.parent = parent;
        }
        if (parent != null) {
            if (parent.left == node) {
                parent.left = replacement;
            }
            else {
                assert parent.right == node;
                parent.right = replacement;
            }
        }
        else {
            this.root = replacement;
        }
    }
    
    private void rebalance(final Node<K, V> unbalanced, final boolean insert) {
        for (Node<K, V> node = unbalanced; node != null; node = node.parent) {
            final Node<K, V> left = node.left;
            final Node<K, V> right = node.right;
            final int leftHeight = (left != null) ? left.height : 0;
            final int rightHeight = (right != null) ? right.height : 0;
            final int delta = leftHeight - rightHeight;
            if (delta == -2) {
                final Node<K, V> rightLeft = right.left;
                final Node<K, V> rightRight = right.right;
                final int rightRightHeight = (rightRight != null) ? rightRight.height : 0;
                final int rightLeftHeight = (rightLeft != null) ? rightLeft.height : 0;
                final int rightDelta = rightLeftHeight - rightRightHeight;
                if (rightDelta == -1 || (rightDelta == 0 && !insert)) {
                    this.rotateLeft(node);
                }
                else {
                    assert rightDelta == 1;
                    this.rotateRight(right);
                    this.rotateLeft(node);
                }
                if (insert) {
                    break;
                }
            }
            else if (delta == 2) {
                final Node<K, V> leftLeft = left.left;
                final Node<K, V> leftRight = left.right;
                final int leftRightHeight = (leftRight != null) ? leftRight.height : 0;
                final int leftLeftHeight = (leftLeft != null) ? leftLeft.height : 0;
                final int leftDelta = leftLeftHeight - leftRightHeight;
                if (leftDelta == 1 || (leftDelta == 0 && !insert)) {
                    this.rotateRight(node);
                }
                else {
                    assert leftDelta == -1;
                    this.rotateLeft(left);
                    this.rotateRight(node);
                }
                if (insert) {
                    break;
                }
            }
            else if (delta == 0) {
                node.height = leftHeight + 1;
                if (insert) {
                    break;
                }
            }
            else {
                assert delta == 1;
                node.height = Math.max(leftHeight, rightHeight) + 1;
                if (!insert) {
                    break;
                }
            }
        }
    }
    
    private void rotateLeft(final Node<K, V> root) {
        final Node<K, V> left = root.left;
        final Node<K, V> pivot = root.right;
        final Node<K, V> pivotLeft = pivot.left;
        final Node<K, V> pivotRight = pivot.right;
        root.right = pivotLeft;
        if (pivotLeft != null) {
            pivotLeft.parent = root;
        }
        this.replaceInParent(root, pivot);
        pivot.left = root;
        root.parent = pivot;
        root.height = Math.max((left != null) ? left.height : 0, (pivotLeft != null) ? pivotLeft.height : 0) + 1;
        pivot.height = Math.max(root.height, (pivotRight != null) ? pivotRight.height : 0) + 1;
    }
    
    private void rotateRight(final Node<K, V> root) {
        final Node<K, V> pivot = root.left;
        final Node<K, V> right = root.right;
        final Node<K, V> pivotLeft = pivot.left;
        final Node<K, V> pivotRight = pivot.right;
        root.left = pivotRight;
        if (pivotRight != null) {
            pivotRight.parent = root;
        }
        this.replaceInParent(root, pivot);
        pivot.right = root;
        root.parent = pivot;
        root.height = Math.max((right != null) ? right.height : 0, (pivotRight != null) ? pivotRight.height : 0) + 1;
        pivot.height = Math.max(root.height, (pivotLeft != null) ? pivotLeft.height : 0) + 1;
    }
    
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        final EntrySet result = this.entrySet;
        return (result != null) ? result : (this.entrySet = new EntrySet());
    }
    
    @Override
    public Set<K> keySet() {
        final KeySet result = this.keySet;
        return (result != null) ? result : (this.keySet = new KeySet());
    }
    
    private Object writeReplace() throws ObjectStreamException {
        return new LinkedHashMap(this);
    }
    
    static {
        NATURAL_ORDER = new Comparator<Comparable>() {
            public int compare(final Comparable a, final Comparable b) {
                return a.compareTo(b);
            }
        };
    }
    
    static final class Node<K, V> implements Map.Entry<K, V>
    {
        Node<K, V> parent;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> next;
        Node<K, V> prev;
        final K key;
        V value;
        int height;
        
        Node() {
            this.key = null;
            this.prev = this;
            this.next = this;
        }
        
        Node(final Node<K, V> parent, final K key, final Node<K, V> next, final Node<K, V> prev) {
            this.parent = parent;
            this.key = key;
            this.height = 1;
            this.next = next;
            this.prev = prev;
            prev.next = this;
            next.prev = this;
        }
        
        public K getKey() {
            return this.key;
        }
        
        public V getValue() {
            return this.value;
        }
        
        public V setValue(final V value) {
            final V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o instanceof Map.Entry) {
                final Map.Entry other = (Map.Entry)o;
                if (this.key == null) {
                    if (other.getKey() != null) {
                        return false;
                    }
                }
                else if (!this.key.equals(other.getKey())) {
                    return false;
                }
                if ((this.value != null) ? this.value.equals(other.getValue()) : (other.getValue() == null)) {
                    return true;
                }
                return false;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return ((this.key == null) ? 0 : this.key.hashCode()) ^ ((this.value == null) ? 0 : this.value.hashCode());
        }
        
        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
        
        public Node<K, V> first() {
            Node<K, V> node = this;
            for (Node<K, V> child = node.left; child != null; child = node.left) {
                node = child;
            }
            return node;
        }
        
        public Node<K, V> last() {
            Node<K, V> node = this;
            for (Node<K, V> child = node.right; child != null; child = node.right) {
                node = child;
            }
            return node;
        }
    }
    
    private abstract class LinkedTreeMapIterator<T> implements Iterator<T>
    {
        Node<K, V> next;
        Node<K, V> lastReturned;
        int expectedModCount;
        
        private LinkedTreeMapIterator() {
            this.next = LinkedTreeMap.this.header.next;
            this.lastReturned = null;
            this.expectedModCount = LinkedTreeMap.this.modCount;
        }
        
        public final boolean hasNext() {
            return this.next != LinkedTreeMap.this.header;
        }
        
        final Node<K, V> nextNode() {
            final Node<K, V> e = this.next;
            if (e == LinkedTreeMap.this.header) {
                throw new NoSuchElementException();
            }
            if (LinkedTreeMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            this.next = e.next;
            return this.lastReturned = e;
        }
        
        public final void remove() {
            if (this.lastReturned == null) {
                throw new IllegalStateException();
            }
            LinkedTreeMap.this.removeInternal(this.lastReturned, true);
            this.lastReturned = null;
            this.expectedModCount = LinkedTreeMap.this.modCount;
        }
    }
    
    class EntrySet extends AbstractSet<Map.Entry<K, V>>
    {
        @Override
        public int size() {
            return LinkedTreeMap.this.size;
        }
        
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new LinkedTreeMapIterator<Map.Entry<K, V>>() {
                public Map.Entry<K, V> next() {
                    return this.nextNode();
                }
            };
        }
        
        @Override
        public boolean contains(final Object o) {
            return o instanceof Map.Entry && LinkedTreeMap.this.findByEntry((Map.Entry<?, ?>)o) != null;
        }
        
        @Override
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Node<K, V> node = LinkedTreeMap.this.findByEntry((Map.Entry<?, ?>)o);
            if (node == null) {
                return false;
            }
            LinkedTreeMap.this.removeInternal(node, true);
            return true;
        }
        
        @Override
        public void clear() {
            LinkedTreeMap.this.clear();
        }
    }
    
    class KeySet extends AbstractSet<K>
    {
        @Override
        public int size() {
            return LinkedTreeMap.this.size;
        }
        
        @Override
        public Iterator<K> iterator() {
            return new LinkedTreeMapIterator<K>() {
                public K next() {
                    return this.nextNode().key;
                }
            };
        }
        
        @Override
        public boolean contains(final Object o) {
            return LinkedTreeMap.this.containsKey(o);
        }
        
        @Override
        public boolean remove(final Object key) {
            return LinkedTreeMap.this.removeInternalByKey(key) != null;
        }
        
        @Override
        public void clear() {
            LinkedTreeMap.this.clear();
        }
    }
}
