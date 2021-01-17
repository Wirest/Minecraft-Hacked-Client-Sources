// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.NoSuchElementException;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public abstract class AbstractSequentialIterator<T> extends UnmodifiableIterator<T>
{
    private T nextOrNull;
    
    protected AbstractSequentialIterator(@Nullable final T firstOrNull) {
        this.nextOrNull = firstOrNull;
    }
    
    protected abstract T computeNext(final T p0);
    
    @Override
    public final boolean hasNext() {
        return this.nextOrNull != null;
    }
    
    @Override
    public final T next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        try {
            return this.nextOrNull;
        }
        finally {
            this.nextOrNull = this.computeNext(this.nextOrNull);
        }
    }
}
