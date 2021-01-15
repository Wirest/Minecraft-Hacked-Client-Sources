/*    */ package ch.qos.logback.core.joran.conditional;
/*    */ 
/*    */ import ch.qos.logback.core.spi.PropertyContainer;
/*    */ import ch.qos.logback.core.util.OptionHelper;
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
/*    */ public class PropertyWrapperForScripts
/*    */ {
/*    */   PropertyContainer local;
/*    */   PropertyContainer context;
/*    */   
/*    */   public void setPropertyContainers(PropertyContainer local, PropertyContainer context)
/*    */   {
/* 26 */     this.local = local;
/* 27 */     this.context = context;
/*    */   }
/*    */   
/*    */   public boolean isNull(String k) {
/* 31 */     String val = OptionHelper.propertyLookup(k, this.local, this.context);
/* 32 */     return val == null;
/*    */   }
/*    */   
/*    */   public boolean isDefined(String k) {
/* 36 */     String val = OptionHelper.propertyLookup(k, this.local, this.context);
/* 37 */     return val != null;
/*    */   }
/*    */   
/*    */   public String p(String k) {
/* 41 */     return property(k);
/*    */   }
/*    */   
/*    */   public String property(String k) {
/* 45 */     String val = OptionHelper.propertyLookup(k, this.local, this.context);
/* 46 */     if (val != null) {
/* 47 */       return val;
/*    */     }
/* 49 */     return "";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\conditional\PropertyWrapperForScripts.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */