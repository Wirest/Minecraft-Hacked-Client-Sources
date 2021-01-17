// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import com.google.common.base.Ticker;
import com.google.common.base.Preconditions;
import java.util.concurrent.TimeUnit;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.annotations.Beta;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
@Beta
public abstract class RateLimiter
{
    private final SleepingTicker ticker;
    private final long offsetNanos;
    double storedPermits;
    double maxPermits;
    volatile double stableIntervalMicros;
    private final Object mutex;
    private long nextFreeTicketMicros;
    
    public static RateLimiter create(final double permitsPerSecond) {
        return create(SleepingTicker.SYSTEM_TICKER, permitsPerSecond);
    }
    
    @VisibleForTesting
    static RateLimiter create(final SleepingTicker ticker, final double permitsPerSecond) {
        final RateLimiter rateLimiter = new Bursty(ticker, 1.0);
        rateLimiter.setRate(permitsPerSecond);
        return rateLimiter;
    }
    
    public static RateLimiter create(final double permitsPerSecond, final long warmupPeriod, final TimeUnit unit) {
        return create(SleepingTicker.SYSTEM_TICKER, permitsPerSecond, warmupPeriod, unit);
    }
    
    @VisibleForTesting
    static RateLimiter create(final SleepingTicker ticker, final double permitsPerSecond, final long warmupPeriod, final TimeUnit unit) {
        final RateLimiter rateLimiter = new WarmingUp(ticker, warmupPeriod, unit);
        rateLimiter.setRate(permitsPerSecond);
        return rateLimiter;
    }
    
    @VisibleForTesting
    static RateLimiter createWithCapacity(final SleepingTicker ticker, final double permitsPerSecond, final long maxBurstBuildup, final TimeUnit unit) {
        final double maxBurstSeconds = unit.toNanos(maxBurstBuildup) / 1.0E9;
        final Bursty rateLimiter = new Bursty(ticker, maxBurstSeconds);
        rateLimiter.setRate(permitsPerSecond);
        return rateLimiter;
    }
    
    private RateLimiter(final SleepingTicker ticker) {
        this.mutex = new Object();
        this.nextFreeTicketMicros = 0L;
        this.ticker = ticker;
        this.offsetNanos = ticker.read();
    }
    
    public final void setRate(final double permitsPerSecond) {
        Preconditions.checkArgument(permitsPerSecond > 0.0 && !Double.isNaN(permitsPerSecond), (Object)"rate must be positive");
        synchronized (this.mutex) {
            this.resync(this.readSafeMicros());
            final double stableIntervalMicros = TimeUnit.SECONDS.toMicros(1L) / permitsPerSecond;
            this.doSetRate(permitsPerSecond, this.stableIntervalMicros = stableIntervalMicros);
        }
    }
    
    abstract void doSetRate(final double p0, final double p1);
    
    public final double getRate() {
        return TimeUnit.SECONDS.toMicros(1L) / this.stableIntervalMicros;
    }
    
    public double acquire() {
        return this.acquire(1);
    }
    
    public double acquire(final int permits) {
        final long microsToWait = this.reserve(permits);
        this.ticker.sleepMicrosUninterruptibly(microsToWait);
        return 1.0 * microsToWait / TimeUnit.SECONDS.toMicros(1L);
    }
    
    long reserve() {
        return this.reserve(1);
    }
    
    long reserve(final int permits) {
        checkPermits(permits);
        synchronized (this.mutex) {
            return this.reserveNextTicket(permits, this.readSafeMicros());
        }
    }
    
    public boolean tryAcquire(final long timeout, final TimeUnit unit) {
        return this.tryAcquire(1, timeout, unit);
    }
    
    public boolean tryAcquire(final int permits) {
        return this.tryAcquire(permits, 0L, TimeUnit.MICROSECONDS);
    }
    
    public boolean tryAcquire() {
        return this.tryAcquire(1, 0L, TimeUnit.MICROSECONDS);
    }
    
    public boolean tryAcquire(final int permits, final long timeout, final TimeUnit unit) {
        final long timeoutMicros = unit.toMicros(timeout);
        checkPermits(permits);
        final long microsToWait;
        synchronized (this.mutex) {
            final long nowMicros = this.readSafeMicros();
            if (this.nextFreeTicketMicros > nowMicros + timeoutMicros) {
                return false;
            }
            microsToWait = this.reserveNextTicket(permits, nowMicros);
        }
        this.ticker.sleepMicrosUninterruptibly(microsToWait);
        return true;
    }
    
    private static void checkPermits(final int permits) {
        Preconditions.checkArgument(permits > 0, (Object)"Requested permits must be positive");
    }
    
    private long reserveNextTicket(final double requiredPermits, final long nowMicros) {
        this.resync(nowMicros);
        final long microsToNextFreeTicket = Math.max(0L, this.nextFreeTicketMicros - nowMicros);
        final double storedPermitsToSpend = Math.min(requiredPermits, this.storedPermits);
        final double freshPermits = requiredPermits - storedPermitsToSpend;
        final long waitMicros = this.storedPermitsToWaitTime(this.storedPermits, storedPermitsToSpend) + (long)(freshPermits * this.stableIntervalMicros);
        this.nextFreeTicketMicros += waitMicros;
        this.storedPermits -= storedPermitsToSpend;
        return microsToNextFreeTicket;
    }
    
    abstract long storedPermitsToWaitTime(final double p0, final double p1);
    
    private void resync(final long nowMicros) {
        if (nowMicros > this.nextFreeTicketMicros) {
            this.storedPermits = Math.min(this.maxPermits, this.storedPermits + (nowMicros - this.nextFreeTicketMicros) / this.stableIntervalMicros);
            this.nextFreeTicketMicros = nowMicros;
        }
    }
    
    private long readSafeMicros() {
        return TimeUnit.NANOSECONDS.toMicros(this.ticker.read() - this.offsetNanos);
    }
    
    @Override
    public String toString() {
        return String.format("RateLimiter[stableRate=%3.1fqps]", 1000000.0 / this.stableIntervalMicros);
    }
    
    private static class WarmingUp extends RateLimiter
    {
        final long warmupPeriodMicros;
        private double slope;
        private double halfPermits;
        
        WarmingUp(final SleepingTicker ticker, final long warmupPeriod, final TimeUnit timeUnit) {
            super(ticker, null);
            this.warmupPeriodMicros = timeUnit.toMicros(warmupPeriod);
        }
        
        @Override
        void doSetRate(final double permitsPerSecond, final double stableIntervalMicros) {
            final double oldMaxPermits = this.maxPermits;
            this.maxPermits = this.warmupPeriodMicros / stableIntervalMicros;
            this.halfPermits = this.maxPermits / 2.0;
            final double coldIntervalMicros = stableIntervalMicros * 3.0;
            this.slope = (coldIntervalMicros - stableIntervalMicros) / this.halfPermits;
            if (oldMaxPermits == Double.POSITIVE_INFINITY) {
                this.storedPermits = 0.0;
            }
            else {
                this.storedPermits = ((oldMaxPermits == 0.0) ? this.maxPermits : (this.storedPermits * this.maxPermits / oldMaxPermits));
            }
        }
        
        @Override
        long storedPermitsToWaitTime(final double storedPermits, double permitsToTake) {
            final double availablePermitsAboveHalf = storedPermits - this.halfPermits;
            long micros = 0L;
            if (availablePermitsAboveHalf > 0.0) {
                final double permitsAboveHalfToTake = Math.min(availablePermitsAboveHalf, permitsToTake);
                micros = (long)(permitsAboveHalfToTake * (this.permitsToTime(availablePermitsAboveHalf) + this.permitsToTime(availablePermitsAboveHalf - permitsAboveHalfToTake)) / 2.0);
                permitsToTake -= permitsAboveHalfToTake;
            }
            micros += (long)(this.stableIntervalMicros * permitsToTake);
            return micros;
        }
        
        private double permitsToTime(final double permits) {
            return this.stableIntervalMicros + permits * this.slope;
        }
    }
    
    private static class Bursty extends RateLimiter
    {
        final double maxBurstSeconds;
        
        Bursty(final SleepingTicker ticker, final double maxBurstSeconds) {
            super(ticker, null);
            this.maxBurstSeconds = maxBurstSeconds;
        }
        
        @Override
        void doSetRate(final double permitsPerSecond, final double stableIntervalMicros) {
            final double oldMaxPermits = this.maxPermits;
            this.maxPermits = this.maxBurstSeconds * permitsPerSecond;
            this.storedPermits = ((oldMaxPermits == 0.0) ? 0.0 : (this.storedPermits * this.maxPermits / oldMaxPermits));
        }
        
        @Override
        long storedPermitsToWaitTime(final double storedPermits, final double permitsToTake) {
            return 0L;
        }
    }
    
    @VisibleForTesting
    abstract static class SleepingTicker extends Ticker
    {
        static final SleepingTicker SYSTEM_TICKER;
        
        abstract void sleepMicrosUninterruptibly(final long p0);
        
        static {
            SYSTEM_TICKER = new SleepingTicker() {
                @Override
                public long read() {
                    return Ticker.systemTicker().read();
                }
                
                public void sleepMicrosUninterruptibly(final long micros) {
                    if (micros > 0L) {
                        Uninterruptibles.sleepUninterruptibly(micros, TimeUnit.MICROSECONDS);
                    }
                }
            };
        }
    }
}
