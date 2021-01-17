// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import java.io.Serializable;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.helpers.Charsets;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import java.util.Iterator;
import org.apache.logging.log4j.core.LogEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.helpers.OptionConverter;
import org.apache.logging.log4j.core.pattern.PatternParser;
import java.nio.charset.Charset;
import org.apache.logging.log4j.core.pattern.RegexReplacement;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import java.util.List;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "PatternLayout", category = "Core", elementType = "layout", printObject = true)
public final class PatternLayout extends AbstractStringLayout
{
    public static final String DEFAULT_CONVERSION_PATTERN = "%m%n";
    public static final String TTCC_CONVERSION_PATTERN = "%r [%t] %p %c %x - %m%n";
    public static final String SIMPLE_CONVERSION_PATTERN = "%d [%t] %p %c - %m%n";
    public static final String KEY = "Converter";
    private List<PatternFormatter> formatters;
    private final String conversionPattern;
    private final Configuration config;
    private final RegexReplacement replace;
    private final boolean alwaysWriteExceptions;
    
    private PatternLayout(final Configuration config, final RegexReplacement replace, final String pattern, final Charset charset, final boolean alwaysWriteExceptions) {
        super(charset);
        this.replace = replace;
        this.conversionPattern = pattern;
        this.config = config;
        this.alwaysWriteExceptions = alwaysWriteExceptions;
        final PatternParser parser = createPatternParser(config);
        this.formatters = parser.parse((pattern == null) ? "%m%n" : pattern, this.alwaysWriteExceptions);
    }
    
    public void setConversionPattern(final String conversionPattern) {
        final String pattern = OptionConverter.convertSpecialChars(conversionPattern);
        if (pattern == null) {
            return;
        }
        final PatternParser parser = createPatternParser(this.config);
        this.formatters = parser.parse(pattern, this.alwaysWriteExceptions);
    }
    
    public String getConversionPattern() {
        return this.conversionPattern;
    }
    
    @Override
    public Map<String, String> getContentFormat() {
        final Map<String, String> result = new HashMap<String, String>();
        result.put("structured", "false");
        result.put("formatType", "conversion");
        result.put("format", this.conversionPattern);
        return result;
    }
    
    @Override
    public String toSerializable(final LogEvent event) {
        final StringBuilder buf = new StringBuilder();
        for (final PatternFormatter formatter : this.formatters) {
            formatter.format(event, buf);
        }
        String str = buf.toString();
        if (this.replace != null) {
            str = this.replace.format(str);
        }
        return str;
    }
    
    public static PatternParser createPatternParser(final Configuration config) {
        if (config == null) {
            return new PatternParser(config, "Converter", LogEventPatternConverter.class);
        }
        PatternParser parser = config.getComponent("Converter");
        if (parser == null) {
            parser = new PatternParser(config, "Converter", LogEventPatternConverter.class);
            config.addComponent("Converter", parser);
            parser = config.getComponent("Converter");
        }
        return parser;
    }
    
    @Override
    public String toString() {
        return this.conversionPattern;
    }
    
    @PluginFactory
    public static PatternLayout createLayout(@PluginAttribute("pattern") final String pattern, @PluginConfiguration final Configuration config, @PluginElement("Replace") final RegexReplacement replace, @PluginAttribute("charset") final String charsetName, @PluginAttribute("alwaysWriteExceptions") final String always) {
        final Charset charset = Charsets.getSupportedCharset(charsetName);
        final boolean alwaysWriteExceptions = Booleans.parseBoolean(always, true);
        return new PatternLayout(config, replace, (pattern == null) ? "%m%n" : pattern, charset, alwaysWriteExceptions);
    }
}
