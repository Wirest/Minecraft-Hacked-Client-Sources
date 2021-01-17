// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import com.google.common.base.Preconditions;
import java.io.Writer;

class AppendableWriter extends Writer
{
    private final Appendable target;
    private boolean closed;
    
    AppendableWriter(final Appendable target) {
        this.target = Preconditions.checkNotNull(target);
    }
    
    @Override
    public void write(final char[] cbuf, final int off, final int len) throws IOException {
        this.checkNotClosed();
        this.target.append(new String(cbuf, off, len));
    }
    
    @Override
    public void flush() throws IOException {
        this.checkNotClosed();
        if (this.target instanceof Flushable) {
            ((Flushable)this.target).flush();
        }
    }
    
    @Override
    public void close() throws IOException {
        this.closed = true;
        if (this.target instanceof Closeable) {
            ((Closeable)this.target).close();
        }
    }
    
    @Override
    public void write(final int c) throws IOException {
        this.checkNotClosed();
        this.target.append((char)c);
    }
    
    @Override
    public void write(@Nullable final String str) throws IOException {
        this.checkNotClosed();
        this.target.append(str);
    }
    
    @Override
    public void write(@Nullable final String str, final int off, final int len) throws IOException {
        this.checkNotClosed();
        this.target.append(str, off, off + len);
    }
    
    @Override
    public Writer append(final char c) throws IOException {
        this.checkNotClosed();
        this.target.append(c);
        return this;
    }
    
    @Override
    public Writer append(@Nullable final CharSequence charSeq) throws IOException {
        this.checkNotClosed();
        this.target.append(charSeq);
        return this;
    }
    
    @Override
    public Writer append(@Nullable final CharSequence charSeq, final int start, final int end) throws IOException {
        this.checkNotClosed();
        this.target.append(charSeq, start, end);
        return this;
    }
    
    private void checkNotClosed() throws IOException {
        if (this.closed) {
            throw new IOException("Cannot write to a closed writer.");
        }
    }
}
