/*     */ package io.netty.util;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
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
/*     */ public abstract class AbstractReferenceCounted
/*     */   implements ReferenceCounted
/*     */ {
/*     */   private static final AtomicIntegerFieldUpdater<AbstractReferenceCounted> refCntUpdater;
/*     */   
/*     */   static
/*     */   {
/*  30 */     AtomicIntegerFieldUpdater<AbstractReferenceCounted> updater = PlatformDependent.newAtomicIntegerFieldUpdater(AbstractReferenceCounted.class, "refCnt");
/*     */     
/*  32 */     if (updater == null) {
/*  33 */       updater = AtomicIntegerFieldUpdater.newUpdater(AbstractReferenceCounted.class, "refCnt");
/*     */     }
/*  35 */     refCntUpdater = updater;
/*     */   }
/*     */   
/*  38 */   private volatile int refCnt = 1;
/*     */   
/*     */   public final int refCnt()
/*     */   {
/*  42 */     return this.refCnt;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final void setRefCnt(int refCnt)
/*     */   {
/*  49 */     this.refCnt = refCnt;
/*     */   }
/*     */   
/*     */   public ReferenceCounted retain()
/*     */   {
/*     */     for (;;) {
/*  55 */       int refCnt = this.refCnt;
/*  56 */       if (refCnt == 0) {
/*  57 */         throw new IllegalReferenceCountException(0, 1);
/*     */       }
/*  59 */       if (refCnt == Integer.MAX_VALUE) {
/*  60 */         throw new IllegalReferenceCountException(Integer.MAX_VALUE, 1);
/*     */       }
/*  62 */       if (refCntUpdater.compareAndSet(this, refCnt, refCnt + 1)) {
/*     */         break;
/*     */       }
/*     */     }
/*  66 */     return this;
/*     */   }
/*     */   
/*     */   public ReferenceCounted retain(int increment)
/*     */   {
/*  71 */     if (increment <= 0) {
/*  72 */       throw new IllegalArgumentException("increment: " + increment + " (expected: > 0)");
/*     */     }
/*     */     for (;;)
/*     */     {
/*  76 */       int refCnt = this.refCnt;
/*  77 */       if (refCnt == 0) {
/*  78 */         throw new IllegalReferenceCountException(0, 1);
/*     */       }
/*  80 */       if (refCnt > Integer.MAX_VALUE - increment) {
/*  81 */         throw new IllegalReferenceCountException(refCnt, increment);
/*     */       }
/*  83 */       if (refCntUpdater.compareAndSet(this, refCnt, refCnt + increment)) {
/*     */         break;
/*     */       }
/*     */     }
/*  87 */     return this;
/*     */   }
/*     */   
/*     */   public ReferenceCounted touch()
/*     */   {
/*  92 */     return touch(null);
/*     */   }
/*     */   
/*     */   public boolean release()
/*     */   {
/*     */     for (;;) {
/*  98 */       int refCnt = this.refCnt;
/*  99 */       if (refCnt == 0) {
/* 100 */         throw new IllegalReferenceCountException(0, -1);
/*     */       }
/*     */       
/* 103 */       if (refCntUpdater.compareAndSet(this, refCnt, refCnt - 1)) {
/* 104 */         if (refCnt == 1) {
/* 105 */           deallocate();
/* 106 */           return true;
/*     */         }
/* 108 */         return false;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean release(int decrement)
/*     */   {
/* 115 */     if (decrement <= 0) {
/* 116 */       throw new IllegalArgumentException("decrement: " + decrement + " (expected: > 0)");
/*     */     }
/*     */     for (;;)
/*     */     {
/* 120 */       int refCnt = this.refCnt;
/* 121 */       if (refCnt < decrement) {
/* 122 */         throw new IllegalReferenceCountException(refCnt, -decrement);
/*     */       }
/*     */       
/* 125 */       if (refCntUpdater.compareAndSet(this, refCnt, refCnt - decrement)) {
/* 126 */         if (refCnt == decrement) {
/* 127 */           deallocate();
/* 128 */           return true;
/*     */         }
/* 130 */         return false;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract void deallocate();
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\AbstractReferenceCounted.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */