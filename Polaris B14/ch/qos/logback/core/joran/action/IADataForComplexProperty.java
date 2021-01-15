/*    */ package ch.qos.logback.core.joran.action;
/*    */ 
/*    */ import ch.qos.logback.core.joran.util.PropertySetter;
/*    */ import ch.qos.logback.core.util.AggregationType;
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
/*    */ public class IADataForComplexProperty
/*    */ {
/*    */   final PropertySetter parentBean;
/*    */   final AggregationType aggregationType;
/*    */   final String complexPropertyName;
/*    */   private Object nestedComplexProperty;
/*    */   boolean inError;
/*    */   
/*    */   public IADataForComplexProperty(PropertySetter parentBean, AggregationType aggregationType, String complexPropertyName)
/*    */   {
/* 32 */     this.parentBean = parentBean;
/* 33 */     this.aggregationType = aggregationType;
/* 34 */     this.complexPropertyName = complexPropertyName;
/*    */   }
/*    */   
/*    */   public AggregationType getAggregationType() {
/* 38 */     return this.aggregationType;
/*    */   }
/*    */   
/*    */   public Object getNestedComplexProperty() {
/* 42 */     return this.nestedComplexProperty;
/*    */   }
/*    */   
/*    */   public String getComplexPropertyName() {
/* 46 */     return this.complexPropertyName;
/*    */   }
/*    */   
/*    */   public void setNestedComplexProperty(Object nestedComplexProperty) {
/* 50 */     this.nestedComplexProperty = nestedComplexProperty;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\action\IADataForComplexProperty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */