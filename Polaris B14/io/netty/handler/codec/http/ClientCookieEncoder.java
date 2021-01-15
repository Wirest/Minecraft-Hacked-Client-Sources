/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ClientCookieEncoder
/*     */ {
/*     */   public static String encode(String name, String value)
/*     */   {
/*  47 */     return encode(new DefaultCookie(name, value));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String encode(Cookie cookie)
/*     */   {
/*  57 */     if (cookie == null) {
/*  58 */       throw new NullPointerException("cookie");
/*     */     }
/*     */     
/*  61 */     StringBuilder buf = CookieEncoderUtil.stringBuilder();
/*  62 */     encode(buf, cookie);
/*  63 */     return CookieEncoderUtil.stripTrailingSeparator(buf);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String encode(Cookie... cookies)
/*     */   {
/*  73 */     if (cookies == null) {
/*  74 */       throw new NullPointerException("cookies");
/*     */     }
/*     */     
/*  77 */     if (cookies.length == 0) {
/*  78 */       return null;
/*     */     }
/*     */     
/*  81 */     StringBuilder buf = CookieEncoderUtil.stringBuilder();
/*  82 */     for (Cookie c : cookies) {
/*  83 */       if (c == null) {
/*     */         break;
/*     */       }
/*     */       
/*  87 */       encode(buf, c);
/*     */     }
/*  89 */     return CookieEncoderUtil.stripTrailingSeparatorOrNull(buf);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String encode(Iterable<Cookie> cookies)
/*     */   {
/*  99 */     if (cookies == null) {
/* 100 */       throw new NullPointerException("cookies");
/*     */     }
/*     */     
/* 103 */     if (!cookies.iterator().hasNext()) {
/* 104 */       return null;
/*     */     }
/*     */     
/* 107 */     StringBuilder buf = CookieEncoderUtil.stringBuilder();
/* 108 */     for (Cookie c : cookies) {
/* 109 */       if (c == null) {
/*     */         break;
/*     */       }
/*     */       
/* 113 */       encode(buf, c);
/*     */     }
/* 115 */     return CookieEncoderUtil.stripTrailingSeparatorOrNull(buf);
/*     */   }
/*     */   
/*     */   private static void encode(StringBuilder buf, Cookie c)
/*     */   {
/* 120 */     String value = c.value() != null ? c.value() : c.rawValue() != null ? c.rawValue() : "";
/*     */     
/* 122 */     CookieEncoderUtil.addUnquoted(buf, c.name(), value);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\ClientCookieEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */