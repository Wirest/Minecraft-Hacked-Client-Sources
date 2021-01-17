// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.config.plugins.Plugin;
import java.util.Iterator;
import org.apache.logging.log4j.core.LogEvent;
import java.util.Arrays;
import org.apache.logging.log4j.core.layout.PatternLayout;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.apache.logging.log4j.core.config.Configuration;
import java.util.List;

public abstract class AbstractStyleNameConverter extends LogEventPatternConverter
{
    private final List<PatternFormatter> formatters;
    private final String style;
    
    protected AbstractStyleNameConverter(final String name, final List<PatternFormatter> formatters, final String styling) {
        super(name, "style");
        this.formatters = formatters;
        this.style = styling;
    }
    
    protected static <T extends AbstractStyleNameConverter> T newInstance(final Class<T> asnConverterClass, final String name, final Configuration config, final String[] options) {
        final List<PatternFormatter> formatters = toPatternFormatterList(config, options);
        if (formatters == null) {
            return null;
        }
        try {
            final Constructor<T> constructor = asnConverterClass.getConstructor(List.class, String.class);
            return constructor.newInstance(formatters, AnsiEscape.createSequence(name));
        }
        catch (SecurityException e) {
            AbstractStyleNameConverter.LOGGER.error(e.toString(), e);
        }
        catch (NoSuchMethodException e2) {
            AbstractStyleNameConverter.LOGGER.error(e2.toString(), e2);
        }
        catch (IllegalArgumentException e3) {
            AbstractStyleNameConverter.LOGGER.error(e3.toString(), e3);
        }
        catch (InstantiationException e4) {
            AbstractStyleNameConverter.LOGGER.error(e4.toString(), e4);
        }
        catch (IllegalAccessException e5) {
            AbstractStyleNameConverter.LOGGER.error(e5.toString(), e5);
        }
        catch (InvocationTargetException e6) {
            AbstractStyleNameConverter.LOGGER.error(e6.toString(), e6);
        }
        return null;
    }
    
    private static List<PatternFormatter> toPatternFormatterList(final Configuration config, final String[] options) {
        if (options.length == 0 || options[0] == null) {
            AbstractStyleNameConverter.LOGGER.error("No pattern supplied on style for config=" + config);
            return null;
        }
        final PatternParser parser = PatternLayout.createPatternParser(config);
        if (parser == null) {
            AbstractStyleNameConverter.LOGGER.error("No PatternParser created for config=" + config + ", options=" + Arrays.toString(options));
            return null;
        }
        return parser.parse(options[0]);
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        final StringBuilder buf = new StringBuilder();
        for (final PatternFormatter formatter : this.formatters) {
            formatter.format(event, buf);
        }
        if (buf.length() > 0) {
            toAppendTo.append(this.style).append(buf.toString()).append(AnsiEscape.getDefaultStyle());
        }
    }
    
    @Plugin(name = "black", category = "Converter")
    @ConverterKeys({ "black" })
    public static final class Black extends AbstractStyleNameConverter
    {
        protected static final String NAME = "black";
        
        public Black(final List<PatternFormatter> formatters, final String styling) {
            super("black", formatters, styling);
        }
        
        public static Black newInstance(final Configuration config, final String[] options) {
            return AbstractStyleNameConverter.newInstance(Black.class, "black", config, options);
        }
    }
    
    @Plugin(name = "blue", category = "Converter")
    @ConverterKeys({ "blue" })
    public static final class Blue extends AbstractStyleNameConverter
    {
        protected static final String NAME = "blue";
        
        public Blue(final List<PatternFormatter> formatters, final String styling) {
            super("blue", formatters, styling);
        }
        
        public static Blue newInstance(final Configuration config, final String[] options) {
            return AbstractStyleNameConverter.newInstance(Blue.class, "blue", config, options);
        }
    }
    
    @Plugin(name = "cyan", category = "Converter")
    @ConverterKeys({ "cyan" })
    public static final class Cyan extends AbstractStyleNameConverter
    {
        protected static final String NAME = "cyan";
        
        public Cyan(final List<PatternFormatter> formatters, final String styling) {
            super("cyan", formatters, styling);
        }
        
        public static Cyan newInstance(final Configuration config, final String[] options) {
            return AbstractStyleNameConverter.newInstance(Cyan.class, "cyan", config, options);
        }
    }
    
    @Plugin(name = "green", category = "Converter")
    @ConverterKeys({ "green" })
    public static final class Green extends AbstractStyleNameConverter
    {
        protected static final String NAME = "green";
        
        public Green(final List<PatternFormatter> formatters, final String styling) {
            super("green", formatters, styling);
        }
        
        public static Green newInstance(final Configuration config, final String[] options) {
            return AbstractStyleNameConverter.newInstance(Green.class, "green", config, options);
        }
    }
    
    @Plugin(name = "magenta", category = "Converter")
    @ConverterKeys({ "magenta" })
    public static final class Magenta extends AbstractStyleNameConverter
    {
        protected static final String NAME = "magenta";
        
        public Magenta(final List<PatternFormatter> formatters, final String styling) {
            super("magenta", formatters, styling);
        }
        
        public static Magenta newInstance(final Configuration config, final String[] options) {
            return AbstractStyleNameConverter.newInstance(Magenta.class, "magenta", config, options);
        }
    }
    
    @Plugin(name = "red", category = "Converter")
    @ConverterKeys({ "red" })
    public static final class Red extends AbstractStyleNameConverter
    {
        protected static final String NAME = "red";
        
        public Red(final List<PatternFormatter> formatters, final String styling) {
            super("red", formatters, styling);
        }
        
        public static Red newInstance(final Configuration config, final String[] options) {
            return AbstractStyleNameConverter.newInstance(Red.class, "red", config, options);
        }
    }
    
    @Plugin(name = "white", category = "Converter")
    @ConverterKeys({ "white" })
    public static final class White extends AbstractStyleNameConverter
    {
        protected static final String NAME = "white";
        
        public White(final List<PatternFormatter> formatters, final String styling) {
            super("white", formatters, styling);
        }
        
        public static White newInstance(final Configuration config, final String[] options) {
            return AbstractStyleNameConverter.newInstance(White.class, "white", config, options);
        }
    }
    
    @Plugin(name = "yellow", category = "Converter")
    @ConverterKeys({ "yellow" })
    public static final class Yellow extends AbstractStyleNameConverter
    {
        protected static final String NAME = "yellow";
        
        public Yellow(final List<PatternFormatter> formatters, final String styling) {
            super("yellow", formatters, styling);
        }
        
        public static Yellow newInstance(final Configuration config, final String[] options) {
            return AbstractStyleNameConverter.newInstance(Yellow.class, "yellow", config, options);
        }
    }
}
