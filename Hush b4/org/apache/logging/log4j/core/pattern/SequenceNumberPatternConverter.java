// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "SequenceNumberPatternConverter", category = "Converter")
@ConverterKeys({ "sn", "sequenceNumber" })
public final class SequenceNumberPatternConverter extends LogEventPatternConverter
{
    private static final AtomicLong SEQUENCE;
    private static final SequenceNumberPatternConverter INSTANCE;
    
    private SequenceNumberPatternConverter() {
        super("Sequence Number", "sn");
    }
    
    public static SequenceNumberPatternConverter newInstance(final String[] options) {
        return SequenceNumberPatternConverter.INSTANCE;
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        toAppendTo.append(Long.toString(SequenceNumberPatternConverter.SEQUENCE.incrementAndGet()));
    }
    
    static {
        SEQUENCE = new AtomicLong();
        INSTANCE = new SequenceNumberPatternConverter();
    }
}
