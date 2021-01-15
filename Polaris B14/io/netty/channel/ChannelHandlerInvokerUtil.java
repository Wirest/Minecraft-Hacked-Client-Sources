/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.util.internal.StringUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ChannelHandlerInvokerUtil
/*     */ {
/*     */   public static void invokeChannelRegisteredNow(ChannelHandlerContext ctx)
/*     */   {
/*     */     try
/*     */     {
/*  32 */       ctx.handler().channelRegistered(ctx);
/*     */     } catch (Throwable t) {
/*  34 */       notifyHandlerException(ctx, t);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void invokeChannelUnregisteredNow(ChannelHandlerContext ctx) {
/*     */     try {
/*  40 */       ctx.handler().channelUnregistered(ctx);
/*     */     } catch (Throwable t) {
/*  42 */       notifyHandlerException(ctx, t);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void invokeChannelActiveNow(ChannelHandlerContext ctx) {
/*     */     try {
/*  48 */       ctx.handler().channelActive(ctx);
/*     */     } catch (Throwable t) {
/*  50 */       notifyHandlerException(ctx, t);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void invokeChannelInactiveNow(ChannelHandlerContext ctx) {
/*     */     try {
/*  56 */       ctx.handler().channelInactive(ctx);
/*     */     } catch (Throwable t) {
/*  58 */       notifyHandlerException(ctx, t);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void invokeExceptionCaughtNow(ChannelHandlerContext ctx, Throwable cause) {
/*     */     try {
/*  64 */       ctx.handler().exceptionCaught(ctx, cause);
/*     */     } catch (Throwable t) {
/*  66 */       if (DefaultChannelPipeline.logger.isWarnEnabled()) {
/*  67 */         DefaultChannelPipeline.logger.warn("An exception was thrown by a user handler's exceptionCaught() method:", t);
/*  68 */         DefaultChannelPipeline.logger.warn(".. and the cause of the exceptionCaught() was:", cause);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void invokeUserEventTriggeredNow(ChannelHandlerContext ctx, Object event) {
/*     */     try {
/*  75 */       ctx.handler().userEventTriggered(ctx, event);
/*     */     } catch (Throwable t) {
/*  77 */       notifyHandlerException(ctx, t);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void invokeChannelReadNow(ChannelHandlerContext ctx, Object msg) {
/*     */     try {
/*  83 */       ((AbstractChannelHandlerContext)ctx).invokedThisChannelRead = true;
/*  84 */       ctx.handler().channelRead(ctx, msg);
/*     */     } catch (Throwable t) {
/*  86 */       notifyHandlerException(ctx, t);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void invokeChannelReadCompleteNow(ChannelHandlerContext ctx) {
/*     */     try {
/*  92 */       ctx.handler().channelReadComplete(ctx);
/*     */     } catch (Throwable t) {
/*  94 */       notifyHandlerException(ctx, t);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void invokeChannelWritabilityChangedNow(ChannelHandlerContext ctx) {
/*     */     try {
/* 100 */       ctx.handler().channelWritabilityChanged(ctx);
/*     */     } catch (Throwable t) {
/* 102 */       notifyHandlerException(ctx, t);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void invokeBindNow(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise)
/*     */   {
/*     */     try {
/* 109 */       ctx.handler().bind(ctx, localAddress, promise);
/*     */     } catch (Throwable t) {
/* 111 */       notifyOutboundHandlerException(t, promise);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void invokeConnectNow(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
/*     */   {
/*     */     try {
/* 118 */       ctx.handler().connect(ctx, remoteAddress, localAddress, promise);
/*     */     } catch (Throwable t) {
/* 120 */       notifyOutboundHandlerException(t, promise);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void invokeDisconnectNow(ChannelHandlerContext ctx, ChannelPromise promise) {
/*     */     try {
/* 126 */       ctx.handler().disconnect(ctx, promise);
/*     */     } catch (Throwable t) {
/* 128 */       notifyOutboundHandlerException(t, promise);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void invokeCloseNow(ChannelHandlerContext ctx, ChannelPromise promise) {
/*     */     try {
/* 134 */       ctx.handler().close(ctx, promise);
/*     */     } catch (Throwable t) {
/* 136 */       notifyOutboundHandlerException(t, promise);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void invokeDeregisterNow(ChannelHandlerContext ctx, ChannelPromise promise) {
/*     */     try {
/* 142 */       ctx.handler().deregister(ctx, promise);
/*     */     } catch (Throwable t) {
/* 144 */       notifyOutboundHandlerException(t, promise);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void invokeReadNow(ChannelHandlerContext ctx) {
/*     */     try {
/* 150 */       ctx.handler().read(ctx);
/*     */     } catch (Throwable t) {
/* 152 */       notifyHandlerException(ctx, t);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void invokeWriteNow(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
/*     */     try {
/* 158 */       ctx.handler().write(ctx, msg, promise);
/*     */     } catch (Throwable t) {
/* 160 */       notifyOutboundHandlerException(t, promise);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void invokeFlushNow(ChannelHandlerContext ctx) {
/*     */     try {
/* 166 */       ctx.handler().flush(ctx);
/*     */     } catch (Throwable t) {
/* 168 */       notifyHandlerException(ctx, t);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean validatePromise(ChannelHandlerContext ctx, ChannelPromise promise, boolean allowVoidPromise)
/*     */   {
/* 174 */     if (ctx == null) {
/* 175 */       throw new NullPointerException("ctx");
/*     */     }
/*     */     
/* 178 */     if (promise == null) {
/* 179 */       throw new NullPointerException("promise");
/*     */     }
/*     */     
/* 182 */     if (promise.isDone()) {
/* 183 */       if (promise.isCancelled()) {
/* 184 */         return false;
/*     */       }
/* 186 */       throw new IllegalArgumentException("promise already done: " + promise);
/*     */     }
/*     */     
/* 189 */     if (promise.channel() != ctx.channel()) {
/* 190 */       throw new IllegalArgumentException(String.format("promise.channel does not match: %s (expected: %s)", new Object[] { promise.channel(), ctx.channel() }));
/*     */     }
/*     */     
/*     */ 
/* 194 */     if (promise.getClass() == DefaultChannelPromise.class) {
/* 195 */       return true;
/*     */     }
/*     */     
/* 198 */     if ((!allowVoidPromise) && ((promise instanceof VoidChannelPromise))) {
/* 199 */       throw new IllegalArgumentException(StringUtil.simpleClassName(VoidChannelPromise.class) + " not allowed for this operation");
/*     */     }
/*     */     
/*     */ 
/* 203 */     if ((promise instanceof AbstractChannel.CloseFuture)) {
/* 204 */       throw new IllegalArgumentException(StringUtil.simpleClassName(AbstractChannel.CloseFuture.class) + " not allowed in a pipeline");
/*     */     }
/*     */     
/* 207 */     return true;
/*     */   }
/*     */   
/*     */   private static void notifyHandlerException(ChannelHandlerContext ctx, Throwable cause) {
/* 211 */     if (inExceptionCaught(cause)) {
/* 212 */       if (DefaultChannelPipeline.logger.isWarnEnabled()) {
/* 213 */         DefaultChannelPipeline.logger.warn("An exception was thrown by a user handler while handling an exceptionCaught event", cause);
/*     */       }
/*     */       
/*     */ 
/* 217 */       return;
/*     */     }
/*     */     
/* 220 */     invokeExceptionCaughtNow(ctx, cause);
/*     */   }
/*     */   
/*     */   private static void notifyOutboundHandlerException(Throwable cause, ChannelPromise promise) {
/* 224 */     if ((!promise.tryFailure(cause)) && (!(promise instanceof VoidChannelPromise)) && 
/* 225 */       (DefaultChannelPipeline.logger.isWarnEnabled())) {
/* 226 */       DefaultChannelPipeline.logger.warn("Failed to fail the promise because it's done already: {}", promise, cause);
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean inExceptionCaught(Throwable cause)
/*     */   {
/*     */     do {
/* 233 */       StackTraceElement[] trace = cause.getStackTrace();
/* 234 */       if (trace != null) {
/* 235 */         for (StackTraceElement t : trace) {
/* 236 */           if (t == null) {
/*     */             break;
/*     */           }
/* 239 */           if ("exceptionCaught".equals(t.getMethodName())) {
/* 240 */             return true;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 245 */       cause = cause.getCause();
/* 246 */     } while (cause != null);
/*     */     
/* 248 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\ChannelHandlerInvokerUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */