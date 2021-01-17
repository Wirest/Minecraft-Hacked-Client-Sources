// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import com.google.common.base.Equivalence;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.Nullable;
import java.lang.ref.ReferenceQueue;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import com.google.common.base.Preconditions;
import com.google.common.base.Function;

class ComputingConcurrentHashMap<K, V> extends MapMakerInternalMap<K, V>
{
    final Function<? super K, ? extends V> computingFunction;
    private static final long serialVersionUID = 4L;
    
    ComputingConcurrentHashMap(final MapMaker builder, final Function<? super K, ? extends V> computingFunction) {
        super(builder);
        this.computingFunction = Preconditions.checkNotNull(computingFunction);
    }
    
    @Override
    Segment<K, V> createSegment(final int initialCapacity, final int maxSegmentSize) {
        return new ComputingSegment<K, V>(this, initialCapacity, maxSegmentSize);
    }
    
    @Override
    ComputingSegment<K, V> segmentFor(final int hash) {
        return (ComputingSegment<K, V>)(ComputingSegment)super.segmentFor(hash);
    }
    
    V getOrCompute(final K key) throws ExecutionException {
        final int hash = this.hash(Preconditions.checkNotNull(key));
        return this.segmentFor(hash).getOrCompute(key, hash, this.computingFunction);
    }
    
    @Override
    Object writeReplace() {
        return new ComputingSerializationProxy(this.keyStrength, this.valueStrength, this.keyEquivalence, this.valueEquivalence, this.expireAfterWriteNanos, this.expireAfterAccessNanos, this.maximumSize, this.concurrencyLevel, (MapMaker.RemovalListener<? super Object, ? super Object>)this.removalListener, (ConcurrentMap<Object, Object>)this, (Function<? super Object, ?>)this.computingFunction);
    }
    
    static final class ComputingSegment<K, V> extends Segment<K, V>
    {
        ComputingSegment(final MapMakerInternalMap<K, V> map, final int initialCapacity, final int maxSegmentSize) {
            super(map, initialCapacity, maxSegmentSize);
        }
        
        V getOrCompute(final K key, final int hash, final Function<? super K, ? extends V> computingFunction) throws ExecutionException {
            try {
                V value;
                ReferenceEntry<K, V> e;
                do {
                    e = this.getEntry(key, hash);
                    if (e != null) {
                        value = this.getLiveValue(e);
                        if (value != null) {
                            this.recordRead(e);
                            return value;
                        }
                    }
                    if (e == null || !e.getValueReference().isComputingReference()) {
                        boolean createNewEntry = true;
                        ComputingValueReference<K, V> computingValueReference = null;
                        this.lock();
                        try {
                            this.preWriteCleanup();
                            final int newCount = this.count - 1;
                            final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                            final int index = hash & table.length() - 1;
                            final ReferenceEntry<K, V> first = e = table.get(index);
                            while (e != null) {
                                final K entryKey = e.getKey();
                                if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                                    final ValueReference<K, V> valueReference = e.getValueReference();
                                    if (valueReference.isComputingReference()) {
                                        createNewEntry = false;
                                        break;
                                    }
                                    final V value2 = e.getValueReference().get();
                                    if (value2 == null) {
                                        this.enqueueNotification(entryKey, hash, value2, MapMaker.RemovalCause.COLLECTED);
                                    }
                                    else {
                                        if (!this.map.expires() || !this.map.isExpired(e)) {
                                            this.recordLockedRead(e);
                                            return value2;
                                        }
                                        this.enqueueNotification(entryKey, hash, value2, MapMaker.RemovalCause.EXPIRED);
                                    }
                                    this.evictionQueue.remove(e);
                                    this.expirationQueue.remove(e);
                                    this.count = newCount;
                                    break;
                                }
                                else {
                                    e = e.getNext();
                                }
                            }
                            if (createNewEntry) {
                                computingValueReference = new ComputingValueReference<K, V>(computingFunction);
                                if (e == null) {
                                    e = this.newEntry(key, hash, first);
                                    e.setValueReference(computingValueReference);
                                    table.set(index, e);
                                }
                                else {
                                    e.setValueReference(computingValueReference);
                                }
                            }
                        }
                        finally {
                            this.unlock();
                            this.postWriteCleanup();
                        }
                        if (createNewEntry) {
                            return (V)this.compute(key, hash, (ReferenceEntry<K, Object>)e, (ComputingValueReference<K, Object>)computingValueReference);
                        }
                    }
                    Preconditions.checkState(!Thread.holdsLock(e), (Object)"Recursive computation");
                    value = e.getValueReference().waitForValue();
                } while (value == null);
                this.recordRead(e);
                return value;
            }
            finally {
                this.postReadCleanup();
            }
        }
        
        V compute(final K key, final int hash, final ReferenceEntry<K, V> e, final ComputingValueReference<K, V> computingValueReference) throws ExecutionException {
            V value = null;
            final long start = System.nanoTime();
            long end = 0L;
            try {
                synchronized (e) {
                    value = computingValueReference.compute(key, hash);
                    end = System.nanoTime();
                }
                if (value != null) {
                    final V oldValue = this.put(key, hash, value, true);
                    if (oldValue != null) {
                        this.enqueueNotification(key, hash, value, MapMaker.RemovalCause.REPLACED);
                    }
                }
                return value;
            }
            finally {
                if (end == 0L) {
                    end = System.nanoTime();
                }
                if (value == null) {
                    this.clearValue(key, hash, computingValueReference);
                }
            }
        }
    }
    
    private static final class ComputationExceptionReference<K, V> implements ValueReference<K, V>
    {
        final Throwable t;
        
        ComputationExceptionReference(final Throwable t) {
            this.t = t;
        }
        
        @Override
        public V get() {
            return null;
        }
        
        @Override
        public ReferenceEntry<K, V> getEntry() {
            return null;
        }
        
        @Override
        public ValueReference<K, V> copyFor(final ReferenceQueue<V> queue, final V value, final ReferenceEntry<K, V> entry) {
            return this;
        }
        
        @Override
        public boolean isComputingReference() {
            return false;
        }
        
        @Override
        public V waitForValue() throws ExecutionException {
            throw new ExecutionException(this.t);
        }
        
        @Override
        public void clear(final ValueReference<K, V> newValue) {
        }
    }
    
    private static final class ComputedReference<K, V> implements ValueReference<K, V>
    {
        final V value;
        
        ComputedReference(@Nullable final V value) {
            this.value = value;
        }
        
        @Override
        public V get() {
            return this.value;
        }
        
        @Override
        public ReferenceEntry<K, V> getEntry() {
            return null;
        }
        
        @Override
        public ValueReference<K, V> copyFor(final ReferenceQueue<V> queue, final V value, final ReferenceEntry<K, V> entry) {
            return this;
        }
        
        @Override
        public boolean isComputingReference() {
            return false;
        }
        
        @Override
        public V waitForValue() {
            return this.get();
        }
        
        @Override
        public void clear(final ValueReference<K, V> newValue) {
        }
    }
    
    private static final class ComputingValueReference<K, V> implements ValueReference<K, V>
    {
        final Function<? super K, ? extends V> computingFunction;
        @GuardedBy("ComputingValueReference.this")
        volatile ValueReference<K, V> computedReference;
        
        public ComputingValueReference(final Function<? super K, ? extends V> computingFunction) {
            this.computedReference = MapMakerInternalMap.unset();
            this.computingFunction = computingFunction;
        }
        
        @Override
        public V get() {
            return null;
        }
        
        @Override
        public ReferenceEntry<K, V> getEntry() {
            return null;
        }
        
        @Override
        public ValueReference<K, V> copyFor(final ReferenceQueue<V> queue, @Nullable final V value, final ReferenceEntry<K, V> entry) {
            return this;
        }
        
        @Override
        public boolean isComputingReference() {
            return true;
        }
        
        @Override
        public V waitForValue() throws ExecutionException {
            if (this.computedReference == MapMakerInternalMap.UNSET) {
                boolean interrupted = false;
                try {
                    synchronized (this) {
                        while (this.computedReference == MapMakerInternalMap.UNSET) {
                            try {
                                this.wait();
                            }
                            catch (InterruptedException ie) {
                                interrupted = true;
                            }
                        }
                    }
                }
                finally {
                    if (interrupted) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            return this.computedReference.waitForValue();
        }
        
        @Override
        public void clear(final ValueReference<K, V> newValue) {
            this.setValueReference(newValue);
        }
        
        V compute(final K key, final int hash) throws ExecutionException {
            V value;
            try {
                value = (V)this.computingFunction.apply((Object)key);
            }
            catch (Throwable t) {
                this.setValueReference(new ComputationExceptionReference<K, V>(t));
                throw new ExecutionException(t);
            }
            this.setValueReference(new ComputedReference<K, V>(value));
            return value;
        }
        
        void setValueReference(final ValueReference<K, V> valueReference) {
            synchronized (this) {
                if (this.computedReference == MapMakerInternalMap.UNSET) {
                    this.computedReference = valueReference;
                    this.notifyAll();
                }
            }
        }
    }
    
    static final class ComputingSerializationProxy<K, V> extends AbstractSerializationProxy<K, V>
    {
        final Function<? super K, ? extends V> computingFunction;
        private static final long serialVersionUID = 4L;
        
        ComputingSerializationProxy(final Strength keyStrength, final Strength valueStrength, final Equivalence<Object> keyEquivalence, final Equivalence<Object> valueEquivalence, final long expireAfterWriteNanos, final long expireAfterAccessNanos, final int maximumSize, final int concurrencyLevel, final MapMaker.RemovalListener<? super K, ? super V> removalListener, final ConcurrentMap<K, V> delegate, final Function<? super K, ? extends V> computingFunction) {
            super(keyStrength, valueStrength, keyEquivalence, valueEquivalence, expireAfterWriteNanos, expireAfterAccessNanos, maximumSize, concurrencyLevel, removalListener, delegate);
            this.computingFunction = computingFunction;
        }
        
        private void writeObject(final ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
            this.writeMapTo(out);
        }
        
        private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            final MapMaker mapMaker = this.readMapMaker(in);
            this.delegate = (ConcurrentMap<K, V>)mapMaker.makeComputingMap((Function<? super K, ? extends V>)this.computingFunction);
            this.readEntries(in);
        }
        
        Object readResolve() {
            return this.delegate;
        }
    }
}
