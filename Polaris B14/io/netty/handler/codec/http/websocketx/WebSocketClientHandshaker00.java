/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
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
/*     */ import java.net.URI;
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WebSocketClientHandshaker00
/*     */   extends WebSocketClientHandshaker
/*     */ {
/*  46 */   private static final AsciiString WEBSOCKET = new AsciiString("WebSocket");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ByteBuf expectedChallengeResponseBytes;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WebSocketClientHandshaker00(URI webSocketURL, WebSocketVersion version, String subprotocol, HttpHeaders customHeaders, int maxFramePayloadLength)
/*     */   {
/*  67 */     super(webSocketURL, version, subprotocol, customHeaders, maxFramePayloadLength);
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
/*  91 */     int spaces1 = WebSocketUtil.randomNumber(1, 12);
/*  92 */     int spaces2 = WebSocketUtil.randomNumber(1, 12);
/*     */     
/*  94 */     int max1 = Integer.MAX_VALUE / spaces1;
/*  95 */     int max2 = Integer.MAX_VALUE / spaces2;
/*     */     
/*  97 */     int number1 = WebSocketUtil.randomNumber(0, max1);
/*  98 */     int number2 = WebSocketUtil.randomNumber(0, max2);
/*     */     
/* 100 */     int product1 = number1 * spaces1;
/* 101 */     int product2 = number2 * spaces2;
/*     */     
/* 103 */     String key1 = Integer.toString(product1);
/* 104 */     String key2 = Integer.toString(product2);
/*     */     
/* 106 */     key1 = insertRandomCharacters(key1);
/* 107 */     key2 = insertRandomCharacters(key2);
/*     */     
/* 109 */     key1 = insertSpaces(key1, spaces1);
/* 110 */     key2 = insertSpaces(key2, spaces2);
/*     */     
/* 112 */     byte[] key3 = WebSocketUtil.randomBytes(8);
/*     */     
/* 114 */     ByteBuffer buffer = ByteBuffer.allocate(4);
/* 115 */     buffer.putInt(number1);
/* 116 */     byte[] number1Array = buffer.array();
/* 117 */     buffer = ByteBuffer.allocate(4);
/* 118 */     buffer.putInt(number2);
/* 119 */     byte[] number2Array = buffer.array();
/*     */     
/* 121 */     byte[] challenge = new byte[16];
/* 122 */     System.arraycopy(number1Array, 0, challenge, 0, 4);
/* 123 */     System.arraycopy(number2Array, 0, challenge, 4, 4);
/* 124 */     System.arraycopy(key3, 0, challenge, 8, 8);
/* 125 */     this.expectedChallengeResponseBytes = Unpooled.wrappedBuffer(WebSocketUtil.md5(challenge));
/*     */     
/*     */ 
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
/* 139 */     FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, path);
/* 140 */     HttpHeaders headers = request.headers();
/* 141 */     headers.add(HttpHeaderNames.UPGRADE, WEBSOCKET).add(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE).add(HttpHeaderNames.HOST, wsURL.getHost());
/*     */     
/*     */ 
/*     */ 
/* 145 */     int wsPort = wsURL.getPort();
/* 146 */     String originValue = "http://" + wsURL.getHost();
/* 147 */     if ((wsPort != 80) && (wsPort != 443))
/*     */     {
/*     */ 
/* 150 */       originValue = originValue + ':' + wsPort;
/*     */     }
/*     */     
/* 153 */     headers.add(HttpHeaderNames.ORIGIN, originValue).add(HttpHeaderNames.SEC_WEBSOCKET_KEY1, key1).add(HttpHeaderNames.SEC_WEBSOCKET_KEY2, key2);
/*     */     
/*     */ 
/*     */ 
/* 157 */     String expectedSubprotocol = expectedSubprotocol();
/* 158 */     if ((expectedSubprotocol != null) && (!expectedSubprotocol.isEmpty())) {
/* 159 */       headers.add(HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL, expectedSubprotocol);
/*     */     }
/*     */     
/* 162 */     if (this.customHeaders != null) {
/* 163 */       headers.add(this.customHeaders);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 168 */     headers.setInt(HttpHeaderNames.CONTENT_LENGTH, key3.length);
/* 169 */     request.content().writeBytes(key3);
/* 170 */     return request;
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
/*     */   protected void verify(FullHttpResponse response)
/*     */   {
/* 195 */     HttpResponseStatus status = new HttpResponseStatus(101, "WebSocket Protocol Handshake");
/*     */     
/* 197 */     if (!response.status().equals(status)) {
/* 198 */       throw new WebSocketHandshakeException("Invalid handshake response getStatus: " + response.status());
/*     */     }
/*     */     
/* 201 */     HttpHeaders headers = response.headers();
/*     */     
/* 203 */     CharSequence upgrade = (CharSequence)headers.get(HttpHeaderNames.UPGRADE);
/* 204 */     if (!WEBSOCKET.equalsIgnoreCase(upgrade)) {
/* 205 */       throw new WebSocketHandshakeException("Invalid handshake response upgrade: " + upgrade);
/*     */     }
/*     */     
/*     */ 
/* 209 */     CharSequence connection = (CharSequence)headers.get(HttpHeaderNames.CONNECTION);
/* 210 */     if (!HttpHeaderValues.UPGRADE.equalsIgnoreCase(connection)) {
/* 211 */       throw new WebSocketHandshakeException("Invalid handshake response connection: " + connection);
/*     */     }
/*     */     
/*     */ 
/* 215 */     ByteBuf challenge = response.content();
/* 216 */     if (!challenge.equals(this.expectedChallengeResponseBytes)) {
/* 217 */       throw new WebSocketHandshakeException("Invalid challenge");
/*     */     }
/*     */   }
/*     */   
/*     */   private static String insertRandomCharacters(String key) {
/* 222 */     int count = WebSocketUtil.randomNumber(1, 12);
/*     */     
/* 224 */     char[] randomChars = new char[count];
/* 225 */     int randCount = 0;
/* 226 */     while (randCount < count) {
/* 227 */       int rand = (int)(Math.random() * 126.0D + 33.0D);
/* 228 */       if (((33 < rand) && (rand < 47)) || ((58 < rand) && (rand < 126))) {
/* 229 */         randomChars[randCount] = ((char)rand);
/* 230 */         randCount++;
/*     */       }
/*     */     }
/*     */     
/* 234 */     for (int i = 0; i < count; i++) {
/* 235 */       int split = WebSocketUtil.randomNumber(0, key.length());
/* 236 */       String part1 = key.substring(0, split);
/* 237 */       String part2 = key.substring(split);
/* 238 */       key = part1 + randomChars[i] + part2;
/*     */     }
/*     */     
/* 241 */     return key;
/*     */   }
/*     */   
/*     */   private static String insertSpaces(String key, int spaces) {
/* 245 */     for (int i = 0; i < spaces; i++) {
/* 246 */       int split = WebSocketUtil.randomNumber(1, key.length() - 1);
/* 247 */       String part1 = key.substring(0, split);
/* 248 */       String part2 = key.substring(split);
/* 249 */       key = part1 + ' ' + part2;
/*     */     }
/*     */     
/* 252 */     return key;
/*     */   }
/*     */   
/*     */   protected WebSocketFrameDecoder newWebsocketDecoder()
/*     */   {
/* 257 */     return new WebSocket00FrameDecoder(maxFramePayloadLength());
/*     */   }
/*     */   
/*     */   protected WebSocketFrameEncoder newWebSocketEncoder()
/*     */   {
/* 262 */     return new WebSocket00FrameEncoder();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\WebSocketClientHandshaker00.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */