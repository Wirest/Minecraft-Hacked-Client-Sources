// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.io;

import org.apache.http.io.HttpTransportMetrics;
import java.nio.charset.CoderResult;
import java.nio.ByteBuffer;
import org.apache.http.util.CharArrayBuffer;
import java.io.IOException;
import org.apache.http.Consts;
import org.apache.http.util.Args;
import org.apache.http.params.HttpParams;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.Charset;
import org.apache.http.util.ByteArrayBuffer;
import java.io.InputStream;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.SessionInputBuffer;

@Deprecated
@NotThreadSafe
public abstract class AbstractSessionInputBuffer implements SessionInputBuffer, BufferInfo
{
    private InputStream instream;
    private byte[] buffer;
    private ByteArrayBuffer linebuffer;
    private Charset charset;
    private boolean ascii;
    private int maxLineLen;
    private int minChunkLimit;
    private HttpTransportMetricsImpl metrics;
    private CodingErrorAction onMalformedCharAction;
    private CodingErrorAction onUnmappableCharAction;
    private int bufferpos;
    private int bufferlen;
    private CharsetDecoder decoder;
    private CharBuffer cbuf;
    
    protected void init(final InputStream instream, final int buffersize, final HttpParams params) {
        Args.notNull(instream, "Input stream");
        Args.notNegative(buffersize, "Buffer size");
        Args.notNull(params, "HTTP parameters");
        this.instream = instream;
        this.buffer = new byte[buffersize];
        this.bufferpos = 0;
        this.bufferlen = 0;
        this.linebuffer = new ByteArrayBuffer(buffersize);
        final String charset = (String)params.getParameter("http.protocol.element-charset");
        this.charset = ((charset != null) ? Charset.forName(charset) : Consts.ASCII);
        this.ascii = this.charset.equals(Consts.ASCII);
        this.decoder = null;
        this.maxLineLen = params.getIntParameter("http.connection.max-line-length", -1);
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
        return this.buffer.length;
    }
    
    public int length() {
        return this.bufferlen - this.bufferpos;
    }
    
    public int available() {
        return this.capacity() - this.length();
    }
    
    protected int fillBuffer() throws IOException {
        if (this.bufferpos > 0) {
            final int len = this.bufferlen - this.bufferpos;
            if (len > 0) {
                System.arraycopy(this.buffer, this.bufferpos, this.buffer, 0, len);
            }
            this.bufferpos = 0;
            this.bufferlen = len;
        }
        final int off = this.bufferlen;
        final int len2 = this.buffer.length - off;
        final int l = this.instream.read(this.buffer, off, len2);
        if (l == -1) {
            return -1;
        }
        this.bufferlen = off + l;
        this.metrics.incrementBytesTransferred(l);
        return l;
    }
    
    protected boolean hasBufferedData() {
        return this.bufferpos < this.bufferlen;
    }
    
    public int read() throws IOException {
        while (!this.hasBufferedData()) {
            final int noRead = this.fillBuffer();
            if (noRead == -1) {
                return -1;
            }
        }
        return this.buffer[this.bufferpos++] & 0xFF;
    }
    
    public int read(final byte[] b, final int off, final int len) throws IOException {
        if (b == null) {
            return 0;
        }
        if (this.hasBufferedData()) {
            final int chunk = Math.min(len, this.bufferlen - this.bufferpos);
            System.arraycopy(this.buffer, this.bufferpos, b, off, chunk);
            this.bufferpos += chunk;
            return chunk;
        }
        if (len > this.minChunkLimit) {
            final int read = this.instream.read(b, off, len);
            if (read > 0) {
                this.metrics.incrementBytesTransferred(read);
            }
            return read;
        }
        while (!this.hasBufferedData()) {
            final int noRead = this.fillBuffer();
            if (noRead == -1) {
                return -1;
            }
        }
        final int chunk = Math.min(len, this.bufferlen - this.bufferpos);
        System.arraycopy(this.buffer, this.bufferpos, b, off, chunk);
        this.bufferpos += chunk;
        return chunk;
    }
    
    public int read(final byte[] b) throws IOException {
        if (b == null) {
            return 0;
        }
        return this.read(b, 0, b.length);
    }
    
    private int locateLF() {
        for (int i = this.bufferpos; i < this.bufferlen; ++i) {
            if (this.buffer[i] == 10) {
                return i;
            }
        }
        return -1;
    }
    
    public int readLine(final CharArrayBuffer charbuffer) throws IOException {
        Args.notNull(charbuffer, "Char array buffer");
        int noRead = 0;
        boolean retry = true;
        while (retry) {
            final int i = this.locateLF();
            if (i != -1) {
                if (this.linebuffer.isEmpty()) {
                    return this.lineFromReadBuffer(charbuffer, i);
                }
                retry = false;
                final int len = i + 1 - this.bufferpos;
                this.linebuffer.append(this.buffer, this.bufferpos, len);
                this.bufferpos = i + 1;
            }
            else {
                if (this.hasBufferedData()) {
                    final int len = this.bufferlen - this.bufferpos;
                    this.linebuffer.append(this.buffer, this.bufferpos, len);
                    this.bufferpos = this.bufferlen;
                }
                noRead = this.fillBuffer();
                if (noRead == -1) {
                    retry = false;
                }
            }
            if (this.maxLineLen > 0 && this.linebuffer.length() >= this.maxLineLen) {
                throw new IOException("Maximum line length limit exceeded");
            }
        }
        if (noRead == -1 && this.linebuffer.isEmpty()) {
            return -1;
        }
        return this.lineFromLineBuffer(charbuffer);
    }
    
    private int lineFromLineBuffer(final CharArrayBuffer charbuffer) throws IOException {
        int len = this.linebuffer.length();
        if (len > 0) {
            if (this.linebuffer.byteAt(len - 1) == 10) {
                --len;
            }
            if (len > 0 && this.linebuffer.byteAt(len - 1) == 13) {
                --len;
            }
        }
        if (this.ascii) {
            charbuffer.append(this.linebuffer, 0, len);
        }
        else {
            final ByteBuffer bbuf = ByteBuffer.wrap(this.linebuffer.buffer(), 0, len);
            len = this.appendDecoded(charbuffer, bbuf);
        }
        this.linebuffer.clear();
        return len;
    }
    
    private int lineFromReadBuffer(final CharArrayBuffer charbuffer, final int position) throws IOException {
        final int off = this.bufferpos;
        int i = position;
        this.bufferpos = i + 1;
        if (i > off && this.buffer[i - 1] == 13) {
            --i;
        }
        int len = i - off;
        if (this.ascii) {
            charbuffer.append(this.buffer, off, len);
        }
        else {
            final ByteBuffer bbuf = ByteBuffer.wrap(this.buffer, off, len);
            len = this.appendDecoded(charbuffer, bbuf);
        }
        return len;
    }
    
    private int appendDecoded(final CharArrayBuffer charbuffer, final ByteBuffer bbuf) throws IOException {
        if (!bbuf.hasRemaining()) {
            return 0;
        }
        if (this.decoder == null) {
            (this.decoder = this.charset.newDecoder()).onMalformedInput(this.onMalformedCharAction);
            this.decoder.onUnmappableCharacter(this.onUnmappableCharAction);
        }
        if (this.cbuf == null) {
            this.cbuf = CharBuffer.allocate(1024);
        }
        this.decoder.reset();
        int len = 0;
        while (bbuf.hasRemaining()) {
            final CoderResult result = this.decoder.decode(bbuf, this.cbuf, true);
            len += this.handleDecodingResult(result, charbuffer, bbuf);
        }
        final CoderResult result = this.decoder.flush(this.cbuf);
        len += this.handleDecodingResult(result, charbuffer, bbuf);
        this.cbuf.clear();
        return len;
    }
    
    private int handleDecodingResult(final CoderResult result, final CharArrayBuffer charbuffer, final ByteBuffer bbuf) throws IOException {
        if (result.isError()) {
            result.throwException();
        }
        this.cbuf.flip();
        final int len = this.cbuf.remaining();
        while (this.cbuf.hasRemaining()) {
            charbuffer.append(this.cbuf.get());
        }
        this.cbuf.compact();
        return len;
    }
    
    public String readLine() throws IOException {
        final CharArrayBuffer charbuffer = new CharArrayBuffer(64);
        final int l = this.readLine(charbuffer);
        if (l != -1) {
            return charbuffer.toString();
        }
        return null;
    }
    
    public HttpTransportMetrics getMetrics() {
        return this.metrics;
    }
}
