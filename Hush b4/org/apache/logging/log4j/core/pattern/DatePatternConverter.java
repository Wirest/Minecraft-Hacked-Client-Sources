// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import java.util.Date;
import org.apache.logging.log4j.core.LogEvent;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "DatePatternConverter", category = "Converter")
@ConverterKeys({ "d", "date" })
public final class DatePatternConverter extends LogEventPatternConverter implements ArrayPatternConverter
{
    private static final String ABSOLUTE_FORMAT = "ABSOLUTE";
    private static final String COMPACT_FORMAT = "COMPACT";
    private static final String ABSOLUTE_TIME_PATTERN = "HH:mm:ss,SSS";
    private static final String DATE_AND_TIME_FORMAT = "DATE";
    private static final String DATE_AND_TIME_PATTERN = "dd MMM yyyy HH:mm:ss,SSS";
    private static final String ISO8601_FORMAT = "ISO8601";
    private static final String ISO8601_BASIC_FORMAT = "ISO8601_BASIC";
    private static final String ISO8601_PATTERN = "yyyy-MM-dd HH:mm:ss,SSS";
    private static final String ISO8601_BASIC_PATTERN = "yyyyMMdd HHmmss,SSS";
    private static final String COMPACT_PATTERN = "yyyyMMddHHmmssSSS";
    private String cachedDate;
    private long lastTimestamp;
    private final SimpleDateFormat simpleFormat;
    
    private DatePatternConverter(final String[] options) {
        super("Date", "date");
        String patternOption;
        if (options == null || options.length == 0) {
            patternOption = null;
        }
        else {
            patternOption = options[0];
        }
        String pattern;
        if (patternOption == null || patternOption.equalsIgnoreCase("ISO8601")) {
            pattern = "yyyy-MM-dd HH:mm:ss,SSS";
        }
        else if (patternOption.equalsIgnoreCase("ISO8601_BASIC")) {
            pattern = "yyyyMMdd HHmmss,SSS";
        }
        else if (patternOption.equalsIgnoreCase("ABSOLUTE")) {
            pattern = "HH:mm:ss,SSS";
        }
        else if (patternOption.equalsIgnoreCase("DATE")) {
            pattern = "dd MMM yyyy HH:mm:ss,SSS";
        }
        else if (patternOption.equalsIgnoreCase("COMPACT")) {
            pattern = "yyyyMMddHHmmssSSS";
        }
        else {
            pattern = patternOption;
        }
        SimpleDateFormat tempFormat;
        try {
            tempFormat = new SimpleDateFormat(pattern);
        }
        catch (IllegalArgumentException e) {
            DatePatternConverter.LOGGER.warn("Could not instantiate SimpleDateFormat with pattern " + patternOption, e);
            tempFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        }
        if (options != null && options.length > 1) {
            final TimeZone tz = TimeZone.getTimeZone(options[1]);
            tempFormat.setTimeZone(tz);
        }
        this.simpleFormat = tempFormat;
    }
    
    public static DatePatternConverter newInstance(final String[] options) {
        return new DatePatternConverter(options);
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder output) {
        final long timestamp = event.getMillis();
        synchronized (this) {
            if (timestamp != this.lastTimestamp) {
                this.lastTimestamp = timestamp;
                this.cachedDate = this.simpleFormat.format(timestamp);
            }
        }
        output.append(this.cachedDate);
    }
    
    @Override
    public void format(final StringBuilder toAppendTo, final Object... objects) {
        for (final Object obj : objects) {
            if (obj instanceof Date) {
                this.format(obj, toAppendTo);
                break;
            }
        }
    }
    
    @Override
    public void format(final Object obj, final StringBuilder output) {
        if (obj instanceof Date) {
            this.format((Date)obj, output);
        }
        super.format(obj, output);
    }
    
    public void format(final Date date, final StringBuilder toAppendTo) {
        synchronized (this) {
            toAppendTo.append(this.simpleFormat.format(date.getTime()));
        }
    }
    
    public String getPattern() {
        return this.simpleFormat.toPattern();
    }
}
