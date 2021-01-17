// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.web;

import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.selector.ContextSelector;
import org.apache.logging.log4j.spi.LoggerContextFactory;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.apache.logging.log4j.core.impl.Log4jContextFactory;
import org.apache.logging.log4j.LogManager;
import java.net.URI;
import javax.servlet.UnavailableException;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.apache.logging.log4j.core.lookup.Interpolator;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.selector.NamedContextSelector;
import javax.servlet.ServletContext;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;

final class Log4jWebInitializerImpl implements Log4jWebInitializer
{
    private static final Object MUTEX;
    private final StrSubstitutor substitutor;
    private final ServletContext servletContext;
    private String name;
    private NamedContextSelector selector;
    private LoggerContext loggerContext;
    private boolean initialized;
    private boolean deinitialized;
    
    private Log4jWebInitializerImpl(final ServletContext servletContext) {
        this.substitutor = new StrSubstitutor(new Interpolator());
        this.initialized = false;
        this.deinitialized = false;
        this.servletContext = servletContext;
    }
    
    @Override
    public synchronized void initialize() throws UnavailableException {
        if (this.deinitialized) {
            throw new IllegalStateException("Cannot initialize Log4jWebInitializer after it was destroyed.");
        }
        if (!this.initialized) {
            this.initialized = true;
            this.name = this.substitutor.replace(this.servletContext.getInitParameter("log4jContextName"));
            final String location = this.substitutor.replace(this.servletContext.getInitParameter("log4jConfiguration"));
            final boolean isJndi = "true".equals(this.servletContext.getInitParameter("isLog4jContextSelectorNamed"));
            if (isJndi) {
                this.initializeJndi(location);
            }
            else {
                this.initializeNonJndi(location);
            }
        }
    }
    
    private void initializeJndi(final String location) throws UnavailableException {
        URI configLocation = null;
        if (location != null) {
            try {
                configLocation = new URI(location);
            }
            catch (Exception e) {
                this.servletContext.log("Unable to convert configuration location [" + location + "] to a URI!", (Throwable)e);
            }
        }
        if (this.name == null) {
            throw new UnavailableException("A log4jContextName context parameter is required");
        }
        final LoggerContextFactory factory = LogManager.getFactory();
        if (!(factory instanceof Log4jContextFactory)) {
            this.servletContext.log("Potential problem: Factory is not an instance of Log4jContextFactory.");
            return;
        }
        final ContextSelector selector = ((Log4jContextFactory)factory).getSelector();
        if (selector instanceof NamedContextSelector) {
            this.selector = (NamedContextSelector)selector;
            final LoggerContext loggerContext = this.selector.locateContext(this.name, this.servletContext, configLocation);
            ContextAnchor.THREAD_CONTEXT.set(loggerContext);
            if (loggerContext.getStatus() == LoggerContext.Status.INITIALIZED) {
                loggerContext.start();
            }
            ContextAnchor.THREAD_CONTEXT.remove();
            this.loggerContext = loggerContext;
            this.servletContext.log("Created logger context for [" + this.name + "] using [" + loggerContext.getClass().getClassLoader() + "].");
            return;
        }
        this.servletContext.log("Potential problem: Selector is not an instance of NamedContextSelector.");
    }
    
    private void initializeNonJndi(final String location) {
        if (this.name == null) {
            this.name = this.servletContext.getServletContextName();
        }
        if (this.name == null && location == null) {
            this.servletContext.log("No Log4j context configuration provided. This is very unusual.");
            return;
        }
        this.loggerContext = Configurator.initialize(this.name, this.getClassLoader(), location, this.servletContext);
    }
    
    @Override
    public synchronized void deinitialize() {
        if (!this.initialized) {
            throw new IllegalStateException("Cannot deinitialize Log4jWebInitializer because it has not initialized.");
        }
        if (!this.deinitialized) {
            this.deinitialized = true;
            if (this.loggerContext != null) {
                this.servletContext.log("Removing LoggerContext for [" + this.name + "].");
                if (this.selector != null) {
                    this.selector.removeContext(this.name);
                }
                this.loggerContext.stop();
                this.loggerContext.setExternalContext(null);
                this.loggerContext = null;
            }
        }
    }
    
    @Override
    public void setLoggerContext() {
        if (this.loggerContext != null) {
            ContextAnchor.THREAD_CONTEXT.set(this.loggerContext);
        }
    }
    
    @Override
    public void clearLoggerContext() {
        ContextAnchor.THREAD_CONTEXT.remove();
    }
    
    private ClassLoader getClassLoader() {
        try {
            return this.servletContext.getClassLoader();
        }
        catch (Throwable ignore) {
            return Log4jWebInitializerImpl.class.getClassLoader();
        }
    }
    
    static Log4jWebInitializer getLog4jWebInitializer(final ServletContext servletContext) {
        synchronized (Log4jWebInitializerImpl.MUTEX) {
            Log4jWebInitializer initializer = (Log4jWebInitializer)servletContext.getAttribute(Log4jWebInitializerImpl.INITIALIZER_ATTRIBUTE);
            if (initializer == null) {
                initializer = new Log4jWebInitializerImpl(servletContext);
                servletContext.setAttribute(Log4jWebInitializerImpl.INITIALIZER_ATTRIBUTE, (Object)initializer);
            }
            return initializer;
        }
    }
    
    static {
        MUTEX = new Object();
        try {
            Class.forName("org.apache.logging.log4j.core.web.JNDIContextFilter");
            throw new IllegalStateException("You are using Log4j 2 in a web application with the old, extinct log4j-web artifact. This is not supported and could cause serious runtime problems. Pleaseremove the log4j-web JAR file from your application.");
        }
        catch (ClassNotFoundException ignore) {}
    }
}
