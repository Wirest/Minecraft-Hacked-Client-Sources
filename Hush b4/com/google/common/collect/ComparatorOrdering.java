// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import javax.annotation.Nullable;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(serializable = true)
final class ComparatorOrdering<T> extends Ordering<T> implements Serializable
{
    final Comparator<T> comparator;
    private static final long serialVersionUID = 0L;
    
    ComparatorOrdering(final Comparator<T> comparator) {
        this.comparator = Preconditions.checkNotNull(comparator);
    }
    
    @Override
    public int compare(final T a, final T b) {
        return this.comparator.compare(a, b);
    }
    
    @Override
    public boolean equals(@Nullable final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof ComparatorOrdering) {
            final ComparatorOrdering<?> that = (ComparatorOrdering<?>)object;
            return this.comparator.equals(that.comparator);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.comparator.hashCode();
    }
    
    @Override
    public String toString() {
        return this.comparator.toString();
    }
}
