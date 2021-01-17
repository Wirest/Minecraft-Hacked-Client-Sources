// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.management;

import java.util.Collection;
import java.util.Set;
import java.util.Iterator;
import com.google.common.collect.Maps;
import java.util.Map;

public class LowerStringMap<V> implements Map<String, V>
{
    private final Map<String, V> internalMap;
    
    public LowerStringMap() {
        this.internalMap = (Map<String, V>)Maps.newLinkedHashMap();
    }
    
    @Override
    public int size() {
        return this.internalMap.size();
    }
    
    @Override
    public boolean isEmpty() {
        return this.internalMap.isEmpty();
    }
    
    @Override
    public boolean containsKey(final Object p_containsKey_1_) {
        return this.internalMap.containsKey(p_containsKey_1_.toString().toLowerCase());
    }
    
    @Override
    public boolean containsValue(final Object p_containsValue_1_) {
        return this.internalMap.containsKey(p_containsValue_1_);
    }
    
    @Override
    public V get(final Object p_get_1_) {
        return this.internalMap.get(p_get_1_.toString().toLowerCase());
    }
    
    @Override
    public V put(final String p_put_1_, final V p_put_2_) {
        return this.internalMap.put(p_put_1_.toLowerCase(), p_put_2_);
    }
    
    @Override
    public V remove(final Object p_remove_1_) {
        return this.internalMap.remove(p_remove_1_.toString().toLowerCase());
    }
    
    @Override
    public void putAll(final Map<? extends String, ? extends V> p_putAll_1_) {
        for (final Entry<? extends String, ? extends V> entry : p_putAll_1_.entrySet()) {
            this.put((String)entry.getKey(), entry.getValue());
        }
    }
    
    @Override
    public void clear() {
        this.internalMap.clear();
    }
    
    @Override
    public Set<String> keySet() {
        return this.internalMap.keySet();
    }
    
    @Override
    public Collection<V> values() {
        return this.internalMap.values();
    }
    
    @Override
    public Set<Entry<String, V>> entrySet() {
        return this.internalMap.entrySet();
    }
}
