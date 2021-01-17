// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.compression.CompressionException;
import com.jcraft.jzlib.JZlib;
import com.jcraft.jzlib.Deflater;

class SpdyHeaderBlockJZlibEncoder extends SpdyHeaderBlockRawEncoder
{
    private final Deflater z;
    private boolean finished;
    
    SpdyHeaderBlockJZlibEncoder(final SpdyVersion version, final int compressionLevel, final int windowBits, final int memLevel) {
        super(version);
        this.z = new Deflater();
        if (compressionLevel < 0 || compressionLevel > 9) {
            throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
        }
        if (windowBits < 9 || windowBits > 15) {
            throw new IllegalArgumentException("windowBits: " + windowBits + " (expected: 9-15)");
        }
        if (memLevel < 1 || memLevel > 9) {
            throw new IllegalArgumentException("memLevel: " + memLevel + " (expected: 1-9)");
        }
        int resultCode = this.z.deflateInit(compressionLevel, windowBits, memLevel, JZlib.W_ZLIB);
        if (resultCode != 0) {
            throw new CompressionException("failed to initialize an SPDY header block deflater: " + resultCode);
        }
        resultCode = this.z.deflateSetDictionary(SpdyCodecUtil.SPDY_DICT, SpdyCodecUtil.SPDY_DICT.length);
        if (resultCode != 0) {
            throw new CompressionException("failed to set the SPDY dictionary: " + resultCode);
        }
    }
    
    private void setInput(final ByteBuf decompressed) {
        final byte[] in = new byte[decompressed.readableBytes()];
        decompressed.readBytes(in);
        this.z.next_in = in;
        this.z.next_in_index = 0;
        this.z.avail_in = in.length;
    }
    
    private void encode(final ByteBuf compressed) {
        try {
            final byte[] out = new byte[(int)Math.ceil(this.z.next_in.length * 1.001) + 12];
            this.z.next_out = out;
            this.z.next_out_index = 0;
            this.z.avail_out = out.length;
            final int resultCode = this.z.deflate(2);
            if (resultCode != 0) {
                throw new CompressionException("compression failure: " + resultCode);
            }
            if (this.z.next_out_index != 0) {
                compressed.writeBytes(out, 0, this.z.next_out_index);
            }
        }
        finally {
            this.z.next_in = null;
            this.z.next_out = null;
        }
    }
    
    @Override
    public ByteBuf encode(final SpdyHeadersFrame frame) throws Exception {
        if (frame == null) {
            throw new IllegalArgumentException("frame");
        }
        if (this.finished) {
            return Unpooled.EMPTY_BUFFER;
        }
        final ByteBuf decompressed = super.encode(frame);
        if (decompressed.readableBytes() == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        final ByteBuf compressed = decompressed.alloc().buffer();
        this.setInput(decompressed);
        this.encode(compressed);
        return compressed;
    }
    
    public void end() {
        if (this.finished) {
            return;
        }
        this.finished = true;
        this.z.deflateEnd();
        this.z.next_in = null;
        this.z.next_out = null;
    }
}
