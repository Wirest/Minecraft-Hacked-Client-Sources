// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.io.Serializable;
import java.util.Iterator;
import javax.annotation.Nullable;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
class RegularImmutableBiMap<K, V> extends ImmutableBiMap<K, V>
{
    static final double MAX_LOAD_FACTOR = 1.2;
    private final transient ImmutableMapEntry<K, V>[] keyTable;
    private final transient ImmutableMapEntry<K, V>[] valueTable;
    private final transient ImmutableMapEntry<K, V>[] entries;
    private final transient int mask;
    private final transient int hashCode;
    private transient ImmutableBiMap<V, K> inverse;
    
    RegularImmutableBiMap(final ImmutableMapEntry.TerminalEntry<?, ?>... entriesToAdd) {
        this(entriesToAdd.length, entriesToAdd);
    }
    
    RegularImmutableBiMap(final int n, final ImmutableMapEntry.TerminalEntry<?, ?>[] entriesToAdd) {
        final int tableSize = Hashing.closedTableSize(n, 1.2);
        this.mask = tableSize - 1;
        final ImmutableMapEntry<K, V>[] keyTable = createEntryArray(tableSize);
        final ImmutableMapEntry<K, V>[] valueTable = createEntryArray(tableSize);
        final ImmutableMapEntry<K, V>[] entries = createEntryArray(n);
        int hashCode = 0;
        for (int i = 0; i < n; ++i) {
            final ImmutableMapEntry.TerminalEntry<K, V> entry = (ImmutableMapEntry.TerminalEntry<K, V>)entriesToAdd[i];
            final K key = entry.getKey();
            final V value = entry.getValue();
            final int keyHash = key.hashCode();
            final int valueHash = value.hashCode();
            final int keyBucket = Hashing.smear(keyHash) & this.mask;
            final int valueBucket = Hashing.smear(valueHash) & this.mask;
            ImmutableMapEntry<K, V> keyEntry;
            ImmutableMapEntry<K, V> nextInKeyBucket;
            for (nextInKeyBucket = (keyEntry = keyTable[keyBucket]); keyEntry != null; keyEntry = keyEntry.getNextInKeyBucket()) {
                ImmutableMap.checkNoConflict(!key.equals(keyEntry.getKey()), "key", entry, keyEntry);
            }
            ImmutableMapEntry<K, V> valueEntry;
            ImmutableMapEntry<K, V> nextInValueBucket;
            for (nextInValueBucket = (valueEntry = valueTable[valueBucket]); valueEntry != null; valueEntry = valueEntry.getNextInValueBucket()) {
                ImmutableMap.checkNoConflict(!value.equals(valueEntry.getValue()), "value", entry, valueEntry);
            }
            final ImmutableMapEntry<K, V> newEntry = (ImmutableMapEntry<K, V>)((nextInKeyBucket == null && nextInValueBucket == null) ? entry : new NonTerminalBiMapEntry<Object, Object>(entry, nextInKeyBucket, nextInValueBucket));
            keyTable[keyBucket] = newEntry;
            entries[i] = (valueTable[valueBucket] = newEntry);
            hashCode += (keyHash ^ valueHash);
        }
        this.keyTable = keyTable;
        this.valueTable = valueTable;
        this.entries = entries;
        this.hashCode = hashCode;
    }
    
    RegularImmutableBiMap(final Map.Entry<?, ?>[] entriesToAdd) {
        final int n = entriesToAdd.length;
        final int tableSize = Hashing.closedTableSize(n, 1.2);
        this.mask = tableSize - 1;
        final ImmutableMapEntry<K, V>[] keyTable = createEntryArray(tableSize);
        final ImmutableMapEntry<K, V>[] valueTable = createEntryArray(tableSize);
        final ImmutableMapEntry<K, V>[] entries = createEntryArray(n);
        int hashCode = 0;
        for (int i = 0; i < n; ++i) {
            final Map.Entry<K, V> entry = (Map.Entry<K, V>)entriesToAdd[i];
            final K key = entry.getKey();
            final V value = entry.getValue();
            CollectPreconditions.checkEntryNotNull(key, value);
            final int keyHash = key.hashCode();
            final int valueHash = value.hashCode();
            final int keyBucket = Hashing.smear(keyHash) & this.mask;
            final int valueBucket = Hashing.smear(valueHash) & this.mask;
            ImmutableMapEntry<K, V> keyEntry;
            ImmutableMapEntry<K, V> nextInKeyBucket;
            for (nextInKeyBucket = (keyEntry = keyTable[keyBucket]); keyEntry != null; keyEntry = keyEntry.getNextInKeyBucket()) {
                ImmutableMap.checkNoConflict(!key.equals(keyEntry.getKey()), "key", entry, keyEntry);
            }
            ImmutableMapEntry<K, V> valueEntry;
            ImmutableMapEntry<K, V> nextInValueBucket;
            for (nextInValueBucket = (valueEntry = valueTable[valueBucket]); valueEntry != null; valueEntry = valueEntry.getNextInValueBucket()) {
                ImmutableMap.checkNoConflict(!value.equals(valueEntry.getValue()), "value", entry, valueEntry);
            }
            final ImmutableMapEntry<K, V> newEntry = (ImmutableMapEntry<K, V>)((nextInKeyBucket == null && nextInValueBucket == null) ? new ImmutableMapEntry.TerminalEntry<Object, Object>(key, value) : new NonTerminalBiMapEntry<Object, Object>(key, value, nextInKeyBucket, nextInValueBucket));
            keyTable[keyBucket] = newEntry;
            entries[i] = (valueTable[valueBucket] = newEntry);
            hashCode += (keyHash ^ valueHash);
        }
        this.keyTable = keyTable;
        this.valueTable = valueTable;
        this.entries = entries;
        this.hashCode = hashCode;
    }
    
    private static <K, V> ImmutableMapEntry<K, V>[] createEntryArray(final int length) {
        return (ImmutableMapEntry<K, V>[])new ImmutableMapEntry[length];
    }
    
    @Nullable
    @Override
    public V get(@Nullable final Object key) {
        if (key == null) {
            return null;
        }
        final int bucket = Hashing.smear(key.hashCode()) & this.mask;
        for (ImmutableMapEntry<K, V> entry = this.keyTable[bucket]; entry != null; entry = entry.getNextInKeyBucket()) {
            if (key.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }
    
    @Override
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        return (ImmutableSet<Map.Entry<K, V>>)new ImmutableMapEntrySet<K, V>() {
            @Override
            ImmutableMap<K, V> map() {
                return (ImmutableMap<K, V>)RegularImmutableBiMap.this;
            }
            
            @Override
            public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
                return (UnmodifiableIterator<Map.Entry<K, V>>)this.asList().iterator();
            }
            
            @Override
            ImmutableList<Map.Entry<K, V>> createAsList() {
                return new RegularImmutableAsList<Map.Entry<K, V>>((ImmutableCollection<Map.Entry<K, V>>)this, RegularImmutableBiMap.this.entries);
            }
            
            @Override
            boolean isHashCodeFast() {
                return true;
            }
            
            @Override
            public int hashCode() {
                return RegularImmutableBiMap.this.hashCode;
            }
        };
    }
    
    @Override
    boolean isPartialView() {
        return false;
    }
    
    @Override
    public int size() {
        return this.entries.length;
    }
    
    @Override
    public ImmutableBiMap<V, K> inverse() {
        final ImmutableBiMap<V, K> result = this.inverse;
        return (result == null) ? (this.inverse = new Inverse()) : result;
    }
    
    private static final class NonTerminalBiMapEntry<K, V> extends ImmutableMapEntry<K, V>
    {
        @Nullable
        private final ImmutableMapEntry<K, V> nextInKeyBucket;
        @Nullable
        private final ImmutableMapEntry<K, V> nextInValueBucket;
        
        NonTerminalBiMapEntry(final K key, final V value, @Nullable final ImmutableMapEntry<K, V> nextInKeyBucket, @Nullable final ImmutableMapEntry<K, V> nextInValueBucket) {
            super(key, value);
            this.nextInKeyBucket = nextInKeyBucket;
            this.nextInValueBucket = nextInValueBucket;
        }
        
        NonTerminalBiMapEntry(final ImmutableMapEntry<K, V> contents, @Nullable final ImmutableMapEntry<K, V> nextInKeyBucket, @Nullable final ImmutableMapEntry<K, V> nextInValueBucket) {
            super(contents);
            this.nextInKeyBucket = nextInKeyBucket;
            this.nextInValueBucket = nextInValueBucket;
        }
        
        @Nullable
        @Override
        ImmutableMapEntry<K, V> getNextInKeyBucket() {
            return this.nextInKeyBucket;
        }
        
        @Nullable
        @Override
        ImmutableMapEntry<K, V> getNextInValueBucket() {
            return this.nextInValueBucket;
        }
    }
    
    private final class Inverse extends ImmutableBiMap<V, K>
    {
        @Override
        public int size() {
            return this.inverse().size();
        }
        
        @Override
        public ImmutableBiMap<K, V> inverse() {
            return (ImmutableBiMap<K, V>)RegularImmutableBiMap.this;
        }
        
        @Override
        public K get(@Nullable final Object value) {
            if (value == null) {
                return null;
            }
            final int bucket = Hashing.smear(value.hashCode()) & RegularImmutableBiMap.this.mask;
            for (ImmutableMapEntry<K, V> entry = RegularImmutableBiMap.this.valueTable[bucket]; entry != null; entry = entry.getNextInValueBucket()) {
                if (value.equals(entry.getValue())) {
                    return entry.getKey();
                }
            }
            return null;
        }
        
        @Override
        ImmutableSet<Map.Entry<V, K>> createEntrySet() {
            return (ImmutableSet<Map.Entry<V, K>>)new InverseEntrySet();
        }
        
        @Override
        boolean isPartialView() {
            return false;
        }
        
        @Override
        Object writeReplace() {
            return new InverseSerializedForm(RegularImmutableBiMap.this);
        }
        
        final class InverseEntrySet extends ImmutableMapEntrySet<V, K>
        {
            @Override
            ImmutableMap<V, K> map() {
                return Inverse.this;
            }
            
            @Override
            boolean isHashCodeFast() {
                return true;
            }
            
            @Override
            public int hashCode() {
                return RegularImmutableBiMap.this.hashCode;
            }
            
            @Override
            public UnmodifiableIterator<Map.Entry<V, K>> iterator() {
                return (UnmodifiableIterator<Map.Entry<V, K>>)this.asList().iterator();
            }
            
            @Override
            ImmutableList<Map.Entry<V, K>> createAsList() {
                return new ImmutableAsList<Map.Entry<V, K>>() {
                    @Override
                    public Map.Entry<V, K> get(final int index) {
                        final Map.Entry<K, V> entry = RegularImmutableBiMap.this.entries[index];
                        return Maps.immutableEntry(entry.getValue(), entry.getKey());
                    }
                    
                    @Override
                    ImmutableCollection<Map.Entry<V, K>> delegateCollection() {
                        return (ImmutableCollection<Map.Entry<V, K>>)InverseEntrySet.this;
                    }
                };
            }
        }
    }
    
    private static class InverseSerializedForm<K, V> implements Serializable
    {
        private final ImmutableBiMap<K, V> forward;
        private static final long serialVersionUID = 1L;
        
        InverseSerializedForm(final ImmutableBiMap<K, V> forward) {
            this.forward = forward;
        }
        
        Object readResolve() {
            return this.forward.inverse();
        }
    }
}
