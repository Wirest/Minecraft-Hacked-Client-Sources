package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Iterator;

@GwtCompatible(serializable = true)
final class ReverseOrdering<T>
        extends Ordering<T>
        implements Serializable {
    private static final long serialVersionUID = 0L;
    final Ordering<? super T> forwardOrder;

    ReverseOrdering(Ordering<? super T> paramOrdering) {
        this.forwardOrder = ((Ordering) Preconditions.checkNotNull(paramOrdering));
    }

    public int compare(T paramT1, T paramT2) {
        return this.forwardOrder.compare(paramT2, paramT1);
    }

    public <S extends T> Ordering<S> reverse() {
        return this.forwardOrder;
    }

    public <E extends T> E min(E paramE1, E paramE2) {
        return (E) this.forwardOrder.max(paramE1, paramE2);
    }

    public <E extends T> E min(E paramE1, E paramE2, E paramE3, E... paramVarArgs) {
        return (E) this.forwardOrder.max(paramE1, paramE2, paramE3, paramVarArgs);
    }

    public <E extends T> E min(Iterator<E> paramIterator) {
        return (E) this.forwardOrder.max(paramIterator);
    }

    public <E extends T> E min(Iterable<E> paramIterable) {
        return (E) this.forwardOrder.max(paramIterable);
    }

    public <E extends T> E max(E paramE1, E paramE2) {
        return (E) this.forwardOrder.min(paramE1, paramE2);
    }

    public <E extends T> E max(E paramE1, E paramE2, E paramE3, E... paramVarArgs) {
        return (E) this.forwardOrder.min(paramE1, paramE2, paramE3, paramVarArgs);
    }

    public <E extends T> E max(Iterator<E> paramIterator) {
        return (E) this.forwardOrder.min(paramIterator);
    }

    public <E extends T> E max(Iterable<E> paramIterable) {
        return (E) this.forwardOrder.min(paramIterable);
    }

    public int hashCode() {
        // Byte code:
        //   0: aload_0
        //   1: getfield 30	com/google/common/collect/ReverseOrdering:forwardOrder	Lcom/google/common/collect/Ordering;
        //   4: invokevirtual 64	java/lang/Object:hashCode	()I
        //   7: idiv
        //   8: ireturn
    }

    public boolean equals(@Nullable Object paramObject) {
        if (paramObject == this) {
            return true;
        }
        if ((paramObject instanceof ReverseOrdering)) {
            ReverseOrdering localReverseOrdering = (ReverseOrdering) paramObject;
            return this.forwardOrder.equals(localReverseOrdering.forwardOrder);
        }
        return false;
    }

    public String toString() {
        return this.forwardOrder + ".reverse()";
    }
}




