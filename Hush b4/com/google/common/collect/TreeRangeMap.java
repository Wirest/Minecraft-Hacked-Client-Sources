// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Predicates;
import java.util.Collection;
import java.util.List;
import com.google.common.base.Predicate;
import java.util.AbstractSet;
import java.util.Set;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.NavigableMap;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtIncompatible("NavigableMap")
public final class TreeRangeMap<K extends Comparable, V> implements RangeMap<K, V>
{
    private final NavigableMap<Cut<K>, RangeMapEntry<K, V>> entriesByLowerBound;
    private static final RangeMap EMPTY_SUB_RANGE_MAP;
    
    public static <K extends Comparable, V> TreeRangeMap<K, V> create() {
        return new TreeRangeMap<K, V>();
    }
    
    private TreeRangeMap() {
        this.entriesByLowerBound = (NavigableMap<Cut<K>, RangeMapEntry<K, V>>)Maps.newTreeMap();
    }
    
    @Nullable
    @Override
    public V get(final K key) {
        final Map.Entry<Range<K>, V> entry = this.getEntry(key);
        return (entry == null) ? null : entry.getValue();
    }
    
    @Nullable
    @Override
    public Map.Entry<Range<K>, V> getEntry(final K key) {
        final Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntry = this.entriesByLowerBound.floorEntry(Cut.belowValue(key));
        if (mapEntry != null && mapEntry.getValue().contains(key)) {
            return (Map.Entry<Range<K>, V>)mapEntry.getValue();
        }
        return null;
    }
    
    @Override
    public void put(final Range<K> range, final V value) {
        if (!range.isEmpty()) {
            Preconditions.checkNotNull(value);
            this.remove(range);
            this.entriesByLowerBound.put(range.lowerBound, new RangeMapEntry<K, V>(range, value));
        }
    }
    
    @Override
    public void putAll(final RangeMap<K, V> rangeMap) {
        for (final Map.Entry<Range<K>, V> entry : rangeMap.asMapOfRanges().entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }
    
    @Override
    public void clear() {
        this.entriesByLowerBound.clear();
    }
    
    @Override
    public Range<K> span() {
        final Map.Entry<Cut<K>, RangeMapEntry<K, V>> firstEntry = this.entriesByLowerBound.firstEntry();
        final Map.Entry<Cut<K>, RangeMapEntry<K, V>> lastEntry = this.entriesByLowerBound.lastEntry();
        if (firstEntry == null) {
            throw new NoSuchElementException();
        }
        return Range.create(firstEntry.getValue().getKey().lowerBound, lastEntry.getValue().getKey().upperBound);
    }
    
    private void putRangeMapEntry(final Cut<K> lowerBound, final Cut<K> upperBound, final V value) {
        this.entriesByLowerBound.put(lowerBound, new RangeMapEntry<K, V>(lowerBound, upperBound, value));
    }
    
    @Override
    public void remove(final Range<K> rangeToRemove) {
        if (rangeToRemove.isEmpty()) {
            return;
        }
        final Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntryBelowToTruncate = this.entriesByLowerBound.lowerEntry(rangeToRemove.lowerBound);
        if (mapEntryBelowToTruncate != null) {
            final RangeMapEntry<K, V> rangeMapEntry = mapEntryBelowToTruncate.getValue();
            if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.lowerBound) > 0) {
                if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.upperBound) > 0) {
                    this.putRangeMapEntry(rangeToRemove.upperBound, rangeMapEntry.getUpperBound(), mapEntryBelowToTruncate.getValue().getValue());
                }
                this.putRangeMapEntry(rangeMapEntry.getLowerBound(), rangeToRemove.lowerBound, mapEntryBelowToTruncate.getValue().getValue());
            }
        }
        final Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntryAboveToTruncate = this.entriesByLowerBound.lowerEntry(rangeToRemove.upperBound);
        if (mapEntryAboveToTruncate != null) {
            final RangeMapEntry<K, V> rangeMapEntry2 = mapEntryAboveToTruncate.getValue();
            if (rangeMapEntry2.getUpperBound().compareTo(rangeToRemove.upperBound) > 0) {
                this.putRangeMapEntry(rangeToRemove.upperBound, rangeMapEntry2.getUpperBound(), mapEntryAboveToTruncate.getValue().getValue());
                this.entriesByLowerBound.remove(rangeToRemove.lowerBound);
            }
        }
        this.entriesByLowerBound.subMap(rangeToRemove.lowerBound, rangeToRemove.upperBound).clear();
    }
    
    @Override
    public Map<Range<K>, V> asMapOfRanges() {
        return new AsMapOfRanges();
    }
    
    @Override
    public RangeMap<K, V> subRangeMap(final Range<K> subRange) {
        if (subRange.equals(Range.all())) {
            return this;
        }
        return new SubRangeMap(subRange);
    }
    
    private RangeMap<K, V> emptySubRangeMap() {
        return (RangeMap<K, V>)TreeRangeMap.EMPTY_SUB_RANGE_MAP;
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
    public int hashCode() {
        return this.asMapOfRanges().hashCode();
    }
    
    @Override
    public String toString() {
        return this.entriesByLowerBound.values().toString();
    }
    
    static {
        EMPTY_SUB_RANGE_MAP = new RangeMap() {
            @Nullable
            @Override
            public Object get(final Comparable key) {
                return null;
            }
            
            @Nullable
            @Override
            public Map.Entry<Range, Object> getEntry(final Comparable key) {
                return null;
            }
            
            @Override
            public Range span() {
                throw new NoSuchElementException();
            }
            
            @Override
            public void put(final Range range, final Object value) {
                Preconditions.checkNotNull(range);
                throw new IllegalArgumentException("Cannot insert range " + range + " into an empty subRangeMap");
            }
            
            @Override
            public void putAll(final RangeMap rangeMap) {
                if (!rangeMap.asMapOfRanges().isEmpty()) {
                    throw new IllegalArgumentException("Cannot putAll(nonEmptyRangeMap) into an empty subRangeMap");
                }
            }
            
            @Override
            public void clear() {
            }
            
            @Override
            public void remove(final Range range) {
                Preconditions.checkNotNull(range);
            }
            
            @Override
            public Map<Range, Object> asMapOfRanges() {
                return (Map<Range, Object>)Collections.emptyMap();
            }
            
            @Override
            public RangeMap subRangeMap(final Range range) {
                Preconditions.checkNotNull(range);
                return this;
            }
        };
    }
    
    private static final class RangeMapEntry<K extends Comparable, V> extends AbstractMapEntry<Range<K>, V>
    {
        private final Range<K> range;
        private final V value;
        
        RangeMapEntry(final Cut<K> lowerBound, final Cut<K> upperBound, final V value) {
            this(Range.create(lowerBound, upperBound), value);
        }
        
        RangeMapEntry(final Range<K> range, final V value) {
            this.range = range;
            this.value = value;
        }
        
        @Override
        public Range<K> getKey() {
            return this.range;
        }
        
        @Override
        public V getValue() {
            return this.value;
        }
        
        public boolean contains(final K value) {
            return this.range.contains(value);
        }
        
        Cut<K> getLowerBound() {
            return this.range.lowerBound;
        }
        
        Cut<K> getUpperBound() {
            return this.range.upperBound;
        }
    }
    
    private final class AsMapOfRanges extends AbstractMap<Range<K>, V>
    {
        @Override
        public boolean containsKey(@Nullable final Object key) {
            return this.get(key) != null;
        }
        
        @Override
        public V get(@Nullable final Object key) {
            if (key instanceof Range) {
                final Range<?> range = (Range<?>)key;
                final RangeMapEntry<K, V> rangeMapEntry = (RangeMapEntry<K, V>)TreeRangeMap.this.entriesByLowerBound.get(range.lowerBound);
                if (rangeMapEntry != null && rangeMapEntry.getKey().equals(range)) {
                    return rangeMapEntry.getValue();
                }
            }
            return null;
        }
        
        @Override
        public Set<Map.Entry<Range<K>, V>> entrySet() {
            return new AbstractSet<Map.Entry<Range<K>, V>>() {
                @Override
                public Iterator<Map.Entry<Range<K>, V>> iterator() {
                    return (Iterator<Map.Entry<Range<K>, V>>)TreeRangeMap.this.entriesByLowerBound.values().iterator();
                }
                
                @Override
                public int size() {
                    return TreeRangeMap.this.entriesByLowerBound.size();
                }
            };
        }
    }
    
    private class SubRangeMap implements RangeMap<K, V>
    {
        private final Range<K> subRange;
        
        SubRangeMap(final Range<K> subRange) {
            this.subRange = subRange;
        }
        
        @Nullable
        @Override
        public V get(final K key) {
            return this.subRange.contains(key) ? TreeRangeMap.this.get(key) : null;
        }
        
        @Nullable
        @Override
        public Map.Entry<Range<K>, V> getEntry(final K key) {
            if (this.subRange.contains(key)) {
                final Map.Entry<Range<K>, V> entry = TreeRangeMap.this.getEntry(key);
                if (entry != null) {
                    return Maps.immutableEntry(entry.getKey().intersection(this.subRange), entry.getValue());
                }
            }
            return null;
        }
        
        @Override
        public Range<K> span() {
            final Map.Entry<Cut<K>, RangeMapEntry<K, V>> lowerEntry = TreeRangeMap.this.entriesByLowerBound.floorEntry(this.subRange.lowerBound);
            Cut<K> lowerBound;
            if (lowerEntry != null && lowerEntry.getValue().getUpperBound().compareTo(this.subRange.lowerBound) > 0) {
                lowerBound = this.subRange.lowerBound;
            }
            else {
                lowerBound = TreeRangeMap.this.entriesByLowerBound.ceilingKey(this.subRange.lowerBound);
                if (lowerBound == null || lowerBound.compareTo(this.subRange.upperBound) >= 0) {
                    throw new NoSuchElementException();
                }
            }
            final Map.Entry<Cut<K>, RangeMapEntry<K, V>> upperEntry = TreeRangeMap.this.entriesByLowerBound.lowerEntry(this.subRange.upperBound);
            if (upperEntry == null) {
                throw new NoSuchElementException();
            }
            Cut<K> upperBound;
            if (upperEntry.getValue().getUpperBound().compareTo(this.subRange.upperBound) >= 0) {
                upperBound = this.subRange.upperBound;
            }
            else {
                upperBound = upperEntry.getValue().getUpperBound();
            }
            return Range.create(lowerBound, upperBound);
        }
        
        @Override
        public void put(final Range<K> range, final V value) {
            Preconditions.checkArgument(this.subRange.encloses(range), "Cannot put range %s into a subRangeMap(%s)", range, this.subRange);
            TreeRangeMap.this.put(range, value);
        }
        
        @Override
        public void putAll(final RangeMap<K, V> rangeMap) {
            if (rangeMap.asMapOfRanges().isEmpty()) {
                return;
            }
            final Range<K> span = rangeMap.span();
            Preconditions.checkArgument(this.subRange.encloses(span), "Cannot putAll rangeMap with span %s into a subRangeMap(%s)", span, this.subRange);
            TreeRangeMap.this.putAll(rangeMap);
        }
        
        @Override
        public void clear() {
            TreeRangeMap.this.remove(this.subRange);
        }
        
        @Override
        public void remove(final Range<K> range) {
            if (range.isConnected(this.subRange)) {
                TreeRangeMap.this.remove(range.intersection(this.subRange));
            }
        }
        
        @Override
        public RangeMap<K, V> subRangeMap(final Range<K> range) {
            if (!range.isConnected(this.subRange)) {
                return (RangeMap<K, V>)TreeRangeMap.this.emptySubRangeMap();
            }
            return TreeRangeMap.this.subRangeMap(range.intersection(this.subRange));
        }
        
        @Override
        public Map<Range<K>, V> asMapOfRanges() {
            return new SubRangeMapAsMap();
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
        public int hashCode() {
            return this.asMapOfRanges().hashCode();
        }
        
        @Override
        public String toString() {
            return this.asMapOfRanges().toString();
        }
        
        class SubRangeMapAsMap extends AbstractMap<Range<K>, V>
        {
            @Override
            public boolean containsKey(final Object key) {
                return this.get(key) != null;
            }
            
            @Override
            public V get(final Object key) {
                try {
                    if (key instanceof Range) {
                        final Range<K> r = (Range<K>)key;
                        if (!SubRangeMap.this.subRange.encloses(r) || r.isEmpty()) {
                            return null;
                        }
                        RangeMapEntry<K, V> candidate = null;
                        if (r.lowerBound.compareTo((Cut<K>)SubRangeMap.this.subRange.lowerBound) == 0) {
                            final Map.Entry<Cut<K>, RangeMapEntry<K, V>> entry = TreeRangeMap.this.entriesByLowerBound.floorEntry(r.lowerBound);
                            if (entry != null) {
                                candidate = entry.getValue();
                            }
                        }
                        else {
                            candidate = (RangeMapEntry<K, V>)TreeRangeMap.this.entriesByLowerBound.get(r.lowerBound);
                        }
                        if (candidate != null && candidate.getKey().isConnected(SubRangeMap.this.subRange) && candidate.getKey().intersection(SubRangeMap.this.subRange).equals(r)) {
                            return candidate.getValue();
                        }
                    }
                }
                catch (ClassCastException e) {
                    return null;
                }
                return null;
            }
            
            @Override
            public V remove(final Object key) {
                final V value = this.get(key);
                if (value != null) {
                    final Range<K> range = (Range<K>)key;
                    TreeRangeMap.this.remove(range);
                    return value;
                }
                return null;
            }
            
            @Override
            public void clear() {
                SubRangeMap.this.clear();
            }
            
            private boolean removeEntryIf(final Predicate<? super Map.Entry<Range<K>, V>> predicate) {
                final List<Range<K>> toRemove = (List<Range<K>>)Lists.newArrayList();
                for (final Map.Entry<Range<K>, V> entry : this.entrySet()) {
                    if (predicate.apply(entry)) {
                        toRemove.add(entry.getKey());
                    }
                }
                for (final Range<K> range : toRemove) {
                    TreeRangeMap.this.remove(range);
                }
                return !toRemove.isEmpty();
            }
            
            @Override
            public Set<Range<K>> keySet() {
                return (Set<Range<K>>)new Maps.KeySet<Range<K>, V>(this) {
                    @Override
                    public boolean remove(@Nullable final Object o) {
                        return SubRangeMapAsMap.this.remove(o) != null;
                    }
                    
                    @Override
                    public boolean retainAll(final Collection<?> c) {
                        return SubRangeMapAsMap.this.removeEntryIf(Predicates.compose((Predicate<Object>)Predicates.not(Predicates.in((Collection<? extends B>)c)), Maps.keyFunction()));
                    }
                };
            }
            
            @Override
            public Set<Map.Entry<Range<K>, V>> entrySet() {
                return (Set<Map.Entry<Range<K>, V>>)new Maps.EntrySet<Range<K>, V>() {
                    @Override
                    Map<Range<K>, V> map() {
                        return SubRangeMapAsMap.this;
                    }
                    
                    @Override
                    public Iterator<Map.Entry<Range<K>, V>> iterator() {
                        if (SubRangeMap.this.subRange.isEmpty()) {
                            return (Iterator<Map.Entry<Range<K>, V>>)Iterators.emptyIterator();
                        }
                        final Cut<K> cutToStart = Objects.firstNonNull((Cut<K>)TreeRangeMap.this.entriesByLowerBound.floorKey(SubRangeMap.this.subRange.lowerBound), SubRangeMap.this.subRange.lowerBound);
                        final Iterator<RangeMapEntry<K, V>> backingItr = TreeRangeMap.this.entriesByLowerBound.tailMap(cutToStart, true).values().iterator();
                        return new AbstractIterator<Map.Entry<Range<K>, V>>() {
                            @Override
                            protected Map.Entry<Range<K>, V> computeNext() {
                                while (backingItr.hasNext()) {
                                    final RangeMapEntry<K, V> entry = backingItr.next();
                                    if (entry.getLowerBound().compareTo((Cut<K>)SubRangeMap.this.subRange.upperBound) >= 0) {
                                        break;
                                    }
                                    if (entry.getUpperBound().compareTo((Cut<K>)SubRangeMap.this.subRange.lowerBound) > 0) {
                                        return Maps.immutableEntry(entry.getKey().intersection(SubRangeMap.this.subRange), entry.getValue());
                                    }
                                }
                                return this.endOfData();
                            }
                        };
                    }
                    
                    @Override
                    public boolean retainAll(final Collection<?> c) {
                        return SubRangeMapAsMap.this.removeEntryIf(Predicates.not(Predicates.in(c)));
                    }
                    
                    @Override
                    public int size() {
                        return Iterators.size(this.iterator());
                    }
                    
                    @Override
                    public boolean isEmpty() {
                        return !this.iterator().hasNext();
                    }
                };
            }
            
            @Override
            public Collection<V> values() {
                return (Collection<V>)new Maps.Values<Range<K>, V>(this) {
                    @Override
                    public boolean removeAll(final Collection<?> c) {
                        return SubRangeMapAsMap.this.removeEntryIf(Predicates.compose(Predicates.in(c), Maps.valueFunction()));
                    }
                    
                    @Override
                    public boolean retainAll(final Collection<?> c) {
                        return SubRangeMapAsMap.this.removeEntryIf(Predicates.compose((Predicate<Object>)Predicates.not(Predicates.in((Collection<? extends B>)c)), Maps.valueFunction()));
                    }
                };
            }
        }
    }
}
