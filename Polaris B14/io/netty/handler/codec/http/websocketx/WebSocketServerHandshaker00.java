/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpResponse;
/*     */ import io.netty.handler.codec.http.FullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaderValues;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpVersion;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WebSocketServerHandshaker00
/*     */   extends WebSocketServerHandshaker
/*     */ {
/*  48 */   private static final AsciiString WEBSOCKET = new AsciiString("WebSocket");
/*     */   
/*  50 */   private static final Pattern BEGINNING_DIGIT = Pattern.compile("[^0-9]");
/*  51 */   private static final Pattern BEGINNING_SPACE = Pattern.compile("[^ ]");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WebSocketServerHandshaker00(String webSocketURL, String subprotocols, int maxFramePayloadLength)
/*     */   {
/*  66 */     super(WebSocketVersion.V00, webSocketURL, subprotocols, maxFramePayloadLength);
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
/* 113 */     if ((!HttpHeaderValues.UPGRADE.equalsIgnoreCase((CharSequence)req.headers().get(HttpHeaderNames.CONNECTION))) || (!WEBSOCKET.equalsIgnoreCase((CharSequence)req.headers().get(HttpHeaderNames.UPGRADE))))
/*     */     {
/* 115 */       throw new WebSocketHandshakeException("not a WebSocket handshake request: missing upgrade");
/*     */     }
/*     */     
/*     */ 
/* 119 */     boolean isHixie76 = (req.headers().contains(HttpHeaderNames.SEC_WEBSOCKET_KEY1)) && (req.headers().contains(HttpHeaderNames.SEC_WEBSOCKET_KEY2));
/*     */     
/*     */ 
/*     */ 
/* 123 */     FullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, new HttpResponseStatus(101, isHixie76 ? "WebSocket Protocol Handshake" : "Web Socket Protocol Handshake"));
/*     */     
/* 125 */     if (headers != null) {
/* 126 */       res.headers().add(headers);
/*     */     }
/*     */     
/* 129 */     res.headers().add(HttpHeaderNames.UPGRADE, WEBSOCKET);
/* 130 */     res.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE);
/*     */     
/*     */ 
/* 133 */     if (isHixie76)
/*     */     {
/* 135 */       res.headers().add(HttpHeaderNames.SEC_WEBSOCKET_ORIGIN, (CharSequence)req.headers().get(HttpHeaderNames.ORIGIN));
/* 136 */       res.headers().add(HttpHeaderNames.SEC_WEBSOCKET_LOCATION, uri());
/* 137 */       String subprotocols = (String)req.headers().getAndConvert(HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL);
/* 138 */       if (subprotocols != null) {
/* 139 */         String selectedSubprotocol = selectSubprotocol(subprotocols);
/* 140 */         if (selectedSubprotocol == null) {
/* 141 */           if (logger.isDebugEnabled()) {
/* 142 */             logger.debug("Requested subprotocol(s) not supported: {}", subprotocols);
/*     */           }
/*     */         } else {
/* 145 */           res.headers().add(HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL, selectedSubprotocol);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 150 */       String key1 = (String)req.headers().getAndConvert(HttpHeaderNames.SEC_WEBSOCKET_KEY1);
/* 151 */       String key2 = (String)req.headers().getAndConvert(HttpHeaderNames.SEC_WEBSOCKET_KEY2);
/* 152 */       int a = (int)(Long.parseLong(BEGINNING_DIGIT.matcher(key1).replaceAll("")) / BEGINNING_SPACE.matcher(key1).replaceAll("").length());
/*     */       
/* 154 */       int b = (int)(Long.parseLong(BEGINNING_DIGIT.matcher(key2).replaceAll("")) / BEGINNING_SPACE.matcher(key2).replaceAll("").length());
/*     */       
/* 156 */       long c = req.content().readLong();
/* 157 */       ByteBuf input = Unpooled.buffer(16);
/* 158 */       input.writeInt(a);
/* 159 */       input.writeInt(b);
/* 160 */       input.writeLong(c);
/* 161 */       res.content().writeBytes(WebSocketUtil.md5(input.array()));
/*     */     }
/*     */     else {
/* 164 */       res.headers().add(HttpHeaderNames.WEBSOCKET_ORIGIN, (CharSequence)req.headers().get(HttpHeaderNames.ORIGIN));
/* 165 */       res.headers().add(HttpHeaderNames.WEBSOCKET_LOCATION, uri());
/* 166 */       String protocol = (String)req.headers().getAndConvert(HttpHeaderNames.WEBSOCKET_PROTOCOL);
/* 167 */       if (protocol != null) {
/* 168 */         res.headers().add(HttpHeaderNames.WEBSOCKET_PROTOCOL, selectSubprotocol(protocol));
/*     */       }
/*     */     }
/* 171 */     return res;
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
/*     */   public ChannelFuture close(Channel channel, CloseWebSocketFrame frame, ChannelPromise promise)
/*     */   {
/* 184 */     return channel.writeAndFlush(frame, promise);
/*     */   }
/*     */   
/*     */   protected WebSocketFrameDecoder newWebsocketDecoder()
/*     */   {
/* 189 */     return new WebSocket00FrameDecoder(maxFramePayloadLength());
/*     */   }
/*     */   
/*     */   protected WebSocketFrameEncoder newWebSocketEncoder()
/*     */   {
/* 194 */     return new WebSocket00FrameEncoder();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\WebSocketServerHandshaker00.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */