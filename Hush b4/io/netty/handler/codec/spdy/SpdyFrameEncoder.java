// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

import java.util.Iterator;
import java.util.Set;
import java.nio.ByteOrder;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBuf;

public class SpdyFrameEncoder
{
    private final int version;
    
    public SpdyFrameEncoder(final SpdyVersion spdyVersion) {
        if (spdyVersion == null) {
            throw new NullPointerException("spdyVersion");
        }
        this.version = spdyVersion.getVersion();
    }
    
    private void writeControlFrameHeader(final ByteBuf buffer, final int type, final byte flags, final int length) {
        buffer.writeShort(this.version | 0x8000);
        buffer.writeShort(type);
        buffer.writeByte(flags);
        buffer.writeMedium(length);
    }
    
    public ByteBuf encodeDataFrame(final ByteBufAllocator allocator, final int streamId, final boolean last, final ByteBuf data) {
        final byte flags = (byte)(last ? 1 : 0);
        final int length = data.readableBytes();
        final ByteBuf frame = allocator.ioBuffer(8 + length).order(ByteOrder.BIG_ENDIAN);
        frame.writeInt(streamId & Integer.MAX_VALUE);
        frame.writeByte(flags);
        frame.writeMedium(length);
        frame.writeBytes(data, data.readerIndex(), length);
        return frame;
    }
    
    public ByteBuf encodeSynStreamFrame(final ByteBufAllocator allocator, final int streamId, final int associatedToStreamId, final byte priority, final boolean last, final boolean unidirectional, final ByteBuf headerBlock) {
        final int headerBlockLength = headerBlock.readableBytes();
        byte flags = (byte)(last ? 1 : 0);
        if (unidirectional) {
            flags |= 0x2;
        }
        final int length = 10 + headerBlockLength;
        final ByteBuf frame = allocator.ioBuffer(8 + length).order(ByteOrder.BIG_ENDIAN);
        this.writeControlFrameHeader(frame, 1, flags, length);
        frame.writeInt(streamId);
        frame.writeInt(associatedToStreamId);
        frame.writeShort((priority & 0xFF) << 13);
        frame.writeBytes(headerBlock, headerBlock.readerIndex(), headerBlockLength);
        return frame;
    }
    
    public ByteBuf encodeSynReplyFrame(final ByteBufAllocator allocator, final int streamId, final boolean last, final ByteBuf headerBlock) {
        final int headerBlockLength = headerBlock.readableBytes();
        final byte flags = (byte)(last ? 1 : 0);
        final int length = 4 + headerBlockLength;
        final ByteBuf frame = allocator.ioBuffer(8 + length).order(ByteOrder.BIG_ENDIAN);
        this.writeControlFrameHeader(frame, 2, flags, length);
        frame.writeInt(streamId);
        frame.writeBytes(headerBlock, headerBlock.readerIndex(), headerBlockLength);
        return frame;
    }
    
    public ByteBuf encodeRstStreamFrame(final ByteBufAllocator allocator, final int streamId, final int statusCode) {
        final byte flags = 0;
        final int length = 8;
        final ByteBuf frame = allocator.ioBuffer(8 + length).order(ByteOrder.BIG_ENDIAN);
        this.writeControlFrameHeader(frame, 3, flags, length);
        frame.writeInt(streamId);
        frame.writeInt(statusCode);
        return frame;
    }
    
    public ByteBuf encodeSettingsFrame(final ByteBufAllocator allocator, final SpdySettingsFrame spdySettingsFrame) {
        final Set<Integer> ids = spdySettingsFrame.ids();
        final int numSettings = ids.size();
        byte flags = (byte)(spdySettingsFrame.clearPreviouslyPersistedSettings() ? 1 : 0);
        final int length = 4 + 8 * numSettings;
        final ByteBuf frame = allocator.ioBuffer(8 + length).order(ByteOrder.BIG_ENDIAN);
        this.writeControlFrameHeader(frame, 4, flags, length);
        frame.writeInt(numSettings);
        for (final Integer id : ids) {
            flags = 0;
            if (spdySettingsFrame.isPersistValue(id)) {
                flags |= 0x1;
            }
            if (spdySettingsFrame.isPersisted(id)) {
                flags |= 0x2;
            }
            frame.writeByte(flags);
            frame.writeMedium(id);
            frame.writeInt(spdySettingsFrame.getValue(id));
        }
        return frame;
    }
    
    public ByteBuf encodePingFrame(final ByteBufAllocator allocator, final int id) {
        final byte flags = 0;
        final int length = 4;
        final ByteBuf frame = allocator.ioBuffer(8 + length).order(ByteOrder.BIG_ENDIAN);
        this.writeControlFrameHeader(frame, 6, flags, length);
        frame.writeInt(id);
        return frame;
    }
    
    public ByteBuf encodeGoAwayFrame(final ByteBufAllocator allocator, final int lastGoodStreamId, final int statusCode) {
        final byte flags = 0;
        final int length = 8;
        final ByteBuf frame = allocator.ioBuffer(8 + length).order(ByteOrder.BIG_ENDIAN);
        this.writeControlFrameHeader(frame, 7, flags, length);
        frame.writeInt(lastGoodStreamId);
        frame.writeInt(statusCode);
        return frame;
    }
    
    public ByteBuf encodeHeadersFrame(final ByteBufAllocator allocator, final int streamId, final boolean last, final ByteBuf headerBlock) {
        final int headerBlockLength = headerBlock.readableBytes();
        final byte flags = (byte)(last ? 1 : 0);
        final int length = 4 + headerBlockLength;
        final ByteBuf frame = allocator.ioBuffer(8 + length).order(ByteOrder.BIG_ENDIAN);
        this.writeControlFrameHeader(frame, 8, flags, length);
        frame.writeInt(streamId);
        frame.writeBytes(headerBlock, headerBlock.readerIndex(), headerBlockLength);
        return frame;
    }
    
    public ByteBuf encodeWindowUpdateFrame(final ByteBufAllocator allocator, final int streamId, final int deltaWindowSize) {
        final byte flags = 0;
        final int length = 8;
        final ByteBuf frame = allocator.ioBuffer(8 + length).order(ByteOrder.BIG_ENDIAN);
        this.writeControlFrameHeader(frame, 9, flags, length);
        frame.writeInt(streamId);
        frame.writeInt(deltaWindowSize);
        return frame;
    }
}
