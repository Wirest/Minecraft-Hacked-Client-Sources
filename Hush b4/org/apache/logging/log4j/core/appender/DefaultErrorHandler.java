// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.ErrorHandler;

public class DefaultErrorHandler implements ErrorHandler
{
    private static final Logger LOGGER;
    private static final int MAX_EXCEPTIONS = 3;
    private static final int EXCEPTION_INTERVAL = 300000;
    private int exceptionCount;
    private long lastException;
    private final Appender appender;
    
    public DefaultErrorHandler(final Appender appender) {
        this.exceptionCount = 0;
        this.appender = appender;
    }
    
    @Override
    public void error(final String msg) {
        final long current = System.currentTimeMillis();
        if (this.lastException + 300000L < current || this.exceptionCount++ < 3) {
            DefaultErrorHandler.LOGGER.error(msg);
        }
        this.lastException = current;
    }
    
    @Override
    public void error(final String msg, final Throwable t) {
        final long current = System.currentTimeMillis();
        if (this.lastException + 300000L < current || this.exceptionCount++ < 3) {
            DefaultErrorHandler.LOGGER.error(msg, t);
        }
        this.lastException = current;
        if (!this.appender.ignoreExceptions() && t != null && !(t instanceof AppenderLoggingException)) {
            throw new AppenderLoggingException(msg, t);
        }
    }
    
    @Override
    public void error(final String msg, final LogEvent event, final Throwable t) {
        final long current = System.currentTimeMillis();
        if (this.lastException + 300000L < current || this.exceptionCount++ < 3) {
            DefaultErrorHandler.LOGGER.error(msg, t);
        }
        this.lastException = current;
        if (!this.appender.ignoreExceptions() && t != null && !(t instanceof AppenderLoggingException)) {
            throw new AppenderLoggingException(msg, t);
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
