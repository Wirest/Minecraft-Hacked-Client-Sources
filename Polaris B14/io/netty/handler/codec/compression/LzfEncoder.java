/*     */ package io.netty.handler.codec.compression;
/*     */ 
/*     */ import com.ning.compress.BufferRecycler;
/*     */ import com.ning.compress.lzf.ChunkEncoder;
/*     */ import com.ning.compress.lzf.LZFEncoder;
/*     */ import com.ning.compress.lzf.util.ChunkEncoderFactory;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.MessageToByteEncoder;
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
/*     */ public class LzfEncoder
/*     */   extends MessageToByteEncoder<ByteBuf>
/*     */ {
/*     */   private static final int MIN_BLOCK_TO_COMPRESS = 16;
/*     */   private final ChunkEncoder encoder;
/*     */   private final BufferRecycler recycler;
/*     */   
/*     */   public LzfEncoder()
/*     */   {
/*  58 */     this(false, 65535);
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
/*     */   public LzfEncoder(boolean safeInstance)
/*     */   {
/*  71 */     this(safeInstance, 65535);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LzfEncoder(int totalLength)
/*     */   {
/*  83 */     this(false, totalLength);
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
/*     */   public LzfEncoder(boolean safeInstance, int totalLength)
/*     */   {
/*  99 */     super(false);
/* 100 */     if ((totalLength < 16) || (totalLength > 65535)) {
/* 101 */       throw new IllegalArgumentException("totalLength: " + totalLength + " (expected: " + 16 + '-' + 65535 + ')');
/*     */     }
/*     */     
/*     */ 
/* 105 */     this.encoder = (safeInstance ? ChunkEncoderFactory.safeNonAllocatingInstance(totalLength) : ChunkEncoderFactory.optimalNonAllocatingInstance(totalLength));
/*     */     
/*     */ 
/*     */ 
/* 109 */     this.recycler = BufferRecycler.instance();
/*     */   }
/*     */   
/*     */   protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception
/*     */   {
/* 114 */     int length = in.readableBytes();
/* 115 */     int idx = in.readerIndex();
/*     */     int inputPtr;
/*     */     byte[] input;
/* 118 */     int inputPtr; if (in.hasArray()) {
/* 119 */       byte[] input = in.array();
/* 120 */       inputPtr = in.arrayOffset() + idx;
/*     */     } else {
/* 122 */       input = this.recycler.allocInputBuffer(length);
/* 123 */       in.getBytes(idx, input, 0, length);
/* 124 */       inputPtr = 0;
/*     */     }
/*     */     
/* 127 */     int maxOutputLength = LZFEncoder.estimateMaxWorkspaceSize(length);
/* 128 */     out.ensureWritable(maxOutputLength);
/* 129 */     byte[] output = out.array();
/* 130 */     int outputPtr = out.arrayOffset() + out.writerIndex();
/* 131 */     int outputLength = LZFEncoder.appendEncoded(this.encoder, input, inputPtr, length, output, outputPtr) - outputPtr;
/*     */     
/* 133 */     out.writerIndex(out.writerIndex() + outputLength);
/* 134 */     in.skipBytes(length);
/*     */     
/* 136 */     if (!in.hasArray()) {
/* 137 */       this.recycler.releaseInputBuffer(input);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\LzfEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */