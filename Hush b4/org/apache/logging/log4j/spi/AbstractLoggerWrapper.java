// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.MessageFactory;

public class AbstractLoggerWrapper extends AbstractLogger
{
    protected final AbstractLogger logger;
    
    public AbstractLoggerWrapper(final AbstractLogger logger, final String name, final MessageFactory messageFactory) {
        super(name, messageFactory);
        this.logger = logger;
    }
    
    @Override
    public void log(final Marker marker, final String fqcn, final Level level, final Message data, final Throwable t) {
        this.logger.log(marker, fqcn, level, data, t);
    }
    
    public boolean isEnabled(final Level level, final Marker marker, final String data) {
        return this.logger.isEnabled(level, marker, data);
    }
    
    public boolean isEnabled(final Level level, final Marker marker, final String data, final Throwable t) {
        return this.logger.isEnabled(level, marker, data, t);
    }
    
    public boolean isEnabled(final Level level, final Marker marker, final String data, final Object... p1) {
        return this.logger.isEnabled(level, marker, data, p1);
    }
    
    public boolean isEnabled(final Level level, final Marker marker, final Object data, final Throwable t) {
        return this.logger.isEnabled(level, marker, data, t);
    }
    
    public boolean isEnabled(final Level level, final Marker marker, final Message data, final Throwable t) {
        return this.logger.isEnabled(level, marker, data, t);
    }
}
