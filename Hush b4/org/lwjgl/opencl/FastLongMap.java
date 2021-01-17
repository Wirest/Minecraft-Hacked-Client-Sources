// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import java.util.Iterator;

final class FastLongMap<V> implements Iterable<Entry<V>>
{
    private Entry[] table;
    private int size;
    private int mask;
    private int capacity;
    private int threshold;
    
    FastLongMap() {
        this(16, 0.75f);
    }
    
    FastLongMap(final int initialCapacity) {
        this(initialCapacity, 0.75f);
    }
    
    FastLongMap(final int initialCapacity, final float loadFactor) {
        if (initialCapacity > 1073741824) {
            throw new IllegalArgumentException("initialCapacity is too large.");
        }
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("initialCapacity must be greater than zero.");
        }
        if (loadFactor <= 0.0f) {
            throw new IllegalArgumentException("initialCapacity must be greater than zero.");
        }
        this.capacity = 1;
        while (this.capacity < initialCapacity) {
            this.capacity <<= 1;
        }
        this.threshold = (int)(this.capacity * loadFactor);
        this.table = new Entry[this.capacity];
        this.mask = this.capacity - 1;
    }
    
    private int index(final long key) {
        return index(key, this.mask);
    }
    
    private static int index(final long key, final int mask) {
        final int hash = (int)(key ^ key >>> 32);
        return hash & mask;
    }
    
    public V put(final long key, final V value) {
        final Entry<V>[] table = (Entry<V>[])this.table;
        final int index = this.index(key);
        for (Entry<V> e = table[index]; e != null; e = e.next) {
            if (e.key == key) {
                final V oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }
        table[index] = new Entry<V>(key, value, table[index]);
        if (this.size++ >= this.threshold) {
            this.rehash(table);
        }
        return null;
    }
    
    private void rehash(final Entry<V>[] table) {
        final int newCapacity = 2 * this.capacity;
        final int newMask = newCapacity - 1;
        final Entry<V>[] newTable = (Entry<V>[])new Entry[newCapacity];
        for (int i = 0; i < table.length; ++i) {
            Entry<V> e = table[i];
            if (e != null) {
                do {
                    final Entry<V> next = e.next;
                    final int index = index(e.key, newMask);
                    e.next = newTable[index];
                    newTable[index] = e;
                    e = next;
                } while (e != null);
            }
        }
        this.table = newTable;
        this.capacity = newCapacity;
        this.mask = newMask;
        this.threshold *= 2;
    }
    
    public V get(final long key) {
        final int index = this.index(key);
        for (Entry<V> e = (Entry<V>)this.table[index]; e != null; e = e.next) {
            if (e.key == key) {
                return e.value;
            }
        }
        return null;
    }
    
    public boolean containsValue(final Object value) {
        final Entry<V>[] table = (Entry<V>[])this.table;
        for (int i = table.length - 1; i >= 0; --i) {
            for (Entry<V> e = table[i]; e != null; e = e.next) {
                if (e.value.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean containsKey(final long key) {
        final int index = this.index(key);
        for (Entry<V> e = (Entry<V>)this.table[index]; e != null; e = e.next) {
            if (e.key == key) {
                return true;
            }
        }
        return false;
    }
    
    public V remove(final long key) {
        final int index = this.index(key);
        Entry<V> e;
        Entry<V> next;
        for (Entry<V> prev = e = (Entry<V>)this.table[index]; e != null; e = next) {
            next = e.next;
            if (e.key == key) {
                --this.size;
                if (prev == e) {
                    this.table[index] = next;
                }
                else {
                    prev.next = next;
                }
                return e.value;
            }
            prev = e;
        }
        return null;
    }
    
    public int size() {
        return this.size;
    }
    
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public void clear() {
        final Entry<V>[] table = (Entry<V>[])this.table;
        for (int index = table.length - 1; index >= 0; --index) {
            table[index] = null;
        }
        this.size = 0;
    }
    
    @Override
    public EntryIterator iterator() {
        return new EntryIterator();
    }
    
    public class EntryIterator implements Iterator<Entry<V>>
    {
        private int nextIndex;
        private Entry<V> current;
        
        EntryIterator() {
            this.reset();
        }
        
        public void reset() {
            this.current = null;
            Entry<V>[] table;
            int i;
            for (table = (Entry<V>[])FastLongMap.this.table, i = table.length - 1; i >= 0 && table[i] == null; --i) {}
            this.nextIndex = i;
        }
        
        @Override
        public boolean hasNext() {
            if (this.nextIndex >= 0) {
                return true;
            }
            final Entry e = this.current;
            return e != null && e.next != null;
        }
        
        @Override
        public Entry<V> next() {
            Entry<V> e = this.current;
            if (e != null) {
                e = e.next;
                if (e != null) {
                    return this.current = e;
                }
            }
            final Entry<V>[] table = (Entry<V>[])FastLongMap.this.table;
            int i = this.nextIndex;
            final Entry<V> current = table[i];
            this.current = current;
            e = current;
            while (--i >= 0 && table[i] == null) {}
            this.nextIndex = i;
            return e;
        }
        
        @Override
        public void remove() {
            FastLongMap.this.remove(this.current.key);
        }
    }
    
    static final class Entry<T>
    {
        final long key;
        T value;
        Entry<T> next;
        
        Entry(final long key, final T value, final Entry<T> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
        
        public long getKey() {
            return this.key;
        }
        
        public T getValue() {
            return this.value;
        }
    }
}
