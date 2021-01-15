/*      */ package io.netty.channel;
/*      */ 
/*      */ import io.netty.buffer.ByteBufAllocator;
/*      */ import io.netty.util.DefaultAttributeMap;
/*      */ import io.netty.util.ReferenceCountUtil;
/*      */ import io.netty.util.internal.EmptyArrays;
/*      */ import io.netty.util.internal.OneTimeTask;
/*      */ import io.netty.util.internal.PlatformDependent;
/*      */ import io.netty.util.internal.logging.InternalLogger;
/*      */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*      */ import java.net.ConnectException;
/*      */ import java.net.InetAddress;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.NoRouteToHostException;
/*      */ import java.net.SocketAddress;
/*      */ import java.net.SocketException;
/*      */ import java.nio.channels.ClosedChannelException;
/*      */ import java.nio.channels.NotYetConnectedException;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.RejectedExecutionException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AbstractChannel
/*      */   extends DefaultAttributeMap
/*      */   implements Channel
/*      */ {
/*   42 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractChannel.class);
/*      */   
/*   44 */   static final ClosedChannelException CLOSED_CHANNEL_EXCEPTION = new ClosedChannelException();
/*   45 */   static final NotYetConnectedException NOT_YET_CONNECTED_EXCEPTION = new NotYetConnectedException();
/*      */   private MessageSizeEstimator.Handle estimatorHandle;
/*      */   
/*   48 */   static { CLOSED_CHANNEL_EXCEPTION.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
/*   49 */     NOT_YET_CONNECTED_EXCEPTION.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
/*      */   }
/*      */   
/*      */ 
/*      */   private final Channel parent;
/*      */   
/*      */   private final ChannelId id;
/*      */   private final Channel.Unsafe unsafe;
/*      */   private final DefaultChannelPipeline pipeline;
/*   58 */   private final ChannelFuture succeededFuture = new SucceededChannelFuture(this, null);
/*   59 */   private final VoidChannelPromise voidPromise = new VoidChannelPromise(this, true);
/*   60 */   private final VoidChannelPromise unsafeVoidPromise = new VoidChannelPromise(this, false);
/*   61 */   private final CloseFuture closeFuture = new CloseFuture(this);
/*      */   
/*      */ 
/*      */   private volatile SocketAddress localAddress;
/*      */   
/*      */   private volatile SocketAddress remoteAddress;
/*      */   
/*      */   private volatile PausableChannelEventLoop eventLoop;
/*      */   
/*      */   private volatile boolean registered;
/*      */   
/*      */   private boolean strValActive;
/*      */   
/*      */   private String strVal;
/*      */   
/*      */ 
/*      */   protected AbstractChannel(Channel parent)
/*      */   {
/*   79 */     this.parent = parent;
/*   80 */     this.id = DefaultChannelId.newInstance();
/*   81 */     this.unsafe = newUnsafe();
/*   82 */     this.pipeline = new DefaultChannelPipeline(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected AbstractChannel(Channel parent, ChannelId id)
/*      */   {
/*   92 */     this.parent = parent;
/*   93 */     this.id = id;
/*   94 */     this.unsafe = newUnsafe();
/*   95 */     this.pipeline = new DefaultChannelPipeline(this);
/*      */   }
/*      */   
/*      */   public final ChannelId id()
/*      */   {
/*  100 */     return this.id;
/*      */   }
/*      */   
/*      */   public boolean isWritable()
/*      */   {
/*  105 */     ChannelOutboundBuffer buf = this.unsafe.outboundBuffer();
/*  106 */     return (buf != null) && (buf.isWritable());
/*      */   }
/*      */   
/*      */   public Channel parent()
/*      */   {
/*  111 */     return this.parent;
/*      */   }
/*      */   
/*      */   public ChannelPipeline pipeline()
/*      */   {
/*  116 */     return this.pipeline;
/*      */   }
/*      */   
/*      */   public ByteBufAllocator alloc()
/*      */   {
/*  121 */     return config().getAllocator();
/*      */   }
/*      */   
/*      */   public final EventLoop eventLoop()
/*      */   {
/*  126 */     EventLoop eventLoop = this.eventLoop;
/*  127 */     if (eventLoop == null) {
/*  128 */       throw new IllegalStateException("channel not registered to an event loop");
/*      */     }
/*  130 */     return eventLoop;
/*      */   }
/*      */   
/*      */   public SocketAddress localAddress()
/*      */   {
/*  135 */     SocketAddress localAddress = this.localAddress;
/*  136 */     if (localAddress == null) {
/*      */       try {
/*  138 */         this.localAddress = (localAddress = unsafe().localAddress());
/*      */       }
/*      */       catch (Throwable t) {
/*  141 */         return null;
/*      */       }
/*      */     }
/*  144 */     return localAddress;
/*      */   }
/*      */   
/*      */   protected void invalidateLocalAddress() {
/*  148 */     this.localAddress = null;
/*      */   }
/*      */   
/*      */   public SocketAddress remoteAddress()
/*      */   {
/*  153 */     SocketAddress remoteAddress = this.remoteAddress;
/*  154 */     if (remoteAddress == null) {
/*      */       try {
/*  156 */         this.remoteAddress = (remoteAddress = unsafe().remoteAddress());
/*      */       }
/*      */       catch (Throwable t) {
/*  159 */         return null;
/*      */       }
/*      */     }
/*  162 */     return remoteAddress;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void invalidateRemoteAddress()
/*      */   {
/*  169 */     this.remoteAddress = null;
/*      */   }
/*      */   
/*      */   public boolean isRegistered()
/*      */   {
/*  174 */     return this.registered;
/*      */   }
/*      */   
/*      */   public ChannelFuture bind(SocketAddress localAddress)
/*      */   {
/*  179 */     return this.pipeline.bind(localAddress);
/*      */   }
/*      */   
/*      */   public ChannelFuture connect(SocketAddress remoteAddress)
/*      */   {
/*  184 */     return this.pipeline.connect(remoteAddress);
/*      */   }
/*      */   
/*      */   public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress)
/*      */   {
/*  189 */     return this.pipeline.connect(remoteAddress, localAddress);
/*      */   }
/*      */   
/*      */   public ChannelFuture disconnect()
/*      */   {
/*  194 */     return this.pipeline.disconnect();
/*      */   }
/*      */   
/*      */   public ChannelFuture close()
/*      */   {
/*  199 */     return this.pipeline.close();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ChannelFuture deregister()
/*      */   {
/*  224 */     this.eventLoop.rejectNewTasks();
/*  225 */     return this.pipeline.deregister();
/*      */   }
/*      */   
/*      */   public Channel flush()
/*      */   {
/*  230 */     this.pipeline.flush();
/*  231 */     return this;
/*      */   }
/*      */   
/*      */   public ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise)
/*      */   {
/*  236 */     return this.pipeline.bind(localAddress, promise);
/*      */   }
/*      */   
/*      */   public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise)
/*      */   {
/*  241 */     return this.pipeline.connect(remoteAddress, promise);
/*      */   }
/*      */   
/*      */   public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
/*      */   {
/*  246 */     return this.pipeline.connect(remoteAddress, localAddress, promise);
/*      */   }
/*      */   
/*      */   public ChannelFuture disconnect(ChannelPromise promise)
/*      */   {
/*  251 */     return this.pipeline.disconnect(promise);
/*      */   }
/*      */   
/*      */   public ChannelFuture close(ChannelPromise promise)
/*      */   {
/*  256 */     return this.pipeline.close(promise);
/*      */   }
/*      */   
/*      */   public ChannelFuture deregister(ChannelPromise promise)
/*      */   {
/*  261 */     this.eventLoop.rejectNewTasks();
/*  262 */     return this.pipeline.deregister(promise);
/*      */   }
/*      */   
/*      */   public Channel read()
/*      */   {
/*  267 */     this.pipeline.read();
/*  268 */     return this;
/*      */   }
/*      */   
/*      */   public ChannelFuture write(Object msg)
/*      */   {
/*  273 */     return this.pipeline.write(msg);
/*      */   }
/*      */   
/*      */   public ChannelFuture write(Object msg, ChannelPromise promise)
/*      */   {
/*  278 */     return this.pipeline.write(msg, promise);
/*      */   }
/*      */   
/*      */   public ChannelFuture writeAndFlush(Object msg)
/*      */   {
/*  283 */     return this.pipeline.writeAndFlush(msg);
/*      */   }
/*      */   
/*      */   public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise)
/*      */   {
/*  288 */     return this.pipeline.writeAndFlush(msg, promise);
/*      */   }
/*      */   
/*      */   public ChannelPromise newPromise()
/*      */   {
/*  293 */     return new DefaultChannelPromise(this);
/*      */   }
/*      */   
/*      */   public ChannelProgressivePromise newProgressivePromise()
/*      */   {
/*  298 */     return new DefaultChannelProgressivePromise(this);
/*      */   }
/*      */   
/*      */   public ChannelFuture newSucceededFuture()
/*      */   {
/*  303 */     return this.succeededFuture;
/*      */   }
/*      */   
/*      */   public ChannelFuture newFailedFuture(Throwable cause)
/*      */   {
/*  308 */     return new FailedChannelFuture(this, null, cause);
/*      */   }
/*      */   
/*      */   public ChannelFuture closeFuture()
/*      */   {
/*  313 */     return this.closeFuture;
/*      */   }
/*      */   
/*      */   public Channel.Unsafe unsafe()
/*      */   {
/*  318 */     return this.unsafe;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final int hashCode()
/*      */   {
/*  331 */     return this.id.hashCode();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final boolean equals(Object o)
/*      */   {
/*  340 */     return this == o;
/*      */   }
/*      */   
/*      */   public final int compareTo(Channel o)
/*      */   {
/*  345 */     if (this == o) {
/*  346 */       return 0;
/*      */     }
/*      */     
/*  349 */     return id().compareTo(o.id());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/*  360 */     boolean active = isActive();
/*  361 */     if ((this.strValActive == active) && (this.strVal != null)) {
/*  362 */       return this.strVal;
/*      */     }
/*      */     
/*  365 */     SocketAddress remoteAddr = remoteAddress();
/*  366 */     SocketAddress localAddr = localAddress();
/*  367 */     if (remoteAddr != null) { SocketAddress dstAddr;
/*      */       SocketAddress srcAddr;
/*      */       SocketAddress dstAddr;
/*  370 */       if (this.parent == null) {
/*  371 */         SocketAddress srcAddr = localAddr;
/*  372 */         dstAddr = remoteAddr;
/*      */       } else {
/*  374 */         srcAddr = remoteAddr;
/*  375 */         dstAddr = localAddr;
/*      */       }
/*      */       
/*  378 */       StringBuilder buf = new StringBuilder(96).append("[id: 0x").append(this.id.asShortText()).append(", ").append(srcAddr).append(active ? " => " : " :> ").append(dstAddr).append(']');
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  386 */       this.strVal = buf.toString();
/*  387 */     } else if (localAddr != null) {
/*  388 */       StringBuilder buf = new StringBuilder(64).append("[id: 0x").append(this.id.asShortText()).append(", ").append(localAddr).append(']');
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  394 */       this.strVal = buf.toString();
/*      */     } else {
/*  396 */       StringBuilder buf = new StringBuilder(16).append("[id: 0x").append(this.id.asShortText()).append(']');
/*      */       
/*      */ 
/*      */ 
/*  400 */       this.strVal = buf.toString();
/*      */     }
/*      */     
/*  403 */     this.strValActive = active;
/*  404 */     return this.strVal;
/*      */   }
/*      */   
/*      */   public final ChannelPromise voidPromise()
/*      */   {
/*  409 */     return this.voidPromise;
/*      */   }
/*      */   
/*      */   final MessageSizeEstimator.Handle estimatorHandle() {
/*  413 */     if (this.estimatorHandle == null) {
/*  414 */       this.estimatorHandle = config().getMessageSizeEstimator().newHandle();
/*      */     }
/*  416 */     return this.estimatorHandle;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected abstract class AbstractUnsafe
/*      */     implements Channel.Unsafe
/*      */   {
/*  424 */     private ChannelOutboundBuffer outboundBuffer = new ChannelOutboundBuffer(AbstractChannel.this);
/*      */     
/*      */     private RecvByteBufAllocator.Handle recvHandle;
/*      */     private boolean inFlush0;
/*  428 */     private boolean neverRegistered = true;
/*      */     
/*      */     protected AbstractUnsafe() {}
/*      */     
/*  432 */     public RecvByteBufAllocator.Handle recvBufAllocHandle() { if (this.recvHandle == null) {
/*  433 */         this.recvHandle = AbstractChannel.this.config().getRecvByteBufAllocator().newHandle();
/*      */       }
/*  435 */       return this.recvHandle;
/*      */     }
/*      */     
/*      */ 
/*      */     public final ChannelHandlerInvoker invoker()
/*      */     {
/*  441 */       return ((PausableChannelEventExecutor)AbstractChannel.this.eventLoop().asInvoker()).unwrapInvoker();
/*      */     }
/*      */     
/*      */     public final ChannelOutboundBuffer outboundBuffer()
/*      */     {
/*  446 */       return this.outboundBuffer;
/*      */     }
/*      */     
/*      */     public final SocketAddress localAddress()
/*      */     {
/*  451 */       return AbstractChannel.this.localAddress0();
/*      */     }
/*      */     
/*      */     public final SocketAddress remoteAddress()
/*      */     {
/*  456 */       return AbstractChannel.this.remoteAddress0();
/*      */     }
/*      */     
/*      */     public final void register(EventLoop eventLoop, final ChannelPromise promise)
/*      */     {
/*  461 */       if (eventLoop == null) {
/*  462 */         throw new NullPointerException("eventLoop");
/*      */       }
/*  464 */       if (promise == null) {
/*  465 */         throw new NullPointerException("promise");
/*      */       }
/*  467 */       if (AbstractChannel.this.isRegistered()) {
/*  468 */         promise.setFailure(new IllegalStateException("registered to an event loop already"));
/*  469 */         return;
/*      */       }
/*  471 */       if (!AbstractChannel.this.isCompatible(eventLoop)) {
/*  472 */         promise.setFailure(new IllegalStateException("incompatible event loop type: " + eventLoop.getClass().getName()));
/*      */         
/*  474 */         return;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  479 */       if (AbstractChannel.this.eventLoop == null) {
/*  480 */         AbstractChannel.this.eventLoop = new AbstractChannel.PausableChannelEventLoop(AbstractChannel.this, eventLoop);
/*      */       } else {
/*  482 */         AbstractChannel.this.eventLoop.unwrapped = eventLoop;
/*      */       }
/*      */       
/*  485 */       if (eventLoop.inEventLoop()) {
/*  486 */         register0(promise);
/*      */       } else {
/*      */         try {
/*  489 */           eventLoop.execute(new OneTimeTask()
/*      */           {
/*      */             public void run() {
/*  492 */               AbstractChannel.AbstractUnsafe.this.register0(promise);
/*      */             }
/*      */           });
/*      */         } catch (Throwable t) {
/*  496 */           AbstractChannel.logger.warn("Force-closing a channel whose registration task was not accepted by an event loop: {}", AbstractChannel.this, t);
/*      */           
/*      */ 
/*  499 */           closeForcibly();
/*  500 */           AbstractChannel.this.closeFuture.setClosed();
/*  501 */           safeSetFailure(promise, t);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     private void register0(ChannelPromise promise)
/*      */     {
/*      */       try
/*      */       {
/*  510 */         if ((!promise.setUncancellable()) || (!ensureOpen(promise))) {
/*  511 */           return;
/*      */         }
/*  513 */         boolean firstRegistration = this.neverRegistered;
/*  514 */         AbstractChannel.this.doRegister();
/*  515 */         this.neverRegistered = false;
/*  516 */         AbstractChannel.this.registered = true;
/*  517 */         AbstractChannel.this.eventLoop.acceptNewTasks();
/*  518 */         safeSetSuccess(promise);
/*  519 */         AbstractChannel.this.pipeline.fireChannelRegistered();
/*      */         
/*      */ 
/*  522 */         if ((firstRegistration) && (AbstractChannel.this.isActive())) {
/*  523 */           AbstractChannel.this.pipeline.fireChannelActive();
/*      */         }
/*      */       }
/*      */       catch (Throwable t) {
/*  527 */         closeForcibly();
/*  528 */         AbstractChannel.this.closeFuture.setClosed();
/*  529 */         safeSetFailure(promise, t);
/*      */       }
/*      */     }
/*      */     
/*      */     public final void bind(SocketAddress localAddress, ChannelPromise promise)
/*      */     {
/*  535 */       if ((!promise.setUncancellable()) || (!ensureOpen(promise))) {
/*  536 */         return;
/*      */       }
/*      */       
/*      */ 
/*  540 */       if ((Boolean.TRUE.equals(AbstractChannel.this.config().getOption(ChannelOption.SO_BROADCAST))) && ((localAddress instanceof InetSocketAddress)) && (!((InetSocketAddress)localAddress).getAddress().isAnyLocalAddress()) && (!PlatformDependent.isWindows()) && (!PlatformDependent.isRoot()))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  546 */         AbstractChannel.logger.warn("A non-root user can't receive a broadcast packet if the socket is not bound to a wildcard address; binding to a non-wildcard address (" + localAddress + ") anyway as requested.");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  552 */       boolean wasActive = AbstractChannel.this.isActive();
/*      */       try {
/*  554 */         AbstractChannel.this.doBind(localAddress);
/*      */       } catch (Throwable t) {
/*  556 */         safeSetFailure(promise, t);
/*  557 */         closeIfClosed();
/*  558 */         return;
/*      */       }
/*      */       
/*  561 */       if ((!wasActive) && (AbstractChannel.this.isActive())) {
/*  562 */         invokeLater(new OneTimeTask()
/*      */         {
/*      */           public void run() {
/*  565 */             AbstractChannel.this.pipeline.fireChannelActive();
/*      */           }
/*      */         });
/*      */       }
/*      */       
/*  570 */       safeSetSuccess(promise);
/*      */     }
/*      */     
/*      */     public final void disconnect(ChannelPromise promise)
/*      */     {
/*  575 */       if (!promise.setUncancellable()) {
/*  576 */         return;
/*      */       }
/*      */       
/*  579 */       boolean wasActive = AbstractChannel.this.isActive();
/*      */       try {
/*  581 */         AbstractChannel.this.doDisconnect();
/*      */       } catch (Throwable t) {
/*  583 */         safeSetFailure(promise, t);
/*  584 */         closeIfClosed();
/*  585 */         return;
/*      */       }
/*      */       
/*  588 */       if ((wasActive) && (!AbstractChannel.this.isActive())) {
/*  589 */         invokeLater(new OneTimeTask()
/*      */         {
/*      */           public void run() {
/*  592 */             AbstractChannel.this.pipeline.fireChannelInactive();
/*      */           }
/*      */         });
/*      */       }
/*      */       
/*  597 */       safeSetSuccess(promise);
/*  598 */       closeIfClosed();
/*      */     }
/*      */     
/*      */     public final void close(final ChannelPromise promise)
/*      */     {
/*  603 */       if (!promise.setUncancellable()) {
/*  604 */         return;
/*      */       }
/*      */       
/*  607 */       if (this.inFlush0) {
/*  608 */         invokeLater(new OneTimeTask()
/*      */         {
/*      */           public void run() {
/*  611 */             AbstractChannel.AbstractUnsafe.this.close(promise);
/*      */           }
/*  613 */         });
/*  614 */         return;
/*      */       }
/*      */       
/*  617 */       if (this.outboundBuffer == null)
/*      */       {
/*  619 */         AbstractChannel.this.closeFuture.addListener(new ChannelFutureListener()
/*      */         {
/*      */           public void operationComplete(ChannelFuture future) throws Exception {
/*  622 */             promise.setSuccess();
/*      */           }
/*  624 */         });
/*  625 */         return;
/*      */       }
/*      */       
/*  628 */       if (AbstractChannel.this.closeFuture.isDone())
/*      */       {
/*  630 */         safeSetSuccess(promise);
/*  631 */         return;
/*      */       }
/*      */       
/*  634 */       final boolean wasActive = AbstractChannel.this.isActive();
/*  635 */       final ChannelOutboundBuffer buffer = this.outboundBuffer;
/*  636 */       this.outboundBuffer = null;
/*  637 */       Executor closeExecutor = closeExecutor();
/*  638 */       if (closeExecutor != null) {
/*  639 */         closeExecutor.execute(new OneTimeTask()
/*      */         {
/*      */           public void run() {
/*  642 */             Throwable cause = null;
/*      */             try {
/*  644 */               AbstractChannel.this.doClose();
/*      */             } catch (Throwable t) {
/*  646 */               cause = t;
/*      */             }
/*  648 */             final Throwable error = cause;
/*      */             
/*  650 */             AbstractChannel.AbstractUnsafe.this.invokeLater(new OneTimeTask()
/*      */             {
/*      */               public void run() {
/*  653 */                 AbstractChannel.AbstractUnsafe.this.closeAndDeregister(AbstractChannel.AbstractUnsafe.6.this.val$buffer, AbstractChannel.AbstractUnsafe.6.this.val$wasActive, AbstractChannel.AbstractUnsafe.6.this.val$promise, error);
/*      */               }
/*      */             });
/*      */           }
/*      */         });
/*      */       } else {
/*  659 */         Throwable error = null;
/*      */         try {
/*  661 */           AbstractChannel.this.doClose();
/*      */         } catch (Throwable t) {
/*  663 */           error = t;
/*      */         }
/*  665 */         closeAndDeregister(buffer, wasActive, promise, error);
/*      */       }
/*      */     }
/*      */     
/*      */     private void closeAndDeregister(ChannelOutboundBuffer outboundBuffer, boolean wasActive, ChannelPromise promise, Throwable error)
/*      */     {
/*      */       try
/*      */       {
/*  673 */         outboundBuffer.failFlushed(AbstractChannel.CLOSED_CHANNEL_EXCEPTION);
/*  674 */         outboundBuffer.close(AbstractChannel.CLOSED_CHANNEL_EXCEPTION);
/*      */       } finally {
/*  676 */         if ((wasActive) && (!AbstractChannel.this.isActive())) {
/*  677 */           invokeLater(new OneTimeTask()
/*      */           {
/*      */             public void run() {
/*  680 */               AbstractChannel.this.pipeline.fireChannelInactive();
/*  681 */               AbstractChannel.AbstractUnsafe.this.deregister(AbstractChannel.AbstractUnsafe.this.voidPromise());
/*      */             }
/*      */           });
/*      */         } else {
/*  685 */           invokeLater(new OneTimeTask()
/*      */           {
/*      */             public void run() {
/*  688 */               AbstractChannel.AbstractUnsafe.this.deregister(AbstractChannel.AbstractUnsafe.this.voidPromise());
/*      */             }
/*      */           });
/*      */         }
/*      */         
/*      */ 
/*  694 */         AbstractChannel.this.closeFuture.setClosed();
/*  695 */         if (error != null) {
/*  696 */           safeSetFailure(promise, error);
/*      */         } else {
/*  698 */           safeSetSuccess(promise);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public final void closeForcibly()
/*      */     {
/*      */       try {
/*  706 */         AbstractChannel.this.doClose();
/*      */       } catch (Exception e) {
/*  708 */         AbstractChannel.logger.warn("Failed to close a channel.", e);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public final void deregister(ChannelPromise promise)
/*      */     {
/*  721 */       if (!promise.setUncancellable()) {
/*  722 */         return;
/*      */       }
/*      */       
/*  725 */       if (!AbstractChannel.this.registered) {
/*  726 */         safeSetSuccess(promise);
/*  727 */         return;
/*      */       }
/*      */       try
/*      */       {
/*  731 */         AbstractChannel.this.doDeregister();
/*      */       } catch (Throwable t) {
/*  733 */         safeSetFailure(promise, t);
/*  734 */         AbstractChannel.logger.warn("Unexpected exception occurred while deregistering a channel.", t);
/*      */       } finally {
/*  736 */         if (AbstractChannel.this.registered) {
/*  737 */           AbstractChannel.this.registered = false;
/*  738 */           safeSetSuccess(promise);
/*  739 */           AbstractChannel.this.pipeline.fireChannelUnregistered();
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  744 */           safeSetSuccess(promise);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public final void beginRead()
/*      */     {
/*  751 */       if (!AbstractChannel.this.isActive()) {
/*  752 */         return;
/*      */       }
/*      */       try
/*      */       {
/*  756 */         AbstractChannel.this.doBeginRead();
/*      */       } catch (Exception e) {
/*  758 */         invokeLater(new OneTimeTask()
/*      */         {
/*      */           public void run() {
/*  761 */             AbstractChannel.this.pipeline.fireExceptionCaught(e);
/*      */           }
/*  763 */         });
/*  764 */         close(voidPromise());
/*      */       }
/*      */     }
/*      */     
/*      */     public final void write(Object msg, ChannelPromise promise)
/*      */     {
/*  770 */       ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
/*  771 */       if (outboundBuffer == null)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  776 */         safeSetFailure(promise, AbstractChannel.CLOSED_CHANNEL_EXCEPTION);
/*      */         
/*  778 */         ReferenceCountUtil.release(msg); return;
/*      */       }
/*      */       
/*      */       int size;
/*      */       try
/*      */       {
/*  784 */         msg = AbstractChannel.this.filterOutboundMessage(msg);
/*  785 */         size = AbstractChannel.this.estimatorHandle().size(msg);
/*  786 */         if (size < 0) {
/*  787 */           size = 0;
/*      */         }
/*      */       } catch (Throwable t) {
/*  790 */         safeSetFailure(promise, t);
/*  791 */         ReferenceCountUtil.release(msg);
/*  792 */         return;
/*      */       }
/*      */       
/*  795 */       outboundBuffer.addMessage(msg, size, promise);
/*      */     }
/*      */     
/*      */     public final void flush()
/*      */     {
/*  800 */       ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
/*  801 */       if (outboundBuffer == null) {
/*  802 */         return;
/*      */       }
/*      */       
/*  805 */       outboundBuffer.addFlush();
/*  806 */       flush0();
/*      */     }
/*      */     
/*      */     protected void flush0() {
/*  810 */       if (this.inFlush0)
/*      */       {
/*  812 */         return;
/*      */       }
/*      */       
/*  815 */       ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
/*  816 */       if ((outboundBuffer == null) || (outboundBuffer.isEmpty())) {
/*  817 */         return;
/*      */       }
/*      */       
/*  820 */       this.inFlush0 = true;
/*      */       
/*      */ 
/*  823 */       if (!AbstractChannel.this.isActive()) {
/*      */         try {
/*  825 */           if (AbstractChannel.this.isOpen()) {
/*  826 */             outboundBuffer.failFlushed(AbstractChannel.NOT_YET_CONNECTED_EXCEPTION);
/*      */           } else {
/*  828 */             outboundBuffer.failFlushed(AbstractChannel.CLOSED_CHANNEL_EXCEPTION);
/*      */           }
/*      */         } finally {
/*  831 */           this.inFlush0 = false;
/*      */         }
/*  833 */         return;
/*      */       }
/*      */       try
/*      */       {
/*  837 */         AbstractChannel.this.doWrite(outboundBuffer);
/*      */       } catch (Throwable t) {
/*  839 */         outboundBuffer.failFlushed(t);
/*      */       } finally {
/*  841 */         this.inFlush0 = false;
/*      */       }
/*      */     }
/*      */     
/*      */     public final ChannelPromise voidPromise()
/*      */     {
/*  847 */       return AbstractChannel.this.unsafeVoidPromise;
/*      */     }
/*      */     
/*      */     protected final boolean ensureOpen(ChannelPromise promise) {
/*  851 */       if (AbstractChannel.this.isOpen()) {
/*  852 */         return true;
/*      */       }
/*      */       
/*  855 */       safeSetFailure(promise, AbstractChannel.CLOSED_CHANNEL_EXCEPTION);
/*  856 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     protected final void safeSetSuccess(ChannelPromise promise)
/*      */     {
/*  863 */       if ((!(promise instanceof VoidChannelPromise)) && (!promise.trySuccess())) {
/*  864 */         AbstractChannel.logger.warn("Failed to mark a promise as success because it is done already: {}", promise);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     protected final void safeSetFailure(ChannelPromise promise, Throwable cause)
/*      */     {
/*  872 */       if ((!(promise instanceof VoidChannelPromise)) && (!promise.tryFailure(cause))) {
/*  873 */         AbstractChannel.logger.warn("Failed to mark a promise as failure because it's done already: {}", promise, cause);
/*      */       }
/*      */     }
/*      */     
/*      */     protected final void closeIfClosed() {
/*  878 */       if (AbstractChannel.this.isOpen()) {
/*  879 */         return;
/*      */       }
/*  881 */       close(voidPromise());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private void invokeLater(Runnable task)
/*      */     {
/*      */       try
/*      */       {
/*  897 */         AbstractChannel.this.eventLoop().unwrap().execute(task);
/*      */       } catch (RejectedExecutionException e) {
/*  899 */         AbstractChannel.logger.warn("Can't invoke task later as EventLoop rejected it", e);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     protected final Throwable annotateConnectException(Throwable cause, SocketAddress remoteAddress)
/*      */     {
/*  907 */       if ((cause instanceof ConnectException)) {
/*  908 */         Throwable newT = new ConnectException(cause.getMessage() + ": " + remoteAddress);
/*  909 */         newT.setStackTrace(cause.getStackTrace());
/*  910 */         cause = newT;
/*  911 */       } else if ((cause instanceof NoRouteToHostException)) {
/*  912 */         Throwable newT = new NoRouteToHostException(cause.getMessage() + ": " + remoteAddress);
/*  913 */         newT.setStackTrace(cause.getStackTrace());
/*  914 */         cause = newT;
/*  915 */       } else if ((cause instanceof SocketException)) {
/*  916 */         Throwable newT = new SocketException(cause.getMessage() + ": " + remoteAddress);
/*  917 */         newT.setStackTrace(cause.getStackTrace());
/*  918 */         cause = newT;
/*      */       }
/*      */       
/*  921 */       return cause;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected Executor closeExecutor()
/*      */     {
/*  930 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  997 */   protected Object filterOutboundMessage(Object msg)
/*  997 */     throws Exception { return msg; }
/*      */   
/*      */   protected abstract AbstractUnsafe newUnsafe();
/*      */   
/*      */   protected abstract boolean isCompatible(EventLoop paramEventLoop);
/*      */   
/* 1003 */   static final class CloseFuture extends DefaultChannelPromise { CloseFuture(AbstractChannel ch) { super(); }
/*      */     
/*      */ 
/*      */     public ChannelPromise setSuccess()
/*      */     {
/* 1008 */       throw new IllegalStateException();
/*      */     }
/*      */     
/*      */     public ChannelPromise setFailure(Throwable cause)
/*      */     {
/* 1013 */       throw new IllegalStateException();
/*      */     }
/*      */     
/*      */     public boolean trySuccess()
/*      */     {
/* 1018 */       throw new IllegalStateException();
/*      */     }
/*      */     
/*      */     public boolean tryFailure(Throwable cause)
/*      */     {
/* 1023 */       throw new IllegalStateException();
/*      */     }
/*      */     
/*      */     boolean setClosed() {
/* 1027 */       return super.trySuccess();
/*      */     }
/*      */   }
/*      */   
/*      */   protected abstract SocketAddress localAddress0();
/*      */   
/*      */   private final class PausableChannelEventLoop extends PausableChannelEventExecutor implements EventLoop {
/* 1034 */     volatile boolean isAcceptingNewTasks = true;
/*      */     volatile EventLoop unwrapped;
/*      */     
/*      */     PausableChannelEventLoop(EventLoop unwrapped) {
/* 1038 */       this.unwrapped = unwrapped;
/*      */     }
/*      */     
/*      */     public void rejectNewTasks()
/*      */     {
/* 1043 */       this.isAcceptingNewTasks = false;
/*      */     }
/*      */     
/*      */     public void acceptNewTasks()
/*      */     {
/* 1048 */       this.isAcceptingNewTasks = true;
/*      */     }
/*      */     
/*      */     public boolean isAcceptingNewTasks()
/*      */     {
/* 1053 */       return this.isAcceptingNewTasks;
/*      */     }
/*      */     
/*      */     public EventLoopGroup parent()
/*      */     {
/* 1058 */       return unwrap().parent();
/*      */     }
/*      */     
/*      */     public EventLoop next()
/*      */     {
/* 1063 */       return unwrap().next();
/*      */     }
/*      */     
/*      */     public EventLoop unwrap()
/*      */     {
/* 1068 */       return this.unwrapped;
/*      */     }
/*      */     
/*      */     public ChannelHandlerInvoker asInvoker()
/*      */     {
/* 1073 */       return this;
/*      */     }
/*      */     
/*      */     public ChannelFuture register(Channel channel)
/*      */     {
/* 1078 */       return unwrap().register(channel);
/*      */     }
/*      */     
/*      */     public ChannelFuture register(Channel channel, ChannelPromise promise)
/*      */     {
/* 1083 */       return unwrap().register(channel, promise);
/*      */     }
/*      */     
/*      */     Channel channel()
/*      */     {
/* 1088 */       return AbstractChannel.this;
/*      */     }
/*      */     
/*      */     ChannelHandlerInvoker unwrapInvoker()
/*      */     {
/* 1093 */       return this.unwrapped.asInvoker();
/*      */     }
/*      */   }
/*      */   
/*      */   protected abstract SocketAddress remoteAddress0();
/*      */   
/*      */   protected void doRegister()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */   protected abstract void doBind(SocketAddress paramSocketAddress)
/*      */     throws Exception;
/*      */   
/*      */   protected abstract void doDisconnect()
/*      */     throws Exception;
/*      */   
/*      */   protected abstract void doClose()
/*      */     throws Exception;
/*      */   
/*      */   protected void doDeregister()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */   protected abstract void doBeginRead()
/*      */     throws Exception;
/*      */   
/*      */   protected abstract void doWrite(ChannelOutboundBuffer paramChannelOutboundBuffer)
/*      */     throws Exception;
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\AbstractChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */