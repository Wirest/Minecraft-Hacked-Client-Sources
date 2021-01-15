/*    */ package ch.qos.logback.classic.pattern;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
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
/*    */ public class FileOfCallerConverter
/*    */   extends ClassicConverter
/*    */ {
/*    */   public String convert(ILoggingEvent le)
/*    */   {
/* 22 */     StackTraceElement[] cda = le.getCallerData();
/* 23 */     if ((cda != null) && (cda.length > 0)) {
/* 24 */       return cda[0].getFileName();
/*    */     }
/* 26 */     return "?";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\pattern\FileOfCallerConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */