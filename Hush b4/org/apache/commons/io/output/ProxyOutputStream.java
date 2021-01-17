// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.FilterOutputStream;

public class ProxyOutputStream extends FilterOutputStream
{
    public ProxyOutputStream(final OutputStream proxy) {
        super(proxy);
    }
    
    @Override
    public void write(final int idx) throws IOException {
        try {
            this.beforeWrite(1);
            this.out.write(idx);
            this.afterWrite(1);
        }
        catch (IOException e) {
            this.handleIOException(e);
        }
    }
    
    @Override
    public void write(final byte[] bts) throws IOException {
        try {
            final int len = (bts != null) ? bts.length : 0;
            this.beforeWrite(len);
            this.out.write(bts);
            this.afterWrite(len);
        }
        catch (IOException e) {
            this.handleIOException(e);
        }
    }
    
    @Override
    public void write(final byte[] bts, final int st, final int end) throws IOException {
        try {
            this.beforeWrite(end);
            this.out.write(bts, st, end);
            this.afterWrite(end);
        }
        catch (IOException e) {
            this.handleIOException(e);
        }
    }
    
    @Override
    public void flush() throws IOException {
        try {
            this.out.flush();
        }
        catch (IOException e) {
            this.handleIOException(e);
        }
    }
    
    @Override
    public void close() throws IOException {
        try {
            this.out.close();
        }
        catch (IOException e) {
            this.handleIOException(e);
        }
    }
    
    protected void beforeWrite(final int n) throws IOException {
    }
    
    protected void afterWrite(final int n) throws IOException {
    }
    
    protected void handleIOException(final IOException e) throws IOException {
        throw e;
    }
}
