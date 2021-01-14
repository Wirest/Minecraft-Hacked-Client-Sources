package io.netty.util.internal.logging;

import org.apache.log4j.Logger;

public class Log4JLoggerFactory
        extends InternalLoggerFactory {
    public InternalLogger newInstance(String paramString) {
        return new Log4JLogger(Logger.getLogger(paramString));
    }
}




