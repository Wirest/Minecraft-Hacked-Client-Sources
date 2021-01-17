// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal;

import java.util.Iterator;

public final class ReadOnlyIterator<T> implements Iterator<T>
{
    private final Iterator<? extends T> iterator;
    
    public ReadOnlyIterator(final Iterator<? extends T> iterator) {
        if (iterator == null) {
            throw new NullPointerException("iterator");
        }
        this.iterator = iterator;
    }
    
    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }
    
    @Override
    public T next() {
        return (T)this.iterator.next();
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException("read-only");
    }
}
