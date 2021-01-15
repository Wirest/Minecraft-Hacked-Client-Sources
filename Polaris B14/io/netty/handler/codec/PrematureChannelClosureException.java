/*    */ package io.netty.handler.codec;
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
/*    */ public class PrematureChannelClosureException
/*    */   extends CodecException
/*    */ {
/*    */   private static final long serialVersionUID = 4907642202594703094L;
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
/*    */   public PrematureChannelClosureException() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public PrematureChannelClosureException(String message, Throwable cause)
/*    */   {
/* 38 */     super(message, cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public PrematureChannelClosureException(String message)
/*    */   {
/* 45 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public PrematureChannelClosureException(Throwable cause)
/*    */   {
/* 52 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\PrematureChannelClosureException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */