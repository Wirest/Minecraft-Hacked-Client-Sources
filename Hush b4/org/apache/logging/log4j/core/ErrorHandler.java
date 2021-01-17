// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core;

public interface ErrorHandler
{
    void error(final String p0);
    
    void error(final String p0, final Throwable p1);
    
    void error(final String p0, final LogEvent p1, final Throwable p2);
}
