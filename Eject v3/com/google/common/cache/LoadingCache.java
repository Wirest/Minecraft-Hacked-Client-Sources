package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

@Beta
@GwtCompatible
public abstract interface LoadingCache<K, V>
        extends Cache<K, V>, Function<K, V> {
    public abstract V get(K paramK)
            throws ExecutionException;

    public abstract V getUnchecked(K paramK);

    public abstract ImmutableMap<K, V> getAll(Iterable<? extends K> paramIterable)
            throws ExecutionException;

    @Deprecated
    public abstract V apply(K paramK);

    public abstract void refresh(K paramK);

    public abstract ConcurrentMap<K, V> asMap();
}




