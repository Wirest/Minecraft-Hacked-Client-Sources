/*     */ package io.netty.handler.codec.base64;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.Unpooled;
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
/*     */ public final class Base64
/*     */ {
/*     */   private static final int MAX_LINE_LENGTH = 76;
/*     */   private static final byte EQUALS_SIGN = 61;
/*     */   private static final byte NEW_LINE = 10;
/*     */   private static final byte WHITE_SPACE_ENC = -5;
/*     */   private static final byte EQUALS_SIGN_ENC = -1;
/*     */   
/*     */   private static byte[] alphabet(Base64Dialect dialect)
/*     */   {
/*  49 */     if (dialect == null) {
/*  50 */       throw new NullPointerException("dialect");
/*     */     }
/*  52 */     return dialect.alphabet;
/*     */   }
/*     */   
/*     */   private static byte[] decodabet(Base64Dialect dialect) {
/*  56 */     if (dialect == null) {
/*  57 */       throw new NullPointerException("dialect");
/*     */     }
/*  59 */     return dialect.decodabet;
/*     */   }
/*     */   
/*     */   private static boolean breakLines(Base64Dialect dialect) {
/*  63 */     if (dialect == null) {
/*  64 */       throw new NullPointerException("dialect");
/*     */     }
/*  66 */     return dialect.breakLinesByDefault;
/*     */   }
/*     */   
/*     */   public static ByteBuf encode(ByteBuf src) {
/*  70 */     return encode(src, Base64Dialect.STANDARD);
/*     */   }
/*     */   
/*     */   public static ByteBuf encode(ByteBuf src, Base64Dialect dialect) {
/*  74 */     return encode(src, breakLines(dialect), dialect);
/*     */   }
/*     */   
/*     */   public static ByteBuf encode(ByteBuf src, boolean breakLines) {
/*  78 */     return encode(src, breakLines, Base64Dialect.STANDARD);
/*     */   }
/*     */   
/*     */   public static ByteBuf encode(ByteBuf src, boolean breakLines, Base64Dialect dialect)
/*     */   {
/*  83 */     if (src == null) {
/*  84 */       throw new NullPointerException("src");
/*     */     }
/*     */     
/*  87 */     ByteBuf dest = encode(src, src.readerIndex(), src.readableBytes(), breakLines, dialect);
/*  88 */     src.readerIndex(src.writerIndex());
/*  89 */     return dest;
/*     */   }
/*     */   
/*     */   public static ByteBuf encode(ByteBuf src, int off, int len) {
/*  93 */     return encode(src, off, len, Base64Dialect.STANDARD);
/*     */   }
/*     */   
/*     */   public static ByteBuf encode(ByteBuf src, int off, int len, Base64Dialect dialect) {
/*  97 */     return encode(src, off, len, breakLines(dialect), dialect);
/*     */   }
/*     */   
/*     */   public static ByteBuf encode(ByteBuf src, int off, int len, boolean breakLines)
/*     */   {
/* 102 */     return encode(src, off, len, breakLines, Base64Dialect.STANDARD);
/*     */   }
/*     */   
/*     */ 
/*     */   public static ByteBuf encode(ByteBuf src, int off, int len, boolean breakLines, Base64Dialect dialect)
/*     */   {
/* 108 */     if (src == null) {
/* 109 */       throw new NullPointerException("src");
/*     */     }
/* 111 */     if (dialect == null) {
/* 112 */       throw new NullPointerException("dialect");
/*     */     }
/*     */     
/* 115 */     int len43 = len * 4 / 3;
/* 116 */     ByteBuf dest = Unpooled.buffer(len43 + (len % 3 > 0 ? 4 : 0) + (breakLines ? len43 / 76 : 0)).order(src.order());
/*     */     
/*     */ 
/*     */ 
/* 120 */     int d = 0;
/* 121 */     int e = 0;
/* 122 */     int len2 = len - 2;
/* 123 */     int lineLength = 0;
/* 124 */     for (; d < len2; e += 4) {
/* 125 */       encode3to4(src, d + off, 3, dest, e, dialect);
/*     */       
/* 127 */       lineLength += 4;
/* 128 */       if ((breakLines) && (lineLength == 76)) {
/* 129 */         dest.setByte(e + 4, 10);
/* 130 */         e++;
/* 131 */         lineLength = 0;
/*     */       }
/* 124 */       d += 3;
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
/* 135 */     if (d < len) {
/* 136 */       encode3to4(src, d + off, len - d, dest, e, dialect);
/* 137 */       e += 4;
/*     */     }
/*     */     
/* 140 */     return dest.slice(0, e);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static void encode3to4(ByteBuf src, int srcOffset, int numSigBytes, ByteBuf dest, int destOffset, Base64Dialect dialect)
/*     */   {
/* 147 */     byte[] ALPHABET = alphabet(dialect);
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
/* 160 */     int inBuff = (numSigBytes > 0 ? src.getByte(srcOffset) << 24 >>> 8 : 0) | (numSigBytes > 1 ? src.getByte(srcOffset + 1) << 24 >>> 16 : 0) | (numSigBytes > 2 ? src.getByte(srcOffset + 2) << 24 >>> 24 : 0);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 165 */     switch (numSigBytes) {
/*     */     case 3: 
/* 167 */       dest.setByte(destOffset, ALPHABET[(inBuff >>> 18)]);
/* 168 */       dest.setByte(destOffset + 1, ALPHABET[(inBuff >>> 12 & 0x3F)]);
/* 169 */       dest.setByte(destOffset + 2, ALPHABET[(inBuff >>> 6 & 0x3F)]);
/* 170 */       dest.setByte(destOffset + 3, ALPHABET[(inBuff & 0x3F)]);
/* 171 */       break;
/*     */     case 2: 
/* 173 */       dest.setByte(destOffset, ALPHABET[(inBuff >>> 18)]);
/* 174 */       dest.setByte(destOffset + 1, ALPHABET[(inBuff >>> 12 & 0x3F)]);
/* 175 */       dest.setByte(destOffset + 2, ALPHABET[(inBuff >>> 6 & 0x3F)]);
/* 176 */       dest.setByte(destOffset + 3, 61);
/* 177 */       break;
/*     */     case 1: 
/* 179 */       dest.setByte(destOffset, ALPHABET[(inBuff >>> 18)]);
/* 180 */       dest.setByte(destOffset + 1, ALPHABET[(inBuff >>> 12 & 0x3F)]);
/* 181 */       dest.setByte(destOffset + 2, 61);
/* 182 */       dest.setByte(destOffset + 3, 61);
/*     */     }
/*     */   }
/*     */   
/*     */   public static ByteBuf decode(ByteBuf src)
/*     */   {
/* 188 */     return decode(src, Base64Dialect.STANDARD);
/*     */   }
/*     */   
/*     */   public static ByteBuf decode(ByteBuf src, Base64Dialect dialect)
/*     */   {
/* 193 */     if (src == null) {
/* 194 */       throw new NullPointerException("src");
/*     */     }
/*     */     
/* 197 */     ByteBuf dest = decode(src, src.readerIndex(), src.readableBytes(), dialect);
/* 198 */     src.readerIndex(src.writerIndex());
/* 199 */     return dest;
/*     */   }
/*     */   
/*     */   public static ByteBuf decode(ByteBuf src, int off, int len)
/*     */   {
/* 204 */     return decode(src, off, len, Base64Dialect.STANDARD);
/*     */   }
/*     */   
/*     */ 
/*     */   public static ByteBuf decode(ByteBuf src, int off, int len, Base64Dialect dialect)
/*     */   {
/* 210 */     if (src == null) {
/* 211 */       throw new NullPointerException("src");
/*     */     }
/* 213 */     if (dialect == null) {
/* 214 */       throw new NullPointerException("dialect");
/*     */     }
/*     */     
/* 217 */     byte[] DECODABET = decodabet(dialect);
/*     */     
/* 219 */     int len34 = len * 3 / 4;
/* 220 */     ByteBuf dest = src.alloc().buffer(len34).order(src.order());
/* 221 */     int outBuffPosn = 0;
/*     */     
/* 223 */     byte[] b4 = new byte[4];
/* 224 */     int b4Posn = 0;
/*     */     
/*     */ 
/*     */ 
/* 228 */     for (int i = off; i < off + len; i++) {
/* 229 */       byte sbiCrop = (byte)(src.getByte(i) & 0x7F);
/* 230 */       byte sbiDecode = DECODABET[sbiCrop];
/*     */       
/* 232 */       if (sbiDecode >= -5) {
/* 233 */         if (sbiDecode >= -1) {
/* 234 */           b4[(b4Posn++)] = sbiCrop;
/* 235 */           if (b4Posn > 3) {
/* 236 */             outBuffPosn += decode4to3(b4, 0, dest, outBuffPosn, dialect);
/*     */             
/* 238 */             b4Posn = 0;
/*     */             
/*     */ 
/* 241 */             if (sbiCrop == 61) {
/*     */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       } else {
/* 247 */         throw new IllegalArgumentException("bad Base64 input character at " + i + ": " + src.getUnsignedByte(i) + " (decimal)");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 253 */     return dest.slice(0, outBuffPosn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static int decode4to3(byte[] src, int srcOffset, ByteBuf dest, int destOffset, Base64Dialect dialect)
/*     */   {
/* 260 */     byte[] DECODABET = decodabet(dialect);
/*     */     
/* 262 */     if (src[(srcOffset + 2)] == 61)
/*     */     {
/* 264 */       int outBuff = (DECODABET[src[srcOffset]] & 0xFF) << 18 | (DECODABET[src[(srcOffset + 1)]] & 0xFF) << 12;
/*     */       
/*     */ 
/*     */ 
/* 268 */       dest.setByte(destOffset, (byte)(outBuff >>> 16));
/* 269 */       return 1; }
/* 270 */     if (src[(srcOffset + 3)] == 61)
/*     */     {
/* 272 */       int outBuff = (DECODABET[src[srcOffset]] & 0xFF) << 18 | (DECODABET[src[(srcOffset + 1)]] & 0xFF) << 12 | (DECODABET[src[(srcOffset + 2)]] & 0xFF) << 6;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 277 */       dest.setByte(destOffset, (byte)(outBuff >>> 16));
/* 278 */       dest.setByte(destOffset + 1, (byte)(outBuff >>> 8));
/* 279 */       return 2;
/*     */     }
/*     */     int outBuff;
/*     */     try
/*     */     {
/* 284 */       outBuff = (DECODABET[src[srcOffset]] & 0xFF) << 18 | (DECODABET[src[(srcOffset + 1)]] & 0xFF) << 12 | (DECODABET[src[(srcOffset + 2)]] & 0xFF) << 6 | DECODABET[src[(srcOffset + 3)]] & 0xFF;
/*     */ 
/*     */     }
/*     */     catch (IndexOutOfBoundsException ignored)
/*     */     {
/*     */ 
/* 290 */       throw new IllegalArgumentException("not encoded in Base64");
/*     */     }
/*     */     
/* 293 */     dest.setByte(destOffset, (byte)(outBuff >> 16));
/* 294 */     dest.setByte(destOffset + 1, (byte)(outBuff >> 8));
/* 295 */     dest.setByte(destOffset + 2, (byte)outBuff);
/* 296 */     return 3;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\base64\Base64.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */