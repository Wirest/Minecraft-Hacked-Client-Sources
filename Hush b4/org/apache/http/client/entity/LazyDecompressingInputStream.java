// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.entity;

import java.io.IOException;
import org.apache.http.annotation.NotThreadSafe;
import java.io.InputStream;

@NotThreadSafe
class LazyDecompressingInputStream extends InputStream
{
    private final InputStream wrappedStream;
    private final DecompressingEntity decompressingEntity;
    private InputStream wrapperStream;
    
    public LazyDecompressingInputStream(final InputStream wrappedStream, final DecompressingEntity decompressingEntity) {
        this.wrappedStream = wrappedStream;
        this.decompressingEntity = decompressingEntity;
    }
    
    private void initWrapper() throws IOException {
        if (this.wrapperStream == null) {
            this.wrapperStream = this.decompressingEntity.decorate(this.wrappedStream);
        }
    }
    
    @Override
    public int read() throws IOException {
        this.initWrapper();
        return this.wrapperStream.read();
    }
    
    @Override
    public int read(final byte[] b) throws IOException {
        this.initWrapper();
        return this.wrapperStream.read(b);
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        this.initWrapper();
        return this.wrapperStream.read(b, off, len);
    }
    
    @Override
    public long skip(final long n) throws IOException {
        this.initWrapper();
        return this.wrapperStream.skip(n);
    }
    
    @Override
    public boolean markSupported() {
        return false;
    }
    
    @Override
    public int available() throws IOException {
        this.initWrapper();
        return this.wrapperStream.available();
    }
    
    @Override
    public void close() throws IOException {
        try {
            if (this.wrapperStream != null) {
                this.wrapperStream.close();
            }
        }
        finally {
            this.wrappedStream.close();
        }
    }
}
