// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j;

import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.StructuredDataMessage;
import org.apache.logging.log4j.spi.AbstractLoggerWrapper;

public final class EventLogger
{
    private static final String NAME = "EventLogger";
    public static final Marker EVENT_MARKER;
    private static final String FQCN;
    private static AbstractLoggerWrapper loggerWrapper;
    
    private EventLogger() {
    }
    
    public static void logEvent(final StructuredDataMessage msg) {
        EventLogger.loggerWrapper.log(EventLogger.EVENT_MARKER, EventLogger.FQCN, Level.OFF, msg, null);
    }
    
    public static void logEvent(final StructuredDataMessage msg, final Level level) {
        EventLogger.loggerWrapper.log(EventLogger.EVENT_MARKER, EventLogger.FQCN, level, msg, null);
    }
    
    static {
        EVENT_MARKER = MarkerManager.getMarker("EVENT");
        FQCN = EventLogger.class.getName();
        final Logger eventLogger = LogManager.getLogger("EventLogger");
        if (!(eventLogger instanceof AbstractLogger)) {
            throw new LoggingException("Logger returned must be based on AbstractLogger");
        }
        EventLogger.loggerWrapper = new AbstractLoggerWrapper((AbstractLogger)eventLogger, "EventLogger", null);
    }
}
