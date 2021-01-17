// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.annotations.Beta;
import java.util.Iterator;
import com.google.common.base.Objects;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;
import java.util.Map;

@GwtCompatible
public abstract class ForwardingMap<K, V> extends ForwardingObject implements Map<K, V>
{
    protected ForwardingMap() {
    }
    
    @Override
    protected abstract Map<K, V> delegate();
    
    @Override
    public int size() {
        return this.delegate().size();
    }
    
    @Override
    public boolean isEmpty() {
        return this.delegate().isEmpty();
    }
    
    @Override
    public V remove(final Object object) {
        return this.delegate().remove(object);
    }
    
    @Override
    public void clear() {
        this.delegate().clear();
    }
    
    @Override
    public boolean containsKey(@Nullable final Object key) {
        return this.delegate().containsKey(key);
    }
    
    @Override
    public boolean containsValue(@Nullable final Object value) {
        return this.delegate().containsValue(value);
    }
    
    @Override
    public V get(@Nullable final Object key) {
        return this.delegate().get(key);
    }
    
    @Override
    public V put(final K key, final V value) {
        return this.delegate().put(key, value);
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        this.delegate().putAll(map);
    }
    
    @Override
    public Set<K> keySet() {
        return this.delegate().keySet();
    }
    
    @Override
    public Collection<V> values() {
        return this.delegate().values();
    }
    
    @Override
    public Set<Entry<K, V>> entrySet() {
        return this.delegate().entrySet();
    }
    
    @Override
    public boolean equals(@Nullable final Object object) {
        return object == this || this.delegate().equals(object);
    }
    
    @Override
    public int hashCode() {
        return this.delegate().hashCode();
    }
    
    protected void standardPutAll(final Map<? extends K, ? extends V> map) {
        Maps.putAllImpl((Map<Object, Object>)this, map);
    }
    
    @Beta
    protected V standardRemove(@Nullable final Object key) {
        final Iterator<Entry<K, V>> entryIterator = this.entrySet().iterator();
        while (entryIterator.hasNext()) {
            final Entry<K, V> entry = entryIterator.next();
            if (Objects.equal(entry.getKey(), key)) {
                final V value = entry.getValue();
                entryIterator.remove();
                return value;
            }
        }
        return null;
    }
    
    protected void standardClear() {
        Iterators.clear(this.entrySet().iterator());
    }
    
    @Beta
    protected boolean standardContainsKey(@Nullable final Object key) {
        return Maps.containsKeyImpl(this, key);
    }
    
    protected boolean standardContainsValue(@Nullable final Object value) {
        return Maps.containsValueImpl(this, value);
    }
    
    protected boolean standardIsEmpty() {
        return !this.entrySet().iterator().hasNext();
    }
    
    protected boolean standardEquals(@Nullable final Object object) {
        return Maps.equalsImpl(this, object);
    }
    
    protected int standardHashCode() {
        return Sets.hashCodeImpl(this.entrySet());
    }
    
    protected String standardToString() {
        return Maps.toStringImpl(this);
    }
    
    @Beta
    protected class StandardKeySet extends Maps.KeySet<K, V>
    {
        public StandardKeySet() {
            super(ForwardingMap.this);
        }
    }
    
    @Beta
    protected class StandardValues extends Maps.Values<K, V>
    {
        public StandardValues() {
            super(ForwardingMap.this);
        }
    }
    
    @Beta
    protected abstract class StandardEntrySet extends Maps.EntrySet<K, V>
    {
        public StandardEntrySet() {
        }
        
        @Override
        Map<K, V> map() {
            return (Map<K, V>)ForwardingMap.this;
        }
    }
}
