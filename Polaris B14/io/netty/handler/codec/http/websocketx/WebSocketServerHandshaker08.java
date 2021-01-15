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
/*     */ public class WebSocketServerHandshaker08
/*     */   extends WebSocketServerHandshaker
/*     */ {
/*     */   public static final String WEBSOCKET_08_ACCEPT_GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
/*     */   private final boolean allowExtensions;
/*     */   private final boolean allowMaskMismatch;
/*     */   
/*     */   public WebSocketServerHandshaker08(String webSocketURL, String subprotocols, boolean allowExtensions, int maxFramePayloadLength)
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
/*     */   public WebSocketServerHandshaker08(String webSocketURL, String subprotocols, boolean allowExtensions, int maxFramePayloadLength, boolean allowMaskMismatch)
/*     */   {
/*  82 */     super(WebSocketVersion.V08, webSocketURL, subprotocols, maxFramePayloadLength);
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
/* 125 */     if (headers != null) {
/* 126 */       res.headers().add(headers);
/*     */     }
/*     */     
/* 129 */     CharSequence key = (CharSequence)req.headers().get(HttpHeaderNames.SEC_WEBSOCKET_KEY);
/* 130 */     if (key == null) {
/* 131 */       throw new WebSocketHandshakeException("not a WebSocket request: missing key");
/*     */     }
/* 133 */     String acceptSeed = key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
/* 134 */     byte[] sha1 = WebSocketUtil.sha1(acceptSeed.getBytes(CharsetUtil.US_ASCII));
/* 135 */     String accept = WebSocketUtil.base64(sha1);
/*     */     
/* 137 */     if (logger.isDebugEnabled()) {
/* 138 */       logger.debug("WebSocket version 08 server handshake key: {}, response: {}", key, accept);
/*     */     }
/*     */     
/* 141 */     res.headers().add(HttpHeaderNames.UPGRADE, HttpHeaderValues.WEBSOCKET);
/* 142 */     res.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE);
/* 143 */     res.headers().add(HttpHeaderNames.SEC_WEBSOCKET_ACCEPT, accept);
/* 144 */     String subprotocols = (String)req.headers().getAndConvert(HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL);
/* 145 */     if (subprotocols != null) {
/* 146 */       String selectedSubprotocol = selectSubprotocol(subprotocols);
/* 147 */       if (selectedSubprotocol == null) {
/* 148 */         if (logger.isDebugEnabled()) {
/* 149 */           logger.debug("Requested subprotocol(s) not supported: {}", subprotocols);
/*     */         }
/*     */       } else {
/* 152 */         res.headers().add(HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL, selectedSubprotocol);
/*     */       }
/*     */     }
/* 155 */     return res;
/*     */   }
/*     */   
/*     */   protected WebSocketFrameDecoder newWebsocketDecoder()
/*     */   {
/* 160 */     return new WebSocket08FrameDecoder(true, this.allowExtensions, maxFramePayloadLength(), this.allowMaskMismatch);
/*     */   }
/*     */   
/*     */   protected WebSocketFrameEncoder newWebSocketEncoder()
/*     */   {
/* 165 */     return new WebSocket08FrameEncoder(false);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\WebSocketServerHandshaker08.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */