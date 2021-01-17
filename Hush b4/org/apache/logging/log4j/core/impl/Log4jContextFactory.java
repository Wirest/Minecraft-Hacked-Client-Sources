// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.impl;

import java.net.URI;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.jmx.Server;
import org.apache.logging.log4j.core.selector.ClassLoaderContextSelector;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.core.selector.ContextSelector;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.spi.LoggerContextFactory;

public class Log4jContextFactory implements LoggerContextFactory
{
    private static final StatusLogger LOGGER;
    private ContextSelector selector;
    
    public Log4jContextFactory() {
        final String sel = PropertiesUtil.getProperties().getStringProperty("Log4jContextSelector");
        if (sel != null) {
            try {
                final Class<?> clazz = Loader.loadClass(sel);
                if (clazz != null && ContextSelector.class.isAssignableFrom(clazz)) {
                    this.selector = (ContextSelector)clazz.newInstance();
                }
            }
            catch (Exception ex) {
                Log4jContextFactory.LOGGER.error("Unable to create context " + sel, ex);
            }
        }
        if (this.selector == null) {
            this.selector = new ClassLoaderContextSelector();
        }
        try {
            Server.registerMBeans(this.selector);
        }
        catch (Exception ex) {
            Log4jContextFactory.LOGGER.error("Could not start JMX", ex);
        }
    }
    
    public ContextSelector getSelector() {
        return this.selector;
    }
    
    @Override
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final boolean currentContext) {
        final LoggerContext ctx = this.selector.getContext(fqcn, loader, currentContext);
        if (ctx.getStatus() == LoggerContext.Status.INITIALIZED) {
            ctx.start();
        }
        return ctx;
    }
    
    @Override
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final boolean currentContext, final URI configLocation) {
        final LoggerContext ctx = this.selector.getContext(fqcn, loader, currentContext, configLocation);
        if (ctx.getStatus() == LoggerContext.Status.INITIALIZED) {
            ctx.start();
        }
        return ctx;
    }
    
    @Override
    public void removeContext(final org.apache.logging.log4j.spi.LoggerContext context) {
        if (context instanceof LoggerContext) {
            this.selector.removeContext((LoggerContext)context);
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
