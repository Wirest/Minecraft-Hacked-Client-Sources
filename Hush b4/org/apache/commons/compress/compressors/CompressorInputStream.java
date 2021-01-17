// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.compressors;

import java.io.InputStream;

public abstract class CompressorInputStream extends InputStream
{
    private long bytesRead;
    
    public CompressorInputStream() {
        this.bytesRead = 0L;
    }
    
    protected void count(final int read) {
        this.count((long)read);
    }
    
    protected void count(final long read) {
        if (read != -1L) {
            this.bytesRead += read;
        }
    }
    
    protected void pushedBackBytes(final long pushedBack) {
        this.bytesRead -= pushedBack;
    }
    
    @Deprecated
    public int getCount() {
        return (int)this.bytesRead;
    }
    
    public long getBytesRead() {
        return this.bytesRead;
    }
}
