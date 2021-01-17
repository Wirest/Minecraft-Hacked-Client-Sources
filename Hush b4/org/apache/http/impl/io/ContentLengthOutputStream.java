// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.io;

import java.io.IOException;
import org.apache.http.util.Args;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.annotation.NotThreadSafe;
import java.io.OutputStream;

@NotThreadSafe
public class ContentLengthOutputStream extends OutputStream
{
    private final SessionOutputBuffer out;
    private final long contentLength;
    private long total;
    private boolean closed;
    
    public ContentLengthOutputStream(final SessionOutputBuffer out, final long contentLength) {
        this.total = 0L;
        this.closed = false;
        this.out = Args.notNull(out, "Session output buffer");
        this.contentLength = Args.notNegative(contentLength, "Content length");
    }
    
    @Override
    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            this.out.flush();
        }
    }
    
    @Override
    public void flush() throws IOException {
        this.out.flush();
    }
    
    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        if (this.closed) {
            throw new IOException("Attempted write to closed stream.");
        }
        if (this.total < this.contentLength) {
            final long max = this.contentLength - this.total;
            int chunk = len;
            if (chunk > max) {
                chunk = (int)max;
            }
            this.out.write(b, off, chunk);
            this.total += chunk;
        }
    }
    
    @Override
    public void write(final byte[] b) throws IOException {
        this.write(b, 0, b.length);
    }
    
    @Override
    public void write(final int b) throws IOException {
        if (this.closed) {
            throw new IOException("Attempted write to closed stream.");
        }
        if (this.total < this.contentLength) {
            this.out.write(b);
            ++this.total;
        }
    }
}
