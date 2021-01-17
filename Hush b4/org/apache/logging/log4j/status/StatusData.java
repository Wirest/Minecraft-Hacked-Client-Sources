// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.status;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Level;
import java.io.Serializable;

public class StatusData implements Serializable
{
    private static final long serialVersionUID = -4341916115118014017L;
    private final long timestamp;
    private final StackTraceElement caller;
    private final Level level;
    private final Message msg;
    private final Throwable throwable;
    
    public StatusData(final StackTraceElement caller, final Level level, final Message msg, final Throwable t) {
        this.timestamp = System.currentTimeMillis();
        this.caller = caller;
        this.level = level;
        this.msg = msg;
        this.throwable = t;
    }
    
    public long getTimestamp() {
        return this.timestamp;
    }
    
    public StackTraceElement getStackTraceElement() {
        return this.caller;
    }
    
    public Level getLevel() {
        return this.level;
    }
    
    public Message getMessage() {
        return this.msg;
    }
    
    public Throwable getThrowable() {
        return this.throwable;
    }
    
    public String getFormattedStatus() {
        final StringBuilder sb = new StringBuilder();
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        sb.append(format.format(new Date(this.timestamp)));
        sb.append(" ");
        sb.append(this.level.toString());
        sb.append(" ");
        sb.append(this.msg.getFormattedMessage());
        final Object[] params = this.msg.getParameters();
        Throwable t;
        if (this.throwable == null && params != null && params[params.length - 1] instanceof Throwable) {
            t = (Throwable)params[params.length - 1];
        }
        else {
            t = this.throwable;
        }
        if (t != null) {
            sb.append(" ");
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            t.printStackTrace(new PrintStream(baos));
            sb.append(baos.toString());
        }
        return sb.toString();
    }
}
