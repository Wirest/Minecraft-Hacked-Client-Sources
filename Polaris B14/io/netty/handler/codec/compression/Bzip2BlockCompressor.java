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
/*     */ final class Bzip2BlockCompressor
/*     */ {
/*     */   private final Bzip2BitWriter writer;
/*  43 */   private final Crc32 crc = new Crc32();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final byte[] block;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int blockLength;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int blockLengthLimit;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  64 */   private final boolean[] blockValuesPresent = new boolean['Ā'];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int[] bwtBlock;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  74 */   private int rleCurrentValue = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int rleLength;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Bzip2BlockCompressor(Bzip2BitWriter writer, int blockSize)
/*     */   {
/*  87 */     this.writer = writer;
/*     */     
/*     */ 
/*  90 */     this.block = new byte[blockSize + 1];
/*  91 */     this.bwtBlock = new int[blockSize + 1];
/*  92 */     this.blockLengthLimit = (blockSize - 6);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void writeSymbolMap(ByteBuf out)
/*     */   {
/*  99 */     Bzip2BitWriter writer = this.writer;
/*     */     
/* 101 */     boolean[] blockValuesPresent = this.blockValuesPresent;
/* 102 */     boolean[] condensedInUse = new boolean[16];
/*     */     
/* 104 */     for (int i = 0; i < condensedInUse.length; i++) {
/* 105 */       int j = 0; for (int k = i << 4; j < 16; k++) {
/* 106 */         if (blockValuesPresent[k] != 0) {
/* 107 */           condensedInUse[i] = true;
/*     */         }
/* 105 */         j++;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 112 */     for (int i = 0; i < condensedInUse.length; i++) {
/* 113 */       writer.writeBoolean(out, condensedInUse[i]);
/*     */     }
/*     */     
/* 116 */     for (int i = 0; i < condensedInUse.length; i++) {
/* 117 */       if (condensedInUse[i] != 0) {
/* 118 */         int j = 0; for (int k = i << 4; j < 16; k++) {
/* 119 */           writer.writeBoolean(out, blockValuesPresent[k]);j++;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void writeRun(int value, int runLength)
/*     */   {
/* 131 */     int blockLength = this.blockLength;
/* 132 */     byte[] block = this.block;
/*     */     
/* 134 */     this.blockValuesPresent[value] = true;
/* 135 */     this.crc.updateCRC(value, runLength);
/*     */     
/* 137 */     byte byteValue = (byte)value;
/* 138 */     switch (runLength) {
/*     */     case 1: 
/* 140 */       block[blockLength] = byteValue;
/* 141 */       this.blockLength = (blockLength + 1);
/* 142 */       break;
/*     */     case 2: 
/* 144 */       block[blockLength] = byteValue;
/* 145 */       block[(blockLength + 1)] = byteValue;
/* 146 */       this.blockLength = (blockLength + 2);
/* 147 */       break;
/*     */     case 3: 
/* 149 */       block[blockLength] = byteValue;
/* 150 */       block[(blockLength + 1)] = byteValue;
/* 151 */       block[(blockLength + 2)] = byteValue;
/* 152 */       this.blockLength = (blockLength + 3);
/* 153 */       break;
/*     */     default: 
/* 155 */       runLength -= 4;
/* 156 */       this.blockValuesPresent[runLength] = true;
/* 157 */       block[blockLength] = byteValue;
/* 158 */       block[(blockLength + 1)] = byteValue;
/* 159 */       block[(blockLength + 2)] = byteValue;
/* 160 */       block[(blockLength + 3)] = byteValue;
/* 161 */       block[(blockLength + 4)] = ((byte)runLength);
/* 162 */       this.blockLength = (blockLength + 5);
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean write(int value)
/*     */   {
/* 173 */     if (this.blockLength > this.blockLengthLimit) {
/* 174 */       return false;
/*     */     }
/* 176 */     int rleCurrentValue = this.rleCurrentValue;
/* 177 */     int rleLength = this.rleLength;
/*     */     
/* 179 */     if (rleLength == 0) {
/* 180 */       this.rleCurrentValue = value;
/* 181 */       this.rleLength = 1;
/* 182 */     } else if (rleCurrentValue != value)
/*     */     {
/* 184 */       writeRun(rleCurrentValue & 0xFF, rleLength);
/* 185 */       this.rleCurrentValue = value;
/* 186 */       this.rleLength = 1;
/*     */     }
/* 188 */     else if (rleLength == 254) {
/* 189 */       writeRun(rleCurrentValue & 0xFF, 255);
/* 190 */       this.rleLength = 0;
/*     */     } else {
/* 192 */       this.rleLength = (rleLength + 1);
/*     */     }
/*     */     
/* 195 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int write(byte[] data, int offset, int length)
/*     */   {
/* 207 */     int written = 0;
/*     */     
/* 209 */     while ((length-- > 0) && 
/* 210 */       (write(data[(offset++)])))
/*     */     {
/*     */ 
/* 213 */       written++;
/*     */     }
/* 215 */     return written;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void close(ByteBuf out)
/*     */   {
/* 223 */     if (this.rleLength > 0) {
/* 224 */       writeRun(this.rleCurrentValue & 0xFF, this.rleLength);
/*     */     }
/*     */     
/*     */ 
/* 228 */     this.block[this.blockLength] = this.block[0];
/*     */     
/*     */ 
/* 231 */     Bzip2DivSufSort divSufSort = new Bzip2DivSufSort(this.block, this.bwtBlock, this.blockLength);
/* 232 */     int bwtStartPointer = divSufSort.bwt();
/*     */     
/* 234 */     Bzip2BitWriter writer = this.writer;
/*     */     
/*     */ 
/* 237 */     writer.writeBits(out, 24, 3227993L);
/* 238 */     writer.writeBits(out, 24, 2511705L);
/* 239 */     writer.writeInt(out, this.crc.getCRC());
/* 240 */     writer.writeBoolean(out, false);
/* 241 */     writer.writeBits(out, 24, bwtStartPointer);
/*     */     
/*     */ 
/* 244 */     writeSymbolMap(out);
/*     */     
/*     */ 
/* 247 */     Bzip2MTFAndRLE2StageEncoder mtfEncoder = new Bzip2MTFAndRLE2StageEncoder(this.bwtBlock, this.blockLength, this.blockValuesPresent);
/*     */     
/* 249 */     mtfEncoder.encode();
/*     */     
/*     */ 
/* 252 */     Bzip2HuffmanStageEncoder huffmanEncoder = new Bzip2HuffmanStageEncoder(writer, mtfEncoder.mtfBlock(), mtfEncoder.mtfLength(), mtfEncoder.mtfAlphabetSize(), mtfEncoder.mtfSymbolFrequencies());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 257 */     huffmanEncoder.encode(out);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   int availableSize()
/*     */   {
/* 265 */     if (this.blockLength == 0) {
/* 266 */       return this.blockLengthLimit + 2;
/*     */     }
/* 268 */     return this.blockLengthLimit - this.blockLength + 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean isFull()
/*     */   {
/* 276 */     return this.blockLength > this.blockLengthLimit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean isEmpty()
/*     */   {
/* 284 */     return (this.blockLength == 0) && (this.rleLength == 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   int crc()
/*     */   {
/* 292 */     return this.crc.getCRC();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\Bzip2BlockCompressor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */