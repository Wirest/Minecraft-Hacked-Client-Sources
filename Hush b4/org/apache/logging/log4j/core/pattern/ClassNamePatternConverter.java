// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "ClassNamePatternConverter", category = "Converter")
@ConverterKeys({ "C", "class" })
public final class ClassNamePatternConverter extends NamePatternConverter
{
    private static final String NA = "?";
    
    private ClassNamePatternConverter(final String[] options) {
        super("Class Name", "class name", options);
    }
    
    public static ClassNamePatternConverter newInstance(final String[] options) {
        return new ClassNamePatternConverter(options);
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        final StackTraceElement element = event.getSource();
        if (element == null) {
            toAppendTo.append("?");
        }
        else {
            toAppendTo.append(this.abbreviate(element.getClassName()));
        }
    }
}
