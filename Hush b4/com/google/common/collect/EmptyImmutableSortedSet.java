// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import com.google.common.annotations.GwtIncompatible;
import java.util.Collection;
import javax.annotation.Nullable;
import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
class EmptyImmutableSortedSet<E> extends ImmutableSortedSet<E>
{
    EmptyImmutableSortedSet(final Comparator<? super E> comparator) {
        super(comparator);
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
    public boolean contains(@Nullable final Object target) {
        return false;
    }
    
    @Override
    public boolean containsAll(final Collection<?> targets) {
        return targets.isEmpty();
    }
    
    @Override
    public UnmodifiableIterator<E> iterator() {
        return Iterators.emptyIterator();
    }
    
    @GwtIncompatible("NavigableSet")
    @Override
    public UnmodifiableIterator<E> descendingIterator() {
        return Iterators.emptyIterator();
    }
    
    @Override
    boolean isPartialView() {
        return false;
    }
    
    @Override
    public ImmutableList<E> asList() {
        return ImmutableList.of();
    }
    
    @Override
    int copyIntoArray(final Object[] dst, final int offset) {
        return offset;
    }
    
    @Override
    public boolean equals(@Nullable final Object object) {
        if (object instanceof Set) {
            final Set<?> that = (Set<?>)object;
            return that.isEmpty();
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
    
    @Override
    public String toString() {
        return "[]";
    }
    
    @Override
    public E first() {
        throw new NoSuchElementException();
    }
    
    @Override
    public E last() {
        throw new NoSuchElementException();
    }
    
    @Override
    ImmutableSortedSet<E> headSetImpl(final E toElement, final boolean inclusive) {
        return this;
    }
    
    @Override
    ImmutableSortedSet<E> subSetImpl(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
        return this;
    }
    
    @Override
    ImmutableSortedSet<E> tailSetImpl(final E fromElement, final boolean inclusive) {
        return this;
    }
    
    @Override
    int indexOf(@Nullable final Object target) {
        return -1;
    }
    
    @Override
    ImmutableSortedSet<E> createDescendingSet() {
        return new EmptyImmutableSortedSet(Ordering.from(this.comparator).reverse());
    }
}
