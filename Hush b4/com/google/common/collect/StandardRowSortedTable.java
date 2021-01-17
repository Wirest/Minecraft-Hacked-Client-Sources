// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import com.google.common.base.Supplier;
import java.util.Map;
import java.util.SortedMap;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
class StandardRowSortedTable<R, C, V> extends StandardTable<R, C, V> implements RowSortedTable<R, C, V>
{
    private static final long serialVersionUID = 0L;
    
    StandardRowSortedTable(final SortedMap<R, Map<C, V>> backingMap, final Supplier<? extends Map<C, V>> factory) {
        super(backingMap, factory);
    }
    
    private SortedMap<R, Map<C, V>> sortedBackingMap() {
        return (SortedMap<R, Map<C, V>>)(SortedMap)this.backingMap;
    }
    
    @Override
    public SortedSet<R> rowKeySet() {
        return (SortedSet<R>)(SortedSet)this.rowMap().keySet();
    }
    
    @Override
    public SortedMap<R, Map<C, V>> rowMap() {
        return (SortedMap<R, Map<C, V>>)(SortedMap)super.rowMap();
    }
    
    @Override
    SortedMap<R, Map<C, V>> createRowMap() {
        return new RowSortedMap();
    }
    
    private class RowSortedMap extends RowMap implements SortedMap<R, Map<C, V>>
    {
        @Override
        public SortedSet<R> keySet() {
            return (SortedSet<R>)(SortedSet)super.keySet();
        }
        
        @Override
        SortedSet<R> createKeySet() {
            return new Maps.SortedKeySet<R, Object>(this);
        }
        
        @Override
        public Comparator<? super R> comparator() {
            return (Comparator<? super R>)StandardRowSortedTable.this.sortedBackingMap().comparator();
        }
        
        @Override
        public R firstKey() {
            return StandardRowSortedTable.this.sortedBackingMap().firstKey();
        }
        
        @Override
        public R lastKey() {
            return StandardRowSortedTable.this.sortedBackingMap().lastKey();
        }
        
        @Override
        public SortedMap<R, Map<C, V>> headMap(final R toKey) {
            Preconditions.checkNotNull(toKey);
            return new StandardRowSortedTable((SortedMap<Object, Map<Object, Object>>)StandardRowSortedTable.this.sortedBackingMap().headMap(toKey), (Supplier<? extends Map<Object, Object>>)StandardRowSortedTable.this.factory).rowMap();
        }
        
        @Override
        public SortedMap<R, Map<C, V>> subMap(final R fromKey, final R toKey) {
            Preconditions.checkNotNull(fromKey);
            Preconditions.checkNotNull(toKey);
            return new StandardRowSortedTable((SortedMap<Object, Map<Object, Object>>)StandardRowSortedTable.this.sortedBackingMap().subMap(fromKey, toKey), (Supplier<? extends Map<Object, Object>>)StandardRowSortedTable.this.factory).rowMap();
        }
        
        @Override
        public SortedMap<R, Map<C, V>> tailMap(final R fromKey) {
            Preconditions.checkNotNull(fromKey);
            return new StandardRowSortedTable((SortedMap<Object, Map<Object, Object>>)StandardRowSortedTable.this.sortedBackingMap().tailMap(fromKey), (Supplier<? extends Map<Object, Object>>)StandardRowSortedTable.this.factory).rowMap();
        }
    }
}
