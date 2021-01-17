// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http.websocketx;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelHandler;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaders;
import java.net.URI;

public class WebSocketClientProtocolHandler extends WebSocketProtocolHandler
{
    private final WebSocketClientHandshaker handshaker;
    private final boolean handleCloseFrames;
    
    public WebSocketClientProtocolHandler(final URI webSocketURL, final WebSocketVersion version, final String subprotocol, final boolean allowExtensions, final HttpHeaders customHeaders, final int maxFramePayloadLength, final boolean handleCloseFrames) {
        this(WebSocketClientHandshakerFactory.newHandshaker(webSocketURL, version, subprotocol, allowExtensions, customHeaders, maxFramePayloadLength), handleCloseFrames);
    }
    
    public WebSocketClientProtocolHandler(final URI webSocketURL, final WebSocketVersion version, final String subprotocol, final boolean allowExtensions, final HttpHeaders customHeaders, final int maxFramePayloadLength) {
        this(webSocketURL, version, subprotocol, allowExtensions, customHeaders, maxFramePayloadLength, true);
    }
    
    public WebSocketClientProtocolHandler(final WebSocketClientHandshaker handshaker, final boolean handleCloseFrames) {
        this.handshaker = handshaker;
        this.handleCloseFrames = handleCloseFrames;
    }
    
    public WebSocketClientProtocolHandler(final WebSocketClientHandshaker handshaker) {
        this(handshaker, true);
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final WebSocketFrame frame, final List<Object> out) throws Exception {
        if (this.handleCloseFrames && frame instanceof CloseWebSocketFrame) {
            ctx.close();
            return;
        }
        super.decode(ctx, frame, out);
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) {
        final ChannelPipeline cp = ctx.pipeline();
        if (cp.get(WebSocketClientProtocolHandshakeHandler.class) == null) {
            ctx.pipeline().addBefore(ctx.name(), WebSocketClientProtocolHandshakeHandler.class.getName(), new WebSocketClientProtocolHandshakeHandler(this.handshaker));
        }
    }
    
    public enum ClientHandshakeStateEvent
    {
        HANDSHAKE_ISSUED, 
        HANDSHAKE_COMPLETE;
    }
}
