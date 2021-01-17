// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.SortedSet;
import java.util.Collection;
import java.util.Set;
import java.util.NavigableSet;
import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
final class UnmodifiableSortedMultiset<E> extends Multisets.UnmodifiableMultiset<E> implements SortedMultiset<E>
{
    private transient UnmodifiableSortedMultiset<E> descendingMultiset;
    private static final long serialVersionUID = 0L;
    
    UnmodifiableSortedMultiset(final SortedMultiset<E> delegate) {
        super(delegate);
    }
    
    @Override
    protected SortedMultiset<E> delegate() {
        return (SortedMultiset<E>)(SortedMultiset)super.delegate();
    }
    
    @Override
    public Comparator<? super E> comparator() {
        return this.delegate().comparator();
    }
    
    @Override
    NavigableSet<E> createElementSet() {
        return Sets.unmodifiableNavigableSet(this.delegate().elementSet());
    }
    
    @Override
    public NavigableSet<E> elementSet() {
        return (NavigableSet<E>)(NavigableSet)super.elementSet();
    }
    
    @Override
    public SortedMultiset<E> descendingMultiset() {
        UnmodifiableSortedMultiset<E> result = this.descendingMultiset;
        if (result == null) {
            result = new UnmodifiableSortedMultiset<E>(this.delegate().descendingMultiset());
            result.descendingMultiset = this;
            return this.descendingMultiset = result;
        }
        return result;
    }
    
    @Override
    public Multiset.Entry<E> firstEntry() {
        return this.delegate().firstEntry();
    }
    
    @Override
    public Multiset.Entry<E> lastEntry() {
        return this.delegate().lastEntry();
    }
    
    @Override
    public Multiset.Entry<E> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Multiset.Entry<E> pollLastEntry() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public SortedMultiset<E> headMultiset(final E upperBound, final BoundType boundType) {
        return Multisets.unmodifiableSortedMultiset(this.delegate().headMultiset(upperBound, boundType));
    }
    
    @Override
    public SortedMultiset<E> subMultiset(final E lowerBound, final BoundType lowerBoundType, final E upperBound, final BoundType upperBoundType) {
        return Multisets.unmodifiableSortedMultiset(this.delegate().subMultiset(lowerBound, lowerBoundType, upperBound, upperBoundType));
    }
    
    @Override
    public SortedMultiset<E> tailMultiset(final E lowerBound, final BoundType boundType) {
        return Multisets.unmodifiableSortedMultiset(this.delegate().tailMultiset(lowerBound, boundType));
    }
}
