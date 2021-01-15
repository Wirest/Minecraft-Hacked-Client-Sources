/*    */ package ch.qos.logback.classic.sift;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.core.sift.AbstractDiscriminator;
/*    */ import ch.qos.logback.core.util.OptionHelper;
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
/*    */ public class MDCBasedDiscriminator
/*    */   extends AbstractDiscriminator<ILoggingEvent>
/*    */ {
/*    */   private String key;
/*    */   private String defaultValue;
/*    */   
/*    */   public String getDiscriminatingValue(ILoggingEvent event)
/*    */   {
/* 42 */     Map<String, String> mdcMap = event.getMDCPropertyMap();
/* 43 */     if (mdcMap == null) {
/* 44 */       return this.defaultValue;
/*    */     }
/* 46 */     String mdcValue = (String)mdcMap.get(this.key);
/* 47 */     if (mdcValue == null) {
/* 48 */       return this.defaultValue;
/*    */     }
/* 50 */     return mdcValue;
/*    */   }
/*    */   
/*    */ 
/*    */   public void start()
/*    */   {
/* 56 */     int errors = 0;
/* 57 */     if (OptionHelper.isEmpty(this.key)) {
/* 58 */       errors++;
/* 59 */       addError("The \"Key\" property must be set");
/*    */     }
/* 61 */     if (OptionHelper.isEmpty(this.defaultValue)) {
/* 62 */       errors++;
/* 63 */       addError("The \"DefaultValue\" property must be set");
/*    */     }
/* 65 */     if (errors == 0) {
/* 66 */       this.started = true;
/*    */     }
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 71 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(String key) {
/* 75 */     this.key = key;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getDefaultValue()
/*    */   {
/* 83 */     return this.defaultValue;
/*    */   }
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
/*    */   public void setDefaultValue(String defaultValue)
/*    */   {
/* 97 */     this.defaultValue = defaultValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\sift\MDCBasedDiscriminator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */