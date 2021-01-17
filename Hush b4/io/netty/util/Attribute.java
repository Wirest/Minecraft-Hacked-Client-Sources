// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util;

public interface Attribute<T>
{
    AttributeKey<T> key();
    
    T get();
    
    void set(final T p0);
    
    T getAndSet(final T p0);
    
    T setIfAbsent(final T p0);
    
    T getAndRemove();
    
    boolean compareAndSet(final T p0, final T p1);
    
    void remove();
}
