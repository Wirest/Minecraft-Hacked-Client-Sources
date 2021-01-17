// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "MethodLocationPatternConverter", category = "Converter")
@ConverterKeys({ "M", "method" })
public final class MethodLocationPatternConverter extends LogEventPatternConverter
{
    private static final MethodLocationPatternConverter INSTANCE;
    
    private MethodLocationPatternConverter() {
        super("Method", "method");
    }
    
    public static MethodLocationPatternConverter newInstance(final String[] options) {
        return MethodLocationPatternConverter.INSTANCE;
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        final StackTraceElement element = event.getSource();
        if (element != null) {
            toAppendTo.append(element.getMethodName());
        }
    }
    
    static {
        INSTANCE = new MethodLocationPatternConverter();
    }
}
