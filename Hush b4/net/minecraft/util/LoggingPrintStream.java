// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import java.io.OutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.PrintStream;

public class LoggingPrintStream extends PrintStream
{
    private static final Logger LOGGER;
    private final String domain;
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public LoggingPrintStream(final String domainIn, final OutputStream outStream) {
        super(outStream);
        this.domain = domainIn;
    }
    
    @Override
    public void println(final String p_println_1_) {
        this.logString(p_println_1_);
    }
    
    @Override
    public void println(final Object p_println_1_) {
        this.logString(String.valueOf(p_println_1_));
    }
    
    private void logString(final String string) {
        final StackTraceElement[] astacktraceelement = Thread.currentThread().getStackTrace();
        final StackTraceElement stacktraceelement = astacktraceelement[Math.min(3, astacktraceelement.length)];
        LoggingPrintStream.LOGGER.info("[{}]@.({}:{}): {}", this.domain, stacktraceelement.getFileName(), stacktraceelement.getLineNumber(), string);
    }
}
