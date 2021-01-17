// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Iterator;
import javax.annotation.Nullable;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
final class RegularImmutableMap<K, V> extends ImmutableMap<K, V>
{
    private final transient ImmutableMapEntry<K, V>[] entries;
    private final transient ImmutableMapEntry<K, V>[] table;
    private final transient int mask;
    private static final double MAX_LOAD_FACTOR = 1.2;
    private static final long serialVersionUID = 0L;
    
    RegularImmutableMap(final ImmutableMapEntry.TerminalEntry<?, ?>... theEntries) {
        this(theEntries.length, theEntries);
    }
    
    RegularImmutableMap(final int size, final ImmutableMapEntry.TerminalEntry<?, ?>[] theEntries) {
        this.entries = this.createEntryArray(size);
        final int tableSize = Hashing.closedTableSize(size, 1.2);
        this.table = this.createEntryArray(tableSize);
        this.mask = tableSize - 1;
        for (int entryIndex = 0; entryIndex < size; ++entryIndex) {
            final ImmutableMapEntry.TerminalEntry<K, V> entry = (ImmutableMapEntry.TerminalEntry<K, V>)theEntries[entryIndex];
            final K key = entry.getKey();
            final int tableIndex = Hashing.smear(key.hashCode()) & this.mask;
            final ImmutableMapEntry<K, V> existing = this.table[tableIndex];
            final ImmutableMapEntry<K, V> newEntry = (ImmutableMapEntry<K, V>)((existing == null) ? entry : new NonTerminalMapEntry<Object, Object>(entry, existing));
            this.table[tableIndex] = newEntry;
            this.checkNoConflictInBucket(key, this.entries[entryIndex] = newEntry, existing);
        }
    }
    
    RegularImmutableMap(final Map.Entry<?, ?>[] theEntries) {
        final int size = theEntries.length;
        this.entries = this.createEntryArray(size);
        final int tableSize = Hashing.closedTableSize(size, 1.2);
        this.table = this.createEntryArray(tableSize);
        this.mask = tableSize - 1;
        for (int entryIndex = 0; entryIndex < size; ++entryIndex) {
            final Map.Entry<K, V> entry = (Map.Entry<K, V>)theEntries[entryIndex];
            final K key = entry.getKey();
            final V value = entry.getValue();
            CollectPreconditions.checkEntryNotNull(key, value);
            final int tableIndex = Hashing.smear(key.hashCode()) & this.mask;
            final ImmutableMapEntry<K, V> existing = this.table[tableIndex];
            final ImmutableMapEntry<K, V> newEntry = (ImmutableMapEntry<K, V>)((existing == null) ? new ImmutableMapEntry.TerminalEntry<Object, Object>(key, value) : new NonTerminalMapEntry<Object, Object>(key, value, existing));
            this.table[tableIndex] = newEntry;
            this.checkNoConflictInBucket(key, this.entries[entryIndex] = newEntry, existing);
        }
    }
    
    private void checkNoConflictInBucket(final K key, final ImmutableMapEntry<K, V> entry, ImmutableMapEntry<K, V> bucketHead) {
        while (bucketHead != null) {
            ImmutableMap.checkNoConflict(!key.equals(bucketHead.getKey()), "key", entry, bucketHead);
            bucketHead = bucketHead.getNextInKeyBucket();
        }
    }
    
    private ImmutableMapEntry<K, V>[] createEntryArray(final int size) {
        return (ImmutableMapEntry<K, V>[])new ImmutableMapEntry[size];
    }
    
    @Override
    public V get(@Nullable final Object key) {
        if (key == null) {
            return null;
        }
        final int index = Hashing.smear(key.hashCode()) & this.mask;
        for (ImmutableMapEntry<K, V> entry = this.table[index]; entry != null; entry = entry.getNextInKeyBucket()) {
            final K candidateKey = entry.getKey();
            if (key.equals(candidateKey)) {
                return entry.getValue();
            }
        }
        return null;
    }
    
    @Override
    public int size() {
        return this.entries.length;
    }
    
    @Override
    boolean isPartialView() {
        return false;
    }
    
    @Override
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        return (ImmutableSet<Map.Entry<K, V>>)new EntrySet();
    }
    
    private static final class NonTerminalMapEntry<K, V> extends ImmutableMapEntry<K, V>
    {
        private final ImmutableMapEntry<K, V> nextInKeyBucket;
        
        NonTerminalMapEntry(final K key, final V value, final ImmutableMapEntry<K, V> nextInKeyBucket) {
            super(key, value);
            this.nextInKeyBucket = nextInKeyBucket;
        }
        
        NonTerminalMapEntry(final ImmutableMapEntry<K, V> contents, final ImmutableMapEntry<K, V> nextInKeyBucket) {
            super(contents);
            this.nextInKeyBucket = nextInKeyBucket;
        }
        
        @Override
        ImmutableMapEntry<K, V> getNextInKeyBucket() {
            return this.nextInKeyBucket;
        }
        
        @Nullable
        @Override
        ImmutableMapEntry<K, V> getNextInValueBucket() {
            return null;
        }
    }
    
    private class EntrySet extends ImmutableMapEntrySet<K, V>
    {
        @Override
        ImmutableMap<K, V> map() {
            return (ImmutableMap<K, V>)RegularImmutableMap.this;
        }
        
        @Override
        public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
            return (UnmodifiableIterator<Map.Entry<K, V>>)this.asList().iterator();
        }
        
        @Override
        ImmutableList<Map.Entry<K, V>> createAsList() {
            return new RegularImmutableAsList<Map.Entry<K, V>>((ImmutableCollection<Map.Entry<K, V>>)this, RegularImmutableMap.this.entries);
        }
    }
}
