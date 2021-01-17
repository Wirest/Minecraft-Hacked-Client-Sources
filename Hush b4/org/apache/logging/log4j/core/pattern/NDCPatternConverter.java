// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "NDCPatternConverter", category = "Converter")
@ConverterKeys({ "x", "NDC" })
public final class NDCPatternConverter extends LogEventPatternConverter
{
    private static final NDCPatternConverter INSTANCE;
    
    private NDCPatternConverter() {
        super("NDC", "ndc");
    }
    
    public static NDCPatternConverter newInstance(final String[] options) {
        return NDCPatternConverter.INSTANCE;
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        toAppendTo.append(event.getContextStack());
    }
    
    static {
        INSTANCE = new NDCPatternConverter();
    }
}
