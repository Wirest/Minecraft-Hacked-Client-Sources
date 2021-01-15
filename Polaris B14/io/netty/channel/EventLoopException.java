/*    */ package io.netty.channel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EventLoopException
/*    */   extends ChannelException
/*    */ {
/*    */   private static final long serialVersionUID = -8969100344583703616L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public EventLoopException() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public EventLoopException(String message, Throwable cause)
/*    */   {
/* 30 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public EventLoopException(String message) {
/* 34 */     super(message);
/*    */   }
/*    */   
/*    */   public EventLoopException(Throwable cause) {
/* 38 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\EventLoopException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */