// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.ListIterator;
import com.google.common.annotations.Beta;
import java.util.Comparator;
import java.util.Enumeration;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import java.util.Collections;
import java.util.Arrays;
import java.util.NoSuchElementException;
import com.google.common.annotations.GwtIncompatible;
import java.util.List;
import com.google.common.base.Objects;
import java.util.Collection;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import javax.annotation.Nullable;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class Iterators
{
    static final UnmodifiableListIterator<Object> EMPTY_LIST_ITERATOR;
    private static final Iterator<Object> EMPTY_MODIFIABLE_ITERATOR;
    
    private Iterators() {
    }
    
    public static <T> UnmodifiableIterator<T> emptyIterator() {
        return (UnmodifiableIterator<T>)emptyListIterator();
    }
    
    static <T> UnmodifiableListIterator<T> emptyListIterator() {
        return (UnmodifiableListIterator<T>)Iterators.EMPTY_LIST_ITERATOR;
    }
    
    static <T> Iterator<T> emptyModifiableIterator() {
        return (Iterator<T>)Iterators.EMPTY_MODIFIABLE_ITERATOR;
    }
    
    public static <T> UnmodifiableIterator<T> unmodifiableIterator(final Iterator<T> iterator) {
        Preconditions.checkNotNull(iterator);
        if (iterator instanceof UnmodifiableIterator) {
            return (UnmodifiableIterator<T>)(UnmodifiableIterator)iterator;
        }
        return new UnmodifiableIterator<T>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            
            @Override
            public T next() {
                return iterator.next();
            }
        };
    }
    
    @Deprecated
    public static <T> UnmodifiableIterator<T> unmodifiableIterator(final UnmodifiableIterator<T> iterator) {
        return Preconditions.checkNotNull(iterator);
    }
    
    public static int size(final Iterator<?> iterator) {
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            ++count;
        }
        return count;
    }
    
    public static boolean contains(final Iterator<?> iterator, @Nullable final Object element) {
        return any(iterator, Predicates.equalTo(element));
    }
    
    public static boolean removeAll(final Iterator<?> removeFrom, final Collection<?> elementsToRemove) {
        return removeIf(removeFrom, Predicates.in(elementsToRemove));
    }
    
    public static <T> boolean removeIf(final Iterator<T> removeFrom, final Predicate<? super T> predicate) {
        Preconditions.checkNotNull(predicate);
        boolean modified = false;
        while (removeFrom.hasNext()) {
            if (predicate.apply((Object)removeFrom.next())) {
                removeFrom.remove();
                modified = true;
            }
        }
        return modified;
    }
    
    public static boolean retainAll(final Iterator<?> removeFrom, final Collection<?> elementsToRetain) {
        return removeIf(removeFrom, Predicates.not(Predicates.in((Collection<? extends T>)elementsToRetain)));
    }
    
    public static boolean elementsEqual(final Iterator<?> iterator1, final Iterator<?> iterator2) {
        while (iterator1.hasNext()) {
            if (!iterator2.hasNext()) {
                return false;
            }
            final Object o1 = iterator1.next();
            final Object o2 = iterator2.next();
            if (!Objects.equal(o1, o2)) {
                return false;
            }
        }
        return !iterator2.hasNext();
    }
    
    public static String toString(final Iterator<?> iterator) {
        return Collections2.STANDARD_JOINER.appendTo(new StringBuilder().append('['), iterator).append(']').toString();
    }
    
    public static <T> T getOnlyElement(final Iterator<T> iterator) {
        final T first = iterator.next();
        if (!iterator.hasNext()) {
            return first;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("expected one element but was: <" + first);
        for (int i = 0; i < 4 && iterator.hasNext(); ++i) {
            sb.append(", " + iterator.next());
        }
        if (iterator.hasNext()) {
            sb.append(", ...");
        }
        sb.append('>');
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Nullable
    public static <T> T getOnlyElement(final Iterator<? extends T> iterator, @Nullable final T defaultValue) {
        return iterator.hasNext() ? getOnlyElement((Iterator<T>)iterator) : defaultValue;
    }
    
    @GwtIncompatible("Array.newInstance(Class, int)")
    public static <T> T[] toArray(final Iterator<? extends T> iterator, final Class<T> type) {
        final List<T> list = (List<T>)Lists.newArrayList((Iterator<?>)iterator);
        return Iterables.toArray((Iterable<? extends T>)list, type);
    }
    
    public static <T> boolean addAll(final Collection<T> addTo, final Iterator<? extends T> iterator) {
        Preconditions.checkNotNull(addTo);
        Preconditions.checkNotNull(iterator);
        boolean wasModified = false;
        while (iterator.hasNext()) {
            wasModified |= addTo.add((T)iterator.next());
        }
        return wasModified;
    }
    
    public static int frequency(final Iterator<?> iterator, @Nullable final Object element) {
        return size(filter(iterator, Predicates.equalTo(element)));
    }
    
    public static <T> Iterator<T> cycle(final Iterable<T> iterable) {
        Preconditions.checkNotNull(iterable);
        return new Iterator<T>() {
            Iterator<T> iterator = Iterators.emptyIterator();
            Iterator<T> removeFrom;
            
            @Override
            public boolean hasNext() {
                if (!this.iterator.hasNext()) {
                    this.iterator = iterable.iterator();
                }
                return this.iterator.hasNext();
            }
            
            @Override
            public T next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                this.removeFrom = this.iterator;
                return this.iterator.next();
            }
            
            @Override
            public void remove() {
                CollectPreconditions.checkRemove(this.removeFrom != null);
                this.removeFrom.remove();
                this.removeFrom = null;
            }
        };
    }
    
    public static <T> Iterator<T> cycle(final T... elements) {
        return cycle(Lists.newArrayList(elements));
    }
    
    public static <T> Iterator<T> concat(final Iterator<? extends T> a, final Iterator<? extends T> b) {
        return concat((Iterator<? extends Iterator<? extends T>>)ImmutableList.of(a, b).iterator());
    }
    
    public static <T> Iterator<T> concat(final Iterator<? extends T> a, final Iterator<? extends T> b, final Iterator<? extends T> c) {
        return concat((Iterator<? extends Iterator<? extends T>>)ImmutableList.of(a, b, c).iterator());
    }
    
    public static <T> Iterator<T> concat(final Iterator<? extends T> a, final Iterator<? extends T> b, final Iterator<? extends T> c, final Iterator<? extends T> d) {
        return concat((Iterator<? extends Iterator<? extends T>>)ImmutableList.of(a, b, c, d).iterator());
    }
    
    public static <T> Iterator<T> concat(final Iterator<? extends T>... inputs) {
        return concat((Iterator<? extends Iterator<? extends T>>)ImmutableList.copyOf(inputs).iterator());
    }
    
    public static <T> Iterator<T> concat(final Iterator<? extends Iterator<? extends T>> inputs) {
        Preconditions.checkNotNull(inputs);
        return new Iterator<T>() {
            Iterator<? extends T> current = Iterators.emptyIterator();
            Iterator<? extends T> removeFrom;
            
            @Override
            public boolean hasNext() {
                boolean currentHasNext;
                while (!(currentHasNext = Preconditions.checkNotNull(this.current).hasNext()) && inputs.hasNext()) {
                    this.current = inputs.next();
                }
                return currentHasNext;
            }
            
            @Override
            public T next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                this.removeFrom = this.current;
                return (T)this.current.next();
            }
            
            @Override
            public void remove() {
                CollectPreconditions.checkRemove(this.removeFrom != null);
                this.removeFrom.remove();
                this.removeFrom = null;
            }
        };
    }
    
    public static <T> UnmodifiableIterator<List<T>> partition(final Iterator<T> iterator, final int size) {
        return partitionImpl(iterator, size, false);
    }
    
    public static <T> UnmodifiableIterator<List<T>> paddedPartition(final Iterator<T> iterator, final int size) {
        return partitionImpl(iterator, size, true);
    }
    
    private static <T> UnmodifiableIterator<List<T>> partitionImpl(final Iterator<T> iterator, final int size, final boolean pad) {
        Preconditions.checkNotNull(iterator);
        Preconditions.checkArgument(size > 0);
        return new UnmodifiableIterator<List<T>>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            
            @Override
            public List<T> next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final Object[] array = new Object[size];
                int count;
                for (count = 0; count < size && iterator.hasNext(); ++count) {
                    array[count] = iterator.next();
                }
                for (int i = count; i < size; ++i) {
                    array[i] = null;
                }
                final List<T> list = Collections.unmodifiableList((List<? extends T>)Arrays.asList((T[])array));
                return (pad || count == size) ? list : list.subList(0, count);
            }
        };
    }
    
    public static <T> UnmodifiableIterator<T> filter(final Iterator<T> unfiltered, final Predicate<? super T> predicate) {
        Preconditions.checkNotNull(unfiltered);
        Preconditions.checkNotNull(predicate);
        return new AbstractIterator<T>() {
            @Override
            protected T computeNext() {
                while (unfiltered.hasNext()) {
                    final T element = unfiltered.next();
                    if (predicate.apply(element)) {
                        return element;
                    }
                }
                return this.endOfData();
            }
        };
    }
    
    @GwtIncompatible("Class.isInstance")
    public static <T> UnmodifiableIterator<T> filter(final Iterator<?> unfiltered, final Class<T> type) {
        return filter(unfiltered, Predicates.instanceOf(type));
    }
    
    public static <T> boolean any(final Iterator<T> iterator, final Predicate<? super T> predicate) {
        return indexOf(iterator, predicate) != -1;
    }
    
    public static <T> boolean all(final Iterator<T> iterator, final Predicate<? super T> predicate) {
        Preconditions.checkNotNull(predicate);
        while (iterator.hasNext()) {
            final T element = iterator.next();
            if (!predicate.apply((Object)element)) {
                return false;
            }
        }
        return true;
    }
    
    public static <T> T find(final Iterator<T> iterator, final Predicate<? super T> predicate) {
        return filter(iterator, predicate).next();
    }
    
    @Nullable
    public static <T> T find(final Iterator<? extends T> iterator, final Predicate<? super T> predicate, @Nullable final T defaultValue) {
        return getNext(filter(iterator, predicate), defaultValue);
    }
    
    public static <T> Optional<T> tryFind(final Iterator<T> iterator, final Predicate<? super T> predicate) {
        final UnmodifiableIterator<T> filteredIterator = filter(iterator, predicate);
        return filteredIterator.hasNext() ? Optional.of(filteredIterator.next()) : Optional.absent();
    }
    
    public static <T> int indexOf(final Iterator<T> iterator, final Predicate<? super T> predicate) {
        Preconditions.checkNotNull(predicate, (Object)"predicate");
        int i = 0;
        while (iterator.hasNext()) {
            final T current = iterator.next();
            if (predicate.apply((Object)current)) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    public static <F, T> Iterator<T> transform(final Iterator<F> fromIterator, final Function<? super F, ? extends T> function) {
        Preconditions.checkNotNull(function);
        return new TransformedIterator<F, T>(fromIterator) {
            @Override
            T transform(final F from) {
                return function.apply(from);
            }
        };
    }
    
    public static <T> T get(final Iterator<T> iterator, final int position) {
        checkNonnegative(position);
        final int skipped = advance(iterator, position);
        if (!iterator.hasNext()) {
            throw new IndexOutOfBoundsException("position (" + position + ") must be less than the number of elements that remained (" + skipped + ")");
        }
        return iterator.next();
    }
    
    static void checkNonnegative(final int position) {
        if (position < 0) {
            throw new IndexOutOfBoundsException("position (" + position + ") must not be negative");
        }
    }
    
    @Nullable
    public static <T> T get(final Iterator<? extends T> iterator, final int position, @Nullable final T defaultValue) {
        checkNonnegative(position);
        advance(iterator, position);
        return getNext(iterator, defaultValue);
    }
    
    @Nullable
    public static <T> T getNext(final Iterator<? extends T> iterator, @Nullable final T defaultValue) {
        return iterator.hasNext() ? iterator.next() : defaultValue;
    }
    
    public static <T> T getLast(final Iterator<T> iterator) {
        T current;
        do {
            current = iterator.next();
        } while (iterator.hasNext());
        return current;
    }
    
    @Nullable
    public static <T> T getLast(final Iterator<? extends T> iterator, @Nullable final T defaultValue) {
        return iterator.hasNext() ? getLast((Iterator<T>)iterator) : defaultValue;
    }
    
    public static int advance(final Iterator<?> iterator, final int numberToAdvance) {
        Preconditions.checkNotNull(iterator);
        Preconditions.checkArgument(numberToAdvance >= 0, (Object)"numberToAdvance must be nonnegative");
        int i;
        for (i = 0; i < numberToAdvance && iterator.hasNext(); ++i) {
            iterator.next();
        }
        return i;
    }
    
    public static <T> Iterator<T> limit(final Iterator<T> iterator, final int limitSize) {
        Preconditions.checkNotNull(iterator);
        Preconditions.checkArgument(limitSize >= 0, (Object)"limit is negative");
        return new Iterator<T>() {
            private int count;
            
            @Override
            public boolean hasNext() {
                return this.count < limitSize && iterator.hasNext();
            }
            
            @Override
            public T next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                ++this.count;
                return iterator.next();
            }
            
            @Override
            public void remove() {
                iterator.remove();
            }
        };
    }
    
    public static <T> Iterator<T> consumingIterator(final Iterator<T> iterator) {
        Preconditions.checkNotNull(iterator);
        return new UnmodifiableIterator<T>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            
            @Override
            public T next() {
                final T next = iterator.next();
                iterator.remove();
                return next;
            }
            
            @Override
            public String toString() {
                return "Iterators.consumingIterator(...)";
            }
        };
    }
    
    @Nullable
    static <T> T pollNext(final Iterator<T> iterator) {
        if (iterator.hasNext()) {
            final T result = iterator.next();
            iterator.remove();
            return result;
        }
        return null;
    }
    
    static void clear(final Iterator<?> iterator) {
        Preconditions.checkNotNull(iterator);
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }
    
    public static <T> UnmodifiableIterator<T> forArray(final T... array) {
        return forArray(array, 0, array.length, 0);
    }
    
    static <T> UnmodifiableListIterator<T> forArray(final T[] array, final int offset, final int length, final int index) {
        Preconditions.checkArgument(length >= 0);
        final int end = offset + length;
        Preconditions.checkPositionIndexes(offset, end, array.length);
        Preconditions.checkPositionIndex(index, length);
        if (length == 0) {
            return emptyListIterator();
        }
        return new AbstractIndexedListIterator<T>(length, index) {
            @Override
            protected T get(final int index) {
                return array[offset + index];
            }
        };
    }
    
    public static <T> UnmodifiableIterator<T> singletonIterator(@Nullable final T value) {
        return new UnmodifiableIterator<T>() {
            boolean done;
            
            @Override
            public boolean hasNext() {
                return !this.done;
            }
            
            @Override
            public T next() {
                if (this.done) {
                    throw new NoSuchElementException();
                }
                this.done = true;
                return value;
            }
        };
    }
    
    public static <T> UnmodifiableIterator<T> forEnumeration(final Enumeration<T> enumeration) {
        Preconditions.checkNotNull(enumeration);
        return new UnmodifiableIterator<T>() {
            @Override
            public boolean hasNext() {
                return enumeration.hasMoreElements();
            }
            
            @Override
            public T next() {
                return enumeration.nextElement();
            }
        };
    }
    
    public static <T> Enumeration<T> asEnumeration(final Iterator<T> iterator) {
        Preconditions.checkNotNull(iterator);
        return new Enumeration<T>() {
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }
            
            @Override
            public T nextElement() {
                return iterator.next();
            }
        };
    }
    
    public static <T> PeekingIterator<T> peekingIterator(final Iterator<? extends T> iterator) {
        if (iterator instanceof PeekingImpl) {
            final PeekingImpl<T> peeking = (PeekingImpl<T>)(PeekingImpl)iterator;
            return peeking;
        }
        return new PeekingImpl<T>(iterator);
    }
    
    @Deprecated
    public static <T> PeekingIterator<T> peekingIterator(final PeekingIterator<T> iterator) {
        return Preconditions.checkNotNull(iterator);
    }
    
    @Beta
    public static <T> UnmodifiableIterator<T> mergeSorted(final Iterable<? extends Iterator<? extends T>> iterators, final Comparator<? super T> comparator) {
        Preconditions.checkNotNull(iterators, (Object)"iterators");
        Preconditions.checkNotNull(comparator, (Object)"comparator");
        return new MergingIterator<T>(iterators, comparator);
    }
    
    static <T> ListIterator<T> cast(final Iterator<T> iterator) {
        return (ListIterator<T>)(ListIterator)iterator;
    }
    
    static {
        EMPTY_LIST_ITERATOR = new UnmodifiableListIterator<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }
            
            @Override
            public Object next() {
                throw new NoSuchElementException();
            }
            
            @Override
            public boolean hasPrevious() {
                return false;
            }
            
            @Override
            public Object previous() {
                throw new NoSuchElementException();
            }
            
            @Override
            public int nextIndex() {
                return 0;
            }
            
            @Override
            public int previousIndex() {
                return -1;
            }
        };
        EMPTY_MODIFIABLE_ITERATOR = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }
            
            @Override
            public Object next() {
                throw new NoSuchElementException();
            }
            
            @Override
            public void remove() {
                CollectPreconditions.checkRemove(false);
            }
        };
    }
    
    private static class PeekingImpl<E> implements PeekingIterator<E>
    {
        private final Iterator<? extends E> iterator;
        private boolean hasPeeked;
        private E peekedElement;
        
        public PeekingImpl(final Iterator<? extends E> iterator) {
            this.iterator = Preconditions.checkNotNull(iterator);
        }
        
        @Override
        public boolean hasNext() {
            return this.hasPeeked || this.iterator.hasNext();
        }
        
        @Override
        public E next() {
            if (!this.hasPeeked) {
                return (E)this.iterator.next();
            }
            final E result = this.peekedElement;
            this.hasPeeked = false;
            this.peekedElement = null;
            return result;
        }
        
        @Override
        public void remove() {
            Preconditions.checkState(!this.hasPeeked, (Object)"Can't remove after you've peeked at next");
            this.iterator.remove();
        }
        
        @Override
        public E peek() {
            if (!this.hasPeeked) {
                this.peekedElement = (E)this.iterator.next();
                this.hasPeeked = true;
            }
            return this.peekedElement;
        }
    }
    
    private static class MergingIterator<T> extends UnmodifiableIterator<T>
    {
        final Queue<PeekingIterator<T>> queue;
        
        public MergingIterator(final Iterable<? extends Iterator<? extends T>> iterators, final Comparator<? super T> itemComparator) {
            final Comparator<PeekingIterator<T>> heapComparator = new Comparator<PeekingIterator<T>>() {
                @Override
                public int compare(final PeekingIterator<T> o1, final PeekingIterator<T> o2) {
                    return itemComparator.compare(o1.peek(), o2.peek());
                }
            };
            this.queue = new PriorityQueue<PeekingIterator<T>>(2, heapComparator);
            for (final Iterator<? extends T> iterator : iterators) {
                if (iterator.hasNext()) {
                    this.queue.add(Iterators.peekingIterator(iterator));
                }
            }
        }
        
        @Override
        public boolean hasNext() {
            return !this.queue.isEmpty();
        }
        
        @Override
        public T next() {
            final PeekingIterator<T> nextIter = this.queue.remove();
            final T next = nextIter.next();
            if (nextIter.hasNext()) {
                this.queue.add(nextIter);
            }
            return next;
        }
    }
}
