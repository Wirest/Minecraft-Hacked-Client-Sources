/*     */ package io.netty.channel.nio;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.AbstractChannel;
/*     */ import io.netty.channel.AbstractChannel.AbstractUnsafe;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.Channel.Unsafe;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.ConnectTimeoutException;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import io.netty.util.internal.OneTimeTask;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.CancelledKeyException;
/*     */ import java.nio.channels.SelectableChannel;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.util.concurrent.ScheduledFuture;
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
/*     */ public abstract class AbstractNioChannel
/*     */   extends AbstractChannel
/*     */ {
/*  49 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractNioChannel.class);
/*     */   
/*     */ 
/*     */   private final SelectableChannel ch;
/*     */   
/*     */ 
/*     */   protected final int readInterestOp;
/*     */   
/*     */ 
/*     */   volatile SelectionKey selectionKey;
/*     */   
/*     */ 
/*     */   private volatile boolean inputShutdown;
/*     */   
/*     */   private volatile boolean readPending;
/*     */   
/*     */   private ChannelPromise connectPromise;
/*     */   
/*     */   private ScheduledFuture<?> connectTimeoutFuture;
/*     */   
/*     */   private SocketAddress requestedRemoteAddress;
/*     */   
/*     */ 
/*     */   protected AbstractNioChannel(Channel parent, SelectableChannel ch, int readInterestOp)
/*     */   {
/*  74 */     super(parent);
/*  75 */     this.ch = ch;
/*  76 */     this.readInterestOp = readInterestOp;
/*     */     try {
/*  78 */       ch.configureBlocking(false);
/*     */     } catch (IOException e) {
/*     */       try {
/*  81 */         ch.close();
/*     */       } catch (IOException e2) {
/*  83 */         if (logger.isWarnEnabled()) {
/*  84 */           logger.warn("Failed to close a partially initialized socket.", e2);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*  89 */       throw new ChannelException("Failed to enter non-blocking mode.", e);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isOpen()
/*     */   {
/*  95 */     return this.ch.isOpen();
/*     */   }
/*     */   
/*     */   public NioUnsafe unsafe()
/*     */   {
/* 100 */     return (NioUnsafe)super.unsafe();
/*     */   }
/*     */   
/*     */   protected SelectableChannel javaChannel() {
/* 104 */     return this.ch;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected SelectionKey selectionKey()
/*     */   {
/* 111 */     assert (this.selectionKey != null);
/* 112 */     return this.selectionKey;
/*     */   }
/*     */   
/*     */   protected boolean isReadPending() {
/* 116 */     return this.readPending;
/*     */   }
/*     */   
/*     */   protected void setReadPending(boolean readPending) {
/* 120 */     this.readPending = readPending;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean isInputShutdown()
/*     */   {
/* 127 */     return this.inputShutdown;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void setInputShutdown()
/*     */   {
/* 134 */     this.inputShutdown = true;
/*     */   }
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
/*     */   protected abstract class AbstractNioUnsafe
/*     */     extends AbstractChannel.AbstractUnsafe
/*     */     implements AbstractNioChannel.NioUnsafe
/*     */   {
/* 159 */     protected AbstractNioUnsafe() { super(); }
/*     */     
/*     */     protected final void removeReadOp() {
/* 162 */       SelectionKey key = AbstractNioChannel.this.selectionKey();
/*     */       
/*     */ 
/*     */ 
/* 166 */       if (!key.isValid()) {
/* 167 */         return;
/*     */       }
/* 169 */       int interestOps = key.interestOps();
/* 170 */       if ((interestOps & AbstractNioChannel.this.readInterestOp) != 0)
/*     */       {
/* 172 */         key.interestOps(interestOps & (AbstractNioChannel.this.readInterestOp ^ 0xFFFFFFFF));
/*     */       }
/*     */     }
/*     */     
/*     */     public final SelectableChannel ch()
/*     */     {
/* 178 */       return AbstractNioChannel.this.javaChannel();
/*     */     }
/*     */     
/*     */ 
/*     */     public final void connect(final SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
/*     */     {
/* 184 */       if ((!promise.setUncancellable()) || (!ensureOpen(promise))) {
/* 185 */         return;
/*     */       }
/*     */       try
/*     */       {
/* 189 */         if (AbstractNioChannel.this.connectPromise != null) {
/* 190 */           throw new IllegalStateException("connection attempt already made");
/*     */         }
/*     */         
/* 193 */         boolean wasActive = AbstractNioChannel.this.isActive();
/* 194 */         if (AbstractNioChannel.this.doConnect(remoteAddress, localAddress)) {
/* 195 */           fulfillConnectPromise(promise, wasActive);
/*     */         } else {
/* 197 */           AbstractNioChannel.this.connectPromise = promise;
/* 198 */           AbstractNioChannel.this.requestedRemoteAddress = remoteAddress;
/*     */           
/*     */ 
/* 201 */           int connectTimeoutMillis = AbstractNioChannel.this.config().getConnectTimeoutMillis();
/* 202 */           if (connectTimeoutMillis > 0) {
/* 203 */             AbstractNioChannel.this.connectTimeoutFuture = AbstractNioChannel.this.eventLoop().schedule(new OneTimeTask()
/*     */             {
/*     */               public void run() {
/* 206 */                 ChannelPromise connectPromise = AbstractNioChannel.this.connectPromise;
/* 207 */                 ConnectTimeoutException cause = new ConnectTimeoutException("connection timed out: " + remoteAddress);
/*     */                 
/* 209 */                 if ((connectPromise != null) && (connectPromise.tryFailure(cause)))
/* 210 */                   AbstractNioChannel.AbstractNioUnsafe.this.close(AbstractNioChannel.AbstractNioUnsafe.this.voidPromise()); } }, connectTimeoutMillis, TimeUnit.MILLISECONDS);
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 216 */           promise.addListener(new ChannelFutureListener()
/*     */           {
/*     */             public void operationComplete(ChannelFuture future) throws Exception {
/* 219 */               if (future.isCancelled()) {
/* 220 */                 if (AbstractNioChannel.this.connectTimeoutFuture != null) {
/* 221 */                   AbstractNioChannel.this.connectTimeoutFuture.cancel(false);
/*     */                 }
/* 223 */                 AbstractNioChannel.this.connectPromise = null;
/* 224 */                 AbstractNioChannel.AbstractNioUnsafe.this.close(AbstractNioChannel.AbstractNioUnsafe.this.voidPromise());
/*     */               }
/*     */             }
/*     */           });
/*     */         }
/*     */       } catch (Throwable t) {
/* 230 */         promise.tryFailure(annotateConnectException(t, remoteAddress));
/* 231 */         closeIfClosed();
/*     */       }
/*     */     }
/*     */     
/*     */     private void fulfillConnectPromise(ChannelPromise promise, boolean wasActive) {
/* 236 */       if (promise == null)
/*     */       {
/* 238 */         return;
/*     */       }
/*     */       
/*     */ 
/* 242 */       boolean promiseSet = promise.trySuccess();
/*     */       
/*     */ 
/*     */ 
/* 246 */       if ((!wasActive) && (AbstractNioChannel.this.isActive())) {
/* 247 */         AbstractNioChannel.this.pipeline().fireChannelActive();
/*     */       }
/*     */       
/*     */ 
/* 251 */       if (!promiseSet) {
/* 252 */         close(voidPromise());
/*     */       }
/*     */     }
/*     */     
/*     */     private void fulfillConnectPromise(ChannelPromise promise, Throwable cause) {
/* 257 */       if (promise == null)
/*     */       {
/* 259 */         return;
/*     */       }
/*     */       
/*     */ 
/* 263 */       promise.tryFailure(cause);
/* 264 */       closeIfClosed();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public final void finishConnect()
/*     */     {
/* 272 */       assert (AbstractNioChannel.this.eventLoop().inEventLoop());
/*     */       try
/*     */       {
/* 275 */         boolean wasActive = AbstractNioChannel.this.isActive();
/* 276 */         AbstractNioChannel.this.doFinishConnect();
/* 277 */         fulfillConnectPromise(AbstractNioChannel.this.connectPromise, wasActive);
/*     */       } catch (Throwable t) {
/* 279 */         fulfillConnectPromise(AbstractNioChannel.this.connectPromise, annotateConnectException(t, AbstractNioChannel.this.requestedRemoteAddress));
/*     */       }
/*     */       finally
/*     */       {
/* 283 */         if (AbstractNioChannel.this.connectTimeoutFuture != null) {
/* 284 */           AbstractNioChannel.this.connectTimeoutFuture.cancel(false);
/*     */         }
/* 286 */         AbstractNioChannel.this.connectPromise = null;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected final void flush0()
/*     */     {
/* 295 */       if (isFlushPending()) {
/* 296 */         return;
/*     */       }
/* 298 */       super.flush0();
/*     */     }
/*     */     
/*     */ 
/*     */     public final void forceFlush()
/*     */     {
/* 304 */       super.flush0();
/*     */     }
/*     */     
/*     */     private boolean isFlushPending() {
/* 308 */       SelectionKey selectionKey = AbstractNioChannel.this.selectionKey();
/* 309 */       return (selectionKey.isValid()) && ((selectionKey.interestOps() & 0x4) != 0);
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean isCompatible(EventLoop loop)
/*     */   {
/* 315 */     return loop instanceof NioEventLoop;
/*     */   }
/*     */   
/*     */   protected void doRegister() throws Exception
/*     */   {
/* 320 */     boolean selected = false;
/*     */     for (;;) {
/*     */       try {
/* 323 */         this.selectionKey = javaChannel().register(((NioEventLoop)eventLoop().unwrap()).selector, 0, this);
/* 324 */         return;
/*     */       } catch (CancelledKeyException e) {
/* 326 */         if (!selected)
/*     */         {
/*     */ 
/* 329 */           ((NioEventLoop)eventLoop().unwrap()).selectNow();
/* 330 */           selected = true;
/*     */         }
/*     */         else
/*     */         {
/* 334 */           throw e;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doDeregister() throws Exception
/*     */   {
/* 342 */     ((NioEventLoop)eventLoop().unwrap()).cancel(selectionKey());
/*     */   }
/*     */   
/*     */   protected void doBeginRead()
/*     */     throws Exception
/*     */   {
/* 348 */     if (this.inputShutdown) {
/* 349 */       return;
/*     */     }
/*     */     
/* 352 */     SelectionKey selectionKey = this.selectionKey;
/* 353 */     if (!selectionKey.isValid()) {
/* 354 */       return;
/*     */     }
/*     */     
/* 357 */     this.readPending = true;
/*     */     
/* 359 */     int interestOps = selectionKey.interestOps();
/* 360 */     if ((interestOps & this.readInterestOp) == 0) {
/* 361 */       selectionKey.interestOps(interestOps | this.readInterestOp);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean doConnect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void doFinishConnect()
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */   protected final ByteBuf newDirectBuffer(ByteBuf buf)
/*     */   {
/* 381 */     int readableBytes = buf.readableBytes();
/* 382 */     if (readableBytes == 0) {
/* 383 */       ReferenceCountUtil.safeRelease(buf);
/* 384 */       return Unpooled.EMPTY_BUFFER;
/*     */     }
/*     */     
/* 387 */     ByteBufAllocator alloc = alloc();
/* 388 */     if (alloc.isDirectBufferPooled()) {
/* 389 */       ByteBuf directBuf = alloc.directBuffer(readableBytes);
/* 390 */       directBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
/* 391 */       ReferenceCountUtil.safeRelease(buf);
/* 392 */       return directBuf;
/*     */     }
/*     */     
/* 395 */     ByteBuf directBuf = ByteBufUtil.threadLocalDirectBuffer();
/* 396 */     if (directBuf != null) {
/* 397 */       directBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
/* 398 */       ReferenceCountUtil.safeRelease(buf);
/* 399 */       return directBuf;
/*     */     }
/*     */     
/*     */ 
/* 403 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final ByteBuf newDirectBuffer(ReferenceCounted holder, ByteBuf buf)
/*     */   {
/* 413 */     int readableBytes = buf.readableBytes();
/* 414 */     if (readableBytes == 0) {
/* 415 */       ReferenceCountUtil.safeRelease(holder);
/* 416 */       return Unpooled.EMPTY_BUFFER;
/*     */     }
/*     */     
/* 419 */     ByteBufAllocator alloc = alloc();
/* 420 */     if (alloc.isDirectBufferPooled()) {
/* 421 */       ByteBuf directBuf = alloc.directBuffer(readableBytes);
/* 422 */       directBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
/* 423 */       ReferenceCountUtil.safeRelease(holder);
/* 424 */       return directBuf;
/*     */     }
/*     */     
/* 427 */     ByteBuf directBuf = ByteBufUtil.threadLocalDirectBuffer();
/* 428 */     if (directBuf != null) {
/* 429 */       directBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
/* 430 */       ReferenceCountUtil.safeRelease(holder);
/* 431 */       return directBuf;
/*     */     }
/*     */     
/*     */ 
/* 435 */     if (holder != buf)
/*     */     {
/* 437 */       buf.retain();
/* 438 */       ReferenceCountUtil.safeRelease(holder);
/*     */     }
/*     */     
/* 441 */     return buf;
/*     */   }
/*     */   
/*     */   public static abstract interface NioUnsafe
/*     */     extends Channel.Unsafe
/*     */   {
/*     */     public abstract SelectableChannel ch();
/*     */     
/*     */     public abstract void finishConnect();
/*     */     
/*     */     public abstract void read();
/*     */     
/*     */     public abstract void forceFlush();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\nio\AbstractNioChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */