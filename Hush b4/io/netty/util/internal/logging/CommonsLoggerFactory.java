// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal.logging;

import org.apache.commons.logging.LogFactory;
import java.util.HashMap;
import java.util.Map;

public class CommonsLoggerFactory extends InternalLoggerFactory
{
    Map<String, InternalLogger> loggerMap;
    
    public CommonsLoggerFactory() {
        this.loggerMap = new HashMap<String, InternalLogger>();
    }
    
    public InternalLogger newInstance(final String name) {
        return new CommonsLogger(LogFactory.getLog(name), name);
    }
}
