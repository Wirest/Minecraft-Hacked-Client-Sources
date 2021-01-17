// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.helpers.Constants;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "LineSeparatorPatternConverter", category = "Converter")
@ConverterKeys({ "n" })
public final class LineSeparatorPatternConverter extends LogEventPatternConverter
{
    private static final LineSeparatorPatternConverter INSTANCE;
    private final String lineSep;
    
    private LineSeparatorPatternConverter() {
        super("Line Sep", "lineSep");
        this.lineSep = Constants.LINE_SEP;
    }
    
    public static LineSeparatorPatternConverter newInstance(final String[] options) {
        return LineSeparatorPatternConverter.INSTANCE;
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        toAppendTo.append(this.lineSep);
    }
    
    static {
        INSTANCE = new LineSeparatorPatternConverter();
    }
}
