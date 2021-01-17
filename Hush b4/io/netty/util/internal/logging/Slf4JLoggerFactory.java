// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal.logging;

import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLoggerFactory;
import java.io.UnsupportedEncodingException;
import java.io.PrintStream;
import java.io.OutputStream;

public class Slf4JLoggerFactory extends InternalLoggerFactory
{
    public Slf4JLoggerFactory() {
    }
    
    Slf4JLoggerFactory(final boolean failIfNOP) {
        assert failIfNOP;
        final StringBuffer buf = new StringBuffer();
        final PrintStream err = System.err;
        try {
            System.setErr(new PrintStream(new OutputStream() {
                @Override
                public void write(final int b) {
                    buf.append((char)b);
                }
            }, true, "US-ASCII"));
        }
        catch (UnsupportedEncodingException e) {
            throw new Error(e);
        }
        try {
            if (LoggerFactory.getILoggerFactory() instanceof NOPLoggerFactory) {
                throw new NoClassDefFoundError(buf.toString());
            }
            err.print(buf);
            err.flush();
        }
        finally {
            System.setErr(err);
        }
    }
    
    public InternalLogger newInstance(final String name) {
        return new Slf4JLogger(LoggerFactory.getLogger(name));
    }
}
