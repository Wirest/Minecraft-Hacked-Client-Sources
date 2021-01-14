package io.netty.util.internal.logging;

import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLoggerFactory;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class Slf4JLoggerFactory
        extends InternalLoggerFactory {
    public Slf4JLoggerFactory() {
    }

    Slf4JLoggerFactory(boolean paramBoolean) {
        assert (paramBoolean);
        final StringBuffer localStringBuffer = new StringBuffer();
        PrintStream localPrintStream = System.err;
        try {
            System.setErr(new PrintStream(new OutputStream() {
                public void write(int paramAnonymousInt) {
                    localStringBuffer.append((char) paramAnonymousInt);
                }
            }, true, "US-ASCII"));
        } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
            throw new Error(localUnsupportedEncodingException);
        }
        try {
            if ((LoggerFactory.getILoggerFactory() instanceof NOPLoggerFactory)) {
                throw new NoClassDefFoundError(localStringBuffer.toString());
            }
            localPrintStream.print(localStringBuffer);
            localPrintStream.flush();
        } finally {
            System.setErr(localPrintStream);
        }
    }

    public InternalLogger newInstance(String paramString) {
        return new Slf4JLogger(LoggerFactory.getLogger(paramString));
    }
}




