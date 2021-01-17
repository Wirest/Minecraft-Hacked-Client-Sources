// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import java.lang.management.ManagementFactory;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "RelativeTimePatternConverter", category = "Converter")
@ConverterKeys({ "r", "relative" })
public class RelativeTimePatternConverter extends LogEventPatternConverter
{
    private long lastTimestamp;
    private final long startTime;
    private String relative;
    
    public RelativeTimePatternConverter() {
        super("Time", "time");
        this.lastTimestamp = Long.MIN_VALUE;
        this.startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
    }
    
    public static RelativeTimePatternConverter newInstance(final String[] options) {
        return new RelativeTimePatternConverter();
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        final long timestamp = event.getMillis();
        synchronized (this) {
            if (timestamp != this.lastTimestamp) {
                this.lastTimestamp = timestamp;
                this.relative = Long.toString(timestamp - this.startTime);
            }
        }
        toAppendTo.append(this.relative);
    }
}
