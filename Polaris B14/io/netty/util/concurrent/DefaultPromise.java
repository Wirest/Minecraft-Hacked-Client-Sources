/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import io.netty.util.Signal;
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import io.netty.util.internal.InternalThreadLocalMap;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.concurrent.CancellationException;
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
/*     */ 
/*     */ public class DefaultPromise<V>
/*     */   extends AbstractFuture<V>
/*     */   implements Promise<V>
/*     */ {
/*  34 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultPromise.class);
/*  35 */   private static final InternalLogger rejectedExecutionLogger = InternalLoggerFactory.getInstance(DefaultPromise.class.getName() + ".rejectedExecution");
/*     */   
/*     */   private static final int MAX_LISTENER_STACK_DEPTH = 8;
/*     */   
/*  39 */   private static final Signal SUCCESS = Signal.valueOf(DefaultPromise.class, "SUCCESS");
/*  40 */   private static final Signal UNCANCELLABLE = Signal.valueOf(DefaultPromise.class, "UNCANCELLABLE");
/*  41 */   private static final CauseHolder CANCELLATION_CAUSE_HOLDER = new CauseHolder(new CancellationException());
/*     */   EventExecutor executor;
/*     */   
/*  44 */   static { CANCELLATION_CAUSE_HOLDER.cause.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE); }
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
/*     */   public DefaultPromise(EventExecutor executor)
/*     */   {
/*  75 */     if (executor == null) {
/*  76 */       throw new NullPointerException("executor");
/*     */     }
/*  78 */     this.executor = executor;
/*     */   }
/*     */   
/*     */   protected DefaultPromise()
/*     */   {
/*  83 */     this.executor = null;
/*     */   }
/*     */   
/*     */   protected EventExecutor executor() {
/*  87 */     return this.executor;
/*     */   }
/*     */   
/*     */   public boolean isCancelled()
/*     */   {
/*  92 */     return isCancelled0(this.result);
/*     */   }
/*     */   
/*     */   private static boolean isCancelled0(Object result) {
/*  96 */     return ((result instanceof CauseHolder)) && ((((CauseHolder)result).cause instanceof CancellationException));
/*     */   }
/*     */   
/*     */   public boolean isCancellable()
/*     */   {
/* 101 */     return this.result == null;
/*     */   }
/*     */   
/*     */   public boolean isDone()
/*     */   {
/* 106 */     return isDone0(this.result);
/*     */   }
/*     */   
/*     */   private static boolean isDone0(Object result) {
/* 110 */     return (result != null) && (result != UNCANCELLABLE);
/*     */   }
/*     */   
/*     */   public boolean isSuccess()
/*     */   {
/* 115 */     Object result = this.result;
/* 116 */     if ((result == null) || (result == UNCANCELLABLE)) {
/* 117 */       return false;
/*     */     }
/* 119 */     return !(result instanceof CauseHolder);
/*     */   }
/*     */   
/*     */   public Throwable cause()
/*     */   {
/* 124 */     Object result = this.result;
/* 125 */     if ((result instanceof CauseHolder)) {
/* 126 */       return ((CauseHolder)result).cause;
/*     */     }
/* 128 */     return null;
/*     */   }
/*     */   
/*     */   public Promise<V> addListener(GenericFutureListener<? extends Future<? super V>> listener)
/*     */   {
/* 133 */     if (listener == null) {
/* 134 */       throw new NullPointerException("listener");
/*     */     }
/*     */     
/* 137 */     if (isDone()) {
/* 138 */       notifyLateListener(listener);
/* 139 */       return this;
/*     */     }
/*     */     
/* 142 */     synchronized (this) {
/* 143 */       if (!isDone()) {
/* 144 */         if (this.listeners == null) {
/* 145 */           this.listeners = listener;
/*     */         }
/* 147 */         else if ((this.listeners instanceof DefaultFutureListeners)) {
/* 148 */           ((DefaultFutureListeners)this.listeners).add(listener);
/*     */         } else {
/* 150 */           GenericFutureListener<? extends Future<V>> firstListener = (GenericFutureListener)this.listeners;
/*     */           
/* 152 */           this.listeners = new DefaultFutureListeners(firstListener, listener);
/*     */         }
/*     */         
/* 155 */         return this;
/*     */       }
/*     */     }
/*     */     
/* 159 */     notifyLateListener(listener);
/* 160 */     return this;
/*     */   }
/*     */   
/*     */   public Promise<V> addListeners(GenericFutureListener<? extends Future<? super V>>... listeners)
/*     */   {
/* 165 */     if (listeners == null) {
/* 166 */       throw new NullPointerException("listeners");
/*     */     }
/*     */     
/* 169 */     for (GenericFutureListener<? extends Future<? super V>> l : listeners) {
/* 170 */       if (l == null) {
/*     */         break;
/*     */       }
/* 173 */       addListener(l);
/*     */     }
/* 175 */     return this;
/*     */   }
/*     */   
/*     */   public Promise<V> removeListener(GenericFutureListener<? extends Future<? super V>> listener)
/*     */   {
/* 180 */     if (listener == null) {
/* 181 */       throw new NullPointerException("listener");
/*     */     }
/*     */     
/* 184 */     if (isDone()) {
/* 185 */       return this;
/*     */     }
/*     */     
/* 188 */     synchronized (this) {
/* 189 */       if (!isDone()) {
/* 190 */         if ((this.listeners instanceof DefaultFutureListeners)) {
/* 191 */           ((DefaultFutureListeners)this.listeners).remove(listener);
/* 192 */         } else if (this.listeners == listener) {
/* 193 */           this.listeners = null;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 198 */     return this;
/*     */   }
/*     */   
/*     */   public Promise<V> removeListeners(GenericFutureListener<? extends Future<? super V>>... listeners)
/*     */   {
/* 203 */     if (listeners == null) {
/* 204 */       throw new NullPointerException("listeners");
/*     */     }
/*     */     
/* 207 */     for (GenericFutureListener<? extends Future<? super V>> l : listeners) {
/* 208 */       if (l == null) {
/*     */         break;
/*     */       }
/* 211 */       removeListener(l);
/*     */     }
/* 213 */     return this;
/*     */   }
/*     */   
/*     */   public Promise<V> sync() throws InterruptedException
/*     */   {
/* 218 */     await();
/* 219 */     rethrowIfFailed();
/* 220 */     return this;
/*     */   }
/*     */   
/*     */   public Promise<V> syncUninterruptibly()
/*     */   {
/* 225 */     awaitUninterruptibly();
/* 226 */     rethrowIfFailed();
/* 227 */     return this;
/*     */   }
/*     */   
/*     */   private void rethrowIfFailed() {
/* 231 */     Throwable cause = cause();
/* 232 */     if (cause == null) {
/* 233 */       return;
/*     */     }
/*     */     
/* 236 */     PlatformDependent.throwException(cause);
/*     */   }
/*     */   
/*     */   public Promise<V> await() throws InterruptedException
/*     */   {
/* 241 */     if (isDone()) {
/* 242 */       return this;
/*     */     }
/*     */     
/* 245 */     if (Thread.interrupted()) {
/* 246 */       throw new InterruptedException(toString());
/*     */     }
/*     */     
/* 249 */     synchronized (this) {
/* 250 */       while (!isDone()) {
/* 251 */         checkDeadLock();
/* 252 */         incWaiters();
/*     */         try {
/* 254 */           wait();
/*     */         } finally {
/* 256 */           decWaiters();
/*     */         }
/*     */       }
/*     */     }
/* 260 */     return this;
/*     */   }
/*     */   
/*     */   public boolean await(long timeout, TimeUnit unit)
/*     */     throws InterruptedException
/*     */   {
/* 266 */     return await0(unit.toNanos(timeout), true);
/*     */   }
/*     */   
/*     */   public boolean await(long timeoutMillis) throws InterruptedException
/*     */   {
/* 271 */     return await0(TimeUnit.MILLISECONDS.toNanos(timeoutMillis), true);
/*     */   }
/*     */   
/*     */   public Promise<V> awaitUninterruptibly()
/*     */   {
/* 276 */     if (isDone()) {
/* 277 */       return this;
/*     */     }
/*     */     
/* 280 */     boolean interrupted = false;
/* 281 */     synchronized (this) {
/* 282 */       while (!isDone()) {
/* 283 */         checkDeadLock();
/* 284 */         incWaiters();
/*     */         try {
/* 286 */           wait();
/*     */         }
/*     */         catch (InterruptedException e) {
/* 289 */           interrupted = true;
/*     */         } finally {
/* 291 */           decWaiters();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 296 */     if (interrupted) {
/* 297 */       Thread.currentThread().interrupt();
/*     */     }
/*     */     
/* 300 */     return this;
/*     */   }
/*     */   
/*     */   public boolean awaitUninterruptibly(long timeout, TimeUnit unit)
/*     */   {
/*     */     try {
/* 306 */       return await0(unit.toNanos(timeout), false);
/*     */     }
/*     */     catch (InterruptedException e) {
/* 309 */       throw new InternalError();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean awaitUninterruptibly(long timeoutMillis)
/*     */   {
/*     */     try {
/* 316 */       return await0(TimeUnit.MILLISECONDS.toNanos(timeoutMillis), false);
/*     */     }
/*     */     catch (InterruptedException e) {
/* 319 */       throw new InternalError();
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
/*     */   private volatile Object result;
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
/*     */   private Object listeners;
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
/*     */   private DefaultPromise<V>.LateListeners lateListeners;
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
/*     */   private short waiters;
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
/*     */   protected void checkDeadLock()
/*     */   {
/* 388 */     EventExecutor e = executor();
/* 389 */     if ((e != null) && (e.inEventLoop())) {
/* 390 */       throw new BlockingOperationException(toString());
/*     */     }
/*     */   }
/*     */   
/*     */   public Promise<V> setSuccess(V result)
/*     */   {
/* 396 */     if (setSuccess0(result)) {
/* 397 */       notifyListeners();
/* 398 */       return this;
/*     */     }
/* 400 */     throw new IllegalStateException("complete already: " + this);
/*     */   }
/*     */   
/*     */   public boolean trySuccess(V result)
/*     */   {
/* 405 */     if (setSuccess0(result)) {
/* 406 */       notifyListeners();
/* 407 */       return true;
/*     */     }
/* 409 */     return false;
/*     */   }
/*     */   
/*     */   public Promise<V> setFailure(Throwable cause)
/*     */   {
/* 414 */     if (setFailure0(cause)) {
/* 415 */       notifyListeners();
/* 416 */       return this;
/*     */     }
/* 418 */     throw new IllegalStateException("complete already: " + this, cause);
/*     */   }
/*     */   
/*     */   public boolean tryFailure(Throwable cause)
/*     */   {
/* 423 */     if (setFailure0(cause)) {
/* 424 */       notifyListeners();
/* 425 */       return true;
/*     */     }
/* 427 */     return false;
/*     */   }
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning)
/*     */   {
/* 432 */     Object result = this.result;
/* 433 */     if ((isDone0(result)) || (result == UNCANCELLABLE)) {
/* 434 */       return false;
/*     */     }
/*     */     
/* 437 */     synchronized (this)
/*     */     {
/* 439 */       result = this.result;
/* 440 */       if ((isDone0(result)) || (result == UNCANCELLABLE)) {
/* 441 */         return false;
/*     */       }
/*     */       
/* 444 */       this.result = CANCELLATION_CAUSE_HOLDER;
/* 445 */       if (hasWaiters()) {
/* 446 */         notifyAll();
/*     */       }
/*     */     }
/*     */     
/* 450 */     notifyListeners();
/* 451 */     return true;
/*     */   }
/*     */   
/*     */   public boolean setUncancellable()
/*     */   {
/* 456 */     Object result = this.result;
/* 457 */     if (isDone0(result)) {
/* 458 */       return !isCancelled0(result);
/*     */     }
/*     */     
/* 461 */     synchronized (this)
/*     */     {
/* 463 */       result = this.result;
/* 464 */       if (isDone0(result)) {
/* 465 */         return !isCancelled0(result);
/*     */       }
/*     */       
/* 468 */       this.result = UNCANCELLABLE;
/*     */     }
/* 470 */     return true;
/*     */   }
/*     */   
/*     */   private boolean setFailure0(Throwable cause) {
/* 474 */     if (cause == null) {
/* 475 */       throw new NullPointerException("cause");
/*     */     }
/*     */     
/* 478 */     if (isDone()) {
/* 479 */       return false;
/*     */     }
/*     */     
/* 482 */     synchronized (this)
/*     */     {
/* 484 */       if (isDone()) {
/* 485 */         return false;
/*     */       }
/*     */       
/* 488 */       this.result = new CauseHolder(cause);
/* 489 */       if (hasWaiters()) {
/* 490 */         notifyAll();
/*     */       }
/*     */     }
/* 493 */     return true;
/*     */   }
/*     */   
/*     */   private boolean setSuccess0(V result) {
/* 497 */     if (isDone()) {
/* 498 */       return false;
/*     */     }
/*     */     
/* 501 */     synchronized (this)
/*     */     {
/* 503 */       if (isDone()) {
/* 504 */         return false;
/*     */       }
/* 506 */       if (result == null) {
/* 507 */         this.result = SUCCESS;
/*     */       } else {
/* 509 */         this.result = result;
/*     */       }
/* 511 */       if (hasWaiters()) {
/* 512 */         notifyAll();
/*     */       }
/*     */     }
/* 515 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public V getNow()
/*     */   {
/* 521 */     Object result = this.result;
/* 522 */     if (((result instanceof CauseHolder)) || (result == SUCCESS)) {
/* 523 */       return null;
/*     */     }
/* 525 */     return (V)result;
/*     */   }
/*     */   
/*     */   private boolean hasWaiters() {
/* 529 */     return this.waiters > 0;
/*     */   }
/*     */   
/*     */   private void incWaiters() {
/* 533 */     if (this.waiters == Short.MAX_VALUE) {
/* 534 */       throw new IllegalStateException("too many waiters: " + this);
/*     */     }
/* 536 */     this.waiters = ((short)(this.waiters + 1));
/*     */   }
/*     */   
/*     */   private void decWaiters() {
/* 540 */     this.waiters = ((short)(this.waiters - 1));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void notifyListeners()
/*     */   {
/* 550 */     Object listeners = this.listeners;
/* 551 */     if (listeners == null) {
/* 552 */       return;
/*     */     }
/*     */     
/* 555 */     EventExecutor executor = executor();
/* 556 */     if (executor.inEventLoop()) {
/* 557 */       InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
/* 558 */       int stackDepth = threadLocals.futureListenerStackDepth();
/* 559 */       if (stackDepth < 8) {
/* 560 */         threadLocals.setFutureListenerStackDepth(stackDepth + 1);
/*     */         try {
/* 562 */           if ((listeners instanceof DefaultFutureListeners)) {
/* 563 */             notifyListeners0(this, (DefaultFutureListeners)listeners);
/*     */           } else {
/* 565 */             GenericFutureListener<? extends Future<V>> l = (GenericFutureListener)listeners;
/*     */             
/* 567 */             notifyListener0(this, l);
/*     */           }
/*     */         } finally {
/* 570 */           this.listeners = null;
/* 571 */           threadLocals.setFutureListenerStackDepth(stackDepth);
/*     */         }
/* 573 */         return;
/*     */       }
/*     */     }
/*     */     
/* 577 */     if ((listeners instanceof DefaultFutureListeners)) {
/* 578 */       final DefaultFutureListeners dfl = (DefaultFutureListeners)listeners;
/* 579 */       execute(executor, new Runnable()
/*     */       {
/*     */         public void run() {
/* 582 */           DefaultPromise.notifyListeners0(DefaultPromise.this, dfl);
/* 583 */           DefaultPromise.this.listeners = null;
/*     */         }
/*     */       });
/*     */     } else {
/* 587 */       final GenericFutureListener<? extends Future<V>> l = (GenericFutureListener)listeners;
/*     */       
/* 589 */       execute(executor, new Runnable()
/*     */       {
/*     */         public void run() {
/* 592 */           DefaultPromise.notifyListener0(DefaultPromise.this, l);
/* 593 */           DefaultPromise.this.listeners = null;
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   private static void notifyListeners0(Future<?> future, DefaultFutureListeners listeners) {
/* 600 */     GenericFutureListener<?>[] a = listeners.listeners();
/* 601 */     int size = listeners.size();
/* 602 */     for (int i = 0; i < size; i++) {
/* 603 */       notifyListener0(future, a[i]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void notifyLateListener(GenericFutureListener<?> l)
/*     */   {
/* 613 */     EventExecutor executor = executor();
/* 614 */     if (executor.inEventLoop()) {
/* 615 */       if ((this.listeners == null) && (this.lateListeners == null)) {
/* 616 */         InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
/* 617 */         int stackDepth = threadLocals.futureListenerStackDepth();
/* 618 */         if (stackDepth < 8) {
/* 619 */           threadLocals.setFutureListenerStackDepth(stackDepth + 1);
/*     */           try {
/* 621 */             notifyListener0(this, l);
/*     */           } finally {
/* 623 */             threadLocals.setFutureListenerStackDepth(stackDepth);
/*     */           }
/* 625 */           return;
/*     */         }
/*     */       } else {
/* 628 */         DefaultPromise<V>.LateListeners lateListeners = this.lateListeners;
/* 629 */         if (lateListeners == null) {
/* 630 */           this.lateListeners = (lateListeners = new LateListeners());
/*     */         }
/* 632 */         lateListeners.add(l);
/* 633 */         execute(executor, lateListeners);
/* 634 */         return;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 641 */     execute(executor, new LateListenerNotifier(l));
/*     */   }
/*     */   
/*     */ 
/*     */   protected static void notifyListener(EventExecutor eventExecutor, Future<?> future, final GenericFutureListener<?> l)
/*     */   {
/* 647 */     if (eventExecutor.inEventLoop()) {
/* 648 */       InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
/* 649 */       int stackDepth = threadLocals.futureListenerStackDepth();
/* 650 */       if (stackDepth < 8) {
/* 651 */         threadLocals.setFutureListenerStackDepth(stackDepth + 1);
/*     */         try {
/* 653 */           notifyListener0(future, l);
/*     */         } finally {
/* 655 */           threadLocals.setFutureListenerStackDepth(stackDepth);
/*     */         }
/* 657 */         return;
/*     */       }
/*     */     }
/*     */     
/* 661 */     execute(eventExecutor, new Runnable()
/*     */     {
/*     */       public void run() {
/* 664 */         DefaultPromise.notifyListener0(this.val$future, l);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private static void execute(EventExecutor executor, Runnable task) {
/*     */     try {
/* 671 */       executor.execute(task);
/*     */     } catch (Throwable t) {
/* 673 */       rejectedExecutionLogger.error("Failed to submit a listener notification task. Event loop shut down?", t);
/*     */     }
/*     */   }
/*     */   
/*     */   static void notifyListener0(Future future, GenericFutureListener l)
/*     */   {
/*     */     try {
/* 680 */       l.operationComplete(future);
/*     */     } catch (Throwable t) {
/* 682 */       if (logger.isWarnEnabled()) {
/* 683 */         logger.warn("An exception was thrown by " + l.getClass().getName() + ".operationComplete()", t);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private synchronized Object progressiveListeners()
/*     */   {
/* 693 */     Object listeners = this.listeners;
/* 694 */     if (listeners == null)
/*     */     {
/* 696 */       return null;
/*     */     }
/*     */     
/* 699 */     if ((listeners instanceof DefaultFutureListeners))
/*     */     {
/* 701 */       DefaultFutureListeners dfl = (DefaultFutureListeners)listeners;
/* 702 */       int progressiveSize = dfl.progressiveSize();
/* 703 */       switch (progressiveSize) {
/*     */       case 0: 
/* 705 */         return null;
/*     */       case 1: 
/* 707 */         for (GenericFutureListener<?> l : dfl.listeners()) {
/* 708 */           if ((l instanceof GenericProgressiveFutureListener)) {
/* 709 */             return l;
/*     */           }
/*     */         }
/* 712 */         return null;
/*     */       }
/*     */       
/* 715 */       GenericFutureListener<?>[] array = dfl.listeners();
/* 716 */       GenericProgressiveFutureListener<?>[] copy = new GenericProgressiveFutureListener[progressiveSize];
/* 717 */       int i = 0; for (int j = 0; j < progressiveSize; i++) {
/* 718 */         GenericFutureListener<?> l = array[i];
/* 719 */         if ((l instanceof GenericProgressiveFutureListener)) {
/* 720 */           copy[(j++)] = ((GenericProgressiveFutureListener)l);
/*     */         }
/*     */       }
/*     */       
/* 724 */       return copy; }
/* 725 */     if ((listeners instanceof GenericProgressiveFutureListener)) {
/* 726 */       return listeners;
/*     */     }
/*     */     
/* 729 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   void notifyProgressiveListeners(final long progress, long total)
/*     */   {
/* 735 */     Object listeners = progressiveListeners();
/* 736 */     if (listeners == null) {
/* 737 */       return;
/*     */     }
/*     */     
/* 740 */     final ProgressiveFuture<V> self = (ProgressiveFuture)this;
/*     */     
/* 742 */     EventExecutor executor = executor();
/* 743 */     if (executor.inEventLoop()) {
/* 744 */       if ((listeners instanceof GenericProgressiveFutureListener[])) {
/* 745 */         notifyProgressiveListeners0(self, (GenericProgressiveFutureListener[])listeners, progress, total);
/*     */       }
/*     */       else {
/* 748 */         notifyProgressiveListener0(self, (GenericProgressiveFutureListener)listeners, progress, total);
/*     */       }
/*     */       
/*     */     }
/* 752 */     else if ((listeners instanceof GenericProgressiveFutureListener[])) {
/* 753 */       final GenericProgressiveFutureListener<?>[] array = (GenericProgressiveFutureListener[])listeners;
/*     */       
/* 755 */       execute(executor, new Runnable()
/*     */       {
/*     */         public void run() {
/* 758 */           DefaultPromise.notifyProgressiveListeners0(self, array, progress, this.val$total);
/*     */         }
/*     */       });
/*     */     } else {
/* 762 */       final GenericProgressiveFutureListener<ProgressiveFuture<V>> l = (GenericProgressiveFutureListener)listeners;
/*     */       
/* 764 */       execute(executor, new Runnable()
/*     */       {
/*     */         public void run() {
/* 767 */           DefaultPromise.notifyProgressiveListener0(self, l, progress, this.val$total);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static void notifyProgressiveListeners0(ProgressiveFuture<?> future, GenericProgressiveFutureListener<?>[] listeners, long progress, long total)
/*     */   {
/* 776 */     for (GenericProgressiveFutureListener<?> l : listeners) {
/* 777 */       if (l == null) {
/*     */         break;
/*     */       }
/* 780 */       notifyProgressiveListener0(future, l, progress, total);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void notifyProgressiveListener0(ProgressiveFuture future, GenericProgressiveFutureListener l, long progress, long total)
/*     */   {
/*     */     try
/*     */     {
/* 788 */       l.operationProgressed(future, progress, total);
/*     */     } catch (Throwable t) {
/* 790 */       if (logger.isWarnEnabled())
/* 791 */         logger.warn("An exception was thrown by " + l.getClass().getName() + ".operationProgressed()", t);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class CauseHolder {
/*     */     final Throwable cause;
/*     */     
/*     */     CauseHolder(Throwable cause) {
/* 799 */       this.cause = cause;
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 805 */     return toStringBuilder().toString();
/*     */   }
/*     */   
/*     */   protected StringBuilder toStringBuilder() {
/* 809 */     StringBuilder buf = new StringBuilder(64).append(StringUtil.simpleClassName(this)).append('@').append(Integer.toHexString(hashCode()));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 814 */     Object result = this.result;
/* 815 */     if (result == SUCCESS) {
/* 816 */       buf.append("(success)");
/* 817 */     } else if (result == UNCANCELLABLE) {
/* 818 */       buf.append("(uncancellable)");
/* 819 */     } else if ((result instanceof CauseHolder)) {
/* 820 */       buf.append("(failure(").append(((CauseHolder)result).cause).append(')');
/*     */     }
/*     */     else
/*     */     {
/* 824 */       buf.append("(incomplete)");
/*     */     }
/* 826 */     return buf;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   private boolean await0(long timeoutNanos, boolean interruptable)
/*     */     throws InterruptedException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual 98	io/netty/util/concurrent/DefaultPromise:isDone	()Z
/*     */     //   4: ifeq +5 -> 9
/*     */     //   7: iconst_1
/*     */     //   8: ireturn
/*     */     //   9: lload_1
/*     */     //   10: lconst_0
/*     */     //   11: lcmp
/*     */     //   12: ifgt +8 -> 20
/*     */     //   15: aload_0
/*     */     //   16: invokevirtual 98	io/netty/util/concurrent/DefaultPromise:isDone	()Z
/*     */     //   19: ireturn
/*     */     //   20: iload_3
/*     */     //   21: ifeq +21 -> 42
/*     */     //   24: invokestatic 166	java/lang/Thread:interrupted	()Z
/*     */     //   27: ifeq +15 -> 42
/*     */     //   30: new 143	java/lang/InterruptedException
/*     */     //   33: dup
/*     */     //   34: aload_0
/*     */     //   35: invokevirtual 170	io/netty/util/concurrent/DefaultPromise:toString	()Ljava/lang/String;
/*     */     //   38: invokespecial 171	java/lang/InterruptedException:<init>	(Ljava/lang/String;)V
/*     */     //   41: athrow
/*     */     //   42: invokestatic 222	java/lang/System:nanoTime	()J
/*     */     //   45: lstore 4
/*     */     //   47: lload_1
/*     */     //   48: lstore 6
/*     */     //   50: iconst_0
/*     */     //   51: istore 8
/*     */     //   53: aload_0
/*     */     //   54: dup
/*     */     //   55: astore 9
/*     */     //   57: monitorenter
/*     */     //   58: aload_0
/*     */     //   59: invokevirtual 98	io/netty/util/concurrent/DefaultPromise:isDone	()Z
/*     */     //   62: ifeq +23 -> 85
/*     */     //   65: iconst_1
/*     */     //   66: istore 10
/*     */     //   68: aload 9
/*     */     //   70: monitorexit
/*     */     //   71: iload 8
/*     */     //   73: ifeq +9 -> 82
/*     */     //   76: invokestatic 207	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   79: invokevirtual 210	java/lang/Thread:interrupt	()V
/*     */     //   82: iload 10
/*     */     //   84: ireturn
/*     */     //   85: lload 6
/*     */     //   87: lconst_0
/*     */     //   88: lcmp
/*     */     //   89: ifgt +26 -> 115
/*     */     //   92: aload_0
/*     */     //   93: invokevirtual 98	io/netty/util/concurrent/DefaultPromise:isDone	()Z
/*     */     //   96: istore 10
/*     */     //   98: aload 9
/*     */     //   100: monitorexit
/*     */     //   101: iload 8
/*     */     //   103: ifeq +9 -> 112
/*     */     //   106: invokestatic 207	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   109: invokevirtual 210	java/lang/Thread:interrupt	()V
/*     */     //   112: iload 10
/*     */     //   114: ireturn
/*     */     //   115: aload_0
/*     */     //   116: invokevirtual 174	io/netty/util/concurrent/DefaultPromise:checkDeadLock	()V
/*     */     //   119: aload_0
/*     */     //   120: invokespecial 177	io/netty/util/concurrent/DefaultPromise:incWaiters	()V
/*     */     //   123: aload_0
/*     */     //   124: lload 6
/*     */     //   126: ldc2_w 223
/*     */     //   129: ldiv
/*     */     //   130: lload 6
/*     */     //   132: ldc2_w 223
/*     */     //   135: lrem
/*     */     //   136: l2i
/*     */     //   137: invokevirtual 227	java/lang/Object:wait	(JI)V
/*     */     //   140: goto +15 -> 155
/*     */     //   143: astore 10
/*     */     //   145: iload_3
/*     */     //   146: ifeq +6 -> 152
/*     */     //   149: aload 10
/*     */     //   151: athrow
/*     */     //   152: iconst_1
/*     */     //   153: istore 8
/*     */     //   155: aload_0
/*     */     //   156: invokevirtual 98	io/netty/util/concurrent/DefaultPromise:isDone	()Z
/*     */     //   159: ifeq +27 -> 186
/*     */     //   162: iconst_1
/*     */     //   163: istore 10
/*     */     //   165: aload_0
/*     */     //   166: invokespecial 183	io/netty/util/concurrent/DefaultPromise:decWaiters	()V
/*     */     //   169: aload 9
/*     */     //   171: monitorexit
/*     */     //   172: iload 8
/*     */     //   174: ifeq +9 -> 183
/*     */     //   177: invokestatic 207	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   180: invokevirtual 210	java/lang/Thread:interrupt	()V
/*     */     //   183: iload 10
/*     */     //   185: ireturn
/*     */     //   186: lload_1
/*     */     //   187: invokestatic 222	java/lang/System:nanoTime	()J
/*     */     //   190: lload 4
/*     */     //   192: lsub
/*     */     //   193: lsub
/*     */     //   194: lstore 6
/*     */     //   196: lload 6
/*     */     //   198: lconst_0
/*     */     //   199: lcmp
/*     */     //   200: ifgt -77 -> 123
/*     */     //   203: aload_0
/*     */     //   204: invokevirtual 98	io/netty/util/concurrent/DefaultPromise:isDone	()Z
/*     */     //   207: istore 10
/*     */     //   209: aload_0
/*     */     //   210: invokespecial 183	io/netty/util/concurrent/DefaultPromise:decWaiters	()V
/*     */     //   213: aload 9
/*     */     //   215: monitorexit
/*     */     //   216: iload 8
/*     */     //   218: ifeq +9 -> 227
/*     */     //   221: invokestatic 207	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   224: invokevirtual 210	java/lang/Thread:interrupt	()V
/*     */     //   227: iload 10
/*     */     //   229: ireturn
/*     */     //   230: astore 11
/*     */     //   232: aload_0
/*     */     //   233: invokespecial 183	io/netty/util/concurrent/DefaultPromise:decWaiters	()V
/*     */     //   236: aload 11
/*     */     //   238: athrow
/*     */     //   239: astore 12
/*     */     //   241: aload 9
/*     */     //   243: monitorexit
/*     */     //   244: aload 12
/*     */     //   246: athrow
/*     */     //   247: astore 13
/*     */     //   249: iload 8
/*     */     //   251: ifeq +9 -> 260
/*     */     //   254: invokestatic 207	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   257: invokevirtual 210	java/lang/Thread:interrupt	()V
/*     */     //   260: aload 13
/*     */     //   262: athrow
/*     */     // Line number table:
/*     */     //   Java source line #324	-> byte code offset #0
/*     */     //   Java source line #325	-> byte code offset #7
/*     */     //   Java source line #328	-> byte code offset #9
/*     */     //   Java source line #329	-> byte code offset #15
/*     */     //   Java source line #332	-> byte code offset #20
/*     */     //   Java source line #333	-> byte code offset #30
/*     */     //   Java source line #336	-> byte code offset #42
/*     */     //   Java source line #337	-> byte code offset #47
/*     */     //   Java source line #338	-> byte code offset #50
/*     */     //   Java source line #341	-> byte code offset #53
/*     */     //   Java source line #342	-> byte code offset #58
/*     */     //   Java source line #343	-> byte code offset #65
/*     */     //   Java source line #378	-> byte code offset #71
/*     */     //   Java source line #379	-> byte code offset #76
/*     */     //   Java source line #346	-> byte code offset #85
/*     */     //   Java source line #347	-> byte code offset #92
/*     */     //   Java source line #378	-> byte code offset #101
/*     */     //   Java source line #379	-> byte code offset #106
/*     */     //   Java source line #350	-> byte code offset #115
/*     */     //   Java source line #351	-> byte code offset #119
/*     */     //   Java source line #355	-> byte code offset #123
/*     */     //   Java source line #362	-> byte code offset #140
/*     */     //   Java source line #356	-> byte code offset #143
/*     */     //   Java source line #357	-> byte code offset #145
/*     */     //   Java source line #358	-> byte code offset #149
/*     */     //   Java source line #360	-> byte code offset #152
/*     */     //   Java source line #364	-> byte code offset #155
/*     */     //   Java source line #365	-> byte code offset #162
/*     */     //   Java source line #374	-> byte code offset #165
/*     */     //   Java source line #378	-> byte code offset #172
/*     */     //   Java source line #379	-> byte code offset #177
/*     */     //   Java source line #367	-> byte code offset #186
/*     */     //   Java source line #368	-> byte code offset #196
/*     */     //   Java source line #369	-> byte code offset #203
/*     */     //   Java source line #374	-> byte code offset #209
/*     */     //   Java source line #378	-> byte code offset #216
/*     */     //   Java source line #379	-> byte code offset #221
/*     */     //   Java source line #374	-> byte code offset #230
/*     */     //   Java source line #376	-> byte code offset #239
/*     */     //   Java source line #378	-> byte code offset #247
/*     */     //   Java source line #379	-> byte code offset #254
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	263	0	this	DefaultPromise<V>
/*     */     //   0	263	1	timeoutNanos	long
/*     */     //   0	263	3	interruptable	boolean
/*     */     //   45	146	4	startTime	long
/*     */     //   48	149	6	waitTime	long
/*     */     //   51	199	8	interrupted	boolean
/*     */     //   66	47	10	bool1	boolean
/*     */     //   143	85	10	e	InterruptedException
/*     */     //   163	65	10	bool2	boolean
/*     */     //   230	7	11	localObject1	Object
/*     */     //   239	6	12	localObject2	Object
/*     */     //   247	14	13	localObject3	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   123	140	143	java/lang/InterruptedException
/*     */     //   123	165	230	finally
/*     */     //   186	209	230	finally
/*     */     //   230	232	230	finally
/*     */     //   58	71	239	finally
/*     */     //   85	101	239	finally
/*     */     //   115	172	239	finally
/*     */     //   186	216	239	finally
/*     */     //   230	244	239	finally
/*     */     //   53	71	247	finally
/*     */     //   85	101	247	finally
/*     */     //   115	172	247	finally
/*     */     //   186	216	247	finally
/*     */     //   230	249	247	finally
/*     */   }
/*     */   
/*     */   private final class LateListeners
/*     */     extends ArrayDeque<GenericFutureListener<?>>
/*     */     implements Runnable
/*     */   {
/*     */     private static final long serialVersionUID = -687137418080392244L;
/*     */     
/*     */     LateListeners()
/*     */     {
/* 834 */       super();
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 839 */       if (DefaultPromise.this.listeners == null) {
/*     */         for (;;) {
/* 841 */           GenericFutureListener<?> l = (GenericFutureListener)poll();
/* 842 */           if (l == null) {
/*     */             break;
/*     */           }
/* 845 */           DefaultPromise.notifyListener0(DefaultPromise.this, l);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 850 */       DefaultPromise.execute(DefaultPromise.this.executor(), this);
/*     */     }
/*     */   }
/*     */   
/*     */   private final class LateListenerNotifier implements Runnable
/*     */   {
/*     */     private GenericFutureListener<?> l;
/*     */     
/*     */     LateListenerNotifier() {
/* 859 */       this.l = l;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 864 */       DefaultPromise<V>.LateListeners lateListeners = DefaultPromise.this.lateListeners;
/* 865 */       if (this.l != null) {
/* 866 */         if (lateListeners == null) {
/* 867 */           DefaultPromise.this.lateListeners = (lateListeners = new DefaultPromise.LateListeners(DefaultPromise.this));
/*     */         }
/* 869 */         lateListeners.add(this.l);
/* 870 */         this.l = null;
/*     */       }
/*     */       
/* 873 */       lateListeners.run();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\concurrent\DefaultPromise.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */