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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Bzip2BitReader
/*     */ {
/*     */   private static final int MAX_COUNT_OF_READABLE_BYTES = 268435455;
/*     */   private ByteBuf in;
/*     */   private long bitBuffer;
/*     */   private int bitCount;
/*     */   
/*     */   void setByteBuf(ByteBuf in)
/*     */   {
/*  50 */     this.in = in;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int readBits(int count)
/*     */   {
/*  59 */     if ((count < 0) || (count > 32)) {
/*  60 */       throw new IllegalArgumentException("count: " + count + " (expected: 0-32 )");
/*     */     }
/*  62 */     int bitCount = this.bitCount;
/*  63 */     long bitBuffer = this.bitBuffer;
/*     */     
/*  65 */     if (bitCount < count) {
/*     */       long readData;
/*     */       int offset;
/*  68 */       switch (this.in.readableBytes()) {
/*     */       case 1: 
/*  70 */         readData = this.in.readUnsignedByte();
/*  71 */         offset = 8;
/*  72 */         break;
/*     */       
/*     */       case 2: 
/*  75 */         readData = this.in.readUnsignedShort();
/*  76 */         offset = 16;
/*  77 */         break;
/*     */       
/*     */       case 3: 
/*  80 */         readData = this.in.readUnsignedMedium();
/*  81 */         offset = 24;
/*  82 */         break;
/*     */       
/*     */       default: 
/*  85 */         readData = this.in.readUnsignedInt();
/*  86 */         offset = 32;
/*     */       }
/*     */       
/*     */       
/*     */ 
/*  91 */       bitBuffer = bitBuffer << offset | readData;
/*  92 */       bitCount += offset;
/*  93 */       this.bitBuffer = bitBuffer;
/*     */     }
/*     */     
/*  96 */     this.bitCount = (bitCount -= count);
/*  97 */     return (int)(bitBuffer >>> bitCount & (count != 32 ? (1 << count) - 1 : 4294967295L));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean readBoolean()
/*     */   {
/* 105 */     return readBits(1) != 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   int readInt()
/*     */   {
/* 113 */     return readBits(32);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void refill()
/*     */   {
/* 120 */     int readData = this.in.readUnsignedByte();
/* 121 */     this.bitBuffer = (this.bitBuffer << 8 | readData);
/* 122 */     this.bitCount += 8;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean isReadable()
/*     */   {
/* 130 */     return (this.bitCount > 0) || (this.in.isReadable());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean hasReadableBits(int count)
/*     */   {
/* 139 */     if (count < 0) {
/* 140 */       throw new IllegalArgumentException("count: " + count + " (expected value greater than 0)");
/*     */     }
/* 142 */     return (this.bitCount >= count) || ((this.in.readableBytes() << 3 & 0x7FFFFFFF) >= count - this.bitCount);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean hasReadableBytes(int count)
/*     */   {
/* 151 */     if ((count < 0) || (count > 268435455)) {
/* 152 */       throw new IllegalArgumentException("count: " + count + " (expected: 0-" + 268435455 + ')');
/*     */     }
/*     */     
/* 155 */     return hasReadableBits(count << 3);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\Bzip2BitReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */