/*     */ package io.netty.resolver;
/*     */ 
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.Promise;
/*     */ import io.netty.util.internal.TypeParameterMatcher;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.UnsupportedAddressTypeException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SimpleNameResolver<T extends SocketAddress>
/*     */   implements NameResolver<T>
/*     */ {
/*     */   private final EventExecutor executor;
/*     */   private final TypeParameterMatcher matcher;
/*     */   
/*     */   protected SimpleNameResolver(EventExecutor executor)
/*     */   {
/*  41 */     if (executor == null) {
/*  42 */       throw new NullPointerException("executor");
/*     */     }
/*     */     
/*  45 */     this.executor = executor;
/*  46 */     this.matcher = TypeParameterMatcher.find(this, SimpleNameResolver.class, "T");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SimpleNameResolver(EventExecutor executor, Class<? extends T> addressType)
/*     */   {
/*  55 */     if (executor == null) {
/*  56 */       throw new NullPointerException("executor");
/*     */     }
/*     */     
/*  59 */     this.executor = executor;
/*  60 */     this.matcher = TypeParameterMatcher.get(addressType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected EventExecutor executor()
/*     */   {
/*  68 */     return this.executor;
/*     */   }
/*     */   
/*     */   public boolean isSupported(SocketAddress address)
/*     */   {
/*  73 */     return this.matcher.match(address);
/*     */   }
/*     */   
/*     */   public final boolean isResolved(SocketAddress address)
/*     */   {
/*  78 */     if (!isSupported(address)) {
/*  79 */       throw new UnsupportedAddressTypeException();
/*     */     }
/*     */     
/*     */ 
/*  83 */     T castAddress = address;
/*  84 */     return doIsResolved(castAddress);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract boolean doIsResolved(T paramT);
/*     */   
/*     */ 
/*     */ 
/*     */   public final Future<T> resolve(String inetHost, int inetPort)
/*     */   {
/*  95 */     if (inetHost == null) {
/*  96 */       throw new NullPointerException("inetHost");
/*     */     }
/*     */     
/*  99 */     return resolve(InetSocketAddress.createUnresolved(inetHost, inetPort));
/*     */   }
/*     */   
/*     */   public Future<T> resolve(String inetHost, int inetPort, Promise<T> promise)
/*     */   {
/* 104 */     if (inetHost == null) {
/* 105 */       throw new NullPointerException("inetHost");
/*     */     }
/*     */     
/* 108 */     return resolve(InetSocketAddress.createUnresolved(inetHost, inetPort), promise);
/*     */   }
/*     */   
/*     */   public final Future<T> resolve(SocketAddress address)
/*     */   {
/* 113 */     if (address == null) {
/* 114 */       throw new NullPointerException("unresolvedAddress");
/*     */     }
/*     */     
/* 117 */     if (!isSupported(address))
/*     */     {
/* 119 */       return executor().newFailedFuture(new UnsupportedAddressTypeException());
/*     */     }
/*     */     
/* 122 */     if (isResolved(address))
/*     */     {
/*     */ 
/* 125 */       T cast = address;
/* 126 */       return this.executor.newSucceededFuture(cast);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 131 */       T cast = address;
/* 132 */       Promise<T> promise = executor().newPromise();
/* 133 */       doResolve(cast, promise);
/* 134 */       return promise;
/*     */     } catch (Exception e) {
/* 136 */       return executor().newFailedFuture(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public final Future<T> resolve(SocketAddress address, Promise<T> promise)
/*     */   {
/* 142 */     if (address == null) {
/* 143 */       throw new NullPointerException("unresolvedAddress");
/*     */     }
/* 145 */     if (promise == null) {
/* 146 */       throw new NullPointerException("promise");
/*     */     }
/*     */     
/* 149 */     if (!isSupported(address))
/*     */     {
/* 151 */       return promise.setFailure(new UnsupportedAddressTypeException());
/*     */     }
/*     */     
/* 154 */     if (isResolved(address))
/*     */     {
/*     */ 
/* 157 */       T cast = address;
/* 158 */       return promise.setSuccess(cast);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 163 */       T cast = address;
/* 164 */       doResolve(cast, promise);
/* 165 */       return promise;
/*     */     } catch (Exception e) {
/* 167 */       return promise.setFailure(e);
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract void doResolve(T paramT, Promise<T> paramPromise)
/*     */     throws Exception;
/*     */   
/*     */   public void close() {}
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\resolver\SimpleNameResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */