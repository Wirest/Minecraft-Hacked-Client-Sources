// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.ListIterator;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
class RegularImmutableAsList<E> extends ImmutableAsList<E>
{
    private final ImmutableCollection<E> delegate;
    private final ImmutableList<? extends E> delegateList;
    
    RegularImmutableAsList(final ImmutableCollection<E> delegate, final ImmutableList<? extends E> delegateList) {
        this.delegate = delegate;
        this.delegateList = delegateList;
    }
    
    RegularImmutableAsList(final ImmutableCollection<E> delegate, final Object[] array) {
        this(delegate, ImmutableList.asImmutableList(array));
    }
    
    @Override
    ImmutableCollection<E> delegateCollection() {
        return this.delegate;
    }
    
    ImmutableList<? extends E> delegateList() {
        return this.delegateList;
    }
    
    @Override
    public UnmodifiableListIterator<E> listIterator(final int index) {
        return (UnmodifiableListIterator<E>)this.delegateList.listIterator(index);
    }
    
    @GwtIncompatible("not present in emulated superclass")
    @Override
    int copyIntoArray(final Object[] dst, final int offset) {
        return this.delegateList.copyIntoArray(dst, offset);
    }
    
    @Override
    public E get(final int index) {
        return (E)this.delegateList.get(index);
    }
}
