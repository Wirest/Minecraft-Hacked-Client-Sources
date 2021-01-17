// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.Logger;

public abstract class AbstractRolloverStrategy implements RolloverStrategy
{
    protected static final Logger LOGGER;
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
