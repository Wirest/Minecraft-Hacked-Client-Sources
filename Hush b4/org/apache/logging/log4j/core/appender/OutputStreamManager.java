// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import java.io.IOException;
import org.apache.logging.log4j.core.Layout;
import java.io.OutputStream;

public class OutputStreamManager extends AbstractManager
{
    private volatile OutputStream os;
    private final byte[] footer;
    private final byte[] header;
    
    protected OutputStreamManager(final OutputStream os, final String streamName, final Layout<?> layout) {
        super(streamName);
        this.os = os;
        if (layout != null) {
            this.footer = layout.getFooter();
            this.header = layout.getHeader();
            if (this.header != null) {
                try {
                    this.os.write(this.header, 0, this.header.length);
                }
                catch (IOException ioe) {
                    OutputStreamManager.LOGGER.error("Unable to write header", ioe);
                }
            }
        }
        else {
            this.footer = null;
            this.header = null;
        }
    }
    
    public static <T> OutputStreamManager getManager(final String name, final T data, final ManagerFactory<? extends OutputStreamManager, T> factory) {
        return AbstractManager.getManager(name, factory, data);
    }
    
    public void releaseSub() {
        if (this.footer != null) {
            this.write(this.footer);
        }
        this.close();
    }
    
    public boolean isOpen() {
        return this.getCount() > 0;
    }
    
    protected OutputStream getOutputStream() {
        return this.os;
    }
    
    protected void setOutputStream(final OutputStream os) {
        if (this.header != null) {
            try {
                os.write(this.header, 0, this.header.length);
                this.os = os;
            }
            catch (IOException ioe) {
                OutputStreamManager.LOGGER.error("Unable to write header", ioe);
            }
        }
        else {
            this.os = os;
        }
    }
    
    protected synchronized void write(final byte[] bytes, final int offset, final int length) {
        try {
            this.os.write(bytes, offset, length);
        }
        catch (IOException ex) {
            final String msg = "Error writing to stream " + this.getName();
            throw new AppenderLoggingException(msg, ex);
        }
    }
    
    protected void write(final byte[] bytes) {
        this.write(bytes, 0, bytes.length);
    }
    
    protected synchronized void close() {
        final OutputStream stream = this.os;
        if (stream == System.out || stream == System.err) {
            return;
        }
        try {
            stream.close();
        }
        catch (IOException ex) {
            OutputStreamManager.LOGGER.error("Unable to close stream " + this.getName() + ". " + ex);
        }
    }
    
    public synchronized void flush() {
        try {
            this.os.flush();
        }
        catch (IOException ex) {
            final String msg = "Error flushing stream " + this.getName();
            throw new AppenderLoggingException(msg, ex);
        }
    }
}
