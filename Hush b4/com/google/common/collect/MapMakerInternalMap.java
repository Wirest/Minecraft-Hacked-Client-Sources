// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.NoSuchElementException;
import java.util.concurrent.CancellationException;
import java.lang.ref.Reference;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.lang.ref.WeakReference;
import java.lang.ref.SoftReference;
import java.util.AbstractQueue;
import java.lang.ref.ReferenceQueue;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import java.util.concurrent.atomic.AtomicReferenceArray;
import com.google.common.primitives.Ints;
import java.util.logging.Level;
import com.google.common.annotations.VisibleForTesting;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Map;
import java.util.Collection;
import java.util.Set;
import com.google.common.base.Ticker;
import java.util.Queue;
import com.google.common.base.Equivalence;
import java.util.logging.Logger;
import java.io.Serializable;
import java.util.concurrent.ConcurrentMap;
import java.util.AbstractMap;

class MapMakerInternalMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V>, Serializable
{
    static final int MAXIMUM_CAPACITY = 1073741824;
    static final int MAX_SEGMENTS = 65536;
    static final int CONTAINS_VALUE_RETRIES = 3;
    static final int DRAIN_THRESHOLD = 63;
    static final int DRAIN_MAX = 16;
    static final long CLEANUP_EXECUTOR_DELAY_SECS = 60L;
    private static final Logger logger;
    final transient int segmentMask;
    final transient int segmentShift;
    final transient Segment<K, V>[] segments;
    final int concurrencyLevel;
    final Equivalence<Object> keyEquivalence;
    final Equivalence<Object> valueEquivalence;
    final Strength keyStrength;
    final Strength valueStrength;
    final int maximumSize;
    final long expireAfterAccessNanos;
    final long expireAfterWriteNanos;
    final Queue<MapMaker.RemovalNotification<K, V>> removalNotificationQueue;
    final MapMaker.RemovalListener<K, V> removalListener;
    final transient EntryFactory entryFactory;
    final Ticker ticker;
    static final ValueReference<Object, Object> UNSET;
    static final Queue<?> DISCARDING_QUEUE;
    transient Set<K> keySet;
    transient Collection<V> values;
    transient Set<Map.Entry<K, V>> entrySet;
    private static final long serialVersionUID = 5L;
    
    MapMakerInternalMap(final MapMaker builder) {
        this.concurrencyLevel = Math.min(builder.getConcurrencyLevel(), 65536);
        this.keyStrength = builder.getKeyStrength();
        this.valueStrength = builder.getValueStrength();
        this.keyEquivalence = builder.getKeyEquivalence();
        this.valueEquivalence = this.valueStrength.defaultEquivalence();
        this.maximumSize = builder.maximumSize;
        this.expireAfterAccessNanos = builder.getExpireAfterAccessNanos();
        this.expireAfterWriteNanos = builder.getExpireAfterWriteNanos();
        this.entryFactory = EntryFactory.getFactory(this.keyStrength, this.expires(), this.evictsBySize());
        this.ticker = builder.getTicker();
        this.removalListener = builder.getRemovalListener();
        this.removalNotificationQueue = ((this.removalListener == GenericMapMaker.NullListener.INSTANCE) ? discardingQueue() : new ConcurrentLinkedQueue<MapMaker.RemovalNotification<K, V>>());
        int initialCapacity = Math.min(builder.getInitialCapacity(), 1073741824);
        if (this.evictsBySize()) {
            initialCapacity = Math.min(initialCapacity, this.maximumSize);
        }
        int segmentShift = 0;
        int segmentCount;
        for (segmentCount = 1; segmentCount < this.concurrencyLevel && (!this.evictsBySize() || segmentCount * 2 <= this.maximumSize); segmentCount <<= 1) {
            ++segmentShift;
        }
        this.segmentShift = 32 - segmentShift;
        this.segmentMask = segmentCount - 1;
        this.segments = this.newSegmentArray(segmentCount);
        int segmentCapacity = initialCapacity / segmentCount;
        if (segmentCapacity * segmentCount < initialCapacity) {
            ++segmentCapacity;
        }
        int segmentSize;
        for (segmentSize = 1; segmentSize < segmentCapacity; segmentSize <<= 1) {}
        if (this.evictsBySize()) {
            int maximumSegmentSize = this.maximumSize / segmentCount + 1;
            final int remainder = this.maximumSize % segmentCount;
            for (int i = 0; i < this.segments.length; ++i) {
                if (i == remainder) {
                    --maximumSegmentSize;
                }
                this.segments[i] = this.createSegment(segmentSize, maximumSegmentSize);
            }
        }
        else {
            for (int j = 0; j < this.segments.length; ++j) {
                this.segments[j] = this.createSegment(segmentSize, -1);
            }
        }
    }
    
    boolean evictsBySize() {
        return this.maximumSize != -1;
    }
    
    boolean expires() {
        return this.expiresAfterWrite() || this.expiresAfterAccess();
    }
    
    boolean expiresAfterWrite() {
        return this.expireAfterWriteNanos > 0L;
    }
    
    boolean expiresAfterAccess() {
        return this.expireAfterAccessNanos > 0L;
    }
    
    boolean usesKeyReferences() {
        return this.keyStrength != Strength.STRONG;
    }
    
    boolean usesValueReferences() {
        return this.valueStrength != Strength.STRONG;
    }
    
    static <K, V> ValueReference<K, V> unset() {
        return (ValueReference<K, V>)MapMakerInternalMap.UNSET;
    }
    
    static <K, V> ReferenceEntry<K, V> nullEntry() {
        return (ReferenceEntry<K, V>)NullEntry.INSTANCE;
    }
    
    static <E> Queue<E> discardingQueue() {
        return (Queue<E>)MapMakerInternalMap.DISCARDING_QUEUE;
    }
    
    static int rehash(int h) {
        h += (h << 15 ^ 0xFFFFCD7D);
        h ^= h >>> 10;
        h += h << 3;
        h ^= h >>> 6;
        h += (h << 2) + (h << 14);
        return h ^ h >>> 16;
    }
    
    @GuardedBy("Segment.this")
    @VisibleForTesting
    ReferenceEntry<K, V> newEntry(final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
        return this.segmentFor(hash).newEntry(key, hash, next);
    }
    
    @GuardedBy("Segment.this")
    @VisibleForTesting
    ReferenceEntry<K, V> copyEntry(final ReferenceEntry<K, V> original, final ReferenceEntry<K, V> newNext) {
        final int hash = original.getHash();
        return this.segmentFor(hash).copyEntry(original, newNext);
    }
    
    @GuardedBy("Segment.this")
    @VisibleForTesting
    ValueReference<K, V> newValueReference(final ReferenceEntry<K, V> entry, final V value) {
        final int hash = entry.getHash();
        return this.valueStrength.referenceValue(this.segmentFor(hash), entry, value);
    }
    
    int hash(final Object key) {
        final int h = this.keyEquivalence.hash(key);
        return rehash(h);
    }
    
    void reclaimValue(final ValueReference<K, V> valueReference) {
        final ReferenceEntry<K, V> entry = valueReference.getEntry();
        final int hash = entry.getHash();
        this.segmentFor(hash).reclaimValue(entry.getKey(), hash, valueReference);
    }
    
    void reclaimKey(final ReferenceEntry<K, V> entry) {
        final int hash = entry.getHash();
        this.segmentFor(hash).reclaimKey(entry, hash);
    }
    
    @VisibleForTesting
    boolean isLive(final ReferenceEntry<K, V> entry) {
        return this.segmentFor(entry.getHash()).getLiveValue(entry) != null;
    }
    
    Segment<K, V> segmentFor(final int hash) {
        return this.segments[hash >>> this.segmentShift & this.segmentMask];
    }
    
    Segment<K, V> createSegment(final int initialCapacity, final int maxSegmentSize) {
        return new Segment<K, V>(this, initialCapacity, maxSegmentSize);
    }
    
    V getLiveValue(final ReferenceEntry<K, V> entry) {
        if (entry.getKey() == null) {
            return null;
        }
        final V value = entry.getValueReference().get();
        if (value == null) {
            return null;
        }
        if (this.expires() && this.isExpired(entry)) {
            return null;
        }
        return value;
    }
    
    boolean isExpired(final ReferenceEntry<K, V> entry) {
        return this.isExpired(entry, this.ticker.read());
    }
    
    boolean isExpired(final ReferenceEntry<K, V> entry, final long now) {
        return now - entry.getExpirationTime() > 0L;
    }
    
    @GuardedBy("Segment.this")
    static <K, V> void connectExpirables(final ReferenceEntry<K, V> previous, final ReferenceEntry<K, V> next) {
        previous.setNextExpirable(next);
        next.setPreviousExpirable(previous);
    }
    
    @GuardedBy("Segment.this")
    static <K, V> void nullifyExpirable(final ReferenceEntry<K, V> nulled) {
        final ReferenceEntry<K, V> nullEntry = nullEntry();
        nulled.setNextExpirable(nullEntry);
        nulled.setPreviousExpirable(nullEntry);
    }
    
    void processPendingNotifications() {
        MapMaker.RemovalNotification<K, V> notification;
        while ((notification = this.removalNotificationQueue.poll()) != null) {
            try {
                this.removalListener.onRemoval(notification);
            }
            catch (Exception e) {
                MapMakerInternalMap.logger.log(Level.WARNING, "Exception thrown by removal listener", e);
            }
        }
    }
    
    @GuardedBy("Segment.this")
    static <K, V> void connectEvictables(final ReferenceEntry<K, V> previous, final ReferenceEntry<K, V> next) {
        previous.setNextEvictable(next);
        next.setPreviousEvictable(previous);
    }
    
    @GuardedBy("Segment.this")
    static <K, V> void nullifyEvictable(final ReferenceEntry<K, V> nulled) {
        final ReferenceEntry<K, V> nullEntry = nullEntry();
        nulled.setNextEvictable(nullEntry);
        nulled.setPreviousEvictable(nullEntry);
    }
    
    final Segment<K, V>[] newSegmentArray(final int ssize) {
        return (Segment<K, V>[])new Segment[ssize];
    }
    
    @Override
    public boolean isEmpty() {
        long sum = 0L;
        final Segment<K, V>[] segments = this.segments;
        for (int i = 0; i < segments.length; ++i) {
            if (segments[i].count != 0) {
                return false;
            }
            sum += segments[i].modCount;
        }
        if (sum != 0L) {
            for (int i = 0; i < segments.length; ++i) {
                if (segments[i].count != 0) {
                    return false;
                }
                sum -= segments[i].modCount;
            }
            if (sum != 0L) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int size() {
        final Segment<K, V>[] segments = this.segments;
        long sum = 0L;
        for (int i = 0; i < segments.length; ++i) {
            sum += segments[i].count;
        }
        return Ints.saturatedCast(sum);
    }
    
    @Override
    public V get(@Nullable final Object key) {
        if (key == null) {
            return null;
        }
        final int hash = this.hash(key);
        return this.segmentFor(hash).get(key, hash);
    }
    
    ReferenceEntry<K, V> getEntry(@Nullable final Object key) {
        if (key == null) {
            return null;
        }
        final int hash = this.hash(key);
        return this.segmentFor(hash).getEntry(key, hash);
    }
    
    @Override
    public boolean containsKey(@Nullable final Object key) {
        if (key == null) {
            return false;
        }
        final int hash = this.hash(key);
        return this.segmentFor(hash).containsKey(key, hash);
    }
    
    @Override
    public boolean containsValue(@Nullable final Object value) {
        if (value == null) {
            return false;
        }
        final Segment<K, V>[] segments = this.segments;
        long last = -1L;
        for (int i = 0; i < 3; ++i) {
            long sum = 0L;
            for (final Segment<K, V> segment : segments) {
                final int c = segment.count;
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = segment.table;
                for (int j = 0; j < table.length(); ++j) {
                    for (ReferenceEntry<K, V> e = table.get(j); e != null; e = e.getNext()) {
                        final V v = segment.getLiveValue(e);
                        if (v != null && this.valueEquivalence.equivalent(value, v)) {
                            return true;
                        }
                    }
                }
                sum += segment.modCount;
            }
            if (sum == last) {
                break;
            }
            last = sum;
        }
        return false;
    }
    
    @Override
    public V put(final K key, final V value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        final int hash = this.hash(key);
        return this.segmentFor(hash).put(key, hash, value, false);
    }
    
    @Override
    public V putIfAbsent(final K key, final V value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        final int hash = this.hash(key);
        return this.segmentFor(hash).put(key, hash, value, true);
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        for (final Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            this.put(e.getKey(), e.getValue());
        }
    }
    
    @Override
    public V remove(@Nullable final Object key) {
        if (key == null) {
            return null;
        }
        final int hash = this.hash(key);
        return this.segmentFor(hash).remove(key, hash);
    }
    
    @Override
    public boolean remove(@Nullable final Object key, @Nullable final Object value) {
        if (key == null || value == null) {
            return false;
        }
        final int hash = this.hash(key);
        return this.segmentFor(hash).remove(key, hash, value);
    }
    
    @Override
    public boolean replace(final K key, @Nullable final V oldValue, final V newValue) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(newValue);
        if (oldValue == null) {
            return false;
        }
        final int hash = this.hash(key);
        return this.segmentFor(hash).replace(key, hash, oldValue, newValue);
    }
    
    @Override
    public V replace(final K key, final V value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        final int hash = this.hash(key);
        return this.segmentFor(hash).replace(key, hash, value);
    }
    
    @Override
    public void clear() {
        for (final Segment<K, V> segment : this.segments) {
            segment.clear();
        }
    }
    
    @Override
    public Set<K> keySet() {
        final Set<K> ks = this.keySet;
        return (ks != null) ? ks : (this.keySet = new KeySet());
    }
    
    @Override
    public Collection<V> values() {
        final Collection<V> vs = this.values;
        return (vs != null) ? vs : (this.values = new Values());
    }
    
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        final Set<Map.Entry<K, V>> es = this.entrySet;
        return (es != null) ? es : (this.entrySet = new EntrySet());
    }
    
    Object writeReplace() {
        return new SerializationProxy(this.keyStrength, this.valueStrength, this.keyEquivalence, this.valueEquivalence, this.expireAfterWriteNanos, this.expireAfterAccessNanos, this.maximumSize, this.concurrencyLevel, (MapMaker.RemovalListener<? super Object, ? super Object>)this.removalListener, (ConcurrentMap<Object, Object>)this);
    }
    
    static {
        logger = Logger.getLogger(MapMakerInternalMap.class.getName());
        UNSET = new ValueReference<Object, Object>() {
            @Override
            public Object get() {
                return null;
            }
            
            @Override
            public ReferenceEntry<Object, Object> getEntry() {
                return null;
            }
            
            @Override
            public ValueReference<Object, Object> copyFor(final ReferenceQueue<Object> queue, @Nullable final Object value, final ReferenceEntry<Object, Object> entry) {
                return this;
            }
            
            @Override
            public boolean isComputingReference() {
                return false;
            }
            
            @Override
            public Object waitForValue() {
                return null;
            }
            
            @Override
            public void clear(final ValueReference<Object, Object> newValue) {
            }
        };
        DISCARDING_QUEUE = new AbstractQueue<Object>() {
            @Override
            public boolean offer(final Object o) {
                return true;
            }
            
            @Override
            public Object peek() {
                return null;
            }
            
            @Override
            public Object poll() {
                return null;
            }
            
            @Override
            public int size() {
                return 0;
            }
            
            @Override
            public Iterator<Object> iterator() {
                return Iterators.emptyIterator();
            }
        };
    }
    
    enum Strength
    {
        STRONG {
            @Override
             <K, V> ValueReference<K, V> referenceValue(final Segment<K, V> segment, final ReferenceEntry<K, V> entry, final V value) {
                return new StrongValueReference<K, V>(value);
            }
            
            @Override
            Equivalence<Object> defaultEquivalence() {
                return Equivalence.equals();
            }
        }, 
        SOFT {
            @Override
             <K, V> ValueReference<K, V> referenceValue(final Segment<K, V> segment, final ReferenceEntry<K, V> entry, final V value) {
                return new SoftValueReference<K, V>(segment.valueReferenceQueue, value, entry);
            }
            
            @Override
            Equivalence<Object> defaultEquivalence() {
                return Equivalence.identity();
            }
        }, 
        WEAK {
            @Override
             <K, V> ValueReference<K, V> referenceValue(final Segment<K, V> segment, final ReferenceEntry<K, V> entry, final V value) {
                return new WeakValueReference<K, V>(segment.valueReferenceQueue, value, entry);
            }
            
            @Override
            Equivalence<Object> defaultEquivalence() {
                return Equivalence.identity();
            }
        };
        
        abstract <K, V> ValueReference<K, V> referenceValue(final Segment<K, V> p0, final ReferenceEntry<K, V> p1, final V p2);
        
        abstract Equivalence<Object> defaultEquivalence();
    }
    
    enum EntryFactory
    {
        STRONG {
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
                return new StrongEntry<K, V>(key, hash, next);
            }
        }, 
        STRONG_EXPIRABLE {
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
                return new StrongExpirableEntry<K, V>(key, hash, next);
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> original, final ReferenceEntry<K, V> newNext) {
                final ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
                this.copyExpirableEntry(original, newEntry);
                return newEntry;
            }
        }, 
        STRONG_EVICTABLE {
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
                return new StrongEvictableEntry<K, V>(key, hash, next);
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> original, final ReferenceEntry<K, V> newNext) {
                final ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
                this.copyEvictableEntry(original, newEntry);
                return newEntry;
            }
        }, 
        STRONG_EXPIRABLE_EVICTABLE {
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
                return new StrongExpirableEvictableEntry<K, V>(key, hash, next);
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> original, final ReferenceEntry<K, V> newNext) {
                final ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
                this.copyExpirableEntry(original, newEntry);
                this.copyEvictableEntry(original, newEntry);
                return newEntry;
            }
        }, 
        WEAK {
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
                return new WeakEntry<K, V>(segment.keyReferenceQueue, key, hash, next);
            }
        }, 
        WEAK_EXPIRABLE {
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
                return new WeakExpirableEntry<K, V>(segment.keyReferenceQueue, key, hash, next);
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> original, final ReferenceEntry<K, V> newNext) {
                final ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
                this.copyExpirableEntry(original, newEntry);
                return newEntry;
            }
        }, 
        WEAK_EVICTABLE {
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
                return new WeakEvictableEntry<K, V>(segment.keyReferenceQueue, key, hash, next);
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> original, final ReferenceEntry<K, V> newNext) {
                final ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
                this.copyEvictableEntry(original, newEntry);
                return newEntry;
            }
        }, 
        WEAK_EXPIRABLE_EVICTABLE {
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
                return new WeakExpirableEvictableEntry<K, V>(segment.keyReferenceQueue, key, hash, next);
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> original, final ReferenceEntry<K, V> newNext) {
                final ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
                this.copyExpirableEntry(original, newEntry);
                this.copyEvictableEntry(original, newEntry);
                return newEntry;
            }
        };
        
        static final int EXPIRABLE_MASK = 1;
        static final int EVICTABLE_MASK = 2;
        static final EntryFactory[][] factories;
        
        static EntryFactory getFactory(final Strength keyStrength, final boolean expireAfterWrite, final boolean evictsBySize) {
            final int flags = (expireAfterWrite ? 1 : 0) | (evictsBySize ? 2 : 0);
            return EntryFactory.factories[keyStrength.ordinal()][flags];
        }
        
        abstract <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> p0, final K p1, final int p2, @Nullable final ReferenceEntry<K, V> p3);
        
        @GuardedBy("Segment.this")
         <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> original, final ReferenceEntry<K, V> newNext) {
            return this.newEntry(segment, original.getKey(), original.getHash(), newNext);
        }
        
        @GuardedBy("Segment.this")
         <K, V> void copyExpirableEntry(final ReferenceEntry<K, V> original, final ReferenceEntry<K, V> newEntry) {
            newEntry.setExpirationTime(original.getExpirationTime());
            MapMakerInternalMap.connectExpirables(original.getPreviousExpirable(), newEntry);
            MapMakerInternalMap.connectExpirables(newEntry, original.getNextExpirable());
            MapMakerInternalMap.nullifyExpirable(original);
        }
        
        @GuardedBy("Segment.this")
         <K, V> void copyEvictableEntry(final ReferenceEntry<K, V> original, final ReferenceEntry<K, V> newEntry) {
            MapMakerInternalMap.connectEvictables(original.getPreviousEvictable(), newEntry);
            MapMakerInternalMap.connectEvictables(newEntry, original.getNextEvictable());
            MapMakerInternalMap.nullifyEvictable(original);
        }
        
        static {
            factories = new EntryFactory[][] { { EntryFactory.STRONG, EntryFactory.STRONG_EXPIRABLE, EntryFactory.STRONG_EVICTABLE, EntryFactory.STRONG_EXPIRABLE_EVICTABLE }, new EntryFactory[0], { EntryFactory.WEAK, EntryFactory.WEAK_EXPIRABLE, EntryFactory.WEAK_EVICTABLE, EntryFactory.WEAK_EXPIRABLE_EVICTABLE } };
        }
    }
    
    private enum NullEntry implements ReferenceEntry<Object, Object>
    {
        INSTANCE;
        
        @Override
        public ValueReference<Object, Object> getValueReference() {
            return null;
        }
        
        @Override
        public void setValueReference(final ValueReference<Object, Object> valueReference) {
        }
        
        @Override
        public ReferenceEntry<Object, Object> getNext() {
            return null;
        }
        
        @Override
        public int getHash() {
            return 0;
        }
        
        @Override
        public Object getKey() {
            return null;
        }
        
        @Override
        public long getExpirationTime() {
            return 0L;
        }
        
        @Override
        public void setExpirationTime(final long time) {
        }
        
        @Override
        public ReferenceEntry<Object, Object> getNextExpirable() {
            return this;
        }
        
        @Override
        public void setNextExpirable(final ReferenceEntry<Object, Object> next) {
        }
        
        @Override
        public ReferenceEntry<Object, Object> getPreviousExpirable() {
            return this;
        }
        
        @Override
        public void setPreviousExpirable(final ReferenceEntry<Object, Object> previous) {
        }
        
        @Override
        public ReferenceEntry<Object, Object> getNextEvictable() {
            return this;
        }
        
        @Override
        public void setNextEvictable(final ReferenceEntry<Object, Object> next) {
        }
        
        @Override
        public ReferenceEntry<Object, Object> getPreviousEvictable() {
            return this;
        }
        
        @Override
        public void setPreviousEvictable(final ReferenceEntry<Object, Object> previous) {
        }
    }
    
    abstract static class AbstractReferenceEntry<K, V> implements ReferenceEntry<K, V>
    {
        @Override
        public ValueReference<K, V> getValueReference() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setValueReference(final ValueReference<K, V> valueReference) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getNext() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int getHash() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public K getKey() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long getExpirationTime() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setExpirationTime(final long time) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setNextExpirable(final ReferenceEntry<K, V> next) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setPreviousExpirable(final ReferenceEntry<K, V> previous) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setNextEvictable(final ReferenceEntry<K, V> next) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setPreviousEvictable(final ReferenceEntry<K, V> previous) {
            throw new UnsupportedOperationException();
        }
    }
    
    static class StrongEntry<K, V> implements ReferenceEntry<K, V>
    {
        final K key;
        final int hash;
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference;
        
        StrongEntry(final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
            this.valueReference = MapMakerInternalMap.unset();
            this.key = key;
            this.hash = hash;
            this.next = next;
        }
        
        @Override
        public K getKey() {
            return this.key;
        }
        
        @Override
        public long getExpirationTime() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setExpirationTime(final long time) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setNextExpirable(final ReferenceEntry<K, V> next) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setPreviousExpirable(final ReferenceEntry<K, V> previous) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setNextEvictable(final ReferenceEntry<K, V> next) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setPreviousEvictable(final ReferenceEntry<K, V> previous) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }
        
        @Override
        public void setValueReference(final ValueReference<K, V> valueReference) {
            final ValueReference<K, V> previous = this.valueReference;
            previous.clear(this.valueReference = valueReference);
        }
        
        @Override
        public int getHash() {
            return this.hash;
        }
        
        @Override
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }
    
    static final class StrongExpirableEntry<K, V> extends StrongEntry<K, V> implements ReferenceEntry<K, V>
    {
        volatile long time;
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> nextExpirable;
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> previousExpirable;
        
        StrongExpirableEntry(final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
            super(key, hash, next);
            this.time = Long.MAX_VALUE;
            this.nextExpirable = MapMakerInternalMap.nullEntry();
            this.previousExpirable = MapMakerInternalMap.nullEntry();
        }
        
        @Override
        public long getExpirationTime() {
            return this.time;
        }
        
        @Override
        public void setExpirationTime(final long time) {
            this.time = time;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }
        
        @Override
        public void setNextExpirable(final ReferenceEntry<K, V> next) {
            this.nextExpirable = next;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }
        
        @Override
        public void setPreviousExpirable(final ReferenceEntry<K, V> previous) {
            this.previousExpirable = previous;
        }
    }
    
    static final class StrongEvictableEntry<K, V> extends StrongEntry<K, V> implements ReferenceEntry<K, V>
    {
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> nextEvictable;
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> previousEvictable;
        
        StrongEvictableEntry(final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
            super(key, hash, next);
            this.nextEvictable = MapMakerInternalMap.nullEntry();
            this.previousEvictable = MapMakerInternalMap.nullEntry();
        }
        
        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }
        
        @Override
        public void setNextEvictable(final ReferenceEntry<K, V> next) {
            this.nextEvictable = next;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }
        
        @Override
        public void setPreviousEvictable(final ReferenceEntry<K, V> previous) {
            this.previousEvictable = previous;
        }
    }
    
    static final class StrongExpirableEvictableEntry<K, V> extends StrongEntry<K, V> implements ReferenceEntry<K, V>
    {
        volatile long time;
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> nextExpirable;
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> previousExpirable;
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> nextEvictable;
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> previousEvictable;
        
        StrongExpirableEvictableEntry(final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
            super(key, hash, next);
            this.time = Long.MAX_VALUE;
            this.nextExpirable = MapMakerInternalMap.nullEntry();
            this.previousExpirable = MapMakerInternalMap.nullEntry();
            this.nextEvictable = MapMakerInternalMap.nullEntry();
            this.previousEvictable = MapMakerInternalMap.nullEntry();
        }
        
        @Override
        public long getExpirationTime() {
            return this.time;
        }
        
        @Override
        public void setExpirationTime(final long time) {
            this.time = time;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }
        
        @Override
        public void setNextExpirable(final ReferenceEntry<K, V> next) {
            this.nextExpirable = next;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }
        
        @Override
        public void setPreviousExpirable(final ReferenceEntry<K, V> previous) {
            this.previousExpirable = previous;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }
        
        @Override
        public void setNextEvictable(final ReferenceEntry<K, V> next) {
            this.nextEvictable = next;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }
        
        @Override
        public void setPreviousEvictable(final ReferenceEntry<K, V> previous) {
            this.previousEvictable = previous;
        }
    }
    
    static class SoftEntry<K, V> extends SoftReference<K> implements ReferenceEntry<K, V>
    {
        final int hash;
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference;
        
        SoftEntry(final ReferenceQueue<K> queue, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
            super(key, queue);
            this.valueReference = MapMakerInternalMap.unset();
            this.hash = hash;
            this.next = next;
        }
        
        @Override
        public K getKey() {
            return this.get();
        }
        
        @Override
        public long getExpirationTime() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setExpirationTime(final long time) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setNextExpirable(final ReferenceEntry<K, V> next) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setPreviousExpirable(final ReferenceEntry<K, V> previous) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setNextEvictable(final ReferenceEntry<K, V> next) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setPreviousEvictable(final ReferenceEntry<K, V> previous) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }
        
        @Override
        public void setValueReference(final ValueReference<K, V> valueReference) {
            final ValueReference<K, V> previous = this.valueReference;
            previous.clear(this.valueReference = valueReference);
        }
        
        @Override
        public int getHash() {
            return this.hash;
        }
        
        @Override
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }
    
    static final class SoftExpirableEntry<K, V> extends SoftEntry<K, V> implements ReferenceEntry<K, V>
    {
        volatile long time;
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> nextExpirable;
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> previousExpirable;
        
        SoftExpirableEntry(final ReferenceQueue<K> queue, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
            super(queue, key, hash, next);
            this.time = Long.MAX_VALUE;
            this.nextExpirable = MapMakerInternalMap.nullEntry();
            this.previousExpirable = MapMakerInternalMap.nullEntry();
        }
        
        @Override
        public long getExpirationTime() {
            return this.time;
        }
        
        @Override
        public void setExpirationTime(final long time) {
            this.time = time;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }
        
        @Override
        public void setNextExpirable(final ReferenceEntry<K, V> next) {
            this.nextExpirable = next;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }
        
        @Override
        public void setPreviousExpirable(final ReferenceEntry<K, V> previous) {
            this.previousExpirable = previous;
        }
    }
    
    static final class SoftEvictableEntry<K, V> extends SoftEntry<K, V> implements ReferenceEntry<K, V>
    {
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> nextEvictable;
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> previousEvictable;
        
        SoftEvictableEntry(final ReferenceQueue<K> queue, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
            super(queue, key, hash, next);
            this.nextEvictable = MapMakerInternalMap.nullEntry();
            this.previousEvictable = MapMakerInternalMap.nullEntry();
        }
        
        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }
        
        @Override
        public void setNextEvictable(final ReferenceEntry<K, V> next) {
            this.nextEvictable = next;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }
        
        @Override
        public void setPreviousEvictable(final ReferenceEntry<K, V> previous) {
            this.previousEvictable = previous;
        }
    }
    
    static final class SoftExpirableEvictableEntry<K, V> extends SoftEntry<K, V> implements ReferenceEntry<K, V>
    {
        volatile long time;
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> nextExpirable;
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> previousExpirable;
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> nextEvictable;
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> previousEvictable;
        
        SoftExpirableEvictableEntry(final ReferenceQueue<K> queue, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
            super(queue, key, hash, next);
            this.time = Long.MAX_VALUE;
            this.nextExpirable = MapMakerInternalMap.nullEntry();
            this.previousExpirable = MapMakerInternalMap.nullEntry();
            this.nextEvictable = MapMakerInternalMap.nullEntry();
            this.previousEvictable = MapMakerInternalMap.nullEntry();
        }
        
        @Override
        public long getExpirationTime() {
            return this.time;
        }
        
        @Override
        public void setExpirationTime(final long time) {
            this.time = time;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }
        
        @Override
        public void setNextExpirable(final ReferenceEntry<K, V> next) {
            this.nextExpirable = next;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }
        
        @Override
        public void setPreviousExpirable(final ReferenceEntry<K, V> previous) {
            this.previousExpirable = previous;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }
        
        @Override
        public void setNextEvictable(final ReferenceEntry<K, V> next) {
            this.nextEvictable = next;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }
        
        @Override
        public void setPreviousEvictable(final ReferenceEntry<K, V> previous) {
            this.previousEvictable = previous;
        }
    }
    
    static class WeakEntry<K, V> extends WeakReference<K> implements ReferenceEntry<K, V>
    {
        final int hash;
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference;
        
        WeakEntry(final ReferenceQueue<K> queue, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
            super(key, queue);
            this.valueReference = MapMakerInternalMap.unset();
            this.hash = hash;
            this.next = next;
        }
        
        @Override
        public K getKey() {
            return this.get();
        }
        
        @Override
        public long getExpirationTime() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setExpirationTime(final long time) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setNextExpirable(final ReferenceEntry<K, V> next) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setPreviousExpirable(final ReferenceEntry<K, V> previous) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setNextEvictable(final ReferenceEntry<K, V> next) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setPreviousEvictable(final ReferenceEntry<K, V> previous) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }
        
        @Override
        public void setValueReference(final ValueReference<K, V> valueReference) {
            final ValueReference<K, V> previous = this.valueReference;
            previous.clear(this.valueReference = valueReference);
        }
        
        @Override
        public int getHash() {
            return this.hash;
        }
        
        @Override
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }
    
    static final class WeakExpirableEntry<K, V> extends WeakEntry<K, V> implements ReferenceEntry<K, V>
    {
        volatile long time;
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> nextExpirable;
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> previousExpirable;
        
        WeakExpirableEntry(final ReferenceQueue<K> queue, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
            super(queue, key, hash, next);
            this.time = Long.MAX_VALUE;
            this.nextExpirable = MapMakerInternalMap.nullEntry();
            this.previousExpirable = MapMakerInternalMap.nullEntry();
        }
        
        @Override
        public long getExpirationTime() {
            return this.time;
        }
        
        @Override
        public void setExpirationTime(final long time) {
            this.time = time;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }
        
        @Override
        public void setNextExpirable(final ReferenceEntry<K, V> next) {
            this.nextExpirable = next;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }
        
        @Override
        public void setPreviousExpirable(final ReferenceEntry<K, V> previous) {
            this.previousExpirable = previous;
        }
    }
    
    static final class WeakEvictableEntry<K, V> extends WeakEntry<K, V> implements ReferenceEntry<K, V>
    {
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> nextEvictable;
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> previousEvictable;
        
        WeakEvictableEntry(final ReferenceQueue<K> queue, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
            super(queue, key, hash, next);
            this.nextEvictable = MapMakerInternalMap.nullEntry();
            this.previousEvictable = MapMakerInternalMap.nullEntry();
        }
        
        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }
        
        @Override
        public void setNextEvictable(final ReferenceEntry<K, V> next) {
            this.nextEvictable = next;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }
        
        @Override
        public void setPreviousEvictable(final ReferenceEntry<K, V> previous) {
            this.previousEvictable = previous;
        }
    }
    
    static final class WeakExpirableEvictableEntry<K, V> extends WeakEntry<K, V> implements ReferenceEntry<K, V>
    {
        volatile long time;
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> nextExpirable;
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> previousExpirable;
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> nextEvictable;
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> previousEvictable;
        
        WeakExpirableEvictableEntry(final ReferenceQueue<K> queue, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
            super(queue, key, hash, next);
            this.time = Long.MAX_VALUE;
            this.nextExpirable = MapMakerInternalMap.nullEntry();
            this.previousExpirable = MapMakerInternalMap.nullEntry();
            this.nextEvictable = MapMakerInternalMap.nullEntry();
            this.previousEvictable = MapMakerInternalMap.nullEntry();
        }
        
        @Override
        public long getExpirationTime() {
            return this.time;
        }
        
        @Override
        public void setExpirationTime(final long time) {
            this.time = time;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }
        
        @Override
        public void setNextExpirable(final ReferenceEntry<K, V> next) {
            this.nextExpirable = next;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }
        
        @Override
        public void setPreviousExpirable(final ReferenceEntry<K, V> previous) {
            this.previousExpirable = previous;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }
        
        @Override
        public void setNextEvictable(final ReferenceEntry<K, V> next) {
            this.nextEvictable = next;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }
        
        @Override
        public void setPreviousEvictable(final ReferenceEntry<K, V> previous) {
            this.previousEvictable = previous;
        }
    }
    
    static final class WeakValueReference<K, V> extends WeakReference<V> implements ValueReference<K, V>
    {
        final ReferenceEntry<K, V> entry;
        
        WeakValueReference(final ReferenceQueue<V> queue, final V referent, final ReferenceEntry<K, V> entry) {
            super(referent, queue);
            this.entry = entry;
        }
        
        @Override
        public ReferenceEntry<K, V> getEntry() {
            return this.entry;
        }
        
        @Override
        public void clear(final ValueReference<K, V> newValue) {
            this.clear();
        }
        
        @Override
        public ValueReference<K, V> copyFor(final ReferenceQueue<V> queue, final V value, final ReferenceEntry<K, V> entry) {
            return new WeakValueReference((ReferenceQueue<Object>)queue, value, (ReferenceEntry<Object, Object>)entry);
        }
        
        @Override
        public boolean isComputingReference() {
            return false;
        }
        
        @Override
        public V waitForValue() {
            return this.get();
        }
    }
    
    static final class SoftValueReference<K, V> extends SoftReference<V> implements ValueReference<K, V>
    {
        final ReferenceEntry<K, V> entry;
        
        SoftValueReference(final ReferenceQueue<V> queue, final V referent, final ReferenceEntry<K, V> entry) {
            super(referent, queue);
            this.entry = entry;
        }
        
        @Override
        public ReferenceEntry<K, V> getEntry() {
            return this.entry;
        }
        
        @Override
        public void clear(final ValueReference<K, V> newValue) {
            this.clear();
        }
        
        @Override
        public ValueReference<K, V> copyFor(final ReferenceQueue<V> queue, final V value, final ReferenceEntry<K, V> entry) {
            return new SoftValueReference((ReferenceQueue<Object>)queue, value, (ReferenceEntry<Object, Object>)entry);
        }
        
        @Override
        public boolean isComputingReference() {
            return false;
        }
        
        @Override
        public V waitForValue() {
            return this.get();
        }
    }
    
    static final class StrongValueReference<K, V> implements ValueReference<K, V>
    {
        final V referent;
        
        StrongValueReference(final V referent) {
            this.referent = referent;
        }
        
        @Override
        public V get() {
            return this.referent;
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
    
    static class Segment<K, V> extends ReentrantLock
    {
        final MapMakerInternalMap<K, V> map;
        volatile int count;
        int modCount;
        int threshold;
        volatile AtomicReferenceArray<ReferenceEntry<K, V>> table;
        final int maxSegmentSize;
        final ReferenceQueue<K> keyReferenceQueue;
        final ReferenceQueue<V> valueReferenceQueue;
        final Queue<ReferenceEntry<K, V>> recencyQueue;
        final AtomicInteger readCount;
        @GuardedBy("Segment.this")
        final Queue<ReferenceEntry<K, V>> evictionQueue;
        @GuardedBy("Segment.this")
        final Queue<ReferenceEntry<K, V>> expirationQueue;
        
        Segment(final MapMakerInternalMap<K, V> map, final int initialCapacity, final int maxSegmentSize) {
            this.readCount = new AtomicInteger();
            this.map = map;
            this.maxSegmentSize = maxSegmentSize;
            this.initTable(this.newEntryArray(initialCapacity));
            this.keyReferenceQueue = (map.usesKeyReferences() ? new ReferenceQueue<K>() : null);
            this.valueReferenceQueue = (map.usesValueReferences() ? new ReferenceQueue<V>() : null);
            this.recencyQueue = ((map.evictsBySize() || map.expiresAfterAccess()) ? new ConcurrentLinkedQueue<ReferenceEntry<K, V>>() : MapMakerInternalMap.discardingQueue());
            this.evictionQueue = (Queue<ReferenceEntry<K, V>>)(map.evictsBySize() ? new EvictionQueue() : MapMakerInternalMap.discardingQueue());
            this.expirationQueue = (Queue<ReferenceEntry<K, V>>)(map.expires() ? new ExpirationQueue() : MapMakerInternalMap.discardingQueue());
        }
        
        AtomicReferenceArray<ReferenceEntry<K, V>> newEntryArray(final int size) {
            return new AtomicReferenceArray<ReferenceEntry<K, V>>(size);
        }
        
        void initTable(final AtomicReferenceArray<ReferenceEntry<K, V>> newTable) {
            this.threshold = newTable.length() * 3 / 4;
            if (this.threshold == this.maxSegmentSize) {
                ++this.threshold;
            }
            this.table = newTable;
        }
        
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> newEntry(final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
            return this.map.entryFactory.newEntry(this, key, hash, next);
        }
        
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> copyEntry(final ReferenceEntry<K, V> original, final ReferenceEntry<K, V> newNext) {
            if (original.getKey() == null) {
                return null;
            }
            final ValueReference<K, V> valueReference = original.getValueReference();
            final V value = valueReference.get();
            if (value == null && !valueReference.isComputingReference()) {
                return null;
            }
            final ReferenceEntry<K, V> newEntry = this.map.entryFactory.copyEntry(this, original, newNext);
            newEntry.setValueReference(valueReference.copyFor(this.valueReferenceQueue, value, newEntry));
            return newEntry;
        }
        
        @GuardedBy("Segment.this")
        void setValue(final ReferenceEntry<K, V> entry, final V value) {
            final ValueReference<K, V> valueReference = this.map.valueStrength.referenceValue(this, entry, value);
            entry.setValueReference(valueReference);
            this.recordWrite(entry);
        }
        
        void tryDrainReferenceQueues() {
            if (this.tryLock()) {
                try {
                    this.drainReferenceQueues();
                }
                finally {
                    this.unlock();
                }
            }
        }
        
        @GuardedBy("Segment.this")
        void drainReferenceQueues() {
            if (this.map.usesKeyReferences()) {
                this.drainKeyReferenceQueue();
            }
            if (this.map.usesValueReferences()) {
                this.drainValueReferenceQueue();
            }
        }
        
        @GuardedBy("Segment.this")
        void drainKeyReferenceQueue() {
            int i = 0;
            Reference<? extends K> ref;
            while ((ref = this.keyReferenceQueue.poll()) != null) {
                final ReferenceEntry<K, V> entry = (ReferenceEntry<K, V>)(ReferenceEntry)ref;
                this.map.reclaimKey(entry);
                if (++i == 16) {
                    break;
                }
            }
        }
        
        @GuardedBy("Segment.this")
        void drainValueReferenceQueue() {
            int i = 0;
            Reference<? extends V> ref;
            while ((ref = this.valueReferenceQueue.poll()) != null) {
                final ValueReference<K, V> valueReference = (ValueReference<K, V>)(ValueReference)ref;
                this.map.reclaimValue(valueReference);
                if (++i == 16) {
                    break;
                }
            }
        }
        
        void clearReferenceQueues() {
            if (this.map.usesKeyReferences()) {
                this.clearKeyReferenceQueue();
            }
            if (this.map.usesValueReferences()) {
                this.clearValueReferenceQueue();
            }
        }
        
        void clearKeyReferenceQueue() {
            while (this.keyReferenceQueue.poll() != null) {}
        }
        
        void clearValueReferenceQueue() {
            while (this.valueReferenceQueue.poll() != null) {}
        }
        
        void recordRead(final ReferenceEntry<K, V> entry) {
            if (this.map.expiresAfterAccess()) {
                this.recordExpirationTime(entry, this.map.expireAfterAccessNanos);
            }
            this.recencyQueue.add(entry);
        }
        
        @GuardedBy("Segment.this")
        void recordLockedRead(final ReferenceEntry<K, V> entry) {
            this.evictionQueue.add(entry);
            if (this.map.expiresAfterAccess()) {
                this.recordExpirationTime(entry, this.map.expireAfterAccessNanos);
                this.expirationQueue.add(entry);
            }
        }
        
        @GuardedBy("Segment.this")
        void recordWrite(final ReferenceEntry<K, V> entry) {
            this.drainRecencyQueue();
            this.evictionQueue.add(entry);
            if (this.map.expires()) {
                final long expiration = this.map.expiresAfterAccess() ? this.map.expireAfterAccessNanos : this.map.expireAfterWriteNanos;
                this.recordExpirationTime(entry, expiration);
                this.expirationQueue.add(entry);
            }
        }
        
        @GuardedBy("Segment.this")
        void drainRecencyQueue() {
            ReferenceEntry<K, V> e;
            while ((e = this.recencyQueue.poll()) != null) {
                if (this.evictionQueue.contains(e)) {
                    this.evictionQueue.add(e);
                }
                if (this.map.expiresAfterAccess() && this.expirationQueue.contains(e)) {
                    this.expirationQueue.add(e);
                }
            }
        }
        
        void recordExpirationTime(final ReferenceEntry<K, V> entry, final long expirationNanos) {
            entry.setExpirationTime(this.map.ticker.read() + expirationNanos);
        }
        
        void tryExpireEntries() {
            if (this.tryLock()) {
                try {
                    this.expireEntries();
                }
                finally {
                    this.unlock();
                }
            }
        }
        
        @GuardedBy("Segment.this")
        void expireEntries() {
            this.drainRecencyQueue();
            if (this.expirationQueue.isEmpty()) {
                return;
            }
            final long now = this.map.ticker.read();
            ReferenceEntry<K, V> e;
            while ((e = this.expirationQueue.peek()) != null && this.map.isExpired(e, now)) {
                if (!this.removeEntry(e, e.getHash(), MapMaker.RemovalCause.EXPIRED)) {
                    throw new AssertionError();
                }
            }
        }
        
        void enqueueNotification(final ReferenceEntry<K, V> entry, final MapMaker.RemovalCause cause) {
            this.enqueueNotification(entry.getKey(), entry.getHash(), entry.getValueReference().get(), cause);
        }
        
        void enqueueNotification(@Nullable final K key, final int hash, @Nullable final V value, final MapMaker.RemovalCause cause) {
            if (this.map.removalNotificationQueue != MapMakerInternalMap.DISCARDING_QUEUE) {
                final MapMaker.RemovalNotification<K, V> notification = new MapMaker.RemovalNotification<K, V>(key, value, cause);
                this.map.removalNotificationQueue.offer(notification);
            }
        }
        
        @GuardedBy("Segment.this")
        boolean evictEntries() {
            if (!this.map.evictsBySize() || this.count < this.maxSegmentSize) {
                return false;
            }
            this.drainRecencyQueue();
            final ReferenceEntry<K, V> e = this.evictionQueue.remove();
            if (!this.removeEntry(e, e.getHash(), MapMaker.RemovalCause.SIZE)) {
                throw new AssertionError();
            }
            return true;
        }
        
        ReferenceEntry<K, V> getFirst(final int hash) {
            final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
            return table.get(hash & table.length() - 1);
        }
        
        ReferenceEntry<K, V> getEntry(final Object key, final int hash) {
            if (this.count != 0) {
                for (ReferenceEntry<K, V> e = this.getFirst(hash); e != null; e = e.getNext()) {
                    if (e.getHash() == hash) {
                        final K entryKey = e.getKey();
                        if (entryKey == null) {
                            this.tryDrainReferenceQueues();
                        }
                        else if (this.map.keyEquivalence.equivalent(key, entryKey)) {
                            return e;
                        }
                    }
                }
            }
            return null;
        }
        
        ReferenceEntry<K, V> getLiveEntry(final Object key, final int hash) {
            final ReferenceEntry<K, V> e = this.getEntry(key, hash);
            if (e == null) {
                return null;
            }
            if (this.map.expires() && this.map.isExpired(e)) {
                this.tryExpireEntries();
                return null;
            }
            return e;
        }
        
        V get(final Object key, final int hash) {
            try {
                final ReferenceEntry<K, V> e = this.getLiveEntry(key, hash);
                if (e == null) {
                    return null;
                }
                final V value = e.getValueReference().get();
                if (value != null) {
                    this.recordRead(e);
                }
                else {
                    this.tryDrainReferenceQueues();
                }
                return value;
            }
            finally {
                this.postReadCleanup();
            }
        }
        
        boolean containsKey(final Object key, final int hash) {
            try {
                if (this.count != 0) {
                    final ReferenceEntry<K, V> e = this.getLiveEntry(key, hash);
                    return e != null && e.getValueReference().get() != null;
                }
                return false;
            }
            finally {
                this.postReadCleanup();
            }
        }
        
        @VisibleForTesting
        boolean containsValue(final Object value) {
            try {
                if (this.count != 0) {
                    final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                    for (int length = table.length(), i = 0; i < length; ++i) {
                        for (ReferenceEntry<K, V> e = table.get(i); e != null; e = e.getNext()) {
                            final V entryValue = this.getLiveValue(e);
                            if (entryValue != null) {
                                if (this.map.valueEquivalence.equivalent(value, entryValue)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
                return false;
            }
            finally {
                this.postReadCleanup();
            }
        }
        
        V put(final K key, final int hash, final V value, final boolean onlyIfAbsent) {
            this.lock();
            try {
                this.preWriteCleanup();
                int newCount = this.count + 1;
                if (newCount > this.threshold) {
                    this.expand();
                    newCount = this.count + 1;
                }
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int index = hash & table.length() - 1;
                ReferenceEntry<K, V> e;
                final ReferenceEntry<K, V> first = e = table.get(index);
                while (e != null) {
                    final K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        final ValueReference<K, V> valueReference = e.getValueReference();
                        final V entryValue = valueReference.get();
                        if (entryValue == null) {
                            ++this.modCount;
                            this.setValue(e, value);
                            if (!valueReference.isComputingReference()) {
                                this.enqueueNotification(key, hash, entryValue, MapMaker.RemovalCause.COLLECTED);
                                newCount = this.count;
                            }
                            else if (this.evictEntries()) {
                                newCount = this.count + 1;
                            }
                            this.count = newCount;
                            return null;
                        }
                        if (onlyIfAbsent) {
                            this.recordLockedRead(e);
                            return entryValue;
                        }
                        ++this.modCount;
                        this.enqueueNotification(key, hash, entryValue, MapMaker.RemovalCause.REPLACED);
                        this.setValue(e, value);
                        return entryValue;
                    }
                    else {
                        e = e.getNext();
                    }
                }
                ++this.modCount;
                final ReferenceEntry<K, V> newEntry = this.newEntry(key, hash, first);
                this.setValue(newEntry, value);
                table.set(index, newEntry);
                if (this.evictEntries()) {
                    newCount = this.count + 1;
                }
                this.count = newCount;
                return null;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }
        
        @GuardedBy("Segment.this")
        void expand() {
            final AtomicReferenceArray<ReferenceEntry<K, V>> oldTable = this.table;
            final int oldCapacity = oldTable.length();
            if (oldCapacity >= 1073741824) {
                return;
            }
            int newCount = this.count;
            final AtomicReferenceArray<ReferenceEntry<K, V>> newTable = this.newEntryArray(oldCapacity << 1);
            this.threshold = newTable.length() * 3 / 4;
            final int newMask = newTable.length() - 1;
            for (int oldIndex = 0; oldIndex < oldCapacity; ++oldIndex) {
                final ReferenceEntry<K, V> head = oldTable.get(oldIndex);
                if (head != null) {
                    final ReferenceEntry<K, V> next = head.getNext();
                    final int headIndex = head.getHash() & newMask;
                    if (next == null) {
                        newTable.set(headIndex, head);
                    }
                    else {
                        ReferenceEntry<K, V> tail = head;
                        int tailIndex = headIndex;
                        for (ReferenceEntry<K, V> e = next; e != null; e = e.getNext()) {
                            final int newIndex = e.getHash() & newMask;
                            if (newIndex != tailIndex) {
                                tailIndex = newIndex;
                                tail = e;
                            }
                        }
                        newTable.set(tailIndex, tail);
                        for (ReferenceEntry<K, V> e = head; e != tail; e = e.getNext()) {
                            final int newIndex = e.getHash() & newMask;
                            final ReferenceEntry<K, V> newNext = newTable.get(newIndex);
                            final ReferenceEntry<K, V> newFirst = this.copyEntry(e, newNext);
                            if (newFirst != null) {
                                newTable.set(newIndex, newFirst);
                            }
                            else {
                                this.removeCollectedEntry(e);
                                --newCount;
                            }
                        }
                    }
                }
            }
            this.table = newTable;
            this.count = newCount;
        }
        
        boolean replace(final K key, final int hash, final V oldValue, final V newValue) {
            this.lock();
            try {
                this.preWriteCleanup();
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int index = hash & table.length() - 1;
                ReferenceEntry<K, V> e;
                final ReferenceEntry<K, V> first = e = table.get(index);
                while (e != null) {
                    final K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        final ValueReference<K, V> valueReference = e.getValueReference();
                        final V entryValue = valueReference.get();
                        if (entryValue == null) {
                            if (this.isCollected(valueReference)) {
                                int newCount = this.count - 1;
                                ++this.modCount;
                                this.enqueueNotification(entryKey, hash, entryValue, MapMaker.RemovalCause.COLLECTED);
                                final ReferenceEntry<K, V> newFirst = this.removeFromChain(first, e);
                                newCount = this.count - 1;
                                table.set(index, newFirst);
                                this.count = newCount;
                            }
                            return false;
                        }
                        if (this.map.valueEquivalence.equivalent(oldValue, entryValue)) {
                            ++this.modCount;
                            this.enqueueNotification(key, hash, entryValue, MapMaker.RemovalCause.REPLACED);
                            this.setValue(e, newValue);
                            return true;
                        }
                        this.recordLockedRead(e);
                        return false;
                    }
                    else {
                        e = e.getNext();
                    }
                }
                return false;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }
        
        V replace(final K key, final int hash, final V newValue) {
            this.lock();
            try {
                this.preWriteCleanup();
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int index = hash & table.length() - 1;
                ReferenceEntry<K, V> e;
                final ReferenceEntry<K, V> first = e = table.get(index);
                while (e != null) {
                    final K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        final ValueReference<K, V> valueReference = e.getValueReference();
                        final V entryValue = valueReference.get();
                        if (entryValue == null) {
                            if (this.isCollected(valueReference)) {
                                int newCount = this.count - 1;
                                ++this.modCount;
                                this.enqueueNotification(entryKey, hash, entryValue, MapMaker.RemovalCause.COLLECTED);
                                final ReferenceEntry<K, V> newFirst = this.removeFromChain(first, e);
                                newCount = this.count - 1;
                                table.set(index, newFirst);
                                this.count = newCount;
                            }
                            return null;
                        }
                        ++this.modCount;
                        this.enqueueNotification(key, hash, entryValue, MapMaker.RemovalCause.REPLACED);
                        this.setValue(e, newValue);
                        return entryValue;
                    }
                    else {
                        e = e.getNext();
                    }
                }
                return null;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }
        
        V remove(final Object key, final int hash) {
            this.lock();
            try {
                this.preWriteCleanup();
                int newCount = this.count - 1;
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int index = hash & table.length() - 1;
                ReferenceEntry<K, V> e;
                for (ReferenceEntry<K, V> first = e = table.get(index); e != null; e = e.getNext()) {
                    final K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        final ValueReference<K, V> valueReference = e.getValueReference();
                        final V entryValue = valueReference.get();
                        MapMaker.RemovalCause cause;
                        if (entryValue != null) {
                            cause = MapMaker.RemovalCause.EXPLICIT;
                        }
                        else {
                            if (!this.isCollected(valueReference)) {
                                return null;
                            }
                            cause = MapMaker.RemovalCause.COLLECTED;
                        }
                        ++this.modCount;
                        this.enqueueNotification(entryKey, hash, entryValue, cause);
                        final ReferenceEntry<K, V> newFirst = this.removeFromChain(first, e);
                        newCount = this.count - 1;
                        table.set(index, newFirst);
                        this.count = newCount;
                        return entryValue;
                    }
                }
                return null;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }
        
        boolean remove(final Object key, final int hash, final Object value) {
            this.lock();
            try {
                this.preWriteCleanup();
                int newCount = this.count - 1;
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int index = hash & table.length() - 1;
                ReferenceEntry<K, V> e;
                for (ReferenceEntry<K, V> first = e = table.get(index); e != null; e = e.getNext()) {
                    final K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        final ValueReference<K, V> valueReference = e.getValueReference();
                        final V entryValue = valueReference.get();
                        MapMaker.RemovalCause cause;
                        if (this.map.valueEquivalence.equivalent(value, entryValue)) {
                            cause = MapMaker.RemovalCause.EXPLICIT;
                        }
                        else {
                            if (!this.isCollected(valueReference)) {
                                return false;
                            }
                            cause = MapMaker.RemovalCause.COLLECTED;
                        }
                        ++this.modCount;
                        this.enqueueNotification(entryKey, hash, entryValue, cause);
                        final ReferenceEntry<K, V> newFirst = this.removeFromChain(first, e);
                        newCount = this.count - 1;
                        table.set(index, newFirst);
                        this.count = newCount;
                        return cause == MapMaker.RemovalCause.EXPLICIT;
                    }
                }
                return false;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }
        
        void clear() {
            if (this.count != 0) {
                this.lock();
                try {
                    final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                    if (this.map.removalNotificationQueue != MapMakerInternalMap.DISCARDING_QUEUE) {
                        for (int i = 0; i < table.length(); ++i) {
                            for (ReferenceEntry<K, V> e = table.get(i); e != null; e = e.getNext()) {
                                if (!e.getValueReference().isComputingReference()) {
                                    this.enqueueNotification(e, MapMaker.RemovalCause.EXPLICIT);
                                }
                            }
                        }
                    }
                    for (int i = 0; i < table.length(); ++i) {
                        table.set(i, null);
                    }
                    this.clearReferenceQueues();
                    this.evictionQueue.clear();
                    this.expirationQueue.clear();
                    this.readCount.set(0);
                    ++this.modCount;
                    this.count = 0;
                }
                finally {
                    this.unlock();
                    this.postWriteCleanup();
                }
            }
        }
        
        @GuardedBy("Segment.this")
        ReferenceEntry<K, V> removeFromChain(final ReferenceEntry<K, V> first, final ReferenceEntry<K, V> entry) {
            this.evictionQueue.remove(entry);
            this.expirationQueue.remove(entry);
            int newCount = this.count;
            ReferenceEntry<K, V> newFirst = entry.getNext();
            for (ReferenceEntry<K, V> e = first; e != entry; e = e.getNext()) {
                final ReferenceEntry<K, V> next = this.copyEntry(e, newFirst);
                if (next != null) {
                    newFirst = next;
                }
                else {
                    this.removeCollectedEntry(e);
                    --newCount;
                }
            }
            this.count = newCount;
            return newFirst;
        }
        
        void removeCollectedEntry(final ReferenceEntry<K, V> entry) {
            this.enqueueNotification(entry, MapMaker.RemovalCause.COLLECTED);
            this.evictionQueue.remove(entry);
            this.expirationQueue.remove(entry);
        }
        
        boolean reclaimKey(final ReferenceEntry<K, V> entry, final int hash) {
            this.lock();
            try {
                int newCount = this.count - 1;
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int index = hash & table.length() - 1;
                ReferenceEntry<K, V> e;
                for (ReferenceEntry<K, V> first = e = table.get(index); e != null; e = e.getNext()) {
                    if (e == entry) {
                        ++this.modCount;
                        this.enqueueNotification(e.getKey(), hash, e.getValueReference().get(), MapMaker.RemovalCause.COLLECTED);
                        final ReferenceEntry<K, V> newFirst = this.removeFromChain(first, e);
                        newCount = this.count - 1;
                        table.set(index, newFirst);
                        this.count = newCount;
                        return true;
                    }
                }
                return false;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }
        
        boolean reclaimValue(final K key, final int hash, final ValueReference<K, V> valueReference) {
            this.lock();
            try {
                int newCount = this.count - 1;
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int index = hash & table.length() - 1;
                ReferenceEntry<K, V> e;
                final ReferenceEntry<K, V> first = e = table.get(index);
                while (e != null) {
                    final K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        final ValueReference<K, V> v = e.getValueReference();
                        if (v == valueReference) {
                            ++this.modCount;
                            this.enqueueNotification(key, hash, valueReference.get(), MapMaker.RemovalCause.COLLECTED);
                            final ReferenceEntry<K, V> newFirst = this.removeFromChain(first, e);
                            newCount = this.count - 1;
                            table.set(index, newFirst);
                            this.count = newCount;
                            return true;
                        }
                        return false;
                    }
                    else {
                        e = e.getNext();
                    }
                }
                return false;
            }
            finally {
                this.unlock();
                if (!this.isHeldByCurrentThread()) {
                    this.postWriteCleanup();
                }
            }
        }
        
        boolean clearValue(final K key, final int hash, final ValueReference<K, V> valueReference) {
            this.lock();
            try {
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int index = hash & table.length() - 1;
                ReferenceEntry<K, V> e;
                final ReferenceEntry<K, V> first = e = table.get(index);
                while (e != null) {
                    final K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        final ValueReference<K, V> v = e.getValueReference();
                        if (v == valueReference) {
                            final ReferenceEntry<K, V> newFirst = this.removeFromChain(first, e);
                            table.set(index, newFirst);
                            return true;
                        }
                        return false;
                    }
                    else {
                        e = e.getNext();
                    }
                }
                return false;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }
        
        @GuardedBy("Segment.this")
        boolean removeEntry(final ReferenceEntry<K, V> entry, final int hash, final MapMaker.RemovalCause cause) {
            int newCount = this.count - 1;
            final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
            final int index = hash & table.length() - 1;
            ReferenceEntry<K, V> e;
            for (ReferenceEntry<K, V> first = e = table.get(index); e != null; e = e.getNext()) {
                if (e == entry) {
                    ++this.modCount;
                    this.enqueueNotification(e.getKey(), hash, e.getValueReference().get(), cause);
                    final ReferenceEntry<K, V> newFirst = this.removeFromChain(first, e);
                    newCount = this.count - 1;
                    table.set(index, newFirst);
                    this.count = newCount;
                    return true;
                }
            }
            return false;
        }
        
        boolean isCollected(final ValueReference<K, V> valueReference) {
            return !valueReference.isComputingReference() && valueReference.get() == null;
        }
        
        V getLiveValue(final ReferenceEntry<K, V> entry) {
            if (entry.getKey() == null) {
                this.tryDrainReferenceQueues();
                return null;
            }
            final V value = entry.getValueReference().get();
            if (value == null) {
                this.tryDrainReferenceQueues();
                return null;
            }
            if (this.map.expires() && this.map.isExpired(entry)) {
                this.tryExpireEntries();
                return null;
            }
            return value;
        }
        
        void postReadCleanup() {
            if ((this.readCount.incrementAndGet() & 0x3F) == 0x0) {
                this.runCleanup();
            }
        }
        
        @GuardedBy("Segment.this")
        void preWriteCleanup() {
            this.runLockedCleanup();
        }
        
        void postWriteCleanup() {
            this.runUnlockedCleanup();
        }
        
        void runCleanup() {
            this.runLockedCleanup();
            this.runUnlockedCleanup();
        }
        
        void runLockedCleanup() {
            if (this.tryLock()) {
                try {
                    this.drainReferenceQueues();
                    this.expireEntries();
                    this.readCount.set(0);
                }
                finally {
                    this.unlock();
                }
            }
        }
        
        void runUnlockedCleanup() {
            if (!this.isHeldByCurrentThread()) {
                this.map.processPendingNotifications();
            }
        }
    }
    
    static final class EvictionQueue<K, V> extends AbstractQueue<ReferenceEntry<K, V>>
    {
        final ReferenceEntry<K, V> head;
        
        EvictionQueue() {
            this.head = new AbstractReferenceEntry<K, V>() {
                ReferenceEntry<K, V> nextEvictable = this;
                ReferenceEntry<K, V> previousEvictable = this;
                
                @Override
                public ReferenceEntry<K, V> getNextEvictable() {
                    return this.nextEvictable;
                }
                
                @Override
                public void setNextEvictable(final ReferenceEntry<K, V> next) {
                    this.nextEvictable = next;
                }
                
                @Override
                public ReferenceEntry<K, V> getPreviousEvictable() {
                    return this.previousEvictable;
                }
                
                @Override
                public void setPreviousEvictable(final ReferenceEntry<K, V> previous) {
                    this.previousEvictable = previous;
                }
            };
        }
        
        @Override
        public boolean offer(final ReferenceEntry<K, V> entry) {
            MapMakerInternalMap.connectEvictables(entry.getPreviousEvictable(), entry.getNextEvictable());
            MapMakerInternalMap.connectEvictables(this.head.getPreviousEvictable(), entry);
            MapMakerInternalMap.connectEvictables(entry, this.head);
            return true;
        }
        
        @Override
        public ReferenceEntry<K, V> peek() {
            final ReferenceEntry<K, V> next = this.head.getNextEvictable();
            return (next == this.head) ? null : next;
        }
        
        @Override
        public ReferenceEntry<K, V> poll() {
            final ReferenceEntry<K, V> next = this.head.getNextEvictable();
            if (next == this.head) {
                return null;
            }
            this.remove(next);
            return next;
        }
        
        @Override
        public boolean remove(final Object o) {
            final ReferenceEntry<K, V> e = (ReferenceEntry<K, V>)o;
            final ReferenceEntry<K, V> previous = e.getPreviousEvictable();
            final ReferenceEntry<K, V> next = e.getNextEvictable();
            MapMakerInternalMap.connectEvictables(previous, next);
            MapMakerInternalMap.nullifyEvictable(e);
            return next != NullEntry.INSTANCE;
        }
        
        @Override
        public boolean contains(final Object o) {
            final ReferenceEntry<K, V> e = (ReferenceEntry<K, V>)o;
            return e.getNextEvictable() != NullEntry.INSTANCE;
        }
        
        @Override
        public boolean isEmpty() {
            return this.head.getNextEvictable() == this.head;
        }
        
        @Override
        public int size() {
            int size = 0;
            for (ReferenceEntry<K, V> e = this.head.getNextEvictable(); e != this.head; e = e.getNextEvictable()) {
                ++size;
            }
            return size;
        }
        
        @Override
        public void clear() {
            ReferenceEntry<K, V> next;
            for (ReferenceEntry<K, V> e = this.head.getNextEvictable(); e != this.head; e = next) {
                next = e.getNextEvictable();
                MapMakerInternalMap.nullifyEvictable(e);
            }
            this.head.setNextEvictable(this.head);
            this.head.setPreviousEvictable(this.head);
        }
        
        @Override
        public Iterator<ReferenceEntry<K, V>> iterator() {
            return new AbstractSequentialIterator<ReferenceEntry<K, V>>(this.peek()) {
                @Override
                protected ReferenceEntry<K, V> computeNext(final ReferenceEntry<K, V> previous) {
                    final ReferenceEntry<K, V> next = previous.getNextEvictable();
                    return (next == EvictionQueue.this.head) ? null : next;
                }
            };
        }
    }
    
    static final class ExpirationQueue<K, V> extends AbstractQueue<ReferenceEntry<K, V>>
    {
        final ReferenceEntry<K, V> head;
        
        ExpirationQueue() {
            this.head = new AbstractReferenceEntry<K, V>() {
                ReferenceEntry<K, V> nextExpirable = this;
                ReferenceEntry<K, V> previousExpirable = this;
                
                @Override
                public long getExpirationTime() {
                    return Long.MAX_VALUE;
                }
                
                @Override
                public void setExpirationTime(final long time) {
                }
                
                @Override
                public ReferenceEntry<K, V> getNextExpirable() {
                    return this.nextExpirable;
                }
                
                @Override
                public void setNextExpirable(final ReferenceEntry<K, V> next) {
                    this.nextExpirable = next;
                }
                
                @Override
                public ReferenceEntry<K, V> getPreviousExpirable() {
                    return this.previousExpirable;
                }
                
                @Override
                public void setPreviousExpirable(final ReferenceEntry<K, V> previous) {
                    this.previousExpirable = previous;
                }
            };
        }
        
        @Override
        public boolean offer(final ReferenceEntry<K, V> entry) {
            MapMakerInternalMap.connectExpirables(entry.getPreviousExpirable(), entry.getNextExpirable());
            MapMakerInternalMap.connectExpirables(this.head.getPreviousExpirable(), entry);
            MapMakerInternalMap.connectExpirables(entry, this.head);
            return true;
        }
        
        @Override
        public ReferenceEntry<K, V> peek() {
            final ReferenceEntry<K, V> next = this.head.getNextExpirable();
            return (next == this.head) ? null : next;
        }
        
        @Override
        public ReferenceEntry<K, V> poll() {
            final ReferenceEntry<K, V> next = this.head.getNextExpirable();
            if (next == this.head) {
                return null;
            }
            this.remove(next);
            return next;
        }
        
        @Override
        public boolean remove(final Object o) {
            final ReferenceEntry<K, V> e = (ReferenceEntry<K, V>)o;
            final ReferenceEntry<K, V> previous = e.getPreviousExpirable();
            final ReferenceEntry<K, V> next = e.getNextExpirable();
            MapMakerInternalMap.connectExpirables(previous, next);
            MapMakerInternalMap.nullifyExpirable(e);
            return next != NullEntry.INSTANCE;
        }
        
        @Override
        public boolean contains(final Object o) {
            final ReferenceEntry<K, V> e = (ReferenceEntry<K, V>)o;
            return e.getNextExpirable() != NullEntry.INSTANCE;
        }
        
        @Override
        public boolean isEmpty() {
            return this.head.getNextExpirable() == this.head;
        }
        
        @Override
        public int size() {
            int size = 0;
            for (ReferenceEntry<K, V> e = this.head.getNextExpirable(); e != this.head; e = e.getNextExpirable()) {
                ++size;
            }
            return size;
        }
        
        @Override
        public void clear() {
            ReferenceEntry<K, V> next;
            for (ReferenceEntry<K, V> e = this.head.getNextExpirable(); e != this.head; e = next) {
                next = e.getNextExpirable();
                MapMakerInternalMap.nullifyExpirable(e);
            }
            this.head.setNextExpirable(this.head);
            this.head.setPreviousExpirable(this.head);
        }
        
        @Override
        public Iterator<ReferenceEntry<K, V>> iterator() {
            return new AbstractSequentialIterator<ReferenceEntry<K, V>>(this.peek()) {
                @Override
                protected ReferenceEntry<K, V> computeNext(final ReferenceEntry<K, V> previous) {
                    final ReferenceEntry<K, V> next = previous.getNextExpirable();
                    return (next == ExpirationQueue.this.head) ? null : next;
                }
            };
        }
    }
    
    static final class CleanupMapTask implements Runnable
    {
        final WeakReference<MapMakerInternalMap<?, ?>> mapReference;
        
        public CleanupMapTask(final MapMakerInternalMap<?, ?> map) {
            this.mapReference = new WeakReference<MapMakerInternalMap<?, ?>>(map);
        }
        
        @Override
        public void run() {
            final MapMakerInternalMap<?, ?> map = this.mapReference.get();
            if (map == null) {
                throw new CancellationException();
            }
            for (final Segment<?, ?> segment : map.segments) {
                segment.runCleanup();
            }
        }
    }
    
    abstract class HashIterator<E> implements Iterator<E>
    {
        int nextSegmentIndex;
        int nextTableIndex;
        Segment<K, V> currentSegment;
        AtomicReferenceArray<ReferenceEntry<K, V>> currentTable;
        ReferenceEntry<K, V> nextEntry;
        WriteThroughEntry nextExternal;
        WriteThroughEntry lastReturned;
        
        HashIterator() {
            this.nextSegmentIndex = MapMakerInternalMap.this.segments.length - 1;
            this.nextTableIndex = -1;
            this.advance();
        }
        
        @Override
        public abstract E next();
        
        final void advance() {
            this.nextExternal = null;
            if (this.nextInChain()) {
                return;
            }
            if (this.nextInTable()) {
                return;
            }
            while (this.nextSegmentIndex >= 0) {
                this.currentSegment = MapMakerInternalMap.this.segments[this.nextSegmentIndex--];
                if (this.currentSegment.count != 0) {
                    this.currentTable = this.currentSegment.table;
                    this.nextTableIndex = this.currentTable.length() - 1;
                    if (this.nextInTable()) {
                        return;
                    }
                    continue;
                }
            }
        }
        
        boolean nextInChain() {
            if (this.nextEntry != null) {
                this.nextEntry = this.nextEntry.getNext();
                while (this.nextEntry != null) {
                    if (this.advanceTo(this.nextEntry)) {
                        return true;
                    }
                    this.nextEntry = this.nextEntry.getNext();
                }
            }
            return false;
        }
        
        boolean nextInTable() {
            while (this.nextTableIndex >= 0) {
                final ReferenceEntry<K, V> nextEntry = this.currentTable.get(this.nextTableIndex--);
                this.nextEntry = nextEntry;
                if (nextEntry != null && (this.advanceTo(this.nextEntry) || this.nextInChain())) {
                    return true;
                }
            }
            return false;
        }
        
        boolean advanceTo(final ReferenceEntry<K, V> entry) {
            try {
                final K key = entry.getKey();
                final V value = MapMakerInternalMap.this.getLiveValue(entry);
                if (value != null) {
                    this.nextExternal = new WriteThroughEntry(key, value);
                    return true;
                }
                return false;
            }
            finally {
                this.currentSegment.postReadCleanup();
            }
        }
        
        @Override
        public boolean hasNext() {
            return this.nextExternal != null;
        }
        
        WriteThroughEntry nextEntry() {
            if (this.nextExternal == null) {
                throw new NoSuchElementException();
            }
            this.lastReturned = this.nextExternal;
            this.advance();
            return this.lastReturned;
        }
        
        @Override
        public void remove() {
            CollectPreconditions.checkRemove(this.lastReturned != null);
            MapMakerInternalMap.this.remove(this.lastReturned.getKey());
            this.lastReturned = null;
        }
    }
    
    final class KeyIterator extends HashIterator<K>
    {
        @Override
        public K next() {
            return this.nextEntry().getKey();
        }
    }
    
    final class ValueIterator extends HashIterator<V>
    {
        @Override
        public V next() {
            return this.nextEntry().getValue();
        }
    }
    
    final class WriteThroughEntry extends AbstractMapEntry<K, V>
    {
        final K key;
        V value;
        
        WriteThroughEntry(final K key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public K getKey() {
            return this.key;
        }
        
        @Override
        public V getValue() {
            return this.value;
        }
        
        @Override
        public boolean equals(@Nullable final Object object) {
            if (object instanceof Map.Entry) {
                final Map.Entry<?, ?> that = (Map.Entry<?, ?>)object;
                return this.key.equals(that.getKey()) && this.value.equals(that.getValue());
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.key.hashCode() ^ this.value.hashCode();
        }
        
        @Override
        public V setValue(final V newValue) {
            final V oldValue = MapMakerInternalMap.this.put(this.key, newValue);
            this.value = newValue;
            return oldValue;
        }
    }
    
    final class EntryIterator extends HashIterator<Map.Entry<K, V>>
    {
        @Override
        public Map.Entry<K, V> next() {
            return this.nextEntry();
        }
    }
    
    final class KeySet extends AbstractSet<K>
    {
        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }
        
        @Override
        public int size() {
            return MapMakerInternalMap.this.size();
        }
        
        @Override
        public boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }
        
        @Override
        public boolean contains(final Object o) {
            return MapMakerInternalMap.this.containsKey(o);
        }
        
        @Override
        public boolean remove(final Object o) {
            return MapMakerInternalMap.this.remove(o) != null;
        }
        
        @Override
        public void clear() {
            MapMakerInternalMap.this.clear();
        }
    }
    
    final class Values extends AbstractCollection<V>
    {
        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }
        
        @Override
        public int size() {
            return MapMakerInternalMap.this.size();
        }
        
        @Override
        public boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }
        
        @Override
        public boolean contains(final Object o) {
            return MapMakerInternalMap.this.containsValue(o);
        }
        
        @Override
        public void clear() {
            MapMakerInternalMap.this.clear();
        }
    }
    
    final class EntrySet extends AbstractSet<Map.Entry<K, V>>
    {
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            final Object key = e.getKey();
            if (key == null) {
                return false;
            }
            final V v = MapMakerInternalMap.this.get(key);
            return v != null && MapMakerInternalMap.this.valueEquivalence.equivalent(e.getValue(), v);
        }
        
        @Override
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            final Object key = e.getKey();
            return key != null && MapMakerInternalMap.this.remove(key, e.getValue());
        }
        
        @Override
        public int size() {
            return MapMakerInternalMap.this.size();
        }
        
        @Override
        public boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }
        
        @Override
        public void clear() {
            MapMakerInternalMap.this.clear();
        }
    }
    
    abstract static class AbstractSerializationProxy<K, V> extends ForwardingConcurrentMap<K, V> implements Serializable
    {
        private static final long serialVersionUID = 3L;
        final Strength keyStrength;
        final Strength valueStrength;
        final Equivalence<Object> keyEquivalence;
        final Equivalence<Object> valueEquivalence;
        final long expireAfterWriteNanos;
        final long expireAfterAccessNanos;
        final int maximumSize;
        final int concurrencyLevel;
        final MapMaker.RemovalListener<? super K, ? super V> removalListener;
        transient ConcurrentMap<K, V> delegate;
        
        AbstractSerializationProxy(final Strength keyStrength, final Strength valueStrength, final Equivalence<Object> keyEquivalence, final Equivalence<Object> valueEquivalence, final long expireAfterWriteNanos, final long expireAfterAccessNanos, final int maximumSize, final int concurrencyLevel, final MapMaker.RemovalListener<? super K, ? super V> removalListener, final ConcurrentMap<K, V> delegate) {
            this.keyStrength = keyStrength;
            this.valueStrength = valueStrength;
            this.keyEquivalence = keyEquivalence;
            this.valueEquivalence = valueEquivalence;
            this.expireAfterWriteNanos = expireAfterWriteNanos;
            this.expireAfterAccessNanos = expireAfterAccessNanos;
            this.maximumSize = maximumSize;
            this.concurrencyLevel = concurrencyLevel;
            this.removalListener = removalListener;
            this.delegate = delegate;
        }
        
        @Override
        protected ConcurrentMap<K, V> delegate() {
            return this.delegate;
        }
        
        void writeMapTo(final ObjectOutputStream out) throws IOException {
            out.writeInt(this.delegate.size());
            for (final Map.Entry<K, V> entry : this.delegate.entrySet()) {
                out.writeObject(entry.getKey());
                out.writeObject(entry.getValue());
            }
            out.writeObject(null);
        }
        
        MapMaker readMapMaker(final ObjectInputStream in) throws IOException {
            final int size = in.readInt();
            final MapMaker mapMaker = new MapMaker().initialCapacity(size).setKeyStrength(this.keyStrength).setValueStrength(this.valueStrength).keyEquivalence(this.keyEquivalence).concurrencyLevel(this.concurrencyLevel);
            mapMaker.removalListener(this.removalListener);
            if (this.expireAfterWriteNanos > 0L) {
                mapMaker.expireAfterWrite(this.expireAfterWriteNanos, TimeUnit.NANOSECONDS);
            }
            if (this.expireAfterAccessNanos > 0L) {
                mapMaker.expireAfterAccess(this.expireAfterAccessNanos, TimeUnit.NANOSECONDS);
            }
            if (this.maximumSize != -1) {
                mapMaker.maximumSize(this.maximumSize);
            }
            return mapMaker;
        }
        
        void readEntries(final ObjectInputStream in) throws IOException, ClassNotFoundException {
            while (true) {
                final K key = (K)in.readObject();
                if (key == null) {
                    break;
                }
                final V value = (V)in.readObject();
                this.delegate.put(key, value);
            }
        }
    }
    
    private static final class SerializationProxy<K, V> extends AbstractSerializationProxy<K, V>
    {
        private static final long serialVersionUID = 3L;
        
        SerializationProxy(final Strength keyStrength, final Strength valueStrength, final Equivalence<Object> keyEquivalence, final Equivalence<Object> valueEquivalence, final long expireAfterWriteNanos, final long expireAfterAccessNanos, final int maximumSize, final int concurrencyLevel, final MapMaker.RemovalListener<? super K, ? super V> removalListener, final ConcurrentMap<K, V> delegate) {
            super(keyStrength, valueStrength, keyEquivalence, valueEquivalence, expireAfterWriteNanos, expireAfterAccessNanos, maximumSize, concurrencyLevel, removalListener, delegate);
        }
        
        private void writeObject(final ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
            this.writeMapTo(out);
        }
        
        private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            final MapMaker mapMaker = this.readMapMaker(in);
            this.delegate = (ConcurrentMap<K, V>)mapMaker.makeMap();
            this.readEntries(in);
        }
        
        private Object readResolve() {
            return this.delegate;
        }
    }
    
    interface ReferenceEntry<K, V>
    {
        ValueReference<K, V> getValueReference();
        
        void setValueReference(final ValueReference<K, V> p0);
        
        ReferenceEntry<K, V> getNext();
        
        int getHash();
        
        K getKey();
        
        long getExpirationTime();
        
        void setExpirationTime(final long p0);
        
        ReferenceEntry<K, V> getNextExpirable();
        
        void setNextExpirable(final ReferenceEntry<K, V> p0);
        
        ReferenceEntry<K, V> getPreviousExpirable();
        
        void setPreviousExpirable(final ReferenceEntry<K, V> p0);
        
        ReferenceEntry<K, V> getNextEvictable();
        
        void setNextEvictable(final ReferenceEntry<K, V> p0);
        
        ReferenceEntry<K, V> getPreviousEvictable();
        
        void setPreviousEvictable(final ReferenceEntry<K, V> p0);
    }
    
    interface ValueReference<K, V>
    {
        V get();
        
        V waitForValue() throws ExecutionException;
        
        ReferenceEntry<K, V> getEntry();
        
        ValueReference<K, V> copyFor(final ReferenceQueue<V> p0, @Nullable final V p1, final ReferenceEntry<K, V> p2);
        
        void clear(@Nullable final ValueReference<K, V> p0);
        
        boolean isComputingReference();
    }
}
