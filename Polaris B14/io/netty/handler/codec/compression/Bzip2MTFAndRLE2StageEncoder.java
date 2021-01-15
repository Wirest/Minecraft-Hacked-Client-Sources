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
/*     */ final class Bzip2MTFAndRLE2StageEncoder
/*     */ {
/*     */   private final int[] bwtBlock;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int bwtLength;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final boolean[] bwtValuesPresent;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final char[] mtfBlock;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int mtfLength;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  55 */   private final int[] mtfSymbolFrequencies = new int['Ă'];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int alphabetSize;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Bzip2MTFAndRLE2StageEncoder(int[] bwtBlock, int bwtLength, boolean[] bwtValuesPresent)
/*     */   {
/*  69 */     this.bwtBlock = bwtBlock;
/*  70 */     this.bwtLength = bwtLength;
/*  71 */     this.bwtValuesPresent = bwtValuesPresent;
/*  72 */     this.mtfBlock = new char[bwtLength + 1];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void encode()
/*     */   {
/*  79 */     int bwtLength = this.bwtLength;
/*  80 */     boolean[] bwtValuesPresent = this.bwtValuesPresent;
/*  81 */     int[] bwtBlock = this.bwtBlock;
/*  82 */     char[] mtfBlock = this.mtfBlock;
/*  83 */     int[] mtfSymbolFrequencies = this.mtfSymbolFrequencies;
/*  84 */     byte[] huffmanSymbolMap = new byte['Ā'];
/*  85 */     Bzip2MoveToFrontTable symbolMTF = new Bzip2MoveToFrontTable();
/*     */     
/*  87 */     int totalUniqueValues = 0;
/*  88 */     for (int i = 0; i < huffmanSymbolMap.length; i++) {
/*  89 */       if (bwtValuesPresent[i] != 0) {
/*  90 */         huffmanSymbolMap[i] = ((byte)totalUniqueValues++);
/*     */       }
/*     */     }
/*  93 */     int endOfBlockSymbol = totalUniqueValues + 1;
/*     */     
/*  95 */     int mtfIndex = 0;
/*  96 */     int repeatCount = 0;
/*  97 */     int totalRunAs = 0;
/*  98 */     int totalRunBs = 0;
/*  99 */     for (int i = 0; i < bwtLength; i++)
/*     */     {
/* 101 */       int mtfPosition = symbolMTF.valueToFront(huffmanSymbolMap[(bwtBlock[i] & 0xFF)]);
/*     */       
/* 103 */       if (mtfPosition == 0) {
/* 104 */         repeatCount++;
/*     */       } else {
/* 106 */         if (repeatCount > 0) {
/* 107 */           repeatCount--;
/*     */           for (;;) {
/* 109 */             if ((repeatCount & 0x1) == 0) {
/* 110 */               mtfBlock[(mtfIndex++)] = '\000';
/* 111 */               totalRunAs++;
/*     */             } else {
/* 113 */               mtfBlock[(mtfIndex++)] = '\001';
/* 114 */               totalRunBs++;
/*     */             }
/*     */             
/* 117 */             if (repeatCount <= 1) {
/*     */               break;
/*     */             }
/* 120 */             repeatCount = repeatCount - 2 >>> 1;
/*     */           }
/* 122 */           repeatCount = 0;
/*     */         }
/* 124 */         mtfBlock[(mtfIndex++)] = ((char)(mtfPosition + 1));
/* 125 */         mtfSymbolFrequencies[(mtfPosition + 1)] += 1;
/*     */       }
/*     */     }
/*     */     
/* 129 */     if (repeatCount > 0) {
/* 130 */       repeatCount--;
/*     */       for (;;) {
/* 132 */         if ((repeatCount & 0x1) == 0) {
/* 133 */           mtfBlock[(mtfIndex++)] = '\000';
/* 134 */           totalRunAs++;
/*     */         } else {
/* 136 */           mtfBlock[(mtfIndex++)] = '\001';
/* 137 */           totalRunBs++;
/*     */         }
/*     */         
/* 140 */         if (repeatCount <= 1) {
/*     */           break;
/*     */         }
/* 143 */         repeatCount = repeatCount - 2 >>> 1;
/*     */       }
/*     */     }
/*     */     
/* 147 */     mtfBlock[mtfIndex] = ((char)endOfBlockSymbol);
/* 148 */     mtfSymbolFrequencies[endOfBlockSymbol] += 1;
/* 149 */     mtfSymbolFrequencies[0] += totalRunAs;
/* 150 */     mtfSymbolFrequencies[1] += totalRunBs;
/*     */     
/* 152 */     this.mtfLength = (mtfIndex + 1);
/* 153 */     this.alphabetSize = (endOfBlockSymbol + 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   char[] mtfBlock()
/*     */   {
/* 160 */     return this.mtfBlock;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   int mtfLength()
/*     */   {
/* 167 */     return this.mtfLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   int mtfAlphabetSize()
/*     */   {
/* 174 */     return this.alphabetSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   int[] mtfSymbolFrequencies()
/*     */   {
/* 181 */     return this.mtfSymbolFrequencies;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\Bzip2MTFAndRLE2StageEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */