// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.concurrent.Immutable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
@Immutable
final class SparseImmutableTable<R, C, V> extends RegularImmutableTable<R, C, V>
{
    private final ImmutableMap<R, Map<C, V>> rowMap;
    private final ImmutableMap<C, Map<R, V>> columnMap;
    private final int[] iterationOrderRow;
    private final int[] iterationOrderColumn;
    
    SparseImmutableTable(final ImmutableList<Table.Cell<R, C, V>> cellList, final ImmutableSet<R> rowSpace, final ImmutableSet<C> columnSpace) {
        final Map<R, Integer> rowIndex = (Map<R, Integer>)Maps.newHashMap();
        final Map<R, Map<C, V>> rows = (Map<R, Map<C, V>>)Maps.newLinkedHashMap();
        for (final R row : rowSpace) {
            rowIndex.put(row, rows.size());
            rows.put(row, new LinkedHashMap<C, V>());
        }
        final Map<C, Map<R, V>> columns = (Map<C, Map<R, V>>)Maps.newLinkedHashMap();
        for (final C col : columnSpace) {
            columns.put(col, new LinkedHashMap<R, V>());
        }
        final int[] iterationOrderRow = new int[cellList.size()];
        final int[] iterationOrderColumn = new int[cellList.size()];
        for (int i = 0; i < cellList.size(); ++i) {
            final Table.Cell<R, C, V> cell = cellList.get(i);
            final R rowKey = cell.getRowKey();
            final C columnKey = cell.getColumnKey();
            final V value = cell.getValue();
            iterationOrderRow[i] = rowIndex.get(rowKey);
            final Map<C, V> thisRow = rows.get(rowKey);
            iterationOrderColumn[i] = thisRow.size();
            final V oldValue = thisRow.put(columnKey, value);
            if (oldValue != null) {
                throw new IllegalArgumentException("Duplicate value for row=" + rowKey + ", column=" + columnKey + ": " + value + ", " + oldValue);
            }
            columns.get(columnKey).put(rowKey, value);
        }
        this.iterationOrderRow = iterationOrderRow;
        this.iterationOrderColumn = iterationOrderColumn;
        final ImmutableMap.Builder<R, Map<C, V>> rowBuilder = ImmutableMap.builder();
        for (final Map.Entry<R, Map<C, V>> row2 : rows.entrySet()) {
            rowBuilder.put(row2.getKey(), (Map<C, V>)ImmutableMap.copyOf((Map<?, ?>)row2.getValue()));
        }
        this.rowMap = rowBuilder.build();
        final ImmutableMap.Builder<C, Map<R, V>> columnBuilder = ImmutableMap.builder();
        for (final Map.Entry<C, Map<R, V>> col2 : columns.entrySet()) {
            columnBuilder.put(col2.getKey(), (Map<R, V>)ImmutableMap.copyOf((Map<?, ?>)col2.getValue()));
        }
        this.columnMap = columnBuilder.build();
    }
    
    @Override
    public ImmutableMap<C, Map<R, V>> columnMap() {
        return this.columnMap;
    }
    
    @Override
    public ImmutableMap<R, Map<C, V>> rowMap() {
        return this.rowMap;
    }
    
    @Override
    public int size() {
        return this.iterationOrderRow.length;
    }
    
    @Override
    Table.Cell<R, C, V> getCell(final int index) {
        final int rowIndex = this.iterationOrderRow[index];
        final Map.Entry<R, Map<C, V>> rowEntry = this.rowMap.entrySet().asList().get(rowIndex);
        final ImmutableMap<C, V> row = rowEntry.getValue();
        final int columnIndex = this.iterationOrderColumn[index];
        final Map.Entry<C, V> colEntry = row.entrySet().asList().get(columnIndex);
        return ImmutableTable.cellOf(rowEntry.getKey(), colEntry.getKey(), colEntry.getValue());
    }
    
    @Override
    V getValue(final int index) {
        final int rowIndex = this.iterationOrderRow[index];
        final ImmutableMap<C, V> row = this.rowMap.values().asList().get(rowIndex);
        final int columnIndex = this.iterationOrderColumn[index];
        return row.values().asList().get(columnIndex);
    }
}
