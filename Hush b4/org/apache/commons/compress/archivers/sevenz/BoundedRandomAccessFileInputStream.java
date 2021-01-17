// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.sevenz;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.InputStream;

class BoundedRandomAccessFileInputStream extends InputStream
{
    private final RandomAccessFile file;
    private long bytesRemaining;
    
    public BoundedRandomAccessFileInputStream(final RandomAccessFile file, final long size) {
        this.file = file;
        this.bytesRemaining = size;
    }
    
    @Override
    public int read() throws IOException {
        if (this.bytesRemaining > 0L) {
            --this.bytesRemaining;
            return this.file.read();
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
        final int bytesRead = this.file.read(b, off, bytesToRead);
        if (bytesRead >= 0) {
            this.bytesRemaining -= bytesRead;
        }
        return bytesRead;
    }
    
    @Override
    public void close() {
    }
}
