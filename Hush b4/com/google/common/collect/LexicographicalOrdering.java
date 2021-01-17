// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import javax.annotation.Nullable;
import java.util.Iterator;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(serializable = true)
final class LexicographicalOrdering<T> extends Ordering<Iterable<T>> implements Serializable
{
    final Ordering<? super T> elementOrder;
    private static final long serialVersionUID = 0L;
    
    LexicographicalOrdering(final Ordering<? super T> elementOrder) {
        this.elementOrder = elementOrder;
    }
    
    @Override
    public int compare(final Iterable<T> leftIterable, final Iterable<T> rightIterable) {
        final Iterator<T> left = leftIterable.iterator();
        final Iterator<T> right = rightIterable.iterator();
        while (left.hasNext()) {
            if (!right.hasNext()) {
                return 1;
            }
            final int result = this.elementOrder.compare((Object)left.next(), (Object)right.next());
            if (result != 0) {
                return result;
            }
        }
        if (right.hasNext()) {
            return -1;
        }
        return 0;
    }
    
    @Override
    public boolean equals(@Nullable final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof LexicographicalOrdering) {
            final LexicographicalOrdering<?> that = (LexicographicalOrdering<?>)object;
            return this.elementOrder.equals(that.elementOrder);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.elementOrder.hashCode() ^ 0x7BB78CF5;
    }
    
    @Override
    public String toString() {
        return this.elementOrder + ".lexicographical()";
    }
}
