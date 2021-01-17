// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.AbstractSet;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.NavigableSet;
import java.util.SortedSet;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.annotations.GwtIncompatible;
import java.util.concurrent.CopyOnWriteArraySet;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Collection;
import java.util.EnumSet;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class Sets
{
    private Sets() {
    }
    
    @GwtCompatible(serializable = true)
    public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(final E anElement, final E... otherElements) {
        return ImmutableEnumSet.asImmutable((EnumSet<E>)EnumSet.of(anElement, (E[])otherElements));
    }
    
    @GwtCompatible(serializable = true)
    public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(final Iterable<E> elements) {
        if (elements instanceof ImmutableEnumSet) {
            return (ImmutableSet<E>)(ImmutableEnumSet)elements;
        }
        if (elements instanceof Collection) {
            final Collection<E> collection = (Collection<E>)(Collection)elements;
            if (collection.isEmpty()) {
                return ImmutableSet.of();
            }
            return ImmutableEnumSet.asImmutable((EnumSet<E>)EnumSet.copyOf((Collection<E>)collection));
        }
        else {
            final Iterator<E> itr = elements.iterator();
            if (itr.hasNext()) {
                final EnumSet<E> enumSet = EnumSet.of(itr.next());
                Iterators.addAll(enumSet, (Iterator<? extends E>)itr);
                return ImmutableEnumSet.asImmutable(enumSet);
            }
            return ImmutableSet.of();
        }
    }
    
    public static <E extends Enum<E>> EnumSet<E> newEnumSet(final Iterable<E> iterable, final Class<E> elementType) {
        final EnumSet<E> set = EnumSet.noneOf(elementType);
        Iterables.addAll(set, (Iterable<? extends E>)iterable);
        return set;
    }
    
    public static <E> HashSet<E> newHashSet() {
        return new HashSet<E>();
    }
    
    public static <E> HashSet<E> newHashSet(final E... elements) {
        final HashSet<E> set = newHashSetWithExpectedSize(elements.length);
        Collections.addAll(set, elements);
        return set;
    }
    
    public static <E> HashSet<E> newHashSetWithExpectedSize(final int expectedSize) {
        return new HashSet<E>(Maps.capacity(expectedSize));
    }
    
    public static <E> HashSet<E> newHashSet(final Iterable<? extends E> elements) {
        return (elements instanceof Collection) ? new HashSet<E>(Collections2.cast(elements)) : newHashSet(elements.iterator());
    }
    
    public static <E> HashSet<E> newHashSet(final Iterator<? extends E> elements) {
        final HashSet<E> set = newHashSet();
        Iterators.addAll(set, elements);
        return set;
    }
    
    public static <E> Set<E> newConcurrentHashSet() {
        return newSetFromMap(new ConcurrentHashMap<E, Boolean>());
    }
    
    public static <E> Set<E> newConcurrentHashSet(final Iterable<? extends E> elements) {
        final Set<E> set = newConcurrentHashSet();
        Iterables.addAll(set, elements);
        return set;
    }
    
    public static <E> LinkedHashSet<E> newLinkedHashSet() {
        return new LinkedHashSet<E>();
    }
    
    public static <E> LinkedHashSet<E> newLinkedHashSetWithExpectedSize(final int expectedSize) {
        return new LinkedHashSet<E>(Maps.capacity(expectedSize));
    }
    
    public static <E> LinkedHashSet<E> newLinkedHashSet(final Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return new LinkedHashSet<E>((Collection<? extends E>)Collections2.cast((Iterable<? extends E>)elements));
        }
        final LinkedHashSet<E> set = newLinkedHashSet();
        Iterables.addAll(set, elements);
        return set;
    }
    
    public static <E extends Comparable> TreeSet<E> newTreeSet() {
        return new TreeSet<E>();
    }
    
    public static <E extends Comparable> TreeSet<E> newTreeSet(final Iterable<? extends E> elements) {
        final TreeSet<E> set = newTreeSet();
        Iterables.addAll(set, elements);
        return set;
    }
    
    public static <E> TreeSet<E> newTreeSet(final Comparator<? super E> comparator) {
        return new TreeSet<E>(Preconditions.checkNotNull(comparator));
    }
    
    public static <E> Set<E> newIdentityHashSet() {
        return newSetFromMap((Map<E, Boolean>)Maps.newIdentityHashMap());
    }
    
    @GwtIncompatible("CopyOnWriteArraySet")
    public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet() {
        return new CopyOnWriteArraySet<E>();
    }
    
    @GwtIncompatible("CopyOnWriteArraySet")
    public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet(final Iterable<? extends E> elements) {
        final Collection<? extends E> elementsCollection = (Collection<? extends E>)((elements instanceof Collection) ? Collections2.cast(elements) : Lists.newArrayList((Iterable<?>)elements));
        return new CopyOnWriteArraySet<E>(elementsCollection);
    }
    
    public static <E extends Enum<E>> EnumSet<E> complementOf(final Collection<E> collection) {
        if (collection instanceof EnumSet) {
            return EnumSet.complementOf((EnumSet<E>)(EnumSet)collection);
        }
        Preconditions.checkArgument(!collection.isEmpty(), (Object)"collection is empty; use the other version of this method");
        final Class<E> type = collection.iterator().next().getDeclaringClass();
        return makeComplementByHand(collection, type);
    }
    
    public static <E extends Enum<E>> EnumSet<E> complementOf(final Collection<E> collection, final Class<E> type) {
        Preconditions.checkNotNull(collection);
        return (EnumSet<E>)((collection instanceof EnumSet) ? EnumSet.complementOf((EnumSet<E>)(EnumSet)collection) : makeComplementByHand((Collection<Enum>)collection, (Class<Enum>)type));
    }
    
    private static <E extends Enum<E>> EnumSet<E> makeComplementByHand(final Collection<E> collection, final Class<E> type) {
        final EnumSet<E> result = EnumSet.allOf(type);
        result.removeAll(collection);
        return result;
    }
    
    public static <E> Set<E> newSetFromMap(final Map<E, Boolean> map) {
        return Platform.newSetFromMap(map);
    }
    
    public static <E> SetView<E> union(final Set<? extends E> set1, final Set<? extends E> set2) {
        Preconditions.checkNotNull(set1, (Object)"set1");
        Preconditions.checkNotNull(set2, (Object)"set2");
        final Set<? extends E> set2minus1 = difference(set2, set1);
        return new SetView<E>() {
            @Override
            public int size() {
                return set1.size() + set2minus1.size();
            }
            
            @Override
            public boolean isEmpty() {
                return set1.isEmpty() && set2.isEmpty();
            }
            
            @Override
            public Iterator<E> iterator() {
                return (Iterator<E>)Iterators.unmodifiableIterator(Iterators.concat(set1.iterator(), set2minus1.iterator()));
            }
            
            @Override
            public boolean contains(final Object object) {
                return set1.contains(object) || set2.contains(object);
            }
            
            @Override
            public <S extends Set<E>> S copyInto(final S set) {
                set.addAll(set1);
                set.addAll(set2);
                return set;
            }
            
            @Override
            public ImmutableSet<E> immutableCopy() {
                return new ImmutableSet.Builder<E>().addAll(set1).addAll(set2).build();
            }
        };
    }
    
    public static <E> SetView<E> intersection(final Set<E> set1, final Set<?> set2) {
        Preconditions.checkNotNull(set1, (Object)"set1");
        Preconditions.checkNotNull(set2, (Object)"set2");
        final Predicate<Object> inSet2 = Predicates.in((Collection<?>)set2);
        return new SetView<E>() {
            @Override
            public Iterator<E> iterator() {
                return (Iterator<E>)Iterators.filter(set1.iterator(), inSet2);
            }
            
            @Override
            public int size() {
                return Iterators.size(this.iterator());
            }
            
            @Override
            public boolean isEmpty() {
                return !this.iterator().hasNext();
            }
            
            @Override
            public boolean contains(final Object object) {
                return set1.contains(object) && set2.contains(object);
            }
            
            @Override
            public boolean containsAll(final Collection<?> collection) {
                return set1.containsAll(collection) && set2.containsAll(collection);
            }
        };
    }
    
    public static <E> SetView<E> difference(final Set<E> set1, final Set<?> set2) {
        Preconditions.checkNotNull(set1, (Object)"set1");
        Preconditions.checkNotNull(set2, (Object)"set2");
        final Predicate<Object> notInSet2 = Predicates.not(Predicates.in((Collection<?>)set2));
        return new SetView<E>() {
            @Override
            public Iterator<E> iterator() {
                return (Iterator<E>)Iterators.filter(set1.iterator(), notInSet2);
            }
            
            @Override
            public int size() {
                return Iterators.size(this.iterator());
            }
            
            @Override
            public boolean isEmpty() {
                return set2.containsAll(set1);
            }
            
            @Override
            public boolean contains(final Object element) {
                return set1.contains(element) && !set2.contains(element);
            }
        };
    }
    
    public static <E> SetView<E> symmetricDifference(final Set<? extends E> set1, final Set<? extends E> set2) {
        Preconditions.checkNotNull(set1, (Object)"set1");
        Preconditions.checkNotNull(set2, (Object)"set2");
        return (SetView<E>)difference(union((Set<?>)set1, (Set<?>)set2), intersection(set1, set2));
    }
    
    public static <E> Set<E> filter(final Set<E> unfiltered, final Predicate<? super E> predicate) {
        if (unfiltered instanceof SortedSet) {
            return (Set<E>)filter((SortedSet<Object>)(SortedSet)unfiltered, (Predicate<? super Object>)predicate);
        }
        if (unfiltered instanceof FilteredSet) {
            final FilteredSet<E> filtered = (FilteredSet<E>)(FilteredSet)unfiltered;
            final Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
            return new FilteredSet<E>((Set)filtered.unfiltered, combinedPredicate);
        }
        return new FilteredSet<E>(Preconditions.checkNotNull(unfiltered), Preconditions.checkNotNull(predicate));
    }
    
    public static <E> SortedSet<E> filter(final SortedSet<E> unfiltered, final Predicate<? super E> predicate) {
        return Platform.setsFilterSortedSet(unfiltered, predicate);
    }
    
    static <E> SortedSet<E> filterSortedIgnoreNavigable(final SortedSet<E> unfiltered, final Predicate<? super E> predicate) {
        if (unfiltered instanceof FilteredSet) {
            final FilteredSet<E> filtered = (FilteredSet<E>)(FilteredSet)unfiltered;
            final Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
            return new FilteredSortedSet<E>((SortedSet)filtered.unfiltered, combinedPredicate);
        }
        return new FilteredSortedSet<E>(Preconditions.checkNotNull(unfiltered), Preconditions.checkNotNull(predicate));
    }
    
    @GwtIncompatible("NavigableSet")
    public static <E> NavigableSet<E> filter(final NavigableSet<E> unfiltered, final Predicate<? super E> predicate) {
        if (unfiltered instanceof FilteredSet) {
            final FilteredSet<E> filtered = (FilteredSet<E>)(FilteredSet)unfiltered;
            final Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
            return new FilteredNavigableSet<E>((NavigableSet)filtered.unfiltered, combinedPredicate);
        }
        return new FilteredNavigableSet<E>(Preconditions.checkNotNull(unfiltered), Preconditions.checkNotNull(predicate));
    }
    
    public static <B> Set<List<B>> cartesianProduct(final List<? extends Set<? extends B>> sets) {
        return CartesianSet.create(sets);
    }
    
    public static <B> Set<List<B>> cartesianProduct(final Set<? extends B>... sets) {
        return cartesianProduct((List<? extends Set<? extends B>>)Arrays.asList((Set<? extends B>[])sets));
    }
    
    @GwtCompatible(serializable = false)
    public static <E> Set<Set<E>> powerSet(final Set<E> set) {
        return (Set<Set<E>>)new PowerSet((Set<Object>)set);
    }
    
    static int hashCodeImpl(final Set<?> s) {
        int hashCode = 0;
        for (final Object o : s) {
            hashCode += ((o != null) ? o.hashCode() : 0);
            hashCode = ~(~hashCode);
        }
        return hashCode;
    }
    
    static boolean equalsImpl(final Set<?> s, @Nullable final Object object) {
        if (s == object) {
            return true;
        }
        if (object instanceof Set) {
            final Set<?> o = (Set<?>)object;
            try {
                return s.size() == o.size() && s.containsAll(o);
            }
            catch (NullPointerException ignored) {
                return false;
            }
            catch (ClassCastException ignored2) {
                return false;
            }
        }
        return false;
    }
    
    @GwtIncompatible("NavigableSet")
    public static <E> NavigableSet<E> unmodifiableNavigableSet(final NavigableSet<E> set) {
        if (set instanceof ImmutableSortedSet || set instanceof UnmodifiableNavigableSet) {
            return set;
        }
        return new UnmodifiableNavigableSet<E>(set);
    }
    
    @GwtIncompatible("NavigableSet")
    public static <E> NavigableSet<E> synchronizedNavigableSet(final NavigableSet<E> navigableSet) {
        return Synchronized.navigableSet(navigableSet);
    }
    
    static boolean removeAllImpl(final Set<?> set, final Iterator<?> iterator) {
        boolean changed = false;
        while (iterator.hasNext()) {
            changed |= set.remove(iterator.next());
        }
        return changed;
    }
    
    static boolean removeAllImpl(final Set<?> set, Collection<?> collection) {
        Preconditions.checkNotNull(collection);
        if (collection instanceof Multiset) {
            collection = (Collection<?>)((Multiset)collection).elementSet();
        }
        if (collection instanceof Set && collection.size() > set.size()) {
            return Iterators.removeAll(set.iterator(), collection);
        }
        return removeAllImpl(set, collection.iterator());
    }
    
    abstract static class ImprovedAbstractSet<E> extends AbstractSet<E>
    {
        @Override
        public boolean removeAll(final Collection<?> c) {
            return Sets.removeAllImpl(this, c);
        }
        
        @Override
        public boolean retainAll(final Collection<?> c) {
            return super.retainAll(Preconditions.checkNotNull(c));
        }
    }
    
    public abstract static class SetView<E> extends AbstractSet<E>
    {
        private SetView() {
        }
        
        public ImmutableSet<E> immutableCopy() {
            return ImmutableSet.copyOf((Collection<? extends E>)this);
        }
        
        public <S extends Set<E>> S copyInto(final S set) {
            set.addAll((Collection<? extends E>)this);
            return set;
        }
    }
    
    private static class FilteredSet<E> extends Collections2.FilteredCollection<E> implements Set<E>
    {
        FilteredSet(final Set<E> unfiltered, final Predicate<? super E> predicate) {
            super(unfiltered, predicate);
        }
        
        @Override
        public boolean equals(@Nullable final Object object) {
            return Sets.equalsImpl(this, object);
        }
        
        @Override
        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
    }
    
    private static class FilteredSortedSet<E> extends FilteredSet<E> implements SortedSet<E>
    {
        FilteredSortedSet(final SortedSet<E> unfiltered, final Predicate<? super E> predicate) {
            super(unfiltered, predicate);
        }
        
        @Override
        public Comparator<? super E> comparator() {
            return (Comparator<? super E>)((SortedSet)this.unfiltered).comparator();
        }
        
        @Override
        public SortedSet<E> subSet(final E fromElement, final E toElement) {
            return new FilteredSortedSet((SortedSet<Object>)((SortedSet)this.unfiltered).subSet(fromElement, toElement), (Predicate<? super Object>)this.predicate);
        }
        
        @Override
        public SortedSet<E> headSet(final E toElement) {
            return new FilteredSortedSet((SortedSet<Object>)((SortedSet)this.unfiltered).headSet(toElement), (Predicate<? super Object>)this.predicate);
        }
        
        @Override
        public SortedSet<E> tailSet(final E fromElement) {
            return new FilteredSortedSet((SortedSet<Object>)((SortedSet)this.unfiltered).tailSet(fromElement), (Predicate<? super Object>)this.predicate);
        }
        
        @Override
        public E first() {
            return this.iterator().next();
        }
        
        @Override
        public E last() {
            SortedSet<E> sortedUnfiltered = (SortedSet<E>)(SortedSet)this.unfiltered;
            E element;
            while (true) {
                element = sortedUnfiltered.last();
                if (this.predicate.apply((Object)element)) {
                    break;
                }
                sortedUnfiltered = sortedUnfiltered.headSet(element);
            }
            return element;
        }
    }
    
    @GwtIncompatible("NavigableSet")
    private static class FilteredNavigableSet<E> extends FilteredSortedSet<E> implements NavigableSet<E>
    {
        FilteredNavigableSet(final NavigableSet<E> unfiltered, final Predicate<? super E> predicate) {
            super(unfiltered, predicate);
        }
        
        NavigableSet<E> unfiltered() {
            return (NavigableSet<E>)(NavigableSet)this.unfiltered;
        }
        
        @Nullable
        @Override
        public E lower(final E e) {
            return Iterators.getNext((Iterator<? extends E>)this.headSet(e, false).descendingIterator(), (E)null);
        }
        
        @Nullable
        @Override
        public E floor(final E e) {
            return Iterators.getNext((Iterator<? extends E>)this.headSet(e, true).descendingIterator(), (E)null);
        }
        
        @Override
        public E ceiling(final E e) {
            return Iterables.getFirst((Iterable<? extends E>)this.tailSet(e, true), (E)null);
        }
        
        @Override
        public E higher(final E e) {
            return Iterables.getFirst((Iterable<? extends E>)this.tailSet(e, false), (E)null);
        }
        
        @Override
        public E pollFirst() {
            return Iterables.removeFirstMatching(this.unfiltered(), this.predicate);
        }
        
        @Override
        public E pollLast() {
            return Iterables.removeFirstMatching(this.unfiltered().descendingSet(), this.predicate);
        }
        
        @Override
        public NavigableSet<E> descendingSet() {
            return Sets.filter(this.unfiltered().descendingSet(), this.predicate);
        }
        
        @Override
        public Iterator<E> descendingIterator() {
            return Iterators.filter(this.unfiltered().descendingIterator(), this.predicate);
        }
        
        @Override
        public E last() {
            return this.descendingIterator().next();
        }
        
        @Override
        public NavigableSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
            return Sets.filter(this.unfiltered().subSet(fromElement, fromInclusive, toElement, toInclusive), this.predicate);
        }
        
        @Override
        public NavigableSet<E> headSet(final E toElement, final boolean inclusive) {
            return Sets.filter(this.unfiltered().headSet(toElement, inclusive), this.predicate);
        }
        
        @Override
        public NavigableSet<E> tailSet(final E fromElement, final boolean inclusive) {
            return Sets.filter(this.unfiltered().tailSet(fromElement, inclusive), this.predicate);
        }
    }
    
    private static final class CartesianSet<E> extends ForwardingCollection<List<E>> implements Set<List<E>>
    {
        private final transient ImmutableList<ImmutableSet<E>> axes;
        private final transient CartesianList<E> delegate;
        
        static <E> Set<List<E>> create(final List<? extends Set<? extends E>> sets) {
            final ImmutableList.Builder<ImmutableSet<E>> axesBuilder = new ImmutableList.Builder<ImmutableSet<E>>(sets.size());
            for (final Set<? extends E> set : sets) {
                final ImmutableSet<E> copy = ImmutableSet.copyOf((Collection<? extends E>)set);
                if (copy.isEmpty()) {
                    return (Set<List<E>>)ImmutableSet.of();
                }
                axesBuilder.add(copy);
            }
            final ImmutableList<ImmutableSet<E>> axes = axesBuilder.build();
            final ImmutableList<List<E>> listAxes = new ImmutableList<List<E>>() {
                @Override
                public int size() {
                    return axes.size();
                }
                
                @Override
                public List<E> get(final int index) {
                    return (List<E>)((ImmutableSet)axes.get(index)).asList();
                }
                
                @Override
                boolean isPartialView() {
                    return true;
                }
            };
            return new CartesianSet<E>(axes, new CartesianList<E>(listAxes));
        }
        
        private CartesianSet(final ImmutableList<ImmutableSet<E>> axes, final CartesianList<E> delegate) {
            this.axes = axes;
            this.delegate = delegate;
        }
        
        @Override
        protected Collection<List<E>> delegate() {
            return (Collection<List<E>>)this.delegate;
        }
        
        @Override
        public boolean equals(@Nullable final Object object) {
            if (object instanceof CartesianSet) {
                final CartesianSet<?> that = (CartesianSet<?>)object;
                return this.axes.equals(that.axes);
            }
            return super.equals(object);
        }
        
        @Override
        public int hashCode() {
            int adjust = this.size() - 1;
            for (int i = 0; i < this.axes.size(); ++i) {
                adjust *= 31;
                adjust = ~(~adjust);
            }
            int hash = 1;
            for (final Set<E> axis : this.axes) {
                hash = 31 * hash + this.size() / axis.size() * axis.hashCode();
                hash = ~(~hash);
            }
            hash += adjust;
            return ~(~hash);
        }
    }
    
    private static final class SubSet<E> extends AbstractSet<E>
    {
        private final ImmutableMap<E, Integer> inputSet;
        private final int mask;
        
        SubSet(final ImmutableMap<E, Integer> inputSet, final int mask) {
            this.inputSet = inputSet;
            this.mask = mask;
        }
        
        @Override
        public Iterator<E> iterator() {
            return new UnmodifiableIterator<E>() {
                final ImmutableList<E> elements = SubSet.this.inputSet.keySet().asList();
                int remainingSetBits = SubSet.this.mask;
                
                @Override
                public boolean hasNext() {
                    return this.remainingSetBits != 0;
                }
                
                @Override
                public E next() {
                    final int index = Integer.numberOfTrailingZeros(this.remainingSetBits);
                    if (index == 32) {
                        throw new NoSuchElementException();
                    }
                    this.remainingSetBits &= ~(1 << index);
                    return this.elements.get(index);
                }
            };
        }
        
        @Override
        public int size() {
            return Integer.bitCount(this.mask);
        }
        
        @Override
        public boolean contains(@Nullable final Object o) {
            final Integer index = this.inputSet.get(o);
            return index != null && (this.mask & 1 << index) != 0x0;
        }
    }
    
    private static final class PowerSet<E> extends AbstractSet<Set<E>>
    {
        final ImmutableMap<E, Integer> inputSet;
        
        PowerSet(final Set<E> input) {
            final ImmutableMap.Builder<E, Integer> builder = ImmutableMap.builder();
            int i = 0;
            for (final E e : Preconditions.checkNotNull(input)) {
                builder.put(e, i++);
            }
            this.inputSet = builder.build();
            Preconditions.checkArgument(this.inputSet.size() <= 30, "Too many elements to create power set: %s > 30", this.inputSet.size());
        }
        
        @Override
        public int size() {
            return 1 << this.inputSet.size();
        }
        
        @Override
        public boolean isEmpty() {
            return false;
        }
        
        @Override
        public Iterator<Set<E>> iterator() {
            return new AbstractIndexedListIterator<Set<E>>(this.size()) {
                @Override
                protected Set<E> get(final int setBits) {
                    return new SubSet<E>(PowerSet.this.inputSet, setBits);
                }
            };
        }
        
        @Override
        public boolean contains(@Nullable final Object obj) {
            if (obj instanceof Set) {
                final Set<?> set = (Set<?>)obj;
                return this.inputSet.keySet().containsAll(set);
            }
            return false;
        }
        
        @Override
        public boolean equals(@Nullable final Object obj) {
            if (obj instanceof PowerSet) {
                final PowerSet<?> that = (PowerSet<?>)obj;
                return this.inputSet.equals(that.inputSet);
            }
            return super.equals(obj);
        }
        
        @Override
        public int hashCode() {
            return this.inputSet.keySet().hashCode() << this.inputSet.size() - 1;
        }
        
        @Override
        public String toString() {
            return "powerSet(" + this.inputSet + ")";
        }
    }
    
    @GwtIncompatible("NavigableSet")
    static final class UnmodifiableNavigableSet<E> extends ForwardingSortedSet<E> implements NavigableSet<E>, Serializable
    {
        private final NavigableSet<E> delegate;
        private transient UnmodifiableNavigableSet<E> descendingSet;
        private static final long serialVersionUID = 0L;
        
        UnmodifiableNavigableSet(final NavigableSet<E> delegate) {
            this.delegate = Preconditions.checkNotNull(delegate);
        }
        
        @Override
        protected SortedSet<E> delegate() {
            return Collections.unmodifiableSortedSet(this.delegate);
        }
        
        @Override
        public E lower(final E e) {
            return this.delegate.lower(e);
        }
        
        @Override
        public E floor(final E e) {
            return this.delegate.floor(e);
        }
        
        @Override
        public E ceiling(final E e) {
            return this.delegate.ceiling(e);
        }
        
        @Override
        public E higher(final E e) {
            return this.delegate.higher(e);
        }
        
        @Override
        public E pollFirst() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public E pollLast() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public NavigableSet<E> descendingSet() {
            UnmodifiableNavigableSet<E> result = this.descendingSet;
            if (result == null) {
                final UnmodifiableNavigableSet descendingSet = new UnmodifiableNavigableSet((NavigableSet<Object>)this.delegate.descendingSet());
                this.descendingSet = descendingSet;
                result = descendingSet;
                result.descendingSet = this;
            }
            return result;
        }
        
        @Override
        public Iterator<E> descendingIterator() {
            return Iterators.unmodifiableIterator(this.delegate.descendingIterator());
        }
        
        @Override
        public NavigableSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
            return Sets.unmodifiableNavigableSet(this.delegate.subSet(fromElement, fromInclusive, toElement, toInclusive));
        }
        
        @Override
        public NavigableSet<E> headSet(final E toElement, final boolean inclusive) {
            return Sets.unmodifiableNavigableSet(this.delegate.headSet(toElement, inclusive));
        }
        
        @Override
        public NavigableSet<E> tailSet(final E fromElement, final boolean inclusive) {
            return Sets.unmodifiableNavigableSet(this.delegate.tailSet(fromElement, inclusive));
        }
    }
    
    @GwtIncompatible("NavigableSet")
    static class DescendingSet<E> extends ForwardingNavigableSet<E>
    {
        private final NavigableSet<E> forward;
        
        DescendingSet(final NavigableSet<E> forward) {
            this.forward = forward;
        }
        
        @Override
        protected NavigableSet<E> delegate() {
            return this.forward;
        }
        
        @Override
        public E lower(final E e) {
            return this.forward.higher(e);
        }
        
        @Override
        public E floor(final E e) {
            return this.forward.ceiling(e);
        }
        
        @Override
        public E ceiling(final E e) {
            return this.forward.floor(e);
        }
        
        @Override
        public E higher(final E e) {
            return this.forward.lower(e);
        }
        
        @Override
        public E pollFirst() {
            return this.forward.pollLast();
        }
        
        @Override
        public E pollLast() {
            return this.forward.pollFirst();
        }
        
        @Override
        public NavigableSet<E> descendingSet() {
            return this.forward;
        }
        
        @Override
        public Iterator<E> descendingIterator() {
            return this.forward.iterator();
        }
        
        @Override
        public NavigableSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
            return this.forward.subSet(toElement, toInclusive, fromElement, fromInclusive).descendingSet();
        }
        
        @Override
        public NavigableSet<E> headSet(final E toElement, final boolean inclusive) {
            return this.forward.tailSet(toElement, inclusive).descendingSet();
        }
        
        @Override
        public NavigableSet<E> tailSet(final E fromElement, final boolean inclusive) {
            return this.forward.headSet(fromElement, inclusive).descendingSet();
        }
        
        @Override
        public Comparator<? super E> comparator() {
            final Comparator<? super E> forwardComparator = this.forward.comparator();
            if (forwardComparator == null) {
                return Ordering.natural().reverse();
            }
            return reverse(forwardComparator);
        }
        
        private static <T> Ordering<T> reverse(final Comparator<T> forward) {
            return Ordering.from(forward).reverse();
        }
        
        @Override
        public E first() {
            return this.forward.last();
        }
        
        @Override
        public SortedSet<E> headSet(final E toElement) {
            return this.standardHeadSet(toElement);
        }
        
        @Override
        public E last() {
            return this.forward.first();
        }
        
        @Override
        public SortedSet<E> subSet(final E fromElement, final E toElement) {
            return this.standardSubSet(fromElement, toElement);
        }
        
        @Override
        public SortedSet<E> tailSet(final E fromElement) {
            return this.standardTailSet(fromElement);
        }
        
        @Override
        public Iterator<E> iterator() {
            return this.forward.descendingIterator();
        }
        
        @Override
        public Object[] toArray() {
            return this.standardToArray();
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            return this.standardToArray(array);
        }
        
        @Override
        public String toString() {
            return this.standardToString();
        }
    }
}
