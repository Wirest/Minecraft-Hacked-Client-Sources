// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.logging;

public interface Log
{
    boolean isDebugEnabled();
    
    boolean isErrorEnabled();
    
    boolean isFatalEnabled();
    
    boolean isInfoEnabled();
    
    boolean isTraceEnabled();
    
    boolean isWarnEnabled();
    
    void trace(final Object p0);
    
    void trace(final Object p0, final Throwable p1);
    
    void debug(final Object p0);
    
    void debug(final Object p0, final Throwable p1);
    
    void info(final Object p0);
    
    void info(final Object p0, final Throwable p1);
    
    void warn(final Object p0);
    
    void warn(final Object p0, final Throwable p1);
    
    void error(final Object p0);
    
    void error(final Object p0, final Throwable p1);
    
    void fatal(final Object p0);
    
    void fatal(final Object p0, final Throwable p1);
}
