// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.ConcurrentModificationException;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.Collections;
import com.google.common.math.IntMath;
import java.util.NoSuchElementException;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.Collection;
import java.util.Comparator;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.annotations.Beta;
import java.util.AbstractQueue;

@Beta
public final class MinMaxPriorityQueue<E> extends AbstractQueue<E>
{
    private final Heap minHeap;
    private final Heap maxHeap;
    @VisibleForTesting
    final int maximumSize;
    private Object[] queue;
    private int size;
    private int modCount;
    private static final int EVEN_POWERS_OF_TWO = 1431655765;
    private static final int ODD_POWERS_OF_TWO = -1431655766;
    private static final int DEFAULT_CAPACITY = 11;
    
    public static <E extends Comparable<E>> MinMaxPriorityQueue<E> create() {
        return (MinMaxPriorityQueue<E>)new Builder((Comparator)Ordering.natural()).create();
    }
    
    public static <E extends Comparable<E>> MinMaxPriorityQueue<E> create(final Iterable<? extends E> initialContents) {
        return (MinMaxPriorityQueue<E>)new Builder((Comparator)Ordering.natural()).create((Iterable)initialContents);
    }
    
    public static <B> Builder<B> orderedBy(final Comparator<B> comparator) {
        return new Builder<B>((Comparator)comparator);
    }
    
    public static Builder<Comparable> expectedSize(final int expectedSize) {
        return (Builder<Comparable>)new Builder<Comparable>((Comparator)Ordering.natural()).expectedSize(expectedSize);
    }
    
    public static Builder<Comparable> maximumSize(final int maximumSize) {
        return (Builder<Comparable>)new Builder<Comparable>((Comparator)Ordering.natural()).maximumSize(maximumSize);
    }
    
    private MinMaxPriorityQueue(final Builder<? super E> builder, final int queueSize) {
        final Ordering<E> ordering = (Ordering<E>)((Builder<Object>)builder).ordering();
        this.minHeap = new Heap(ordering);
        this.maxHeap = new Heap(ordering.reverse());
        this.minHeap.otherHeap = this.maxHeap;
        this.maxHeap.otherHeap = this.minHeap;
        this.maximumSize = ((Builder<Object>)builder).maximumSize;
        this.queue = new Object[queueSize];
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean add(final E element) {
        this.offer(element);
        return true;
    }
    
    @Override
    public boolean addAll(final Collection<? extends E> newElements) {
        boolean modified = false;
        for (final E element : newElements) {
            this.offer(element);
            modified = true;
        }
        return modified;
    }
    
    @Override
    public boolean offer(final E element) {
        Preconditions.checkNotNull(element);
        ++this.modCount;
        final int insertIndex = this.size++;
        this.growIfNeeded();
        this.heapForIndex(insertIndex).bubbleUp(insertIndex, element);
        return this.size <= this.maximumSize || this.pollLast() != element;
    }
    
    @Override
    public E poll() {
        return this.isEmpty() ? null : this.removeAndGet(0);
    }
    
    E elementData(final int index) {
        return (E)this.queue[index];
    }
    
    @Override
    public E peek() {
        return this.isEmpty() ? null : this.elementData(0);
    }
    
    private int getMaxElementIndex() {
        switch (this.size) {
            case 1: {
                return 0;
            }
            case 2: {
                return 1;
            }
            default: {
                return (this.maxHeap.compareElements(1, 2) <= 0) ? 1 : 2;
            }
        }
    }
    
    public E pollFirst() {
        return this.poll();
    }
    
    public E removeFirst() {
        return this.remove();
    }
    
    public E peekFirst() {
        return this.peek();
    }
    
    public E pollLast() {
        return this.isEmpty() ? null : this.removeAndGet(this.getMaxElementIndex());
    }
    
    public E removeLast() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.removeAndGet(this.getMaxElementIndex());
    }
    
    public E peekLast() {
        return this.isEmpty() ? null : this.elementData(this.getMaxElementIndex());
    }
    
    @VisibleForTesting
    MoveDesc<E> removeAt(final int index) {
        Preconditions.checkPositionIndex(index, this.size);
        ++this.modCount;
        --this.size;
        if (this.size == index) {
            this.queue[this.size] = null;
            return null;
        }
        final E actualLastElement = this.elementData(this.size);
        final int lastElementAt = this.heapForIndex(this.size).getCorrectLastElement(actualLastElement);
        final E toTrickle = this.elementData(this.size);
        this.queue[this.size] = null;
        final MoveDesc<E> changes = this.fillHole(index, toTrickle);
        if (lastElementAt >= index) {
            return changes;
        }
        if (changes == null) {
            return new MoveDesc<E>(actualLastElement, toTrickle);
        }
        return new MoveDesc<E>(actualLastElement, changes.replaced);
    }
    
    private MoveDesc<E> fillHole(final int index, final E toTrickle) {
        final Heap heap = this.heapForIndex(index);
        final int vacated = heap.fillHoleAt(index);
        final int bubbledTo = heap.bubbleUpAlternatingLevels(vacated, toTrickle);
        if (bubbledTo == vacated) {
            return heap.tryCrossOverAndBubbleUp(index, vacated, toTrickle);
        }
        return (bubbledTo < index) ? new MoveDesc<E>(toTrickle, this.elementData(index)) : null;
    }
    
    private E removeAndGet(final int index) {
        final E value = this.elementData(index);
        this.removeAt(index);
        return value;
    }
    
    private Heap heapForIndex(final int i) {
        return isEvenLevel(i) ? this.minHeap : this.maxHeap;
    }
    
    @VisibleForTesting
    static boolean isEvenLevel(final int index) {
        final int oneBased = index + 1;
        Preconditions.checkState(oneBased > 0, (Object)"negative index");
        return (oneBased & 0x55555555) > (oneBased & 0xAAAAAAAA);
    }
    
    @VisibleForTesting
    boolean isIntact() {
        for (int i = 1; i < this.size; ++i) {
            if (!this.heapForIndex(i).verifyIndex(i)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public Iterator<E> iterator() {
        return new QueueIterator();
    }
    
    @Override
    public void clear() {
        for (int i = 0; i < this.size; ++i) {
            this.queue[i] = null;
        }
        this.size = 0;
    }
    
    @Override
    public Object[] toArray() {
        final Object[] copyTo = new Object[this.size];
        System.arraycopy(this.queue, 0, copyTo, 0, this.size);
        return copyTo;
    }
    
    public Comparator<? super E> comparator() {
        return this.minHeap.ordering;
    }
    
    @VisibleForTesting
    int capacity() {
        return this.queue.length;
    }
    
    @VisibleForTesting
    static int initialQueueSize(final int configuredExpectedSize, final int maximumSize, final Iterable<?> initialContents) {
        int result = (configuredExpectedSize == -1) ? 11 : configuredExpectedSize;
        if (initialContents instanceof Collection) {
            final int initialSize = ((Collection)initialContents).size();
            result = Math.max(result, initialSize);
        }
        return capAtMaximumSize(result, maximumSize);
    }
    
    private void growIfNeeded() {
        if (this.size > this.queue.length) {
            final int newCapacity = this.calculateNewCapacity();
            final Object[] newQueue = new Object[newCapacity];
            System.arraycopy(this.queue, 0, newQueue, 0, this.queue.length);
            this.queue = newQueue;
        }
    }
    
    private int calculateNewCapacity() {
        final int oldCapacity = this.queue.length;
        final int newCapacity = (oldCapacity < 64) ? ((oldCapacity + 1) * 2) : IntMath.checkedMultiply(oldCapacity / 2, 3);
        return capAtMaximumSize(newCapacity, this.maximumSize);
    }
    
    private static int capAtMaximumSize(final int queueSize, final int maximumSize) {
        return Math.min(queueSize - 1, maximumSize) + 1;
    }
    
    @Beta
    public static final class Builder<B>
    {
        private static final int UNSET_EXPECTED_SIZE = -1;
        private final Comparator<B> comparator;
        private int expectedSize;
        private int maximumSize;
        
        private Builder(final Comparator<B> comparator) {
            this.expectedSize = -1;
            this.maximumSize = Integer.MAX_VALUE;
            this.comparator = Preconditions.checkNotNull(comparator);
        }
        
        public Builder<B> expectedSize(final int expectedSize) {
            Preconditions.checkArgument(expectedSize >= 0);
            this.expectedSize = expectedSize;
            return this;
        }
        
        public Builder<B> maximumSize(final int maximumSize) {
            Preconditions.checkArgument(maximumSize > 0);
            this.maximumSize = maximumSize;
            return this;
        }
        
        public <T extends B> MinMaxPriorityQueue<T> create() {
            return this.create((Iterable<? extends T>)Collections.emptySet());
        }
        
        public <T extends B> MinMaxPriorityQueue<T> create(final Iterable<? extends T> initialContents) {
            final MinMaxPriorityQueue<T> queue = new MinMaxPriorityQueue<T>(this, MinMaxPriorityQueue.initialQueueSize(this.expectedSize, this.maximumSize, initialContents), null);
            for (final T element : initialContents) {
                queue.offer(element);
            }
            return queue;
        }
        
        private <T extends B> Ordering<T> ordering() {
            return Ordering.from((Comparator<T>)this.comparator);
        }
    }
    
    static class MoveDesc<E>
    {
        final E toTrickle;
        final E replaced;
        
        MoveDesc(final E toTrickle, final E replaced) {
            this.toTrickle = toTrickle;
            this.replaced = replaced;
        }
    }
    
    private class Heap
    {
        final Ordering<E> ordering;
        Heap otherHeap;
        
        Heap(final Ordering<E> ordering) {
            this.ordering = ordering;
        }
        
        int compareElements(final int a, final int b) {
            return this.ordering.compare(MinMaxPriorityQueue.this.elementData(a), MinMaxPriorityQueue.this.elementData(b));
        }
        
        MoveDesc<E> tryCrossOverAndBubbleUp(final int removeIndex, final int vacated, final E toTrickle) {
            final int crossOver = this.crossOver(vacated, toTrickle);
            if (crossOver == vacated) {
                return null;
            }
            E parent;
            if (crossOver < removeIndex) {
                parent = MinMaxPriorityQueue.this.elementData(removeIndex);
            }
            else {
                parent = MinMaxPriorityQueue.this.elementData(this.getParentIndex(removeIndex));
            }
            if (this.otherHeap.bubbleUpAlternatingLevels(crossOver, toTrickle) < removeIndex) {
                return new MoveDesc<E>(toTrickle, parent);
            }
            return null;
        }
        
        void bubbleUp(int index, final E x) {
            final int crossOver = this.crossOverUp(index, x);
            Heap heap;
            if (crossOver == index) {
                heap = this;
            }
            else {
                index = crossOver;
                heap = this.otherHeap;
            }
            heap.bubbleUpAlternatingLevels(index, x);
        }
        
        int bubbleUpAlternatingLevels(int index, final E x) {
            while (index > 2) {
                final int grandParentIndex = this.getGrandparentIndex(index);
                final E e = MinMaxPriorityQueue.this.elementData(grandParentIndex);
                if (this.ordering.compare(e, x) <= 0) {
                    break;
                }
                MinMaxPriorityQueue.this.queue[index] = e;
                index = grandParentIndex;
            }
            MinMaxPriorityQueue.this.queue[index] = x;
            return index;
        }
        
        int findMin(final int index, final int len) {
            if (index >= MinMaxPriorityQueue.this.size) {
                return -1;
            }
            Preconditions.checkState(index > 0);
            final int limit = Math.min(index, MinMaxPriorityQueue.this.size - len) + len;
            int minIndex = index;
            for (int i = index + 1; i < limit; ++i) {
                if (this.compareElements(i, minIndex) < 0) {
                    minIndex = i;
                }
            }
            return minIndex;
        }
        
        int findMinChild(final int index) {
            return this.findMin(this.getLeftChildIndex(index), 2);
        }
        
        int findMinGrandChild(final int index) {
            final int leftChildIndex = this.getLeftChildIndex(index);
            if (leftChildIndex < 0) {
                return -1;
            }
            return this.findMin(this.getLeftChildIndex(leftChildIndex), 4);
        }
        
        int crossOverUp(final int index, final E x) {
            if (index == 0) {
                MinMaxPriorityQueue.this.queue[0] = x;
                return 0;
            }
            int parentIndex = this.getParentIndex(index);
            E parentElement = MinMaxPriorityQueue.this.elementData(parentIndex);
            if (parentIndex != 0) {
                final int grandparentIndex = this.getParentIndex(parentIndex);
                final int uncleIndex = this.getRightChildIndex(grandparentIndex);
                if (uncleIndex != parentIndex && this.getLeftChildIndex(uncleIndex) >= MinMaxPriorityQueue.this.size) {
                    final E uncleElement = MinMaxPriorityQueue.this.elementData(uncleIndex);
                    if (this.ordering.compare(uncleElement, parentElement) < 0) {
                        parentIndex = uncleIndex;
                        parentElement = uncleElement;
                    }
                }
            }
            if (this.ordering.compare(parentElement, x) < 0) {
                MinMaxPriorityQueue.this.queue[index] = parentElement;
                MinMaxPriorityQueue.this.queue[parentIndex] = x;
                return parentIndex;
            }
            MinMaxPriorityQueue.this.queue[index] = x;
            return index;
        }
        
        int getCorrectLastElement(final E actualLastElement) {
            final int parentIndex = this.getParentIndex(MinMaxPriorityQueue.this.size);
            if (parentIndex != 0) {
                final int grandparentIndex = this.getParentIndex(parentIndex);
                final int uncleIndex = this.getRightChildIndex(grandparentIndex);
                if (uncleIndex != parentIndex && this.getLeftChildIndex(uncleIndex) >= MinMaxPriorityQueue.this.size) {
                    final E uncleElement = MinMaxPriorityQueue.this.elementData(uncleIndex);
                    if (this.ordering.compare(uncleElement, actualLastElement) < 0) {
                        MinMaxPriorityQueue.this.queue[uncleIndex] = actualLastElement;
                        MinMaxPriorityQueue.this.queue[MinMaxPriorityQueue.this.size] = uncleElement;
                        return uncleIndex;
                    }
                }
            }
            return MinMaxPriorityQueue.this.size;
        }
        
        int crossOver(final int index, final E x) {
            final int minChildIndex = this.findMinChild(index);
            if (minChildIndex > 0 && this.ordering.compare(MinMaxPriorityQueue.this.elementData(minChildIndex), x) < 0) {
                MinMaxPriorityQueue.this.queue[index] = MinMaxPriorityQueue.this.elementData(minChildIndex);
                MinMaxPriorityQueue.this.queue[minChildIndex] = x;
                return minChildIndex;
            }
            return this.crossOverUp(index, x);
        }
        
        int fillHoleAt(int index) {
            int minGrandchildIndex;
            while ((minGrandchildIndex = this.findMinGrandChild(index)) > 0) {
                MinMaxPriorityQueue.this.queue[index] = MinMaxPriorityQueue.this.elementData(minGrandchildIndex);
                index = minGrandchildIndex;
            }
            return index;
        }
        
        private boolean verifyIndex(final int i) {
            return (this.getLeftChildIndex(i) >= MinMaxPriorityQueue.this.size || this.compareElements(i, this.getLeftChildIndex(i)) <= 0) && (this.getRightChildIndex(i) >= MinMaxPriorityQueue.this.size || this.compareElements(i, this.getRightChildIndex(i)) <= 0) && (i <= 0 || this.compareElements(i, this.getParentIndex(i)) <= 0) && (i <= 2 || this.compareElements(this.getGrandparentIndex(i), i) <= 0);
        }
        
        private int getLeftChildIndex(final int i) {
            return i * 2 + 1;
        }
        
        private int getRightChildIndex(final int i) {
            return i * 2 + 2;
        }
        
        private int getParentIndex(final int i) {
            return (i - 1) / 2;
        }
        
        private int getGrandparentIndex(final int i) {
            return this.getParentIndex(this.getParentIndex(i));
        }
    }
    
    private class QueueIterator implements Iterator<E>
    {
        private int cursor;
        private int expectedModCount;
        private Queue<E> forgetMeNot;
        private List<E> skipMe;
        private E lastFromForgetMeNot;
        private boolean canRemove;
        
        private QueueIterator() {
            this.cursor = -1;
            this.expectedModCount = MinMaxPriorityQueue.this.modCount;
        }
        
        @Override
        public boolean hasNext() {
            this.checkModCount();
            return this.nextNotInSkipMe(this.cursor + 1) < MinMaxPriorityQueue.this.size() || (this.forgetMeNot != null && !this.forgetMeNot.isEmpty());
        }
        
        @Override
        public E next() {
            this.checkModCount();
            final int tempCursor = this.nextNotInSkipMe(this.cursor + 1);
            if (tempCursor < MinMaxPriorityQueue.this.size()) {
                this.cursor = tempCursor;
                this.canRemove = true;
                return MinMaxPriorityQueue.this.elementData(this.cursor);
            }
            if (this.forgetMeNot != null) {
                this.cursor = MinMaxPriorityQueue.this.size();
                this.lastFromForgetMeNot = this.forgetMeNot.poll();
                if (this.lastFromForgetMeNot != null) {
                    this.canRemove = true;
                    return this.lastFromForgetMeNot;
                }
            }
            throw new NoSuchElementException("iterator moved past last element in queue.");
        }
        
        @Override
        public void remove() {
            CollectPreconditions.checkRemove(this.canRemove);
            this.checkModCount();
            this.canRemove = false;
            ++this.expectedModCount;
            if (this.cursor < MinMaxPriorityQueue.this.size()) {
                final MoveDesc<E> moved = MinMaxPriorityQueue.this.removeAt(this.cursor);
                if (moved != null) {
                    if (this.forgetMeNot == null) {
                        this.forgetMeNot = new ArrayDeque<E>();
                        this.skipMe = new ArrayList<E>(3);
                    }
                    this.forgetMeNot.add(moved.toTrickle);
                    this.skipMe.add(moved.replaced);
                }
                --this.cursor;
            }
            else {
                Preconditions.checkState(this.removeExact(this.lastFromForgetMeNot));
                this.lastFromForgetMeNot = null;
            }
        }
        
        private boolean containsExact(final Iterable<E> elements, final E target) {
            for (final E element : elements) {
                if (element == target) {
                    return true;
                }
            }
            return false;
        }
        
        boolean removeExact(final Object target) {
            for (int i = 0; i < MinMaxPriorityQueue.this.size; ++i) {
                if (MinMaxPriorityQueue.this.queue[i] == target) {
                    MinMaxPriorityQueue.this.removeAt(i);
                    return true;
                }
            }
            return false;
        }
        
        void checkModCount() {
            if (MinMaxPriorityQueue.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
        
        private int nextNotInSkipMe(int c) {
            if (this.skipMe != null) {
                while (c < MinMaxPriorityQueue.this.size() && this.containsExact(this.skipMe, MinMaxPriorityQueue.this.elementData(c))) {
                    ++c;
                }
            }
            return c;
        }
    }
}
