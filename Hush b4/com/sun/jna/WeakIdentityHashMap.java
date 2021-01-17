// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Iterator;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.lang.ref.ReferenceQueue;
import java.util.Map;

public class WeakIdentityHashMap implements Map
{
    private final ReferenceQueue queue;
    private Map backingStore;
    
    public WeakIdentityHashMap() {
        this.queue = new ReferenceQueue();
        this.backingStore = new HashMap();
    }
    
    public void clear() {
        this.backingStore.clear();
        this.reap();
    }
    
    public boolean containsKey(final Object key) {
        this.reap();
        return this.backingStore.containsKey(new IdentityWeakReference(key));
    }
    
    public boolean containsValue(final Object value) {
        this.reap();
        return this.backingStore.containsValue(value);
    }
    
    public Set entrySet() {
        this.reap();
        final Set ret = new HashSet();
        for (final Entry ref : this.backingStore.entrySet()) {
            final Object key = ref.getKey().get();
            final Object value = ref.getValue();
            final Entry entry = new Entry() {
                public Object getKey() {
                    return key;
                }
                
                public Object getValue() {
                    return value;
                }
                
                public Object setValue(final Object value) {
                    throw new UnsupportedOperationException();
                }
            };
            ret.add(entry);
        }
        return Collections.unmodifiableSet((Set<?>)ret);
    }
    
    public Set keySet() {
        this.reap();
        final Set ret = new HashSet();
        for (final IdentityWeakReference ref : this.backingStore.keySet()) {
            ret.add(ref.get());
        }
        return Collections.unmodifiableSet((Set<?>)ret);
    }
    
    public boolean equals(final Object o) {
        return this.backingStore.equals(((WeakIdentityHashMap)o).backingStore);
    }
    
    public Object get(final Object key) {
        this.reap();
        return this.backingStore.get(new IdentityWeakReference(key));
    }
    
    public Object put(final Object key, final Object value) {
        this.reap();
        return this.backingStore.put(new IdentityWeakReference(key), value);
    }
    
    public int hashCode() {
        this.reap();
        return this.backingStore.hashCode();
    }
    
    public boolean isEmpty() {
        this.reap();
        return this.backingStore.isEmpty();
    }
    
    public void putAll(final Map t) {
        throw new UnsupportedOperationException();
    }
    
    public Object remove(final Object key) {
        this.reap();
        return this.backingStore.remove(new IdentityWeakReference(key));
    }
    
    public int size() {
        this.reap();
        return this.backingStore.size();
    }
    
    public Collection values() {
        this.reap();
        return this.backingStore.values();
    }
    
    private synchronized void reap() {
        for (Object zombie = this.queue.poll(); zombie != null; zombie = this.queue.poll()) {
            final IdentityWeakReference victim = (IdentityWeakReference)zombie;
            this.backingStore.remove(victim);
        }
    }
    
    class IdentityWeakReference extends WeakReference
    {
        int hash;
        
        IdentityWeakReference(final Object obj) {
            super(obj, WeakIdentityHashMap.this.queue);
            this.hash = System.identityHashCode(obj);
        }
        
        public int hashCode() {
            return this.hash;
        }
        
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            final IdentityWeakReference ref = (IdentityWeakReference)o;
            return this.get() == ref.get();
        }
    }
}
