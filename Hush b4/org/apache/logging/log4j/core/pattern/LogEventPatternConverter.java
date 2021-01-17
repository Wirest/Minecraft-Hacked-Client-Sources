// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;

public abstract class LogEventPatternConverter extends AbstractPatternConverter
{
    protected LogEventPatternConverter(final String name, final String style) {
        super(name, style);
    }
    
    public abstract void format(final LogEvent p0, final StringBuilder p1);
    
    @Override
    public void format(final Object obj, final StringBuilder output) {
        if (obj instanceof LogEvent) {
            this.format((LogEvent)obj, output);
        }
    }
    
    public boolean handlesThrowable() {
        return false;
    }
}
