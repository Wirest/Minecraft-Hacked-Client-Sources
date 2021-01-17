// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.Appender;
import java.util.Map;
import org.apache.logging.log4j.core.filter.Filterable;

public interface Configuration extends Filterable
{
    public static final String CONTEXT_PROPERTIES = "ContextProperties";
    
    String getName();
    
    LoggerConfig getLoggerConfig(final String p0);
    
    Map<String, Appender> getAppenders();
    
    Map<String, LoggerConfig> getLoggers();
    
    void addLoggerAppender(final Logger p0, final Appender p1);
    
    void addLoggerFilter(final Logger p0, final Filter p1);
    
    void setLoggerAdditive(final Logger p0, final boolean p1);
    
    Map<String, String> getProperties();
    
    void start();
    
    void stop();
    
    void addListener(final ConfigurationListener p0);
    
    void removeListener(final ConfigurationListener p0);
    
    StrSubstitutor getStrSubstitutor();
    
    void createConfiguration(final Node p0, final LogEvent p1);
    
     <T> T getComponent(final String p0);
    
    void addComponent(final String p0, final Object p1);
    
    void setConfigurationMonitor(final ConfigurationMonitor p0);
    
    ConfigurationMonitor getConfigurationMonitor();
    
    void setAdvertiser(final Advertiser p0);
    
    Advertiser getAdvertiser();
    
    boolean isShutdownHookEnabled();
}
