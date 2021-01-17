// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Set;
import java.util.SortedSet;
import java.util.NavigableSet;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import javax.annotation.Nullable;

final class RegularImmutableSortedMultiset<E> extends ImmutableSortedMultiset<E>
{
    private final transient RegularImmutableSortedSet<E> elementSet;
    private final transient int[] counts;
    private final transient long[] cumulativeCounts;
    private final transient int offset;
    private final transient int length;
    
    RegularImmutableSortedMultiset(final RegularImmutableSortedSet<E> elementSet, final int[] counts, final long[] cumulativeCounts, final int offset, final int length) {
        this.elementSet = elementSet;
        this.counts = counts;
        this.cumulativeCounts = cumulativeCounts;
        this.offset = offset;
        this.length = length;
    }
    
    @Override
    Multiset.Entry<E> getEntry(final int index) {
        return Multisets.immutableEntry(this.elementSet.asList().get(index), this.counts[this.offset + index]);
    }
    
    @Override
    public Multiset.Entry<E> firstEntry() {
        return this.getEntry(0);
    }
    
    @Override
    public Multiset.Entry<E> lastEntry() {
        return this.getEntry(this.length - 1);
    }
    
    @Override
    public int count(@Nullable final Object element) {
        final int index = this.elementSet.indexOf(element);
        return (index == -1) ? 0 : this.counts[index + this.offset];
    }
    
    @Override
    public int size() {
        final long size = this.cumulativeCounts[this.offset + this.length] - this.cumulativeCounts[this.offset];
        return Ints.saturatedCast(size);
    }
    
    @Override
    public ImmutableSortedSet<E> elementSet() {
        return this.elementSet;
    }
    
    @Override
    public ImmutableSortedMultiset<E> headMultiset(final E upperBound, final BoundType boundType) {
        return this.getSubMultiset(0, this.elementSet.headIndex(upperBound, Preconditions.checkNotNull(boundType) == BoundType.CLOSED));
    }
    
    @Override
    public ImmutableSortedMultiset<E> tailMultiset(final E lowerBound, final BoundType boundType) {
        return this.getSubMultiset(this.elementSet.tailIndex(lowerBound, Preconditions.checkNotNull(boundType) == BoundType.CLOSED), this.length);
    }
    
    ImmutableSortedMultiset<E> getSubMultiset(final int from, final int to) {
        Preconditions.checkPositionIndexes(from, to, this.length);
        if (from == to) {
            return ImmutableSortedMultiset.emptyMultiset(this.comparator());
        }
        if (from == 0 && to == this.length) {
            return this;
        }
        final RegularImmutableSortedSet<E> subElementSet = (RegularImmutableSortedSet<E>)(RegularImmutableSortedSet)this.elementSet.getSubSet(from, to);
        return new RegularImmutableSortedMultiset((RegularImmutableSortedSet<Object>)subElementSet, this.counts, this.cumulativeCounts, this.offset + from, to - from);
    }
    
    @Override
    boolean isPartialView() {
        return this.offset > 0 || this.length < this.counts.length;
    }
}
