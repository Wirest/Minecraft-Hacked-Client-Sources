// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.binary;

import java.io.IOException;
import java.io.OutputStream;
import java.io.FilterOutputStream;

public class BaseNCodecOutputStream extends FilterOutputStream
{
    private final boolean doEncode;
    private final BaseNCodec baseNCodec;
    private final byte[] singleByte;
    private final BaseNCodec.Context context;
    
    public BaseNCodecOutputStream(final OutputStream out, final BaseNCodec basedCodec, final boolean doEncode) {
        super(out);
        this.singleByte = new byte[1];
        this.context = new BaseNCodec.Context();
        this.baseNCodec = basedCodec;
        this.doEncode = doEncode;
    }
    
    @Override
    public void write(final int i) throws IOException {
        this.singleByte[0] = (byte)i;
        this.write(this.singleByte, 0, 1);
    }
    
    @Override
    public void write(final byte[] b, final int offset, final int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        }
        if (offset < 0 || len < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (offset > b.length || offset + len > b.length) {
            throw new IndexOutOfBoundsException();
        }
        if (len > 0) {
            if (this.doEncode) {
                this.baseNCodec.encode(b, offset, len, this.context);
            }
            else {
                this.baseNCodec.decode(b, offset, len, this.context);
            }
            this.flush(false);
        }
    }
    
    private void flush(final boolean propagate) throws IOException {
        final int avail = this.baseNCodec.available(this.context);
        if (avail > 0) {
            final byte[] buf = new byte[avail];
            final int c = this.baseNCodec.readResults(buf, 0, avail, this.context);
            if (c > 0) {
                this.out.write(buf, 0, c);
            }
        }
        if (propagate) {
            this.out.flush();
        }
    }
    
    @Override
    public void flush() throws IOException {
        this.flush(true);
    }
    
    @Override
    public void close() throws IOException {
        if (this.doEncode) {
            this.baseNCodec.encode(this.singleByte, 0, -1, this.context);
        }
        else {
            this.baseNCodec.decode(this.singleByte, 0, -1, this.context);
        }
        this.flush();
        this.out.close();
    }
}
