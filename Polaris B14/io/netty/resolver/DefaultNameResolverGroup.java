/*    */ package io.netty.resolver;
/*    */ 
/*    */ import io.netty.util.concurrent.EventExecutor;
/*    */ import java.net.InetSocketAddress;
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
/*    */ public final class DefaultNameResolverGroup
/*    */   extends NameResolverGroup<InetSocketAddress>
/*    */ {
/* 28 */   public static final DefaultNameResolverGroup INSTANCE = new DefaultNameResolverGroup();
/*    */   
/*    */ 
/*    */   protected NameResolver<InetSocketAddress> newResolver(EventExecutor executor)
/*    */     throws Exception
/*    */   {
/* 34 */     return new DefaultNameResolver(executor);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\resolver\DefaultNameResolverGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */