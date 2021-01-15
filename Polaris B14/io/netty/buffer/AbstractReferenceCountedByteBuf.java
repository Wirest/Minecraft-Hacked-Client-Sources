/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.IllegalReferenceCountException;
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
/*     */ 
/*     */ public abstract class AbstractReferenceCountedByteBuf
/*     */   extends AbstractByteBuf
/*     */ {
/*     */   private static final AtomicIntegerFieldUpdater<AbstractReferenceCountedByteBuf> refCntUpdater;
/*     */   
/*     */   static
/*     */   {
/*  32 */     AtomicIntegerFieldUpdater<AbstractReferenceCountedByteBuf> updater = PlatformDependent.newAtomicIntegerFieldUpdater(AbstractReferenceCountedByteBuf.class, "refCnt");
/*     */     
/*  34 */     if (updater == null) {
/*  35 */       updater = AtomicIntegerFieldUpdater.newUpdater(AbstractReferenceCountedByteBuf.class, "refCnt");
/*     */     }
/*  37 */     refCntUpdater = updater;
/*     */   }
/*     */   
/*  40 */   private volatile int refCnt = 1;
/*     */   
/*     */   protected AbstractReferenceCountedByteBuf(int maxCapacity) {
/*  43 */     super(maxCapacity);
/*     */   }
/*     */   
/*     */   public final int refCnt()
/*     */   {
/*  48 */     return this.refCnt;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final void setRefCnt(int refCnt)
/*     */   {
/*  55 */     this.refCnt = refCnt;
/*     */   }
/*     */   
/*     */   public ByteBuf retain()
/*     */   {
/*     */     for (;;) {
/*  61 */       int refCnt = this.refCnt;
/*  62 */       if (refCnt == 0) {
/*  63 */         throw new IllegalReferenceCountException(0, 1);
/*     */       }
/*  65 */       if (refCnt == Integer.MAX_VALUE) {
/*  66 */         throw new IllegalReferenceCountException(Integer.MAX_VALUE, 1);
/*     */       }
/*  68 */       if (refCntUpdater.compareAndSet(this, refCnt, refCnt + 1)) {
/*     */         break;
/*     */       }
/*     */     }
/*  72 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf retain(int increment)
/*     */   {
/*  77 */     if (increment <= 0) {
/*  78 */       throw new IllegalArgumentException("increment: " + increment + " (expected: > 0)");
/*     */     }
/*     */     for (;;)
/*     */     {
/*  82 */       int refCnt = this.refCnt;
/*  83 */       if (refCnt == 0) {
/*  84 */         throw new IllegalReferenceCountException(0, increment);
/*     */       }
/*  86 */       if (refCnt > Integer.MAX_VALUE - increment) {
/*  87 */         throw new IllegalReferenceCountException(refCnt, increment);
/*     */       }
/*  89 */       if (refCntUpdater.compareAndSet(this, refCnt, refCnt + increment)) {
/*     */         break;
/*     */       }
/*     */     }
/*  93 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf touch()
/*     */   {
/*  98 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf touch(Object hint)
/*     */   {
/* 103 */     return this;
/*     */   }
/*     */   
/*     */   public final boolean release()
/*     */   {
/*     */     for (;;) {
/* 109 */       int refCnt = this.refCnt;
/* 110 */       if (refCnt == 0) {
/* 111 */         throw new IllegalReferenceCountException(0, -1);
/*     */       }
/*     */       
/* 114 */       if (refCntUpdater.compareAndSet(this, refCnt, refCnt - 1)) {
/* 115 */         if (refCnt == 1) {
/* 116 */           deallocate();
/* 117 */           return true;
/*     */         }
/* 119 */         return false;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public final boolean release(int decrement)
/*     */   {
/* 126 */     if (decrement <= 0) {
/* 127 */       throw new IllegalArgumentException("decrement: " + decrement + " (expected: > 0)");
/*     */     }
/*     */     for (;;)
/*     */     {
/* 131 */       int refCnt = this.refCnt;
/* 132 */       if (refCnt < decrement) {
/* 133 */         throw new IllegalReferenceCountException(refCnt, -decrement);
/*     */       }
/*     */       
/* 136 */       if (refCntUpdater.compareAndSet(this, refCnt, refCnt - decrement)) {
/* 137 */         if (refCnt == decrement) {
/* 138 */           deallocate();
/* 139 */           return true;
/*     */         }
/* 141 */         return false;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract void deallocate();
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\AbstractReferenceCountedByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */