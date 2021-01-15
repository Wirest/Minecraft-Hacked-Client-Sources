/*    */ package ch.qos.logback.classic.filter;
/*    */ 
/*    */ import ch.qos.logback.classic.Level;
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.core.filter.AbstractMatcherFilter;
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
/*    */ public class LevelFilter
/*    */   extends AbstractMatcherFilter<ILoggingEvent>
/*    */ {
/*    */   Level level;
/*    */   
/*    */   public FilterReply decide(ILoggingEvent event)
/*    */   {
/* 37 */     if (!isStarted()) {
/* 38 */       return FilterReply.NEUTRAL;
/*    */     }
/*    */     
/* 41 */     if (event.getLevel().equals(this.level)) {
/* 42 */       return this.onMatch;
/*    */     }
/* 44 */     return this.onMismatch;
/*    */   }
/*    */   
/*    */   public void setLevel(Level level)
/*    */   {
/* 49 */     this.level = level;
/*    */   }
/*    */   
/*    */   public void start() {
/* 53 */     if (this.level != null) {
/* 54 */       super.start();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\filter\LevelFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */