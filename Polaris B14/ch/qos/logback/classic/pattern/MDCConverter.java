/*    */ package ch.qos.logback.classic.pattern;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.core.util.OptionHelper;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
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
/*    */ public class MDCConverter
/*    */   extends ClassicConverter
/*    */ {
/*    */   private String key;
/* 25 */   private String defaultValue = "";
/*    */   
/*    */   public void start()
/*    */   {
/* 29 */     String[] keyInfo = OptionHelper.extractDefaultReplacement(getFirstOption());
/* 30 */     this.key = keyInfo[0];
/* 31 */     if (keyInfo[1] != null) {
/* 32 */       this.defaultValue = keyInfo[1];
/*    */     }
/* 34 */     super.start();
/*    */   }
/*    */   
/*    */   public void stop()
/*    */   {
/* 39 */     this.key = null;
/* 40 */     super.stop();
/*    */   }
/*    */   
/*    */   public String convert(ILoggingEvent event)
/*    */   {
/* 45 */     Map<String, String> mdcPropertyMap = event.getMDCPropertyMap();
/*    */     
/* 47 */     if (mdcPropertyMap == null) {
/* 48 */       return this.defaultValue;
/*    */     }
/*    */     
/* 51 */     if (this.key == null) {
/* 52 */       return outputMDCForAllKeys(mdcPropertyMap);
/*    */     }
/*    */     
/* 55 */     String value = (String)event.getMDCPropertyMap().get(this.key);
/* 56 */     if (value != null) {
/* 57 */       return value;
/*    */     }
/* 59 */     return this.defaultValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private String outputMDCForAllKeys(Map<String, String> mdcPropertyMap)
/*    */   {
/* 68 */     StringBuilder buf = new StringBuilder();
/* 69 */     boolean first = true;
/* 70 */     for (Map.Entry<String, String> entry : mdcPropertyMap.entrySet()) {
/* 71 */       if (first) {
/* 72 */         first = false;
/*    */       } else {
/* 74 */         buf.append(", ");
/*    */       }
/*    */       
/* 77 */       buf.append((String)entry.getKey()).append('=').append((String)entry.getValue());
/*    */     }
/* 79 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\pattern\MDCConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */