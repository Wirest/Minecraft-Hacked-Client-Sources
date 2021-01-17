// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Integers;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "TimeBasedTriggeringPolicy", category = "Core", printObject = true)
public final class TimeBasedTriggeringPolicy implements TriggeringPolicy
{
    private long nextRollover;
    private final int interval;
    private final boolean modulate;
    private RollingFileManager manager;
    
    private TimeBasedTriggeringPolicy(final int interval, final boolean modulate) {
        this.interval = interval;
        this.modulate = modulate;
    }
    
    @Override
    public void initialize(final RollingFileManager manager) {
        this.manager = manager;
        this.nextRollover = manager.getPatternProcessor().getNextTime(manager.getFileTime(), this.interval, this.modulate);
    }
    
    @Override
    public boolean isTriggeringEvent(final LogEvent event) {
        if (this.manager.getFileSize() == 0L) {
            return false;
        }
        final long now = System.currentTimeMillis();
        if (now > this.nextRollover) {
            this.nextRollover = this.manager.getPatternProcessor().getNextTime(now, this.interval, this.modulate);
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "TimeBasedTriggeringPolicy";
    }
    
    @PluginFactory
    public static TimeBasedTriggeringPolicy createPolicy(@PluginAttribute("interval") final String interval, @PluginAttribute("modulate") final String modulate) {
        final int increment = Integers.parseInt(interval, 1);
        final boolean mod = Boolean.parseBoolean(modulate);
        return new TimeBasedTriggeringPolicy(increment, mod);
    }
}
