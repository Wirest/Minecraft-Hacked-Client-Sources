/*    */ package ch.qos.logback.core.util;
/*    */ 
/*    */ import java.util.Calendar;
/*    */ import java.util.Date;
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
/*    */ 
/*    */ 
/*    */ public class TimeUtil
/*    */ {
/*    */   public static long computeStartOfNextSecond(long now)
/*    */   {
/* 23 */     Calendar cal = Calendar.getInstance();
/* 24 */     cal.setTime(new Date(now));
/* 25 */     cal.set(14, 0);
/* 26 */     cal.add(13, 1);
/* 27 */     return cal.getTime().getTime();
/*    */   }
/*    */   
/*    */   public static long computeStartOfNextMinute(long now) {
/* 31 */     Calendar cal = Calendar.getInstance();
/* 32 */     cal.setTime(new Date(now));
/* 33 */     cal.set(14, 0);
/* 34 */     cal.set(13, 0);
/* 35 */     cal.add(12, 1);
/* 36 */     return cal.getTime().getTime();
/*    */   }
/*    */   
/*    */   public static long computeStartOfNextHour(long now) {
/* 40 */     Calendar cal = Calendar.getInstance();
/* 41 */     cal.setTime(new Date(now));
/* 42 */     cal.set(14, 0);
/* 43 */     cal.set(13, 0);
/* 44 */     cal.set(12, 0);
/* 45 */     cal.add(10, 1);
/* 46 */     return cal.getTime().getTime();
/*    */   }
/*    */   
/*    */   public static long computeStartOfNextDay(long now) {
/* 50 */     Calendar cal = Calendar.getInstance();
/* 51 */     cal.setTime(new Date(now));
/*    */     
/* 53 */     cal.add(5, 1);
/* 54 */     cal.set(14, 0);
/* 55 */     cal.set(13, 0);
/* 56 */     cal.set(12, 0);
/* 57 */     cal.set(11, 0);
/* 58 */     return cal.getTime().getTime();
/*    */   }
/*    */   
/*    */   public static long computeStartOfNextWeek(long now) {
/* 62 */     Calendar cal = Calendar.getInstance();
/* 63 */     cal.setTime(new Date(now));
/*    */     
/* 65 */     cal.set(7, cal.getFirstDayOfWeek());
/* 66 */     cal.set(11, 0);
/* 67 */     cal.set(12, 0);
/* 68 */     cal.set(13, 0);
/* 69 */     cal.set(14, 0);
/* 70 */     cal.add(3, 1);
/* 71 */     return cal.getTime().getTime();
/*    */   }
/*    */   
/*    */   public static long computeStartOfNextMonth(long now) {
/* 75 */     Calendar cal = Calendar.getInstance();
/* 76 */     cal.setTime(new Date(now));
/*    */     
/* 78 */     cal.set(5, 1);
/* 79 */     cal.set(11, 0);
/* 80 */     cal.set(12, 0);
/* 81 */     cal.set(13, 0);
/* 82 */     cal.set(14, 0);
/* 83 */     cal.add(2, 1);
/* 84 */     return cal.getTime().getTime();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\util\TimeUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */