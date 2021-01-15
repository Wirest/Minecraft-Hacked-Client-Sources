/*    */ package ch.qos.logback.core.util;
/*    */ 
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.TimeZone;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CachingDateFormatter
/*    */ {
/* 29 */   long lastTimestamp = -1L;
/* 30 */   String cachedStr = null;
/*    */   final SimpleDateFormat sdf;
/*    */   
/*    */   public CachingDateFormatter(String pattern) {
/* 34 */     this.sdf = new SimpleDateFormat(pattern);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public final String format(long now)
/*    */   {
/* 46 */     synchronized (this) {
/* 47 */       if (now != this.lastTimestamp) {
/* 48 */         this.lastTimestamp = now;
/* 49 */         this.cachedStr = this.sdf.format(new Date(now));
/*    */       }
/* 51 */       return this.cachedStr;
/*    */     }
/*    */   }
/*    */   
/*    */   public void setTimeZone(TimeZone tz) {
/* 56 */     this.sdf.setTimeZone(tz);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\util\CachingDateFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */