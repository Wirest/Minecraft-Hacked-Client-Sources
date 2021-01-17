// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

import io.netty.channel.ChannelInboundHandler;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.ssl.SslHandler;
import io.netty.channel.ChannelHandler;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.StringUtil;
import javax.net.ssl.SSLEngine;
import io.netty.handler.codec.ByteToMessageDecoder;

public abstract class SpdyOrHttpChooser extends ByteToMessageDecoder
{
    private final int maxSpdyContentLength;
    private final int maxHttpContentLength;
    
    protected SpdyOrHttpChooser(final int maxSpdyContentLength, final int maxHttpContentLength) {
        this.maxSpdyContentLength = maxSpdyContentLength;
        this.maxHttpContentLength = maxHttpContentLength;
    }
    
    protected SelectedProtocol getProtocol(final SSLEngine engine) {
        final String[] protocol = StringUtil.split(engine.getSession().getProtocol(), ':');
        if (protocol.length < 2) {
            return SelectedProtocol.HTTP_1_1;
        }
        final SelectedProtocol selectedProtocol = SelectedProtocol.protocol(protocol[1]);
        return selectedProtocol;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        if (this.initPipeline(ctx)) {
            ctx.pipeline().remove(this);
        }
    }
    
    private boolean initPipeline(final ChannelHandlerContext ctx) {
        final SslHandler handler = ctx.pipeline().get(SslHandler.class);
        if (handler == null) {
            throw new IllegalStateException("SslHandler is needed for SPDY");
        }
        final SelectedProtocol protocol = this.getProtocol(handler.engine());
        switch (protocol) {
            case UNKNOWN: {
                return false;
            }
            case SPDY_3_1: {
                this.addSpdyHandlers(ctx, SpdyVersion.SPDY_3_1);
                break;
            }
            case HTTP_1_0:
            case HTTP_1_1: {
                this.addHttpHandlers(ctx);
                break;
            }
            default: {
                throw new IllegalStateException("Unknown SelectedProtocol");
            }
        }
        return true;
    }
    
    protected void addSpdyHandlers(final ChannelHandlerContext ctx, final SpdyVersion version) {
        final ChannelPipeline pipeline = ctx.pipeline();
        pipeline.addLast("spdyFrameCodec", new SpdyFrameCodec(version));
        pipeline.addLast("spdySessionHandler", new SpdySessionHandler(version, true));
        pipeline.addLast("spdyHttpEncoder", new SpdyHttpEncoder(version));
        pipeline.addLast("spdyHttpDecoder", new SpdyHttpDecoder(version, this.maxSpdyContentLength));
        pipeline.addLast("spdyStreamIdHandler", new SpdyHttpResponseStreamIdHandler());
        pipeline.addLast("httpRequestHandler", this.createHttpRequestHandlerForSpdy());
    }
    
    protected void addHttpHandlers(final ChannelHandlerContext ctx) {
        final ChannelPipeline pipeline = ctx.pipeline();
        pipeline.addLast("httpRequestDecoder", new HttpRequestDecoder());
        pipeline.addLast("httpResponseEncoder", new HttpResponseEncoder());
        pipeline.addLast("httpChunkAggregator", new HttpObjectAggregator(this.maxHttpContentLength));
        pipeline.addLast("httpRequestHandler", this.createHttpRequestHandlerForHttp());
    }
    
    protected abstract ChannelInboundHandler createHttpRequestHandlerForHttp();
    
    protected ChannelInboundHandler createHttpRequestHandlerForSpdy() {
        return this.createHttpRequestHandlerForHttp();
    }
    
    public enum SelectedProtocol
    {
        SPDY_3_1("spdy/3.1"), 
        HTTP_1_1("http/1.1"), 
        HTTP_1_0("http/1.0"), 
        UNKNOWN("Unknown");
        
        private final String name;
        
        private SelectedProtocol(final String defaultName) {
            this.name = defaultName;
        }
        
        public String protocolName() {
            return this.name;
        }
        
        public static SelectedProtocol protocol(final String name) {
            for (final SelectedProtocol protocol : values()) {
                if (protocol.protocolName().equals(name)) {
                    return protocol;
                }
            }
            return SelectedProtocol.UNKNOWN;
        }
    }
}
