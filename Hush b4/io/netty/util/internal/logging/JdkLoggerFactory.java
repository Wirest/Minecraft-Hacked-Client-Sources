// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal.logging;

import java.util.logging.Logger;

public class JdkLoggerFactory extends InternalLoggerFactory
{
    public InternalLogger newInstance(final String name) {
        return new JdkLogger(Logger.getLogger(name));
    }
}
