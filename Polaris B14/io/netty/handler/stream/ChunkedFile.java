/*     */ package io.netty.handler.stream;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
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
/*     */ public class ChunkedFile
/*     */   implements ChunkedInput<ByteBuf>
/*     */ {
/*     */   private final RandomAccessFile file;
/*     */   private final long startOffset;
/*     */   private final long endOffset;
/*     */   private final int chunkSize;
/*     */   private long offset;
/*     */   
/*     */   public ChunkedFile(File file)
/*     */     throws IOException
/*     */   {
/*  45 */     this(file, 8192);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChunkedFile(File file, int chunkSize)
/*     */     throws IOException
/*     */   {
/*  55 */     this(new RandomAccessFile(file, "r"), chunkSize);
/*     */   }
/*     */   
/*     */ 
/*     */   public ChunkedFile(RandomAccessFile file)
/*     */     throws IOException
/*     */   {
/*  62 */     this(file, 8192);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChunkedFile(RandomAccessFile file, int chunkSize)
/*     */     throws IOException
/*     */   {
/*  72 */     this(file, 0L, file.length(), chunkSize);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChunkedFile(RandomAccessFile file, long offset, long length, int chunkSize)
/*     */     throws IOException
/*     */   {
/*  84 */     if (file == null) {
/*  85 */       throw new NullPointerException("file");
/*     */     }
/*  87 */     if (offset < 0L) {
/*  88 */       throw new IllegalArgumentException("offset: " + offset + " (expected: 0 or greater)");
/*     */     }
/*     */     
/*  91 */     if (length < 0L) {
/*  92 */       throw new IllegalArgumentException("length: " + length + " (expected: 0 or greater)");
/*     */     }
/*     */     
/*  95 */     if (chunkSize <= 0) {
/*  96 */       throw new IllegalArgumentException("chunkSize: " + chunkSize + " (expected: a positive integer)");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 101 */     this.file = file;
/* 102 */     this.offset = (this.startOffset = offset);
/* 103 */     this.endOffset = (offset + length);
/* 104 */     this.chunkSize = chunkSize;
/*     */     
/* 106 */     file.seek(offset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long startOffset()
/*     */   {
/* 113 */     return this.startOffset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long endOffset()
/*     */   {
/* 120 */     return this.endOffset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long currentOffset()
/*     */   {
/* 127 */     return this.offset;
/*     */   }
/*     */   
/*     */   public boolean isEndOfInput() throws Exception
/*     */   {
/* 132 */     return (this.offset >= this.endOffset) || (!this.file.getChannel().isOpen());
/*     */   }
/*     */   
/*     */   public void close() throws Exception
/*     */   {
/* 137 */     this.file.close();
/*     */   }
/*     */   
/*     */   public ByteBuf readChunk(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 142 */     long offset = this.offset;
/* 143 */     if (offset >= this.endOffset) {
/* 144 */       return null;
/*     */     }
/*     */     
/* 147 */     int chunkSize = (int)Math.min(this.chunkSize, this.endOffset - offset);
/*     */     
/*     */ 
/* 150 */     ByteBuf buf = ctx.alloc().heapBuffer(chunkSize);
/* 151 */     boolean release = true;
/*     */     try {
/* 153 */       this.file.readFully(buf.array(), buf.arrayOffset(), chunkSize);
/* 154 */       buf.writerIndex(chunkSize);
/* 155 */       this.offset = (offset + chunkSize);
/* 156 */       release = false;
/* 157 */       return buf;
/*     */     } finally {
/* 159 */       if (release) {
/* 160 */         buf.release();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public long length()
/*     */   {
/* 167 */     return this.endOffset - this.startOffset;
/*     */   }
/*     */   
/*     */   public long progress()
/*     */   {
/* 172 */     return this.offset - this.startOffset;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\stream\ChunkedFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */