// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core;

import org.apache.logging.log4j.ThreadContext;
import java.util.Map;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Level;
import java.io.Serializable;

public interface LogEvent extends Serializable
{
    Level getLevel();
    
    String getLoggerName();
    
    StackTraceElement getSource();
    
    Message getMessage();
    
    Marker getMarker();
    
    String getThreadName();
    
    long getMillis();
    
    Throwable getThrown();
    
    Map<String, String> getContextMap();
    
    ThreadContext.ContextStack getContextStack();
    
    String getFQCN();
    
    boolean isIncludeLocation();
    
    void setIncludeLocation(final boolean p0);
    
    boolean isEndOfBatch();
    
    void setEndOfBatch(final boolean p0);
}
