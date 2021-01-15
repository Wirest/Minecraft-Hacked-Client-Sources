/*    */ package ch.qos.logback.classic.turbo;
/*    */ 
/*    */ import ch.qos.logback.classic.Level;
/*    */ import ch.qos.logback.classic.Logger;
/*    */ import ch.qos.logback.core.spi.FilterReply;
/*    */ import org.slf4j.MDC;
/*    */ import org.slf4j.Marker;
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
/*    */ public class MDCFilter
/*    */   extends MatchingFilter
/*    */ {
/*    */   String MDCKey;
/*    */   String value;
/*    */   
/*    */   public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t)
/*    */   {
/* 53 */     if (this.MDCKey == null) {
/* 54 */       return FilterReply.NEUTRAL;
/*    */     }
/*    */     
/* 57 */     String value = MDC.get(this.MDCKey);
/* 58 */     if (this.value.equals(value)) {
/* 59 */       return this.onMatch;
/*    */     }
/* 61 */     return this.onMismatch;
/*    */   }
/*    */   
/*    */   public void setValue(String value) {
/* 65 */     this.value = value;
/*    */   }
/*    */   
/*    */   public void setMDCKey(String MDCKey) {
/* 69 */     this.MDCKey = MDCKey;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\turbo\MDCFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */