package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nullable;
import java.util.Map.Entry;

@GwtCompatible(serializable = true, emulated = true)
final class SingletonImmutableBiMap<K, V>
        extends ImmutableBiMap<K, V> {
    final transient K singleKey;
    final transient V singleValue;
    transient ImmutableBiMap<V, K> inverse;

    SingletonImmutableBiMap(K paramK, V paramV) {
        CollectPreconditions.checkEntryNotNull(paramK, paramV);
        this.singleKey = paramK;
        this.singleValue = paramV;
    }

    private SingletonImmutableBiMap(K paramK, V paramV, ImmutableBiMap<V, K> paramImmutableBiMap) {
        this.singleKey = paramK;
        this.singleValue = paramV;
        this.inverse = paramImmutableBiMap;
    }

    SingletonImmutableBiMap(Map.Entry<? extends K, ? extends V> paramEntry) {
        this(paramEntry.getKey(), paramEntry.getValue());
    }

    public V get(@Nullable Object paramObject) {
        return (V) (this.singleKey.equals(paramObject) ? this.singleValue : null);
    }

    public int size() {
        return 1;
    }

    public boolean containsKey(@Nullable Object paramObject) {
        return this.singleKey.equals(paramObject);
    }

    public boolean containsValue(@Nullable Object paramObject) {
        return this.singleValue.equals(paramObject);
    }

    boolean isPartialView() {
        return false;
    }

    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        return ImmutableSet.of(Maps.immutableEntry(this.singleKey, this.singleValue));
    }

    ImmutableSet<K> createKeySet() {
        return ImmutableSet.of(this.singleKey);
    }

    public ImmutableBiMap<V, K> inverse() {
        ImmutableBiMap localImmutableBiMap = this.inverse;
        if (localImmutableBiMap == null) {
            return this.inverse = new SingletonImmutableBiMap(this.singleValue, this.singleKey, this);
        }
        return localImmutableBiMap;
    }
}




