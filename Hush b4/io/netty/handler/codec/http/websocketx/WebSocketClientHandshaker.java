// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http.websocketx;

import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpHeaders;
import java.net.URI;

public abstract class WebSocketClientHandshaker
{
    private final URI uri;
    private final WebSocketVersion version;
    private volatile boolean handshakeComplete;
    private final String expectedSubprotocol;
    private volatile String actualSubprotocol;
    protected final HttpHeaders customHeaders;
    private final int maxFramePayloadLength;
    
    protected WebSocketClientHandshaker(final URI uri, final WebSocketVersion version, final String subprotocol, final HttpHeaders customHeaders, final int maxFramePayloadLength) {
        this.uri = uri;
        this.version = version;
        this.expectedSubprotocol = subprotocol;
        this.customHeaders = customHeaders;
        this.maxFramePayloadLength = maxFramePayloadLength;
    }
    
    public URI uri() {
        return this.uri;
    }
    
    public WebSocketVersion version() {
        return this.version;
    }
    
    public int maxFramePayloadLength() {
        return this.maxFramePayloadLength;
    }
    
    public boolean isHandshakeComplete() {
        return this.handshakeComplete;
    }
    
    private void setHandshakeComplete() {
        this.handshakeComplete = true;
    }
    
    public String expectedSubprotocol() {
        return this.expectedSubprotocol;
    }
    
    public String actualSubprotocol() {
        return this.actualSubprotocol;
    }
    
    private void setActualSubprotocol(final String actualSubprotocol) {
        this.actualSubprotocol = actualSubprotocol;
    }
    
    public ChannelFuture handshake(final Channel channel) {
        if (channel == null) {
            throw new NullPointerException("channel");
        }
        return this.handshake(channel, channel.newPromise());
    }
    
    public final ChannelFuture handshake(final Channel channel, final ChannelPromise promise) {
        final FullHttpRequest request = this.newHandshakeRequest();
        final HttpResponseDecoder decoder = channel.pipeline().get(HttpResponseDecoder.class);
        if (decoder == null) {
            final HttpClientCodec codec = channel.pipeline().get(HttpClientCodec.class);
            if (codec == null) {
                promise.setFailure((Throwable)new IllegalStateException("ChannelPipeline does not contain a HttpResponseDecoder or HttpClientCodec"));
                return promise;
            }
        }
        channel.writeAndFlush(request).addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture future) {
                if (future.isSuccess()) {
                    final ChannelPipeline p = future.channel().pipeline();
                    ChannelHandlerContext ctx = p.context(HttpRequestEncoder.class);
                    if (ctx == null) {
                        ctx = p.context(HttpClientCodec.class);
                    }
                    if (ctx == null) {
                        promise.setFailure((Throwable)new IllegalStateException("ChannelPipeline does not contain a HttpRequestEncoder or HttpClientCodec"));
                        return;
                    }
                    p.addAfter(ctx.name(), "ws-encoder", WebSocketClientHandshaker.this.newWebSocketEncoder());
                    promise.setSuccess();
                }
                else {
                    promise.setFailure(future.cause());
                }
            }
        });
        return promise;
    }
    
    protected abstract FullHttpRequest newHandshakeRequest();
    
    public final void finishHandshake(final Channel channel, final FullHttpResponse response) {
        this.verify(response);
        this.setActualSubprotocol(response.headers().get("Sec-WebSocket-Protocol"));
        this.setHandshakeComplete();
        final ChannelPipeline p = channel.pipeline();
        final HttpContentDecompressor decompressor = p.get(HttpContentDecompressor.class);
        if (decompressor != null) {
            p.remove(decompressor);
        }
        ChannelHandlerContext ctx = p.context(HttpResponseDecoder.class);
        if (ctx == null) {
            ctx = p.context(HttpClientCodec.class);
            if (ctx == null) {
                throw new IllegalStateException("ChannelPipeline does not contain a HttpRequestEncoder or HttpClientCodec");
            }
            p.replace(ctx.name(), "ws-decoder", this.newWebsocketDecoder());
        }
        else {
            if (p.get(HttpRequestEncoder.class) != null) {
                p.remove(HttpRequestEncoder.class);
            }
            p.replace(ctx.name(), "ws-decoder", this.newWebsocketDecoder());
        }
    }
    
    protected abstract void verify(final FullHttpResponse p0);
    
    protected abstract WebSocketFrameDecoder newWebsocketDecoder();
    
    protected abstract WebSocketFrameEncoder newWebSocketEncoder();
    
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
        return channel.writeAndFlush(frame, promise);
    }
}
