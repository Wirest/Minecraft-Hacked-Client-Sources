// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;
import com.google.common.base.Function;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtIncompatible("NavigableMap")
public class ImmutableRangeMap<K extends Comparable<?>, V> implements RangeMap<K, V>
{
    private static final ImmutableRangeMap<Comparable<?>, Object> EMPTY;
    private final ImmutableList<Range<K>> ranges;
    private final ImmutableList<V> values;
    
    public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of() {
        return (ImmutableRangeMap<K, V>)ImmutableRangeMap.EMPTY;
    }
    
    public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of(final Range<K> range, final V value) {
        return new ImmutableRangeMap<K, V>((ImmutableList<Range<K>>)ImmutableList.of((Range<K>)range), ImmutableList.of(value));
    }
    
    public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> copyOf(final RangeMap<K, ? extends V> rangeMap) {
        if (rangeMap instanceof ImmutableRangeMap) {
            return (ImmutableRangeMap<K, V>)(ImmutableRangeMap)rangeMap;
        }
        final Map<Range<K>, ? extends V> map = rangeMap.asMapOfRanges();
        final ImmutableList.Builder<Range<K>> rangesBuilder = new ImmutableList.Builder<Range<K>>(map.size());
        final ImmutableList.Builder<V> valuesBuilder = new ImmutableList.Builder<V>(map.size());
        for (final Map.Entry<Range<K>, ? extends V> entry : map.entrySet()) {
            rangesBuilder.add(entry.getKey());
            valuesBuilder.add((V)entry.getValue());
        }
        return new ImmutableRangeMap<K, V>(rangesBuilder.build(), valuesBuilder.build());
    }
    
    public static <K extends Comparable<?>, V> Builder<K, V> builder() {
        return new Builder<K, V>();
    }
    
    ImmutableRangeMap(final ImmutableList<Range<K>> ranges, final ImmutableList<V> values) {
        this.ranges = ranges;
        this.values = values;
    }
    
    @Nullable
    @Override
    public V get(final K key) {
        final int index = SortedLists.binarySearch(this.ranges, (Function<? super Range<K>, Cut<Comparable>>)Range.lowerBoundFn(), (Cut<Comparable>)Cut.belowValue(key), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
        if (index == -1) {
            return null;
        }
        final Range<K> range = this.ranges.get(index);
        return range.contains(key) ? this.values.get(index) : null;
    }
    
    @Nullable
    @Override
    public Map.Entry<Range<K>, V> getEntry(final K key) {
        final int index = SortedLists.binarySearch(this.ranges, (Function<? super Range<K>, Cut<Comparable>>)Range.lowerBoundFn(), (Cut<Comparable>)Cut.belowValue(key), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
        if (index == -1) {
            return null;
        }
        final Range<K> range = this.ranges.get(index);
        return range.contains(key) ? Maps.immutableEntry(range, this.values.get(index)) : null;
    }
    
    @Override
    public Range<K> span() {
        if (this.ranges.isEmpty()) {
            throw new NoSuchElementException();
        }
        final Range<K> firstRange = this.ranges.get(0);
        final Range<K> lastRange = this.ranges.get(this.ranges.size() - 1);
        return Range.create(firstRange.lowerBound, lastRange.upperBound);
    }
    
    @Override
    public void put(final Range<K> range, final V value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void putAll(final RangeMap<K, V> rangeMap) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void remove(final Range<K> range) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ImmutableMap<Range<K>, V> asMapOfRanges() {
        if (this.ranges.isEmpty()) {
            return ImmutableMap.of();
        }
        final RegularImmutableSortedSet<Range<K>> rangeSet = new RegularImmutableSortedSet<Range<K>>(this.ranges, Range.RANGE_LEX_ORDERING);
        return new RegularImmutableSortedMap<Range<K>, V>(rangeSet, this.values);
    }
    
    @Override
    public ImmutableRangeMap<K, V> subRangeMap(final Range<K> range) {
        if (Preconditions.checkNotNull(range).isEmpty()) {
            return of();
        }
        if (this.ranges.isEmpty() || range.encloses(this.span())) {
            return this;
        }
        final int lowerIndex = SortedLists.binarySearch(this.ranges, (Function<? super Range<K>, Cut<Comparable>>)Range.upperBoundFn(), (Cut<Comparable>)range.lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
        final int upperIndex = SortedLists.binarySearch(this.ranges, (Function<? super Range<K>, Cut<Comparable>>)Range.lowerBoundFn(), (Cut<Comparable>)range.upperBound, SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
        if (lowerIndex >= upperIndex) {
            return of();
        }
        final int off = lowerIndex;
        final int len = upperIndex - lowerIndex;
        final ImmutableList<Range<K>> subRanges = new ImmutableList<Range<K>>() {
            @Override
            public int size() {
                return len;
            }
            
            @Override
            public Range<K> get(final int index) {
                Preconditions.checkElementIndex(index, len);
                if (index == 0 || index == len - 1) {
                    return ((Range)ImmutableRangeMap.this.ranges.get(index + off)).intersection(range);
                }
                return (Range<K>)ImmutableRangeMap.this.ranges.get(index + off);
            }
            
            @Override
            boolean isPartialView() {
                return true;
            }
        };
        final ImmutableRangeMap<K, V> outer = this;
        return new ImmutableRangeMap<K, V>(subRanges, this.values.subList(lowerIndex, upperIndex)) {
            @Override
            public ImmutableRangeMap<K, V> subRangeMap(final Range<K> subRange) {
                if (range.isConnected(subRange)) {
                    return outer.subRangeMap(subRange.intersection(range));
                }
                return ImmutableRangeMap.of();
            }
        };
    }
    
    @Override
    public int hashCode() {
        return this.asMapOfRanges().hashCode();
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        if (o instanceof RangeMap) {
            final RangeMap<?, ?> rangeMap = (RangeMap<?, ?>)o;
            return this.asMapOfRanges().equals(rangeMap.asMapOfRanges());
        }
        return false;
    }
    
    @Override
    public String toString() {
        return this.asMapOfRanges().toString();
    }
    
    static {
        EMPTY = new ImmutableRangeMap<Comparable<?>, Object>(ImmutableList.of(), ImmutableList.of());
    }
    
    public static final class Builder<K extends Comparable<?>, V>
    {
        private final RangeSet<K> keyRanges;
        private final RangeMap<K, V> rangeMap;
        
        public Builder() {
            this.keyRanges = (RangeSet<K>)TreeRangeSet.create();
            this.rangeMap = (RangeMap<K, V>)TreeRangeMap.create();
        }
        
        public Builder<K, V> put(final Range<K> range, final V value) {
            Preconditions.checkNotNull(range);
            Preconditions.checkNotNull(value);
            Preconditions.checkArgument(!range.isEmpty(), "Range must not be empty, but was %s", range);
            if (!this.keyRanges.complement().encloses(range)) {
                for (final Map.Entry<Range<K>, V> entry : this.rangeMap.asMapOfRanges().entrySet()) {
                    final Range<K> key = entry.getKey();
                    if (key.isConnected(range) && !key.intersection(range).isEmpty()) {
                        throw new IllegalArgumentException("Overlapping ranges: range " + range + " overlaps with entry " + entry);
                    }
                }
            }
            this.keyRanges.add(range);
            this.rangeMap.put(range, value);
            return this;
        }
        
        public Builder<K, V> putAll(final RangeMap<K, ? extends V> rangeMap) {
            for (final Map.Entry<Range<K>, ? extends V> entry : rangeMap.asMapOfRanges().entrySet()) {
                this.put(entry.getKey(), entry.getValue());
            }
            return this;
        }
        
        public ImmutableRangeMap<K, V> build() {
            final Map<Range<K>, V> map = this.rangeMap.asMapOfRanges();
            final ImmutableList.Builder<Range<K>> rangesBuilder = new ImmutableList.Builder<Range<K>>(map.size());
            final ImmutableList.Builder<V> valuesBuilder = new ImmutableList.Builder<V>(map.size());
            for (final Map.Entry<Range<K>, V> entry : map.entrySet()) {
                rangesBuilder.add(entry.getKey());
                valuesBuilder.add(entry.getValue());
            }
            return new ImmutableRangeMap<K, V>(rangesBuilder.build(), valuesBuilder.build());
        }
    }
}
