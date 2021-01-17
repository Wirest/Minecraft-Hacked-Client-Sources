// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.logging.impl;

import java.util.StringTokenizer;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.LogRecord;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.Serializable;
import org.apache.commons.logging.Log;

public class Jdk13LumberjackLogger implements Log, Serializable
{
    private static final long serialVersionUID = -8649807923527610591L;
    protected transient Logger logger;
    protected String name;
    private String sourceClassName;
    private String sourceMethodName;
    private boolean classAndMethodFound;
    protected static final Level dummyLevel;
    
    public Jdk13LumberjackLogger(final String name) {
        this.logger = null;
        this.name = null;
        this.sourceClassName = "unknown";
        this.sourceMethodName = "unknown";
        this.classAndMethodFound = false;
        this.name = name;
        this.logger = this.getLogger();
    }
    
    private void log(final Level level, final String msg, final Throwable ex) {
        if (this.getLogger().isLoggable(level)) {
            final LogRecord record = new LogRecord(level, msg);
            if (!this.classAndMethodFound) {
                this.getClassAndMethod();
            }
            record.setSourceClassName(this.sourceClassName);
            record.setSourceMethodName(this.sourceMethodName);
            if (ex != null) {
                record.setThrown(ex);
            }
            this.getLogger().log(record);
        }
    }
    
    private void getClassAndMethod() {
        try {
            final Throwable throwable = new Throwable();
            throwable.fillInStackTrace();
            final StringWriter stringWriter = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(stringWriter);
            throwable.printStackTrace(printWriter);
            final String traceString = stringWriter.getBuffer().toString();
            final StringTokenizer tokenizer = new StringTokenizer(traceString, "\n");
            tokenizer.nextToken();
            String line;
            for (line = tokenizer.nextToken(); line.indexOf(this.getClass().getName()) == -1; line = tokenizer.nextToken()) {}
            while (line.indexOf(this.getClass().getName()) >= 0) {
                line = tokenizer.nextToken();
            }
            final int start = line.indexOf("at ") + 3;
            final int end = line.indexOf(40);
            final String temp = line.substring(start, end);
            final int lastPeriod = temp.lastIndexOf(46);
            this.sourceClassName = temp.substring(0, lastPeriod);
            this.sourceMethodName = temp.substring(lastPeriod + 1);
        }
        catch (Exception ex) {}
        this.classAndMethodFound = true;
    }
    
    public void debug(final Object message) {
        this.log(Level.FINE, String.valueOf(message), null);
    }
    
    public void debug(final Object message, final Throwable exception) {
        this.log(Level.FINE, String.valueOf(message), exception);
    }
    
    public void error(final Object message) {
        this.log(Level.SEVERE, String.valueOf(message), null);
    }
    
    public void error(final Object message, final Throwable exception) {
        this.log(Level.SEVERE, String.valueOf(message), exception);
    }
    
    public void fatal(final Object message) {
        this.log(Level.SEVERE, String.valueOf(message), null);
    }
    
    public void fatal(final Object message, final Throwable exception) {
        this.log(Level.SEVERE, String.valueOf(message), exception);
    }
    
    public Logger getLogger() {
        if (this.logger == null) {
            this.logger = Logger.getLogger(this.name);
        }
        return this.logger;
    }
    
    public void info(final Object message) {
        this.log(Level.INFO, String.valueOf(message), null);
    }
    
    public void info(final Object message, final Throwable exception) {
        this.log(Level.INFO, String.valueOf(message), exception);
    }
    
    public boolean isDebugEnabled() {
        return this.getLogger().isLoggable(Level.FINE);
    }
    
    public boolean isErrorEnabled() {
        return this.getLogger().isLoggable(Level.SEVERE);
    }
    
    public boolean isFatalEnabled() {
        return this.getLogger().isLoggable(Level.SEVERE);
    }
    
    public boolean isInfoEnabled() {
        return this.getLogger().isLoggable(Level.INFO);
    }
    
    public boolean isTraceEnabled() {
        return this.getLogger().isLoggable(Level.FINEST);
    }
    
    public boolean isWarnEnabled() {
        return this.getLogger().isLoggable(Level.WARNING);
    }
    
    public void trace(final Object message) {
        this.log(Level.FINEST, String.valueOf(message), null);
    }
    
    public void trace(final Object message, final Throwable exception) {
        this.log(Level.FINEST, String.valueOf(message), exception);
    }
    
    public void warn(final Object message) {
        this.log(Level.WARNING, String.valueOf(message), null);
    }
    
    public void warn(final Object message, final Throwable exception) {
        this.log(Level.WARNING, String.valueOf(message), exception);
    }
    
    static {
        dummyLevel = Level.FINE;
    }
}
