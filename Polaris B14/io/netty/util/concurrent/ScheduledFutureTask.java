/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import io.netty.util.internal.CallableEventExecutorAdapter;
/*     */ import io.netty.util.internal.OneTimeTask;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Delayed;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ final class ScheduledFutureTask<V>
/*     */   extends PromiseTask<V>
/*     */   implements ScheduledFuture<V>
/*     */ {
/*  30 */   private static final AtomicLong nextTaskId = new AtomicLong();
/*  31 */   private static final long START_TIME = System.nanoTime();
/*     */   
/*     */   static long nanoTime() {
/*  34 */     return System.nanoTime() - START_TIME;
/*     */   }
/*     */   
/*     */   static long deadlineNanos(long delay) {
/*  38 */     return nanoTime() + delay;
/*     */   }
/*     */   
/*  41 */   private final long id = nextTaskId.getAndIncrement();
/*     */   
/*     */   private long deadlineNanos;
/*     */   private final long periodNanos;
/*     */   
/*     */   ScheduledFutureTask(EventExecutor executor, Callable<V> callable, long nanoTime, long period)
/*     */   {
/*  48 */     super(executor.unwrap(), callable);
/*  49 */     if (period == 0L) {
/*  50 */       throw new IllegalArgumentException("period: 0 (expected: != 0)");
/*     */     }
/*     */     
/*  53 */     this.deadlineNanos = nanoTime;
/*  54 */     this.periodNanos = period;
/*     */   }
/*     */   
/*     */   ScheduledFutureTask(EventExecutor executor, Callable<V> callable, long nanoTime)
/*     */   {
/*  59 */     super(executor.unwrap(), callable);
/*  60 */     this.deadlineNanos = nanoTime;
/*  61 */     this.periodNanos = 0L;
/*     */   }
/*     */   
/*     */   public long deadlineNanos() {
/*  65 */     return this.deadlineNanos;
/*     */   }
/*     */   
/*     */   public long delayNanos() {
/*  69 */     return Math.max(0L, deadlineNanos() - nanoTime());
/*     */   }
/*     */   
/*     */   public long delayNanos(long currentTimeNanos) {
/*  73 */     return Math.max(0L, deadlineNanos() - (currentTimeNanos - START_TIME));
/*     */   }
/*     */   
/*     */   public long getDelay(TimeUnit unit)
/*     */   {
/*  78 */     return unit.convert(delayNanos(), TimeUnit.NANOSECONDS);
/*     */   }
/*     */   
/*     */   public int compareTo(Delayed o)
/*     */   {
/*  83 */     if (this == o) {
/*  84 */       return 0;
/*     */     }
/*     */     
/*  87 */     ScheduledFutureTask<?> that = (ScheduledFutureTask)o;
/*  88 */     long d = deadlineNanos() - that.deadlineNanos();
/*  89 */     if (d < 0L)
/*  90 */       return -1;
/*  91 */     if (d > 0L)
/*  92 */       return 1;
/*  93 */     if (this.id < that.id)
/*  94 */       return -1;
/*  95 */     if (this.id == that.id) {
/*  96 */       throw new Error();
/*     */     }
/*  98 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */   public void run()
/*     */   {
/* 104 */     assert (executor().inEventLoop());
/*     */     try
/*     */     {
/* 107 */       if (isMigrationPending()) {
/* 108 */         scheduleWithNewExecutor();
/* 109 */       } else if (needsLaterExecution()) {
/* 110 */         if (!executor().isShutdown())
/*     */         {
/* 112 */           this.deadlineNanos = (nanoTime() + TimeUnit.MICROSECONDS.toNanos(10L));
/* 113 */           if (!isCancelled())
/*     */           {
/* 115 */             Queue<ScheduledFutureTask<?>> scheduledTaskQueue = ((AbstractScheduledEventExecutor)executor()).scheduledTaskQueue;
/*     */             
/* 117 */             assert (scheduledTaskQueue != null);
/* 118 */             scheduledTaskQueue.add(this);
/*     */           }
/*     */           
/*     */         }
/*     */       }
/* 123 */       else if (this.periodNanos == 0L) {
/* 124 */         if (setUncancellableInternal()) {
/* 125 */           V result = this.task.call();
/* 126 */           setSuccessInternal(result);
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 131 */       else if (!isCancelled()) {
/* 132 */         this.task.call();
/* 133 */         if (!executor().isShutdown()) {
/* 134 */           long p = this.periodNanos;
/* 135 */           if (p > 0L) {
/* 136 */             this.deadlineNanos += p;
/*     */           } else {
/* 138 */             this.deadlineNanos = (nanoTime() - p);
/*     */           }
/* 140 */           if (!isCancelled())
/*     */           {
/* 142 */             Queue<ScheduledFutureTask<?>> scheduledTaskQueue = ((AbstractScheduledEventExecutor)executor()).scheduledTaskQueue;
/*     */             
/* 144 */             assert (scheduledTaskQueue != null);
/* 145 */             scheduledTaskQueue.add(this);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Throwable cause)
/*     */     {
/* 152 */       setFailureInternal(cause);
/*     */     }
/*     */   }
/*     */   
/*     */   protected StringBuilder toStringBuilder()
/*     */   {
/* 158 */     StringBuilder buf = super.toStringBuilder();
/* 159 */     buf.setCharAt(buf.length() - 1, ',');
/*     */     
/* 161 */     return buf.append(" id: ").append(this.id).append(", deadline: ").append(this.deadlineNanos).append(", period: ").append(this.periodNanos).append(')');
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
/*     */   private boolean needsLaterExecution()
/*     */   {
/* 175 */     return ((this.task instanceof CallableEventExecutorAdapter)) && ((((CallableEventExecutorAdapter)this.task).executor() instanceof PausableEventExecutor)) && (!((PausableEventExecutor)((CallableEventExecutorAdapter)this.task).executor()).isAcceptingNewTasks());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isMigrationPending()
/*     */   {
/* 185 */     return (!isCancelled()) && ((this.task instanceof CallableEventExecutorAdapter)) && (executor() != ((CallableEventExecutorAdapter)this.task).executor().unwrap());
/*     */   }
/*     */   
/*     */ 
/*     */   private void scheduleWithNewExecutor()
/*     */   {
/* 191 */     EventExecutor newExecutor = ((CallableEventExecutorAdapter)this.task).executor().unwrap();
/*     */     
/* 193 */     if ((newExecutor instanceof SingleThreadEventExecutor)) {
/* 194 */       if (!newExecutor.isShutdown()) {
/* 195 */         this.executor = newExecutor;
/* 196 */         final Queue<ScheduledFutureTask<?>> scheduledTaskQueue = ((SingleThreadEventExecutor)newExecutor).scheduledTaskQueue();
/*     */         
/*     */ 
/* 199 */         this.executor.execute(new OneTimeTask()
/*     */         {
/*     */           public void run()
/*     */           {
/* 203 */             ScheduledFutureTask.this.deadlineNanos = ScheduledFutureTask.nanoTime();
/* 204 */             if (!ScheduledFutureTask.this.isCancelled()) {
/* 205 */               scheduledTaskQueue.add(ScheduledFutureTask.this);
/*     */             }
/*     */           }
/*     */         });
/*     */       }
/*     */     } else {
/* 211 */       throw new UnsupportedOperationException("task migration unsupported");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\concurrent\ScheduledFutureTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */