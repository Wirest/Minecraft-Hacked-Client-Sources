// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.io;

import org.apache.http.io.HttpTransportMetrics;
import java.nio.charset.CoderResult;
import org.apache.http.util.CharArrayBuffer;
import java.nio.CharBuffer;
import java.io.IOException;
import org.apache.http.util.Asserts;
import org.apache.http.util.Args;
import java.nio.ByteBuffer;
import java.io.OutputStream;
import java.nio.charset.CharsetEncoder;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.SessionOutputBuffer;

@NotThreadSafe
public class SessionOutputBufferImpl implements SessionOutputBuffer, BufferInfo
{
    private static final byte[] CRLF;
    private final HttpTransportMetricsImpl metrics;
    private final ByteArrayBuffer buffer;
    private final int fragementSizeHint;
    private final CharsetEncoder encoder;
    private OutputStream outstream;
    private ByteBuffer bbuf;
    
    public SessionOutputBufferImpl(final HttpTransportMetricsImpl metrics, final int buffersize, final int fragementSizeHint, final CharsetEncoder charencoder) {
        Args.positive(buffersize, "Buffer size");
        Args.notNull(metrics, "HTTP transport metrcis");
        this.metrics = metrics;
        this.buffer = new ByteArrayBuffer(buffersize);
        this.fragementSizeHint = ((fragementSizeHint >= 0) ? fragementSizeHint : 0);
        this.encoder = charencoder;
    }
    
    public SessionOutputBufferImpl(final HttpTransportMetricsImpl metrics, final int buffersize) {
        this(metrics, buffersize, buffersize, null);
    }
    
    public void bind(final OutputStream outstream) {
        this.outstream = outstream;
    }
    
    public boolean isBound() {
        return this.outstream != null;
    }
    
    public int capacity() {
        return this.buffer.capacity();
    }
    
    public int length() {
        return this.buffer.length();
    }
    
    public int available() {
        return this.capacity() - this.length();
    }
    
    private void streamWrite(final byte[] b, final int off, final int len) throws IOException {
        Asserts.notNull(this.outstream, "Output stream");
        this.outstream.write(b, off, len);
    }
    
    private void flushStream() throws IOException {
        if (this.outstream != null) {
            this.outstream.flush();
        }
    }
    
    private void flushBuffer() throws IOException {
        final int len = this.buffer.length();
        if (len > 0) {
            this.streamWrite(this.buffer.buffer(), 0, len);
            this.buffer.clear();
            this.metrics.incrementBytesTransferred(len);
        }
    }
    
    public void flush() throws IOException {
        this.flushBuffer();
        this.flushStream();
    }
    
    public void write(final byte[] b, final int off, final int len) throws IOException {
        if (b == null) {
            return;
        }
        if (len > this.fragementSizeHint || len > this.buffer.capacity()) {
            this.flushBuffer();
            this.streamWrite(b, off, len);
            this.metrics.incrementBytesTransferred(len);
        }
        else {
            final int freecapacity = this.buffer.capacity() - this.buffer.length();
            if (len > freecapacity) {
                this.flushBuffer();
            }
            this.buffer.append(b, off, len);
        }
    }
    
    public void write(final byte[] b) throws IOException {
        if (b == null) {
            return;
        }
        this.write(b, 0, b.length);
    }
    
    public void write(final int b) throws IOException {
        if (this.fragementSizeHint > 0) {
            if (this.buffer.isFull()) {
                this.flushBuffer();
            }
            this.buffer.append(b);
        }
        else {
            this.flushBuffer();
            this.outstream.write(b);
        }
    }
    
    public void writeLine(final String s) throws IOException {
        if (s == null) {
            return;
        }
        if (s.length() > 0) {
            if (this.encoder == null) {
                for (int i = 0; i < s.length(); ++i) {
                    this.write(s.charAt(i));
                }
            }
            else {
                final CharBuffer cbuf = CharBuffer.wrap(s);
                this.writeEncoded(cbuf);
            }
        }
        this.write(SessionOutputBufferImpl.CRLF);
    }
    
    public void writeLine(final CharArrayBuffer charbuffer) throws IOException {
        if (charbuffer == null) {
            return;
        }
        if (this.encoder == null) {
            int off = 0;
            int chunk;
            for (int remaining = charbuffer.length(); remaining > 0; remaining -= chunk) {
                chunk = this.buffer.capacity() - this.buffer.length();
                chunk = Math.min(chunk, remaining);
                if (chunk > 0) {
                    this.buffer.append(charbuffer, off, chunk);
                }
                if (this.buffer.isFull()) {
                    this.flushBuffer();
                }
                off += chunk;
            }
        }
        else {
            final CharBuffer cbuf = CharBuffer.wrap(charbuffer.buffer(), 0, charbuffer.length());
            this.writeEncoded(cbuf);
        }
        this.write(SessionOutputBufferImpl.CRLF);
    }
    
    private void writeEncoded(final CharBuffer cbuf) throws IOException {
        if (!cbuf.hasRemaining()) {
            return;
        }
        if (this.bbuf == null) {
            this.bbuf = ByteBuffer.allocate(1024);
        }
        this.encoder.reset();
        while (cbuf.hasRemaining()) {
            final CoderResult result = this.encoder.encode(cbuf, this.bbuf, true);
            this.handleEncodingResult(result);
        }
        final CoderResult result = this.encoder.flush(this.bbuf);
        this.handleEncodingResult(result);
        this.bbuf.clear();
    }
    
    private void handleEncodingResult(final CoderResult result) throws IOException {
        if (result.isError()) {
            result.throwException();
        }
        this.bbuf.flip();
        while (this.bbuf.hasRemaining()) {
            this.write(this.bbuf.get());
        }
        this.bbuf.compact();
    }
    
    public HttpTransportMetrics getMetrics() {
        return this.metrics;
    }
    
    static {
        CRLF = new byte[] { 13, 10 };
    }
}
