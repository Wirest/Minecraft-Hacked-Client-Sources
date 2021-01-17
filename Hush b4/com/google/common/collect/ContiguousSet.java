// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.NavigableSet;
import java.util.SortedSet;
import com.google.common.annotations.GwtIncompatible;
import java.util.Comparator;
import java.util.NoSuchElementException;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible(emulated = true)
public abstract class ContiguousSet<C extends Comparable> extends ImmutableSortedSet<C>
{
    final DiscreteDomain<C> domain;
    
    public static <C extends Comparable> ContiguousSet<C> create(final Range<C> range, final DiscreteDomain<C> domain) {
        Preconditions.checkNotNull(range);
        Preconditions.checkNotNull(domain);
        Range<C> effectiveRange = range;
        try {
            if (!range.hasLowerBound()) {
                effectiveRange = effectiveRange.intersection(Range.atLeast(domain.minValue()));
            }
            if (!range.hasUpperBound()) {
                effectiveRange = effectiveRange.intersection(Range.atMost(domain.maxValue()));
            }
        }
        catch (NoSuchElementException e) {
            throw new IllegalArgumentException(e);
        }
        final boolean empty = effectiveRange.isEmpty() || Range.compareOrThrow(range.lowerBound.leastValueAbove(domain), range.upperBound.greatestValueBelow(domain)) > 0;
        return (ContiguousSet<C>)(empty ? new EmptyContiguousSet<Object>(domain) : new RegularContiguousSet<Object>(effectiveRange, domain));
    }
    
    ContiguousSet(final DiscreteDomain<C> domain) {
        super(Ordering.natural());
        this.domain = domain;
    }
    
    @Override
    public ContiguousSet<C> headSet(final C toElement) {
        return this.headSetImpl(Preconditions.checkNotNull(toElement), false);
    }
    
    @GwtIncompatible("NavigableSet")
    @Override
    public ContiguousSet<C> headSet(final C toElement, final boolean inclusive) {
        return this.headSetImpl(Preconditions.checkNotNull(toElement), inclusive);
    }
    
    @Override
    public ContiguousSet<C> subSet(final C fromElement, final C toElement) {
        Preconditions.checkNotNull(fromElement);
        Preconditions.checkNotNull(toElement);
        Preconditions.checkArgument(this.comparator().compare((Object)fromElement, (Object)toElement) <= 0);
        return this.subSetImpl(fromElement, true, toElement, false);
    }
    
    @GwtIncompatible("NavigableSet")
    @Override
    public ContiguousSet<C> subSet(final C fromElement, final boolean fromInclusive, final C toElement, final boolean toInclusive) {
        Preconditions.checkNotNull(fromElement);
        Preconditions.checkNotNull(toElement);
        Preconditions.checkArgument(this.comparator().compare((Object)fromElement, (Object)toElement) <= 0);
        return this.subSetImpl(fromElement, fromInclusive, toElement, toInclusive);
    }
    
    @Override
    public ContiguousSet<C> tailSet(final C fromElement) {
        return this.tailSetImpl(Preconditions.checkNotNull(fromElement), true);
    }
    
    @GwtIncompatible("NavigableSet")
    @Override
    public ContiguousSet<C> tailSet(final C fromElement, final boolean inclusive) {
        return this.tailSetImpl(Preconditions.checkNotNull(fromElement), inclusive);
    }
    
    @Override
    abstract ContiguousSet<C> headSetImpl(final C p0, final boolean p1);
    
    @Override
    abstract ContiguousSet<C> subSetImpl(final C p0, final boolean p1, final C p2, final boolean p3);
    
    @Override
    abstract ContiguousSet<C> tailSetImpl(final C p0, final boolean p1);
    
    public abstract ContiguousSet<C> intersection(final ContiguousSet<C> p0);
    
    public abstract Range<C> range();
    
    public abstract Range<C> range(final BoundType p0, final BoundType p1);
    
    @Override
    public String toString() {
        return this.range().toString();
    }
    
    @Deprecated
    public static <E> Builder<E> builder() {
        throw new UnsupportedOperationException();
    }
}
