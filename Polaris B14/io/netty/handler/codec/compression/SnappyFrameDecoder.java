/*     */ package io.netty.handler.codec.compression;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import java.util.Arrays;
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
/*     */ public class SnappyFrameDecoder
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   private static enum ChunkType
/*     */   {
/*  42 */     STREAM_IDENTIFIER, 
/*  43 */     COMPRESSED_DATA, 
/*  44 */     UNCOMPRESSED_DATA, 
/*  45 */     RESERVED_UNSKIPPABLE, 
/*  46 */     RESERVED_SKIPPABLE;
/*     */     
/*     */     private ChunkType() {} }
/*  49 */   private static final byte[] SNAPPY = { 115, 78, 97, 80, 112, 89 };
/*     */   
/*     */   private static final int MAX_UNCOMPRESSED_DATA_SIZE = 65540;
/*  52 */   private final Snappy snappy = new Snappy();
/*     */   
/*     */ 
/*     */   private final boolean validateChecksums;
/*     */   
/*     */   private boolean started;
/*     */   
/*     */   private boolean corrupted;
/*     */   
/*     */ 
/*     */   public SnappyFrameDecoder()
/*     */   {
/*  64 */     this(false);
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
/*     */   public SnappyFrameDecoder(boolean validateChecksums)
/*     */   {
/*  77 */     this.validateChecksums = validateChecksums;
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */   {
/*  82 */     if (this.corrupted) {
/*  83 */       in.skipBytes(in.readableBytes());
/*  84 */       return;
/*     */     }
/*     */     try
/*     */     {
/*  88 */       int idx = in.readerIndex();
/*  89 */       int inSize = in.readableBytes();
/*  90 */       if (inSize < 4)
/*     */       {
/*     */ 
/*  93 */         return;
/*     */       }
/*     */       
/*  96 */       int chunkTypeVal = in.getUnsignedByte(idx);
/*  97 */       ChunkType chunkType = mapChunkType((byte)chunkTypeVal);
/*  98 */       int chunkLength = ByteBufUtil.swapMedium(in.getUnsignedMedium(idx + 1));
/*     */       
/* 100 */       switch (chunkType) {
/*     */       case STREAM_IDENTIFIER: 
/* 102 */         if (chunkLength != SNAPPY.length) {
/* 103 */           throw new DecompressionException("Unexpected length of stream identifier: " + chunkLength);
/*     */         }
/*     */         
/* 106 */         if (inSize >= 4 + SNAPPY.length)
/*     */         {
/*     */ 
/*     */ 
/* 110 */           byte[] identifier = new byte[chunkLength];
/* 111 */           in.skipBytes(4).readBytes(identifier);
/*     */           
/* 113 */           if (!Arrays.equals(identifier, SNAPPY)) {
/* 114 */             throw new DecompressionException("Unexpected stream identifier contents. Mismatched snappy protocol version?");
/*     */           }
/*     */           
/*     */ 
/* 118 */           this.started = true; }
/* 119 */         break;
/*     */       case RESERVED_SKIPPABLE: 
/* 121 */         if (!this.started) {
/* 122 */           throw new DecompressionException("Received RESERVED_SKIPPABLE tag before STREAM_IDENTIFIER");
/*     */         }
/*     */         
/* 125 */         if (inSize < 4 + chunkLength)
/*     */         {
/* 127 */           return;
/*     */         }
/*     */         
/* 130 */         in.skipBytes(4 + chunkLength);
/* 131 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case RESERVED_UNSKIPPABLE: 
/* 136 */         throw new DecompressionException("Found reserved unskippable chunk type: 0x" + Integer.toHexString(chunkTypeVal));
/*     */       
/*     */       case UNCOMPRESSED_DATA: 
/* 139 */         if (!this.started) {
/* 140 */           throw new DecompressionException("Received UNCOMPRESSED_DATA tag before STREAM_IDENTIFIER");
/*     */         }
/* 142 */         if (chunkLength > 65540) {
/* 143 */           throw new DecompressionException("Received UNCOMPRESSED_DATA larger than 65540 bytes");
/*     */         }
/*     */         
/* 146 */         if (inSize < 4 + chunkLength) {
/* 147 */           return;
/*     */         }
/*     */         
/* 150 */         in.skipBytes(4);
/* 151 */         if (this.validateChecksums) {
/* 152 */           int checksum = ByteBufUtil.swapInt(in.readInt());
/* 153 */           Snappy.validateChecksum(checksum, in, in.readerIndex(), chunkLength - 4);
/*     */         } else {
/* 155 */           in.skipBytes(4);
/*     */         }
/* 157 */         out.add(in.readSlice(chunkLength - 4).retain());
/* 158 */         break;
/*     */       case COMPRESSED_DATA: 
/* 160 */         if (!this.started) {
/* 161 */           throw new DecompressionException("Received COMPRESSED_DATA tag before STREAM_IDENTIFIER");
/*     */         }
/*     */         
/* 164 */         if (inSize < 4 + chunkLength) {
/* 165 */           return;
/*     */         }
/*     */         
/* 168 */         in.skipBytes(4);
/* 169 */         int checksum = ByteBufUtil.swapInt(in.readInt());
/* 170 */         ByteBuf uncompressed = ctx.alloc().buffer(0);
/* 171 */         if (this.validateChecksums) {
/* 172 */           int oldWriterIndex = in.writerIndex();
/*     */           try {
/* 174 */             in.writerIndex(in.readerIndex() + chunkLength - 4);
/* 175 */             this.snappy.decode(in, uncompressed);
/*     */           } finally {
/* 177 */             in.writerIndex(oldWriterIndex);
/*     */           }
/* 179 */           Snappy.validateChecksum(checksum, uncompressed, 0, uncompressed.writerIndex());
/*     */         } else {
/* 181 */           this.snappy.decode(in.readSlice(chunkLength - 4), uncompressed);
/*     */         }
/* 183 */         out.add(uncompressed);
/* 184 */         this.snappy.reset();
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 188 */       this.corrupted = true;
/* 189 */       throw e;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static ChunkType mapChunkType(byte type)
/*     */   {
/* 200 */     if (type == 0)
/* 201 */       return ChunkType.COMPRESSED_DATA;
/* 202 */     if (type == 1)
/* 203 */       return ChunkType.UNCOMPRESSED_DATA;
/* 204 */     if (type == -1)
/* 205 */       return ChunkType.STREAM_IDENTIFIER;
/* 206 */     if ((type & 0x80) == 128) {
/* 207 */       return ChunkType.RESERVED_SKIPPABLE;
/*     */     }
/* 209 */     return ChunkType.RESERVED_UNSKIPPABLE;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\SnappyFrameDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */