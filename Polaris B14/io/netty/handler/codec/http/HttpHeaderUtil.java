/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class HttpHeaderUtil
/*     */ {
/*     */   public static boolean isKeepAlive(HttpMessage message)
/*     */   {
/*  33 */     CharSequence connection = (CharSequence)message.headers().get(HttpHeaderNames.CONNECTION);
/*  34 */     if ((connection != null) && (HttpHeaderValues.CLOSE.equalsIgnoreCase(connection))) {
/*  35 */       return false;
/*     */     }
/*     */     
/*  38 */     if (message.protocolVersion().isKeepAliveDefault()) {
/*  39 */       return !HttpHeaderValues.CLOSE.equalsIgnoreCase(connection);
/*     */     }
/*  41 */     return HttpHeaderValues.KEEP_ALIVE.equalsIgnoreCase(connection);
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
/*     */   public static void setKeepAlive(HttpMessage message, boolean keepAlive)
/*     */   {
/*  65 */     HttpHeaders h = message.headers();
/*  66 */     if (message.protocolVersion().isKeepAliveDefault()) {
/*  67 */       if (keepAlive) {
/*  68 */         h.remove(HttpHeaderNames.CONNECTION);
/*     */       } else {
/*  70 */         h.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
/*     */       }
/*     */     }
/*  73 */     else if (keepAlive) {
/*  74 */       h.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
/*     */     } else {
/*  76 */       h.remove(HttpHeaderNames.CONNECTION);
/*     */     }
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
/*     */   public static long getContentLength(HttpMessage message)
/*     */   {
/*  94 */     Long value = message.headers().getLong(HttpHeaderNames.CONTENT_LENGTH);
/*  95 */     if (value != null) {
/*  96 */       return value.longValue();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 101 */     long webSocketContentLength = getWebSocketContentLength(message);
/* 102 */     if (webSocketContentLength >= 0L) {
/* 103 */       return webSocketContentLength;
/*     */     }
/*     */     
/*     */ 
/* 107 */     throw new NumberFormatException("header not found: " + HttpHeaderNames.CONTENT_LENGTH);
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
/*     */   public static long getContentLength(HttpMessage message, long defaultValue)
/*     */   {
/* 121 */     Long value = message.headers().getLong(HttpHeaderNames.CONTENT_LENGTH);
/* 122 */     if (value != null) {
/* 123 */       return value.longValue();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 128 */     long webSocketContentLength = getWebSocketContentLength(message);
/* 129 */     if (webSocketContentLength >= 0L) {
/* 130 */       return webSocketContentLength;
/*     */     }
/*     */     
/*     */ 
/* 134 */     return defaultValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int getWebSocketContentLength(HttpMessage message)
/*     */   {
/* 143 */     HttpHeaders h = message.headers();
/* 144 */     if ((message instanceof HttpRequest)) {
/* 145 */       HttpRequest req = (HttpRequest)message;
/* 146 */       if ((HttpMethod.GET.equals(req.method())) && (h.contains(HttpHeaderNames.SEC_WEBSOCKET_KEY1)) && (h.contains(HttpHeaderNames.SEC_WEBSOCKET_KEY2)))
/*     */       {
/*     */ 
/* 149 */         return 8;
/*     */       }
/* 151 */     } else if ((message instanceof HttpResponse)) {
/* 152 */       HttpResponse res = (HttpResponse)message;
/* 153 */       if ((res.status().code() == 101) && (h.contains(HttpHeaderNames.SEC_WEBSOCKET_ORIGIN)) && (h.contains(HttpHeaderNames.SEC_WEBSOCKET_LOCATION)))
/*     */       {
/*     */ 
/* 156 */         return 16;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 161 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void setContentLength(HttpMessage message, long length)
/*     */   {
/* 168 */     message.headers().setLong(HttpHeaderNames.CONTENT_LENGTH, length);
/*     */   }
/*     */   
/*     */   public static boolean isContentLengthSet(HttpMessage m) {
/* 172 */     return m.headers().contains(HttpHeaderNames.CONTENT_LENGTH);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean is100ContinueExpected(HttpMessage message)
/*     */   {
/* 181 */     if (!(message instanceof HttpRequest)) {
/* 182 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 186 */     if (message.protocolVersion().compareTo(HttpVersion.HTTP_1_1) < 0) {
/* 187 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 191 */     CharSequence value = (CharSequence)message.headers().get(HttpHeaderNames.EXPECT);
/* 192 */     if (value == null) {
/* 193 */       return false;
/*     */     }
/* 195 */     if (HttpHeaderValues.CONTINUE.equalsIgnoreCase(value)) {
/* 196 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 200 */     return message.headers().contains(HttpHeaderNames.EXPECT, HttpHeaderValues.CONTINUE, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void set100ContinueExpected(HttpMessage message, boolean expected)
/*     */   {
/* 211 */     if (expected) {
/* 212 */       message.headers().set(HttpHeaderNames.EXPECT, HttpHeaderValues.CONTINUE);
/*     */     } else {
/* 214 */       message.headers().remove(HttpHeaderNames.EXPECT);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isTransferEncodingChunked(HttpMessage message)
/*     */   {
/* 225 */     return message.headers().contains(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED, true);
/*     */   }
/*     */   
/*     */   public static void setTransferEncodingChunked(HttpMessage m, boolean chunked) {
/* 229 */     if (chunked) {
/* 230 */       m.headers().add(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
/* 231 */       m.headers().remove(HttpHeaderNames.CONTENT_LENGTH);
/*     */     } else {
/* 233 */       List<CharSequence> values = m.headers().getAll(HttpHeaderNames.TRANSFER_ENCODING);
/* 234 */       if (values.isEmpty()) {
/* 235 */         return;
/*     */       }
/* 237 */       Iterator<CharSequence> valuesIt = values.iterator();
/* 238 */       while (valuesIt.hasNext()) {
/* 239 */         CharSequence value = (CharSequence)valuesIt.next();
/* 240 */         if (HttpHeaderValues.CHUNKED.equalsIgnoreCase(value)) {
/* 241 */           valuesIt.remove();
/*     */         }
/*     */       }
/* 244 */       if (values.isEmpty()) {
/* 245 */         m.headers().remove(HttpHeaderNames.TRANSFER_ENCODING);
/*     */       } else {
/* 247 */         m.headers().set(HttpHeaderNames.TRANSFER_ENCODING, values);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static void encodeAscii0(CharSequence seq, ByteBuf buf) {
/* 253 */     int length = seq.length();
/* 254 */     for (int i = 0; i < length; i++) {
/* 255 */       buf.writeByte((byte)seq.charAt(i));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpHeaderUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */