// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.compression;

import java.util.Arrays;
import io.netty.buffer.ByteBufUtil;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class SnappyFramedDecoder extends ByteToMessageDecoder
{
    private static final byte[] SNAPPY;
    private static final int MAX_UNCOMPRESSED_DATA_SIZE = 65540;
    private final Snappy snappy;
    private final boolean validateChecksums;
    private boolean started;
    private boolean corrupted;
    
    public SnappyFramedDecoder() {
        this(false);
    }
    
    public SnappyFramedDecoder(final boolean validateChecksums) {
        this.snappy = new Snappy();
        this.validateChecksums = validateChecksums;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        if (this.corrupted) {
            in.skipBytes(in.readableBytes());
            return;
        }
        try {
            final int idx = in.readerIndex();
            final int inSize = in.readableBytes();
            if (inSize < 4) {
                return;
            }
            final int chunkTypeVal = in.getUnsignedByte(idx);
            final ChunkType chunkType = mapChunkType((byte)chunkTypeVal);
            final int chunkLength = ByteBufUtil.swapMedium(in.getUnsignedMedium(idx + 1));
            switch (chunkType) {
                case STREAM_IDENTIFIER: {
                    if (chunkLength != SnappyFramedDecoder.SNAPPY.length) {
                        throw new DecompressionException("Unexpected length of stream identifier: " + chunkLength);
                    }
                    if (inSize < 4 + SnappyFramedDecoder.SNAPPY.length) {
                        break;
                    }
                    final byte[] identifier = new byte[chunkLength];
                    in.skipBytes(4).readBytes(identifier);
                    if (!Arrays.equals(identifier, SnappyFramedDecoder.SNAPPY)) {
                        throw new DecompressionException("Unexpected stream identifier contents. Mismatched snappy protocol version?");
                    }
                    this.started = true;
                    break;
                }
                case RESERVED_SKIPPABLE: {
                    if (!this.started) {
                        throw new DecompressionException("Received RESERVED_SKIPPABLE tag before STREAM_IDENTIFIER");
                    }
                    if (inSize < 4 + chunkLength) {
                        return;
                    }
                    in.skipBytes(4 + chunkLength);
                    break;
                }
                case RESERVED_UNSKIPPABLE: {
                    throw new DecompressionException("Found reserved unskippable chunk type: 0x" + Integer.toHexString(chunkTypeVal));
                }
                case UNCOMPRESSED_DATA: {
                    if (!this.started) {
                        throw new DecompressionException("Received UNCOMPRESSED_DATA tag before STREAM_IDENTIFIER");
                    }
                    if (chunkLength > 65540) {
                        throw new DecompressionException("Received UNCOMPRESSED_DATA larger than 65540 bytes");
                    }
                    if (inSize < 4 + chunkLength) {
                        return;
                    }
                    in.skipBytes(4);
                    if (this.validateChecksums) {
                        final int checksum = ByteBufUtil.swapInt(in.readInt());
                        Snappy.validateChecksum(checksum, in, in.readerIndex(), chunkLength - 4);
                    }
                    else {
                        in.skipBytes(4);
                    }
                    out.add(in.readSlice(chunkLength - 4).retain());
                    break;
                }
                case COMPRESSED_DATA: {
                    if (!this.started) {
                        throw new DecompressionException("Received COMPRESSED_DATA tag before STREAM_IDENTIFIER");
                    }
                    if (inSize < 4 + chunkLength) {
                        return;
                    }
                    in.skipBytes(4);
                    final int checksum = ByteBufUtil.swapInt(in.readInt());
                    final ByteBuf uncompressed = ctx.alloc().buffer(0);
                    if (this.validateChecksums) {
                        final int oldWriterIndex = in.writerIndex();
                        try {
                            in.writerIndex(in.readerIndex() + chunkLength - 4);
                            this.snappy.decode(in, uncompressed);
                        }
                        finally {
                            in.writerIndex(oldWriterIndex);
                        }
                        Snappy.validateChecksum(checksum, uncompressed, 0, uncompressed.writerIndex());
                    }
                    else {
                        this.snappy.decode(in.readSlice(chunkLength - 4), uncompressed);
                    }
                    out.add(uncompressed);
                    this.snappy.reset();
                    break;
                }
            }
        }
        catch (Exception e) {
            this.corrupted = true;
            throw e;
        }
    }
    
    private static ChunkType mapChunkType(final byte type) {
        if (type == 0) {
            return ChunkType.COMPRESSED_DATA;
        }
        if (type == 1) {
            return ChunkType.UNCOMPRESSED_DATA;
        }
        if (type == -1) {
            return ChunkType.STREAM_IDENTIFIER;
        }
        if ((type & 0x80) == 0x80) {
            return ChunkType.RESERVED_SKIPPABLE;
        }
        return ChunkType.RESERVED_UNSKIPPABLE;
    }
    
    static {
        SNAPPY = new byte[] { 115, 78, 97, 80, 112, 89 };
    }
    
    private enum ChunkType
    {
        STREAM_IDENTIFIER, 
        COMPRESSED_DATA, 
        UNCOMPRESSED_DATA, 
        RESERVED_UNSKIPPABLE, 
        RESERVED_SKIPPABLE;
    }
}
