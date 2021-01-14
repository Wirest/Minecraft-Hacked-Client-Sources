package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.*;
import com.google.common.base.Objects.ToStringHelper;

import javax.annotation.CheckReturnValue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@GwtCompatible(emulated = true)
public final class CacheBuilder<K, V> {
    static final CacheStats EMPTY_STATS = new CacheStats(0L, 0L, 0L, 0L, 0L, 0L);
    static final Supplier<? extends AbstractCache.StatsCounter> NULL_STATS_COUNTER = Suppliers.ofInstance(new AbstractCache.StatsCounter() {
        public void recordHits(int paramAnonymousInt) {
        }

        public void recordMisses(int paramAnonymousInt) {
        }

        public void recordLoadSuccess(long paramAnonymousLong) {
        }

        public void recordLoadException(long paramAnonymousLong) {
        }

        public void recordEviction() {
        }

        public CacheStats snapshot() {
            return CacheBuilder.EMPTY_STATS;
        }
    });
    static final Supplier<AbstractCache.StatsCounter> CACHE_STATS_COUNTER = new Supplier() {
        public AbstractCache.StatsCounter get() {
            return new AbstractCache.SimpleStatsCounter();
        }
    };
    static final Ticker NULL_TICKER = new Ticker() {
        public long read() {
            return 0L;
        }
    };
    static final int UNSET_INT = -1;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
    private static final int DEFAULT_EXPIRATION_NANOS = 0;
    private static final int DEFAULT_REFRESH_NANOS = 0;
    private static final Logger logger = Logger.getLogger(CacheBuilder.class.getName());
    boolean strictParsing = true;
    int initialCapacity = -1;
    int concurrencyLevel = -1;
    long maximumSize = -1L;
    long maximumWeight = -1L;
    Weigher<? super K, ? super V> weigher;
    LocalCache.Strength keyStrength;
    LocalCache.Strength valueStrength;
    long expireAfterWriteNanos = -1L;
    long expireAfterAccessNanos = -1L;
    long refreshNanos = -1L;
    Equivalence<Object> keyEquivalence;
    Equivalence<Object> valueEquivalence;
    RemovalListener<? super K, ? super V> removalListener;
    Ticker ticker;
    Supplier<? extends AbstractCache.StatsCounter> statsCounterSupplier = NULL_STATS_COUNTER;

    public static CacheBuilder<Object, Object> newBuilder() {
        return new CacheBuilder();
    }

    @Beta
    @GwtIncompatible("To be supported")
    public static CacheBuilder<Object, Object> from(CacheBuilderSpec paramCacheBuilderSpec) {
        return paramCacheBuilderSpec.toCacheBuilder().lenientParsing();
    }

    @Beta
    @GwtIncompatible("To be supported")
    public static CacheBuilder<Object, Object> from(String paramString) {
        return from(CacheBuilderSpec.parse(paramString));
    }

    @GwtIncompatible("To be supported")
    CacheBuilder<K, V> lenientParsing() {
        this.strictParsing = false;
        return this;
    }

    @GwtIncompatible("To be supported")
    CacheBuilder<K, V> keyEquivalence(Equivalence<Object> paramEquivalence) {
        Preconditions.checkState(this.keyEquivalence == null, "key equivalence was already set to %s", new Object[]{this.keyEquivalence});
        this.keyEquivalence = ((Equivalence) Preconditions.checkNotNull(paramEquivalence));
        return this;
    }

    Equivalence<Object> getKeyEquivalence() {
        return (Equivalence) Objects.firstNonNull(this.keyEquivalence, getKeyStrength().defaultEquivalence());
    }

    @GwtIncompatible("To be supported")
    CacheBuilder<K, V> valueEquivalence(Equivalence<Object> paramEquivalence) {
        Preconditions.checkState(this.valueEquivalence == null, "value equivalence was already set to %s", new Object[]{this.valueEquivalence});
        this.valueEquivalence = ((Equivalence) Preconditions.checkNotNull(paramEquivalence));
        return this;
    }

    Equivalence<Object> getValueEquivalence() {
        return (Equivalence) Objects.firstNonNull(this.valueEquivalence, getValueStrength().defaultEquivalence());
    }

    public CacheBuilder<K, V> initialCapacity(int paramInt) {
        Preconditions.checkState(this.initialCapacity == -1, "initial capacity was already set to %s", new Object[]{Integer.valueOf(this.initialCapacity)});
        Preconditions.checkArgument(paramInt >= 0);
        this.initialCapacity = paramInt;
        return this;
    }

    int getInitialCapacity() {
        return this.initialCapacity == -1 ? 16 : this.initialCapacity;
    }

    public CacheBuilder<K, V> concurrencyLevel(int paramInt) {
        Preconditions.checkState(this.concurrencyLevel == -1, "concurrency level was already set to %s", new Object[]{Integer.valueOf(this.concurrencyLevel)});
        Preconditions.checkArgument(paramInt > 0);
        this.concurrencyLevel = paramInt;
        return this;
    }

    int getConcurrencyLevel() {
        return this.concurrencyLevel == -1 ? 4 : this.concurrencyLevel;
    }

    public CacheBuilder<K, V> maximumSize(long paramLong) {
        Preconditions.checkState(this.maximumSize == -1L, "maximum size was already set to %s", new Object[]{Long.valueOf(this.maximumSize)});
        Preconditions.checkState(this.maximumWeight == -1L, "maximum weight was already set to %s", new Object[]{Long.valueOf(this.maximumWeight)});
        Preconditions.checkState(this.weigher == null, "maximum size can not be combined with weigher");
        Preconditions.checkArgument(paramLong >= 0L, "maximum size must not be negative");
        this.maximumSize = paramLong;
        return this;
    }

    @GwtIncompatible("To be supported")
    public CacheBuilder<K, V> maximumWeight(long paramLong) {
        Preconditions.checkState(this.maximumWeight == -1L, "maximum weight was already set to %s", new Object[]{Long.valueOf(this.maximumWeight)});
        Preconditions.checkState(this.maximumSize == -1L, "maximum size was already set to %s", new Object[]{Long.valueOf(this.maximumSize)});
        this.maximumWeight = paramLong;
        Preconditions.checkArgument(paramLong >= 0L, "maximum weight must not be negative");
        return this;
    }

    @GwtIncompatible("To be supported")
    public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> weigher(Weigher<? super K1, ? super V1> paramWeigher) {
        Preconditions.checkState(this.weigher == null);
        if (this.strictParsing) {
            Preconditions.checkState(this.maximumSize == -1L, "weigher can not be combined with maximum size", new Object[]{Long.valueOf(this.maximumSize)});
        }
        CacheBuilder localCacheBuilder = this;
        localCacheBuilder.weigher = ((Weigher) Preconditions.checkNotNull(paramWeigher));
        return localCacheBuilder;
    }

    long getMaximumWeight() {
        if ((this.expireAfterWriteNanos == 0L) || (this.expireAfterAccessNanos == 0L)) {
            return 0L;
        }
        return this.weigher == null ? this.maximumSize : this.maximumWeight;
    }

    <K1 extends K, V1 extends V> Weigher<K1, V1> getWeigher() {
        return (Weigher) Objects.firstNonNull(this.weigher, OneWeigher.INSTANCE);
    }

    @GwtIncompatible("java.lang.ref.WeakReference")
    public CacheBuilder<K, V> weakKeys() {
        return setKeyStrength(LocalCache.Strength.WEAK);
    }

    LocalCache.Strength getKeyStrength() {
        return (LocalCache.Strength) Objects.firstNonNull(this.keyStrength, LocalCache.Strength.STRONG);
    }

    CacheBuilder<K, V> setKeyStrength(LocalCache.Strength paramStrength) {
        Preconditions.checkState(this.keyStrength == null, "Key strength was already set to %s", new Object[]{this.keyStrength});
        this.keyStrength = ((LocalCache.Strength) Preconditions.checkNotNull(paramStrength));
        return this;
    }

    @GwtIncompatible("java.lang.ref.WeakReference")
    public CacheBuilder<K, V> weakValues() {
        return setValueStrength(LocalCache.Strength.WEAK);
    }

    @GwtIncompatible("java.lang.ref.SoftReference")
    public CacheBuilder<K, V> softValues() {
        return setValueStrength(LocalCache.Strength.SOFT);
    }

    LocalCache.Strength getValueStrength() {
        return (LocalCache.Strength) Objects.firstNonNull(this.valueStrength, LocalCache.Strength.STRONG);
    }

    CacheBuilder<K, V> setValueStrength(LocalCache.Strength paramStrength) {
        Preconditions.checkState(this.valueStrength == null, "Value strength was already set to %s", new Object[]{this.valueStrength});
        this.valueStrength = ((LocalCache.Strength) Preconditions.checkNotNull(paramStrength));
        return this;
    }

    public CacheBuilder<K, V> expireAfterWrite(long paramLong, TimeUnit paramTimeUnit) {
        Preconditions.checkState(this.expireAfterWriteNanos == -1L, "expireAfterWrite was already set to %s ns", new Object[]{Long.valueOf(this.expireAfterWriteNanos)});
        Preconditions.checkArgument(paramLong >= 0L, "duration cannot be negative: %s %s", new Object[]{Long.valueOf(paramLong), paramTimeUnit});
        this.expireAfterWriteNanos = paramTimeUnit.toNanos(paramLong);
        return this;
    }

    long getExpireAfterWriteNanos() {
        return this.expireAfterWriteNanos == -1L ? 0L : this.expireAfterWriteNanos;
    }

    public CacheBuilder<K, V> expireAfterAccess(long paramLong, TimeUnit paramTimeUnit) {
        Preconditions.checkState(this.expireAfterAccessNanos == -1L, "expireAfterAccess was already set to %s ns", new Object[]{Long.valueOf(this.expireAfterAccessNanos)});
        Preconditions.checkArgument(paramLong >= 0L, "duration cannot be negative: %s %s", new Object[]{Long.valueOf(paramLong), paramTimeUnit});
        this.expireAfterAccessNanos = paramTimeUnit.toNanos(paramLong);
        return this;
    }

    long getExpireAfterAccessNanos() {
        return this.expireAfterAccessNanos == -1L ? 0L : this.expireAfterAccessNanos;
    }

    @Beta
    @GwtIncompatible("To be supported (synchronously).")
    public CacheBuilder<K, V> refreshAfterWrite(long paramLong, TimeUnit paramTimeUnit) {
        Preconditions.checkNotNull(paramTimeUnit);
        Preconditions.checkState(this.refreshNanos == -1L, "refresh was already set to %s ns", new Object[]{Long.valueOf(this.refreshNanos)});
        Preconditions.checkArgument(paramLong > 0L, "duration must be positive: %s %s", new Object[]{Long.valueOf(paramLong), paramTimeUnit});
        this.refreshNanos = paramTimeUnit.toNanos(paramLong);
        return this;
    }

    long getRefreshNanos() {
        return this.refreshNanos == -1L ? 0L : this.refreshNanos;
    }

    public CacheBuilder<K, V> ticker(Ticker paramTicker) {
        Preconditions.checkState(this.ticker == null);
        this.ticker = ((Ticker) Preconditions.checkNotNull(paramTicker));
        return this;
    }

    Ticker getTicker(boolean paramBoolean) {
        if (this.ticker != null) {
            return this.ticker;
        }
        return paramBoolean ? Ticker.systemTicker() : NULL_TICKER;
    }

    @CheckReturnValue
    public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> removalListener(RemovalListener<? super K1, ? super V1> paramRemovalListener) {
        Preconditions.checkState(this.removalListener == null);
        CacheBuilder localCacheBuilder = this;
        localCacheBuilder.removalListener = ((RemovalListener) Preconditions.checkNotNull(paramRemovalListener));
        return localCacheBuilder;
    }

    <K1 extends K, V1 extends V> RemovalListener<K1, V1> getRemovalListener() {
        return (RemovalListener) Objects.firstNonNull(this.removalListener, NullListener.INSTANCE);
    }

    public CacheBuilder<K, V> recordStats() {
        this.statsCounterSupplier = CACHE_STATS_COUNTER;
        return this;
    }

    boolean isRecordingStats() {
        return this.statsCounterSupplier == CACHE_STATS_COUNTER;
    }

    Supplier<? extends AbstractCache.StatsCounter> getStatsCounterSupplier() {
        return this.statsCounterSupplier;
    }

    public <K1 extends K, V1 extends V> LoadingCache<K1, V1> build(CacheLoader<? super K1, V1> paramCacheLoader) {
        checkWeightWithWeigher();
        return new LocalCache.LocalLoadingCache(this, paramCacheLoader);
    }

    public <K1 extends K, V1 extends V> Cache<K1, V1> build() {
        checkWeightWithWeigher();
        checkNonLoadingCache();
        return new LocalCache.LocalManualCache(this);
    }

    private void checkNonLoadingCache() {
        Preconditions.checkState(this.refreshNanos == -1L, "refreshAfterWrite requires a LoadingCache");
    }

    private void checkWeightWithWeigher() {
        if (this.weigher == null) {
            Preconditions.checkState(this.maximumWeight == -1L, "maximumWeight requires weigher");
        } else if (this.strictParsing) {
            Preconditions.checkState(this.maximumWeight != -1L, "weigher requires maximumWeight");
        } else if (this.maximumWeight == -1L) {
            logger.log(Level.WARNING, "ignoring weigher specified without maximumWeight");
        }
    }

    public String toString() {
        Objects.ToStringHelper localToStringHelper = Objects.toStringHelper(this);
        if (this.initialCapacity != -1) {
            localToStringHelper.add("initialCapacity", this.initialCapacity);
        }
        if (this.concurrencyLevel != -1) {
            localToStringHelper.add("concurrencyLevel", this.concurrencyLevel);
        }
        if (this.maximumSize != -1L) {
            localToStringHelper.add("maximumSize", this.maximumSize);
        }
        if (this.maximumWeight != -1L) {
            localToStringHelper.add("maximumWeight", this.maximumWeight);
        }
        if (this.expireAfterWriteNanos != -1L) {
            localToStringHelper.add("expireAfterWrite", this.expireAfterWriteNanos + "ns");
        }
        if (this.expireAfterAccessNanos != -1L) {
            localToStringHelper.add("expireAfterAccess", this.expireAfterAccessNanos + "ns");
        }
        if (this.keyStrength != null) {
            localToStringHelper.add("keyStrength", Ascii.toLowerCase(this.keyStrength.toString()));
        }
        if (this.valueStrength != null) {
            localToStringHelper.add("valueStrength", Ascii.toLowerCase(this.valueStrength.toString()));
        }
        if (this.keyEquivalence != null) {
            localToStringHelper.addValue("keyEquivalence");
        }
        if (this.valueEquivalence != null) {
            localToStringHelper.addValue("valueEquivalence");
        }
        if (this.removalListener != null) {
            localToStringHelper.addValue("removalListener");
        }
        return localToStringHelper.toString();
    }

    static enum OneWeigher
            implements Weigher<Object, Object> {
        INSTANCE;

        private OneWeigher() {
        }

        public int weigh(Object paramObject1, Object paramObject2) {
            return 1;
        }
    }

    static enum NullListener
            implements RemovalListener<Object, Object> {
        INSTANCE;

        private NullListener() {
        }

        public void onRemoval(RemovalNotification<Object, Object> paramRemovalNotification) {
        }
    }
}




