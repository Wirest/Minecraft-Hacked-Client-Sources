/*    */ package ch.qos.logback.core.util;
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
/*    */ public class PropertySetterException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = -2771077768281663949L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public PropertySetterException(String msg)
/*    */   {
/* 27 */     super(msg);
/*    */   }
/*    */   
/*    */   public PropertySetterException(Throwable rootCause) {
/* 31 */     super(rootCause);
/*    */   }
/*    */   
/*    */   public PropertySetterException(String message, Throwable cause) {
/* 35 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\util\PropertySetterException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */