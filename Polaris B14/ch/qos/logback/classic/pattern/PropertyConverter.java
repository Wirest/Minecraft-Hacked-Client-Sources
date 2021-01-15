/*    */ package ch.qos.logback.classic.pattern;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.classic.spi.LoggerContextVO;
/*    */ import java.util.Map;
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
/*    */ public final class PropertyConverter
/*    */   extends ClassicConverter
/*    */ {
/*    */   String key;
/*    */   
/*    */   public void start()
/*    */   {
/* 26 */     String optStr = getFirstOption();
/* 27 */     if (optStr != null) {
/* 28 */       this.key = optStr;
/* 29 */       super.start();
/*    */     }
/*    */   }
/*    */   
/*    */   public String convert(ILoggingEvent event) {
/* 34 */     if (this.key == null) {
/* 35 */       return "Property_HAS_NO_KEY";
/*    */     }
/* 37 */     LoggerContextVO lcvo = event.getLoggerContextVO();
/* 38 */     Map<String, String> map = lcvo.getPropertyMap();
/* 39 */     String val = (String)map.get(this.key);
/* 40 */     if (val != null) {
/* 41 */       return val;
/*    */     }
/* 43 */     return System.getProperty(this.key);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\pattern\PropertyConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */