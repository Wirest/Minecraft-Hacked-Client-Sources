/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.util.Recycler;
/*     */ import io.netty.util.Recycler.Handle;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.internal.OneTimeTask;
/*     */ import io.netty.util.internal.RecyclableMpscLinkedQueueNode;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import java.net.SocketAddress;
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
/*     */ public class DefaultChannelHandlerInvoker
/*     */   implements ChannelHandlerInvoker
/*     */ {
/*     */   private final EventExecutor executor;
/*     */   
/*     */   public DefaultChannelHandlerInvoker(EventExecutor executor)
/*     */   {
/*  35 */     if (executor == null) {
/*  36 */       throw new NullPointerException("executor");
/*     */     }
/*     */     
/*  39 */     this.executor = executor;
/*     */   }
/*     */   
/*     */   public EventExecutor executor()
/*     */   {
/*  44 */     return this.executor;
/*     */   }
/*     */   
/*     */   public void invokeChannelRegistered(final ChannelHandlerContext ctx)
/*     */   {
/*  49 */     if (this.executor.inEventLoop()) {
/*  50 */       ChannelHandlerInvokerUtil.invokeChannelRegisteredNow(ctx);
/*     */     } else {
/*  52 */       this.executor.execute(new OneTimeTask()
/*     */       {
/*     */         public void run() {
/*  55 */           ChannelHandlerInvokerUtil.invokeChannelRegisteredNow(ctx);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   public void invokeChannelUnregistered(final ChannelHandlerContext ctx)
/*     */   {
/*  63 */     if (this.executor.inEventLoop()) {
/*  64 */       ChannelHandlerInvokerUtil.invokeChannelUnregisteredNow(ctx);
/*     */     } else {
/*  66 */       this.executor.execute(new OneTimeTask()
/*     */       {
/*     */         public void run() {
/*  69 */           ChannelHandlerInvokerUtil.invokeChannelUnregisteredNow(ctx);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   public void invokeChannelActive(final ChannelHandlerContext ctx)
/*     */   {
/*  77 */     if (this.executor.inEventLoop()) {
/*  78 */       ChannelHandlerInvokerUtil.invokeChannelActiveNow(ctx);
/*     */     } else {
/*  80 */       this.executor.execute(new OneTimeTask()
/*     */       {
/*     */         public void run() {
/*  83 */           ChannelHandlerInvokerUtil.invokeChannelActiveNow(ctx);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   public void invokeChannelInactive(final ChannelHandlerContext ctx)
/*     */   {
/*  91 */     if (this.executor.inEventLoop()) {
/*  92 */       ChannelHandlerInvokerUtil.invokeChannelInactiveNow(ctx);
/*     */     } else {
/*  94 */       this.executor.execute(new OneTimeTask()
/*     */       {
/*     */         public void run() {
/*  97 */           ChannelHandlerInvokerUtil.invokeChannelInactiveNow(ctx);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   public void invokeExceptionCaught(final ChannelHandlerContext ctx, final Throwable cause)
/*     */   {
/* 105 */     if (cause == null) {
/* 106 */       throw new NullPointerException("cause");
/*     */     }
/*     */     
/* 109 */     if (this.executor.inEventLoop()) {
/* 110 */       ChannelHandlerInvokerUtil.invokeExceptionCaughtNow(ctx, cause);
/*     */     } else {
/*     */       try {
/* 113 */         this.executor.execute(new OneTimeTask()
/*     */         {
/*     */           public void run() {
/* 116 */             ChannelHandlerInvokerUtil.invokeExceptionCaughtNow(ctx, cause);
/*     */           }
/*     */         });
/*     */       } catch (Throwable t) {
/* 120 */         if (DefaultChannelPipeline.logger.isWarnEnabled()) {
/* 121 */           DefaultChannelPipeline.logger.warn("Failed to submit an exceptionCaught() event.", t);
/* 122 */           DefaultChannelPipeline.logger.warn("The exceptionCaught() event that was failed to submit was:", cause);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void invokeUserEventTriggered(final ChannelHandlerContext ctx, final Object event)
/*     */   {
/* 130 */     if (event == null) {
/* 131 */       throw new NullPointerException("event");
/*     */     }
/*     */     
/* 134 */     if (this.executor.inEventLoop()) {
/* 135 */       ChannelHandlerInvokerUtil.invokeUserEventTriggeredNow(ctx, event);
/*     */     } else {
/* 137 */       safeExecuteInbound(new OneTimeTask()
/*     */       {
/*     */ 
/* 140 */         public void run() { ChannelHandlerInvokerUtil.invokeUserEventTriggeredNow(ctx, event); } }, event);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void invokeChannelRead(final ChannelHandlerContext ctx, final Object msg)
/*     */   {
/* 148 */     if (msg == null) {
/* 149 */       throw new NullPointerException("msg");
/*     */     }
/*     */     
/* 152 */     if (this.executor.inEventLoop()) {
/* 153 */       ChannelHandlerInvokerUtil.invokeChannelReadNow(ctx, msg);
/*     */     } else {
/* 155 */       safeExecuteInbound(new OneTimeTask()
/*     */       {
/*     */ 
/* 158 */         public void run() { ChannelHandlerInvokerUtil.invokeChannelReadNow(ctx, msg); } }, msg);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void invokeChannelReadComplete(final ChannelHandlerContext ctx)
/*     */   {
/* 166 */     if (this.executor.inEventLoop()) {
/* 167 */       ChannelHandlerInvokerUtil.invokeChannelReadCompleteNow(ctx);
/*     */     } else {
/* 169 */       AbstractChannelHandlerContext dctx = (AbstractChannelHandlerContext)ctx;
/* 170 */       Runnable task = dctx.invokeChannelReadCompleteTask;
/* 171 */       if (task == null) {
/* 172 */         dctx.invokeChannelReadCompleteTask = ( = new Runnable()
/*     */         {
/*     */           public void run() {
/* 175 */             ChannelHandlerInvokerUtil.invokeChannelReadCompleteNow(ctx);
/*     */           }
/*     */         });
/*     */       }
/* 179 */       this.executor.execute(task);
/*     */     }
/*     */   }
/*     */   
/*     */   public void invokeChannelWritabilityChanged(final ChannelHandlerContext ctx)
/*     */   {
/* 185 */     if (this.executor.inEventLoop()) {
/* 186 */       ChannelHandlerInvokerUtil.invokeChannelWritabilityChangedNow(ctx);
/*     */     } else {
/* 188 */       AbstractChannelHandlerContext dctx = (AbstractChannelHandlerContext)ctx;
/* 189 */       Runnable task = dctx.invokeChannelWritableStateChangedTask;
/* 190 */       if (task == null) {
/* 191 */         dctx.invokeChannelWritableStateChangedTask = ( = new Runnable()
/*     */         {
/*     */           public void run() {
/* 194 */             ChannelHandlerInvokerUtil.invokeChannelWritabilityChangedNow(ctx);
/*     */           }
/*     */         });
/*     */       }
/* 198 */       this.executor.execute(task);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void invokeBind(final ChannelHandlerContext ctx, final SocketAddress localAddress, final ChannelPromise promise)
/*     */   {
/* 205 */     if (localAddress == null) {
/* 206 */       throw new NullPointerException("localAddress");
/*     */     }
/* 208 */     if (!ChannelHandlerInvokerUtil.validatePromise(ctx, promise, false))
/*     */     {
/* 210 */       return;
/*     */     }
/*     */     
/* 213 */     if (this.executor.inEventLoop()) {
/* 214 */       ChannelHandlerInvokerUtil.invokeBindNow(ctx, localAddress, promise);
/*     */     } else {
/* 216 */       safeExecuteOutbound(new OneTimeTask()
/*     */       {
/*     */ 
/* 219 */         public void run() { ChannelHandlerInvokerUtil.invokeBindNow(ctx, localAddress, promise); } }, promise);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void invokeConnect(final ChannelHandlerContext ctx, final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise)
/*     */   {
/* 229 */     if (remoteAddress == null) {
/* 230 */       throw new NullPointerException("remoteAddress");
/*     */     }
/* 232 */     if (!ChannelHandlerInvokerUtil.validatePromise(ctx, promise, false))
/*     */     {
/* 234 */       return;
/*     */     }
/*     */     
/* 237 */     if (this.executor.inEventLoop()) {
/* 238 */       ChannelHandlerInvokerUtil.invokeConnectNow(ctx, remoteAddress, localAddress, promise);
/*     */     } else {
/* 240 */       safeExecuteOutbound(new OneTimeTask()
/*     */       {
/*     */ 
/* 243 */         public void run() { ChannelHandlerInvokerUtil.invokeConnectNow(ctx, remoteAddress, localAddress, promise); } }, promise);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void invokeDisconnect(final ChannelHandlerContext ctx, final ChannelPromise promise)
/*     */   {
/* 251 */     if (!ChannelHandlerInvokerUtil.validatePromise(ctx, promise, false))
/*     */     {
/* 253 */       return;
/*     */     }
/*     */     
/* 256 */     if (this.executor.inEventLoop()) {
/* 257 */       ChannelHandlerInvokerUtil.invokeDisconnectNow(ctx, promise);
/*     */     } else {
/* 259 */       safeExecuteOutbound(new OneTimeTask()
/*     */       {
/*     */ 
/* 262 */         public void run() { ChannelHandlerInvokerUtil.invokeDisconnectNow(ctx, promise); } }, promise);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void invokeClose(final ChannelHandlerContext ctx, final ChannelPromise promise)
/*     */   {
/* 270 */     if (!ChannelHandlerInvokerUtil.validatePromise(ctx, promise, false))
/*     */     {
/* 272 */       return;
/*     */     }
/*     */     
/* 275 */     if (this.executor.inEventLoop()) {
/* 276 */       ChannelHandlerInvokerUtil.invokeCloseNow(ctx, promise);
/*     */     } else {
/* 278 */       safeExecuteOutbound(new OneTimeTask()
/*     */       {
/*     */ 
/* 281 */         public void run() { ChannelHandlerInvokerUtil.invokeCloseNow(ctx, promise); } }, promise);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void invokeDeregister(final ChannelHandlerContext ctx, final ChannelPromise promise)
/*     */   {
/* 289 */     if (!ChannelHandlerInvokerUtil.validatePromise(ctx, promise, false))
/*     */     {
/* 291 */       return;
/*     */     }
/*     */     
/* 294 */     if (this.executor.inEventLoop()) {
/* 295 */       ChannelHandlerInvokerUtil.invokeDeregisterNow(ctx, promise);
/*     */     } else {
/* 297 */       safeExecuteOutbound(new OneTimeTask()
/*     */       {
/*     */ 
/* 300 */         public void run() { ChannelHandlerInvokerUtil.invokeDeregisterNow(ctx, promise); } }, promise);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void invokeRead(final ChannelHandlerContext ctx)
/*     */   {
/* 308 */     if (this.executor.inEventLoop()) {
/* 309 */       ChannelHandlerInvokerUtil.invokeReadNow(ctx);
/*     */     } else {
/* 311 */       AbstractChannelHandlerContext dctx = (AbstractChannelHandlerContext)ctx;
/* 312 */       Runnable task = dctx.invokeReadTask;
/* 313 */       if (task == null) {
/* 314 */         dctx.invokeReadTask = ( = new Runnable()
/*     */         {
/*     */           public void run() {
/* 317 */             ChannelHandlerInvokerUtil.invokeReadNow(ctx);
/*     */           }
/*     */         });
/*     */       }
/* 321 */       this.executor.execute(task);
/*     */     }
/*     */   }
/*     */   
/*     */   public void invokeWrite(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
/*     */   {
/* 327 */     if (msg == null) {
/* 328 */       throw new NullPointerException("msg");
/*     */     }
/* 330 */     if (!ChannelHandlerInvokerUtil.validatePromise(ctx, promise, true))
/*     */     {
/* 332 */       ReferenceCountUtil.release(msg);
/* 333 */       return;
/*     */     }
/*     */     
/* 336 */     if (this.executor.inEventLoop()) {
/* 337 */       ChannelHandlerInvokerUtil.invokeWriteNow(ctx, msg, promise);
/*     */     } else {
/* 339 */       AbstractChannel channel = (AbstractChannel)ctx.channel();
/* 340 */       int size = channel.estimatorHandle().size(msg);
/* 341 */       if (size > 0) {
/* 342 */         ChannelOutboundBuffer buffer = channel.unsafe().outboundBuffer();
/*     */         
/* 344 */         if (buffer != null) {
/* 345 */           buffer.incrementPendingOutboundBytes(size);
/*     */         }
/*     */       }
/* 348 */       safeExecuteOutbound(WriteTask.newInstance(ctx, msg, size, promise), promise, msg);
/*     */     }
/*     */   }
/*     */   
/*     */   public void invokeFlush(final ChannelHandlerContext ctx)
/*     */   {
/* 354 */     if (this.executor.inEventLoop()) {
/* 355 */       ChannelHandlerInvokerUtil.invokeFlushNow(ctx);
/*     */     } else {
/* 357 */       AbstractChannelHandlerContext dctx = (AbstractChannelHandlerContext)ctx;
/* 358 */       Runnable task = dctx.invokeFlushTask;
/* 359 */       if (task == null) {
/* 360 */         dctx.invokeFlushTask = ( = new Runnable()
/*     */         {
/*     */           public void run() {
/* 363 */             ChannelHandlerInvokerUtil.invokeFlushNow(ctx);
/*     */           }
/*     */         });
/*     */       }
/* 367 */       this.executor.execute(task);
/*     */     }
/*     */   }
/*     */   
/*     */   private void safeExecuteInbound(Runnable task, Object msg) {
/* 372 */     boolean success = false;
/*     */     try {
/* 374 */       this.executor.execute(task);
/* 375 */       success = true;
/*     */     } finally {
/* 377 */       if (!success) {
/* 378 */         ReferenceCountUtil.release(msg);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void safeExecuteOutbound(Runnable task, ChannelPromise promise) {
/*     */     try {
/* 385 */       this.executor.execute(task);
/*     */     } catch (Throwable cause) {
/* 387 */       promise.setFailure(cause);
/*     */     }
/*     */   }
/*     */   
/*     */   private void safeExecuteOutbound(Runnable task, ChannelPromise promise, Object msg) {
/* 392 */     try { this.executor.execute(task);
/*     */     } catch (Throwable cause) {
/*     */       try {
/* 395 */         promise.setFailure(cause);
/*     */       } finally {
/* 397 */         ReferenceCountUtil.release(msg);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static final class WriteTask
/*     */     extends RecyclableMpscLinkedQueueNode<SingleThreadEventLoop.NonWakeupRunnable> implements SingleThreadEventLoop.NonWakeupRunnable
/*     */   {
/*     */     private ChannelHandlerContext ctx;
/*     */     private Object msg;
/*     */     private ChannelPromise promise;
/*     */     private int size;
/* 409 */     private static final Recycler<WriteTask> RECYCLER = new Recycler()
/*     */     {
/*     */       protected DefaultChannelHandlerInvoker.WriteTask newObject(Recycler.Handle<DefaultChannelHandlerInvoker.WriteTask> handle) {
/* 412 */         return new DefaultChannelHandlerInvoker.WriteTask(handle, null);
/*     */       }
/*     */     };
/*     */     
/*     */     private static WriteTask newInstance(ChannelHandlerContext ctx, Object msg, int size, ChannelPromise promise)
/*     */     {
/* 418 */       WriteTask task = (WriteTask)RECYCLER.get();
/* 419 */       task.ctx = ctx;
/* 420 */       task.msg = msg;
/* 421 */       task.promise = promise;
/* 422 */       task.size = size;
/* 423 */       return task;
/*     */     }
/*     */     
/*     */     private WriteTask(Recycler.Handle<WriteTask> handle) {
/* 427 */       super();
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/*     */       try {
/* 433 */         if (this.size > 0) {
/* 434 */           ChannelOutboundBuffer buffer = this.ctx.channel().unsafe().outboundBuffer();
/*     */           
/* 436 */           if (buffer != null) {
/* 437 */             buffer.decrementPendingOutboundBytes(this.size);
/*     */           }
/*     */         }
/* 440 */         ChannelHandlerInvokerUtil.invokeWriteNow(this.ctx, this.msg, this.promise);
/*     */       }
/*     */       finally {
/* 443 */         this.ctx = null;
/* 444 */         this.msg = null;
/* 445 */         this.promise = null;
/*     */       }
/*     */     }
/*     */     
/*     */     public SingleThreadEventLoop.NonWakeupRunnable value()
/*     */     {
/* 451 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\DefaultChannelHandlerInvoker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */