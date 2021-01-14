package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;

@GwtCompatible(serializable = true, emulated = true)
final class EmptyImmutableSet
        extends ImmutableSet<Object> {
    static final EmptyImmutableSet INSTANCE = new EmptyImmutableSet();
    private static final long serialVersionUID = 0L;

    public int size() {
        return 0;
    }

    public boolean isEmpty() {
        return true;
    }

    public boolean contains(@Nullable Object paramObject) {
        return false;
    }

    public boolean containsAll(Collection<?> paramCollection) {
        return paramCollection.isEmpty();
    }

    public UnmodifiableIterator<Object> iterator() {
        return Iterators.emptyIterator();
    }

    boolean isPartialView() {
        return false;
    }

    int copyIntoArray(Object[] paramArrayOfObject, int paramInt) {
        return paramInt;
    }

    public ImmutableList<Object> asList() {
        return ImmutableList.of();
    }

    public boolean equals(@Nullable Object paramObject) {
        if ((paramObject instanceof Set)) {
            Set localSet = (Set) paramObject;
            return localSet.isEmpty();
        }
        return false;
    }

    public final int hashCode() {
        return 0;
    }

    boolean isHashCodeFast() {
        return true;
    }

    public String toString() {
        return "[]";
    }

    Object readResolve() {
        return INSTANCE;
    }
}




