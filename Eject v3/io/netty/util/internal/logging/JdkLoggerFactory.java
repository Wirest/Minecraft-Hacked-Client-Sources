package io.netty.util.internal.logging;

import java.util.logging.Logger;

public class JdkLoggerFactory
        extends InternalLoggerFactory {
    public InternalLogger newInstance(String paramString) {
        return new JdkLogger(Logger.getLogger(paramString));
    }
}




