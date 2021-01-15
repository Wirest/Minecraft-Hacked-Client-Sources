/*    */ package rip.jutting.polaris.utils;
/*    */ 
/*    */ public class Timer
/*    */ {
/*    */   private long prevMS;
/*    */   private static long lastMS;
/*    */   
/*    */   public Timer() {
/*  9 */     this.prevMS = 0L;
/*    */   }
/*    */   
/*    */   public boolean delay(double speed) {
/* 13 */     return getTime() - this.prevMS >= speed;
/*    */   }
/*    */   
/* 16 */   public boolean hasPassed(float milliSec) { return (float)(getTime() - this.prevMS) >= milliSec; }
/*    */   
/*    */   public static long getCurrentMS()
/*    */   {
/* 20 */     return System.nanoTime() / 1000000L;
/*    */   }
/*    */   
/*    */   public void reset() {
/* 24 */     this.prevMS = getTime();
/*    */   }
/*    */   
/*    */   public static boolean hasReached(long milliseconds) {
/* 28 */     return getCurrentMS() - lastMS >= milliseconds;
/*    */   }
/*    */   
/*    */   public boolean hasReachedd(double milliseconds) {
/* 32 */     return getCurrentMS() - lastMS >= milliseconds;
/*    */   }
/*    */   
/*    */   public long getTime() {
/* 36 */     return System.nanoTime() / 1000000L;
/*    */   }
/*    */   
/*    */   public long getDifference() {
/* 40 */     return getTime() - this.prevMS;
/*    */   }
/*    */   
/*    */   public void setDifference(long difference) {
/* 44 */     this.prevMS = (getTime() - difference);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\utils\Timer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */