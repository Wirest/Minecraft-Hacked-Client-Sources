// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.binary;

import java.io.IOException;
import java.io.InputStream;
import java.io.FilterInputStream;

public class BaseNCodecInputStream extends FilterInputStream
{
    private final BaseNCodec baseNCodec;
    private final boolean doEncode;
    private final byte[] singleByte;
    private final BaseNCodec.Context context;
    
    protected BaseNCodecInputStream(final InputStream in, final BaseNCodec baseNCodec, final boolean doEncode) {
        super(in);
        this.singleByte = new byte[1];
        this.context = new BaseNCodec.Context();
        this.doEncode = doEncode;
        this.baseNCodec = baseNCodec;
    }
    
    @Override
    public int available() throws IOException {
        return this.context.eof ? 0 : 1;
    }
    
    @Override
    public synchronized void mark(final int readLimit) {
    }
    
    @Override
    public boolean markSupported() {
        return false;
    }
    
    @Override
    public int read() throws IOException {
        int r;
        for (r = this.read(this.singleByte, 0, 1); r == 0; r = this.read(this.singleByte, 0, 1)) {}
        if (r > 0) {
            final byte b = this.singleByte[0];
            return (b < 0) ? (256 + b) : b;
        }
        return -1;
    }
    
    @Override
    public int read(final byte[] b, final int offset, final int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        }
        if (offset < 0 || len < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (offset > b.length || offset + len > b.length) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return 0;
        }
        int readLen;
        for (readLen = 0; readLen == 0; readLen = this.baseNCodec.readResults(b, offset, len, this.context)) {
            if (!this.baseNCodec.hasData(this.context)) {
                final byte[] buf = new byte[this.doEncode ? 4096 : 8192];
                final int c = this.in.read(buf);
                if (this.doEncode) {
                    this.baseNCodec.encode(buf, 0, c, this.context);
                }
                else {
                    this.baseNCodec.decode(buf, 0, c, this.context);
                }
            }
        }
        return readLen;
    }
    
    @Override
    public synchronized void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }
    
    @Override
    public long skip(final long n) throws IOException {
        if (n < 0L) {
            throw new IllegalArgumentException("Negative skip length: " + n);
        }
        final byte[] b = new byte[512];
        long todo;
        int len;
        for (todo = n; todo > 0L; todo -= len) {
            len = (int)Math.min(b.length, todo);
            len = this.read(b, 0, len);
            if (len == -1) {
                break;
            }
        }
        return n - todo;
    }
}
