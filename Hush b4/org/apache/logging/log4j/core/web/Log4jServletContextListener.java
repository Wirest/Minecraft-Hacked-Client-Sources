// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.web;

import javax.servlet.UnavailableException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;

public class Log4jServletContextListener implements ServletContextListener
{
    private ServletContext servletContext;
    private Log4jWebInitializer initializer;
    
    public void contextInitialized(final ServletContextEvent event) {
        (this.servletContext = event.getServletContext()).log("Log4jServletContextListener ensuring that Log4j starts up properly.");
        this.initializer = Log4jWebInitializerImpl.getLog4jWebInitializer(this.servletContext);
        try {
            this.initializer.initialize();
            this.initializer.setLoggerContext();
        }
        catch (UnavailableException e) {
            throw new RuntimeException("Failed to initialize Log4j properly.", (Throwable)e);
        }
    }
    
    public void contextDestroyed(final ServletContextEvent event) {
        if (this.servletContext == null || this.initializer == null) {
            throw new IllegalStateException("Context destroyed before it was initialized.");
        }
        this.servletContext.log("Log4jServletContextListener ensuring that Log4j shuts down properly.");
        this.initializer.clearLoggerContext();
        this.initializer.deinitialize();
    }
}
