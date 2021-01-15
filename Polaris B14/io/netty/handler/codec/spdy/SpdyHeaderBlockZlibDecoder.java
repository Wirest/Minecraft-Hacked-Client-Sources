/*     */ package io.netty.handler.codec.spdy;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import java.util.zip.DataFormatException;
/*     */ import java.util.zip.Inflater;
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
/*     */ final class SpdyHeaderBlockZlibDecoder
/*     */   extends SpdyHeaderBlockRawDecoder
/*     */ {
/*     */   private static final int DEFAULT_BUFFER_CAPACITY = 4096;
/*  29 */   private static final SpdyProtocolException INVALID_HEADER_BLOCK = new SpdyProtocolException("Invalid Header Block");
/*     */   
/*     */ 
/*  32 */   private final Inflater decompressor = new Inflater();
/*     */   private ByteBuf decompressed;
/*     */   
/*     */   SpdyHeaderBlockZlibDecoder(SpdyVersion spdyVersion, int maxHeaderSize)
/*     */   {
/*  37 */     super(spdyVersion, maxHeaderSize);
/*     */   }
/*     */   
/*     */   void decode(ByteBufAllocator alloc, ByteBuf headerBlock, SpdyHeadersFrame frame) throws Exception
/*     */   {
/*  42 */     int len = setInput(headerBlock);
/*     */     int numBytes;
/*     */     do
/*     */     {
/*  46 */       numBytes = decompress(alloc, frame);
/*  47 */     } while (numBytes > 0);
/*     */     
/*     */ 
/*     */ 
/*  51 */     if (this.decompressor.getRemaining() != 0)
/*     */     {
/*  53 */       throw INVALID_HEADER_BLOCK;
/*     */     }
/*     */     
/*  56 */     headerBlock.skipBytes(len);
/*     */   }
/*     */   
/*     */   private int setInput(ByteBuf compressed) {
/*  60 */     int len = compressed.readableBytes();
/*     */     
/*  62 */     if (compressed.hasArray()) {
/*  63 */       this.decompressor.setInput(compressed.array(), compressed.arrayOffset() + compressed.readerIndex(), len);
/*     */     } else {
/*  65 */       byte[] in = new byte[len];
/*  66 */       compressed.getBytes(compressed.readerIndex(), in);
/*  67 */       this.decompressor.setInput(in, 0, in.length);
/*     */     }
/*     */     
/*  70 */     return len;
/*     */   }
/*     */   
/*     */   private int decompress(ByteBufAllocator alloc, SpdyHeadersFrame frame) throws Exception {
/*  74 */     ensureBuffer(alloc);
/*  75 */     byte[] out = this.decompressed.array();
/*  76 */     int off = this.decompressed.arrayOffset() + this.decompressed.writerIndex();
/*     */     try {
/*  78 */       int numBytes = this.decompressor.inflate(out, off, this.decompressed.writableBytes());
/*  79 */       if ((numBytes == 0) && (this.decompressor.needsDictionary())) {
/*     */         try {
/*  81 */           this.decompressor.setDictionary(SpdyCodecUtil.SPDY_DICT);
/*     */         } catch (IllegalArgumentException ignored) {
/*  83 */           throw INVALID_HEADER_BLOCK;
/*     */         }
/*  85 */         numBytes = this.decompressor.inflate(out, off, this.decompressed.writableBytes());
/*     */       }
/*  87 */       if (frame != null) {
/*  88 */         this.decompressed.writerIndex(this.decompressed.writerIndex() + numBytes);
/*  89 */         decodeHeaderBlock(this.decompressed, frame);
/*  90 */         this.decompressed.discardReadBytes();
/*     */       }
/*     */       
/*  93 */       return numBytes;
/*     */     } catch (DataFormatException e) {
/*  95 */       throw new SpdyProtocolException("Received invalid header block", e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void ensureBuffer(ByteBufAllocator alloc) {
/* 100 */     if (this.decompressed == null) {
/* 101 */       this.decompressed = alloc.heapBuffer(4096);
/*     */     }
/* 103 */     this.decompressed.ensureWritable(1);
/*     */   }
/*     */   
/*     */   void endHeaderBlock(SpdyHeadersFrame frame) throws Exception
/*     */   {
/* 108 */     super.endHeaderBlock(frame);
/* 109 */     releaseBuffer();
/*     */   }
/*     */   
/*     */   public void end()
/*     */   {
/* 114 */     super.end();
/* 115 */     releaseBuffer();
/* 116 */     this.decompressor.end();
/*     */   }
/*     */   
/*     */   private void releaseBuffer() {
/* 120 */     if (this.decompressed != null) {
/* 121 */       this.decompressed.release();
/* 122 */       this.decompressed = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\SpdyHeaderBlockZlibDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */