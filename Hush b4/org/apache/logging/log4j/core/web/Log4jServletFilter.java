// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.web;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.ServletException;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.Filter;

public class Log4jServletFilter implements Filter
{
    static final String ALREADY_FILTERED_ATTRIBUTE;
    private ServletContext servletContext;
    private Log4jWebInitializer initializer;
    
    public void init(final FilterConfig filterConfig) throws ServletException {
        (this.servletContext = filterConfig.getServletContext()).log("Log4jServletFilter initialized.");
        (this.initializer = Log4jWebInitializerImpl.getLog4jWebInitializer(this.servletContext)).clearLoggerContext();
    }
    
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        if (request.getAttribute(Log4jServletFilter.ALREADY_FILTERED_ATTRIBUTE) != null) {
            chain.doFilter(request, response);
        }
        else {
            request.setAttribute(Log4jServletFilter.ALREADY_FILTERED_ATTRIBUTE, (Object)true);
            try {
                this.initializer.setLoggerContext();
                chain.doFilter(request, response);
            }
            finally {
                this.initializer.clearLoggerContext();
            }
        }
    }
    
    public void destroy() {
        if (this.servletContext == null || this.initializer == null) {
            throw new IllegalStateException("Filter destroyed before it was initialized.");
        }
        this.servletContext.log("Log4jServletFilter destroyed.");
        this.initializer.setLoggerContext();
    }
    
    static {
        ALREADY_FILTERED_ATTRIBUTE = Log4jServletFilter.class.getName() + ".FILTERED";
    }
}
