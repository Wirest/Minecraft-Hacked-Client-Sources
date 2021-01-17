// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.impl;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Collections;
import java.util.HashMap;
import org.apache.logging.log4j.message.LoggerNameAwareMessage;
import org.apache.logging.log4j.message.TimestampMessage;
import org.apache.logging.log4j.core.config.Property;
import java.util.List;
import org.apache.logging.log4j.ThreadContext;
import java.util.Map;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;

public class Log4jLogEvent implements LogEvent
{
    private static final long serialVersionUID = -1351367343806656055L;
    private static final String NOT_AVAIL = "?";
    private final String fqcnOfLogger;
    private final Marker marker;
    private final Level level;
    private final String name;
    private final Message message;
    private final long timestamp;
    private final ThrowableProxy throwable;
    private final Map<String, String> mdc;
    private final ThreadContext.ContextStack ndc;
    private String threadName;
    private StackTraceElement location;
    private boolean includeLocation;
    private boolean endOfBatch;
    
    public Log4jLogEvent(final long timestamp) {
        this("", null, "", null, null, (ThrowableProxy)null, null, null, null, null, timestamp);
    }
    
    public Log4jLogEvent(final String loggerName, final Marker marker, final String fqcn, final Level level, final Message message, final Throwable t) {
        this(loggerName, marker, fqcn, level, message, null, t);
    }
    
    public Log4jLogEvent(final String loggerName, final Marker marker, final String fqcn, final Level level, final Message message, final List<Property> properties, final Throwable t) {
        this(loggerName, marker, fqcn, level, message, t, createMap(properties), (ThreadContext.getDepth() == 0) ? null : ThreadContext.cloneStack(), null, null, System.currentTimeMillis());
    }
    
    public Log4jLogEvent(final String loggerName, final Marker marker, final String fqcn, final Level level, final Message message, final Throwable t, final Map<String, String> mdc, final ThreadContext.ContextStack ndc, final String threadName, final StackTraceElement location, final long timestamp) {
        this(loggerName, marker, fqcn, level, message, (t == null) ? null : new ThrowableProxy(t), mdc, ndc, threadName, location, timestamp);
    }
    
    public static Log4jLogEvent createEvent(final String loggerName, final Marker marker, final String fqcn, final Level level, final Message message, final ThrowableProxy t, final Map<String, String> mdc, final ThreadContext.ContextStack ndc, final String threadName, final StackTraceElement location, final long timestamp) {
        return new Log4jLogEvent(loggerName, marker, fqcn, level, message, t, mdc, ndc, threadName, location, timestamp);
    }
    
    private Log4jLogEvent(final String loggerName, final Marker marker, final String fqcn, final Level level, final Message message, final ThrowableProxy t, final Map<String, String> mdc, final ThreadContext.ContextStack ndc, final String threadName, final StackTraceElement location, final long timestamp) {
        this.threadName = null;
        this.endOfBatch = false;
        this.name = loggerName;
        this.marker = marker;
        this.fqcnOfLogger = fqcn;
        this.level = level;
        this.message = message;
        this.throwable = t;
        this.mdc = mdc;
        this.ndc = ndc;
        this.timestamp = ((message instanceof TimestampMessage) ? ((TimestampMessage)message).getTimestamp() : timestamp);
        this.threadName = threadName;
        this.location = location;
        if (message != null && message instanceof LoggerNameAwareMessage) {
            ((LoggerNameAwareMessage)message).setLoggerName(this.name);
        }
    }
    
    private static Map<String, String> createMap(final List<Property> properties) {
        final Map<String, String> contextMap = ThreadContext.getImmutableContext();
        if (contextMap == null && (properties == null || properties.size() == 0)) {
            return null;
        }
        if (properties == null || properties.size() == 0) {
            return contextMap;
        }
        final Map<String, String> map = new HashMap<String, String>(contextMap);
        for (final Property prop : properties) {
            if (!map.containsKey(prop.getName())) {
                map.put(prop.getName(), prop.getValue());
            }
        }
        return Collections.unmodifiableMap((Map<? extends String, ? extends String>)map);
    }
    
    @Override
    public Level getLevel() {
        return this.level;
    }
    
    @Override
    public String getLoggerName() {
        return this.name;
    }
    
    @Override
    public Message getMessage() {
        return this.message;
    }
    
    @Override
    public String getThreadName() {
        if (this.threadName == null) {
            this.threadName = Thread.currentThread().getName();
        }
        return this.threadName;
    }
    
    @Override
    public long getMillis() {
        return this.timestamp;
    }
    
    @Override
    public Throwable getThrown() {
        return (this.throwable == null) ? null : this.throwable.getThrowable();
    }
    
    public ThrowableProxy getThrownProxy() {
        return this.throwable;
    }
    
    @Override
    public Marker getMarker() {
        return this.marker;
    }
    
    @Override
    public String getFQCN() {
        return this.fqcnOfLogger;
    }
    
    @Override
    public Map<String, String> getContextMap() {
        return (this.mdc == null) ? ThreadContext.EMPTY_MAP : this.mdc;
    }
    
    @Override
    public ThreadContext.ContextStack getContextStack() {
        return (this.ndc == null) ? ThreadContext.EMPTY_STACK : this.ndc;
    }
    
    @Override
    public StackTraceElement getSource() {
        if (this.location != null) {
            return this.location;
        }
        if (this.fqcnOfLogger == null || !this.includeLocation) {
            return null;
        }
        return this.location = calcLocation(this.fqcnOfLogger);
    }
    
    public static StackTraceElement calcLocation(final String fqcnOfLogger) {
        if (fqcnOfLogger == null) {
            return null;
        }
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        boolean next = false;
        for (final StackTraceElement element : stackTrace) {
            final String className = element.getClassName();
            if (next) {
                if (!fqcnOfLogger.equals(className)) {
                    return element;
                }
            }
            else if (fqcnOfLogger.equals(className)) {
                next = true;
            }
            else if ("?".equals(className)) {
                break;
            }
        }
        return null;
    }
    
    @Override
    public boolean isIncludeLocation() {
        return this.includeLocation;
    }
    
    @Override
    public void setIncludeLocation(final boolean includeLocation) {
        this.includeLocation = includeLocation;
    }
    
    @Override
    public boolean isEndOfBatch() {
        return this.endOfBatch;
    }
    
    @Override
    public void setEndOfBatch(final boolean endOfBatch) {
        this.endOfBatch = endOfBatch;
    }
    
    protected Object writeReplace() {
        return new LogEventProxy(this, this.includeLocation);
    }
    
    public static Serializable serialize(final Log4jLogEvent event, final boolean includeLocation) {
        return new LogEventProxy(event, includeLocation);
    }
    
    public static Log4jLogEvent deserialize(final Serializable event) {
        if (event == null) {
            throw new NullPointerException("Event cannot be null");
        }
        if (event instanceof LogEventProxy) {
            final LogEventProxy proxy = (LogEventProxy)event;
            final Log4jLogEvent result = new Log4jLogEvent(proxy.name, proxy.marker, proxy.fqcnOfLogger, proxy.level, proxy.message, proxy.throwable, proxy.mdc, proxy.ndc, proxy.threadName, proxy.location, proxy.timestamp);
            result.setEndOfBatch(proxy.isEndOfBatch);
            result.setIncludeLocation(proxy.isLocationRequired);
            return result;
        }
        throw new IllegalArgumentException("Event is not a serialized LogEvent: " + event.toString());
    }
    
    private void readObject(final ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final String n = this.name.isEmpty() ? "root" : this.name;
        sb.append("Logger=").append(n);
        sb.append(" Level=").append(this.level.name());
        sb.append(" Message=").append(this.message.getFormattedMessage());
        return sb.toString();
    }
    
    private static class LogEventProxy implements Serializable
    {
        private static final long serialVersionUID = -7139032940312647146L;
        private final String fqcnOfLogger;
        private final Marker marker;
        private final Level level;
        private final String name;
        private final Message message;
        private final long timestamp;
        private final ThrowableProxy throwable;
        private final Map<String, String> mdc;
        private final ThreadContext.ContextStack ndc;
        private final String threadName;
        private final StackTraceElement location;
        private final boolean isLocationRequired;
        private final boolean isEndOfBatch;
        
        public LogEventProxy(final Log4jLogEvent event, final boolean includeLocation) {
            this.fqcnOfLogger = event.fqcnOfLogger;
            this.marker = event.marker;
            this.level = event.level;
            this.name = event.name;
            this.message = event.message;
            this.timestamp = event.timestamp;
            this.throwable = event.throwable;
            this.mdc = event.mdc;
            this.ndc = event.ndc;
            this.location = (includeLocation ? event.getSource() : null);
            this.threadName = event.getThreadName();
            this.isLocationRequired = includeLocation;
            this.isEndOfBatch = event.endOfBatch;
        }
        
        protected Object readResolve() {
            final Log4jLogEvent result = new Log4jLogEvent(this.name, this.marker, this.fqcnOfLogger, this.level, this.message, this.throwable, this.mdc, this.ndc, this.threadName, this.location, this.timestamp, null);
            result.setEndOfBatch(this.isEndOfBatch);
            result.setIncludeLocation(this.isLocationRequired);
            return result;
        }
    }
}
