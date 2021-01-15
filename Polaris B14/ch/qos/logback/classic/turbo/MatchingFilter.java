/*    */ package ch.qos.logback.classic.turbo;
/*    */ 
/*    */ import ch.qos.logback.core.spi.FilterReply;
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
/*    */ public abstract class MatchingFilter
/*    */   extends TurboFilter
/*    */ {
/* 26 */   protected FilterReply onMatch = FilterReply.NEUTRAL;
/* 27 */   protected FilterReply onMismatch = FilterReply.NEUTRAL;
/*    */   
/*    */   public final void setOnMatch(String action) {
/* 30 */     if ("NEUTRAL".equals(action)) {
/* 31 */       this.onMatch = FilterReply.NEUTRAL;
/* 32 */     } else if ("ACCEPT".equals(action)) {
/* 33 */       this.onMatch = FilterReply.ACCEPT;
/* 34 */     } else if ("DENY".equals(action)) {
/* 35 */       this.onMatch = FilterReply.DENY;
/*    */     }
/*    */   }
/*    */   
/*    */   public final void setOnMismatch(String action) {
/* 40 */     if ("NEUTRAL".equals(action)) {
/* 41 */       this.onMismatch = FilterReply.NEUTRAL;
/* 42 */     } else if ("ACCEPT".equals(action)) {
/* 43 */       this.onMismatch = FilterReply.ACCEPT;
/* 44 */     } else if ("DENY".equals(action)) {
/* 45 */       this.onMismatch = FilterReply.DENY;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\turbo\MatchingFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */