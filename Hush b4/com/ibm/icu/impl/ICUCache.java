// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

public interface ICUCache<K, V>
{
    public static final int SOFT = 0;
    public static final int WEAK = 1;
    public static final Object NULL = new Object();
    
    void clear();
    
    void put(final K p0, final V p1);
    
    V get(final Object p0);
}
