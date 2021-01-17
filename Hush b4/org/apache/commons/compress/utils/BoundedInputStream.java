// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.utils;

import java.io.IOException;
import java.io.InputStream;

public class BoundedInputStream extends InputStream
{
    private final InputStream in;
    private long bytesRemaining;
    
    public BoundedInputStream(final InputStream in, final long size) {
        this.in = in;
        this.bytesRemaining = size;
    }
    
    @Override
    public int read() throws IOException {
        if (this.bytesRemaining > 0L) {
            --this.bytesRemaining;
            return this.in.read();
        }
        return -1;
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        if (this.bytesRemaining == 0L) {
            return -1;
        }
        int bytesToRead = len;
        if (bytesToRead > this.bytesRemaining) {
            bytesToRead = (int)this.bytesRemaining;
        }
        final int bytesRead = this.in.read(b, off, bytesToRead);
        if (bytesRead >= 0) {
            this.bytesRemaining -= bytesRead;
        }
        return bytesRead;
    }
    
    @Override
    public void close() {
    }
}
