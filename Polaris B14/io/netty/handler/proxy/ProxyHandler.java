/*     */ package io.netty.handler.proxy;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerAdapter;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.PendingWriteQueue;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.concurrent.DefaultPromise;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.ScheduledFuture;
/*     */ import io.netty.util.internal.OneTimeTask;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.ConnectionPendingException;
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
/*     */ public abstract class ProxyHandler
/*     */   extends ChannelHandlerAdapter
/*     */ {
/*  41 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ProxyHandler.class);
/*     */   
/*     */ 
/*     */   private static final long DEFAULT_CONNECT_TIMEOUT_MILLIS = 10000L;
/*     */   
/*     */ 
/*     */   static final String AUTH_NONE = "none";
/*     */   
/*     */ 
/*     */   private final SocketAddress proxyAddress;
/*     */   
/*     */ 
/*     */   private volatile SocketAddress destinationAddress;
/*     */   
/*  55 */   private volatile long connectTimeoutMillis = 10000L;
/*     */   
/*     */   private volatile ChannelHandlerContext ctx;
/*     */   private PendingWriteQueue pendingWrites;
/*     */   private boolean finished;
/*     */   private boolean suppressChannelReadComplete;
/*     */   private boolean flushedPrematurely;
/*  62 */   private final LazyChannelPromise connectPromise = new LazyChannelPromise(null);
/*     */   private ScheduledFuture<?> connectTimeoutFuture;
/*  64 */   private final ChannelFutureListener writeListener = new ChannelFutureListener()
/*     */   {
/*     */     public void operationComplete(ChannelFuture future) throws Exception {
/*  67 */       if (!future.isSuccess()) {
/*  68 */         ProxyHandler.this.setConnectFailure(future.cause());
/*     */       }
/*     */     }
/*     */   };
/*     */   
/*     */   protected ProxyHandler(SocketAddress proxyAddress) {
/*  74 */     if (proxyAddress == null) {
/*  75 */       throw new NullPointerException("proxyAddress");
/*     */     }
/*  77 */     this.proxyAddress = proxyAddress;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract String protocol();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract String authScheme();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final <T extends SocketAddress> T proxyAddress()
/*     */   {
/*  95 */     return this.proxyAddress;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final <T extends SocketAddress> T destinationAddress()
/*     */   {
/* 103 */     return this.destinationAddress;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final boolean isConnected()
/*     */   {
/* 110 */     return this.connectPromise.isSuccess();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Future<Channel> connectFuture()
/*     */   {
/* 118 */     return this.connectPromise;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final long connectTimeoutMillis()
/*     */   {
/* 126 */     return this.connectTimeoutMillis;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setConnectTimeoutMillis(long connectTimeoutMillis)
/*     */   {
/* 134 */     if (connectTimeoutMillis <= 0L) {
/* 135 */       connectTimeoutMillis = 0L;
/*     */     }
/*     */     
/* 138 */     this.connectTimeoutMillis = connectTimeoutMillis;
/*     */   }
/*     */   
/*     */   public final void handlerAdded(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 143 */     this.ctx = ctx;
/* 144 */     addCodec(ctx);
/*     */     
/* 146 */     if (ctx.channel().isActive())
/*     */     {
/*     */ 
/* 149 */       sendInitialMessage(ctx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void addCodec(ChannelHandlerContext paramChannelHandlerContext)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void removeEncoder(ChannelHandlerContext paramChannelHandlerContext)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void removeDecoder(ChannelHandlerContext paramChannelHandlerContext)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */   public final void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
/*     */     throws Exception
/*     */   {
/* 176 */     if (this.destinationAddress != null) {
/* 177 */       promise.setFailure(new ConnectionPendingException());
/* 178 */       return;
/*     */     }
/*     */     
/* 181 */     this.destinationAddress = remoteAddress;
/* 182 */     ctx.connect(this.proxyAddress, localAddress, promise);
/*     */   }
/*     */   
/*     */   public final void channelActive(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 187 */     sendInitialMessage(ctx);
/* 188 */     ctx.fireChannelActive();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void sendInitialMessage(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 196 */     long connectTimeoutMillis = this.connectTimeoutMillis;
/* 197 */     if (connectTimeoutMillis > 0L) {
/* 198 */       this.connectTimeoutFuture = ctx.executor().schedule(new OneTimeTask()
/*     */       {
/*     */         public void run() {
/* 201 */           if (!ProxyHandler.this.connectPromise.isDone())
/* 202 */             ProxyHandler.this.setConnectFailure(new ProxyConnectException(ProxyHandler.this.exceptionMessage("timeout"))); } }, connectTimeoutMillis, TimeUnit.MILLISECONDS);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 208 */     Object initialMessage = newInitialMessage(ctx);
/* 209 */     if (initialMessage != null) {
/* 210 */       sendToProxyServer(initialMessage);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract Object newInitialMessage(ChannelHandlerContext paramChannelHandlerContext)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void sendToProxyServer(Object msg)
/*     */   {
/* 226 */     this.ctx.writeAndFlush(msg).addListener(this.writeListener);
/*     */   }
/*     */   
/*     */   public final void channelInactive(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 231 */     if (this.finished) {
/* 232 */       ctx.fireChannelInactive();
/*     */     }
/*     */     else {
/* 235 */       setConnectFailure(new ProxyConnectException(exceptionMessage("disconnected")));
/*     */     }
/*     */   }
/*     */   
/*     */   public final void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
/*     */   {
/* 241 */     if (this.finished) {
/* 242 */       ctx.fireExceptionCaught(cause);
/*     */     }
/*     */     else {
/* 245 */       setConnectFailure(cause);
/*     */     }
/*     */   }
/*     */   
/*     */   public final void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
/*     */   {
/* 251 */     if (this.finished)
/*     */     {
/* 253 */       this.suppressChannelReadComplete = false;
/* 254 */       ctx.fireChannelRead(msg);
/*     */     } else {
/* 256 */       this.suppressChannelReadComplete = true;
/* 257 */       Throwable cause = null;
/*     */       try {
/* 259 */         boolean done = handleResponse(ctx, msg);
/* 260 */         if (done) {
/* 261 */           setConnectSuccess();
/*     */         }
/*     */       } catch (Throwable t) {
/* 264 */         cause = t;
/*     */       } finally {
/* 266 */         ReferenceCountUtil.release(msg);
/* 267 */         if (cause != null) {
/* 268 */           setConnectFailure(cause);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean handleResponse(ChannelHandlerContext paramChannelHandlerContext, Object paramObject)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */   private void setConnectSuccess()
/*     */   {
/* 284 */     this.finished = true;
/* 285 */     if (this.connectTimeoutFuture != null) {
/* 286 */       this.connectTimeoutFuture.cancel(false);
/*     */     }
/*     */     
/* 289 */     if (this.connectPromise.trySuccess(this.ctx.channel())) {
/* 290 */       boolean removedCodec = true;
/*     */       
/* 292 */       removedCodec &= safeRemoveEncoder();
/*     */       
/* 294 */       this.ctx.fireUserEventTriggered(new ProxyConnectionEvent(protocol(), authScheme(), this.proxyAddress, this.destinationAddress));
/*     */       
/*     */ 
/* 297 */       removedCodec &= safeRemoveDecoder();
/*     */       
/* 299 */       if (removedCodec) {
/* 300 */         writePendingWrites();
/*     */         
/* 302 */         if (this.flushedPrematurely) {
/* 303 */           this.ctx.flush();
/*     */         }
/*     */       }
/*     */       else {
/* 307 */         Exception cause = new ProxyConnectException("failed to remove all codec handlers added by the proxy handler; bug?");
/*     */         
/* 309 */         failPendingWrites(cause);
/* 310 */         this.ctx.fireExceptionCaught(cause);
/* 311 */         this.ctx.close();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean safeRemoveDecoder() {
/*     */     try {
/* 318 */       removeDecoder(this.ctx);
/* 319 */       return true;
/*     */     } catch (Exception e) {
/* 321 */       logger.warn("Failed to remove proxy decoders:", e);
/*     */     }
/*     */     
/* 324 */     return false;
/*     */   }
/*     */   
/*     */   private boolean safeRemoveEncoder() {
/*     */     try {
/* 329 */       removeEncoder(this.ctx);
/* 330 */       return true;
/*     */     } catch (Exception e) {
/* 332 */       logger.warn("Failed to remove proxy encoders:", e);
/*     */     }
/*     */     
/* 335 */     return false;
/*     */   }
/*     */   
/*     */   private void setConnectFailure(Throwable cause) {
/* 339 */     this.finished = true;
/* 340 */     if (this.connectTimeoutFuture != null) {
/* 341 */       this.connectTimeoutFuture.cancel(false);
/*     */     }
/*     */     
/* 344 */     if (!(cause instanceof ProxyConnectException)) {
/* 345 */       cause = new ProxyConnectException(exceptionMessage(cause.toString()), cause);
/*     */     }
/*     */     
/*     */ 
/* 349 */     if (this.connectPromise.tryFailure(cause)) {
/* 350 */       safeRemoveDecoder();
/* 351 */       safeRemoveEncoder();
/*     */       
/* 353 */       failPendingWrites(cause);
/* 354 */       this.ctx.fireExceptionCaught(cause);
/* 355 */       this.ctx.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String exceptionMessage(String msg)
/*     */   {
/* 364 */     if (msg == null) {
/* 365 */       msg = "";
/*     */     }
/*     */     
/* 368 */     StringBuilder buf = new StringBuilder(128 + msg.length()).append(protocol()).append(", ").append(authScheme()).append(", ").append(this.proxyAddress).append(" => ").append(this.destinationAddress);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 376 */     if (!msg.isEmpty()) {
/* 377 */       buf.append(", ").append(msg);
/*     */     }
/*     */     
/* 380 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public final void channelReadComplete(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 385 */     if (this.suppressChannelReadComplete) {
/* 386 */       this.suppressChannelReadComplete = false;
/*     */       
/* 388 */       if (!ctx.channel().config().isAutoRead()) {
/* 389 */         ctx.read();
/*     */       }
/*     */     } else {
/* 392 */       ctx.fireChannelReadComplete();
/*     */     }
/*     */   }
/*     */   
/*     */   public final void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
/*     */   {
/* 398 */     if (this.finished) {
/* 399 */       writePendingWrites();
/* 400 */       ctx.write(msg, promise);
/*     */     } else {
/* 402 */       addPendingWrite(ctx, msg, promise);
/*     */     }
/*     */   }
/*     */   
/*     */   public final void flush(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 408 */     if (this.finished) {
/* 409 */       writePendingWrites();
/* 410 */       ctx.flush();
/*     */     } else {
/* 412 */       this.flushedPrematurely = true;
/*     */     }
/*     */   }
/*     */   
/*     */   private void writePendingWrites() {
/* 417 */     if (this.pendingWrites != null) {
/* 418 */       this.pendingWrites.removeAndWriteAll();
/* 419 */       this.pendingWrites = null;
/*     */     }
/*     */   }
/*     */   
/*     */   private void failPendingWrites(Throwable cause) {
/* 424 */     if (this.pendingWrites != null) {
/* 425 */       this.pendingWrites.removeAndFailAll(cause);
/* 426 */       this.pendingWrites = null;
/*     */     }
/*     */   }
/*     */   
/*     */   private void addPendingWrite(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
/* 431 */     PendingWriteQueue pendingWrites = this.pendingWrites;
/* 432 */     if (pendingWrites == null) {
/* 433 */       this.pendingWrites = (pendingWrites = new PendingWriteQueue(ctx));
/*     */     }
/* 435 */     pendingWrites.add(msg, promise);
/*     */   }
/*     */   
/*     */   private final class LazyChannelPromise extends DefaultPromise<Channel> {
/*     */     private LazyChannelPromise() {}
/*     */     
/* 441 */     protected EventExecutor executor() { if (ProxyHandler.this.ctx == null) {
/* 442 */         throw new IllegalStateException();
/*     */       }
/* 444 */       return ProxyHandler.this.ctx.executor();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\proxy\ProxyHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */