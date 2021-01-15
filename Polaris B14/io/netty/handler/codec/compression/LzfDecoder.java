/*     */ package io.netty.handler.codec.compression;
/*     */ 
/*     */ import com.ning.compress.BufferRecycler;
/*     */ import com.ning.compress.lzf.ChunkDecoder;
/*     */ import com.ning.compress.lzf.util.ChunkDecoderFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LzfDecoder
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   private static enum State
/*     */   {
/*  44 */     INIT_BLOCK, 
/*  45 */     INIT_ORIGINAL_LENGTH, 
/*  46 */     DECOMPRESS_DATA, 
/*  47 */     CORRUPTED;
/*     */     
/*     */     private State() {} }
/*  50 */   private State currentState = State.INIT_BLOCK;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final short MAGIC_NUMBER = 23126;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ChunkDecoder decoder;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private BufferRecycler recycler;
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
/*     */ 
/*     */   public LzfDecoder()
/*     */   {
/*  90 */     this(false);
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
/*     */   public LzfDecoder(boolean safeInstance)
/*     */   {
/* 103 */     this.decoder = (safeInstance ? ChunkDecoderFactory.safeInstance() : ChunkDecoderFactory.optimalInstance());
/*     */     
/*     */ 
/*     */ 
/* 107 */     this.recycler = BufferRecycler.instance();
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */   {
/*     */     try {
/* 113 */       switch (this.currentState) {
/*     */       case INIT_BLOCK: 
/* 115 */         if (in.readableBytes() >= 5)
/*     */         {
/*     */ 
/* 118 */           int magic = in.readUnsignedShort();
/* 119 */           if (magic != 23126) {
/* 120 */             throw new DecompressionException("unexpected block identifier");
/*     */           }
/*     */           
/* 123 */           int type = in.readByte();
/* 124 */           switch (type) {
/*     */           case 0: 
/* 126 */             this.isCompressed = false;
/* 127 */             this.currentState = State.DECOMPRESS_DATA;
/* 128 */             break;
/*     */           case 1: 
/* 130 */             this.isCompressed = true;
/* 131 */             this.currentState = State.INIT_ORIGINAL_LENGTH;
/* 132 */             break;
/*     */           default: 
/* 134 */             throw new DecompressionException(String.format("unknown type of chunk: %d (expected: %d or %d)", new Object[] { Integer.valueOf(type), Integer.valueOf(0), Integer.valueOf(1) }));
/*     */           }
/*     */           
/*     */           
/* 138 */           this.chunkLength = in.readUnsignedShort();
/*     */           
/* 140 */           if (type != 1)
/*     */             break;
/*     */         }
/*     */         break;
/* 144 */       case INIT_ORIGINAL_LENGTH:  if (in.readableBytes() >= 2)
/*     */         {
/*     */ 
/* 147 */           this.originalLength = in.readUnsignedShort();
/*     */           
/* 149 */           this.currentState = State.DECOMPRESS_DATA;
/*     */         }
/*     */         break; case DECOMPRESS_DATA:  int chunkLength = this.chunkLength;
/* 152 */         if (in.readableBytes() >= chunkLength)
/*     */         {
/*     */ 
/* 155 */           int originalLength = this.originalLength;
/*     */           
/* 157 */           if (this.isCompressed) {
/* 158 */             int idx = in.readerIndex();
/*     */             int inPos;
/*     */             byte[] inputArray;
/*     */             int inPos;
/* 162 */             if (in.hasArray()) {
/* 163 */               byte[] inputArray = in.array();
/* 164 */               inPos = in.arrayOffset() + idx;
/*     */             } else {
/* 166 */               inputArray = this.recycler.allocInputBuffer(chunkLength);
/* 167 */               in.getBytes(idx, inputArray, 0, chunkLength);
/* 168 */               inPos = 0;
/*     */             }
/*     */             
/* 171 */             ByteBuf uncompressed = ctx.alloc().heapBuffer(originalLength, originalLength);
/* 172 */             byte[] outputArray = uncompressed.array();
/* 173 */             int outPos = uncompressed.arrayOffset() + uncompressed.writerIndex();
/*     */             
/* 175 */             boolean success = false;
/*     */             try {
/* 177 */               this.decoder.decodeChunk(inputArray, inPos, outputArray, outPos, outPos + originalLength);
/* 178 */               uncompressed.writerIndex(uncompressed.writerIndex() + originalLength);
/* 179 */               out.add(uncompressed);
/* 180 */               in.skipBytes(chunkLength);
/* 181 */               success = true;
/*     */             } finally {
/* 183 */               if (!success) {
/* 184 */                 uncompressed.release();
/*     */               }
/*     */             }
/*     */             
/* 188 */             if (!in.hasArray()) {
/* 189 */               this.recycler.releaseInputBuffer(inputArray);
/*     */             }
/* 191 */           } else if (chunkLength > 0) {
/* 192 */             out.add(in.readSlice(chunkLength).retain());
/*     */           }
/*     */           
/* 195 */           this.currentState = State.INIT_BLOCK; }
/* 196 */         break;
/*     */       case CORRUPTED: 
/* 198 */         in.skipBytes(in.readableBytes());
/* 199 */         break;
/*     */       default: 
/* 201 */         throw new IllegalStateException();
/*     */       }
/*     */     } catch (Exception e) {
/* 204 */       this.currentState = State.CORRUPTED;
/* 205 */       this.decoder = null;
/* 206 */       this.recycler = null;
/* 207 */       throw e;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\LzfDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */