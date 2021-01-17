// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.io.FilterInputStream;

public abstract class ProxyInputStream extends FilterInputStream
{
    public ProxyInputStream(final InputStream proxy) {
        super(proxy);
    }
    
    @Override
    public int read() throws IOException {
        try {
            this.beforeRead(1);
            final int b = this.in.read();
            this.afterRead((b != -1) ? 1 : -1);
            return b;
        }
        catch (IOException e) {
            this.handleIOException(e);
            return -1;
        }
    }
    
    @Override
    public int read(final byte[] bts) throws IOException {
        try {
            this.beforeRead((bts != null) ? bts.length : 0);
            final int n = this.in.read(bts);
            this.afterRead(n);
            return n;
        }
        catch (IOException e) {
            this.handleIOException(e);
            return -1;
        }
    }
    
    @Override
    public int read(final byte[] bts, final int off, final int len) throws IOException {
        try {
            this.beforeRead(len);
            final int n = this.in.read(bts, off, len);
            this.afterRead(n);
            return n;
        }
        catch (IOException e) {
            this.handleIOException(e);
            return -1;
        }
    }
    
    @Override
    public long skip(final long ln) throws IOException {
        try {
            return this.in.skip(ln);
        }
        catch (IOException e) {
            this.handleIOException(e);
            return 0L;
        }
    }
    
    @Override
    public int available() throws IOException {
        try {
            return super.available();
        }
        catch (IOException e) {
            this.handleIOException(e);
            return 0;
        }
    }
    
    @Override
    public void close() throws IOException {
        try {
            this.in.close();
        }
        catch (IOException e) {
            this.handleIOException(e);
        }
    }
    
    @Override
    public synchronized void mark(final int readlimit) {
        this.in.mark(readlimit);
    }
    
    @Override
    public synchronized void reset() throws IOException {
        try {
            this.in.reset();
        }
        catch (IOException e) {
            this.handleIOException(e);
        }
    }
    
    @Override
    public boolean markSupported() {
        return this.in.markSupported();
    }
    
    protected void beforeRead(final int n) throws IOException {
    }
    
    protected void afterRead(final int n) throws IOException {
    }
    
    protected void handleIOException(final IOException e) throws IOException {
        throw e;
    }
}
