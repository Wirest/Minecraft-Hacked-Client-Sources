// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.io;

import org.apache.http.HttpException;
import org.apache.http.message.LineParser;
import org.apache.http.MalformedChunkCodingException;
import org.apache.http.TruncatedChunkException;
import java.io.IOException;
import org.apache.http.io.BufferInfo;
import org.apache.http.util.Args;
import org.apache.http.Header;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.annotation.NotThreadSafe;
import java.io.InputStream;

@NotThreadSafe
public class ChunkedInputStream extends InputStream
{
    private static final int CHUNK_LEN = 1;
    private static final int CHUNK_DATA = 2;
    private static final int CHUNK_CRLF = 3;
    private static final int BUFFER_SIZE = 2048;
    private final SessionInputBuffer in;
    private final CharArrayBuffer buffer;
    private int state;
    private int chunkSize;
    private int pos;
    private boolean eof;
    private boolean closed;
    private Header[] footers;
    
    public ChunkedInputStream(final SessionInputBuffer in) {
        this.eof = false;
        this.closed = false;
        this.footers = new Header[0];
        this.in = Args.notNull(in, "Session input buffer");
        this.pos = 0;
        this.buffer = new CharArrayBuffer(16);
        this.state = 1;
    }
    
    @Override
    public int available() throws IOException {
        if (this.in instanceof BufferInfo) {
            final int len = ((BufferInfo)this.in).length();
            return Math.min(len, this.chunkSize - this.pos);
        }
        return 0;
    }
    
    @Override
    public int read() throws IOException {
        if (this.closed) {
            throw new IOException("Attempted read from closed stream.");
        }
        if (this.eof) {
            return -1;
        }
        if (this.state != 2) {
            this.nextChunk();
            if (this.eof) {
                return -1;
            }
        }
        final int b = this.in.read();
        if (b != -1) {
            ++this.pos;
            if (this.pos >= this.chunkSize) {
                this.state = 3;
            }
        }
        return b;
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        if (this.closed) {
            throw new IOException("Attempted read from closed stream.");
        }
        if (this.eof) {
            return -1;
        }
        if (this.state != 2) {
            this.nextChunk();
            if (this.eof) {
                return -1;
            }
        }
        final int bytesRead = this.in.read(b, off, Math.min(len, this.chunkSize - this.pos));
        if (bytesRead != -1) {
            this.pos += bytesRead;
            if (this.pos >= this.chunkSize) {
                this.state = 3;
            }
            return bytesRead;
        }
        this.eof = true;
        throw new TruncatedChunkException("Truncated chunk ( expected size: " + this.chunkSize + "; actual size: " + this.pos + ")");
    }
    
    @Override
    public int read(final byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }
    
    private void nextChunk() throws IOException {
        this.chunkSize = this.getChunkSize();
        if (this.chunkSize < 0) {
            throw new MalformedChunkCodingException("Negative chunk size");
        }
        this.state = 2;
        this.pos = 0;
        if (this.chunkSize == 0) {
            this.eof = true;
            this.parseTrailerHeaders();
        }
    }
    
    private int getChunkSize() throws IOException {
        final int st = this.state;
        switch (st) {
            case 3: {
                this.buffer.clear();
                final int bytesRead1 = this.in.readLine(this.buffer);
                if (bytesRead1 == -1) {
                    return 0;
                }
                if (!this.buffer.isEmpty()) {
                    throw new MalformedChunkCodingException("Unexpected content at the end of chunk");
                }
                this.state = 1;
            }
            case 1: {
                this.buffer.clear();
                final int bytesRead2 = this.in.readLine(this.buffer);
                if (bytesRead2 == -1) {
                    return 0;
                }
                int separator = this.buffer.indexOf(59);
                if (separator < 0) {
                    separator = this.buffer.length();
                }
                try {
                    return Integer.parseInt(this.buffer.substringTrimmed(0, separator), 16);
                }
                catch (NumberFormatException e) {
                    throw new MalformedChunkCodingException("Bad chunk header");
                }
                break;
            }
        }
        throw new IllegalStateException("Inconsistent codec state");
    }
    
    private void parseTrailerHeaders() throws IOException {
        try {
            this.footers = AbstractMessageParser.parseHeaders(this.in, -1, -1, null);
        }
        catch (HttpException ex) {
            final IOException ioe = new MalformedChunkCodingException("Invalid footer: " + ex.getMessage());
            ioe.initCause(ex);
            throw ioe;
        }
    }
    
    @Override
    public void close() throws IOException {
        if (!this.closed) {
            try {
                if (!this.eof) {
                    final byte[] buff = new byte[2048];
                    while (this.read(buff) >= 0) {}
                }
            }
            finally {
                this.eof = true;
                this.closed = true;
            }
        }
    }
    
    public Header[] getFooters() {
        return this.footers.clone();
    }
}
