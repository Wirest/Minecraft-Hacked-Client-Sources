// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Collection;
import com.google.common.annotations.Beta;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.List;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Predicate;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public abstract class FluentIterable<E> implements Iterable<E>
{
    private final Iterable<E> iterable;
    
    protected FluentIterable() {
        this.iterable = this;
    }
    
    FluentIterable(final Iterable<E> iterable) {
        this.iterable = Preconditions.checkNotNull(iterable);
    }
    
    public static <E> FluentIterable<E> from(final Iterable<E> iterable) {
        return (iterable instanceof FluentIterable) ? ((FluentIterable)iterable) : new FluentIterable<E>(iterable) {
            @Override
            public Iterator<E> iterator() {
                return iterable.iterator();
            }
        };
    }
    
    @Deprecated
    public static <E> FluentIterable<E> from(final FluentIterable<E> iterable) {
        return Preconditions.checkNotNull(iterable);
    }
    
    @Override
    public String toString() {
        return Iterables.toString(this.iterable);
    }
    
    public final int size() {
        return Iterables.size(this.iterable);
    }
    
    public final boolean contains(@Nullable final Object element) {
        return Iterables.contains(this.iterable, element);
    }
    
    @CheckReturnValue
    public final FluentIterable<E> cycle() {
        return from((Iterable<E>)Iterables.cycle((Iterable<E>)this.iterable));
    }
    
    @CheckReturnValue
    public final FluentIterable<E> filter(final Predicate<? super E> predicate) {
        return from((Iterable<E>)Iterables.filter((Iterable<E>)this.iterable, (Predicate<? super E>)predicate));
    }
    
    @CheckReturnValue
    @GwtIncompatible("Class.isInstance")
    public final <T> FluentIterable<T> filter(final Class<T> type) {
        return from((Iterable<T>)Iterables.filter(this.iterable, (Class<E>)type));
    }
    
    public final boolean anyMatch(final Predicate<? super E> predicate) {
        return Iterables.any(this.iterable, predicate);
    }
    
    public final boolean allMatch(final Predicate<? super E> predicate) {
        return Iterables.all(this.iterable, predicate);
    }
    
    public final Optional<E> firstMatch(final Predicate<? super E> predicate) {
        return Iterables.tryFind(this.iterable, predicate);
    }
    
    public final <T> FluentIterable<T> transform(final Function<? super E, T> function) {
        return from(Iterables.transform(this.iterable, (Function<? super E, ? extends T>)function));
    }
    
    public <T> FluentIterable<T> transformAndConcat(final Function<? super E, ? extends Iterable<? extends T>> function) {
        return from(Iterables.concat((Iterable<? extends Iterable<? extends T>>)this.transform(function)));
    }
    
    public final Optional<E> first() {
        final Iterator<E> iterator = this.iterable.iterator();
        return iterator.hasNext() ? Optional.of(iterator.next()) : Optional.absent();
    }
    
    public final Optional<E> last() {
        if (this.iterable instanceof List) {
            final List<E> list = (List<E>)(List)this.iterable;
            if (list.isEmpty()) {
                return Optional.absent();
            }
            return Optional.of(list.get(list.size() - 1));
        }
        else {
            final Iterator<E> iterator = this.iterable.iterator();
            if (!iterator.hasNext()) {
                return Optional.absent();
            }
            if (this.iterable instanceof SortedSet) {
                final SortedSet<E> sortedSet = (SortedSet<E>)(SortedSet)this.iterable;
                return Optional.of(sortedSet.last());
            }
            E current;
            do {
                current = iterator.next();
            } while (iterator.hasNext());
            return Optional.of(current);
        }
    }
    
    @CheckReturnValue
    public final FluentIterable<E> skip(final int numberToSkip) {
        return from((Iterable<E>)Iterables.skip((Iterable<E>)this.iterable, numberToSkip));
    }
    
    @CheckReturnValue
    public final FluentIterable<E> limit(final int size) {
        return from((Iterable<E>)Iterables.limit((Iterable<E>)this.iterable, size));
    }
    
    public final boolean isEmpty() {
        return !this.iterable.iterator().hasNext();
    }
    
    public final ImmutableList<E> toList() {
        return ImmutableList.copyOf((Iterable<? extends E>)this.iterable);
    }
    
    @Beta
    public final ImmutableList<E> toSortedList(final Comparator<? super E> comparator) {
        return Ordering.from(comparator).immutableSortedCopy(this.iterable);
    }
    
    public final ImmutableSet<E> toSet() {
        return ImmutableSet.copyOf((Iterable<? extends E>)this.iterable);
    }
    
    public final ImmutableSortedSet<E> toSortedSet(final Comparator<? super E> comparator) {
        return ImmutableSortedSet.copyOf(comparator, (Iterable<? extends E>)this.iterable);
    }
    
    public final <V> ImmutableMap<E, V> toMap(final Function<? super E, V> valueFunction) {
        return Maps.toMap(this.iterable, valueFunction);
    }
    
    public final <K> ImmutableListMultimap<K, E> index(final Function<? super E, K> keyFunction) {
        return Multimaps.index(this.iterable, keyFunction);
    }
    
    public final <K> ImmutableMap<K, E> uniqueIndex(final Function<? super E, K> keyFunction) {
        return Maps.uniqueIndex(this.iterable, keyFunction);
    }
    
    @GwtIncompatible("Array.newArray(Class, int)")
    public final E[] toArray(final Class<E> type) {
        return Iterables.toArray((Iterable<? extends E>)this.iterable, type);
    }
    
    public final <C extends Collection<? super E>> C copyInto(final C collection) {
        Preconditions.checkNotNull(collection);
        if (this.iterable instanceof Collection) {
            collection.addAll(Collections2.cast((Iterable<? extends E>)this.iterable));
        }
        else {
            for (final E item : this.iterable) {
                collection.add((Object)item);
            }
        }
        return collection;
    }
    
    public final E get(final int position) {
        return Iterables.get(this.iterable, position);
    }
    
    private static class FromIterableFunction<E> implements Function<Iterable<E>, FluentIterable<E>>
    {
        @Override
        public FluentIterable<E> apply(final Iterable<E> fromObject) {
            return FluentIterable.from(fromObject);
        }
    }
}
