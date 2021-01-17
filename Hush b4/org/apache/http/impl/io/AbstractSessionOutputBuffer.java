// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.io;

import org.apache.http.io.HttpTransportMetrics;
import java.nio.charset.CoderResult;
import org.apache.http.util.CharArrayBuffer;
import java.nio.CharBuffer;
import java.io.IOException;
import org.apache.http.params.HttpParams;
import org.apache.http.Consts;
import org.apache.http.util.Args;
import java.nio.ByteBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.Charset;
import org.apache.http.util.ByteArrayBuffer;
import java.io.OutputStream;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.SessionOutputBuffer;

@Deprecated
@NotThreadSafe
public abstract class AbstractSessionOutputBuffer implements SessionOutputBuffer, BufferInfo
{
    private static final byte[] CRLF;
    private OutputStream outstream;
    private ByteArrayBuffer buffer;
    private Charset charset;
    private boolean ascii;
    private int minChunkLimit;
    private HttpTransportMetricsImpl metrics;
    private CodingErrorAction onMalformedCharAction;
    private CodingErrorAction onUnmappableCharAction;
    private CharsetEncoder encoder;
    private ByteBuffer bbuf;
    
    protected AbstractSessionOutputBuffer(final OutputStream outstream, final int buffersize, final Charset charset, final int minChunkLimit, final CodingErrorAction malformedCharAction, final CodingErrorAction unmappableCharAction) {
        Args.notNull(outstream, "Input stream");
        Args.notNegative(buffersize, "Buffer size");
        this.outstream = outstream;
        this.buffer = new ByteArrayBuffer(buffersize);
        this.charset = ((charset != null) ? charset : Consts.ASCII);
        this.ascii = this.charset.equals(Consts.ASCII);
        this.encoder = null;
        this.minChunkLimit = ((minChunkLimit >= 0) ? minChunkLimit : 512);
        this.metrics = this.createTransportMetrics();
        this.onMalformedCharAction = ((malformedCharAction != null) ? malformedCharAction : CodingErrorAction.REPORT);
        this.onUnmappableCharAction = ((unmappableCharAction != null) ? unmappableCharAction : CodingErrorAction.REPORT);
    }
    
    public AbstractSessionOutputBuffer() {
    }
    
    protected void init(final OutputStream outstream, final int buffersize, final HttpParams params) {
        Args.notNull(outstream, "Input stream");
        Args.notNegative(buffersize, "Buffer size");
        Args.notNull(params, "HTTP parameters");
        this.outstream = outstream;
        this.buffer = new ByteArrayBuffer(buffersize);
        final String charset = (String)params.getParameter("http.protocol.element-charset");
        this.charset = ((charset != null) ? Charset.forName(charset) : Consts.ASCII);
        this.ascii = this.charset.equals(Consts.ASCII);
        this.encoder = null;
        this.minChunkLimit = params.getIntParameter("http.connection.min-chunk-limit", 512);
        this.metrics = this.createTransportMetrics();
        final CodingErrorAction a1 = (CodingErrorAction)params.getParameter("http.malformed.input.action");
        this.onMalformedCharAction = ((a1 != null) ? a1 : CodingErrorAction.REPORT);
        final CodingErrorAction a2 = (CodingErrorAction)params.getParameter("http.unmappable.input.action");
        this.onUnmappableCharAction = ((a2 != null) ? a2 : CodingErrorAction.REPORT);
    }
    
    protected HttpTransportMetricsImpl createTransportMetrics() {
        return new HttpTransportMetricsImpl();
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
    
    protected void flushBuffer() throws IOException {
        final int len = this.buffer.length();
        if (len > 0) {
            this.outstream.write(this.buffer.buffer(), 0, len);
            this.buffer.clear();
            this.metrics.incrementBytesTransferred(len);
        }
    }
    
    public void flush() throws IOException {
        this.flushBuffer();
        this.outstream.flush();
    }
    
    public void write(final byte[] b, final int off, final int len) throws IOException {
        if (b == null) {
            return;
        }
        if (len > this.minChunkLimit || len > this.buffer.capacity()) {
            this.flushBuffer();
            this.outstream.write(b, off, len);
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
        if (this.buffer.isFull()) {
            this.flushBuffer();
        }
        this.buffer.append(b);
    }
    
    public void writeLine(final String s) throws IOException {
        if (s == null) {
            return;
        }
        if (s.length() > 0) {
            if (this.ascii) {
                for (int i = 0; i < s.length(); ++i) {
                    this.write(s.charAt(i));
                }
            }
            else {
                final CharBuffer cbuf = CharBuffer.wrap(s);
                this.writeEncoded(cbuf);
            }
        }
        this.write(AbstractSessionOutputBuffer.CRLF);
    }
    
    public void writeLine(final CharArrayBuffer charbuffer) throws IOException {
        if (charbuffer == null) {
            return;
        }
        if (this.ascii) {
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
        this.write(AbstractSessionOutputBuffer.CRLF);
    }
    
    private void writeEncoded(final CharBuffer cbuf) throws IOException {
        if (!cbuf.hasRemaining()) {
            return;
        }
        if (this.encoder == null) {
            (this.encoder = this.charset.newEncoder()).onMalformedInput(this.onMalformedCharAction);
            this.encoder.onUnmappableCharacter(this.onUnmappableCharAction);
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
