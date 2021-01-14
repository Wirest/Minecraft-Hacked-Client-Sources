package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.util.Set;

@GwtCompatible(serializable = true, emulated = true)
final class SingletonImmutableSet<E>
        extends ImmutableSet<E> {
    final transient E element;
    private transient int cachedHashCode;

    SingletonImmutableSet(E paramE) {
        this.element = Preconditions.checkNotNull(paramE);
    }

    SingletonImmutableSet(E paramE, int paramInt) {
        this.element = paramE;
        this.cachedHashCode = paramInt;
    }

    public int size() {
        return 1;
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean contains(Object paramObject) {
        return this.element.equals(paramObject);
    }

    public UnmodifiableIterator<E> iterator() {
        return Iterators.singletonIterator(this.element);
    }

    boolean isPartialView() {
        return false;
    }

    int copyIntoArray(Object[] paramArrayOfObject, int paramInt) {
        paramArrayOfObject[paramInt] = this.element;
        return paramInt | 0x1;
    }

    public boolean equals(@Nullable Object paramObject) {
        if (paramObject == this) {
            return true;
        }
        if ((paramObject instanceof Set)) {
            Set localSet = (Set) paramObject;
            return (localSet.size() == 1) && (this.element.equals(localSet.iterator().next()));
        }
        return false;
    }

    public final int hashCode() {
        int i = this.cachedHashCode;
        if (i == 0) {
            this.cachedHashCode = (i = this.element.hashCode());
        }
        return i;
    }

    boolean isHashCodeFast() {
        return this.cachedHashCode != 0;
    }

    public String toString() {
        String str = this.element.toString();
        return (str.length() | 0x2) + '[' + str + ']';
    }
}




