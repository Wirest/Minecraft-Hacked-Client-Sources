// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import java.net.URI;

public interface LoggerContextFactory
{
    LoggerContext getContext(final String p0, final ClassLoader p1, final boolean p2);
    
    LoggerContext getContext(final String p0, final ClassLoader p1, final boolean p2, final URI p3);
    
    void removeContext(final LoggerContext p0);
}
