/*    */ package ch.qos.logback.classic.sift;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.classic.spi.LoggerContextVO;
/*    */ import ch.qos.logback.core.sift.AbstractDiscriminator;
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
/*    */ public class ContextBasedDiscriminator
/*    */   extends AbstractDiscriminator<ILoggingEvent>
/*    */ {
/*    */   private static final String KEY = "contextName";
/*    */   private String defaultValue;
/*    */   
/*    */   public String getDiscriminatingValue(ILoggingEvent event)
/*    */   {
/* 38 */     String contextName = event.getLoggerContextVO().getName();
/*    */     
/* 40 */     if (contextName == null) {
/* 41 */       return this.defaultValue;
/*    */     }
/* 43 */     return contextName;
/*    */   }
/*    */   
/*    */   public String getKey()
/*    */   {
/* 48 */     return "contextName";
/*    */   }
/*    */   
/*    */   public void setKey(String key) {
/* 52 */     throw new UnsupportedOperationException("Key cannot be set. Using fixed key contextName");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getDefaultValue()
/*    */   {
/* 61 */     return this.defaultValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setDefaultValue(String defaultValue)
/*    */   {
/* 71 */     this.defaultValue = defaultValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\sift\ContextBasedDiscriminator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */