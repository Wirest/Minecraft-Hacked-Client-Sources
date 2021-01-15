/*     */ package io.netty.util;
/*     */ 
/*     */ import io.netty.util.concurrent.DefaultThreadFactory;
/*     */ import io.netty.util.internal.MpscLinkedQueueNode;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ThreadDeathWatcher
/*     */ {
/*  42 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ThreadDeathWatcher.class);
/*  43 */   private static final ThreadFactory threadFactory = new DefaultThreadFactory(ThreadDeathWatcher.class, true, 1);
/*     */   
/*     */ 
/*  46 */   private static final Queue<Entry> pendingEntries = PlatformDependent.newMpscQueue();
/*  47 */   private static final Watcher watcher = new Watcher(null);
/*  48 */   private static final AtomicBoolean started = new AtomicBoolean();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static volatile Thread watcherThread;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void watch(Thread thread, Runnable task)
/*     */   {
/*  60 */     if (thread == null) {
/*  61 */       throw new NullPointerException("thread");
/*     */     }
/*  63 */     if (task == null) {
/*  64 */       throw new NullPointerException("task");
/*     */     }
/*  66 */     if (!thread.isAlive()) {
/*  67 */       throw new IllegalArgumentException("thread must be alive.");
/*     */     }
/*     */     
/*  70 */     schedule(thread, task, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void unwatch(Thread thread, Runnable task)
/*     */   {
/*  77 */     if (thread == null) {
/*  78 */       throw new NullPointerException("thread");
/*     */     }
/*  80 */     if (task == null) {
/*  81 */       throw new NullPointerException("task");
/*     */     }
/*     */     
/*  84 */     schedule(thread, task, false);
/*     */   }
/*     */   
/*     */   private static void schedule(Thread thread, Runnable task, boolean isWatch) {
/*  88 */     pendingEntries.add(new Entry(thread, task, isWatch));
/*     */     
/*  90 */     if (started.compareAndSet(false, true)) {
/*  91 */       Thread watcherThread = threadFactory.newThread(watcher);
/*  92 */       watcherThread.start();
/*  93 */       watcherThread = watcherThread;
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
/*     */   public static boolean awaitInactivity(long timeout, TimeUnit unit)
/*     */     throws InterruptedException
/*     */   {
/* 107 */     if (unit == null) {
/* 108 */       throw new NullPointerException("unit");
/*     */     }
/*     */     
/* 111 */     Thread watcherThread = watcherThread;
/* 112 */     if (watcherThread != null) {
/* 113 */       watcherThread.join(unit.toMillis(timeout));
/* 114 */       return !watcherThread.isAlive();
/*     */     }
/* 116 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final class Watcher
/*     */     implements Runnable
/*     */   {
/* 124 */     private final List<ThreadDeathWatcher.Entry> watchees = new ArrayList();
/*     */     
/*     */     public void run()
/*     */     {
/*     */       for (;;) {
/* 129 */         fetchWatchees();
/* 130 */         notifyWatchees();
/*     */         
/*     */ 
/* 133 */         fetchWatchees();
/* 134 */         notifyWatchees();
/*     */         try
/*     */         {
/* 137 */           Thread.sleep(1000L);
/*     */         }
/*     */         catch (InterruptedException ignore) {}
/*     */         
/*     */ 
/* 142 */         if ((this.watchees.isEmpty()) && (ThreadDeathWatcher.pendingEntries.isEmpty()))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 147 */           boolean stopped = ThreadDeathWatcher.started.compareAndSet(true, false);
/* 148 */           assert (stopped);
/*     */           
/*     */ 
/* 151 */           if (ThreadDeathWatcher.pendingEntries.isEmpty()) {
/*     */             break;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 160 */           if (!ThreadDeathWatcher.started.compareAndSet(false, true)) {
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private void fetchWatchees()
/*     */     {
/*     */       for (;;)
/*     */       {
/* 175 */         ThreadDeathWatcher.Entry e = (ThreadDeathWatcher.Entry)ThreadDeathWatcher.pendingEntries.poll();
/* 176 */         if (e == null) {
/*     */           break;
/*     */         }
/*     */         
/* 180 */         if (e.isWatch) {
/* 181 */           this.watchees.add(e);
/*     */         } else {
/* 183 */           this.watchees.remove(e);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private void notifyWatchees() {
/* 189 */       List<ThreadDeathWatcher.Entry> watchees = this.watchees;
/* 190 */       for (int i = 0; i < watchees.size();) {
/* 191 */         ThreadDeathWatcher.Entry e = (ThreadDeathWatcher.Entry)watchees.get(i);
/* 192 */         if (!e.thread.isAlive()) {
/* 193 */           watchees.remove(i);
/*     */           try {
/* 195 */             e.task.run();
/*     */           } catch (Throwable t) {
/* 197 */             ThreadDeathWatcher.logger.warn("Thread death watcher task raised an exception:", t);
/*     */           }
/*     */         } else {
/* 200 */           i++;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Entry extends MpscLinkedQueueNode<Entry> {
/*     */     final Thread thread;
/*     */     final Runnable task;
/*     */     final boolean isWatch;
/*     */     
/*     */     Entry(Thread thread, Runnable task, boolean isWatch) {
/* 212 */       this.thread = thread;
/* 213 */       this.task = task;
/* 214 */       this.isWatch = isWatch;
/*     */     }
/*     */     
/*     */     public Entry value()
/*     */     {
/* 219 */       return this;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 224 */       return this.thread.hashCode() ^ this.task.hashCode();
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 229 */       if (obj == this) {
/* 230 */         return true;
/*     */       }
/*     */       
/* 233 */       if (!(obj instanceof Entry)) {
/* 234 */         return false;
/*     */       }
/*     */       
/* 237 */       Entry that = (Entry)obj;
/* 238 */       return (this.thread == that.thread) && (this.task == that.task);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\ThreadDeathWatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */