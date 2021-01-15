/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.ThreadFactory;
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
/*     */ public final class GlobalEventExecutor
/*     */   extends AbstractScheduledEventExecutor
/*     */ {
/*  37 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(GlobalEventExecutor.class);
/*     */   
/*  39 */   private static final long SCHEDULE_PURGE_INTERVAL = TimeUnit.SECONDS.toNanos(1L);
/*     */   
/*  41 */   public static final GlobalEventExecutor INSTANCE = new GlobalEventExecutor();
/*     */   
/*  43 */   final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue();
/*  44 */   final ScheduledFutureTask<Void> purgeTask = new ScheduledFutureTask(this, Executors.callable(new PurgeTask(null), null), ScheduledFutureTask.deadlineNanos(SCHEDULE_PURGE_INTERVAL), -SCHEDULE_PURGE_INTERVAL);
/*     */   
/*     */ 
/*     */ 
/*  48 */   private final ThreadFactory threadFactory = new DefaultThreadFactory(getClass());
/*  49 */   private final TaskRunner taskRunner = new TaskRunner();
/*  50 */   private final AtomicBoolean started = new AtomicBoolean();
/*     */   
/*     */   volatile Thread thread;
/*  53 */   private final Future<?> terminationFuture = new FailedFuture(this, new UnsupportedOperationException());
/*     */   
/*     */   private GlobalEventExecutor() {
/*  56 */     scheduledTaskQueue().add(this.purgeTask);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Runnable takeTask()
/*     */   {
/*  65 */     BlockingQueue<Runnable> taskQueue = this.taskQueue;
/*     */     for (;;) {
/*  67 */       ScheduledFutureTask<?> scheduledTask = peekScheduledTask();
/*  68 */       if (scheduledTask == null) {
/*  69 */         Runnable task = null;
/*     */         try {
/*  71 */           task = (Runnable)taskQueue.take();
/*     */         }
/*     */         catch (InterruptedException e) {}
/*     */         
/*  75 */         return task;
/*     */       }
/*  77 */       long delayNanos = scheduledTask.delayNanos();
/*     */       Runnable task;
/*  79 */       if (delayNanos > 0L) {
/*     */         try {
/*  81 */           task = (Runnable)taskQueue.poll(delayNanos, TimeUnit.NANOSECONDS);
/*     */         }
/*     */         catch (InterruptedException e) {
/*  84 */           return null;
/*     */         }
/*     */       } else {
/*  87 */         task = (Runnable)taskQueue.poll();
/*     */       }
/*     */       
/*  90 */       if (task == null) {
/*  91 */         fetchFromScheduledTaskQueue();
/*  92 */         task = (Runnable)taskQueue.poll();
/*     */       }
/*     */       
/*  95 */       if (task != null) {
/*  96 */         return task;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void fetchFromScheduledTaskQueue()
/*     */   {
/* 103 */     if (hasScheduledTasks()) {
/* 104 */       long nanoTime = AbstractScheduledEventExecutor.nanoTime();
/*     */       for (;;) {
/* 106 */         Runnable scheduledTask = pollScheduledTask(nanoTime);
/* 107 */         if (scheduledTask == null) {
/*     */           break;
/*     */         }
/* 110 */         this.taskQueue.add(scheduledTask);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int pendingTasks()
/*     */   {
/* 122 */     return this.taskQueue.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addTask(Runnable task)
/*     */   {
/* 130 */     if (task == null) {
/* 131 */       throw new NullPointerException("task");
/*     */     }
/* 133 */     this.taskQueue.add(task);
/*     */   }
/*     */   
/*     */   public boolean inEventLoop(Thread thread)
/*     */   {
/* 138 */     return thread == this.thread;
/*     */   }
/*     */   
/*     */   public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit)
/*     */   {
/* 143 */     return terminationFuture();
/*     */   }
/*     */   
/*     */   public Future<?> terminationFuture()
/*     */   {
/* 148 */     return this.terminationFuture;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void shutdown()
/*     */   {
/* 154 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isShuttingDown()
/*     */   {
/* 159 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isShutdown()
/*     */   {
/* 164 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isTerminated()
/*     */   {
/* 169 */     return false;
/*     */   }
/*     */   
/*     */   public boolean awaitTermination(long timeout, TimeUnit unit)
/*     */   {
/* 174 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean awaitInactivity(long timeout, TimeUnit unit)
/*     */     throws InterruptedException
/*     */   {
/* 186 */     if (unit == null) {
/* 187 */       throw new NullPointerException("unit");
/*     */     }
/*     */     
/* 190 */     Thread thread = this.thread;
/* 191 */     if (thread == null) {
/* 192 */       throw new IllegalStateException("thread was not started");
/*     */     }
/* 194 */     thread.join(unit.toMillis(timeout));
/* 195 */     return !thread.isAlive();
/*     */   }
/*     */   
/*     */   public void execute(Runnable task)
/*     */   {
/* 200 */     if (task == null) {
/* 201 */       throw new NullPointerException("task");
/*     */     }
/*     */     
/* 204 */     addTask(task);
/* 205 */     if (!inEventLoop()) {
/* 206 */       startThread();
/*     */     }
/*     */   }
/*     */   
/*     */   private void startThread() {
/* 211 */     if (this.started.compareAndSet(false, true)) {
/* 212 */       Thread t = this.threadFactory.newThread(this.taskRunner);
/* 213 */       t.start();
/* 214 */       this.thread = t;
/*     */     }
/*     */   }
/*     */   
/*     */   final class TaskRunner implements Runnable {
/*     */     TaskRunner() {}
/*     */     
/*     */     public void run() {
/* 222 */       for (;;) { Runnable task = GlobalEventExecutor.this.takeTask();
/* 223 */         if (task != null) {
/*     */           try {
/* 225 */             task.run();
/*     */           } catch (Throwable t) {
/* 227 */             GlobalEventExecutor.logger.warn("Unexpected exception from the global event executor: ", t);
/*     */           }
/*     */           
/* 230 */           if (task != GlobalEventExecutor.this.purgeTask) {}
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 235 */           Queue<ScheduledFutureTask<?>> scheduledTaskQueue = GlobalEventExecutor.this.scheduledTaskQueue;
/*     */           
/* 237 */           if ((GlobalEventExecutor.this.taskQueue.isEmpty()) && ((scheduledTaskQueue == null) || (scheduledTaskQueue.size() == 1)))
/*     */           {
/*     */ 
/*     */ 
/* 241 */             boolean stopped = GlobalEventExecutor.this.started.compareAndSet(true, false);
/* 242 */             assert (stopped);
/*     */             
/*     */ 
/* 245 */             if ((GlobalEventExecutor.this.taskQueue.isEmpty()) && ((scheduledTaskQueue == null) || (scheduledTaskQueue.size() == 1))) {
/*     */               break;
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 254 */             if (!GlobalEventExecutor.this.started.compareAndSet(false, true)) {
/*     */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private final class PurgeTask
/*     */     implements Runnable
/*     */   {
/*     */     private PurgeTask() {}
/*     */     
/*     */     public void run()
/*     */     {
/* 271 */       GlobalEventExecutor.this.purgeCancelledScheduledTasks();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\concurrent\GlobalEventExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */