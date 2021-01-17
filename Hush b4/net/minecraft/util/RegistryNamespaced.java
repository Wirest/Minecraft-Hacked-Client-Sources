// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import java.util.Iterator;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.BiMap;
import java.util.Map;

public class RegistryNamespaced<K, V> extends RegistrySimple<K, V> implements IObjectIntIterable<V>
{
    protected final ObjectIntIdentityMap underlyingIntegerMap;
    protected final Map<V, K> inverseObjectRegistry;
    
    public RegistryNamespaced() {
        this.underlyingIntegerMap = new ObjectIntIdentityMap();
        this.inverseObjectRegistry = (Map<V, K>)((BiMap)this.registryObjects).inverse();
    }
    
    public void register(final int id, final K p_177775_2_, final V p_177775_3_) {
        this.underlyingIntegerMap.put(p_177775_3_, id);
        this.putObject(p_177775_2_, p_177775_3_);
    }
    
    @Override
    protected Map<K, V> createUnderlyingMap() {
        return (Map<K, V>)HashBiMap.create();
    }
    
    @Override
    public V getObject(final K name) {
        return super.getObject(name);
    }
    
    public K getNameForObject(final V p_177774_1_) {
        return this.inverseObjectRegistry.get(p_177774_1_);
    }
    
    @Override
    public boolean containsKey(final K p_148741_1_) {
        return super.containsKey(p_148741_1_);
    }
    
    public int getIDForObject(final V p_148757_1_) {
        return this.underlyingIntegerMap.get(p_148757_1_);
    }
    
    public V getObjectById(final int id) {
        return (V)this.underlyingIntegerMap.getByValue(id);
    }
    
    @Override
    public Iterator<V> iterator() {
        return (Iterator<V>)this.underlyingIntegerMap.iterator();
    }
}
