// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;

public final class LiteralPatternConverter extends LogEventPatternConverter implements ArrayPatternConverter
{
    private final String literal;
    private final Configuration config;
    private final boolean substitute;
    
    public LiteralPatternConverter(final Configuration config, final String literal) {
        super("Literal", "literal");
        this.literal = literal;
        this.config = config;
        this.substitute = (config != null && literal.contains("${"));
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        toAppendTo.append(this.substitute ? this.config.getStrSubstitutor().replace(event, this.literal) : this.literal);
    }
    
    @Override
    public void format(final Object obj, final StringBuilder output) {
        output.append(this.substitute ? this.config.getStrSubstitutor().replace(this.literal) : this.literal);
    }
    
    @Override
    public void format(final StringBuilder output, final Object... objects) {
        output.append(this.substitute ? this.config.getStrSubstitutor().replace(this.literal) : this.literal);
    }
    
    public String getLiteral() {
        return this.literal;
    }
}
