/*     */ package io.netty.handler.codec.compression;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.MessageToByteEncoder;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FastLzFrameEncoder
/*     */   extends MessageToByteEncoder<ByteBuf>
/*     */ {
/*     */   private final int level;
/*     */   private final Checksum checksum;
/*     */   
/*     */   public FastLzFrameEncoder()
/*     */   {
/*  47 */     this(0, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FastLzFrameEncoder(int level)
/*     */   {
/*  59 */     this(level, null);
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
/*     */   public FastLzFrameEncoder(boolean validateChecksums)
/*     */   {
/*  73 */     this(0, validateChecksums ? new Adler32() : null);
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
/*     */   public FastLzFrameEncoder(int level, Checksum checksum)
/*     */   {
/*  88 */     super(false);
/*  89 */     if ((level != 0) && (level != 1) && (level != 2)) {
/*  90 */       throw new IllegalArgumentException(String.format("level: %d (expected: %d or %d or %d)", new Object[] { Integer.valueOf(level), Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2) }));
/*     */     }
/*     */     
/*  93 */     this.level = level;
/*  94 */     this.checksum = checksum;
/*     */   }
/*     */   
/*     */   protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception
/*     */   {
/*  99 */     Checksum checksum = this.checksum;
/*     */     for (;;)
/*     */     {
/* 102 */       if (!in.isReadable()) {
/* 103 */         return;
/*     */       }
/* 105 */       int idx = in.readerIndex();
/* 106 */       int length = Math.min(in.readableBytes(), 65535);
/*     */       
/* 108 */       int outputIdx = out.writerIndex();
/* 109 */       out.setMedium(outputIdx, 4607066);
/* 110 */       int outputOffset = outputIdx + 4 + (checksum != null ? 4 : 0);
/*     */       int chunkLength;
/*     */       byte blockType;
/*     */       int chunkLength;
/* 114 */       if (length < 32) {
/* 115 */         byte blockType = 0;
/*     */         
/* 117 */         out.ensureWritable(outputOffset + 2 + length);
/* 118 */         byte[] output = out.array();
/* 119 */         int outputPtr = out.arrayOffset() + outputOffset + 2;
/*     */         
/* 121 */         if (checksum != null) { int inputPtr;
/*     */           byte[] input;
/*     */           int inputPtr;
/* 124 */           if (in.hasArray()) {
/* 125 */             byte[] input = in.array();
/* 126 */             inputPtr = in.arrayOffset() + idx;
/*     */           } else {
/* 128 */             input = new byte[length];
/* 129 */             in.getBytes(idx, input);
/* 130 */             inputPtr = 0;
/*     */           }
/*     */           
/* 133 */           checksum.reset();
/* 134 */           checksum.update(input, inputPtr, length);
/* 135 */           out.setInt(outputIdx + 4, (int)checksum.getValue());
/*     */           
/* 137 */           System.arraycopy(input, inputPtr, output, outputPtr, length);
/*     */         } else {
/* 139 */           in.getBytes(idx, output, outputPtr, length);
/*     */         }
/* 141 */         chunkLength = length;
/*     */       } else {
/*     */         int inputPtr;
/*     */         byte[] input;
/*     */         int inputPtr;
/* 146 */         if (in.hasArray()) {
/* 147 */           byte[] input = in.array();
/* 148 */           inputPtr = in.arrayOffset() + idx;
/*     */         } else {
/* 150 */           input = new byte[length];
/* 151 */           in.getBytes(idx, input);
/* 152 */           inputPtr = 0;
/*     */         }
/*     */         
/* 155 */         if (checksum != null) {
/* 156 */           checksum.reset();
/* 157 */           checksum.update(input, inputPtr, length);
/* 158 */           out.setInt(outputIdx + 4, (int)checksum.getValue());
/*     */         }
/*     */         
/* 161 */         int maxOutputLength = FastLz.calculateOutputBufferLength(length);
/* 162 */         out.ensureWritable(outputOffset + 4 + maxOutputLength);
/* 163 */         byte[] output = out.array();
/* 164 */         int outputPtr = out.arrayOffset() + outputOffset + 4;
/* 165 */         int compressedLength = FastLz.compress(input, inputPtr, length, output, outputPtr, this.level);
/* 166 */         if (compressedLength < length) {
/* 167 */           byte blockType = 1;
/* 168 */           int chunkLength = compressedLength;
/*     */           
/* 170 */           out.setShort(outputOffset, chunkLength);
/* 171 */           outputOffset += 2;
/*     */         } else {
/* 173 */           blockType = 0;
/* 174 */           System.arraycopy(input, inputPtr, output, outputPtr - 2, length);
/* 175 */           chunkLength = length;
/*     */         }
/*     */       }
/* 178 */       out.setShort(outputOffset, length);
/*     */       
/* 180 */       out.setByte(outputIdx + 3, blockType | (checksum != null ? 16 : 0));
/*     */       
/* 182 */       out.writerIndex(outputOffset + 2 + chunkLength);
/* 183 */       in.skipBytes(length);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\FastLzFrameEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */