// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http.websocketx;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

class WebSocketClientProtocolHandshakeHandler extends ChannelInboundHandlerAdapter
{
    private final WebSocketClientHandshaker handshaker;
    
    WebSocketClientProtocolHandshakeHandler(final WebSocketClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }
    
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.handshaker.handshake(ctx.channel()).addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    ctx.fireExceptionCaught(future.cause());
                }
                else {
                    ctx.fireUserEventTriggered(WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_ISSUED);
                }
            }
        });
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        if (!(msg instanceof FullHttpResponse)) {
            ctx.fireChannelRead(msg);
            return;
        }
        if (!this.handshaker.isHandshakeComplete()) {
            this.handshaker.finishHandshake(ctx.channel(), (FullHttpResponse)msg);
            ctx.fireUserEventTriggered(WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE);
            ctx.pipeline().remove(this);
            return;
        }
        throw new IllegalStateException("WebSocketClientHandshaker should have been non finished yet");
    }
}
