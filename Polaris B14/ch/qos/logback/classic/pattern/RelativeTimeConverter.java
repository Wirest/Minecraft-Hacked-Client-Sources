/*    */ package ch.qos.logback.classic.pattern;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.classic.spi.LoggerContextVO;
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
/*    */ public class RelativeTimeConverter
/*    */   extends ClassicConverter
/*    */ {
/* 20 */   long lastTimestamp = -1L;
/* 21 */   String timesmapCache = null;
/*    */   
/*    */   public String convert(ILoggingEvent event) {
/* 24 */     long now = event.getTimeStamp();
/*    */     
/* 26 */     synchronized (this)
/*    */     {
/* 28 */       if (now != this.lastTimestamp) {
/* 29 */         this.lastTimestamp = now;
/* 30 */         this.timesmapCache = Long.toString(now - event.getLoggerContextVO().getBirthTime());
/*    */       }
/* 32 */       return this.timesmapCache;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\pattern\RelativeTimeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */