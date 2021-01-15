/*    */ package ch.qos.logback.core.joran.action;
/*    */ 
/*    */ import ch.qos.logback.core.Context;
/*    */ import ch.qos.logback.core.hook.ShutdownHookBase;
/*    */ import ch.qos.logback.core.joran.spi.ActionException;
/*    */ import ch.qos.logback.core.joran.spi.InterpretationContext;
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
/*    */ public class ShutdownHookAction
/*    */   extends Action
/*    */ {
/*    */   ShutdownHookBase hook;
/*    */   private boolean inError;
/*    */   
/*    */   public void begin(InterpretationContext ic, String name, Attributes attributes)
/*    */     throws ActionException
/*    */   {
/* 42 */     this.hook = null;
/* 43 */     this.inError = false;
/*    */     
/* 45 */     String className = attributes.getValue("class");
/* 46 */     if (OptionHelper.isEmpty(className)) {
/* 47 */       addError("Missing class name for shutdown hook. Near [" + name + "] line " + getLineNumber(ic));
/*    */       
/* 49 */       this.inError = true;
/* 50 */       return;
/*    */     }
/*    */     try
/*    */     {
/* 54 */       addInfo("About to instantiate shutdown hook of type [" + className + "]");
/*    */       
/* 56 */       this.hook = ((ShutdownHookBase)OptionHelper.instantiateByClassName(className, ShutdownHookBase.class, this.context));
/*    */       
/* 58 */       this.hook.setContext(this.context);
/*    */       
/* 60 */       ic.pushObject(this.hook);
/*    */     } catch (Exception e) {
/* 62 */       this.inError = true;
/* 63 */       addError("Could not create a shutdown hook of type [" + className + "].", e);
/* 64 */       throw new ActionException(e);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void end(InterpretationContext ic, String name)
/*    */     throws ActionException
/*    */   {
/* 74 */     if (this.inError) {
/* 75 */       return;
/*    */     }
/*    */     
/* 78 */     Object o = ic.peekObject();
/* 79 */     if (o != this.hook) {
/* 80 */       addWarn("The object at the of the stack is not the hook pushed earlier.");
/*    */     } else {
/* 82 */       ic.popObject();
/*    */       
/* 84 */       Thread hookThread = new Thread(this.hook, "Logback shutdown hook [" + this.context.getName() + "]");
/*    */       
/* 86 */       this.context.putObject("SHUTDOWN_HOOK", hookThread);
/* 87 */       Runtime.getRuntime().addShutdownHook(hookThread);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\action\ShutdownHookAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */