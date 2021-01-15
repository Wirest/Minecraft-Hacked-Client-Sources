/*    */ package ch.qos.logback.core.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InvocationGate
/*    */ {
/*    */   private static final int MAX_MASK = 65535;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 27 */   private volatile long mask = 15L;
/* 28 */   private volatile long lastMaskCheck = System.currentTimeMillis();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 35 */   private long invocationCounter = 0L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private static final long thresholdForMaskIncrease = 100L;
/*    */   
/*    */ 
/*    */ 
/* 44 */   private final long thresholdForMaskDecrease = 800L;
/*    */   
/*    */   public boolean skipFurtherWork()
/*    */   {
/* 48 */     return (this.invocationCounter++ & this.mask) != this.mask;
/*    */   }
/*    */   
/*    */   public void updateMaskIfNecessary(long now)
/*    */   {
/* 53 */     long timeElapsedSinceLastMaskUpdateCheck = now - this.lastMaskCheck;
/* 54 */     this.lastMaskCheck = now;
/* 55 */     if ((timeElapsedSinceLastMaskUpdateCheck < 100L) && (this.mask < 65535L)) {
/* 56 */       this.mask = (this.mask << 1 | 1L);
/* 57 */     } else if (timeElapsedSinceLastMaskUpdateCheck > 800L) {
/* 58 */       this.mask >>>= 2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\util\InvocationGate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */