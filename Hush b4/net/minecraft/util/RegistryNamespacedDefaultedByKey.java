// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import org.apache.commons.lang3.Validate;

public class RegistryNamespacedDefaultedByKey<K, V> extends RegistryNamespaced<K, V>
{
    private final K defaultValueKey;
    private V defaultValue;
    
    public RegistryNamespacedDefaultedByKey(final K p_i46017_1_) {
        this.defaultValueKey = p_i46017_1_;
    }
    
    @Override
    public void register(final int id, final K p_177775_2_, final V p_177775_3_) {
        if (this.defaultValueKey.equals(p_177775_2_)) {
            this.defaultValue = p_177775_3_;
        }
        super.register(id, p_177775_2_, p_177775_3_);
    }
    
    public void validateKey() {
        Validate.notNull(this.defaultValueKey);
    }
    
    @Override
    public V getObject(final K name) {
        final V v = super.getObject(name);
        return (v == null) ? this.defaultValue : v;
    }
    
    @Override
    public V getObjectById(final int id) {
        final V v = super.getObjectById(id);
        return (v == null) ? this.defaultValue : v;
    }
}
