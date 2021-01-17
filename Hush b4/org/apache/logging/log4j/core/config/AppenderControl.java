// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.filter.Filterable;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.filter.AbstractFilterable;

public class AppenderControl extends AbstractFilterable
{
    private final ThreadLocal<AppenderControl> recursive;
    private final Appender appender;
    private final Level level;
    private final int intLevel;
    
    public AppenderControl(final Appender appender, final Level level, final Filter filter) {
        super(filter);
        this.recursive = new ThreadLocal<AppenderControl>();
        this.appender = appender;
        this.level = level;
        this.intLevel = ((level == null) ? Level.ALL.intLevel() : level.intLevel());
        this.startFilter();
    }
    
    public Appender getAppender() {
        return this.appender;
    }
    
    public void callAppender(final LogEvent event) {
        if (this.getFilter() != null) {
            final Filter.Result r = this.getFilter().filter(event);
            if (r == Filter.Result.DENY) {
                return;
            }
        }
        if (this.level != null && this.intLevel < event.getLevel().intLevel()) {
            return;
        }
        if (this.recursive.get() != null) {
            this.appender.getHandler().error("Recursive call to appender " + this.appender.getName());
            return;
        }
        try {
            this.recursive.set(this);
            if (!this.appender.isStarted()) {
                this.appender.getHandler().error("Attempted to append to non-started appender " + this.appender.getName());
                if (!this.appender.ignoreExceptions()) {
                    throw new AppenderLoggingException("Attempted to append to non-started appender " + this.appender.getName());
                }
            }
            if (this.appender instanceof Filterable && ((Filterable)this.appender).isFiltered(event)) {
                return;
            }
            try {
                this.appender.append(event);
            }
            catch (RuntimeException ex) {
                this.appender.getHandler().error("An exception occurred processing Appender " + this.appender.getName(), ex);
                if (!this.appender.ignoreExceptions()) {
                    throw ex;
                }
            }
            catch (Exception ex2) {
                this.appender.getHandler().error("An exception occurred processing Appender " + this.appender.getName(), ex2);
                if (!this.appender.ignoreExceptions()) {
                    throw new AppenderLoggingException(ex2);
                }
            }
        }
        finally {
            this.recursive.set(null);
        }
    }
}
