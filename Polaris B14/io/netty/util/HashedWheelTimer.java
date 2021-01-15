/*     */ package io.netty.util;
/*     */ 
/*     */ import io.netty.util.internal.MpscLinkedQueueNode;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
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
/*     */ public class HashedWheelTimer
/*     */   implements Timer
/*     */ {
/*  77 */   static final InternalLogger logger = InternalLoggerFactory.getInstance(HashedWheelTimer.class);
/*     */   
/*     */ 
/*  80 */   private static final ResourceLeakDetector<HashedWheelTimer> leakDetector = new ResourceLeakDetector(HashedWheelTimer.class, 1, Runtime.getRuntime().availableProcessors() * 4);
/*     */   private static final AtomicIntegerFieldUpdater<HashedWheelTimer> WORKER_STATE_UPDATER;
/*     */   private final ResourceLeak leak;
/*     */   
/*     */   static
/*     */   {
/*  86 */     AtomicIntegerFieldUpdater<HashedWheelTimer> workerStateUpdater = PlatformDependent.newAtomicIntegerFieldUpdater(HashedWheelTimer.class, "workerState");
/*     */     
/*  88 */     if (workerStateUpdater == null) {
/*  89 */       workerStateUpdater = AtomicIntegerFieldUpdater.newUpdater(HashedWheelTimer.class, "workerState");
/*     */     }
/*  91 */     WORKER_STATE_UPDATER = workerStateUpdater;
/*     */   }
/*     */   
/*     */ 
/*  95 */   private final Worker worker = new Worker(null);
/*     */   
/*     */   private final Thread workerThread;
/*     */   public static final int WORKER_STATE_INIT = 0;
/*     */   public static final int WORKER_STATE_STARTED = 1;
/*     */   public static final int WORKER_STATE_SHUTDOWN = 2;
/* 101 */   private volatile int workerState = 0;
/*     */   
/*     */   private final long tickDuration;
/*     */   
/*     */   private final HashedWheelBucket[] wheel;
/*     */   private final int mask;
/* 107 */   private final CountDownLatch startTimeInitialized = new CountDownLatch(1);
/* 108 */   private final Queue<HashedWheelTimeout> timeouts = PlatformDependent.newMpscQueue();
/* 109 */   private final Queue<Runnable> cancelledTimeouts = PlatformDependent.newMpscQueue();
/*     */   
/*     */ 
/*     */ 
/*     */   private volatile long startTime;
/*     */   
/*     */ 
/*     */ 
/*     */   public HashedWheelTimer()
/*     */   {
/* 119 */     this(Executors.defaultThreadFactory());
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
/*     */   public HashedWheelTimer(long tickDuration, TimeUnit unit)
/*     */   {
/* 133 */     this(Executors.defaultThreadFactory(), tickDuration, unit);
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
/*     */   public HashedWheelTimer(long tickDuration, TimeUnit unit, int ticksPerWheel)
/*     */   {
/* 147 */     this(Executors.defaultThreadFactory(), tickDuration, unit, ticksPerWheel);
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
/*     */   public HashedWheelTimer(ThreadFactory threadFactory)
/*     */   {
/* 160 */     this(threadFactory, 100L, TimeUnit.MILLISECONDS);
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
/*     */   public HashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit)
/*     */   {
/* 176 */     this(threadFactory, tickDuration, unit, 512);
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
/*     */   public HashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit, int ticksPerWheel)
/*     */   {
/* 195 */     if (threadFactory == null) {
/* 196 */       throw new NullPointerException("threadFactory");
/*     */     }
/* 198 */     if (unit == null) {
/* 199 */       throw new NullPointerException("unit");
/*     */     }
/* 201 */     if (tickDuration <= 0L) {
/* 202 */       throw new IllegalArgumentException("tickDuration must be greater than 0: " + tickDuration);
/*     */     }
/* 204 */     if (ticksPerWheel <= 0) {
/* 205 */       throw new IllegalArgumentException("ticksPerWheel must be greater than 0: " + ticksPerWheel);
/*     */     }
/*     */     
/*     */ 
/* 209 */     this.wheel = createWheel(ticksPerWheel);
/* 210 */     this.mask = (this.wheel.length - 1);
/*     */     
/*     */ 
/* 213 */     this.tickDuration = unit.toNanos(tickDuration);
/*     */     
/*     */ 
/* 216 */     if (this.tickDuration >= Long.MAX_VALUE / this.wheel.length) {
/* 217 */       throw new IllegalArgumentException(String.format("tickDuration: %d (expected: 0 < tickDuration in nanos < %d", new Object[] { Long.valueOf(tickDuration), Long.valueOf(Long.MAX_VALUE / this.wheel.length) }));
/*     */     }
/*     */     
/*     */ 
/* 221 */     this.workerThread = threadFactory.newThread(this.worker);
/*     */     
/* 223 */     this.leak = leakDetector.open(this);
/*     */   }
/*     */   
/*     */   private static HashedWheelBucket[] createWheel(int ticksPerWheel) {
/* 227 */     if (ticksPerWheel <= 0) {
/* 228 */       throw new IllegalArgumentException("ticksPerWheel must be greater than 0: " + ticksPerWheel);
/*     */     }
/*     */     
/* 231 */     if (ticksPerWheel > 1073741824) {
/* 232 */       throw new IllegalArgumentException("ticksPerWheel may not be greater than 2^30: " + ticksPerWheel);
/*     */     }
/*     */     
/*     */ 
/* 236 */     ticksPerWheel = normalizeTicksPerWheel(ticksPerWheel);
/* 237 */     HashedWheelBucket[] wheel = new HashedWheelBucket[ticksPerWheel];
/* 238 */     for (int i = 0; i < wheel.length; i++) {
/* 239 */       wheel[i] = new HashedWheelBucket(null);
/*     */     }
/* 241 */     return wheel;
/*     */   }
/*     */   
/*     */   private static int normalizeTicksPerWheel(int ticksPerWheel) {
/* 245 */     int normalizedTicksPerWheel = 1;
/* 246 */     while (normalizedTicksPerWheel < ticksPerWheel) {
/* 247 */       normalizedTicksPerWheel <<= 1;
/*     */     }
/* 249 */     return normalizedTicksPerWheel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void start()
/*     */   {
/* 260 */     switch (WORKER_STATE_UPDATER.get(this)) {
/*     */     case 0: 
/* 262 */       if (WORKER_STATE_UPDATER.compareAndSet(this, 0, 1)) {
/* 263 */         this.workerThread.start();
/*     */       }
/*     */       break;
/*     */     case 1: 
/*     */       break;
/*     */     case 2: 
/* 269 */       throw new IllegalStateException("cannot be started once stopped");
/*     */     default: 
/* 271 */       throw new Error("Invalid WorkerState");
/*     */     }
/*     */     
/*     */     
/* 275 */     while (this.startTime == 0L) {
/*     */       try {
/* 277 */         this.startTimeInitialized.await();
/*     */       }
/*     */       catch (InterruptedException ignore) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public Set<Timeout> stop()
/*     */   {
/* 286 */     if (Thread.currentThread() == this.workerThread) {
/* 287 */       throw new IllegalStateException(HashedWheelTimer.class.getSimpleName() + ".stop() cannot be called from " + TimerTask.class.getSimpleName());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 293 */     if (!WORKER_STATE_UPDATER.compareAndSet(this, 1, 2))
/*     */     {
/* 295 */       WORKER_STATE_UPDATER.set(this, 2);
/*     */       
/* 297 */       if (this.leak != null) {
/* 298 */         this.leak.close();
/*     */       }
/*     */       
/* 301 */       return Collections.emptySet();
/*     */     }
/*     */     
/* 304 */     boolean interrupted = false;
/* 305 */     while (this.workerThread.isAlive()) {
/* 306 */       this.workerThread.interrupt();
/*     */       try {
/* 308 */         this.workerThread.join(100L);
/*     */       } catch (InterruptedException ignored) {
/* 310 */         interrupted = true;
/*     */       }
/*     */     }
/*     */     
/* 314 */     if (interrupted) {
/* 315 */       Thread.currentThread().interrupt();
/*     */     }
/*     */     
/* 318 */     if (this.leak != null) {
/* 319 */       this.leak.close();
/*     */     }
/* 321 */     return this.worker.unprocessedTimeouts();
/*     */   }
/*     */   
/*     */   public Timeout newTimeout(TimerTask task, long delay, TimeUnit unit)
/*     */   {
/* 326 */     if (task == null) {
/* 327 */       throw new NullPointerException("task");
/*     */     }
/* 329 */     if (unit == null) {
/* 330 */       throw new NullPointerException("unit");
/*     */     }
/* 332 */     start();
/*     */     
/*     */ 
/*     */ 
/* 336 */     long deadline = System.nanoTime() + unit.toNanos(delay) - this.startTime;
/* 337 */     HashedWheelTimeout timeout = new HashedWheelTimeout(this, task, deadline);
/* 338 */     this.timeouts.add(timeout);
/* 339 */     return timeout;
/*     */   }
/*     */   
/*     */   private final class Worker implements Runnable {
/* 343 */     private final Set<Timeout> unprocessedTimeouts = new HashSet();
/*     */     private long tick;
/*     */     
/*     */     private Worker() {}
/*     */     
/*     */     public void run()
/*     */     {
/* 350 */       HashedWheelTimer.this.startTime = System.nanoTime();
/* 351 */       if (HashedWheelTimer.this.startTime == 0L)
/*     */       {
/* 353 */         HashedWheelTimer.this.startTime = 1L;
/*     */       }
/*     */       
/*     */ 
/* 357 */       HashedWheelTimer.this.startTimeInitialized.countDown();
/*     */       do
/*     */       {
/* 360 */         long deadline = waitForNextTick();
/* 361 */         if (deadline > 0L) {
/* 362 */           int idx = (int)(this.tick & HashedWheelTimer.this.mask);
/* 363 */           processCancelledTasks();
/* 364 */           HashedWheelTimer.HashedWheelBucket bucket = HashedWheelTimer.this.wheel[idx];
/*     */           
/* 366 */           transferTimeoutsToBuckets();
/* 367 */           bucket.expireTimeouts(deadline);
/* 368 */           this.tick += 1L;
/*     */         }
/* 370 */       } while (HashedWheelTimer.WORKER_STATE_UPDATER.get(HashedWheelTimer.this) == 1);
/*     */       
/*     */ 
/* 373 */       for (HashedWheelTimer.HashedWheelBucket bucket : HashedWheelTimer.this.wheel) {
/* 374 */         bucket.clearTimeouts(this.unprocessedTimeouts);
/*     */       }
/*     */       for (;;) {
/* 377 */         HashedWheelTimer.HashedWheelTimeout timeout = (HashedWheelTimer.HashedWheelTimeout)HashedWheelTimer.this.timeouts.poll();
/* 378 */         if (timeout == null) {
/*     */           break;
/*     */         }
/* 381 */         if (!timeout.isCancelled()) {
/* 382 */           this.unprocessedTimeouts.add(timeout);
/*     */         }
/*     */       }
/* 385 */       processCancelledTasks();
/*     */     }
/*     */     
/*     */ 
/*     */     private void transferTimeoutsToBuckets()
/*     */     {
/* 391 */       for (int i = 0; i < 100000; i++) {
/* 392 */         HashedWheelTimer.HashedWheelTimeout timeout = (HashedWheelTimer.HashedWheelTimeout)HashedWheelTimer.this.timeouts.poll();
/* 393 */         if (timeout == null) {
/*     */           break;
/*     */         }
/*     */         
/* 397 */         if (timeout.state() != 1)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 402 */           long calculated = timeout.deadline / HashedWheelTimer.this.tickDuration;
/* 403 */           timeout.remainingRounds = ((calculated - this.tick) / HashedWheelTimer.this.wheel.length);
/*     */           
/* 405 */           long ticks = Math.max(calculated, this.tick);
/* 406 */           int stopIndex = (int)(ticks & HashedWheelTimer.this.mask);
/*     */           
/* 408 */           HashedWheelTimer.HashedWheelBucket bucket = HashedWheelTimer.this.wheel[stopIndex];
/* 409 */           bucket.addTimeout(timeout);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private void processCancelledTasks() {
/* 415 */       for (;;) { Runnable task = (Runnable)HashedWheelTimer.this.cancelledTimeouts.poll();
/* 416 */         if (task == null) {
/*     */           break;
/*     */         }
/*     */         try
/*     */         {
/* 421 */           task.run();
/*     */         } catch (Throwable t) {
/* 423 */           if (HashedWheelTimer.logger.isWarnEnabled()) {
/* 424 */             HashedWheelTimer.logger.warn("An exception was thrown while process a cancellation task", t);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private long waitForNextTick()
/*     */     {
/* 437 */       long deadline = HashedWheelTimer.this.tickDuration * (this.tick + 1L);
/*     */       for (;;)
/*     */       {
/* 440 */         long currentTime = System.nanoTime() - HashedWheelTimer.this.startTime;
/* 441 */         long sleepTimeMs = (deadline - currentTime + 999999L) / 1000000L;
/*     */         
/* 443 */         if (sleepTimeMs <= 0L) {
/* 444 */           if (currentTime == Long.MIN_VALUE) {
/* 445 */             return -9223372036854775807L;
/*     */           }
/* 447 */           return currentTime;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 456 */         if (PlatformDependent.isWindows()) {
/* 457 */           sleepTimeMs = sleepTimeMs / 10L * 10L;
/*     */         }
/*     */         try
/*     */         {
/* 461 */           Thread.sleep(sleepTimeMs);
/*     */         } catch (InterruptedException ignored) {
/* 463 */           if (HashedWheelTimer.WORKER_STATE_UPDATER.get(HashedWheelTimer.this) == 2) {
/* 464 */             return Long.MIN_VALUE;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 471 */     public Set<Timeout> unprocessedTimeouts() { return Collections.unmodifiableSet(this.unprocessedTimeouts); }
/*     */   }
/*     */   
/*     */   private static final class HashedWheelTimeout extends MpscLinkedQueueNode<Timeout> implements Timeout {
/*     */     private static final int ST_INIT = 0;
/*     */     private static final int ST_CANCELLED = 1;
/*     */     private static final int ST_EXPIRED = 2;
/*     */     private static final AtomicIntegerFieldUpdater<HashedWheelTimeout> STATE_UPDATER;
/*     */     private final HashedWheelTimer timer;
/*     */     private final TimerTask task;
/*     */     private final long deadline;
/*     */     
/*     */     static {
/* 484 */       AtomicIntegerFieldUpdater<HashedWheelTimeout> updater = PlatformDependent.newAtomicIntegerFieldUpdater(HashedWheelTimeout.class, "state");
/*     */       
/* 486 */       if (updater == null) {
/* 487 */         updater = AtomicIntegerFieldUpdater.newUpdater(HashedWheelTimeout.class, "state");
/*     */       }
/* 489 */       STATE_UPDATER = updater;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 496 */     private volatile int state = 0;
/*     */     
/*     */ 
/*     */     long remainingRounds;
/*     */     
/*     */ 
/*     */     HashedWheelTimeout next;
/*     */     
/*     */ 
/*     */     HashedWheelTimeout prev;
/*     */     
/*     */     HashedWheelTimer.HashedWheelBucket bucket;
/*     */     
/*     */ 
/*     */     HashedWheelTimeout(HashedWheelTimer timer, TimerTask task, long deadline)
/*     */     {
/* 512 */       this.timer = timer;
/* 513 */       this.task = task;
/* 514 */       this.deadline = deadline;
/*     */     }
/*     */     
/*     */     public Timer timer()
/*     */     {
/* 519 */       return this.timer;
/*     */     }
/*     */     
/*     */     public TimerTask task()
/*     */     {
/* 524 */       return this.task;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean cancel()
/*     */     {
/* 530 */       if (!compareAndSetState(0, 1)) {
/* 531 */         return false;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 540 */       this.timer.cancelledTimeouts.add(new Runnable()
/*     */       {
/*     */         public void run() {
/* 543 */           HashedWheelTimer.HashedWheelBucket bucket = HashedWheelTimer.HashedWheelTimeout.this.bucket;
/* 544 */           if (bucket != null) {
/* 545 */             bucket.remove(HashedWheelTimer.HashedWheelTimeout.this);
/*     */           }
/*     */         }
/* 548 */       });
/* 549 */       return true;
/*     */     }
/*     */     
/*     */     public boolean compareAndSetState(int expected, int state) {
/* 553 */       return STATE_UPDATER.compareAndSet(this, expected, state);
/*     */     }
/*     */     
/*     */     public int state() {
/* 557 */       return this.state;
/*     */     }
/*     */     
/*     */     public boolean isCancelled()
/*     */     {
/* 562 */       return state() == 1;
/*     */     }
/*     */     
/*     */     public boolean isExpired()
/*     */     {
/* 567 */       return state() == 2;
/*     */     }
/*     */     
/*     */     public HashedWheelTimeout value()
/*     */     {
/* 572 */       return this;
/*     */     }
/*     */     
/*     */     public void expire() {
/* 576 */       if (!compareAndSetState(0, 2)) {
/* 577 */         return;
/*     */       }
/*     */       try
/*     */       {
/* 581 */         this.task.run(this);
/*     */       } catch (Throwable t) {
/* 583 */         if (HashedWheelTimer.logger.isWarnEnabled()) {
/* 584 */           HashedWheelTimer.logger.warn("An exception was thrown by " + TimerTask.class.getSimpleName() + '.', t);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 591 */       long currentTime = System.nanoTime();
/* 592 */       long remaining = this.deadline - currentTime + this.timer.startTime;
/*     */       
/* 594 */       StringBuilder buf = new StringBuilder(192).append(StringUtil.simpleClassName(this)).append('(').append("deadline: ");
/*     */       
/*     */ 
/*     */ 
/* 598 */       if (remaining > 0L) {
/* 599 */         buf.append(remaining).append(" ns later");
/*     */       }
/* 601 */       else if (remaining < 0L) {
/* 602 */         buf.append(-remaining).append(" ns ago");
/*     */       }
/*     */       else {
/* 605 */         buf.append("now");
/*     */       }
/*     */       
/* 608 */       if (isCancelled()) {
/* 609 */         buf.append(", cancelled");
/*     */       }
/*     */       
/* 612 */       return ", task: " + task() + ')';
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class HashedWheelBucket
/*     */   {
/*     */     private HashedWheelTimer.HashedWheelTimeout head;
/*     */     
/*     */ 
/*     */ 
/*     */     private HashedWheelTimer.HashedWheelTimeout tail;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void addTimeout(HashedWheelTimer.HashedWheelTimeout timeout)
/*     */     {
/* 633 */       assert (timeout.bucket == null);
/* 634 */       timeout.bucket = this;
/* 635 */       if (this.head == null) {
/* 636 */         this.head = (this.tail = timeout);
/*     */       } else {
/* 638 */         this.tail.next = timeout;
/* 639 */         timeout.prev = this.tail;
/* 640 */         this.tail = timeout;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void expireTimeouts(long deadline)
/*     */     {
/* 648 */       HashedWheelTimer.HashedWheelTimeout timeout = this.head;
/*     */       
/*     */ 
/* 651 */       while (timeout != null) {
/* 652 */         boolean remove = false;
/* 653 */         if (timeout.remainingRounds <= 0L) {
/* 654 */           if (HashedWheelTimer.HashedWheelTimeout.access$800(timeout) <= deadline) {
/* 655 */             timeout.expire();
/*     */           }
/*     */           else {
/* 658 */             throw new IllegalStateException(String.format("timeout.deadline (%d) > deadline (%d)", new Object[] { Long.valueOf(HashedWheelTimer.HashedWheelTimeout.access$800(timeout)), Long.valueOf(deadline) }));
/*     */           }
/*     */           
/* 661 */           remove = true;
/* 662 */         } else if (timeout.isCancelled()) {
/* 663 */           remove = true;
/*     */         } else {
/* 665 */           timeout.remainingRounds -= 1L;
/*     */         }
/*     */         
/* 668 */         HashedWheelTimer.HashedWheelTimeout next = timeout.next;
/* 669 */         if (remove) {
/* 670 */           remove(timeout);
/*     */         }
/* 672 */         timeout = next;
/*     */       }
/*     */     }
/*     */     
/*     */     public void remove(HashedWheelTimer.HashedWheelTimeout timeout) {
/* 677 */       HashedWheelTimer.HashedWheelTimeout next = timeout.next;
/*     */       
/* 679 */       if (timeout.prev != null) {
/* 680 */         timeout.prev.next = next;
/*     */       }
/* 682 */       if (timeout.next != null) {
/* 683 */         timeout.next.prev = timeout.prev;
/*     */       }
/*     */       
/* 686 */       if (timeout == this.head)
/*     */       {
/* 688 */         if (timeout == this.tail) {
/* 689 */           this.tail = null;
/* 690 */           this.head = null;
/*     */         } else {
/* 692 */           this.head = next;
/*     */         }
/* 694 */       } else if (timeout == this.tail)
/*     */       {
/* 696 */         this.tail = timeout.prev;
/*     */       }
/*     */       
/* 699 */       timeout.prev = null;
/* 700 */       timeout.next = null;
/* 701 */       timeout.bucket = null;
/*     */     }
/*     */     
/*     */ 
/*     */     public void clearTimeouts(Set<Timeout> set)
/*     */     {
/*     */       for (;;)
/*     */       {
/* 709 */         HashedWheelTimer.HashedWheelTimeout timeout = pollTimeout();
/* 710 */         if (timeout == null) {
/* 711 */           return;
/*     */         }
/* 713 */         if ((!timeout.isExpired()) && (!timeout.isCancelled()))
/*     */         {
/*     */ 
/* 716 */           set.add(timeout); }
/*     */       }
/*     */     }
/*     */     
/*     */     private HashedWheelTimer.HashedWheelTimeout pollTimeout() {
/* 721 */       HashedWheelTimer.HashedWheelTimeout head = this.head;
/* 722 */       if (head == null) {
/* 723 */         return null;
/*     */       }
/* 725 */       HashedWheelTimer.HashedWheelTimeout next = head.next;
/* 726 */       if (next == null) {
/* 727 */         this.tail = (this.head = null);
/*     */       } else {
/* 729 */         this.head = next;
/* 730 */         next.prev = null;
/*     */       }
/*     */       
/*     */ 
/* 734 */       head.next = null;
/* 735 */       head.prev = null;
/* 736 */       head.bucket = null;
/* 737 */       return head;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\HashedWheelTimer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */