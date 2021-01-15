/*     */ package ch.qos.logback.core.joran.action;
/*     */ 
/*     */ import ch.qos.logback.core.joran.spi.ActionException;
/*     */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*     */ import ch.qos.logback.core.spi.LifeCycle;
/*     */ import ch.qos.logback.core.spi.PropertyDefiner;
/*     */ import ch.qos.logback.core.util.OptionHelper;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefinePropertyAction
/*     */   extends Action
/*     */ {
/*     */   String scopeStr;
/*     */   ActionUtil.Scope scope;
/*     */   String propertyName;
/*     */   PropertyDefiner definer;
/*     */   boolean inError;
/*     */   
/*     */   public void begin(InterpretationContext ec, String localName, Attributes attributes)
/*     */     throws ActionException
/*     */   {
/*  42 */     this.scopeStr = null;
/*  43 */     this.scope = null;
/*  44 */     this.propertyName = null;
/*  45 */     this.definer = null;
/*  46 */     this.inError = false;
/*     */     
/*     */ 
/*  49 */     this.propertyName = attributes.getValue("name");
/*  50 */     this.scopeStr = attributes.getValue("scope");
/*     */     
/*  52 */     this.scope = ActionUtil.stringToScope(this.scopeStr);
/*  53 */     if (OptionHelper.isEmpty(this.propertyName)) {
/*  54 */       addError("Missing property name for property definer. Near [" + localName + "] line " + getLineNumber(ec));
/*     */       
/*  56 */       this.inError = true;
/*  57 */       return;
/*     */     }
/*     */     
/*     */ 
/*  61 */     String className = attributes.getValue("class");
/*  62 */     if (OptionHelper.isEmpty(className)) {
/*  63 */       addError("Missing class name for property definer. Near [" + localName + "] line " + getLineNumber(ec));
/*     */       
/*  65 */       this.inError = true;
/*  66 */       return;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*  71 */       addInfo("About to instantiate property definer of type [" + className + "]");
/*     */       
/*  73 */       this.definer = ((PropertyDefiner)OptionHelper.instantiateByClassName(className, PropertyDefiner.class, this.context));
/*     */       
/*  75 */       this.definer.setContext(this.context);
/*  76 */       if ((this.definer instanceof LifeCycle)) {
/*  77 */         ((LifeCycle)this.definer).start();
/*     */       }
/*  79 */       ec.pushObject(this.definer);
/*     */     } catch (Exception oops) {
/*  81 */       this.inError = true;
/*  82 */       addError("Could not create an PropertyDefiner of type [" + className + "].", oops);
/*     */       
/*  84 */       throw new ActionException(oops);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void end(InterpretationContext ec, String name)
/*     */   {
/*  93 */     if (this.inError) {
/*  94 */       return;
/*     */     }
/*     */     
/*  97 */     Object o = ec.peekObject();
/*     */     
/*  99 */     if (o != this.definer) {
/* 100 */       addWarn("The object at the of the stack is not the property definer for property named [" + this.propertyName + "] pushed earlier.");
/*     */     }
/*     */     else {
/* 103 */       addInfo("Popping property definer for property named [" + this.propertyName + "] from the object stack");
/*     */       
/* 105 */       ec.popObject();
/*     */       
/*     */ 
/* 108 */       String propertyValue = this.definer.getPropertyValue();
/* 109 */       if (propertyValue != null) {
/* 110 */         ActionUtil.setProperty(ec, this.propertyName, propertyValue, this.scope);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\action\DefinePropertyAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */