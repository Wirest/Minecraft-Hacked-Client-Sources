/*     */ package io.netty.resolver;
/*     */ 
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.FutureListener;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.Closeable;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Collection;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class NameResolverGroup<T extends SocketAddress>
/*     */   implements Closeable
/*     */ {
/*  36 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NameResolverGroup.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  41 */   private final Map<EventExecutor, NameResolver<T>> resolvers = new IdentityHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NameResolver<T> getResolver(EventExecutor executor)
/*     */   {
/*  53 */     if (executor == null) {
/*  54 */       throw new NullPointerException("executor");
/*     */     }
/*     */     
/*  57 */     if (executor.isShuttingDown()) {
/*  58 */       throw new IllegalStateException("executor not accepting a task");
/*     */     }
/*     */     
/*  61 */     return getResolver0(executor.unwrap());
/*     */   }
/*     */   
/*     */   private NameResolver<T> getResolver0(final EventExecutor executor) {
/*     */     NameResolver<T> r;
/*  66 */     synchronized (this.resolvers) {
/*  67 */       r = (NameResolver)this.resolvers.get(executor);
/*  68 */       if (r == null) {
/*     */         final NameResolver<T> newResolver;
/*     */         try {
/*  71 */           newResolver = newResolver(executor);
/*     */         } catch (Exception e) {
/*  73 */           throw new IllegalStateException("failed to create a new resolver", e);
/*     */         }
/*     */         
/*  76 */         this.resolvers.put(executor, newResolver);
/*  77 */         executor.terminationFuture().addListener(new FutureListener()
/*     */         {
/*     */           public void operationComplete(Future<Object> future) throws Exception {
/*  80 */             NameResolverGroup.this.resolvers.remove(executor);
/*  81 */             newResolver.close();
/*     */           }
/*     */           
/*  84 */         });
/*  85 */         r = newResolver;
/*     */       }
/*     */     }
/*     */     
/*  89 */     return r;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract NameResolver<T> newResolver(EventExecutor paramEventExecutor)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */   public void close()
/*     */   {
/*     */     NameResolver<T>[] rArray;
/*     */     
/*     */ 
/* 104 */     synchronized (this.resolvers) {
/* 105 */       rArray = (NameResolver[])this.resolvers.values().toArray(new NameResolver[this.resolvers.size()]);
/* 106 */       this.resolvers.clear();
/*     */     }
/*     */     
/* 109 */     for (NameResolver<T> r : rArray) {
/*     */       try {
/* 111 */         r.close();
/*     */       } catch (Throwable t) {
/* 113 */         logger.warn("Failed to close a resolver:", t);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\resolver\NameResolverGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */