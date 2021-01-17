// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Queue;
import java.util.Deque;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.BlockingDeque;

public abstract class ForwardingBlockingDeque<E> extends ForwardingDeque<E> implements BlockingDeque<E>
{
    protected ForwardingBlockingDeque() {
    }
    
    @Override
    protected abstract BlockingDeque<E> delegate();
    
    @Override
    public int remainingCapacity() {
        return this.delegate().remainingCapacity();
    }
    
    @Override
    public void putFirst(final E e) throws InterruptedException {
        this.delegate().putFirst(e);
    }
    
    @Override
    public void putLast(final E e) throws InterruptedException {
        this.delegate().putLast(e);
    }
    
    @Override
    public boolean offerFirst(final E e, final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.delegate().offerFirst(e, timeout, unit);
    }
    
    @Override
    public boolean offerLast(final E e, final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.delegate().offerLast(e, timeout, unit);
    }
    
    @Override
    public E takeFirst() throws InterruptedException {
        return this.delegate().takeFirst();
    }
    
    @Override
    public E takeLast() throws InterruptedException {
        return this.delegate().takeLast();
    }
    
    @Override
    public E pollFirst(final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.delegate().pollFirst(timeout, unit);
    }
    
    @Override
    public E pollLast(final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.delegate().pollLast(timeout, unit);
    }
    
    @Override
    public void put(final E e) throws InterruptedException {
        this.delegate().put(e);
    }
    
    @Override
    public boolean offer(final E e, final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.delegate().offer(e, timeout, unit);
    }
    
    @Override
    public E take() throws InterruptedException {
        return this.delegate().take();
    }
    
    @Override
    public E poll(final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.delegate().poll(timeout, unit);
    }
    
    @Override
    public int drainTo(final Collection<? super E> c) {
        return this.delegate().drainTo(c);
    }
    
    @Override
    public int drainTo(final Collection<? super E> c, final int maxElements) {
        return this.delegate().drainTo(c, maxElements);
    }
}
