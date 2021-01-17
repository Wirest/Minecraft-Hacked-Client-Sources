// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.simple;

import java.net.URI;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerContextFactory;

public class SimpleLoggerContextFactory implements LoggerContextFactory
{
    private static LoggerContext context;
    
    @Override
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final boolean currentContext) {
        return SimpleLoggerContextFactory.context;
    }
    
    @Override
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final boolean currentContext, final URI configLocation) {
        return SimpleLoggerContextFactory.context;
    }
    
    @Override
    public void removeContext(final LoggerContext context) {
    }
    
    static {
        SimpleLoggerContextFactory.context = new SimpleLoggerContext();
    }
}
