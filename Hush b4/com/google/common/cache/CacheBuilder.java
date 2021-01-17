// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.cache;

import com.google.common.base.Suppliers;
import com.google.common.base.Ascii;
import java.util.logging.Level;
import javax.annotation.CheckReturnValue;
import java.util.concurrent.TimeUnit;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;
import com.google.common.base.Equivalence;
import java.util.logging.Logger;
import com.google.common.base.Ticker;
import com.google.common.base.Supplier;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class CacheBuilder<K, V>
{
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
    private static final int DEFAULT_EXPIRATION_NANOS = 0;
    private static final int DEFAULT_REFRESH_NANOS = 0;
    static final Supplier<? extends AbstractCache.StatsCounter> NULL_STATS_COUNTER;
    static final CacheStats EMPTY_STATS;
    static final Supplier<AbstractCache.StatsCounter> CACHE_STATS_COUNTER;
    static final Ticker NULL_TICKER;
    private static final Logger logger;
    static final int UNSET_INT = -1;
    boolean strictParsing;
    int initialCapacity;
    int concurrencyLevel;
    long maximumSize;
    long maximumWeight;
    Weigher<? super K, ? super V> weigher;
    LocalCache.Strength keyStrength;
    LocalCache.Strength valueStrength;
    long expireAfterWriteNanos;
    long expireAfterAccessNanos;
    long refreshNanos;
    Equivalence<Object> keyEquivalence;
    Equivalence<Object> valueEquivalence;
    RemovalListener<? super K, ? super V> removalListener;
    Ticker ticker;
    Supplier<? extends AbstractCache.StatsCounter> statsCounterSupplier;
    
    CacheBuilder() {
        this.strictParsing = true;
        this.initialCapacity = -1;
        this.concurrencyLevel = -1;
        this.maximumSize = -1L;
        this.maximumWeight = -1L;
        this.expireAfterWriteNanos = -1L;
        this.expireAfterAccessNanos = -1L;
        this.refreshNanos = -1L;
        this.statsCounterSupplier = CacheBuilder.NULL_STATS_COUNTER;
    }
    
    public static CacheBuilder<Object, Object> newBuilder() {
        return new CacheBuilder<Object, Object>();
    }
    
    @Beta
    @GwtIncompatible("To be supported")
    public static CacheBuilder<Object, Object> from(final CacheBuilderSpec spec) {
        return spec.toCacheBuilder().lenientParsing();
    }
    
    @Beta
    @GwtIncompatible("To be supported")
    public static CacheBuilder<Object, Object> from(final String spec) {
        return from(CacheBuilderSpec.parse(spec));
    }
    
    @GwtIncompatible("To be supported")
    CacheBuilder<K, V> lenientParsing() {
        this.strictParsing = false;
        return this;
    }
    
    @GwtIncompatible("To be supported")
    CacheBuilder<K, V> keyEquivalence(final Equivalence<Object> equivalence) {
        Preconditions.checkState(this.keyEquivalence == null, "key equivalence was already set to %s", this.keyEquivalence);
        this.keyEquivalence = Preconditions.checkNotNull(equivalence);
        return this;
    }
    
    Equivalence<Object> getKeyEquivalence() {
        return Objects.firstNonNull(this.keyEquivalence, this.getKeyStrength().defaultEquivalence());
    }
    
    @GwtIncompatible("To be supported")
    CacheBuilder<K, V> valueEquivalence(final Equivalence<Object> equivalence) {
        Preconditions.checkState(this.valueEquivalence == null, "value equivalence was already set to %s", this.valueEquivalence);
        this.valueEquivalence = Preconditions.checkNotNull(equivalence);
        return this;
    }
    
    Equivalence<Object> getValueEquivalence() {
        return Objects.firstNonNull(this.valueEquivalence, this.getValueStrength().defaultEquivalence());
    }
    
    public CacheBuilder<K, V> initialCapacity(final int initialCapacity) {
        Preconditions.checkState(this.initialCapacity == -1, "initial capacity was already set to %s", this.initialCapacity);
        Preconditions.checkArgument(initialCapacity >= 0);
        this.initialCapacity = initialCapacity;
        return this;
    }
    
    int getInitialCapacity() {
        return (this.initialCapacity == -1) ? 16 : this.initialCapacity;
    }
    
    public CacheBuilder<K, V> concurrencyLevel(final int concurrencyLevel) {
        Preconditions.checkState(this.concurrencyLevel == -1, "concurrency level was already set to %s", this.concurrencyLevel);
        Preconditions.checkArgument(concurrencyLevel > 0);
        this.concurrencyLevel = concurrencyLevel;
        return this;
    }
    
    int getConcurrencyLevel() {
        return (this.concurrencyLevel == -1) ? 4 : this.concurrencyLevel;
    }
    
    public CacheBuilder<K, V> maximumSize(final long size) {
        Preconditions.checkState(this.maximumSize == -1L, "maximum size was already set to %s", this.maximumSize);
        Preconditions.checkState(this.maximumWeight == -1L, "maximum weight was already set to %s", this.maximumWeight);
        Preconditions.checkState(this.weigher == null, (Object)"maximum size can not be combined with weigher");
        Preconditions.checkArgument(size >= 0L, (Object)"maximum size must not be negative");
        this.maximumSize = size;
        return this;
    }
    
    @GwtIncompatible("To be supported")
    public CacheBuilder<K, V> maximumWeight(final long weight) {
        Preconditions.checkState(this.maximumWeight == -1L, "maximum weight was already set to %s", this.maximumWeight);
        Preconditions.checkState(this.maximumSize == -1L, "maximum size was already set to %s", this.maximumSize);
        this.maximumWeight = weight;
        Preconditions.checkArgument(weight >= 0L, (Object)"maximum weight must not be negative");
        return this;
    }
    
    @GwtIncompatible("To be supported")
    public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> weigher(final Weigher<? super K1, ? super V1> weigher) {
        Preconditions.checkState(this.weigher == null);
        if (this.strictParsing) {
            Preconditions.checkState(this.maximumSize == -1L, "weigher can not be combined with maximum size", this.maximumSize);
        }
        final CacheBuilder<K1, V1> me = (CacheBuilder<K1, V1>)this;
        me.weigher = Preconditions.checkNotNull(weigher);
        return me;
    }
    
    long getMaximumWeight() {
        if (this.expireAfterWriteNanos == 0L || this.expireAfterAccessNanos == 0L) {
            return 0L;
        }
        return (this.weigher == null) ? this.maximumSize : this.maximumWeight;
    }
    
     <K1 extends K, V1 extends V> Weigher<K1, V1> getWeigher() {
        return Objects.firstNonNull((Weigher<K1, V1>)this.weigher, (Weigher<K1, V1>)OneWeigher.INSTANCE);
    }
    
    @GwtIncompatible("java.lang.ref.WeakReference")
    public CacheBuilder<K, V> weakKeys() {
        return this.setKeyStrength(LocalCache.Strength.WEAK);
    }
    
    CacheBuilder<K, V> setKeyStrength(final LocalCache.Strength strength) {
        Preconditions.checkState(this.keyStrength == null, "Key strength was already set to %s", this.keyStrength);
        this.keyStrength = Preconditions.checkNotNull(strength);
        return this;
    }
    
    LocalCache.Strength getKeyStrength() {
        return Objects.firstNonNull(this.keyStrength, LocalCache.Strength.STRONG);
    }
    
    @GwtIncompatible("java.lang.ref.WeakReference")
    public CacheBuilder<K, V> weakValues() {
        return this.setValueStrength(LocalCache.Strength.WEAK);
    }
    
    @GwtIncompatible("java.lang.ref.SoftReference")
    public CacheBuilder<K, V> softValues() {
        return this.setValueStrength(LocalCache.Strength.SOFT);
    }
    
    CacheBuilder<K, V> setValueStrength(final LocalCache.Strength strength) {
        Preconditions.checkState(this.valueStrength == null, "Value strength was already set to %s", this.valueStrength);
        this.valueStrength = Preconditions.checkNotNull(strength);
        return this;
    }
    
    LocalCache.Strength getValueStrength() {
        return Objects.firstNonNull(this.valueStrength, LocalCache.Strength.STRONG);
    }
    
    public CacheBuilder<K, V> expireAfterWrite(final long duration, final TimeUnit unit) {
        Preconditions.checkState(this.expireAfterWriteNanos == -1L, "expireAfterWrite was already set to %s ns", this.expireAfterWriteNanos);
        Preconditions.checkArgument(duration >= 0L, "duration cannot be negative: %s %s", duration, unit);
        this.expireAfterWriteNanos = unit.toNanos(duration);
        return this;
    }
    
    long getExpireAfterWriteNanos() {
        return (this.expireAfterWriteNanos == -1L) ? 0L : this.expireAfterWriteNanos;
    }
    
    public CacheBuilder<K, V> expireAfterAccess(final long duration, final TimeUnit unit) {
        Preconditions.checkState(this.expireAfterAccessNanos == -1L, "expireAfterAccess was already set to %s ns", this.expireAfterAccessNanos);
        Preconditions.checkArgument(duration >= 0L, "duration cannot be negative: %s %s", duration, unit);
        this.expireAfterAccessNanos = unit.toNanos(duration);
        return this;
    }
    
    long getExpireAfterAccessNanos() {
        return (this.expireAfterAccessNanos == -1L) ? 0L : this.expireAfterAccessNanos;
    }
    
    @Beta
    @GwtIncompatible("To be supported (synchronously).")
    public CacheBuilder<K, V> refreshAfterWrite(final long duration, final TimeUnit unit) {
        Preconditions.checkNotNull(unit);
        Preconditions.checkState(this.refreshNanos == -1L, "refresh was already set to %s ns", this.refreshNanos);
        Preconditions.checkArgument(duration > 0L, "duration must be positive: %s %s", duration, unit);
        this.refreshNanos = unit.toNanos(duration);
        return this;
    }
    
    long getRefreshNanos() {
        return (this.refreshNanos == -1L) ? 0L : this.refreshNanos;
    }
    
    public CacheBuilder<K, V> ticker(final Ticker ticker) {
        Preconditions.checkState(this.ticker == null);
        this.ticker = Preconditions.checkNotNull(ticker);
        return this;
    }
    
    Ticker getTicker(final boolean recordsTime) {
        if (this.ticker != null) {
            return this.ticker;
        }
        return recordsTime ? Ticker.systemTicker() : CacheBuilder.NULL_TICKER;
    }
    
    @CheckReturnValue
    public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> removalListener(final RemovalListener<? super K1, ? super V1> listener) {
        Preconditions.checkState(this.removalListener == null);
        final CacheBuilder<K1, V1> me = (CacheBuilder<K1, V1>)this;
        me.removalListener = Preconditions.checkNotNull(listener);
        return me;
    }
    
     <K1 extends K, V1 extends V> RemovalListener<K1, V1> getRemovalListener() {
        return Objects.firstNonNull((RemovalListener<K1, V1>)this.removalListener, (RemovalListener<K1, V1>)NullListener.INSTANCE);
    }
    
    public CacheBuilder<K, V> recordStats() {
        this.statsCounterSupplier = CacheBuilder.CACHE_STATS_COUNTER;
        return this;
    }
    
    boolean isRecordingStats() {
        return this.statsCounterSupplier == CacheBuilder.CACHE_STATS_COUNTER;
    }
    
    Supplier<? extends AbstractCache.StatsCounter> getStatsCounterSupplier() {
        return this.statsCounterSupplier;
    }
    
    public <K1 extends K, V1 extends V> LoadingCache<K1, V1> build(final CacheLoader<? super K1, V1> loader) {
        this.checkWeightWithWeigher();
        return new LocalCache.LocalLoadingCache<K1, V1>(this, loader);
    }
    
    public <K1 extends K, V1 extends V> Cache<K1, V1> build() {
        this.checkWeightWithWeigher();
        this.checkNonLoadingCache();
        return new LocalCache.LocalManualCache<K1, V1>(this);
    }
    
    private void checkNonLoadingCache() {
        Preconditions.checkState(this.refreshNanos == -1L, (Object)"refreshAfterWrite requires a LoadingCache");
    }
    
    private void checkWeightWithWeigher() {
        if (this.weigher == null) {
            Preconditions.checkState(this.maximumWeight == -1L, (Object)"maximumWeight requires weigher");
        }
        else if (this.strictParsing) {
            Preconditions.checkState(this.maximumWeight != -1L, (Object)"weigher requires maximumWeight");
        }
        else if (this.maximumWeight == -1L) {
            CacheBuilder.logger.log(Level.WARNING, "ignoring weigher specified without maximumWeight");
        }
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
        if (this.maximumSize != -1L) {
            s.add("maximumSize", this.maximumSize);
        }
        if (this.maximumWeight != -1L) {
            s.add("maximumWeight", this.maximumWeight);
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
        if (this.valueEquivalence != null) {
            s.addValue("valueEquivalence");
        }
        if (this.removalListener != null) {
            s.addValue("removalListener");
        }
        return s.toString();
    }
    
    static {
        NULL_STATS_COUNTER = Suppliers.ofInstance((AbstractCache.StatsCounter)new AbstractCache.StatsCounter() {
            @Override
            public void recordHits(final int count) {
            }
            
            @Override
            public void recordMisses(final int count) {
            }
            
            @Override
            public void recordLoadSuccess(final long loadTime) {
            }
            
            @Override
            public void recordLoadException(final long loadTime) {
            }
            
            @Override
            public void recordEviction() {
            }
            
            @Override
            public CacheStats snapshot() {
                return CacheBuilder.EMPTY_STATS;
            }
        });
        EMPTY_STATS = new CacheStats(0L, 0L, 0L, 0L, 0L, 0L);
        CACHE_STATS_COUNTER = new Supplier<AbstractCache.StatsCounter>() {
            @Override
            public AbstractCache.StatsCounter get() {
                return new AbstractCache.SimpleStatsCounter();
            }
        };
        NULL_TICKER = new Ticker() {
            @Override
            public long read() {
                return 0L;
            }
        };
        logger = Logger.getLogger(CacheBuilder.class.getName());
    }
    
    enum NullListener implements RemovalListener<Object, Object>
    {
        INSTANCE;
        
        @Override
        public void onRemoval(final RemovalNotification<Object, Object> notification) {
        }
    }
    
    enum OneWeigher implements Weigher<Object, Object>
    {
        INSTANCE;
        
        @Override
        public int weigh(final Object key, final Object value) {
            return 1;
        }
    }
}
