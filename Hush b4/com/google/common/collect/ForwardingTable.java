// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public abstract class ForwardingTable<R, C, V> extends ForwardingObject implements Table<R, C, V>
{
    protected ForwardingTable() {
    }
    
    @Override
    protected abstract Table<R, C, V> delegate();
    
    @Override
    public Set<Cell<R, C, V>> cellSet() {
        return this.delegate().cellSet();
    }
    
    @Override
    public void clear() {
        this.delegate().clear();
    }
    
    @Override
    public Map<R, V> column(final C columnKey) {
        return this.delegate().column(columnKey);
    }
    
    @Override
    public Set<C> columnKeySet() {
        return this.delegate().columnKeySet();
    }
    
    @Override
    public Map<C, Map<R, V>> columnMap() {
        return this.delegate().columnMap();
    }
    
    @Override
    public boolean contains(final Object rowKey, final Object columnKey) {
        return this.delegate().contains(rowKey, columnKey);
    }
    
    @Override
    public boolean containsColumn(final Object columnKey) {
        return this.delegate().containsColumn(columnKey);
    }
    
    @Override
    public boolean containsRow(final Object rowKey) {
        return this.delegate().containsRow(rowKey);
    }
    
    @Override
    public boolean containsValue(final Object value) {
        return this.delegate().containsValue(value);
    }
    
    @Override
    public V get(final Object rowKey, final Object columnKey) {
        return this.delegate().get(rowKey, columnKey);
    }
    
    @Override
    public boolean isEmpty() {
        return this.delegate().isEmpty();
    }
    
    @Override
    public V put(final R rowKey, final C columnKey, final V value) {
        return this.delegate().put(rowKey, columnKey, value);
    }
    
    @Override
    public void putAll(final Table<? extends R, ? extends C, ? extends V> table) {
        this.delegate().putAll(table);
    }
    
    @Override
    public V remove(final Object rowKey, final Object columnKey) {
        return this.delegate().remove(rowKey, columnKey);
    }
    
    @Override
    public Map<C, V> row(final R rowKey) {
        return this.delegate().row(rowKey);
    }
    
    @Override
    public Set<R> rowKeySet() {
        return this.delegate().rowKeySet();
    }
    
    @Override
    public Map<R, Map<C, V>> rowMap() {
        return this.delegate().rowMap();
    }
    
    @Override
    public int size() {
        return this.delegate().size();
    }
    
    @Override
    public Collection<V> values() {
        return this.delegate().values();
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj == this || this.delegate().equals(obj);
    }
    
    @Override
    public int hashCode() {
        return this.delegate().hashCode();
    }
}
