/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.util.Attribute;
/*     */ import io.netty.util.AttributeKey;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.ResourceLeakHint;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.FastThreadLocal;
/*     */ import io.netty.util.concurrent.PausableEventExecutor;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
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
/*     */ abstract class AbstractChannelHandlerContext
/*     */   implements ChannelHandlerContext, ResourceLeakHint
/*     */ {
/*     */   static final int MASK_HANDLER_ADDED = 1;
/*     */   static final int MASK_HANDLER_REMOVED = 2;
/*     */   private static final int MASK_EXCEPTION_CAUGHT = 4;
/*     */   private static final int MASK_CHANNEL_REGISTERED = 8;
/*     */   private static final int MASK_CHANNEL_UNREGISTERED = 16;
/*     */   private static final int MASK_CHANNEL_ACTIVE = 32;
/*     */   private static final int MASK_CHANNEL_INACTIVE = 64;
/*     */   private static final int MASK_CHANNEL_READ = 128;
/*     */   private static final int MASK_CHANNEL_READ_COMPLETE = 256;
/*     */   private static final int MASK_CHANNEL_WRITABILITY_CHANGED = 512;
/*     */   private static final int MASK_USER_EVENT_TRIGGERED = 1024;
/*     */   private static final int MASK_BIND = 2048;
/*     */   private static final int MASK_CONNECT = 4096;
/*     */   private static final int MASK_DISCONNECT = 8192;
/*     */   private static final int MASK_CLOSE = 16384;
/*     */   private static final int MASK_DEREGISTER = 32768;
/*     */   private static final int MASK_READ = 65536;
/*     */   private static final int MASK_WRITE = 131072;
/*     */   private static final int MASK_FLUSH = 262144;
/*     */   private static final int MASKGROUP_INBOUND = 2044;
/*     */   private static final int MASKGROUP_OUTBOUND = 522240;
/*  86 */   private static final FastThreadLocal<WeakHashMap<Class<?>, Integer>> skipFlagsCache = new FastThreadLocal()
/*     */   {
/*     */ 
/*     */     protected WeakHashMap<Class<?>, Integer> initialValue() throws Exception {
/*  90 */       return new WeakHashMap(); }
/*     */   };
/*     */   private static final AtomicReferenceFieldUpdater<AbstractChannelHandlerContext, PausableChannelEventExecutor> WRAPPED_EVENTEXECUTOR_UPDATER;
/*     */   volatile AbstractChannelHandlerContext next;
/*     */   volatile AbstractChannelHandlerContext prev;
/*     */   private final AbstractChannel channel;
/*     */   
/*     */   static {
/*  98 */     AtomicReferenceFieldUpdater<AbstractChannelHandlerContext, PausableChannelEventExecutor> updater = PlatformDependent.newAtomicReferenceFieldUpdater(AbstractChannelHandlerContext.class, "wrappedEventLoop");
/*     */     
/*     */ 
/* 101 */     if (updater == null) {
/* 102 */       updater = AtomicReferenceFieldUpdater.newUpdater(AbstractChannelHandlerContext.class, PausableChannelEventExecutor.class, "wrappedEventLoop");
/*     */     }
/*     */     
/* 105 */     WRAPPED_EVENTEXECUTOR_UPDATER = updater;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static int skipFlags(ChannelHandler handler)
/*     */   {
/* 114 */     WeakHashMap<Class<?>, Integer> cache = (WeakHashMap)skipFlagsCache.get();
/* 115 */     Class<? extends ChannelHandler> handlerType = handler.getClass();
/*     */     
/* 117 */     Integer flags = (Integer)cache.get(handlerType);
/* 118 */     int flagsVal; int flagsVal; if (flags != null) {
/* 119 */       flagsVal = flags.intValue();
/*     */     } else {
/* 121 */       flagsVal = skipFlags0(handlerType);
/* 122 */       cache.put(handlerType, Integer.valueOf(flagsVal));
/*     */     }
/*     */     
/* 125 */     return flagsVal;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static int skipFlags0(Class<? extends ChannelHandler> handlerType)
/*     */   {
/* 132 */     int flags = 0;
/*     */     try {
/* 134 */       if (isSkippable(handlerType, "handlerAdded", new Class[0])) {
/* 135 */         flags |= 0x1;
/*     */       }
/* 137 */       if (isSkippable(handlerType, "handlerRemoved", new Class[0])) {
/* 138 */         flags |= 0x2;
/*     */       }
/* 140 */       if (isSkippable(handlerType, "exceptionCaught", new Class[] { Throwable.class })) {
/* 141 */         flags |= 0x4;
/*     */       }
/* 143 */       if (isSkippable(handlerType, "channelRegistered", new Class[0])) {
/* 144 */         flags |= 0x8;
/*     */       }
/* 146 */       if (isSkippable(handlerType, "channelUnregistered", new Class[0])) {
/* 147 */         flags |= 0x10;
/*     */       }
/* 149 */       if (isSkippable(handlerType, "channelActive", new Class[0])) {
/* 150 */         flags |= 0x20;
/*     */       }
/* 152 */       if (isSkippable(handlerType, "channelInactive", new Class[0])) {
/* 153 */         flags |= 0x40;
/*     */       }
/* 155 */       if (isSkippable(handlerType, "channelRead", new Class[] { Object.class })) {
/* 156 */         flags |= 0x80;
/*     */       }
/* 158 */       if (isSkippable(handlerType, "channelReadComplete", new Class[0])) {
/* 159 */         flags |= 0x100;
/*     */       }
/* 161 */       if (isSkippable(handlerType, "channelWritabilityChanged", new Class[0])) {
/* 162 */         flags |= 0x200;
/*     */       }
/* 164 */       if (isSkippable(handlerType, "userEventTriggered", new Class[] { Object.class })) {
/* 165 */         flags |= 0x400;
/*     */       }
/* 167 */       if (isSkippable(handlerType, "bind", new Class[] { SocketAddress.class, ChannelPromise.class })) {
/* 168 */         flags |= 0x800;
/*     */       }
/* 170 */       if (isSkippable(handlerType, "connect", new Class[] { SocketAddress.class, SocketAddress.class, ChannelPromise.class })) {
/* 171 */         flags |= 0x1000;
/*     */       }
/* 173 */       if (isSkippable(handlerType, "disconnect", new Class[] { ChannelPromise.class })) {
/* 174 */         flags |= 0x2000;
/*     */       }
/* 176 */       if (isSkippable(handlerType, "close", new Class[] { ChannelPromise.class })) {
/* 177 */         flags |= 0x4000;
/*     */       }
/* 179 */       if (isSkippable(handlerType, "deregister", new Class[] { ChannelPromise.class })) {
/* 180 */         flags |= 0x8000;
/*     */       }
/* 182 */       if (isSkippable(handlerType, "read", new Class[0])) {
/* 183 */         flags |= 0x10000;
/*     */       }
/* 185 */       if (isSkippable(handlerType, "write", new Class[] { Object.class, ChannelPromise.class })) {
/* 186 */         flags |= 0x20000;
/*     */       }
/* 188 */       if (isSkippable(handlerType, "flush", new Class[0])) {
/* 189 */         flags |= 0x40000;
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 193 */       PlatformDependent.throwException(e);
/*     */     }
/*     */     
/* 196 */     return flags;
/*     */   }
/*     */   
/*     */ 
/*     */   private static boolean isSkippable(Class<?> handlerType, String methodName, Class<?>... paramTypes)
/*     */     throws Exception
/*     */   {
/* 203 */     Class[] newParamTypes = new Class[paramTypes.length + 1];
/* 204 */     newParamTypes[0] = ChannelHandlerContext.class;
/* 205 */     System.arraycopy(paramTypes, 0, newParamTypes, 1, paramTypes.length);
/*     */     
/* 207 */     return handlerType.getMethod(methodName, newParamTypes).isAnnotationPresent(ChannelHandler.Skip.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final DefaultChannelPipeline pipeline;
/*     */   
/*     */ 
/*     */ 
/*     */   private final String name;
/*     */   
/*     */ 
/*     */ 
/*     */   boolean invokedThisChannelRead;
/*     */   
/*     */ 
/*     */ 
/*     */   private volatile boolean invokedNextChannelRead;
/*     */   
/*     */ 
/*     */ 
/*     */   private volatile boolean invokedPrevRead;
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean removed;
/*     */   
/*     */ 
/*     */ 
/*     */   final int skipFlags;
/*     */   
/*     */ 
/*     */ 
/*     */   final ChannelHandlerInvoker invoker;
/*     */   
/*     */ 
/*     */ 
/*     */   private ChannelFuture succeededFuture;
/*     */   
/*     */ 
/*     */ 
/*     */   volatile Runnable invokeChannelReadCompleteTask;
/*     */   
/*     */ 
/*     */ 
/*     */   volatile Runnable invokeReadTask;
/*     */   
/*     */ 
/*     */ 
/*     */   volatile Runnable invokeFlushTask;
/*     */   
/*     */ 
/*     */ 
/*     */   volatile Runnable invokeChannelWritableStateChangedTask;
/*     */   
/*     */ 
/*     */ 
/*     */   private volatile PausableChannelEventExecutor wrappedEventLoop;
/*     */   
/*     */ 
/*     */ 
/*     */   AbstractChannelHandlerContext(DefaultChannelPipeline pipeline, ChannelHandlerInvoker invoker, String name, int skipFlags)
/*     */   {
/* 271 */     if (name == null) {
/* 272 */       throw new NullPointerException("name");
/*     */     }
/*     */     
/* 275 */     this.channel = pipeline.channel;
/* 276 */     this.pipeline = pipeline;
/* 277 */     this.name = name;
/* 278 */     this.invoker = invoker;
/* 279 */     this.skipFlags = skipFlags;
/*     */   }
/*     */   
/*     */   public final Channel channel()
/*     */   {
/* 284 */     return this.channel;
/*     */   }
/*     */   
/*     */   public ChannelPipeline pipeline()
/*     */   {
/* 289 */     return this.pipeline;
/*     */   }
/*     */   
/*     */   public ByteBufAllocator alloc()
/*     */   {
/* 294 */     return channel().config().getAllocator();
/*     */   }
/*     */   
/*     */   public final EventExecutor executor()
/*     */   {
/* 299 */     if (this.invoker == null) {
/* 300 */       return channel().eventLoop();
/*     */     }
/* 302 */     return wrappedEventLoop();
/*     */   }
/*     */   
/*     */ 
/*     */   public final ChannelHandlerInvoker invoker()
/*     */   {
/* 308 */     if (this.invoker == null) {
/* 309 */       return channel().eventLoop().asInvoker();
/*     */     }
/* 311 */     return wrappedEventLoop();
/*     */   }
/*     */   
/*     */   private PausableChannelEventExecutor wrappedEventLoop()
/*     */   {
/* 316 */     PausableChannelEventExecutor wrapped = this.wrappedEventLoop;
/* 317 */     if (wrapped == null) {
/* 318 */       wrapped = new PausableChannelEventExecutor0(null);
/* 319 */       if (!WRAPPED_EVENTEXECUTOR_UPDATER.compareAndSet(this, null, wrapped))
/*     */       {
/* 321 */         return this.wrappedEventLoop;
/*     */       }
/*     */     }
/* 324 */     return wrapped;
/*     */   }
/*     */   
/*     */   public String name()
/*     */   {
/* 329 */     return this.name;
/*     */   }
/*     */   
/*     */   public <T> Attribute<T> attr(AttributeKey<T> key)
/*     */   {
/* 334 */     return this.channel.attr(key);
/*     */   }
/*     */   
/*     */   public <T> boolean hasAttr(AttributeKey<T> key)
/*     */   {
/* 339 */     return this.channel.hasAttr(key);
/*     */   }
/*     */   
/*     */   public ChannelHandlerContext fireChannelRegistered()
/*     */   {
/* 344 */     AbstractChannelHandlerContext next = findContextInbound();
/* 345 */     next.invoker().invokeChannelRegistered(next);
/* 346 */     return this;
/*     */   }
/*     */   
/*     */   public ChannelHandlerContext fireChannelUnregistered()
/*     */   {
/* 351 */     AbstractChannelHandlerContext next = findContextInbound();
/* 352 */     next.invoker().invokeChannelUnregistered(next);
/* 353 */     return this;
/*     */   }
/*     */   
/*     */   public ChannelHandlerContext fireChannelActive()
/*     */   {
/* 358 */     AbstractChannelHandlerContext next = findContextInbound();
/* 359 */     next.invoker().invokeChannelActive(next);
/* 360 */     return this;
/*     */   }
/*     */   
/*     */   public ChannelHandlerContext fireChannelInactive()
/*     */   {
/* 365 */     AbstractChannelHandlerContext next = findContextInbound();
/* 366 */     next.invoker().invokeChannelInactive(next);
/* 367 */     return this;
/*     */   }
/*     */   
/*     */   public ChannelHandlerContext fireExceptionCaught(Throwable cause)
/*     */   {
/* 372 */     AbstractChannelHandlerContext next = findContextInbound();
/* 373 */     next.invoker().invokeExceptionCaught(next, cause);
/* 374 */     return this;
/*     */   }
/*     */   
/*     */   public ChannelHandlerContext fireUserEventTriggered(Object event)
/*     */   {
/* 379 */     AbstractChannelHandlerContext next = findContextInbound();
/* 380 */     next.invoker().invokeUserEventTriggered(next, event);
/* 381 */     return this;
/*     */   }
/*     */   
/*     */   public ChannelHandlerContext fireChannelRead(Object msg)
/*     */   {
/* 386 */     AbstractChannelHandlerContext next = findContextInbound();
/* 387 */     ReferenceCountUtil.touch(msg, next);
/* 388 */     this.invokedNextChannelRead = true;
/* 389 */     next.invoker().invokeChannelRead(next, msg);
/* 390 */     return this;
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
/*     */   public ChannelHandlerContext fireChannelReadComplete()
/*     */   {
/* 405 */     if ((this.invokedNextChannelRead) || (!this.invokedThisChannelRead))
/*     */     {
/*     */ 
/* 408 */       this.invokedNextChannelRead = false;
/* 409 */       this.invokedPrevRead = false;
/*     */       
/* 411 */       AbstractChannelHandlerContext next = findContextInbound();
/* 412 */       next.invoker().invokeChannelReadComplete(next);
/* 413 */       return this;
/*     */     }
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
/* 427 */     if ((this.invokedPrevRead) && (!channel().config().isAutoRead()))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 433 */       read();
/*     */     } else {
/* 435 */       this.invokedPrevRead = false;
/*     */     }
/*     */     
/* 438 */     return this;
/*     */   }
/*     */   
/*     */   public ChannelHandlerContext fireChannelWritabilityChanged()
/*     */   {
/* 443 */     AbstractChannelHandlerContext next = findContextInbound();
/* 444 */     next.invoker().invokeChannelWritabilityChanged(next);
/* 445 */     return this;
/*     */   }
/*     */   
/*     */   public ChannelFuture bind(SocketAddress localAddress)
/*     */   {
/* 450 */     return bind(localAddress, newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture connect(SocketAddress remoteAddress)
/*     */   {
/* 455 */     return connect(remoteAddress, newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress)
/*     */   {
/* 460 */     return connect(remoteAddress, localAddress, newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture disconnect()
/*     */   {
/* 465 */     return disconnect(newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture close()
/*     */   {
/* 470 */     return close(newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture deregister()
/*     */   {
/* 475 */     return deregister(newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise)
/*     */   {
/* 480 */     AbstractChannelHandlerContext next = findContextOutbound();
/* 481 */     next.invoker().invokeBind(next, localAddress, promise);
/* 482 */     return promise;
/*     */   }
/*     */   
/*     */   public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise)
/*     */   {
/* 487 */     return connect(remoteAddress, null, promise);
/*     */   }
/*     */   
/*     */   public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
/*     */   {
/* 492 */     AbstractChannelHandlerContext next = findContextOutbound();
/* 493 */     next.invoker().invokeConnect(next, remoteAddress, localAddress, promise);
/* 494 */     return promise;
/*     */   }
/*     */   
/*     */   public ChannelFuture disconnect(ChannelPromise promise)
/*     */   {
/* 499 */     if (!channel().metadata().hasDisconnect()) {
/* 500 */       return close(promise);
/*     */     }
/*     */     
/* 503 */     AbstractChannelHandlerContext next = findContextOutbound();
/* 504 */     next.invoker().invokeDisconnect(next, promise);
/* 505 */     return promise;
/*     */   }
/*     */   
/*     */   public ChannelFuture close(ChannelPromise promise)
/*     */   {
/* 510 */     AbstractChannelHandlerContext next = findContextOutbound();
/* 511 */     next.invoker().invokeClose(next, promise);
/* 512 */     return promise;
/*     */   }
/*     */   
/*     */   public ChannelFuture deregister(ChannelPromise promise)
/*     */   {
/* 517 */     AbstractChannelHandlerContext next = findContextOutbound();
/* 518 */     next.invoker().invokeDeregister(next, promise);
/* 519 */     return promise;
/*     */   }
/*     */   
/*     */   public ChannelHandlerContext read()
/*     */   {
/* 524 */     AbstractChannelHandlerContext next = findContextOutbound();
/* 525 */     this.invokedPrevRead = true;
/* 526 */     next.invoker().invokeRead(next);
/* 527 */     return this;
/*     */   }
/*     */   
/*     */   public ChannelFuture write(Object msg)
/*     */   {
/* 532 */     return write(msg, newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture write(Object msg, ChannelPromise promise)
/*     */   {
/* 537 */     AbstractChannelHandlerContext next = findContextOutbound();
/* 538 */     ReferenceCountUtil.touch(msg, next);
/* 539 */     next.invoker().invokeWrite(next, msg, promise);
/* 540 */     return promise;
/*     */   }
/*     */   
/*     */   public ChannelHandlerContext flush()
/*     */   {
/* 545 */     AbstractChannelHandlerContext next = findContextOutbound();
/* 546 */     next.invoker().invokeFlush(next);
/* 547 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise)
/*     */   {
/* 553 */     AbstractChannelHandlerContext next = findContextOutbound();
/* 554 */     ReferenceCountUtil.touch(msg, next);
/* 555 */     next.invoker().invokeWrite(next, msg, promise);
/* 556 */     next = findContextOutbound();
/* 557 */     next.invoker().invokeFlush(next);
/* 558 */     return promise;
/*     */   }
/*     */   
/*     */   public ChannelFuture writeAndFlush(Object msg)
/*     */   {
/* 563 */     return writeAndFlush(msg, newPromise());
/*     */   }
/*     */   
/*     */   public ChannelPromise newPromise()
/*     */   {
/* 568 */     return new DefaultChannelPromise(channel(), executor());
/*     */   }
/*     */   
/*     */   public ChannelProgressivePromise newProgressivePromise()
/*     */   {
/* 573 */     return new DefaultChannelProgressivePromise(channel(), executor());
/*     */   }
/*     */   
/*     */   public ChannelFuture newSucceededFuture()
/*     */   {
/* 578 */     ChannelFuture succeededFuture = this.succeededFuture;
/* 579 */     if (succeededFuture == null) {
/* 580 */       this.succeededFuture = (succeededFuture = new SucceededChannelFuture(channel(), executor()));
/*     */     }
/* 582 */     return succeededFuture;
/*     */   }
/*     */   
/*     */   public ChannelFuture newFailedFuture(Throwable cause)
/*     */   {
/* 587 */     return new FailedChannelFuture(channel(), executor(), cause);
/*     */   }
/*     */   
/*     */   private AbstractChannelHandlerContext findContextInbound() {
/* 591 */     AbstractChannelHandlerContext ctx = this;
/*     */     do {
/* 593 */       ctx = ctx.next;
/* 594 */     } while ((ctx.skipFlags & 0x7FC) == 2044);
/* 595 */     return ctx;
/*     */   }
/*     */   
/*     */   private AbstractChannelHandlerContext findContextOutbound() {
/* 599 */     AbstractChannelHandlerContext ctx = this;
/*     */     do {
/* 601 */       ctx = ctx.prev;
/* 602 */     } while ((ctx.skipFlags & 0x7F800) == 522240);
/* 603 */     return ctx;
/*     */   }
/*     */   
/*     */   public ChannelPromise voidPromise()
/*     */   {
/* 608 */     return this.channel.voidPromise();
/*     */   }
/*     */   
/*     */   void setRemoved() {
/* 612 */     this.removed = true;
/*     */   }
/*     */   
/*     */   public boolean isRemoved()
/*     */   {
/* 617 */     return this.removed;
/*     */   }
/*     */   
/*     */   public String toHintString()
/*     */   {
/* 622 */     return '\'' + this.name + "' will handle the message from this point.";
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 627 */     return StringUtil.simpleClassName(ChannelHandlerContext.class) + '(' + this.name + ", " + this.channel + ')';
/*     */   }
/*     */   
/*     */ 
/*     */   private final class PausableChannelEventExecutor0
/*     */     extends PausableChannelEventExecutor
/*     */   {
/*     */     private PausableChannelEventExecutor0() {}
/*     */     
/*     */     public void rejectNewTasks()
/*     */     {
/* 638 */       ((PausableEventExecutor)channel().eventLoop()).rejectNewTasks();
/*     */     }
/*     */     
/*     */     public void acceptNewTasks()
/*     */     {
/* 643 */       ((PausableEventExecutor)channel().eventLoop()).acceptNewTasks();
/*     */     }
/*     */     
/*     */     public boolean isAcceptingNewTasks()
/*     */     {
/* 648 */       return ((PausableEventExecutor)channel().eventLoop()).isAcceptingNewTasks();
/*     */     }
/*     */     
/*     */     public Channel channel()
/*     */     {
/* 653 */       return AbstractChannelHandlerContext.this.channel();
/*     */     }
/*     */     
/*     */     public EventExecutor unwrap()
/*     */     {
/* 658 */       return unwrapInvoker().executor();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ChannelHandlerInvoker unwrapInvoker()
/*     */     {
/* 667 */       return AbstractChannelHandlerContext.this.invoker;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\AbstractChannelHandlerContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */