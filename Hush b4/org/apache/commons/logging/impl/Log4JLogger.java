// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.logging.impl;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Priority;
import org.apache.log4j.Logger;
import java.io.Serializable;
import org.apache.commons.logging.Log;

public class Log4JLogger implements Log, Serializable
{
    private static final long serialVersionUID = 5160705895411730424L;
    private static final String FQCN;
    private transient volatile Logger logger;
    private final String name;
    private static final Priority traceLevel;
    
    public Log4JLogger() {
        this.logger = null;
        this.name = null;
    }
    
    public Log4JLogger(final String name) {
        this.logger = null;
        this.name = name;
        this.logger = this.getLogger();
    }
    
    public Log4JLogger(final Logger logger) {
        this.logger = null;
        if (logger == null) {
            throw new IllegalArgumentException("Warning - null logger in constructor; possible log4j misconfiguration.");
        }
        this.name = ((Category)logger).getName();
        this.logger = logger;
    }
    
    public void trace(final Object message) {
        ((Category)this.getLogger()).log(Log4JLogger.FQCN, Log4JLogger.traceLevel, message, (Throwable)null);
    }
    
    public void trace(final Object message, final Throwable t) {
        ((Category)this.getLogger()).log(Log4JLogger.FQCN, Log4JLogger.traceLevel, message, t);
    }
    
    public void debug(final Object message) {
        ((Category)this.getLogger()).log(Log4JLogger.FQCN, (Priority)Level.DEBUG, message, (Throwable)null);
    }
    
    public void debug(final Object message, final Throwable t) {
        ((Category)this.getLogger()).log(Log4JLogger.FQCN, (Priority)Level.DEBUG, message, t);
    }
    
    public void info(final Object message) {
        ((Category)this.getLogger()).log(Log4JLogger.FQCN, (Priority)Level.INFO, message, (Throwable)null);
    }
    
    public void info(final Object message, final Throwable t) {
        ((Category)this.getLogger()).log(Log4JLogger.FQCN, (Priority)Level.INFO, message, t);
    }
    
    public void warn(final Object message) {
        ((Category)this.getLogger()).log(Log4JLogger.FQCN, (Priority)Level.WARN, message, (Throwable)null);
    }
    
    public void warn(final Object message, final Throwable t) {
        ((Category)this.getLogger()).log(Log4JLogger.FQCN, (Priority)Level.WARN, message, t);
    }
    
    public void error(final Object message) {
        ((Category)this.getLogger()).log(Log4JLogger.FQCN, (Priority)Level.ERROR, message, (Throwable)null);
    }
    
    public void error(final Object message, final Throwable t) {
        ((Category)this.getLogger()).log(Log4JLogger.FQCN, (Priority)Level.ERROR, message, t);
    }
    
    public void fatal(final Object message) {
        ((Category)this.getLogger()).log(Log4JLogger.FQCN, (Priority)Level.FATAL, message, (Throwable)null);
    }
    
    public void fatal(final Object message, final Throwable t) {
        ((Category)this.getLogger()).log(Log4JLogger.FQCN, (Priority)Level.FATAL, message, t);
    }
    
    public Logger getLogger() {
        Logger result = this.logger;
        if (result == null) {
            synchronized (this) {
                result = this.logger;
                if (result == null) {
                    result = (this.logger = Logger.getLogger(this.name));
                }
            }
        }
        return result;
    }
    
    public boolean isDebugEnabled() {
        return ((Category)this.getLogger()).isDebugEnabled();
    }
    
    public boolean isErrorEnabled() {
        return ((Category)this.getLogger()).isEnabledFor((Priority)Level.ERROR);
    }
    
    public boolean isFatalEnabled() {
        return ((Category)this.getLogger()).isEnabledFor((Priority)Level.FATAL);
    }
    
    public boolean isInfoEnabled() {
        return ((Category)this.getLogger()).isInfoEnabled();
    }
    
    public boolean isTraceEnabled() {
        return ((Category)this.getLogger()).isEnabledFor(Log4JLogger.traceLevel);
    }
    
    public boolean isWarnEnabled() {
        return ((Category)this.getLogger()).isEnabledFor((Priority)Level.WARN);
    }
    
    static {
        FQCN = Log4JLogger.class.getName();
        if (!Priority.class.isAssignableFrom(Level.class)) {
            throw new InstantiationError("Log4J 1.2 not available");
        }
        Priority _traceLevel;
        try {
            _traceLevel = (Priority)Level.class.getDeclaredField("TRACE").get(null);
        }
        catch (Exception ex) {
            _traceLevel = (Priority)Level.DEBUG;
        }
        traceLevel = _traceLevel;
    }
}
