// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.filter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Delayed;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.util.Iterator;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Logger;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.logging.log4j.core.Filter;
import java.util.Queue;
import java.util.concurrent.DelayQueue;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "BurstFilter", category = "Core", elementType = "filter", printObject = true)
public final class BurstFilter extends AbstractFilter
{
    private static final long NANOS_IN_SECONDS = 1000000000L;
    private static final int DEFAULT_RATE = 10;
    private static final int DEFAULT_RATE_MULTIPLE = 100;
    private static final int HASH_SHIFT = 32;
    private final Level level;
    private final long burstInterval;
    private final DelayQueue<LogDelay> history;
    private final Queue<LogDelay> available;
    
    private BurstFilter(final Level level, final float rate, final long maxBurst, final Filter.Result onMatch, final Filter.Result onMismatch) {
        super(onMatch, onMismatch);
        this.history = new DelayQueue<LogDelay>();
        this.available = new ConcurrentLinkedQueue<LogDelay>();
        this.level = level;
        this.burstInterval = (long)(1.0E9f * (maxBurst / rate));
        for (int i = 0; i < maxBurst; ++i) {
            this.available.add(new LogDelay());
        }
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object... params) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Object msg, final Throwable t) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final LogEvent event) {
        return this.filter(event.getLevel());
    }
    
    private Filter.Result filter(final Level level) {
        if (!this.level.isAtLeastAsSpecificAs(level)) {
            return this.onMatch;
        }
        for (LogDelay delay = this.history.poll(); delay != null; delay = this.history.poll()) {
            this.available.add(delay);
        }
        LogDelay delay = this.available.poll();
        if (delay != null) {
            delay.setDelay(this.burstInterval);
            this.history.add(delay);
            return this.onMatch;
        }
        return this.onMismatch;
    }
    
    public int getAvailable() {
        return this.available.size();
    }
    
    public void clear() {
        for (final LogDelay delay : this.history) {
            this.history.remove(delay);
            this.available.add(delay);
        }
    }
    
    @Override
    public String toString() {
        return "level=" + this.level.toString() + ", interval=" + this.burstInterval + ", max=" + this.history.size();
    }
    
    @PluginFactory
    public static BurstFilter createFilter(@PluginAttribute("level") final String levelName, @PluginAttribute("rate") final String rate, @PluginAttribute("maxBurst") final String maxBurst, @PluginAttribute("onMatch") final String match, @PluginAttribute("onMismatch") final String mismatch) {
        final Filter.Result onMatch = Filter.Result.toResult(match, Filter.Result.NEUTRAL);
        final Filter.Result onMismatch = Filter.Result.toResult(mismatch, Filter.Result.DENY);
        final Level level = Level.toLevel(levelName, Level.WARN);
        float eventRate = (rate == null) ? 10.0f : Float.parseFloat(rate);
        if (eventRate <= 0.0f) {
            eventRate = 10.0f;
        }
        final long max = (maxBurst == null) ? ((long)(eventRate * 100.0f)) : Long.parseLong(maxBurst);
        return new BurstFilter(level, eventRate, max, onMatch, onMismatch);
    }
    
    private class LogDelay implements Delayed
    {
        private long expireTime;
        
        public LogDelay() {
        }
        
        public void setDelay(final long delay) {
            this.expireTime = delay + System.nanoTime();
        }
        
        @Override
        public long getDelay(final TimeUnit timeUnit) {
            return timeUnit.convert(this.expireTime - System.nanoTime(), TimeUnit.NANOSECONDS);
        }
        
        @Override
        public int compareTo(final Delayed delayed) {
            if (this.expireTime < ((LogDelay)delayed).expireTime) {
                return -1;
            }
            if (this.expireTime > ((LogDelay)delayed).expireTime) {
                return 1;
            }
            return 0;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final LogDelay logDelay = (LogDelay)o;
            return this.expireTime == logDelay.expireTime;
        }
        
        @Override
        public int hashCode() {
            return (int)(this.expireTime ^ this.expireTime >>> 32);
        }
    }
}
