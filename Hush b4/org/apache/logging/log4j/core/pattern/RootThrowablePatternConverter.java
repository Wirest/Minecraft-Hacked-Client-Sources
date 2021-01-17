// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.core.helpers.Constants;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "RootThrowablePatternConverter", category = "Converter")
@ConverterKeys({ "rEx", "rThrowable", "rException" })
public final class RootThrowablePatternConverter extends ThrowablePatternConverter
{
    private RootThrowablePatternConverter(final String[] options) {
        super("RootThrowable", "throwable", options);
    }
    
    public static RootThrowablePatternConverter newInstance(final String[] options) {
        return new RootThrowablePatternConverter(options);
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        ThrowableProxy proxy = null;
        if (event instanceof Log4jLogEvent) {
            proxy = ((Log4jLogEvent)event).getThrownProxy();
        }
        final Throwable throwable = event.getThrown();
        if (throwable != null && this.options.anyLines()) {
            if (proxy == null) {
                super.format(event, toAppendTo);
                return;
            }
            final String trace = proxy.getRootCauseStackTrace(this.options.getPackages());
            final int len = toAppendTo.length();
            if (len > 0 && !Character.isWhitespace(toAppendTo.charAt(len - 1))) {
                toAppendTo.append(" ");
            }
            if (!this.options.allLines() || !Constants.LINE_SEP.equals(this.options.getSeparator())) {
                final StringBuilder sb = new StringBuilder();
                final String[] array = trace.split(Constants.LINE_SEP);
                for (int limit = this.options.minLines(array.length) - 1, i = 0; i <= limit; ++i) {
                    sb.append(array[i]);
                    if (i < limit) {
                        sb.append(this.options.getSeparator());
                    }
                }
                toAppendTo.append(sb.toString());
            }
            else {
                toAppendTo.append(trace);
            }
        }
    }
}
