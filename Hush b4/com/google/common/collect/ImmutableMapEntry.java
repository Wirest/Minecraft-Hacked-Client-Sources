// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import javax.annotation.Nullable;
import com.google.common.annotations.GwtIncompatible;

@GwtIncompatible("unnecessary")
abstract class ImmutableMapEntry<K, V> extends ImmutableEntry<K, V>
{
    ImmutableMapEntry(final K key, final V value) {
        super(key, value);
        CollectPreconditions.checkEntryNotNull(key, value);
    }
    
    ImmutableMapEntry(final ImmutableMapEntry<K, V> contents) {
        super(contents.getKey(), contents.getValue());
    }
    
    @Nullable
    abstract ImmutableMapEntry<K, V> getNextInKeyBucket();
    
    @Nullable
    abstract ImmutableMapEntry<K, V> getNextInValueBucket();
    
    static final class TerminalEntry<K, V> extends ImmutableMapEntry<K, V>
    {
        TerminalEntry(final ImmutableMapEntry<K, V> contents) {
            super(contents);
        }
        
        TerminalEntry(final K key, final V value) {
            super(key, value);
        }
        
        @Nullable
        @Override
        ImmutableMapEntry<K, V> getNextInKeyBucket() {
            return null;
        }
        
        @Nullable
        @Override
        ImmutableMapEntry<K, V> getNextInValueBucket() {
            return null;
        }
    }
}
