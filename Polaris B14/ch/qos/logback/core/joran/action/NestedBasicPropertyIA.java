/*     */ package ch.qos.logback.core.joran.action;
/*     */ 
/*     */ import ch.qos.logback.core.joran.spi.ElementPath;
/*     */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*     */ import ch.qos.logback.core.joran.util.PropertySetter;
/*     */ import ch.qos.logback.core.util.AggregationType;
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
/*     */ 
/*     */ public class NestedBasicPropertyIA
/*     */   extends ImplicitAction
/*     */ {
/*  41 */   Stack<IADataForBasicProperty> actionDataStack = new Stack();
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isApplicable(ElementPath elementPath, Attributes attributes, InterpretationContext ec)
/*     */   {
/*  47 */     String nestedElementTagName = elementPath.peekLast();
/*     */     
/*     */ 
/*  50 */     if (ec.isEmpty()) {
/*  51 */       return false;
/*     */     }
/*     */     
/*  54 */     Object o = ec.peekObject();
/*  55 */     PropertySetter parentBean = new PropertySetter(o);
/*  56 */     parentBean.setContext(this.context);
/*     */     
/*  58 */     AggregationType aggregationType = parentBean.computeAggregationType(nestedElementTagName);
/*     */     
/*     */ 
/*  61 */     switch (aggregationType) {
/*     */     case NOT_FOUND: 
/*     */     case AS_COMPLEX_PROPERTY: 
/*     */     case AS_COMPLEX_PROPERTY_COLLECTION: 
/*  65 */       return false;
/*     */     
/*     */     case AS_BASIC_PROPERTY: 
/*     */     case AS_BASIC_PROPERTY_COLLECTION: 
/*  69 */       IADataForBasicProperty ad = new IADataForBasicProperty(parentBean, aggregationType, nestedElementTagName);
/*     */       
/*  71 */       this.actionDataStack.push(ad);
/*     */       
/*  73 */       return true;
/*     */     }
/*  75 */     addError("PropertySetter.canContainComponent returned " + aggregationType);
/*  76 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void begin(InterpretationContext ec, String localName, Attributes attributes) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void body(InterpretationContext ec, String body)
/*     */   {
/*  87 */     String finalBody = ec.subst(body);
/*     */     
/*  89 */     IADataForBasicProperty actionData = (IADataForBasicProperty)this.actionDataStack.peek();
/*  90 */     switch (actionData.aggregationType) {
/*     */     case AS_BASIC_PROPERTY: 
/*  92 */       actionData.parentBean.setProperty(actionData.propertyName, finalBody);
/*  93 */       break;
/*     */     case AS_BASIC_PROPERTY_COLLECTION: 
/*  95 */       actionData.parentBean.addBasicProperty(actionData.propertyName, finalBody);
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */   public void end(InterpretationContext ec, String tagName)
/*     */   {
/* 102 */     this.actionDataStack.pop();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\action\NestedBasicPropertyIA.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */