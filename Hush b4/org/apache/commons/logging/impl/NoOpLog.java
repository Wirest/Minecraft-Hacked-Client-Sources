// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.logging.impl;

import java.io.Serializable;
import org.apache.commons.logging.Log;

public class NoOpLog implements Log, Serializable
{
    private static final long serialVersionUID = 561423906191706148L;
    
    public NoOpLog() {
    }
    
    public NoOpLog(final String name) {
    }
    
    public void trace(final Object message) {
    }
    
    public void trace(final Object message, final Throwable t) {
    }
    
    public void debug(final Object message) {
    }
    
    public void debug(final Object message, final Throwable t) {
    }
    
    public void info(final Object message) {
    }
    
    public void info(final Object message, final Throwable t) {
    }
    
    public void warn(final Object message) {
    }
    
    public void warn(final Object message, final Throwable t) {
    }
    
    public void error(final Object message) {
    }
    
    public void error(final Object message, final Throwable t) {
    }
    
    public void fatal(final Object message) {
    }
    
    public void fatal(final Object message, final Throwable t) {
    }
    
    public final boolean isDebugEnabled() {
        return false;
    }
    
    public final boolean isErrorEnabled() {
        return false;
    }
    
    public final boolean isFatalEnabled() {
        return false;
    }
    
    public final boolean isInfoEnabled() {
        return false;
    }
    
    public final boolean isTraceEnabled() {
        return false;
    }
    
    public final boolean isWarnEnabled() {
        return false;
    }
}
