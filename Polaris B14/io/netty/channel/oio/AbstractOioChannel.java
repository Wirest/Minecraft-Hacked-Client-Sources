/*     */ package io.netty.channel.oio;
/*     */ 
/*     */ import io.netty.channel.AbstractChannel;
/*     */ import io.netty.channel.AbstractChannel.AbstractUnsafe;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.ThreadPerChannelEventLoop;
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
/*     */ public abstract class AbstractOioChannel
/*     */   extends AbstractChannel
/*     */ {
/*     */   protected static final int SO_TIMEOUT = 1000;
/*     */   private volatile boolean readPending;
/*  35 */   private final Runnable readTask = new Runnable()
/*     */   {
/*     */     public void run() {
/*  38 */       if ((!AbstractOioChannel.this.isReadPending()) && (!AbstractOioChannel.this.config().isAutoRead()))
/*     */       {
/*  40 */         return;
/*     */       }
/*     */       
/*  43 */       AbstractOioChannel.this.setReadPending(false);
/*  44 */       AbstractOioChannel.this.doRead();
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */   protected AbstractOioChannel(Channel parent)
/*     */   {
/*  52 */     super(parent);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  57 */   protected AbstractChannel.AbstractUnsafe newUnsafe() { return new DefaultOioUnsafe(null); }
/*     */   
/*     */   private final class DefaultOioUnsafe extends AbstractChannel.AbstractUnsafe {
/*  60 */     private DefaultOioUnsafe() { super(); }
/*     */     
/*     */ 
/*     */     public void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
/*     */     {
/*  65 */       if ((!promise.setUncancellable()) || (!ensureOpen(promise))) {
/*  66 */         return;
/*     */       }
/*     */       try
/*     */       {
/*  70 */         boolean wasActive = AbstractOioChannel.this.isActive();
/*  71 */         AbstractOioChannel.this.doConnect(remoteAddress, localAddress);
/*  72 */         safeSetSuccess(promise);
/*  73 */         if ((!wasActive) && (AbstractOioChannel.this.isActive())) {
/*  74 */           AbstractOioChannel.this.pipeline().fireChannelActive();
/*     */         }
/*     */       } catch (Throwable t) {
/*  77 */         safeSetFailure(promise, annotateConnectException(t, remoteAddress));
/*  78 */         closeIfClosed();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean isCompatible(EventLoop loop)
/*     */   {
/*  85 */     return loop instanceof ThreadPerChannelEventLoop;
/*     */   }
/*     */   
/*     */ 
/*     */   protected abstract void doConnect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */   protected void doBeginRead()
/*     */     throws Exception
/*     */   {
/*  96 */     if (isReadPending()) {
/*  97 */       return;
/*     */     }
/*     */     
/* 100 */     setReadPending(true);
/* 101 */     eventLoop().execute(this.readTask);
/*     */   }
/*     */   
/*     */   protected abstract void doRead();
/*     */   
/*     */   protected boolean isReadPending() {
/* 107 */     return this.readPending;
/*     */   }
/*     */   
/*     */   protected void setReadPending(boolean readPending) {
/* 111 */     this.readPending = readPending;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\oio\AbstractOioChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */