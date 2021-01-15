/*     */ package io.netty.util;
/*     */ 
/*     */ import io.netty.util.internal.InternalThreadLocalMap;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CodingErrorAction;
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
/*     */ public final class CharsetUtil
/*     */ {
/*  36 */   public static final Charset UTF_16 = Charset.forName("UTF-16");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  41 */   public static final Charset UTF_16BE = Charset.forName("UTF-16BE");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  46 */   public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  51 */   public static final Charset UTF_8 = Charset.forName("UTF-8");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  56 */   public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  62 */   public static final Charset US_ASCII = Charset.forName("US-ASCII");
/*     */   
/*  64 */   private static final Charset[] CHARSETS = { UTF_16, UTF_16BE, UTF_16LE, UTF_8, ISO_8859_1, US_ASCII };
/*     */   
/*     */   public static Charset[] values() {
/*  67 */     return CHARSETS;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static CharsetEncoder getEncoder(Charset charset)
/*     */   {
/*  74 */     if (charset == null) {
/*  75 */       throw new NullPointerException("charset");
/*     */     }
/*     */     
/*  78 */     Map<Charset, CharsetEncoder> map = InternalThreadLocalMap.get().charsetEncoderCache();
/*  79 */     CharsetEncoder e = (CharsetEncoder)map.get(charset);
/*  80 */     if (e != null) {
/*  81 */       e.reset();
/*  82 */       e.onMalformedInput(CodingErrorAction.REPLACE);
/*  83 */       e.onUnmappableCharacter(CodingErrorAction.REPLACE);
/*  84 */       return e;
/*     */     }
/*     */     
/*  87 */     e = charset.newEncoder();
/*  88 */     e.onMalformedInput(CodingErrorAction.REPLACE);
/*  89 */     e.onUnmappableCharacter(CodingErrorAction.REPLACE);
/*  90 */     map.put(charset, e);
/*  91 */     return e;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static CharsetDecoder getDecoder(Charset charset)
/*     */   {
/*  99 */     if (charset == null) {
/* 100 */       throw new NullPointerException("charset");
/*     */     }
/*     */     
/* 103 */     Map<Charset, CharsetDecoder> map = InternalThreadLocalMap.get().charsetDecoderCache();
/* 104 */     CharsetDecoder d = (CharsetDecoder)map.get(charset);
/* 105 */     if (d != null) {
/* 106 */       d.reset();
/* 107 */       d.onMalformedInput(CodingErrorAction.REPLACE);
/* 108 */       d.onUnmappableCharacter(CodingErrorAction.REPLACE);
/* 109 */       return d;
/*     */     }
/*     */     
/* 112 */     d = charset.newDecoder();
/* 113 */     d.onMalformedInput(CodingErrorAction.REPLACE);
/* 114 */     d.onUnmappableCharacter(CodingErrorAction.REPLACE);
/* 115 */     map.put(charset, d);
/* 116 */     return d;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\CharsetUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */