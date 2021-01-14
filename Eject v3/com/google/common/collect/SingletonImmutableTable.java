package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

import java.util.Map;

@GwtCompatible
class SingletonImmutableTable<R, C, V>
        extends ImmutableTable<R, C, V> {
    final R singleRowKey;
    final C singleColumnKey;
    final V singleValue;

    SingletonImmutableTable(R paramR, C paramC, V paramV) {
        this.singleRowKey = Preconditions.checkNotNull(paramR);
        this.singleColumnKey = Preconditions.checkNotNull(paramC);
        this.singleValue = Preconditions.checkNotNull(paramV);
    }

    SingletonImmutableTable(Table.Cell<R, C, V> paramCell) {
        this(paramCell.getRowKey(), paramCell.getColumnKey(), paramCell.getValue());
    }

    public ImmutableMap<R, V> column(C paramC) {
        Preconditions.checkNotNull(paramC);
        return containsColumn(paramC) ? ImmutableMap.of(this.singleRowKey, this.singleValue) : ImmutableMap.of();
    }

    public ImmutableMap<C, Map<R, V>> columnMap() {
        return ImmutableMap.of(this.singleColumnKey, ImmutableMap.of(this.singleRowKey, this.singleValue));
    }

    public ImmutableMap<R, Map<C, V>> rowMap() {
        return ImmutableMap.of(this.singleRowKey, ImmutableMap.of(this.singleColumnKey, this.singleValue));
    }

    public int size() {
        return 1;
    }

    ImmutableSet<Table.Cell<R, C, V>> createCellSet() {
        return ImmutableSet.of(cellOf(this.singleRowKey, this.singleColumnKey, this.singleValue));
    }

    ImmutableCollection<V> createValues() {
        return ImmutableSet.of(this.singleValue);
    }
}




