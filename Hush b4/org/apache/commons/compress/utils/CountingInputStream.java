// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.FilterInputStream;

public class CountingInputStream extends FilterInputStream
{
    private long bytesRead;
    
    public CountingInputStream(final InputStream in) {
        super(in);
    }
    
    @Override
    public int read() throws IOException {
        final int r = this.in.read();
        if (r >= 0) {
            this.count(1L);
        }
        return r;
    }
    
    @Override
    public int read(final byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        final int r = this.in.read(b, off, len);
        if (r >= 0) {
            this.count(r);
        }
        return r;
    }
    
    protected final void count(final long read) {
        if (read != -1L) {
            this.bytesRead += read;
        }
    }
    
    public long getBytesRead() {
        return this.bytesRead;
    }
}
