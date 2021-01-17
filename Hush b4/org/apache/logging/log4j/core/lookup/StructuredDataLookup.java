// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.message.StructuredDataMessage;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "sd", category = "Lookup")
public class StructuredDataLookup implements StrLookup
{
    @Override
    public String lookup(final String key) {
        return null;
    }
    
    @Override
    public String lookup(final LogEvent event, final String key) {
        if (event == null || !(event.getMessage() instanceof StructuredDataMessage)) {
            return null;
        }
        final StructuredDataMessage msg = (StructuredDataMessage)event.getMessage();
        if (key.equalsIgnoreCase("id")) {
            return msg.getId().getName();
        }
        if (key.equalsIgnoreCase("type")) {
            return msg.getType();
        }
        return msg.get(key);
    }
}
