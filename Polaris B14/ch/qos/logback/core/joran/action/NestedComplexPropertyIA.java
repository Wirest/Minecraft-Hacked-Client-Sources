/*     */ package ch.qos.logback.core.joran.action;
/*     */ 
/*     */ import ch.qos.logback.core.joran.spi.ElementPath;
/*     */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*     */ import ch.qos.logback.core.joran.spi.NoAutoStartUtil;
/*     */ import ch.qos.logback.core.joran.util.PropertySetter;
/*     */ import ch.qos.logback.core.spi.ContextAware;
/*     */ import ch.qos.logback.core.spi.LifeCycle;
/*     */ import ch.qos.logback.core.util.AggregationType;
/*     */ import ch.qos.logback.core.util.Loader;
/*     */ import ch.qos.logback.core.util.OptionHelper;
/*     */ import java.util.Stack;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NestedComplexPropertyIA
/*     */   extends ImplicitAction
/*     */ {
/*  45 */   Stack<IADataForComplexProperty> actionDataStack = new Stack();
/*     */   
/*     */ 
/*     */   public boolean isApplicable(ElementPath elementPath, Attributes attributes, InterpretationContext ic)
/*     */   {
/*  50 */     String nestedElementTagName = elementPath.peekLast();
/*     */     
/*     */ 
/*  53 */     if (ic.isEmpty()) {
/*  54 */       return false;
/*     */     }
/*     */     
/*  57 */     Object o = ic.peekObject();
/*  58 */     PropertySetter parentBean = new PropertySetter(o);
/*  59 */     parentBean.setContext(this.context);
/*     */     
/*  61 */     AggregationType aggregationType = parentBean.computeAggregationType(nestedElementTagName);
/*     */     
/*     */ 
/*  64 */     switch (aggregationType) {
/*     */     case NOT_FOUND: 
/*     */     case AS_BASIC_PROPERTY: 
/*     */     case AS_BASIC_PROPERTY_COLLECTION: 
/*  68 */       return false;
/*     */     
/*     */ 
/*     */     case AS_COMPLEX_PROPERTY_COLLECTION: 
/*     */     case AS_COMPLEX_PROPERTY: 
/*  73 */       IADataForComplexProperty ad = new IADataForComplexProperty(parentBean, aggregationType, nestedElementTagName);
/*     */       
/*  75 */       this.actionDataStack.push(ad);
/*     */       
/*  77 */       return true;
/*     */     }
/*  79 */     addError("PropertySetter.computeAggregationType returned " + aggregationType);
/*     */     
/*  81 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void begin(InterpretationContext ec, String localName, Attributes attributes)
/*     */   {
/*  89 */     IADataForComplexProperty actionData = (IADataForComplexProperty)this.actionDataStack.peek();
/*     */     
/*     */ 
/*  92 */     String className = attributes.getValue("class");
/*     */     
/*  94 */     className = ec.subst(className);
/*     */     
/*  96 */     Class<?> componentClass = null;
/*     */     try
/*     */     {
/*  99 */       if (!OptionHelper.isEmpty(className)) {
/* 100 */         componentClass = Loader.loadClass(className, this.context);
/*     */       }
/*     */       else {
/* 103 */         PropertySetter parentBean = actionData.parentBean;
/* 104 */         componentClass = parentBean.getClassNameViaImplicitRules(actionData.getComplexPropertyName(), actionData.getAggregationType(), ec.getDefaultNestedComponentRegistry());
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 109 */       if (componentClass == null) {
/* 110 */         actionData.inError = true;
/* 111 */         String errMsg = "Could not find an appropriate class for property [" + localName + "]";
/*     */         
/* 113 */         addError(errMsg);
/* 114 */         return;
/*     */       }
/*     */       
/* 117 */       if (OptionHelper.isEmpty(className)) {
/* 118 */         addInfo("Assuming default type [" + componentClass.getName() + "] for [" + localName + "] property");
/*     */       }
/*     */       
/*     */ 
/* 122 */       actionData.setNestedComplexProperty(componentClass.newInstance());
/*     */       
/*     */ 
/* 125 */       if ((actionData.getNestedComplexProperty() instanceof ContextAware)) {
/* 126 */         ((ContextAware)actionData.getNestedComplexProperty()).setContext(this.context);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 131 */       ec.pushObject(actionData.getNestedComplexProperty());
/*     */     }
/*     */     catch (Exception oops) {
/* 134 */       actionData.inError = true;
/* 135 */       String msg = "Could not create component [" + localName + "] of type [" + className + "]";
/*     */       
/* 137 */       addError(msg, oops);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void end(InterpretationContext ec, String tagName)
/*     */   {
/* 146 */     IADataForComplexProperty actionData = (IADataForComplexProperty)this.actionDataStack.pop();
/*     */     
/*     */ 
/* 149 */     if (actionData.inError) {
/* 150 */       return;
/*     */     }
/*     */     
/* 153 */     PropertySetter nestedBean = new PropertySetter(actionData.getNestedComplexProperty());
/*     */     
/* 155 */     nestedBean.setContext(this.context);
/*     */     
/*     */ 
/* 158 */     if (nestedBean.computeAggregationType("parent") == AggregationType.AS_COMPLEX_PROPERTY) {
/* 159 */       nestedBean.setComplexProperty("parent", actionData.parentBean.getObj());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 164 */     Object nestedComplexProperty = actionData.getNestedComplexProperty();
/* 165 */     if (((nestedComplexProperty instanceof LifeCycle)) && (NoAutoStartUtil.notMarkedWithNoAutoStart(nestedComplexProperty)))
/*     */     {
/* 167 */       ((LifeCycle)nestedComplexProperty).start();
/*     */     }
/*     */     
/* 170 */     Object o = ec.peekObject();
/*     */     
/* 172 */     if (o != actionData.getNestedComplexProperty()) {
/* 173 */       addError("The object on the top the of the stack is not the component pushed earlier.");
/*     */     } else {
/* 175 */       ec.popObject();
/*     */       
/* 177 */       switch (actionData.aggregationType) {
/*     */       case AS_COMPLEX_PROPERTY: 
/* 179 */         actionData.parentBean.setComplexProperty(tagName, actionData.getNestedComplexProperty());
/*     */         
/*     */ 
/* 182 */         break;
/*     */       case AS_COMPLEX_PROPERTY_COLLECTION: 
/* 184 */         actionData.parentBean.addComplexProperty(tagName, actionData.getNestedComplexProperty());
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\action\NestedComplexPropertyIA.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */