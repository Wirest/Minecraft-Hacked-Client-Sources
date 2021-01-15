/*     */ package io.netty.channel.nio;
/*     */ 
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.EventLoopException;
/*     */ import io.netty.channel.SingleThreadEventLoop;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.nio.channels.CancelledKeyException;
/*     */ import java.nio.channels.SelectableChannel;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.Selector;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ public final class NioEventLoop
/*     */   extends SingleThreadEventLoop
/*     */ {
/*  51 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NioEventLoop.class);
/*     */   
/*     */   private static final int CLEANUP_INTERVAL = 256;
/*     */   
/*  55 */   private static final boolean DISABLE_KEYSET_OPTIMIZATION = SystemPropertyUtil.getBoolean("io.netty.noKeySetOptimization", false);
/*     */   
/*     */   private static final int MIN_PREMATURE_SELECTOR_RETURNS = 3;
/*     */   
/*     */   private static final int SELECTOR_AUTO_REBUILD_THRESHOLD;
/*     */   
/*     */   Selector selector;
/*     */   private SelectedSelectionKeySet selectedKeys;
/*     */   private final SelectorProvider provider;
/*     */   
/*     */   static
/*     */   {
/*  67 */     String key = "sun.nio.ch.bugLevel";
/*     */     try {
/*  69 */       String buglevel = SystemPropertyUtil.get(key);
/*  70 */       if (buglevel == null) {
/*  71 */         System.setProperty(key, "");
/*     */       }
/*     */     } catch (SecurityException e) {
/*  74 */       if (logger.isDebugEnabled()) {
/*  75 */         logger.debug("Unable to get/set System Property: {}", key, e);
/*     */       }
/*     */     }
/*     */     
/*  79 */     int selectorAutoRebuildThreshold = SystemPropertyUtil.getInt("io.netty.selectorAutoRebuildThreshold", 512);
/*  80 */     if (selectorAutoRebuildThreshold < 3) {
/*  81 */       selectorAutoRebuildThreshold = 0;
/*     */     }
/*     */     
/*  84 */     SELECTOR_AUTO_REBUILD_THRESHOLD = selectorAutoRebuildThreshold;
/*     */     
/*  86 */     if (logger.isDebugEnabled()) {
/*  87 */       logger.debug("-Dio.netty.noKeySetOptimization: {}", Boolean.valueOf(DISABLE_KEYSET_OPTIMIZATION));
/*  88 */       logger.debug("-Dio.netty.selectorAutoRebuildThreshold: {}", Integer.valueOf(SELECTOR_AUTO_REBUILD_THRESHOLD));
/*     */     }
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
/* 106 */   private final AtomicBoolean wakenUp = new AtomicBoolean();
/*     */   
/* 108 */   private volatile int ioRatio = 50;
/*     */   private int cancelledKeys;
/*     */   private boolean needsToSelectAgain;
/*     */   
/*     */   NioEventLoop(NioEventLoopGroup parent, Executor executor, SelectorProvider selectorProvider) {
/* 113 */     super(parent, executor, false);
/* 114 */     if (selectorProvider == null) {
/* 115 */       throw new NullPointerException("selectorProvider");
/*     */     }
/* 117 */     this.provider = selectorProvider;
/* 118 */     this.selector = openSelector();
/*     */   }
/*     */   
/*     */   private Selector openSelector() {
/*     */     Selector selector;
/*     */     try {
/* 124 */       selector = this.provider.openSelector();
/*     */     } catch (IOException e) {
/* 126 */       throw new ChannelException("failed to open a new selector", e);
/*     */     }
/*     */     
/* 129 */     if (DISABLE_KEYSET_OPTIMIZATION) {
/* 130 */       return selector;
/*     */     }
/*     */     try
/*     */     {
/* 134 */       SelectedSelectionKeySet selectedKeySet = new SelectedSelectionKeySet();
/*     */       
/* 136 */       Class<?> selectorImplClass = Class.forName("sun.nio.ch.SelectorImpl", false, PlatformDependent.getSystemClassLoader());
/*     */       
/*     */ 
/*     */ 
/* 140 */       if (!selectorImplClass.isAssignableFrom(selector.getClass())) {
/* 141 */         return selector;
/*     */       }
/*     */       
/* 144 */       Field selectedKeysField = selectorImplClass.getDeclaredField("selectedKeys");
/* 145 */       Field publicSelectedKeysField = selectorImplClass.getDeclaredField("publicSelectedKeys");
/*     */       
/* 147 */       selectedKeysField.setAccessible(true);
/* 148 */       publicSelectedKeysField.setAccessible(true);
/*     */       
/* 150 */       selectedKeysField.set(selector, selectedKeySet);
/* 151 */       publicSelectedKeysField.set(selector, selectedKeySet);
/*     */       
/* 153 */       this.selectedKeys = selectedKeySet;
/* 154 */       logger.trace("Instrumented an optimized java.util.Set into: {}", selector);
/*     */     } catch (Throwable t) {
/* 156 */       this.selectedKeys = null;
/* 157 */       logger.trace("Failed to instrument an optimized java.util.Set into: {}", selector, t);
/*     */     }
/*     */     
/* 160 */     return selector;
/*     */   }
/*     */   
/*     */ 
/*     */   protected Queue<Runnable> newTaskQueue()
/*     */   {
/* 166 */     return PlatformDependent.newMpscQueue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void register(SelectableChannel ch, int interestOps, NioTask<?> task)
/*     */   {
/* 175 */     if (ch == null) {
/* 176 */       throw new NullPointerException("ch");
/*     */     }
/* 178 */     if (interestOps == 0) {
/* 179 */       throw new IllegalArgumentException("interestOps must be non-zero.");
/*     */     }
/* 181 */     if ((interestOps & (ch.validOps() ^ 0xFFFFFFFF)) != 0) {
/* 182 */       throw new IllegalArgumentException("invalid interestOps: " + interestOps + "(validOps: " + ch.validOps() + ')');
/*     */     }
/*     */     
/* 185 */     if (task == null) {
/* 186 */       throw new NullPointerException("task");
/*     */     }
/*     */     
/* 189 */     if (isShutdown()) {
/* 190 */       throw new IllegalStateException("event loop shut down");
/*     */     }
/*     */     try
/*     */     {
/* 194 */       ch.register(this.selector, interestOps, task);
/*     */     } catch (Exception e) {
/* 196 */       throw new EventLoopException("failed to register a channel", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getIoRatio()
/*     */   {
/* 204 */     return this.ioRatio;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIoRatio(int ioRatio)
/*     */   {
/* 212 */     if ((ioRatio <= 0) || (ioRatio > 100)) {
/* 213 */       throw new IllegalArgumentException("ioRatio: " + ioRatio + " (expected: 0 < ioRatio <= 100)");
/*     */     }
/* 215 */     this.ioRatio = ioRatio;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void rebuildSelector()
/*     */   {
/* 223 */     if (!inEventLoop()) {
/* 224 */       execute(new Runnable()
/*     */       {
/*     */         public void run() {
/* 227 */           NioEventLoop.this.rebuildSelector();
/*     */         }
/* 229 */       });
/* 230 */       return;
/*     */     }
/*     */     
/* 233 */     Selector oldSelector = this.selector;
/*     */     
/*     */ 
/* 236 */     if (oldSelector == null) {
/*     */       return;
/*     */     }
/*     */     Selector newSelector;
/*     */     try {
/* 241 */       newSelector = openSelector();
/*     */     } catch (Exception e) {
/* 243 */       logger.warn("Failed to create a new Selector.", e);
/* 244 */       return;
/*     */     }
/*     */     
/*     */ 
/* 248 */     int nChannels = 0;
/*     */     for (;;) {
/*     */       try {
/* 251 */         Iterator i$ = oldSelector.keys().iterator(); if (i$.hasNext()) { SelectionKey key = (SelectionKey)i$.next();
/* 252 */           Object a = key.attachment();
/*     */           try {
/* 254 */             if ((key.isValid()) && (key.channel().keyFor(newSelector) != null)) {
/*     */               continue;
/*     */             }
/*     */             
/* 258 */             int interestOps = key.interestOps();
/* 259 */             key.cancel();
/* 260 */             SelectionKey newKey = key.channel().register(newSelector, interestOps, a);
/* 261 */             if ((a instanceof AbstractNioChannel))
/*     */             {
/* 263 */               ((AbstractNioChannel)a).selectionKey = newKey;
/*     */             }
/* 265 */             nChannels++;
/*     */           } catch (Exception e) {
/* 267 */             logger.warn("Failed to re-register a Channel to the new Selector.", e);
/* 268 */             if ((a instanceof AbstractNioChannel)) {
/* 269 */               AbstractNioChannel ch = (AbstractNioChannel)a;
/* 270 */               ch.unsafe().close(ch.unsafe().voidPromise());
/*     */             }
/*     */             else {
/* 273 */               NioTask<SelectableChannel> task = (NioTask)a;
/* 274 */               invokeChannelUnregistered(task, key, e);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (ConcurrentModificationException e) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 286 */     this.selector = newSelector;
/*     */     
/*     */     try
/*     */     {
/* 290 */       oldSelector.close();
/*     */     } catch (Throwable t) {
/* 292 */       if (logger.isWarnEnabled()) {
/* 293 */         logger.warn("Failed to close the old Selector.", t);
/*     */       }
/*     */     }
/*     */     
/* 297 */     logger.info("Migrated " + nChannels + " channel(s) to the new Selector.");
/*     */   }
/*     */   
/*     */   protected void run()
/*     */   {
/* 302 */     boolean oldWakenUp = this.wakenUp.getAndSet(false);
/*     */     try {
/* 304 */       if (hasTasks()) {
/* 305 */         selectNow();
/*     */       } else {
/* 307 */         select(oldWakenUp);
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
/* 337 */         if (this.wakenUp.get()) {
/* 338 */           this.selector.wakeup();
/*     */         }
/*     */       }
/*     */       
/* 342 */       this.cancelledKeys = 0;
/* 343 */       this.needsToSelectAgain = false;
/* 344 */       int ioRatio = this.ioRatio;
/* 345 */       if (ioRatio == 100) {
/* 346 */         processSelectedKeys();
/* 347 */         runAllTasks();
/*     */       } else {
/* 349 */         long ioStartTime = System.nanoTime();
/*     */         
/* 351 */         processSelectedKeys();
/*     */         
/* 353 */         long ioTime = System.nanoTime() - ioStartTime;
/* 354 */         runAllTasks(ioTime * (100 - ioRatio) / ioRatio);
/*     */       }
/*     */       
/* 357 */       if (isShuttingDown()) {
/* 358 */         closeAll();
/* 359 */         if (confirmShutdown()) {
/* 360 */           cleanupAndTerminate(true);
/* 361 */           return;
/*     */         }
/*     */       }
/*     */     } catch (Throwable t) {
/* 365 */       logger.warn("Unexpected exception in the selector loop.", t);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       try
/*     */       {
/* 374 */         Thread.sleep(1000L);
/*     */       }
/*     */       catch (InterruptedException e) {}
/*     */     }
/*     */     
/*     */ 
/* 380 */     scheduleExecution();
/*     */   }
/*     */   
/*     */   private void processSelectedKeys() {
/* 384 */     if (this.selectedKeys != null) {
/* 385 */       processSelectedKeysOptimized(this.selectedKeys.flip());
/*     */     } else {
/* 387 */       processSelectedKeysPlain(this.selector.selectedKeys());
/*     */     }
/*     */   }
/*     */   
/*     */   protected void cleanup()
/*     */   {
/*     */     try {
/* 394 */       this.selector.close();
/*     */     } catch (IOException e) {
/* 396 */       logger.warn("Failed to close a selector.", e);
/*     */     }
/*     */   }
/*     */   
/*     */   void cancel(SelectionKey key) {
/* 401 */     key.cancel();
/* 402 */     this.cancelledKeys += 1;
/* 403 */     if (this.cancelledKeys >= 256) {
/* 404 */       this.cancelledKeys = 0;
/* 405 */       this.needsToSelectAgain = true;
/*     */     }
/*     */   }
/*     */   
/*     */   protected Runnable pollTask()
/*     */   {
/* 411 */     Runnable task = super.pollTask();
/* 412 */     if (this.needsToSelectAgain) {
/* 413 */       selectAgain();
/*     */     }
/* 415 */     return task;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void processSelectedKeysPlain(Set<SelectionKey> selectedKeys)
/*     */   {
/* 422 */     if (selectedKeys.isEmpty()) {
/* 423 */       return;
/*     */     }
/*     */     
/* 426 */     Iterator<SelectionKey> i = selectedKeys.iterator();
/*     */     for (;;) {
/* 428 */       SelectionKey k = (SelectionKey)i.next();
/* 429 */       Object a = k.attachment();
/* 430 */       i.remove();
/*     */       
/* 432 */       if ((a instanceof AbstractNioChannel)) {
/* 433 */         processSelectedKey(k, (AbstractNioChannel)a);
/*     */       }
/*     */       else {
/* 436 */         NioTask<SelectableChannel> task = (NioTask)a;
/* 437 */         processSelectedKey(k, task);
/*     */       }
/*     */       
/* 440 */       if (!i.hasNext()) {
/*     */         break;
/*     */       }
/*     */       
/* 444 */       if (this.needsToSelectAgain) {
/* 445 */         selectAgain();
/* 446 */         selectedKeys = this.selector.selectedKeys();
/*     */         
/*     */ 
/* 449 */         if (selectedKeys.isEmpty()) {
/*     */           break;
/*     */         }
/* 452 */         i = selectedKeys.iterator();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void processSelectedKeysOptimized(SelectionKey[] selectedKeys)
/*     */   {
/* 459 */     for (int i = 0;; i++) {
/* 460 */       SelectionKey k = selectedKeys[i];
/* 461 */       if (k == null) {
/*     */         break;
/*     */       }
/*     */       
/*     */ 
/* 466 */       selectedKeys[i] = null;
/*     */       
/* 468 */       Object a = k.attachment();
/*     */       
/* 470 */       if ((a instanceof AbstractNioChannel)) {
/* 471 */         processSelectedKey(k, (AbstractNioChannel)a);
/*     */       }
/*     */       else {
/* 474 */         NioTask<SelectableChannel> task = (NioTask)a;
/* 475 */         processSelectedKey(k, task);
/*     */       }
/*     */       
/* 478 */       if (this.needsToSelectAgain)
/*     */       {
/*     */ 
/*     */ 
/* 482 */         while (selectedKeys[i] != null)
/*     */         {
/*     */ 
/* 485 */           selectedKeys[i] = null;
/* 486 */           i++;
/*     */         }
/*     */         
/* 489 */         selectAgain();
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 495 */         selectedKeys = this.selectedKeys.flip();
/* 496 */         i = -1;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void processSelectedKey(SelectionKey k, AbstractNioChannel ch) {
/* 502 */     AbstractNioChannel.NioUnsafe unsafe = ch.unsafe();
/* 503 */     if (!k.isValid())
/*     */     {
/* 505 */       unsafe.close(unsafe.voidPromise());
/* 506 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 510 */       int readyOps = k.readyOps();
/*     */       
/*     */ 
/* 513 */       if (((readyOps & 0x11) != 0) || (readyOps == 0)) {
/* 514 */         unsafe.read();
/* 515 */         if (!ch.isOpen())
/*     */         {
/* 517 */           return;
/*     */         }
/*     */       }
/* 520 */       if ((readyOps & 0x4) != 0)
/*     */       {
/* 522 */         ch.unsafe().forceFlush();
/*     */       }
/* 524 */       if ((readyOps & 0x8) != 0)
/*     */       {
/*     */ 
/* 527 */         int ops = k.interestOps();
/* 528 */         ops &= 0xFFFFFFF7;
/* 529 */         k.interestOps(ops);
/*     */         
/* 531 */         unsafe.finishConnect();
/*     */       }
/*     */     } catch (CancelledKeyException ignored) {
/* 534 */       unsafe.close(unsafe.voidPromise());
/*     */     }
/*     */   }
/*     */   
/*     */   private static void processSelectedKey(SelectionKey k, NioTask<SelectableChannel> task) {
/* 539 */     int state = 0;
/*     */     try {
/* 541 */       task.channelReady(k.channel(), k);
/* 542 */       state = 1;
/*     */     } catch (Exception e) {
/* 544 */       k.cancel();
/* 545 */       invokeChannelUnregistered(task, k, e);
/* 546 */       state = 2;
/*     */     } finally {
/* 548 */       switch (state) {
/*     */       case 0: 
/* 550 */         k.cancel();
/* 551 */         invokeChannelUnregistered(task, k, null);
/* 552 */         break;
/*     */       case 1: 
/* 554 */         if (!k.isValid()) {
/* 555 */           invokeChannelUnregistered(task, k, null);
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void closeAll() {
/* 563 */     selectAgain();
/* 564 */     Set<SelectionKey> keys = this.selector.keys();
/* 565 */     Collection<AbstractNioChannel> channels = new ArrayList(keys.size());
/* 566 */     for (SelectionKey k : keys) {
/* 567 */       Object a = k.attachment();
/* 568 */       if ((a instanceof AbstractNioChannel)) {
/* 569 */         channels.add((AbstractNioChannel)a);
/*     */       } else {
/* 571 */         k.cancel();
/*     */         
/* 573 */         NioTask<SelectableChannel> task = (NioTask)a;
/* 574 */         invokeChannelUnregistered(task, k, null);
/*     */       }
/*     */     }
/*     */     
/* 578 */     for (AbstractNioChannel ch : channels) {
/* 579 */       ch.unsafe().close(ch.unsafe().voidPromise());
/*     */     }
/*     */   }
/*     */   
/*     */   private static void invokeChannelUnregistered(NioTask<SelectableChannel> task, SelectionKey k, Throwable cause) {
/*     */     try {
/* 585 */       task.channelUnregistered(k.channel(), cause);
/*     */     } catch (Exception e) {
/* 587 */       logger.warn("Unexpected exception while running NioTask.channelUnregistered()", e);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void wakeup(boolean inEventLoop)
/*     */   {
/* 593 */     if ((!inEventLoop) && (this.wakenUp.compareAndSet(false, true))) {
/* 594 */       this.selector.wakeup();
/*     */     }
/*     */   }
/*     */   
/*     */   void selectNow() throws IOException {
/*     */     try {
/* 600 */       this.selector.selectNow();
/*     */     }
/*     */     finally {
/* 603 */       if (this.wakenUp.get()) {
/* 604 */         this.selector.wakeup();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void select(boolean oldWakenUp) throws IOException {
/* 610 */     Selector selector = this.selector;
/*     */     try {
/* 612 */       int selectCnt = 0;
/* 613 */       long currentTimeNanos = System.nanoTime();
/* 614 */       long selectDeadLineNanos = currentTimeNanos + delayNanos(currentTimeNanos);
/*     */       for (;;) {
/* 616 */         long timeoutMillis = (selectDeadLineNanos - currentTimeNanos + 500000L) / 1000000L;
/* 617 */         if (timeoutMillis <= 0L) {
/* 618 */           if (selectCnt != 0) break;
/* 619 */           selector.selectNow();
/* 620 */           selectCnt = 1; break;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 625 */         int selectedKeys = selector.select(timeoutMillis);
/* 626 */         selectCnt++;
/*     */         
/* 628 */         if ((selectedKeys != 0) || (oldWakenUp) || (this.wakenUp.get()) || (hasTasks()) || (hasScheduledTasks())) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 635 */         if (Thread.interrupted())
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 641 */           if (logger.isDebugEnabled()) {
/* 642 */             logger.debug("Selector.select() returned prematurely because Thread.currentThread().interrupt() was called. Use NioEventLoop.shutdownGracefully() to shutdown the NioEventLoop.");
/*     */           }
/*     */           
/*     */ 
/* 646 */           selectCnt = 1;
/* 647 */           break;
/*     */         }
/*     */         
/* 650 */         long time = System.nanoTime();
/* 651 */         if (time - TimeUnit.MILLISECONDS.toNanos(timeoutMillis) >= currentTimeNanos)
/*     */         {
/* 653 */           selectCnt = 1;
/* 654 */         } else if ((SELECTOR_AUTO_REBUILD_THRESHOLD > 0) && (selectCnt >= SELECTOR_AUTO_REBUILD_THRESHOLD))
/*     */         {
/*     */ 
/*     */ 
/* 658 */           logger.warn("Selector.select() returned prematurely {} times in a row; rebuilding selector.", Integer.valueOf(selectCnt));
/*     */           
/*     */ 
/*     */ 
/* 662 */           rebuildSelector();
/* 663 */           selector = this.selector;
/*     */           
/*     */ 
/* 666 */           selector.selectNow();
/* 667 */           selectCnt = 1;
/* 668 */           break;
/*     */         }
/*     */         
/* 671 */         currentTimeNanos = time;
/*     */       }
/*     */       
/* 674 */       if ((selectCnt > 3) && 
/* 675 */         (logger.isDebugEnabled())) {
/* 676 */         logger.debug("Selector.select() returned prematurely {} times in a row.", Integer.valueOf(selectCnt - 1));
/*     */       }
/*     */     }
/*     */     catch (CancelledKeyException e) {
/* 680 */       if (logger.isDebugEnabled()) {
/* 681 */         logger.debug(CancelledKeyException.class.getSimpleName() + " raised by a Selector - JDK bug?", e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void selectAgain()
/*     */   {
/* 688 */     this.needsToSelectAgain = false;
/*     */     try {
/* 690 */       this.selector.selectNow();
/*     */     } catch (Throwable t) {
/* 692 */       logger.warn("Failed to update SelectionKeys.", t);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\nio\NioEventLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */