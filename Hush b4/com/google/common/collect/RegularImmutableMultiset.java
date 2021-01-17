// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Set;
import java.util.Map;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true)
class RegularImmutableMultiset<E> extends ImmutableMultiset<E>
{
    private final transient ImmutableMap<E, Integer> map;
    private final transient int size;
    
    RegularImmutableMultiset(final ImmutableMap<E, Integer> map, final int size) {
        this.map = map;
        this.size = size;
    }
    
    @Override
    boolean isPartialView() {
        return this.map.isPartialView();
    }
    
    @Override
    public int count(@Nullable final Object element) {
        final Integer value = this.map.get(element);
        return (value == null) ? 0 : value;
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean contains(@Nullable final Object element) {
        return this.map.containsKey(element);
    }
    
    @Override
    public ImmutableSet<E> elementSet() {
        return this.map.keySet();
    }
    
    @Override
    Multiset.Entry<E> getEntry(final int index) {
        final Map.Entry<E, Integer> mapEntry = this.map.entrySet().asList().get(index);
        return Multisets.immutableEntry(mapEntry.getKey(), mapEntry.getValue());
    }
    
    @Override
    public int hashCode() {
        return this.map.hashCode();
    }
}
