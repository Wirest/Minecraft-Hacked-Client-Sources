/*     */ package ch.qos.logback.core.joran.action;
/*     */ 
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.boolex.EventEvaluator;
/*     */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*     */ import ch.qos.logback.core.spi.LifeCycle;
/*     */ import ch.qos.logback.core.util.OptionHelper;
/*     */ import java.util.Map;
/*     */ import org.xml.sax.Attributes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractEventEvaluatorAction
/*     */   extends Action
/*     */ {
/*     */   EventEvaluator<?> evaluator;
/*  29 */   boolean inError = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void begin(InterpretationContext ec, String name, Attributes attributes)
/*     */   {
/*  36 */     this.inError = false;
/*  37 */     this.evaluator = null;
/*     */     
/*  39 */     String className = attributes.getValue("class");
/*  40 */     if (OptionHelper.isEmpty(className)) {
/*  41 */       className = defaultClassName();
/*  42 */       addInfo("Assuming default evaluator class [" + className + "]");
/*     */     }
/*     */     
/*  45 */     if (OptionHelper.isEmpty(className)) {
/*  46 */       className = defaultClassName();
/*  47 */       this.inError = true;
/*  48 */       addError("Mandatory \"class\" attribute not set for <evaluator>");
/*     */       
/*  50 */       return;
/*     */     }
/*     */     
/*  53 */     String evaluatorName = attributes.getValue("name");
/*  54 */     if (OptionHelper.isEmpty(evaluatorName)) {
/*  55 */       this.inError = true;
/*  56 */       addError("Mandatory \"name\" attribute not set for <evaluator>");
/*     */       
/*  58 */       return;
/*     */     }
/*     */     try {
/*  61 */       this.evaluator = ((EventEvaluator)OptionHelper.instantiateByClassName(className, EventEvaluator.class, this.context));
/*     */       
/*     */ 
/*  64 */       this.evaluator.setContext(this.context);
/*  65 */       this.evaluator.setName(evaluatorName);
/*     */       
/*  67 */       ec.pushObject(this.evaluator);
/*  68 */       addInfo("Adding evaluator named [" + evaluatorName + "] to the object stack");
/*     */     }
/*     */     catch (Exception oops)
/*     */     {
/*  72 */       this.inError = true;
/*  73 */       addError("Could not create evaluator of type " + className + "].", oops);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract String defaultClassName();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void end(InterpretationContext ec, String e)
/*     */   {
/*  90 */     if (this.inError) {
/*  91 */       return;
/*     */     }
/*     */     
/*  94 */     if ((this.evaluator instanceof LifeCycle)) {
/*  95 */       this.evaluator.start();
/*  96 */       addInfo("Starting evaluator named [" + this.evaluator.getName() + "]");
/*     */     }
/*     */     
/*  99 */     Object o = ec.peekObject();
/*     */     
/* 101 */     if (o != this.evaluator) {
/* 102 */       addWarn("The object on the top the of the stack is not the evaluator pushed earlier.");
/*     */     } else {
/* 104 */       ec.popObject();
/*     */       try
/*     */       {
/* 107 */         Map<String, EventEvaluator<?>> evaluatorMap = (Map)this.context.getObject("EVALUATOR_MAP");
/*     */         
/* 109 */         if (evaluatorMap == null) {
/* 110 */           addError("Could not find EvaluatorMap");
/*     */         } else {
/* 112 */           evaluatorMap.put(this.evaluator.getName(), this.evaluator);
/*     */         }
/*     */       } catch (Exception ex) {
/* 115 */         addError("Could not set evaluator named [" + this.evaluator + "].", ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void finish(InterpretationContext ec) {}
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\action\AbstractEventEvaluatorAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */