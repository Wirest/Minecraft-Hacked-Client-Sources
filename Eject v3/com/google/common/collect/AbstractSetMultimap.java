package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@GwtCompatible
abstract class AbstractSetMultimap<K, V>
        extends AbstractMapBasedMultimap<K, V>
        implements SetMultimap<K, V> {
    private static final long serialVersionUID = 7431625294878419160L;

    protected AbstractSetMultimap(Map<K, Collection<V>> paramMap) {
        super(paramMap);
    }

    abstract Set<V> createCollection();

    Set<V> createUnmodifiableEmptyCollection() {
        return ImmutableSet.of();
    }

    public Set<V> get(@Nullable K paramK) {
        return (Set) super.get(paramK);
    }

    public Set<Map.Entry<K, V>> entries() {
        return (Set) super.entries();
    }

    public Set<V> removeAll(@Nullable Object paramObject) {
        return (Set) super.removeAll(paramObject);
    }

    public Set<V> replaceValues(@Nullable K paramK, Iterable<? extends V> paramIterable) {
        return (Set) super.replaceValues(paramK, paramIterable);
    }

    public Map<K, Collection<V>> asMap() {
        return super.asMap();
    }

    public boolean put(@Nullable K paramK, @Nullable V paramV) {
        return super.put(paramK, paramV);
    }

    public boolean equals(@Nullable Object paramObject) {
        return super.equals(paramObject);
    }
}




