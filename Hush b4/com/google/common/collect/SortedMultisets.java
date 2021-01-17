// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Iterator;
import com.google.common.annotations.GwtIncompatible;
import java.util.NavigableSet;
import java.util.Comparator;
import java.util.SortedSet;
import javax.annotation.Nullable;
import java.util.NoSuchElementException;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
final class SortedMultisets
{
    private SortedMultisets() {
    }
    
    private static <E> E getElementOrThrow(final Multiset.Entry<E> entry) {
        if (entry == null) {
            throw new NoSuchElementException();
        }
        return entry.getElement();
    }
    
    private static <E> E getElementOrNull(@Nullable final Multiset.Entry<E> entry) {
        return (entry == null) ? null : entry.getElement();
    }
    
    static class ElementSet<E> extends Multisets.ElementSet<E> implements SortedSet<E>
    {
        private final SortedMultiset<E> multiset;
        
        ElementSet(final SortedMultiset<E> multiset) {
            this.multiset = multiset;
        }
        
        @Override
        final SortedMultiset<E> multiset() {
            return this.multiset;
        }
        
        @Override
        public Comparator<? super E> comparator() {
            return this.multiset().comparator();
        }
        
        @Override
        public SortedSet<E> subSet(final E fromElement, final E toElement) {
            return this.multiset().subMultiset(fromElement, BoundType.CLOSED, toElement, BoundType.OPEN).elementSet();
        }
        
        @Override
        public SortedSet<E> headSet(final E toElement) {
            return this.multiset().headMultiset(toElement, BoundType.OPEN).elementSet();
        }
        
        @Override
        public SortedSet<E> tailSet(final E fromElement) {
            return this.multiset().tailMultiset(fromElement, BoundType.CLOSED).elementSet();
        }
        
        @Override
        public E first() {
            return (E)getElementOrThrow((Multiset.Entry<Object>)this.multiset().firstEntry());
        }
        
        @Override
        public E last() {
            return (E)getElementOrThrow((Multiset.Entry<Object>)this.multiset().lastEntry());
        }
    }
    
    @GwtIncompatible("Navigable")
    static class NavigableElementSet<E> extends ElementSet<E> implements NavigableSet<E>
    {
        NavigableElementSet(final SortedMultiset<E> multiset) {
            super(multiset);
        }
        
        @Override
        public E lower(final E e) {
            return (E)getElementOrNull((Multiset.Entry<Object>)this.multiset().headMultiset(e, BoundType.OPEN).lastEntry());
        }
        
        @Override
        public E floor(final E e) {
            return (E)getElementOrNull((Multiset.Entry<Object>)this.multiset().headMultiset(e, BoundType.CLOSED).lastEntry());
        }
        
        @Override
        public E ceiling(final E e) {
            return (E)getElementOrNull((Multiset.Entry<Object>)this.multiset().tailMultiset(e, BoundType.CLOSED).firstEntry());
        }
        
        @Override
        public E higher(final E e) {
            return (E)getElementOrNull((Multiset.Entry<Object>)this.multiset().tailMultiset(e, BoundType.OPEN).firstEntry());
        }
        
        @Override
        public NavigableSet<E> descendingSet() {
            return new NavigableElementSet((SortedMultiset<Object>)this.multiset().descendingMultiset());
        }
        
        @Override
        public Iterator<E> descendingIterator() {
            return this.descendingSet().iterator();
        }
        
        @Override
        public E pollFirst() {
            return (E)getElementOrNull((Multiset.Entry<Object>)this.multiset().pollFirstEntry());
        }
        
        @Override
        public E pollLast() {
            return (E)getElementOrNull((Multiset.Entry<Object>)this.multiset().pollLastEntry());
        }
        
        @Override
        public NavigableSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
            return new NavigableElementSet((SortedMultiset<Object>)this.multiset().subMultiset(fromElement, BoundType.forBoolean(fromInclusive), toElement, BoundType.forBoolean(toInclusive)));
        }
        
        @Override
        public NavigableSet<E> headSet(final E toElement, final boolean inclusive) {
            return new NavigableElementSet((SortedMultiset<Object>)this.multiset().headMultiset(toElement, BoundType.forBoolean(inclusive)));
        }
        
        @Override
        public NavigableSet<E> tailSet(final E fromElement, final boolean inclusive) {
            return new NavigableElementSet((SortedMultiset<Object>)this.multiset().tailMultiset(fromElement, BoundType.forBoolean(inclusive)));
        }
    }
}
