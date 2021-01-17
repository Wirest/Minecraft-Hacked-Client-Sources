// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

public class BrokenOutputStream extends OutputStream
{
    private final IOException exception;
    
    public BrokenOutputStream(final IOException exception) {
        this.exception = exception;
    }
    
    public BrokenOutputStream() {
        this(new IOException("Broken output stream"));
    }
    
    @Override
    public void write(final int b) throws IOException {
        throw this.exception;
    }
    
    @Override
    public void flush() throws IOException {
        throw this.exception;
    }
    
    @Override
    public void close() throws IOException {
        throw this.exception;
    }
}
