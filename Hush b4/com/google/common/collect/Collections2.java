// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Arrays;
import java.util.ArrayList;
import com.google.common.math.IntMath;
import java.util.Collections;
import com.google.common.math.LongMath;
import java.util.Iterator;
import java.util.AbstractCollection;
import com.google.common.annotations.Beta;
import java.util.Comparator;
import java.util.List;
import com.google.common.base.Predicates;
import com.google.common.base.Function;
import javax.annotation.Nullable;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.util.Collection;
import com.google.common.base.Joiner;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public final class Collections2
{
    static final Joiner STANDARD_JOINER;
    
    private Collections2() {
    }
    
    public static <E> Collection<E> filter(final Collection<E> unfiltered, final Predicate<? super E> predicate) {
        if (unfiltered instanceof FilteredCollection) {
            return (Collection<E>)((FilteredCollection)unfiltered).createCombined(predicate);
        }
        return new FilteredCollection<E>(Preconditions.checkNotNull(unfiltered), Preconditions.checkNotNull(predicate));
    }
    
    static boolean safeContains(final Collection<?> collection, @Nullable final Object object) {
        Preconditions.checkNotNull(collection);
        try {
            return collection.contains(object);
        }
        catch (ClassCastException e) {
            return false;
        }
        catch (NullPointerException e2) {
            return false;
        }
    }
    
    static boolean safeRemove(final Collection<?> collection, @Nullable final Object object) {
        Preconditions.checkNotNull(collection);
        try {
            return collection.remove(object);
        }
        catch (ClassCastException e) {
            return false;
        }
        catch (NullPointerException e2) {
            return false;
        }
    }
    
    public static <F, T> Collection<T> transform(final Collection<F> fromCollection, final Function<? super F, T> function) {
        return (Collection<T>)new TransformedCollection((Collection<Object>)fromCollection, (Function<? super Object, ?>)function);
    }
    
    static boolean containsAllImpl(final Collection<?> self, final Collection<?> c) {
        return Iterables.all(c, Predicates.in(self));
    }
    
    static String toStringImpl(final Collection<?> collection) {
        final StringBuilder sb = newStringBuilderForCollection(collection.size()).append('[');
        Collections2.STANDARD_JOINER.appendTo(sb, Iterables.transform((Iterable<?>)collection, (Function<?, ?>)new Function<Object, Object>() {
            @Override
            public Object apply(final Object input) {
                return (input == collection) ? "(this Collection)" : input;
            }
        }));
        return sb.append(']').toString();
    }
    
    static StringBuilder newStringBuilderForCollection(final int size) {
        CollectPreconditions.checkNonnegative(size, "size");
        return new StringBuilder((int)Math.min(size * 8L, 1073741824L));
    }
    
    static <T> Collection<T> cast(final Iterable<T> iterable) {
        return (Collection<T>)(Collection)iterable;
    }
    
    @Beta
    public static <E extends Comparable<? super E>> Collection<List<E>> orderedPermutations(final Iterable<E> elements) {
        return orderedPermutations(elements, Ordering.natural());
    }
    
    @Beta
    public static <E> Collection<List<E>> orderedPermutations(final Iterable<E> elements, final Comparator<? super E> comparator) {
        return (Collection<List<E>>)new OrderedPermutationCollection((Iterable<Object>)elements, (Comparator<? super Object>)comparator);
    }
    
    @Beta
    public static <E> Collection<List<E>> permutations(final Collection<E> elements) {
        return (Collection<List<E>>)new PermutationCollection(ImmutableList.copyOf((Collection<?>)elements));
    }
    
    private static boolean isPermutation(final List<?> first, final List<?> second) {
        if (first.size() != second.size()) {
            return false;
        }
        final Multiset<?> firstMultiset = HashMultiset.create((Iterable<?>)first);
        final Multiset<?> secondMultiset = HashMultiset.create((Iterable<?>)second);
        return firstMultiset.equals(secondMultiset);
    }
    
    private static boolean isPositiveInt(final long n) {
        return n >= 0L && n <= 2147483647L;
    }
    
    static {
        STANDARD_JOINER = Joiner.on(", ").useForNull("null");
    }
    
    static class FilteredCollection<E> extends AbstractCollection<E>
    {
        final Collection<E> unfiltered;
        final Predicate<? super E> predicate;
        
        FilteredCollection(final Collection<E> unfiltered, final Predicate<? super E> predicate) {
            this.unfiltered = unfiltered;
            this.predicate = predicate;
        }
        
        FilteredCollection<E> createCombined(final Predicate<? super E> newPredicate) {
            return new FilteredCollection<E>(this.unfiltered, Predicates.and(this.predicate, newPredicate));
        }
        
        @Override
        public boolean add(final E element) {
            Preconditions.checkArgument(this.predicate.apply((Object)element));
            return this.unfiltered.add(element);
        }
        
        @Override
        public boolean addAll(final Collection<? extends E> collection) {
            for (final E element : collection) {
                Preconditions.checkArgument(this.predicate.apply((Object)element));
            }
            return this.unfiltered.addAll(collection);
        }
        
        @Override
        public void clear() {
            Iterables.removeIf(this.unfiltered, this.predicate);
        }
        
        @Override
        public boolean contains(@Nullable final Object element) {
            if (Collections2.safeContains(this.unfiltered, element)) {
                final E e = (E)element;
                return this.predicate.apply((Object)e);
            }
            return false;
        }
        
        @Override
        public boolean containsAll(final Collection<?> collection) {
            return Collections2.containsAllImpl(this, collection);
        }
        
        @Override
        public boolean isEmpty() {
            return !Iterables.any(this.unfiltered, this.predicate);
        }
        
        @Override
        public Iterator<E> iterator() {
            return Iterators.filter(this.unfiltered.iterator(), this.predicate);
        }
        
        @Override
        public boolean remove(final Object element) {
            return this.contains(element) && this.unfiltered.remove(element);
        }
        
        @Override
        public boolean removeAll(final Collection<?> collection) {
            return Iterables.removeIf(this.unfiltered, Predicates.and(this.predicate, Predicates.in((Collection<? extends E>)collection)));
        }
        
        @Override
        public boolean retainAll(final Collection<?> collection) {
            return Iterables.removeIf(this.unfiltered, Predicates.and(this.predicate, (Predicate<? super E>)Predicates.not(Predicates.in((Collection<? extends T>)collection))));
        }
        
        @Override
        public int size() {
            return Iterators.size(this.iterator());
        }
        
        @Override
        public Object[] toArray() {
            return Lists.newArrayList(this.iterator()).toArray();
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            return Lists.newArrayList(this.iterator()).toArray(array);
        }
    }
    
    static class TransformedCollection<F, T> extends AbstractCollection<T>
    {
        final Collection<F> fromCollection;
        final Function<? super F, ? extends T> function;
        
        TransformedCollection(final Collection<F> fromCollection, final Function<? super F, ? extends T> function) {
            this.fromCollection = Preconditions.checkNotNull(fromCollection);
            this.function = Preconditions.checkNotNull(function);
        }
        
        @Override
        public void clear() {
            this.fromCollection.clear();
        }
        
        @Override
        public boolean isEmpty() {
            return this.fromCollection.isEmpty();
        }
        
        @Override
        public Iterator<T> iterator() {
            return Iterators.transform(this.fromCollection.iterator(), this.function);
        }
        
        @Override
        public int size() {
            return this.fromCollection.size();
        }
    }
    
    private static final class OrderedPermutationCollection<E> extends AbstractCollection<List<E>>
    {
        final ImmutableList<E> inputList;
        final Comparator<? super E> comparator;
        final int size;
        
        OrderedPermutationCollection(final Iterable<E> input, final Comparator<? super E> comparator) {
            this.inputList = Ordering.from(comparator).immutableSortedCopy(input);
            this.comparator = comparator;
            this.size = calculateSize(this.inputList, comparator);
        }
        
        private static <E> int calculateSize(final List<E> sortedInputList, final Comparator<? super E> comparator) {
            long permutations = 1L;
            int n;
            int r;
            for (n = 1, r = 1; n < sortedInputList.size(); ++n, ++r) {
                final int comparison = comparator.compare((Object)sortedInputList.get(n - 1), (Object)sortedInputList.get(n));
                if (comparison < 0) {
                    permutations *= LongMath.binomial(n, r);
                    r = 0;
                    if (!isPositiveInt(permutations)) {
                        return Integer.MAX_VALUE;
                    }
                }
            }
            permutations *= LongMath.binomial(n, r);
            if (!isPositiveInt(permutations)) {
                return Integer.MAX_VALUE;
            }
            return (int)permutations;
        }
        
        @Override
        public int size() {
            return this.size;
        }
        
        @Override
        public boolean isEmpty() {
            return false;
        }
        
        @Override
        public Iterator<List<E>> iterator() {
            return (Iterator<List<E>>)new OrderedPermutationIterator((List<Object>)this.inputList, (Comparator<? super Object>)this.comparator);
        }
        
        @Override
        public boolean contains(@Nullable final Object obj) {
            if (obj instanceof List) {
                final List<?> list = (List<?>)obj;
                return isPermutation(this.inputList, list);
            }
            return false;
        }
        
        @Override
        public String toString() {
            return "orderedPermutationCollection(" + this.inputList + ")";
        }
    }
    
    private static final class OrderedPermutationIterator<E> extends AbstractIterator<List<E>>
    {
        List<E> nextPermutation;
        final Comparator<? super E> comparator;
        
        OrderedPermutationIterator(final List<E> list, final Comparator<? super E> comparator) {
            this.nextPermutation = (List<E>)Lists.newArrayList((Iterable<?>)list);
            this.comparator = comparator;
        }
        
        @Override
        protected List<E> computeNext() {
            if (this.nextPermutation == null) {
                return this.endOfData();
            }
            final ImmutableList<E> next = ImmutableList.copyOf((Collection<? extends E>)this.nextPermutation);
            this.calculateNextPermutation();
            return next;
        }
        
        void calculateNextPermutation() {
            final int j = this.findNextJ();
            if (j == -1) {
                this.nextPermutation = null;
                return;
            }
            final int l = this.findNextL(j);
            Collections.swap(this.nextPermutation, j, l);
            final int n = this.nextPermutation.size();
            Collections.reverse(this.nextPermutation.subList(j + 1, n));
        }
        
        int findNextJ() {
            for (int k = this.nextPermutation.size() - 2; k >= 0; --k) {
                if (this.comparator.compare((Object)this.nextPermutation.get(k), (Object)this.nextPermutation.get(k + 1)) < 0) {
                    return k;
                }
            }
            return -1;
        }
        
        int findNextL(final int j) {
            final E ak = this.nextPermutation.get(j);
            for (int l = this.nextPermutation.size() - 1; l > j; --l) {
                if (this.comparator.compare((Object)ak, (Object)this.nextPermutation.get(l)) < 0) {
                    return l;
                }
            }
            throw new AssertionError((Object)"this statement should be unreachable");
        }
    }
    
    private static final class PermutationCollection<E> extends AbstractCollection<List<E>>
    {
        final ImmutableList<E> inputList;
        
        PermutationCollection(final ImmutableList<E> input) {
            this.inputList = input;
        }
        
        @Override
        public int size() {
            return IntMath.factorial(this.inputList.size());
        }
        
        @Override
        public boolean isEmpty() {
            return false;
        }
        
        @Override
        public Iterator<List<E>> iterator() {
            return (Iterator<List<E>>)new PermutationIterator((List<Object>)this.inputList);
        }
        
        @Override
        public boolean contains(@Nullable final Object obj) {
            if (obj instanceof List) {
                final List<?> list = (List<?>)obj;
                return isPermutation(this.inputList, list);
            }
            return false;
        }
        
        @Override
        public String toString() {
            return "permutations(" + this.inputList + ")";
        }
    }
    
    private static class PermutationIterator<E> extends AbstractIterator<List<E>>
    {
        final List<E> list;
        final int[] c;
        final int[] o;
        int j;
        
        PermutationIterator(final List<E> list) {
            this.list = new ArrayList<E>((Collection<? extends E>)list);
            final int n = list.size();
            this.c = new int[n];
            this.o = new int[n];
            Arrays.fill(this.c, 0);
            Arrays.fill(this.o, 1);
            this.j = Integer.MAX_VALUE;
        }
        
        @Override
        protected List<E> computeNext() {
            if (this.j <= 0) {
                return this.endOfData();
            }
            final ImmutableList<E> next = ImmutableList.copyOf((Collection<? extends E>)this.list);
            this.calculateNextPermutation();
            return next;
        }
        
        void calculateNextPermutation() {
            this.j = this.list.size() - 1;
            int s = 0;
            if (this.j == -1) {
                return;
            }
            while (true) {
                final int q = this.c[this.j] + this.o[this.j];
                if (q < 0) {
                    this.switchDirection();
                }
                else {
                    if (q != this.j + 1) {
                        Collections.swap(this.list, this.j - this.c[this.j] + s, this.j - q + s);
                        this.c[this.j] = q;
                        break;
                    }
                    if (this.j == 0) {
                        break;
                    }
                    ++s;
                    this.switchDirection();
                }
            }
        }
        
        void switchDirection() {
            this.o[this.j] = -this.o[this.j];
            --this.j;
        }
    }
}
