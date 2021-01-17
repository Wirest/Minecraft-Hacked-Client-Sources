// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.NavigableSet;
import java.util.Iterator;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtIncompatible;
import java.util.Comparator;

class DescendingImmutableSortedSet<E> extends ImmutableSortedSet<E>
{
    private final ImmutableSortedSet<E> forward;
    
    DescendingImmutableSortedSet(final ImmutableSortedSet<E> forward) {
        super(Ordering.from(forward.comparator()).reverse());
        this.forward = forward;
    }
    
    @Override
    public int size() {
        return this.forward.size();
    }
    
    @Override
    public UnmodifiableIterator<E> iterator() {
        return this.forward.descendingIterator();
    }
    
    @Override
    ImmutableSortedSet<E> headSetImpl(final E toElement, final boolean inclusive) {
        return this.forward.tailSet(toElement, inclusive).descendingSet();
    }
    
    @Override
    ImmutableSortedSet<E> subSetImpl(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
        return this.forward.subSet(toElement, toInclusive, fromElement, fromInclusive).descendingSet();
    }
    
    @Override
    ImmutableSortedSet<E> tailSetImpl(final E fromElement, final boolean inclusive) {
        return this.forward.headSet(fromElement, inclusive).descendingSet();
    }
    
    @GwtIncompatible("NavigableSet")
    @Override
    public ImmutableSortedSet<E> descendingSet() {
        return this.forward;
    }
    
    @GwtIncompatible("NavigableSet")
    @Override
    public UnmodifiableIterator<E> descendingIterator() {
        return this.forward.iterator();
    }
    
    @GwtIncompatible("NavigableSet")
    @Override
    ImmutableSortedSet<E> createDescendingSet() {
        throw new AssertionError((Object)"should never be called");
    }
    
    @Override
    public E lower(final E element) {
        return this.forward.higher(element);
    }
    
    @Override
    public E floor(final E element) {
        return this.forward.ceiling(element);
    }
    
    @Override
    public E ceiling(final E element) {
        return this.forward.floor(element);
    }
    
    @Override
    public E higher(final E element) {
        return this.forward.lower(element);
    }
    
    @Override
    int indexOf(@Nullable final Object target) {
        final int index = this.forward.indexOf(target);
        if (index == -1) {
            return index;
        }
        return this.size() - 1 - index;
    }
    
    @Override
    boolean isPartialView() {
        return this.forward.isPartialView();
    }
}
