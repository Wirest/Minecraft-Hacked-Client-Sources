/*    */ package io.netty.handler.ssl;
/*    */ 
/*    */ import javax.net.ssl.SSLException;
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
/*    */ public class NotSslRecordException
/*    */   extends SSLException
/*    */ {
/*    */   private static final long serialVersionUID = -4316784434770656841L;
/*    */   
/*    */   public NotSslRecordException()
/*    */   {
/* 33 */     super("");
/*    */   }
/*    */   
/*    */   public NotSslRecordException(String message) {
/* 37 */     super(message);
/*    */   }
/*    */   
/*    */   public NotSslRecordException(Throwable cause) {
/* 41 */     super(cause);
/*    */   }
/*    */   
/*    */   public NotSslRecordException(String message, Throwable cause) {
/* 45 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\NotSslRecordException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */