/*    */ package ch.qos.logback.core.joran.action;
/*    */ 
/*    */ import ch.qos.logback.core.Context;
/*    */ import ch.qos.logback.core.joran.spi.ActionException;
/*    */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*    */ import ch.qos.logback.core.spi.ContextAware;
/*    */ import ch.qos.logback.core.spi.LifeCycle;
/*    */ import ch.qos.logback.core.status.StatusListener;
/*    */ import ch.qos.logback.core.status.StatusManager;
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
/*    */ 
/*    */ public class StatusListenerAction
/*    */   extends Action
/*    */ {
/* 29 */   boolean inError = false;
/* 30 */   StatusListener statusListener = null;
/*    */   
/*    */   public void begin(InterpretationContext ec, String name, Attributes attributes) throws ActionException {
/* 33 */     this.inError = false;
/* 34 */     String className = attributes.getValue("class");
/* 35 */     if (OptionHelper.isEmpty(className)) {
/* 36 */       addError("Missing class name for statusListener. Near [" + name + "] line " + getLineNumber(ec));
/*    */       
/* 38 */       this.inError = true;
/* 39 */       return;
/*    */     }
/*    */     try
/*    */     {
/* 43 */       this.statusListener = ((StatusListener)OptionHelper.instantiateByClassName(className, StatusListener.class, this.context));
/*    */       
/* 45 */       ec.getContext().getStatusManager().add(this.statusListener);
/* 46 */       if ((this.statusListener instanceof ContextAware)) {
/* 47 */         ((ContextAware)this.statusListener).setContext(this.context);
/*    */       }
/* 49 */       addInfo("Added status listener of type [" + className + "]");
/* 50 */       ec.pushObject(this.statusListener);
/*    */     } catch (Exception e) {
/* 52 */       this.inError = true;
/* 53 */       addError("Could not create an StatusListener of type [" + className + "].", e);
/*    */       
/* 55 */       throw new ActionException(e);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public void finish(InterpretationContext ec) {}
/*    */   
/*    */   public void end(InterpretationContext ec, String e)
/*    */   {
/* 64 */     if (this.inError) {
/* 65 */       return;
/*    */     }
/* 67 */     if ((this.statusListener instanceof LifeCycle)) {
/* 68 */       ((LifeCycle)this.statusListener).start();
/*    */     }
/* 70 */     Object o = ec.peekObject();
/* 71 */     if (o != this.statusListener) {
/* 72 */       addWarn("The object at the of the stack is not the statusListener pushed earlier.");
/*    */     } else {
/* 74 */       ec.popObject();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\action\StatusListenerAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */