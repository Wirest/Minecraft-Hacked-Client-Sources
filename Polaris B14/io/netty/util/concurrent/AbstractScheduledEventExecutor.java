/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import io.netty.util.internal.CallableEventExecutorAdapter;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.RunnableEventExecutorAdapter;
/*     */ import java.util.Iterator;
/*     */ import java.util.PriorityQueue;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executors;
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
/*     */ 
/*     */ 
/*     */ public abstract class AbstractScheduledEventExecutor
/*     */   extends AbstractEventExecutor
/*     */ {
/*     */   Queue<ScheduledFutureTask<?>> scheduledTaskQueue;
/*     */   
/*     */   protected AbstractScheduledEventExecutor() {}
/*     */   
/*     */   protected AbstractScheduledEventExecutor(EventExecutorGroup parent)
/*     */   {
/*  40 */     super(parent);
/*     */   }
/*     */   
/*     */   protected static long nanoTime() {
/*  44 */     return ScheduledFutureTask.nanoTime();
/*     */   }
/*     */   
/*     */   Queue<ScheduledFutureTask<?>> scheduledTaskQueue() {
/*  48 */     if (this.scheduledTaskQueue == null) {
/*  49 */       this.scheduledTaskQueue = new PriorityQueue();
/*     */     }
/*  51 */     return this.scheduledTaskQueue;
/*     */   }
/*     */   
/*     */   private static boolean isNullOrEmpty(Queue<ScheduledFutureTask<?>> queue) {
/*  55 */     return (queue == null) || (queue.isEmpty());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void cancelScheduledTasks()
/*     */   {
/*  64 */     assert (inEventLoop());
/*  65 */     Queue<ScheduledFutureTask<?>> scheduledTaskQueue = this.scheduledTaskQueue;
/*  66 */     if (isNullOrEmpty(scheduledTaskQueue)) {
/*  67 */       return;
/*     */     }
/*     */     
/*  70 */     ScheduledFutureTask<?>[] scheduledTasks = (ScheduledFutureTask[])scheduledTaskQueue.toArray(new ScheduledFutureTask[scheduledTaskQueue.size()]);
/*     */     
/*     */ 
/*  73 */     for (ScheduledFutureTask<?> task : scheduledTasks) {
/*  74 */       task.cancel(false);
/*     */     }
/*     */     
/*  77 */     scheduledTaskQueue.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final Runnable pollScheduledTask()
/*     */   {
/*  84 */     return pollScheduledTask(nanoTime());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Runnable pollScheduledTask(long nanoTime)
/*     */   {
/*  92 */     assert (inEventLoop());
/*     */     
/*  94 */     Queue<ScheduledFutureTask<?>> scheduledTaskQueue = this.scheduledTaskQueue;
/*  95 */     ScheduledFutureTask<?> scheduledTask = scheduledTaskQueue == null ? null : (ScheduledFutureTask)scheduledTaskQueue.peek();
/*  96 */     if (scheduledTask == null) {
/*  97 */       return null;
/*     */     }
/*     */     
/* 100 */     if (scheduledTask.deadlineNanos() <= nanoTime) {
/* 101 */       scheduledTaskQueue.remove();
/* 102 */       return scheduledTask;
/*     */     }
/* 104 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final long nextScheduledTaskNano()
/*     */   {
/* 111 */     Queue<ScheduledFutureTask<?>> scheduledTaskQueue = this.scheduledTaskQueue;
/* 112 */     ScheduledFutureTask<?> scheduledTask = scheduledTaskQueue == null ? null : (ScheduledFutureTask)scheduledTaskQueue.peek();
/* 113 */     if (scheduledTask == null) {
/* 114 */       return -1L;
/*     */     }
/* 116 */     return Math.max(0L, scheduledTask.deadlineNanos() - nanoTime());
/*     */   }
/*     */   
/*     */   final ScheduledFutureTask<?> peekScheduledTask() {
/* 120 */     Queue<ScheduledFutureTask<?>> scheduledTaskQueue = this.scheduledTaskQueue;
/* 121 */     if (scheduledTaskQueue == null) {
/* 122 */       return null;
/*     */     }
/* 124 */     return (ScheduledFutureTask)scheduledTaskQueue.peek();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final boolean hasScheduledTasks()
/*     */   {
/* 131 */     Queue<ScheduledFutureTask<?>> scheduledTaskQueue = this.scheduledTaskQueue;
/* 132 */     ScheduledFutureTask<?> scheduledTask = scheduledTaskQueue == null ? null : (ScheduledFutureTask)scheduledTaskQueue.peek();
/* 133 */     return (scheduledTask != null) && (scheduledTask.deadlineNanos() <= nanoTime());
/*     */   }
/*     */   
/*     */   public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit)
/*     */   {
/* 138 */     ObjectUtil.checkNotNull(command, "command");
/* 139 */     ObjectUtil.checkNotNull(unit, "unit");
/* 140 */     if (delay < 0L) {
/* 141 */       throw new IllegalArgumentException(String.format("delay: %d (expected: >= 0)", new Object[] { Long.valueOf(delay) }));
/*     */     }
/*     */     
/* 144 */     return schedule(new ScheduledFutureTask(this, toCallable(command), ScheduledFutureTask.deadlineNanos(unit.toNanos(delay))));
/*     */   }
/*     */   
/*     */ 
/*     */   public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit)
/*     */   {
/* 150 */     ObjectUtil.checkNotNull(callable, "callable");
/* 151 */     ObjectUtil.checkNotNull(unit, "unit");
/* 152 */     if (delay < 0L) {
/* 153 */       throw new IllegalArgumentException(String.format("delay: %d (expected: >= 0)", new Object[] { Long.valueOf(delay) }));
/*     */     }
/*     */     
/* 156 */     return schedule(new ScheduledFutureTask(this, callable, ScheduledFutureTask.deadlineNanos(unit.toNanos(delay))));
/*     */   }
/*     */   
/*     */ 
/*     */   public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)
/*     */   {
/* 162 */     ObjectUtil.checkNotNull(command, "command");
/* 163 */     ObjectUtil.checkNotNull(unit, "unit");
/* 164 */     if (initialDelay < 0L) {
/* 165 */       throw new IllegalArgumentException(String.format("initialDelay: %d (expected: >= 0)", new Object[] { Long.valueOf(initialDelay) }));
/*     */     }
/*     */     
/* 168 */     if (period <= 0L) {
/* 169 */       throw new IllegalArgumentException(String.format("period: %d (expected: > 0)", new Object[] { Long.valueOf(period) }));
/*     */     }
/*     */     
/*     */ 
/* 173 */     return schedule(new ScheduledFutureTask(this, toCallable(command), ScheduledFutureTask.deadlineNanos(unit.toNanos(initialDelay)), unit.toNanos(period)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit)
/*     */   {
/* 180 */     ObjectUtil.checkNotNull(command, "command");
/* 181 */     ObjectUtil.checkNotNull(unit, "unit");
/* 182 */     if (initialDelay < 0L) {
/* 183 */       throw new IllegalArgumentException(String.format("initialDelay: %d (expected: >= 0)", new Object[] { Long.valueOf(initialDelay) }));
/*     */     }
/*     */     
/* 186 */     if (delay <= 0L) {
/* 187 */       throw new IllegalArgumentException(String.format("delay: %d (expected: > 0)", new Object[] { Long.valueOf(delay) }));
/*     */     }
/*     */     
/*     */ 
/* 191 */     return schedule(new ScheduledFutureTask(this, toCallable(command), ScheduledFutureTask.deadlineNanos(unit.toNanos(initialDelay)), -unit.toNanos(delay)));
/*     */   }
/*     */   
/*     */ 
/*     */   <V> ScheduledFuture<V> schedule(final ScheduledFutureTask<V> task)
/*     */   {
/* 197 */     if (inEventLoop()) {
/* 198 */       scheduledTaskQueue().add(task);
/*     */     } else {
/* 200 */       execute(new Runnable()
/*     */       {
/*     */         public void run() {
/* 203 */           AbstractScheduledEventExecutor.this.scheduledTaskQueue().add(task);
/*     */         }
/*     */       });
/*     */     }
/*     */     
/* 208 */     return task;
/*     */   }
/*     */   
/*     */   void purgeCancelledScheduledTasks() {
/* 212 */     Queue<ScheduledFutureTask<?>> scheduledTaskQueue = this.scheduledTaskQueue;
/* 213 */     if (isNullOrEmpty(scheduledTaskQueue)) {
/* 214 */       return;
/*     */     }
/* 216 */     Iterator<ScheduledFutureTask<?>> i = scheduledTaskQueue.iterator();
/* 217 */     while (i.hasNext()) {
/* 218 */       ScheduledFutureTask<?> task = (ScheduledFutureTask)i.next();
/* 219 */       if (task.isCancelled()) {
/* 220 */         i.remove();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static Callable<Void> toCallable(Runnable command) {
/* 226 */     if ((command instanceof RunnableEventExecutorAdapter)) {
/* 227 */       return new RunnableToCallableAdapter((RunnableEventExecutorAdapter)command);
/*     */     }
/* 229 */     return Executors.callable(command, null);
/*     */   }
/*     */   
/*     */   private static class RunnableToCallableAdapter implements CallableEventExecutorAdapter<Void>
/*     */   {
/*     */     final RunnableEventExecutorAdapter runnable;
/*     */     
/*     */     RunnableToCallableAdapter(RunnableEventExecutorAdapter runnable)
/*     */     {
/* 238 */       this.runnable = runnable;
/*     */     }
/*     */     
/*     */     public EventExecutor executor()
/*     */     {
/* 243 */       return this.runnable.executor();
/*     */     }
/*     */     
/*     */     public Callable<Void> unwrap()
/*     */     {
/* 248 */       return null;
/*     */     }
/*     */     
/*     */     public Void call() throws Exception
/*     */     {
/* 253 */       this.runnable.run();
/* 254 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\concurrent\AbstractScheduledEventExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */