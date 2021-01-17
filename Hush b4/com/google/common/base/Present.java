// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
final class Present<T> extends Optional<T>
{
    private final T reference;
    private static final long serialVersionUID = 0L;
    
    Present(final T reference) {
        this.reference = reference;
    }
    
    @Override
    public boolean isPresent() {
        return true;
    }
    
    @Override
    public T get() {
        return this.reference;
    }
    
    @Override
    public T or(final T defaultValue) {
        Preconditions.checkNotNull(defaultValue, (Object)"use Optional.orNull() instead of Optional.or(null)");
        return this.reference;
    }
    
    @Override
    public Optional<T> or(final Optional<? extends T> secondChoice) {
        Preconditions.checkNotNull(secondChoice);
        return this;
    }
    
    @Override
    public T or(final Supplier<? extends T> supplier) {
        Preconditions.checkNotNull(supplier);
        return this.reference;
    }
    
    @Override
    public T orNull() {
        return this.reference;
    }
    
    @Override
    public Set<T> asSet() {
        return Collections.singleton(this.reference);
    }
    
    @Override
    public <V> Optional<V> transform(final Function<? super T, V> function) {
        return new Present<V>(Preconditions.checkNotNull(function.apply((Object)this.reference), (Object)"the Function passed to Optional.transform() must not return null."));
    }
    
    @Override
    public boolean equals(@Nullable final Object object) {
        if (object instanceof Present) {
            final Present<?> other = (Present<?>)object;
            return this.reference.equals(other.reference);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return 1502476572 + this.reference.hashCode();
    }
    
    @Override
    public String toString() {
        return "Optional.of(" + this.reference + ")";
    }
}
