/*    */ package io.netty.channel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class DefaultChannelHandlerContext
/*    */   extends AbstractChannelHandlerContext
/*    */ {
/*    */   private final ChannelHandler handler;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   DefaultChannelHandlerContext(DefaultChannelPipeline pipeline, ChannelHandlerInvoker invoker, String name, ChannelHandler handler)
/*    */   {
/* 23 */     super(pipeline, invoker, name, skipFlags(checkNull(handler)));
/* 24 */     this.handler = handler;
/*    */   }
/*    */   
/*    */   private static ChannelHandler checkNull(ChannelHandler handler) {
/* 28 */     if (handler == null) {
/* 29 */       throw new NullPointerException("handler");
/*    */     }
/* 31 */     return handler;
/*    */   }
/*    */   
/*    */   public ChannelHandler handler()
/*    */   {
/* 36 */     return this.handler;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\DefaultChannelHandlerContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */