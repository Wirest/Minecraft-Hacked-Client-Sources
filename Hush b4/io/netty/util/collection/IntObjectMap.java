// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.collection;

public interface IntObjectMap<V>
{
    V get(final int p0);
    
    V put(final int p0, final V p1);
    
    void putAll(final IntObjectMap<V> p0);
    
    V remove(final int p0);
    
    int size();
    
    boolean isEmpty();
    
    void clear();
    
    boolean containsKey(final int p0);
    
    boolean containsValue(final V p0);
    
    Iterable<Entry<V>> entries();
    
    int[] keys();
    
    V[] values(final Class<V> p0);
    
    public interface Entry<V>
    {
        int key();
        
        V value();
        
        void setValue(final V p0);
    }
}
