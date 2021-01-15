/*     */ package io.netty.handler.codec.compression;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import java.util.List;
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
/*     */ public class Bzip2Decoder
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   private static enum State
/*     */   {
/*  36 */     INIT, 
/*  37 */     INIT_BLOCK, 
/*  38 */     INIT_BLOCK_PARAMS, 
/*  39 */     RECEIVE_HUFFMAN_USED_MAP, 
/*  40 */     RECEIVE_HUFFMAN_USED_BITMAPS, 
/*  41 */     RECEIVE_SELECTORS_NUMBER, 
/*  42 */     RECEIVE_SELECTORS, 
/*  43 */     RECEIVE_HUFFMAN_LENGTH, 
/*  44 */     DECODE_HUFFMAN_DATA, 
/*  45 */     EOF;
/*     */     private State() {} }
/*  47 */   private State currentState = State.INIT;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  52 */   private final Bzip2BitReader reader = new Bzip2BitReader();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Bzip2BlockDecompressor blockDecompressor;
/*     */   
/*     */ 
/*     */ 
/*     */   private Bzip2HuffmanStageDecoder huffmanStageDecoder;
/*     */   
/*     */ 
/*     */ 
/*     */   private int blockSize;
/*     */   
/*     */ 
/*     */ 
/*     */   private int blockCRC;
/*     */   
/*     */ 
/*     */ 
/*     */   private int streamCRC;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
/*     */     throws Exception
/*     */   {
/*  81 */     if (!in.isReadable()) {
/*  82 */       return;
/*     */     }
/*     */     
/*  85 */     Bzip2BitReader reader = this.reader;
/*  86 */     reader.setByteBuf(in);
/*     */     for (;;) { Bzip2BlockDecompressor blockDecompressor;
/*     */       int totalTables;
/*  89 */       int alphaSize; int totalSelectors; Bzip2HuffmanStageDecoder huffmanStageDecoder; switch (this.currentState) {
/*     */       case INIT: 
/*  91 */         if (in.readableBytes() < 4) {
/*  92 */           return;
/*     */         }
/*  94 */         int magicNumber = in.readUnsignedMedium();
/*  95 */         if (magicNumber != 4348520) {
/*  96 */           throw new DecompressionException("Unexpected stream identifier contents. Mismatched bzip2 protocol version?");
/*     */         }
/*     */         
/*  99 */         int blockSize = in.readByte() - 48;
/* 100 */         if ((blockSize < 1) || (blockSize > 9)) {
/* 101 */           throw new DecompressionException("block size is invalid");
/*     */         }
/* 103 */         this.blockSize = (blockSize * 100000);
/*     */         
/* 105 */         this.streamCRC = 0;
/* 106 */         this.currentState = State.INIT_BLOCK;
/*     */       case INIT_BLOCK: 
/* 108 */         if (!reader.hasReadableBytes(10)) {
/* 109 */           return;
/*     */         }
/*     */         
/* 112 */         int magic1 = reader.readBits(24);
/* 113 */         int magic2 = reader.readBits(24);
/* 114 */         if ((magic1 == 1536581) && (magic2 == 3690640))
/*     */         {
/* 116 */           int storedCombinedCRC = reader.readInt();
/* 117 */           if (storedCombinedCRC != this.streamCRC) {
/* 118 */             throw new DecompressionException("stream CRC error");
/*     */           }
/* 120 */           this.currentState = State.EOF;
/*     */         }
/*     */         else {
/* 123 */           if ((magic1 != 3227993) || (magic2 != 2511705)) {
/* 124 */             throw new DecompressionException("bad block header");
/*     */           }
/* 126 */           this.blockCRC = reader.readInt();
/* 127 */           this.currentState = State.INIT_BLOCK_PARAMS;
/*     */         }
/*     */         break; case INIT_BLOCK_PARAMS:  if (!reader.hasReadableBits(25)) {
/* 130 */           return;
/*     */         }
/* 132 */         boolean blockRandomised = reader.readBoolean();
/* 133 */         int bwtStartPointer = reader.readBits(24);
/*     */         
/* 135 */         this.blockDecompressor = new Bzip2BlockDecompressor(this.blockSize, this.blockCRC, blockRandomised, bwtStartPointer, reader);
/*     */         
/* 137 */         this.currentState = State.RECEIVE_HUFFMAN_USED_MAP;
/*     */       case RECEIVE_HUFFMAN_USED_MAP: 
/* 139 */         if (!reader.hasReadableBits(16)) {
/* 140 */           return;
/*     */         }
/* 142 */         this.blockDecompressor.huffmanInUse16 = reader.readBits(16);
/* 143 */         this.currentState = State.RECEIVE_HUFFMAN_USED_BITMAPS;
/*     */       case RECEIVE_HUFFMAN_USED_BITMAPS: 
/* 145 */         blockDecompressor = this.blockDecompressor;
/* 146 */         int inUse16 = blockDecompressor.huffmanInUse16;
/* 147 */         int bitNumber = Integer.bitCount(inUse16);
/* 148 */         byte[] huffmanSymbolMap = blockDecompressor.huffmanSymbolMap;
/*     */         
/* 150 */         if (!reader.hasReadableBits(bitNumber * 16 + 3)) {
/* 151 */           return;
/*     */         }
/*     */         
/* 154 */         int huffmanSymbolCount = 0;
/* 155 */         if (bitNumber > 0) {
/* 156 */           for (int i = 0; i < 16; i++) {
/* 157 */             if ((inUse16 & 32768 >>> i) != 0) {
/* 158 */               int j = 0; for (int k = i << 4; j < 16; k++) {
/* 159 */                 if (reader.readBoolean()) {
/* 160 */                   huffmanSymbolMap[(huffmanSymbolCount++)] = ((byte)k);
/*     */                 }
/* 158 */                 j++;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 166 */         blockDecompressor.huffmanEndOfBlockSymbol = (huffmanSymbolCount + 1);
/*     */         
/* 168 */         totalTables = reader.readBits(3);
/* 169 */         if ((totalTables < 2) || (totalTables > 6)) {
/* 170 */           throw new DecompressionException("incorrect huffman groups number");
/*     */         }
/* 172 */         alphaSize = huffmanSymbolCount + 2;
/* 173 */         if (alphaSize > 258) {
/* 174 */           throw new DecompressionException("incorrect alphabet size");
/*     */         }
/* 176 */         this.huffmanStageDecoder = new Bzip2HuffmanStageDecoder(reader, totalTables, alphaSize);
/* 177 */         this.currentState = State.RECEIVE_SELECTORS_NUMBER;
/*     */       case RECEIVE_SELECTORS_NUMBER: 
/* 179 */         if (!reader.hasReadableBits(15)) {
/* 180 */           return;
/*     */         }
/* 182 */         totalSelectors = reader.readBits(15);
/* 183 */         if ((totalSelectors < 1) || (totalSelectors > 18002)) {
/* 184 */           throw new DecompressionException("incorrect selectors number");
/*     */         }
/* 186 */         this.huffmanStageDecoder.selectors = new byte[totalSelectors];
/*     */         
/* 188 */         this.currentState = State.RECEIVE_SELECTORS;
/*     */       case RECEIVE_SELECTORS: 
/* 190 */         huffmanStageDecoder = this.huffmanStageDecoder;
/* 191 */         byte[] selectors = huffmanStageDecoder.selectors;
/* 192 */         totalSelectors = selectors.length;
/* 193 */         Bzip2MoveToFrontTable tableMtf = huffmanStageDecoder.tableMTF;
/*     */         
/*     */ 
/*     */ 
/* 197 */         for (int currSelector = huffmanStageDecoder.currentSelector; 
/* 198 */             currSelector < totalSelectors; currSelector++) {
/* 199 */           if (!reader.hasReadableBits(6))
/*     */           {
/* 201 */             huffmanStageDecoder.currentSelector = currSelector;
/* 202 */             return;
/*     */           }
/* 204 */           int index = 0;
/* 205 */           while (reader.readBoolean()) {
/* 206 */             index++;
/*     */           }
/* 208 */           selectors[currSelector] = tableMtf.indexToFront(index);
/*     */         }
/*     */         
/* 211 */         this.currentState = State.RECEIVE_HUFFMAN_LENGTH;
/*     */       case RECEIVE_HUFFMAN_LENGTH: 
/* 213 */         huffmanStageDecoder = this.huffmanStageDecoder;
/* 214 */         totalTables = huffmanStageDecoder.totalTables;
/* 215 */         byte[][] codeLength = huffmanStageDecoder.tableCodeLengths;
/* 216 */         alphaSize = huffmanStageDecoder.alphabetSize;
/*     */         
/*     */ 
/*     */ 
/* 220 */         int currLength = huffmanStageDecoder.currentLength;
/* 221 */         int currAlpha = 0;
/* 222 */         boolean modifyLength = huffmanStageDecoder.modifyLength;
/* 223 */         boolean saveStateAndReturn = false;
/* 224 */         for (int currGroup = huffmanStageDecoder.currentGroup; currGroup < totalTables; currGroup++)
/*     */         {
/* 226 */           if (!reader.hasReadableBits(5)) {
/* 227 */             saveStateAndReturn = true;
/* 228 */             break;
/*     */           }
/* 230 */           if (currLength < 0) {
/* 231 */             currLength = reader.readBits(5);
/*     */           }
/* 233 */           for (currAlpha = huffmanStageDecoder.currentAlpha; currAlpha < alphaSize; currAlpha++)
/*     */           {
/* 235 */             if (!reader.isReadable()) {
/* 236 */               saveStateAndReturn = true;
/*     */               break label970;
/*     */             }
/* 239 */             while ((modifyLength) || (reader.readBoolean())) {
/* 240 */               if (!reader.isReadable()) {
/* 241 */                 modifyLength = true;
/* 242 */                 saveStateAndReturn = true;
/*     */                 
/*     */                 break label970;
/*     */               }
/* 246 */               currLength += (reader.readBoolean() ? -1 : 1);
/* 247 */               modifyLength = false;
/* 248 */               if (!reader.isReadable()) {
/* 249 */                 saveStateAndReturn = true;
/*     */                 break label970;
/*     */               }
/*     */             }
/* 253 */             codeLength[currGroup][currAlpha] = ((byte)currLength);
/*     */           }
/* 255 */           currLength = -1;
/* 256 */           currAlpha = huffmanStageDecoder.currentAlpha = 0;
/* 257 */           modifyLength = false;
/*     */         }
/* 259 */         if (saveStateAndReturn)
/*     */         {
/* 261 */           huffmanStageDecoder.currentGroup = currGroup;
/* 262 */           huffmanStageDecoder.currentLength = currLength;
/* 263 */           huffmanStageDecoder.currentAlpha = currAlpha;
/* 264 */           huffmanStageDecoder.modifyLength = modifyLength;
/* 265 */           return;
/*     */         }
/*     */         
/*     */ 
/* 269 */         huffmanStageDecoder.createHuffmanDecodingTables();
/* 270 */         this.currentState = State.DECODE_HUFFMAN_DATA;
/*     */       case DECODE_HUFFMAN_DATA: 
/* 272 */         blockDecompressor = this.blockDecompressor;
/* 273 */         int oldReaderIndex = in.readerIndex();
/* 274 */         boolean decoded = blockDecompressor.decodeHuffmanData(this.huffmanStageDecoder);
/* 275 */         if (!decoded) {
/* 276 */           return;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 281 */         if ((in.readerIndex() == oldReaderIndex) && (in.isReadable())) {
/* 282 */           reader.refill();
/*     */         }
/*     */         
/* 285 */         int blockLength = blockDecompressor.blockLength();
/* 286 */         ByteBuf uncompressed = ctx.alloc().buffer(blockLength);
/* 287 */         boolean success = false;
/*     */         try {
/*     */           int uncByte;
/* 290 */           while ((uncByte = blockDecompressor.read()) >= 0) {
/* 291 */             uncompressed.writeByte(uncByte);
/*     */           }
/*     */           
/* 294 */           int currentBlockCRC = blockDecompressor.checkCRC();
/* 295 */           this.streamCRC = ((this.streamCRC << 1 | this.streamCRC >>> 31) ^ currentBlockCRC);
/*     */           
/* 297 */           out.add(uncompressed);
/* 298 */           success = true;
/*     */         } finally {
/* 300 */           if (!success) {
/* 301 */             uncompressed.release();
/*     */           }
/*     */         }
/* 304 */         this.currentState = State.INIT_BLOCK;
/* 305 */         break;
/*     */       case EOF: 
/* 307 */         in.skipBytes(in.readableBytes());
/* 308 */         return;
/*     */       default:  label970:
/* 310 */         throw new IllegalStateException();
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isClosed()
/*     */   {
/* 320 */     return this.currentState == State.EOF;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\Bzip2Decoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */