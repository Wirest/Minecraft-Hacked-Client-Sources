// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToByteEncoder;

public class SnappyFramedEncoder extends MessageToByteEncoder<ByteBuf>
{
    private static final int MIN_COMPRESSIBLE_LENGTH = 18;
    private static final byte[] STREAM_START;
    private final Snappy snappy;
    private boolean started;
    
    public SnappyFramedEncoder() {
        this.snappy = new Snappy();
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final ByteBuf in, final ByteBuf out) throws Exception {
        if (!in.isReadable()) {
            return;
        }
        if (!this.started) {
            this.started = true;
            out.writeBytes(SnappyFramedEncoder.STREAM_START);
        }
        int dataLength = in.readableBytes();
        if (dataLength > 18) {
            while (true) {
                final int lengthIdx = out.writerIndex() + 1;
                if (dataLength < 18) {
                    final ByteBuf slice = in.readSlice(dataLength);
                    writeUnencodedChunk(slice, out, dataLength);
                    break;
                }
                out.writeInt(0);
                if (dataLength <= 32767) {
                    final ByteBuf slice = in.readSlice(dataLength);
                    calculateAndWriteChecksum(slice, out);
                    this.snappy.encode(slice, out, dataLength);
                    setChunkLength(out, lengthIdx);
                    break;
                }
                final ByteBuf slice = in.readSlice(32767);
                calculateAndWriteChecksum(slice, out);
                this.snappy.encode(slice, out, 32767);
                setChunkLength(out, lengthIdx);
                dataLength -= 32767;
            }
        }
        else {
            writeUnencodedChunk(in, out, dataLength);
        }
    }
    
    private static void writeUnencodedChunk(final ByteBuf in, final ByteBuf out, final int dataLength) {
        out.writeByte(1);
        writeChunkLength(out, dataLength + 4);
        calculateAndWriteChecksum(in, out);
        out.writeBytes(in, dataLength);
    }
    
    private static void setChunkLength(final ByteBuf out, final int lengthIdx) {
        final int chunkLength = out.writerIndex() - lengthIdx - 3;
        if (chunkLength >>> 24 != 0) {
            throw new CompressionException("compressed data too large: " + chunkLength);
        }
        out.setMedium(lengthIdx, ByteBufUtil.swapMedium(chunkLength));
    }
    
    private static void writeChunkLength(final ByteBuf out, final int chunkLength) {
        out.writeMedium(ByteBufUtil.swapMedium(chunkLength));
    }
    
    private static void calculateAndWriteChecksum(final ByteBuf slice, final ByteBuf out) {
        out.writeInt(ByteBufUtil.swapInt(Snappy.calculateChecksum(slice)));
    }
    
    static {
        STREAM_START = new byte[] { -1, 6, 0, 0, 115, 78, 97, 80, 112, 89 };
    }
}
