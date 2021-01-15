/*    */ package ch.qos.logback.core.status;
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
/*    */ public class ErrorStatus
/*    */   extends StatusBase
/*    */ {
/*    */   public ErrorStatus(String msg, Object origin)
/*    */   {
/* 21 */     super(2, msg, origin);
/*    */   }
/*    */   
/*    */   public ErrorStatus(String msg, Object origin, Throwable t) {
/* 25 */     super(2, msg, origin, t);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\status\ErrorStatus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */