// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
final class WellBehavedMap<K, V> extends ForwardingMap<K, V>
{
    private final Map<K, V> delegate;
    private Set<Map.Entry<K, V>> entrySet;
    
    private WellBehavedMap(final Map<K, V> delegate) {
        this.delegate = delegate;
    }
    
    static <K, V> WellBehavedMap<K, V> wrap(final Map<K, V> delegate) {
        return new WellBehavedMap<K, V>(delegate);
    }
    
    @Override
    protected Map<K, V> delegate() {
        return this.delegate;
    }
    
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        final Set<Map.Entry<K, V>> es = this.entrySet;
        if (es != null) {
            return es;
        }
        return this.entrySet = (Set<Map.Entry<K, V>>)new EntrySet();
    }
    
    private final class EntrySet extends Maps.EntrySet<K, V>
    {
        @Override
        Map<K, V> map() {
            return (Map<K, V>)WellBehavedMap.this;
        }
        
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new TransformedIterator<K, Map.Entry<K, V>>(WellBehavedMap.this.keySet().iterator()) {
                @Override
                Map.Entry<K, V> transform(final K key) {
                    return new AbstractMapEntry<K, V>() {
                        @Override
                        public K getKey() {
                            return key;
                        }
                        
                        @Override
                        public V getValue() {
                            return WellBehavedMap.this.get(key);
                        }
                        
                        @Override
                        public V setValue(final V value) {
                            return WellBehavedMap.this.put(key, value);
                        }
                    };
                }
            };
        }
    }
}
