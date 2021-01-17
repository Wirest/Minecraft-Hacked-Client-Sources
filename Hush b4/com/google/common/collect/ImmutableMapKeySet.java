// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.io.Serializable;
import java.util.Iterator;
import com.google.common.annotations.GwtIncompatible;
import java.util.Map;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
final class ImmutableMapKeySet<K, V> extends ImmutableSet<K>
{
    private final ImmutableMap<K, V> map;
    
    ImmutableMapKeySet(final ImmutableMap<K, V> map) {
        this.map = map;
    }
    
    @Override
    public int size() {
        return this.map.size();
    }
    
    @Override
    public UnmodifiableIterator<K> iterator() {
        return this.asList().iterator();
    }
    
    @Override
    public boolean contains(@Nullable final Object object) {
        return this.map.containsKey(object);
    }
    
    @Override
    ImmutableList<K> createAsList() {
        final ImmutableList<Map.Entry<K, V>> entryList = this.map.entrySet().asList();
        return new ImmutableAsList<K>() {
            @Override
            public K get(final int index) {
                return ((Map.Entry)entryList.get(index)).getKey();
            }
            
            @Override
            ImmutableCollection<K> delegateCollection() {
                return (ImmutableCollection<K>)ImmutableMapKeySet.this;
            }
        };
    }
    
    @Override
    boolean isPartialView() {
        return true;
    }
    
    @GwtIncompatible("serialization")
    @Override
    Object writeReplace() {
        return new KeySetSerializedForm((ImmutableMap<Object, ?>)this.map);
    }
    
    @GwtIncompatible("serialization")
    private static class KeySetSerializedForm<K> implements Serializable
    {
        final ImmutableMap<K, ?> map;
        private static final long serialVersionUID = 0L;
        
        KeySetSerializedForm(final ImmutableMap<K, ?> map) {
            this.map = map;
        }
        
        Object readResolve() {
            return this.map.keySet();
        }
    }
}
