/*     */ package io.netty.channel.local;
/*     */ 
/*     */ import io.netty.channel.AbstractChannel;
/*     */ import io.netty.channel.AbstractChannel.AbstractUnsafe;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.Channel.Unsafe;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.DefaultChannelConfig;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.SingleThreadEventLoop;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.internal.InternalThreadLocalMap;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.AlreadyConnectedException;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.ConnectionPendingException;
/*     */ import java.nio.channels.NotYetConnectedException;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Collections;
/*     */ import java.util.Queue;
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
/*     */ public class LocalChannel
/*     */   extends AbstractChannel
/*     */ {
/*     */   private static enum State
/*     */   {
/*  46 */     OPEN,  BOUND,  CONNECTED,  CLOSED;
/*     */     private State() {} }
/*  48 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
/*     */   
/*     */   private static final int MAX_READER_STACK_DEPTH = 8;
/*     */   
/*  52 */   private final ChannelConfig config = new DefaultChannelConfig(this);
/*  53 */   private final Queue<Object> inboundBuffer = new ArrayDeque();
/*  54 */   private final Runnable readTask = new Runnable()
/*     */   {
/*     */     public void run() {
/*  57 */       ChannelPipeline pipeline = LocalChannel.this.pipeline();
/*     */       for (;;) {
/*  59 */         Object m = LocalChannel.this.inboundBuffer.poll();
/*  60 */         if (m == null) {
/*     */           break;
/*     */         }
/*  63 */         pipeline.fireChannelRead(m);
/*     */       }
/*  65 */       pipeline.fireChannelReadComplete();
/*     */     }
/*     */   };
/*     */   
/*  69 */   private final Runnable shutdownHook = new Runnable()
/*     */   {
/*     */     public void run() {
/*  72 */       LocalChannel.this.unsafe().close(LocalChannel.this.unsafe().voidPromise());
/*     */     }
/*     */   };
/*     */   private volatile State state;
/*     */   private volatile LocalChannel peer;
/*     */   private volatile LocalAddress localAddress;
/*     */   private volatile LocalAddress remoteAddress;
/*     */   private volatile ChannelPromise connectPromise;
/*     */   private volatile boolean readInProgress;
/*     */   private volatile boolean registerInProgress;
/*     */   
/*     */   public LocalChannel()
/*     */   {
/*  85 */     super(null);
/*     */   }
/*     */   
/*     */   LocalChannel(LocalServerChannel parent, LocalChannel peer) {
/*  89 */     super(parent);
/*  90 */     this.peer = peer;
/*  91 */     this.localAddress = parent.localAddress();
/*  92 */     this.remoteAddress = peer.localAddress();
/*     */   }
/*     */   
/*     */   public ChannelMetadata metadata()
/*     */   {
/*  97 */     return METADATA;
/*     */   }
/*     */   
/*     */   public ChannelConfig config()
/*     */   {
/* 102 */     return this.config;
/*     */   }
/*     */   
/*     */   public LocalServerChannel parent()
/*     */   {
/* 107 */     return (LocalServerChannel)super.parent();
/*     */   }
/*     */   
/*     */   public LocalAddress localAddress()
/*     */   {
/* 112 */     return (LocalAddress)super.localAddress();
/*     */   }
/*     */   
/*     */   public LocalAddress remoteAddress()
/*     */   {
/* 117 */     return (LocalAddress)super.remoteAddress();
/*     */   }
/*     */   
/*     */   public boolean isOpen()
/*     */   {
/* 122 */     return this.state != State.CLOSED;
/*     */   }
/*     */   
/*     */   public boolean isActive()
/*     */   {
/* 127 */     return this.state == State.CONNECTED;
/*     */   }
/*     */   
/*     */   protected AbstractChannel.AbstractUnsafe newUnsafe()
/*     */   {
/* 132 */     return new LocalUnsafe(null);
/*     */   }
/*     */   
/*     */   protected boolean isCompatible(EventLoop loop)
/*     */   {
/* 137 */     return loop instanceof SingleThreadEventLoop;
/*     */   }
/*     */   
/*     */   protected SocketAddress localAddress0()
/*     */   {
/* 142 */     return this.localAddress;
/*     */   }
/*     */   
/*     */   protected SocketAddress remoteAddress0()
/*     */   {
/* 147 */     return this.remoteAddress;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doRegister()
/*     */     throws Exception
/*     */   {
/* 157 */     if ((this.peer != null) && (parent() != null))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 164 */       final LocalChannel peer = this.peer;
/* 165 */       this.registerInProgress = true;
/* 166 */       this.state = State.CONNECTED;
/*     */       
/* 168 */       peer.remoteAddress = (parent() == null ? null : parent().localAddress());
/* 169 */       peer.state = State.CONNECTED;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 175 */       peer.eventLoop().execute(new Runnable()
/*     */       {
/*     */         public void run() {
/* 178 */           LocalChannel.this.registerInProgress = false;
/* 179 */           peer.pipeline().fireChannelActive();
/* 180 */           peer.connectPromise.setSuccess();
/*     */         }
/*     */       });
/*     */     }
/* 184 */     ((SingleThreadEventLoop)eventLoop().unwrap()).addShutdownHook(this.shutdownHook);
/*     */   }
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception
/*     */   {
/* 189 */     this.localAddress = LocalChannelRegistry.register(this, this.localAddress, localAddress);
/*     */     
/*     */ 
/* 192 */     this.state = State.BOUND;
/*     */   }
/*     */   
/*     */   protected void doDisconnect() throws Exception
/*     */   {
/* 197 */     doClose();
/*     */   }
/*     */   
/*     */   protected void doClose() throws Exception
/*     */   {
/* 202 */     if (this.state != State.CLOSED)
/*     */     {
/* 204 */       if (this.localAddress != null) {
/* 205 */         if (parent() == null) {
/* 206 */           LocalChannelRegistry.unregister(this.localAddress);
/*     */         }
/* 208 */         this.localAddress = null;
/*     */       }
/* 210 */       this.state = State.CLOSED;
/*     */     }
/*     */     
/* 213 */     final LocalChannel peer = this.peer;
/* 214 */     if ((peer != null) && (peer.isActive()))
/*     */     {
/*     */ 
/* 217 */       EventLoop eventLoop = peer.eventLoop();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 223 */       if ((eventLoop.inEventLoop()) && (!this.registerInProgress)) {
/* 224 */         peer.unsafe().close(unsafe().voidPromise());
/*     */       } else {
/* 226 */         peer.eventLoop().execute(new Runnable()
/*     */         {
/*     */           public void run() {
/* 229 */             peer.unsafe().close(LocalChannel.this.unsafe().voidPromise());
/*     */           }
/*     */         });
/*     */       }
/* 233 */       this.peer = null;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doDeregister()
/*     */     throws Exception
/*     */   {
/* 240 */     ((SingleThreadEventLoop)eventLoop().unwrap()).removeShutdownHook(this.shutdownHook);
/*     */   }
/*     */   
/*     */   protected void doBeginRead() throws Exception
/*     */   {
/* 245 */     if (this.readInProgress) {
/* 246 */       return;
/*     */     }
/*     */     
/* 249 */     ChannelPipeline pipeline = pipeline();
/* 250 */     Queue<Object> inboundBuffer = this.inboundBuffer;
/* 251 */     if (inboundBuffer.isEmpty()) {
/* 252 */       this.readInProgress = true;
/* 253 */       return;
/*     */     }
/*     */     
/* 256 */     InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
/* 257 */     Integer stackDepth = Integer.valueOf(threadLocals.localChannelReaderStackDepth());
/* 258 */     if (stackDepth.intValue() < 8) {
/* 259 */       threadLocals.setLocalChannelReaderStackDepth(stackDepth.intValue() + 1);
/*     */       try {
/*     */         for (;;) {
/* 262 */           Object received = inboundBuffer.poll();
/* 263 */           if (received == null) {
/*     */             break;
/*     */           }
/* 266 */           pipeline.fireChannelRead(received);
/*     */         }
/* 268 */         pipeline.fireChannelReadComplete();
/*     */       } finally {
/* 270 */         threadLocals.setLocalChannelReaderStackDepth(stackDepth.intValue());
/*     */       }
/*     */     } else {
/* 273 */       eventLoop().execute(this.readTask);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception
/*     */   {
/* 279 */     switch (this.state) {
/*     */     case OPEN: 
/*     */     case BOUND: 
/* 282 */       throw new NotYetConnectedException();
/*     */     case CLOSED: 
/* 284 */       throw new ClosedChannelException();
/*     */     }
/*     */     
/* 287 */     final LocalChannel peer = this.peer;
/* 288 */     final ChannelPipeline peerPipeline = peer.pipeline();
/* 289 */     EventLoop peerLoop = peer.eventLoop();
/*     */     
/* 291 */     if (peerLoop == eventLoop()) {
/*     */       for (;;) {
/* 293 */         Object msg = in.current();
/* 294 */         if (msg == null) {
/*     */           break;
/*     */         }
/* 297 */         peer.inboundBuffer.add(msg);
/* 298 */         ReferenceCountUtil.retain(msg);
/* 299 */         in.remove();
/*     */       }
/* 301 */       finishPeerRead(peer, peerPipeline);
/*     */     }
/*     */     else {
/* 304 */       final Object[] msgsCopy = new Object[in.size()];
/* 305 */       for (int i = 0; i < msgsCopy.length; i++) {
/* 306 */         msgsCopy[i] = ReferenceCountUtil.retain(in.current());
/* 307 */         in.remove();
/*     */       }
/*     */       
/* 310 */       peerLoop.execute(new Runnable()
/*     */       {
/*     */         public void run() {
/* 313 */           Collections.addAll(peer.inboundBuffer, msgsCopy);
/* 314 */           LocalChannel.finishPeerRead(peer, peerPipeline);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   private static void finishPeerRead(LocalChannel peer, ChannelPipeline peerPipeline) {
/* 321 */     if (peer.readInProgress) {
/* 322 */       peer.readInProgress = false;
/*     */       for (;;) {
/* 324 */         Object received = peer.inboundBuffer.poll();
/* 325 */         if (received == null) {
/*     */           break;
/*     */         }
/* 328 */         peerPipeline.fireChannelRead(received);
/*     */       }
/* 330 */       peerPipeline.fireChannelReadComplete();
/*     */     }
/*     */   }
/*     */   
/* 334 */   private class LocalUnsafe extends AbstractChannel.AbstractUnsafe { private LocalUnsafe() { super(); }
/*     */     
/*     */ 
/*     */     public void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
/*     */     {
/* 339 */       if ((!promise.setUncancellable()) || (!ensureOpen(promise))) {
/* 340 */         return;
/*     */       }
/*     */       
/* 343 */       if (LocalChannel.this.state == LocalChannel.State.CONNECTED) {
/* 344 */         Exception cause = new AlreadyConnectedException();
/* 345 */         safeSetFailure(promise, cause);
/* 346 */         LocalChannel.this.pipeline().fireExceptionCaught(cause);
/* 347 */         return;
/*     */       }
/*     */       
/* 350 */       if (LocalChannel.this.connectPromise != null) {
/* 351 */         throw new ConnectionPendingException();
/*     */       }
/*     */       
/* 354 */       LocalChannel.this.connectPromise = promise;
/*     */       
/* 356 */       if (LocalChannel.this.state != LocalChannel.State.BOUND)
/*     */       {
/* 358 */         if (localAddress == null) {
/* 359 */           localAddress = new LocalAddress(LocalChannel.this);
/*     */         }
/*     */       }
/*     */       
/* 363 */       if (localAddress != null) {
/*     */         try {
/* 365 */           LocalChannel.this.doBind(localAddress);
/*     */         } catch (Throwable t) {
/* 367 */           safeSetFailure(promise, t);
/* 368 */           close(voidPromise());
/* 369 */           return;
/*     */         }
/*     */       }
/*     */       
/* 373 */       Channel boundChannel = LocalChannelRegistry.get(remoteAddress);
/* 374 */       if (!(boundChannel instanceof LocalServerChannel)) {
/* 375 */         Exception cause = new ChannelException("connection refused");
/* 376 */         safeSetFailure(promise, cause);
/* 377 */         close(voidPromise());
/* 378 */         return;
/*     */       }
/*     */       
/* 381 */       LocalServerChannel serverChannel = (LocalServerChannel)boundChannel;
/* 382 */       LocalChannel.this.peer = serverChannel.serve(LocalChannel.this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\local\LocalChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */