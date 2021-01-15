/*     */ package io.netty.handler.codec.spdy;
/*     */ 
/*     */ import com.jcraft.jzlib.Deflater;
/*     */ import com.jcraft.jzlib.JZlib;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.handler.codec.compression.CompressionException;
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
/*     */ class SpdyHeaderBlockJZlibEncoder
/*     */   extends SpdyHeaderBlockRawEncoder
/*     */ {
/*  29 */   private final Deflater z = new Deflater();
/*     */   
/*     */   private boolean finished;
/*     */   
/*     */   SpdyHeaderBlockJZlibEncoder(SpdyVersion version, int compressionLevel, int windowBits, int memLevel)
/*     */   {
/*  35 */     super(version);
/*  36 */     if ((compressionLevel < 0) || (compressionLevel > 9)) {
/*  37 */       throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
/*     */     }
/*     */     
/*  40 */     if ((windowBits < 9) || (windowBits > 15)) {
/*  41 */       throw new IllegalArgumentException("windowBits: " + windowBits + " (expected: 9-15)");
/*     */     }
/*     */     
/*  44 */     if ((memLevel < 1) || (memLevel > 9)) {
/*  45 */       throw new IllegalArgumentException("memLevel: " + memLevel + " (expected: 1-9)");
/*     */     }
/*     */     
/*     */ 
/*  49 */     int resultCode = this.z.deflateInit(compressionLevel, windowBits, memLevel, JZlib.W_ZLIB);
/*     */     
/*  51 */     if (resultCode != 0) {
/*  52 */       throw new CompressionException("failed to initialize an SPDY header block deflater: " + resultCode);
/*     */     }
/*     */     
/*  55 */     resultCode = this.z.deflateSetDictionary(SpdyCodecUtil.SPDY_DICT, SpdyCodecUtil.SPDY_DICT.length);
/*  56 */     if (resultCode != 0) {
/*  57 */       throw new CompressionException("failed to set the SPDY dictionary: " + resultCode);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void setInput(ByteBuf decompressed)
/*     */   {
/*  64 */     int len = decompressed.readableBytes();
/*     */     int offset;
/*     */     byte[] in;
/*     */     int offset;
/*  68 */     if (decompressed.hasArray()) {
/*  69 */       byte[] in = decompressed.array();
/*  70 */       offset = decompressed.arrayOffset() + decompressed.readerIndex();
/*     */     } else {
/*  72 */       in = new byte[len];
/*  73 */       decompressed.getBytes(decompressed.readerIndex(), in);
/*  74 */       offset = 0;
/*     */     }
/*  76 */     this.z.next_in = in;
/*  77 */     this.z.next_in_index = offset;
/*  78 */     this.z.avail_in = len;
/*     */   }
/*     */   
/*     */   private ByteBuf encode(ByteBufAllocator alloc) {
/*  82 */     boolean release = true;
/*  83 */     ByteBuf out = null;
/*     */     try {
/*  85 */       int oldNextInIndex = this.z.next_in_index;
/*  86 */       int oldNextOutIndex = this.z.next_out_index;
/*     */       
/*  88 */       int maxOutputLength = (int)Math.ceil(this.z.next_in.length * 1.001D) + 12;
/*  89 */       out = alloc.heapBuffer(maxOutputLength);
/*  90 */       this.z.next_out = out.array();
/*  91 */       this.z.next_out_index = (out.arrayOffset() + out.writerIndex());
/*  92 */       this.z.avail_out = maxOutputLength;
/*     */       int resultCode;
/*     */       try
/*     */       {
/*  96 */         resultCode = this.z.deflate(2);
/*     */       } finally {
/*  98 */         out.skipBytes(this.z.next_in_index - oldNextInIndex);
/*     */       }
/* 100 */       if (resultCode != 0) {
/* 101 */         throw new CompressionException("compression failure: " + resultCode);
/*     */       }
/*     */       
/* 104 */       int outputLength = this.z.next_out_index - oldNextOutIndex;
/* 105 */       if (outputLength > 0) {
/* 106 */         out.writerIndex(out.writerIndex() + outputLength);
/*     */       }
/* 108 */       release = false;
/* 109 */       return out;
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/*     */ 
/* 115 */       this.z.next_in = null;
/* 116 */       this.z.next_out = null;
/* 117 */       if ((release) && (out != null)) {
/* 118 */         out.release();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public ByteBuf encode(ByteBufAllocator alloc, SpdyHeadersFrame frame) throws Exception
/*     */   {
/* 125 */     if (frame == null) {
/* 126 */       throw new IllegalArgumentException("frame");
/*     */     }
/*     */     
/* 129 */     if (this.finished) {
/* 130 */       return Unpooled.EMPTY_BUFFER;
/*     */     }
/*     */     
/* 133 */     ByteBuf decompressed = super.encode(alloc, frame);
/*     */     try { ByteBuf localByteBuf1;
/* 135 */       if (!decompressed.isReadable()) {
/* 136 */         return Unpooled.EMPTY_BUFFER;
/*     */       }
/*     */       
/* 139 */       setInput(decompressed);
/* 140 */       return encode(alloc);
/*     */     } finally {
/* 142 */       decompressed.release();
/*     */     }
/*     */   }
/*     */   
/*     */   public void end()
/*     */   {
/* 148 */     if (this.finished) {
/* 149 */       return;
/*     */     }
/* 151 */     this.finished = true;
/* 152 */     this.z.deflateEnd();
/* 153 */     this.z.next_in = null;
/* 154 */     this.z.next_out = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\SpdyHeaderBlockJZlibEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */