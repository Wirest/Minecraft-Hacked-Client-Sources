/*     */ package io.netty.handler.codec.compression;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.ByteBufInputStream;
/*     */ import io.netty.buffer.ByteBufOutputStream;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.MessageToByteEncoder;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.InputStream;
/*     */ import lzma.sdk.lzma.Encoder;
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
/*     */ public class LzmaFrameEncoder
/*     */   extends MessageToByteEncoder<ByteBuf>
/*     */ {
/*  41 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(LzmaFrameEncoder.class);
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int MEDIUM_DICTIONARY_SIZE = 65536;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int MIN_FAST_BYTES = 5;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int MEDIUM_FAST_BYTES = 32;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int MAX_FAST_BYTES = 273;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int DEFAULT_MATCH_FINDER = 1;
/*     */   
/*     */ 
/*     */   private static final int DEFAULT_LC = 3;
/*     */   
/*     */ 
/*     */   private static final int DEFAULT_LP = 0;
/*     */   
/*     */ 
/*     */   private static final int DEFAULT_PB = 2;
/*     */   
/*     */ 
/*     */   private final Encoder encoder;
/*     */   
/*     */ 
/*     */   private final byte properties;
/*     */   
/*     */ 
/*     */   private final int littleEndianDictionarySize;
/*     */   
/*     */ 
/*     */   private static boolean warningLogged;
/*     */   
/*     */ 
/*     */ 
/*     */   public LzmaFrameEncoder()
/*     */   {
/*  88 */     this(65536);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public LzmaFrameEncoder(int lc, int lp, int pb)
/*     */   {
/*  96 */     this(lc, lp, pb, 65536);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LzmaFrameEncoder(int dictionarySize)
/*     */   {
/* 106 */     this(3, 0, 2, dictionarySize);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public LzmaFrameEncoder(int lc, int lp, int pb, int dictionarySize)
/*     */   {
/* 113 */     this(lc, lp, pb, dictionarySize, false, 32);
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
/*     */   public LzmaFrameEncoder(int lc, int lp, int pb, int dictionarySize, boolean endMarkerMode, int numFastBytes)
/*     */   {
/* 138 */     if ((lc < 0) || (lc > 8)) {
/* 139 */       throw new IllegalArgumentException("lc: " + lc + " (expected: 0-8)");
/*     */     }
/* 141 */     if ((lp < 0) || (lp > 4)) {
/* 142 */       throw new IllegalArgumentException("lp: " + lp + " (expected: 0-4)");
/*     */     }
/* 144 */     if ((pb < 0) || (pb > 4)) {
/* 145 */       throw new IllegalArgumentException("pb: " + pb + " (expected: 0-4)");
/*     */     }
/* 147 */     if ((lc + lp > 4) && 
/* 148 */       (!warningLogged)) {
/* 149 */       logger.warn("The latest versions of LZMA libraries (for example, XZ Utils) has an additional requirement: lc + lp <= 4. Data which don't follow this requirement cannot be decompressed with this libraries.");
/*     */       
/*     */ 
/* 152 */       warningLogged = true;
/*     */     }
/*     */     
/* 155 */     if (dictionarySize < 0) {
/* 156 */       throw new IllegalArgumentException("dictionarySize: " + dictionarySize + " (expected: 0+)");
/*     */     }
/* 158 */     if ((numFastBytes < 5) || (numFastBytes > 273)) {
/* 159 */       throw new IllegalArgumentException(String.format("numFastBytes: %d (expected: %d-%d)", new Object[] { Integer.valueOf(numFastBytes), Integer.valueOf(5), Integer.valueOf(273) }));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 164 */     this.encoder = new Encoder();
/* 165 */     this.encoder.setDictionarySize(dictionarySize);
/* 166 */     this.encoder.setEndMarkerMode(endMarkerMode);
/* 167 */     this.encoder.setMatchFinder(1);
/* 168 */     this.encoder.setNumFastBytes(numFastBytes);
/* 169 */     this.encoder.setLcLpPb(lc, lp, pb);
/*     */     
/* 171 */     this.properties = ((byte)((pb * 5 + lp) * 9 + lc));
/* 172 */     this.littleEndianDictionarySize = Integer.reverseBytes(dictionarySize);
/*     */   }
/*     */   
/*     */   protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception
/*     */   {
/* 177 */     int length = in.readableBytes();
/* 178 */     InputStream bbIn = new ByteBufInputStream(in);
/*     */     
/* 180 */     ByteBufOutputStream bbOut = new ByteBufOutputStream(out);
/* 181 */     bbOut.writeByte(this.properties);
/* 182 */     bbOut.writeInt(this.littleEndianDictionarySize);
/* 183 */     bbOut.writeLong(Long.reverseBytes(length));
/* 184 */     this.encoder.code(bbIn, bbOut, -1L, -1L, null);
/*     */     
/* 186 */     bbIn.close();
/* 187 */     bbOut.close();
/*     */   }
/*     */   
/*     */   protected ByteBuf allocateBuffer(ChannelHandlerContext ctx, ByteBuf in, boolean preferDirect) throws Exception
/*     */   {
/* 192 */     int length = in.readableBytes();
/* 193 */     int maxOutputLength = maxOutputBufferLength(length);
/* 194 */     return ctx.alloc().ioBuffer(maxOutputLength);
/*     */   }
/*     */   
/*     */ 
/*     */   private static int maxOutputBufferLength(int inputLength)
/*     */   {
/*     */     double factor;
/*     */     double factor;
/* 202 */     if (inputLength < 200) {
/* 203 */       factor = 1.5D; } else { double factor;
/* 204 */       if (inputLength < 500) {
/* 205 */         factor = 1.2D; } else { double factor;
/* 206 */         if (inputLength < 1000) {
/* 207 */           factor = 1.1D; } else { double factor;
/* 208 */           if (inputLength < 10000) {
/* 209 */             factor = 1.05D;
/*     */           } else
/* 211 */             factor = 1.02D;
/*     */         } } }
/* 213 */     return 13 + (int)(inputLength * factor);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\LzmaFrameEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */