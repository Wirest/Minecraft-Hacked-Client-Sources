// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.serialization;

import java.util.Collection;
import java.util.Set;
import java.util.Iterator;
import java.lang.ref.Reference;
import java.util.Map;

abstract class ReferenceMap<K, V> implements Map<K, V>
{
    private final Map<K, Reference<V>> delegate;
    
    protected ReferenceMap(final Map<K, Reference<V>> delegate) {
        this.delegate = delegate;
    }
    
    abstract Reference<V> fold(final V p0);
    
    private V unfold(final Reference<V> ref) {
        if (ref == null) {
            return null;
        }
        return ref.get();
    }
    
    @Override
    public int size() {
        return this.delegate.size();
    }
    
    @Override
    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }
    
    @Override
    public boolean containsKey(final Object key) {
        return this.delegate.containsKey(key);
    }
    
    @Override
    public boolean containsValue(final Object value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public V get(final Object key) {
        return this.unfold(this.delegate.get(key));
    }
    
    @Override
    public V put(final K key, final V value) {
        return this.unfold(this.delegate.put(key, this.fold(value)));
    }
    
    @Override
    public V remove(final Object key) {
        return this.unfold(this.delegate.remove(key));
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        for (final Entry<? extends K, ? extends V> entry : m.entrySet()) {
            this.delegate.put((K)entry.getKey(), (Reference<V>)this.fold(entry.getValue()));
        }
    }
    
    @Override
    public void clear() {
        this.delegate.clear();
    }
    
    @Override
    public Set<K> keySet() {
        return this.delegate.keySet();
    }
    
    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
