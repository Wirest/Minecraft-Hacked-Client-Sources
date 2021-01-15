/*    */ package rip.jutting.polaris.utils;
/*    */ 
/*    */ public class TimeUtils
/*    */ {
/*    */   private long prevMS;
/*    */   
/*    */   public TimeUtils()
/*    */   {
/*  9 */     this.prevMS = 0L;
/*    */   }
/*    */   
/*    */   public boolean delay(float milliSec)
/*    */   {
/* 14 */     return (float)(getTime() - this.prevMS) >= milliSec;
/*    */   }
/*    */   
/*    */   public void reset()
/*    */   {
/* 19 */     this.prevMS = getTime();
/*    */   }
/*    */   
/*    */   public long getTime()
/*    */   {
/* 24 */     return System.nanoTime() / 1000000L;
/*    */   }
/*    */   
/*    */   public long getDifference()
/*    */   {
/* 29 */     return getTime() - this.prevMS;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\utils\TimeUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */