// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.entity;

import java.util.zip.InflaterInputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.io.InputStream;

public class DeflateInputStream extends InputStream
{
    private InputStream sourceStream;
    
    public DeflateInputStream(final InputStream wrapped) throws IOException {
        final byte[] peeked = new byte[6];
        final PushbackInputStream pushback = new PushbackInputStream(wrapped, peeked.length);
        final int headerLength = pushback.read(peeked);
        if (headerLength == -1) {
            throw new IOException("Unable to read the response");
        }
        final byte[] dummy = { 0 };
        final Inflater inf = new Inflater();
        try {
            int n;
            while ((n = inf.inflate(dummy)) == 0) {
                if (inf.finished()) {
                    throw new IOException("Unable to read the response");
                }
                if (inf.needsDictionary()) {
                    break;
                }
                if (!inf.needsInput()) {
                    continue;
                }
                inf.setInput(peeked);
            }
            if (n == -1) {
                throw new IOException("Unable to read the response");
            }
            pushback.unread(peeked, 0, headerLength);
            this.sourceStream = new DeflateStream(pushback, new Inflater());
        }
        catch (DataFormatException e) {
            pushback.unread(peeked, 0, headerLength);
            this.sourceStream = new DeflateStream(pushback, new Inflater(true));
        }
        finally {
            inf.end();
        }
    }
    
    @Override
    public int read() throws IOException {
        return this.sourceStream.read();
    }
    
    @Override
    public int read(final byte[] b) throws IOException {
        return this.sourceStream.read(b);
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        return this.sourceStream.read(b, off, len);
    }
    
    @Override
    public long skip(final long n) throws IOException {
        return this.sourceStream.skip(n);
    }
    
    @Override
    public int available() throws IOException {
        return this.sourceStream.available();
    }
    
    @Override
    public void mark(final int readLimit) {
        this.sourceStream.mark(readLimit);
    }
    
    @Override
    public void reset() throws IOException {
        this.sourceStream.reset();
    }
    
    @Override
    public boolean markSupported() {
        return this.sourceStream.markSupported();
    }
    
    @Override
    public void close() throws IOException {
        this.sourceStream.close();
    }
    
    static class DeflateStream extends InflaterInputStream
    {
        private boolean closed;
        
        public DeflateStream(final InputStream in, final Inflater inflater) {
            super(in, inflater);
            this.closed = false;
        }
        
        @Override
        public void close() throws IOException {
            if (this.closed) {
                return;
            }
            this.closed = true;
            this.inf.end();
            super.close();
        }
    }
}
