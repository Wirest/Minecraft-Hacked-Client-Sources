/*    */ package ch.qos.logback.core;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LogbackException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -799956346239073266L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public LogbackException(String msg)
/*    */   {
/* 21 */     super(msg);
/*    */   }
/*    */   
/*    */   public LogbackException(String msg, Throwable nested)
/*    */   {
/* 26 */     super(msg, nested);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\LogbackException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */