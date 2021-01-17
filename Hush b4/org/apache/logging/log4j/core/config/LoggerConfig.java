// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.impl.DefaultLogEventFactory;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.async.AsyncLoggerContextSelector;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import java.util.Arrays;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import java.util.Collections;
import org.apache.logging.log4j.core.LifeCycle;
import java.util.Collection;
import java.util.Iterator;
import org.apache.logging.log4j.core.Appender;
import java.util.HashMap;
import org.apache.logging.log4j.core.Filter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.Level;
import java.util.Map;
import java.util.List;
import org.apache.logging.log4j.core.impl.LogEventFactory;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.filter.AbstractFilterable;

@Plugin(name = "logger", category = "Core", printObject = true)
public class LoggerConfig extends AbstractFilterable
{
    protected static final Logger LOGGER;
    private static final int MAX_RETRIES = 3;
    private static final long WAIT_TIME = 1000L;
    private static LogEventFactory LOG_EVENT_FACTORY;
    private List<AppenderRef> appenderRefs;
    private final Map<String, AppenderControl> appenders;
    private final String name;
    private LogEventFactory logEventFactory;
    private Level level;
    private boolean additive;
    private boolean includeLocation;
    private LoggerConfig parent;
    private final AtomicInteger counter;
    private boolean shutdown;
    private final Map<Property, Boolean> properties;
    private final Configuration config;
    
    public LoggerConfig() {
        this.appenderRefs = new ArrayList<AppenderRef>();
        this.appenders = new ConcurrentHashMap<String, AppenderControl>();
        this.additive = true;
        this.includeLocation = true;
        this.counter = new AtomicInteger();
        this.shutdown = false;
        this.logEventFactory = LoggerConfig.LOG_EVENT_FACTORY;
        this.level = Level.ERROR;
        this.name = "";
        this.properties = null;
        this.config = null;
    }
    
    public LoggerConfig(final String name, final Level level, final boolean additive) {
        this.appenderRefs = new ArrayList<AppenderRef>();
        this.appenders = new ConcurrentHashMap<String, AppenderControl>();
        this.additive = true;
        this.includeLocation = true;
        this.counter = new AtomicInteger();
        this.shutdown = false;
        this.logEventFactory = LoggerConfig.LOG_EVENT_FACTORY;
        this.name = name;
        this.level = level;
        this.additive = additive;
        this.properties = null;
        this.config = null;
    }
    
    protected LoggerConfig(final String name, final List<AppenderRef> appenders, final Filter filter, final Level level, final boolean additive, final Property[] properties, final Configuration config, final boolean includeLocation) {
        super(filter);
        this.appenderRefs = new ArrayList<AppenderRef>();
        this.appenders = new ConcurrentHashMap<String, AppenderControl>();
        this.additive = true;
        this.includeLocation = true;
        this.counter = new AtomicInteger();
        this.shutdown = false;
        this.logEventFactory = LoggerConfig.LOG_EVENT_FACTORY;
        this.name = name;
        this.appenderRefs = appenders;
        this.level = level;
        this.additive = additive;
        this.includeLocation = includeLocation;
        this.config = config;
        if (properties != null && properties.length > 0) {
            this.properties = new HashMap<Property, Boolean>(properties.length);
            for (final Property prop : properties) {
                final boolean interpolate = prop.getValue().contains("${");
                this.properties.put(prop, interpolate);
            }
        }
        else {
            this.properties = null;
        }
    }
    
    @Override
    public Filter getFilter() {
        return super.getFilter();
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setParent(final LoggerConfig parent) {
        this.parent = parent;
    }
    
    public LoggerConfig getParent() {
        return this.parent;
    }
    
    public void addAppender(final Appender appender, final Level level, final Filter filter) {
        this.appenders.put(appender.getName(), new AppenderControl(appender, level, filter));
    }
    
    public void removeAppender(final String name) {
        final AppenderControl ctl = this.appenders.remove(name);
        if (ctl != null) {
            this.cleanupFilter(ctl);
        }
    }
    
    public Map<String, Appender> getAppenders() {
        final Map<String, Appender> map = new HashMap<String, Appender>();
        for (final Map.Entry<String, AppenderControl> entry : this.appenders.entrySet()) {
            map.put(entry.getKey(), entry.getValue().getAppender());
        }
        return map;
    }
    
    protected void clearAppenders() {
        this.waitForCompletion();
        final Collection<AppenderControl> controls = this.appenders.values();
        final Iterator<AppenderControl> iterator = controls.iterator();
        while (iterator.hasNext()) {
            final AppenderControl ctl = iterator.next();
            iterator.remove();
            this.cleanupFilter(ctl);
        }
    }
    
    private void cleanupFilter(final AppenderControl ctl) {
        final Filter filter = ctl.getFilter();
        if (filter != null) {
            ctl.removeFilter(filter);
            if (filter instanceof LifeCycle) {
                ((LifeCycle)filter).stop();
            }
        }
    }
    
    public List<AppenderRef> getAppenderRefs() {
        return this.appenderRefs;
    }
    
    public void setLevel(final Level level) {
        this.level = level;
    }
    
    public Level getLevel() {
        return this.level;
    }
    
    public LogEventFactory getLogEventFactory() {
        return this.logEventFactory;
    }
    
    public void setLogEventFactory(final LogEventFactory logEventFactory) {
        this.logEventFactory = logEventFactory;
    }
    
    public boolean isAdditive() {
        return this.additive;
    }
    
    public void setAdditive(final boolean additive) {
        this.additive = additive;
    }
    
    public boolean isIncludeLocation() {
        return this.includeLocation;
    }
    
    public Map<Property, Boolean> getProperties() {
        return (this.properties == null) ? null : Collections.unmodifiableMap((Map<? extends Property, ? extends Boolean>)this.properties);
    }
    
    public void log(final String loggerName, final Marker marker, final String fqcn, final Level level, final Message data, final Throwable t) {
        List<Property> props = null;
        if (this.properties != null) {
            props = new ArrayList<Property>(this.properties.size());
            for (final Map.Entry<Property, Boolean> entry : this.properties.entrySet()) {
                final Property prop = entry.getKey();
                final String value = entry.getValue() ? this.config.getStrSubstitutor().replace(prop.getValue()) : prop.getValue();
                props.add(Property.createProperty(prop.getName(), value));
            }
        }
        final LogEvent event = this.logEventFactory.createEvent(loggerName, marker, fqcn, level, data, props, t);
        this.log(event);
    }
    
    private synchronized void waitForCompletion() {
        if (this.shutdown) {
            return;
        }
        this.shutdown = true;
        int retries = 0;
        while (this.counter.get() > 0) {
            try {
                this.wait(1000L * (retries + 1));
            }
            catch (InterruptedException ie) {
                if (++retries > 3) {
                    break;
                }
                continue;
            }
        }
    }
    
    public void log(final LogEvent event) {
        this.counter.incrementAndGet();
        try {
            if (this.isFiltered(event)) {
                return;
            }
            event.setIncludeLocation(this.isIncludeLocation());
            this.callAppenders(event);
            if (this.additive && this.parent != null) {
                this.parent.log(event);
            }
        }
        finally {
            if (this.counter.decrementAndGet() == 0) {
                synchronized (this) {
                    if (this.shutdown) {
                        this.notifyAll();
                    }
                }
            }
        }
    }
    
    protected void callAppenders(final LogEvent event) {
        for (final AppenderControl control : this.appenders.values()) {
            control.callAppender(event);
        }
    }
    
    @Override
    public String toString() {
        return Strings.isEmpty(this.name) ? "root" : this.name;
    }
    
    @PluginFactory
    public static LoggerConfig createLogger(@PluginAttribute("additivity") final String additivity, @PluginAttribute("level") final String levelName, @PluginAttribute("name") final String loggerName, @PluginAttribute("includeLocation") final String includeLocation, @PluginElement("AppenderRef") final AppenderRef[] refs, @PluginElement("Properties") final Property[] properties, @PluginConfiguration final Configuration config, @PluginElement("Filters") final Filter filter) {
        if (loggerName == null) {
            LoggerConfig.LOGGER.error("Loggers cannot be configured without a name");
            return null;
        }
        final List<AppenderRef> appenderRefs = Arrays.asList(refs);
        Level level;
        try {
            level = Level.toLevel(levelName, Level.ERROR);
        }
        catch (Exception ex) {
            LoggerConfig.LOGGER.error("Invalid Log level specified: {}. Defaulting to Error", levelName);
            level = Level.ERROR;
        }
        final String name = loggerName.equals("root") ? "" : loggerName;
        final boolean additive = Booleans.parseBoolean(additivity, true);
        return new LoggerConfig(name, appenderRefs, filter, level, additive, properties, config, includeLocation(includeLocation));
    }
    
    protected static boolean includeLocation(final String includeLocationConfigValue) {
        if (includeLocationConfigValue == null) {
            final boolean sync = !AsyncLoggerContextSelector.class.getName().equals(System.getProperty("Log4jContextSelector"));
            return sync;
        }
        return Boolean.parseBoolean(includeLocationConfigValue);
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        LoggerConfig.LOG_EVENT_FACTORY = null;
        final String factory = PropertiesUtil.getProperties().getStringProperty("Log4jLogEventFactory");
        if (factory != null) {
            try {
                final Class<?> clazz = Loader.loadClass(factory);
                if (clazz != null && LogEventFactory.class.isAssignableFrom(clazz)) {
                    LoggerConfig.LOG_EVENT_FACTORY = (LogEventFactory)clazz.newInstance();
                }
            }
            catch (Exception ex) {
                LoggerConfig.LOGGER.error("Unable to create LogEventFactory " + factory, ex);
            }
        }
        if (LoggerConfig.LOG_EVENT_FACTORY == null) {
            LoggerConfig.LOG_EVENT_FACTORY = new DefaultLogEventFactory();
        }
    }
    
    @Plugin(name = "root", category = "Core", printObject = true)
    public static class RootLogger extends LoggerConfig
    {
        @PluginFactory
        public static LoggerConfig createLogger(@PluginAttribute("additivity") final String additivity, @PluginAttribute("level") final String levelName, @PluginAttribute("includeLocation") final String includeLocation, @PluginElement("AppenderRef") final AppenderRef[] refs, @PluginElement("Properties") final Property[] properties, @PluginConfiguration final Configuration config, @PluginElement("Filters") final Filter filter) {
            final List<AppenderRef> appenderRefs = Arrays.asList(refs);
            Level level;
            try {
                level = Level.toLevel(levelName, Level.ERROR);
            }
            catch (Exception ex) {
                RootLogger.LOGGER.error("Invalid Log level specified: {}. Defaulting to Error", levelName);
                level = Level.ERROR;
            }
            final boolean additive = Booleans.parseBoolean(additivity, true);
            return new LoggerConfig("", appenderRefs, filter, level, additive, properties, config, LoggerConfig.includeLocation(includeLocation));
        }
    }
}
