// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.Filter;

public abstract class AbstractFilter implements Filter, LifeCycle
{
    protected static final Logger LOGGER;
    protected final Result onMatch;
    protected final Result onMismatch;
    private boolean started;
    
    protected AbstractFilter() {
        this(null, null);
    }
    
    protected AbstractFilter(final Result onMatch, final Result onMismatch) {
        this.onMatch = ((onMatch == null) ? Result.NEUTRAL : onMatch);
        this.onMismatch = ((onMismatch == null) ? Result.DENY : onMismatch);
    }
    
    @Override
    public void start() {
        this.started = true;
    }
    
    @Override
    public boolean isStarted() {
        return this.started;
    }
    
    @Override
    public void stop() {
        this.started = false;
    }
    
    @Override
    public final Result getOnMismatch() {
        return this.onMismatch;
    }
    
    @Override
    public final Result getOnMatch() {
        return this.onMatch;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
    
    @Override
    public Result filter(final org.apache.logging.log4j.core.Logger logger, final Level level, final Marker marker, final String msg, final Object... params) {
        return Result.NEUTRAL;
    }
    
    @Override
    public Result filter(final org.apache.logging.log4j.core.Logger logger, final Level level, final Marker marker, final Object msg, final Throwable t) {
        return Result.NEUTRAL;
    }
    
    @Override
    public Result filter(final org.apache.logging.log4j.core.Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t) {
        return Result.NEUTRAL;
    }
    
    @Override
    public Result filter(final LogEvent event) {
        return Result.NEUTRAL;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
