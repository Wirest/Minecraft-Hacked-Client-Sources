// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.logging.impl;

import org.apache.log.Hierarchy;
import org.apache.log.Logger;
import java.io.Serializable;
import org.apache.commons.logging.Log;

public class LogKitLogger implements Log, Serializable
{
    private static final long serialVersionUID = 3768538055836059519L;
    protected transient volatile Logger logger;
    protected String name;
    
    public LogKitLogger(final String name) {
        this.logger = null;
        this.name = null;
        this.name = name;
        this.logger = this.getLogger();
    }
    
    public Logger getLogger() {
        Logger result = this.logger;
        if (result == null) {
            synchronized (this) {
                result = this.logger;
                if (result == null) {
                    result = (this.logger = Hierarchy.getDefaultHierarchy().getLoggerFor(this.name));
                }
            }
        }
        return result;
    }
    
    public void trace(final Object message) {
        this.debug(message);
    }
    
    public void trace(final Object message, final Throwable t) {
        this.debug(message, t);
    }
    
    public void debug(final Object message) {
        if (message != null) {
            this.getLogger().debug(String.valueOf(message));
        }
    }
    
    public void debug(final Object message, final Throwable t) {
        if (message != null) {
            this.getLogger().debug(String.valueOf(message), t);
        }
    }
    
    public void info(final Object message) {
        if (message != null) {
            this.getLogger().info(String.valueOf(message));
        }
    }
    
    public void info(final Object message, final Throwable t) {
        if (message != null) {
            this.getLogger().info(String.valueOf(message), t);
        }
    }
    
    public void warn(final Object message) {
        if (message != null) {
            this.getLogger().warn(String.valueOf(message));
        }
    }
    
    public void warn(final Object message, final Throwable t) {
        if (message != null) {
            this.getLogger().warn(String.valueOf(message), t);
        }
    }
    
    public void error(final Object message) {
        if (message != null) {
            this.getLogger().error(String.valueOf(message));
        }
    }
    
    public void error(final Object message, final Throwable t) {
        if (message != null) {
            this.getLogger().error(String.valueOf(message), t);
        }
    }
    
    public void fatal(final Object message) {
        if (message != null) {
            this.getLogger().fatalError(String.valueOf(message));
        }
    }
    
    public void fatal(final Object message, final Throwable t) {
        if (message != null) {
            this.getLogger().fatalError(String.valueOf(message), t);
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
}
