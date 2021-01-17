// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.collection;

import java.util.NoSuchElementException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;

public class IntObjectHashMap<V> implements IntObjectMap<V>, Iterable<Entry<V>>
{
    private static final int DEFAULT_CAPACITY = 11;
    private static final float DEFAULT_LOAD_FACTOR = 0.5f;
    private static final Object NULL_VALUE;
    private int maxSize;
    private final float loadFactor;
    private int[] keys;
    private V[] values;
    private int size;
    
    public IntObjectHashMap() {
        this(11, 0.5f);
    }
    
    public IntObjectHashMap(final int initialCapacity) {
        this(initialCapacity, 0.5f);
    }
    
    public IntObjectHashMap(final int initialCapacity, final float loadFactor) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("initialCapacity must be >= 1");
        }
        if (loadFactor <= 0.0f || loadFactor > 1.0f) {
            throw new IllegalArgumentException("loadFactor must be > 0 and <= 1");
        }
        this.loadFactor = loadFactor;
        final int capacity = adjustCapacity(initialCapacity);
        this.keys = new int[capacity];
        final V[] temp = (V[])new Object[capacity];
        this.values = temp;
        this.maxSize = this.calcMaxSize(capacity);
    }
    
    private static <T> T toExternal(final T value) {
        return (value == IntObjectHashMap.NULL_VALUE) ? null : value;
    }
    
    private static <T> T toInternal(final T value) {
        return (T)((value == null) ? IntObjectHashMap.NULL_VALUE : value);
    }
    
    @Override
    public V get(final int key) {
        final int index = this.indexOf(key);
        return (index == -1) ? null : toExternal(this.values[index]);
    }
    
    @Override
    public V put(final int key, final V value) {
        int index;
        final int startIndex = index = this.hashIndex(key);
        while (this.values[index] != null) {
            if (this.keys[index] == key) {
                final V previousValue = this.values[index];
                this.values[index] = toInternal(value);
                return toExternal(previousValue);
            }
            if ((index = this.probeNext(index)) == startIndex) {
                throw new IllegalStateException("Unable to insert");
            }
        }
        this.keys[index] = key;
        this.values[index] = toInternal(value);
        this.growSize();
        return null;
    }
    
    private int probeNext(final int index) {
        return (index == this.values.length - 1) ? 0 : (index + 1);
    }
    
    @Override
    public void putAll(final IntObjectMap<V> sourceMap) {
        if (sourceMap instanceof IntObjectHashMap) {
            final IntObjectHashMap<V> source = (IntObjectHashMap)sourceMap;
            for (int i = 0; i < source.values.length; ++i) {
                final V sourceValue = source.values[i];
                if (sourceValue != null) {
                    this.put(source.keys[i], sourceValue);
                }
            }
            return;
        }
        for (final Entry<V> entry : sourceMap.entries()) {
            this.put(entry.key(), entry.value());
        }
    }
    
    @Override
    public V remove(final int key) {
        final int index = this.indexOf(key);
        if (index == -1) {
            return null;
        }
        final V prev = this.values[index];
        this.removeAt(index);
        return toExternal(prev);
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    @Override
    public void clear() {
        Arrays.fill(this.keys, 0);
        Arrays.fill(this.values, null);
        this.size = 0;
    }
    
    @Override
    public boolean containsKey(final int key) {
        return this.indexOf(key) >= 0;
    }
    
    @Override
    public boolean containsValue(final V value) {
        final V v = toInternal(value);
        for (int i = 0; i < this.values.length; ++i) {
            if (this.values[i] != null && this.values[i].equals(v)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Iterable<Entry<V>> entries() {
        return this;
    }
    
    @Override
    public Iterator<Entry<V>> iterator() {
        return new IteratorImpl();
    }
    
    @Override
    public int[] keys() {
        final int[] outKeys = new int[this.size()];
        int targetIx = 0;
        for (int i = 0; i < this.values.length; ++i) {
            if (this.values[i] != null) {
                outKeys[targetIx++] = this.keys[i];
            }
        }
        return outKeys;
    }
    
    @Override
    public V[] values(final Class<V> clazz) {
        final V[] outValues = (V[])Array.newInstance(clazz, this.size());
        int targetIx = 0;
        for (int i = 0; i < this.values.length; ++i) {
            if (this.values[i] != null) {
                outValues[targetIx++] = this.values[i];
            }
        }
        return outValues;
    }
    
    @Override
    public int hashCode() {
        int hash = this.size;
        for (int i = 0; i < this.keys.length; ++i) {
            hash ^= this.keys[i];
        }
        return hash;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof IntObjectMap)) {
            return false;
        }
        final IntObjectMap other = (IntObjectMap)obj;
        if (this.size != other.size()) {
            return false;
        }
        for (int i = 0; i < this.values.length; ++i) {
            final V value = this.values[i];
            if (value != null) {
                final int key = this.keys[i];
                final Object otherValue = other.get(key);
                if (value == IntObjectHashMap.NULL_VALUE) {
                    if (otherValue != null) {
                        return false;
                    }
                }
                else if (!value.equals(otherValue)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private int indexOf(final int key) {
        int index;
        final int startIndex = index = this.hashIndex(key);
        while (this.values[index] != null) {
            if (key == this.keys[index]) {
                return index;
            }
            if ((index = this.probeNext(index)) == startIndex) {
                return -1;
            }
        }
        return -1;
    }
    
    private int hashIndex(final int key) {
        return key % this.keys.length;
    }
    
    private void growSize() {
        ++this.size;
        if (this.size > this.maxSize) {
            this.rehash(adjustCapacity((int)Math.min(this.keys.length * 2.0, 2.147483639E9)));
        }
        else if (this.size == this.keys.length) {
            this.rehash(this.keys.length);
        }
    }
    
    private static int adjustCapacity(final int capacity) {
        return capacity | 0x1;
    }
    
    private void removeAt(final int index) {
        --this.size;
        this.keys[index] = 0;
        this.values[index] = null;
        int nextFree = index;
        for (int i = this.probeNext(index); this.values[i] != null; i = this.probeNext(i)) {
            final int bucket = this.hashIndex(this.keys[i]);
            if ((i < bucket && (bucket <= nextFree || nextFree <= i)) || (bucket <= nextFree && nextFree <= i)) {
                this.keys[nextFree] = this.keys[i];
                this.values[nextFree] = this.values[i];
                this.keys[i] = 0;
                this.values[i] = null;
                nextFree = i;
            }
        }
    }
    
    private int calcMaxSize(final int capacity) {
        final int upperBound = capacity - 1;
        return Math.min(upperBound, (int)(capacity * this.loadFactor));
    }
    
    private void rehash(final int newCapacity) {
        final int[] oldKeys = this.keys;
        final V[] oldVals = this.values;
        this.keys = new int[newCapacity];
        final V[] temp = (V[])new Object[newCapacity];
        this.values = temp;
        this.maxSize = this.calcMaxSize(newCapacity);
        for (int i = 0; i < oldVals.length; ++i) {
            final V oldVal = oldVals[i];
            if (oldVal != null) {
                final int oldKey = oldKeys[i];
                int index;
                for (int startIndex = index = this.hashIndex(oldKey); this.values[index] != null; index = this.probeNext(index)) {}
                this.keys[index] = oldKey;
                this.values[index] = toInternal(oldVal);
            }
        }
    }
    
    @Override
    public String toString() {
        if (this.size == 0) {
            return "{}";
        }
        final StringBuilder sb = new StringBuilder(4 * this.size);
        for (int i = 0; i < this.values.length; ++i) {
            final V value = this.values[i];
            if (value != null) {
                sb.append((sb.length() == 0) ? "{" : ", ");
                sb.append(this.keys[i]).append('=').append((value == this) ? "(this Map)" : value);
            }
        }
        return sb.append('}').toString();
    }
    
    static {
        NULL_VALUE = new Object();
    }
    
    private final class IteratorImpl implements Iterator<Entry<V>>, Entry<V>
    {
        private int prevIndex;
        private int nextIndex;
        private int entryIndex;
        
        private IteratorImpl() {
            this.prevIndex = -1;
            this.nextIndex = -1;
            this.entryIndex = -1;
        }
        
        private void scanNext() {
            while (++this.nextIndex != IntObjectHashMap.this.values.length && IntObjectHashMap.this.values[this.nextIndex] == null) {}
        }
        
        @Override
        public boolean hasNext() {
            if (this.nextIndex == -1) {
                this.scanNext();
            }
            return this.nextIndex < IntObjectHashMap.this.keys.length;
        }
        
        @Override
        public Entry<V> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.prevIndex = this.nextIndex;
            this.scanNext();
            this.entryIndex = this.prevIndex;
            return this;
        }
        
        @Override
        public void remove() {
            if (this.prevIndex < 0) {
                throw new IllegalStateException("next must be called before each remove.");
            }
            IntObjectHashMap.this.removeAt(this.prevIndex);
            this.prevIndex = -1;
        }
        
        @Override
        public int key() {
            return IntObjectHashMap.this.keys[this.entryIndex];
        }
        
        @Override
        public V value() {
            return (V)toExternal(IntObjectHashMap.this.values[this.entryIndex]);
        }
        
        @Override
        public void setValue(final V value) {
            IntObjectHashMap.this.values[this.entryIndex] = (V)toInternal(value);
        }
    }
}
