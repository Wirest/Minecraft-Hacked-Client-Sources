/*     */ package io.netty.handler.codec.http.cors;
/*     */ 
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerAdapter;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpMethod;
/*     */ import io.netty.handler.codec.http.HttpRequest;
/*     */ import io.netty.handler.codec.http.HttpResponse;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CorsHandler
/*     */   extends ChannelHandlerAdapter
/*     */ {
/*  42 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(CorsHandler.class);
/*     */   private static final String ANY_ORIGIN = "*";
/*     */   private final CorsConfig config;
/*     */   private HttpRequest request;
/*     */   
/*     */   public CorsHandler(CorsConfig config)
/*     */   {
/*  49 */     this.config = config;
/*     */   }
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
/*     */   {
/*  54 */     if ((this.config.isCorsSupportEnabled()) && ((msg instanceof HttpRequest))) {
/*  55 */       this.request = ((HttpRequest)msg);
/*  56 */       if (isPreflightRequest(this.request)) {
/*  57 */         handlePreflight(ctx, this.request);
/*  58 */         return;
/*     */       }
/*  60 */       if ((this.config.isShortCurcuit()) && (!validateOrigin())) {
/*  61 */         forbidden(ctx, this.request);
/*  62 */         return;
/*     */       }
/*     */     }
/*  65 */     ctx.fireChannelRead(msg);
/*     */   }
/*     */   
/*     */   private void handlePreflight(ChannelHandlerContext ctx, HttpRequest request) {
/*  69 */     HttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.OK, true, true);
/*  70 */     if (setOrigin(response)) {
/*  71 */       setAllowMethods(response);
/*  72 */       setAllowHeaders(response);
/*  73 */       setAllowCredentials(response);
/*  74 */       setMaxAge(response);
/*  75 */       setPreflightHeaders(response);
/*     */     }
/*  77 */     ReferenceCountUtil.release(request);
/*  78 */     ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setPreflightHeaders(HttpResponse response)
/*     */   {
/*  88 */     response.headers().add(this.config.preflightResponseHeaders());
/*     */   }
/*     */   
/*     */   private boolean setOrigin(HttpResponse response) {
/*  92 */     CharSequence origin = (CharSequence)this.request.headers().get(HttpHeaderNames.ORIGIN);
/*  93 */     if (origin != null) {
/*  94 */       if (("null".equals(origin)) && (this.config.isNullOriginAllowed())) {
/*  95 */         setAnyOrigin(response);
/*  96 */         return true;
/*     */       }
/*  98 */       if (this.config.isAnyOriginSupported()) {
/*  99 */         if (this.config.isCredentialsAllowed()) {
/* 100 */           echoRequestOrigin(response);
/* 101 */           setVaryHeader(response);
/*     */         } else {
/* 103 */           setAnyOrigin(response);
/*     */         }
/* 105 */         return true;
/*     */       }
/* 107 */       if (this.config.origins().contains(origin)) {
/* 108 */         setOrigin(response, origin);
/* 109 */         setVaryHeader(response);
/* 110 */         return true;
/*     */       }
/* 112 */       logger.debug("Request origin [" + origin + "] was not among the configured origins " + this.config.origins());
/*     */     }
/* 114 */     return false;
/*     */   }
/*     */   
/*     */   private boolean validateOrigin() {
/* 118 */     if (this.config.isAnyOriginSupported()) {
/* 119 */       return true;
/*     */     }
/*     */     
/* 122 */     CharSequence origin = (CharSequence)this.request.headers().get(HttpHeaderNames.ORIGIN);
/* 123 */     if (origin == null)
/*     */     {
/* 125 */       return true;
/*     */     }
/*     */     
/* 128 */     if (("null".equals(origin)) && (this.config.isNullOriginAllowed())) {
/* 129 */       return true;
/*     */     }
/*     */     
/* 132 */     return this.config.origins().contains(origin);
/*     */   }
/*     */   
/*     */   private void echoRequestOrigin(HttpResponse response) {
/* 136 */     setOrigin(response, (CharSequence)this.request.headers().get(HttpHeaderNames.ORIGIN));
/*     */   }
/*     */   
/*     */   private static void setVaryHeader(HttpResponse response) {
/* 140 */     response.headers().set(HttpHeaderNames.VARY, HttpHeaderNames.ORIGIN);
/*     */   }
/*     */   
/*     */   private static void setAnyOrigin(HttpResponse response) {
/* 144 */     setOrigin(response, "*");
/*     */   }
/*     */   
/*     */   private static void setOrigin(HttpResponse response, CharSequence origin) {
/* 148 */     response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
/*     */   }
/*     */   
/*     */   private void setAllowCredentials(HttpResponse response) {
/* 152 */     if ((this.config.isCredentialsAllowed()) && (!((CharSequence)response.headers().get(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN)).equals("*")))
/*     */     {
/* 154 */       response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean isPreflightRequest(HttpRequest request) {
/* 159 */     HttpHeaders headers = request.headers();
/* 160 */     return (request.method().equals(HttpMethod.OPTIONS)) && (headers.contains(HttpHeaderNames.ORIGIN)) && (headers.contains(HttpHeaderNames.ACCESS_CONTROL_REQUEST_METHOD));
/*     */   }
/*     */   
/*     */ 
/*     */   private void setExposeHeaders(HttpResponse response)
/*     */   {
/* 166 */     if (!this.config.exposedHeaders().isEmpty()) {
/* 167 */       response.headers().set(HttpHeaderNames.ACCESS_CONTROL_EXPOSE_HEADERS, this.config.exposedHeaders());
/*     */     }
/*     */   }
/*     */   
/*     */   private void setAllowMethods(HttpResponse response) {
/* 172 */     response.headers().setObject(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, this.config.allowedRequestMethods());
/*     */   }
/*     */   
/*     */   private void setAllowHeaders(HttpResponse response) {
/* 176 */     response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, this.config.allowedRequestHeaders());
/*     */   }
/*     */   
/*     */   private void setMaxAge(HttpResponse response) {
/* 180 */     response.headers().setLong(HttpHeaderNames.ACCESS_CONTROL_MAX_AGE, this.config.maxAge());
/*     */   }
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
/*     */     throws Exception
/*     */   {
/* 186 */     if ((this.config.isCorsSupportEnabled()) && ((msg instanceof HttpResponse))) {
/* 187 */       HttpResponse response = (HttpResponse)msg;
/* 188 */       if (setOrigin(response)) {
/* 189 */         setAllowCredentials(response);
/* 190 */         setAllowHeaders(response);
/* 191 */         setExposeHeaders(response);
/*     */       }
/*     */     }
/* 194 */     ctx.writeAndFlush(msg, promise);
/*     */   }
/*     */   
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
/*     */   {
/* 199 */     logger.error("Caught error in CorsHandler", cause);
/* 200 */     ctx.fireExceptionCaught(cause);
/*     */   }
/*     */   
/*     */   private static void forbidden(ChannelHandlerContext ctx, HttpRequest request) {
/* 204 */     ctx.writeAndFlush(new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.FORBIDDEN)).addListener(ChannelFutureListener.CLOSE);
/*     */     
/* 206 */     ReferenceCountUtil.release(request);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\cors\CorsHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */