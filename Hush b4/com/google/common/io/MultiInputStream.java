// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import javax.annotation.Nullable;
import java.io.IOException;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.io.InputStream;

final class MultiInputStream extends InputStream
{
    private Iterator<? extends ByteSource> it;
    private InputStream in;
    
    public MultiInputStream(final Iterator<? extends ByteSource> it) throws IOException {
        this.it = Preconditions.checkNotNull(it);
        this.advance();
    }
    
    @Override
    public void close() throws IOException {
        if (this.in != null) {
            try {
                this.in.close();
            }
            finally {
                this.in = null;
            }
        }
    }
    
    private void advance() throws IOException {
        this.close();
        if (this.it.hasNext()) {
            this.in = ((ByteSource)this.it.next()).openStream();
        }
    }
    
    @Override
    public int available() throws IOException {
        if (this.in == null) {
            return 0;
        }
        return this.in.available();
    }
    
    @Override
    public boolean markSupported() {
        return false;
    }
    
    @Override
    public int read() throws IOException {
        if (this.in == null) {
            return -1;
        }
        final int result = this.in.read();
        if (result == -1) {
            this.advance();
            return this.read();
        }
        return result;
    }
    
    @Override
    public int read(@Nullable final byte[] b, final int off, final int len) throws IOException {
        if (this.in == null) {
            return -1;
        }
        final int result = this.in.read(b, off, len);
        if (result == -1) {
            this.advance();
            return this.read(b, off, len);
        }
        return result;
    }
    
    @Override
    public long skip(final long n) throws IOException {
        if (this.in == null || n <= 0L) {
            return 0L;
        }
        final long result = this.in.skip(n);
        if (result != 0L) {
            return result;
        }
        if (this.read() == -1) {
            return 0L;
        }
        return 1L + this.in.skip(n - 1L);
    }
}
