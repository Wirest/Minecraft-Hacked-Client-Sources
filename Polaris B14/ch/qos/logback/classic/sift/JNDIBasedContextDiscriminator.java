/*    */ package ch.qos.logback.classic.sift;
/*    */ 
/*    */ import ch.qos.logback.classic.LoggerContext;
/*    */ import ch.qos.logback.classic.selector.ContextSelector;
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.classic.util.ContextSelectorStaticBinder;
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
/*    */ 
/*    */ public class JNDIBasedContextDiscriminator
/*    */   extends AbstractDiscriminator<ILoggingEvent>
/*    */ {
/*    */   private static final String KEY = "contextName";
/*    */   private String defaultValue;
/*    */   
/*    */   public String getDiscriminatingValue(ILoggingEvent event)
/*    */   {
/* 41 */     ContextSelector selector = ContextSelectorStaticBinder.getSingleton().getContextSelector();
/*    */     
/*    */ 
/* 44 */     if (selector == null) {
/* 45 */       return this.defaultValue;
/*    */     }
/*    */     
/* 48 */     LoggerContext lc = selector.getLoggerContext();
/* 49 */     if (lc == null) {
/* 50 */       return this.defaultValue;
/*    */     }
/*    */     
/* 53 */     return lc.getName();
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 57 */     return "contextName";
/*    */   }
/*    */   
/*    */   public void setKey(String key) {
/* 61 */     throw new UnsupportedOperationException("Key cannot be set. Using fixed key contextName");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getDefaultValue()
/*    */   {
/* 70 */     return this.defaultValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setDefaultValue(String defaultValue)
/*    */   {
/* 80 */     this.defaultValue = defaultValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\sift\JNDIBasedContextDiscriminator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */