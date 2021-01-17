// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(serializable = true)
final class NullsFirstOrdering<T> extends Ordering<T> implements Serializable
{
    final Ordering<? super T> ordering;
    private static final long serialVersionUID = 0L;
    
    NullsFirstOrdering(final Ordering<? super T> ordering) {
        this.ordering = ordering;
    }
    
    @Override
    public int compare(@Nullable final T left, @Nullable final T right) {
        if (left == right) {
            return 0;
        }
        if (left == null) {
            return -1;
        }
        if (right == null) {
            return 1;
        }
        return this.ordering.compare((Object)left, (Object)right);
    }
    
    @Override
    public <S extends T> Ordering<S> reverse() {
        return this.ordering.reverse().nullsLast();
    }
    
    @Override
    public <S extends T> Ordering<S> nullsFirst() {
        return (Ordering<S>)this;
    }
    
    @Override
    public <S extends T> Ordering<S> nullsLast() {
        return this.ordering.nullsLast();
    }
    
    @Override
    public boolean equals(@Nullable final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof NullsFirstOrdering) {
            final NullsFirstOrdering<?> that = (NullsFirstOrdering<?>)object;
            return this.ordering.equals(that.ordering);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.ordering.hashCode() ^ 0x39153A74;
    }
    
    @Override
    public String toString() {
        return this.ordering + ".nullsFirst()";
    }
}
