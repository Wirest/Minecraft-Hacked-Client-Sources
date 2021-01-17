// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

import java.util.zip.DataFormatException;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBuf;
import java.util.zip.Inflater;

final class SpdyHeaderBlockZlibDecoder extends SpdyHeaderBlockRawDecoder
{
    private static final int DEFAULT_BUFFER_CAPACITY = 4096;
    private static final SpdyProtocolException INVALID_HEADER_BLOCK;
    private final Inflater decompressor;
    private ByteBuf decompressed;
    
    SpdyHeaderBlockZlibDecoder(final SpdyVersion spdyVersion, final int maxHeaderSize) {
        super(spdyVersion, maxHeaderSize);
        this.decompressor = new Inflater();
    }
    
    @Override
    void decode(final ByteBuf headerBlock, final SpdyHeadersFrame frame) throws Exception {
        final int len = this.setInput(headerBlock);
        int numBytes;
        do {
            numBytes = this.decompress(headerBlock.alloc(), frame);
        } while (numBytes > 0);
        if (this.decompressor.getRemaining() != 0) {
            throw SpdyHeaderBlockZlibDecoder.INVALID_HEADER_BLOCK;
        }
        headerBlock.skipBytes(len);
    }
    
    private int setInput(final ByteBuf compressed) {
        final int len = compressed.readableBytes();
        if (compressed.hasArray()) {
            this.decompressor.setInput(compressed.array(), compressed.arrayOffset() + compressed.readerIndex(), len);
        }
        else {
            final byte[] in = new byte[len];
            compressed.getBytes(compressed.readerIndex(), in);
            this.decompressor.setInput(in, 0, in.length);
        }
        return len;
    }
    
    private int decompress(final ByteBufAllocator alloc, final SpdyHeadersFrame frame) throws Exception {
        this.ensureBuffer(alloc);
        final byte[] out = this.decompressed.array();
        final int off = this.decompressed.arrayOffset() + this.decompressed.writerIndex();
        try {
            int numBytes = this.decompressor.inflate(out, off, this.decompressed.writableBytes());
            if (numBytes == 0 && this.decompressor.needsDictionary()) {
                try {
                    this.decompressor.setDictionary(SpdyCodecUtil.SPDY_DICT);
                }
                catch (IllegalArgumentException ignored) {
                    throw SpdyHeaderBlockZlibDecoder.INVALID_HEADER_BLOCK;
                }
                numBytes = this.decompressor.inflate(out, off, this.decompressed.writableBytes());
            }
            if (frame != null) {
                this.decompressed.writerIndex(this.decompressed.writerIndex() + numBytes);
                this.decodeHeaderBlock(this.decompressed, frame);
                this.decompressed.discardReadBytes();
            }
            return numBytes;
        }
        catch (DataFormatException e) {
            throw new SpdyProtocolException("Received invalid header block", e);
        }
    }
    
    private void ensureBuffer(final ByteBufAllocator alloc) {
        if (this.decompressed == null) {
            this.decompressed = alloc.heapBuffer(4096);
        }
        this.decompressed.ensureWritable(1);
    }
    
    @Override
    void endHeaderBlock(final SpdyHeadersFrame frame) throws Exception {
        super.endHeaderBlock(frame);
        this.releaseBuffer();
    }
    
    public void end() {
        super.end();
        this.releaseBuffer();
        this.decompressor.end();
    }
    
    private void releaseBuffer() {
        if (this.decompressed != null) {
            this.decompressed.release();
            this.decompressed = null;
        }
    }
    
    static {
        INVALID_HEADER_BLOCK = new SpdyProtocolException("Invalid Header Block");
    }
}
