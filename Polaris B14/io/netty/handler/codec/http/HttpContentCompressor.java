/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.embedded.EmbeddedChannel;
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import io.netty.handler.codec.compression.ZlibCodecFactory;
/*     */ import io.netty.handler.codec.compression.ZlibWrapper;
/*     */ import io.netty.util.internal.StringUtil;
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
/*     */ public class HttpContentCompressor
/*     */   extends HttpContentEncoder
/*     */ {
/*     */   private final int compressionLevel;
/*     */   private final int windowBits;
/*     */   private final int memLevel;
/*     */   
/*     */   public HttpContentCompressor()
/*     */   {
/*  41 */     this(6);
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
/*     */   public HttpContentCompressor(int compressionLevel)
/*     */   {
/*  54 */     this(compressionLevel, 15, 8);
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
/*     */   public HttpContentCompressor(int compressionLevel, int windowBits, int memLevel)
/*     */   {
/*  77 */     if ((compressionLevel < 0) || (compressionLevel > 9)) {
/*  78 */       throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
/*     */     }
/*     */     
/*     */ 
/*  82 */     if ((windowBits < 9) || (windowBits > 15)) {
/*  83 */       throw new IllegalArgumentException("windowBits: " + windowBits + " (expected: 9-15)");
/*     */     }
/*     */     
/*  86 */     if ((memLevel < 1) || (memLevel > 9)) {
/*  87 */       throw new IllegalArgumentException("memLevel: " + memLevel + " (expected: 1-9)");
/*     */     }
/*     */     
/*  90 */     this.compressionLevel = compressionLevel;
/*  91 */     this.windowBits = windowBits;
/*  92 */     this.memLevel = memLevel;
/*     */   }
/*     */   
/*     */   protected HttpContentEncoder.Result beginEncode(HttpResponse headers, CharSequence acceptEncoding) throws Exception
/*     */   {
/*  97 */     CharSequence contentEncoding = (CharSequence)headers.headers().get(HttpHeaderNames.CONTENT_ENCODING);
/*  98 */     if ((contentEncoding != null) && (!HttpHeaderValues.IDENTITY.equalsIgnoreCase(contentEncoding)))
/*     */     {
/* 100 */       return null;
/*     */     }
/*     */     
/* 103 */     ZlibWrapper wrapper = determineWrapper(acceptEncoding);
/* 104 */     if (wrapper == null) {
/* 105 */       return null;
/*     */     }
/*     */     
/*     */     String targetContentEncoding;
/* 109 */     switch (wrapper) {
/*     */     case GZIP: 
/* 111 */       targetContentEncoding = "gzip";
/* 112 */       break;
/*     */     case ZLIB: 
/* 114 */       targetContentEncoding = "deflate";
/* 115 */       break;
/*     */     default: 
/* 117 */       throw new Error();
/*     */     }
/*     */     
/* 120 */     return new HttpContentEncoder.Result(targetContentEncoding, new EmbeddedChannel(new ChannelHandler[] { ZlibCodecFactory.newZlibEncoder(wrapper, this.compressionLevel, this.windowBits, this.memLevel) }));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ZlibWrapper determineWrapper(CharSequence acceptEncoding)
/*     */   {
/* 128 */     float starQ = -1.0F;
/* 129 */     float gzipQ = -1.0F;
/* 130 */     float deflateQ = -1.0F;
/* 131 */     for (String encoding : StringUtil.split(acceptEncoding.toString(), ',')) {
/* 132 */       float q = 1.0F;
/* 133 */       int equalsPos = encoding.indexOf('=');
/* 134 */       if (equalsPos != -1) {
/*     */         try {
/* 136 */           q = Float.valueOf(encoding.substring(equalsPos + 1)).floatValue();
/*     */         }
/*     */         catch (NumberFormatException e) {
/* 139 */           q = 0.0F;
/*     */         }
/*     */       }
/* 142 */       if (encoding.contains("*")) {
/* 143 */         starQ = q;
/* 144 */       } else if ((encoding.contains("gzip")) && (q > gzipQ)) {
/* 145 */         gzipQ = q;
/* 146 */       } else if ((encoding.contains("deflate")) && (q > deflateQ)) {
/* 147 */         deflateQ = q;
/*     */       }
/*     */     }
/* 150 */     if ((gzipQ > 0.0F) || (deflateQ > 0.0F)) {
/* 151 */       if (gzipQ >= deflateQ) {
/* 152 */         return ZlibWrapper.GZIP;
/*     */       }
/* 154 */       return ZlibWrapper.ZLIB;
/*     */     }
/*     */     
/* 157 */     if (starQ > 0.0F) {
/* 158 */       if (gzipQ == -1.0F) {
/* 159 */         return ZlibWrapper.GZIP;
/*     */       }
/* 161 */       if (deflateQ == -1.0F) {
/* 162 */         return ZlibWrapper.ZLIB;
/*     */       }
/*     */     }
/* 165 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpContentCompressor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */