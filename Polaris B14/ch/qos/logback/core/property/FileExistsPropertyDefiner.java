/*    */ package ch.qos.logback.core.property;
/*    */ 
/*    */ import ch.qos.logback.core.PropertyDefinerBase;
/*    */ import ch.qos.logback.core.util.OptionHelper;
/*    */ import java.io.File;
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
/*    */ public class FileExistsPropertyDefiner
/*    */   extends PropertyDefinerBase
/*    */ {
/*    */   String path;
/*    */   
/*    */   public String getPath()
/*    */   {
/* 35 */     return this.path;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setPath(String path)
/*    */   {
/* 44 */     this.path = path;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getPropertyValue()
/*    */   {
/* 54 */     if (OptionHelper.isEmpty(this.path)) {
/* 55 */       addError("The \"path\" property must be set.");
/* 56 */       return null;
/*    */     }
/*    */     
/* 59 */     File file = new File(this.path);
/* 60 */     return booleanAsStr(file.exists());
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\property\FileExistsPropertyDefiner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */