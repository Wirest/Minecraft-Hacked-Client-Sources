package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@GwtCompatible
public abstract interface Table<R, C, V> {
    public abstract boolean contains(@Nullable Object paramObject1, @Nullable Object paramObject2);

    public abstract boolean containsRow(@Nullable Object paramObject);

    public abstract boolean containsColumn(@Nullable Object paramObject);

    public abstract boolean containsValue(@Nullable Object paramObject);

    public abstract V get(@Nullable Object paramObject1, @Nullable Object paramObject2);

    public abstract boolean isEmpty();

    public abstract int size();

    public abstract boolean equals(@Nullable Object paramObject);

    public abstract int hashCode();

    public abstract void clear();

    public abstract V put(R paramR, C paramC, V paramV);

    public abstract void putAll(Table<? extends R, ? extends C, ? extends V> paramTable);

    public abstract V remove(@Nullable Object paramObject1, @Nullable Object paramObject2);

    public abstract Map<C, V> row(R paramR);

    public abstract Map<R, V> column(C paramC);

    public abstract Set<Cell<R, C, V>> cellSet();

    public abstract Set<R> rowKeySet();

    public abstract Set<C> columnKeySet();

    public abstract Collection<V> values();

    public abstract Map<R, Map<C, V>> rowMap();

    public abstract Map<C, Map<R, V>> columnMap();

    public static abstract interface Cell<R, C, V> {
        public abstract R getRowKey();

        public abstract C getColumnKey();

        public abstract V getValue();

        public abstract boolean equals(@Nullable Object paramObject);

        public abstract int hashCode();
    }
}




