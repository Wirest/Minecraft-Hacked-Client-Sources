/*    */ package io.netty.channel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChannelException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 2908618315971075004L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ChannelException() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ChannelException(String message, Throwable cause)
/*    */   {
/* 35 */     super(message, cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public ChannelException(String message)
/*    */   {
/* 42 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public ChannelException(Throwable cause)
/*    */   {
/* 49 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\ChannelException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */