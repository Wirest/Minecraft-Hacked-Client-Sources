// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import java.util.Iterator;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.LogEvent;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.core.config.ConfigurationException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.Filter;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.core.config.AppenderControl;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "Async", category = "Core", elementType = "appender", printObject = true)
public final class AsyncAppender extends AbstractAppender
{
    private static final int DEFAULT_QUEUE_SIZE = 128;
    private static final String SHUTDOWN = "Shutdown";
    private final BlockingQueue<Serializable> queue;
    private final boolean blocking;
    private final Configuration config;
    private final AppenderRef[] appenderRefs;
    private final String errorRef;
    private final boolean includeLocation;
    private AppenderControl errorAppender;
    private AsyncThread thread;
    private static final AtomicLong threadSequence;
    
    private AsyncAppender(final String name, final Filter filter, final AppenderRef[] appenderRefs, final String errorRef, final int queueSize, final boolean blocking, final boolean ignoreExceptions, final Configuration config, final boolean includeLocation) {
        super(name, filter, null, ignoreExceptions);
        this.queue = new ArrayBlockingQueue<Serializable>(queueSize);
        this.blocking = blocking;
        this.config = config;
        this.appenderRefs = appenderRefs;
        this.errorRef = errorRef;
        this.includeLocation = includeLocation;
    }
    
    @Override
    public void start() {
        final Map<String, Appender> map = this.config.getAppenders();
        final List<AppenderControl> appenders = new ArrayList<AppenderControl>();
        for (final AppenderRef appenderRef : this.appenderRefs) {
            if (map.containsKey(appenderRef.getRef())) {
                appenders.add(new AppenderControl(map.get(appenderRef.getRef()), appenderRef.getLevel(), appenderRef.getFilter()));
            }
            else {
                AsyncAppender.LOGGER.error("No appender named {} was configured", appenderRef);
            }
        }
        if (this.errorRef != null) {
            if (map.containsKey(this.errorRef)) {
                this.errorAppender = new AppenderControl(map.get(this.errorRef), null, null);
            }
            else {
                AsyncAppender.LOGGER.error("Unable to set up error Appender. No appender named {} was configured", this.errorRef);
            }
        }
        if (appenders.size() > 0) {
            (this.thread = new AsyncThread(appenders, this.queue)).setName("AsyncAppender-" + this.getName());
        }
        else if (this.errorRef == null) {
            throw new ConfigurationException("No appenders are available for AsyncAppender " + this.getName());
        }
        this.thread.start();
        super.start();
    }
    
    @Override
    public void stop() {
        super.stop();
        this.thread.shutdown();
        try {
            this.thread.join();
        }
        catch (InterruptedException ex) {
            AsyncAppender.LOGGER.warn("Interrupted while stopping AsyncAppender {}", this.getName());
        }
    }
    
    @Override
    public void append(final LogEvent event) {
        if (!this.isStarted()) {
            throw new IllegalStateException("AsyncAppender " + this.getName() + " is not active");
        }
        if (event instanceof Log4jLogEvent) {
            boolean appendSuccessful = false;
            if (this.blocking) {
                try {
                    this.queue.put(Log4jLogEvent.serialize((Log4jLogEvent)event, this.includeLocation));
                    appendSuccessful = true;
                }
                catch (InterruptedException e) {
                    AsyncAppender.LOGGER.warn("Interrupted while waiting for a free slot in the AsyncAppender LogEvent-queue {}", this.getName());
                }
            }
            else {
                appendSuccessful = this.queue.offer(Log4jLogEvent.serialize((Log4jLogEvent)event, this.includeLocation));
                if (!appendSuccessful) {
                    this.error("Appender " + this.getName() + " is unable to write primary appenders. queue is full");
                }
            }
            if (!appendSuccessful && this.errorAppender != null) {
                this.errorAppender.callAppender(event);
            }
        }
    }
    
    @PluginFactory
    public static AsyncAppender createAppender(@PluginElement("AppenderRef") final AppenderRef[] appenderRefs, @PluginAttribute("errorRef") @PluginAliases({ "error-ref" }) final String errorRef, @PluginAttribute("blocking") final String blocking, @PluginAttribute("bufferSize") final String size, @PluginAttribute("name") final String name, @PluginAttribute("includeLocation") final String includeLocation, @PluginElement("Filter") final Filter filter, @PluginConfiguration final Configuration config, @PluginAttribute("ignoreExceptions") final String ignore) {
        if (name == null) {
            AsyncAppender.LOGGER.error("No name provided for AsyncAppender");
            return null;
        }
        if (appenderRefs == null) {
            AsyncAppender.LOGGER.error("No appender references provided to AsyncAppender {}", name);
        }
        final boolean isBlocking = Booleans.parseBoolean(blocking, true);
        final int queueSize = AbstractAppender.parseInt(size, 128);
        final boolean isIncludeLocation = Boolean.parseBoolean(includeLocation);
        final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        return new AsyncAppender(name, filter, appenderRefs, errorRef, queueSize, isBlocking, ignoreExceptions, config, isIncludeLocation);
    }
    
    static {
        threadSequence = new AtomicLong(1L);
    }
    
    private class AsyncThread extends Thread
    {
        private volatile boolean shutdown;
        private final List<AppenderControl> appenders;
        private final BlockingQueue<Serializable> queue;
        
        public AsyncThread(final List<AppenderControl> appenders, final BlockingQueue<Serializable> queue) {
            this.shutdown = false;
            this.appenders = appenders;
            this.queue = queue;
            this.setDaemon(true);
            this.setName("AsyncAppenderThread" + AsyncAppender.threadSequence.getAndIncrement());
        }
        
        @Override
        public void run() {
            while (!this.shutdown) {
                Serializable s;
                try {
                    s = this.queue.take();
                    if (s != null && s instanceof String && "Shutdown".equals(s.toString())) {
                        this.shutdown = true;
                        continue;
                    }
                }
                catch (InterruptedException ex) {
                    continue;
                }
                final Log4jLogEvent event = Log4jLogEvent.deserialize(s);
                event.setEndOfBatch(this.queue.isEmpty());
                boolean success = false;
                for (final AppenderControl control : this.appenders) {
                    try {
                        control.callAppender(event);
                        success = true;
                    }
                    catch (Exception ex3) {}
                }
                if (!success && AsyncAppender.this.errorAppender != null) {
                    try {
                        AsyncAppender.this.errorAppender.callAppender(event);
                    }
                    catch (Exception ex4) {}
                }
            }
            while (!this.queue.isEmpty()) {
                try {
                    final Serializable s = this.queue.take();
                    if (!(s instanceof Log4jLogEvent)) {
                        continue;
                    }
                    final Log4jLogEvent event = Log4jLogEvent.deserialize(s);
                    event.setEndOfBatch(this.queue.isEmpty());
                    for (final AppenderControl control2 : this.appenders) {
                        control2.callAppender(event);
                    }
                }
                catch (InterruptedException ex2) {}
            }
        }
        
        public void shutdown() {
            this.shutdown = true;
            if (this.queue.isEmpty()) {
                this.queue.offer("Shutdown");
            }
        }
    }
}
