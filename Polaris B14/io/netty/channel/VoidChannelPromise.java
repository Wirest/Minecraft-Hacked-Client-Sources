/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.util.concurrent.AbstractFuture;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ final class VoidChannelPromise
/*     */   extends AbstractFuture<Void>
/*     */   implements ChannelPromise
/*     */ {
/*     */   private final Channel channel;
/*     */   private final boolean fireException;
/*     */   
/*     */   VoidChannelPromise(Channel channel, boolean fireException)
/*     */   {
/*  35 */     if (channel == null) {
/*  36 */       throw new NullPointerException("channel");
/*     */     }
/*  38 */     this.channel = channel;
/*  39 */     this.fireException = fireException;
/*     */   }
/*     */   
/*     */   public VoidChannelPromise addListener(GenericFutureListener<? extends Future<? super Void>> listener)
/*     */   {
/*  44 */     fail();
/*  45 */     return this;
/*     */   }
/*     */   
/*     */   public VoidChannelPromise addListeners(GenericFutureListener<? extends Future<? super Void>>... listeners)
/*     */   {
/*  50 */     fail();
/*  51 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public VoidChannelPromise removeListener(GenericFutureListener<? extends Future<? super Void>> listener)
/*     */   {
/*  57 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public VoidChannelPromise removeListeners(GenericFutureListener<? extends Future<? super Void>>... listeners)
/*     */   {
/*  63 */     return this;
/*     */   }
/*     */   
/*     */   public VoidChannelPromise await() throws InterruptedException
/*     */   {
/*  68 */     if (Thread.interrupted()) {
/*  69 */       throw new InterruptedException();
/*     */     }
/*  71 */     return this;
/*     */   }
/*     */   
/*     */   public boolean await(long timeout, TimeUnit unit)
/*     */   {
/*  76 */     fail();
/*  77 */     return false;
/*     */   }
/*     */   
/*     */   public boolean await(long timeoutMillis)
/*     */   {
/*  82 */     fail();
/*  83 */     return false;
/*     */   }
/*     */   
/*     */   public VoidChannelPromise awaitUninterruptibly()
/*     */   {
/*  88 */     fail();
/*  89 */     return this;
/*     */   }
/*     */   
/*     */   public boolean awaitUninterruptibly(long timeout, TimeUnit unit)
/*     */   {
/*  94 */     fail();
/*  95 */     return false;
/*     */   }
/*     */   
/*     */   public boolean awaitUninterruptibly(long timeoutMillis)
/*     */   {
/* 100 */     fail();
/* 101 */     return false;
/*     */   }
/*     */   
/*     */   public Channel channel()
/*     */   {
/* 106 */     return this.channel;
/*     */   }
/*     */   
/*     */   public boolean isDone()
/*     */   {
/* 111 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isSuccess()
/*     */   {
/* 116 */     return false;
/*     */   }
/*     */   
/*     */   public boolean setUncancellable()
/*     */   {
/* 121 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isCancellable()
/*     */   {
/* 126 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isCancelled()
/*     */   {
/* 131 */     return false;
/*     */   }
/*     */   
/*     */   public Throwable cause()
/*     */   {
/* 136 */     return null;
/*     */   }
/*     */   
/*     */   public VoidChannelPromise sync()
/*     */   {
/* 141 */     fail();
/* 142 */     return this;
/*     */   }
/*     */   
/*     */   public VoidChannelPromise syncUninterruptibly()
/*     */   {
/* 147 */     fail();
/* 148 */     return this;
/*     */   }
/*     */   
/*     */   public VoidChannelPromise setFailure(Throwable cause) {
/* 152 */     fireException(cause);
/* 153 */     return this;
/*     */   }
/*     */   
/*     */   public VoidChannelPromise setSuccess()
/*     */   {
/* 158 */     return this;
/*     */   }
/*     */   
/*     */   public boolean tryFailure(Throwable cause)
/*     */   {
/* 163 */     fireException(cause);
/* 164 */     return false;
/*     */   }
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning)
/*     */   {
/* 169 */     return false;
/*     */   }
/*     */   
/*     */   public boolean trySuccess()
/*     */   {
/* 174 */     return false;
/*     */   }
/*     */   
/*     */   private static void fail() {
/* 178 */     throw new IllegalStateException("void future");
/*     */   }
/*     */   
/*     */   public VoidChannelPromise setSuccess(Void result)
/*     */   {
/* 183 */     return this;
/*     */   }
/*     */   
/*     */   public boolean trySuccess(Void result)
/*     */   {
/* 188 */     return false;
/*     */   }
/*     */   
/*     */   public Void getNow()
/*     */   {
/* 193 */     return null;
/*     */   }
/*     */   
/*     */   public ChannelPromise unvoid()
/*     */   {
/* 198 */     ChannelPromise promise = new DefaultChannelPromise(this.channel);
/* 199 */     if (this.fireException) {
/* 200 */       promise.addListener(new ChannelFutureListener()
/*     */       {
/*     */         public void operationComplete(ChannelFuture future) throws Exception {
/* 203 */           if (!future.isSuccess()) {
/* 204 */             VoidChannelPromise.this.fireException(future.cause());
/*     */           }
/*     */         }
/*     */       });
/*     */     }
/* 209 */     return promise;
/*     */   }
/*     */   
/*     */   public boolean isVoid()
/*     */   {
/* 214 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void fireException(Throwable cause)
/*     */   {
/* 222 */     if ((this.fireException) && (this.channel.isRegistered())) {
/* 223 */       this.channel.pipeline().fireExceptionCaught(cause);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\VoidChannelPromise.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */