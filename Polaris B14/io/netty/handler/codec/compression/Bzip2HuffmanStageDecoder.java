/*     */ package io.netty.handler.codec.compression;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class Bzip2HuffmanStageDecoder
/*     */ {
/*     */   private final Bzip2BitReader reader;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   byte[] selectors;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int[] minimumLengths;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int[][] codeBases;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int[][] codeLimits;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int[][] codeSymbols;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int currentTable;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  64 */   private int groupIndex = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  69 */   private int groupPosition = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   final int totalTables;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   final int alphabetSize;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  84 */   final Bzip2MoveToFrontTable tableMTF = new Bzip2MoveToFrontTable();
/*     */   
/*     */ 
/*     */   int currentSelector;
/*     */   
/*     */ 
/*     */   final byte[][] tableCodeLengths;
/*     */   
/*     */ 
/*     */   int currentGroup;
/*     */   
/*     */ 
/*  96 */   int currentLength = -1;
/*     */   int currentAlpha;
/*     */   boolean modifyLength;
/*     */   
/*     */   Bzip2HuffmanStageDecoder(Bzip2BitReader reader, int totalTables, int alphabetSize) {
/* 101 */     this.reader = reader;
/* 102 */     this.totalTables = totalTables;
/* 103 */     this.alphabetSize = alphabetSize;
/*     */     
/* 105 */     this.minimumLengths = new int[totalTables];
/* 106 */     this.codeBases = new int[totalTables][25];
/* 107 */     this.codeLimits = new int[totalTables][24];
/* 108 */     this.codeSymbols = new int[totalTables]['Ă'];
/* 109 */     this.tableCodeLengths = new byte[totalTables]['Ă'];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void createHuffmanDecodingTables()
/*     */   {
/* 116 */     int alphabetSize = this.alphabetSize;
/*     */     
/* 118 */     for (int table = 0; table < this.tableCodeLengths.length; table++) {
/* 119 */       int[] tableBases = this.codeBases[table];
/* 120 */       int[] tableLimits = this.codeLimits[table];
/* 121 */       int[] tableSymbols = this.codeSymbols[table];
/* 122 */       byte[] codeLengths = this.tableCodeLengths[table];
/*     */       
/* 124 */       int minimumLength = 23;
/* 125 */       int maximumLength = 0;
/*     */       
/*     */ 
/* 128 */       for (int i = 0; i < alphabetSize; i++) {
/* 129 */         byte currLength = codeLengths[i];
/* 130 */         maximumLength = Math.max(currLength, maximumLength);
/* 131 */         minimumLength = Math.min(currLength, minimumLength);
/*     */       }
/* 133 */       this.minimumLengths[table] = minimumLength;
/*     */       
/*     */ 
/* 136 */       for (int i = 0; i < alphabetSize; i++) {
/* 137 */         tableBases[(codeLengths[i] + 1)] += 1;
/*     */       }
/* 139 */       int i = 1; for (int b = tableBases[0]; i < 25; i++) {
/* 140 */         b += tableBases[i];
/* 141 */         tableBases[i] = b;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 146 */       int i = minimumLength; for (int code = 0; i <= maximumLength; i++) {
/* 147 */         int base = code;
/* 148 */         code += tableBases[(i + 1)] - tableBases[i];
/* 149 */         tableBases[i] = (base - tableBases[i]);
/* 150 */         tableLimits[i] = (code - 1);
/* 151 */         code <<= 1;
/*     */       }
/*     */       
/*     */ 
/* 155 */       int bitLength = minimumLength; for (int codeIndex = 0; bitLength <= maximumLength; bitLength++) {
/* 156 */         for (int symbol = 0; symbol < alphabetSize; symbol++) {
/* 157 */           if (codeLengths[symbol] == bitLength) {
/* 158 */             tableSymbols[(codeIndex++)] = symbol;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 164 */     this.currentTable = this.selectors[0];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int nextSymbol()
/*     */   {
/* 173 */     if (++this.groupPosition % 50 == 0) {
/* 174 */       this.groupIndex += 1;
/* 175 */       if (this.groupIndex == this.selectors.length) {
/* 176 */         throw new DecompressionException("error decoding block");
/*     */       }
/* 178 */       this.currentTable = (this.selectors[this.groupIndex] & 0xFF);
/*     */     }
/*     */     
/* 181 */     Bzip2BitReader reader = this.reader;
/* 182 */     int currentTable = this.currentTable;
/* 183 */     int[] tableLimits = this.codeLimits[currentTable];
/* 184 */     int[] tableBases = this.codeBases[currentTable];
/* 185 */     int[] tableSymbols = this.codeSymbols[currentTable];
/* 186 */     int codeLength = this.minimumLengths[currentTable];
/*     */     
/*     */ 
/*     */ 
/* 190 */     int codeBits = reader.readBits(codeLength);
/* 191 */     for (; codeLength <= 23; codeLength++) {
/* 192 */       if (codeBits <= tableLimits[codeLength])
/*     */       {
/* 194 */         return tableSymbols[(codeBits - tableBases[codeLength])];
/*     */       }
/* 196 */       codeBits = codeBits << 1 | reader.readBits(1);
/*     */     }
/*     */     
/* 199 */     throw new DecompressionException("a valid code was not recognised");
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\Bzip2HuffmanStageDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */