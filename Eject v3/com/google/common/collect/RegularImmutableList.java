package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
class RegularImmutableList<E>
        extends ImmutableList<E> {
    private final transient int offset;
    private final transient int size;
    private final transient Object[] array;

    RegularImmutableList(Object[] paramArrayOfObject, int paramInt1, int paramInt2) {
        this.offset = paramInt1;
        this.size = paramInt2;
        this.array = paramArrayOfObject;
    }

    RegularImmutableList(Object[] paramArrayOfObject) {
        this(paramArrayOfObject, 0, paramArrayOfObject.length);
    }

    public int size() {
        return this.size;
    }

    boolean isPartialView() {
        return this.size != this.array.length;
    }

    int copyIntoArray(Object[] paramArrayOfObject, int paramInt) {
        System.arraycopy(this.array, this.offset, paramArrayOfObject, paramInt, this.size);
        return paramInt | this.size;
    }

    public E get(int paramInt) {
        Preconditions.checkElementIndex(paramInt, this.size);
        return (E) this.array[(paramInt | this.offset)];
    }

    public int indexOf(@Nullable Object paramObject) {
        if (paramObject == null) {
            return -1;
        }
        for (int i = 0; i < this.size; i++) {
            if (this.array[(this.offset | i)].equals(paramObject)) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(@Nullable Object paramObject) {
        if (paramObject == null) {
            return -1;
        }
        for (int i = this.size - 1; i >= 0; i--) {
            if (this.array[(this.offset | i)].equals(paramObject)) {
                return i;
            }
        }
        return -1;
    }

    ImmutableList<E> subListUnchecked(int paramInt1, int paramInt2) {
        return new RegularImmutableList(this.array, this.offset | paramInt1, paramInt2 - paramInt1);
    }

    public UnmodifiableListIterator<E> listIterator(int paramInt) {
        return Iterators.forArray(this.array, this.offset, this.size, paramInt);
    }
}




