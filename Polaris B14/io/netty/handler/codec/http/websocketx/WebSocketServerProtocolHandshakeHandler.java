/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerAdapter;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpResponse;
/*     */ import io.netty.handler.codec.http.FullHttpRequest;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaderUtil;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpMethod;
/*     */ import io.netty.handler.codec.http.HttpRequest;
/*     */ import io.netty.handler.codec.http.HttpResponse;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpVersion;
/*     */ import io.netty.handler.ssl.SslHandler;
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
/*     */ class WebSocketServerProtocolHandshakeHandler
/*     */   extends ChannelHandlerAdapter
/*     */ {
/*     */   private final String websocketPath;
/*     */   private final String subprotocols;
/*     */   private final boolean allowExtensions;
/*     */   private final int maxFramePayloadSize;
/*     */   private final boolean allowMaskMismatch;
/*     */   
/*     */   WebSocketServerProtocolHandshakeHandler(String websocketPath, String subprotocols, boolean allowExtensions, int maxFrameSize, boolean allowMaskMismatch)
/*     */   {
/*  48 */     this.websocketPath = websocketPath;
/*  49 */     this.subprotocols = subprotocols;
/*  50 */     this.allowExtensions = allowExtensions;
/*  51 */     this.maxFramePayloadSize = maxFrameSize;
/*  52 */     this.allowMaskMismatch = allowMaskMismatch;
/*     */   }
/*     */   
/*     */   public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception
/*     */   {
/*  57 */     FullHttpRequest req = (FullHttpRequest)msg;
/*     */     try {
/*  59 */       if (req.method() != HttpMethod.GET) {
/*  60 */         sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
/*     */       }
/*     */       else
/*     */       {
/*  64 */         WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(ctx.pipeline(), req, this.websocketPath), this.subprotocols, this.allowExtensions, this.maxFramePayloadSize, this.allowMaskMismatch);
/*     */         
/*     */ 
/*  67 */         WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);
/*  68 */         if (handshaker == null) {
/*  69 */           WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
/*     */         } else {
/*  71 */           ChannelFuture handshakeFuture = handshaker.handshake(ctx.channel(), req);
/*  72 */           handshakeFuture.addListener(new ChannelFutureListener()
/*     */           {
/*     */             public void operationComplete(ChannelFuture future) throws Exception {
/*  75 */               if (!future.isSuccess()) {
/*  76 */                 ctx.fireExceptionCaught(future.cause());
/*     */               } else {
/*  78 */                 ctx.fireUserEventTriggered(WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE);
/*     */               }
/*     */               
/*     */             }
/*  82 */           });
/*  83 */           WebSocketServerProtocolHandler.setHandshaker(ctx, handshaker);
/*  84 */           ctx.pipeline().replace(this, "WS403Responder", WebSocketServerProtocolHandler.forbiddenHttpRequestResponder());
/*     */         }
/*     */       }
/*     */     } finally {
/*  88 */       req.release();
/*     */     }
/*     */   }
/*     */   
/*     */   private static void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest req, HttpResponse res) {
/*  93 */     ChannelFuture f = ctx.channel().writeAndFlush(res);
/*  94 */     if ((!HttpHeaderUtil.isKeepAlive(req)) || (res.status().code() != 200)) {
/*  95 */       f.addListener(ChannelFutureListener.CLOSE);
/*     */     }
/*     */   }
/*     */   
/*     */   private static String getWebSocketLocation(ChannelPipeline cp, HttpRequest req, String path) {
/* 100 */     String protocol = "ws";
/* 101 */     if (cp.get(SslHandler.class) != null)
/*     */     {
/* 103 */       protocol = "wss";
/*     */     }
/* 105 */     return protocol + "://" + req.headers().get(HttpHeaderNames.HOST) + path;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\WebSocketServerProtocolHandshakeHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */