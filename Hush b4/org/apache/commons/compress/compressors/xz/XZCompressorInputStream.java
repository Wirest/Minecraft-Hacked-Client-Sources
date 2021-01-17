// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.compressors.xz;

import org.tukaani.xz.SingleXZInputStream;
import org.tukaani.xz.XZInputStream;
import java.io.IOException;
import org.tukaani.xz.XZ;
import java.io.InputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;

public class XZCompressorInputStream extends CompressorInputStream
{
    private final InputStream in;
    
    public static boolean matches(final byte[] signature, final int length) {
        if (length < XZ.HEADER_MAGIC.length) {
            return false;
        }
        for (int i = 0; i < XZ.HEADER_MAGIC.length; ++i) {
            if (signature[i] != XZ.HEADER_MAGIC[i]) {
                return false;
            }
        }
        return true;
    }
    
    public XZCompressorInputStream(final InputStream inputStream) throws IOException {
        this(inputStream, false);
    }
    
    public XZCompressorInputStream(final InputStream inputStream, final boolean decompressConcatenated) throws IOException {
        if (decompressConcatenated) {
            this.in = (InputStream)new XZInputStream(inputStream);
        }
        else {
            this.in = (InputStream)new SingleXZInputStream(inputStream);
        }
    }
    
    @Override
    public int read() throws IOException {
        final int ret = this.in.read();
        this.count((ret == -1) ? -1 : 1);
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
