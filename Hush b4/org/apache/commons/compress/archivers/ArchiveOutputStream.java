// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public abstract class ArchiveOutputStream extends OutputStream
{
    private final byte[] oneByte;
    static final int BYTE_MASK = 255;
    private long bytesWritten;
    
    public ArchiveOutputStream() {
        this.oneByte = new byte[1];
        this.bytesWritten = 0L;
    }
    
    public abstract void putArchiveEntry(final ArchiveEntry p0) throws IOException;
    
    public abstract void closeArchiveEntry() throws IOException;
    
    public abstract void finish() throws IOException;
    
    public abstract ArchiveEntry createArchiveEntry(final File p0, final String p1) throws IOException;
    
    @Override
    public void write(final int b) throws IOException {
        this.oneByte[0] = (byte)(b & 0xFF);
        this.write(this.oneByte, 0, 1);
    }
    
    protected void count(final int written) {
        this.count((long)written);
    }
    
    protected void count(final long written) {
        if (written != -1L) {
            this.bytesWritten += written;
        }
    }
    
    @Deprecated
    public int getCount() {
        return (int)this.bytesWritten;
    }
    
    public long getBytesWritten() {
        return this.bytesWritten;
    }
    
    public boolean canWriteEntryData(final ArchiveEntry archiveEntry) {
        return true;
    }
}
