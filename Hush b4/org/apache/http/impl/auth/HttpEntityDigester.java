// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.auth;

import java.io.IOException;
import java.security.MessageDigest;
import java.io.OutputStream;

class HttpEntityDigester extends OutputStream
{
    private final MessageDigest digester;
    private boolean closed;
    private byte[] digest;
    
    HttpEntityDigester(final MessageDigest digester) {
        (this.digester = digester).reset();
    }
    
    @Override
    public void write(final int b) throws IOException {
        if (this.closed) {
            throw new IOException("Stream has been already closed");
        }
        this.digester.update((byte)b);
    }
    
    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        if (this.closed) {
            throw new IOException("Stream has been already closed");
        }
        this.digester.update(b, off, len);
    }
    
    @Override
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        this.closed = true;
        this.digest = this.digester.digest();
        super.close();
    }
    
    public byte[] getDigest() {
        return this.digest;
    }
}
