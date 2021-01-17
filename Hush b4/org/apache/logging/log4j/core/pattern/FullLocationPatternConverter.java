// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "FullLocationPatternConverter", category = "Converter")
@ConverterKeys({ "l", "location" })
public final class FullLocationPatternConverter extends LogEventPatternConverter
{
    private static final FullLocationPatternConverter INSTANCE;
    
    private FullLocationPatternConverter() {
        super("Full Location", "fullLocation");
    }
    
    public static FullLocationPatternConverter newInstance(final String[] options) {
        return FullLocationPatternConverter.INSTANCE;
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder output) {
        final StackTraceElement element = event.getSource();
        if (element != null) {
            output.append(element.toString());
        }
    }
    
    static {
        INSTANCE = new FullLocationPatternConverter();
    }
}
