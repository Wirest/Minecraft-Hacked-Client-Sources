// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import javax.annotation.Nullable;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.concurrent.Immutable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
@Immutable
final class DenseImmutableTable<R, C, V> extends RegularImmutableTable<R, C, V>
{
    private final ImmutableMap<R, Integer> rowKeyToIndex;
    private final ImmutableMap<C, Integer> columnKeyToIndex;
    private final ImmutableMap<R, Map<C, V>> rowMap;
    private final ImmutableMap<C, Map<R, V>> columnMap;
    private final int[] rowCounts;
    private final int[] columnCounts;
    private final V[][] values;
    private final int[] iterationOrderRow;
    private final int[] iterationOrderColumn;
    
    private static <E> ImmutableMap<E, Integer> makeIndex(final ImmutableSet<E> set) {
        final ImmutableMap.Builder<E, Integer> indexBuilder = ImmutableMap.builder();
        int i = 0;
        for (final E key : set) {
            indexBuilder.put(key, i);
            ++i;
        }
        return indexBuilder.build();
    }
    
    DenseImmutableTable(final ImmutableList<Table.Cell<R, C, V>> cellList, final ImmutableSet<R> rowSpace, final ImmutableSet<C> columnSpace) {
        final V[][] array = (V[][])new Object[rowSpace.size()][columnSpace.size()];
        this.values = array;
        this.rowKeyToIndex = makeIndex(rowSpace);
        this.columnKeyToIndex = makeIndex(columnSpace);
        this.rowCounts = new int[this.rowKeyToIndex.size()];
        this.columnCounts = new int[this.columnKeyToIndex.size()];
        final int[] iterationOrderRow = new int[cellList.size()];
        final int[] iterationOrderColumn = new int[cellList.size()];
        for (int i = 0; i < cellList.size(); ++i) {
            final Table.Cell<R, C, V> cell = cellList.get(i);
            final R rowKey = cell.getRowKey();
            final C columnKey = cell.getColumnKey();
            final int rowIndex = this.rowKeyToIndex.get(rowKey);
            final int columnIndex = this.columnKeyToIndex.get(columnKey);
            final V existingValue = this.values[rowIndex][columnIndex];
            Preconditions.checkArgument(existingValue == null, "duplicate key: (%s, %s)", rowKey, columnKey);
            this.values[rowIndex][columnIndex] = cell.getValue();
            final int[] rowCounts = this.rowCounts;
            final int n = rowIndex;
            ++rowCounts[n];
            final int[] columnCounts = this.columnCounts;
            final int n2 = columnIndex;
            ++columnCounts[n2];
            iterationOrderRow[i] = rowIndex;
            iterationOrderColumn[i] = columnIndex;
        }
        this.iterationOrderRow = iterationOrderRow;
        this.iterationOrderColumn = iterationOrderColumn;
        this.rowMap = new RowMap();
        this.columnMap = new ColumnMap();
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
    public V get(@Nullable final Object rowKey, @Nullable final Object columnKey) {
        final Integer rowIndex = this.rowKeyToIndex.get(rowKey);
        final Integer columnIndex = this.columnKeyToIndex.get(columnKey);
        return (rowIndex == null || columnIndex == null) ? null : this.values[rowIndex][columnIndex];
    }
    
    @Override
    public int size() {
        return this.iterationOrderRow.length;
    }
    
    @Override
    Table.Cell<R, C, V> getCell(final int index) {
        final int rowIndex = this.iterationOrderRow[index];
        final int columnIndex = this.iterationOrderColumn[index];
        final R rowKey = (R)this.rowKeySet().asList().get(rowIndex);
        final C columnKey = (C)this.columnKeySet().asList().get(columnIndex);
        final V value = this.values[rowIndex][columnIndex];
        return ImmutableTable.cellOf(rowKey, columnKey, value);
    }
    
    @Override
    V getValue(final int index) {
        return this.values[this.iterationOrderRow[index]][this.iterationOrderColumn[index]];
    }
    
    private abstract static class ImmutableArrayMap<K, V> extends ImmutableMap<K, V>
    {
        private final int size;
        
        ImmutableArrayMap(final int size) {
            this.size = size;
        }
        
        abstract ImmutableMap<K, Integer> keyToIndex();
        
        private boolean isFull() {
            return this.size == this.keyToIndex().size();
        }
        
        K getKey(final int index) {
            return (K)this.keyToIndex().keySet().asList().get(index);
        }
        
        @Nullable
        abstract V getValue(final int p0);
        
        @Override
        ImmutableSet<K> createKeySet() {
            return this.isFull() ? this.keyToIndex().keySet() : super.createKeySet();
        }
        
        @Override
        public int size() {
            return this.size;
        }
        
        @Override
        public V get(@Nullable final Object key) {
            final Integer keyIndex = this.keyToIndex().get(key);
            return (keyIndex == null) ? null : this.getValue(keyIndex);
        }
        
        @Override
        ImmutableSet<Map.Entry<K, V>> createEntrySet() {
            return (ImmutableSet<Map.Entry<K, V>>)new ImmutableMapEntrySet<K, V>() {
                @Override
                ImmutableMap<K, V> map() {
                    return (ImmutableMap<K, V>)ImmutableArrayMap.this;
                }
                
                @Override
                public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
                    return new AbstractIterator<Map.Entry<K, V>>() {
                        private int index = -1;
                        private final int maxIndex = ImmutableArrayMap.this.keyToIndex().size();
                        
                        @Override
                        protected Map.Entry<K, V> computeNext() {
                            ++this.index;
                            while (this.index < this.maxIndex) {
                                final V value = ImmutableArrayMap.this.getValue(this.index);
                                if (value != null) {
                                    return Maps.immutableEntry(ImmutableArrayMap.this.getKey(this.index), value);
                                }
                                ++this.index;
                            }
                            return this.endOfData();
                        }
                    };
                }
            };
        }
    }
    
    private final class Row extends ImmutableArrayMap<C, V>
    {
        private final int rowIndex;
        
        Row(final int rowIndex) {
            super(DenseImmutableTable.this.rowCounts[rowIndex]);
            this.rowIndex = rowIndex;
        }
        
        @Override
        ImmutableMap<C, Integer> keyToIndex() {
            return DenseImmutableTable.this.columnKeyToIndex;
        }
        
        @Override
        V getValue(final int keyIndex) {
            return DenseImmutableTable.this.values[this.rowIndex][keyIndex];
        }
        
        @Override
        boolean isPartialView() {
            return true;
        }
    }
    
    private final class Column extends ImmutableArrayMap<R, V>
    {
        private final int columnIndex;
        
        Column(final int columnIndex) {
            super(DenseImmutableTable.this.columnCounts[columnIndex]);
            this.columnIndex = columnIndex;
        }
        
        @Override
        ImmutableMap<R, Integer> keyToIndex() {
            return DenseImmutableTable.this.rowKeyToIndex;
        }
        
        @Override
        V getValue(final int keyIndex) {
            return DenseImmutableTable.this.values[keyIndex][this.columnIndex];
        }
        
        @Override
        boolean isPartialView() {
            return true;
        }
    }
    
    private final class RowMap extends ImmutableArrayMap<R, Map<C, V>>
    {
        private RowMap() {
            super(DenseImmutableTable.this.rowCounts.length);
        }
        
        @Override
        ImmutableMap<R, Integer> keyToIndex() {
            return DenseImmutableTable.this.rowKeyToIndex;
        }
        
        @Override
        Map<C, V> getValue(final int keyIndex) {
            return new Row(keyIndex);
        }
        
        @Override
        boolean isPartialView() {
            return false;
        }
    }
    
    private final class ColumnMap extends ImmutableArrayMap<C, Map<R, V>>
    {
        private ColumnMap() {
            super(DenseImmutableTable.this.columnCounts.length);
        }
        
        @Override
        ImmutableMap<C, Integer> keyToIndex() {
            return DenseImmutableTable.this.columnKeyToIndex;
        }
        
        @Override
        Map<R, V> getValue(final int keyIndex) {
            return new Column(keyIndex);
        }
        
        @Override
        boolean isPartialView() {
            return false;
        }
    }
}
