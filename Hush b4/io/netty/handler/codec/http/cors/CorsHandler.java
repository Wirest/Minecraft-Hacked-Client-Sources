// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http.cors;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.ChannelDuplexHandler;

public class CorsHandler extends ChannelDuplexHandler
{
    private static final InternalLogger logger;
    private final CorsConfig config;
    private HttpRequest request;
    
    public CorsHandler(final CorsConfig config) {
        this.config = config;
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        if (this.config.isCorsSupportEnabled() && msg instanceof HttpRequest) {
            this.request = (HttpRequest)msg;
            if (isPreflightRequest(this.request)) {
                this.handlePreflight(ctx, this.request);
                return;
            }
            if (this.config.isShortCurcuit() && !this.validateOrigin()) {
                forbidden(ctx, this.request);
                return;
            }
        }
        ctx.fireChannelRead(msg);
    }
    
    private void handlePreflight(final ChannelHandlerContext ctx, final HttpRequest request) {
        final HttpResponse response = new DefaultFullHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);
        if (this.setOrigin(response)) {
            this.setAllowMethods(response);
            this.setAllowHeaders(response);
            this.setAllowCredentials(response);
            this.setMaxAge(response);
            this.setPreflightHeaders(response);
        }
        ctx.writeAndFlush(response).addListener((GenericFutureListener<? extends Future<? super Void>>)ChannelFutureListener.CLOSE);
    }
    
    private void setPreflightHeaders(final HttpResponse response) {
        response.headers().add(this.config.preflightResponseHeaders());
    }
    
    private boolean setOrigin(final HttpResponse response) {
        final String origin = this.request.headers().get("Origin");
        if (origin != null) {
            if ("null".equals(origin) && this.config.isNullOriginAllowed()) {
                setAnyOrigin(response);
                return true;
            }
            if (this.config.isAnyOriginSupported()) {
                if (this.config.isCredentialsAllowed()) {
                    this.echoRequestOrigin(response);
                    setVaryHeader(response);
                }
                else {
                    setAnyOrigin(response);
                }
                return true;
            }
            if (this.config.origins().contains(origin)) {
                setOrigin(response, origin);
                setVaryHeader(response);
                return true;
            }
            CorsHandler.logger.debug("Request origin [" + origin + "] was not among the configured origins " + this.config.origins());
        }
        return false;
    }
    
    private boolean validateOrigin() {
        if (this.config.isAnyOriginSupported()) {
            return true;
        }
        final String origin = this.request.headers().get("Origin");
        return origin == null || ("null".equals(origin) && this.config.isNullOriginAllowed()) || this.config.origins().contains(origin);
    }
    
    private void echoRequestOrigin(final HttpResponse response) {
        setOrigin(response, this.request.headers().get("Origin"));
    }
    
    private static void setVaryHeader(final HttpResponse response) {
        response.headers().set("Vary", "Origin");
    }
    
    private static void setAnyOrigin(final HttpResponse response) {
        setOrigin(response, "*");
    }
    
    private static void setOrigin(final HttpResponse response, final String origin) {
        response.headers().set("Access-Control-Allow-Origin", origin);
    }
    
    private void setAllowCredentials(final HttpResponse response) {
        if (this.config.isCredentialsAllowed()) {
            response.headers().set("Access-Control-Allow-Credentials", "true");
        }
    }
    
    private static boolean isPreflightRequest(final HttpRequest request) {
        final HttpHeaders headers = request.headers();
        return request.getMethod().equals(HttpMethod.OPTIONS) && headers.contains("Origin") && headers.contains("Access-Control-Request-Method");
    }
    
    private void setExposeHeaders(final HttpResponse response) {
        if (!this.config.exposedHeaders().isEmpty()) {
            response.headers().set("Access-Control-Expose-Headers", this.config.exposedHeaders());
        }
    }
    
    private void setAllowMethods(final HttpResponse response) {
        response.headers().set("Access-Control-Allow-Methods", this.config.allowedRequestMethods());
    }
    
    private void setAllowHeaders(final HttpResponse response) {
        response.headers().set("Access-Control-Allow-Headers", this.config.allowedRequestHeaders());
    }
    
    private void setMaxAge(final HttpResponse response) {
        response.headers().set("Access-Control-Max-Age", this.config.maxAge());
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        if (this.config.isCorsSupportEnabled() && msg instanceof HttpResponse) {
            final HttpResponse response = (HttpResponse)msg;
            if (this.setOrigin(response)) {
                this.setAllowCredentials(response);
                this.setAllowHeaders(response);
                this.setExposeHeaders(response);
            }
        }
        ctx.writeAndFlush(msg, promise);
    }
    
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        CorsHandler.logger.error("Caught error in CorsHandler", cause);
        ctx.fireExceptionCaught(cause);
    }
    
    private static void forbidden(final ChannelHandlerContext ctx, final HttpRequest request) {
        ctx.writeAndFlush(new DefaultFullHttpResponse(request.getProtocolVersion(), HttpResponseStatus.FORBIDDEN)).addListener((GenericFutureListener<? extends Future<? super Void>>)ChannelFutureListener.CLOSE);
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(CorsHandler.class);
    }
}
