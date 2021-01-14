package com.google.common.base;

import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

@GwtCompatible
final class Absent<T>
        extends Optional<T> {
    static final Absent<Object> INSTANCE = new Absent();
    private static final long serialVersionUID = 0L;

    static <T> Optional<T> withType() {
        return INSTANCE;
    }

    public boolean isPresent() {
        return false;
    }

    public T get() {
        throw new IllegalStateException("Optional.get() cannot be called on an absent value");
    }

    public T or(T paramT) {
        return (T) Preconditions.checkNotNull(paramT, "use Optional.orNull() instead of Optional.or(null)");
    }

    public Optional<T> or(Optional<? extends T> paramOptional) {
        return (Optional) Preconditions.checkNotNull(paramOptional);
    }

    public T or(Supplier<? extends T> paramSupplier) {
        return (T) Preconditions.checkNotNull(paramSupplier.get(), "use Optional.orNull() instead of a Supplier that returns null");
    }

    @Nullable
    public T orNull() {
        return null;
    }

    public Set<T> asSet() {
        return Collections.emptySet();
    }

    public <V> Optional<V> transform(Function<? super T, V> paramFunction) {
        Preconditions.checkNotNull(paramFunction);
        return Optional.absent();
    }

    public boolean equals(@Nullable Object paramObject) {
        return paramObject == this;
    }

    public int hashCode() {
        return 1502476572;
    }

    public String toString() {
        return "Optional.absent()";
    }

    private Object readResolve() {
        return INSTANCE;
    }
}




