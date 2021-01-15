/*     */ package io.netty.handler.codec.compression;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ZlibCodecFactory
/*     */ {
/*  27 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ZlibCodecFactory.class);
/*     */   
/*     */ 
/*     */   private static final int DEFAULT_JDK_WINDOW_SIZE = 15;
/*     */   
/*     */ 
/*     */   private static final int DEFAULT_JDK_MEM_LEVEL = 8;
/*     */   
/*     */ 
/*  36 */   private static final boolean noJdkZlibDecoder = SystemPropertyUtil.getBoolean("io.netty.noJdkZlibDecoder", true);
/*  37 */   static { logger.debug("-Dio.netty.noJdkZlibDecoder: {}", Boolean.valueOf(noJdkZlibDecoder));
/*     */     
/*  39 */     noJdkZlibEncoder = SystemPropertyUtil.getBoolean("io.netty.noJdkZlibEncoder", false);
/*  40 */     logger.debug("-Dio.netty.noJdkZlibEncoder: {}", Boolean.valueOf(noJdkZlibEncoder));
/*     */   }
/*     */   
/*     */   private static final boolean noJdkZlibEncoder;
/*  44 */   public static ZlibEncoder newZlibEncoder(int compressionLevel) { if ((PlatformDependent.javaVersion() < 7) || (noJdkZlibEncoder)) {
/*  45 */       return new JZlibEncoder(compressionLevel);
/*     */     }
/*  47 */     return new JdkZlibEncoder(compressionLevel);
/*     */   }
/*     */   
/*     */   public static ZlibEncoder newZlibEncoder(ZlibWrapper wrapper)
/*     */   {
/*  52 */     if ((PlatformDependent.javaVersion() < 7) || (noJdkZlibEncoder)) {
/*  53 */       return new JZlibEncoder(wrapper);
/*     */     }
/*  55 */     return new JdkZlibEncoder(wrapper);
/*     */   }
/*     */   
/*     */   public static ZlibEncoder newZlibEncoder(ZlibWrapper wrapper, int compressionLevel)
/*     */   {
/*  60 */     if ((PlatformDependent.javaVersion() < 7) || (noJdkZlibEncoder)) {
/*  61 */       return new JZlibEncoder(wrapper, compressionLevel);
/*     */     }
/*  63 */     return new JdkZlibEncoder(wrapper, compressionLevel);
/*     */   }
/*     */   
/*     */   public static ZlibEncoder newZlibEncoder(ZlibWrapper wrapper, int compressionLevel, int windowBits, int memLevel)
/*     */   {
/*  68 */     if ((PlatformDependent.javaVersion() < 7) || (noJdkZlibEncoder) || (windowBits != 15) || (memLevel != 8))
/*     */     {
/*  70 */       return new JZlibEncoder(wrapper, compressionLevel, windowBits, memLevel);
/*     */     }
/*  72 */     return new JdkZlibEncoder(wrapper, compressionLevel);
/*     */   }
/*     */   
/*     */   public static ZlibEncoder newZlibEncoder(byte[] dictionary)
/*     */   {
/*  77 */     if ((PlatformDependent.javaVersion() < 7) || (noJdkZlibEncoder)) {
/*  78 */       return new JZlibEncoder(dictionary);
/*     */     }
/*  80 */     return new JdkZlibEncoder(dictionary);
/*     */   }
/*     */   
/*     */   public static ZlibEncoder newZlibEncoder(int compressionLevel, byte[] dictionary)
/*     */   {
/*  85 */     if ((PlatformDependent.javaVersion() < 7) || (noJdkZlibEncoder)) {
/*  86 */       return new JZlibEncoder(compressionLevel, dictionary);
/*     */     }
/*  88 */     return new JdkZlibEncoder(compressionLevel, dictionary);
/*     */   }
/*     */   
/*     */   public static ZlibEncoder newZlibEncoder(int compressionLevel, int windowBits, int memLevel, byte[] dictionary)
/*     */   {
/*  93 */     if ((PlatformDependent.javaVersion() < 7) || (noJdkZlibEncoder) || (windowBits != 15) || (memLevel != 8))
/*     */     {
/*  95 */       return new JZlibEncoder(compressionLevel, windowBits, memLevel, dictionary);
/*     */     }
/*  97 */     return new JdkZlibEncoder(compressionLevel, dictionary);
/*     */   }
/*     */   
/*     */   public static ZlibDecoder newZlibDecoder()
/*     */   {
/* 102 */     if ((PlatformDependent.javaVersion() < 7) || (noJdkZlibDecoder)) {
/* 103 */       return new JZlibDecoder();
/*     */     }
/* 105 */     return new JdkZlibDecoder();
/*     */   }
/*     */   
/*     */   public static ZlibDecoder newZlibDecoder(ZlibWrapper wrapper)
/*     */   {
/* 110 */     if ((PlatformDependent.javaVersion() < 7) || (noJdkZlibDecoder)) {
/* 111 */       return new JZlibDecoder(wrapper);
/*     */     }
/* 113 */     return new JdkZlibDecoder(wrapper);
/*     */   }
/*     */   
/*     */   public static ZlibDecoder newZlibDecoder(byte[] dictionary)
/*     */   {
/* 118 */     if ((PlatformDependent.javaVersion() < 7) || (noJdkZlibDecoder)) {
/* 119 */       return new JZlibDecoder(dictionary);
/*     */     }
/* 121 */     return new JdkZlibDecoder(dictionary);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\ZlibCodecFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */