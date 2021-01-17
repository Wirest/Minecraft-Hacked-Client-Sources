// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;

public class SpdyFrameDecoder
{
    private final int spdyVersion;
    private final int maxChunkSize;
    private final SpdyFrameDecoderDelegate delegate;
    private State state;
    private byte flags;
    private int length;
    private int streamId;
    private int numSettings;
    
    public SpdyFrameDecoder(final SpdyVersion spdyVersion, final SpdyFrameDecoderDelegate delegate) {
        this(spdyVersion, delegate, 8192);
    }
    
    public SpdyFrameDecoder(final SpdyVersion spdyVersion, final SpdyFrameDecoderDelegate delegate, final int maxChunkSize) {
        if (spdyVersion == null) {
            throw new NullPointerException("spdyVersion");
        }
        if (delegate == null) {
            throw new NullPointerException("delegate");
        }
        if (maxChunkSize <= 0) {
            throw new IllegalArgumentException("maxChunkSize must be a positive integer: " + maxChunkSize);
        }
        this.spdyVersion = spdyVersion.getVersion();
        this.delegate = delegate;
        this.maxChunkSize = maxChunkSize;
        this.state = State.READ_COMMON_HEADER;
    }
    
    public void decode(final ByteBuf buffer) {
        while (true) {
            switch (this.state) {
                case READ_COMMON_HEADER: {
                    if (buffer.readableBytes() < 8) {
                        return;
                    }
                    final int frameOffset = buffer.readerIndex();
                    final int flagsOffset = frameOffset + 4;
                    final int lengthOffset = frameOffset + 5;
                    buffer.skipBytes(8);
                    final boolean control = (buffer.getByte(frameOffset) & 0x80) != 0x0;
                    int version;
                    int type;
                    if (control) {
                        version = (SpdyCodecUtil.getUnsignedShort(buffer, frameOffset) & 0x7FFF);
                        type = SpdyCodecUtil.getUnsignedShort(buffer, frameOffset + 2);
                        this.streamId = 0;
                    }
                    else {
                        version = this.spdyVersion;
                        type = 0;
                        this.streamId = SpdyCodecUtil.getUnsignedInt(buffer, frameOffset);
                    }
                    this.flags = buffer.getByte(flagsOffset);
                    this.length = SpdyCodecUtil.getUnsignedMedium(buffer, lengthOffset);
                    if (version != this.spdyVersion) {
                        this.state = State.FRAME_ERROR;
                        this.delegate.readFrameError("Invalid SPDY Version");
                        continue;
                    }
                    if (!isValidFrameHeader(this.streamId, type, this.flags, this.length)) {
                        this.state = State.FRAME_ERROR;
                        this.delegate.readFrameError("Invalid Frame Error");
                        continue;
                    }
                    this.state = getNextState(type, this.length);
                    continue;
                }
                case READ_DATA_FRAME: {
                    if (this.length == 0) {
                        this.state = State.READ_COMMON_HEADER;
                        this.delegate.readDataFrame(this.streamId, hasFlag(this.flags, (byte)1), Unpooled.buffer(0));
                        continue;
                    }
                    final int dataLength = Math.min(this.maxChunkSize, this.length);
                    if (buffer.readableBytes() < dataLength) {
                        return;
                    }
                    final ByteBuf data = buffer.alloc().buffer(dataLength);
                    data.writeBytes(buffer, dataLength);
                    this.length -= dataLength;
                    if (this.length == 0) {
                        this.state = State.READ_COMMON_HEADER;
                    }
                    final boolean last = this.length == 0 && hasFlag(this.flags, (byte)1);
                    this.delegate.readDataFrame(this.streamId, last, data);
                    continue;
                }
                case READ_SYN_STREAM_FRAME: {
                    if (buffer.readableBytes() < 10) {
                        return;
                    }
                    final int offset = buffer.readerIndex();
                    this.streamId = SpdyCodecUtil.getUnsignedInt(buffer, offset);
                    final int associatedToStreamId = SpdyCodecUtil.getUnsignedInt(buffer, offset + 4);
                    final byte priority = (byte)(buffer.getByte(offset + 8) >> 5 & 0x7);
                    final boolean last = hasFlag(this.flags, (byte)1);
                    final boolean unidirectional = hasFlag(this.flags, (byte)2);
                    buffer.skipBytes(10);
                    this.length -= 10;
                    if (this.streamId == 0) {
                        this.state = State.FRAME_ERROR;
                        this.delegate.readFrameError("Invalid SYN_STREAM Frame");
                        continue;
                    }
                    this.state = State.READ_HEADER_BLOCK;
                    this.delegate.readSynStreamFrame(this.streamId, associatedToStreamId, priority, last, unidirectional);
                    continue;
                }
                case READ_SYN_REPLY_FRAME: {
                    if (buffer.readableBytes() < 4) {
                        return;
                    }
                    this.streamId = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex());
                    final boolean last = hasFlag(this.flags, (byte)1);
                    buffer.skipBytes(4);
                    this.length -= 4;
                    if (this.streamId == 0) {
                        this.state = State.FRAME_ERROR;
                        this.delegate.readFrameError("Invalid SYN_REPLY Frame");
                        continue;
                    }
                    this.state = State.READ_HEADER_BLOCK;
                    this.delegate.readSynReplyFrame(this.streamId, last);
                    continue;
                }
                case READ_RST_STREAM_FRAME: {
                    if (buffer.readableBytes() < 8) {
                        return;
                    }
                    this.streamId = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex());
                    final int statusCode = SpdyCodecUtil.getSignedInt(buffer, buffer.readerIndex() + 4);
                    buffer.skipBytes(8);
                    if (this.streamId == 0 || statusCode == 0) {
                        this.state = State.FRAME_ERROR;
                        this.delegate.readFrameError("Invalid RST_STREAM Frame");
                        continue;
                    }
                    this.state = State.READ_COMMON_HEADER;
                    this.delegate.readRstStreamFrame(this.streamId, statusCode);
                    continue;
                }
                case READ_SETTINGS_FRAME: {
                    if (buffer.readableBytes() < 4) {
                        return;
                    }
                    final boolean clear = hasFlag(this.flags, (byte)1);
                    this.numSettings = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex());
                    buffer.skipBytes(4);
                    this.length -= 4;
                    if ((this.length & 0x7) != 0x0 || this.length >> 3 != this.numSettings) {
                        this.state = State.FRAME_ERROR;
                        this.delegate.readFrameError("Invalid SETTINGS Frame");
                        continue;
                    }
                    this.state = State.READ_SETTING;
                    this.delegate.readSettingsFrame(clear);
                    continue;
                }
                case READ_SETTING: {
                    if (this.numSettings == 0) {
                        this.state = State.READ_COMMON_HEADER;
                        this.delegate.readSettingsEnd();
                        continue;
                    }
                    if (buffer.readableBytes() < 8) {
                        return;
                    }
                    final byte settingsFlags = buffer.getByte(buffer.readerIndex());
                    final int id = SpdyCodecUtil.getUnsignedMedium(buffer, buffer.readerIndex() + 1);
                    final int value = SpdyCodecUtil.getSignedInt(buffer, buffer.readerIndex() + 4);
                    final boolean persistValue = hasFlag(settingsFlags, (byte)1);
                    final boolean persisted = hasFlag(settingsFlags, (byte)2);
                    buffer.skipBytes(8);
                    --this.numSettings;
                    this.delegate.readSetting(id, value, persistValue, persisted);
                    continue;
                }
                case READ_PING_FRAME: {
                    if (buffer.readableBytes() < 4) {
                        return;
                    }
                    final int pingId = SpdyCodecUtil.getSignedInt(buffer, buffer.readerIndex());
                    buffer.skipBytes(4);
                    this.state = State.READ_COMMON_HEADER;
                    this.delegate.readPingFrame(pingId);
                    continue;
                }
                case READ_GOAWAY_FRAME: {
                    if (buffer.readableBytes() < 8) {
                        return;
                    }
                    final int lastGoodStreamId = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex());
                    final int statusCode = SpdyCodecUtil.getSignedInt(buffer, buffer.readerIndex() + 4);
                    buffer.skipBytes(8);
                    this.state = State.READ_COMMON_HEADER;
                    this.delegate.readGoAwayFrame(lastGoodStreamId, statusCode);
                    continue;
                }
                case READ_HEADERS_FRAME: {
                    if (buffer.readableBytes() < 4) {
                        return;
                    }
                    this.streamId = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex());
                    final boolean last = hasFlag(this.flags, (byte)1);
                    buffer.skipBytes(4);
                    this.length -= 4;
                    if (this.streamId == 0) {
                        this.state = State.FRAME_ERROR;
                        this.delegate.readFrameError("Invalid HEADERS Frame");
                        continue;
                    }
                    this.state = State.READ_HEADER_BLOCK;
                    this.delegate.readHeadersFrame(this.streamId, last);
                    continue;
                }
                case READ_WINDOW_UPDATE_FRAME: {
                    if (buffer.readableBytes() < 8) {
                        return;
                    }
                    this.streamId = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex());
                    final int deltaWindowSize = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex() + 4);
                    buffer.skipBytes(8);
                    if (deltaWindowSize == 0) {
                        this.state = State.FRAME_ERROR;
                        this.delegate.readFrameError("Invalid WINDOW_UPDATE Frame");
                        continue;
                    }
                    this.state = State.READ_COMMON_HEADER;
                    this.delegate.readWindowUpdateFrame(this.streamId, deltaWindowSize);
                    continue;
                }
                case READ_HEADER_BLOCK: {
                    if (this.length == 0) {
                        this.state = State.READ_COMMON_HEADER;
                        this.delegate.readHeaderBlockEnd();
                        continue;
                    }
                    if (!buffer.isReadable()) {
                        return;
                    }
                    final int compressedBytes = Math.min(buffer.readableBytes(), this.length);
                    final ByteBuf headerBlock = buffer.alloc().buffer(compressedBytes);
                    headerBlock.writeBytes(buffer, compressedBytes);
                    this.length -= compressedBytes;
                    this.delegate.readHeaderBlock(headerBlock);
                    continue;
                }
                case DISCARD_FRAME: {
                    final int numBytes = Math.min(buffer.readableBytes(), this.length);
                    buffer.skipBytes(numBytes);
                    this.length -= numBytes;
                    if (this.length == 0) {
                        this.state = State.READ_COMMON_HEADER;
                        continue;
                    }
                }
                case FRAME_ERROR: {
                    buffer.skipBytes(buffer.readableBytes());
                }
                default: {
                    throw new Error("Shouldn't reach here.");
                }
            }
        }
    }
    
    private static boolean hasFlag(final byte flags, final byte flag) {
        return (flags & flag) != 0x0;
    }
    
    private static State getNextState(final int type, final int length) {
        switch (type) {
            case 0: {
                return State.READ_DATA_FRAME;
            }
            case 1: {
                return State.READ_SYN_STREAM_FRAME;
            }
            case 2: {
                return State.READ_SYN_REPLY_FRAME;
            }
            case 3: {
                return State.READ_RST_STREAM_FRAME;
            }
            case 4: {
                return State.READ_SETTINGS_FRAME;
            }
            case 6: {
                return State.READ_PING_FRAME;
            }
            case 7: {
                return State.READ_GOAWAY_FRAME;
            }
            case 8: {
                return State.READ_HEADERS_FRAME;
            }
            case 9: {
                return State.READ_WINDOW_UPDATE_FRAME;
            }
            default: {
                if (length != 0) {
                    return State.DISCARD_FRAME;
                }
                return State.READ_COMMON_HEADER;
            }
        }
    }
    
    private static boolean isValidFrameHeader(final int streamId, final int type, final byte flags, final int length) {
        switch (type) {
            case 0: {
                return streamId != 0;
            }
            case 1: {
                return length >= 10;
            }
            case 2: {
                return length >= 4;
            }
            case 3: {
                return flags == 0 && length == 8;
            }
            case 4: {
                return length >= 4;
            }
            case 6: {
                return length == 4;
            }
            case 7: {
                return length == 8;
            }
            case 8: {
                return length >= 4;
            }
            case 9: {
                return length == 8;
            }
            default: {
                return true;
            }
        }
    }
    
    private enum State
    {
        READ_COMMON_HEADER, 
        READ_DATA_FRAME, 
        READ_SYN_STREAM_FRAME, 
        READ_SYN_REPLY_FRAME, 
        READ_RST_STREAM_FRAME, 
        READ_SETTINGS_FRAME, 
        READ_SETTING, 
        READ_PING_FRAME, 
        READ_GOAWAY_FRAME, 
        READ_HEADERS_FRAME, 
        READ_WINDOW_UPDATE_FRAME, 
        READ_HEADER_BLOCK, 
        DISCARD_FRAME, 
        FRAME_ERROR;
    }
}
