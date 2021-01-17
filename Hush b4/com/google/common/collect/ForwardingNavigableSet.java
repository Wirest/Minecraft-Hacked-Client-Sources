// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import com.google.common.annotations.Beta;
import java.util.Iterator;
import java.util.NavigableSet;

public abstract class ForwardingNavigableSet<E> extends ForwardingSortedSet<E> implements NavigableSet<E>
{
    protected ForwardingNavigableSet() {
    }
    
    @Override
    protected abstract NavigableSet<E> delegate();
    
    @Override
    public E lower(final E e) {
        return this.delegate().lower(e);
    }
    
    protected E standardLower(final E e) {
        return Iterators.getNext((Iterator<? extends E>)this.headSet(e, false).descendingIterator(), (E)null);
    }
    
    @Override
    public E floor(final E e) {
        return this.delegate().floor(e);
    }
    
    protected E standardFloor(final E e) {
        return Iterators.getNext((Iterator<? extends E>)this.headSet(e, true).descendingIterator(), (E)null);
    }
    
    @Override
    public E ceiling(final E e) {
        return this.delegate().ceiling(e);
    }
    
    protected E standardCeiling(final E e) {
        return Iterators.getNext((Iterator<? extends E>)this.tailSet(e, true).iterator(), (E)null);
    }
    
    @Override
    public E higher(final E e) {
        return this.delegate().higher(e);
    }
    
    protected E standardHigher(final E e) {
        return Iterators.getNext((Iterator<? extends E>)this.tailSet(e, false).iterator(), (E)null);
    }
    
    @Override
    public E pollFirst() {
        return this.delegate().pollFirst();
    }
    
    protected E standardPollFirst() {
        return Iterators.pollNext(this.iterator());
    }
    
    @Override
    public E pollLast() {
        return this.delegate().pollLast();
    }
    
    protected E standardPollLast() {
        return Iterators.pollNext(this.descendingIterator());
    }
    
    protected E standardFirst() {
        return this.iterator().next();
    }
    
    protected E standardLast() {
        return this.descendingIterator().next();
    }
    
    @Override
    public NavigableSet<E> descendingSet() {
        return this.delegate().descendingSet();
    }
    
    @Override
    public Iterator<E> descendingIterator() {
        return this.delegate().descendingIterator();
    }
    
    @Override
    public NavigableSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
        return this.delegate().subSet(fromElement, fromInclusive, toElement, toInclusive);
    }
    
    @Beta
    protected NavigableSet<E> standardSubSet(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
        return this.tailSet(fromElement, fromInclusive).headSet(toElement, toInclusive);
    }
    
    @Override
    protected SortedSet<E> standardSubSet(final E fromElement, final E toElement) {
        return this.subSet(fromElement, true, toElement, false);
    }
    
    @Override
    public NavigableSet<E> headSet(final E toElement, final boolean inclusive) {
        return this.delegate().headSet(toElement, inclusive);
    }
    
    protected SortedSet<E> standardHeadSet(final E toElement) {
        return this.headSet(toElement, false);
    }
    
    @Override
    public NavigableSet<E> tailSet(final E fromElement, final boolean inclusive) {
        return this.delegate().tailSet(fromElement, inclusive);
    }
    
    protected SortedSet<E> standardTailSet(final E fromElement) {
        return this.tailSet(fromElement, true);
    }
    
    @Beta
    protected class StandardDescendingSet extends Sets.DescendingSet<E>
    {
        public StandardDescendingSet() {
            super(ForwardingNavigableSet.this);
        }
    }
}
