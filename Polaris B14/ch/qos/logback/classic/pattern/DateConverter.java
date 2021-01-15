/*    */ package ch.qos.logback.classic.pattern;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.core.util.CachingDateFormatter;
/*    */ import java.util.List;
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
/*    */ public class DateConverter
/*    */   extends ClassicConverter
/*    */ {
/* 25 */   long lastTimestamp = -1L;
/* 26 */   String timestampStrCache = null;
/* 27 */   CachingDateFormatter cachingDateFormatter = null;
/*    */   
/*    */ 
/*    */   public void start()
/*    */   {
/* 32 */     String datePattern = getFirstOption();
/* 33 */     if (datePattern == null) {
/* 34 */       datePattern = "yyyy-MM-dd HH:mm:ss,SSS";
/*    */     }
/*    */     
/* 37 */     if (datePattern.equals("ISO8601")) {
/* 38 */       datePattern = "yyyy-MM-dd HH:mm:ss,SSS";
/*    */     }
/*    */     try
/*    */     {
/* 42 */       this.cachingDateFormatter = new CachingDateFormatter(datePattern);
/*    */     }
/*    */     catch (IllegalArgumentException e)
/*    */     {
/* 46 */       addWarn("Could not instantiate SimpleDateFormat with pattern " + datePattern, e);
/*    */       
/*    */ 
/* 49 */       this.cachingDateFormatter = new CachingDateFormatter("yyyy-MM-dd HH:mm:ss,SSS");
/*    */     }
/*    */     
/* 52 */     List optionList = getOptionList();
/*    */     
/*    */ 
/* 55 */     if ((optionList != null) && (optionList.size() > 1)) {
/* 56 */       TimeZone tz = TimeZone.getTimeZone((String)optionList.get(1));
/* 57 */       this.cachingDateFormatter.setTimeZone(tz);
/*    */     }
/*    */   }
/*    */   
/*    */   public String convert(ILoggingEvent le) {
/* 62 */     long timestamp = le.getTimeStamp();
/* 63 */     return this.cachingDateFormatter.format(timestamp);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\pattern\DateConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */