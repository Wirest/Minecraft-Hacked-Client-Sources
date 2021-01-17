// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.output;

import java.nio.charset.CoderResult;
import java.io.IOException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.Charset;
import java.nio.CharBuffer;
import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;
import java.io.Writer;
import java.io.OutputStream;

public class WriterOutputStream extends OutputStream
{
    private static final int DEFAULT_BUFFER_SIZE = 1024;
    private final Writer writer;
    private final CharsetDecoder decoder;
    private final boolean writeImmediately;
    private final ByteBuffer decoderIn;
    private final CharBuffer decoderOut;
    
    public WriterOutputStream(final Writer writer, final CharsetDecoder decoder) {
        this(writer, decoder, 1024, false);
    }
    
    public WriterOutputStream(final Writer writer, final CharsetDecoder decoder, final int bufferSize, final boolean writeImmediately) {
        this.decoderIn = ByteBuffer.allocate(128);
        this.writer = writer;
        this.decoder = decoder;
        this.writeImmediately = writeImmediately;
        this.decoderOut = CharBuffer.allocate(bufferSize);
    }
    
    public WriterOutputStream(final Writer writer, final Charset charset, final int bufferSize, final boolean writeImmediately) {
        this(writer, charset.newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).replaceWith("?"), bufferSize, writeImmediately);
    }
    
    public WriterOutputStream(final Writer writer, final Charset charset) {
        this(writer, charset, 1024, false);
    }
    
    public WriterOutputStream(final Writer writer, final String charsetName, final int bufferSize, final boolean writeImmediately) {
        this(writer, Charset.forName(charsetName), bufferSize, writeImmediately);
    }
    
    public WriterOutputStream(final Writer writer, final String charsetName) {
        this(writer, charsetName, 1024, false);
    }
    
    public WriterOutputStream(final Writer writer) {
        this(writer, Charset.defaultCharset(), 1024, false);
    }
    
    @Override
    public void write(final byte[] b, int off, int len) throws IOException {
        while (len > 0) {
            final int c = Math.min(len, this.decoderIn.remaining());
            this.decoderIn.put(b, off, c);
            this.processInput(false);
            len -= c;
            off += c;
        }
        if (this.writeImmediately) {
            this.flushOutput();
        }
    }
    
    @Override
    public void write(final byte[] b) throws IOException {
        this.write(b, 0, b.length);
    }
    
    @Override
    public void write(final int b) throws IOException {
        this.write(new byte[] { (byte)b }, 0, 1);
    }
    
    @Override
    public void flush() throws IOException {
        this.flushOutput();
        this.writer.flush();
    }
    
    @Override
    public void close() throws IOException {
        this.processInput(true);
        this.flushOutput();
        this.writer.close();
    }
    
    private void processInput(final boolean endOfInput) throws IOException {
        this.decoderIn.flip();
        CoderResult coderResult;
        while (true) {
            coderResult = this.decoder.decode(this.decoderIn, this.decoderOut, endOfInput);
            if (!coderResult.isOverflow()) {
                break;
            }
            this.flushOutput();
        }
        if (coderResult.isUnderflow()) {
            this.decoderIn.compact();
            return;
        }
        throw new IOException("Unexpected coder result");
    }
    
    private void flushOutput() throws IOException {
        if (this.decoderOut.position() > 0) {
            this.writer.write(this.decoderOut.array(), 0, this.decoderOut.position());
            this.decoderOut.rewind();
        }
    }
}
