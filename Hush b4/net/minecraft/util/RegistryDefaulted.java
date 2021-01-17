// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

public class RegistryDefaulted<K, V> extends RegistrySimple<K, V>
{
    private final V defaultObject;
    
    public RegistryDefaulted(final V defaultObjectIn) {
        this.defaultObject = defaultObjectIn;
    }
    
    @Override
    public V getObject(final K name) {
        final V v = super.getObject(name);
        return (v == null) ? this.defaultObject : v;
    }
}
