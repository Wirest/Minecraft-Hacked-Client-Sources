/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.util.AbstractReferenceCounted;
/*     */ import io.netty.util.IllegalReferenceCountException;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.WritableByteChannel;
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
/*     */ public class DefaultFileRegion
/*     */   extends AbstractReferenceCounted
/*     */   implements FileRegion
/*     */ {
/*  37 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultFileRegion.class);
/*     */   
/*     */   private final File f;
/*     */   
/*     */   private final long position;
/*     */   
/*     */   private final long count;
/*     */   
/*     */   private long transfered;
/*     */   
/*     */   private FileChannel file;
/*     */   
/*     */ 
/*     */   public DefaultFileRegion(FileChannel file, long position, long count)
/*     */   {
/*  52 */     if (file == null) {
/*  53 */       throw new NullPointerException("file");
/*     */     }
/*  55 */     if (position < 0L) {
/*  56 */       throw new IllegalArgumentException("position must be >= 0 but was " + position);
/*     */     }
/*  58 */     if (count < 0L) {
/*  59 */       throw new IllegalArgumentException("count must be >= 0 but was " + count);
/*     */     }
/*  61 */     this.file = file;
/*  62 */     this.position = position;
/*  63 */     this.count = count;
/*  64 */     this.f = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultFileRegion(File f, long position, long count)
/*     */   {
/*  76 */     if (f == null) {
/*  77 */       throw new NullPointerException("f");
/*     */     }
/*  79 */     if (position < 0L) {
/*  80 */       throw new IllegalArgumentException("position must be >= 0 but was " + position);
/*     */     }
/*  82 */     if (count < 0L) {
/*  83 */       throw new IllegalArgumentException("count must be >= 0 but was " + count);
/*     */     }
/*  85 */     this.position = position;
/*  86 */     this.count = count;
/*  87 */     this.f = f;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isOpen()
/*     */   {
/*  94 */     return this.file != null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void open()
/*     */     throws IOException
/*     */   {
/* 101 */     if ((!isOpen()) && (refCnt() > 0))
/*     */     {
/* 103 */       this.file = new RandomAccessFile(this.f, "r").getChannel();
/*     */     }
/*     */   }
/*     */   
/*     */   public long position()
/*     */   {
/* 109 */     return this.position;
/*     */   }
/*     */   
/*     */   public long count()
/*     */   {
/* 114 */     return this.count;
/*     */   }
/*     */   
/*     */   public long transfered()
/*     */   {
/* 119 */     return this.transfered;
/*     */   }
/*     */   
/*     */   public long transferTo(WritableByteChannel target, long position) throws IOException
/*     */   {
/* 124 */     long count = this.count - position;
/* 125 */     if ((count < 0L) || (position < 0L)) {
/* 126 */       throw new IllegalArgumentException("position out of range: " + position + " (expected: 0 - " + (this.count - 1L) + ')');
/*     */     }
/*     */     
/*     */ 
/* 130 */     if (count == 0L) {
/* 131 */       return 0L;
/*     */     }
/* 133 */     if (refCnt() == 0) {
/* 134 */       throw new IllegalReferenceCountException(0);
/*     */     }
/*     */     
/* 137 */     open();
/*     */     
/* 139 */     long written = this.file.transferTo(this.position + position, count, target);
/* 140 */     if (written > 0L) {
/* 141 */       this.transfered += written;
/*     */     }
/* 143 */     return written;
/*     */   }
/*     */   
/*     */   protected void deallocate()
/*     */   {
/* 148 */     FileChannel file = this.file;
/*     */     
/* 150 */     if (file == null) {
/* 151 */       return;
/*     */     }
/* 153 */     this.file = null;
/*     */     try
/*     */     {
/* 156 */       file.close();
/*     */     } catch (IOException e) {
/* 158 */       if (logger.isWarnEnabled()) {
/* 159 */         logger.warn("Failed to close a file.", e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public FileRegion retain()
/*     */   {
/* 166 */     super.retain();
/* 167 */     return this;
/*     */   }
/*     */   
/*     */   public FileRegion retain(int increment)
/*     */   {
/* 172 */     super.retain(increment);
/* 173 */     return this;
/*     */   }
/*     */   
/*     */   public FileRegion touch()
/*     */   {
/* 178 */     return this;
/*     */   }
/*     */   
/*     */   public FileRegion touch(Object hint)
/*     */   {
/* 183 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\DefaultFileRegion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */