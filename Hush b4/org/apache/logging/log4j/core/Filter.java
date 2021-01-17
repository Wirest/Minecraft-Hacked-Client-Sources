// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core;

import org.apache.logging.log4j.util.EnglishEnums;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;

public interface Filter
{
    Result getOnMismatch();
    
    Result getOnMatch();
    
    Result filter(final Logger p0, final Level p1, final Marker p2, final String p3, final Object... p4);
    
    Result filter(final Logger p0, final Level p1, final Marker p2, final Object p3, final Throwable p4);
    
    Result filter(final Logger p0, final Level p1, final Marker p2, final Message p3, final Throwable p4);
    
    Result filter(final LogEvent p0);
    
    public enum Result
    {
        ACCEPT, 
        NEUTRAL, 
        DENY;
        
        public static Result toResult(final String name) {
            return toResult(name, null);
        }
        
        public static Result toResult(final String name, final Result defaultResult) {
            return EnglishEnums.valueOf(Result.class, name, defaultResult);
        }
    }
}
