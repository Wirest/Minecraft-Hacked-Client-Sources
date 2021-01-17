// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Collection;
import java.util.EnumSet;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
final class ImmutableEnumSet<E extends Enum<E>> extends ImmutableSet<E>
{
    private final transient EnumSet<E> delegate;
    private transient int hashCode;
    
    static <E extends Enum<E>> ImmutableSet<E> asImmutable(final EnumSet<E> set) {
        switch (set.size()) {
            case 0: {
                return ImmutableSet.of();
            }
            case 1: {
                return ImmutableSet.of((E)Iterables.getOnlyElement((Iterable<E>)set));
            }
            default: {
                return new ImmutableEnumSet<E>(set);
            }
        }
    }
    
    private ImmutableEnumSet(final EnumSet<E> delegate) {
        this.delegate = delegate;
    }
    
    @Override
    boolean isPartialView() {
        return false;
    }
    
    @Override
    public UnmodifiableIterator<E> iterator() {
        return Iterators.unmodifiableIterator(this.delegate.iterator());
    }
    
    @Override
    public int size() {
        return this.delegate.size();
    }
    
    @Override
    public boolean contains(final Object object) {
        return this.delegate.contains(object);
    }
    
    @Override
    public boolean containsAll(final Collection<?> collection) {
        return this.delegate.containsAll(collection);
    }
    
    @Override
    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }
    
    @Override
    public boolean equals(final Object object) {
        return object == this || this.delegate.equals(object);
    }
    
    @Override
    public int hashCode() {
        final int result = this.hashCode;
        return (result == 0) ? (this.hashCode = this.delegate.hashCode()) : result;
    }
    
    @Override
    public String toString() {
        return this.delegate.toString();
    }
    
    @Override
    Object writeReplace() {
        return new EnumSerializedForm((EnumSet<Enum>)this.delegate);
    }
    
    private static class EnumSerializedForm<E extends Enum<E>> implements Serializable
    {
        final EnumSet<E> delegate;
        private static final long serialVersionUID = 0L;
        
        EnumSerializedForm(final EnumSet<E> delegate) {
            this.delegate = delegate;
        }
        
        Object readResolve() {
            return new ImmutableEnumSet(this.delegate.clone(), null);
        }
    }
}
