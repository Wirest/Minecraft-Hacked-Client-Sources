/*     */ package io.netty.handler.stream;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import java.io.InputStream;
/*     */ import java.io.PushbackInputStream;
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
/*     */ public class ChunkedStream
/*     */   implements ChunkedInput<ByteBuf>
/*     */ {
/*     */   static final int DEFAULT_CHUNK_SIZE = 8192;
/*     */   private final PushbackInputStream in;
/*     */   private final int chunkSize;
/*     */   private long offset;
/*     */   
/*     */   public ChunkedStream(InputStream in)
/*     */   {
/*  46 */     this(in, 8192);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChunkedStream(InputStream in, int chunkSize)
/*     */   {
/*  56 */     if (in == null) {
/*  57 */       throw new NullPointerException("in");
/*     */     }
/*  59 */     if (chunkSize <= 0) {
/*  60 */       throw new IllegalArgumentException("chunkSize: " + chunkSize + " (expected: a positive integer)");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  65 */     if ((in instanceof PushbackInputStream)) {
/*  66 */       this.in = ((PushbackInputStream)in);
/*     */     } else {
/*  68 */       this.in = new PushbackInputStream(in);
/*     */     }
/*  70 */     this.chunkSize = chunkSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long transferredBytes()
/*     */   {
/*  77 */     return this.offset;
/*     */   }
/*     */   
/*     */   public boolean isEndOfInput() throws Exception
/*     */   {
/*  82 */     int b = this.in.read();
/*  83 */     if (b < 0) {
/*  84 */       return true;
/*     */     }
/*  86 */     this.in.unread(b);
/*  87 */     return false;
/*     */   }
/*     */   
/*     */   public void close()
/*     */     throws Exception
/*     */   {
/*  93 */     this.in.close();
/*     */   }
/*     */   
/*     */   public ByteBuf readChunk(ChannelHandlerContext ctx) throws Exception
/*     */   {
/*  98 */     if (isEndOfInput()) {
/*  99 */       return null;
/*     */     }
/*     */     
/* 102 */     int availableBytes = this.in.available();
/*     */     int chunkSize;
/* 104 */     int chunkSize; if (availableBytes <= 0) {
/* 105 */       chunkSize = this.chunkSize;
/*     */     } else {
/* 107 */       chunkSize = Math.min(this.chunkSize, this.in.available());
/*     */     }
/*     */     
/* 110 */     boolean release = true;
/* 111 */     ByteBuf buffer = ctx.alloc().buffer(chunkSize);
/*     */     try
/*     */     {
/* 114 */       this.offset += buffer.writeBytes(this.in, chunkSize);
/* 115 */       release = false;
/* 116 */       return buffer;
/*     */     } finally {
/* 118 */       if (release) {
/* 119 */         buffer.release();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public long length()
/*     */   {
/* 126 */     return -1L;
/*     */   }
/*     */   
/*     */   public long progress()
/*     */   {
/* 131 */     return this.offset;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\stream\ChunkedStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */