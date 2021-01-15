/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.handler.codec.http.DefaultFullHttpResponse;
/*     */ import io.netty.handler.codec.http.FullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaderValues;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpVersion;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WebSocketServerHandshaker07
/*     */   extends WebSocketServerHandshaker
/*     */ {
/*     */   public static final String WEBSOCKET_07_ACCEPT_GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
/*     */   private final boolean allowExtensions;
/*     */   private final boolean allowMaskMismatch;
/*     */   
/*     */   public WebSocketServerHandshaker07(String webSocketURL, String subprotocols, boolean allowExtensions, int maxFramePayloadLength)
/*     */   {
/*  59 */     this(webSocketURL, subprotocols, allowExtensions, maxFramePayloadLength, false);
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
/*     */   public WebSocketServerHandshaker07(String webSocketURL, String subprotocols, boolean allowExtensions, int maxFramePayloadLength, boolean allowMaskMismatch)
/*     */   {
/*  82 */     super(WebSocketVersion.V07, webSocketURL, subprotocols, maxFramePayloadLength);
/*  83 */     this.allowExtensions = allowExtensions;
/*  84 */     this.allowMaskMismatch = allowMaskMismatch;
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
/*     */   protected FullHttpResponse newHandshakeResponse(FullHttpRequest req, HttpHeaders headers)
/*     */   {
/* 123 */     FullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.SWITCHING_PROTOCOLS);
/*     */     
/*     */ 
/* 126 */     if (headers != null) {
/* 127 */       res.headers().add(headers);
/*     */     }
/*     */     
/* 130 */     CharSequence key = (CharSequence)req.headers().get(HttpHeaderNames.SEC_WEBSOCKET_KEY);
/* 131 */     if (key == null) {
/* 132 */       throw new WebSocketHandshakeException("not a WebSocket request: missing key");
/*     */     }
/* 134 */     String acceptSeed = key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
/* 135 */     byte[] sha1 = WebSocketUtil.sha1(acceptSeed.getBytes(CharsetUtil.US_ASCII));
/* 136 */     String accept = WebSocketUtil.base64(sha1);
/*     */     
/* 138 */     if (logger.isDebugEnabled()) {
/* 139 */       logger.debug("WebSocket version 07 server handshake key: {}, response: {}.", key, accept);
/*     */     }
/*     */     
/* 142 */     res.headers().add(HttpHeaderNames.UPGRADE, HttpHeaderValues.WEBSOCKET);
/* 143 */     res.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE);
/* 144 */     res.headers().add(HttpHeaderNames.SEC_WEBSOCKET_ACCEPT, accept);
/* 145 */     String subprotocols = (String)req.headers().getAndConvert(HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL);
/* 146 */     if (subprotocols != null) {
/* 147 */       String selectedSubprotocol = selectSubprotocol(subprotocols);
/* 148 */       if (selectedSubprotocol == null) {
/* 149 */         if (logger.isDebugEnabled()) {
/* 150 */           logger.debug("Requested subprotocol(s) not supported: {}", subprotocols);
/*     */         }
/*     */       } else {
/* 153 */         res.headers().add(HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL, selectedSubprotocol);
/*     */       }
/*     */     }
/* 156 */     return res;
/*     */   }
/*     */   
/*     */   protected WebSocketFrameDecoder newWebsocketDecoder()
/*     */   {
/* 161 */     return new WebSocket07FrameDecoder(true, this.allowExtensions, maxFramePayloadLength(), this.allowMaskMismatch);
/*     */   }
/*     */   
/*     */   protected WebSocketFrameEncoder newWebSocketEncoder()
/*     */   {
/* 166 */     return new WebSocket07FrameEncoder(false);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\WebSocketServerHandshaker07.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */