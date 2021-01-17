// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.status.StatusLogger;
import java.lang.management.ManagementFactory;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "OnStartupTriggeringPolicy", category = "Core", printObject = true)
public class OnStartupTriggeringPolicy implements TriggeringPolicy
{
    private static long JVM_START_TIME;
    private static final Logger LOGGER;
    private boolean evaluated;
    private RollingFileManager manager;
    
    public OnStartupTriggeringPolicy() {
        this.evaluated = false;
    }
    
    @Override
    public void initialize(final RollingFileManager manager) {
        this.manager = manager;
        if (OnStartupTriggeringPolicy.JVM_START_TIME == 0L) {
            this.evaluated = true;
        }
    }
    
    @Override
    public boolean isTriggeringEvent(final LogEvent event) {
        if (this.evaluated) {
            return false;
        }
        this.evaluated = true;
        return this.manager.getFileTime() < OnStartupTriggeringPolicy.JVM_START_TIME;
    }
    
    @Override
    public String toString() {
        return "OnStartupTriggeringPolicy";
    }
    
    @PluginFactory
    public static OnStartupTriggeringPolicy createPolicy() {
        return new OnStartupTriggeringPolicy();
    }
    
    static {
        OnStartupTriggeringPolicy.JVM_START_TIME = ManagementFactory.getRuntimeMXBean().getStartTime();
        LOGGER = StatusLogger.getLogger();
    }
}
