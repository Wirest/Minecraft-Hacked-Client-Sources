/*    */ package ch.qos.logback.core.spi;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ScanException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = -3132040414328475658L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   Throwable cause;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ScanException(String msg)
/*    */   {
/* 23 */     super(msg);
/*    */   }
/*    */   
/*    */   public ScanException(String msg, Throwable rootCause) {
/* 27 */     super(msg);
/* 28 */     this.cause = rootCause;
/*    */   }
/*    */   
/*    */   public Throwable getCause() {
/* 32 */     return this.cause;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\spi\ScanException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */