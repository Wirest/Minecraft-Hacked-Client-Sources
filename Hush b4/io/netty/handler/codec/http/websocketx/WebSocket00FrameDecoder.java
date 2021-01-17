// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http.websocketx;

import io.netty.handler.codec.TooLongFrameException;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class WebSocket00FrameDecoder extends ReplayingDecoder<Void> implements WebSocketFrameDecoder
{
    static final int DEFAULT_MAX_FRAME_SIZE = 16384;
    private final long maxFrameSize;
    private boolean receivedClosingHandshake;
    
    public WebSocket00FrameDecoder() {
        this(16384);
    }
    
    public WebSocket00FrameDecoder(final int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        if (this.receivedClosingHandshake) {
            in.skipBytes(this.actualReadableBytes());
            return;
        }
        final byte type = in.readByte();
        WebSocketFrame frame;
        if ((type & 0x80) == 0x80) {
            frame = this.decodeBinaryFrame(ctx, type, in);
        }
        else {
            frame = this.decodeTextFrame(ctx, in);
        }
        if (frame != null) {
            out.add(frame);
        }
    }
    
    private WebSocketFrame decodeBinaryFrame(final ChannelHandlerContext ctx, final byte type, final ByteBuf buffer) {
        long frameSize = 0L;
        int lengthFieldSize = 0;
        byte b;
        do {
            b = buffer.readByte();
            frameSize <<= 7;
            frameSize |= (b & 0x7F);
            if (frameSize > this.maxFrameSize) {
                throw new TooLongFrameException();
            }
            if (++lengthFieldSize > 8) {
                throw new TooLongFrameException();
            }
        } while ((b & 0x80) == 0x80);
        if (type == -1 && frameSize == 0L) {
            this.receivedClosingHandshake = true;
            return new CloseWebSocketFrame();
        }
        final ByteBuf payload = ctx.alloc().buffer((int)frameSize);
        buffer.readBytes(payload);
        return new BinaryWebSocketFrame(payload);
    }
    
    private WebSocketFrame decodeTextFrame(final ChannelHandlerContext ctx, final ByteBuf buffer) {
        final int ridx = buffer.readerIndex();
        final int rbytes = this.actualReadableBytes();
        final int delimPos = buffer.indexOf(ridx, ridx + rbytes, (byte)(-1));
        if (delimPos == -1) {
            if (rbytes > this.maxFrameSize) {
                throw new TooLongFrameException();
            }
            return null;
        }
        else {
            final int frameSize = delimPos - ridx;
            if (frameSize > this.maxFrameSize) {
                throw new TooLongFrameException();
            }
            final ByteBuf binaryData = ctx.alloc().buffer(frameSize);
            buffer.readBytes(binaryData);
            buffer.skipBytes(1);
            final int ffDelimPos = binaryData.indexOf(binaryData.readerIndex(), binaryData.writerIndex(), (byte)(-1));
            if (ffDelimPos >= 0) {
                throw new IllegalArgumentException("a text frame should not contain 0xFF.");
            }
            return new TextWebSocketFrame(binaryData);
        }
    }
}
