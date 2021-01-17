// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.buffer.CompositeByteBuf;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class WebSocketFrameAggregator extends MessageToMessageDecoder<WebSocketFrame>
{
    private final int maxFrameSize;
    private WebSocketFrame currentFrame;
    private boolean tooLongFrameFound;
    
    public WebSocketFrameAggregator(final int maxFrameSize) {
        if (maxFrameSize < 1) {
            throw new IllegalArgumentException("maxFrameSize must be > 0");
        }
        this.maxFrameSize = maxFrameSize;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final WebSocketFrame msg, final List<Object> out) throws Exception {
        if (this.currentFrame == null) {
            this.tooLongFrameFound = false;
            if (msg.isFinalFragment()) {
                out.add(msg.retain());
                return;
            }
            final ByteBuf buf = ctx.alloc().compositeBuffer().addComponent(msg.content().retain());
            buf.writerIndex(buf.writerIndex() + msg.content().readableBytes());
            if (msg instanceof TextWebSocketFrame) {
                this.currentFrame = new TextWebSocketFrame(true, msg.rsv(), buf);
            }
            else {
                if (!(msg instanceof BinaryWebSocketFrame)) {
                    buf.release();
                    throw new IllegalStateException("WebSocket frame was not of type TextWebSocketFrame or BinaryWebSocketFrame");
                }
                this.currentFrame = new BinaryWebSocketFrame(true, msg.rsv(), buf);
            }
        }
        else {
            if (!(msg instanceof ContinuationWebSocketFrame)) {
                out.add(msg.retain());
                return;
            }
            if (this.tooLongFrameFound) {
                if (msg.isFinalFragment()) {
                    this.currentFrame = null;
                }
                return;
            }
            final CompositeByteBuf content = (CompositeByteBuf)this.currentFrame.content();
            if (content.readableBytes() > this.maxFrameSize - msg.content().readableBytes()) {
                this.currentFrame.release();
                this.tooLongFrameFound = true;
                throw new TooLongFrameException("WebSocketFrame length exceeded " + content + " bytes.");
            }
            content.addComponent(msg.content().retain());
            content.writerIndex(content.writerIndex() + msg.content().readableBytes());
            if (msg.isFinalFragment()) {
                final WebSocketFrame currentFrame = this.currentFrame;
                this.currentFrame = null;
                out.add(currentFrame);
            }
        }
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        if (this.currentFrame != null) {
            this.currentFrame.release();
            this.currentFrame = null;
        }
    }
    
    @Override
    public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        if (this.currentFrame != null) {
            this.currentFrame.release();
            this.currentFrame = null;
        }
    }
}
