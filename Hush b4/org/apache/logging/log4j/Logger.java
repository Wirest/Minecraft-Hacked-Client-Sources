// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j;

import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.Message;

public interface Logger
{
    void catching(final Level p0, final Throwable p1);
    
    void catching(final Throwable p0);
    
    void debug(final Marker p0, final Message p1);
    
    void debug(final Marker p0, final Message p1, final Throwable p2);
    
    void debug(final Marker p0, final Object p1);
    
    void debug(final Marker p0, final Object p1, final Throwable p2);
    
    void debug(final Marker p0, final String p1);
    
    void debug(final Marker p0, final String p1, final Object... p2);
    
    void debug(final Marker p0, final String p1, final Throwable p2);
    
    void debug(final Message p0);
    
    void debug(final Message p0, final Throwable p1);
    
    void debug(final Object p0);
    
    void debug(final Object p0, final Throwable p1);
    
    void debug(final String p0);
    
    void debug(final String p0, final Object... p1);
    
    void debug(final String p0, final Throwable p1);
    
    void entry();
    
    void entry(final Object... p0);
    
    void error(final Marker p0, final Message p1);
    
    void error(final Marker p0, final Message p1, final Throwable p2);
    
    void error(final Marker p0, final Object p1);
    
    void error(final Marker p0, final Object p1, final Throwable p2);
    
    void error(final Marker p0, final String p1);
    
    void error(final Marker p0, final String p1, final Object... p2);
    
    void error(final Marker p0, final String p1, final Throwable p2);
    
    void error(final Message p0);
    
    void error(final Message p0, final Throwable p1);
    
    void error(final Object p0);
    
    void error(final Object p0, final Throwable p1);
    
    void error(final String p0);
    
    void error(final String p0, final Object... p1);
    
    void error(final String p0, final Throwable p1);
    
    void exit();
    
     <R> R exit(final R p0);
    
    void fatal(final Marker p0, final Message p1);
    
    void fatal(final Marker p0, final Message p1, final Throwable p2);
    
    void fatal(final Marker p0, final Object p1);
    
    void fatal(final Marker p0, final Object p1, final Throwable p2);
    
    void fatal(final Marker p0, final String p1);
    
    void fatal(final Marker p0, final String p1, final Object... p2);
    
    void fatal(final Marker p0, final String p1, final Throwable p2);
    
    void fatal(final Message p0);
    
    void fatal(final Message p0, final Throwable p1);
    
    void fatal(final Object p0);
    
    void fatal(final Object p0, final Throwable p1);
    
    void fatal(final String p0);
    
    void fatal(final String p0, final Object... p1);
    
    void fatal(final String p0, final Throwable p1);
    
    MessageFactory getMessageFactory();
    
    String getName();
    
    void info(final Marker p0, final Message p1);
    
    void info(final Marker p0, final Message p1, final Throwable p2);
    
    void info(final Marker p0, final Object p1);
    
    void info(final Marker p0, final Object p1, final Throwable p2);
    
    void info(final Marker p0, final String p1);
    
    void info(final Marker p0, final String p1, final Object... p2);
    
    void info(final Marker p0, final String p1, final Throwable p2);
    
    void info(final Message p0);
    
    void info(final Message p0, final Throwable p1);
    
    void info(final Object p0);
    
    void info(final Object p0, final Throwable p1);
    
    void info(final String p0);
    
    void info(final String p0, final Object... p1);
    
    void info(final String p0, final Throwable p1);
    
    boolean isDebugEnabled();
    
    boolean isDebugEnabled(final Marker p0);
    
    boolean isEnabled(final Level p0);
    
    boolean isEnabled(final Level p0, final Marker p1);
    
    boolean isErrorEnabled();
    
    boolean isErrorEnabled(final Marker p0);
    
    boolean isFatalEnabled();
    
    boolean isFatalEnabled(final Marker p0);
    
    boolean isInfoEnabled();
    
    boolean isInfoEnabled(final Marker p0);
    
    boolean isTraceEnabled();
    
    boolean isTraceEnabled(final Marker p0);
    
    boolean isWarnEnabled();
    
    boolean isWarnEnabled(final Marker p0);
    
    void log(final Level p0, final Marker p1, final Message p2);
    
    void log(final Level p0, final Marker p1, final Message p2, final Throwable p3);
    
    void log(final Level p0, final Marker p1, final Object p2);
    
    void log(final Level p0, final Marker p1, final Object p2, final Throwable p3);
    
    void log(final Level p0, final Marker p1, final String p2);
    
    void log(final Level p0, final Marker p1, final String p2, final Object... p3);
    
    void log(final Level p0, final Marker p1, final String p2, final Throwable p3);
    
    void log(final Level p0, final Message p1);
    
    void log(final Level p0, final Message p1, final Throwable p2);
    
    void log(final Level p0, final Object p1);
    
    void log(final Level p0, final Object p1, final Throwable p2);
    
    void log(final Level p0, final String p1);
    
    void log(final Level p0, final String p1, final Object... p2);
    
    void log(final Level p0, final String p1, final Throwable p2);
    
    void printf(final Level p0, final Marker p1, final String p2, final Object... p3);
    
    void printf(final Level p0, final String p1, final Object... p2);
    
     <T extends Throwable> T throwing(final Level p0, final T p1);
    
     <T extends Throwable> T throwing(final T p0);
    
    void trace(final Marker p0, final Message p1);
    
    void trace(final Marker p0, final Message p1, final Throwable p2);
    
    void trace(final Marker p0, final Object p1);
    
    void trace(final Marker p0, final Object p1, final Throwable p2);
    
    void trace(final Marker p0, final String p1);
    
    void trace(final Marker p0, final String p1, final Object... p2);
    
    void trace(final Marker p0, final String p1, final Throwable p2);
    
    void trace(final Message p0);
    
    void trace(final Message p0, final Throwable p1);
    
    void trace(final Object p0);
    
    void trace(final Object p0, final Throwable p1);
    
    void trace(final String p0);
    
    void trace(final String p0, final Object... p1);
    
    void trace(final String p0, final Throwable p1);
    
    void warn(final Marker p0, final Message p1);
    
    void warn(final Marker p0, final Message p1, final Throwable p2);
    
    void warn(final Marker p0, final Object p1);
    
    void warn(final Marker p0, final Object p1, final Throwable p2);
    
    void warn(final Marker p0, final String p1);
    
    void warn(final Marker p0, final String p1, final Object... p2);
    
    void warn(final Marker p0, final String p1, final Throwable p2);
    
    void warn(final Message p0);
    
    void warn(final Message p0, final Throwable p1);
    
    void warn(final Object p0);
    
    void warn(final Object p0, final Throwable p1);
    
    void warn(final String p0);
    
    void warn(final String p0, final Object... p1);
    
    void warn(final String p0, final Throwable p1);
}
