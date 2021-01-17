// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Iterator;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtIncompatible;
import java.util.Set;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(emulated = true)
abstract class AbstractBiMap<K, V> extends ForwardingMap<K, V> implements BiMap<K, V>, Serializable
{
    private transient Map<K, V> delegate;
    transient AbstractBiMap<V, K> inverse;
    private transient Set<K> keySet;
    private transient Set<V> valueSet;
    private transient Set<Map.Entry<K, V>> entrySet;
    @GwtIncompatible("Not needed in emulated source.")
    private static final long serialVersionUID = 0L;
    
    AbstractBiMap(final Map<K, V> forward, final Map<V, K> backward) {
        this.setDelegates(forward, backward);
    }
    
    private AbstractBiMap(final Map<K, V> backward, final AbstractBiMap<V, K> forward) {
        this.delegate = backward;
        this.inverse = forward;
    }
    
    @Override
    protected Map<K, V> delegate() {
        return this.delegate;
    }
    
    K checkKey(@Nullable final K key) {
        return key;
    }
    
    V checkValue(@Nullable final V value) {
        return value;
    }
    
    void setDelegates(final Map<K, V> forward, final Map<V, K> backward) {
        Preconditions.checkState(this.delegate == null);
        Preconditions.checkState(this.inverse == null);
        Preconditions.checkArgument(forward.isEmpty());
        Preconditions.checkArgument(backward.isEmpty());
        Preconditions.checkArgument(forward != backward);
        this.delegate = forward;
        this.inverse = new Inverse<V, K>((Map)backward, this);
    }
    
    void setInverse(final AbstractBiMap<V, K> inverse) {
        this.inverse = inverse;
    }
    
    @Override
    public boolean containsValue(@Nullable final Object value) {
        return this.inverse.containsKey(value);
    }
    
    @Override
    public V put(@Nullable final K key, @Nullable final V value) {
        return this.putInBothMaps(key, value, false);
    }
    
    @Override
    public V forcePut(@Nullable final K key, @Nullable final V value) {
        return this.putInBothMaps(key, value, true);
    }
    
    private V putInBothMaps(@Nullable final K key, @Nullable final V value, final boolean force) {
        this.checkKey(key);
        this.checkValue(value);
        final boolean containedKey = this.containsKey(key);
        if (containedKey && Objects.equal(value, this.get(key))) {
            return value;
        }
        if (force) {
            this.inverse().remove(value);
        }
        else {
            Preconditions.checkArgument(!this.containsValue(value), "value already present: %s", value);
        }
        final V oldValue = this.delegate.put(key, value);
        this.updateInverseMap(key, containedKey, oldValue, value);
        return oldValue;
    }
    
    private void updateInverseMap(final K key, final boolean containedKey, final V oldValue, final V newValue) {
        if (containedKey) {
            this.removeFromInverseMap(oldValue);
        }
        this.inverse.delegate.put((K)newValue, (V)key);
    }
    
    @Override
    public V remove(@Nullable final Object key) {
        return this.containsKey(key) ? this.removeFromBothMaps(key) : null;
    }
    
    private V removeFromBothMaps(final Object key) {
        final V oldValue = this.delegate.remove(key);
        this.removeFromInverseMap(oldValue);
        return oldValue;
    }
    
    private void removeFromInverseMap(final V oldValue) {
        this.inverse.delegate.remove(oldValue);
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        for (final Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }
    
    @Override
    public void clear() {
        this.delegate.clear();
        this.inverse.delegate.clear();
    }
    
    @Override
    public BiMap<V, K> inverse() {
        return (BiMap<V, K>)this.inverse;
    }
    
    @Override
    public Set<K> keySet() {
        final Set<K> result = this.keySet;
        return (result == null) ? (this.keySet = new KeySet()) : result;
    }
    
    @Override
    public Set<V> values() {
        final Set<V> result = this.valueSet;
        return (result == null) ? (this.valueSet = new ValueSet()) : result;
    }
    
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        final Set<Map.Entry<K, V>> result = this.entrySet;
        return (result == null) ? (this.entrySet = new EntrySet()) : result;
    }
    
    private class KeySet extends ForwardingSet<K>
    {
        @Override
        protected Set<K> delegate() {
            return AbstractBiMap.this.delegate.keySet();
        }
        
        @Override
        public void clear() {
            AbstractBiMap.this.clear();
        }
        
        @Override
        public boolean remove(final Object key) {
            if (!this.contains(key)) {
                return false;
            }
            AbstractBiMap.this.removeFromBothMaps(key);
            return true;
        }
        
        @Override
        public boolean removeAll(final Collection<?> keysToRemove) {
            return this.standardRemoveAll(keysToRemove);
        }
        
        @Override
        public boolean retainAll(final Collection<?> keysToRetain) {
            return this.standardRetainAll(keysToRetain);
        }
        
        @Override
        public Iterator<K> iterator() {
            return Maps.keyIterator(AbstractBiMap.this.entrySet().iterator());
        }
    }
    
    private class ValueSet extends ForwardingSet<V>
    {
        final Set<V> valuesDelegate;
        
        private ValueSet() {
            this.valuesDelegate = (Set<V>)AbstractBiMap.this.inverse.keySet();
        }
        
        @Override
        protected Set<V> delegate() {
            return this.valuesDelegate;
        }
        
        @Override
        public Iterator<V> iterator() {
            return Maps.valueIterator(AbstractBiMap.this.entrySet().iterator());
        }
        
        @Override
        public Object[] toArray() {
            return this.standardToArray();
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            return this.standardToArray(array);
        }
        
        @Override
        public String toString() {
            return this.standardToString();
        }
    }
    
    private class EntrySet extends ForwardingSet<Map.Entry<K, V>>
    {
        final Set<Map.Entry<K, V>> esDelegate;
        
        private EntrySet() {
            this.esDelegate = AbstractBiMap.this.delegate.entrySet();
        }
        
        @Override
        protected Set<Map.Entry<K, V>> delegate() {
            return this.esDelegate;
        }
        
        @Override
        public void clear() {
            AbstractBiMap.this.clear();
        }
        
        @Override
        public boolean remove(final Object object) {
            if (!this.esDelegate.contains(object)) {
                return false;
            }
            final Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
            AbstractBiMap.this.inverse.delegate.remove(entry.getValue());
            this.esDelegate.remove(entry);
            return true;
        }
        
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            final Iterator<Map.Entry<K, V>> iterator = this.esDelegate.iterator();
            return new Iterator<Map.Entry<K, V>>() {
                Map.Entry<K, V> entry;
                
                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }
                
                @Override
                public Map.Entry<K, V> next() {
                    this.entry = iterator.next();
                    final Map.Entry<K, V> finalEntry = this.entry;
                    return new ForwardingMapEntry<K, V>() {
                        @Override
                        protected Map.Entry<K, V> delegate() {
                            return finalEntry;
                        }
                        
                        @Override
                        public V setValue(final V value) {
                            Preconditions.checkState(EntrySet.this.contains(this), (Object)"entry no longer in map");
                            if (Objects.equal(value, ((ForwardingMapEntry<K, Object>)this).getValue())) {
                                return value;
                            }
                            Preconditions.checkArgument(!AbstractBiMap.this.containsValue(value), "value already present: %s", value);
                            final V oldValue = finalEntry.setValue(value);
                            Preconditions.checkState(Objects.equal(value, AbstractBiMap.this.get(((ForwardingMapEntry<Object, V>)this).getKey())), (Object)"entry no longer in map");
                            AbstractBiMap.this.updateInverseMap(((ForwardingMapEntry<Object, V>)this).getKey(), true, oldValue, value);
                            return oldValue;
                        }
                    };
                }
                
                @Override
                public void remove() {
                    CollectPreconditions.checkRemove(this.entry != null);
                    final V value = this.entry.getValue();
                    iterator.remove();
                    AbstractBiMap.this.removeFromInverseMap(value);
                }
            };
        }
        
        @Override
        public Object[] toArray() {
            return this.standardToArray();
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            return this.standardToArray(array);
        }
        
        @Override
        public boolean contains(final Object o) {
            return Maps.containsEntryImpl(this.delegate(), o);
        }
        
        @Override
        public boolean containsAll(final Collection<?> c) {
            return this.standardContainsAll(c);
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            return this.standardRemoveAll(c);
        }
        
        @Override
        public boolean retainAll(final Collection<?> c) {
            return this.standardRetainAll(c);
        }
    }
    
    private static class Inverse<K, V> extends AbstractBiMap<K, V>
    {
        @GwtIncompatible("Not needed in emulated source.")
        private static final long serialVersionUID = 0L;
        
        private Inverse(final Map<K, V> backward, final AbstractBiMap<V, K> forward) {
            super(backward, (AbstractBiMap<Object, Object>)forward, null);
        }
        
        @Override
        K checkKey(final K key) {
            return this.inverse.checkValue(key);
        }
        
        @Override
        V checkValue(final V value) {
            return this.inverse.checkKey(value);
        }
        
        @GwtIncompatible("java.io.ObjectOuputStream")
        private void writeObject(final ObjectOutputStream stream) throws IOException {
            stream.defaultWriteObject();
            stream.writeObject(this.inverse());
        }
        
        @GwtIncompatible("java.io.ObjectInputStream")
        private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
            stream.defaultReadObject();
            this.setInverse((AbstractBiMap<V, K>)stream.readObject());
        }
        
        @GwtIncompatible("Not needed in the emulated source.")
        Object readResolve() {
            return this.inverse().inverse();
        }
    }
}
