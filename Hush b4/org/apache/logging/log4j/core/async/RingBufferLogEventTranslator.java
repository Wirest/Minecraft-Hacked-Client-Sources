// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.ThreadContext;
import java.util.Map;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import com.lmax.disruptor.EventTranslator;

public class RingBufferLogEventTranslator implements EventTranslator<RingBufferLogEvent>
{
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
    
    public void translateTo(final RingBufferLogEvent event, final long sequence) {
        event.setValues(this.asyncLogger, this.loggerName, this.marker, this.fqcn, this.level, this.message, this.thrown, this.contextMap, this.contextStack, this.threadName, this.location, this.currentTimeMillis);
    }
    
    public void setValues(final AsyncLogger asyncLogger, final String loggerName, final Marker marker, final String fqcn, final Level level, final Message message, final Throwable thrown, final Map<String, String> contextMap, final ThreadContext.ContextStack contextStack, final String threadName, final StackTraceElement location, final long currentTimeMillis) {
        this.asyncLogger = asyncLogger;
        this.loggerName = loggerName;
        this.marker = marker;
        this.fqcn = fqcn;
        this.level = level;
        this.message = message;
        this.thrown = thrown;
        this.contextMap = contextMap;
        this.contextStack = contextStack;
        this.threadName = threadName;
        this.location = location;
        this.currentTimeMillis = currentTimeMillis;
    }
}
