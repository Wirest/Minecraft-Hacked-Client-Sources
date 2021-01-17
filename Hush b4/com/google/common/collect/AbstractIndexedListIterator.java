// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.NoSuchElementException;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract class AbstractIndexedListIterator<E> extends UnmodifiableListIterator<E>
{
    private final int size;
    private int position;
    
    protected abstract E get(final int p0);
    
    protected AbstractIndexedListIterator(final int size) {
        this(size, 0);
    }
    
    protected AbstractIndexedListIterator(final int size, final int position) {
        Preconditions.checkPositionIndex(position, size);
        this.size = size;
        this.position = position;
    }
    
    @Override
    public final boolean hasNext() {
        return this.position < this.size;
    }
    
    @Override
    public final E next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        return this.get(this.position++);
    }
    
    @Override
    public final int nextIndex() {
        return this.position;
    }
    
    @Override
    public final boolean hasPrevious() {
        return this.position > 0;
    }
    
    @Override
    public final E previous() {
        if (!this.hasPrevious()) {
            throw new NoSuchElementException();
        }
        final int position = this.position - 1;
        this.position = position;
        return this.get(position);
    }
    
    @Override
    public final int previousIndex() {
        return this.position - 1;
    }
}
