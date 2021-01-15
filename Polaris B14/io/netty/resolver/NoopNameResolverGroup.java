/*    */ package io.netty.resolver;
/*    */ 
/*    */ import io.netty.util.concurrent.EventExecutor;
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
/*    */ public final class NoopNameResolverGroup
/*    */   extends NameResolverGroup<SocketAddress>
/*    */ {
/* 28 */   public static final NoopNameResolverGroup INSTANCE = new NoopNameResolverGroup();
/*    */   
/*    */ 
/*    */   protected NameResolver<SocketAddress> newResolver(EventExecutor executor)
/*    */     throws Exception
/*    */   {
/* 34 */     return new NoopNameResolver(executor);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\resolver\NoopNameResolverGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */