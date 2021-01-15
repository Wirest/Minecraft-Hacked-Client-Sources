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
/*     */ public class WebSocketServerHandshaker13
/*     */   extends WebSocketServerHandshaker
/*     */ {
/*     */   public static final String WEBSOCKET_13_ACCEPT_GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
/*     */   private final boolean allowExtensions;
/*     */   private final boolean allowMaskMismatch;
/*     */   
/*     */   public WebSocketServerHandshaker13(String webSocketURL, String subprotocols, boolean allowExtensions, int maxFramePayloadLength)
/*     */   {
/*  58 */     this(webSocketURL, subprotocols, allowExtensions, maxFramePayloadLength, false);
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
/*     */   public WebSocketServerHandshaker13(String webSocketURL, String subprotocols, boolean allowExtensions, int maxFramePayloadLength, boolean allowMaskMismatch)
/*     */   {
/*  81 */     super(WebSocketVersion.V13, webSocketURL, subprotocols, maxFramePayloadLength);
/*  82 */     this.allowExtensions = allowExtensions;
/*  83 */     this.allowMaskMismatch = allowMaskMismatch;
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
/* 122 */     FullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.SWITCHING_PROTOCOLS);
/* 123 */     if (headers != null) {
/* 124 */       res.headers().add(headers);
/*     */     }
/*     */     
/* 127 */     CharSequence key = (CharSequence)req.headers().get(HttpHeaderNames.SEC_WEBSOCKET_KEY);
/* 128 */     if (key == null) {
/* 129 */       throw new WebSocketHandshakeException("not a WebSocket request: missing key");
/*     */     }
/* 131 */     String acceptSeed = key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
/* 132 */     byte[] sha1 = WebSocketUtil.sha1(acceptSeed.getBytes(CharsetUtil.US_ASCII));
/* 133 */     String accept = WebSocketUtil.base64(sha1);
/*     */     
/* 135 */     if (logger.isDebugEnabled()) {
/* 136 */       logger.debug("WebSocket version 13 server handshake key: {}, response: {}", key, accept);
/*     */     }
/*     */     
/* 139 */     res.headers().add(HttpHeaderNames.UPGRADE, HttpHeaderValues.WEBSOCKET);
/* 140 */     res.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE);
/* 141 */     res.headers().add(HttpHeaderNames.SEC_WEBSOCKET_ACCEPT, accept);
/* 142 */     String subprotocols = (String)req.headers().getAndConvert(HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL);
/* 143 */     if (subprotocols != null) {
/* 144 */       String selectedSubprotocol = selectSubprotocol(subprotocols);
/* 145 */       if (selectedSubprotocol == null) {
/* 146 */         if (logger.isDebugEnabled()) {
/* 147 */           logger.debug("Requested subprotocol(s) not supported: {}", subprotocols);
/*     */         }
/*     */       } else {
/* 150 */         res.headers().add(HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL, selectedSubprotocol);
/*     */       }
/*     */     }
/* 153 */     return res;
/*     */   }
/*     */   
/*     */   protected WebSocketFrameDecoder newWebsocketDecoder()
/*     */   {
/* 158 */     return new WebSocket13FrameDecoder(true, this.allowExtensions, maxFramePayloadLength(), this.allowMaskMismatch);
/*     */   }
/*     */   
/*     */   protected WebSocketFrameEncoder newWebSocketEncoder()
/*     */   {
/* 163 */     return new WebSocket13FrameEncoder(false);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\WebSocketServerHandshaker13.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */