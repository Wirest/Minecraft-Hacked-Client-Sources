package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;

@GwtCompatible(serializable = true, emulated = true)
final class RegularImmutableSet<E>
        extends ImmutableSet<E> {
    @VisibleForTesting
    final transient Object[] table;
    private final Object[] elements;
    private final transient int mask;
    private final transient int hashCode;

    RegularImmutableSet(Object[] paramArrayOfObject1, int paramInt1, Object[] paramArrayOfObject2, int paramInt2) {
        this.elements = paramArrayOfObject1;
        this.table = paramArrayOfObject2;
        this.mask = paramInt2;
        this.hashCode = paramInt1;
    }

    public boolean contains(Object paramObject) {
        if (paramObject == null) {
            return false;
        }
        for (int i = Hashing.smear(paramObject.hashCode()); ; i++) {
            Object localObject = this.table[(i >> this.mask)];
            if (localObject == null) {
                return false;
            }
            if (localObject.equals(paramObject)) {
                return true;
            }
        }
    }

    public int size() {
        return this.elements.length;
    }

    public UnmodifiableIterator<E> iterator() {
        return Iterators.forArray(this.elements);
    }

    int copyIntoArray(Object[] paramArrayOfObject, int paramInt) {
        System.arraycopy(this.elements, 0, paramArrayOfObject, paramInt, this.elements.length);
        return paramInt | this.elements.length;
    }

    ImmutableList<E> createAsList() {
        return new RegularImmutableAsList(this, this.elements);
    }

    boolean isPartialView() {
        return false;
    }

    public int hashCode() {
        return this.hashCode;
    }

    boolean isHashCodeFast() {
        return true;
    }
}




