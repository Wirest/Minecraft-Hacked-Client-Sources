// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;

public class PatternFormatter
{
    private final LogEventPatternConverter converter;
    private final FormattingInfo field;
    
    public PatternFormatter(final LogEventPatternConverter converter, final FormattingInfo field) {
        this.converter = converter;
        this.field = field;
    }
    
    public void format(final LogEvent event, final StringBuilder buf) {
        final int startField = buf.length();
        this.converter.format(event, buf);
        this.field.format(startField, buf);
    }
    
    public LogEventPatternConverter getConverter() {
        return this.converter;
    }
    
    public FormattingInfo getFormattingInfo() {
        return this.field;
    }
    
    public boolean handlesThrowable() {
        return this.converter.handlesThrowable();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("[converter=");
        sb.append(this.converter);
        sb.append(", field=");
        sb.append(this.field);
        sb.append("]");
        return sb.toString();
    }
}
