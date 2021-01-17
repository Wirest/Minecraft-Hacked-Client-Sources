// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.compressors.lzma;

import java.io.IOException;
import org.tukaani.xz.LZMAInputStream;
import java.io.InputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;

public class LZMACompressorInputStream extends CompressorInputStream
{
    private final InputStream in;
    
    public LZMACompressorInputStream(final InputStream inputStream) throws IOException {
        this.in = (InputStream)new LZMAInputStream(inputStream);
    }
    
    @Override
    public int read() throws IOException {
        final int ret = this.in.read();
        this.count((ret != -1) ? 1 : 0);
        return ret;
    }
    
    @Override
    public int read(final byte[] buf, final int off, final int len) throws IOException {
        final int ret = this.in.read(buf, off, len);
        this.count(ret);
        return ret;
    }
    
    @Override
    public long skip(final long n) throws IOException {
        return this.in.skip(n);
    }
    
    @Override
    public int available() throws IOException {
        return this.in.available();
    }
    
    @Override
    public void close() throws IOException {
        this.in.close();
    }
}
