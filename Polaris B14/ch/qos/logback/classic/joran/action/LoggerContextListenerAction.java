/*    */ package ch.qos.logback.classic.joran.action;
/*    */ 
/*    */ import ch.qos.logback.classic.LoggerContext;
/*    */ import ch.qos.logback.classic.spi.LoggerContextListener;
/*    */ import ch.qos.logback.core.joran.action.Action;
/*    */ import ch.qos.logback.core.joran.spi.ActionException;
/*    */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*    */ import ch.qos.logback.core.spi.ContextAware;
/*    */ import ch.qos.logback.core.spi.LifeCycle;
/*    */ import ch.qos.logback.core.util.OptionHelper;
/*    */ import org.xml.sax.Attributes;
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
/*    */ public class LoggerContextListenerAction
/*    */   extends Action
/*    */ {
/* 28 */   boolean inError = false;
/*    */   
/*    */   LoggerContextListener lcl;
/*    */   
/*    */   public void begin(InterpretationContext ec, String name, Attributes attributes)
/*    */     throws ActionException
/*    */   {
/* 35 */     this.inError = false;
/*    */     
/* 37 */     String className = attributes.getValue("class");
/* 38 */     if (OptionHelper.isEmpty(className)) {
/* 39 */       addError("Mandatory \"class\" attribute not set for <loggerContextListener> element");
/*    */       
/* 41 */       this.inError = true;
/* 42 */       return;
/*    */     }
/*    */     try
/*    */     {
/* 46 */       this.lcl = ((LoggerContextListener)OptionHelper.instantiateByClassName(className, LoggerContextListener.class, this.context));
/*    */       
/*    */ 
/* 49 */       if ((this.lcl instanceof ContextAware)) {
/* 50 */         ((ContextAware)this.lcl).setContext(this.context);
/*    */       }
/*    */       
/* 53 */       ec.pushObject(this.lcl);
/* 54 */       addInfo("Adding LoggerContextListener of type [" + className + "] to the object stack");
/*    */     }
/*    */     catch (Exception oops)
/*    */     {
/* 58 */       this.inError = true;
/* 59 */       addError("Could not create LoggerContextListener of type " + className + "].", oops);
/*    */     }
/*    */   }
/*    */   
/*    */   public void end(InterpretationContext ec, String name) throws ActionException
/*    */   {
/* 65 */     if (this.inError) {
/* 66 */       return;
/*    */     }
/* 68 */     Object o = ec.peekObject();
/*    */     
/* 70 */     if (o != this.lcl) {
/* 71 */       addWarn("The object on the top the of the stack is not the LoggerContextListener pushed earlier.");
/*    */     } else {
/* 73 */       if ((this.lcl instanceof LifeCycle)) {
/* 74 */         ((LifeCycle)this.lcl).start();
/* 75 */         addInfo("Starting LoggerContextListener");
/*    */       }
/* 77 */       ((LoggerContext)this.context).addListener(this.lcl);
/* 78 */       ec.popObject();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\joran\action\LoggerContextListenerAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */