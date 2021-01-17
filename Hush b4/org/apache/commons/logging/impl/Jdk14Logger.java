// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.logging.impl;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.Serializable;
import org.apache.commons.logging.Log;

public class Jdk14Logger implements Log, Serializable
{
    private static final long serialVersionUID = 4784713551416303804L;
    protected static final Level dummyLevel;
    protected transient Logger logger;
    protected String name;
    
    public Jdk14Logger(final String name) {
        this.logger = null;
        this.name = null;
        this.name = name;
        this.logger = this.getLogger();
    }
    
    protected void log(final Level level, final String msg, final Throwable ex) {
        final Logger logger = this.getLogger();
        if (logger.isLoggable(level)) {
            final Throwable dummyException = new Throwable();
            final StackTraceElement[] locations = dummyException.getStackTrace();
            final String cname = this.name;
            String method = "unknown";
            if (locations != null && locations.length > 2) {
                final StackTraceElement caller = locations[2];
                method = caller.getMethodName();
            }
            if (ex == null) {
                logger.logp(level, cname, method, msg);
            }
            else {
                logger.logp(level, cname, method, msg, ex);
            }
        }
    }
    
    public void debug(final Object message) {
        this.log(Level.FINE, String.valueOf(message), null);
    }
    
    public void debug(final Object message, final Throwable exception) {
        this.log(Level.FINE, String.valueOf(message), exception);
    }
    
    public void error(final Object message) {
        this.log(Level.SEVERE, String.valueOf(message), null);
    }
    
    public void error(final Object message, final Throwable exception) {
        this.log(Level.SEVERE, String.valueOf(message), exception);
    }
    
    public void fatal(final Object message) {
        this.log(Level.SEVERE, String.valueOf(message), null);
    }
    
    public void fatal(final Object message, final Throwable exception) {
        this.log(Level.SEVERE, String.valueOf(message), exception);
    }
    
    public Logger getLogger() {
        if (this.logger == null) {
            this.logger = Logger.getLogger(this.name);
        }
        return this.logger;
    }
    
    public void info(final Object message) {
        this.log(Level.INFO, String.valueOf(message), null);
    }
    
    public void info(final Object message, final Throwable exception) {
        this.log(Level.INFO, String.valueOf(message), exception);
    }
    
    public boolean isDebugEnabled() {
        return this.getLogger().isLoggable(Level.FINE);
    }
    
    public boolean isErrorEnabled() {
        return this.getLogger().isLoggable(Level.SEVERE);
    }
    
    public boolean isFatalEnabled() {
        return this.getLogger().isLoggable(Level.SEVERE);
    }
    
    public boolean isInfoEnabled() {
        return this.getLogger().isLoggable(Level.INFO);
    }
    
    public boolean isTraceEnabled() {
        return this.getLogger().isLoggable(Level.FINEST);
    }
    
    public boolean isWarnEnabled() {
        return this.getLogger().isLoggable(Level.WARNING);
    }
    
    public void trace(final Object message) {
        this.log(Level.FINEST, String.valueOf(message), null);
    }
    
    public void trace(final Object message, final Throwable exception) {
        this.log(Level.FINEST, String.valueOf(message), exception);
    }
    
    public void warn(final Object message) {
        this.log(Level.WARNING, String.valueOf(message), null);
    }
    
    public void warn(final Object message, final Throwable exception) {
        this.log(Level.WARNING, String.valueOf(message), exception);
    }
    
    static {
        dummyLevel = Level.FINE;
    }
}
