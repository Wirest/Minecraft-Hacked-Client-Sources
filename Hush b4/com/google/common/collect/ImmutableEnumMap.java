// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.io.Serializable;
import javax.annotation.Nullable;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.EnumMap;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
final class ImmutableEnumMap<K extends Enum<K>, V> extends ImmutableMap<K, V>
{
    private final transient EnumMap<K, V> delegate;
    
    static <K extends Enum<K>, V> ImmutableMap<K, V> asImmutable(final EnumMap<K, V> map) {
        switch (map.size()) {
            case 0: {
                return ImmutableMap.of();
            }
            case 1: {
                final Map.Entry<K, V> entry = Iterables.getOnlyElement(map.entrySet());
                return ImmutableMap.of(entry.getKey(), entry.getValue());
            }
            default: {
                return new ImmutableEnumMap<K, V>(map);
            }
        }
    }
    
    private ImmutableEnumMap(final EnumMap<K, V> delegate) {
        this.delegate = delegate;
        Preconditions.checkArgument(!delegate.isEmpty());
    }
    
    @Override
    ImmutableSet<K> createKeySet() {
        return new ImmutableSet<K>() {
            @Override
            public boolean contains(final Object object) {
                return ImmutableEnumMap.this.delegate.containsKey(object);
            }
            
            @Override
            public int size() {
                return ImmutableEnumMap.this.size();
            }
            
            @Override
            public UnmodifiableIterator<K> iterator() {
                return Iterators.unmodifiableIterator((Iterator<K>)ImmutableEnumMap.this.delegate.keySet().iterator());
            }
            
            @Override
            boolean isPartialView() {
                return true;
            }
        };
    }
    
    @Override
    public int size() {
        return this.delegate.size();
    }
    
    @Override
    public boolean containsKey(@Nullable final Object key) {
        return this.delegate.containsKey(key);
    }
    
    @Override
    public V get(final Object key) {
        return this.delegate.get(key);
    }
    
    @Override
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        return (ImmutableSet<Map.Entry<K, V>>)new ImmutableMapEntrySet<K, V>() {
            @Override
            ImmutableMap<K, V> map() {
                return (ImmutableMap<K, V>)ImmutableEnumMap.this;
            }
            
            @Override
            public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
                return new UnmodifiableIterator<Map.Entry<K, V>>() {
                    private final Iterator<Map.Entry<K, V>> backingIterator = ImmutableEnumMap.this.delegate.entrySet().iterator();
                    
                    @Override
                    public boolean hasNext() {
                        return this.backingIterator.hasNext();
                    }
                    
                    @Override
                    public Map.Entry<K, V> next() {
                        final Map.Entry<K, V> entry = this.backingIterator.next();
                        return Maps.immutableEntry(entry.getKey(), entry.getValue());
                    }
                };
            }
        };
    }
    
    @Override
    boolean isPartialView() {
        return false;
    }
    
    @Override
    Object writeReplace() {
        return new EnumSerializedForm((EnumMap<Enum, Object>)this.delegate);
    }
    
    private static class EnumSerializedForm<K extends Enum<K>, V> implements Serializable
    {
        final EnumMap<K, V> delegate;
        private static final long serialVersionUID = 0L;
        
        EnumSerializedForm(final EnumMap<K, V> delegate) {
            this.delegate = delegate;
        }
        
        Object readResolve() {
            return new ImmutableEnumMap(this.delegate, null);
        }
    }
}
