/*     */ package io.netty.handler.codec.compression;
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
/*     */ final class Bzip2BlockDecompressor
/*     */ {
/*     */   private final Bzip2BitReader reader;
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
/*  42 */   private final Crc32 crc = new Crc32();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int blockCRC;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final boolean blockRandomised;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int huffmanEndOfBlockSymbol;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   int huffmanInUse16;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  70 */   final byte[] huffmanSymbolMap = new byte['Ā'];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  77 */   private final int[] bwtByteCounts = new int['Ā'];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final byte[] bwtBlock;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int bwtStartPointer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int[] bwtMergedPointers;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int bwtCurrentMergedPointer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int bwtBlockLength;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int bwtBytesDecoded;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 122 */   private int rleLastDecodedByte = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int rleAccumulator;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int rleRepeat;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int randomIndex;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 143 */   private int randomCount = Bzip2Rand.rNums(0) - 1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 148 */   private final Bzip2MoveToFrontTable symbolMTF = new Bzip2MoveToFrontTable();
/*     */   
/*     */   private int repeatCount;
/*     */   
/* 152 */   private int repeatIncrement = 1;
/*     */   
/*     */   private int mtfValue;
/*     */   
/*     */   Bzip2BlockDecompressor(int blockSize, int blockCRC, boolean blockRandomised, int bwtStartPointer, Bzip2BitReader reader)
/*     */   {
/* 158 */     this.bwtBlock = new byte[blockSize];
/*     */     
/* 160 */     this.blockCRC = blockCRC;
/* 161 */     this.blockRandomised = blockRandomised;
/* 162 */     this.bwtStartPointer = bwtStartPointer;
/*     */     
/* 164 */     this.reader = reader;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean decodeHuffmanData(Bzip2HuffmanStageDecoder huffmanDecoder)
/*     */   {
/* 172 */     Bzip2BitReader reader = this.reader;
/* 173 */     byte[] bwtBlock = this.bwtBlock;
/* 174 */     byte[] huffmanSymbolMap = this.huffmanSymbolMap;
/* 175 */     int streamBlockSize = this.bwtBlock.length;
/* 176 */     int huffmanEndOfBlockSymbol = this.huffmanEndOfBlockSymbol;
/* 177 */     int[] bwtByteCounts = this.bwtByteCounts;
/* 178 */     Bzip2MoveToFrontTable symbolMTF = this.symbolMTF;
/*     */     
/* 180 */     int bwtBlockLength = this.bwtBlockLength;
/* 181 */     int repeatCount = this.repeatCount;
/* 182 */     int repeatIncrement = this.repeatIncrement;
/* 183 */     int mtfValue = this.mtfValue;
/*     */     for (;;)
/*     */     {
/* 186 */       if (!reader.hasReadableBits(23)) {
/* 187 */         this.bwtBlockLength = bwtBlockLength;
/* 188 */         this.repeatCount = repeatCount;
/* 189 */         this.repeatIncrement = repeatIncrement;
/* 190 */         this.mtfValue = mtfValue;
/* 191 */         return false;
/*     */       }
/* 193 */       int nextSymbol = huffmanDecoder.nextSymbol();
/*     */       
/* 195 */       if (nextSymbol == 0) {
/* 196 */         repeatCount += repeatIncrement;
/* 197 */         repeatIncrement <<= 1;
/* 198 */       } else if (nextSymbol == 1) {
/* 199 */         repeatCount += (repeatIncrement << 1);
/* 200 */         repeatIncrement <<= 1;
/*     */       } else {
/* 202 */         if (repeatCount > 0) {
/* 203 */           if (bwtBlockLength + repeatCount > streamBlockSize) {
/* 204 */             throw new DecompressionException("block exceeds declared block size");
/*     */           }
/* 206 */           byte nextByte = huffmanSymbolMap[mtfValue];
/* 207 */           bwtByteCounts[(nextByte & 0xFF)] += repeatCount;
/* 208 */           for (;;) { repeatCount--; if (repeatCount < 0) break;
/* 209 */             bwtBlock[(bwtBlockLength++)] = nextByte;
/*     */           }
/*     */           
/* 212 */           repeatCount = 0;
/* 213 */           repeatIncrement = 1;
/*     */         }
/*     */         
/* 216 */         if (nextSymbol == huffmanEndOfBlockSymbol) {
/*     */           break;
/*     */         }
/*     */         
/* 220 */         if (bwtBlockLength >= streamBlockSize) {
/* 221 */           throw new DecompressionException("block exceeds declared block size");
/*     */         }
/*     */         
/* 224 */         mtfValue = symbolMTF.indexToFront(nextSymbol - 1) & 0xFF;
/*     */         
/* 226 */         byte nextByte = huffmanSymbolMap[mtfValue];
/* 227 */         bwtByteCounts[(nextByte & 0xFF)] += 1;
/* 228 */         bwtBlock[(bwtBlockLength++)] = nextByte;
/*     */       }
/*     */     }
/* 231 */     this.bwtBlockLength = bwtBlockLength;
/* 232 */     initialiseInverseBWT();
/* 233 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void initialiseInverseBWT()
/*     */   {
/* 240 */     int bwtStartPointer = this.bwtStartPointer;
/* 241 */     byte[] bwtBlock = this.bwtBlock;
/* 242 */     int[] bwtMergedPointers = new int[this.bwtBlockLength];
/* 243 */     int[] characterBase = new int['Ā'];
/*     */     
/* 245 */     if ((bwtStartPointer < 0) || (bwtStartPointer >= this.bwtBlockLength)) {
/* 246 */       throw new DecompressionException("start pointer invalid");
/*     */     }
/*     */     
/*     */ 
/* 250 */     System.arraycopy(this.bwtByteCounts, 0, characterBase, 1, 255);
/* 251 */     for (int i = 2; i <= 255; i++) {
/* 252 */       characterBase[i] += characterBase[(i - 1)];
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 259 */     for (int i = 0; i < this.bwtBlockLength; i++) {
/* 260 */       int value = bwtBlock[i] & 0xFF; int 
/* 261 */         tmp119_117 = value; int[] tmp119_115 = characterBase; int tmp121_120 = tmp119_115[tmp119_117];tmp119_115[tmp119_117] = (tmp121_120 + 1);bwtMergedPointers[tmp121_120] = ((i << 8) + value);
/*     */     }
/*     */     
/* 264 */     this.bwtMergedPointers = bwtMergedPointers;
/* 265 */     this.bwtCurrentMergedPointer = bwtMergedPointers[bwtStartPointer];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int read()
/*     */   {
/* 274 */     while (this.rleRepeat < 1) {
/* 275 */       if (this.bwtBytesDecoded == this.bwtBlockLength) {
/* 276 */         return -1;
/*     */       }
/*     */       
/* 279 */       int nextByte = decodeNextBWTByte();
/* 280 */       if (nextByte != this.rleLastDecodedByte)
/*     */       {
/* 282 */         this.rleLastDecodedByte = nextByte;
/* 283 */         this.rleRepeat = 1;
/* 284 */         this.rleAccumulator = 1;
/* 285 */         this.crc.updateCRC(nextByte);
/*     */       }
/* 287 */       else if (++this.rleAccumulator == 4)
/*     */       {
/* 289 */         int rleRepeat = decodeNextBWTByte() + 1;
/* 290 */         this.rleRepeat = rleRepeat;
/* 291 */         this.rleAccumulator = 0;
/* 292 */         this.crc.updateCRC(nextByte, rleRepeat);
/*     */       } else {
/* 294 */         this.rleRepeat = 1;
/* 295 */         this.crc.updateCRC(nextByte);
/*     */       }
/*     */     }
/*     */     
/* 299 */     this.rleRepeat -= 1;
/*     */     
/* 301 */     return this.rleLastDecodedByte;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int decodeNextBWTByte()
/*     */   {
/* 310 */     int mergedPointer = this.bwtCurrentMergedPointer;
/* 311 */     int nextDecodedByte = mergedPointer & 0xFF;
/* 312 */     this.bwtCurrentMergedPointer = this.bwtMergedPointers[(mergedPointer >>> 8)];
/*     */     
/* 314 */     if ((this.blockRandomised) && 
/* 315 */       (--this.randomCount == 0)) {
/* 316 */       nextDecodedByte ^= 0x1;
/* 317 */       this.randomIndex = ((this.randomIndex + 1) % 512);
/* 318 */       this.randomCount = Bzip2Rand.rNums(this.randomIndex);
/*     */     }
/*     */     
/* 321 */     this.bwtBytesDecoded += 1;
/*     */     
/* 323 */     return nextDecodedByte;
/*     */   }
/*     */   
/*     */   public int blockLength() {
/* 327 */     return this.bwtBlockLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int checkCRC()
/*     */   {
/* 336 */     int computedBlockCRC = this.crc.getCRC();
/* 337 */     if (this.blockCRC != computedBlockCRC) {
/* 338 */       throw new DecompressionException("block CRC error");
/*     */     }
/* 340 */     return computedBlockCRC;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\Bzip2BlockDecompressor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */