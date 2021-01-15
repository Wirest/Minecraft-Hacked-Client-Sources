/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpMethod
/*     */   implements Comparable<HttpMethod>
/*     */ {
/*  36 */   public static final HttpMethod OPTIONS = new HttpMethod("OPTIONS");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  44 */   public static final HttpMethod GET = new HttpMethod("GET");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  50 */   public static final HttpMethod HEAD = new HttpMethod("HEAD");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  57 */   public static final HttpMethod POST = new HttpMethod("POST");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  62 */   public static final HttpMethod PUT = new HttpMethod("PUT");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  68 */   public static final HttpMethod PATCH = new HttpMethod("PATCH");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  74 */   public static final HttpMethod DELETE = new HttpMethod("DELETE");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  80 */   public static final HttpMethod TRACE = new HttpMethod("TRACE");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  86 */   public static final HttpMethod CONNECT = new HttpMethod("CONNECT");
/*     */   
/*  88 */   private static final Map<String, HttpMethod> methodMap = new HashMap();
/*     */   private final AsciiString name;
/*     */   private final String nameAsString;
/*     */   
/*  92 */   static { methodMap.put(OPTIONS.toString(), OPTIONS);
/*  93 */     methodMap.put(GET.toString(), GET);
/*  94 */     methodMap.put(HEAD.toString(), HEAD);
/*  95 */     methodMap.put(POST.toString(), POST);
/*  96 */     methodMap.put(PUT.toString(), PUT);
/*  97 */     methodMap.put(PATCH.toString(), PATCH);
/*  98 */     methodMap.put(DELETE.toString(), DELETE);
/*  99 */     methodMap.put(TRACE.toString(), TRACE);
/* 100 */     methodMap.put(CONNECT.toString(), CONNECT);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HttpMethod valueOf(String name)
/*     */   {
/* 109 */     if (name == null) {
/* 110 */       throw new NullPointerException("name");
/*     */     }
/*     */     
/* 113 */     name = name.trim();
/* 114 */     if (name.isEmpty()) {
/* 115 */       throw new IllegalArgumentException("empty name");
/*     */     }
/*     */     
/* 118 */     HttpMethod result = (HttpMethod)methodMap.get(name);
/* 119 */     if (result != null) {
/* 120 */       return result;
/*     */     }
/* 122 */     return new HttpMethod(name);
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
/*     */   public HttpMethod(String name)
/*     */   {
/* 137 */     if (name == null) {
/* 138 */       throw new NullPointerException("name");
/*     */     }
/*     */     
/* 141 */     name = name.trim();
/* 142 */     if (name.isEmpty()) {
/* 143 */       throw new IllegalArgumentException("empty name");
/*     */     }
/*     */     
/* 146 */     for (int i = 0; i < name.length(); i++) {
/* 147 */       char c = name.charAt(i);
/* 148 */       if ((Character.isISOControl(c)) || (Character.isWhitespace(c))) {
/* 149 */         throw new IllegalArgumentException("invalid character in name");
/*     */       }
/*     */     }
/*     */     
/* 153 */     this.name = new AsciiString(name);
/* 154 */     this.nameAsString = name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public AsciiString name()
/*     */   {
/* 161 */     return this.name;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 166 */     return name().hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 171 */     if (!(o instanceof HttpMethod)) {
/* 172 */       return false;
/*     */     }
/*     */     
/* 175 */     HttpMethod that = (HttpMethod)o;
/* 176 */     return name().equals(that.name());
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 181 */     return this.nameAsString;
/*     */   }
/*     */   
/*     */   public int compareTo(HttpMethod o)
/*     */   {
/* 186 */     return name().compareTo(o.name());
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */