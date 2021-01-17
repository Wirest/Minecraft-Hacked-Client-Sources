// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
final class SingletonImmutableSet<E> extends ImmutableSet<E>
{
    final transient E element;
    private transient int cachedHashCode;
    
    SingletonImmutableSet(final E element) {
        this.element = Preconditions.checkNotNull(element);
    }
    
    SingletonImmutableSet(final E element, final int hashCode) {
        this.element = element;
        this.cachedHashCode = hashCode;
    }
    
    @Override
    public int size() {
        return 1;
    }
    
    @Override
    public boolean isEmpty() {
        return false;
    }
    
    @Override
    public boolean contains(final Object target) {
        return this.element.equals(target);
    }
    
    @Override
    public UnmodifiableIterator<E> iterator() {
        return Iterators.singletonIterator(this.element);
    }
    
    @Override
    boolean isPartialView() {
        return false;
    }
    
    @Override
    int copyIntoArray(final Object[] dst, final int offset) {
        dst[offset] = this.element;
        return offset + 1;
    }
    
    @Override
    public boolean equals(@Nullable final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Set) {
            final Set<?> that = (Set<?>)object;
            return that.size() == 1 && this.element.equals(that.iterator().next());
        }
        return false;
    }
    
    @Override
    public final int hashCode() {
        int code = this.cachedHashCode;
        if (code == 0) {
            code = (this.cachedHashCode = this.element.hashCode());
        }
        return code;
    }
    
    @Override
    boolean isHashCodeFast() {
        return this.cachedHashCode != 0;
    }
    
    @Override
    public String toString() {
        final String elementToString = this.element.toString();
        return new StringBuilder(elementToString.length() + 2).append('[').append(elementToString).append(']').toString();
    }
}
