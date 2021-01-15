/*    */ package io.netty.handler.codec.haproxy;
/*    */ 
/*    */ import io.netty.handler.codec.DecoderException;
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
/*    */ public class HAProxyProtocolException
/*    */   extends DecoderException
/*    */ {
/*    */   private static final long serialVersionUID = 713710864325167351L;
/*    */   
/*    */   public HAProxyProtocolException() {}
/*    */   
/*    */   public HAProxyProtocolException(String message, Throwable cause)
/*    */   {
/* 36 */     super(message, cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public HAProxyProtocolException(String message)
/*    */   {
/* 43 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public HAProxyProtocolException(Throwable cause)
/*    */   {
/* 50 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\haproxy\HAProxyProtocolException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */