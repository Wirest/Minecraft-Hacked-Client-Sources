/*    */ package io.netty.resolver;
/*    */ 
/*    */ import io.netty.util.concurrent.EventExecutor;
/*    */ import io.netty.util.concurrent.Promise;
/*    */ import java.net.SocketAddress;
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
/*    */ public class NoopNameResolver
/*    */   extends SimpleNameResolver<SocketAddress>
/*    */ {
/*    */   public NoopNameResolver(EventExecutor executor)
/*    */   {
/* 31 */     super(executor);
/*    */   }
/*    */   
/*    */   protected boolean doIsResolved(SocketAddress address)
/*    */   {
/* 36 */     return true;
/*    */   }
/*    */   
/*    */   protected void doResolve(SocketAddress unresolvedAddress, Promise<SocketAddress> promise) throws Exception
/*    */   {
/* 41 */     promise.setSuccess(unresolvedAddress);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\resolver\NoopNameResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */