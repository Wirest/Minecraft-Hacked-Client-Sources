/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SingleThreadEventExecutor
/*     */   extends AbstractScheduledEventExecutor
/*     */ {
/*     */   private static final InternalLogger logger;
/*     */   private static final int ST_NOT_STARTED = 1;
/*     */   private static final int ST_STARTED = 2;
/*     */   private static final int ST_SHUTTING_DOWN = 3;
/*     */   private static final int ST_SHUTDOWN = 4;
/*     */   private static final int ST_TERMINATED = 5;
/*     */   private static final Runnable WAKEUP_TASK;
/*     */   private static final AtomicIntegerFieldUpdater<SingleThreadEventExecutor> STATE_UPDATER;
/*     */   private static final AtomicReferenceFieldUpdater<SingleThreadEventExecutor, Thread> THREAD_UPDATER;
/*     */   private final Queue<Runnable> taskQueue;
/*     */   private volatile Thread thread;
/*     */   private final Executor executor;
/*     */   
/*     */   static
/*     */   {
/*  43 */     logger = InternalLoggerFactory.getInstance(SingleThreadEventExecutor.class);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  52 */     WAKEUP_TASK = new Runnable()
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       public void run() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  62 */     };
/*  63 */     AtomicIntegerFieldUpdater<SingleThreadEventExecutor> updater = PlatformDependent.newAtomicIntegerFieldUpdater(SingleThreadEventExecutor.class, "state");
/*     */     
/*  65 */     if (updater == null) {
/*  66 */       updater = AtomicIntegerFieldUpdater.newUpdater(SingleThreadEventExecutor.class, "state");
/*     */     }
/*  68 */     STATE_UPDATER = updater;
/*     */     
/*  70 */     AtomicReferenceFieldUpdater<SingleThreadEventExecutor, Thread> refUpdater = PlatformDependent.newAtomicReferenceFieldUpdater(SingleThreadEventExecutor.class, "thread");
/*     */     
/*  72 */     if (refUpdater == null) {
/*  73 */       refUpdater = AtomicReferenceFieldUpdater.newUpdater(SingleThreadEventExecutor.class, Thread.class, "thread");
/*     */     }
/*     */     
/*  76 */     THREAD_UPDATER = refUpdater;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  84 */   private final Semaphore threadLock = new Semaphore(0);
/*  85 */   private final Set<Runnable> shutdownHooks = new LinkedHashSet();
/*     */   
/*     */   private final boolean addTaskWakesUp;
/*     */   
/*     */   private long lastExecutionTime;
/*  90 */   private volatile int state = 1;
/*     */   
/*     */   private volatile long gracefulShutdownQuietPeriod;
/*     */   
/*     */   private volatile long gracefulShutdownTimeout;
/*     */   
/*     */   private long gracefulShutdownStartTime;
/*  97 */   private final Promise<?> terminationFuture = new DefaultPromise(GlobalEventExecutor.INSTANCE);
/*     */   
/*  99 */   private boolean firstRun = true;
/*     */   
/* 101 */   private final Runnable asRunnable = new Runnable()
/*     */   {
/*     */     public void run() {
/* 104 */       SingleThreadEventExecutor.this.updateThread(Thread.currentThread());
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 110 */       if (SingleThreadEventExecutor.this.firstRun) {
/* 111 */         SingleThreadEventExecutor.this.firstRun = false;
/* 112 */         SingleThreadEventExecutor.this.updateLastExecutionTime();
/*     */       }
/*     */       try
/*     */       {
/* 116 */         SingleThreadEventExecutor.this.run();
/*     */       } catch (Throwable t) {
/* 118 */         SingleThreadEventExecutor.logger.warn("Unexpected exception from an event executor: ", t);
/* 119 */         SingleThreadEventExecutor.this.cleanupAndTerminate(false);
/*     */       }
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SingleThreadEventExecutor(EventExecutorGroup parent, Executor executor, boolean addTaskWakesUp)
/*     */   {
/* 131 */     super(parent);
/*     */     
/* 133 */     if (executor == null) {
/* 134 */       throw new NullPointerException("executor");
/*     */     }
/*     */     
/* 137 */     this.addTaskWakesUp = addTaskWakesUp;
/* 138 */     this.executor = executor;
/* 139 */     this.taskQueue = newTaskQueue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Queue<Runnable> newTaskQueue()
/*     */   {
/* 149 */     return new LinkedBlockingQueue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Runnable pollTask()
/*     */   {
/* 156 */     assert (inEventLoop());
/*     */     Runnable task;
/* 158 */     do { task = (Runnable)this.taskQueue.poll();
/* 159 */     } while (task == WAKEUP_TASK);
/*     */     
/*     */ 
/* 162 */     return task;
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
/*     */   protected Runnable takeTask()
/*     */   {
/* 176 */     assert (inEventLoop());
/* 177 */     if (!(this.taskQueue instanceof BlockingQueue)) {
/* 178 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/* 181 */     BlockingQueue<Runnable> taskQueue = (BlockingQueue)this.taskQueue;
/*     */     for (;;) {
/* 183 */       ScheduledFutureTask<?> scheduledTask = peekScheduledTask();
/* 184 */       if (scheduledTask == null) {
/* 185 */         Runnable task = null;
/*     */         try {
/* 187 */           task = (Runnable)taskQueue.take();
/* 188 */           if (task == WAKEUP_TASK) {
/* 189 */             task = null;
/*     */           }
/*     */         }
/*     */         catch (InterruptedException e) {}
/*     */         
/* 194 */         return task;
/*     */       }
/* 196 */       long delayNanos = scheduledTask.delayNanos();
/* 197 */       Runnable task = null;
/* 198 */       if (delayNanos > 0L) {
/*     */         try {
/* 200 */           task = (Runnable)taskQueue.poll(delayNanos, TimeUnit.NANOSECONDS);
/*     */         }
/*     */         catch (InterruptedException e) {
/* 203 */           return null;
/*     */         }
/*     */       }
/* 206 */       if (task == null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 211 */         fetchFromScheduledTaskQueue();
/* 212 */         task = (Runnable)taskQueue.poll();
/*     */       }
/*     */       
/* 215 */       if (task != null) {
/* 216 */         return task;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void fetchFromScheduledTaskQueue()
/*     */   {
/* 223 */     if (hasScheduledTasks()) {
/* 224 */       long nanoTime = AbstractScheduledEventExecutor.nanoTime();
/*     */       for (;;) {
/* 226 */         Runnable scheduledTask = pollScheduledTask(nanoTime);
/* 227 */         if (scheduledTask == null) {
/*     */           break;
/*     */         }
/* 230 */         this.taskQueue.add(scheduledTask);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Runnable peekTask()
/*     */   {
/* 239 */     assert (inEventLoop());
/* 240 */     return (Runnable)this.taskQueue.peek();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean hasTasks()
/*     */   {
/* 247 */     assert (inEventLoop());
/* 248 */     return !this.taskQueue.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int pendingTasks()
/*     */   {
/* 258 */     return this.taskQueue.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addTask(Runnable task)
/*     */   {
/* 266 */     if (task == null) {
/* 267 */       throw new NullPointerException("task");
/*     */     }
/* 269 */     if (isShutdown()) {
/* 270 */       reject();
/*     */     }
/* 272 */     this.taskQueue.add(task);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean removeTask(Runnable task)
/*     */   {
/* 279 */     if (task == null) {
/* 280 */       throw new NullPointerException("task");
/*     */     }
/* 282 */     return this.taskQueue.remove(task);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean runAllTasks()
/*     */   {
/* 291 */     fetchFromScheduledTaskQueue();
/* 292 */     Runnable task = pollTask();
/* 293 */     if (task == null) {
/* 294 */       return false;
/*     */     }
/*     */     do
/*     */     {
/*     */       try {
/* 299 */         task.run();
/*     */       } catch (Throwable t) {
/* 301 */         logger.warn("A task raised an exception.", t);
/*     */       }
/*     */       
/* 304 */       task = pollTask();
/* 305 */     } while (task != null);
/* 306 */     this.lastExecutionTime = ScheduledFutureTask.nanoTime();
/* 307 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean runAllTasks(long timeoutNanos)
/*     */   {
/* 317 */     fetchFromScheduledTaskQueue();
/* 318 */     Runnable task = pollTask();
/* 319 */     if (task == null) {
/* 320 */       return false;
/*     */     }
/*     */     
/* 323 */     long deadline = ScheduledFutureTask.nanoTime() + timeoutNanos;
/* 324 */     long runTasks = 0L;
/*     */     do
/*     */     {
/*     */       try {
/* 328 */         task.run();
/*     */       } catch (Throwable t) {
/* 330 */         logger.warn("A task raised an exception.", t);
/*     */       }
/*     */       
/* 333 */       runTasks += 1L;
/*     */       
/*     */ 
/*     */ 
/* 337 */       if ((runTasks & 0x3F) == 0L) {
/* 338 */         long lastExecutionTime = ScheduledFutureTask.nanoTime();
/* 339 */         if (lastExecutionTime >= deadline) {
/*     */           break;
/*     */         }
/*     */       }
/*     */       
/* 344 */       task = pollTask();
/* 345 */     } while (task != null);
/* 346 */     long lastExecutionTime = ScheduledFutureTask.nanoTime();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 351 */     this.lastExecutionTime = lastExecutionTime;
/* 352 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected long delayNanos(long currentTimeNanos)
/*     */   {
/* 359 */     ScheduledFutureTask<?> scheduledTask = peekScheduledTask();
/* 360 */     if (scheduledTask == null) {
/* 361 */       return SCHEDULE_PURGE_INTERVAL;
/*     */     }
/*     */     
/* 364 */     return scheduledTask.delayNanos(currentTimeNanos);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void updateLastExecutionTime()
/*     */   {
/* 375 */     this.lastExecutionTime = ScheduledFutureTask.nanoTime();
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
/*     */   protected void wakeup(boolean inEventLoop)
/*     */   {
/* 391 */     if ((!inEventLoop) || (STATE_UPDATER.get(this) == 3)) {
/* 392 */       this.taskQueue.add(WAKEUP_TASK);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean inEventLoop(Thread thread)
/*     */   {
/* 398 */     return thread == this.thread;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addShutdownHook(final Runnable task)
/*     */   {
/* 405 */     if (inEventLoop()) {
/* 406 */       this.shutdownHooks.add(task);
/*     */     } else {
/* 408 */       execute(new Runnable()
/*     */       {
/*     */         public void run() {
/* 411 */           SingleThreadEventExecutor.this.shutdownHooks.add(task);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void removeShutdownHook(final Runnable task)
/*     */   {
/* 421 */     if (inEventLoop()) {
/* 422 */       this.shutdownHooks.remove(task);
/*     */     } else {
/* 424 */       execute(new Runnable()
/*     */       {
/*     */         public void run() {
/* 427 */           SingleThreadEventExecutor.this.shutdownHooks.remove(task);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean runShutdownHooks() {
/* 434 */     boolean ran = false;
/*     */     
/* 436 */     while (!this.shutdownHooks.isEmpty()) {
/* 437 */       List<Runnable> copy = new ArrayList(this.shutdownHooks);
/* 438 */       this.shutdownHooks.clear();
/* 439 */       for (Runnable task : copy) {
/*     */         try {
/* 441 */           task.run();
/*     */         } catch (Throwable t) {
/* 443 */           logger.warn("Shutdown hook raised an exception.", t);
/*     */         } finally {
/* 445 */           ran = true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 450 */     if (ran) {
/* 451 */       this.lastExecutionTime = ScheduledFutureTask.nanoTime();
/*     */     }
/*     */     
/* 454 */     return ran;
/*     */   }
/*     */   
/*     */   public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit)
/*     */   {
/* 459 */     if (quietPeriod < 0L) {
/* 460 */       throw new IllegalArgumentException("quietPeriod: " + quietPeriod + " (expected >= 0)");
/*     */     }
/* 462 */     if (timeout < quietPeriod) {
/* 463 */       throw new IllegalArgumentException("timeout: " + timeout + " (expected >= quietPeriod (" + quietPeriod + "))");
/*     */     }
/*     */     
/* 466 */     if (unit == null) {
/* 467 */       throw new NullPointerException("unit");
/*     */     }
/*     */     
/* 470 */     if (isShuttingDown()) {
/* 471 */       return terminationFuture();
/*     */     }
/*     */     
/* 474 */     boolean inEventLoop = inEventLoop();
/*     */     boolean wakeup;
/*     */     int oldState;
/*     */     for (;;) {
/* 478 */       if (isShuttingDown()) {
/* 479 */         return terminationFuture();
/*     */       }
/*     */       
/* 482 */       wakeup = true;
/* 483 */       oldState = STATE_UPDATER.get(this);
/* 484 */       int newState; int newState; if (inEventLoop) {
/* 485 */         newState = 3;
/*     */       } else {
/* 487 */         switch (oldState) {
/*     */         case 1: 
/*     */         case 2: 
/* 490 */           newState = 3;
/* 491 */           break;
/*     */         default: 
/* 493 */           newState = oldState;
/* 494 */           wakeup = false;
/*     */         }
/*     */       }
/* 497 */       if (STATE_UPDATER.compareAndSet(this, oldState, newState)) {
/*     */         break;
/*     */       }
/*     */     }
/* 501 */     this.gracefulShutdownQuietPeriod = unit.toNanos(quietPeriod);
/* 502 */     this.gracefulShutdownTimeout = unit.toNanos(timeout);
/*     */     
/* 504 */     if (oldState == 1) {
/* 505 */       scheduleExecution();
/*     */     }
/*     */     
/* 508 */     if (wakeup) {
/* 509 */       wakeup(inEventLoop);
/*     */     }
/*     */     
/* 512 */     return terminationFuture();
/*     */   }
/*     */   
/*     */   public Future<?> terminationFuture()
/*     */   {
/* 517 */     return this.terminationFuture;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void shutdown()
/*     */   {
/* 523 */     if (isShutdown()) {
/* 524 */       return;
/*     */     }
/*     */     
/* 527 */     boolean inEventLoop = inEventLoop();
/*     */     boolean wakeup;
/*     */     int oldState;
/*     */     for (;;) {
/* 531 */       if (isShuttingDown()) {
/* 532 */         return;
/*     */       }
/*     */       
/* 535 */       wakeup = true;
/* 536 */       oldState = STATE_UPDATER.get(this);
/* 537 */       int newState; int newState; if (inEventLoop) {
/* 538 */         newState = 4;
/*     */       } else {
/* 540 */         switch (oldState) {
/*     */         case 1: 
/*     */         case 2: 
/*     */         case 3: 
/* 544 */           newState = 4;
/* 545 */           break;
/*     */         default: 
/* 547 */           newState = oldState;
/* 548 */           wakeup = false;
/*     */         }
/*     */       }
/* 551 */       if (STATE_UPDATER.compareAndSet(this, oldState, newState)) {
/*     */         break;
/*     */       }
/*     */     }
/*     */     
/* 556 */     if (oldState == 1) {
/* 557 */       scheduleExecution();
/*     */     }
/*     */     
/* 560 */     if (wakeup) {
/* 561 */       wakeup(inEventLoop);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isShuttingDown()
/*     */   {
/* 567 */     return STATE_UPDATER.get(this) >= 3;
/*     */   }
/*     */   
/*     */   public boolean isShutdown()
/*     */   {
/* 572 */     return STATE_UPDATER.get(this) >= 4;
/*     */   }
/*     */   
/*     */   public boolean isTerminated()
/*     */   {
/* 577 */     return STATE_UPDATER.get(this) == 5;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean confirmShutdown()
/*     */   {
/* 584 */     if (!isShuttingDown()) {
/* 585 */       return false;
/*     */     }
/*     */     
/* 588 */     if (!inEventLoop()) {
/* 589 */       throw new IllegalStateException("must be invoked from an event loop");
/*     */     }
/*     */     
/* 592 */     cancelScheduledTasks();
/*     */     
/* 594 */     if (this.gracefulShutdownStartTime == 0L) {
/* 595 */       this.gracefulShutdownStartTime = ScheduledFutureTask.nanoTime();
/*     */     }
/*     */     
/* 598 */     if ((runAllTasks()) || (runShutdownHooks())) {
/* 599 */       if (isShutdown())
/*     */       {
/* 601 */         return true;
/*     */       }
/*     */       
/*     */ 
/* 605 */       wakeup(true);
/* 606 */       return false;
/*     */     }
/*     */     
/* 609 */     long nanoTime = ScheduledFutureTask.nanoTime();
/*     */     
/* 611 */     if ((isShutdown()) || (nanoTime - this.gracefulShutdownStartTime > this.gracefulShutdownTimeout)) {
/* 612 */       return true;
/*     */     }
/*     */     
/* 615 */     if (nanoTime - this.lastExecutionTime <= this.gracefulShutdownQuietPeriod)
/*     */     {
/*     */ 
/* 618 */       wakeup(true);
/*     */       try {
/* 620 */         Thread.sleep(100L);
/*     */       }
/*     */       catch (InterruptedException e) {}
/*     */       
/*     */ 
/* 625 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 630 */     return true;
/*     */   }
/*     */   
/*     */   public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException
/*     */   {
/* 635 */     if (unit == null) {
/* 636 */       throw new NullPointerException("unit");
/*     */     }
/*     */     
/* 639 */     if (inEventLoop()) {
/* 640 */       throw new IllegalStateException("cannot await termination of the current thread");
/*     */     }
/*     */     
/* 643 */     if (this.threadLock.tryAcquire(timeout, unit)) {
/* 644 */       this.threadLock.release();
/*     */     }
/*     */     
/* 647 */     return isTerminated();
/*     */   }
/*     */   
/*     */   public void execute(Runnable task)
/*     */   {
/* 652 */     if (task == null) {
/* 653 */       throw new NullPointerException("task");
/*     */     }
/*     */     
/* 656 */     boolean inEventLoop = inEventLoop();
/* 657 */     if (inEventLoop) {
/* 658 */       addTask(task);
/*     */     } else {
/* 660 */       startExecution();
/* 661 */       addTask(task);
/* 662 */       if ((isShutdown()) && (removeTask(task))) {
/* 663 */         reject();
/*     */       }
/*     */     }
/*     */     
/* 667 */     if ((!this.addTaskWakesUp) && (wakesUpForTask(task))) {
/* 668 */       wakeup(inEventLoop);
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean wakesUpForTask(Runnable task)
/*     */   {
/* 674 */     return true;
/*     */   }
/*     */   
/*     */   protected static void reject() {
/* 678 */     throw new RejectedExecutionException("event executor terminated");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 683 */   private static final long SCHEDULE_PURGE_INTERVAL = TimeUnit.SECONDS.toNanos(1L);
/*     */   
/*     */   protected void cleanupAndTerminate(boolean success) {
/*     */     for (;;) {
/* 687 */       int oldState = STATE_UPDATER.get(this);
/* 688 */       if ((oldState >= 3) || (STATE_UPDATER.compareAndSet(this, oldState, 3))) {
/*     */         break;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 695 */     if ((success) && (this.gracefulShutdownStartTime == 0L)) {
/* 696 */       logger.error("Buggy " + EventExecutor.class.getSimpleName() + " implementation; " + SingleThreadEventExecutor.class.getSimpleName() + ".confirmShutdown() must be called " + "before run() implementation terminates.");
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/*     */       for (;;)
/*     */       {
/* 704 */         if (confirmShutdown()) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     } finally {
/*     */       try {
/* 710 */         cleanup();
/*     */       } finally {
/* 712 */         STATE_UPDATER.set(this, 5);
/* 713 */         this.threadLock.release();
/* 714 */         if (!this.taskQueue.isEmpty()) {
/* 715 */           logger.warn("An event executor terminated with non-empty task queue (" + this.taskQueue.size() + ')');
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 720 */         this.firstRun = true;
/* 721 */         this.terminationFuture.setSuccess(null);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void startExecution() {
/* 727 */     if ((STATE_UPDATER.get(this) == 1) && 
/* 728 */       (STATE_UPDATER.compareAndSet(this, 1, 2))) {
/* 729 */       schedule(new ScheduledFutureTask(this, Executors.callable(new PurgeTask(null), null), ScheduledFutureTask.deadlineNanos(SCHEDULE_PURGE_INTERVAL), -SCHEDULE_PURGE_INTERVAL));
/*     */       
/*     */ 
/* 732 */       scheduleExecution();
/*     */     }
/*     */   }
/*     */   
/*     */   protected final void scheduleExecution()
/*     */   {
/* 738 */     updateThread(null);
/* 739 */     this.executor.execute(this.asRunnable);
/*     */   }
/*     */   
/*     */ 
/* 743 */   private void updateThread(Thread t) { THREAD_UPDATER.lazySet(this, t); }
/*     */   
/*     */   protected abstract void run();
/*     */   
/*     */   private final class PurgeTask implements Runnable { private PurgeTask() {}
/*     */     
/* 749 */     public void run() { SingleThreadEventExecutor.this.purgeCancelledScheduledTasks(); }
/*     */   }
/*     */   
/*     */   protected void cleanup() {}
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\concurrent\SingleThreadEventExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */