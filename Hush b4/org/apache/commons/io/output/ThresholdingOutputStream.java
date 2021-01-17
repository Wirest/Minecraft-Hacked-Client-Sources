// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

public abstract class ThresholdingOutputStream extends OutputStream
{
    private final int threshold;
    private long written;
    private boolean thresholdExceeded;
    
    public ThresholdingOutputStream(final int threshold) {
        this.threshold = threshold;
    }
    
    @Override
    public void write(final int b) throws IOException {
        this.checkThreshold(1);
        this.getStream().write(b);
        ++this.written;
    }
    
    @Override
    public void write(final byte[] b) throws IOException {
        this.checkThreshold(b.length);
        this.getStream().write(b);
        this.written += b.length;
    }
    
    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        this.checkThreshold(len);
        this.getStream().write(b, off, len);
        this.written += len;
    }
    
    @Override
    public void flush() throws IOException {
        this.getStream().flush();
    }
    
    @Override
    public void close() throws IOException {
        try {
            this.flush();
        }
        catch (IOException ex) {}
        this.getStream().close();
    }
    
    public int getThreshold() {
        return this.threshold;
    }
    
    public long getByteCount() {
        return this.written;
    }
    
    public boolean isThresholdExceeded() {
        return this.written > this.threshold;
    }
    
    protected void checkThreshold(final int count) throws IOException {
        if (!this.thresholdExceeded && this.written + count > this.threshold) {
            this.thresholdExceeded = true;
            this.thresholdReached();
        }
    }
    
    protected void resetByteCount() {
        this.thresholdExceeded = false;
        this.written = 0L;
    }
    
    protected abstract OutputStream getStream() throws IOException;
    
    protected abstract void thresholdReached() throws IOException;
}
