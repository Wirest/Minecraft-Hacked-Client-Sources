/*     */ package io.netty.handler.codec.rtsp;
/*     */ 
/*     */ import io.netty.handler.codec.http.HttpMethod;
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
/*     */ public final class RtspMethods
/*     */ {
/*  35 */   public static final HttpMethod OPTIONS = HttpMethod.OPTIONS;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  41 */   public static final HttpMethod DESCRIBE = new HttpMethod("DESCRIBE");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  48 */   public static final HttpMethod ANNOUNCE = new HttpMethod("ANNOUNCE");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  54 */   public static final HttpMethod SETUP = new HttpMethod("SETUP");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  60 */   public static final HttpMethod PLAY = new HttpMethod("PLAY");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  66 */   public static final HttpMethod PAUSE = new HttpMethod("PAUSE");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  72 */   public static final HttpMethod TEARDOWN = new HttpMethod("TEARDOWN");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  78 */   public static final HttpMethod GET_PARAMETER = new HttpMethod("GET_PARAMETER");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  84 */   public static final HttpMethod SET_PARAMETER = new HttpMethod("SET_PARAMETER");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  90 */   public static final HttpMethod REDIRECT = new HttpMethod("REDIRECT");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  96 */   public static final HttpMethod RECORD = new HttpMethod("RECORD");
/*     */   
/*  98 */   private static final Map<String, HttpMethod> methodMap = new HashMap();
/*     */   
/*     */   static {
/* 101 */     methodMap.put(DESCRIBE.toString(), DESCRIBE);
/* 102 */     methodMap.put(ANNOUNCE.toString(), ANNOUNCE);
/* 103 */     methodMap.put(GET_PARAMETER.toString(), GET_PARAMETER);
/* 104 */     methodMap.put(OPTIONS.toString(), OPTIONS);
/* 105 */     methodMap.put(PAUSE.toString(), PAUSE);
/* 106 */     methodMap.put(PLAY.toString(), PLAY);
/* 107 */     methodMap.put(RECORD.toString(), RECORD);
/* 108 */     methodMap.put(REDIRECT.toString(), REDIRECT);
/* 109 */     methodMap.put(SETUP.toString(), SETUP);
/* 110 */     methodMap.put(SET_PARAMETER.toString(), SET_PARAMETER);
/* 111 */     methodMap.put(TEARDOWN.toString(), TEARDOWN);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HttpMethod valueOf(String name)
/*     */   {
/* 120 */     if (name == null) {
/* 121 */       throw new NullPointerException("name");
/*     */     }
/*     */     
/* 124 */     name = name.trim().toUpperCase();
/* 125 */     if (name.isEmpty()) {
/* 126 */       throw new IllegalArgumentException("empty name");
/*     */     }
/*     */     
/* 129 */     HttpMethod result = (HttpMethod)methodMap.get(name);
/* 130 */     if (result != null) {
/* 131 */       return result;
/*     */     }
/* 133 */     return new HttpMethod(name);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\rtsp\RtspMethods.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */