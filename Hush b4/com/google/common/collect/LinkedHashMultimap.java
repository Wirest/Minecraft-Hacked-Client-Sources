// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import com.google.common.base.Objects;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.Iterator;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Map;
import java.util.LinkedHashMap;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
public final class LinkedHashMultimap<K, V> extends AbstractSetMultimap<K, V>
{
    private static final int DEFAULT_KEY_CAPACITY = 16;
    private static final int DEFAULT_VALUE_SET_CAPACITY = 2;
    @VisibleForTesting
    static final double VALUE_SET_LOAD_FACTOR = 1.0;
    @VisibleForTesting
    transient int valueSetCapacity;
    private transient ValueEntry<K, V> multimapHeaderEntry;
    @GwtIncompatible("java serialization not supported")
    private static final long serialVersionUID = 1L;
    
    public static <K, V> LinkedHashMultimap<K, V> create() {
        return new LinkedHashMultimap<K, V>(16, 2);
    }
    
    public static <K, V> LinkedHashMultimap<K, V> create(final int expectedKeys, final int expectedValuesPerKey) {
        return new LinkedHashMultimap<K, V>(Maps.capacity(expectedKeys), Maps.capacity(expectedValuesPerKey));
    }
    
    public static <K, V> LinkedHashMultimap<K, V> create(final Multimap<? extends K, ? extends V> multimap) {
        final LinkedHashMultimap<K, V> result = create(multimap.keySet().size(), 2);
        result.putAll(multimap);
        return result;
    }
    
    private static <K, V> void succeedsInValueSet(final ValueSetLink<K, V> pred, final ValueSetLink<K, V> succ) {
        pred.setSuccessorInValueSet(succ);
        succ.setPredecessorInValueSet(pred);
    }
    
    private static <K, V> void succeedsInMultimap(final ValueEntry<K, V> pred, final ValueEntry<K, V> succ) {
        pred.setSuccessorInMultimap(succ);
        succ.setPredecessorInMultimap(pred);
    }
    
    private static <K, V> void deleteFromValueSet(final ValueSetLink<K, V> entry) {
        succeedsInValueSet(entry.getPredecessorInValueSet(), entry.getSuccessorInValueSet());
    }
    
    private static <K, V> void deleteFromMultimap(final ValueEntry<K, V> entry) {
        succeedsInMultimap(entry.getPredecessorInMultimap(), entry.getSuccessorInMultimap());
    }
    
    private LinkedHashMultimap(final int keyCapacity, final int valueSetCapacity) {
        super(new LinkedHashMap(keyCapacity));
        this.valueSetCapacity = 2;
        CollectPreconditions.checkNonnegative(valueSetCapacity, "expectedValuesPerKey");
        this.valueSetCapacity = valueSetCapacity;
        succeedsInMultimap(this.multimapHeaderEntry = new ValueEntry<K, V>(null, null, 0, null), this.multimapHeaderEntry);
    }
    
    @Override
    Set<V> createCollection() {
        return new LinkedHashSet<V>(this.valueSetCapacity);
    }
    
    @Override
    Collection<V> createCollection(final K key) {
        return new ValueSet(key, this.valueSetCapacity);
    }
    
    @Override
    public Set<V> replaceValues(@Nullable final K key, final Iterable<? extends V> values) {
        return super.replaceValues(key, values);
    }
    
    @Override
    public Set<Map.Entry<K, V>> entries() {
        return super.entries();
    }
    
    @Override
    public Collection<V> values() {
        return super.values();
    }
    
    @Override
    Iterator<Map.Entry<K, V>> entryIterator() {
        return new Iterator<Map.Entry<K, V>>() {
            ValueEntry<K, V> nextEntry = LinkedHashMultimap.this.multimapHeaderEntry.successorInMultimap;
            ValueEntry<K, V> toRemove;
            
            @Override
            public boolean hasNext() {
                return this.nextEntry != LinkedHashMultimap.this.multimapHeaderEntry;
            }
            
            @Override
            public Map.Entry<K, V> next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final ValueEntry<K, V> result = this.nextEntry;
                this.toRemove = result;
                this.nextEntry = this.nextEntry.successorInMultimap;
                return result;
            }
            
            @Override
            public void remove() {
                CollectPreconditions.checkRemove(this.toRemove != null);
                LinkedHashMultimap.this.remove(this.toRemove.getKey(), this.toRemove.getValue());
                this.toRemove = null;
            }
        };
    }
    
    @Override
    Iterator<V> valueIterator() {
        return Maps.valueIterator(this.entryIterator());
    }
    
    @Override
    public void clear() {
        super.clear();
        succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
    }
    
    @GwtIncompatible("java.io.ObjectOutputStream")
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeInt(this.valueSetCapacity);
        stream.writeInt(this.keySet().size());
        for (final K key : this.keySet()) {
            stream.writeObject(key);
        }
        stream.writeInt(this.size());
        for (final Map.Entry<K, V> entry : this.entries()) {
            stream.writeObject(entry.getKey());
            stream.writeObject(entry.getValue());
        }
    }
    
    @GwtIncompatible("java.io.ObjectInputStream")
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        succeedsInMultimap(this.multimapHeaderEntry = new ValueEntry<K, V>(null, null, 0, null), this.multimapHeaderEntry);
        this.valueSetCapacity = stream.readInt();
        final int distinctKeys = stream.readInt();
        final Map<K, Collection<V>> map = new LinkedHashMap<K, Collection<V>>(Maps.capacity(distinctKeys));
        for (int i = 0; i < distinctKeys; ++i) {
            final K key = (K)stream.readObject();
            map.put(key, this.createCollection(key));
        }
        for (int entries = stream.readInt(), j = 0; j < entries; ++j) {
            final K key2 = (K)stream.readObject();
            final V value = (V)stream.readObject();
            map.get(key2).add(value);
        }
        this.setMap(map);
    }
    
    @VisibleForTesting
    static final class ValueEntry<K, V> extends ImmutableEntry<K, V> implements ValueSetLink<K, V>
    {
        final int smearedValueHash;
        @Nullable
        ValueEntry<K, V> nextInValueBucket;
        ValueSetLink<K, V> predecessorInValueSet;
        ValueSetLink<K, V> successorInValueSet;
        ValueEntry<K, V> predecessorInMultimap;
        ValueEntry<K, V> successorInMultimap;
        
        ValueEntry(@Nullable final K key, @Nullable final V value, final int smearedValueHash, @Nullable final ValueEntry<K, V> nextInValueBucket) {
            super(key, value);
            this.smearedValueHash = smearedValueHash;
            this.nextInValueBucket = nextInValueBucket;
        }
        
        boolean matchesValue(@Nullable final Object v, final int smearedVHash) {
            return this.smearedValueHash == smearedVHash && Objects.equal(this.getValue(), v);
        }
        
        @Override
        public ValueSetLink<K, V> getPredecessorInValueSet() {
            return this.predecessorInValueSet;
        }
        
        @Override
        public ValueSetLink<K, V> getSuccessorInValueSet() {
            return this.successorInValueSet;
        }
        
        @Override
        public void setPredecessorInValueSet(final ValueSetLink<K, V> entry) {
            this.predecessorInValueSet = entry;
        }
        
        @Override
        public void setSuccessorInValueSet(final ValueSetLink<K, V> entry) {
            this.successorInValueSet = entry;
        }
        
        public ValueEntry<K, V> getPredecessorInMultimap() {
            return this.predecessorInMultimap;
        }
        
        public ValueEntry<K, V> getSuccessorInMultimap() {
            return this.successorInMultimap;
        }
        
        public void setSuccessorInMultimap(final ValueEntry<K, V> multimapSuccessor) {
            this.successorInMultimap = multimapSuccessor;
        }
        
        public void setPredecessorInMultimap(final ValueEntry<K, V> multimapPredecessor) {
            this.predecessorInMultimap = multimapPredecessor;
        }
    }
    
    @VisibleForTesting
    final class ValueSet extends Sets.ImprovedAbstractSet<V> implements ValueSetLink<K, V>
    {
        private final K key;
        @VisibleForTesting
        ValueEntry<K, V>[] hashTable;
        private int size;
        private int modCount;
        private ValueSetLink<K, V> firstEntry;
        private ValueSetLink<K, V> lastEntry;
        
        ValueSet(final K key, final int expectedValues) {
            this.size = 0;
            this.modCount = 0;
            this.key = key;
            this.firstEntry = this;
            this.lastEntry = this;
            final int tableSize = Hashing.closedTableSize(expectedValues, 1.0);
            final ValueEntry<K, V>[] hashTable = (ValueEntry<K, V>[])new ValueEntry[tableSize];
            this.hashTable = hashTable;
        }
        
        private int mask() {
            return this.hashTable.length - 1;
        }
        
        @Override
        public ValueSetLink<K, V> getPredecessorInValueSet() {
            return this.lastEntry;
        }
        
        @Override
        public ValueSetLink<K, V> getSuccessorInValueSet() {
            return this.firstEntry;
        }
        
        @Override
        public void setPredecessorInValueSet(final ValueSetLink<K, V> entry) {
            this.lastEntry = entry;
        }
        
        @Override
        public void setSuccessorInValueSet(final ValueSetLink<K, V> entry) {
            this.firstEntry = entry;
        }
        
        @Override
        public Iterator<V> iterator() {
            return new Iterator<V>() {
                ValueSetLink<K, V> nextEntry = ValueSet.this.firstEntry;
                ValueEntry<K, V> toRemove;
                int expectedModCount = ValueSet.this.modCount;
                
                private void checkForComodification() {
                    if (ValueSet.this.modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    }
                }
                
                @Override
                public boolean hasNext() {
                    this.checkForComodification();
                    return this.nextEntry != ValueSet.this;
                }
                
                @Override
                public V next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final ValueEntry<K, V> entry = (ValueEntry<K, V>)(ValueEntry)this.nextEntry;
                    final V result = entry.getValue();
                    this.toRemove = entry;
                    this.nextEntry = entry.getSuccessorInValueSet();
                    return result;
                }
                
                @Override
                public void remove() {
                    this.checkForComodification();
                    CollectPreconditions.checkRemove(this.toRemove != null);
                    ValueSet.this.remove(this.toRemove.getValue());
                    this.expectedModCount = ValueSet.this.modCount;
                    this.toRemove = null;
                }
            };
        }
        
        @Override
        public int size() {
            return this.size;
        }
        
        @Override
        public boolean contains(@Nullable final Object o) {
            final int smearedHash = Hashing.smearedHash(o);
            for (ValueEntry<K, V> entry = this.hashTable[smearedHash & this.mask()]; entry != null; entry = entry.nextInValueBucket) {
                if (entry.matchesValue(o, smearedHash)) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public boolean add(@Nullable final V value) {
            final int smearedHash = Hashing.smearedHash(value);
            final int bucket = smearedHash & this.mask();
            ValueEntry<K, V> entry;
            ValueEntry<K, V> rowHead;
            for (rowHead = (entry = this.hashTable[bucket]); entry != null; entry = entry.nextInValueBucket) {
                if (entry.matchesValue(value, smearedHash)) {
                    return false;
                }
            }
            final ValueEntry<K, V> newEntry = new ValueEntry<K, V>(this.key, value, smearedHash, rowHead);
            succeedsInValueSet(this.lastEntry, (ValueSetLink<Object, Object>)newEntry);
            succeedsInValueSet(newEntry, (ValueSetLink<Object, Object>)this);
            succeedsInMultimap(LinkedHashMultimap.this.multimapHeaderEntry.getPredecessorInMultimap(), (ValueEntry<Object, Object>)newEntry);
            succeedsInMultimap(newEntry, (ValueEntry<Object, Object>)LinkedHashMultimap.this.multimapHeaderEntry);
            this.hashTable[bucket] = newEntry;
            ++this.size;
            ++this.modCount;
            this.rehashIfNecessary();
            return true;
        }
        
        private void rehashIfNecessary() {
            if (Hashing.needsResizing(this.size, this.hashTable.length, 1.0)) {
                final ValueEntry<K, V>[] hashTable = (ValueEntry<K, V>[])new ValueEntry[this.hashTable.length * 2];
                this.hashTable = hashTable;
                final int mask = hashTable.length - 1;
                for (ValueSetLink<K, V> entry = this.firstEntry; entry != this; entry = entry.getSuccessorInValueSet()) {
                    final ValueEntry<K, V> valueEntry = (ValueEntry<K, V>)(ValueEntry)entry;
                    final int bucket = valueEntry.smearedValueHash & mask;
                    valueEntry.nextInValueBucket = hashTable[bucket];
                    hashTable[bucket] = valueEntry;
                }
            }
        }
        
        @Override
        public boolean remove(@Nullable final Object o) {
            final int smearedHash = Hashing.smearedHash(o);
            final int bucket = smearedHash & this.mask();
            ValueEntry<K, V> prev = null;
            for (ValueEntry<K, V> entry = this.hashTable[bucket]; entry != null; entry = entry.nextInValueBucket) {
                if (entry.matchesValue(o, smearedHash)) {
                    if (prev == null) {
                        this.hashTable[bucket] = entry.nextInValueBucket;
                    }
                    else {
                        prev.nextInValueBucket = entry.nextInValueBucket;
                    }
                    deleteFromValueSet((ValueSetLink<Object, Object>)entry);
                    deleteFromMultimap((ValueEntry<Object, Object>)entry);
                    --this.size;
                    ++this.modCount;
                    return true;
                }
                prev = entry;
            }
            return false;
        }
        
        @Override
        public void clear() {
            Arrays.fill(this.hashTable, null);
            this.size = 0;
            for (ValueSetLink<K, V> entry = this.firstEntry; entry != this; entry = entry.getSuccessorInValueSet()) {
                final ValueEntry<K, V> valueEntry = (ValueEntry<K, V>)(ValueEntry)entry;
                deleteFromMultimap((ValueEntry<Object, Object>)valueEntry);
            }
            succeedsInValueSet(this, (ValueSetLink<Object, Object>)this);
            ++this.modCount;
        }
    }
    
    private interface ValueSetLink<K, V>
    {
        ValueSetLink<K, V> getPredecessorInValueSet();
        
        ValueSetLink<K, V> getSuccessorInValueSet();
        
        void setPredecessorInValueSet(final ValueSetLink<K, V> p0);
        
        void setSuccessorInValueSet(final ValueSetLink<K, V> p0);
    }
}
