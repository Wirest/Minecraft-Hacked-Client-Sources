package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;

@GwtCompatible(emulated = true)
class RegularImmutableAsList<E>
        extends ImmutableAsList<E> {
    private final ImmutableCollection<E> delegate;
    private final ImmutableList<? extends E> delegateList;

    RegularImmutableAsList(ImmutableCollection<E> paramImmutableCollection, ImmutableList<? extends E> paramImmutableList) {
        this.delegate = paramImmutableCollection;
        this.delegateList = paramImmutableList;
    }

    RegularImmutableAsList(ImmutableCollection<E> paramImmutableCollection, Object[] paramArrayOfObject) {
        this(paramImmutableCollection, ImmutableList.asImmutableList(paramArrayOfObject));
    }

    ImmutableCollection<E> delegateCollection() {
        return this.delegate;
    }

    ImmutableList<? extends E> delegateList() {
        return this.delegateList;
    }

    public UnmodifiableListIterator<E> listIterator(int paramInt) {
        return this.delegateList.listIterator(paramInt);
    }

    @GwtIncompatible("not present in emulated superclass")
    int copyIntoArray(Object[] paramArrayOfObject, int paramInt) {
        return this.delegateList.copyIntoArray(paramArrayOfObject, paramInt);
    }

    public E get(int paramInt) {
        return (E) this.delegateList.get(paramInt);
    }
}




