// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.input;

import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.io.InputStream;

public class CharSequenceInputStream extends InputStream
{
    private final CharsetEncoder encoder;
    private final CharBuffer cbuf;
    private final ByteBuffer bbuf;
    private int mark;
    
    public CharSequenceInputStream(final CharSequence s, final Charset charset, final int bufferSize) {
        this.encoder = charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
        (this.bbuf = ByteBuffer.allocate(bufferSize)).flip();
        this.cbuf = CharBuffer.wrap(s);
        this.mark = -1;
    }
    
    public CharSequenceInputStream(final CharSequence s, final String charset, final int bufferSize) {
        this(s, Charset.forName(charset), bufferSize);
    }
    
    public CharSequenceInputStream(final CharSequence s, final Charset charset) {
        this(s, charset, 2048);
    }
    
    public CharSequenceInputStream(final CharSequence s, final String charset) {
        this(s, charset, 2048);
    }
    
    private void fillBuffer() throws CharacterCodingException {
        this.bbuf.compact();
        final CoderResult result = this.encoder.encode(this.cbuf, this.bbuf, true);
        if (result.isError()) {
            result.throwException();
        }
        this.bbuf.flip();
    }
    
    @Override
    public int read(final byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException("Byte array is null");
        }
        if (len < 0 || off + len > b.length) {
            throw new IndexOutOfBoundsException("Array Size=" + b.length + ", offset=" + off + ", length=" + len);
        }
        if (len == 0) {
            return 0;
        }
        if (!this.bbuf.hasRemaining() && !this.cbuf.hasRemaining()) {
            return -1;
        }
        int bytesRead = 0;
        while (len > 0) {
            if (this.bbuf.hasRemaining()) {
                final int chunk = Math.min(this.bbuf.remaining(), len);
                this.bbuf.get(b, off, chunk);
                off += chunk;
                len -= chunk;
                bytesRead += chunk;
            }
            else {
                this.fillBuffer();
                if (!this.bbuf.hasRemaining() && !this.cbuf.hasRemaining()) {
                    break;
                }
                continue;
            }
        }
        return (bytesRead == 0 && !this.cbuf.hasRemaining()) ? -1 : bytesRead;
    }
    
    @Override
    public int read() throws IOException {
        while (!this.bbuf.hasRemaining()) {
            this.fillBuffer();
            if (!this.bbuf.hasRemaining() && !this.cbuf.hasRemaining()) {
                return -1;
            }
        }
        return this.bbuf.get() & 0xFF;
    }
    
    @Override
    public int read(final byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }
    
    @Override
    public long skip(long n) throws IOException {
        int skipped;
        for (skipped = 0; n > 0L && this.cbuf.hasRemaining(); --n, ++skipped) {
            this.cbuf.get();
        }
        return skipped;
    }
    
    @Override
    public int available() throws IOException {
        return this.cbuf.remaining();
    }
    
    @Override
    public void close() throws IOException {
    }
    
    @Override
    public synchronized void mark(final int readlimit) {
        this.mark = this.cbuf.position();
    }
    
    @Override
    public synchronized void reset() throws IOException {
        if (this.mark != -1) {
            this.cbuf.position(this.mark);
            this.mark = -1;
        }
    }
    
    @Override
    public boolean markSupported() {
        return true;
    }
}
