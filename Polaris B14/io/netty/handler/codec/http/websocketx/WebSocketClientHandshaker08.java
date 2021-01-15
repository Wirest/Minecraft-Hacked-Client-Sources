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
/*     */ public class WebSocketClientHandshaker08
/*     */   extends WebSocketClientHandshaker
/*     */ {
/*  42 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(WebSocketClientHandshaker08.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String MAGIC_GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
/*     */   
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
/*     */   public WebSocketClientHandshaker08(URI webSocketURL, WebSocketVersion version, String subprotocol, boolean allowExtensions, HttpHeaders customHeaders, int maxFramePayloadLength)
/*     */   {
/*  71 */     this(webSocketURL, version, subprotocol, allowExtensions, customHeaders, maxFramePayloadLength, true, false);
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
/*     */   public WebSocketClientHandshaker08(URI webSocketURL, WebSocketVersion version, String subprotocol, boolean allowExtensions, HttpHeaders customHeaders, int maxFramePayloadLength, boolean performMasking, boolean allowMaskMismatch)
/*     */   {
/* 101 */     super(webSocketURL, version, subprotocol, customHeaders, maxFramePayloadLength);
/* 102 */     this.allowExtensions = allowExtensions;
/* 103 */     this.performMasking = performMasking;
/* 104 */     this.allowMaskMismatch = allowMaskMismatch;
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
/* 128 */     URI wsURL = uri();
/* 129 */     String path = wsURL.getPath();
/* 130 */     if ((wsURL.getQuery() != null) && (!wsURL.getQuery().isEmpty())) {
/* 131 */       path = wsURL.getPath() + '?' + wsURL.getQuery();
/*     */     }
/*     */     
/* 134 */     if ((path == null) || (path.isEmpty())) {
/* 135 */       path = "/";
/*     */     }
/*     */     
/*     */ 
/* 139 */     byte[] nonce = WebSocketUtil.randomBytes(16);
/* 140 */     String key = WebSocketUtil.base64(nonce);
/*     */     
/* 142 */     String acceptSeed = key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
/* 143 */     byte[] sha1 = WebSocketUtil.sha1(acceptSeed.getBytes(CharsetUtil.US_ASCII));
/* 144 */     this.expectedChallengeResponseString = WebSocketUtil.base64(sha1);
/*     */     
/* 146 */     if (logger.isDebugEnabled()) {
/* 147 */       logger.debug("WebSocket version 08 client handshake key: {}, expected response: {}", key, this.expectedChallengeResponseString);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 153 */     FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, path);
/* 154 */     HttpHeaders headers = request.headers();
/*     */     
/* 156 */     headers.add(HttpHeaderNames.UPGRADE, HttpHeaderValues.WEBSOCKET).add(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE).add(HttpHeaderNames.SEC_WEBSOCKET_KEY, key).add(HttpHeaderNames.HOST, wsURL.getHost());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 161 */     int wsPort = wsURL.getPort();
/* 162 */     String originValue = "http://" + wsURL.getHost();
/* 163 */     if ((wsPort != 80) && (wsPort != 443))
/*     */     {
/*     */ 
/* 166 */       originValue = originValue + ':' + wsPort;
/*     */     }
/* 168 */     headers.add(HttpHeaderNames.SEC_WEBSOCKET_ORIGIN, originValue);
/*     */     
/* 170 */     String expectedSubprotocol = expectedSubprotocol();
/* 171 */     if ((expectedSubprotocol != null) && (!expectedSubprotocol.isEmpty())) {
/* 172 */       headers.add(HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL, expectedSubprotocol);
/*     */     }
/*     */     
/* 175 */     headers.add(HttpHeaderNames.SEC_WEBSOCKET_VERSION, "8");
/*     */     
/* 177 */     if (this.customHeaders != null) {
/* 178 */       headers.add(this.customHeaders);
/*     */     }
/* 180 */     return request;
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
/* 202 */     HttpResponseStatus status = HttpResponseStatus.SWITCHING_PROTOCOLS;
/* 203 */     HttpHeaders headers = response.headers();
/*     */     
/* 205 */     if (!response.status().equals(status)) {
/* 206 */       throw new WebSocketHandshakeException("Invalid handshake response getStatus: " + response.status());
/*     */     }
/*     */     
/* 209 */     CharSequence upgrade = (CharSequence)headers.get(HttpHeaderNames.UPGRADE);
/* 210 */     if (!HttpHeaderValues.WEBSOCKET.equalsIgnoreCase(upgrade)) {
/* 211 */       throw new WebSocketHandshakeException("Invalid handshake response upgrade: " + upgrade);
/*     */     }
/*     */     
/* 214 */     CharSequence connection = (CharSequence)headers.get(HttpHeaderNames.CONNECTION);
/* 215 */     if (!HttpHeaderValues.UPGRADE.equalsIgnoreCase(connection)) {
/* 216 */       throw new WebSocketHandshakeException("Invalid handshake response connection: " + connection);
/*     */     }
/*     */     
/* 219 */     CharSequence accept = (CharSequence)headers.get(HttpHeaderNames.SEC_WEBSOCKET_ACCEPT);
/* 220 */     if ((accept == null) || (!accept.equals(this.expectedChallengeResponseString))) {
/* 221 */       throw new WebSocketHandshakeException(String.format("Invalid challenge. Actual: %s. Expected: %s", new Object[] { accept, this.expectedChallengeResponseString }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected WebSocketFrameDecoder newWebsocketDecoder()
/*     */   {
/* 228 */     return new WebSocket08FrameDecoder(false, this.allowExtensions, maxFramePayloadLength(), this.allowMaskMismatch);
/*     */   }
/*     */   
/*     */   protected WebSocketFrameEncoder newWebSocketEncoder()
/*     */   {
/* 233 */     return new WebSocket08FrameEncoder(this.performMasking);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\WebSocketClientHandshaker08.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */