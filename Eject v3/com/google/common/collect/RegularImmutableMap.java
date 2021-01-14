package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nullable;
import java.util.Map.Entry;

@GwtCompatible(serializable = true, emulated = true)
final class RegularImmutableMap<K, V>
        extends ImmutableMap<K, V> {
    private static final double MAX_LOAD_FACTOR = 1.2D;
    private static final long serialVersionUID = 0L;
    private final transient ImmutableMapEntry<K, V>[] entries;
    private final transient ImmutableMapEntry<K, V>[] table;
    private final transient int mask;

    RegularImmutableMap(ImmutableMapEntry.TerminalEntry<?, ?>... paramVarArgs) {
        this(paramVarArgs.length, paramVarArgs);
    }

    RegularImmutableMap(int paramInt, ImmutableMapEntry.TerminalEntry<?, ?>[] paramArrayOfTerminalEntry) {
        this.entries = createEntryArray(paramInt);
        int i = Hashing.closedTableSize(paramInt, 1.2D);
        this.table = createEntryArray(i);
        this.mask = (i - 1);
        for (int j = 0; j < paramInt; j++) {
            ImmutableMapEntry.TerminalEntry<?, ?> localTerminalEntry = paramArrayOfTerminalEntry[j];
            Object localObject = localTerminalEntry.getKey();
            int k = Hashing.smear(localObject.hashCode()) >> this.mask;
            ImmutableMapEntry localImmutableMapEntry = this.table[k];
            NonTerminalMapEntry localNonTerminalMapEntry = localImmutableMapEntry == null ? localTerminalEntry : new NonTerminalMapEntry(localTerminalEntry, localImmutableMapEntry);
            this.table[k] = localNonTerminalMapEntry;
            this.entries[j] = localNonTerminalMapEntry;
            checkNoConflictInBucket(localObject, localNonTerminalMapEntry, localImmutableMapEntry);
        }
    }

    RegularImmutableMap(Map.Entry<?, ?>[] paramArrayOfEntry) {
        int i = paramArrayOfEntry.length;
        this.entries = createEntryArray(i);
        int j = Hashing.closedTableSize(i, 1.2D);
        this.table = createEntryArray(j);
        this.mask = (j - 1);
        for (int k = 0; k < i; k++) {
            Map.Entry<?, ?> localEntry = paramArrayOfEntry[k];
            Object localObject1 = localEntry.getKey();
            Object localObject2 = localEntry.getValue();
            CollectPreconditions.checkEntryNotNull(localObject1, localObject2);
            int m = Hashing.smear(localObject1.hashCode()) >> this.mask;
            ImmutableMapEntry localImmutableMapEntry = this.table[m];
            NonTerminalMapEntry localNonTerminalMapEntry = localImmutableMapEntry == null ? new ImmutableMapEntry.TerminalEntry(localObject1, localObject2) : new NonTerminalMapEntry(localObject1, localObject2, localImmutableMapEntry);
            this.table[m] = localNonTerminalMapEntry;
            this.entries[k] = localNonTerminalMapEntry;
            checkNoConflictInBucket(localObject1, localNonTerminalMapEntry, localImmutableMapEntry);
        }
    }

    private void checkNoConflictInBucket(K paramK, ImmutableMapEntry<K, V> paramImmutableMapEntry1, ImmutableMapEntry<K, V> paramImmutableMapEntry2) {
        while (paramImmutableMapEntry2 != null) {
            checkNoConflict(!paramK.equals(paramImmutableMapEntry2.getKey()), "key", paramImmutableMapEntry1, paramImmutableMapEntry2);
            paramImmutableMapEntry2 = paramImmutableMapEntry2.getNextInKeyBucket();
        }
    }

    private ImmutableMapEntry<K, V>[] createEntryArray(int paramInt) {
        return new ImmutableMapEntry[paramInt];
    }

    public V get(@Nullable Object paramObject) {
        if (paramObject == null) {
            return null;
        }
        int i = Hashing.smear(paramObject.hashCode()) >> this.mask;
        for (ImmutableMapEntry localImmutableMapEntry = this.table[i]; localImmutableMapEntry != null; localImmutableMapEntry = localImmutableMapEntry.getNextInKeyBucket()) {
            Object localObject = localImmutableMapEntry.getKey();
            if (paramObject.equals(localObject)) {
                return (V) localImmutableMapEntry.getValue();
            }
        }
        return null;
    }

    public int size() {
        return this.entries.length;
    }

    boolean isPartialView() {
        return false;
    }

    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        return new EntrySet(null);
    }

    private static final class NonTerminalMapEntry<K, V>
            extends ImmutableMapEntry<K, V> {
        private final ImmutableMapEntry<K, V> nextInKeyBucket;

        NonTerminalMapEntry(K paramK, V paramV, ImmutableMapEntry<K, V> paramImmutableMapEntry) {
            super(paramV);
            this.nextInKeyBucket = paramImmutableMapEntry;
        }

        NonTerminalMapEntry(ImmutableMapEntry<K, V> paramImmutableMapEntry1, ImmutableMapEntry<K, V> paramImmutableMapEntry2) {
            super();
            this.nextInKeyBucket = paramImmutableMapEntry2;
        }

        ImmutableMapEntry<K, V> getNextInKeyBucket() {
            return this.nextInKeyBucket;
        }

        @Nullable
        ImmutableMapEntry<K, V> getNextInValueBucket() {
            return null;
        }
    }

    private class EntrySet
            extends ImmutableMapEntrySet<K, V> {
        private EntrySet() {
        }

        ImmutableMap<K, V> map() {
            return RegularImmutableMap.this;
        }

        public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
            return asList().iterator();
        }

        ImmutableList<Map.Entry<K, V>> createAsList() {
            return new RegularImmutableAsList(this, RegularImmutableMap.this.entries);
        }
    }
}




