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
/*    */ public abstract class NamedConverter
/*    */   extends ClassicConverter
/*    */ {
/* 20 */   Abbreviator abbreviator = null;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected abstract String getFullyQualifiedName(ILoggingEvent paramILoggingEvent);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void start()
/*    */   {
/* 32 */     String optStr = getFirstOption();
/* 33 */     if (optStr != null) {
/*    */       try {
/* 35 */         int targetLen = Integer.parseInt(optStr);
/* 36 */         if (targetLen == 0) {
/* 37 */           this.abbreviator = new ClassNameOnlyAbbreviator();
/* 38 */         } else if (targetLen > 0) {
/* 39 */           this.abbreviator = new TargetLengthBasedClassNameAbbreviator(targetLen);
/*    */         }
/*    */       }
/*    */       catch (NumberFormatException nfe) {}
/*    */     }
/*    */   }
/*    */   
/*    */   public String convert(ILoggingEvent event)
/*    */   {
/* 48 */     String fqn = getFullyQualifiedName(event);
/*    */     
/* 50 */     if (this.abbreviator == null) {
/* 51 */       return fqn;
/*    */     }
/* 53 */     return this.abbreviator.abbreviate(fqn);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\pattern\NamedConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */