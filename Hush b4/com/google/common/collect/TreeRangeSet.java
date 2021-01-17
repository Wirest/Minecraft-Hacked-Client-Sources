// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.base.Objects;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;
import java.util.Map;
import com.google.common.base.Preconditions;
import java.util.TreeMap;
import java.util.Set;
import com.google.common.annotations.VisibleForTesting;
import java.util.NavigableMap;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtIncompatible("uses NavigableMap")
public class TreeRangeSet<C extends Comparable<?>> extends AbstractRangeSet<C>
{
    @VisibleForTesting
    final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
    private transient Set<Range<C>> asRanges;
    private transient RangeSet<C> complement;
    
    public static <C extends Comparable<?>> TreeRangeSet<C> create() {
        return new TreeRangeSet<C>(new TreeMap<Cut<C>, Range<C>>());
    }
    
    public static <C extends Comparable<?>> TreeRangeSet<C> create(final RangeSet<C> rangeSet) {
        final TreeRangeSet<C> result = create();
        result.addAll(rangeSet);
        return result;
    }
    
    private TreeRangeSet(final NavigableMap<Cut<C>, Range<C>> rangesByLowerCut) {
        this.rangesByLowerBound = rangesByLowerCut;
    }
    
    @Override
    public Set<Range<C>> asRanges() {
        final Set<Range<C>> result = this.asRanges;
        return (result == null) ? (this.asRanges = new AsRanges()) : result;
    }
    
    @Nullable
    @Override
    public Range<C> rangeContaining(final C value) {
        Preconditions.checkNotNull(value);
        final Map.Entry<Cut<C>, Range<C>> floorEntry = this.rangesByLowerBound.floorEntry(Cut.belowValue(value));
        if (floorEntry != null && floorEntry.getValue().contains(value)) {
            return floorEntry.getValue();
        }
        return null;
    }
    
    @Override
    public boolean encloses(final Range<C> range) {
        Preconditions.checkNotNull(range);
        final Map.Entry<Cut<C>, Range<C>> floorEntry = this.rangesByLowerBound.floorEntry(range.lowerBound);
        return floorEntry != null && floorEntry.getValue().encloses(range);
    }
    
    @Nullable
    private Range<C> rangeEnclosing(final Range<C> range) {
        Preconditions.checkNotNull(range);
        final Map.Entry<Cut<C>, Range<C>> floorEntry = this.rangesByLowerBound.floorEntry(range.lowerBound);
        return (floorEntry != null && floorEntry.getValue().encloses(range)) ? floorEntry.getValue() : null;
    }
    
    @Override
    public Range<C> span() {
        final Map.Entry<Cut<C>, Range<C>> firstEntry = this.rangesByLowerBound.firstEntry();
        final Map.Entry<Cut<C>, Range<C>> lastEntry = this.rangesByLowerBound.lastEntry();
        if (firstEntry == null) {
            throw new NoSuchElementException();
        }
        return Range.create(firstEntry.getValue().lowerBound, lastEntry.getValue().upperBound);
    }
    
    @Override
    public void add(final Range<C> rangeToAdd) {
        Preconditions.checkNotNull(rangeToAdd);
        if (rangeToAdd.isEmpty()) {
            return;
        }
        Cut<C> lbToAdd = rangeToAdd.lowerBound;
        Cut<C> ubToAdd = rangeToAdd.upperBound;
        final Map.Entry<Cut<C>, Range<C>> entryBelowLB = this.rangesByLowerBound.lowerEntry(lbToAdd);
        if (entryBelowLB != null) {
            final Range<C> rangeBelowLB = entryBelowLB.getValue();
            if (rangeBelowLB.upperBound.compareTo(lbToAdd) >= 0) {
                if (rangeBelowLB.upperBound.compareTo(ubToAdd) >= 0) {
                    ubToAdd = rangeBelowLB.upperBound;
                }
                lbToAdd = rangeBelowLB.lowerBound;
            }
        }
        final Map.Entry<Cut<C>, Range<C>> entryBelowUB = this.rangesByLowerBound.floorEntry(ubToAdd);
        if (entryBelowUB != null) {
            final Range<C> rangeBelowUB = entryBelowUB.getValue();
            if (rangeBelowUB.upperBound.compareTo(ubToAdd) >= 0) {
                ubToAdd = rangeBelowUB.upperBound;
            }
        }
        this.rangesByLowerBound.subMap(lbToAdd, ubToAdd).clear();
        this.replaceRangeWithSameLowerBound(Range.create(lbToAdd, ubToAdd));
    }
    
    @Override
    public void remove(final Range<C> rangeToRemove) {
        Preconditions.checkNotNull(rangeToRemove);
        if (rangeToRemove.isEmpty()) {
            return;
        }
        final Map.Entry<Cut<C>, Range<C>> entryBelowLB = this.rangesByLowerBound.lowerEntry(rangeToRemove.lowerBound);
        if (entryBelowLB != null) {
            final Range<C> rangeBelowLB = entryBelowLB.getValue();
            if (rangeBelowLB.upperBound.compareTo(rangeToRemove.lowerBound) >= 0) {
                if (rangeToRemove.hasUpperBound() && rangeBelowLB.upperBound.compareTo(rangeToRemove.upperBound) >= 0) {
                    this.replaceRangeWithSameLowerBound(Range.create(rangeToRemove.upperBound, rangeBelowLB.upperBound));
                }
                this.replaceRangeWithSameLowerBound(Range.create(rangeBelowLB.lowerBound, rangeToRemove.lowerBound));
            }
        }
        final Map.Entry<Cut<C>, Range<C>> entryBelowUB = this.rangesByLowerBound.floorEntry(rangeToRemove.upperBound);
        if (entryBelowUB != null) {
            final Range<C> rangeBelowUB = entryBelowUB.getValue();
            if (rangeToRemove.hasUpperBound() && rangeBelowUB.upperBound.compareTo(rangeToRemove.upperBound) >= 0) {
                this.replaceRangeWithSameLowerBound(Range.create(rangeToRemove.upperBound, rangeBelowUB.upperBound));
            }
        }
        this.rangesByLowerBound.subMap(rangeToRemove.lowerBound, rangeToRemove.upperBound).clear();
    }
    
    private void replaceRangeWithSameLowerBound(final Range<C> range) {
        if (range.isEmpty()) {
            this.rangesByLowerBound.remove(range.lowerBound);
        }
        else {
            this.rangesByLowerBound.put(range.lowerBound, range);
        }
    }
    
    @Override
    public RangeSet<C> complement() {
        final RangeSet<C> result = this.complement;
        return (result == null) ? (this.complement = new Complement()) : result;
    }
    
    @Override
    public RangeSet<C> subRangeSet(final Range<C> view) {
        return view.equals(Range.all()) ? this : new SubRangeSet(view);
    }
    
    final class AsRanges extends ForwardingCollection<Range<C>> implements Set<Range<C>>
    {
        @Override
        protected Collection<Range<C>> delegate() {
            return TreeRangeSet.this.rangesByLowerBound.values();
        }
        
        @Override
        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
        
        @Override
        public boolean equals(@Nullable final Object o) {
            return Sets.equalsImpl(this, o);
        }
    }
    
    @VisibleForTesting
    static final class RangesByUpperBound<C extends Comparable<?>> extends AbstractNavigableMap<Cut<C>, Range<C>>
    {
        private final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
        private final Range<Cut<C>> upperBoundWindow;
        
        RangesByUpperBound(final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound) {
            this.rangesByLowerBound = rangesByLowerBound;
            this.upperBoundWindow = Range.all();
        }
        
        private RangesByUpperBound(final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound, final Range<Cut<C>> upperBoundWindow) {
            this.rangesByLowerBound = rangesByLowerBound;
            this.upperBoundWindow = upperBoundWindow;
        }
        
        private NavigableMap<Cut<C>, Range<C>> subMap(final Range<Cut<C>> window) {
            if (window.isConnected(this.upperBoundWindow)) {
                return new RangesByUpperBound<Object>((NavigableMap<Cut<?>, Range<?>>)this.rangesByLowerBound, (Range<Cut<?>>)window.intersection(this.upperBoundWindow));
            }
            return (NavigableMap<Cut<C>, Range<C>>)ImmutableSortedMap.of();
        }
        
        @Override
        public NavigableMap<Cut<C>, Range<C>> subMap(final Cut<C> fromKey, final boolean fromInclusive, final Cut<C> toKey, final boolean toInclusive) {
            return this.subMap(Range.range(fromKey, BoundType.forBoolean(fromInclusive), toKey, BoundType.forBoolean(toInclusive)));
        }
        
        @Override
        public NavigableMap<Cut<C>, Range<C>> headMap(final Cut<C> toKey, final boolean inclusive) {
            return this.subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
        }
        
        @Override
        public NavigableMap<Cut<C>, Range<C>> tailMap(final Cut<C> fromKey, final boolean inclusive) {
            return this.subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
        }
        
        @Override
        public Comparator<? super Cut<C>> comparator() {
            return Ordering.natural();
        }
        
        @Override
        public boolean containsKey(@Nullable final Object key) {
            return this.get(key) != null;
        }
        
        @Override
        public Range<C> get(@Nullable final Object key) {
            if (key instanceof Cut) {
                try {
                    final Cut<C> cut = (Cut<C>)key;
                    if (!this.upperBoundWindow.contains(cut)) {
                        return null;
                    }
                    final Map.Entry<Cut<C>, Range<C>> candidate = this.rangesByLowerBound.lowerEntry(cut);
                    if (candidate != null && candidate.getValue().upperBound.equals(cut)) {
                        return candidate.getValue();
                    }
                }
                catch (ClassCastException e) {
                    return null;
                }
            }
            return null;
        }
        
        @Override
        Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator() {
            Iterator<Range<C>> backingItr;
            if (!this.upperBoundWindow.hasLowerBound()) {
                backingItr = this.rangesByLowerBound.values().iterator();
            }
            else {
                final Map.Entry<Cut<C>, Range<C>> lowerEntry = this.rangesByLowerBound.lowerEntry(this.upperBoundWindow.lowerEndpoint());
                if (lowerEntry == null) {
                    backingItr = this.rangesByLowerBound.values().iterator();
                }
                else if (this.upperBoundWindow.lowerBound.isLessThan(lowerEntry.getValue().upperBound)) {
                    backingItr = this.rangesByLowerBound.tailMap(lowerEntry.getKey(), true).values().iterator();
                }
                else {
                    backingItr = this.rangesByLowerBound.tailMap(this.upperBoundWindow.lowerEndpoint(), true).values().iterator();
                }
            }
            return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>() {
                @Override
                protected Map.Entry<Cut<C>, Range<C>> computeNext() {
                    if (!backingItr.hasNext()) {
                        return this.endOfData();
                    }
                    final Range<C> range = backingItr.next();
                    if (RangesByUpperBound.this.upperBoundWindow.upperBound.isLessThan((C)range.upperBound)) {
                        return this.endOfData();
                    }
                    return Maps.immutableEntry(range.upperBound, range);
                }
            };
        }
        
        @Override
        Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
            Collection<Range<C>> candidates;
            if (this.upperBoundWindow.hasUpperBound()) {
                candidates = this.rangesByLowerBound.headMap(this.upperBoundWindow.upperEndpoint(), false).descendingMap().values();
            }
            else {
                candidates = this.rangesByLowerBound.descendingMap().values();
            }
            final PeekingIterator<Range<C>> backingItr = Iterators.peekingIterator((Iterator<? extends Range<C>>)candidates.iterator());
            if (backingItr.hasNext() && this.upperBoundWindow.upperBound.isLessThan(backingItr.peek().upperBound)) {
                backingItr.next();
            }
            return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>() {
                @Override
                protected Map.Entry<Cut<C>, Range<C>> computeNext() {
                    if (!backingItr.hasNext()) {
                        return this.endOfData();
                    }
                    final Range<C> range = backingItr.next();
                    return RangesByUpperBound.this.upperBoundWindow.lowerBound.isLessThan((C)range.upperBound) ? Maps.immutableEntry(range.upperBound, range) : this.endOfData();
                }
            };
        }
        
        @Override
        public int size() {
            if (this.upperBoundWindow.equals(Range.all())) {
                return this.rangesByLowerBound.size();
            }
            return Iterators.size(this.entryIterator());
        }
        
        @Override
        public boolean isEmpty() {
            return this.upperBoundWindow.equals(Range.all()) ? this.rangesByLowerBound.isEmpty() : (!this.entryIterator().hasNext());
        }
    }
    
    private static final class ComplementRangesByLowerBound<C extends Comparable<?>> extends AbstractNavigableMap<Cut<C>, Range<C>>
    {
        private final NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound;
        private final NavigableMap<Cut<C>, Range<C>> positiveRangesByUpperBound;
        private final Range<Cut<C>> complementLowerBoundWindow;
        
        ComplementRangesByLowerBound(final NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound) {
            this(positiveRangesByLowerBound, Range.all());
        }
        
        private ComplementRangesByLowerBound(final NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound, final Range<Cut<C>> window) {
            this.positiveRangesByLowerBound = positiveRangesByLowerBound;
            this.positiveRangesByUpperBound = (NavigableMap<Cut<C>, Range<C>>)new RangesByUpperBound((NavigableMap<Cut<Comparable>, Range<Comparable>>)positiveRangesByLowerBound);
            this.complementLowerBoundWindow = window;
        }
        
        private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> subWindow) {
            if (!this.complementLowerBoundWindow.isConnected(subWindow)) {
                return (NavigableMap<Cut<C>, Range<C>>)ImmutableSortedMap.of();
            }
            subWindow = subWindow.intersection(this.complementLowerBoundWindow);
            return new ComplementRangesByLowerBound<Object>((NavigableMap<Cut<?>, Range<?>>)this.positiveRangesByLowerBound, (Range<Cut<?>>)subWindow);
        }
        
        @Override
        public NavigableMap<Cut<C>, Range<C>> subMap(final Cut<C> fromKey, final boolean fromInclusive, final Cut<C> toKey, final boolean toInclusive) {
            return this.subMap(Range.range(fromKey, BoundType.forBoolean(fromInclusive), toKey, BoundType.forBoolean(toInclusive)));
        }
        
        @Override
        public NavigableMap<Cut<C>, Range<C>> headMap(final Cut<C> toKey, final boolean inclusive) {
            return this.subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
        }
        
        @Override
        public NavigableMap<Cut<C>, Range<C>> tailMap(final Cut<C> fromKey, final boolean inclusive) {
            return this.subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
        }
        
        @Override
        public Comparator<? super Cut<C>> comparator() {
            return Ordering.natural();
        }
        
        @Override
        Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator() {
            Collection<Range<C>> positiveRanges;
            if (this.complementLowerBoundWindow.hasLowerBound()) {
                positiveRanges = this.positiveRangesByUpperBound.tailMap(this.complementLowerBoundWindow.lowerEndpoint(), this.complementLowerBoundWindow.lowerBoundType() == BoundType.CLOSED).values();
            }
            else {
                positiveRanges = this.positiveRangesByUpperBound.values();
            }
            final PeekingIterator<Range<C>> positiveItr = Iterators.peekingIterator((Iterator<? extends Range<C>>)positiveRanges.iterator());
            Cut<C> firstComplementRangeLowerBound;
            if (this.complementLowerBoundWindow.contains(Cut.belowAll()) && (!positiveItr.hasNext() || positiveItr.peek().lowerBound != Cut.belowAll())) {
                firstComplementRangeLowerBound = Cut.belowAll();
            }
            else {
                if (!positiveItr.hasNext()) {
                    return (Iterator<Map.Entry<Cut<C>, Range<C>>>)Iterators.emptyIterator();
                }
                firstComplementRangeLowerBound = positiveItr.next().upperBound;
            }
            return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>() {
                Cut<C> nextComplementRangeLowerBound = firstComplementRangeLowerBound;
                
                @Override
                protected Map.Entry<Cut<C>, Range<C>> computeNext() {
                    if (ComplementRangesByLowerBound.this.complementLowerBoundWindow.upperBound.isLessThan((C)this.nextComplementRangeLowerBound) || this.nextComplementRangeLowerBound == Cut.aboveAll()) {
                        return this.endOfData();
                    }
                    Range<C> negativeRange;
                    if (positiveItr.hasNext()) {
                        final Range<C> positiveRange = positiveItr.next();
                        negativeRange = Range.create(this.nextComplementRangeLowerBound, positiveRange.lowerBound);
                        this.nextComplementRangeLowerBound = positiveRange.upperBound;
                    }
                    else {
                        negativeRange = Range.create(this.nextComplementRangeLowerBound, Cut.aboveAll());
                        this.nextComplementRangeLowerBound = Cut.aboveAll();
                    }
                    return Maps.immutableEntry(negativeRange.lowerBound, negativeRange);
                }
            };
        }
        
        @Override
        Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
            final Cut<C> startingPoint = this.complementLowerBoundWindow.hasUpperBound() ? this.complementLowerBoundWindow.upperEndpoint() : Cut.aboveAll();
            final boolean inclusive = this.complementLowerBoundWindow.hasUpperBound() && this.complementLowerBoundWindow.upperBoundType() == BoundType.CLOSED;
            final PeekingIterator<Range<C>> positiveItr = Iterators.peekingIterator((Iterator<? extends Range<C>>)this.positiveRangesByUpperBound.headMap(startingPoint, inclusive).descendingMap().values().iterator());
            Cut<C> cut;
            if (positiveItr.hasNext()) {
                cut = ((positiveItr.peek().upperBound == Cut.aboveAll()) ? positiveItr.next().lowerBound : this.positiveRangesByLowerBound.higherKey(positiveItr.peek().upperBound));
            }
            else {
                if (!this.complementLowerBoundWindow.contains(Cut.belowAll()) || this.positiveRangesByLowerBound.containsKey(Cut.belowAll())) {
                    return (Iterator<Map.Entry<Cut<C>, Range<C>>>)Iterators.emptyIterator();
                }
                cut = this.positiveRangesByLowerBound.higherKey(Cut.belowAll());
            }
            final Cut<C> firstComplementRangeUpperBound = Objects.firstNonNull(cut, Cut.aboveAll());
            return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>() {
                Cut<C> nextComplementRangeUpperBound = firstComplementRangeUpperBound;
                
                @Override
                protected Map.Entry<Cut<C>, Range<C>> computeNext() {
                    if (this.nextComplementRangeUpperBound == Cut.belowAll()) {
                        return this.endOfData();
                    }
                    if (positiveItr.hasNext()) {
                        final Range<C> positiveRange = positiveItr.next();
                        final Range<C> negativeRange = Range.create(positiveRange.upperBound, this.nextComplementRangeUpperBound);
                        this.nextComplementRangeUpperBound = positiveRange.lowerBound;
                        if (ComplementRangesByLowerBound.this.complementLowerBoundWindow.lowerBound.isLessThan((C)negativeRange.lowerBound)) {
                            return Maps.immutableEntry(negativeRange.lowerBound, negativeRange);
                        }
                    }
                    else if (ComplementRangesByLowerBound.this.complementLowerBoundWindow.lowerBound.isLessThan((C)Cut.belowAll())) {
                        final Range<C> negativeRange2 = Range.create(Cut.belowAll(), this.nextComplementRangeUpperBound);
                        this.nextComplementRangeUpperBound = Cut.belowAll();
                        return Maps.immutableEntry(Cut.belowAll(), negativeRange2);
                    }
                    return this.endOfData();
                }
            };
        }
        
        @Override
        public int size() {
            return Iterators.size(this.entryIterator());
        }
        
        @Nullable
        @Override
        public Range<C> get(final Object key) {
            if (key instanceof Cut) {
                try {
                    final Cut<C> cut = (Cut<C>)key;
                    final Map.Entry<Cut<C>, Range<C>> firstEntry = this.tailMap(cut, true).firstEntry();
                    if (firstEntry != null && firstEntry.getKey().equals(cut)) {
                        return firstEntry.getValue();
                    }
                }
                catch (ClassCastException e) {
                    return null;
                }
            }
            return null;
        }
        
        @Override
        public boolean containsKey(final Object key) {
            return this.get(key) != null;
        }
    }
    
    private final class Complement extends TreeRangeSet<C>
    {
        Complement() {
            super(new ComplementRangesByLowerBound(TreeRangeSet.this.rangesByLowerBound), null);
        }
        
        @Override
        public void add(final Range<C> rangeToAdd) {
            TreeRangeSet.this.remove(rangeToAdd);
        }
        
        @Override
        public void remove(final Range<C> rangeToRemove) {
            TreeRangeSet.this.add(rangeToRemove);
        }
        
        @Override
        public boolean contains(final C value) {
            return !TreeRangeSet.this.contains(value);
        }
        
        @Override
        public RangeSet<C> complement() {
            return (RangeSet<C>)TreeRangeSet.this;
        }
    }
    
    private static final class SubRangeSetRangesByLowerBound<C extends Comparable<?>> extends AbstractNavigableMap<Cut<C>, Range<C>>
    {
        private final Range<Cut<C>> lowerBoundWindow;
        private final Range<C> restriction;
        private final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
        private final NavigableMap<Cut<C>, Range<C>> rangesByUpperBound;
        
        private SubRangeSetRangesByLowerBound(final Range<Cut<C>> lowerBoundWindow, final Range<C> restriction, final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound) {
            this.lowerBoundWindow = Preconditions.checkNotNull(lowerBoundWindow);
            this.restriction = Preconditions.checkNotNull(restriction);
            this.rangesByLowerBound = Preconditions.checkNotNull(rangesByLowerBound);
            this.rangesByUpperBound = (NavigableMap<Cut<C>, Range<C>>)new RangesByUpperBound((NavigableMap<Cut<Comparable>, Range<Comparable>>)rangesByLowerBound);
        }
        
        private NavigableMap<Cut<C>, Range<C>> subMap(final Range<Cut<C>> window) {
            if (!window.isConnected(this.lowerBoundWindow)) {
                return (NavigableMap<Cut<C>, Range<C>>)ImmutableSortedMap.of();
            }
            return new SubRangeSetRangesByLowerBound<Object>((Range<Cut<?>>)this.lowerBoundWindow.intersection(window), this.restriction, (NavigableMap<Cut<?>, Range<?>>)this.rangesByLowerBound);
        }
        
        @Override
        public NavigableMap<Cut<C>, Range<C>> subMap(final Cut<C> fromKey, final boolean fromInclusive, final Cut<C> toKey, final boolean toInclusive) {
            return this.subMap(Range.range(fromKey, BoundType.forBoolean(fromInclusive), toKey, BoundType.forBoolean(toInclusive)));
        }
        
        @Override
        public NavigableMap<Cut<C>, Range<C>> headMap(final Cut<C> toKey, final boolean inclusive) {
            return this.subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
        }
        
        @Override
        public NavigableMap<Cut<C>, Range<C>> tailMap(final Cut<C> fromKey, final boolean inclusive) {
            return this.subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
        }
        
        @Override
        public Comparator<? super Cut<C>> comparator() {
            return Ordering.natural();
        }
        
        @Override
        public boolean containsKey(@Nullable final Object key) {
            return this.get(key) != null;
        }
        
        @Nullable
        @Override
        public Range<C> get(@Nullable final Object key) {
            if (key instanceof Cut) {
                try {
                    final Cut<C> cut = (Cut<C>)key;
                    if (!this.lowerBoundWindow.contains(cut) || cut.compareTo(this.restriction.lowerBound) < 0 || cut.compareTo(this.restriction.upperBound) >= 0) {
                        return null;
                    }
                    if (cut.equals(this.restriction.lowerBound)) {
                        final Range<C> candidate = Maps.valueOrNull(this.rangesByLowerBound.floorEntry(cut));
                        if (candidate != null && candidate.upperBound.compareTo(this.restriction.lowerBound) > 0) {
                            return candidate.intersection(this.restriction);
                        }
                    }
                    else {
                        final Range<C> result = this.rangesByLowerBound.get(cut);
                        if (result != null) {
                            return result.intersection(this.restriction);
                        }
                    }
                }
                catch (ClassCastException e) {
                    return null;
                }
            }
            return null;
        }
        
        @Override
        Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator() {
            if (this.restriction.isEmpty()) {
                return (Iterator<Map.Entry<Cut<C>, Range<C>>>)Iterators.emptyIterator();
            }
            if (this.lowerBoundWindow.upperBound.isLessThan(this.restriction.lowerBound)) {
                return (Iterator<Map.Entry<Cut<C>, Range<C>>>)Iterators.emptyIterator();
            }
            Iterator<Range<C>> completeRangeItr;
            if (this.lowerBoundWindow.lowerBound.isLessThan(this.restriction.lowerBound)) {
                completeRangeItr = this.rangesByUpperBound.tailMap(this.restriction.lowerBound, false).values().iterator();
            }
            else {
                completeRangeItr = this.rangesByLowerBound.tailMap(this.lowerBoundWindow.lowerBound.endpoint(), this.lowerBoundWindow.lowerBoundType() == BoundType.CLOSED).values().iterator();
            }
            final Cut<Cut<C>> upperBoundOnLowerBounds = Ordering.natural().min(this.lowerBoundWindow.upperBound, Cut.belowValue(this.restriction.upperBound));
            return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>() {
                @Override
                protected Map.Entry<Cut<C>, Range<C>> computeNext() {
                    if (!completeRangeItr.hasNext()) {
                        return this.endOfData();
                    }
                    Range<C> nextRange = completeRangeItr.next();
                    if (upperBoundOnLowerBounds.isLessThan(nextRange.lowerBound)) {
                        return this.endOfData();
                    }
                    nextRange = nextRange.intersection(SubRangeSetRangesByLowerBound.this.restriction);
                    return Maps.immutableEntry(nextRange.lowerBound, nextRange);
                }
            };
        }
        
        @Override
        Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
            if (this.restriction.isEmpty()) {
                return (Iterator<Map.Entry<Cut<C>, Range<C>>>)Iterators.emptyIterator();
            }
            final Cut<Cut<C>> upperBoundOnLowerBounds = Ordering.natural().min(this.lowerBoundWindow.upperBound, Cut.belowValue(this.restriction.upperBound));
            final Iterator<Range<C>> completeRangeItr = this.rangesByLowerBound.headMap(upperBoundOnLowerBounds.endpoint(), upperBoundOnLowerBounds.typeAsUpperBound() == BoundType.CLOSED).descendingMap().values().iterator();
            return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>() {
                @Override
                protected Map.Entry<Cut<C>, Range<C>> computeNext() {
                    if (!completeRangeItr.hasNext()) {
                        return this.endOfData();
                    }
                    Range<C> nextRange = completeRangeItr.next();
                    if (SubRangeSetRangesByLowerBound.this.restriction.lowerBound.compareTo((Cut<C>)nextRange.upperBound) >= 0) {
                        return this.endOfData();
                    }
                    nextRange = nextRange.intersection(SubRangeSetRangesByLowerBound.this.restriction);
                    if (SubRangeSetRangesByLowerBound.this.lowerBoundWindow.contains(nextRange.lowerBound)) {
                        return Maps.immutableEntry(nextRange.lowerBound, nextRange);
                    }
                    return this.endOfData();
                }
            };
        }
        
        @Override
        public int size() {
            return Iterators.size(this.entryIterator());
        }
    }
    
    private final class SubRangeSet extends TreeRangeSet<C>
    {
        private final Range<C> restriction;
        
        SubRangeSet(final Range<C> restriction) {
            super(new SubRangeSetRangesByLowerBound((Range)Range.all(), (Range)restriction, (NavigableMap)TreeRangeSet.this.rangesByLowerBound), null);
            this.restriction = restriction;
        }
        
        @Override
        public boolean encloses(final Range<C> range) {
            if (!this.restriction.isEmpty() && this.restriction.encloses(range)) {
                final Range<C> enclosing = (Range<C>)TreeRangeSet.this.rangeEnclosing(range);
                return enclosing != null && !enclosing.intersection(this.restriction).isEmpty();
            }
            return false;
        }
        
        @Nullable
        @Override
        public Range<C> rangeContaining(final C value) {
            if (!this.restriction.contains(value)) {
                return null;
            }
            final Range<C> result = TreeRangeSet.this.rangeContaining(value);
            return (result == null) ? null : result.intersection(this.restriction);
        }
        
        @Override
        public void add(final Range<C> rangeToAdd) {
            Preconditions.checkArgument(this.restriction.encloses(rangeToAdd), "Cannot add range %s to subRangeSet(%s)", rangeToAdd, this.restriction);
            super.add(rangeToAdd);
        }
        
        @Override
        public void remove(final Range<C> rangeToRemove) {
            if (rangeToRemove.isConnected(this.restriction)) {
                TreeRangeSet.this.remove(rangeToRemove.intersection(this.restriction));
            }
        }
        
        @Override
        public boolean contains(final C value) {
            return this.restriction.contains(value) && TreeRangeSet.this.contains(value);
        }
        
        @Override
        public void clear() {
            TreeRangeSet.this.remove(this.restriction);
        }
        
        @Override
        public RangeSet<C> subRangeSet(final Range<C> view) {
            if (view.encloses(this.restriction)) {
                return this;
            }
            if (view.isConnected(this.restriction)) {
                return new SubRangeSet(this.restriction.intersection(view));
            }
            return (RangeSet<C>)ImmutableRangeSet.of();
        }
    }
}
