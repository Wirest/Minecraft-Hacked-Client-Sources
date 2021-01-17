// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core;

import org.apache.logging.log4j.core.config.Configuration;
import java.util.List;
import org.apache.logging.log4j.core.filter.CompositeFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;

public class Logger extends AbstractLogger
{
    protected volatile PrivateConfig config;
    private final LoggerContext context;
    
    protected Logger(final LoggerContext context, final String name, final MessageFactory messageFactory) {
        super(name, messageFactory);
        this.context = context;
        this.config = new PrivateConfig(context.getConfiguration(), this);
    }
    
    public Logger getParent() {
        final LoggerConfig lc = this.config.loggerConfig.getName().equals(this.getName()) ? this.config.loggerConfig.getParent() : this.config.loggerConfig;
        if (lc == null) {
            return null;
        }
        if (this.context.hasLogger(lc.getName())) {
            return this.context.getLogger(lc.getName(), this.getMessageFactory());
        }
        return new Logger(this.context, lc.getName(), this.getMessageFactory());
    }
    
    public LoggerContext getContext() {
        return this.context;
    }
    
    public synchronized void setLevel(final Level level) {
        if (level != null) {
            this.config = new PrivateConfig(this.config, level);
        }
    }
    
    public Level getLevel() {
        return this.config.level;
    }
    
    @Override
    public void log(final Marker marker, final String fqcn, final Level level, Message data, final Throwable t) {
        if (data == null) {
            data = new SimpleMessage("");
        }
        this.config.config.getConfigurationMonitor().checkConfiguration();
        this.config.loggerConfig.log(this.getName(), marker, fqcn, level, data, t);
    }
    
    public boolean isEnabled(final Level level, final Marker marker, final String msg) {
        return this.config.filter(level, marker, msg);
    }
    
    public boolean isEnabled(final Level level, final Marker marker, final String msg, final Throwable t) {
        return this.config.filter(level, marker, msg, t);
    }
    
    public boolean isEnabled(final Level level, final Marker marker, final String msg, final Object... p1) {
        return this.config.filter(level, marker, msg, p1);
    }
    
    public boolean isEnabled(final Level level, final Marker marker, final Object msg, final Throwable t) {
        return this.config.filter(level, marker, msg, t);
    }
    
    public boolean isEnabled(final Level level, final Marker marker, final Message msg, final Throwable t) {
        return this.config.filter(level, marker, msg, t);
    }
    
    public void addAppender(final Appender appender) {
        this.config.config.addLoggerAppender(this, appender);
    }
    
    public void removeAppender(final Appender appender) {
        this.config.loggerConfig.removeAppender(appender.getName());
    }
    
    public Map<String, Appender> getAppenders() {
        return this.config.loggerConfig.getAppenders();
    }
    
    public Iterator<Filter> getFilters() {
        final Filter filter = this.config.loggerConfig.getFilter();
        if (filter == null) {
            return new ArrayList<Filter>().iterator();
        }
        if (filter instanceof CompositeFilter) {
            return ((CompositeFilter)filter).iterator();
        }
        final List<Filter> filters = new ArrayList<Filter>();
        filters.add(filter);
        return filters.iterator();
    }
    
    public int filterCount() {
        final Filter filter = this.config.loggerConfig.getFilter();
        if (filter == null) {
            return 0;
        }
        if (filter instanceof CompositeFilter) {
            return ((CompositeFilter)filter).size();
        }
        return 1;
    }
    
    public void addFilter(final Filter filter) {
        this.config.config.addLoggerFilter(this, filter);
    }
    
    public boolean isAdditive() {
        return this.config.loggerConfig.isAdditive();
    }
    
    public void setAdditive(final boolean additive) {
        this.config.config.setLoggerAdditive(this, additive);
    }
    
    void updateConfiguration(final Configuration config) {
        this.config = new PrivateConfig(config, this);
    }
    
    @Override
    public String toString() {
        final String nameLevel = "" + this.getName() + ":" + this.getLevel();
        if (this.context == null) {
            return nameLevel;
        }
        final String contextName = this.context.getName();
        return (contextName == null) ? nameLevel : (nameLevel + " in " + contextName);
    }
    
    protected class PrivateConfig
    {
        public final LoggerConfig loggerConfig;
        public final Configuration config;
        private final Level level;
        private final int intLevel;
        private final Logger logger;
        
        public PrivateConfig(final Configuration config, final Logger logger) {
            this.config = config;
            this.loggerConfig = config.getLoggerConfig(Logger.this.getName());
            this.level = this.loggerConfig.getLevel();
            this.intLevel = this.level.intLevel();
            this.logger = logger;
        }
        
        public PrivateConfig(final PrivateConfig pc, final Level level) {
            this.config = pc.config;
            this.loggerConfig = pc.loggerConfig;
            this.level = level;
            this.intLevel = this.level.intLevel();
            this.logger = pc.logger;
        }
        
        public PrivateConfig(final PrivateConfig pc, final LoggerConfig lc) {
            this.config = pc.config;
            this.loggerConfig = lc;
            this.level = lc.getLevel();
            this.intLevel = this.level.intLevel();
            this.logger = pc.logger;
        }
        
        public void logEvent(final LogEvent event) {
            this.config.getConfigurationMonitor().checkConfiguration();
            this.loggerConfig.log(event);
        }
        
        boolean filter(final Level level, final Marker marker, final String msg) {
            this.config.getConfigurationMonitor().checkConfiguration();
            final Filter filter = this.config.getFilter();
            if (filter != null) {
                final Filter.Result r = filter.filter(this.logger, level, marker, msg, new Object[0]);
                if (r != Filter.Result.NEUTRAL) {
                    return r == Filter.Result.ACCEPT;
                }
            }
            return this.intLevel >= level.intLevel();
        }
        
        boolean filter(final Level level, final Marker marker, final String msg, final Throwable t) {
            this.config.getConfigurationMonitor().checkConfiguration();
            final Filter filter = this.config.getFilter();
            if (filter != null) {
                final Filter.Result r = filter.filter(this.logger, level, marker, (Object)msg, t);
                if (r != Filter.Result.NEUTRAL) {
                    return r == Filter.Result.ACCEPT;
                }
            }
            return this.intLevel >= level.intLevel();
        }
        
        boolean filter(final Level level, final Marker marker, final String msg, final Object... p1) {
            this.config.getConfigurationMonitor().checkConfiguration();
            final Filter filter = this.config.getFilter();
            if (filter != null) {
                final Filter.Result r = filter.filter(this.logger, level, marker, msg, p1);
                if (r != Filter.Result.NEUTRAL) {
                    return r == Filter.Result.ACCEPT;
                }
            }
            return this.intLevel >= level.intLevel();
        }
        
        boolean filter(final Level level, final Marker marker, final Object msg, final Throwable t) {
            this.config.getConfigurationMonitor().checkConfiguration();
            final Filter filter = this.config.getFilter();
            if (filter != null) {
                final Filter.Result r = filter.filter(this.logger, level, marker, msg, t);
                if (r != Filter.Result.NEUTRAL) {
                    return r == Filter.Result.ACCEPT;
                }
            }
            return this.intLevel >= level.intLevel();
        }
        
        boolean filter(final Level level, final Marker marker, final Message msg, final Throwable t) {
            this.config.getConfigurationMonitor().checkConfiguration();
            final Filter filter = this.config.getFilter();
            if (filter != null) {
                final Filter.Result r = filter.filter(this.logger, level, marker, msg, t);
                if (r != Filter.Result.NEUTRAL) {
                    return r == Filter.Result.ACCEPT;
                }
            }
            return this.intLevel >= level.intLevel();
        }
    }
}
