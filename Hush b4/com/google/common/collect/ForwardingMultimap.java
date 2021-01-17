// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Set;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public abstract class ForwardingMultimap<K, V> extends ForwardingObject implements Multimap<K, V>
{
    protected ForwardingMultimap() {
    }
    
    @Override
    protected abstract Multimap<K, V> delegate();
    
    @Override
    public Map<K, Collection<V>> asMap() {
        return this.delegate().asMap();
    }
    
    @Override
    public void clear() {
        this.delegate().clear();
    }
    
    @Override
    public boolean containsEntry(@Nullable final Object key, @Nullable final Object value) {
        return this.delegate().containsEntry(key, value);
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
    public Collection<Map.Entry<K, V>> entries() {
        return this.delegate().entries();
    }
    
    @Override
    public Collection<V> get(@Nullable final K key) {
        return this.delegate().get(key);
    }
    
    @Override
    public boolean isEmpty() {
        return this.delegate().isEmpty();
    }
    
    @Override
    public Multiset<K> keys() {
        return this.delegate().keys();
    }
    
    @Override
    public Set<K> keySet() {
        return this.delegate().keySet();
    }
    
    @Override
    public boolean put(final K key, final V value) {
        return this.delegate().put(key, value);
    }
    
    @Override
    public boolean putAll(final K key, final Iterable<? extends V> values) {
        return this.delegate().putAll(key, values);
    }
    
    @Override
    public boolean putAll(final Multimap<? extends K, ? extends V> multimap) {
        return this.delegate().putAll(multimap);
    }
    
    @Override
    public boolean remove(@Nullable final Object key, @Nullable final Object value) {
        return this.delegate().remove(key, value);
    }
    
    @Override
    public Collection<V> removeAll(@Nullable final Object key) {
        return this.delegate().removeAll(key);
    }
    
    @Override
    public Collection<V> replaceValues(final K key, final Iterable<? extends V> values) {
        return this.delegate().replaceValues(key, values);
    }
    
    @Override
    public int size() {
        return this.delegate().size();
    }
    
    @Override
    public Collection<V> values() {
        return this.delegate().values();
    }
    
    @Override
    public boolean equals(@Nullable final Object object) {
        return object == this || this.delegate().equals(object);
    }
    
    @Override
    public int hashCode() {
        return this.delegate().hashCode();
    }
}
