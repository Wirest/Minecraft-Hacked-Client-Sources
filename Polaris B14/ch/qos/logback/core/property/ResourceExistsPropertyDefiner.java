/*    */ package ch.qos.logback.core.property;
/*    */ 
/*    */ import ch.qos.logback.core.PropertyDefinerBase;
/*    */ import ch.qos.logback.core.util.Loader;
/*    */ import ch.qos.logback.core.util.OptionHelper;
/*    */ import java.net.URL;
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
/*    */ public class ResourceExistsPropertyDefiner
/*    */   extends PropertyDefinerBase
/*    */ {
/*    */   String resourceStr;
/*    */   
/*    */   public String getResource()
/*    */   {
/* 38 */     return this.resourceStr;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setResource(String resource)
/*    */   {
/* 47 */     this.resourceStr = resource;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getPropertyValue()
/*    */   {
/* 57 */     if (OptionHelper.isEmpty(this.resourceStr)) {
/* 58 */       addError("The \"resource\" property must be set.");
/* 59 */       return null;
/*    */     }
/*    */     
/* 62 */     URL resourceURL = Loader.getResourceBySelfClassLoader(this.resourceStr);
/* 63 */     return booleanAsStr(resourceURL != null);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\property\ResourceExistsPropertyDefiner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */