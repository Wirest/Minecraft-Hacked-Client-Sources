// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.reflect;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.google.common.base.Function;
import java.util.Collection;
import java.util.Iterator;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.ForwardingMapEntry;
import java.util.Set;
import javax.annotation.Nullable;
import com.google.common.collect.Maps;
import java.util.Map;
import com.google.common.annotations.Beta;
import com.google.common.collect.ForwardingMap;

@Beta
public final class MutableTypeToInstanceMap<B> extends ForwardingMap<TypeToken<? extends B>, B> implements TypeToInstanceMap<B>
{
    private final Map<TypeToken<? extends B>, B> backingMap;
    
    public MutableTypeToInstanceMap() {
        this.backingMap = (Map<TypeToken<? extends B>, B>)Maps.newHashMap();
    }
    
    @Nullable
    @Override
    public <T extends B> T getInstance(final Class<T> type) {
        return this.trustedGet((TypeToken<T>)TypeToken.of((Class<T>)type));
    }
    
    @Nullable
    @Override
    public <T extends B> T putInstance(final Class<T> type, @Nullable final T value) {
        return this.trustedPut((TypeToken<T>)TypeToken.of((Class<T>)type), value);
    }
    
    @Nullable
    @Override
    public <T extends B> T getInstance(final TypeToken<T> type) {
        return (T)this.trustedGet((TypeToken<Object>)type.rejectTypeVariables());
    }
    
    @Nullable
    @Override
    public <T extends B> T putInstance(final TypeToken<T> type, @Nullable final T value) {
        return (T)this.trustedPut((TypeToken<Object>)type.rejectTypeVariables(), value);
    }
    
    @Override
    public B put(final TypeToken<? extends B> key, final B value) {
        throw new UnsupportedOperationException("Please use putInstance() instead.");
    }
    
    @Override
    public void putAll(final Map<? extends TypeToken<? extends B>, ? extends B> map) {
        throw new UnsupportedOperationException("Please use putInstance() instead.");
    }
    
    @Override
    public Set<Map.Entry<TypeToken<? extends B>, B>> entrySet() {
        return UnmodifiableEntry.transformEntries(super.entrySet());
    }
    
    @Override
    protected Map<TypeToken<? extends B>, B> delegate() {
        return this.backingMap;
    }
    
    @Nullable
    private <T extends B> T trustedPut(final TypeToken<T> type, @Nullable final T value) {
        return (T)this.backingMap.put((TypeToken<? extends B>)type, value);
    }
    
    @Nullable
    private <T extends B> T trustedGet(final TypeToken<T> type) {
        return (T)this.backingMap.get(type);
    }
    
    private static final class UnmodifiableEntry<K, V> extends ForwardingMapEntry<K, V>
    {
        private final Map.Entry<K, V> delegate;
        
        static <K, V> Set<Map.Entry<K, V>> transformEntries(final Set<Map.Entry<K, V>> entries) {
            return new ForwardingSet<Map.Entry<K, V>>() {
                @Override
                protected Set<Map.Entry<K, V>> delegate() {
                    return entries;
                }
                
                @Override
                public Iterator<Map.Entry<K, V>> iterator() {
                    return (Iterator<Map.Entry<K, V>>)transformEntries((Iterator<Map.Entry<Object, Object>>)super.iterator());
                }
                
                @Override
                public Object[] toArray() {
                    return this.standardToArray();
                }
                
                @Override
                public <T> T[] toArray(final T[] array) {
                    return this.standardToArray(array);
                }
            };
        }
        
        private static <K, V> Iterator<Map.Entry<K, V>> transformEntries(final Iterator<Map.Entry<K, V>> entries) {
            return Iterators.transform(entries, (Function<? super Map.Entry<K, V>, ? extends Map.Entry<K, V>>)new Function<Map.Entry<K, V>, Map.Entry<K, V>>() {
                @Override
                public Map.Entry<K, V> apply(final Map.Entry<K, V> entry) {
                    return new UnmodifiableEntry<K, V>((Map.Entry)entry);
                }
            });
        }
        
        private UnmodifiableEntry(final Map.Entry<K, V> delegate) {
            this.delegate = Preconditions.checkNotNull(delegate);
        }
        
        @Override
        protected Map.Entry<K, V> delegate() {
            return this.delegate;
        }
        
        @Override
        public V setValue(final V value) {
            throw new UnsupportedOperationException();
        }
    }
}
