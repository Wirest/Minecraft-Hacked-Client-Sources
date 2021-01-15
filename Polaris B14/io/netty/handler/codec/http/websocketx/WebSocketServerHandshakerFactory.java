/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.handler.codec.http.DefaultHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpRequest;
/*     */ import io.netty.handler.codec.http.HttpResponse;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpVersion;
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
/*     */ public class WebSocketServerHandshakerFactory
/*     */ {
/*     */   private final String webSocketURL;
/*     */   private final String subprotocols;
/*     */   private final boolean allowExtensions;
/*     */   private final int maxFramePayloadLength;
/*     */   private final boolean allowMaskMismatch;
/*     */   
/*     */   public WebSocketServerHandshakerFactory(String webSocketURL, String subprotocols, boolean allowExtensions)
/*     */   {
/*  57 */     this(webSocketURL, subprotocols, allowExtensions, 65536);
/*     */   }
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
/*     */   public WebSocketServerHandshakerFactory(String webSocketURL, String subprotocols, boolean allowExtensions, int maxFramePayloadLength)
/*     */   {
/*  77 */     this(webSocketURL, subprotocols, allowExtensions, maxFramePayloadLength, false);
/*     */   }
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
/*     */   public WebSocketServerHandshakerFactory(String webSocketURL, String subprotocols, boolean allowExtensions, int maxFramePayloadLength, boolean allowMaskMismatch)
/*     */   {
/* 100 */     this.webSocketURL = webSocketURL;
/* 101 */     this.subprotocols = subprotocols;
/* 102 */     this.allowExtensions = allowExtensions;
/* 103 */     this.maxFramePayloadLength = maxFramePayloadLength;
/* 104 */     this.allowMaskMismatch = allowMaskMismatch;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WebSocketServerHandshaker newHandshaker(HttpRequest req)
/*     */   {
/* 115 */     CharSequence version = (CharSequence)req.headers().get(HttpHeaderNames.SEC_WEBSOCKET_VERSION);
/* 116 */     if (version != null) {
/* 117 */       if (version.equals(WebSocketVersion.V13.toHttpHeaderValue()))
/*     */       {
/* 119 */         return new WebSocketServerHandshaker13(this.webSocketURL, this.subprotocols, this.allowExtensions, this.maxFramePayloadLength, this.allowMaskMismatch);
/*     */       }
/* 121 */       if (version.equals(WebSocketVersion.V08.toHttpHeaderValue()))
/*     */       {
/* 123 */         return new WebSocketServerHandshaker08(this.webSocketURL, this.subprotocols, this.allowExtensions, this.maxFramePayloadLength, this.allowMaskMismatch);
/*     */       }
/* 125 */       if (version.equals(WebSocketVersion.V07.toHttpHeaderValue()))
/*     */       {
/* 127 */         return new WebSocketServerHandshaker07(this.webSocketURL, this.subprotocols, this.allowExtensions, this.maxFramePayloadLength, this.allowMaskMismatch);
/*     */       }
/*     */       
/* 130 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 134 */     return new WebSocketServerHandshaker00(this.webSocketURL, this.subprotocols, this.maxFramePayloadLength);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ChannelFuture sendUnsupportedVersionResponse(Channel channel)
/*     */   {
/* 142 */     return sendUnsupportedVersionResponse(channel, channel.newPromise());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ChannelFuture sendUnsupportedVersionResponse(Channel channel, ChannelPromise promise)
/*     */   {
/* 149 */     HttpResponse res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UPGRADE_REQUIRED);
/*     */     
/*     */ 
/* 152 */     res.headers().set(HttpHeaderNames.SEC_WEBSOCKET_VERSION, WebSocketVersion.V13.toHttpHeaderValue());
/* 153 */     return channel.write(res, promise);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\WebSocketServerHandshakerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */