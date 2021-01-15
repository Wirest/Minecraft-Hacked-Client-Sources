/*    */ package ch.qos.logback.core.recovery;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RecoveryCoordinator
/*    */ {
/*    */   public static final long BACKOFF_COEFFICIENT_MIN = 20L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 19 */   static long BACKOFF_COEFFICIENT_MAX = 327680L;
/*    */   
/* 21 */   private long backOffCoefficient = 20L;
/*    */   
/* 23 */   private static long UNSET = -1L;
/* 24 */   private long currentTime = UNSET;
/* 25 */   long next = System.currentTimeMillis() + getBackoffCoefficient();
/*    */   
/*    */   public boolean isTooSoon() {
/* 28 */     long now = getCurrentTime();
/* 29 */     if (now > this.next) {
/* 30 */       this.next = (now + getBackoffCoefficient());
/* 31 */       return false;
/*    */     }
/* 33 */     return true;
/*    */   }
/*    */   
/*    */   void setCurrentTime(long forcedTime)
/*    */   {
/* 38 */     this.currentTime = forcedTime;
/*    */   }
/*    */   
/*    */   private long getCurrentTime() {
/* 42 */     if (this.currentTime != UNSET) {
/* 43 */       return this.currentTime;
/*    */     }
/* 45 */     return System.currentTimeMillis();
/*    */   }
/*    */   
/*    */   private long getBackoffCoefficient() {
/* 49 */     long currentCoeff = this.backOffCoefficient;
/* 50 */     if (this.backOffCoefficient < BACKOFF_COEFFICIENT_MAX) {
/* 51 */       this.backOffCoefficient *= 4L;
/*    */     }
/* 53 */     return currentCoeff;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\recovery\RecoveryCoordinator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */