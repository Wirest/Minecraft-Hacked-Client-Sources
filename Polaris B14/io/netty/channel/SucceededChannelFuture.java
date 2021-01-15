/*    */ package io.netty.channel;
/*    */ 
/*    */ import io.netty.util.concurrent.EventExecutor;
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
/*    */ 
/*    */ final class SucceededChannelFuture
/*    */   extends CompleteChannelFuture
/*    */ {
/*    */   SucceededChannelFuture(Channel channel, EventExecutor executor)
/*    */   {
/* 33 */     super(channel, executor);
/*    */   }
/*    */   
/*    */   public Throwable cause()
/*    */   {
/* 38 */     return null;
/*    */   }
/*    */   
/*    */   public boolean isSuccess()
/*    */   {
/* 43 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\SucceededChannelFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */