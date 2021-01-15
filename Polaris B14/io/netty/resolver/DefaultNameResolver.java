/*    */ package io.netty.resolver;
/*    */ 
/*    */ import io.netty.util.concurrent.EventExecutor;
/*    */ import io.netty.util.concurrent.Promise;
/*    */ import java.net.InetAddress;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.UnknownHostException;
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
/*    */ public class DefaultNameResolver
/*    */   extends SimpleNameResolver<InetSocketAddress>
/*    */ {
/*    */   public DefaultNameResolver(EventExecutor executor)
/*    */   {
/* 33 */     super(executor);
/*    */   }
/*    */   
/*    */   protected boolean doIsResolved(InetSocketAddress address)
/*    */   {
/* 38 */     return !address.isUnresolved();
/*    */   }
/*    */   
/*    */   protected void doResolve(InetSocketAddress unresolvedAddress, Promise<InetSocketAddress> promise) throws Exception
/*    */   {
/*    */     try {
/* 44 */       promise.setSuccess(new InetSocketAddress(InetAddress.getByName(unresolvedAddress.getHostString()), unresolvedAddress.getPort()));
/*    */ 
/*    */     }
/*    */     catch (UnknownHostException e)
/*    */     {
/* 49 */       promise.setFailure(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\resolver\DefaultNameResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */