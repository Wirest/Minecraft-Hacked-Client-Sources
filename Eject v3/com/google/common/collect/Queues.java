package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;

import java.util.*;
import java.util.concurrent.*;

public final class Queues {
    public static <E> ArrayBlockingQueue<E> newArrayBlockingQueue(int paramInt) {
        return new ArrayBlockingQueue(paramInt);
    }

    public static <E> ArrayDeque<E> newArrayDeque() {
        return new ArrayDeque();
    }

    public static <E> ArrayDeque<E> newArrayDeque(Iterable<? extends E> paramIterable) {
        if ((paramIterable instanceof Collection)) {
            return new ArrayDeque(Collections2.cast(paramIterable));
        }
        ArrayDeque localArrayDeque = new ArrayDeque();
        Iterables.addAll(localArrayDeque, paramIterable);
        return localArrayDeque;
    }

    public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue() {
        return new ConcurrentLinkedQueue();
    }

    public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue(Iterable<? extends E> paramIterable) {
        if ((paramIterable instanceof Collection)) {
            return new ConcurrentLinkedQueue(Collections2.cast(paramIterable));
        }
        ConcurrentLinkedQueue localConcurrentLinkedQueue = new ConcurrentLinkedQueue();
        Iterables.addAll(localConcurrentLinkedQueue, paramIterable);
        return localConcurrentLinkedQueue;
    }

    public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque() {
        return new LinkedBlockingDeque();
    }

    public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(int paramInt) {
        return new LinkedBlockingDeque(paramInt);
    }

    public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(Iterable<? extends E> paramIterable) {
        if ((paramIterable instanceof Collection)) {
            return new LinkedBlockingDeque(Collections2.cast(paramIterable));
        }
        LinkedBlockingDeque localLinkedBlockingDeque = new LinkedBlockingDeque();
        Iterables.addAll(localLinkedBlockingDeque, paramIterable);
        return localLinkedBlockingDeque;
    }

    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue() {
        return new LinkedBlockingQueue();
    }

    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(int paramInt) {
        return new LinkedBlockingQueue(paramInt);
    }

    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(Iterable<? extends E> paramIterable) {
        if ((paramIterable instanceof Collection)) {
            return new LinkedBlockingQueue(Collections2.cast(paramIterable));
        }
        LinkedBlockingQueue localLinkedBlockingQueue = new LinkedBlockingQueue();
        Iterables.addAll(localLinkedBlockingQueue, paramIterable);
        return localLinkedBlockingQueue;
    }

    public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue() {
        return new PriorityBlockingQueue();
    }

    public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue(Iterable<? extends E> paramIterable) {
        if ((paramIterable instanceof Collection)) {
            return new PriorityBlockingQueue(Collections2.cast(paramIterable));
        }
        PriorityBlockingQueue localPriorityBlockingQueue = new PriorityBlockingQueue();
        Iterables.addAll(localPriorityBlockingQueue, paramIterable);
        return localPriorityBlockingQueue;
    }

    public static <E extends Comparable> PriorityQueue<E> newPriorityQueue() {
        return new PriorityQueue();
    }

    public static <E extends Comparable> PriorityQueue<E> newPriorityQueue(Iterable<? extends E> paramIterable) {
        if ((paramIterable instanceof Collection)) {
            return new PriorityQueue(Collections2.cast(paramIterable));
        }
        PriorityQueue localPriorityQueue = new PriorityQueue();
        Iterables.addAll(localPriorityQueue, paramIterable);
        return localPriorityQueue;
    }

    public static <E> SynchronousQueue<E> newSynchronousQueue() {
        return new SynchronousQueue();
    }

    @Beta
    public static <E> int drain(BlockingQueue<E> paramBlockingQueue, Collection<? super E> paramCollection, int paramInt, long paramLong, TimeUnit paramTimeUnit)
            throws InterruptedException {
        Preconditions.checkNotNull(paramCollection);
        long l = System.nanoTime() + paramTimeUnit.toNanos(paramLong);
        int i = 0;
        while (i < paramInt) {
            i |= paramBlockingQueue.drainTo(paramCollection, paramInt - i);
            if (i < paramInt) {
                Object localObject = paramBlockingQueue.poll(l - System.nanoTime(), TimeUnit.NANOSECONDS);
                if (localObject == null) {
                    break;
                }
                paramCollection.add(localObject);
                i++;
            }
        }
        return i;
    }

    @Beta
    public static <E> int drainUninterruptibly(BlockingQueue<E> paramBlockingQueue, Collection<? super E> paramCollection, int paramInt, long paramLong, TimeUnit paramTimeUnit) {
        Preconditions.checkNotNull(paramCollection);
        long l = System.nanoTime() + paramTimeUnit.toNanos(paramLong);
        int i = 0;
        int j = 0;
        try {
            while (i < paramInt) {
                i |= paramBlockingQueue.drainTo(paramCollection, paramInt - i);
                if (i < paramInt) {
                    Object localObject1;
                    for (; ; ) {
                        try {
                            localObject1 = paramBlockingQueue.poll(l - System.nanoTime(), TimeUnit.NANOSECONDS);
                        } catch (InterruptedException localInterruptedException) {
                            j = 1;
                        }
                    }
                    if (localObject1 == null) {
                        break;
                    }
                    paramCollection.add(localObject1);
                    i++;
                }
            }
        } finally {
            if (j != 0) {
                Thread.currentThread().interrupt();
            }
        }
        return i;
    }

    @Beta
    public static <E> Queue<E> synchronizedQueue(Queue<E> paramQueue) {
        return Synchronized.queue(paramQueue, null);
    }

    @Beta
    public static <E> Deque<E> synchronizedDeque(Deque<E> paramDeque) {
        return Synchronized.deque(paramDeque, null);
    }
}




