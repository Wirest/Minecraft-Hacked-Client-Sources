// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.util;

public interface LogSystem
{
    void error(final String p0, final Throwable p1);
    
    void error(final Throwable p0);
    
    void error(final String p0);
    
    void warn(final String p0);
    
    void warn(final String p0, final Throwable p1);
    
    void info(final String p0);
    
    void debug(final String p0);
}
