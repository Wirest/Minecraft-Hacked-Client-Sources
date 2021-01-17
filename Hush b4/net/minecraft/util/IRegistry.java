// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

public interface IRegistry<K, V> extends Iterable<V>
{
    V getObject(final K p0);
    
    void putObject(final K p0, final V p1);
}
