// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.logging.impl;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.Enumeration;
import java.lang.ref.ReferenceQueue;
import java.util.Hashtable;

public final class WeakHashtable extends Hashtable
{
    private static final long serialVersionUID = -1546036869799732453L;
    private static final int MAX_CHANGES_BEFORE_PURGE = 100;
    private static final int PARTIAL_PURGE_COUNT = 10;
    private final ReferenceQueue queue;
    private int changeCount;
    
    public WeakHashtable() {
        this.queue = new ReferenceQueue();
        this.changeCount = 0;
    }
    
    public boolean containsKey(final Object key) {
        final Referenced referenced = new Referenced(key);
        return super.containsKey(referenced);
    }
    
    public Enumeration elements() {
        this.purge();
        return super.elements();
    }
    
    public Set entrySet() {
        this.purge();
        final Set referencedEntries = super.entrySet();
        final Set unreferencedEntries = new HashSet();
        for (final Map.Entry entry : referencedEntries) {
            final Referenced referencedKey = entry.getKey();
            final Object key = referencedKey.getValue();
            final Object value = entry.getValue();
            if (key != null) {
                final Entry dereferencedEntry = new Entry(key, value);
                unreferencedEntries.add(dereferencedEntry);
            }
        }
        return unreferencedEntries;
    }
    
    public Object get(final Object key) {
        final Referenced referenceKey = new Referenced(key);
        return super.get(referenceKey);
    }
    
    public Enumeration keys() {
        this.purge();
        final Enumeration enumer = super.keys();
        return new Enumeration() {
            public boolean hasMoreElements() {
                return enumer.hasMoreElements();
            }
            
            public Object nextElement() {
                final Referenced nextReference = enumer.nextElement();
                return nextReference.getValue();
            }
        };
    }
    
    public Set keySet() {
        this.purge();
        final Set referencedKeys = super.keySet();
        final Set unreferencedKeys = new HashSet();
        for (final Referenced referenceKey : referencedKeys) {
            final Object keyValue = referenceKey.getValue();
            if (keyValue != null) {
                unreferencedKeys.add(keyValue);
            }
        }
        return unreferencedKeys;
    }
    
    public synchronized Object put(final Object key, final Object value) {
        if (key == null) {
            throw new NullPointerException("Null keys are not allowed");
        }
        if (value == null) {
            throw new NullPointerException("Null values are not allowed");
        }
        if (this.changeCount++ > 100) {
            this.purge();
            this.changeCount = 0;
        }
        else if (this.changeCount % 10 == 0) {
            this.purgeOne();
        }
        final Referenced keyRef = new Referenced(key, this.queue);
        return super.put(keyRef, value);
    }
    
    public void putAll(final Map t) {
        if (t != null) {
            final Set entrySet = t.entrySet();
            for (final Map.Entry entry : entrySet) {
                this.put(entry.getKey(), entry.getValue());
            }
        }
    }
    
    public Collection values() {
        this.purge();
        return super.values();
    }
    
    public synchronized Object remove(final Object key) {
        if (this.changeCount++ > 100) {
            this.purge();
            this.changeCount = 0;
        }
        else if (this.changeCount % 10 == 0) {
            this.purgeOne();
        }
        return super.remove(new Referenced(key));
    }
    
    public boolean isEmpty() {
        this.purge();
        return super.isEmpty();
    }
    
    public int size() {
        this.purge();
        return super.size();
    }
    
    public String toString() {
        this.purge();
        return super.toString();
    }
    
    protected void rehash() {
        this.purge();
        super.rehash();
    }
    
    private void purge() {
        final List toRemove = new ArrayList();
        synchronized (this.queue) {
            WeakKey key;
            while ((key = (WeakKey)this.queue.poll()) != null) {
                toRemove.add(key.getReferenced());
            }
        }
        for (int size = toRemove.size(), i = 0; i < size; ++i) {
            super.remove(toRemove.get(i));
        }
    }
    
    private void purgeOne() {
        synchronized (this.queue) {
            final WeakKey key = (WeakKey)this.queue.poll();
            if (key != null) {
                super.remove(key.getReferenced());
            }
        }
    }
    
    private static final class Entry implements Map.Entry
    {
        private final Object key;
        private final Object value;
        
        private Entry(final Object key, final Object value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean equals(final Object o) {
            boolean result = false;
            if (o != null && o instanceof Map.Entry) {
                final Map.Entry entry = (Map.Entry)o;
                boolean b = false;
                Label_0093: {
                    Label_0092: {
                        if (this.getKey() == null) {
                            if (entry.getKey() != null) {
                                break Label_0092;
                            }
                        }
                        else if (!this.getKey().equals(entry.getKey())) {
                            break Label_0092;
                        }
                        if ((this.getValue() != null) ? this.getValue().equals(entry.getValue()) : (entry.getValue() == null)) {
                            b = true;
                            break Label_0093;
                        }
                    }
                    b = false;
                }
                result = b;
            }
            return result;
        }
        
        public int hashCode() {
            return ((this.getKey() == null) ? 0 : this.getKey().hashCode()) ^ ((this.getValue() == null) ? 0 : this.getValue().hashCode());
        }
        
        public Object setValue(final Object value) {
            throw new UnsupportedOperationException("Entry.setValue is not supported.");
        }
        
        public Object getValue() {
            return this.value;
        }
        
        public Object getKey() {
            return this.key;
        }
    }
    
    private static final class Referenced
    {
        private final WeakReference reference;
        private final int hashCode;
        
        private Referenced(final Object referant) {
            this.reference = new WeakReference((T)referant);
            this.hashCode = referant.hashCode();
        }
        
        private Referenced(final Object key, final ReferenceQueue queue) {
            this.reference = new WeakKey(key, queue, this);
            this.hashCode = key.hashCode();
        }
        
        public int hashCode() {
            return this.hashCode;
        }
        
        private Object getValue() {
            return this.reference.get();
        }
        
        public boolean equals(final Object o) {
            boolean result = false;
            if (o instanceof Referenced) {
                final Referenced otherKey = (Referenced)o;
                final Object thisKeyValue = this.getValue();
                final Object otherKeyValue = otherKey.getValue();
                if (thisKeyValue == null) {
                    result = (otherKeyValue == null);
                    result = (result && this.hashCode() == otherKey.hashCode());
                }
                else {
                    result = thisKeyValue.equals(otherKeyValue);
                }
            }
            return result;
        }
    }
    
    private static final class WeakKey extends WeakReference
    {
        private final Referenced referenced;
        
        private WeakKey(final Object key, final ReferenceQueue queue, final Referenced referenced) {
            super(key, queue);
            this.referenced = referenced;
        }
        
        private Referenced getReferenced() {
            return this.referenced;
        }
    }
}
