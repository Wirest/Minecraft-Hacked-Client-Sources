// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.utils;

import java.io.IOException;
import java.util.zip.Checksum;
import java.io.InputStream;

public class ChecksumVerifyingInputStream extends InputStream
{
    private final InputStream in;
    private long bytesRemaining;
    private final long expectedChecksum;
    private final Checksum checksum;
    
    public ChecksumVerifyingInputStream(final Checksum checksum, final InputStream in, final long size, final long expectedChecksum) {
        this.checksum = checksum;
        this.in = in;
        this.expectedChecksum = expectedChecksum;
        this.bytesRemaining = size;
    }
    
    @Override
    public int read() throws IOException {
        if (this.bytesRemaining <= 0L) {
            return -1;
        }
        final int ret = this.in.read();
        if (ret >= 0) {
            this.checksum.update(ret);
            --this.bytesRemaining;
        }
        if (this.bytesRemaining == 0L && this.expectedChecksum != this.checksum.getValue()) {
            throw new IOException("Checksum verification failed");
        }
        return ret;
    }
    
    @Override
    public int read(final byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        final int ret = this.in.read(b, off, len);
        if (ret >= 0) {
            this.checksum.update(b, off, ret);
            this.bytesRemaining -= ret;
        }
        if (this.bytesRemaining <= 0L && this.expectedChecksum != this.checksum.getValue()) {
            throw new IOException("Checksum verification failed");
        }
        return ret;
    }
    
    @Override
    public long skip(final long n) throws IOException {
        if (this.read() >= 0) {
            return 1L;
        }
        return 0L;
    }
    
    @Override
    public void close() throws IOException {
        this.in.close();
    }
}
