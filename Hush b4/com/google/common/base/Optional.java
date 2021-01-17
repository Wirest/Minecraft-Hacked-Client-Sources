// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import java.util.Iterator;
import java.util.Set;
import com.google.common.annotations.Beta;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(serializable = true)
public abstract class Optional<T> implements Serializable
{
    private static final long serialVersionUID = 0L;
    
    public static <T> Optional<T> absent() {
        return Absent.withType();
    }
    
    public static <T> Optional<T> of(final T reference) {
        return new Present<T>(Preconditions.checkNotNull(reference));
    }
    
    public static <T> Optional<T> fromNullable(@Nullable final T nullableReference) {
        return (nullableReference == null) ? absent() : new Present<T>(nullableReference);
    }
    
    Optional() {
    }
    
    public abstract boolean isPresent();
    
    public abstract T get();
    
    public abstract T or(final T p0);
    
    public abstract Optional<T> or(final Optional<? extends T> p0);
    
    @Beta
    public abstract T or(final Supplier<? extends T> p0);
    
    @Nullable
    public abstract T orNull();
    
    public abstract Set<T> asSet();
    
    public abstract <V> Optional<V> transform(final Function<? super T, V> p0);
    
    @Override
    public abstract boolean equals(@Nullable final Object p0);
    
    @Override
    public abstract int hashCode();
    
    @Override
    public abstract String toString();
    
    @Beta
    public static <T> Iterable<T> presentInstances(final Iterable<? extends Optional<? extends T>> optionals) {
        Preconditions.checkNotNull(optionals);
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new AbstractIterator<T>() {
                    private final Iterator<? extends Optional<? extends T>> iterator = Preconditions.checkNotNull(optionals.iterator());
                    
                    @Override
                    protected T computeNext() {
                        while (this.iterator.hasNext()) {
                            final Optional<? extends T> optional = (Optional<? extends T>)this.iterator.next();
                            if (optional.isPresent()) {
                                return (T)optional.get();
                            }
                        }
                        return this.endOfData();
                    }
                };
            }
        };
    }
}
