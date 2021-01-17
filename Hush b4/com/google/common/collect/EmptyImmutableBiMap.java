// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Set;
import java.util.Map;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
final class EmptyImmutableBiMap extends ImmutableBiMap<Object, Object>
{
    static final EmptyImmutableBiMap INSTANCE;
    
    private EmptyImmutableBiMap() {
    }
    
    @Override
    public ImmutableBiMap<Object, Object> inverse() {
        return this;
    }
    
    @Override
    public int size() {
        return 0;
    }
    
    @Override
    public boolean isEmpty() {
        return true;
    }
    
    @Override
    public Object get(@Nullable final Object key) {
        return null;
    }
    
    @Override
    public ImmutableSet<Map.Entry<Object, Object>> entrySet() {
        return ImmutableSet.of();
    }
    
    @Override
    ImmutableSet<Map.Entry<Object, Object>> createEntrySet() {
        throw new AssertionError((Object)"should never be called");
    }
    
    @Override
    public ImmutableSetMultimap<Object, Object> asMultimap() {
        return ImmutableSetMultimap.of();
    }
    
    @Override
    public ImmutableSet<Object> keySet() {
        return ImmutableSet.of();
    }
    
    @Override
    boolean isPartialView() {
        return false;
    }
    
    Object readResolve() {
        return EmptyImmutableBiMap.INSTANCE;
    }
    
    static {
        INSTANCE = new EmptyImmutableBiMap();
    }
}
