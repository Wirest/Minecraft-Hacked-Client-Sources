/*     */ package io.netty.handler.codec.spdy;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import java.util.zip.Deflater;
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
/*     */ class SpdyHeaderBlockZlibEncoder
/*     */   extends SpdyHeaderBlockRawEncoder
/*     */ {
/*     */   private final Deflater compressor;
/*     */   private boolean finished;
/*     */   
/*     */   SpdyHeaderBlockZlibEncoder(SpdyVersion spdyVersion, int compressionLevel)
/*     */   {
/*  33 */     super(spdyVersion);
/*  34 */     if ((compressionLevel < 0) || (compressionLevel > 9)) {
/*  35 */       throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
/*     */     }
/*     */     
/*  38 */     this.compressor = new Deflater(compressionLevel);
/*  39 */     this.compressor.setDictionary(SpdyCodecUtil.SPDY_DICT);
/*     */   }
/*     */   
/*     */   private int setInput(ByteBuf decompressed) {
/*  43 */     int len = decompressed.readableBytes();
/*     */     
/*  45 */     if (decompressed.hasArray()) {
/*  46 */       this.compressor.setInput(decompressed.array(), decompressed.arrayOffset() + decompressed.readerIndex(), len);
/*     */     } else {
/*  48 */       byte[] in = new byte[len];
/*  49 */       decompressed.getBytes(decompressed.readerIndex(), in);
/*  50 */       this.compressor.setInput(in, 0, in.length);
/*     */     }
/*     */     
/*  53 */     return len;
/*     */   }
/*     */   
/*     */   private ByteBuf encode(ByteBufAllocator alloc, int len) {
/*  57 */     ByteBuf compressed = alloc.heapBuffer(len);
/*  58 */     boolean release = true;
/*     */     try {
/*  60 */       while (compressInto(compressed))
/*     */       {
/*  62 */         compressed.ensureWritable(compressed.capacity() << 1);
/*     */       }
/*  64 */       release = false;
/*  65 */       return compressed;
/*     */     } finally {
/*  67 */       if (release) {
/*  68 */         compressed.release();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean compressInto(ByteBuf compressed) {
/*  74 */     byte[] out = compressed.array();
/*  75 */     int off = compressed.arrayOffset() + compressed.writerIndex();
/*  76 */     int toWrite = compressed.writableBytes();
/*  77 */     int numBytes = this.compressor.deflate(out, off, toWrite, 2);
/*  78 */     compressed.writerIndex(compressed.writerIndex() + numBytes);
/*  79 */     return numBytes == toWrite;
/*     */   }
/*     */   
/*     */   public ByteBuf encode(ByteBufAllocator alloc, SpdyHeadersFrame frame) throws Exception
/*     */   {
/*  84 */     if (frame == null) {
/*  85 */       throw new IllegalArgumentException("frame");
/*     */     }
/*     */     
/*  88 */     if (this.finished) {
/*  89 */       return Unpooled.EMPTY_BUFFER;
/*     */     }
/*     */     
/*  92 */     ByteBuf decompressed = super.encode(alloc, frame);
/*     */     try {
/*  94 */       if (!decompressed.isReadable()) {
/*  95 */         return Unpooled.EMPTY_BUFFER;
/*     */       }
/*     */       
/*  98 */       int len = setInput(decompressed);
/*  99 */       return encode(alloc, len);
/*     */     } finally {
/* 101 */       decompressed.release();
/*     */     }
/*     */   }
/*     */   
/*     */   public void end()
/*     */   {
/* 107 */     if (this.finished) {
/* 108 */       return;
/*     */     }
/* 110 */     this.finished = true;
/* 111 */     this.compressor.end();
/* 112 */     super.end();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\SpdyHeaderBlockZlibEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */