// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "ctx", category = "Lookup")
public class ContextMapLookup implements StrLookup
{
    @Override
    public String lookup(final String key) {
        return ThreadContext.get(key);
    }
    
    @Override
    public String lookup(final LogEvent event, final String key) {
        return event.getContextMap().get(key);
    }
}
