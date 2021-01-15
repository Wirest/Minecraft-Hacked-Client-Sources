/*     */ package io.netty.handler.codec.compression;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import java.util.List;
/*     */ import java.util.zip.Checksum;
/*     */ import net.jpountz.lz4.LZ4Exception;
/*     */ import net.jpountz.lz4.LZ4Factory;
/*     */ import net.jpountz.lz4.LZ4FastDecompressor;
/*     */ import net.jpountz.xxhash.StreamingXXHash32;
/*     */ import net.jpountz.xxhash.XXHashFactory;
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
/*     */ public class Lz4FrameDecoder
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   private static enum State
/*     */   {
/*  52 */     INIT_BLOCK, 
/*  53 */     DECOMPRESS_DATA, 
/*  54 */     FINISHED, 
/*  55 */     CORRUPTED;
/*     */     
/*     */     private State() {} }
/*  58 */   private State currentState = State.INIT_BLOCK;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private LZ4FastDecompressor decompressor;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Checksum checksum;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int blockType;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int compressedLength;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int decompressedLength;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int currentChecksum;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Lz4FrameDecoder()
/*     */   {
/* 100 */     this(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Lz4FrameDecoder(boolean validateChecksums)
/*     */   {
/* 111 */     this(LZ4Factory.fastestInstance(), validateChecksums);
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
/*     */   public Lz4FrameDecoder(LZ4Factory factory, boolean validateChecksums)
/*     */   {
/* 127 */     this(factory, validateChecksums ? XXHashFactory.fastestInstance().newStreamingHash32(-1756908916).asChecksum() : null);
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
/*     */   public Lz4FrameDecoder(LZ4Factory factory, Checksum checksum)
/*     */   {
/* 142 */     if (factory == null) {
/* 143 */       throw new NullPointerException("factory");
/*     */     }
/* 145 */     this.decompressor = factory.fastDecompressor();
/* 146 */     this.checksum = checksum; }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception { try { int blockType;
/*     */       int compressedLength;
/*     */       int decompressedLength;
/*     */       int currentChecksum;
/* 152 */       switch (this.currentState) {
/*     */       case INIT_BLOCK: 
/* 154 */         if (in.readableBytes() >= 21)
/*     */         {
/*     */ 
/* 157 */           long magic = in.readLong();
/* 158 */           if (magic != 5501767354678207339L) {
/* 159 */             throw new DecompressionException("unexpected block identifier");
/*     */           }
/*     */           
/* 162 */           int token = in.readByte();
/* 163 */           int compressionLevel = (token & 0xF) + 10;
/* 164 */           blockType = token & 0xF0;
/*     */           
/* 166 */           compressedLength = Integer.reverseBytes(in.readInt());
/* 167 */           if ((compressedLength < 0) || (compressedLength > 33554432)) {
/* 168 */             throw new DecompressionException(String.format("invalid compressedLength: %d (expected: 0-%d)", new Object[] { Integer.valueOf(compressedLength), Integer.valueOf(33554432) }));
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 173 */           decompressedLength = Integer.reverseBytes(in.readInt());
/* 174 */           int maxDecompressedLength = 1 << compressionLevel;
/* 175 */           if ((decompressedLength < 0) || (decompressedLength > maxDecompressedLength)) {
/* 176 */             throw new DecompressionException(String.format("invalid decompressedLength: %d (expected: 0-%d)", new Object[] { Integer.valueOf(decompressedLength), Integer.valueOf(maxDecompressedLength) }));
/*     */           }
/*     */           
/*     */ 
/* 180 */           if (((decompressedLength == 0) && (compressedLength != 0)) || ((decompressedLength != 0) && (compressedLength == 0)) || ((blockType == 16) && (decompressedLength != compressedLength)))
/*     */           {
/*     */ 
/* 183 */             throw new DecompressionException(String.format("stream corrupted: compressedLength(%d) and decompressedLength(%d) mismatch", new Object[] { Integer.valueOf(compressedLength), Integer.valueOf(decompressedLength) }));
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 188 */           currentChecksum = Integer.reverseBytes(in.readInt());
/* 189 */           if ((decompressedLength == 0) && (compressedLength == 0)) {
/* 190 */             if (currentChecksum != 0) {
/* 191 */               throw new DecompressionException("stream corrupted: checksum error");
/*     */             }
/* 193 */             this.currentState = State.FINISHED;
/* 194 */             this.decompressor = null;
/* 195 */             this.checksum = null;
/*     */           }
/*     */           else
/*     */           {
/* 199 */             this.blockType = blockType;
/* 200 */             this.compressedLength = compressedLength;
/* 201 */             this.decompressedLength = decompressedLength;
/* 202 */             this.currentChecksum = currentChecksum;
/*     */             
/* 204 */             this.currentState = State.DECOMPRESS_DATA;
/*     */           } }
/*     */         break; case DECOMPRESS_DATA:  blockType = this.blockType;
/* 207 */         compressedLength = this.compressedLength;
/* 208 */         decompressedLength = this.decompressedLength;
/* 209 */         currentChecksum = this.currentChecksum;
/*     */         
/* 211 */         if (in.readableBytes() >= compressedLength)
/*     */         {
/*     */ 
/*     */ 
/* 215 */           int idx = in.readerIndex();
/*     */           
/* 217 */           ByteBuf uncompressed = ctx.alloc().heapBuffer(decompressedLength, decompressedLength);
/* 218 */           byte[] dest = uncompressed.array();
/* 219 */           int destOff = uncompressed.arrayOffset() + uncompressed.writerIndex();
/*     */           
/* 221 */           boolean success = false;
/*     */           try {
/* 223 */             switch (blockType) {
/*     */             case 16: 
/* 225 */               in.getBytes(idx, dest, destOff, decompressedLength);
/* 226 */               break;
/*     */             case 32: 
/*     */               int srcOff;
/*     */               byte[] src;
/*     */               int srcOff;
/* 231 */               if (in.hasArray()) {
/* 232 */                 byte[] src = in.array();
/* 233 */                 srcOff = in.arrayOffset() + idx;
/*     */               } else {
/* 235 */                 src = new byte[compressedLength];
/* 236 */                 in.getBytes(idx, src);
/* 237 */                 srcOff = 0;
/*     */               }
/*     */               try
/*     */               {
/* 241 */                 int readBytes = this.decompressor.decompress(src, srcOff, dest, destOff, decompressedLength);
/*     */                 
/* 243 */                 if (compressedLength != readBytes) {
/* 244 */                   throw new DecompressionException(String.format("stream corrupted: compressedLength(%d) and actual length(%d) mismatch", new Object[] { Integer.valueOf(compressedLength), Integer.valueOf(readBytes) }));
/*     */                 }
/*     */               }
/*     */               catch (LZ4Exception e)
/*     */               {
/* 249 */                 throw new DecompressionException(e);
/*     */               }
/*     */             
/*     */ 
/*     */             default: 
/* 254 */               throw new DecompressionException(String.format("unexpected blockType: %d (expected: %d or %d)", new Object[] { Integer.valueOf(blockType), Integer.valueOf(16), Integer.valueOf(32) }));
/*     */             }
/*     */             
/*     */             
/*     */ 
/* 259 */             Checksum checksum = this.checksum;
/* 260 */             if (checksum != null) {
/* 261 */               checksum.reset();
/* 262 */               checksum.update(dest, destOff, decompressedLength);
/* 263 */               int checksumResult = (int)checksum.getValue();
/* 264 */               if (checksumResult != currentChecksum) {
/* 265 */                 throw new DecompressionException(String.format("stream corrupted: mismatching checksum: %d (expected: %d)", new Object[] { Integer.valueOf(checksumResult), Integer.valueOf(currentChecksum) }));
/*     */               }
/*     */             }
/*     */             
/*     */ 
/* 270 */             uncompressed.writerIndex(uncompressed.writerIndex() + decompressedLength);
/* 271 */             out.add(uncompressed);
/* 272 */             in.skipBytes(compressedLength);
/*     */             
/* 274 */             this.currentState = State.INIT_BLOCK;
/* 275 */             success = true;
/*     */           } finally {
/* 277 */             if (!success)
/* 278 */               uncompressed.release();
/*     */           }
/*     */         }
/* 281 */         break;
/*     */       case FINISHED: 
/*     */       case CORRUPTED: 
/* 284 */         in.skipBytes(in.readableBytes());
/* 285 */         break;
/*     */       default: 
/* 287 */         throw new IllegalStateException();
/*     */       }
/*     */     } catch (Exception e) {
/* 290 */       this.currentState = State.CORRUPTED;
/* 291 */       throw e;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isClosed()
/*     */   {
/* 300 */     return this.currentState == State.FINISHED;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\Lz4FrameDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */