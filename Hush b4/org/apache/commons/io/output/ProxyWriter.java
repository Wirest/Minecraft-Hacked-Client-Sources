// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.output;

import java.io.IOException;
import java.io.Writer;
import java.io.FilterWriter;

public class ProxyWriter extends FilterWriter
{
    public ProxyWriter(final Writer proxy) {
        super(proxy);
    }
    
    @Override
    public Writer append(final char c) throws IOException {
        try {
            this.beforeWrite(1);
            this.out.append(c);
            this.afterWrite(1);
        }
        catch (IOException e) {
            this.handleIOException(e);
        }
        return this;
    }
    
    @Override
    public Writer append(final CharSequence csq, final int start, final int end) throws IOException {
        try {
            this.beforeWrite(end - start);
            this.out.append(csq, start, end);
            this.afterWrite(end - start);
        }
        catch (IOException e) {
            this.handleIOException(e);
        }
        return this;
    }
    
    @Override
    public Writer append(final CharSequence csq) throws IOException {
        try {
            int len = 0;
            if (csq != null) {
                len = csq.length();
            }
            this.beforeWrite(len);
            this.out.append(csq);
            this.afterWrite(len);
        }
        catch (IOException e) {
            this.handleIOException(e);
        }
        return this;
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
    public void write(final char[] chr) throws IOException {
        try {
            int len = 0;
            if (chr != null) {
                len = chr.length;
            }
            this.beforeWrite(len);
            this.out.write(chr);
            this.afterWrite(len);
        }
        catch (IOException e) {
            this.handleIOException(e);
        }
    }
    
    @Override
    public void write(final char[] chr, final int st, final int len) throws IOException {
        try {
            this.beforeWrite(len);
            this.out.write(chr, st, len);
            this.afterWrite(len);
        }
        catch (IOException e) {
            this.handleIOException(e);
        }
    }
    
    @Override
    public void write(final String str) throws IOException {
        try {
            int len = 0;
            if (str != null) {
                len = str.length();
            }
            this.beforeWrite(len);
            this.out.write(str);
            this.afterWrite(len);
        }
        catch (IOException e) {
            this.handleIOException(e);
        }
    }
    
    @Override
    public void write(final String str, final int st, final int len) throws IOException {
        try {
            this.beforeWrite(len);
            this.out.write(str, st, len);
            this.afterWrite(len);
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
