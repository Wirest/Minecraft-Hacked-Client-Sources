// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.concurrent.ExecutionException;
import com.google.common.base.Throwables;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.io.Serializable;
import java.util.AbstractMap;
import javax.annotation.Nullable;
import com.google.common.base.Ascii;
import com.google.common.base.Function;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import com.google.common.base.Objects;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Ticker;
import com.google.common.base.Equivalence;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class MapMaker extends GenericMapMaker<Object, Object>
{
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
    private static final int DEFAULT_EXPIRATION_NANOS = 0;
    static final int UNSET_INT = -1;
    boolean useCustomMap;
    int initialCapacity;
    int concurrencyLevel;
    int maximumSize;
    MapMakerInternalMap.Strength keyStrength;
    MapMakerInternalMap.Strength valueStrength;
    long expireAfterWriteNanos;
    long expireAfterAccessNanos;
    RemovalCause nullRemovalCause;
    Equivalence<Object> keyEquivalence;
    Ticker ticker;
    
    public MapMaker() {
        this.initialCapacity = -1;
        this.concurrencyLevel = -1;
        this.maximumSize = -1;
        this.expireAfterWriteNanos = -1L;
        this.expireAfterAccessNanos = -1L;
    }
    
    @GwtIncompatible("To be supported")
    @Override
    MapMaker keyEquivalence(final Equivalence<Object> equivalence) {
        Preconditions.checkState(this.keyEquivalence == null, "key equivalence was already set to %s", this.keyEquivalence);
        this.keyEquivalence = Preconditions.checkNotNull(equivalence);
        this.useCustomMap = true;
        return this;
    }
    
    Equivalence<Object> getKeyEquivalence() {
        return Objects.firstNonNull(this.keyEquivalence, this.getKeyStrength().defaultEquivalence());
    }
    
    @Override
    public MapMaker initialCapacity(final int initialCapacity) {
        Preconditions.checkState(this.initialCapacity == -1, "initial capacity was already set to %s", this.initialCapacity);
        Preconditions.checkArgument(initialCapacity >= 0);
        this.initialCapacity = initialCapacity;
        return this;
    }
    
    int getInitialCapacity() {
        return (this.initialCapacity == -1) ? 16 : this.initialCapacity;
    }
    
    @Deprecated
    @Override
    MapMaker maximumSize(final int size) {
        Preconditions.checkState(this.maximumSize == -1, "maximum size was already set to %s", this.maximumSize);
        Preconditions.checkArgument(size >= 0, (Object)"maximum size must not be negative");
        this.maximumSize = size;
        this.useCustomMap = true;
        if (this.maximumSize == 0) {
            this.nullRemovalCause = RemovalCause.SIZE;
        }
        return this;
    }
    
    @Override
    public MapMaker concurrencyLevel(final int concurrencyLevel) {
        Preconditions.checkState(this.concurrencyLevel == -1, "concurrency level was already set to %s", this.concurrencyLevel);
        Preconditions.checkArgument(concurrencyLevel > 0);
        this.concurrencyLevel = concurrencyLevel;
        return this;
    }
    
    int getConcurrencyLevel() {
        return (this.concurrencyLevel == -1) ? 4 : this.concurrencyLevel;
    }
    
    @GwtIncompatible("java.lang.ref.WeakReference")
    @Override
    public MapMaker weakKeys() {
        return this.setKeyStrength(MapMakerInternalMap.Strength.WEAK);
    }
    
    MapMaker setKeyStrength(final MapMakerInternalMap.Strength strength) {
        Preconditions.checkState(this.keyStrength == null, "Key strength was already set to %s", this.keyStrength);
        this.keyStrength = Preconditions.checkNotNull(strength);
        Preconditions.checkArgument(this.keyStrength != MapMakerInternalMap.Strength.SOFT, (Object)"Soft keys are not supported");
        if (strength != MapMakerInternalMap.Strength.STRONG) {
            this.useCustomMap = true;
        }
        return this;
    }
    
    MapMakerInternalMap.Strength getKeyStrength() {
        return Objects.firstNonNull(this.keyStrength, MapMakerInternalMap.Strength.STRONG);
    }
    
    @GwtIncompatible("java.lang.ref.WeakReference")
    @Override
    public MapMaker weakValues() {
        return this.setValueStrength(MapMakerInternalMap.Strength.WEAK);
    }
    
    @Deprecated
    @GwtIncompatible("java.lang.ref.SoftReference")
    @Override
    public MapMaker softValues() {
        return this.setValueStrength(MapMakerInternalMap.Strength.SOFT);
    }
    
    MapMaker setValueStrength(final MapMakerInternalMap.Strength strength) {
        Preconditions.checkState(this.valueStrength == null, "Value strength was already set to %s", this.valueStrength);
        this.valueStrength = Preconditions.checkNotNull(strength);
        if (strength != MapMakerInternalMap.Strength.STRONG) {
            this.useCustomMap = true;
        }
        return this;
    }
    
    MapMakerInternalMap.Strength getValueStrength() {
        return Objects.firstNonNull(this.valueStrength, MapMakerInternalMap.Strength.STRONG);
    }
    
    @Deprecated
    @Override
    MapMaker expireAfterWrite(final long duration, final TimeUnit unit) {
        this.checkExpiration(duration, unit);
        this.expireAfterWriteNanos = unit.toNanos(duration);
        if (duration == 0L && this.nullRemovalCause == null) {
            this.nullRemovalCause = RemovalCause.EXPIRED;
        }
        this.useCustomMap = true;
        return this;
    }
    
    private void checkExpiration(final long duration, final TimeUnit unit) {
        Preconditions.checkState(this.expireAfterWriteNanos == -1L, "expireAfterWrite was already set to %s ns", this.expireAfterWriteNanos);
        Preconditions.checkState(this.expireAfterAccessNanos == -1L, "expireAfterAccess was already set to %s ns", this.expireAfterAccessNanos);
        Preconditions.checkArgument(duration >= 0L, "duration cannot be negative: %s %s", duration, unit);
    }
    
    long getExpireAfterWriteNanos() {
        return (this.expireAfterWriteNanos == -1L) ? 0L : this.expireAfterWriteNanos;
    }
    
    @Deprecated
    @GwtIncompatible("To be supported")
    @Override
    MapMaker expireAfterAccess(final long duration, final TimeUnit unit) {
        this.checkExpiration(duration, unit);
        this.expireAfterAccessNanos = unit.toNanos(duration);
        if (duration == 0L && this.nullRemovalCause == null) {
            this.nullRemovalCause = RemovalCause.EXPIRED;
        }
        this.useCustomMap = true;
        return this;
    }
    
    long getExpireAfterAccessNanos() {
        return (this.expireAfterAccessNanos == -1L) ? 0L : this.expireAfterAccessNanos;
    }
    
    Ticker getTicker() {
        return Objects.firstNonNull(this.ticker, Ticker.systemTicker());
    }
    
    @Deprecated
    @GwtIncompatible("To be supported")
     <K, V> GenericMapMaker<K, V> removalListener(final RemovalListener<K, V> listener) {
        Preconditions.checkState(this.removalListener == null);
        final GenericMapMaker<K, V> me = (GenericMapMaker<K, V>)this;
        me.removalListener = (RemovalListener<K, V>)Preconditions.checkNotNull((RemovalListener<K0, V0>)listener);
        this.useCustomMap = true;
        return me;
    }
    
    @Override
    public <K, V> ConcurrentMap<K, V> makeMap() {
        if (!this.useCustomMap) {
            return new ConcurrentHashMap<K, V>(this.getInitialCapacity(), 0.75f, this.getConcurrencyLevel());
        }
        return (ConcurrentMap<K, V>)((this.nullRemovalCause == null) ? new MapMakerInternalMap<Object, Object>(this) : new NullConcurrentMap<Object, Object>(this));
    }
    
    @GwtIncompatible("MapMakerInternalMap")
    @Override
     <K, V> MapMakerInternalMap<K, V> makeCustomMap() {
        return new MapMakerInternalMap<K, V>(this);
    }
    
    @Deprecated
    @Override
     <K, V> ConcurrentMap<K, V> makeComputingMap(final Function<? super K, ? extends V> computingFunction) {
        return (ConcurrentMap<K, V>)((this.nullRemovalCause == null) ? new ComputingMapAdapter<Object, Object>(this, computingFunction) : new NullComputingConcurrentMap<Object, Object>(this, computingFunction));
    }
    
    @Override
    public String toString() {
        final Objects.ToStringHelper s = Objects.toStringHelper(this);
        if (this.initialCapacity != -1) {
            s.add("initialCapacity", this.initialCapacity);
        }
        if (this.concurrencyLevel != -1) {
            s.add("concurrencyLevel", this.concurrencyLevel);
        }
        if (this.maximumSize != -1) {
            s.add("maximumSize", this.maximumSize);
        }
        if (this.expireAfterWriteNanos != -1L) {
            s.add("expireAfterWrite", this.expireAfterWriteNanos + "ns");
        }
        if (this.expireAfterAccessNanos != -1L) {
            s.add("expireAfterAccess", this.expireAfterAccessNanos + "ns");
        }
        if (this.keyStrength != null) {
            s.add("keyStrength", Ascii.toLowerCase(this.keyStrength.toString()));
        }
        if (this.valueStrength != null) {
            s.add("valueStrength", Ascii.toLowerCase(this.valueStrength.toString()));
        }
        if (this.keyEquivalence != null) {
            s.addValue("keyEquivalence");
        }
        if (this.removalListener != null) {
            s.addValue("removalListener");
        }
        return s.toString();
    }
    
    static final class RemovalNotification<K, V> extends ImmutableEntry<K, V>
    {
        private static final long serialVersionUID = 0L;
        private final RemovalCause cause;
        
        RemovalNotification(@Nullable final K key, @Nullable final V value, final RemovalCause cause) {
            super(key, value);
            this.cause = cause;
        }
        
        public RemovalCause getCause() {
            return this.cause;
        }
        
        public boolean wasEvicted() {
            return this.cause.wasEvicted();
        }
    }
    
    enum RemovalCause
    {
        EXPLICIT {
            @Override
            boolean wasEvicted() {
                return false;
            }
        }, 
        REPLACED {
            @Override
            boolean wasEvicted() {
                return false;
            }
        }, 
        COLLECTED {
            @Override
            boolean wasEvicted() {
                return true;
            }
        }, 
        EXPIRED {
            @Override
            boolean wasEvicted() {
                return true;
            }
        }, 
        SIZE {
            @Override
            boolean wasEvicted() {
                return true;
            }
        };
        
        abstract boolean wasEvicted();
    }
    
    static class NullConcurrentMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V>, Serializable
    {
        private static final long serialVersionUID = 0L;
        private final RemovalListener<K, V> removalListener;
        private final RemovalCause removalCause;
        
        NullConcurrentMap(final MapMaker mapMaker) {
            this.removalListener = mapMaker.getRemovalListener();
            this.removalCause = mapMaker.nullRemovalCause;
        }
        
        @Override
        public boolean containsKey(@Nullable final Object key) {
            return false;
        }
        
        @Override
        public boolean containsValue(@Nullable final Object value) {
            return false;
        }
        
        @Override
        public V get(@Nullable final Object key) {
            return null;
        }
        
        void notifyRemoval(final K key, final V value) {
            final RemovalNotification<K, V> notification = new RemovalNotification<K, V>(key, value, this.removalCause);
            this.removalListener.onRemoval(notification);
        }
        
        @Override
        public V put(final K key, final V value) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(value);
            this.notifyRemoval(key, value);
            return null;
        }
        
        @Override
        public V putIfAbsent(final K key, final V value) {
            return this.put(key, value);
        }
        
        @Override
        public V remove(@Nullable final Object key) {
            return null;
        }
        
        @Override
        public boolean remove(@Nullable final Object key, @Nullable final Object value) {
            return false;
        }
        
        @Override
        public V replace(final K key, final V value) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(value);
            return null;
        }
        
        @Override
        public boolean replace(final K key, @Nullable final V oldValue, final V newValue) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(newValue);
            return false;
        }
        
        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            return Collections.emptySet();
        }
    }
    
    static final class NullComputingConcurrentMap<K, V> extends NullConcurrentMap<K, V>
    {
        private static final long serialVersionUID = 0L;
        final Function<? super K, ? extends V> computingFunction;
        
        NullComputingConcurrentMap(final MapMaker mapMaker, final Function<? super K, ? extends V> computingFunction) {
            super(mapMaker);
            this.computingFunction = Preconditions.checkNotNull(computingFunction);
        }
        
        @Override
        public V get(final Object k) {
            final K key = (K)k;
            final V value = this.compute(key);
            Preconditions.checkNotNull(value, "%s returned null for key %s.", this.computingFunction, key);
            this.notifyRemoval(key, value);
            return value;
        }
        
        private V compute(final K key) {
            Preconditions.checkNotNull(key);
            try {
                return (V)this.computingFunction.apply((Object)key);
            }
            catch (ComputationException e) {
                throw e;
            }
            catch (Throwable t) {
                throw new ComputationException(t);
            }
        }
    }
    
    static final class ComputingMapAdapter<K, V> extends ComputingConcurrentHashMap<K, V> implements Serializable
    {
        private static final long serialVersionUID = 0L;
        
        ComputingMapAdapter(final MapMaker mapMaker, final Function<? super K, ? extends V> computingFunction) {
            super(mapMaker, computingFunction);
        }
        
        @Override
        public V get(final Object key) {
            V value;
            try {
                value = this.getOrCompute((K)key);
            }
            catch (ExecutionException e) {
                final Throwable cause = e.getCause();
                Throwables.propagateIfInstanceOf(cause, ComputationException.class);
                throw new ComputationException(cause);
            }
            if (value == null) {
                throw new NullPointerException(this.computingFunction + " returned null for key " + key + ".");
            }
            return value;
        }
    }
    
    interface RemovalListener<K, V>
    {
        void onRemoval(final RemovalNotification<K, V> p0);
    }
}
