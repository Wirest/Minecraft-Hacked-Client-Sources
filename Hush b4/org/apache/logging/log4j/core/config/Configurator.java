// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.apache.logging.log4j.LogManager;
import java.net.URISyntaxException;
import java.net.URI;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.status.StatusLogger;

public final class Configurator
{
    protected static final StatusLogger LOGGER;
    
    private Configurator() {
    }
    
    public static LoggerContext initialize(final String name, final ClassLoader loader, final String configLocation) {
        return initialize(name, loader, configLocation, null);
    }
    
    public static LoggerContext initialize(final String name, final ClassLoader loader, final String configLocation, final Object externalContext) {
        try {
            final URI uri = (configLocation == null) ? null : new URI(configLocation);
            return initialize(name, loader, uri, externalContext);
        }
        catch (URISyntaxException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static LoggerContext initialize(final String name, final String configLocation) {
        return initialize(name, null, configLocation);
    }
    
    public static LoggerContext initialize(final String name, final ClassLoader loader, final URI configLocation) {
        return initialize(name, loader, configLocation, null);
    }
    
    public static LoggerContext initialize(final String name, final ClassLoader loader, final URI configLocation, final Object externalContext) {
        try {
            final org.apache.logging.log4j.spi.LoggerContext context = LogManager.getContext(loader, false, configLocation);
            if (context instanceof LoggerContext) {
                final LoggerContext ctx = (LoggerContext)context;
                ContextAnchor.THREAD_CONTEXT.set(ctx);
                if (externalContext != null) {
                    ctx.setExternalContext(externalContext);
                }
                final Configuration config = ConfigurationFactory.getInstance().getConfiguration(name, configLocation);
                ctx.start(config);
                ContextAnchor.THREAD_CONTEXT.remove();
                return ctx;
            }
            Configurator.LOGGER.error("LogManager returned an instance of {} which does not implement {}. Unable to initialize Log4j", context.getClass().getName(), LoggerContext.class.getName());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public static LoggerContext initialize(final ClassLoader loader, final ConfigurationFactory.ConfigurationSource source) {
        try {
            URI configLocation = null;
            try {
                configLocation = ((source.getLocation() == null) ? null : new URI(source.getLocation()));
            }
            catch (Exception ex2) {}
            final org.apache.logging.log4j.spi.LoggerContext context = LogManager.getContext(loader, false, configLocation);
            if (context instanceof LoggerContext) {
                final LoggerContext ctx = (LoggerContext)context;
                ContextAnchor.THREAD_CONTEXT.set(ctx);
                final Configuration config = ConfigurationFactory.getInstance().getConfiguration(source);
                ctx.start(config);
                ContextAnchor.THREAD_CONTEXT.remove();
                return ctx;
            }
            Configurator.LOGGER.error("LogManager returned an instance of {} which does not implement {}. Unable to initialize Log4j", context.getClass().getName(), LoggerContext.class.getName());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public static void shutdown(final LoggerContext ctx) {
        if (ctx != null) {
            ctx.stop();
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
