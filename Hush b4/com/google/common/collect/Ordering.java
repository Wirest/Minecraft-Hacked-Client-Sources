// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.concurrent.atomic.AtomicInteger;
import com.google.common.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Nullable;
import java.util.Map;
import com.google.common.base.Function;
import java.util.List;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;
import java.util.Comparator;

@GwtCompatible
public abstract class Ordering<T> implements Comparator<T>
{
    static final int LEFT_IS_GREATER = 1;
    static final int RIGHT_IS_GREATER = -1;
    
    @GwtCompatible(serializable = true)
    public static <C extends Comparable> Ordering<C> natural() {
        return (Ordering<C>)NaturalOrdering.INSTANCE;
    }
    
    @GwtCompatible(serializable = true)
    public static <T> Ordering<T> from(final Comparator<T> comparator) {
        return (comparator instanceof Ordering) ? ((Ordering)comparator) : new ComparatorOrdering<T>(comparator);
    }
    
    @Deprecated
    @GwtCompatible(serializable = true)
    public static <T> Ordering<T> from(final Ordering<T> ordering) {
        return Preconditions.checkNotNull(ordering);
    }
    
    @GwtCompatible(serializable = true)
    public static <T> Ordering<T> explicit(final List<T> valuesInOrder) {
        return new ExplicitOrdering<T>(valuesInOrder);
    }
    
    @GwtCompatible(serializable = true)
    public static <T> Ordering<T> explicit(final T leastValue, final T... remainingValuesInOrder) {
        return explicit((List<T>)Lists.asList(leastValue, (T[])remainingValuesInOrder));
    }
    
    @GwtCompatible(serializable = true)
    public static Ordering<Object> allEqual() {
        return AllEqualOrdering.INSTANCE;
    }
    
    @GwtCompatible(serializable = true)
    public static Ordering<Object> usingToString() {
        return UsingToStringOrdering.INSTANCE;
    }
    
    public static Ordering<Object> arbitrary() {
        return ArbitraryOrderingHolder.ARBITRARY_ORDERING;
    }
    
    protected Ordering() {
    }
    
    @GwtCompatible(serializable = true)
    public <S extends T> Ordering<S> reverse() {
        return new ReverseOrdering<S>(this);
    }
    
    @GwtCompatible(serializable = true)
    public <S extends T> Ordering<S> nullsFirst() {
        return new NullsFirstOrdering<S>(this);
    }
    
    @GwtCompatible(serializable = true)
    public <S extends T> Ordering<S> nullsLast() {
        return new NullsLastOrdering<S>(this);
    }
    
    @GwtCompatible(serializable = true)
    public <F> Ordering<F> onResultOf(final Function<F, ? extends T> function) {
        return (Ordering<F>)new ByFunctionOrdering((Function<Object, ?>)function, (Ordering<Object>)this);
    }
    
     <T2 extends T> Ordering<Map.Entry<T2, ?>> onKeys() {
        return this.onResultOf((Function<Map.Entry<T2, ?>, ? extends T>)Maps.keyFunction());
    }
    
    @GwtCompatible(serializable = true)
    public <U extends T> Ordering<U> compound(final Comparator<? super U> secondaryComparator) {
        return new CompoundOrdering<U>(this, Preconditions.checkNotNull(secondaryComparator));
    }
    
    @GwtCompatible(serializable = true)
    public static <T> Ordering<T> compound(final Iterable<? extends Comparator<? super T>> comparators) {
        return new CompoundOrdering<T>(comparators);
    }
    
    @GwtCompatible(serializable = true)
    public <S extends T> Ordering<Iterable<S>> lexicographical() {
        return (Ordering<Iterable<S>>)new LexicographicalOrdering((Ordering<? super Object>)this);
    }
    
    @Override
    public abstract int compare(@Nullable final T p0, @Nullable final T p1);
    
    public <E extends T> E min(final Iterator<E> iterator) {
        E minSoFar = iterator.next();
        while (iterator.hasNext()) {
            minSoFar = this.min(minSoFar, iterator.next());
        }
        return minSoFar;
    }
    
    public <E extends T> E min(final Iterable<E> iterable) {
        return this.min(iterable.iterator());
    }
    
    public <E extends T> E min(@Nullable final E a, @Nullable final E b) {
        return (this.compare(a, b) <= 0) ? a : b;
    }
    
    public <E extends T> E min(@Nullable final E a, @Nullable final E b, @Nullable final E c, final E... rest) {
        E minSoFar = this.min(this.min(a, b), c);
        for (final E r : rest) {
            minSoFar = this.min(minSoFar, r);
        }
        return minSoFar;
    }
    
    public <E extends T> E max(final Iterator<E> iterator) {
        E maxSoFar = iterator.next();
        while (iterator.hasNext()) {
            maxSoFar = this.max(maxSoFar, iterator.next());
        }
        return maxSoFar;
    }
    
    public <E extends T> E max(final Iterable<E> iterable) {
        return this.max(iterable.iterator());
    }
    
    public <E extends T> E max(@Nullable final E a, @Nullable final E b) {
        return (this.compare(a, b) >= 0) ? a : b;
    }
    
    public <E extends T> E max(@Nullable final E a, @Nullable final E b, @Nullable final E c, final E... rest) {
        E maxSoFar = this.max(this.max(a, b), c);
        for (final E r : rest) {
            maxSoFar = this.max(maxSoFar, r);
        }
        return maxSoFar;
    }
    
    public <E extends T> List<E> leastOf(final Iterable<E> iterable, final int k) {
        if (iterable instanceof Collection) {
            final Collection<E> collection = (Collection<E>)(Collection)iterable;
            if (collection.size() <= 2L * k) {
                E[] array = (E[])collection.toArray();
                Arrays.sort(array, this);
                if (array.length > k) {
                    array = ObjectArrays.arraysCopyOf(array, k);
                }
                return Collections.unmodifiableList((List<? extends E>)Arrays.asList((T[])array));
            }
        }
        return this.leastOf(iterable.iterator(), k);
    }
    
    public <E extends T> List<E> leastOf(final Iterator<E> elements, final int k) {
        Preconditions.checkNotNull(elements);
        CollectPreconditions.checkNonnegative(k, "k");
        if (k == 0 || !elements.hasNext()) {
            return (List<E>)ImmutableList.of();
        }
        if (k >= 1073741823) {
            final ArrayList<E> list = Lists.newArrayList((Iterator<? extends E>)elements);
            Collections.sort(list, this);
            if (list.size() > k) {
                list.subList(k, list.size()).clear();
            }
            list.trimToSize();
            return Collections.unmodifiableList((List<? extends E>)list);
        }
        final int bufferCap = k * 2;
        final E[] buffer = (E[])new Object[bufferCap];
        E threshold = elements.next();
        buffer[0] = threshold;
        int bufferSize;
        E e;
        for (bufferSize = 1; bufferSize < k && elements.hasNext(); buffer[bufferSize++] = e, threshold = this.max(threshold, e)) {
            e = elements.next();
        }
        while (elements.hasNext()) {
            e = elements.next();
            if (this.compare(e, threshold) >= 0) {
                continue;
            }
            buffer[bufferSize++] = e;
            if (bufferSize != bufferCap) {
                continue;
            }
            int left = 0;
            int right = bufferCap - 1;
            int minThresholdPosition = 0;
            while (left < right) {
                final int pivotIndex = left + right + 1 >>> 1;
                final int pivotNewIndex = this.partition(buffer, left, right, pivotIndex);
                if (pivotNewIndex > k) {
                    right = pivotNewIndex - 1;
                }
                else {
                    if (pivotNewIndex >= k) {
                        break;
                    }
                    left = Math.max(pivotNewIndex, left + 1);
                    minThresholdPosition = pivotNewIndex;
                }
            }
            bufferSize = k;
            threshold = buffer[minThresholdPosition];
            for (int i = minThresholdPosition + 1; i < bufferSize; ++i) {
                threshold = this.max(threshold, buffer[i]);
            }
        }
        Arrays.sort(buffer, 0, bufferSize, this);
        bufferSize = Math.min(bufferSize, k);
        return Collections.unmodifiableList((List<? extends E>)Arrays.asList((T[])ObjectArrays.arraysCopyOf((T[])buffer, bufferSize)));
    }
    
    private <E extends T> int partition(final E[] values, final int left, final int right, final int pivotIndex) {
        final E pivotValue = values[pivotIndex];
        values[pivotIndex] = values[right];
        values[right] = pivotValue;
        int storeIndex = left;
        for (int i = left; i < right; ++i) {
            if (this.compare(values[i], pivotValue) < 0) {
                ObjectArrays.swap(values, storeIndex, i);
                ++storeIndex;
            }
        }
        ObjectArrays.swap(values, right, storeIndex);
        return storeIndex;
    }
    
    public <E extends T> List<E> greatestOf(final Iterable<E> iterable, final int k) {
        return (List<E>)this.reverse().leastOf((Iterable<Object>)iterable, k);
    }
    
    public <E extends T> List<E> greatestOf(final Iterator<E> iterator, final int k) {
        return (List<E>)this.reverse().leastOf((Iterator<Object>)iterator, k);
    }
    
    public <E extends T> List<E> sortedCopy(final Iterable<E> elements) {
        final E[] array = (E[])Iterables.toArray(elements);
        Arrays.sort(array, this);
        return (List<E>)Lists.newArrayList((Iterable<?>)Arrays.asList(array));
    }
    
    public <E extends T> ImmutableList<E> immutableSortedCopy(final Iterable<E> elements) {
        final Object[] arr$;
        final E[] array = (E[])(arr$ = Iterables.toArray(elements));
        for (final E e : arr$) {
            Preconditions.checkNotNull(e);
        }
        Arrays.sort(array, this);
        return ImmutableList.asImmutableList(array);
    }
    
    public boolean isOrdered(final Iterable<? extends T> iterable) {
        final Iterator<? extends T> it = iterable.iterator();
        if (it.hasNext()) {
            T prev = (T)it.next();
            while (it.hasNext()) {
                final T next = (T)it.next();
                if (this.compare(prev, next) > 0) {
                    return false;
                }
                prev = next;
            }
        }
        return true;
    }
    
    public boolean isStrictlyOrdered(final Iterable<? extends T> iterable) {
        final Iterator<? extends T> it = iterable.iterator();
        if (it.hasNext()) {
            T prev = (T)it.next();
            while (it.hasNext()) {
                final T next = (T)it.next();
                if (this.compare(prev, next) >= 0) {
                    return false;
                }
                prev = next;
            }
        }
        return true;
    }
    
    public int binarySearch(final List<? extends T> sortedList, @Nullable final T key) {
        return Collections.binarySearch(sortedList, key, this);
    }
    
    private static class ArbitraryOrderingHolder
    {
        static final Ordering<Object> ARBITRARY_ORDERING;
        
        static {
            ARBITRARY_ORDERING = new ArbitraryOrdering();
        }
    }
    
    @VisibleForTesting
    static class ArbitraryOrdering extends Ordering<Object>
    {
        private Map<Object, Integer> uids;
        
        ArbitraryOrdering() {
            this.uids = (Map<Object, Integer>)Platform.tryWeakKeys(new MapMaker()).makeComputingMap((Function<? super Object, ?>)new Function<Object, Integer>() {
                final AtomicInteger counter = new AtomicInteger(0);
                
                @Override
                public Integer apply(final Object from) {
                    return this.counter.getAndIncrement();
                }
            });
        }
        
        @Override
        public int compare(final Object left, final Object right) {
            if (left == right) {
                return 0;
            }
            if (left == null) {
                return -1;
            }
            if (right == null) {
                return 1;
            }
            final int leftCode = this.identityHashCode(left);
            final int rightCode = this.identityHashCode(right);
            if (leftCode != rightCode) {
                return (leftCode < rightCode) ? -1 : 1;
            }
            final int result = this.uids.get(left).compareTo(this.uids.get(right));
            if (result == 0) {
                throw new AssertionError();
            }
            return result;
        }
        
        @Override
        public String toString() {
            return "Ordering.arbitrary()";
        }
        
        int identityHashCode(final Object object) {
            return System.identityHashCode(object);
        }
    }
    
    @VisibleForTesting
    static class IncomparableValueException extends ClassCastException
    {
        final Object value;
        private static final long serialVersionUID = 0L;
        
        IncomparableValueException(final Object value) {
            super("Cannot compare value: " + value);
            this.value = value;
        }
    }
}
