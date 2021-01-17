// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import com.google.common.base.Objects;
import com.google.common.collect.MapMaker;
import java.util.concurrent.ConcurrentMap;
import java.lang.ref.WeakReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.concurrent.atomic.AtomicReferenceArray;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import com.google.common.math.IntMath;
import java.math.RoundingMode;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.List;
import java.util.Collections;
import java.util.Arrays;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.concurrent.locks.ReadWriteLock;
import com.google.common.base.Supplier;
import com.google.common.annotations.Beta;

@Beta
public abstract class Striped<L>
{
    private static final int LARGE_LAZY_CUTOFF = 1024;
    private static final Supplier<ReadWriteLock> READ_WRITE_LOCK_SUPPLIER;
    private static final int ALL_SET = -1;
    
    private Striped() {
    }
    
    public abstract L get(final Object p0);
    
    public abstract L getAt(final int p0);
    
    abstract int indexFor(final Object p0);
    
    public abstract int size();
    
    public Iterable<L> bulkGet(final Iterable<?> keys) {
        final Object[] array = Iterables.toArray(keys, Object.class);
        if (array.length == 0) {
            return (Iterable<L>)ImmutableList.of();
        }
        final int[] stripes = new int[array.length];
        for (int i = 0; i < array.length; ++i) {
            stripes[i] = this.indexFor(array[i]);
        }
        Arrays.sort(stripes);
        int previousStripe = stripes[0];
        array[0] = this.getAt(previousStripe);
        for (int j = 1; j < array.length; ++j) {
            final int currentStripe = stripes[j];
            if (currentStripe == previousStripe) {
                array[j] = array[j - 1];
            }
            else {
                array[j] = this.getAt(currentStripe);
                previousStripe = currentStripe;
            }
        }
        final List<L> asList = Arrays.asList((L[])array);
        return (Iterable<L>)Collections.unmodifiableList((List<?>)asList);
    }
    
    public static Striped<Lock> lock(final int stripes) {
        return new CompactStriped<Lock>(stripes, (Supplier)new Supplier<Lock>() {
            @Override
            public Lock get() {
                return new PaddedLock();
            }
        });
    }
    
    public static Striped<Lock> lazyWeakLock(final int stripes) {
        return lazy(stripes, (Supplier<Lock>)new Supplier<Lock>() {
            @Override
            public Lock get() {
                return new ReentrantLock(false);
            }
        });
    }
    
    private static <L> Striped<L> lazy(final int stripes, final Supplier<L> supplier) {
        return (Striped<L>)((stripes < 1024) ? new SmallLazyStriped<L>(stripes, (Supplier<Object>)supplier) : new LargeLazyStriped<L>(stripes, (Supplier<Object>)supplier));
    }
    
    public static Striped<Semaphore> semaphore(final int stripes, final int permits) {
        return new CompactStriped<Semaphore>(stripes, (Supplier)new Supplier<Semaphore>() {
            @Override
            public Semaphore get() {
                return new PaddedSemaphore(permits);
            }
        });
    }
    
    public static Striped<Semaphore> lazyWeakSemaphore(final int stripes, final int permits) {
        return lazy(stripes, (Supplier<Semaphore>)new Supplier<Semaphore>() {
            @Override
            public Semaphore get() {
                return new Semaphore(permits, false);
            }
        });
    }
    
    public static Striped<ReadWriteLock> readWriteLock(final int stripes) {
        return new CompactStriped<ReadWriteLock>(stripes, (Supplier)Striped.READ_WRITE_LOCK_SUPPLIER);
    }
    
    public static Striped<ReadWriteLock> lazyWeakReadWriteLock(final int stripes) {
        return lazy(stripes, Striped.READ_WRITE_LOCK_SUPPLIER);
    }
    
    private static int ceilToPowerOfTwo(final int x) {
        return 1 << IntMath.log2(x, RoundingMode.CEILING);
    }
    
    private static int smear(int hashCode) {
        hashCode ^= (hashCode >>> 20 ^ hashCode >>> 12);
        return hashCode ^ hashCode >>> 7 ^ hashCode >>> 4;
    }
    
    static {
        READ_WRITE_LOCK_SUPPLIER = new Supplier<ReadWriteLock>() {
            @Override
            public ReadWriteLock get() {
                return new ReentrantReadWriteLock();
            }
        };
    }
    
    private abstract static class PowerOfTwoStriped<L> extends Striped<L>
    {
        final int mask;
        
        PowerOfTwoStriped(final int stripes) {
            super(null);
            Preconditions.checkArgument(stripes > 0, (Object)"Stripes must be positive");
            this.mask = ((stripes > 1073741824) ? -1 : (ceilToPowerOfTwo(stripes) - 1));
        }
        
        @Override
        final int indexFor(final Object key) {
            final int hash = smear(key.hashCode());
            return hash & this.mask;
        }
        
        @Override
        public final L get(final Object key) {
            return this.getAt(this.indexFor(key));
        }
    }
    
    private static class CompactStriped<L> extends PowerOfTwoStriped<L>
    {
        private final Object[] array;
        
        private CompactStriped(final int stripes, final Supplier<L> supplier) {
            super(stripes);
            Preconditions.checkArgument(stripes <= 1073741824, (Object)"Stripes must be <= 2^30)");
            this.array = new Object[this.mask + 1];
            for (int i = 0; i < this.array.length; ++i) {
                this.array[i] = supplier.get();
            }
        }
        
        @Override
        public L getAt(final int index) {
            return (L)this.array[index];
        }
        
        @Override
        public int size() {
            return this.array.length;
        }
    }
    
    @VisibleForTesting
    static class SmallLazyStriped<L> extends PowerOfTwoStriped<L>
    {
        final AtomicReferenceArray<ArrayReference<? extends L>> locks;
        final Supplier<L> supplier;
        final int size;
        final ReferenceQueue<L> queue;
        
        SmallLazyStriped(final int stripes, final Supplier<L> supplier) {
            super(stripes);
            this.queue = new ReferenceQueue<L>();
            this.size = ((this.mask == -1) ? Integer.MAX_VALUE : (this.mask + 1));
            this.locks = new AtomicReferenceArray<ArrayReference<? extends L>>(this.size);
            this.supplier = supplier;
        }
        
        @Override
        public L getAt(final int index) {
            if (this.size != Integer.MAX_VALUE) {
                Preconditions.checkElementIndex(index, this.size());
            }
            ArrayReference<? extends L> existingRef = this.locks.get(index);
            L existing = (existingRef == null) ? null : existingRef.get();
            if (existing != null) {
                return existing;
            }
            final L created = this.supplier.get();
            final ArrayReference<L> newRef = new ArrayReference<L>(created, index, this.queue);
            while (!this.locks.compareAndSet(index, existingRef, (ArrayReference<? extends L>)newRef)) {
                existingRef = this.locks.get(index);
                existing = ((existingRef == null) ? null : existingRef.get());
                if (existing != null) {
                    return existing;
                }
            }
            this.drainQueue();
            return created;
        }
        
        private void drainQueue() {
            Reference<? extends L> ref;
            while ((ref = this.queue.poll()) != null) {
                final ArrayReference<? extends L> arrayRef = (ArrayReference<? extends L>)(ArrayReference)ref;
                this.locks.compareAndSet(arrayRef.index, arrayRef, null);
            }
        }
        
        @Override
        public int size() {
            return this.size;
        }
        
        private static final class ArrayReference<L> extends WeakReference<L>
        {
            final int index;
            
            ArrayReference(final L referent, final int index, final ReferenceQueue<L> queue) {
                super(referent, queue);
                this.index = index;
            }
        }
    }
    
    @VisibleForTesting
    static class LargeLazyStriped<L> extends PowerOfTwoStriped<L>
    {
        final ConcurrentMap<Integer, L> locks;
        final Supplier<L> supplier;
        final int size;
        
        LargeLazyStriped(final int stripes, final Supplier<L> supplier) {
            super(stripes);
            this.size = ((this.mask == -1) ? Integer.MAX_VALUE : (this.mask + 1));
            this.supplier = supplier;
            this.locks = new MapMaker().weakValues().makeMap();
        }
        
        @Override
        public L getAt(final int index) {
            if (this.size != Integer.MAX_VALUE) {
                Preconditions.checkElementIndex(index, this.size());
            }
            L existing = this.locks.get(index);
            if (existing != null) {
                return existing;
            }
            final L created = this.supplier.get();
            existing = this.locks.putIfAbsent(index, created);
            return Objects.firstNonNull(existing, created);
        }
        
        @Override
        public int size() {
            return this.size;
        }
    }
    
    private static class PaddedLock extends ReentrantLock
    {
        long q1;
        long q2;
        long q3;
        
        PaddedLock() {
            super(false);
        }
    }
    
    private static class PaddedSemaphore extends Semaphore
    {
        long q1;
        long q2;
        long q3;
        
        PaddedSemaphore(final int permits) {
            super(permits, false);
        }
    }
}
