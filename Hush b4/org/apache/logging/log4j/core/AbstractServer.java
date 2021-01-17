// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core;

import org.apache.logging.log4j.LogManager;

public class AbstractServer
{
    private final LoggerContext context;
    
    protected AbstractServer() {
        this.context = (LoggerContext)LogManager.getContext(false);
    }
    
    protected void log(final LogEvent event) {
        final Logger logger = this.context.getLogger(event.getLoggerName());
        if (logger.config.filter(event.getLevel(), event.getMarker(), event.getMessage(), event.getThrown())) {
            logger.config.logEvent(event);
        }
    }
}
