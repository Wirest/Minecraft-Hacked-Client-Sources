// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.Unpooled;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.MessageToMessageEncoder;

@ChannelHandler.Sharable
public class WebSocket00FrameEncoder extends MessageToMessageEncoder<WebSocketFrame> implements WebSocketFrameEncoder
{
    private static final ByteBuf _0X00;
    private static final ByteBuf _0XFF;
    private static final ByteBuf _0XFF_0X00;
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final WebSocketFrame msg, final List<Object> out) throws Exception {
        if (msg instanceof TextWebSocketFrame) {
            final ByteBuf data = msg.content();
            out.add(WebSocket00FrameEncoder._0X00.duplicate());
            out.add(data.retain());
            out.add(WebSocket00FrameEncoder._0XFF.duplicate());
        }
        else if (msg instanceof CloseWebSocketFrame) {
            out.add(WebSocket00FrameEncoder._0XFF_0X00.duplicate());
        }
        else {
            final ByteBuf data = msg.content();
            final int dataLen = data.readableBytes();
            final ByteBuf buf = ctx.alloc().buffer(5);
            boolean release = true;
            try {
                buf.writeByte(-128);
                final int b1 = dataLen >>> 28 & 0x7F;
                final int b2 = dataLen >>> 14 & 0x7F;
                final int b3 = dataLen >>> 7 & 0x7F;
                final int b4 = dataLen & 0x7F;
                if (b1 == 0) {
                    if (b2 == 0) {
                        if (b3 == 0) {
                            buf.writeByte(b4);
                        }
                        else {
                            buf.writeByte(b3 | 0x80);
                            buf.writeByte(b4);
                        }
                    }
                    else {
                        buf.writeByte(b2 | 0x80);
                        buf.writeByte(b3 | 0x80);
                        buf.writeByte(b4);
                    }
                }
                else {
                    buf.writeByte(b1 | 0x80);
                    buf.writeByte(b2 | 0x80);
                    buf.writeByte(b3 | 0x80);
                    buf.writeByte(b4);
                }
                out.add(buf);
                out.add(data.retain());
                release = false;
            }
            finally {
                if (release) {
                    buf.release();
                }
            }
        }
    }
    
    static {
        _0X00 = Unpooled.unreleasableBuffer(Unpooled.directBuffer(1, 1).writeByte(0));
        _0XFF = Unpooled.unreleasableBuffer(Unpooled.directBuffer(1, 1).writeByte(-1));
        _0XFF_0X00 = Unpooled.unreleasableBuffer(Unpooled.directBuffer(2, 2).writeByte(-1).writeByte(0));
    }
}
