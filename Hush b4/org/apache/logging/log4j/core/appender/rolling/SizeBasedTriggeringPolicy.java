// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.regex.Matcher;
import java.text.ParseException;
import java.text.NumberFormat;
import java.util.Locale;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.LogEvent;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "SizeBasedTriggeringPolicy", category = "Core", printObject = true)
public class SizeBasedTriggeringPolicy implements TriggeringPolicy
{
    protected static final Logger LOGGER;
    private static final long KB = 1024L;
    private static final long MB = 1048576L;
    private static final long GB = 1073741824L;
    private static final long MAX_FILE_SIZE = 10485760L;
    private static final Pattern VALUE_PATTERN;
    private final long maxFileSize;
    private RollingFileManager manager;
    
    protected SizeBasedTriggeringPolicy() {
        this.maxFileSize = 10485760L;
    }
    
    protected SizeBasedTriggeringPolicy(final long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }
    
    @Override
    public void initialize(final RollingFileManager manager) {
        this.manager = manager;
    }
    
    @Override
    public boolean isTriggeringEvent(final LogEvent event) {
        return this.manager.getFileSize() > this.maxFileSize;
    }
    
    @Override
    public String toString() {
        return "SizeBasedTriggeringPolicy(size=" + this.maxFileSize + ")";
    }
    
    @PluginFactory
    public static SizeBasedTriggeringPolicy createPolicy(@PluginAttribute("size") final String size) {
        final long maxSize = (size == null) ? 10485760L : valueOf(size);
        return new SizeBasedTriggeringPolicy(maxSize);
    }
    
    private static long valueOf(final String string) {
        final Matcher matcher = SizeBasedTriggeringPolicy.VALUE_PATTERN.matcher(string);
        if (matcher.matches()) {
            try {
                final long value = NumberFormat.getNumberInstance(Locale.getDefault()).parse(matcher.group(1)).longValue();
                final String units = matcher.group(3);
                if (units.equalsIgnoreCase("")) {
                    return value;
                }
                if (units.equalsIgnoreCase("K")) {
                    return value * 1024L;
                }
                if (units.equalsIgnoreCase("M")) {
                    return value * 1048576L;
                }
                if (units.equalsIgnoreCase("G")) {
                    return value * 1073741824L;
                }
                SizeBasedTriggeringPolicy.LOGGER.error("Units not recognized: " + string);
                return 10485760L;
            }
            catch (ParseException e) {
                SizeBasedTriggeringPolicy.LOGGER.error("Unable to parse numeric part: " + string, e);
                return 10485760L;
            }
        }
        SizeBasedTriggeringPolicy.LOGGER.error("Unable to parse bytes: " + string);
        return 10485760L;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        VALUE_PATTERN = Pattern.compile("([0-9]+([\\.,][0-9]+)?)\\s*(|K|M|G)B?", 2);
    }
}
