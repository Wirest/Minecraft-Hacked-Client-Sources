package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Comparator;

@GwtCompatible(serializable = true)
final class ComparatorOrdering<T>
        extends Ordering<T>
        implements Serializable {
    private static final long serialVersionUID = 0L;
    final Comparator<T> comparator;

    ComparatorOrdering(Comparator<T> paramComparator) {
        this.comparator = ((Comparator) Preconditions.checkNotNull(paramComparator));
    }

    public int compare(T paramT1, T paramT2) {
        return this.comparator.compare(paramT1, paramT2);
    }

    public boolean equals(@Nullable Object paramObject) {
        if (paramObject == this) {
            return true;
        }
        if ((paramObject instanceof ComparatorOrdering)) {
            ComparatorOrdering localComparatorOrdering = (ComparatorOrdering) paramObject;
            return this.comparator.equals(localComparatorOrdering.comparator);
        }
        return false;
    }

    public int hashCode() {
        return this.comparator.hashCode();
    }

    public String toString() {
        return this.comparator.toString();
    }
}




