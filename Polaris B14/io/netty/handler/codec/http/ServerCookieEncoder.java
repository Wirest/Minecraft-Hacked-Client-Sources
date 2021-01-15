/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ServerCookieEncoder
/*     */ {
/*     */   public static String encode(String name, String value)
/*     */   {
/*  52 */     return encode(new DefaultCookie(name, value));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String encode(Cookie cookie)
/*     */   {
/*  62 */     if (cookie == null) {
/*  63 */       throw new NullPointerException("cookie");
/*     */     }
/*     */     
/*  66 */     StringBuilder buf = CookieEncoderUtil.stringBuilder();
/*     */     
/*  68 */     CookieEncoderUtil.addUnquoted(buf, cookie.name(), cookie.value());
/*     */     
/*  70 */     if (cookie.maxAge() != Long.MIN_VALUE) {
/*  71 */       CookieEncoderUtil.add(buf, "Max-Age", cookie.maxAge());
/*  72 */       Date expires = new Date(cookie.maxAge() * 1000L + System.currentTimeMillis());
/*  73 */       CookieEncoderUtil.addUnquoted(buf, "Expires", HttpHeaderDateFormat.get().format(expires));
/*     */     }
/*     */     
/*  76 */     if (cookie.path() != null) {
/*  77 */       CookieEncoderUtil.addUnquoted(buf, "Path", cookie.path());
/*     */     }
/*     */     
/*  80 */     if (cookie.domain() != null) {
/*  81 */       CookieEncoderUtil.addUnquoted(buf, "Domain", cookie.domain());
/*     */     }
/*  83 */     if (cookie.isSecure()) {
/*  84 */       buf.append("Secure");
/*  85 */       buf.append(';');
/*  86 */       buf.append(' ');
/*     */     }
/*  88 */     if (cookie.isHttpOnly()) {
/*  89 */       buf.append("HTTPOnly");
/*  90 */       buf.append(';');
/*  91 */       buf.append(' ');
/*     */     }
/*     */     
/*  94 */     return CookieEncoderUtil.stripTrailingSeparator(buf);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<String> encode(Cookie... cookies)
/*     */   {
/* 104 */     if (cookies == null) {
/* 105 */       throw new NullPointerException("cookies");
/*     */     }
/*     */     
/* 108 */     if (cookies.length == 0) {
/* 109 */       return Collections.emptyList();
/*     */     }
/*     */     
/* 112 */     List<String> encoded = new ArrayList(cookies.length);
/* 113 */     for (Cookie c : cookies) {
/* 114 */       if (c == null) {
/*     */         break;
/*     */       }
/* 117 */       encoded.add(encode(c));
/*     */     }
/* 119 */     return encoded;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<String> encode(Collection<Cookie> cookies)
/*     */   {
/* 129 */     if (cookies == null) {
/* 130 */       throw new NullPointerException("cookies");
/*     */     }
/*     */     
/* 133 */     if (cookies.isEmpty()) {
/* 134 */       return Collections.emptyList();
/*     */     }
/*     */     
/* 137 */     List<String> encoded = new ArrayList(cookies.size());
/* 138 */     for (Cookie c : cookies) {
/* 139 */       if (c == null) {
/*     */         break;
/*     */       }
/* 142 */       encoded.add(encode(c));
/*     */     }
/* 144 */     return encoded;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<String> encode(Iterable<Cookie> cookies)
/*     */   {
/* 154 */     if (cookies == null) {
/* 155 */       throw new NullPointerException("cookies");
/*     */     }
/*     */     
/* 158 */     if (!cookies.iterator().hasNext()) {
/* 159 */       return Collections.emptyList();
/*     */     }
/*     */     
/* 162 */     List<String> encoded = new ArrayList();
/* 163 */     for (Cookie c : cookies) {
/* 164 */       if (c == null) {
/*     */         break;
/*     */       }
/* 167 */       encoded.add(encode(c));
/*     */     }
/* 169 */     return encoded;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\ServerCookieEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */