/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.util.Recycler;
/*     */ import io.netty.util.Recycler.Handle;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.Promise;
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
/*     */ public final class PendingWriteQueue
/*     */ {
/*  29 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(PendingWriteQueue.class);
/*     */   
/*     */   private final ChannelHandlerContext ctx;
/*     */   
/*     */   private final ChannelOutboundBuffer buffer;
/*     */   private final MessageSizeEstimator.Handle estimatorHandle;
/*     */   private PendingWrite head;
/*     */   private PendingWrite tail;
/*     */   private int size;
/*     */   
/*     */   public PendingWriteQueue(ChannelHandlerContext ctx)
/*     */   {
/*  41 */     if (ctx == null) {
/*  42 */       throw new NullPointerException("ctx");
/*     */     }
/*  44 */     this.ctx = ctx;
/*  45 */     this.buffer = ctx.channel().unsafe().outboundBuffer();
/*  46 */     this.estimatorHandle = ctx.channel().config().getMessageSizeEstimator().newHandle();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/*  53 */     assert (this.ctx.executor().inEventLoop());
/*  54 */     return this.head == null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/*  61 */     assert (this.ctx.executor().inEventLoop());
/*  62 */     return this.size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void add(Object msg, ChannelPromise promise)
/*     */   {
/*  69 */     assert (this.ctx.executor().inEventLoop());
/*  70 */     if (msg == null) {
/*  71 */       throw new NullPointerException("msg");
/*     */     }
/*  73 */     if (promise == null) {
/*  74 */       throw new NullPointerException("promise");
/*     */     }
/*  76 */     int messageSize = this.estimatorHandle.size(msg);
/*  77 */     if (messageSize < 0)
/*     */     {
/*  79 */       messageSize = 0;
/*     */     }
/*  81 */     PendingWrite write = PendingWrite.newInstance(msg, messageSize, promise);
/*  82 */     PendingWrite currentTail = this.tail;
/*  83 */     if (currentTail == null) {
/*  84 */       this.tail = (this.head = write);
/*     */     } else {
/*  86 */       currentTail.next = write;
/*  87 */       this.tail = write;
/*     */     }
/*  89 */     this.size += 1;
/*  90 */     this.buffer.incrementPendingOutboundBytes(write.size);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeAndFailAll(Throwable cause)
/*     */   {
/*  98 */     assert (this.ctx.executor().inEventLoop());
/*  99 */     if (cause == null) {
/* 100 */       throw new NullPointerException("cause");
/*     */     }
/*     */     
/* 103 */     PendingWrite write = this.head;
/* 104 */     this.head = (this.tail = null);
/* 105 */     this.size = 0;
/*     */     
/* 107 */     while (write != null) {
/* 108 */       PendingWrite next = write.next;
/* 109 */       ReferenceCountUtil.safeRelease(write.msg);
/* 110 */       ChannelPromise promise = write.promise;
/* 111 */       recycle(write, false);
/* 112 */       safeFail(promise, cause);
/* 113 */       write = next;
/*     */     }
/* 115 */     assertEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeAndFail(Throwable cause)
/*     */   {
/* 123 */     assert (this.ctx.executor().inEventLoop());
/* 124 */     if (cause == null) {
/* 125 */       throw new NullPointerException("cause");
/*     */     }
/* 127 */     PendingWrite write = this.head;
/*     */     
/* 129 */     if (write == null) {
/* 130 */       return;
/*     */     }
/* 132 */     ReferenceCountUtil.safeRelease(write.msg);
/* 133 */     ChannelPromise promise = write.promise;
/* 134 */     safeFail(promise, cause);
/* 135 */     recycle(write, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelFuture removeAndWriteAll()
/*     */   {
/* 146 */     assert (this.ctx.executor().inEventLoop());
/*     */     
/* 148 */     if (this.size == 1)
/*     */     {
/* 150 */       return removeAndWrite();
/*     */     }
/* 152 */     PendingWrite write = this.head;
/* 153 */     if (write == null)
/*     */     {
/* 155 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 159 */     this.head = (this.tail = null);
/* 160 */     this.size = 0;
/*     */     
/* 162 */     ChannelPromise p = this.ctx.newPromise();
/* 163 */     ChannelPromiseAggregator aggregator = new ChannelPromiseAggregator(p);
/* 164 */     while (write != null) {
/* 165 */       PendingWrite next = write.next;
/* 166 */       Object msg = write.msg;
/* 167 */       ChannelPromise promise = write.promise;
/* 168 */       recycle(write, false);
/* 169 */       this.ctx.write(msg, promise);
/* 170 */       aggregator.add(new Promise[] { promise });
/* 171 */       write = next;
/*     */     }
/* 173 */     assertEmpty();
/* 174 */     return p;
/*     */   }
/*     */   
/*     */   private void assertEmpty() {
/* 178 */     assert ((this.tail == null) && (this.head == null) && (this.size == 0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelFuture removeAndWrite()
/*     */   {
/* 189 */     assert (this.ctx.executor().inEventLoop());
/* 190 */     PendingWrite write = this.head;
/* 191 */     if (write == null) {
/* 192 */       return null;
/*     */     }
/* 194 */     Object msg = write.msg;
/* 195 */     ChannelPromise promise = write.promise;
/* 196 */     recycle(write, true);
/* 197 */     return this.ctx.write(msg, promise);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelPromise remove()
/*     */   {
/* 207 */     assert (this.ctx.executor().inEventLoop());
/* 208 */     PendingWrite write = this.head;
/* 209 */     if (write == null) {
/* 210 */       return null;
/*     */     }
/* 212 */     ChannelPromise promise = write.promise;
/* 213 */     ReferenceCountUtil.safeRelease(write.msg);
/* 214 */     recycle(write, true);
/* 215 */     return promise;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object current()
/*     */   {
/* 222 */     assert (this.ctx.executor().inEventLoop());
/* 223 */     PendingWrite write = this.head;
/* 224 */     if (write == null) {
/* 225 */       return null;
/*     */     }
/* 227 */     return write.msg;
/*     */   }
/*     */   
/*     */   private void recycle(PendingWrite write, boolean update) {
/* 231 */     PendingWrite next = write.next;
/* 232 */     long writeSize = write.size;
/*     */     
/* 234 */     if (update) {
/* 235 */       if (next == null)
/*     */       {
/*     */ 
/* 238 */         this.head = (this.tail = null);
/* 239 */         this.size = 0;
/*     */       } else {
/* 241 */         this.head = next;
/* 242 */         this.size -= 1;
/* 243 */         assert (this.size > 0);
/*     */       }
/*     */     }
/*     */     
/* 247 */     write.recycle();
/* 248 */     this.buffer.decrementPendingOutboundBytes(writeSize);
/*     */   }
/*     */   
/*     */   private static void safeFail(ChannelPromise promise, Throwable cause) {
/* 252 */     if ((!(promise instanceof VoidChannelPromise)) && (!promise.tryFailure(cause))) {
/* 253 */       logger.warn("Failed to mark a promise as failure because it's done already: {}", promise, cause);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static final class PendingWrite
/*     */   {
/* 261 */     private static final Recycler<PendingWrite> RECYCLER = new Recycler()
/*     */     {
/*     */       protected PendingWriteQueue.PendingWrite newObject(Recycler.Handle handle) {
/* 264 */         return new PendingWriteQueue.PendingWrite(handle, null);
/*     */       }
/*     */     };
/*     */     private final Recycler.Handle handle;
/*     */     private PendingWrite next;
/*     */     private long size;
/*     */     private ChannelPromise promise;
/*     */     private Object msg;
/*     */     
/*     */     private PendingWrite(Recycler.Handle handle)
/*     */     {
/* 275 */       this.handle = handle;
/*     */     }
/*     */     
/*     */     static PendingWrite newInstance(Object msg, int size, ChannelPromise promise) {
/* 279 */       PendingWrite write = (PendingWrite)RECYCLER.get();
/* 280 */       write.size = size;
/* 281 */       write.msg = msg;
/* 282 */       write.promise = promise;
/* 283 */       return write;
/*     */     }
/*     */     
/*     */     private void recycle() {
/* 287 */       this.size = 0L;
/* 288 */       this.next = null;
/* 289 */       this.msg = null;
/* 290 */       this.promise = null;
/* 291 */       RECYCLER.recycle(this, this.handle);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\PendingWriteQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */