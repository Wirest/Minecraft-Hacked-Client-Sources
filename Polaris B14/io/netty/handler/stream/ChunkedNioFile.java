/*     */ package io.netty.handler.stream;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.FileChannel;
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
/*     */ public class ChunkedNioFile
/*     */   implements ChunkedInput<ByteBuf>
/*     */ {
/*     */   private final FileChannel in;
/*     */   private final long startOffset;
/*     */   private final long endOffset;
/*     */   private final int chunkSize;
/*     */   private long offset;
/*     */   
/*     */   public ChunkedNioFile(File in)
/*     */     throws IOException
/*     */   {
/*  47 */     this(new FileInputStream(in).getChannel());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChunkedNioFile(File in, int chunkSize)
/*     */     throws IOException
/*     */   {
/*  57 */     this(new FileInputStream(in).getChannel(), chunkSize);
/*     */   }
/*     */   
/*     */ 
/*     */   public ChunkedNioFile(FileChannel in)
/*     */     throws IOException
/*     */   {
/*  64 */     this(in, 8192);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChunkedNioFile(FileChannel in, int chunkSize)
/*     */     throws IOException
/*     */   {
/*  74 */     this(in, 0L, in.size(), chunkSize);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChunkedNioFile(FileChannel in, long offset, long length, int chunkSize)
/*     */     throws IOException
/*     */   {
/*  87 */     if (in == null) {
/*  88 */       throw new NullPointerException("in");
/*     */     }
/*  90 */     if (offset < 0L) {
/*  91 */       throw new IllegalArgumentException("offset: " + offset + " (expected: 0 or greater)");
/*     */     }
/*     */     
/*  94 */     if (length < 0L) {
/*  95 */       throw new IllegalArgumentException("length: " + length + " (expected: 0 or greater)");
/*     */     }
/*     */     
/*  98 */     if (chunkSize <= 0) {
/*  99 */       throw new IllegalArgumentException("chunkSize: " + chunkSize + " (expected: a positive integer)");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 104 */     if (offset != 0L) {
/* 105 */       in.position(offset);
/*     */     }
/* 107 */     this.in = in;
/* 108 */     this.chunkSize = chunkSize;
/* 109 */     this.offset = (this.startOffset = offset);
/* 110 */     this.endOffset = (offset + length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long startOffset()
/*     */   {
/* 117 */     return this.startOffset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long endOffset()
/*     */   {
/* 124 */     return this.endOffset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long currentOffset()
/*     */   {
/* 131 */     return this.offset;
/*     */   }
/*     */   
/*     */   public boolean isEndOfInput() throws Exception
/*     */   {
/* 136 */     return (this.offset >= this.endOffset) || (!this.in.isOpen());
/*     */   }
/*     */   
/*     */   public void close() throws Exception
/*     */   {
/* 141 */     this.in.close();
/*     */   }
/*     */   
/*     */   public ByteBuf readChunk(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 146 */     long offset = this.offset;
/* 147 */     if (offset >= this.endOffset) {
/* 148 */       return null;
/*     */     }
/*     */     
/* 151 */     int chunkSize = (int)Math.min(this.chunkSize, this.endOffset - offset);
/* 152 */     ByteBuf buffer = ctx.alloc().buffer(chunkSize);
/* 153 */     boolean release = true;
/*     */     try {
/* 155 */       int readBytes = 0;
/*     */       int localReadBytes;
/* 157 */       for (;;) { localReadBytes = buffer.writeBytes(this.in, chunkSize - readBytes);
/* 158 */         if (localReadBytes < 0) {
/*     */           break;
/*     */         }
/* 161 */         readBytes += localReadBytes;
/* 162 */         if (readBytes == chunkSize) {
/*     */           break;
/*     */         }
/*     */       }
/* 166 */       this.offset += readBytes;
/* 167 */       release = false;
/* 168 */       return buffer;
/*     */     } finally {
/* 170 */       if (release) {
/* 171 */         buffer.release();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public long length()
/*     */   {
/* 178 */     return this.endOffset - this.startOffset;
/*     */   }
/*     */   
/*     */   public long progress()
/*     */   {
/* 183 */     return this.offset - this.startOffset;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\stream\ChunkedNioFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */