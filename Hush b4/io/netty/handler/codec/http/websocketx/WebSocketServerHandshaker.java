// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http.websocketx;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.channel.Channel;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;

public abstract class WebSocketServerHandshaker
{
    protected static final InternalLogger logger;
    private final String uri;
    private final String[] subprotocols;
    private final WebSocketVersion version;
    private final int maxFramePayloadLength;
    private String selectedSubprotocol;
    public static final String SUB_PROTOCOL_WILDCARD = "*";
    
    protected WebSocketServerHandshaker(final WebSocketVersion version, final String uri, final String subprotocols, final int maxFramePayloadLength) {
        this.version = version;
        this.uri = uri;
        if (subprotocols != null) {
            final String[] subprotocolArray = StringUtil.split(subprotocols, ',');
            for (int i = 0; i < subprotocolArray.length; ++i) {
                subprotocolArray[i] = subprotocolArray[i].trim();
            }
            this.subprotocols = subprotocolArray;
        }
        else {
            this.subprotocols = EmptyArrays.EMPTY_STRINGS;
        }
        this.maxFramePayloadLength = maxFramePayloadLength;
    }
    
    public String uri() {
        return this.uri;
    }
    
    public Set<String> subprotocols() {
        final Set<String> ret = new LinkedHashSet<String>();
        Collections.addAll(ret, this.subprotocols);
        return ret;
    }
    
    public WebSocketVersion version() {
        return this.version;
    }
    
    public int maxFramePayloadLength() {
        return this.maxFramePayloadLength;
    }
    
    public ChannelFuture handshake(final Channel channel, final FullHttpRequest req) {
        return this.handshake(channel, req, null, channel.newPromise());
    }
    
    public final ChannelFuture handshake(final Channel channel, final FullHttpRequest req, final HttpHeaders responseHeaders, final ChannelPromise promise) {
        if (WebSocketServerHandshaker.logger.isDebugEnabled()) {
            WebSocketServerHandshaker.logger.debug("{} WebSocket version {} server handshake", channel, this.version());
        }
        final FullHttpResponse response = this.newHandshakeResponse(req, responseHeaders);
        final ChannelPipeline p = channel.pipeline();
        if (p.get(HttpObjectAggregator.class) != null) {
            p.remove(HttpObjectAggregator.class);
        }
        if (p.get(HttpContentCompressor.class) != null) {
            p.remove(HttpContentCompressor.class);
        }
        ChannelHandlerContext ctx = p.context(HttpRequestDecoder.class);
        String encoderName;
        if (ctx == null) {
            ctx = p.context(HttpServerCodec.class);
            if (ctx == null) {
                promise.setFailure((Throwable)new IllegalStateException("No HttpDecoder and no HttpServerCodec in the pipeline"));
                return promise;
            }
            p.addBefore(ctx.name(), "wsdecoder", this.newWebsocketDecoder());
            p.addBefore(ctx.name(), "wsencoder", this.newWebSocketEncoder());
            encoderName = ctx.name();
        }
        else {
            p.replace(ctx.name(), "wsdecoder", this.newWebsocketDecoder());
            encoderName = p.context(HttpResponseEncoder.class).name();
            p.addBefore(encoderName, "wsencoder", this.newWebSocketEncoder());
        }
        channel.writeAndFlush(response).addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    final ChannelPipeline p = future.channel().pipeline();
                    p.remove(encoderName);
                    promise.setSuccess();
                }
                else {
                    promise.setFailure(future.cause());
                }
            }
        });
        return promise;
    }
    
    protected abstract FullHttpResponse newHandshakeResponse(final FullHttpRequest p0, final HttpHeaders p1);
    
    public ChannelFuture close(final Channel channel, final CloseWebSocketFrame frame) {
        if (channel == null) {
            throw new NullPointerException("channel");
        }
        return this.close(channel, frame, channel.newPromise());
    }
    
    public ChannelFuture close(final Channel channel, final CloseWebSocketFrame frame, final ChannelPromise promise) {
        if (channel == null) {
            throw new NullPointerException("channel");
        }
        return channel.writeAndFlush(frame, promise).addListener((GenericFutureListener<? extends Future<? super Void>>)ChannelFutureListener.CLOSE);
    }
    
    protected String selectSubprotocol(final String requestedSubprotocols) {
        if (requestedSubprotocols == null || this.subprotocols.length == 0) {
            return null;
        }
        final String[] arr$;
        final String[] requestedSubprotocolArray = arr$ = StringUtil.split(requestedSubprotocols, ',');
        for (final String p : arr$) {
            final String requestedSubprotocol = p.trim();
            for (final String supportedSubprotocol : this.subprotocols) {
                if ("*".equals(supportedSubprotocol) || requestedSubprotocol.equals(supportedSubprotocol)) {
                    return this.selectedSubprotocol = requestedSubprotocol;
                }
            }
        }
        return null;
    }
    
    public String selectedSubprotocol() {
        return this.selectedSubprotocol;
    }
    
    protected abstract WebSocketFrameDecoder newWebsocketDecoder();
    
    protected abstract WebSocketFrameEncoder newWebSocketEncoder();
    
    static {
        logger = InternalLoggerFactory.getInstance(WebSocketServerHandshaker.class);
    }
}
