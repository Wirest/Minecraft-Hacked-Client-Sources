// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.SortedSet;
import java.util.Collection;
import java.util.Set;
import java.util.Iterator;
import java.util.Comparator;
import java.util.NavigableSet;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible(emulated = true)
public abstract class ForwardingSortedMultiset<E> extends ForwardingMultiset<E> implements SortedMultiset<E>
{
    protected ForwardingSortedMultiset() {
    }
    
    @Override
    protected abstract SortedMultiset<E> delegate();
    
    @Override
    public NavigableSet<E> elementSet() {
        return (NavigableSet<E>)(NavigableSet)super.elementSet();
    }
    
    @Override
    public Comparator<? super E> comparator() {
        return this.delegate().comparator();
    }
    
    @Override
    public SortedMultiset<E> descendingMultiset() {
        return this.delegate().descendingMultiset();
    }
    
    @Override
    public Multiset.Entry<E> firstEntry() {
        return this.delegate().firstEntry();
    }
    
    protected Multiset.Entry<E> standardFirstEntry() {
        final Iterator<Multiset.Entry<E>> entryIterator = this.entrySet().iterator();
        if (!entryIterator.hasNext()) {
            return null;
        }
        final Multiset.Entry<E> entry = entryIterator.next();
        return Multisets.immutableEntry(entry.getElement(), entry.getCount());
    }
    
    @Override
    public Multiset.Entry<E> lastEntry() {
        return this.delegate().lastEntry();
    }
    
    protected Multiset.Entry<E> standardLastEntry() {
        final Iterator<Multiset.Entry<E>> entryIterator = this.descendingMultiset().entrySet().iterator();
        if (!entryIterator.hasNext()) {
            return null;
        }
        final Multiset.Entry<E> entry = entryIterator.next();
        return Multisets.immutableEntry(entry.getElement(), entry.getCount());
    }
    
    @Override
    public Multiset.Entry<E> pollFirstEntry() {
        return this.delegate().pollFirstEntry();
    }
    
    protected Multiset.Entry<E> standardPollFirstEntry() {
        final Iterator<Multiset.Entry<E>> entryIterator = this.entrySet().iterator();
        if (!entryIterator.hasNext()) {
            return null;
        }
        Multiset.Entry<E> entry = entryIterator.next();
        entry = Multisets.immutableEntry(entry.getElement(), entry.getCount());
        entryIterator.remove();
        return entry;
    }
    
    @Override
    public Multiset.Entry<E> pollLastEntry() {
        return this.delegate().pollLastEntry();
    }
    
    protected Multiset.Entry<E> standardPollLastEntry() {
        final Iterator<Multiset.Entry<E>> entryIterator = this.descendingMultiset().entrySet().iterator();
        if (!entryIterator.hasNext()) {
            return null;
        }
        Multiset.Entry<E> entry = entryIterator.next();
        entry = Multisets.immutableEntry(entry.getElement(), entry.getCount());
        entryIterator.remove();
        return entry;
    }
    
    @Override
    public SortedMultiset<E> headMultiset(final E upperBound, final BoundType boundType) {
        return this.delegate().headMultiset(upperBound, boundType);
    }
    
    @Override
    public SortedMultiset<E> subMultiset(final E lowerBound, final BoundType lowerBoundType, final E upperBound, final BoundType upperBoundType) {
        return this.delegate().subMultiset(lowerBound, lowerBoundType, upperBound, upperBoundType);
    }
    
    protected SortedMultiset<E> standardSubMultiset(final E lowerBound, final BoundType lowerBoundType, final E upperBound, final BoundType upperBoundType) {
        return this.tailMultiset(lowerBound, lowerBoundType).headMultiset(upperBound, upperBoundType);
    }
    
    @Override
    public SortedMultiset<E> tailMultiset(final E lowerBound, final BoundType boundType) {
        return this.delegate().tailMultiset(lowerBound, boundType);
    }
    
    protected class StandardElementSet extends SortedMultisets.NavigableElementSet<E>
    {
        public StandardElementSet() {
            super(ForwardingSortedMultiset.this);
        }
    }
    
    protected abstract class StandardDescendingMultiset extends DescendingMultiset<E>
    {
        public StandardDescendingMultiset() {
        }
        
        @Override
        SortedMultiset<E> forwardMultiset() {
            return (SortedMultiset<E>)ForwardingSortedMultiset.this;
        }
    }
}
