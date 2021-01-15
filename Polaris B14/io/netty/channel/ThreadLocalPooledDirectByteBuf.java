/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.UnpooledByteBufAllocator;
/*     */ import io.netty.buffer.UnpooledDirectByteBuf;
/*     */ import io.netty.buffer.UnpooledUnsafeDirectByteBuf;
/*     */ import io.netty.util.Recycler;
/*     */ import io.netty.util.Recycler.Handle;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
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
/*     */ final class ThreadLocalPooledDirectByteBuf
/*     */ {
/*  33 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ThreadLocalPooledDirectByteBuf.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  38 */   public static final int threadLocalDirectBufferSize = SystemPropertyUtil.getInt("io.netty.threadLocalDirectBufferSize", 65536);
/*  39 */   static { logger.debug("-Dio.netty.threadLocalDirectBufferSize: {}", Integer.valueOf(threadLocalDirectBufferSize)); }
/*     */   
/*     */   public static ByteBuf newInstance()
/*     */   {
/*  43 */     if (PlatformDependent.hasUnsafe()) {
/*  44 */       return ThreadLocalUnsafeDirectByteBuf.newInstance();
/*     */     }
/*  46 */     return ThreadLocalDirectByteBuf.newInstance();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static final class ThreadLocalUnsafeDirectByteBuf
/*     */     extends UnpooledUnsafeDirectByteBuf
/*     */   {
/*  56 */     private static final Recycler<ThreadLocalUnsafeDirectByteBuf> RECYCLER = new Recycler()
/*     */     {
/*     */ 
/*     */       protected ThreadLocalPooledDirectByteBuf.ThreadLocalUnsafeDirectByteBuf newObject(Recycler.Handle<ThreadLocalPooledDirectByteBuf.ThreadLocalUnsafeDirectByteBuf> handle) {
/*  60 */         return new ThreadLocalPooledDirectByteBuf.ThreadLocalUnsafeDirectByteBuf(handle, null); }
/*     */     };
/*     */     private final Recycler.Handle<ThreadLocalUnsafeDirectByteBuf> handle;
/*     */     
/*     */     static ThreadLocalUnsafeDirectByteBuf newInstance() {
/*  65 */       ThreadLocalUnsafeDirectByteBuf buf = (ThreadLocalUnsafeDirectByteBuf)RECYCLER.get();
/*  66 */       buf.setRefCnt(1);
/*  67 */       return buf;
/*     */     }
/*     */     
/*     */ 
/*     */     private ThreadLocalUnsafeDirectByteBuf(Recycler.Handle<ThreadLocalUnsafeDirectByteBuf> handle)
/*     */     {
/*  73 */       super(256, Integer.MAX_VALUE);
/*  74 */       this.handle = handle;
/*     */     }
/*     */     
/*     */     protected void deallocate()
/*     */     {
/*  79 */       if (capacity() > ThreadLocalPooledDirectByteBuf.threadLocalDirectBufferSize) {
/*  80 */         super.deallocate();
/*     */       } else {
/*  82 */         clear();
/*  83 */         RECYCLER.recycle(this, this.handle);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static final class ThreadLocalDirectByteBuf extends UnpooledDirectByteBuf
/*     */   {
/*  90 */     private static final Recycler<ThreadLocalDirectByteBuf> RECYCLER = new Recycler()
/*     */     {
/*     */ 
/*  93 */       protected ThreadLocalPooledDirectByteBuf.ThreadLocalDirectByteBuf newObject(Recycler.Handle<ThreadLocalPooledDirectByteBuf.ThreadLocalDirectByteBuf> handle) { return new ThreadLocalPooledDirectByteBuf.ThreadLocalDirectByteBuf(handle, null); }
/*     */     };
/*     */     private final Recycler.Handle<ThreadLocalDirectByteBuf> handle;
/*     */     
/*     */     static ThreadLocalDirectByteBuf newInstance() {
/*  98 */       ThreadLocalDirectByteBuf buf = (ThreadLocalDirectByteBuf)RECYCLER.get();
/*  99 */       buf.setRefCnt(1);
/* 100 */       return buf;
/*     */     }
/*     */     
/*     */ 
/*     */     private ThreadLocalDirectByteBuf(Recycler.Handle<ThreadLocalDirectByteBuf> handle)
/*     */     {
/* 106 */       super(256, Integer.MAX_VALUE);
/* 107 */       this.handle = handle;
/*     */     }
/*     */     
/*     */     protected void deallocate()
/*     */     {
/* 112 */       if (capacity() > ThreadLocalPooledDirectByteBuf.threadLocalDirectBufferSize) {
/* 113 */         super.deallocate();
/*     */       } else {
/* 115 */         clear();
/* 116 */         RECYCLER.recycle(this, this.handle);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\ThreadLocalPooledDirectByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */