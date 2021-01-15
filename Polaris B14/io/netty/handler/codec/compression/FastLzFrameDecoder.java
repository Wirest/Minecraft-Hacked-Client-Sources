/*     */ package io.netty.handler.codec.compression;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import java.util.List;
/*     */ import java.util.zip.Adler32;
/*     */ import java.util.zip.Checksum;
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
/*     */ public class FastLzFrameDecoder
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   private static enum State
/*     */   {
/*  39 */     INIT_BLOCK, 
/*  40 */     INIT_BLOCK_PARAMS, 
/*  41 */     DECOMPRESS_DATA, 
/*  42 */     CORRUPTED;
/*     */     
/*     */     private State() {} }
/*  45 */   private State currentState = State.INIT_BLOCK;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Checksum checksum;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int chunkLength;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int originalLength;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isCompressed;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean hasChecksum;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int currentChecksum;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public FastLzFrameDecoder()
/*     */   {
/*  82 */     this(false);
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
/*     */   public FastLzFrameDecoder(boolean validateChecksums)
/*     */   {
/*  96 */     this(validateChecksums ? new Adler32() : null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FastLzFrameDecoder(Checksum checksum)
/*     */   {
/* 107 */     this.checksum = checksum;
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */   {
/*     */     try {
/* 113 */       switch (this.currentState) {
/*     */       case INIT_BLOCK: 
/* 115 */         if (in.readableBytes() >= 4)
/*     */         {
/*     */ 
/*     */ 
/* 119 */           int magic = in.readUnsignedMedium();
/* 120 */           if (magic != 4607066) {
/* 121 */             throw new DecompressionException("unexpected block identifier");
/*     */           }
/*     */           
/* 124 */           byte options = in.readByte();
/* 125 */           this.isCompressed = ((options & 0x1) == 1);
/* 126 */           this.hasChecksum = ((options & 0x10) == 16);
/*     */           
/* 128 */           this.currentState = State.INIT_BLOCK_PARAMS;
/*     */         }
/*     */         break; case INIT_BLOCK_PARAMS:  if (in.readableBytes() >= 2 + (this.isCompressed ? 2 : 0) + (this.hasChecksum ? 4 : 0))
/*     */         {
/*     */ 
/* 133 */           this.currentChecksum = (this.hasChecksum ? in.readInt() : 0);
/* 134 */           this.chunkLength = in.readUnsignedShort();
/* 135 */           this.originalLength = (this.isCompressed ? in.readUnsignedShort() : this.chunkLength);
/*     */           
/* 137 */           this.currentState = State.DECOMPRESS_DATA;
/*     */         }
/*     */         break; case DECOMPRESS_DATA:  int chunkLength = this.chunkLength;
/* 140 */         if (in.readableBytes() >= chunkLength)
/*     */         {
/*     */ 
/*     */ 
/* 144 */           int idx = in.readerIndex();
/* 145 */           int originalLength = this.originalLength;
/*     */           
/*     */           int outputPtr;
/*     */           ByteBuf uncompressed;
/*     */           byte[] output;
/*     */           int outputPtr;
/* 151 */           if (originalLength != 0) {
/* 152 */             ByteBuf uncompressed = ctx.alloc().heapBuffer(originalLength, originalLength);
/* 153 */             byte[] output = uncompressed.array();
/* 154 */             outputPtr = uncompressed.arrayOffset() + uncompressed.writerIndex();
/*     */           } else {
/* 156 */             uncompressed = null;
/* 157 */             output = EmptyArrays.EMPTY_BYTES;
/* 158 */             outputPtr = 0;
/*     */           }
/*     */           
/* 161 */           boolean success = false;
/*     */           try {
/* 163 */             if (this.isCompressed) { int inputPtr;
/*     */               byte[] input;
/*     */               int inputPtr;
/* 166 */               if (in.hasArray()) {
/* 167 */                 byte[] input = in.array();
/* 168 */                 inputPtr = in.arrayOffset() + idx;
/*     */               } else {
/* 170 */                 input = new byte[chunkLength];
/* 171 */                 in.getBytes(idx, input);
/* 172 */                 inputPtr = 0;
/*     */               }
/*     */               
/* 175 */               int decompressedBytes = FastLz.decompress(input, inputPtr, chunkLength, output, outputPtr, originalLength);
/*     */               
/* 177 */               if (originalLength != decompressedBytes) {
/* 178 */                 throw new DecompressionException(String.format("stream corrupted: originalLength(%d) and actual length(%d) mismatch", new Object[] { Integer.valueOf(originalLength), Integer.valueOf(decompressedBytes) }));
/*     */               }
/*     */             }
/*     */             else
/*     */             {
/* 183 */               in.getBytes(idx, output, outputPtr, chunkLength);
/*     */             }
/*     */             
/* 186 */             Checksum checksum = this.checksum;
/* 187 */             if ((this.hasChecksum) && (checksum != null)) {
/* 188 */               checksum.reset();
/* 189 */               checksum.update(output, outputPtr, originalLength);
/* 190 */               int checksumResult = (int)checksum.getValue();
/* 191 */               if (checksumResult != this.currentChecksum) {
/* 192 */                 throw new DecompressionException(String.format("stream corrupted: mismatching checksum: %d (expected: %d)", new Object[] { Integer.valueOf(checksumResult), Integer.valueOf(this.currentChecksum) }));
/*     */               }
/*     */             }
/*     */             
/*     */ 
/*     */ 
/* 198 */             if (uncompressed != null) {
/* 199 */               uncompressed.writerIndex(uncompressed.writerIndex() + originalLength);
/* 200 */               out.add(uncompressed);
/*     */             }
/* 202 */             in.skipBytes(chunkLength);
/*     */             
/* 204 */             this.currentState = State.INIT_BLOCK;
/* 205 */             success = true;
/*     */           } finally {
/* 207 */             if (!success)
/* 208 */               uncompressed.release();
/*     */           }
/*     */         }
/* 211 */         break;
/*     */       case CORRUPTED: 
/* 213 */         in.skipBytes(in.readableBytes());
/* 214 */         break;
/*     */       default: 
/* 216 */         throw new IllegalStateException();
/*     */       }
/*     */     } catch (Exception e) {
/* 219 */       this.currentState = State.CORRUPTED;
/* 220 */       throw e;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\FastLzFrameDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */