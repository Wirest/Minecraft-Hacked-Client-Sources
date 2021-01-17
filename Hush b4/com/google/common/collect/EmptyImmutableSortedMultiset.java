// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Set;
import java.util.SortedSet;
import java.util.NavigableSet;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import java.util.Collection;
import javax.annotation.Nullable;
import java.util.Comparator;

final class EmptyImmutableSortedMultiset<E> extends ImmutableSortedMultiset<E>
{
    private final ImmutableSortedSet<E> elementSet;
    
    EmptyImmutableSortedMultiset(final Comparator<? super E> comparator) {
        this.elementSet = ImmutableSortedSet.emptySet(comparator);
    }
    
    @Override
    public Multiset.Entry<E> firstEntry() {
        return null;
    }
    
    @Override
    public Multiset.Entry<E> lastEntry() {
        return null;
    }
    
    @Override
    public int count(@Nullable final Object element) {
        return 0;
    }
    
    @Override
    public boolean containsAll(final Collection<?> targets) {
        return targets.isEmpty();
    }
    
    @Override
    public int size() {
        return 0;
    }
    
    @Override
    public ImmutableSortedSet<E> elementSet() {
        return this.elementSet;
    }
    
    @Override
    Multiset.Entry<E> getEntry(final int index) {
        throw new AssertionError((Object)"should never be called");
    }
    
    @Override
    public ImmutableSortedMultiset<E> headMultiset(final E upperBound, final BoundType boundType) {
        Preconditions.checkNotNull(upperBound);
        Preconditions.checkNotNull(boundType);
        return this;
    }
    
    @Override
    public ImmutableSortedMultiset<E> tailMultiset(final E lowerBound, final BoundType boundType) {
        Preconditions.checkNotNull(lowerBound);
        Preconditions.checkNotNull(boundType);
        return this;
    }
    
    @Override
    public UnmodifiableIterator<E> iterator() {
        return Iterators.emptyIterator();
    }
    
    @Override
    public boolean equals(@Nullable final Object object) {
        if (object instanceof Multiset) {
            final Multiset<?> other = (Multiset<?>)object;
            return other.isEmpty();
        }
        return false;
    }
    
    @Override
    boolean isPartialView() {
        return false;
    }
    
    @Override
    int copyIntoArray(final Object[] dst, final int offset) {
        return offset;
    }
    
    @Override
    public ImmutableList<E> asList() {
        return ImmutableList.of();
    }
}
