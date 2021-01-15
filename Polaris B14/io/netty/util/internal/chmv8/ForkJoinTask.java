/*      */ package io.netty.util.internal.chmv8;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.Field;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import java.util.RandomAccess;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.CancellationException;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.RunnableFuture;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.TimeoutException;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ import sun.misc.Unsafe;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class ForkJoinTask<V>
/*      */   implements Future<V>, Serializable
/*      */ {
/*      */   volatile int status;
/*      */   static final int DONE_MASK = -268435456;
/*      */   static final int NORMAL = -268435456;
/*      */   static final int CANCELLED = -1073741824;
/*      */   static final int EXCEPTIONAL = Integer.MIN_VALUE;
/*      */   static final int SIGNAL = 65536;
/*      */   static final int SMASK = 65535;
/*      */   private static final ExceptionNode[] exceptionTable;
/*      */   
/*      */   private int setCompletion(int completion)
/*      */   {
/*      */     int s;
/*      */     do
/*      */     {
/*  259 */       if ((s = this.status) < 0)
/*  260 */         return s;
/*  261 */     } while (!U.compareAndSwapInt(this, STATUS, s, s | completion));
/*  262 */     if (s >>> 16 != 0)
/*  263 */       synchronized (this) { notifyAll(); }
/*  264 */     return completion;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final int doExec()
/*      */   {
/*      */     int s;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  278 */     if ((s = this.status) >= 0) {
/*      */       boolean completed;
/*  280 */       try { completed = exec();
/*      */       } catch (Throwable rex) {
/*  282 */         return setExceptionalCompletion(rex);
/*      */       }
/*  284 */       if (completed)
/*  285 */         s = setCompletion(-268435456);
/*      */     }
/*  287 */     return s;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final boolean trySetSignal()
/*      */   {
/*  298 */     int s = this.status;
/*  299 */     return (s >= 0) && (U.compareAndSwapInt(this, STATUS, s, s | 0x10000));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int externalAwaitDone()
/*      */   {
/*  308 */     ForkJoinPool cp = ForkJoinPool.common;
/*  309 */     int s; if ((s = this.status) >= 0) {
/*  310 */       if (cp != null) {
/*  311 */         if ((this instanceof CountedCompleter)) {
/*  312 */           s = cp.externalHelpComplete((CountedCompleter)this);
/*  313 */         } else if (cp.tryExternalUnpush(this))
/*  314 */           s = doExec();
/*      */       }
/*  316 */       if ((s >= 0) && ((s = this.status) >= 0)) {
/*  317 */         boolean interrupted = false;
/*      */         do {
/*  319 */           if (U.compareAndSwapInt(this, STATUS, s, s | 0x10000)) {
/*  320 */             synchronized (this) {
/*  321 */               if (this.status >= 0) {
/*      */                 try {
/*  323 */                   wait();
/*      */                 } catch (InterruptedException ie) {
/*  325 */                   interrupted = true;
/*      */                 }
/*      */                 
/*      */               } else
/*  329 */                 notifyAll();
/*      */             }
/*      */           }
/*  332 */         } while ((s = this.status) >= 0);
/*  333 */         if (interrupted)
/*  334 */           Thread.currentThread().interrupt();
/*      */       }
/*      */     }
/*  337 */     return s;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private int externalInterruptibleAwaitDone()
/*      */     throws InterruptedException
/*      */   {
/*  345 */     ForkJoinPool cp = ForkJoinPool.common;
/*  346 */     if (Thread.interrupted())
/*  347 */       throw new InterruptedException();
/*  348 */     int s; if (((s = this.status) >= 0) && (cp != null)) {
/*  349 */       if ((this instanceof CountedCompleter)) {
/*  350 */         cp.externalHelpComplete((CountedCompleter)this);
/*  351 */       } else if (cp.tryExternalUnpush(this))
/*  352 */         doExec();
/*      */     }
/*  354 */     while ((s = this.status) >= 0) {
/*  355 */       if (U.compareAndSwapInt(this, STATUS, s, s | 0x10000)) {
/*  356 */         synchronized (this) {
/*  357 */           if (this.status >= 0) {
/*  358 */             wait();
/*      */           } else
/*  360 */             notifyAll();
/*      */         }
/*      */       }
/*      */     }
/*  364 */     return s;
/*      */   }
/*      */   
/*      */ 
/*      */   private int doJoin()
/*      */   {
/*      */     int s;
/*      */     
/*      */     Thread t;
/*      */     
/*      */     ForkJoinWorkerThread wt;
/*      */     
/*      */     ForkJoinPool.WorkQueue w;
/*  377 */     return ((t = Thread.currentThread()) instanceof ForkJoinWorkerThread) ? wt.pool.awaitJoin(w, this) : ((w = (wt = (ForkJoinWorkerThread)t).workQueue).tryUnpush(this)) && ((s = doExec()) < 0) ? s : (s = this.status) < 0 ? s : externalAwaitDone();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private int doInvoke()
/*      */   {
/*      */     int s;
/*      */     
/*      */ 
/*      */     Thread t;
/*      */     
/*      */ 
/*      */     ForkJoinWorkerThread wt;
/*      */     
/*  392 */     return ((t = Thread.currentThread()) instanceof ForkJoinWorkerThread) ? (wt = (ForkJoinWorkerThread)t).pool.awaitJoin(wt.workQueue, this) : (s = doExec()) < 0 ? s : externalAwaitDone();
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
/*      */   static final class ExceptionNode
/*      */     extends WeakReference<ForkJoinTask<?>>
/*      */   {
/*      */     final Throwable ex;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     ExceptionNode next;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     final long thrower;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     ExceptionNode(ForkJoinTask<?> task, Throwable ex, ExceptionNode next)
/*      */     {
/*  435 */       super(ForkJoinTask.exceptionTableRefQueue);
/*  436 */       this.ex = ex;
/*  437 */       this.next = next;
/*  438 */       this.thrower = Thread.currentThread().getId();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   final int recordExceptionalCompletion(Throwable ex)
/*      */   {
/*      */     int s;
/*      */     
/*      */ 
/*  449 */     if ((s = this.status) >= 0) {
/*  450 */       int h = System.identityHashCode(this);
/*  451 */       ReentrantLock lock = exceptionTableLock;
/*  452 */       lock.lock();
/*      */       try {
/*  454 */         expungeStaleExceptions();
/*  455 */         ExceptionNode[] t = exceptionTable;
/*  456 */         int i = h & t.length - 1;
/*  457 */         for (ExceptionNode e = t[i];; e = e.next) {
/*  458 */           if (e == null) {
/*  459 */             t[i] = new ExceptionNode(this, ex, t[i]);
/*      */           }
/*      */           else
/*  462 */             if (e.get() == this)
/*      */               break;
/*      */         }
/*      */       } finally {
/*  466 */         lock.unlock();
/*      */       }
/*  468 */       s = setCompletion(Integer.MIN_VALUE);
/*      */     }
/*  470 */     return s;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int setExceptionalCompletion(Throwable ex)
/*      */   {
/*  479 */     int s = recordExceptionalCompletion(ex);
/*  480 */     if ((s & 0xF0000000) == Integer.MIN_VALUE)
/*  481 */       internalPropagateException(ex);
/*  482 */     return s;
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
/*      */   static final void cancelIgnoringExceptions(ForkJoinTask<?> t)
/*      */   {
/*  498 */     if ((t != null) && (t.status >= 0)) {
/*      */       try {
/*  500 */         t.cancel(false);
/*      */       }
/*      */       catch (Throwable ignore) {}
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void clearExceptionalCompletion()
/*      */   {
/*  510 */     int h = System.identityHashCode(this);
/*  511 */     ReentrantLock lock = exceptionTableLock;
/*  512 */     lock.lock();
/*      */     try {
/*  514 */       ExceptionNode[] t = exceptionTable;
/*  515 */       int i = h & t.length - 1;
/*  516 */       ExceptionNode e = t[i];
/*  517 */       ExceptionNode pred = null;
/*  518 */       while (e != null) {
/*  519 */         ExceptionNode next = e.next;
/*  520 */         if (e.get() == this) {
/*  521 */           if (pred == null) {
/*  522 */             t[i] = next; break;
/*      */           }
/*  524 */           pred.next = next;
/*  525 */           break;
/*      */         }
/*  527 */         pred = e;
/*  528 */         e = next;
/*      */       }
/*  530 */       expungeStaleExceptions();
/*  531 */       this.status = 0;
/*      */     } finally {
/*  533 */       lock.unlock();
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
/*      */   private Throwable getThrowableException()
/*      */   {
/*  552 */     if ((this.status & 0xF0000000) != Integer.MIN_VALUE)
/*  553 */       return null;
/*  554 */     int h = System.identityHashCode(this);
/*      */     
/*  556 */     ReentrantLock lock = exceptionTableLock;
/*  557 */     lock.lock();
/*      */     ExceptionNode e;
/*  559 */     try { expungeStaleExceptions();
/*  560 */       ExceptionNode[] t = exceptionTable;
/*  561 */       e = t[(h & t.length - 1)];
/*  562 */       while ((e != null) && (e.get() != this))
/*  563 */         e = e.next;
/*      */     } finally {
/*  565 */       lock.unlock();
/*      */     }
/*      */     Throwable ex;
/*  568 */     if ((e == null) || ((ex = e.ex) == null)) {
/*  569 */       return null;
/*      */     }
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
/*      */     Throwable ex;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  591 */     return ex;
/*      */   }
/*      */   
/*      */ 
/*      */   private static void expungeStaleExceptions()
/*      */   {
/*      */     Object x;
/*  598 */     while ((x = exceptionTableRefQueue.poll()) != null) {
/*  599 */       if ((x instanceof ExceptionNode)) {
/*  600 */         ForkJoinTask<?> key = (ForkJoinTask)((ExceptionNode)x).get();
/*  601 */         ExceptionNode[] t = exceptionTable;
/*  602 */         int i = System.identityHashCode(key) & t.length - 1;
/*  603 */         ExceptionNode e = t[i];
/*  604 */         ExceptionNode pred = null;
/*  605 */         while (e != null) {
/*  606 */           ExceptionNode next = e.next;
/*  607 */           if (e == x) {
/*  608 */             if (pred == null) {
/*  609 */               t[i] = next; break;
/*      */             }
/*  611 */             pred.next = next;
/*  612 */             break;
/*      */           }
/*  614 */           pred = e;
/*  615 */           e = next;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static final void helpExpungeStaleExceptions()
/*      */   {
/*  626 */     ReentrantLock lock = exceptionTableLock;
/*  627 */     if (lock.tryLock()) {
/*      */       try {
/*  629 */         expungeStaleExceptions();
/*      */       } finally {
/*  631 */         lock.unlock();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   static void rethrow(Throwable ex)
/*      */   {
/*  640 */     if (ex != null) {
/*  641 */       uncheckedThrow(ex);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static <T extends Throwable> void uncheckedThrow(Throwable t)
/*      */     throws Throwable
/*      */   {
/*  651 */     throw t;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void reportException(int s)
/*      */   {
/*  658 */     if (s == -1073741824)
/*  659 */       throw new CancellationException();
/*  660 */     if (s == Integer.MIN_VALUE) {
/*  661 */       rethrow(getThrowableException());
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
/*      */   public final ForkJoinTask<V> fork()
/*      */   {
/*      */     Thread t;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  683 */     if (((t = Thread.currentThread()) instanceof ForkJoinWorkerThread)) {
/*  684 */       ((ForkJoinWorkerThread)t).workQueue.push(this);
/*      */     } else
/*  686 */       ForkJoinPool.common.externalPush(this);
/*  687 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final V join()
/*      */   {
/*      */     int s;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  703 */     if ((s = doJoin() & 0xF0000000) != -268435456)
/*  704 */       reportException(s);
/*  705 */     return (V)getRawResult();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final V invoke()
/*      */   {
/*      */     int s;
/*      */     
/*      */ 
/*      */ 
/*  718 */     if ((s = doInvoke() & 0xF0000000) != -268435456)
/*  719 */       reportException(s);
/*  720 */     return (V)getRawResult();
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
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void invokeAll(ForkJoinTask<?> t1, ForkJoinTask<?> t2)
/*      */   {
/*  742 */     t2.fork();
/*  743 */     int s1; if ((s1 = t1.doInvoke() & 0xF0000000) != -268435456)
/*  744 */       t1.reportException(s1);
/*  745 */     int s2; if ((s2 = t2.doJoin() & 0xF0000000) != -268435456) {
/*  746 */       t2.reportException(s2);
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
/*      */   public static void invokeAll(ForkJoinTask<?>... tasks)
/*      */   {
/*  765 */     Throwable ex = null;
/*  766 */     int last = tasks.length - 1;
/*  767 */     for (int i = last; i >= 0; i--) {
/*  768 */       ForkJoinTask<?> t = tasks[i];
/*  769 */       if (t == null) {
/*  770 */         if (ex == null) {
/*  771 */           ex = new NullPointerException();
/*      */         }
/*  773 */       } else if (i != 0) {
/*  774 */         t.fork();
/*  775 */       } else if ((t.doInvoke() < -268435456) && (ex == null))
/*  776 */         ex = t.getException();
/*      */     }
/*  778 */     for (int i = 1; i <= last; i++) {
/*  779 */       ForkJoinTask<?> t = tasks[i];
/*  780 */       if (t != null) {
/*  781 */         if (ex != null) {
/*  782 */           t.cancel(false);
/*  783 */         } else if (t.doJoin() < -268435456)
/*  784 */           ex = t.getException();
/*      */       }
/*      */     }
/*  787 */     if (ex != null) {
/*  788 */       rethrow(ex);
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
/*      */ 
/*      */   public static <T extends ForkJoinTask<?>> Collection<T> invokeAll(Collection<T> tasks)
/*      */   {
/*  809 */     if ((!(tasks instanceof RandomAccess)) || (!(tasks instanceof List))) {
/*  810 */       invokeAll((ForkJoinTask[])tasks.toArray(new ForkJoinTask[tasks.size()]));
/*  811 */       return tasks;
/*      */     }
/*      */     
/*  814 */     List<? extends ForkJoinTask<?>> ts = (List)tasks;
/*      */     
/*  816 */     Throwable ex = null;
/*  817 */     int last = ts.size() - 1;
/*  818 */     for (int i = last; i >= 0; i--) {
/*  819 */       ForkJoinTask<?> t = (ForkJoinTask)ts.get(i);
/*  820 */       if (t == null) {
/*  821 */         if (ex == null) {
/*  822 */           ex = new NullPointerException();
/*      */         }
/*  824 */       } else if (i != 0) {
/*  825 */         t.fork();
/*  826 */       } else if ((t.doInvoke() < -268435456) && (ex == null))
/*  827 */         ex = t.getException();
/*      */     }
/*  829 */     for (int i = 1; i <= last; i++) {
/*  830 */       ForkJoinTask<?> t = (ForkJoinTask)ts.get(i);
/*  831 */       if (t != null) {
/*  832 */         if (ex != null) {
/*  833 */           t.cancel(false);
/*  834 */         } else if (t.doJoin() < -268435456)
/*  835 */           ex = t.getException();
/*      */       }
/*      */     }
/*  838 */     if (ex != null)
/*  839 */       rethrow(ex);
/*  840 */     return tasks;
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
/*      */   public boolean cancel(boolean mayInterruptIfRunning)
/*      */   {
/*  871 */     return (setCompletion(-1073741824) & 0xF0000000) == -1073741824;
/*      */   }
/*      */   
/*      */   public final boolean isDone() {
/*  875 */     return this.status < 0;
/*      */   }
/*      */   
/*      */   public final boolean isCancelled() {
/*  879 */     return (this.status & 0xF0000000) == -1073741824;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final boolean isCompletedAbnormally()
/*      */   {
/*  888 */     return this.status < -268435456;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final boolean isCompletedNormally()
/*      */   {
/*  899 */     return (this.status & 0xF0000000) == -268435456;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final Throwable getException()
/*      */   {
/*  910 */     int s = this.status & 0xF0000000;
/*  911 */     return s == -1073741824 ? new CancellationException() : s >= -268435456 ? null : getThrowableException();
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
/*      */ 
/*      */   public void completeExceptionally(Throwable ex)
/*      */   {
/*  931 */     setExceptionalCompletion(((ex instanceof RuntimeException)) || ((ex instanceof Error)) ? ex : new RuntimeException(ex));
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
/*      */   public void complete(V value)
/*      */   {
/*      */     try
/*      */     {
/*  951 */       setRawResult(value);
/*      */     } catch (Throwable rex) {
/*  953 */       setExceptionalCompletion(rex);
/*  954 */       return;
/*      */     }
/*  956 */     setCompletion(-268435456);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void quietlyComplete()
/*      */   {
/*  968 */     setCompletion(-268435456);
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
/*      */   public final V get()
/*      */     throws InterruptedException, ExecutionException
/*      */   {
/*  983 */     int s = (Thread.currentThread() instanceof ForkJoinWorkerThread) ? doJoin() : externalInterruptibleAwaitDone();
/*      */     
/*      */ 
/*  986 */     if ((s &= 0xF0000000) == -1073741824)
/*  987 */       throw new CancellationException();
/*  988 */     Throwable ex; if ((s == Integer.MIN_VALUE) && ((ex = getThrowableException()) != null))
/*  989 */       throw new ExecutionException(ex);
/*  990 */     return (V)getRawResult();
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
/*      */   public final V get(long timeout, TimeUnit unit)
/*      */     throws InterruptedException, ExecutionException, TimeoutException
/*      */   {
/* 1009 */     if (Thread.interrupted()) {
/* 1010 */       throw new InterruptedException();
/*      */     }
/*      */     
/* 1013 */     long ns = unit.toNanos(timeout);
/*      */     int s;
/* 1015 */     if (((s = this.status) >= 0) && (ns > 0L)) {
/* 1016 */       long deadline = System.nanoTime() + ns;
/* 1017 */       ForkJoinPool p = null;
/* 1018 */       ForkJoinPool.WorkQueue w = null;
/* 1019 */       Thread t = Thread.currentThread();
/* 1020 */       if ((t instanceof ForkJoinWorkerThread)) {
/* 1021 */         ForkJoinWorkerThread wt = (ForkJoinWorkerThread)t;
/* 1022 */         p = wt.pool;
/* 1023 */         w = wt.workQueue;
/* 1024 */         p.helpJoinOnce(w, this);
/*      */       } else { ForkJoinPool cp;
/* 1026 */         if ((cp = ForkJoinPool.common) != null)
/* 1027 */           if ((this instanceof CountedCompleter)) {
/* 1028 */             cp.externalHelpComplete((CountedCompleter)this);
/* 1029 */           } else if (cp.tryExternalUnpush(this))
/* 1030 */             doExec();
/*      */       }
/* 1032 */       boolean canBlock = false;
/* 1033 */       boolean interrupted = false;
/*      */       try {
/* 1035 */         while ((s = this.status) >= 0) {
/* 1036 */           if ((w != null) && (w.qlock < 0)) {
/* 1037 */             cancelIgnoringExceptions(this);
/* 1038 */           } else if (!canBlock) {
/* 1039 */             if ((p == null) || (p.tryCompensate(p.ctl)))
/* 1040 */               canBlock = true;
/*      */           } else {
/*      */             long ms;
/* 1043 */             if (((ms = TimeUnit.NANOSECONDS.toMillis(ns)) > 0L) && (U.compareAndSwapInt(this, STATUS, s, s | 0x10000)))
/*      */             {
/* 1045 */               synchronized (this) {
/* 1046 */                 if (this.status >= 0) {
/*      */                   try {
/* 1048 */                     wait(ms);
/*      */                   } catch (InterruptedException ie) {
/* 1050 */                     if (p == null) {
/* 1051 */                       interrupted = true;
/*      */                     }
/*      */                   }
/*      */                 } else
/* 1055 */                   notifyAll();
/*      */               }
/*      */             }
/* 1058 */             if (((s = this.status) >= 0) && (!interrupted)) if ((ns = deadline - System.nanoTime()) <= 0L) {
/*      */                 break;
/*      */               }
/*      */           }
/*      */         }
/*      */       } finally {
/* 1064 */         if ((p != null) && (canBlock))
/* 1065 */           p.incrementActiveCount();
/*      */       }
/* 1067 */       if (interrupted)
/* 1068 */         throw new InterruptedException();
/*      */     }
/* 1070 */     if ((s &= 0xF0000000) != -268435456)
/*      */     {
/* 1072 */       if (s == -1073741824)
/* 1073 */         throw new CancellationException();
/* 1074 */       if (s != Integer.MIN_VALUE)
/* 1075 */         throw new TimeoutException();
/* 1076 */       Throwable ex; if ((ex = getThrowableException()) != null)
/* 1077 */         throw new ExecutionException(ex);
/*      */     }
/* 1079 */     return (V)getRawResult();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void quietlyJoin()
/*      */   {
/* 1089 */     doJoin();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void quietlyInvoke()
/*      */   {
/* 1098 */     doInvoke();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void helpQuiesce()
/*      */   {
/*      */     Thread t;
/*      */     
/*      */ 
/*      */ 
/* 1110 */     if (((t = Thread.currentThread()) instanceof ForkJoinWorkerThread)) {
/* 1111 */       ForkJoinWorkerThread wt = (ForkJoinWorkerThread)t;
/* 1112 */       wt.pool.helpQuiescePool(wt.workQueue);
/*      */     }
/*      */     else {
/* 1115 */       ForkJoinPool.quiesceCommonPool();
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
/*      */   public void reinitialize()
/*      */   {
/* 1135 */     if ((this.status & 0xF0000000) == Integer.MIN_VALUE) {
/* 1136 */       clearExceptionalCompletion();
/*      */     } else {
/* 1138 */       this.status = 0;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ForkJoinPool getPool()
/*      */   {
/* 1149 */     Thread t = Thread.currentThread();
/* 1150 */     return (t instanceof ForkJoinWorkerThread) ? ((ForkJoinWorkerThread)t).pool : null;
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
/*      */   public static boolean inForkJoinPool()
/*      */   {
/* 1163 */     return Thread.currentThread() instanceof ForkJoinWorkerThread;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean tryUnfork()
/*      */   {
/*      */     Thread t;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1178 */     return ((t = Thread.currentThread()) instanceof ForkJoinWorkerThread) ? ((ForkJoinWorkerThread)t).workQueue.tryUnpush(this) : ForkJoinPool.common.tryExternalUnpush(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static int getQueuedTaskCount()
/*      */   {
/*      */     Thread t;
/*      */     
/*      */ 
/*      */     ForkJoinPool.WorkQueue q;
/*      */     
/*      */ 
/*      */     ForkJoinPool.WorkQueue q;
/*      */     
/* 1193 */     if (((t = Thread.currentThread()) instanceof ForkJoinWorkerThread)) {
/* 1194 */       q = ((ForkJoinWorkerThread)t).workQueue;
/*      */     } else
/* 1196 */       q = ForkJoinPool.commonSubmitterQueue();
/* 1197 */     return q == null ? 0 : q.queueSize();
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
/*      */   public static int getSurplusQueuedTaskCount()
/*      */   {
/* 1214 */     return ForkJoinPool.getSurplusQueuedTaskCount();
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
/*      */   protected static ForkJoinTask<?> peekNextLocalTask()
/*      */   {
/*      */     Thread t;
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
/*      */     ForkJoinPool.WorkQueue q;
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
/*      */     ForkJoinPool.WorkQueue q;
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
/* 1269 */     if (((t = Thread.currentThread()) instanceof ForkJoinWorkerThread)) {
/* 1270 */       q = ((ForkJoinWorkerThread)t).workQueue;
/*      */     } else
/* 1272 */       q = ForkJoinPool.commonSubmitterQueue();
/* 1273 */     return q == null ? null : q.peek();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static ForkJoinTask<?> pollNextLocalTask()
/*      */   {
/*      */     Thread t;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1287 */     return ((t = Thread.currentThread()) instanceof ForkJoinWorkerThread) ? ((ForkJoinWorkerThread)t).workQueue.nextLocalTask() : null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static ForkJoinTask<?> pollTask()
/*      */   {
/*      */     Thread t;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     ForkJoinWorkerThread wt;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1307 */     return ((t = Thread.currentThread()) instanceof ForkJoinWorkerThread) ? (wt = (ForkJoinWorkerThread)t).pool.nextTaskFor(wt.workQueue) : null;
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
/*      */   public final short getForkJoinTaskTag()
/*      */   {
/* 1321 */     return (short)this.status;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public final short setForkJoinTaskTag(short tag)
/*      */   {
/*      */     int s;
/*      */     
/*      */ 
/*      */ 
/* 1333 */     while (!U.compareAndSwapInt(this, STATUS, s = this.status, s & 0xFFFF0000 | tag & 0xFFFF)) {}
/*      */     
/* 1335 */     return (short)s;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final boolean compareAndSetForkJoinTaskTag(short e, short tag)
/*      */   {
/*      */     int s;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     do
/*      */     {
/* 1355 */       if ((short)(s = this.status) != e)
/* 1356 */         return false;
/* 1357 */     } while (!U.compareAndSwapInt(this, STATUS, s, s & 0xFFFF0000 | tag & 0xFFFF));
/*      */     
/* 1359 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   static final class AdaptedRunnable<T>
/*      */     extends ForkJoinTask<T>
/*      */     implements RunnableFuture<T>
/*      */   {
/*      */     final Runnable runnable;
/*      */     T result;
/*      */     private static final long serialVersionUID = 5232453952276885070L;
/*      */     
/*      */     AdaptedRunnable(Runnable runnable, T result)
/*      */     {
/* 1373 */       if (runnable == null) throw new NullPointerException();
/* 1374 */       this.runnable = runnable;
/* 1375 */       this.result = result; }
/*      */     
/* 1377 */     public final T getRawResult() { return (T)this.result; }
/* 1378 */     public final void setRawResult(T v) { this.result = v; }
/* 1379 */     public final boolean exec() { this.runnable.run();return true; }
/* 1380 */     public final void run() { invoke(); }
/*      */   }
/*      */   
/*      */   static final class AdaptedRunnableAction
/*      */     extends ForkJoinTask<Void> implements RunnableFuture<Void>
/*      */   {
/*      */     final Runnable runnable;
/*      */     private static final long serialVersionUID = 5232453952276885070L;
/*      */     
/*      */     AdaptedRunnableAction(Runnable runnable)
/*      */     {
/* 1391 */       if (runnable == null) throw new NullPointerException();
/* 1392 */       this.runnable = runnable; }
/*      */     
/* 1394 */     public final Void getRawResult() { return null; }
/*      */     public final void setRawResult(Void v) {}
/* 1396 */     public final boolean exec() { this.runnable.run();return true; }
/* 1397 */     public final void run() { invoke(); }
/*      */   }
/*      */   
/*      */   static final class RunnableExecuteAction extends ForkJoinTask<Void>
/*      */   {
/*      */     final Runnable runnable;
/*      */     private static final long serialVersionUID = 5232453952276885070L;
/*      */     
/*      */     RunnableExecuteAction(Runnable runnable)
/*      */     {
/* 1407 */       if (runnable == null) throw new NullPointerException();
/* 1408 */       this.runnable = runnable; }
/*      */     
/* 1410 */     public final Void getRawResult() { return null; }
/*      */     public final void setRawResult(Void v) {}
/* 1412 */     public final boolean exec() { this.runnable.run();return true; }
/*      */     
/* 1414 */     void internalPropagateException(Throwable ex) { rethrow(ex); }
/*      */   }
/*      */   
/*      */   static final class AdaptedCallable<T>
/*      */     extends ForkJoinTask<T>
/*      */     implements RunnableFuture<T>
/*      */   {
/*      */     final Callable<? extends T> callable;
/*      */     T result;
/*      */     private static final long serialVersionUID = 2838392045355241008L;
/*      */     
/*      */     AdaptedCallable(Callable<? extends T> callable)
/*      */     {
/* 1427 */       if (callable == null) throw new NullPointerException();
/* 1428 */       this.callable = callable; }
/*      */     
/* 1430 */     public final T getRawResult() { return (T)this.result; }
/* 1431 */     public final void setRawResult(T v) { this.result = v; }
/*      */     
/*      */     public final boolean exec() {
/* 1434 */       try { this.result = this.callable.call();
/* 1435 */         return true;
/*      */       } catch (Error err) {
/* 1437 */         throw err;
/*      */       } catch (RuntimeException rex) {
/* 1439 */         throw rex;
/*      */       } catch (Exception ex) {
/* 1441 */         throw new RuntimeException(ex);
/*      */       } }
/*      */     
/* 1444 */     public final void run() { invoke(); }
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
/*      */   public static ForkJoinTask<?> adapt(Runnable runnable)
/*      */   {
/* 1457 */     return new AdaptedRunnableAction(runnable);
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
/*      */   public static <T> ForkJoinTask<T> adapt(Runnable runnable, T result)
/*      */   {
/* 1470 */     return new AdaptedRunnable(runnable, result);
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
/*      */   public static <T> ForkJoinTask<T> adapt(Callable<? extends T> callable)
/*      */   {
/* 1483 */     return new AdaptedCallable(callable);
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
/*      */   private void writeObject(ObjectOutputStream s)
/*      */     throws IOException
/*      */   {
/* 1498 */     s.defaultWriteObject();
/* 1499 */     s.writeObject(getException());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void readObject(ObjectInputStream s)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1507 */     s.defaultReadObject();
/* 1508 */     Object ex = s.readObject();
/* 1509 */     if (ex != null) {
/* 1510 */       setExceptionalCompletion((Throwable)ex);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1518 */   private static final ReentrantLock exceptionTableLock = new ReentrantLock();
/* 1519 */   private static final ReferenceQueue<Object> exceptionTableRefQueue = new ReferenceQueue();
/* 1520 */   static { exceptionTable = new ExceptionNode[32];
/*      */     try {
/* 1522 */       U = getUnsafe();
/* 1523 */       Class<?> k = ForkJoinTask.class;
/* 1524 */       STATUS = U.objectFieldOffset(k.getDeclaredField("status"));
/*      */     }
/*      */     catch (Exception e) {
/* 1527 */       throw new Error(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static final int EXCEPTION_MAP_CAPACITY = 32;
/*      */   private static final long serialVersionUID = -7721805057305804111L;
/*      */   private static final Unsafe U;
/*      */   private static final long STATUS;
/*      */   private static Unsafe getUnsafe()
/*      */   {
/*      */     try
/*      */     {
/* 1540 */       return Unsafe.getUnsafe();
/*      */     } catch (SecurityException tryReflectionInstead) {
/*      */       try {
/* 1543 */         (Unsafe)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */         {
/*      */           public Unsafe run() throws Exception {
/* 1546 */             Class<Unsafe> k = Unsafe.class;
/* 1547 */             for (Field f : k.getDeclaredFields()) {
/* 1548 */               f.setAccessible(true);
/* 1549 */               Object x = f.get(null);
/* 1550 */               if (k.isInstance(x))
/* 1551 */                 return (Unsafe)k.cast(x);
/*      */             }
/* 1553 */             throw new NoSuchFieldError("the Unsafe");
/*      */           }
/*      */         });
/* 1556 */       } catch (PrivilegedActionException e) { throw new RuntimeException("Could not initialize intrinsics", e.getCause());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   void internalPropagateException(Throwable ex) {}
/*      */   
/*      */   public abstract V getRawResult();
/*      */   
/*      */   protected abstract void setRawResult(V paramV);
/*      */   
/*      */   protected abstract boolean exec();
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\chmv8\ForkJoinTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */