// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.EventFactory;
import java.util.Iterator;
import java.util.HashMap;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.ThreadContext;
import java.util.Map;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;

public class RingBufferLogEvent implements LogEvent
{
    private static final long serialVersionUID = 8462119088943934758L;
    public static final Factory FACTORY;
    private AsyncLogger asyncLogger;
    private String loggerName;
    private Marker marker;
    private String fqcn;
    private Level level;
    private Message message;
    private Throwable thrown;
    private Map<String, String> contextMap;
    private ThreadContext.ContextStack contextStack;
    private String threadName;
    private StackTraceElement location;
    private long currentTimeMillis;
    private boolean endOfBatch;
    private boolean includeLocation;
    
    public void setValues(final AsyncLogger asyncLogger, final String loggerName, final Marker marker, final String fqcn, final Level level, final Message data, final Throwable t, final Map<String, String> map, final ThreadContext.ContextStack contextStack, final String threadName, final StackTraceElement location, final long currentTimeMillis) {
        this.asyncLogger = asyncLogger;
        this.loggerName = loggerName;
        this.marker = marker;
        this.fqcn = fqcn;
        this.level = level;
        this.message = data;
        this.thrown = t;
        this.contextMap = map;
        this.contextStack = contextStack;
        this.threadName = threadName;
        this.location = location;
        this.currentTimeMillis = currentTimeMillis;
    }
    
    public void execute(final boolean endOfBatch) {
        this.endOfBatch = endOfBatch;
        this.asyncLogger.actualAsyncLog(this);
    }
    
    @Override
    public boolean isEndOfBatch() {
        return this.endOfBatch;
    }
    
    @Override
    public void setEndOfBatch(final boolean endOfBatch) {
        this.endOfBatch = endOfBatch;
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
    public String getLoggerName() {
        return this.loggerName;
    }
    
    @Override
    public Marker getMarker() {
        return this.marker;
    }
    
    @Override
    public String getFQCN() {
        return this.fqcn;
    }
    
    @Override
    public Level getLevel() {
        return this.level;
    }
    
    @Override
    public Message getMessage() {
        if (this.message == null) {
            this.message = new SimpleMessage("");
        }
        return this.message;
    }
    
    @Override
    public Throwable getThrown() {
        return this.thrown;
    }
    
    @Override
    public Map<String, String> getContextMap() {
        return this.contextMap;
    }
    
    @Override
    public ThreadContext.ContextStack getContextStack() {
        return this.contextStack;
    }
    
    @Override
    public String getThreadName() {
        return this.threadName;
    }
    
    @Override
    public StackTraceElement getSource() {
        return this.location;
    }
    
    @Override
    public long getMillis() {
        return this.currentTimeMillis;
    }
    
    public void mergePropertiesIntoContextMap(final Map<Property, Boolean> properties, final StrSubstitutor strSubstitutor) {
        if (properties == null) {
            return;
        }
        final Map<String, String> map = (this.contextMap == null) ? new HashMap<String, String>() : new HashMap<String, String>(this.contextMap);
        for (final Map.Entry<Property, Boolean> entry : properties.entrySet()) {
            final Property prop = entry.getKey();
            if (map.containsKey(prop.getName())) {
                continue;
            }
            final String value = entry.getValue() ? strSubstitutor.replace(prop.getValue()) : prop.getValue();
            map.put(prop.getName(), value);
        }
        this.contextMap = map;
    }
    
    public void clear() {
        this.setValues(null, null, null, null, null, null, null, null, null, null, null, 0L);
    }
    
    static {
        FACTORY = new Factory();
    }
    
    private static class Factory implements EventFactory<RingBufferLogEvent>
    {
        public RingBufferLogEvent newInstance() {
            return new RingBufferLogEvent();
        }
    }
}
