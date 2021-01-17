// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;
import java.util.concurrent.TimeUnit;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible(emulated = true)
public final class Stopwatch
{
    private final Ticker ticker;
    private boolean isRunning;
    private long elapsedNanos;
    private long startTick;
    
    public static Stopwatch createUnstarted() {
        return new Stopwatch();
    }
    
    public static Stopwatch createUnstarted(final Ticker ticker) {
        return new Stopwatch(ticker);
    }
    
    public static Stopwatch createStarted() {
        return new Stopwatch().start();
    }
    
    public static Stopwatch createStarted(final Ticker ticker) {
        return new Stopwatch(ticker).start();
    }
    
    @Deprecated
    Stopwatch() {
        this(Ticker.systemTicker());
    }
    
    @Deprecated
    Stopwatch(final Ticker ticker) {
        this.ticker = Preconditions.checkNotNull(ticker, (Object)"ticker");
    }
    
    public boolean isRunning() {
        return this.isRunning;
    }
    
    public Stopwatch start() {
        Preconditions.checkState(!this.isRunning, (Object)"This stopwatch is already running.");
        this.isRunning = true;
        this.startTick = this.ticker.read();
        return this;
    }
    
    public Stopwatch stop() {
        final long tick = this.ticker.read();
        Preconditions.checkState(this.isRunning, (Object)"This stopwatch is already stopped.");
        this.isRunning = false;
        this.elapsedNanos += tick - this.startTick;
        return this;
    }
    
    public Stopwatch reset() {
        this.elapsedNanos = 0L;
        this.isRunning = false;
        return this;
    }
    
    private long elapsedNanos() {
        return this.isRunning ? (this.ticker.read() - this.startTick + this.elapsedNanos) : this.elapsedNanos;
    }
    
    public long elapsed(final TimeUnit desiredUnit) {
        return desiredUnit.convert(this.elapsedNanos(), TimeUnit.NANOSECONDS);
    }
    
    @GwtIncompatible("String.format()")
    @Override
    public String toString() {
        final long nanos = this.elapsedNanos();
        final TimeUnit unit = chooseUnit(nanos);
        final double value = nanos / (double)TimeUnit.NANOSECONDS.convert(1L, unit);
        return String.format("%.4g %s", value, abbreviate(unit));
    }
    
    private static TimeUnit chooseUnit(final long nanos) {
        if (TimeUnit.DAYS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
            return TimeUnit.DAYS;
        }
        if (TimeUnit.HOURS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
            return TimeUnit.HOURS;
        }
        if (TimeUnit.MINUTES.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
            return TimeUnit.MINUTES;
        }
        if (TimeUnit.SECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
            return TimeUnit.SECONDS;
        }
        if (TimeUnit.MILLISECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
            return TimeUnit.MILLISECONDS;
        }
        if (TimeUnit.MICROSECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
            return TimeUnit.MICROSECONDS;
        }
        return TimeUnit.NANOSECONDS;
    }
    
    private static String abbreviate(final TimeUnit unit) {
        switch (unit) {
            case NANOSECONDS: {
                return "ns";
            }
            case MICROSECONDS: {
                return "\u03bcs";
            }
            case MILLISECONDS: {
                return "ms";
            }
            case SECONDS: {
                return "s";
            }
            case MINUTES: {
                return "min";
            }
            case HOURS: {
                return "h";
            }
            case DAYS: {
                return "d";
            }
            default: {
                throw new AssertionError();
            }
        }
    }
}
