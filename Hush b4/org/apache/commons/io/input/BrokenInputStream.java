// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class BrokenInputStream extends InputStream
{
    private final IOException exception;
    
    public BrokenInputStream(final IOException exception) {
        this.exception = exception;
    }
    
    public BrokenInputStream() {
        this(new IOException("Broken input stream"));
    }
    
    @Override
    public int read() throws IOException {
        throw this.exception;
    }
    
    @Override
    public int available() throws IOException {
        throw this.exception;
    }
    
    @Override
    public long skip(final long n) throws IOException {
        throw this.exception;
    }
    
    @Override
    public void reset() throws IOException {
        throw this.exception;
    }
    
    @Override
    public void close() throws IOException {
        throw this.exception;
    }
}
