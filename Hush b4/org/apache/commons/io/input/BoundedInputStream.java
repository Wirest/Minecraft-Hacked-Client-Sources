// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class BoundedInputStream extends InputStream
{
    private final InputStream in;
    private final long max;
    private long pos;
    private long mark;
    private boolean propagateClose;
    
    public BoundedInputStream(final InputStream in, final long size) {
        this.pos = 0L;
        this.mark = -1L;
        this.propagateClose = true;
        this.max = size;
        this.in = in;
    }
    
    public BoundedInputStream(final InputStream in) {
        this(in, -1L);
    }
    
    @Override
    public int read() throws IOException {
        if (this.max >= 0L && this.pos >= this.max) {
            return -1;
        }
        final int result = this.in.read();
        ++this.pos;
        return result;
    }
    
    @Override
    public int read(final byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        if (this.max >= 0L && this.pos >= this.max) {
            return -1;
        }
        final long maxRead = (this.max >= 0L) ? Math.min(len, this.max - this.pos) : len;
        final int bytesRead = this.in.read(b, off, (int)maxRead);
        if (bytesRead == -1) {
            return -1;
        }
        this.pos += bytesRead;
        return bytesRead;
    }
    
    @Override
    public long skip(final long n) throws IOException {
        final long toSkip = (this.max >= 0L) ? Math.min(n, this.max - this.pos) : n;
        final long skippedBytes = this.in.skip(toSkip);
        this.pos += skippedBytes;
        return skippedBytes;
    }
    
    @Override
    public int available() throws IOException {
        if (this.max >= 0L && this.pos >= this.max) {
            return 0;
        }
        return this.in.available();
    }
    
    @Override
    public String toString() {
        return this.in.toString();
    }
    
    @Override
    public void close() throws IOException {
        if (this.propagateClose) {
            this.in.close();
        }
    }
    
    @Override
    public synchronized void reset() throws IOException {
        this.in.reset();
        this.pos = this.mark;
    }
    
    @Override
    public synchronized void mark(final int readlimit) {
        this.in.mark(readlimit);
        this.mark = this.pos;
    }
    
    @Override
    public boolean markSupported() {
        return this.in.markSupported();
    }
    
    public boolean isPropagateClose() {
        return this.propagateClose;
    }
    
    public void setPropagateClose(final boolean propagateClose) {
        this.propagateClose = propagateClose;
    }
}
