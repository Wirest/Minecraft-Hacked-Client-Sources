// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal.logging;

public interface InternalLogger
{
    String name();
    
    boolean isTraceEnabled();
    
    void trace(final String p0);
    
    void trace(final String p0, final Object p1);
    
    void trace(final String p0, final Object p1, final Object p2);
    
    void trace(final String p0, final Object... p1);
    
    void trace(final String p0, final Throwable p1);
    
    boolean isDebugEnabled();
    
    void debug(final String p0);
    
    void debug(final String p0, final Object p1);
    
    void debug(final String p0, final Object p1, final Object p2);
    
    void debug(final String p0, final Object... p1);
    
    void debug(final String p0, final Throwable p1);
    
    boolean isInfoEnabled();
    
    void info(final String p0);
    
    void info(final String p0, final Object p1);
    
    void info(final String p0, final Object p1, final Object p2);
    
    void info(final String p0, final Object... p1);
    
    void info(final String p0, final Throwable p1);
    
    boolean isWarnEnabled();
    
    void warn(final String p0);
    
    void warn(final String p0, final Object p1);
    
    void warn(final String p0, final Object... p1);
    
    void warn(final String p0, final Object p1, final Object p2);
    
    void warn(final String p0, final Throwable p1);
    
    boolean isErrorEnabled();
    
    void error(final String p0);
    
    void error(final String p0, final Object p1);
    
    void error(final String p0, final Object p1, final Object p2);
    
    void error(final String p0, final Object... p1);
    
    void error(final String p0, final Throwable p1);
    
    boolean isEnabled(final InternalLogLevel p0);
    
    void log(final InternalLogLevel p0, final String p1);
    
    void log(final InternalLogLevel p0, final String p1, final Object p2);
    
    void log(final InternalLogLevel p0, final String p1, final Object p2, final Object p3);
    
    void log(final InternalLogLevel p0, final String p1, final Object... p2);
    
    void log(final InternalLogLevel p0, final String p1, final Throwable p2);
}
