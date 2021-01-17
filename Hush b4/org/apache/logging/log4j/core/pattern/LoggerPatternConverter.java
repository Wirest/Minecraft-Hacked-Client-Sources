// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "LoggerPatternConverter", category = "Converter")
@ConverterKeys({ "c", "logger" })
public final class LoggerPatternConverter extends NamePatternConverter
{
    private static final LoggerPatternConverter INSTANCE;
    
    private LoggerPatternConverter(final String[] options) {
        super("Logger", "logger", options);
    }
    
    public static LoggerPatternConverter newInstance(final String[] options) {
        if (options == null || options.length == 0) {
            return LoggerPatternConverter.INSTANCE;
        }
        return new LoggerPatternConverter(options);
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        toAppendTo.append(this.abbreviate(event.getLoggerName()));
    }
    
    static {
        INSTANCE = new LoggerPatternConverter(null);
    }
}
