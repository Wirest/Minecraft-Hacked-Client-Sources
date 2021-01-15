/*      */ package io.netty.channel;
/*      */ 
/*      */ import io.netty.util.ReferenceCountUtil;
/*      */ import io.netty.util.concurrent.EventExecutor;
/*      */ import io.netty.util.concurrent.EventExecutorGroup;
/*      */ import io.netty.util.concurrent.PausableEventExecutor;
/*      */ import io.netty.util.internal.OneTimeTask;
/*      */ import io.netty.util.internal.PlatformDependent;
/*      */ import io.netty.util.internal.StringUtil;
/*      */ import io.netty.util.internal.logging.InternalLogger;
/*      */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*      */ import java.net.SocketAddress;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.WeakHashMap;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Future;
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
/*      */ final class DefaultChannelPipeline
/*      */   implements ChannelPipeline
/*      */ {
/*      */   static final InternalLogger logger;
/*      */   private static final WeakHashMap<Class<?>, String>[] nameCaches;
/*      */   final AbstractChannel channel;
/*      */   final AbstractChannelHandlerContext head;
/*      */   final AbstractChannelHandlerContext tail;
/*      */   
/*      */   static
/*      */   {
/*   48 */     logger = InternalLoggerFactory.getInstance(DefaultChannelPipeline.class);
/*      */     
/*      */ 
/*   51 */     nameCaches = new WeakHashMap[Runtime.getRuntime().availableProcessors()];
/*      */     
/*      */ 
/*      */ 
/*   55 */     for (int i = 0; i < nameCaches.length; i++) {
/*   56 */       nameCaches[i] = new WeakHashMap();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   65 */   private final Map<String, AbstractChannelHandlerContext> name2ctx = new HashMap(4);
/*      */   
/*      */ 
/*      */   private Map<EventExecutorGroup, ChannelHandlerInvoker> childInvokers;
/*      */   
/*      */ 
/*      */ 
/*      */   DefaultChannelPipeline(AbstractChannel channel)
/*      */   {
/*   74 */     if (channel == null) {
/*   75 */       throw new NullPointerException("channel");
/*      */     }
/*   77 */     this.channel = channel;
/*      */     
/*   79 */     this.tail = new TailContext(this);
/*   80 */     this.head = new HeadContext(this);
/*      */     
/*   82 */     this.head.next = this.tail;
/*   83 */     this.tail.prev = this.head;
/*      */   }
/*      */   
/*      */   public Channel channel()
/*      */   {
/*   88 */     return this.channel;
/*      */   }
/*      */   
/*      */   public ChannelPipeline addFirst(String name, ChannelHandler handler)
/*      */   {
/*   93 */     return addFirst((ChannelHandlerInvoker)null, name, handler);
/*      */   }
/*      */   
/*      */   public ChannelPipeline addFirst(EventExecutorGroup group, String name, ChannelHandler handler)
/*      */   {
/*   98 */     synchronized (this) {
/*   99 */       name = filterName(name, handler);
/*  100 */       addFirst0(name, new DefaultChannelHandlerContext(this, findInvoker(group), name, handler));
/*      */     }
/*  102 */     return this;
/*      */   }
/*      */   
/*      */   public ChannelPipeline addFirst(ChannelHandlerInvoker invoker, String name, ChannelHandler handler)
/*      */   {
/*  107 */     synchronized (this) {
/*  108 */       name = filterName(name, handler);
/*  109 */       addFirst0(name, new DefaultChannelHandlerContext(this, invoker, name, handler));
/*      */     }
/*  111 */     return this;
/*      */   }
/*      */   
/*      */   private void addFirst0(String name, AbstractChannelHandlerContext newCtx) {
/*  115 */     checkMultiplicity(newCtx);
/*      */     
/*  117 */     AbstractChannelHandlerContext nextCtx = this.head.next;
/*  118 */     newCtx.prev = this.head;
/*  119 */     newCtx.next = nextCtx;
/*  120 */     this.head.next = newCtx;
/*  121 */     nextCtx.prev = newCtx;
/*      */     
/*  123 */     this.name2ctx.put(name, newCtx);
/*      */     
/*  125 */     callHandlerAdded(newCtx);
/*      */   }
/*      */   
/*      */   public ChannelPipeline addLast(String name, ChannelHandler handler)
/*      */   {
/*  130 */     return addLast((ChannelHandlerInvoker)null, name, handler);
/*      */   }
/*      */   
/*      */   public ChannelPipeline addLast(EventExecutorGroup group, String name, ChannelHandler handler)
/*      */   {
/*  135 */     synchronized (this) {
/*  136 */       name = filterName(name, handler);
/*  137 */       addLast0(name, new DefaultChannelHandlerContext(this, findInvoker(group), name, handler));
/*      */     }
/*  139 */     return this;
/*      */   }
/*      */   
/*      */   public ChannelPipeline addLast(ChannelHandlerInvoker invoker, String name, ChannelHandler handler)
/*      */   {
/*  144 */     synchronized (this) {
/*  145 */       name = filterName(name, handler);
/*  146 */       addLast0(name, new DefaultChannelHandlerContext(this, invoker, name, handler));
/*      */     }
/*  148 */     return this;
/*      */   }
/*      */   
/*      */   private void addLast0(String name, AbstractChannelHandlerContext newCtx) {
/*  152 */     checkMultiplicity(newCtx);
/*      */     
/*  154 */     AbstractChannelHandlerContext prev = this.tail.prev;
/*  155 */     newCtx.prev = prev;
/*  156 */     newCtx.next = this.tail;
/*  157 */     prev.next = newCtx;
/*  158 */     this.tail.prev = newCtx;
/*      */     
/*  160 */     this.name2ctx.put(name, newCtx);
/*      */     
/*  162 */     callHandlerAdded(newCtx);
/*      */   }
/*      */   
/*      */   public ChannelPipeline addBefore(String baseName, String name, ChannelHandler handler)
/*      */   {
/*  167 */     return addBefore((ChannelHandlerInvoker)null, baseName, name, handler);
/*      */   }
/*      */   
/*      */   public ChannelPipeline addBefore(EventExecutorGroup group, String baseName, String name, ChannelHandler handler)
/*      */   {
/*  172 */     synchronized (this) {
/*  173 */       AbstractChannelHandlerContext ctx = getContextOrDie(baseName);
/*  174 */       name = filterName(name, handler);
/*  175 */       addBefore0(name, ctx, new DefaultChannelHandlerContext(this, findInvoker(group), name, handler));
/*      */     }
/*  177 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */   public ChannelPipeline addBefore(ChannelHandlerInvoker invoker, String baseName, String name, ChannelHandler handler)
/*      */   {
/*  183 */     synchronized (this) {
/*  184 */       AbstractChannelHandlerContext ctx = getContextOrDie(baseName);
/*  185 */       name = filterName(name, handler);
/*  186 */       addBefore0(name, ctx, new DefaultChannelHandlerContext(this, invoker, name, handler));
/*      */     }
/*  188 */     return this;
/*      */   }
/*      */   
/*      */   private void addBefore0(String name, AbstractChannelHandlerContext ctx, AbstractChannelHandlerContext newCtx)
/*      */   {
/*  193 */     checkMultiplicity(newCtx);
/*      */     
/*  195 */     newCtx.prev = ctx.prev;
/*  196 */     newCtx.next = ctx;
/*  197 */     ctx.prev.next = newCtx;
/*  198 */     ctx.prev = newCtx;
/*      */     
/*  200 */     this.name2ctx.put(name, newCtx);
/*      */     
/*  202 */     callHandlerAdded(newCtx);
/*      */   }
/*      */   
/*      */   public ChannelPipeline addAfter(String baseName, String name, ChannelHandler handler)
/*      */   {
/*  207 */     return addAfter((ChannelHandlerInvoker)null, baseName, name, handler);
/*      */   }
/*      */   
/*      */   public ChannelPipeline addAfter(EventExecutorGroup group, String baseName, String name, ChannelHandler handler)
/*      */   {
/*  212 */     synchronized (this) {
/*  213 */       AbstractChannelHandlerContext ctx = getContextOrDie(baseName);
/*  214 */       name = filterName(name, handler);
/*  215 */       addAfter0(name, ctx, new DefaultChannelHandlerContext(this, findInvoker(group), name, handler));
/*      */     }
/*  217 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ChannelPipeline addAfter(ChannelHandlerInvoker invoker, String baseName, String name, ChannelHandler handler)
/*      */   {
/*  224 */     synchronized (this) {
/*  225 */       AbstractChannelHandlerContext ctx = getContextOrDie(baseName);
/*  226 */       name = filterName(name, handler);
/*  227 */       addAfter0(name, ctx, new DefaultChannelHandlerContext(this, invoker, name, handler));
/*      */     }
/*  229 */     return this;
/*      */   }
/*      */   
/*      */   private void addAfter0(String name, AbstractChannelHandlerContext ctx, AbstractChannelHandlerContext newCtx) {
/*  233 */     checkMultiplicity(newCtx);
/*      */     
/*  235 */     newCtx.prev = ctx;
/*  236 */     newCtx.next = ctx.next;
/*  237 */     ctx.next.prev = newCtx;
/*  238 */     ctx.next = newCtx;
/*      */     
/*  240 */     this.name2ctx.put(name, newCtx);
/*      */     
/*  242 */     callHandlerAdded(newCtx);
/*      */   }
/*      */   
/*      */   public ChannelPipeline addFirst(ChannelHandler... handlers)
/*      */   {
/*  247 */     return addFirst((ChannelHandlerInvoker)null, handlers);
/*      */   }
/*      */   
/*      */   public ChannelPipeline addFirst(EventExecutorGroup group, ChannelHandler... handlers)
/*      */   {
/*  252 */     if (handlers == null) {
/*  253 */       throw new NullPointerException("handlers");
/*      */     }
/*  255 */     if ((handlers.length == 0) || (handlers[0] == null)) {
/*  256 */       return this;
/*      */     }
/*      */     
/*      */ 
/*  260 */     for (int size = 1; size < handlers.length; size++) {
/*  261 */       if (handlers[size] == null) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/*  266 */     for (int i = size - 1; i >= 0; i--) {
/*  267 */       ChannelHandler h = handlers[i];
/*  268 */       addFirst(group, generateName(h), h);
/*      */     }
/*      */     
/*  271 */     return this;
/*      */   }
/*      */   
/*      */   public ChannelPipeline addFirst(ChannelHandlerInvoker invoker, ChannelHandler... handlers)
/*      */   {
/*  276 */     if (handlers == null) {
/*  277 */       throw new NullPointerException("handlers");
/*      */     }
/*  279 */     if ((handlers.length == 0) || (handlers[0] == null)) {
/*  280 */       return this;
/*      */     }
/*      */     
/*      */ 
/*  284 */     for (int size = 1; size < handlers.length; size++) {
/*  285 */       if (handlers[size] == null) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/*  290 */     for (int i = size - 1; i >= 0; i--) {
/*  291 */       ChannelHandler h = handlers[i];
/*  292 */       addFirst(invoker, null, h);
/*      */     }
/*      */     
/*  295 */     return this;
/*      */   }
/*      */   
/*      */   public ChannelPipeline addLast(ChannelHandler... handlers)
/*      */   {
/*  300 */     return addLast((ChannelHandlerInvoker)null, handlers);
/*      */   }
/*      */   
/*      */   public ChannelPipeline addLast(EventExecutorGroup group, ChannelHandler... handlers)
/*      */   {
/*  305 */     if (handlers == null) {
/*  306 */       throw new NullPointerException("handlers");
/*      */     }
/*      */     
/*  309 */     for (ChannelHandler h : handlers) {
/*  310 */       if (h == null) {
/*      */         break;
/*      */       }
/*  313 */       addLast(group, generateName(h), h);
/*      */     }
/*      */     
/*  316 */     return this;
/*      */   }
/*      */   
/*      */   public ChannelPipeline addLast(ChannelHandlerInvoker invoker, ChannelHandler... handlers)
/*      */   {
/*  321 */     if (handlers == null) {
/*  322 */       throw new NullPointerException("handlers");
/*      */     }
/*      */     
/*  325 */     for (ChannelHandler h : handlers) {
/*  326 */       if (h == null) {
/*      */         break;
/*      */       }
/*  329 */       addLast(invoker, null, h);
/*      */     }
/*      */     
/*  332 */     return this;
/*      */   }
/*      */   
/*      */   private ChannelHandlerInvoker findInvoker(EventExecutorGroup group)
/*      */   {
/*  337 */     if (group == null) {
/*  338 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  342 */     Map<EventExecutorGroup, ChannelHandlerInvoker> childInvokers = this.childInvokers;
/*  343 */     if (childInvokers == null) {
/*  344 */       childInvokers = this.childInvokers = new IdentityHashMap(4);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  349 */     ChannelHandlerInvoker invoker = (ChannelHandlerInvoker)childInvokers.get(group);
/*  350 */     if (invoker == null) {
/*  351 */       EventExecutor executor = group.next();
/*  352 */       if ((executor instanceof EventLoop)) {
/*  353 */         invoker = ((EventLoop)executor).asInvoker();
/*      */       } else {
/*  355 */         invoker = new DefaultChannelHandlerInvoker(executor);
/*      */       }
/*  357 */       childInvokers.put(group, invoker);
/*      */     }
/*      */     
/*  360 */     return invoker;
/*      */   }
/*      */   
/*      */   String generateName(ChannelHandler handler) {
/*  364 */     WeakHashMap<Class<?>, String> cache = nameCaches[((int)(Thread.currentThread().getId() % nameCaches.length))];
/*  365 */     Class<?> handlerType = handler.getClass();
/*      */     String name;
/*  367 */     synchronized (cache) {
/*  368 */       name = (String)cache.get(handlerType);
/*  369 */       if (name == null) {
/*  370 */         name = generateName0(handlerType);
/*  371 */         cache.put(handlerType, name);
/*      */       }
/*      */     }
/*      */     
/*  375 */     synchronized (this)
/*      */     {
/*      */ 
/*  378 */       if (this.name2ctx.containsKey(name)) {
/*  379 */         String baseName = name.substring(0, name.length() - 1);
/*  380 */         for (int i = 1;; i++) {
/*  381 */           String newName = baseName + i;
/*  382 */           if (!this.name2ctx.containsKey(newName)) {
/*  383 */             name = newName;
/*  384 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  390 */     return name;
/*      */   }
/*      */   
/*      */   private static String generateName0(Class<?> handlerType) {
/*  394 */     return StringUtil.simpleClassName(handlerType) + "#0";
/*      */   }
/*      */   
/*      */   public ChannelPipeline remove(ChannelHandler handler)
/*      */   {
/*  399 */     remove(getContextOrDie(handler));
/*  400 */     return this;
/*      */   }
/*      */   
/*      */   public ChannelHandler remove(String name)
/*      */   {
/*  405 */     return remove(getContextOrDie(name)).handler();
/*      */   }
/*      */   
/*      */ 
/*      */   public <T extends ChannelHandler> T remove(Class<T> handlerType)
/*      */   {
/*  411 */     return remove(getContextOrDie(handlerType)).handler();
/*      */   }
/*      */   
/*      */   private AbstractChannelHandlerContext remove(final AbstractChannelHandlerContext ctx) {
/*  415 */     assert ((ctx != this.head) && (ctx != this.tail));
/*      */     
/*      */     Future<?> future;
/*      */     
/*      */     AbstractChannelHandlerContext context;
/*  420 */     synchronized (this) {
/*  421 */       if ((!ctx.channel().isRegistered()) || (ctx.executor().inEventLoop())) {
/*  422 */         remove0(ctx);
/*  423 */         return ctx;
/*      */       }
/*  425 */       future = ctx.executor().submit(new Runnable()
/*      */       {
/*      */         public void run() {
/*  428 */           synchronized (DefaultChannelPipeline.this) {
/*  429 */             DefaultChannelPipeline.this.remove0(ctx);
/*      */           }
/*      */         }
/*  432 */       });
/*  433 */       context = ctx;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  440 */     waitForFuture(future);
/*      */     
/*  442 */     return context;
/*      */   }
/*      */   
/*      */   void remove0(AbstractChannelHandlerContext ctx) {
/*  446 */     AbstractChannelHandlerContext prev = ctx.prev;
/*  447 */     AbstractChannelHandlerContext next = ctx.next;
/*  448 */     prev.next = next;
/*  449 */     next.prev = prev;
/*  450 */     this.name2ctx.remove(ctx.name());
/*  451 */     callHandlerRemoved(ctx);
/*      */   }
/*      */   
/*      */   public ChannelHandler removeFirst()
/*      */   {
/*  456 */     if (this.head.next == this.tail) {
/*  457 */       throw new NoSuchElementException();
/*      */     }
/*  459 */     return remove(this.head.next).handler();
/*      */   }
/*      */   
/*      */   public ChannelHandler removeLast()
/*      */   {
/*  464 */     if (this.head.next == this.tail) {
/*  465 */       throw new NoSuchElementException();
/*      */     }
/*  467 */     return remove(this.tail.prev).handler();
/*      */   }
/*      */   
/*      */   public ChannelPipeline replace(ChannelHandler oldHandler, String newName, ChannelHandler newHandler)
/*      */   {
/*  472 */     replace(getContextOrDie(oldHandler), newName, newHandler);
/*  473 */     return this;
/*      */   }
/*      */   
/*      */   public ChannelHandler replace(String oldName, String newName, ChannelHandler newHandler)
/*      */   {
/*  478 */     return replace(getContextOrDie(oldName), newName, newHandler);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public <T extends ChannelHandler> T replace(Class<T> oldHandlerType, String newName, ChannelHandler newHandler)
/*      */   {
/*  485 */     return replace(getContextOrDie(oldHandlerType), newName, newHandler);
/*      */   }
/*      */   
/*      */ 
/*      */   private ChannelHandler replace(final AbstractChannelHandlerContext ctx, String newName, ChannelHandler newHandler)
/*      */   {
/*  491 */     assert ((ctx != this.head) && (ctx != this.tail));
/*      */     
/*      */     Future<?> future;
/*  494 */     synchronized (this) {
/*  495 */       if (newName == null) {
/*  496 */         newName = ctx.name();
/*  497 */       } else if (!ctx.name().equals(newName)) {
/*  498 */         newName = filterName(newName, newHandler);
/*      */       }
/*      */       
/*  501 */       final AbstractChannelHandlerContext newCtx = new DefaultChannelHandlerContext(this, ctx.invoker, newName, newHandler);
/*      */       
/*      */ 
/*  504 */       if ((!newCtx.channel().isRegistered()) || (newCtx.executor().inEventLoop())) {
/*  505 */         replace0(ctx, newName, newCtx);
/*  506 */         return ctx.handler();
/*      */       }
/*  508 */       final String finalNewName = newName;
/*  509 */       future = newCtx.executor().submit(new Runnable()
/*      */       {
/*      */         public void run() {
/*  512 */           synchronized (DefaultChannelPipeline.this) {
/*  513 */             DefaultChannelPipeline.this.replace0(ctx, finalNewName, newCtx);
/*      */           }
/*      */         }
/*      */       });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  523 */     waitForFuture(future);
/*      */     
/*  525 */     return ctx.handler();
/*      */   }
/*      */   
/*      */   private void replace0(AbstractChannelHandlerContext oldCtx, String newName, AbstractChannelHandlerContext newCtx)
/*      */   {
/*  530 */     checkMultiplicity(newCtx);
/*      */     
/*  532 */     AbstractChannelHandlerContext prev = oldCtx.prev;
/*  533 */     AbstractChannelHandlerContext next = oldCtx.next;
/*  534 */     newCtx.prev = prev;
/*  535 */     newCtx.next = next;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  541 */     prev.next = newCtx;
/*  542 */     next.prev = newCtx;
/*      */     
/*  544 */     if (!oldCtx.name().equals(newName)) {
/*  545 */       this.name2ctx.remove(oldCtx.name());
/*      */     }
/*  547 */     this.name2ctx.put(newName, newCtx);
/*      */     
/*      */ 
/*  550 */     oldCtx.prev = newCtx;
/*  551 */     oldCtx.next = newCtx;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  556 */     callHandlerAdded(newCtx);
/*  557 */     callHandlerRemoved(oldCtx);
/*      */   }
/*      */   
/*      */   private static void checkMultiplicity(ChannelHandlerContext ctx) {
/*  561 */     ChannelHandler handler = ctx.handler();
/*  562 */     if ((handler instanceof ChannelHandlerAdapter)) {
/*  563 */       ChannelHandlerAdapter h = (ChannelHandlerAdapter)handler;
/*  564 */       if ((!h.isSharable()) && (h.added)) {
/*  565 */         throw new ChannelPipelineException(h.getClass().getName() + " is not a @Sharable handler, so can't be added or removed multiple times.");
/*      */       }
/*      */       
/*      */ 
/*  569 */       h.added = true;
/*      */     }
/*      */   }
/*      */   
/*      */   private void callHandlerAdded(final AbstractChannelHandlerContext ctx) {
/*  574 */     if ((ctx.skipFlags & 0x1) != 0) {
/*  575 */       return;
/*      */     }
/*      */     
/*  578 */     if ((ctx.channel().isRegistered()) && (!ctx.executor().inEventLoop())) {
/*  579 */       ctx.executor().execute(new Runnable()
/*      */       {
/*      */         public void run() {
/*  582 */           DefaultChannelPipeline.this.callHandlerAdded0(ctx);
/*      */         }
/*  584 */       });
/*  585 */       return;
/*      */     }
/*  587 */     callHandlerAdded0(ctx);
/*      */   }
/*      */   
/*      */   private void callHandlerAdded0(AbstractChannelHandlerContext ctx) {
/*      */     try {
/*  592 */       ctx.invokedThisChannelRead = false;
/*  593 */       ctx.handler().handlerAdded(ctx);
/*      */     } catch (Throwable t) {
/*  595 */       boolean removed = false;
/*      */       try {
/*  597 */         remove(ctx);
/*  598 */         removed = true;
/*      */       } catch (Throwable t2) {
/*  600 */         if (logger.isWarnEnabled()) {
/*  601 */           logger.warn("Failed to remove a handler: " + ctx.name(), t2);
/*      */         }
/*      */       }
/*      */       
/*  605 */       if (removed) {
/*  606 */         fireExceptionCaught(new ChannelPipelineException(ctx.handler().getClass().getName() + ".handlerAdded() has thrown an exception; removed.", t));
/*      */       }
/*      */       else
/*      */       {
/*  610 */         fireExceptionCaught(new ChannelPipelineException(ctx.handler().getClass().getName() + ".handlerAdded() has thrown an exception; also failed to remove.", t));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void callHandlerRemoved(final AbstractChannelHandlerContext ctx)
/*      */   {
/*  618 */     if ((ctx.skipFlags & 0x2) != 0) {
/*  619 */       return;
/*      */     }
/*      */     
/*  622 */     if ((ctx.channel().isRegistered()) && (!ctx.executor().inEventLoop())) {
/*  623 */       ctx.executor().execute(new Runnable()
/*      */       {
/*      */         public void run() {
/*  626 */           DefaultChannelPipeline.this.callHandlerRemoved0(ctx);
/*      */         }
/*  628 */       });
/*  629 */       return;
/*      */     }
/*  631 */     callHandlerRemoved0(ctx);
/*      */   }
/*      */   
/*      */   private void callHandlerRemoved0(AbstractChannelHandlerContext ctx)
/*      */   {
/*      */     try {
/*  637 */       ctx.handler().handlerRemoved(ctx);
/*  638 */       ctx.setRemoved();
/*      */     } catch (Throwable t) {
/*  640 */       fireExceptionCaught(new ChannelPipelineException(ctx.handler().getClass().getName() + ".handlerRemoved() has thrown an exception.", t));
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
/*      */   private static void waitForFuture(Future<?> future)
/*      */   {
/*      */     try
/*      */     {
/*  662 */       future.get();
/*      */     }
/*      */     catch (ExecutionException ex) {
/*  665 */       PlatformDependent.throwException(ex.getCause());
/*      */     }
/*      */     catch (InterruptedException ex) {
/*  668 */       Thread.currentThread().interrupt();
/*      */     }
/*      */   }
/*      */   
/*      */   public ChannelHandler first()
/*      */   {
/*  674 */     ChannelHandlerContext first = firstContext();
/*  675 */     if (first == null) {
/*  676 */       return null;
/*      */     }
/*  678 */     return first.handler();
/*      */   }
/*      */   
/*      */   public ChannelHandlerContext firstContext()
/*      */   {
/*  683 */     AbstractChannelHandlerContext first = this.head.next;
/*  684 */     if (first == this.tail) {
/*  685 */       return null;
/*      */     }
/*  687 */     return this.head.next;
/*      */   }
/*      */   
/*      */   public ChannelHandler last()
/*      */   {
/*  692 */     AbstractChannelHandlerContext last = this.tail.prev;
/*  693 */     if (last == this.head) {
/*  694 */       return null;
/*      */     }
/*  696 */     return last.handler();
/*      */   }
/*      */   
/*      */   public ChannelHandlerContext lastContext()
/*      */   {
/*  701 */     AbstractChannelHandlerContext last = this.tail.prev;
/*  702 */     if (last == this.head) {
/*  703 */       return null;
/*      */     }
/*  705 */     return last;
/*      */   }
/*      */   
/*      */   public ChannelHandler get(String name)
/*      */   {
/*  710 */     ChannelHandlerContext ctx = context(name);
/*  711 */     if (ctx == null) {
/*  712 */       return null;
/*      */     }
/*  714 */     return ctx.handler();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public <T extends ChannelHandler> T get(Class<T> handlerType)
/*      */   {
/*  721 */     ChannelHandlerContext ctx = context(handlerType);
/*  722 */     if (ctx == null) {
/*  723 */       return null;
/*      */     }
/*  725 */     return ctx.handler();
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
/*      */   public ChannelHandlerContext context(ChannelHandler handler)
/*      */   {
/*  742 */     if (handler == null) {
/*  743 */       throw new NullPointerException("handler");
/*      */     }
/*      */     
/*  746 */     AbstractChannelHandlerContext ctx = this.head.next;
/*      */     for (;;)
/*      */     {
/*  749 */       if (ctx == null) {
/*  750 */         return null;
/*      */       }
/*      */       
/*  753 */       if (ctx.handler() == handler) {
/*  754 */         return ctx;
/*      */       }
/*      */       
/*  757 */       ctx = ctx.next;
/*      */     }
/*      */   }
/*      */   
/*      */   public ChannelHandlerContext context(Class<? extends ChannelHandler> handlerType)
/*      */   {
/*  763 */     if (handlerType == null) {
/*  764 */       throw new NullPointerException("handlerType");
/*      */     }
/*      */     
/*  767 */     AbstractChannelHandlerContext ctx = this.head.next;
/*      */     for (;;) {
/*  769 */       if (ctx == null) {
/*  770 */         return null;
/*      */       }
/*  772 */       if (handlerType.isAssignableFrom(ctx.handler().getClass())) {
/*  773 */         return ctx;
/*      */       }
/*  775 */       ctx = ctx.next;
/*      */     }
/*      */   }
/*      */   
/*      */   public List<String> names()
/*      */   {
/*  781 */     List<String> list = new ArrayList();
/*  782 */     AbstractChannelHandlerContext ctx = this.head.next;
/*      */     for (;;) {
/*  784 */       if (ctx == null) {
/*  785 */         return list;
/*      */       }
/*  787 */       list.add(ctx.name());
/*  788 */       ctx = ctx.next;
/*      */     }
/*      */   }
/*      */   
/*      */   public Map<String, ChannelHandler> toMap()
/*      */   {
/*  794 */     Map<String, ChannelHandler> map = new LinkedHashMap();
/*  795 */     AbstractChannelHandlerContext ctx = this.head.next;
/*      */     for (;;) {
/*  797 */       if (ctx == this.tail) {
/*  798 */         return map;
/*      */       }
/*  800 */       map.put(ctx.name(), ctx.handler());
/*  801 */       ctx = ctx.next;
/*      */     }
/*      */   }
/*      */   
/*      */   public Iterator<Map.Entry<String, ChannelHandler>> iterator()
/*      */   {
/*  807 */     return toMap().entrySet().iterator();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/*  815 */     StringBuilder buf = new StringBuilder().append(StringUtil.simpleClassName(this)).append('{');
/*      */     
/*      */ 
/*  818 */     AbstractChannelHandlerContext ctx = this.head.next;
/*      */     
/*  820 */     while (ctx != this.tail)
/*      */     {
/*      */ 
/*      */ 
/*  824 */       buf.append('(').append(ctx.name()).append(" = ").append(ctx.handler().getClass().getName()).append(')');
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  830 */       ctx = ctx.next;
/*  831 */       if (ctx == this.tail) {
/*      */         break;
/*      */       }
/*      */       
/*  835 */       buf.append(", ");
/*      */     }
/*  837 */     buf.append('}');
/*  838 */     return buf.toString();
/*      */   }
/*      */   
/*      */   public ChannelPipeline fireChannelRegistered()
/*      */   {
/*  843 */     this.head.fireChannelRegistered();
/*  844 */     return this;
/*      */   }
/*      */   
/*      */   public ChannelPipeline fireChannelUnregistered()
/*      */   {
/*  849 */     this.head.fireChannelUnregistered();
/*      */     
/*      */ 
/*  852 */     if (!this.channel.isOpen()) {
/*  853 */       destroy();
/*      */     }
/*  855 */     return this;
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
/*      */   private void destroy()
/*      */   {
/*  869 */     destroyUp(this.head.next);
/*      */   }
/*      */   
/*      */   private void destroyUp(AbstractChannelHandlerContext ctx) {
/*  873 */     Thread currentThread = Thread.currentThread();
/*  874 */     AbstractChannelHandlerContext tail = this.tail;
/*      */     for (;;) {
/*  876 */       if (ctx == tail) {
/*  877 */         destroyDown(currentThread, tail.prev);
/*  878 */         break;
/*      */       }
/*      */       
/*  881 */       EventExecutor executor = ctx.executor();
/*  882 */       if (!executor.inEventLoop(currentThread)) {
/*  883 */         final AbstractChannelHandlerContext finalCtx = ctx;
/*  884 */         executor.unwrap().execute(new OneTimeTask()
/*      */         {
/*      */           public void run() {
/*  887 */             DefaultChannelPipeline.this.destroyUp(finalCtx);
/*      */           }
/*  889 */         });
/*  890 */         break;
/*      */       }
/*      */       
/*  893 */       ctx = ctx.next;
/*      */     }
/*      */   }
/*      */   
/*      */   private void destroyDown(Thread currentThread, AbstractChannelHandlerContext ctx)
/*      */   {
/*  899 */     AbstractChannelHandlerContext head = this.head;
/*      */     
/*  901 */     while (ctx != head)
/*      */     {
/*      */ 
/*      */ 
/*  905 */       EventExecutor executor = ctx.executor();
/*  906 */       if (executor.inEventLoop(currentThread)) {
/*  907 */         synchronized (this) {
/*  908 */           remove0(ctx);
/*      */         }
/*      */       } else {
/*  911 */         final AbstractChannelHandlerContext finalCtx = ctx;
/*  912 */         executor.unwrap().execute(new OneTimeTask()
/*      */         {
/*      */           public void run() {
/*  915 */             DefaultChannelPipeline.this.destroyDown(Thread.currentThread(), finalCtx);
/*      */           }
/*  917 */         });
/*  918 */         break;
/*      */       }
/*      */       
/*  921 */       ctx = ctx.prev;
/*      */     }
/*      */   }
/*      */   
/*      */   public ChannelPipeline fireChannelActive()
/*      */   {
/*  927 */     this.head.fireChannelActive();
/*      */     
/*  929 */     if (this.channel.config().isAutoRead()) {
/*  930 */       this.channel.read();
/*      */     }
/*      */     
/*  933 */     return this;
/*      */   }
/*      */   
/*      */   public ChannelPipeline fireChannelInactive()
/*      */   {
/*  938 */     this.head.fireChannelInactive();
/*  939 */     return this;
/*      */   }
/*      */   
/*      */   public ChannelPipeline fireExceptionCaught(Throwable cause)
/*      */   {
/*  944 */     this.head.fireExceptionCaught(cause);
/*  945 */     return this;
/*      */   }
/*      */   
/*      */   public ChannelPipeline fireUserEventTriggered(Object event)
/*      */   {
/*  950 */     this.head.fireUserEventTriggered(event);
/*  951 */     return this;
/*      */   }
/*      */   
/*      */   public ChannelPipeline fireChannelRead(Object msg)
/*      */   {
/*  956 */     this.head.fireChannelRead(msg);
/*  957 */     return this;
/*      */   }
/*      */   
/*      */   public ChannelPipeline fireChannelReadComplete()
/*      */   {
/*  962 */     this.head.fireChannelReadComplete();
/*  963 */     if (this.channel.config().isAutoRead()) {
/*  964 */       read();
/*      */     }
/*  966 */     return this;
/*      */   }
/*      */   
/*      */   public ChannelPipeline fireChannelWritabilityChanged()
/*      */   {
/*  971 */     this.head.fireChannelWritabilityChanged();
/*  972 */     return this;
/*      */   }
/*      */   
/*      */   public ChannelFuture bind(SocketAddress localAddress)
/*      */   {
/*  977 */     return this.tail.bind(localAddress);
/*      */   }
/*      */   
/*      */   public ChannelFuture connect(SocketAddress remoteAddress)
/*      */   {
/*  982 */     return this.tail.connect(remoteAddress);
/*      */   }
/*      */   
/*      */   public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress)
/*      */   {
/*  987 */     return this.tail.connect(remoteAddress, localAddress);
/*      */   }
/*      */   
/*      */   public ChannelFuture disconnect()
/*      */   {
/*  992 */     return this.tail.disconnect();
/*      */   }
/*      */   
/*      */   public ChannelFuture close()
/*      */   {
/*  997 */     return this.tail.close();
/*      */   }
/*      */   
/*      */   public ChannelFuture deregister()
/*      */   {
/* 1002 */     return this.tail.deregister();
/*      */   }
/*      */   
/*      */   public ChannelPipeline flush()
/*      */   {
/* 1007 */     this.tail.flush();
/* 1008 */     return this;
/*      */   }
/*      */   
/*      */   public ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise)
/*      */   {
/* 1013 */     return this.tail.bind(localAddress, promise);
/*      */   }
/*      */   
/*      */   public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise)
/*      */   {
/* 1018 */     return this.tail.connect(remoteAddress, promise);
/*      */   }
/*      */   
/*      */   public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
/*      */   {
/* 1023 */     return this.tail.connect(remoteAddress, localAddress, promise);
/*      */   }
/*      */   
/*      */   public ChannelFuture disconnect(ChannelPromise promise)
/*      */   {
/* 1028 */     return this.tail.disconnect(promise);
/*      */   }
/*      */   
/*      */   public ChannelFuture close(ChannelPromise promise)
/*      */   {
/* 1033 */     return this.tail.close(promise);
/*      */   }
/*      */   
/*      */   public ChannelFuture deregister(ChannelPromise promise)
/*      */   {
/* 1038 */     return this.tail.deregister(promise);
/*      */   }
/*      */   
/*      */   public ChannelPipeline read()
/*      */   {
/* 1043 */     this.tail.read();
/* 1044 */     return this;
/*      */   }
/*      */   
/*      */   public ChannelFuture write(Object msg)
/*      */   {
/* 1049 */     return this.tail.write(msg);
/*      */   }
/*      */   
/*      */   public ChannelFuture write(Object msg, ChannelPromise promise)
/*      */   {
/* 1054 */     return this.tail.write(msg, promise);
/*      */   }
/*      */   
/*      */   public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise)
/*      */   {
/* 1059 */     return this.tail.writeAndFlush(msg, promise);
/*      */   }
/*      */   
/*      */   public ChannelFuture writeAndFlush(Object msg)
/*      */   {
/* 1064 */     return this.tail.writeAndFlush(msg);
/*      */   }
/*      */   
/*      */   private String filterName(String name, ChannelHandler handler) {
/* 1068 */     if (name == null) {
/* 1069 */       return generateName(handler);
/*      */     }
/*      */     
/* 1072 */     if (!this.name2ctx.containsKey(name)) {
/* 1073 */       return name;
/*      */     }
/*      */     
/* 1076 */     throw new IllegalArgumentException("Duplicate handler name: " + name);
/*      */   }
/*      */   
/*      */   private AbstractChannelHandlerContext getContextOrDie(String name) {
/* 1080 */     AbstractChannelHandlerContext ctx = (AbstractChannelHandlerContext)context(name);
/* 1081 */     if (ctx == null) {
/* 1082 */       throw new NoSuchElementException(name);
/*      */     }
/* 1084 */     return ctx;
/*      */   }
/*      */   
/*      */   private AbstractChannelHandlerContext getContextOrDie(ChannelHandler handler)
/*      */   {
/* 1089 */     AbstractChannelHandlerContext ctx = (AbstractChannelHandlerContext)context(handler);
/* 1090 */     if (ctx == null) {
/* 1091 */       throw new NoSuchElementException(handler.getClass().getName());
/*      */     }
/* 1093 */     return ctx;
/*      */   }
/*      */   
/*      */   private AbstractChannelHandlerContext getContextOrDie(Class<? extends ChannelHandler> handlerType)
/*      */   {
/* 1098 */     AbstractChannelHandlerContext ctx = (AbstractChannelHandlerContext)context(handlerType);
/* 1099 */     if (ctx == null) {
/* 1100 */       throw new NoSuchElementException(handlerType.getName());
/*      */     }
/* 1102 */     return ctx;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public ChannelHandlerContext context(String name)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_1
/*      */     //   1: ifnonnull +14 -> 15
/*      */     //   4: new 61	java/lang/NullPointerException
/*      */     //   7: dup
/*      */     //   8: ldc_w 496
/*      */     //   11: invokespecial 65	java/lang/NullPointerException:<init>	(Ljava/lang/String;)V
/*      */     //   14: athrow
/*      */     //   15: aload_0
/*      */     //   16: dup
/*      */     //   17: astore_2
/*      */     //   18: monitorenter
/*      */     //   19: aload_0
/*      */     //   20: getfield 59	io/netty/channel/DefaultChannelPipeline:name2ctx	Ljava/util/Map;
/*      */     //   23: aload_1
/*      */     //   24: invokeinterface 206 2 0
/*      */     //   29: checkcast 384	io/netty/channel/ChannelHandlerContext
/*      */     //   32: aload_2
/*      */     //   33: monitorexit
/*      */     //   34: areturn
/*      */     //   35: astore_3
/*      */     //   36: aload_2
/*      */     //   37: monitorexit
/*      */     //   38: aload_3
/*      */     //   39: athrow
/*      */     // Line number table:
/*      */     //   Java source line #731	-> byte code offset #0
/*      */     //   Java source line #732	-> byte code offset #4
/*      */     //   Java source line #735	-> byte code offset #15
/*      */     //   Java source line #736	-> byte code offset #19
/*      */     //   Java source line #737	-> byte code offset #35
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	40	0	this	DefaultChannelPipeline
/*      */     //   0	40	1	name	String
/*      */     //   17	20	2	Ljava/lang/Object;	Object
/*      */     //   35	4	3	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   19	34	35	finally
/*      */     //   35	38	35	finally
/*      */   }
/*      */   
/*      */   static final class TailContext
/*      */     extends AbstractChannelHandlerContext
/*      */     implements ChannelHandler
/*      */   {
/* 1107 */     private static final int SKIP_FLAGS = skipFlags0(TailContext.class);
/* 1108 */     private static final String TAIL_NAME = DefaultChannelPipeline.generateName0(TailContext.class);
/*      */     
/*      */     TailContext(DefaultChannelPipeline pipeline) {
/* 1111 */       super(null, TAIL_NAME, SKIP_FLAGS);
/*      */     }
/*      */     
/*      */     public ChannelHandler handler()
/*      */     {
/* 1116 */       return this;
/*      */     }
/*      */     
/*      */     public void channelRegistered(ChannelHandlerContext ctx) throws Exception
/*      */     {}
/*      */     
/*      */     public void channelUnregistered(ChannelHandlerContext ctx) throws Exception
/*      */     {}
/*      */     
/*      */     public void channelActive(ChannelHandlerContext ctx) throws Exception
/*      */     {}
/*      */     
/*      */     public void channelInactive(ChannelHandlerContext ctx) throws Exception
/*      */     {}
/*      */     
/*      */     public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception
/*      */     {}
/*      */     
/*      */     public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
/*      */     {}
/*      */     
/*      */     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
/*      */     {
/* 1139 */       DefaultChannelPipeline.logger.warn("An exceptionCaught() event was fired, and it reached at the tail of the pipeline. It usually means the last handler in the pipeline did not handle the exception.", cause);
/*      */     }
/*      */     
/*      */     public void channelRead(ChannelHandlerContext ctx, Object msg)
/*      */       throws Exception
/*      */     {
/*      */       try
/*      */       {
/* 1147 */         DefaultChannelPipeline.logger.debug("Discarded inbound message {} that reached at the tail of the pipeline. Please check your pipeline configuration.", msg);
/*      */       }
/*      */       finally
/*      */       {
/* 1151 */         ReferenceCountUtil.release(msg);
/*      */       }
/*      */     }
/*      */     
/*      */     public void channelReadComplete(ChannelHandlerContext ctx)
/*      */       throws Exception
/*      */     {}
/*      */     
/*      */     @ChannelHandler.Skip
/*      */     public void handlerAdded(ChannelHandlerContext ctx) throws Exception
/*      */     {}
/*      */     
/*      */     @ChannelHandler.Skip
/*      */     public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
/*      */     {}
/*      */     
/*      */     @ChannelHandler.Skip
/*      */     public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception
/*      */     {
/* 1170 */       ctx.bind(localAddress, promise);
/*      */     }
/*      */     
/*      */     @ChannelHandler.Skip
/*      */     public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
/*      */       throws Exception
/*      */     {
/* 1177 */       ctx.connect(remoteAddress, localAddress, promise);
/*      */     }
/*      */     
/*      */     @ChannelHandler.Skip
/*      */     public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception
/*      */     {
/* 1183 */       ctx.disconnect(promise);
/*      */     }
/*      */     
/*      */     @ChannelHandler.Skip
/*      */     public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception
/*      */     {
/* 1189 */       ctx.close(promise);
/*      */     }
/*      */     
/*      */     @ChannelHandler.Skip
/*      */     public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception
/*      */     {
/* 1195 */       ctx.deregister(promise);
/*      */     }
/*      */     
/*      */     @ChannelHandler.Skip
/*      */     public void read(ChannelHandlerContext ctx) throws Exception
/*      */     {
/* 1201 */       ctx.read();
/*      */     }
/*      */     
/*      */     @ChannelHandler.Skip
/*      */     public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
/*      */     {
/* 1207 */       ctx.write(msg, promise);
/*      */     }
/*      */     
/*      */     @ChannelHandler.Skip
/*      */     public void flush(ChannelHandlerContext ctx) throws Exception
/*      */     {
/* 1213 */       ctx.flush();
/*      */     }
/*      */   }
/*      */   
/*      */   static final class HeadContext extends AbstractChannelHandlerContext implements ChannelHandler {
/* 1218 */     private static final int SKIP_FLAGS = skipFlags0(HeadContext.class);
/* 1219 */     private static final String HEAD_NAME = DefaultChannelPipeline.generateName0(HeadContext.class);
/*      */     private final Channel.Unsafe unsafe;
/*      */     
/*      */     HeadContext(DefaultChannelPipeline pipeline)
/*      */     {
/* 1224 */       super(null, HEAD_NAME, SKIP_FLAGS);
/* 1225 */       this.unsafe = pipeline.channel().unsafe();
/*      */     }
/*      */     
/*      */     public ChannelHandler handler()
/*      */     {
/* 1230 */       return this;
/*      */     }
/*      */     
/*      */ 
/*      */     public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise)
/*      */       throws Exception
/*      */     {
/* 1237 */       this.unsafe.bind(localAddress, promise);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
/*      */       throws Exception
/*      */     {
/* 1245 */       this.unsafe.connect(remoteAddress, localAddress, promise);
/*      */     }
/*      */     
/*      */     public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception
/*      */     {
/* 1250 */       this.unsafe.disconnect(promise);
/*      */     }
/*      */     
/*      */     public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception
/*      */     {
/* 1255 */       this.unsafe.close(promise);
/*      */     }
/*      */     
/*      */     public void deregister(ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception
/*      */     {
/* 1260 */       assert (!((PausableEventExecutor)ctx.channel().eventLoop()).isAcceptingNewTasks());
/*      */       
/*      */ 
/* 1263 */       ctx.channel().eventLoop().unwrap().execute(new OneTimeTask()
/*      */       {
/*      */         public void run() {
/* 1266 */           DefaultChannelPipeline.HeadContext.this.unsafe.deregister(promise);
/*      */         }
/*      */       });
/*      */     }
/*      */     
/*      */     public void read(ChannelHandlerContext ctx)
/*      */     {
/* 1273 */       this.unsafe.beginRead();
/*      */     }
/*      */     
/*      */     public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
/*      */     {
/* 1278 */       this.unsafe.write(msg, promise);
/*      */     }
/*      */     
/*      */     public void flush(ChannelHandlerContext ctx) throws Exception
/*      */     {
/* 1283 */       this.unsafe.flush();
/*      */     }
/*      */     
/*      */     @ChannelHandler.Skip
/*      */     public void handlerAdded(ChannelHandlerContext ctx) throws Exception
/*      */     {}
/*      */     
/*      */     @ChannelHandler.Skip
/*      */     public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
/*      */     {}
/*      */     
/*      */     @ChannelHandler.Skip
/*      */     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
/*      */     {
/* 1297 */       ctx.fireExceptionCaught(cause);
/*      */     }
/*      */     
/*      */     @ChannelHandler.Skip
/*      */     public void channelRegistered(ChannelHandlerContext ctx) throws Exception
/*      */     {
/* 1303 */       ctx.fireChannelRegistered();
/*      */     }
/*      */     
/*      */     @ChannelHandler.Skip
/*      */     public void channelUnregistered(ChannelHandlerContext ctx) throws Exception
/*      */     {
/* 1309 */       ctx.fireChannelUnregistered();
/*      */     }
/*      */     
/*      */     @ChannelHandler.Skip
/*      */     public void channelActive(ChannelHandlerContext ctx) throws Exception
/*      */     {
/* 1315 */       ctx.fireChannelActive();
/*      */     }
/*      */     
/*      */     @ChannelHandler.Skip
/*      */     public void channelInactive(ChannelHandlerContext ctx) throws Exception
/*      */     {
/* 1321 */       ctx.fireChannelInactive();
/*      */     }
/*      */     
/*      */     @ChannelHandler.Skip
/*      */     public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
/*      */     {
/* 1327 */       ctx.fireChannelRead(msg);
/*      */     }
/*      */     
/*      */     @ChannelHandler.Skip
/*      */     public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
/*      */     {
/* 1333 */       ctx.fireChannelReadComplete();
/*      */     }
/*      */     
/*      */     @ChannelHandler.Skip
/*      */     public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
/*      */     {
/* 1339 */       ctx.fireUserEventTriggered(evt);
/*      */     }
/*      */     
/*      */     @ChannelHandler.Skip
/*      */     public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception
/*      */     {
/* 1345 */       ctx.fireChannelWritabilityChanged();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\DefaultChannelPipeline.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */