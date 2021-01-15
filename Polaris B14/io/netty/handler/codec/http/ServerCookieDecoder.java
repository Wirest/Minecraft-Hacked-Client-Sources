/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ServerCookieDecoder
/*     */ {
/*     */   public static Set<Cookie> decode(String header)
/*     */   {
/*  43 */     if (header == null) {
/*  44 */       throw new NullPointerException("header");
/*     */     }
/*     */     
/*  47 */     int headerLen = header.length();
/*     */     
/*  49 */     if (headerLen == 0) {
/*  50 */       return Collections.emptySet();
/*     */     }
/*     */     
/*  53 */     Set<Cookie> cookies = new TreeSet();
/*     */     
/*  55 */     int i = 0;
/*     */     
/*  57 */     boolean rfc2965Style = false;
/*  58 */     if (header.regionMatches(true, 0, "$Version", 0, 8))
/*     */     {
/*  60 */       i = header.indexOf(';') + 1;
/*  61 */       rfc2965Style = true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  68 */     while (i != headerLen)
/*     */     {
/*     */ 
/*  71 */       char c = header.charAt(i);
/*  72 */       if ((c == '\t') || (c == '\n') || (c == '\013') || (c == '\f') || (c == '\r') || (c == ' ') || (c == ',') || (c == ';'))
/*     */       {
/*  74 */         i++;
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/*  80 */         int newNameStart = i;
/*  81 */         int newNameEnd = i;
/*     */         
/*     */         String value;
/*  84 */         if (i == headerLen) {
/*  85 */           value = null;
/*     */         } else {
/*     */           for (;;)
/*     */           {
/*  89 */             char curChar = header.charAt(i);
/*  90 */             if (curChar == ';')
/*     */             {
/*  92 */               newNameEnd = i;
/*  93 */               String value = null;
/*  94 */               break; }
/*  95 */             if (curChar == '=')
/*     */             {
/*  97 */               newNameEnd = i;
/*  98 */               i++;
/*  99 */               if (i == headerLen)
/*     */               {
/* 101 */                 String value = "";
/* 102 */                 break;
/*     */               }
/*     */               
/* 105 */               int newValueStart = i;
/* 106 */               char c = header.charAt(i);
/* 107 */               if (c == '"')
/*     */               {
/* 109 */                 StringBuilder newValueBuf = CookieEncoderUtil.stringBuilder();
/*     */                 
/* 111 */                 char q = c;
/* 112 */                 boolean hadBackslash = false;
/* 113 */                 i++;
/*     */                 for (;;) {
/* 115 */                   if (i == headerLen) {
/* 116 */                     String value = newValueBuf.toString();
/* 117 */                     break;
/*     */                   }
/* 119 */                   if (hadBackslash) {
/* 120 */                     hadBackslash = false;
/* 121 */                     c = header.charAt(i++);
/* 122 */                     if ((c == '\\') || (c == '"'))
/*     */                     {
/* 124 */                       newValueBuf.setCharAt(newValueBuf.length() - 1, c);
/*     */                     }
/*     */                     else {
/* 127 */                       newValueBuf.append(c);
/*     */                     }
/*     */                   } else {
/* 130 */                     c = header.charAt(i++);
/* 131 */                     if (c == q) {
/* 132 */                       String value = newValueBuf.toString();
/* 133 */                       break;
/*     */                     }
/* 135 */                     newValueBuf.append(c);
/* 136 */                     if (c == '\\') {
/* 137 */                       hadBackslash = true;
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               }
/*     */               
/* 143 */               int semiPos = header.indexOf(';', i);
/* 144 */               if (semiPos > 0) {
/* 145 */                 String value = header.substring(newValueStart, semiPos);
/* 146 */                 i = semiPos;
/*     */               } else {
/* 148 */                 String value = header.substring(newValueStart);
/* 149 */                 i = headerLen;
/*     */               }
/*     */               
/* 152 */               break;
/*     */             }
/* 154 */             i++;
/*     */             
/*     */ 
/* 157 */             if (i == headerLen)
/*     */             {
/* 159 */               newNameEnd = headerLen;
/* 160 */               String value = null;
/* 161 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */         String value;
/* 166 */         if ((!rfc2965Style) || ((!header.regionMatches(newNameStart, "$Path", 0, "$Path".length())) && (!header.regionMatches(newNameStart, "$Domain", 0, "$Domain".length())) && (!header.regionMatches(newNameStart, "$Port", 0, "$Port".length()))))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 171 */           String name = header.substring(newNameStart, newNameEnd);
/* 172 */           cookies.add(new DefaultCookie(name, value));
/*     */         }
/*     */       }
/*     */     }
/* 176 */     return cookies;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\ServerCookieDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */