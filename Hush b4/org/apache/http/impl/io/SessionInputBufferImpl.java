// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.io;

import org.apache.http.io.HttpTransportMetrics;
import java.nio.charset.CoderResult;
import java.nio.ByteBuffer;
import org.apache.http.MessageConstraintException;
import org.apache.http.util.CharArrayBuffer;
import java.io.IOException;
import org.apache.http.util.Asserts;
import org.apache.http.util.Args;
import java.nio.CharBuffer;
import java.io.InputStream;
import java.nio.charset.CharsetDecoder;
import org.apache.http.config.MessageConstraints;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.SessionInputBuffer;

@NotThreadSafe
public class SessionInputBufferImpl implements SessionInputBuffer, BufferInfo
{
    private final HttpTransportMetricsImpl metrics;
    private final byte[] buffer;
    private final ByteArrayBuffer linebuffer;
    private final int minChunkLimit;
    private final MessageConstraints constraints;
    private final CharsetDecoder decoder;
    private InputStream instream;
    private int bufferpos;
    private int bufferlen;
    private CharBuffer cbuf;
    
    public SessionInputBufferImpl(final HttpTransportMetricsImpl metrics, final int buffersize, final int minChunkLimit, final MessageConstraints constraints, final CharsetDecoder chardecoder) {
        Args.notNull(metrics, "HTTP transport metrcis");
        Args.positive(buffersize, "Buffer size");
        this.metrics = metrics;
        this.buffer = new byte[buffersize];
        this.bufferpos = 0;
        this.bufferlen = 0;
        this.minChunkLimit = ((minChunkLimit >= 0) ? minChunkLimit : 512);
        this.constraints = ((constraints != null) ? constraints : MessageConstraints.DEFAULT);
        this.linebuffer = new ByteArrayBuffer(buffersize);
        this.decoder = chardecoder;
    }
    
    public SessionInputBufferImpl(final HttpTransportMetricsImpl metrics, final int buffersize) {
        this(metrics, buffersize, buffersize, null, null);
    }
    
    public void bind(final InputStream instream) {
        this.instream = instream;
    }
    
    public boolean isBound() {
        return this.instream != null;
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
    
    private int streamRead(final byte[] b, final int off, final int len) throws IOException {
        Asserts.notNull(this.instream, "Input stream");
        return this.instream.read(b, off, len);
    }
    
    public int fillBuffer() throws IOException {
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
        final int l = this.streamRead(this.buffer, off, len2);
        if (l == -1) {
            return -1;
        }
        this.bufferlen = off + l;
        this.metrics.incrementBytesTransferred(l);
        return l;
    }
    
    public boolean hasBufferedData() {
        return this.bufferpos < this.bufferlen;
    }
    
    public void clear() {
        this.bufferpos = 0;
        this.bufferlen = 0;
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
            final int read = this.streamRead(b, off, len);
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
            final int maxLineLen = this.constraints.getMaxLineLength();
            if (maxLineLen > 0 && this.linebuffer.length() >= maxLineLen) {
                throw new MessageConstraintException("Maximum line length limit exceeded");
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
        if (this.decoder == null) {
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
        int pos = position;
        final int off = this.bufferpos;
        this.bufferpos = pos + 1;
        if (pos > off && this.buffer[pos - 1] == 13) {
            --pos;
        }
        int len = pos - off;
        if (this.decoder == null) {
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
    
    public boolean isDataAvailable(final int timeout) throws IOException {
        return this.hasBufferedData();
    }
    
    public HttpTransportMetrics getMetrics() {
        return this.metrics;
    }
}
