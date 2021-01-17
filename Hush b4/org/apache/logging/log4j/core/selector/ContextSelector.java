// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.selector;

import java.util.List;
import java.net.URI;
import org.apache.logging.log4j.core.LoggerContext;

public interface ContextSelector
{
    LoggerContext getContext(final String p0, final ClassLoader p1, final boolean p2);
    
    LoggerContext getContext(final String p0, final ClassLoader p1, final boolean p2, final URI p3);
    
    List<LoggerContext> getLoggerContexts();
    
    void removeContext(final LoggerContext p0);
}
