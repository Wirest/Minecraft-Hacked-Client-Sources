// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Collection;
import java.util.ArrayDeque;
import com.google.common.base.Preconditions;
import com.google.common.annotations.VisibleForTesting;
import java.util.Queue;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;
import java.io.Serializable;

@Beta
@GwtIncompatible("java.util.ArrayDeque")
public final class EvictingQueue<E> extends ForwardingQueue<E> implements Serializable
{
    private final Queue<E> delegate;
    @VisibleForTesting
    final int maxSize;
    private static final long serialVersionUID = 0L;
    
    private EvictingQueue(final int maxSize) {
        Preconditions.checkArgument(maxSize >= 0, "maxSize (%s) must >= 0", maxSize);
        this.delegate = new ArrayDeque<E>(maxSize);
        this.maxSize = maxSize;
    }
    
    public static <E> EvictingQueue<E> create(final int maxSize) {
        return new EvictingQueue<E>(maxSize);
    }
    
    public int remainingCapacity() {
        return this.maxSize - this.size();
    }
    
    @Override
    protected Queue<E> delegate() {
        return this.delegate;
    }
    
    @Override
    public boolean offer(final E e) {
        return this.add(e);
    }
    
    @Override
    public boolean add(final E e) {
        Preconditions.checkNotNull(e);
        if (this.maxSize == 0) {
            return true;
        }
        if (this.size() == this.maxSize) {
            this.delegate.remove();
        }
        this.delegate.add(e);
        return true;
    }
    
    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        return this.standardAddAll(collection);
    }
    
    @Override
    public boolean contains(final Object object) {
        return this.delegate().contains(Preconditions.checkNotNull(object));
    }
    
    @Override
    public boolean remove(final Object object) {
        return this.delegate().remove(Preconditions.checkNotNull(object));
    }
}
