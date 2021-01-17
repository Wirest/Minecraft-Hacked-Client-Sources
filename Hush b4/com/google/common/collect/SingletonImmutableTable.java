// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Set;
import java.util.Collection;
import java.util.Map;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
class SingletonImmutableTable<R, C, V> extends ImmutableTable<R, C, V>
{
    final R singleRowKey;
    final C singleColumnKey;
    final V singleValue;
    
    SingletonImmutableTable(final R rowKey, final C columnKey, final V value) {
        this.singleRowKey = Preconditions.checkNotNull(rowKey);
        this.singleColumnKey = Preconditions.checkNotNull(columnKey);
        this.singleValue = Preconditions.checkNotNull(value);
    }
    
    SingletonImmutableTable(final Table.Cell<R, C, V> cell) {
        this(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
    }
    
    @Override
    public ImmutableMap<R, V> column(final C columnKey) {
        Preconditions.checkNotNull(columnKey);
        return this.containsColumn(columnKey) ? ImmutableMap.of(this.singleRowKey, this.singleValue) : ImmutableMap.of();
    }
    
    @Override
    public ImmutableMap<C, Map<R, V>> columnMap() {
        return (ImmutableMap<C, Map<R, V>>)ImmutableMap.of(this.singleColumnKey, ImmutableMap.of(this.singleRowKey, this.singleValue));
    }
    
    @Override
    public ImmutableMap<R, Map<C, V>> rowMap() {
        return (ImmutableMap<R, Map<C, V>>)ImmutableMap.of(this.singleRowKey, ImmutableMap.of(this.singleColumnKey, this.singleValue));
    }
    
    @Override
    public int size() {
        return 1;
    }
    
    @Override
    ImmutableSet<Table.Cell<R, C, V>> createCellSet() {
        return ImmutableSet.of(ImmutableTable.cellOf(this.singleRowKey, this.singleColumnKey, this.singleValue));
    }
    
    @Override
    ImmutableCollection<V> createValues() {
        return ImmutableSet.of(this.singleValue);
    }
}
