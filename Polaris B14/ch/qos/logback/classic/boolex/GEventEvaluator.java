/*    */ package ch.qos.logback.classic.boolex;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.core.boolex.EvaluationException;
/*    */ import ch.qos.logback.core.boolex.EventEvaluatorBase;
/*    */ import ch.qos.logback.core.util.FileUtil;
/*    */ import groovy.lang.GroovyClassLoader;
/*    */ import groovy.lang.GroovyObject;
/*    */ import groovy.lang.Script;
/*    */ import org.codehaus.groovy.control.CompilationFailedException;
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
/*    */ public class GEventEvaluator
/*    */   extends EventEvaluatorBase<ILoggingEvent>
/*    */ {
/*    */   String expression;
/*    */   IEvaluator delegateEvaluator;
/*    */   Script script;
/*    */   
/*    */   public String getExpression()
/*    */   {
/* 34 */     return this.expression;
/*    */   }
/*    */   
/*    */   public void setExpression(String expression) {
/* 38 */     this.expression = expression;
/*    */   }
/*    */   
/*    */   public void start() {
/* 42 */     int errors = 0;
/* 43 */     if ((this.expression == null) || (this.expression.length() == 0)) {
/* 44 */       addError("Empty expression");
/* 45 */       return;
/*    */     }
/* 47 */     addInfo("Expression to evaluate [" + this.expression + "]");
/*    */     
/*    */ 
/*    */ 
/* 51 */     ClassLoader classLoader = getClass().getClassLoader();
/* 52 */     String currentPackageName = getClass().getPackage().getName();
/* 53 */     currentPackageName = currentPackageName.replace('.', '/');
/*    */     
/* 55 */     FileUtil fileUtil = new FileUtil(getContext());
/* 56 */     String scriptText = fileUtil.resourceAsString(classLoader, currentPackageName + "/EvaluatorTemplate.groovy");
/* 57 */     if (scriptText == null) {
/* 58 */       return;
/*    */     }
/*    */     
/*    */ 
/* 62 */     scriptText = scriptText.replace("//EXPRESSION", this.expression);
/*    */     
/* 64 */     GroovyClassLoader gLoader = new GroovyClassLoader(classLoader);
/*    */     try {
/* 66 */       Class scriptClass = gLoader.parseClass(scriptText);
/*    */       
/* 68 */       GroovyObject goo = (GroovyObject)scriptClass.newInstance();
/* 69 */       this.delegateEvaluator = ((IEvaluator)goo);
/*    */     }
/*    */     catch (CompilationFailedException cfe) {
/* 72 */       addError("Failed to compile expression [" + this.expression + "]", cfe);
/* 73 */       errors++;
/*    */     } catch (Exception e) {
/* 75 */       addError("Failed to compile expression [" + this.expression + "]", e);
/* 76 */       errors++;
/*    */     }
/* 78 */     if (errors == 0)
/* 79 */       super.start();
/*    */   }
/*    */   
/*    */   public boolean evaluate(ILoggingEvent event) throws NullPointerException, EvaluationException {
/* 83 */     if (this.delegateEvaluator == null) {
/* 84 */       return false;
/*    */     }
/* 86 */     return this.delegateEvaluator.doEvaluate(event);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\boolex\GEventEvaluator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */