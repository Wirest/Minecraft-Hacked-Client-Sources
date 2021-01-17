// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.NoSuchElementException;
import com.google.common.math.IntMath;
import java.math.RoundingMode;
import java.util.AbstractSequentialList;
import java.io.Serializable;
import java.util.AbstractList;
import com.google.common.base.Objects;
import java.util.ListIterator;
import com.google.common.annotations.Beta;
import java.util.RandomAccess;
import com.google.common.base.Function;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtIncompatible;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.primitives.Ints;
import java.util.Collection;
import java.util.Collections;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class Lists
{
    private Lists() {
    }
    
    @GwtCompatible(serializable = true)
    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<E>();
    }
    
    @GwtCompatible(serializable = true)
    public static <E> ArrayList<E> newArrayList(final E... elements) {
        Preconditions.checkNotNull(elements);
        final int capacity = computeArrayListCapacity(elements.length);
        final ArrayList<E> list = new ArrayList<E>(capacity);
        Collections.addAll(list, elements);
        return list;
    }
    
    @VisibleForTesting
    static int computeArrayListCapacity(final int arraySize) {
        CollectPreconditions.checkNonnegative(arraySize, "arraySize");
        return Ints.saturatedCast(5L + arraySize + arraySize / 10);
    }
    
    @GwtCompatible(serializable = true)
    public static <E> ArrayList<E> newArrayList(final Iterable<? extends E> elements) {
        Preconditions.checkNotNull(elements);
        return (elements instanceof Collection) ? new ArrayList<E>(Collections2.cast(elements)) : newArrayList(elements.iterator());
    }
    
    @GwtCompatible(serializable = true)
    public static <E> ArrayList<E> newArrayList(final Iterator<? extends E> elements) {
        final ArrayList<E> list = newArrayList();
        Iterators.addAll(list, elements);
        return list;
    }
    
    @GwtCompatible(serializable = true)
    public static <E> ArrayList<E> newArrayListWithCapacity(final int initialArraySize) {
        CollectPreconditions.checkNonnegative(initialArraySize, "initialArraySize");
        return new ArrayList<E>(initialArraySize);
    }
    
    @GwtCompatible(serializable = true)
    public static <E> ArrayList<E> newArrayListWithExpectedSize(final int estimatedSize) {
        return new ArrayList<E>(computeArrayListCapacity(estimatedSize));
    }
    
    @GwtCompatible(serializable = true)
    public static <E> LinkedList<E> newLinkedList() {
        return new LinkedList<E>();
    }
    
    @GwtCompatible(serializable = true)
    public static <E> LinkedList<E> newLinkedList(final Iterable<? extends E> elements) {
        final LinkedList<E> list = newLinkedList();
        Iterables.addAll(list, elements);
        return list;
    }
    
    @GwtIncompatible("CopyOnWriteArrayList")
    public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList() {
        return new CopyOnWriteArrayList<E>();
    }
    
    @GwtIncompatible("CopyOnWriteArrayList")
    public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList(final Iterable<? extends E> elements) {
        final Collection<? extends E> elementsCollection = (Collection<? extends E>)((elements instanceof Collection) ? Collections2.cast(elements) : newArrayList((Iterable<?>)elements));
        return new CopyOnWriteArrayList<E>(elementsCollection);
    }
    
    public static <E> List<E> asList(@Nullable final E first, final E[] rest) {
        return new OnePlusArrayList<E>(first, rest);
    }
    
    public static <E> List<E> asList(@Nullable final E first, @Nullable final E second, final E[] rest) {
        return new TwoPlusArrayList<E>(first, second, rest);
    }
    
    static <B> List<List<B>> cartesianProduct(final List<? extends List<? extends B>> lists) {
        return CartesianList.create(lists);
    }
    
    static <B> List<List<B>> cartesianProduct(final List<? extends B>... lists) {
        return cartesianProduct((List<? extends List<? extends B>>)Arrays.asList((List<? extends B>[])lists));
    }
    
    public static <F, T> List<T> transform(final List<F> fromList, final Function<? super F, ? extends T> function) {
        return (List<T>)((fromList instanceof RandomAccess) ? new TransformingRandomAccessList<Object, Object>(fromList, function) : new TransformingSequentialList<Object, Object>(fromList, function));
    }
    
    public static <T> List<List<T>> partition(final List<T> list, final int size) {
        Preconditions.checkNotNull(list);
        Preconditions.checkArgument(size > 0);
        return (List<List<T>>)((list instanceof RandomAccess) ? new RandomAccessPartition<Object>(list, size) : new Partition(list, size));
    }
    
    @Beta
    public static ImmutableList<Character> charactersOf(final String string) {
        return new StringAsImmutableList(Preconditions.checkNotNull(string));
    }
    
    @Beta
    public static List<Character> charactersOf(final CharSequence sequence) {
        return new CharSequenceAsList(Preconditions.checkNotNull(sequence));
    }
    
    public static <T> List<T> reverse(final List<T> list) {
        if (list instanceof ImmutableList) {
            return (List<T>)((ImmutableList)list).reverse();
        }
        if (list instanceof ReverseList) {
            return ((ReverseList)list).getForwardList();
        }
        if (list instanceof RandomAccess) {
            return new RandomAccessReverseList<T>(list);
        }
        return new ReverseList<T>(list);
    }
    
    static int hashCodeImpl(final List<?> list) {
        int hashCode = 1;
        for (final Object o : list) {
            hashCode = 31 * hashCode + ((o == null) ? 0 : o.hashCode());
            hashCode = ~(~hashCode);
        }
        return hashCode;
    }
    
    static boolean equalsImpl(final List<?> list, @Nullable final Object object) {
        if (object == Preconditions.checkNotNull(list)) {
            return true;
        }
        if (!(object instanceof List)) {
            return false;
        }
        final List<?> o = (List<?>)object;
        return list.size() == o.size() && Iterators.elementsEqual(list.iterator(), o.iterator());
    }
    
    static <E> boolean addAllImpl(final List<E> list, final int index, final Iterable<? extends E> elements) {
        boolean changed = false;
        final ListIterator<E> listIterator = list.listIterator(index);
        for (final E e : elements) {
            listIterator.add(e);
            changed = true;
        }
        return changed;
    }
    
    static int indexOfImpl(final List<?> list, @Nullable final Object element) {
        final ListIterator<?> listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            if (Objects.equal(element, listIterator.next())) {
                return listIterator.previousIndex();
            }
        }
        return -1;
    }
    
    static int lastIndexOfImpl(final List<?> list, @Nullable final Object element) {
        final ListIterator<?> listIterator = list.listIterator(list.size());
        while (listIterator.hasPrevious()) {
            if (Objects.equal(element, listIterator.previous())) {
                return listIterator.nextIndex();
            }
        }
        return -1;
    }
    
    static <E> ListIterator<E> listIteratorImpl(final List<E> list, final int index) {
        return new AbstractListWrapper<E>(list).listIterator(index);
    }
    
    static <E> List<E> subListImpl(final List<E> list, final int fromIndex, final int toIndex) {
        List<E> wrapper;
        if (list instanceof RandomAccess) {
            wrapper = new RandomAccessListWrapper<E>(list) {
                private static final long serialVersionUID = 0L;
                
                @Override
                public ListIterator<E> listIterator(final int index) {
                    return (ListIterator<E>)this.backingList.listIterator(index);
                }
            };
        }
        else {
            wrapper = new AbstractListWrapper<E>(list) {
                private static final long serialVersionUID = 0L;
                
                @Override
                public ListIterator<E> listIterator(final int index) {
                    return (ListIterator<E>)this.backingList.listIterator(index);
                }
            };
        }
        return wrapper.subList(fromIndex, toIndex);
    }
    
    static <T> List<T> cast(final Iterable<T> iterable) {
        return (List<T>)(List)iterable;
    }
    
    private static class OnePlusArrayList<E> extends AbstractList<E> implements Serializable, RandomAccess
    {
        final E first;
        final E[] rest;
        private static final long serialVersionUID = 0L;
        
        OnePlusArrayList(@Nullable final E first, final E[] rest) {
            this.first = first;
            this.rest = Preconditions.checkNotNull(rest);
        }
        
        @Override
        public int size() {
            return this.rest.length + 1;
        }
        
        @Override
        public E get(final int index) {
            Preconditions.checkElementIndex(index, this.size());
            return (index == 0) ? this.first : this.rest[index - 1];
        }
    }
    
    private static class TwoPlusArrayList<E> extends AbstractList<E> implements Serializable, RandomAccess
    {
        final E first;
        final E second;
        final E[] rest;
        private static final long serialVersionUID = 0L;
        
        TwoPlusArrayList(@Nullable final E first, @Nullable final E second, final E[] rest) {
            this.first = first;
            this.second = second;
            this.rest = Preconditions.checkNotNull(rest);
        }
        
        @Override
        public int size() {
            return this.rest.length + 2;
        }
        
        @Override
        public E get(final int index) {
            switch (index) {
                case 0: {
                    return this.first;
                }
                case 1: {
                    return this.second;
                }
                default: {
                    Preconditions.checkElementIndex(index, this.size());
                    return this.rest[index - 2];
                }
            }
        }
    }
    
    private static class TransformingSequentialList<F, T> extends AbstractSequentialList<T> implements Serializable
    {
        final List<F> fromList;
        final Function<? super F, ? extends T> function;
        private static final long serialVersionUID = 0L;
        
        TransformingSequentialList(final List<F> fromList, final Function<? super F, ? extends T> function) {
            this.fromList = Preconditions.checkNotNull(fromList);
            this.function = Preconditions.checkNotNull(function);
        }
        
        @Override
        public void clear() {
            this.fromList.clear();
        }
        
        @Override
        public int size() {
            return this.fromList.size();
        }
        
        @Override
        public ListIterator<T> listIterator(final int index) {
            return new TransformedListIterator<F, T>(this.fromList.listIterator(index)) {
                @Override
                T transform(final F from) {
                    return (T)TransformingSequentialList.this.function.apply((Object)from);
                }
            };
        }
    }
    
    private static class TransformingRandomAccessList<F, T> extends AbstractList<T> implements RandomAccess, Serializable
    {
        final List<F> fromList;
        final Function<? super F, ? extends T> function;
        private static final long serialVersionUID = 0L;
        
        TransformingRandomAccessList(final List<F> fromList, final Function<? super F, ? extends T> function) {
            this.fromList = Preconditions.checkNotNull(fromList);
            this.function = Preconditions.checkNotNull(function);
        }
        
        @Override
        public void clear() {
            this.fromList.clear();
        }
        
        @Override
        public T get(final int index) {
            return (T)this.function.apply((Object)this.fromList.get(index));
        }
        
        @Override
        public Iterator<T> iterator() {
            return this.listIterator();
        }
        
        @Override
        public ListIterator<T> listIterator(final int index) {
            return new TransformedListIterator<F, T>(this.fromList.listIterator(index)) {
                @Override
                T transform(final F from) {
                    return (T)TransformingRandomAccessList.this.function.apply((Object)from);
                }
            };
        }
        
        @Override
        public boolean isEmpty() {
            return this.fromList.isEmpty();
        }
        
        @Override
        public T remove(final int index) {
            return (T)this.function.apply((Object)this.fromList.remove(index));
        }
        
        @Override
        public int size() {
            return this.fromList.size();
        }
    }
    
    private static class Partition<T> extends AbstractList<List<T>>
    {
        final List<T> list;
        final int size;
        
        Partition(final List<T> list, final int size) {
            this.list = list;
            this.size = size;
        }
        
        @Override
        public List<T> get(final int index) {
            Preconditions.checkElementIndex(index, this.size());
            final int start = index * this.size;
            final int end = Math.min(start + this.size, this.list.size());
            return this.list.subList(start, end);
        }
        
        @Override
        public int size() {
            return IntMath.divide(this.list.size(), this.size, RoundingMode.CEILING);
        }
        
        @Override
        public boolean isEmpty() {
            return this.list.isEmpty();
        }
    }
    
    private static class RandomAccessPartition<T> extends Partition<T> implements RandomAccess
    {
        RandomAccessPartition(final List<T> list, final int size) {
            super(list, size);
        }
    }
    
    private static final class StringAsImmutableList extends ImmutableList<Character>
    {
        private final String string;
        
        StringAsImmutableList(final String string) {
            this.string = string;
        }
        
        @Override
        public int indexOf(@Nullable final Object object) {
            return (object instanceof Character) ? this.string.indexOf((char)object) : -1;
        }
        
        @Override
        public int lastIndexOf(@Nullable final Object object) {
            return (object instanceof Character) ? this.string.lastIndexOf((char)object) : -1;
        }
        
        @Override
        public ImmutableList<Character> subList(final int fromIndex, final int toIndex) {
            Preconditions.checkPositionIndexes(fromIndex, toIndex, this.size());
            return Lists.charactersOf(this.string.substring(fromIndex, toIndex));
        }
        
        @Override
        boolean isPartialView() {
            return false;
        }
        
        @Override
        public Character get(final int index) {
            Preconditions.checkElementIndex(index, this.size());
            return this.string.charAt(index);
        }
        
        @Override
        public int size() {
            return this.string.length();
        }
    }
    
    private static final class CharSequenceAsList extends AbstractList<Character>
    {
        private final CharSequence sequence;
        
        CharSequenceAsList(final CharSequence sequence) {
            this.sequence = sequence;
        }
        
        @Override
        public Character get(final int index) {
            Preconditions.checkElementIndex(index, this.size());
            return this.sequence.charAt(index);
        }
        
        @Override
        public int size() {
            return this.sequence.length();
        }
    }
    
    private static class ReverseList<T> extends AbstractList<T>
    {
        private final List<T> forwardList;
        
        ReverseList(final List<T> forwardList) {
            this.forwardList = Preconditions.checkNotNull(forwardList);
        }
        
        List<T> getForwardList() {
            return this.forwardList;
        }
        
        private int reverseIndex(final int index) {
            final int size = this.size();
            Preconditions.checkElementIndex(index, size);
            return size - 1 - index;
        }
        
        private int reversePosition(final int index) {
            final int size = this.size();
            Preconditions.checkPositionIndex(index, size);
            return size - index;
        }
        
        @Override
        public void add(final int index, @Nullable final T element) {
            this.forwardList.add(this.reversePosition(index), element);
        }
        
        @Override
        public void clear() {
            this.forwardList.clear();
        }
        
        @Override
        public T remove(final int index) {
            return this.forwardList.remove(this.reverseIndex(index));
        }
        
        @Override
        protected void removeRange(final int fromIndex, final int toIndex) {
            this.subList(fromIndex, toIndex).clear();
        }
        
        @Override
        public T set(final int index, @Nullable final T element) {
            return this.forwardList.set(this.reverseIndex(index), element);
        }
        
        @Override
        public T get(final int index) {
            return this.forwardList.get(this.reverseIndex(index));
        }
        
        @Override
        public int size() {
            return this.forwardList.size();
        }
        
        @Override
        public List<T> subList(final int fromIndex, final int toIndex) {
            Preconditions.checkPositionIndexes(fromIndex, toIndex, this.size());
            return Lists.reverse(this.forwardList.subList(this.reversePosition(toIndex), this.reversePosition(fromIndex)));
        }
        
        @Override
        public Iterator<T> iterator() {
            return this.listIterator();
        }
        
        @Override
        public ListIterator<T> listIterator(final int index) {
            final int start = this.reversePosition(index);
            final ListIterator<T> forwardIterator = this.forwardList.listIterator(start);
            return new ListIterator<T>() {
                boolean canRemoveOrSet;
                
                @Override
                public void add(final T e) {
                    forwardIterator.add(e);
                    forwardIterator.previous();
                    this.canRemoveOrSet = false;
                }
                
                @Override
                public boolean hasNext() {
                    return forwardIterator.hasPrevious();
                }
                
                @Override
                public boolean hasPrevious() {
                    return forwardIterator.hasNext();
                }
                
                @Override
                public T next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    this.canRemoveOrSet = true;
                    return forwardIterator.previous();
                }
                
                @Override
                public int nextIndex() {
                    return ReverseList.this.reversePosition(forwardIterator.nextIndex());
                }
                
                @Override
                public T previous() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    this.canRemoveOrSet = true;
                    return forwardIterator.next();
                }
                
                @Override
                public int previousIndex() {
                    return this.nextIndex() - 1;
                }
                
                @Override
                public void remove() {
                    CollectPreconditions.checkRemove(this.canRemoveOrSet);
                    forwardIterator.remove();
                    this.canRemoveOrSet = false;
                }
                
                @Override
                public void set(final T e) {
                    Preconditions.checkState(this.canRemoveOrSet);
                    forwardIterator.set(e);
                }
            };
        }
    }
    
    private static class RandomAccessReverseList<T> extends ReverseList<T> implements RandomAccess
    {
        RandomAccessReverseList(final List<T> forwardList) {
            super(forwardList);
        }
    }
    
    private static class AbstractListWrapper<E> extends AbstractList<E>
    {
        final List<E> backingList;
        
        AbstractListWrapper(final List<E> backingList) {
            this.backingList = Preconditions.checkNotNull(backingList);
        }
        
        @Override
        public void add(final int index, final E element) {
            this.backingList.add(index, element);
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends E> c) {
            return this.backingList.addAll(index, c);
        }
        
        @Override
        public E get(final int index) {
            return this.backingList.get(index);
        }
        
        @Override
        public E remove(final int index) {
            return this.backingList.remove(index);
        }
        
        @Override
        public E set(final int index, final E element) {
            return this.backingList.set(index, element);
        }
        
        @Override
        public boolean contains(final Object o) {
            return this.backingList.contains(o);
        }
        
        @Override
        public int size() {
            return this.backingList.size();
        }
    }
    
    private static class RandomAccessListWrapper<E> extends AbstractListWrapper<E> implements RandomAccess
    {
        RandomAccessListWrapper(final List<E> backingList) {
            super(backingList);
        }
    }
}
