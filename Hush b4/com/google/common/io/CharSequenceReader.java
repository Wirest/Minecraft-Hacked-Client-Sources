// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import java.nio.CharBuffer;
import java.io.IOException;
import com.google.common.base.Preconditions;
import java.io.Reader;

final class CharSequenceReader extends Reader
{
    private CharSequence seq;
    private int pos;
    private int mark;
    
    public CharSequenceReader(final CharSequence seq) {
        this.seq = Preconditions.checkNotNull(seq);
    }
    
    private void checkOpen() throws IOException {
        if (this.seq == null) {
            throw new IOException("reader closed");
        }
    }
    
    private boolean hasRemaining() {
        return this.remaining() > 0;
    }
    
    private int remaining() {
        return this.seq.length() - this.pos;
    }
    
    @Override
    public synchronized int read(final CharBuffer target) throws IOException {
        Preconditions.checkNotNull(target);
        this.checkOpen();
        if (!this.hasRemaining()) {
            return -1;
        }
        final int charsToRead = Math.min(target.remaining(), this.remaining());
        for (int i = 0; i < charsToRead; ++i) {
            target.put(this.seq.charAt(this.pos++));
        }
        return charsToRead;
    }
    
    @Override
    public synchronized int read() throws IOException {
        this.checkOpen();
        return this.hasRemaining() ? this.seq.charAt(this.pos++) : -1;
    }
    
    @Override
    public synchronized int read(final char[] cbuf, final int off, final int len) throws IOException {
        Preconditions.checkPositionIndexes(off, off + len, cbuf.length);
        this.checkOpen();
        if (!this.hasRemaining()) {
            return -1;
        }
        final int charsToRead = Math.min(len, this.remaining());
        for (int i = 0; i < charsToRead; ++i) {
            cbuf[off + i] = this.seq.charAt(this.pos++);
        }
        return charsToRead;
    }
    
    @Override
    public synchronized long skip(final long n) throws IOException {
        Preconditions.checkArgument(n >= 0L, "n (%s) may not be negative", n);
        this.checkOpen();
        final int charsToSkip = (int)Math.min(this.remaining(), n);
        this.pos += charsToSkip;
        return charsToSkip;
    }
    
    @Override
    public synchronized boolean ready() throws IOException {
        this.checkOpen();
        return true;
    }
    
    @Override
    public boolean markSupported() {
        return true;
    }
    
    @Override
    public synchronized void mark(final int readAheadLimit) throws IOException {
        Preconditions.checkArgument(readAheadLimit >= 0, "readAheadLimit (%s) may not be negative", readAheadLimit);
        this.checkOpen();
        this.mark = this.pos;
    }
    
    @Override
    public synchronized void reset() throws IOException {
        this.checkOpen();
        this.pos = this.mark;
    }
    
    @Override
    public synchronized void close() throws IOException {
        this.seq = null;
    }
}
