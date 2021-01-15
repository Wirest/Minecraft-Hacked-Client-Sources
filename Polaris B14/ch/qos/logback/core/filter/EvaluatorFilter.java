/*    */ package ch.qos.logback.core.filter;
/*    */ 
/*    */ import ch.qos.logback.core.boolex.EvaluationException;
/*    */ import ch.qos.logback.core.boolex.EventEvaluator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EvaluatorFilter<E>
/*    */   extends AbstractMatcherFilter<E>
/*    */ {
/*    */   EventEvaluator<E> evaluator;
/*    */   
/*    */   public void start()
/*    */   {
/* 43 */     if (this.evaluator != null) {
/* 44 */       super.start();
/*    */     } else {
/* 46 */       addError("No evaluator set for filter " + getName());
/*    */     }
/*    */   }
/*    */   
/*    */   public EventEvaluator<E> getEvaluator() {
/* 51 */     return this.evaluator;
/*    */   }
/*    */   
/*    */   public void setEvaluator(EventEvaluator<E> evaluator) {
/* 55 */     this.evaluator = evaluator;
/*    */   }
/*    */   
/*    */ 
/*    */   public FilterReply decide(E event)
/*    */   {
/* 61 */     if ((!isStarted()) || (!this.evaluator.isStarted())) {
/* 62 */       return FilterReply.NEUTRAL;
/*    */     }
/*    */     try {
/* 65 */       if (this.evaluator.evaluate(event)) {
/* 66 */         return this.onMatch;
/*    */       }
/* 68 */       return this.onMismatch;
/*    */     }
/*    */     catch (EvaluationException e) {
/* 71 */       addError("Evaluator " + this.evaluator.getName() + " threw an exception", e); }
/* 72 */     return FilterReply.NEUTRAL;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\filter\EvaluatorFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */