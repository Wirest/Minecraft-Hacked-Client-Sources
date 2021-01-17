// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.output;

import java.io.IOException;
import java.io.Writer;

public class NullWriter extends Writer
{
    public static final NullWriter NULL_WRITER;
    
    @Override
    public Writer append(final char c) {
        return this;
    }
    
    @Override
    public Writer append(final CharSequence csq, final int start, final int end) {
        return this;
    }
    
    @Override
    public Writer append(final CharSequence csq) {
        return this;
    }
    
    @Override
    public void write(final int idx) {
    }
    
    @Override
    public void write(final char[] chr) {
    }
    
    @Override
    public void write(final char[] chr, final int st, final int end) {
    }
    
    @Override
    public void write(final String str) {
    }
    
    @Override
    public void write(final String str, final int st, final int end) {
    }
    
    @Override
    public void flush() {
    }
    
    @Override
    public void close() {
    }
    
    static {
        NULL_WRITER = new NullWriter();
    }
}
