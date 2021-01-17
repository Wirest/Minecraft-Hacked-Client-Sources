// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core;

import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.helpers.Assert;
import java.util.Iterator;
import java.util.Map;
import java.beans.PropertyChangeEvent;
import org.apache.logging.log4j.core.helpers.NetUtils;
import java.util.HashMap;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.NullConfiguration;
import java.io.File;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.net.URI;
import org.apache.logging.log4j.core.config.Configuration;
import java.beans.PropertyChangeListener;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.ConfigurationListener;

public class LoggerContext implements org.apache.logging.log4j.spi.LoggerContext, ConfigurationListener, LifeCycle
{
    public static final String PROPERTY_CONFIG = "config";
    private static final StatusLogger LOGGER;
    private final ConcurrentMap<String, Logger> loggers;
    private final CopyOnWriteArrayList<PropertyChangeListener> propertyChangeListeners;
    private volatile Configuration config;
    private Object externalContext;
    private final String name;
    private URI configLocation;
    private ShutdownThread shutdownThread;
    private volatile Status status;
    private final Lock configLock;
    
    public LoggerContext(final String name) {
        this(name, null, (URI)null);
    }
    
    public LoggerContext(final String name, final Object externalContext) {
        this(name, externalContext, (URI)null);
    }
    
    public LoggerContext(final String name, final Object externalContext, final URI configLocn) {
        this.loggers = new ConcurrentHashMap<String, Logger>();
        this.propertyChangeListeners = new CopyOnWriteArrayList<PropertyChangeListener>();
        this.config = new DefaultConfiguration();
        this.shutdownThread = null;
        this.status = Status.INITIALIZED;
        this.configLock = new ReentrantLock();
        this.name = name;
        this.externalContext = externalContext;
        this.configLocation = configLocn;
    }
    
    public LoggerContext(final String name, final Object externalContext, final String configLocn) {
        this.loggers = new ConcurrentHashMap<String, Logger>();
        this.propertyChangeListeners = new CopyOnWriteArrayList<PropertyChangeListener>();
        this.config = new DefaultConfiguration();
        this.shutdownThread = null;
        this.status = Status.INITIALIZED;
        this.configLock = new ReentrantLock();
        this.name = name;
        this.externalContext = externalContext;
        if (configLocn != null) {
            URI uri;
            try {
                uri = new File(configLocn).toURI();
            }
            catch (Exception ex) {
                uri = null;
            }
            this.configLocation = uri;
        }
        else {
            this.configLocation = null;
        }
    }
    
    @Override
    public void start() {
        if (this.configLock.tryLock()) {
            try {
                if (this.status == Status.INITIALIZED || this.status == Status.STOPPED) {
                    this.status = Status.STARTING;
                    this.reconfigure();
                    if (this.config.isShutdownHookEnabled()) {
                        this.shutdownThread = new ShutdownThread(this);
                        try {
                            Runtime.getRuntime().addShutdownHook(this.shutdownThread);
                        }
                        catch (IllegalStateException ise) {
                            LoggerContext.LOGGER.warn("Unable to register shutdown hook due to JVM state");
                            this.shutdownThread = null;
                        }
                        catch (SecurityException se) {
                            LoggerContext.LOGGER.warn("Unable to register shutdown hook due to security restrictions");
                            this.shutdownThread = null;
                        }
                    }
                    this.status = Status.STARTED;
                }
            }
            finally {
                this.configLock.unlock();
            }
        }
    }
    
    public void start(final Configuration config) {
        if (this.configLock.tryLock()) {
            try {
                if ((this.status == Status.INITIALIZED || this.status == Status.STOPPED) && config.isShutdownHookEnabled()) {
                    this.shutdownThread = new ShutdownThread(this);
                    try {
                        Runtime.getRuntime().addShutdownHook(this.shutdownThread);
                    }
                    catch (IllegalStateException ise) {
                        LoggerContext.LOGGER.warn("Unable to register shutdown hook due to JVM state");
                        this.shutdownThread = null;
                    }
                    catch (SecurityException se) {
                        LoggerContext.LOGGER.warn("Unable to register shutdown hook due to security restrictions");
                        this.shutdownThread = null;
                    }
                    this.status = Status.STARTED;
                }
            }
            finally {
                this.configLock.unlock();
            }
        }
        this.setConfiguration(config);
    }
    
    @Override
    public void stop() {
        this.configLock.lock();
        try {
            if (this.status == Status.STOPPED) {
                return;
            }
            this.status = Status.STOPPING;
            if (this.shutdownThread != null) {
                Runtime.getRuntime().removeShutdownHook(this.shutdownThread);
                this.shutdownThread = null;
            }
            final Configuration prev = this.config;
            this.config = new NullConfiguration();
            this.updateLoggers();
            prev.stop();
            this.externalContext = null;
            LogManager.getFactory().removeContext(this);
            this.status = Status.STOPPED;
        }
        finally {
            this.configLock.unlock();
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public Status getStatus() {
        return this.status;
    }
    
    @Override
    public boolean isStarted() {
        return this.status == Status.STARTED;
    }
    
    public void setExternalContext(final Object context) {
        this.externalContext = context;
    }
    
    @Override
    public Object getExternalContext() {
        return this.externalContext;
    }
    
    @Override
    public Logger getLogger(final String name) {
        return this.getLogger(name, (MessageFactory)null);
    }
    
    @Override
    public Logger getLogger(final String name, final MessageFactory messageFactory) {
        Logger logger = this.loggers.get(name);
        if (logger != null) {
            AbstractLogger.checkMessageFactory(logger, messageFactory);
            return logger;
        }
        logger = this.newInstance(this, name, messageFactory);
        final Logger prev = this.loggers.putIfAbsent(name, logger);
        return (prev == null) ? logger : prev;
    }
    
    @Override
    public boolean hasLogger(final String name) {
        return this.loggers.containsKey(name);
    }
    
    public Configuration getConfiguration() {
        return this.config;
    }
    
    public void addFilter(final Filter filter) {
        this.config.addFilter(filter);
    }
    
    public void removeFilter(final Filter filter) {
        this.config.removeFilter(filter);
    }
    
    private synchronized Configuration setConfiguration(final Configuration config) {
        if (config == null) {
            throw new NullPointerException("No Configuration was provided");
        }
        final Configuration prev = this.config;
        config.addListener(this);
        final Map<String, String> map = new HashMap<String, String>();
        map.put("hostName", NetUtils.getLocalHostname());
        map.put("contextName", this.name);
        config.addComponent("ContextProperties", map);
        config.start();
        this.config = config;
        this.updateLoggers();
        if (prev != null) {
            prev.removeListener(this);
            prev.stop();
        }
        final PropertyChangeEvent evt = new PropertyChangeEvent(this, "config", prev, config);
        for (final PropertyChangeListener listener : this.propertyChangeListeners) {
            listener.propertyChange(evt);
        }
        return prev;
    }
    
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        this.propertyChangeListeners.add(Assert.isNotNull(listener, "listener"));
    }
    
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        this.propertyChangeListeners.remove(listener);
    }
    
    public synchronized URI getConfigLocation() {
        return this.configLocation;
    }
    
    public synchronized void setConfigLocation(final URI configLocation) {
        this.configLocation = configLocation;
        this.reconfigure();
    }
    
    public synchronized void reconfigure() {
        LoggerContext.LOGGER.debug("Reconfiguration started for context " + this.name);
        final Configuration instance = ConfigurationFactory.getInstance().getConfiguration(this.name, this.configLocation);
        this.setConfiguration(instance);
        LoggerContext.LOGGER.debug("Reconfiguration completed");
    }
    
    public void updateLoggers() {
        this.updateLoggers(this.config);
    }
    
    public void updateLoggers(final Configuration config) {
        for (final Logger logger : this.loggers.values()) {
            logger.updateConfiguration(config);
        }
    }
    
    @Override
    public synchronized void onChange(final Reconfigurable reconfigurable) {
        LoggerContext.LOGGER.debug("Reconfiguration started for context " + this.name);
        final Configuration config = reconfigurable.reconfigure();
        if (config != null) {
            this.setConfiguration(config);
            LoggerContext.LOGGER.debug("Reconfiguration completed");
        }
        else {
            LoggerContext.LOGGER.debug("Reconfiguration failed");
        }
    }
    
    protected Logger newInstance(final LoggerContext ctx, final String name, final MessageFactory messageFactory) {
        return new Logger(ctx, name, messageFactory);
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
    
    public enum Status
    {
        INITIALIZED, 
        STARTING, 
        STARTED, 
        STOPPING, 
        STOPPED;
    }
    
    private class ShutdownThread extends Thread
    {
        private final LoggerContext context;
        
        public ShutdownThread(final LoggerContext context) {
            this.context = context;
        }
        
        @Override
        public void run() {
            this.context.shutdownThread = null;
            this.context.stop();
        }
    }
}
