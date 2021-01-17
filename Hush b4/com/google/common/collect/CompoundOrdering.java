// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(serializable = true)
final class CompoundOrdering<T> extends Ordering<T> implements Serializable
{
    final ImmutableList<Comparator<? super T>> comparators;
    private static final long serialVersionUID = 0L;
    
    CompoundOrdering(final Comparator<? super T> primary, final Comparator<? super T> secondary) {
        this.comparators = ImmutableList.of(primary, secondary);
    }
    
    CompoundOrdering(final Iterable<? extends Comparator<? super T>> comparators) {
        this.comparators = ImmutableList.copyOf(comparators);
    }
    
    @Override
    public int compare(final T left, final T right) {
        for (int size = this.comparators.size(), i = 0; i < size; ++i) {
            final int result = this.comparators.get(i).compare((Object)left, (Object)right);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }
    
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof CompoundOrdering) {
            final CompoundOrdering<?> that = (CompoundOrdering<?>)object;
            return this.comparators.equals(that.comparators);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.comparators.hashCode();
    }
    
    @Override
    public String toString() {
        return "Ordering.compound(" + this.comparators + ")";
    }
}
