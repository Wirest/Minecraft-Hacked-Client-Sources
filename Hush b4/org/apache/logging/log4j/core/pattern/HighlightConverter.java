// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import java.util.HashMap;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.config.Configuration;
import java.util.Iterator;
import java.util.Arrays;
import java.util.Locale;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Level;
import java.util.EnumMap;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "highlight", category = "Converter")
@ConverterKeys({ "highlight" })
public final class HighlightConverter extends LogEventPatternConverter
{
    private static final EnumMap<Level, String> DEFAULT_STYLES;
    private static final EnumMap<Level, String> LOGBACK_STYLES;
    private static final String STYLE_KEY = "STYLE";
    private static final String STYLE_KEY_DEFAULT = "DEFAULT";
    private static final String STYLE_KEY_LOGBACK = "LOGBACK";
    private static final Map<String, EnumMap<Level, String>> STYLES;
    private final EnumMap<Level, String> levelStyles;
    private final List<PatternFormatter> patternFormatters;
    
    private static EnumMap<Level, String> createLevelStyleMap(final String[] options) {
        if (options.length < 2) {
            return HighlightConverter.DEFAULT_STYLES;
        }
        final Map<String, String> styles = AnsiEscape.createMap(options[1], new String[] { "STYLE" });
        final EnumMap<Level, String> levelStyles = new EnumMap<Level, String>(HighlightConverter.DEFAULT_STYLES);
        for (final Map.Entry<String, String> entry : styles.entrySet()) {
            final String key = entry.getKey().toUpperCase(Locale.ENGLISH);
            final String value = entry.getValue();
            if ("STYLE".equalsIgnoreCase(key)) {
                final EnumMap<Level, String> enumMap = HighlightConverter.STYLES.get(value.toUpperCase(Locale.ENGLISH));
                if (enumMap == null) {
                    HighlightConverter.LOGGER.error("Unknown level style: " + value + ". Use one of " + Arrays.toString(HighlightConverter.STYLES.keySet().toArray()));
                }
                else {
                    levelStyles.putAll(enumMap);
                }
            }
            else {
                final Level level = Level.valueOf(key);
                if (level == null) {
                    HighlightConverter.LOGGER.error("Unknown level name: " + key + ". Use one of " + Arrays.toString(HighlightConverter.DEFAULT_STYLES.keySet().toArray()));
                }
                else {
                    levelStyles.put(level, value);
                }
            }
        }
        return levelStyles;
    }
    
    public static HighlightConverter newInstance(final Configuration config, final String[] options) {
        if (options.length < 1) {
            HighlightConverter.LOGGER.error("Incorrect number of options on style. Expected at least 1, received " + options.length);
            return null;
        }
        if (options[0] == null) {
            HighlightConverter.LOGGER.error("No pattern supplied on style");
            return null;
        }
        final PatternParser parser = PatternLayout.createPatternParser(config);
        final List<PatternFormatter> formatters = parser.parse(options[0]);
        return new HighlightConverter(formatters, createLevelStyleMap(options));
    }
    
    private HighlightConverter(final List<PatternFormatter> patternFormatters, final EnumMap<Level, String> levelStyles) {
        super("style", "style");
        this.patternFormatters = patternFormatters;
        this.levelStyles = levelStyles;
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        final StringBuilder buf = new StringBuilder();
        for (final PatternFormatter formatter : this.patternFormatters) {
            formatter.format(event, buf);
        }
        if (buf.length() > 0) {
            toAppendTo.append(this.levelStyles.get(event.getLevel())).append(buf.toString()).append(AnsiEscape.getDefaultStyle());
        }
    }
    
    @Override
    public boolean handlesThrowable() {
        for (final PatternFormatter formatter : this.patternFormatters) {
            if (formatter.handlesThrowable()) {
                return true;
            }
        }
        return false;
    }
    
    static {
        DEFAULT_STYLES = new EnumMap<Level, String>(Level.class);
        LOGBACK_STYLES = new EnumMap<Level, String>(Level.class);
        STYLES = new HashMap<String, EnumMap<Level, String>>();
        HighlightConverter.DEFAULT_STYLES.put(Level.FATAL, AnsiEscape.createSequence("BRIGHT", "RED"));
        HighlightConverter.DEFAULT_STYLES.put(Level.ERROR, AnsiEscape.createSequence("BRIGHT", "RED"));
        HighlightConverter.DEFAULT_STYLES.put(Level.WARN, AnsiEscape.createSequence("YELLOW"));
        HighlightConverter.DEFAULT_STYLES.put(Level.INFO, AnsiEscape.createSequence("GREEN"));
        HighlightConverter.DEFAULT_STYLES.put(Level.DEBUG, AnsiEscape.createSequence("CYAN"));
        HighlightConverter.DEFAULT_STYLES.put(Level.TRACE, AnsiEscape.createSequence("BLACK"));
        HighlightConverter.LOGBACK_STYLES.put(Level.FATAL, AnsiEscape.createSequence("BLINK", "BRIGHT", "RED"));
        HighlightConverter.LOGBACK_STYLES.put(Level.ERROR, AnsiEscape.createSequence("BRIGHT", "RED"));
        HighlightConverter.LOGBACK_STYLES.put(Level.WARN, AnsiEscape.createSequence("RED"));
        HighlightConverter.LOGBACK_STYLES.put(Level.INFO, AnsiEscape.createSequence("BLUE"));
        HighlightConverter.LOGBACK_STYLES.put(Level.DEBUG, AnsiEscape.createSequence((String[])null));
        HighlightConverter.LOGBACK_STYLES.put(Level.TRACE, AnsiEscape.createSequence((String[])null));
        HighlightConverter.STYLES.put("DEFAULT", HighlightConverter.DEFAULT_STYLES);
        HighlightConverter.STYLES.put("LOGBACK", HighlightConverter.LOGBACK_STYLES);
    }
}
