// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.web;

import javax.servlet.UnavailableException;

interface Log4jWebInitializer
{
    public static final String LOG4J_CONTEXT_NAME = "log4jContextName";
    public static final String LOG4J_CONFIG_LOCATION = "log4jConfiguration";
    public static final String IS_LOG4J_CONTEXT_SELECTOR_NAMED = "isLog4jContextSelectorNamed";
    public static final String INITIALIZER_ATTRIBUTE = Log4jWebInitializer.class.getName() + ".INSTANCE";
    
    void initialize() throws UnavailableException;
    
    void deinitialize();
    
    void setLoggerContext();
    
    void clearLoggerContext();
}
