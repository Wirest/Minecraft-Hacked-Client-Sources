/*     */ package ch.qos.logback.core.joran.action;
/*     */ 
/*     */ import ch.qos.logback.core.Appender;
/*     */ import ch.qos.logback.core.joran.spi.ActionException;
/*     */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*     */ import ch.qos.logback.core.spi.LifeCycle;
/*     */ import ch.qos.logback.core.util.OptionHelper;
/*     */ import java.util.HashMap;
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
/*     */ public class AppenderAction<E>
/*     */   extends Action
/*     */ {
/*     */   Appender<E> appender;
/*  28 */   private boolean inError = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void begin(InterpretationContext ec, String localName, Attributes attributes)
/*     */     throws ActionException
/*     */   {
/*  40 */     this.appender = null;
/*  41 */     this.inError = false;
/*     */     
/*  43 */     String className = attributes.getValue("class");
/*  44 */     if (OptionHelper.isEmpty(className)) {
/*  45 */       addError("Missing class name for appender. Near [" + localName + "] line " + getLineNumber(ec));
/*     */       
/*  47 */       this.inError = true;
/*  48 */       return;
/*     */     }
/*     */     try
/*     */     {
/*  52 */       addInfo("About to instantiate appender of type [" + className + "]");
/*     */       
/*  54 */       this.appender = ((Appender)OptionHelper.instantiateByClassName(className, Appender.class, this.context));
/*     */       
/*     */ 
/*  57 */       this.appender.setContext(this.context);
/*     */       
/*  59 */       String appenderName = ec.subst(attributes.getValue("name"));
/*     */       
/*  61 */       if (OptionHelper.isEmpty(appenderName)) {
/*  62 */         addWarn("No appender name given for appender of type " + className + "].");
/*     */       }
/*     */       else {
/*  65 */         this.appender.setName(appenderName);
/*  66 */         addInfo("Naming appender as [" + appenderName + "]");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*  71 */       HashMap<String, Appender<E>> appenderBag = (HashMap)ec.getObjectMap().get("APPENDER_BAG");
/*     */       
/*     */ 
/*     */ 
/*  75 */       appenderBag.put(appenderName, this.appender);
/*     */       
/*  77 */       ec.pushObject(this.appender);
/*     */     } catch (Exception oops) {
/*  79 */       this.inError = true;
/*  80 */       addError("Could not create an Appender of type [" + className + "].", oops);
/*     */       
/*  82 */       throw new ActionException(oops);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void end(InterpretationContext ec, String name)
/*     */   {
/*  91 */     if (this.inError) {
/*  92 */       return;
/*     */     }
/*     */     
/*  95 */     if ((this.appender instanceof LifeCycle)) {
/*  96 */       this.appender.start();
/*     */     }
/*     */     
/*  99 */     Object o = ec.peekObject();
/*     */     
/* 101 */     if (o != this.appender) {
/* 102 */       addWarn("The object at the of the stack is not the appender named [" + this.appender.getName() + "] pushed earlier.");
/*     */     }
/*     */     else {
/* 105 */       ec.popObject();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\action\AppenderAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */