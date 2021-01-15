/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import java.text.ParsePosition;
/*     */ import java.util.Date;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ClientCookieDecoder
/*     */ {
/*     */   public static Cookie decode(String header)
/*     */   {
/*  40 */     if (header == null) {
/*  41 */       throw new NullPointerException("header");
/*     */     }
/*     */     
/*  44 */     int headerLen = header.length();
/*     */     
/*  46 */     if (headerLen == 0) {
/*  47 */       return null;
/*     */     }
/*     */     
/*  50 */     CookieBuilder cookieBuilder = null;
/*     */     
/*  52 */     int i = 0;
/*     */     
/*     */ 
/*     */ 
/*  56 */     while (i != headerLen)
/*     */     {
/*     */ 
/*  59 */       char c = header.charAt(i);
/*  60 */       if (c == ',') {
/*     */         break;
/*     */       }
/*     */       
/*     */ 
/*  65 */       if ((c == '\t') || (c == '\n') || (c == '\013') || (c == '\f') || (c == '\r') || (c == ' ') || (c == ';'))
/*     */       {
/*  67 */         i++;
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/*  73 */         int newNameStart = i;
/*  74 */         int newNameEnd = i;
/*     */         
/*     */         String value;
/*  77 */         if (i == headerLen) { String rawValue;
/*  78 */           value = rawValue = null;
/*     */         }
/*     */         else {
/*     */           for (;;) {
/*  82 */             char curChar = header.charAt(i);
/*  83 */             if (curChar == ';')
/*     */             {
/*  85 */               newNameEnd = i;
/*  86 */               String rawValue; String value = rawValue = null;
/*  87 */               break; }
/*  88 */             if (curChar == '=')
/*     */             {
/*  90 */               newNameEnd = i;
/*  91 */               i++;
/*  92 */               if (i == headerLen) {
/*     */                 String rawValue;
/*  94 */                 String value = rawValue = "";
/*  95 */                 break;
/*     */               }
/*     */               
/*  98 */               int newValueStart = i;
/*  99 */               char c = header.charAt(i);
/* 100 */               if (c == '"')
/*     */               {
/* 102 */                 StringBuilder newValueBuf = CookieEncoderUtil.stringBuilder();
/*     */                 
/* 104 */                 int rawValueStart = i;
/* 105 */                 int rawValueEnd = i;
/*     */                 
/* 107 */                 char q = c;
/* 108 */                 boolean hadBackslash = false;
/* 109 */                 i++;
/*     */                 for (;;) {
/* 111 */                   if (i == headerLen) {
/* 112 */                     String value = newValueBuf.toString();
/*     */                     
/*     */ 
/* 115 */                     String rawValue = header.substring(rawValueStart, rawValueEnd);
/* 116 */                     break;
/*     */                   }
/* 118 */                   if (hadBackslash) {
/* 119 */                     hadBackslash = false;
/* 120 */                     c = header.charAt(i++);
/* 121 */                     rawValueEnd = i;
/* 122 */                     if ((c == '\\') || (c == '"')) {
/* 123 */                       newValueBuf.setCharAt(newValueBuf.length() - 1, c);
/*     */                     }
/*     */                     else {
/* 126 */                       newValueBuf.append(c);
/*     */                     }
/*     */                   } else {
/* 129 */                     c = header.charAt(i++);
/* 130 */                     rawValueEnd = i;
/* 131 */                     if (c == q) {
/* 132 */                       String value = newValueBuf.toString();
/*     */                       
/*     */ 
/*     */ 
/* 136 */                       String rawValue = header.substring(rawValueStart, rawValueEnd);
/* 137 */                       break;
/*     */                     }
/* 139 */                     newValueBuf.append(c);
/* 140 */                     if (c == '\\') {
/* 141 */                       hadBackslash = true;
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               }
/*     */               
/* 147 */               int semiPos = header.indexOf(';', i);
/* 148 */               if (semiPos > 0) { String rawValue;
/* 149 */                 String value = rawValue = header.substring(newValueStart, semiPos);
/* 150 */                 i = semiPos;
/*     */               } else { String rawValue;
/* 152 */                 String value = rawValue = header.substring(newValueStart);
/* 153 */                 i = headerLen;
/*     */               }
/*     */               
/* 156 */               break;
/*     */             }
/* 158 */             i++;
/*     */             
/*     */ 
/* 161 */             if (i == headerLen)
/*     */             {
/* 163 */               newNameEnd = i;
/* 164 */               String rawValue; String value = rawValue = null;
/* 165 */               break;
/*     */             }
/*     */           } }
/*     */         String rawValue;
/*     */         String value;
/* 170 */         if (cookieBuilder == null) {
/* 171 */           cookieBuilder = new CookieBuilder(header, newNameStart, newNameEnd, value, rawValue);
/*     */         } else
/* 173 */           cookieBuilder.appendAttribute(header, newNameStart, newNameEnd, value);
/*     */       }
/*     */     }
/* 176 */     return cookieBuilder.cookie();
/*     */   }
/*     */   
/*     */   private static class CookieBuilder
/*     */   {
/*     */     private final String name;
/*     */     private final String value;
/*     */     private final String rawValue;
/*     */     private String domain;
/*     */     private String path;
/* 186 */     private long maxAge = Long.MIN_VALUE;
/*     */     private String expires;
/*     */     private boolean secure;
/*     */     private boolean httpOnly;
/*     */     
/*     */     public CookieBuilder(String header, int keyStart, int keyEnd, String value, String rawValue)
/*     */     {
/* 193 */       this.name = header.substring(keyStart, keyEnd);
/* 194 */       this.value = value;
/* 195 */       this.rawValue = rawValue;
/*     */     }
/*     */     
/*     */     private long mergeMaxAgeAndExpire(long maxAge, String expires)
/*     */     {
/* 200 */       if (maxAge != Long.MIN_VALUE)
/* 201 */         return maxAge;
/* 202 */       if (expires != null) {
/* 203 */         Date expiresDate = HttpHeaderDateFormat.get().parse(expires, new ParsePosition(0));
/* 204 */         if (expiresDate != null) {
/* 205 */           long maxAgeMillis = expiresDate.getTime() - System.currentTimeMillis();
/* 206 */           return maxAgeMillis / 1000L + (maxAgeMillis % 1000L != 0L ? 1 : 0);
/*     */         }
/*     */       }
/* 209 */       return Long.MIN_VALUE;
/*     */     }
/*     */     
/*     */     public Cookie cookie() {
/* 213 */       if (this.name == null) {
/* 214 */         return null;
/*     */       }
/*     */       
/* 217 */       DefaultCookie cookie = new DefaultCookie(this.name, this.value);
/* 218 */       cookie.setValue(this.value);
/* 219 */       cookie.setRawValue(this.rawValue);
/* 220 */       cookie.setDomain(this.domain);
/* 221 */       cookie.setPath(this.path);
/* 222 */       cookie.setMaxAge(mergeMaxAgeAndExpire(this.maxAge, this.expires));
/* 223 */       cookie.setSecure(this.secure);
/* 224 */       cookie.setHttpOnly(this.httpOnly);
/* 225 */       return cookie;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void appendAttribute(String header, int keyStart, int keyEnd, String value)
/*     */     {
/* 243 */       setCookieAttribute(header, keyStart, keyEnd, value);
/*     */     }
/*     */     
/*     */ 
/*     */     private void setCookieAttribute(String header, int keyStart, int keyEnd, String value)
/*     */     {
/* 249 */       int length = keyEnd - keyStart;
/*     */       
/* 251 */       if (length == 4) {
/* 252 */         parse4(header, keyStart, value);
/* 253 */       } else if (length == 6) {
/* 254 */         parse6(header, keyStart, value);
/* 255 */       } else if (length == 7) {
/* 256 */         parse7(header, keyStart, value);
/* 257 */       } else if (length == 8) {
/* 258 */         parse8(header, keyStart, value);
/*     */       }
/*     */     }
/*     */     
/*     */     private void parse4(String header, int nameStart, String value) {
/* 263 */       if (header.regionMatches(true, nameStart, "Path", 0, 4)) {
/* 264 */         this.path = value;
/*     */       }
/*     */     }
/*     */     
/*     */     private void parse6(String header, int nameStart, String value) {
/* 269 */       if (header.regionMatches(true, nameStart, "Domain", 0, 5)) {
/* 270 */         this.domain = (value.isEmpty() ? null : value);
/* 271 */       } else if (header.regionMatches(true, nameStart, "Secure", 0, 5)) {
/* 272 */         this.secure = true;
/*     */       }
/*     */     }
/*     */     
/*     */     private void setExpire(String value) {
/* 277 */       this.expires = value;
/*     */     }
/*     */     
/*     */     private void setMaxAge(String value) {
/*     */       try {
/* 282 */         this.maxAge = Math.max(Long.valueOf(value).longValue(), 0L);
/*     */       }
/*     */       catch (NumberFormatException e1) {}
/*     */     }
/*     */     
/*     */     private void parse7(String header, int nameStart, String value)
/*     */     {
/* 289 */       if (header.regionMatches(true, nameStart, "Expires", 0, 7)) {
/* 290 */         setExpire(value);
/* 291 */       } else if (header.regionMatches(true, nameStart, "Max-Age", 0, 7)) {
/* 292 */         setMaxAge(value);
/*     */       }
/*     */     }
/*     */     
/*     */     private void parse8(String header, int nameStart, String value)
/*     */     {
/* 298 */       if (header.regionMatches(true, nameStart, "HttpOnly", 0, 8)) {
/* 299 */         this.httpOnly = true;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\ClientCookieDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */