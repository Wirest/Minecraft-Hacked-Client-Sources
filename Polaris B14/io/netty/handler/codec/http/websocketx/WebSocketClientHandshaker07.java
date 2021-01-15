/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaderValues;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpMethod;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpVersion;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.net.URI;
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
/*     */ public class WebSocketClientHandshaker07
/*     */   extends WebSocketClientHandshaker
/*     */ {
/*  42 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(WebSocketClientHandshaker07.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String MAGIC_GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
/*     */   
/*     */ 
/*     */ 
/*     */   private String expectedChallengeResponseString;
/*     */   
/*     */ 
/*     */ 
/*     */   private final boolean allowExtensions;
/*     */   
/*     */ 
/*     */ 
/*     */   private final boolean performMasking;
/*     */   
/*     */ 
/*     */ 
/*     */   private final boolean allowMaskMismatch;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public WebSocketClientHandshaker07(URI webSocketURL, WebSocketVersion version, String subprotocol, boolean allowExtensions, HttpHeaders customHeaders, int maxFramePayloadLength)
/*     */   {
/*  70 */     this(webSocketURL, version, subprotocol, allowExtensions, customHeaders, maxFramePayloadLength, true, false);
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
/*     */   public WebSocketClientHandshaker07(URI webSocketURL, WebSocketVersion version, String subprotocol, boolean allowExtensions, HttpHeaders customHeaders, int maxFramePayloadLength, boolean performMasking, boolean allowMaskMismatch)
/*     */   {
/* 100 */     super(webSocketURL, version, subprotocol, customHeaders, maxFramePayloadLength);
/* 101 */     this.allowExtensions = allowExtensions;
/* 102 */     this.performMasking = performMasking;
/* 103 */     this.allowMaskMismatch = allowMaskMismatch;
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
/*     */   protected FullHttpRequest newHandshakeRequest()
/*     */   {
/* 127 */     URI wsURL = uri();
/* 128 */     String path = wsURL.getPath();
/* 129 */     if ((wsURL.getQuery() != null) && (!wsURL.getQuery().isEmpty())) {
/* 130 */       path = wsURL.getPath() + '?' + wsURL.getQuery();
/*     */     }
/*     */     
/* 133 */     if ((path == null) || (path.isEmpty())) {
/* 134 */       path = "/";
/*     */     }
/*     */     
/*     */ 
/* 138 */     byte[] nonce = WebSocketUtil.randomBytes(16);
/* 139 */     String key = WebSocketUtil.base64(nonce);
/*     */     
/* 141 */     String acceptSeed = key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
/* 142 */     byte[] sha1 = WebSocketUtil.sha1(acceptSeed.getBytes(CharsetUtil.US_ASCII));
/* 143 */     this.expectedChallengeResponseString = WebSocketUtil.base64(sha1);
/*     */     
/* 145 */     if (logger.isDebugEnabled()) {
/* 146 */       logger.debug("WebSocket version 07 client handshake key: {}, expected response: {}", key, this.expectedChallengeResponseString);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 152 */     FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, path);
/* 153 */     HttpHeaders headers = request.headers();
/*     */     
/* 155 */     headers.add(HttpHeaderNames.UPGRADE, HttpHeaderValues.WEBSOCKET).add(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE).add(HttpHeaderNames.SEC_WEBSOCKET_KEY, key).add(HttpHeaderNames.HOST, wsURL.getHost());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 160 */     int wsPort = wsURL.getPort();
/* 161 */     String originValue = "http://" + wsURL.getHost();
/* 162 */     if ((wsPort != 80) && (wsPort != 443))
/*     */     {
/*     */ 
/* 165 */       originValue = originValue + ':' + wsPort;
/*     */     }
/* 167 */     headers.add(HttpHeaderNames.SEC_WEBSOCKET_ORIGIN, originValue);
/*     */     
/* 169 */     String expectedSubprotocol = expectedSubprotocol();
/* 170 */     if ((expectedSubprotocol != null) && (!expectedSubprotocol.isEmpty())) {
/* 171 */       headers.add(HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL, expectedSubprotocol);
/*     */     }
/*     */     
/* 174 */     headers.add(HttpHeaderNames.SEC_WEBSOCKET_VERSION, "7");
/*     */     
/* 176 */     if (this.customHeaders != null) {
/* 177 */       headers.add(this.customHeaders);
/*     */     }
/* 179 */     return request;
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
/*     */   protected void verify(FullHttpResponse response)
/*     */   {
/* 201 */     HttpResponseStatus status = HttpResponseStatus.SWITCHING_PROTOCOLS;
/* 202 */     HttpHeaders headers = response.headers();
/*     */     
/* 204 */     if (!response.status().equals(status)) {
/* 205 */       throw new WebSocketHandshakeException("Invalid handshake response getStatus: " + response.status());
/*     */     }
/*     */     
/* 208 */     CharSequence upgrade = (CharSequence)headers.get(HttpHeaderNames.UPGRADE);
/* 209 */     if (!HttpHeaderValues.WEBSOCKET.equalsIgnoreCase(upgrade)) {
/* 210 */       throw new WebSocketHandshakeException("Invalid handshake response upgrade: " + upgrade);
/*     */     }
/*     */     
/* 213 */     CharSequence connection = (CharSequence)headers.get(HttpHeaderNames.CONNECTION);
/* 214 */     if (!HttpHeaderValues.UPGRADE.equalsIgnoreCase(connection)) {
/* 215 */       throw new WebSocketHandshakeException("Invalid handshake response connection: " + connection);
/*     */     }
/*     */     
/* 218 */     CharSequence accept = (CharSequence)headers.get(HttpHeaderNames.SEC_WEBSOCKET_ACCEPT);
/* 219 */     if ((accept == null) || (!accept.equals(this.expectedChallengeResponseString))) {
/* 220 */       throw new WebSocketHandshakeException(String.format("Invalid challenge. Actual: %s. Expected: %s", new Object[] { accept, this.expectedChallengeResponseString }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected WebSocketFrameDecoder newWebsocketDecoder()
/*     */   {
/* 227 */     return new WebSocket07FrameDecoder(false, this.allowExtensions, maxFramePayloadLength(), this.allowMaskMismatch);
/*     */   }
/*     */   
/*     */   protected WebSocketFrameEncoder newWebSocketEncoder()
/*     */   {
/* 232 */     return new WebSocket07FrameEncoder(this.performMasking);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\WebSocketClientHandshaker07.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */