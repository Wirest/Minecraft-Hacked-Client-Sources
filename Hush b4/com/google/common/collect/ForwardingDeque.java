// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Collection;
import java.util.Queue;
import java.util.Iterator;
import java.util.Deque;

public abstract class ForwardingDeque<E> extends ForwardingQueue<E> implements Deque<E>
{
    protected ForwardingDeque() {
    }
    
    @Override
    protected abstract Deque<E> delegate();
    
    @Override
    public void addFirst(final E e) {
        this.delegate().addFirst(e);
    }
    
    @Override
    public void addLast(final E e) {
        this.delegate().addLast(e);
    }
    
    @Override
    public Iterator<E> descendingIterator() {
        return this.delegate().descendingIterator();
    }
    
    @Override
    public E getFirst() {
        return this.delegate().getFirst();
    }
    
    @Override
    public E getLast() {
        return this.delegate().getLast();
    }
    
    @Override
    public boolean offerFirst(final E e) {
        return this.delegate().offerFirst(e);
    }
    
    @Override
    public boolean offerLast(final E e) {
        return this.delegate().offerLast(e);
    }
    
    @Override
    public E peekFirst() {
        return this.delegate().peekFirst();
    }
    
    @Override
    public E peekLast() {
        return this.delegate().peekLast();
    }
    
    @Override
    public E pollFirst() {
        return this.delegate().pollFirst();
    }
    
    @Override
    public E pollLast() {
        return this.delegate().pollLast();
    }
    
    @Override
    public E pop() {
        return this.delegate().pop();
    }
    
    @Override
    public void push(final E e) {
        this.delegate().push(e);
    }
    
    @Override
    public E removeFirst() {
        return this.delegate().removeFirst();
    }
    
    @Override
    public E removeLast() {
        return this.delegate().removeLast();
    }
    
    @Override
    public boolean removeFirstOccurrence(final Object o) {
        return this.delegate().removeFirstOccurrence(o);
    }
    
    @Override
    public boolean removeLastOccurrence(final Object o) {
        return this.delegate().removeLastOccurrence(o);
    }
}
