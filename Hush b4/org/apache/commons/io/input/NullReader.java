// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.input;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

public class NullReader extends Reader
{
    private final long size;
    private long position;
    private long mark;
    private long readlimit;
    private boolean eof;
    private final boolean throwEofException;
    private final boolean markSupported;
    
    public NullReader(final long size) {
        this(size, true, false);
    }
    
    public NullReader(final long size, final boolean markSupported, final boolean throwEofException) {
        this.mark = -1L;
        this.size = size;
        this.markSupported = markSupported;
        this.throwEofException = throwEofException;
    }
    
    public long getPosition() {
        return this.position;
    }
    
    public long getSize() {
        return this.size;
    }
    
    @Override
    public void close() throws IOException {
        this.eof = false;
        this.position = 0L;
        this.mark = -1L;
    }
    
    @Override
    public synchronized void mark(final int readlimit) {
        if (!this.markSupported) {
            throw new UnsupportedOperationException("Mark not supported");
        }
        this.mark = this.position;
        this.readlimit = readlimit;
    }
    
    @Override
    public boolean markSupported() {
        return this.markSupported;
    }
    
    @Override
    public int read() throws IOException {
        if (this.eof) {
            throw new IOException("Read after end of file");
        }
        if (this.position == this.size) {
            return this.doEndOfFile();
        }
        ++this.position;
        return this.processChar();
    }
    
    @Override
    public int read(final char[] chars) throws IOException {
        return this.read(chars, 0, chars.length);
    }
    
    @Override
    public int read(final char[] chars, final int offset, final int length) throws IOException {
        if (this.eof) {
            throw new IOException("Read after end of file");
        }
        if (this.position == this.size) {
            return this.doEndOfFile();
        }
        this.position += length;
        int returnLength = length;
        if (this.position > this.size) {
            returnLength = length - (int)(this.position - this.size);
            this.position = this.size;
        }
        this.processChars(chars, offset, returnLength);
        return returnLength;
    }
    
    @Override
    public synchronized void reset() throws IOException {
        if (!this.markSupported) {
            throw new UnsupportedOperationException("Mark not supported");
        }
        if (this.mark < 0L) {
            throw new IOException("No position has been marked");
        }
        if (this.position > this.mark + this.readlimit) {
            throw new IOException("Marked position [" + this.mark + "] is no longer valid - passed the read limit [" + this.readlimit + "]");
        }
        this.position = this.mark;
        this.eof = false;
    }
    
    @Override
    public long skip(final long numberOfChars) throws IOException {
        if (this.eof) {
            throw new IOException("Skip after end of file");
        }
        if (this.position == this.size) {
            return this.doEndOfFile();
        }
        this.position += numberOfChars;
        long returnLength = numberOfChars;
        if (this.position > this.size) {
            returnLength = numberOfChars - (this.position - this.size);
            this.position = this.size;
        }
        return returnLength;
    }
    
    protected int processChar() {
        return 0;
    }
    
    protected void processChars(final char[] chars, final int offset, final int length) {
    }
    
    private int doEndOfFile() throws EOFException {
        this.eof = true;
        if (this.throwEofException) {
            throw new EOFException();
        }
        return -1;
    }
}
