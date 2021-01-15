/*    */ package rip.jutting.polaris.utils;
/*    */ 
/*    */ public class Timer2
/*    */ {
/*    */   private long last;
/*    */   
/*    */   public Timer2() {
/*  8 */     updateLastTime();
/*    */   }
/*    */   
/*    */   public boolean hasPassed(double milli) {
/* 12 */     return getTime() - this.last >= milli;
/*    */   }
/*    */   
/*    */   public long getTime() {
/* 16 */     return System.nanoTime() / 1000000L;
/*    */   }
/*    */   
/*    */   public long getElapsedTime() {
/* 20 */     return getTime() - this.last;
/*    */   }
/*    */   
/*    */   public void updateLastTime() {
/* 24 */     this.last = getTime();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\utils\Timer2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */