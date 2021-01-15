/*     */ package io.netty.channel;
/*     */ 
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Queue;
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
/*     */ public final class ChannelFlushPromiseNotifier
/*     */ {
/*     */   private long writeCounter;
/*  28 */   private final Queue<FlushCheckpoint> flushCheckpoints = new ArrayDeque();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final boolean tryNotify;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelFlushPromiseNotifier(boolean tryNotify)
/*     */   {
/*  40 */     this.tryNotify = tryNotify;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelFlushPromiseNotifier()
/*     */   {
/*  48 */     this(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelFlushPromiseNotifier add(ChannelPromise promise, long pendingDataSize)
/*     */   {
/*  56 */     if (promise == null) {
/*  57 */       throw new NullPointerException("promise");
/*     */     }
/*  59 */     if (pendingDataSize < 0L) {
/*  60 */       throw new IllegalArgumentException("pendingDataSize must be >= 0 but was " + pendingDataSize);
/*     */     }
/*  62 */     long checkpoint = this.writeCounter + pendingDataSize;
/*  63 */     if ((promise instanceof FlushCheckpoint)) {
/*  64 */       FlushCheckpoint cp = (FlushCheckpoint)promise;
/*  65 */       cp.flushCheckpoint(checkpoint);
/*  66 */       this.flushCheckpoints.add(cp);
/*     */     } else {
/*  68 */       this.flushCheckpoints.add(new DefaultFlushCheckpoint(checkpoint, promise));
/*     */     }
/*  70 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFlushPromiseNotifier increaseWriteCounter(long delta)
/*     */   {
/*  76 */     if (delta < 0L) {
/*  77 */       throw new IllegalArgumentException("delta must be >= 0 but was " + delta);
/*     */     }
/*  79 */     this.writeCounter += delta;
/*  80 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long writeCounter()
/*     */   {
/*  87 */     return this.writeCounter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelFlushPromiseNotifier notifyPromises()
/*     */   {
/*  98 */     notifyPromises0(null);
/*  99 */     return this;
/*     */   }
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
/*     */   public ChannelFlushPromiseNotifier notifyPromises(Throwable cause)
/*     */   {
/* 114 */     notifyPromises();
/*     */     for (;;) {
/* 116 */       FlushCheckpoint cp = (FlushCheckpoint)this.flushCheckpoints.poll();
/* 117 */       if (cp == null) {
/*     */         break;
/*     */       }
/* 120 */       if (this.tryNotify) {
/* 121 */         cp.promise().tryFailure(cause);
/*     */       } else {
/* 123 */         cp.promise().setFailure(cause);
/*     */       }
/*     */     }
/* 126 */     return this;
/*     */   }
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
/*     */   public ChannelFlushPromiseNotifier notifyPromises(Throwable cause1, Throwable cause2)
/*     */   {
/* 146 */     notifyPromises0(cause1);
/*     */     for (;;) {
/* 148 */       FlushCheckpoint cp = (FlushCheckpoint)this.flushCheckpoints.poll();
/* 149 */       if (cp == null) {
/*     */         break;
/*     */       }
/* 152 */       if (this.tryNotify) {
/* 153 */         cp.promise().tryFailure(cause2);
/*     */       } else {
/* 155 */         cp.promise().setFailure(cause2);
/*     */       }
/*     */     }
/* 158 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public ChannelFlushPromiseNotifier notifyFlushFutures(Throwable cause1, Throwable cause2)
/*     */   {
/* 166 */     return notifyPromises(cause1, cause2);
/*     */   }
/*     */   
/*     */   private void notifyPromises0(Throwable cause) {
/* 170 */     if (this.flushCheckpoints.isEmpty()) {
/* 171 */       this.writeCounter = 0L;
/* 172 */       return;
/*     */     }
/*     */     
/* 175 */     long writeCounter = this.writeCounter;
/*     */     for (;;) {
/* 177 */       FlushCheckpoint cp = (FlushCheckpoint)this.flushCheckpoints.peek();
/* 178 */       if (cp == null)
/*     */       {
/* 180 */         this.writeCounter = 0L;
/* 181 */         break;
/*     */       }
/*     */       
/* 184 */       if (cp.flushCheckpoint() > writeCounter) {
/* 185 */         if ((writeCounter <= 0L) || (this.flushCheckpoints.size() != 1)) break;
/* 186 */         this.writeCounter = 0L;
/* 187 */         cp.flushCheckpoint(cp.flushCheckpoint() - writeCounter); break;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 192 */       this.flushCheckpoints.remove();
/* 193 */       ChannelPromise promise = cp.promise();
/* 194 */       if (cause == null) {
/* 195 */         if (this.tryNotify) {
/* 196 */           promise.trySuccess();
/*     */         } else {
/* 198 */           promise.setSuccess();
/*     */         }
/*     */       }
/* 201 */       else if (this.tryNotify) {
/* 202 */         promise.tryFailure(cause);
/*     */       } else {
/* 204 */         promise.setFailure(cause);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 210 */     long newWriteCounter = this.writeCounter;
/* 211 */     if (newWriteCounter >= 549755813888L)
/*     */     {
/*     */ 
/* 214 */       this.writeCounter = 0L;
/* 215 */       for (FlushCheckpoint cp : this.flushCheckpoints) {
/* 216 */         cp.flushCheckpoint(cp.flushCheckpoint() - newWriteCounter);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class DefaultFlushCheckpoint
/*     */     implements ChannelFlushPromiseNotifier.FlushCheckpoint
/*     */   {
/*     */     private long checkpoint;
/*     */     
/*     */     private final ChannelPromise future;
/*     */     
/*     */ 
/*     */     DefaultFlushCheckpoint(long checkpoint, ChannelPromise future)
/*     */     {
/* 232 */       this.checkpoint = checkpoint;
/* 233 */       this.future = future;
/*     */     }
/*     */     
/*     */     public long flushCheckpoint()
/*     */     {
/* 238 */       return this.checkpoint;
/*     */     }
/*     */     
/*     */     public void flushCheckpoint(long checkpoint)
/*     */     {
/* 243 */       this.checkpoint = checkpoint;
/*     */     }
/*     */     
/*     */     public ChannelPromise promise()
/*     */     {
/* 248 */       return this.future;
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract interface FlushCheckpoint
/*     */   {
/*     */     public abstract long flushCheckpoint();
/*     */     
/*     */     public abstract void flushCheckpoint(long paramLong);
/*     */     
/*     */     public abstract ChannelPromise promise();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\ChannelFlushPromiseNotifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */