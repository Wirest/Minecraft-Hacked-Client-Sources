// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import java.util.Iterator;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.config.Configuration;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "replace", category = "Converter")
@ConverterKeys({ "replace" })
public final class RegexReplacementConverter extends LogEventPatternConverter
{
    private final Pattern pattern;
    private final String substitution;
    private final List<PatternFormatter> formatters;
    
    private RegexReplacementConverter(final List<PatternFormatter> formatters, final Pattern pattern, final String substitution) {
        super("replace", "replace");
        this.pattern = pattern;
        this.substitution = substitution;
        this.formatters = formatters;
    }
    
    public static RegexReplacementConverter newInstance(final Configuration config, final String[] options) {
        if (options.length != 3) {
            RegexReplacementConverter.LOGGER.error("Incorrect number of options on replace. Expected 3 received " + options.length);
            return null;
        }
        if (options[0] == null) {
            RegexReplacementConverter.LOGGER.error("No pattern supplied on replace");
            return null;
        }
        if (options[1] == null) {
            RegexReplacementConverter.LOGGER.error("No regular expression supplied on replace");
            return null;
        }
        if (options[2] == null) {
            RegexReplacementConverter.LOGGER.error("No substitution supplied on replace");
            return null;
        }
        final Pattern p = Pattern.compile(options[1]);
        final PatternParser parser = PatternLayout.createPatternParser(config);
        final List<PatternFormatter> formatters = parser.parse(options[0]);
        return new RegexReplacementConverter(formatters, p, options[2]);
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        final StringBuilder buf = new StringBuilder();
        for (final PatternFormatter formatter : this.formatters) {
            formatter.format(event, buf);
        }
        toAppendTo.append(this.pattern.matcher(buf.toString()).replaceAll(this.substitution));
    }
}
