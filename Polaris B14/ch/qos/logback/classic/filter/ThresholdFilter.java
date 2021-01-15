/*    */ package ch.qos.logback.classic.filter;
/*    */ 
/*    */ import ch.qos.logback.classic.Level;
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.core.filter.Filter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ThresholdFilter
/*    */   extends Filter<ILoggingEvent>
/*    */ {
/*    */   Level level;
/*    */   
/*    */   public FilterReply decide(ILoggingEvent event)
/*    */   {
/* 41 */     if (!isStarted()) {
/* 42 */       return FilterReply.NEUTRAL;
/*    */     }
/*    */     
/* 45 */     if (event.getLevel().isGreaterOrEqual(this.level)) {
/* 46 */       return FilterReply.NEUTRAL;
/*    */     }
/* 48 */     return FilterReply.DENY;
/*    */   }
/*    */   
/*    */   public void setLevel(String level)
/*    */   {
/* 53 */     this.level = Level.toLevel(level);
/*    */   }
/*    */   
/*    */   public void start() {
/* 57 */     if (this.level != null) {
/* 58 */       super.start();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\filter\ThresholdFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */