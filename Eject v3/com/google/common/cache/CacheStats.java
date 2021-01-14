package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;

@Beta
@GwtCompatible
public final class CacheStats {
    private final long hitCount;
    private final long missCount;
    private final long loadSuccessCount;
    private final long loadExceptionCount;
    private final long totalLoadTime;
    private final long evictionCount;

    public CacheStats(long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5, long paramLong6) {
        Preconditions.checkArgument(paramLong1 >= 0L);
        Preconditions.checkArgument(paramLong2 >= 0L);
        Preconditions.checkArgument(paramLong3 >= 0L);
        Preconditions.checkArgument(paramLong4 >= 0L);
        Preconditions.checkArgument(paramLong5 >= 0L);
        Preconditions.checkArgument(paramLong6 >= 0L);
        this.hitCount = paramLong1;
        this.missCount = paramLong2;
        this.loadSuccessCount = paramLong3;
        this.loadExceptionCount = paramLong4;
        this.totalLoadTime = paramLong5;
        this.evictionCount = paramLong6;
    }

    public long requestCount() {
        return this.hitCount + this.missCount;
    }

    public long hitCount() {
        return this.hitCount;
    }

    public double hitRate() {
        long l = requestCount();
        return l == 0L ? 1.0D : this.hitCount / l;
    }

    public long missCount() {
        return this.missCount;
    }

    public double missRate() {
        long l = requestCount();
        return l == 0L ? 0.0D : this.missCount / l;
    }

    public long loadCount() {
        return this.loadSuccessCount + this.loadExceptionCount;
    }

    public long loadSuccessCount() {
        return this.loadSuccessCount;
    }

    public long loadExceptionCount() {
        return this.loadExceptionCount;
    }

    public double loadExceptionRate() {
        long l = this.loadSuccessCount + this.loadExceptionCount;
        return l == 0L ? 0.0D : this.loadExceptionCount / l;
    }

    public long totalLoadTime() {
        return this.totalLoadTime;
    }

    public double averageLoadPenalty() {
        long l = this.loadSuccessCount + this.loadExceptionCount;
        return l == 0L ? 0.0D : this.totalLoadTime / l;
    }

    public long evictionCount() {
        return this.evictionCount;
    }

    public CacheStats minus(CacheStats paramCacheStats) {
        return new CacheStats(Math.max(0L, this.hitCount - paramCacheStats.hitCount), Math.max(0L, this.missCount - paramCacheStats.missCount), Math.max(0L, this.loadSuccessCount - paramCacheStats.loadSuccessCount), Math.max(0L, this.loadExceptionCount - paramCacheStats.loadExceptionCount), Math.max(0L, this.totalLoadTime - paramCacheStats.totalLoadTime), Math.max(0L, this.evictionCount - paramCacheStats.evictionCount));
    }

    public CacheStats plus(CacheStats paramCacheStats) {
        return new CacheStats(this.hitCount + paramCacheStats.hitCount, this.missCount + paramCacheStats.missCount, this.loadSuccessCount + paramCacheStats.loadSuccessCount, this.loadExceptionCount + paramCacheStats.loadExceptionCount, this.totalLoadTime + paramCacheStats.totalLoadTime, this.evictionCount + paramCacheStats.evictionCount);
    }

    public int hashCode() {
        return Objects.hashCode(new Object[]{Long.valueOf(this.hitCount), Long.valueOf(this.missCount), Long.valueOf(this.loadSuccessCount), Long.valueOf(this.loadExceptionCount), Long.valueOf(this.totalLoadTime), Long.valueOf(this.evictionCount)});
    }

    public boolean equals(@Nullable Object paramObject) {
        if ((paramObject instanceof CacheStats)) {
            CacheStats localCacheStats = (CacheStats) paramObject;
            return (this.hitCount == localCacheStats.hitCount) && (this.missCount == localCacheStats.missCount) && (this.loadSuccessCount == localCacheStats.loadSuccessCount) && (this.loadExceptionCount == localCacheStats.loadExceptionCount) && (this.totalLoadTime == localCacheStats.totalLoadTime) && (this.evictionCount == localCacheStats.evictionCount);
        }
        return false;
    }

    public String toString() {
        return Objects.toStringHelper(this).add("hitCount", this.hitCount).add("missCount", this.missCount).add("loadSuccessCount", this.loadSuccessCount).add("loadExceptionCount", this.loadExceptionCount).add("totalLoadTime", this.totalLoadTime).add("evictionCount", this.evictionCount).toString();
    }
}




