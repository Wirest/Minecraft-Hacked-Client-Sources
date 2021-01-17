// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.compression;

import java.util.zip.DataFormatException;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.zip.CRC32;
import java.util.zip.Inflater;

public class JdkZlibDecoder extends ZlibDecoder
{
    private static final int FHCRC = 2;
    private static final int FEXTRA = 4;
    private static final int FNAME = 8;
    private static final int FCOMMENT = 16;
    private static final int FRESERVED = 224;
    private Inflater inflater;
    private final byte[] dictionary;
    private final CRC32 crc;
    private GzipState gzipState;
    private int flags;
    private int xlen;
    private volatile boolean finished;
    private boolean decideZlibOrNone;
    
    public JdkZlibDecoder() {
        this(ZlibWrapper.ZLIB, null);
    }
    
    public JdkZlibDecoder(final byte[] dictionary) {
        this(ZlibWrapper.ZLIB, dictionary);
    }
    
    public JdkZlibDecoder(final ZlibWrapper wrapper) {
        this(wrapper, null);
    }
    
    private JdkZlibDecoder(final ZlibWrapper wrapper, final byte[] dictionary) {
        this.gzipState = GzipState.HEADER_START;
        this.flags = -1;
        this.xlen = -1;
        if (wrapper == null) {
            throw new NullPointerException("wrapper");
        }
        switch (wrapper) {
            case GZIP: {
                this.inflater = new Inflater(true);
                this.crc = new CRC32();
                break;
            }
            case NONE: {
                this.inflater = new Inflater(true);
                this.crc = null;
                break;
            }
            case ZLIB: {
                this.inflater = new Inflater();
                this.crc = null;
                break;
            }
            case ZLIB_OR_NONE: {
                this.decideZlibOrNone = true;
                this.crc = null;
                break;
            }
            default: {
                throw new IllegalArgumentException("Only GZIP or ZLIB is supported, but you used " + wrapper);
            }
        }
        this.dictionary = dictionary;
    }
    
    @Override
    public boolean isClosed() {
        return this.finished;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        if (this.finished) {
            in.skipBytes(in.readableBytes());
            return;
        }
        if (!in.isReadable()) {
            return;
        }
        if (this.decideZlibOrNone) {
            if (in.readableBytes() < 2) {
                return;
            }
            final boolean nowrap = !looksLikeZlib(in.getShort(0));
            this.inflater = new Inflater(nowrap);
            this.decideZlibOrNone = false;
        }
        if (this.crc != null) {
            switch (this.gzipState) {
                case FOOTER_START: {
                    if (this.readGZIPFooter(in)) {
                        this.finished = true;
                    }
                    return;
                }
                default: {
                    if (this.gzipState != GzipState.HEADER_END && !this.readGZIPHeader(in)) {
                        return;
                    }
                    break;
                }
            }
        }
        final int readableBytes = in.readableBytes();
        if (in.hasArray()) {
            this.inflater.setInput(in.array(), in.arrayOffset() + in.readerIndex(), in.readableBytes());
        }
        else {
            final byte[] array = new byte[in.readableBytes()];
            in.getBytes(in.readerIndex(), array);
            this.inflater.setInput(array);
        }
        final int maxOutputLength = this.inflater.getRemaining() << 1;
        ByteBuf decompressed = ctx.alloc().heapBuffer(maxOutputLength);
        try {
            boolean readFooter = false;
            byte[] outArray = decompressed.array();
            while (!this.inflater.needsInput()) {
                final int writerIndex = decompressed.writerIndex();
                final int outIndex = decompressed.arrayOffset() + writerIndex;
                final int length = decompressed.writableBytes();
                if (length == 0) {
                    out.add(decompressed);
                    decompressed = ctx.alloc().heapBuffer(maxOutputLength);
                    outArray = decompressed.array();
                }
                else {
                    final int outputLength = this.inflater.inflate(outArray, outIndex, length);
                    if (outputLength > 0) {
                        decompressed.writerIndex(writerIndex + outputLength);
                        if (this.crc != null) {
                            this.crc.update(outArray, outIndex, outputLength);
                        }
                    }
                    else if (this.inflater.needsDictionary()) {
                        if (this.dictionary == null) {
                            throw new DecompressionException("decompression failure, unable to set dictionary as non was specified");
                        }
                        this.inflater.setDictionary(this.dictionary);
                    }
                    if (!this.inflater.finished()) {
                        continue;
                    }
                    if (this.crc == null) {
                        this.finished = true;
                        break;
                    }
                    readFooter = true;
                    break;
                }
            }
            in.skipBytes(readableBytes - this.inflater.getRemaining());
            if (readFooter) {
                this.gzipState = GzipState.FOOTER_START;
                if (this.readGZIPFooter(in)) {
                    this.finished = true;
                }
            }
        }
        catch (DataFormatException e) {
            throw new DecompressionException("decompression failure", e);
        }
        finally {
            if (decompressed.isReadable()) {
                out.add(decompressed);
            }
            else {
                decompressed.release();
            }
        }
    }
    
    @Override
    protected void handlerRemoved0(final ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved0(ctx);
        if (this.inflater != null) {
            this.inflater.end();
        }
    }
    
    private boolean readGZIPHeader(final ByteBuf in) {
        switch (this.gzipState) {
            case HEADER_START: {
                if (in.readableBytes() < 10) {
                    return false;
                }
                final int magic0 = in.readByte();
                final int magic2 = in.readByte();
                if (magic0 != 31) {
                    throw new DecompressionException("Input is not in the GZIP format");
                }
                this.crc.update(magic0);
                this.crc.update(magic2);
                final int method = in.readUnsignedByte();
                if (method != 8) {
                    throw new DecompressionException("Unsupported compression method " + method + " in the GZIP header");
                }
                this.crc.update(method);
                this.flags = in.readUnsignedByte();
                this.crc.update(this.flags);
                if ((this.flags & 0xE0) != 0x0) {
                    throw new DecompressionException("Reserved flags are set in the GZIP header");
                }
                this.crc.update(in.readByte());
                this.crc.update(in.readByte());
                this.crc.update(in.readByte());
                this.crc.update(in.readByte());
                this.crc.update(in.readUnsignedByte());
                this.crc.update(in.readUnsignedByte());
                this.gzipState = GzipState.FLG_READ;
            }
            case FLG_READ: {
                if ((this.flags & 0x4) != 0x0) {
                    if (in.readableBytes() < 2) {
                        return false;
                    }
                    final int xlen1 = in.readUnsignedByte();
                    final int xlen2 = in.readUnsignedByte();
                    this.crc.update(xlen1);
                    this.crc.update(xlen2);
                    this.xlen |= (xlen1 << 8 | xlen2);
                }
                this.gzipState = GzipState.XLEN_READ;
            }
            case XLEN_READ: {
                if (this.xlen != -1) {
                    if (in.readableBytes() < this.xlen) {
                        return false;
                    }
                    final byte[] xtra = new byte[this.xlen];
                    in.readBytes(xtra);
                    this.crc.update(xtra);
                }
                this.gzipState = GzipState.SKIP_FNAME;
            }
            case SKIP_FNAME: {
                if ((this.flags & 0x8) != 0x0) {
                    if (!in.isReadable()) {
                        return false;
                    }
                    do {
                        final int b = in.readUnsignedByte();
                        this.crc.update(b);
                        if (b == 0) {
                            break;
                        }
                    } while (in.isReadable());
                }
                this.gzipState = GzipState.SKIP_COMMENT;
            }
            case SKIP_COMMENT: {
                if ((this.flags & 0x10) != 0x0) {
                    if (!in.isReadable()) {
                        return false;
                    }
                    do {
                        final int b = in.readUnsignedByte();
                        this.crc.update(b);
                        if (b == 0) {
                            break;
                        }
                    } while (in.isReadable());
                }
                this.gzipState = GzipState.PROCESS_FHCRC;
            }
            case PROCESS_FHCRC: {
                if ((this.flags & 0x2) != 0x0) {
                    if (in.readableBytes() < 4) {
                        return false;
                    }
                    this.verifyCrc(in);
                }
                this.crc.reset();
                this.gzipState = GzipState.HEADER_END;
            }
            case HEADER_END: {
                return true;
            }
            default: {
                throw new IllegalStateException();
            }
        }
    }
    
    private boolean readGZIPFooter(final ByteBuf buf) {
        if (buf.readableBytes() < 8) {
            return false;
        }
        this.verifyCrc(buf);
        int dataLength = 0;
        for (int i = 0; i < 4; ++i) {
            dataLength |= buf.readUnsignedByte() << i * 8;
        }
        final int readLength = this.inflater.getTotalOut();
        if (dataLength != readLength) {
            throw new DecompressionException("Number of bytes mismatch. Expected: " + dataLength + ", Got: " + readLength);
        }
        return true;
    }
    
    private void verifyCrc(final ByteBuf in) {
        long crcValue = 0L;
        for (int i = 0; i < 4; ++i) {
            crcValue |= (long)in.readUnsignedByte() << i * 8;
        }
        final long readCrc = this.crc.getValue();
        if (crcValue != readCrc) {
            throw new DecompressionException("CRC value missmatch. Expected: " + crcValue + ", Got: " + readCrc);
        }
    }
    
    private static boolean looksLikeZlib(final short cmf_flg) {
        return (cmf_flg & 0x7800) == 0x7800 && cmf_flg % 31 == 0;
    }
    
    private enum GzipState
    {
        HEADER_START, 
        HEADER_END, 
        FLG_READ, 
        XLEN_READ, 
        SKIP_FNAME, 
        SKIP_COMMENT, 
        PROCESS_FHCRC, 
        FOOTER_START;
    }
}
