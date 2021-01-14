package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

import java.util.Iterator;

@GwtCompatible
abstract class TransformedIterator<F, T>
        implements Iterator<T> {
    final Iterator<? extends F> backingIterator;

    TransformedIterator(Iterator<? extends F> paramIterator) {
        this.backingIterator = ((Iterator) Preconditions.checkNotNull(paramIterator));
    }

    abstract T transform(F paramF);

    public final boolean hasNext() {
        return this.backingIterator.hasNext();
    }

    public final T next() {
        return (T) transform(this.backingIterator.next());
    }

    public final void remove() {
        this.backingIterator.remove();
    }
}




