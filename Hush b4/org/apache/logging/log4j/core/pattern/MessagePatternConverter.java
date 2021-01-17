// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MultiformatMessage;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "MessagePatternConverter", category = "Converter")
@ConverterKeys({ "m", "msg", "message" })
public final class MessagePatternConverter extends LogEventPatternConverter
{
    private final String[] formats;
    private final Configuration config;
    
    private MessagePatternConverter(final Configuration config, final String[] options) {
        super("Message", "message");
        this.formats = options;
        this.config = config;
    }
    
    public static MessagePatternConverter newInstance(final Configuration config, final String[] options) {
        return new MessagePatternConverter(config, options);
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        final Message msg = event.getMessage();
        if (msg != null) {
            String result;
            if (msg instanceof MultiformatMessage) {
                result = ((MultiformatMessage)msg).getFormattedMessage(this.formats);
            }
            else {
                result = msg.getFormattedMessage();
            }
            if (result != null) {
                toAppendTo.append((this.config != null && result.contains("${")) ? this.config.getStrSubstitutor().replace(event, result) : result);
            }
            else {
                toAppendTo.append("null");
            }
        }
    }
}
