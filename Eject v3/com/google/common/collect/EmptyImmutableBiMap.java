package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nullable;
import java.util.Map.Entry;

@GwtCompatible(emulated = true)
final class EmptyImmutableBiMap
        extends ImmutableBiMap<Object, Object> {
    static final EmptyImmutableBiMap INSTANCE = new EmptyImmutableBiMap();

    public ImmutableBiMap<Object, Object> inverse() {
        return this;
    }

    public int size() {
        return 0;
    }

    public boolean isEmpty() {
        return true;
    }

    public Object get(@Nullable Object paramObject) {
        return null;
    }

    public ImmutableSet<Map.Entry<Object, Object>> entrySet() {
        return ImmutableSet.of();
    }

    ImmutableSet<Map.Entry<Object, Object>> createEntrySet() {
        throw new AssertionError("should never be called");
    }

    public ImmutableSetMultimap<Object, Object> asMultimap() {
        return ImmutableSetMultimap.of();
    }

    public ImmutableSet<Object> keySet() {
        return ImmutableSet.of();
    }

    boolean isPartialView() {
        return false;
    }

    Object readResolve() {
        return INSTANCE;
    }
}




