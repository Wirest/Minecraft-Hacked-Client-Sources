/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class QueryStringDecoder
/*     */ {
/*     */   private static final int DEFAULT_MAX_PARAMS = 1024;
/*     */   private final Charset charset;
/*     */   private final String uri;
/*     */   private final boolean hasPath;
/*     */   private final int maxParams;
/*     */   private String path;
/*     */   private Map<String, List<String>> params;
/*     */   private int nParams;
/*     */   
/*     */   public QueryStringDecoder(String uri)
/*     */   {
/*  73 */     this(uri, HttpConstants.DEFAULT_CHARSET);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public QueryStringDecoder(String uri, boolean hasPath)
/*     */   {
/*  81 */     this(uri, HttpConstants.DEFAULT_CHARSET, hasPath);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public QueryStringDecoder(String uri, Charset charset)
/*     */   {
/*  89 */     this(uri, charset, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public QueryStringDecoder(String uri, Charset charset, boolean hasPath)
/*     */   {
/*  97 */     this(uri, charset, hasPath, 1024);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public QueryStringDecoder(String uri, Charset charset, boolean hasPath, int maxParams)
/*     */   {
/* 105 */     if (uri == null) {
/* 106 */       throw new NullPointerException("getUri");
/*     */     }
/* 108 */     if (charset == null) {
/* 109 */       throw new NullPointerException("charset");
/*     */     }
/* 111 */     if (maxParams <= 0) {
/* 112 */       throw new IllegalArgumentException("maxParams: " + maxParams + " (expected: a positive integer)");
/*     */     }
/*     */     
/*     */ 
/* 116 */     this.uri = uri;
/* 117 */     this.charset = charset;
/* 118 */     this.maxParams = maxParams;
/* 119 */     this.hasPath = hasPath;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public QueryStringDecoder(URI uri)
/*     */   {
/* 127 */     this(uri, HttpConstants.DEFAULT_CHARSET);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public QueryStringDecoder(URI uri, Charset charset)
/*     */   {
/* 135 */     this(uri, charset, 1024);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public QueryStringDecoder(URI uri, Charset charset, int maxParams)
/*     */   {
/* 143 */     if (uri == null) {
/* 144 */       throw new NullPointerException("getUri");
/*     */     }
/* 146 */     if (charset == null) {
/* 147 */       throw new NullPointerException("charset");
/*     */     }
/* 149 */     if (maxParams <= 0) {
/* 150 */       throw new IllegalArgumentException("maxParams: " + maxParams + " (expected: a positive integer)");
/*     */     }
/*     */     
/*     */ 
/* 154 */     String rawPath = uri.getRawPath();
/* 155 */     if (rawPath != null) {
/* 156 */       this.hasPath = true;
/*     */     } else {
/* 158 */       rawPath = "";
/* 159 */       this.hasPath = false;
/*     */     }
/*     */     
/* 162 */     this.uri = (rawPath + (uri.getRawQuery() == null ? "" : new StringBuilder().append('?').append(uri.getRawQuery()).toString()));
/*     */     
/* 164 */     this.charset = charset;
/* 165 */     this.maxParams = maxParams;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String uri()
/*     */   {
/* 172 */     return this.uri;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String path()
/*     */   {
/* 179 */     if (this.path == null) {
/* 180 */       if (!this.hasPath) {
/* 181 */         return this.path = "";
/*     */       }
/*     */       
/* 184 */       int pathEndPos = this.uri.indexOf('?');
/* 185 */       if (pathEndPos < 0) {
/* 186 */         this.path = this.uri;
/*     */       } else {
/* 188 */         return this.path = this.uri.substring(0, pathEndPos);
/*     */       }
/*     */     }
/* 191 */     return this.path;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Map<String, List<String>> parameters()
/*     */   {
/* 198 */     if (this.params == null) {
/* 199 */       if (this.hasPath) {
/* 200 */         int pathLength = path().length();
/* 201 */         if (this.uri.length() == pathLength) {
/* 202 */           return Collections.emptyMap();
/*     */         }
/* 204 */         decodeParams(this.uri.substring(pathLength + 1));
/*     */       } else {
/* 206 */         if (this.uri.isEmpty()) {
/* 207 */           return Collections.emptyMap();
/*     */         }
/* 209 */         decodeParams(this.uri);
/*     */       }
/*     */     }
/* 212 */     return this.params;
/*     */   }
/*     */   
/*     */   private void decodeParams(String s) {
/* 216 */     Map<String, List<String>> params = this.params = new LinkedHashMap();
/* 217 */     this.nParams = 0;
/* 218 */     String name = null;
/* 219 */     int pos = 0;
/*     */     
/*     */ 
/* 222 */     for (int i = 0; i < s.length(); i++) {
/* 223 */       char c = s.charAt(i);
/* 224 */       if ((c == '=') && (name == null)) {
/* 225 */         if (pos != i) {
/* 226 */           name = decodeComponent(s.substring(pos, i), this.charset);
/*     */         }
/* 228 */         pos = i + 1;
/*     */       }
/* 230 */       else if ((c == '&') || (c == ';')) {
/* 231 */         if ((name == null) && (pos != i))
/*     */         {
/*     */ 
/*     */ 
/* 235 */           if (addParam(params, decodeComponent(s.substring(pos, i), this.charset), "")) {}
/*     */ 
/*     */         }
/* 238 */         else if (name != null) {
/* 239 */           if (!addParam(params, name, decodeComponent(s.substring(pos, i), this.charset))) {
/* 240 */             return;
/*     */           }
/* 242 */           name = null;
/*     */         }
/* 244 */         pos = i + 1;
/*     */       }
/*     */     }
/*     */     
/* 248 */     if (pos != i) {
/* 249 */       if (name == null) {
/* 250 */         addParam(params, decodeComponent(s.substring(pos, i), this.charset), "");
/*     */       } else {
/* 252 */         addParam(params, name, decodeComponent(s.substring(pos, i), this.charset));
/*     */       }
/* 254 */     } else if (name != null) {
/* 255 */       addParam(params, name, "");
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean addParam(Map<String, List<String>> params, String name, String value) {
/* 260 */     if (this.nParams >= this.maxParams) {
/* 261 */       return false;
/*     */     }
/*     */     
/* 264 */     List<String> values = (List)params.get(name);
/* 265 */     if (values == null) {
/* 266 */       values = new ArrayList(1);
/* 267 */       params.put(name, values);
/*     */     }
/* 269 */     values.add(value);
/* 270 */     this.nParams += 1;
/* 271 */     return true;
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
/*     */   public static String decodeComponent(String s)
/*     */   {
/* 286 */     return decodeComponent(s, HttpConstants.DEFAULT_CHARSET);
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
/*     */   public static String decodeComponent(String s, Charset charset)
/*     */   {
/* 312 */     if (s == null) {
/* 313 */       return "";
/*     */     }
/* 315 */     int size = s.length();
/* 316 */     boolean modified = false;
/* 317 */     for (int i = 0; i < size; i++) {
/* 318 */       char c = s.charAt(i);
/* 319 */       if ((c == '%') || (c == '+')) {
/* 320 */         modified = true;
/* 321 */         break;
/*     */       }
/*     */     }
/* 324 */     if (!modified) {
/* 325 */       return s;
/*     */     }
/* 327 */     byte[] buf = new byte[size];
/* 328 */     int pos = 0;
/* 329 */     for (int i = 0; i < size; i++) {
/* 330 */       char c = s.charAt(i);
/* 331 */       switch (c) {
/*     */       case '+': 
/* 333 */         buf[(pos++)] = 32;
/* 334 */         break;
/*     */       case '%': 
/* 336 */         if (i == size - 1) {
/* 337 */           throw new IllegalArgumentException("unterminated escape sequence at end of string: " + s);
/*     */         }
/*     */         
/* 340 */         c = s.charAt(++i);
/* 341 */         if (c == '%') {
/* 342 */           buf[(pos++)] = 37;
/*     */         }
/*     */         else {
/* 345 */           if (i == size - 1) {
/* 346 */             throw new IllegalArgumentException("partial escape sequence at end of string: " + s);
/*     */           }
/*     */           
/* 349 */           c = decodeHexNibble(c);
/* 350 */           char c2 = decodeHexNibble(s.charAt(++i));
/* 351 */           if ((c == 65535) || (c2 == 65535)) {
/* 352 */             throw new IllegalArgumentException("invalid escape sequence `%" + s.charAt(i - 1) + s.charAt(i) + "' at index " + (i - 2) + " of: " + s);
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 357 */           c = (char)(c * '\020' + c2);
/*     */         }
/*     */         break;
/* 360 */       default:  buf[(pos++)] = ((byte)c);
/*     */       }
/*     */       
/*     */     }
/* 364 */     return new String(buf, 0, pos, charset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static char decodeHexNibble(char c)
/*     */   {
/* 375 */     if (('0' <= c) && (c <= '9'))
/* 376 */       return (char)(c - '0');
/* 377 */     if (('a' <= c) && (c <= 'f'))
/* 378 */       return (char)(c - 'a' + 10);
/* 379 */     if (('A' <= c) && (c <= 'F')) {
/* 380 */       return (char)(c - 'A' + 10);
/*     */     }
/* 382 */     return 65535;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\QueryStringDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */