/*     */ package io.netty.handler.stream;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ReadableByteChannel;
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
/*     */ public class ChunkedNioStream
/*     */   implements ChunkedInput<ByteBuf>
/*     */ {
/*     */   private final ReadableByteChannel in;
/*     */   private final int chunkSize;
/*     */   private long offset;
/*     */   private final ByteBuffer byteBuffer;
/*     */   
/*     */   public ChunkedNioStream(ReadableByteChannel in)
/*     */   {
/*  45 */     this(in, 8192);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChunkedNioStream(ReadableByteChannel in, int chunkSize)
/*     */   {
/*  55 */     if (in == null) {
/*  56 */       throw new NullPointerException("in");
/*     */     }
/*  58 */     if (chunkSize <= 0) {
/*  59 */       throw new IllegalArgumentException("chunkSize: " + chunkSize + " (expected: a positive integer)");
/*     */     }
/*     */     
/*  62 */     this.in = in;
/*  63 */     this.offset = 0L;
/*  64 */     this.chunkSize = chunkSize;
/*  65 */     this.byteBuffer = ByteBuffer.allocate(chunkSize);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long transferredBytes()
/*     */   {
/*  72 */     return this.offset;
/*     */   }
/*     */   
/*     */   public boolean isEndOfInput() throws Exception
/*     */   {
/*  77 */     if (this.byteBuffer.position() > 0)
/*     */     {
/*  79 */       return false;
/*     */     }
/*  81 */     if (this.in.isOpen())
/*     */     {
/*  83 */       int b = this.in.read(this.byteBuffer);
/*  84 */       if (b < 0) {
/*  85 */         return true;
/*     */       }
/*  87 */       this.offset += b;
/*  88 */       return false;
/*     */     }
/*     */     
/*  91 */     return true;
/*     */   }
/*     */   
/*     */   public void close() throws Exception
/*     */   {
/*  96 */     this.in.close();
/*     */   }
/*     */   
/*     */   public ByteBuf readChunk(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 101 */     if (isEndOfInput()) {
/* 102 */       return null;
/*     */     }
/*     */     
/* 105 */     int readBytes = this.byteBuffer.position();
/*     */     for (;;) {
/* 107 */       int localReadBytes = this.in.read(this.byteBuffer);
/* 108 */       if (localReadBytes < 0) {
/*     */         break;
/*     */       }
/* 111 */       readBytes += localReadBytes;
/* 112 */       this.offset += localReadBytes;
/* 113 */       if (readBytes == this.chunkSize) {
/*     */         break;
/*     */       }
/*     */     }
/* 117 */     this.byteBuffer.flip();
/* 118 */     boolean release = true;
/* 119 */     ByteBuf buffer = ctx.alloc().buffer(this.byteBuffer.remaining());
/*     */     try {
/* 121 */       buffer.writeBytes(this.byteBuffer);
/* 122 */       this.byteBuffer.clear();
/* 123 */       release = false;
/* 124 */       return buffer;
/*     */     } finally {
/* 126 */       if (release) {
/* 127 */         buffer.release();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public long length()
/*     */   {
/* 134 */     return -1L;
/*     */   }
/*     */   
/*     */   public long progress()
/*     */   {
/* 139 */     return this.offset;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\stream\ChunkedNioStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */