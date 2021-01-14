package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

import java.util.NoSuchElementException;

@GwtCompatible
abstract class AbstractIndexedListIterator<E>
        extends UnmodifiableListIterator<E> {
    private final int size;
    private int position;

    protected AbstractIndexedListIterator(int paramInt) {
        this(paramInt, 0);
    }

    protected AbstractIndexedListIterator(int paramInt1, int paramInt2) {
        Preconditions.checkPositionIndex(paramInt2, paramInt1);
        this.size = paramInt1;
        this.position = paramInt2;
    }

    protected abstract E get(int paramInt);

    public final boolean hasNext() {
        return this.position < this.size;
    }

    public final E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        int tmp21_18 = this.position;
        this.position = (tmp21_18 | 0x1);
        return (E) get(tmp21_18);
    }

    public final int nextIndex() {
        return this.position;
    }

    public final boolean hasPrevious() {
        return this.position > 0;
    }

    public final E previous() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        return (E) get(--this.position);
    }

    public final int previousIndex() {
        return this.position - 1;
    }
}




