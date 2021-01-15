/*     */ package io.netty.handler.codec.compression;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
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
/*     */ final class Bzip2BitWriter
/*     */ {
/*     */   private long bitBuffer;
/*     */   private int bitCount;
/*     */   
/*     */   void writeBits(ByteBuf out, int count, long value)
/*     */   {
/*  42 */     if ((count < 0) || (count > 32)) {
/*  43 */       throw new IllegalArgumentException("count: " + count + " (expected: 0-32)");
/*     */     }
/*  45 */     int bitCount = this.bitCount;
/*  46 */     long bitBuffer = this.bitBuffer | value << 64 - count >>> bitCount;
/*  47 */     bitCount += count;
/*     */     
/*  49 */     if (bitCount >= 32) {
/*  50 */       out.writeInt((int)(bitBuffer >>> 32));
/*  51 */       bitBuffer <<= 32;
/*  52 */       bitCount -= 32;
/*     */     }
/*  54 */     this.bitBuffer = bitBuffer;
/*  55 */     this.bitCount = bitCount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void writeBoolean(ByteBuf out, boolean value)
/*     */   {
/*  63 */     int bitCount = this.bitCount + 1;
/*  64 */     long bitBuffer = this.bitBuffer | (value ? 1L << 64 - bitCount : 0L);
/*     */     
/*  66 */     if (bitCount == 32) {
/*  67 */       out.writeInt((int)(bitBuffer >>> 32));
/*  68 */       bitBuffer = 0L;
/*  69 */       bitCount = 0;
/*     */     }
/*  71 */     this.bitBuffer = bitBuffer;
/*  72 */     this.bitCount = bitCount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void writeUnary(ByteBuf out, int value)
/*     */   {
/*  81 */     if (value < 0) {
/*  82 */       throw new IllegalArgumentException("value: " + value + " (expected 0 or more)");
/*     */     }
/*  84 */     while (value-- > 0) {
/*  85 */       writeBoolean(out, true);
/*     */     }
/*  87 */     writeBoolean(out, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void writeInt(ByteBuf out, int value)
/*     */   {
/*  95 */     writeBits(out, 32, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void flush(ByteBuf out)
/*     */   {
/* 103 */     int bitCount = this.bitCount;
/*     */     
/* 105 */     if (bitCount > 0) {
/* 106 */       long bitBuffer = this.bitBuffer;
/* 107 */       int shiftToRight = 64 - bitCount;
/*     */       
/* 109 */       if (bitCount <= 8) {
/* 110 */         out.writeByte((int)(bitBuffer >>> shiftToRight << 8 - bitCount));
/* 111 */       } else if (bitCount <= 16) {
/* 112 */         out.writeShort((int)(bitBuffer >>> shiftToRight << 16 - bitCount));
/* 113 */       } else if (bitCount <= 24) {
/* 114 */         out.writeMedium((int)(bitBuffer >>> shiftToRight << 24 - bitCount));
/*     */       } else {
/* 116 */         out.writeInt((int)(bitBuffer >>> shiftToRight << 32 - bitCount));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\Bzip2BitWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */