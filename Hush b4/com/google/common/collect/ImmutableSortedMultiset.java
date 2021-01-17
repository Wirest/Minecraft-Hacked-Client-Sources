// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.NavigableSet;
import java.util.Set;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import java.util.Arrays;
import java.util.Comparator;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtIncompatible("hasn't been tested yet")
public abstract class ImmutableSortedMultiset<E> extends ImmutableSortedMultisetFauxverideShim<E> implements SortedMultiset<E>
{
    private static final Comparator<Comparable> NATURAL_ORDER;
    private static final ImmutableSortedMultiset<Comparable> NATURAL_EMPTY_MULTISET;
    transient ImmutableSortedMultiset<E> descendingMultiset;
    
    public static <E> ImmutableSortedMultiset<E> of() {
        return (ImmutableSortedMultiset<E>)ImmutableSortedMultiset.NATURAL_EMPTY_MULTISET;
    }
    
    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(final E element) {
        final RegularImmutableSortedSet<E> elementSet = (RegularImmutableSortedSet<E>)(RegularImmutableSortedSet)ImmutableSortedSet.of(element);
        final int[] counts = { 1 };
        final long[] cumulativeCounts = { 0L, 1L };
        return new RegularImmutableSortedMultiset<E>(elementSet, counts, cumulativeCounts, 0, 1);
    }
    
    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(final E e1, final E e2) {
        return copyOf((Comparator<? super E>)Ordering.natural(), (Iterable<? extends E>)Arrays.asList(e1, e2));
    }
    
    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(final E e1, final E e2, final E e3) {
        return copyOf((Comparator<? super E>)Ordering.natural(), (Iterable<? extends E>)Arrays.asList(e1, e2, e3));
    }
    
    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(final E e1, final E e2, final E e3, final E e4) {
        return copyOf((Comparator<? super E>)Ordering.natural(), (Iterable<? extends E>)Arrays.asList(e1, e2, e3, e4));
    }
    
    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(final E e1, final E e2, final E e3, final E e4, final E e5) {
        return copyOf((Comparator<? super E>)Ordering.natural(), (Iterable<? extends E>)Arrays.asList(e1, e2, e3, e4, e5));
    }
    
    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E... remaining) {
        final int size = remaining.length + 6;
        final List<E> all = (List<E>)Lists.newArrayListWithCapacity(size);
        Collections.addAll((Collection<? super Comparable>)all, (Comparable[])new Comparable[] { e1, e2, e3, e4, e5, e6 });
        Collections.addAll(all, remaining);
        return copyOf((Comparator<? super E>)Ordering.natural(), (Iterable<? extends E>)all);
    }
    
    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> copyOf(final E[] elements) {
        return copyOf((Comparator<? super E>)Ordering.natural(), (Iterable<? extends E>)Arrays.asList(elements));
    }
    
    public static <E> ImmutableSortedMultiset<E> copyOf(final Iterable<? extends E> elements) {
        final Ordering<E> naturalOrder = Ordering.natural();
        return copyOf((Comparator<? super E>)naturalOrder, elements);
    }
    
    public static <E> ImmutableSortedMultiset<E> copyOf(final Iterator<? extends E> elements) {
        final Ordering<E> naturalOrder = Ordering.natural();
        return copyOf((Comparator<? super E>)naturalOrder, elements);
    }
    
    public static <E> ImmutableSortedMultiset<E> copyOf(final Comparator<? super E> comparator, final Iterator<? extends E> elements) {
        Preconditions.checkNotNull(comparator);
        return new Builder<E>(comparator).addAll(elements).build();
    }
    
    public static <E> ImmutableSortedMultiset<E> copyOf(final Comparator<? super E> comparator, Iterable<? extends E> elements) {
        if (elements instanceof ImmutableSortedMultiset) {
            final ImmutableSortedMultiset<E> multiset = (ImmutableSortedMultiset<E>)(ImmutableSortedMultiset)elements;
            if (comparator.equals(multiset.comparator())) {
                if (multiset.isPartialView()) {
                    return copyOfSortedEntries(comparator, (Collection<Multiset.Entry<E>>)multiset.entrySet().asList());
                }
                return multiset;
            }
        }
        elements = (Iterable<? extends E>)Lists.newArrayList((Iterable<?>)elements);
        final TreeMultiset<E> sortedCopy = TreeMultiset.create((Comparator<? super E>)Preconditions.checkNotNull((Comparator<? super E>)comparator));
        Iterables.addAll(sortedCopy, elements);
        return copyOfSortedEntries(comparator, (Collection<Multiset.Entry<E>>)sortedCopy.entrySet());
    }
    
    public static <E> ImmutableSortedMultiset<E> copyOfSorted(final SortedMultiset<E> sortedMultiset) {
        return copyOfSortedEntries(sortedMultiset.comparator(), (Collection<Multiset.Entry<E>>)Lists.newArrayList((Iterable<?>)sortedMultiset.entrySet()));
    }
    
    private static <E> ImmutableSortedMultiset<E> copyOfSortedEntries(final Comparator<? super E> comparator, final Collection<Multiset.Entry<E>> entries) {
        if (entries.isEmpty()) {
            return emptyMultiset(comparator);
        }
        final ImmutableList.Builder<E> elementsBuilder = new ImmutableList.Builder<E>(entries.size());
        final int[] counts = new int[entries.size()];
        final long[] cumulativeCounts = new long[entries.size() + 1];
        int i = 0;
        for (final Multiset.Entry<E> entry : entries) {
            elementsBuilder.add(entry.getElement());
            counts[i] = entry.getCount();
            cumulativeCounts[i + 1] = cumulativeCounts[i] + counts[i];
            ++i;
        }
        return new RegularImmutableSortedMultiset<E>(new RegularImmutableSortedSet<E>(elementsBuilder.build(), comparator), counts, cumulativeCounts, 0, entries.size());
    }
    
    static <E> ImmutableSortedMultiset<E> emptyMultiset(final Comparator<? super E> comparator) {
        if (ImmutableSortedMultiset.NATURAL_ORDER.equals(comparator)) {
            return (ImmutableSortedMultiset<E>)ImmutableSortedMultiset.NATURAL_EMPTY_MULTISET;
        }
        return new EmptyImmutableSortedMultiset<E>(comparator);
    }
    
    ImmutableSortedMultiset() {
    }
    
    @Override
    public final Comparator<? super E> comparator() {
        return this.elementSet().comparator();
    }
    
    @Override
    public abstract ImmutableSortedSet<E> elementSet();
    
    @Override
    public ImmutableSortedMultiset<E> descendingMultiset() {
        final ImmutableSortedMultiset<E> result = this.descendingMultiset;
        if (result == null) {
            return this.descendingMultiset = new DescendingImmutableSortedMultiset<E>(this);
        }
        return result;
    }
    
    @Deprecated
    @Override
    public final Multiset.Entry<E> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public final Multiset.Entry<E> pollLastEntry() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public abstract ImmutableSortedMultiset<E> headMultiset(final E p0, final BoundType p1);
    
    @Override
    public ImmutableSortedMultiset<E> subMultiset(final E lowerBound, final BoundType lowerBoundType, final E upperBound, final BoundType upperBoundType) {
        Preconditions.checkArgument(this.comparator().compare((Object)lowerBound, (Object)upperBound) <= 0, "Expected lowerBound <= upperBound but %s > %s", lowerBound, upperBound);
        return this.tailMultiset(lowerBound, lowerBoundType).headMultiset(upperBound, upperBoundType);
    }
    
    @Override
    public abstract ImmutableSortedMultiset<E> tailMultiset(final E p0, final BoundType p1);
    
    public static <E> Builder<E> orderedBy(final Comparator<E> comparator) {
        return new Builder<E>(comparator);
    }
    
    public static <E extends Comparable<E>> Builder<E> reverseOrder() {
        return new Builder<E>(Ordering.natural().reverse());
    }
    
    public static <E extends Comparable<E>> Builder<E> naturalOrder() {
        return new Builder<E>(Ordering.natural());
    }
    
    @Override
    Object writeReplace() {
        return new SerializedForm((SortedMultiset<Object>)this);
    }
    
    static {
        NATURAL_ORDER = Ordering.natural();
        NATURAL_EMPTY_MULTISET = new EmptyImmutableSortedMultiset<Comparable>(ImmutableSortedMultiset.NATURAL_ORDER);
    }
    
    public static class Builder<E> extends ImmutableMultiset.Builder<E>
    {
        public Builder(final Comparator<? super E> comparator) {
            super(TreeMultiset.create(Preconditions.checkNotNull(comparator)));
        }
        
        @Override
        public Builder<E> add(final E element) {
            super.add(element);
            return this;
        }
        
        @Override
        public Builder<E> addCopies(final E element, final int occurrences) {
            super.addCopies(element, occurrences);
            return this;
        }
        
        @Override
        public Builder<E> setCount(final E element, final int count) {
            super.setCount(element, count);
            return this;
        }
        
        @Override
        public Builder<E> add(final E... elements) {
            super.add(elements);
            return this;
        }
        
        @Override
        public Builder<E> addAll(final Iterable<? extends E> elements) {
            super.addAll(elements);
            return this;
        }
        
        @Override
        public Builder<E> addAll(final Iterator<? extends E> elements) {
            super.addAll(elements);
            return this;
        }
        
        @Override
        public ImmutableSortedMultiset<E> build() {
            return ImmutableSortedMultiset.copyOfSorted((SortedMultiset<E>)(SortedMultiset)this.contents);
        }
    }
    
    private static final class SerializedForm<E> implements Serializable
    {
        Comparator<? super E> comparator;
        E[] elements;
        int[] counts;
        
        SerializedForm(final SortedMultiset<E> multiset) {
            this.comparator = multiset.comparator();
            final int n = multiset.entrySet().size();
            this.elements = (E[])new Object[n];
            this.counts = new int[n];
            int i = 0;
            for (final Multiset.Entry<E> entry : multiset.entrySet()) {
                this.elements[i] = entry.getElement();
                this.counts[i] = entry.getCount();
                ++i;
            }
        }
        
        Object readResolve() {
            final int n = this.elements.length;
            final Builder<E> builder = new Builder<E>(this.comparator);
            for (int i = 0; i < n; ++i) {
                builder.addCopies(this.elements[i], this.counts[i]);
            }
            return builder.build();
        }
    }
}
