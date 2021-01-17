// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.helpers.UUIDUtil;
import java.util.UUID;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "UUIDPatternConverter", category = "Converter")
@ConverterKeys({ "u", "uuid" })
public final class UUIDPatternConverter extends LogEventPatternConverter
{
    private final boolean isRandom;
    
    private UUIDPatternConverter(final boolean isRandom) {
        super("u", "uuid");
        this.isRandom = isRandom;
    }
    
    public static UUIDPatternConverter newInstance(final String[] options) {
        if (options.length == 0) {
            return new UUIDPatternConverter(false);
        }
        if (options.length > 1 || (!options[0].equalsIgnoreCase("RANDOM") && !options[0].equalsIgnoreCase("Time"))) {
            UUIDPatternConverter.LOGGER.error("UUID Pattern Converter only accepts a single option with the value \"RANDOM\" or \"TIME\"");
        }
        return new UUIDPatternConverter(options[0].equalsIgnoreCase("RANDOM"));
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        final UUID uuid = this.isRandom ? UUID.randomUUID() : UUIDUtil.getTimeBasedUUID();
        toAppendTo.append(uuid.toString());
    }
}
