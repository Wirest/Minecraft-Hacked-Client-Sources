// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.web;

import javax.servlet.ServletException;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.UnavailableException;
import javax.servlet.Filter;
import java.util.EventListener;
import javax.servlet.ServletContext;
import java.util.Set;
import javax.servlet.ServletContainerInitializer;

public class Log4jServletContainerInitializer implements ServletContainerInitializer
{
    public void onStartup(final Set<Class<?>> classes, final ServletContext servletContext) throws ServletException {
        if (servletContext.getMajorVersion() > 2) {
            servletContext.log("Log4jServletContainerInitializer starting up Log4j in Servlet 3.0+ environment.");
            final Log4jWebInitializer initializer = Log4jWebInitializerImpl.getLog4jWebInitializer(servletContext);
            initializer.initialize();
            initializer.setLoggerContext();
            servletContext.addListener((EventListener)new Log4jServletContextListener());
            final FilterRegistration.Dynamic filter = servletContext.addFilter("log4jServletFilter", (Filter)new Log4jServletFilter());
            if (filter == null) {
                throw new UnavailableException("In a Servlet 3.0+ application, you must not define a log4jServletFilter in web.xml. Log4j 2 defines this for you automatically.");
            }
            filter.addMappingForUrlPatterns((EnumSet)EnumSet.allOf(DispatcherType.class), false, new String[] { "/*" });
        }
    }
}
