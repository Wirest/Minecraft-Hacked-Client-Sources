/*     */ package io.netty.handler.codec.compression;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Arrays;
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
/*     */ final class Bzip2HuffmanStageEncoder
/*     */ {
/*     */   private static final int HUFFMAN_HIGH_SYMBOL_COST = 15;
/*     */   private final Bzip2BitWriter writer;
/*     */   private final char[] mtfBlock;
/*     */   private final int mtfLength;
/*     */   private final int mtfAlphabetSize;
/*     */   private final int[] mtfSymbolFrequencies;
/*     */   private final int[][] huffmanCodeLengths;
/*     */   private final int[][] huffmanMergedCodeSymbols;
/*     */   private final byte[] selectors;
/*     */   
/*     */   Bzip2HuffmanStageEncoder(Bzip2BitWriter writer, char[] mtfBlock, int mtfLength, int mtfAlphabetSize, int[] mtfSymbolFrequencies)
/*     */   {
/*  82 */     this.writer = writer;
/*  83 */     this.mtfBlock = mtfBlock;
/*  84 */     this.mtfLength = mtfLength;
/*  85 */     this.mtfAlphabetSize = mtfAlphabetSize;
/*  86 */     this.mtfSymbolFrequencies = mtfSymbolFrequencies;
/*     */     
/*  88 */     int totalTables = selectTableCount(mtfLength);
/*     */     
/*  90 */     this.huffmanCodeLengths = new int[totalTables][mtfAlphabetSize];
/*  91 */     this.huffmanMergedCodeSymbols = new int[totalTables][mtfAlphabetSize];
/*  92 */     this.selectors = new byte[(mtfLength + 50 - 1) / 50];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int selectTableCount(int mtfLength)
/*     */   {
/* 101 */     if (mtfLength >= 2400) {
/* 102 */       return 6;
/*     */     }
/* 104 */     if (mtfLength >= 1200) {
/* 105 */       return 5;
/*     */     }
/* 107 */     if (mtfLength >= 600) {
/* 108 */       return 4;
/*     */     }
/* 110 */     if (mtfLength >= 200) {
/* 111 */       return 3;
/*     */     }
/* 113 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void generateHuffmanCodeLengths(int alphabetSize, int[] symbolFrequencies, int[] codeLengths)
/*     */   {
/* 125 */     int[] mergedFrequenciesAndIndices = new int[alphabetSize];
/* 126 */     int[] sortedFrequencies = new int[alphabetSize];
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
/* 137 */     for (int i = 0; i < alphabetSize; i++) {
/* 138 */       mergedFrequenciesAndIndices[i] = (symbolFrequencies[i] << 9 | i);
/*     */     }
/* 140 */     Arrays.sort(mergedFrequenciesAndIndices);
/* 141 */     for (int i = 0; i < alphabetSize; i++) {
/* 142 */       mergedFrequenciesAndIndices[i] >>>= 9;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 147 */     Bzip2HuffmanAllocator.allocateHuffmanCodeLengths(sortedFrequencies, 20);
/*     */     
/*     */ 
/* 150 */     for (int i = 0; i < alphabetSize; i++) {
/* 151 */       codeLengths[(mergedFrequenciesAndIndices[i] & 0x1FF)] = sortedFrequencies[i];
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void generateHuffmanOptimisationSeeds()
/*     */   {
/* 162 */     int[][] huffmanCodeLengths = this.huffmanCodeLengths;
/* 163 */     int[] mtfSymbolFrequencies = this.mtfSymbolFrequencies;
/* 164 */     int mtfAlphabetSize = this.mtfAlphabetSize;
/*     */     
/* 166 */     int totalTables = huffmanCodeLengths.length;
/*     */     
/* 168 */     int remainingLength = this.mtfLength;
/* 169 */     int lowCostEnd = -1;
/*     */     
/* 171 */     for (int i = 0; i < totalTables; i++)
/*     */     {
/* 173 */       int targetCumulativeFrequency = remainingLength / (totalTables - i);
/* 174 */       int lowCostStart = lowCostEnd + 1;
/* 175 */       int actualCumulativeFrequency = 0;
/*     */       
/* 177 */       while ((actualCumulativeFrequency < targetCumulativeFrequency) && (lowCostEnd < mtfAlphabetSize - 1)) {
/* 178 */         actualCumulativeFrequency += mtfSymbolFrequencies[(++lowCostEnd)];
/*     */       }
/*     */       
/* 181 */       if ((lowCostEnd > lowCostStart) && (i != 0) && (i != totalTables - 1) && ((totalTables - i & 0x1) == 0)) {
/* 182 */         actualCumulativeFrequency -= mtfSymbolFrequencies[(lowCostEnd--)];
/*     */       }
/*     */       
/* 185 */       int[] tableCodeLengths = huffmanCodeLengths[i];
/* 186 */       for (int j = 0; j < mtfAlphabetSize; j++) {
/* 187 */         if ((j < lowCostStart) || (j > lowCostEnd)) {
/* 188 */           tableCodeLengths[j] = 15;
/*     */         }
/*     */       }
/*     */       
/* 192 */       remainingLength -= actualCumulativeFrequency;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void optimiseSelectorsAndHuffmanTables(boolean storeSelectors)
/*     */   {
/* 205 */     char[] mtfBlock = this.mtfBlock;
/* 206 */     byte[] selectors = this.selectors;
/* 207 */     int[][] huffmanCodeLengths = this.huffmanCodeLengths;
/* 208 */     int mtfLength = this.mtfLength;
/* 209 */     int mtfAlphabetSize = this.mtfAlphabetSize;
/*     */     
/* 211 */     int totalTables = huffmanCodeLengths.length;
/* 212 */     int[][] tableFrequencies = new int[totalTables][mtfAlphabetSize];
/*     */     
/* 214 */     int selectorIndex = 0;
/*     */     
/*     */ 
/* 217 */     for (int groupStart = 0; groupStart < mtfLength;)
/*     */     {
/* 219 */       int groupEnd = Math.min(groupStart + 50, mtfLength) - 1;
/*     */       
/*     */ 
/* 222 */       short[] cost = new short[totalTables];
/* 223 */       for (int i = groupStart; i <= groupEnd; i++) {
/* 224 */         int value = mtfBlock[i];
/* 225 */         for (int j = 0; j < totalTables; j++) {
/* 226 */           int tmp107_105 = j; short[] tmp107_103 = cost;tmp107_103[tmp107_105] = ((short)(tmp107_103[tmp107_105] + huffmanCodeLengths[j][value]));
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 231 */       byte bestTable = 0;
/* 232 */       int bestCost = cost[0];
/* 233 */       for (byte i = 1; i < totalTables; i = (byte)(i + 1)) {
/* 234 */         int tableCost = cost[i];
/* 235 */         if (tableCost < bestCost) {
/* 236 */           bestCost = tableCost;
/* 237 */           bestTable = i;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 242 */       int[] bestGroupFrequencies = tableFrequencies[bestTable];
/* 243 */       for (int i = groupStart; i <= groupEnd; i++) {
/* 244 */         bestGroupFrequencies[mtfBlock[i]] += 1;
/*     */       }
/*     */       
/*     */ 
/* 248 */       if (storeSelectors) {
/* 249 */         selectors[(selectorIndex++)] = bestTable;
/*     */       }
/* 251 */       groupStart = groupEnd + 1;
/*     */     }
/*     */     
/*     */ 
/* 255 */     for (int i = 0; i < totalTables; i++) {
/* 256 */       generateHuffmanCodeLengths(mtfAlphabetSize, tableFrequencies[i], huffmanCodeLengths[i]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void assignHuffmanCodeSymbols()
/*     */   {
/* 264 */     int[][] huffmanMergedCodeSymbols = this.huffmanMergedCodeSymbols;
/* 265 */     int[][] huffmanCodeLengths = this.huffmanCodeLengths;
/* 266 */     int mtfAlphabetSize = this.mtfAlphabetSize;
/*     */     
/* 268 */     int totalTables = huffmanCodeLengths.length;
/*     */     
/* 270 */     for (int i = 0; i < totalTables; i++) {
/* 271 */       int[] tableLengths = huffmanCodeLengths[i];
/*     */       
/* 273 */       int minimumLength = 32;
/* 274 */       int maximumLength = 0;
/* 275 */       for (int j = 0; j < mtfAlphabetSize; j++) {
/* 276 */         int length = tableLengths[j];
/* 277 */         if (length > maximumLength) {
/* 278 */           maximumLength = length;
/*     */         }
/* 280 */         if (length < minimumLength) {
/* 281 */           minimumLength = length;
/*     */         }
/*     */       }
/*     */       
/* 285 */       int code = 0;
/* 286 */       for (int j = minimumLength; j <= maximumLength; j++) {
/* 287 */         for (int k = 0; k < mtfAlphabetSize; k++) {
/* 288 */           if ((huffmanCodeLengths[i][k] & 0xFF) == j) {
/* 289 */             huffmanMergedCodeSymbols[i][k] = (j << 24 | code);
/* 290 */             code++;
/*     */           }
/*     */         }
/* 293 */         code <<= 1;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void writeSelectorsAndHuffmanTables(ByteBuf out)
/*     */   {
/* 302 */     Bzip2BitWriter writer = this.writer;
/* 303 */     byte[] selectors = this.selectors;
/* 304 */     int totalSelectors = selectors.length;
/* 305 */     int[][] huffmanCodeLengths = this.huffmanCodeLengths;
/* 306 */     int totalTables = huffmanCodeLengths.length;
/* 307 */     int mtfAlphabetSize = this.mtfAlphabetSize;
/*     */     
/* 309 */     writer.writeBits(out, 3, totalTables);
/* 310 */     writer.writeBits(out, 15, totalSelectors);
/*     */     
/*     */ 
/* 313 */     Bzip2MoveToFrontTable selectorMTF = new Bzip2MoveToFrontTable();
/* 314 */     for (byte selector : selectors) {
/* 315 */       writer.writeUnary(out, selectorMTF.valueToFront(selector));
/*     */     }
/*     */     
/*     */ 
/* 319 */     for (int[] tableLengths : huffmanCodeLengths) {
/* 320 */       int currentLength = tableLengths[0];
/*     */       
/* 322 */       writer.writeBits(out, 5, currentLength);
/*     */       
/* 324 */       for (int j = 0; j < mtfAlphabetSize; j++) {
/* 325 */         int codeLength = tableLengths[j];
/* 326 */         int value = currentLength < codeLength ? 2 : 3;
/* 327 */         int delta = Math.abs(codeLength - currentLength);
/* 328 */         while (delta-- > 0) {
/* 329 */           writer.writeBits(out, 2, value);
/*     */         }
/* 331 */         writer.writeBoolean(out, false);
/* 332 */         currentLength = codeLength;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void writeBlockData(ByteBuf out)
/*     */   {
/* 341 */     Bzip2BitWriter writer = this.writer;
/* 342 */     int[][] huffmanMergedCodeSymbols = this.huffmanMergedCodeSymbols;
/* 343 */     byte[] selectors = this.selectors;
/* 344 */     char[] mtf = this.mtfBlock;
/* 345 */     int mtfLength = this.mtfLength;
/*     */     
/* 347 */     int selectorIndex = 0;
/* 348 */     for (int mtfIndex = 0; mtfIndex < mtfLength;) {
/* 349 */       int groupEnd = Math.min(mtfIndex + 50, mtfLength) - 1;
/* 350 */       int[] tableMergedCodeSymbols = huffmanMergedCodeSymbols[selectors[(selectorIndex++)]];
/*     */       
/* 352 */       while (mtfIndex <= groupEnd) {
/* 353 */         int mergedCodeSymbol = tableMergedCodeSymbols[mtf[(mtfIndex++)]];
/* 354 */         writer.writeBits(out, mergedCodeSymbol >>> 24, mergedCodeSymbol);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void encode(ByteBuf out)
/*     */   {
/* 364 */     generateHuffmanOptimisationSeeds();
/* 365 */     for (int i = 3; i >= 0; i--) {
/* 366 */       optimiseSelectorsAndHuffmanTables(i == 0);
/*     */     }
/* 368 */     assignHuffmanCodeSymbols();
/*     */     
/*     */ 
/* 371 */     writeSelectorsAndHuffmanTables(out);
/* 372 */     writeBlockData(out);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\Bzip2HuffmanStageEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */