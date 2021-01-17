// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Set;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import com.google.common.base.Preconditions;
import java.util.EnumMap;
import java.util.Map;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class EnumBiMap<K extends Enum<K>, V extends Enum<V>> extends AbstractBiMap<K, V>
{
    private transient Class<K> keyType;
    private transient Class<V> valueType;
    @GwtIncompatible("not needed in emulated source.")
    private static final long serialVersionUID = 0L;
    
    public static <K extends Enum<K>, V extends Enum<V>> EnumBiMap<K, V> create(final Class<K> keyType, final Class<V> valueType) {
        return new EnumBiMap<K, V>(keyType, valueType);
    }
    
    public static <K extends Enum<K>, V extends Enum<V>> EnumBiMap<K, V> create(final Map<K, V> map) {
        final EnumBiMap<K, V> bimap = create((Class<K>)inferKeyType((Map<K, ?>)map), (Class<V>)inferValueType((Map<?, V>)map));
        bimap.putAll((Map)map);
        return bimap;
    }
    
    private EnumBiMap(final Class<K> keyType, final Class<V> valueType) {
        super((Map<Object, Object>)WellBehavedMap.wrap((Map<K, V>)new EnumMap<K, Object>(keyType)), (Map<Object, Object>)WellBehavedMap.wrap((Map<V, V>)new EnumMap<V, Object>(valueType)));
        this.keyType = keyType;
        this.valueType = valueType;
    }
    
    static <K extends Enum<K>> Class<K> inferKeyType(final Map<K, ?> map) {
        if (map instanceof EnumBiMap) {
            return ((EnumBiMap)map).keyType();
        }
        if (map instanceof EnumHashBiMap) {
            return ((EnumHashBiMap)map).keyType();
        }
        Preconditions.checkArgument(!map.isEmpty());
        return map.keySet().iterator().next().getDeclaringClass();
    }
    
    private static <V extends Enum<V>> Class<V> inferValueType(final Map<?, V> map) {
        if (map instanceof EnumBiMap) {
            return (Class<V>)((EnumBiMap)map).valueType;
        }
        Preconditions.checkArgument(!map.isEmpty());
        return map.values().iterator().next().getDeclaringClass();
    }
    
    public Class<K> keyType() {
        return this.keyType;
    }
    
    public Class<V> valueType() {
        return this.valueType;
    }
    
    @Override
    K checkKey(final K key) {
        return Preconditions.checkNotNull(key);
    }
    
    @Override
    V checkValue(final V value) {
        return Preconditions.checkNotNull(value);
    }
    
    @GwtIncompatible("java.io.ObjectOutputStream")
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(this.keyType);
        stream.writeObject(this.valueType);
        Serialization.writeMap((Map<Object, Object>)this, stream);
    }
    
    @GwtIncompatible("java.io.ObjectInputStream")
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.keyType = (Class<K>)stream.readObject();
        this.valueType = (Class<V>)stream.readObject();
        this.setDelegates(WellBehavedMap.wrap(new EnumMap<K, V>(this.keyType)), (Map<V, K>)WellBehavedMap.wrap((Map<V, V>)new EnumMap<V, Object>(this.valueType)));
        Serialization.populateMap((Map<Object, Object>)this, stream);
    }
}
