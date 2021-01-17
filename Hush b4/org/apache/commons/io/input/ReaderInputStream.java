// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.input;

import java.io.IOException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.Charset;
import java.nio.charset.CoderResult;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.io.Reader;
import java.io.InputStream;

public class ReaderInputStream extends InputStream
{
    private static final int DEFAULT_BUFFER_SIZE = 1024;
    private final Reader reader;
    private final CharsetEncoder encoder;
    private final CharBuffer encoderIn;
    private final ByteBuffer encoderOut;
    private CoderResult lastCoderResult;
    private boolean endOfInput;
    
    public ReaderInputStream(final Reader reader, final CharsetEncoder encoder) {
        this(reader, encoder, 1024);
    }
    
    public ReaderInputStream(final Reader reader, final CharsetEncoder encoder, final int bufferSize) {
        this.reader = reader;
        this.encoder = encoder;
        (this.encoderIn = CharBuffer.allocate(bufferSize)).flip();
        (this.encoderOut = ByteBuffer.allocate(128)).flip();
    }
    
    public ReaderInputStream(final Reader reader, final Charset charset, final int bufferSize) {
        this(reader, charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE), bufferSize);
    }
    
    public ReaderInputStream(final Reader reader, final Charset charset) {
        this(reader, charset, 1024);
    }
    
    public ReaderInputStream(final Reader reader, final String charsetName, final int bufferSize) {
        this(reader, Charset.forName(charsetName), bufferSize);
    }
    
    public ReaderInputStream(final Reader reader, final String charsetName) {
        this(reader, charsetName, 1024);
    }
    
    public ReaderInputStream(final Reader reader) {
        this(reader, Charset.defaultCharset());
    }
    
    private void fillBuffer() throws IOException {
        if (!this.endOfInput && (this.lastCoderResult == null || this.lastCoderResult.isUnderflow())) {
            this.encoderIn.compact();
            final int position = this.encoderIn.position();
            final int c = this.reader.read(this.encoderIn.array(), position, this.encoderIn.remaining());
            if (c == -1) {
                this.endOfInput = true;
            }
            else {
                this.encoderIn.position(position + c);
            }
            this.encoderIn.flip();
        }
        this.encoderOut.compact();
        this.lastCoderResult = this.encoder.encode(this.encoderIn, this.encoderOut, this.endOfInput);
        this.encoderOut.flip();
    }
    
    @Override
    public int read(final byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException("Byte array must not be null");
        }
        if (len < 0 || off < 0 || off + len > b.length) {
            throw new IndexOutOfBoundsException("Array Size=" + b.length + ", offset=" + off + ", length=" + len);
        }
        int read = 0;
        if (len == 0) {
            return 0;
        }
        while (len > 0) {
            if (this.encoderOut.hasRemaining()) {
                final int c = Math.min(this.encoderOut.remaining(), len);
                this.encoderOut.get(b, off, c);
                off += c;
                len -= c;
                read += c;
            }
            else {
                this.fillBuffer();
                if (this.endOfInput && !this.encoderOut.hasRemaining()) {
                    break;
                }
                continue;
            }
        }
        return (read == 0 && this.endOfInput) ? -1 : read;
    }
    
    @Override
    public int read(final byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }
    
    @Override
    public int read() throws IOException {
        while (!this.encoderOut.hasRemaining()) {
            this.fillBuffer();
            if (this.endOfInput && !this.encoderOut.hasRemaining()) {
                return -1;
            }
        }
        return this.encoderOut.get() & 0xFF;
    }
    
    @Override
    public void close() throws IOException {
        this.reader.close();
    }
}
