/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.handler.codec.base64.Base64;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class WebSocketUtil
/*     */ {
/*     */   static byte[] md5(byte[] data)
/*     */   {
/*     */     try
/*     */     {
/*  39 */       MessageDigest md = MessageDigest.getInstance("MD5");
/*     */       
/*  41 */       return md.digest(data);
/*     */     }
/*     */     catch (NoSuchAlgorithmException e) {
/*  44 */       throw new InternalError("MD5 not supported on this platform - Outdated?");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static byte[] sha1(byte[] data)
/*     */   {
/*     */     try
/*     */     {
/*  57 */       MessageDigest md = MessageDigest.getInstance("SHA1");
/*     */       
/*  59 */       return md.digest(data);
/*     */     }
/*     */     catch (NoSuchAlgorithmException e) {
/*  62 */       throw new InternalError("SHA-1 is not supported on this platform - Outdated?");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static String base64(byte[] data)
/*     */   {
/*  73 */     ByteBuf encodedData = Unpooled.wrappedBuffer(data);
/*  74 */     ByteBuf encoded = Base64.encode(encodedData);
/*  75 */     String encodedString = encoded.toString(CharsetUtil.UTF_8);
/*  76 */     encoded.release();
/*  77 */     return encodedString;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static byte[] randomBytes(int size)
/*     */   {
/*  87 */     byte[] bytes = new byte[size];
/*     */     
/*  89 */     for (int index = 0; index < size; index++) {
/*  90 */       bytes[index] = ((byte)randomNumber(0, 255));
/*     */     }
/*     */     
/*  93 */     return bytes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static int randomNumber(int minimum, int maximum)
/*     */   {
/* 104 */     return (int)(Math.random() * maximum + minimum);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\WebSocketUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */