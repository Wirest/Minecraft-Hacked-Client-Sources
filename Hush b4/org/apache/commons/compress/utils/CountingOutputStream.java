// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.FilterOutputStream;

public class CountingOutputStream extends FilterOutputStream
{
    private long bytesWritten;
    
    public CountingOutputStream(final OutputStream out) {
        super(out);
        this.bytesWritten = 0L;
    }
    
    @Override
    public void write(final int b) throws IOException {
        this.out.write(b);
        this.count(1L);
    }
    
    @Override
    public void write(final byte[] b) throws IOException {
        this.write(b, 0, b.length);
    }
    
    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        this.out.write(b, off, len);
        this.count(len);
    }
    
    protected void count(final long written) {
        if (written != -1L) {
            this.bytesWritten += written;
        }
    }
    
    public long getBytesWritten() {
        return this.bytesWritten;
    }
}
