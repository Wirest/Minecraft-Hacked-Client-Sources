// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.logging.impl;

import org.apache.avalon.framework.logger.Logger;
import org.apache.commons.logging.Log;

public class AvalonLogger implements Log
{
    private static volatile Logger defaultLogger;
    private final transient Logger logger;
    
    public AvalonLogger(final Logger logger) {
        this.logger = logger;
    }
    
    public AvalonLogger(final String name) {
        if (AvalonLogger.defaultLogger == null) {
            throw new NullPointerException("default logger has to be specified if this constructor is used!");
        }
        this.logger = AvalonLogger.defaultLogger.getChildLogger(name);
    }
    
    public Logger getLogger() {
        return this.logger;
    }
    
    public static void setDefaultLogger(final Logger logger) {
        AvalonLogger.defaultLogger = logger;
    }
    
    public void debug(final Object message, final Throwable t) {
        if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug(String.valueOf(message), t);
        }
    }
    
    public void debug(final Object message) {
        if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug(String.valueOf(message));
        }
    }
    
    public void error(final Object message, final Throwable t) {
        if (this.getLogger().isErrorEnabled()) {
            this.getLogger().error(String.valueOf(message), t);
        }
    }
    
    public void error(final Object message) {
        if (this.getLogger().isErrorEnabled()) {
            this.getLogger().error(String.valueOf(message));
        }
    }
    
    public void fatal(final Object message, final Throwable t) {
        if (this.getLogger().isFatalErrorEnabled()) {
            this.getLogger().fatalError(String.valueOf(message), t);
        }
    }
    
    public void fatal(final Object message) {
        if (this.getLogger().isFatalErrorEnabled()) {
            this.getLogger().fatalError(String.valueOf(message));
        }
    }
    
    public void info(final Object message, final Throwable t) {
        if (this.getLogger().isInfoEnabled()) {
            this.getLogger().info(String.valueOf(message), t);
        }
    }
    
    public void info(final Object message) {
        if (this.getLogger().isInfoEnabled()) {
            this.getLogger().info(String.valueOf(message));
        }
    }
    
    public boolean isDebugEnabled() {
        return this.getLogger().isDebugEnabled();
    }
    
    public boolean isErrorEnabled() {
        return this.getLogger().isErrorEnabled();
    }
    
    public boolean isFatalEnabled() {
        return this.getLogger().isFatalErrorEnabled();
    }
    
    public boolean isInfoEnabled() {
        return this.getLogger().isInfoEnabled();
    }
    
    public boolean isTraceEnabled() {
        return this.getLogger().isDebugEnabled();
    }
    
    public boolean isWarnEnabled() {
        return this.getLogger().isWarnEnabled();
    }
    
    public void trace(final Object message, final Throwable t) {
        if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug(String.valueOf(message), t);
        }
    }
    
    public void trace(final Object message) {
        if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug(String.valueOf(message));
        }
    }
    
    public void warn(final Object message, final Throwable t) {
        if (this.getLogger().isWarnEnabled()) {
            this.getLogger().warn(String.valueOf(message), t);
        }
    }
    
    public void warn(final Object message) {
        if (this.getLogger().isWarnEnabled()) {
            this.getLogger().warn(String.valueOf(message));
        }
    }
    
    static {
        AvalonLogger.defaultLogger = null;
    }
}
