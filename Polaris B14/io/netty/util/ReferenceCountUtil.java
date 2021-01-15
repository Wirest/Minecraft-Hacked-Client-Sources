/*     */ package io.netty.util;
/*     */ 
/*     */ import io.netty.util.internal.StringUtil;
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
/*     */ 
/*     */ public final class ReferenceCountUtil
/*     */ {
/*  27 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ReferenceCountUtil.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> T retain(T msg)
/*     */   {
/*  35 */     if ((msg instanceof ReferenceCounted)) {
/*  36 */       return ((ReferenceCounted)msg).retain();
/*     */     }
/*  38 */     return msg;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> T retain(T msg, int increment)
/*     */   {
/*  47 */     if ((msg instanceof ReferenceCounted)) {
/*  48 */       return ((ReferenceCounted)msg).retain(increment);
/*     */     }
/*  50 */     return msg;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> T touch(T msg)
/*     */   {
/*  59 */     if ((msg instanceof ReferenceCounted)) {
/*  60 */       return ((ReferenceCounted)msg).touch();
/*     */     }
/*  62 */     return msg;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> T touch(T msg, Object hint)
/*     */   {
/*  72 */     if ((msg instanceof ReferenceCounted)) {
/*  73 */       return ((ReferenceCounted)msg).touch(hint);
/*     */     }
/*  75 */     return msg;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean release(Object msg)
/*     */   {
/*  83 */     if ((msg instanceof ReferenceCounted)) {
/*  84 */       return ((ReferenceCounted)msg).release();
/*     */     }
/*  86 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean release(Object msg, int decrement)
/*     */   {
/*  94 */     if ((msg instanceof ReferenceCounted)) {
/*  95 */       return ((ReferenceCounted)msg).release(decrement);
/*     */     }
/*  97 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void safeRelease(Object msg)
/*     */   {
/*     */     try
/*     */     {
/* 109 */       release(msg);
/*     */     } catch (Throwable t) {
/* 111 */       logger.warn("Failed to release a message: {}", msg, t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void safeRelease(Object msg, int decrement)
/*     */   {
/*     */     try
/*     */     {
/* 124 */       release(msg, decrement);
/*     */     } catch (Throwable t) {
/* 126 */       if (logger.isWarnEnabled()) {
/* 127 */         logger.warn("Failed to release a message: {} (decrement: {})", new Object[] { msg, Integer.valueOf(decrement), t });
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> T releaseLater(T msg)
/*     */   {
/* 138 */     return (T)releaseLater(msg, 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> T releaseLater(T msg, int decrement)
/*     */   {
/* 147 */     if ((msg instanceof ReferenceCounted)) {
/* 148 */       ThreadDeathWatcher.watch(Thread.currentThread(), new ReleasingTask((ReferenceCounted)msg, decrement));
/*     */     }
/* 150 */     return msg;
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class ReleasingTask
/*     */     implements Runnable
/*     */   {
/*     */     private final ReferenceCounted obj;
/*     */     private final int decrement;
/*     */     
/*     */     ReleasingTask(ReferenceCounted obj, int decrement)
/*     */     {
/* 162 */       this.obj = obj;
/* 163 */       this.decrement = decrement;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/*     */       try {
/* 169 */         if (!this.obj.release(this.decrement)) {
/* 170 */           ReferenceCountUtil.logger.warn("Non-zero refCnt: {}", this);
/*     */         } else {
/* 172 */           ReferenceCountUtil.logger.debug("Released: {}", this);
/*     */         }
/*     */       } catch (Exception ex) {
/* 175 */         ReferenceCountUtil.logger.warn("Failed to release an object: {}", this.obj, ex);
/*     */       }
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 181 */       return StringUtil.simpleClassName(this.obj) + ".release(" + this.decrement + ") refCnt: " + this.obj.refCnt();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\ReferenceCountUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */