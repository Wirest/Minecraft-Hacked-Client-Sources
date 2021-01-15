/*      */ package io.netty.util.internal.chmv8;
/*      */ 
/*      */ import io.netty.util.internal.ThreadLocalRandom;
/*      */ import java.lang.reflect.Field;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.concurrent.AbstractExecutorService;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.RejectedExecutionException;
/*      */ import java.util.concurrent.RunnableFuture;
/*      */ import java.util.concurrent.TimeUnit;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ForkJoinPool
/*      */   extends AbstractExecutorService
/*      */ {
/*      */   static final ThreadLocal<Submitter> submitters;
/*      */   public static final ForkJoinWorkerThreadFactory defaultForkJoinWorkerThreadFactory;
/*      */   private static final RuntimePermission modifyThreadPermission;
/*      */   static final ForkJoinPool common;
/*      */   static final int commonParallelism;
/*      */   private static int poolNumberSequence;
/*      */   private static final long IDLE_TIMEOUT = 2000000000L;
/*      */   private static final long FAST_IDLE_TIMEOUT = 200000000L;
/*      */   private static final long TIMEOUT_SLOP = 2000000L;
/*      */   private static final int MAX_HELP = 64;
/*      */   private static final int SEED_INCREMENT = 1640531527;
/*      */   private static final int AC_SHIFT = 48;
/*      */   private static final int TC_SHIFT = 32;
/*      */   private static final int ST_SHIFT = 31;
/*      */   private static final int EC_SHIFT = 16;
/*      */   private static final int SMASK = 65535;
/*      */   private static final int MAX_CAP = 32767;
/*      */   private static final int EVENMASK = 65534;
/*      */   private static final int SQMASK = 126;
/*      */   private static final int SHORT_SIGN = 32768;
/*      */   private static final int INT_SIGN = Integer.MIN_VALUE;
/*      */   private static final long STOP_BIT = 2147483648L;
/*      */   private static final long AC_MASK = -281474976710656L;
/*      */   private static final long TC_MASK = 281470681743360L;
/*      */   private static final long TC_UNIT = 4294967296L;
/*      */   private static final long AC_UNIT = 281474976710656L;
/*      */   private static final int UAC_SHIFT = 16;
/*      */   private static final int UTC_SHIFT = 0;
/*      */   private static final int UAC_MASK = -65536;
/*      */   private static final int UTC_MASK = 65535;
/*      */   private static final int UAC_UNIT = 65536;
/*      */   private static final int UTC_UNIT = 1;
/*      */   private static final int E_MASK = Integer.MAX_VALUE;
/*      */   private static final int E_SEQ = 65536;
/*      */   private static final int SHUTDOWN = Integer.MIN_VALUE;
/*      */   private static final int PL_LOCK = 2;
/*      */   private static final int PL_SIGNAL = 1;
/*      */   private static final int PL_SPINS = 256;
/*      */   static final int LIFO_QUEUE = 0;
/*      */   static final int FIFO_QUEUE = 1;
/*      */   static final int SHARED_QUEUE = -1;
/*      */   volatile long pad00;
/*      */   volatile long pad01;
/*      */   volatile long pad02;
/*      */   volatile long pad03;
/*      */   volatile long pad04;
/*      */   volatile long pad05;
/*      */   volatile long pad06;
/*      */   volatile long stealCount;
/*      */   volatile long ctl;
/*      */   volatile int plock;
/*      */   volatile int indexSeed;
/*      */   final short parallelism;
/*      */   final short mode;
/*      */   WorkQueue[] workQueues;
/*      */   final ForkJoinWorkerThreadFactory factory;
/*      */   final Thread.UncaughtExceptionHandler ueh;
/*      */   final String workerNamePrefix;
/*      */   volatile Object pad10;
/*      */   volatile Object pad11;
/*      */   volatile Object pad12;
/*      */   volatile Object pad13;
/*      */   volatile Object pad14;
/*      */   volatile Object pad15;
/*      */   volatile Object pad16;
/*      */   volatile Object pad17;
/*      */   volatile Object pad18;
/*      */   volatile Object pad19;
/*      */   volatile Object pad1a;
/*      */   volatile Object pad1b;
/*      */   private static final Unsafe U;
/*      */   private static final long CTL;
/*      */   private static final long PARKBLOCKER;
/*      */   private static final int ABASE;
/*      */   private static final int ASHIFT;
/*      */   private static final long STEALCOUNT;
/*      */   private static final long PLOCK;
/*      */   private static final long INDEXSEED;
/*      */   private static final long QBASE;
/*      */   private static final long QLOCK;
/*      */   
/*      */   private static void checkPermission()
/*      */   {
/*  534 */     SecurityManager security = System.getSecurityManager();
/*  535 */     if (security != null) {
/*  536 */       security.checkPermission(modifyThreadPermission);
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
/*      */   public static abstract interface ForkJoinWorkerThreadFactory
/*      */   {
/*      */     public abstract ForkJoinWorkerThread newThread(ForkJoinPool paramForkJoinPool);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final class DefaultForkJoinWorkerThreadFactory
/*      */     implements ForkJoinPool.ForkJoinWorkerThreadFactory
/*      */   {
/*      */     public final ForkJoinWorkerThread newThread(ForkJoinPool pool)
/*      */     {
/*  565 */       return new ForkJoinWorkerThread(pool);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   static final class EmptyTask
/*      */     extends ForkJoinTask<Void>
/*      */   {
/*      */     private static final long serialVersionUID = -7721805057305804111L;
/*      */     
/*      */ 
/*  577 */     EmptyTask() { this.status = -268435456; }
/*  578 */     public final Void getRawResult() { return null; }
/*      */     public final void setRawResult(Void x) {}
/*  580 */     public final boolean exec() { return true; }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   static final class WorkQueue
/*      */   {
/*      */     static final int INITIAL_QUEUE_CAPACITY = 8192;
/*      */     
/*      */     static final int MAXIMUM_QUEUE_CAPACITY = 67108864;
/*      */     
/*      */     volatile long pad00;
/*      */     
/*      */     volatile long pad01;
/*      */     
/*      */     volatile long pad02;
/*      */     
/*      */     volatile long pad03;
/*      */     
/*      */     volatile long pad04;
/*      */     
/*      */     volatile long pad05;
/*      */     
/*      */     volatile long pad06;
/*      */     
/*      */     volatile int eventCount;
/*      */     
/*      */     int nextWait;
/*      */     
/*      */     int nsteals;
/*      */     
/*      */     int hint;
/*      */     
/*      */     short poolIndex;
/*      */     
/*      */     final short mode;
/*      */     
/*      */     volatile int qlock;
/*      */     
/*      */     volatile int base;
/*      */     
/*      */     int top;
/*      */     
/*      */     ForkJoinTask<?>[] array;
/*      */     
/*      */     final ForkJoinPool pool;
/*      */     
/*      */     final ForkJoinWorkerThread owner;
/*      */     
/*      */     volatile Thread parker;
/*      */     
/*      */     volatile ForkJoinTask<?> currentJoin;
/*      */     
/*      */     ForkJoinTask<?> currentSteal;
/*      */     
/*      */     volatile Object pad10;
/*      */     
/*      */     volatile Object pad11;
/*      */     
/*      */     volatile Object pad12;
/*      */     
/*      */     volatile Object pad13;
/*      */     
/*      */     volatile Object pad14;
/*      */     
/*      */     volatile Object pad15;
/*      */     
/*      */     volatile Object pad16;
/*      */     
/*      */     volatile Object pad17;
/*      */     
/*      */     volatile Object pad18;
/*      */     
/*      */     volatile Object pad19;
/*      */     
/*      */     volatile Object pad1a;
/*      */     
/*      */     volatile Object pad1b;
/*      */     
/*      */     volatile Object pad1c;
/*      */     
/*      */     volatile Object pad1d;
/*      */     
/*      */     private static final Unsafe U;
/*      */     
/*      */     private static final long QBASE;
/*      */     
/*      */     private static final long QLOCK;
/*      */     
/*      */     private static final int ABASE;
/*      */     
/*      */     private static final int ASHIFT;
/*      */     
/*      */ 
/*      */     WorkQueue(ForkJoinPool pool, ForkJoinWorkerThread owner, int mode, int seed)
/*      */     {
/*  676 */       this.pool = pool;
/*  677 */       this.owner = owner;
/*  678 */       this.mode = ((short)mode);
/*  679 */       this.hint = seed;
/*      */       
/*  681 */       this.base = (this.top = 'က');
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     final int queueSize()
/*      */     {
/*  688 */       int n = this.base - this.top;
/*  689 */       return n >= 0 ? 0 : -n;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     final boolean isEmpty()
/*      */     {
/*      */       int s;
/*      */       
/*      */ 
/*  699 */       int n = this.base - (s = this.top);
/*  700 */       ForkJoinTask<?>[] a; int m; return (n >= 0) || ((n == -1) && (((a = this.array) == null) || ((m = a.length - 1) < 0) || (U.getObject(a, ((m & s - 1) << ASHIFT) + ABASE) == null)));
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
/*      */ 
/*      */ 
/*      */ 
/*      */     final void push(ForkJoinTask<?> task)
/*      */     {
/*  717 */       int s = this.top;
/*  718 */       ForkJoinTask<?>[] a; if ((a = this.array) != null) {
/*  719 */         int m = a.length - 1;
/*  720 */         U.putOrderedObject(a, ((m & s) << ASHIFT) + ABASE, task);
/*  721 */         int n; if ((n = (this.top = s + 1) - this.base) <= 2) { ForkJoinPool p;
/*  722 */           (p = this.pool).signalWork(p.workQueues, this);
/*  723 */         } else if (n >= m) {
/*  724 */           growArray();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     final ForkJoinTask<?>[] growArray()
/*      */     {
/*  734 */       ForkJoinTask<?>[] oldA = this.array;
/*  735 */       int size = oldA != null ? oldA.length << 1 : 8192;
/*  736 */       if (size > 67108864) {
/*  737 */         throw new RejectedExecutionException("Queue capacity exceeded");
/*      */       }
/*  739 */       ForkJoinTask<?>[] a = this.array = new ForkJoinTask[size];
/*  740 */       int oldMask; int t; int b; if ((oldA != null) && ((oldMask = oldA.length - 1) >= 0) && ((t = this.top) - (b = this.base) > 0))
/*      */       {
/*  742 */         int mask = size - 1;
/*      */         do
/*      */         {
/*  745 */           int oldj = ((b & oldMask) << ASHIFT) + ABASE;
/*  746 */           int j = ((b & mask) << ASHIFT) + ABASE;
/*  747 */           ForkJoinTask<?> x = (ForkJoinTask)U.getObjectVolatile(oldA, oldj);
/*  748 */           if ((x != null) && (U.compareAndSwapObject(oldA, oldj, x, null)))
/*      */           {
/*  750 */             U.putObjectVolatile(a, j, x); }
/*  751 */           b++; } while (b != t);
/*      */       }
/*  753 */       return a;
/*      */     }
/*      */     
/*      */ 
/*      */     final ForkJoinTask<?> pop()
/*      */     {
/*      */       ForkJoinTask<?>[] a;
/*      */       
/*      */       int m;
/*  762 */       if (((a = this.array) != null) && ((m = a.length - 1) >= 0)) { int s;
/*  763 */         while ((s = this.top - 1) - this.base >= 0) {
/*  764 */           long j = ((m & s) << ASHIFT) + ABASE;
/*  765 */           ForkJoinTask<?> t; if ((t = (ForkJoinTask)U.getObject(a, j)) == null)
/*      */             break;
/*  767 */           if (U.compareAndSwapObject(a, j, t, null)) {
/*  768 */             this.top = s;
/*  769 */             return t;
/*      */           }
/*      */         }
/*      */       }
/*  773 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     final ForkJoinTask<?> pollAt(int b)
/*      */     {
/*      */       ForkJoinTask<?>[] a;
/*      */       
/*      */ 
/*  783 */       if ((a = this.array) != null) {
/*  784 */         int j = ((a.length - 1 & b) << ASHIFT) + ABASE;
/*  785 */         ForkJoinTask<?> t; if (((t = (ForkJoinTask)U.getObjectVolatile(a, j)) != null) && (this.base == b) && (U.compareAndSwapObject(a, j, t, null)))
/*      */         {
/*  787 */           U.putOrderedInt(this, QBASE, b + 1);
/*  788 */           return t;
/*      */         }
/*      */       }
/*  791 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */     final ForkJoinTask<?> poll()
/*      */     {
/*      */       int b;
/*      */       ForkJoinTask<?>[] a;
/*  799 */       while (((b = this.base) - this.top < 0) && ((a = this.array) != null)) {
/*  800 */         int j = ((a.length - 1 & b) << ASHIFT) + ABASE;
/*  801 */         ForkJoinTask<?> t = (ForkJoinTask)U.getObjectVolatile(a, j);
/*  802 */         if (t != null) {
/*  803 */           if (U.compareAndSwapObject(a, j, t, null)) {
/*  804 */             U.putOrderedInt(this, QBASE, b + 1);
/*  805 */             return t;
/*      */           }
/*      */         }
/*  808 */         else if (this.base == b) {
/*  809 */           if (b + 1 == this.top)
/*      */             break;
/*  811 */           Thread.yield();
/*      */         }
/*      */       }
/*  814 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     final ForkJoinTask<?> nextLocalTask()
/*      */     {
/*  821 */       return this.mode == 0 ? pop() : poll();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     final ForkJoinTask<?> peek()
/*      */     {
/*  828 */       ForkJoinTask<?>[] a = this.array;
/*  829 */       int m; if ((a == null) || ((m = a.length - 1) < 0))
/*  830 */         return null;
/*  831 */       int m; int i = this.mode == 0 ? this.top - 1 : this.base;
/*  832 */       int j = ((i & m) << ASHIFT) + ABASE;
/*  833 */       return (ForkJoinTask)U.getObjectVolatile(a, j);
/*      */     }
/*      */     
/*      */ 
/*      */     final boolean tryUnpush(ForkJoinTask<?> t)
/*      */     {
/*      */       ForkJoinTask<?>[] a;
/*      */       
/*      */       int s;
/*  842 */       if (((a = this.array) != null) && ((s = this.top) != this.base) && (U.compareAndSwapObject(a, ((a.length - 1 & --s) << ASHIFT) + ABASE, t, null)))
/*      */       {
/*      */ 
/*  845 */         this.top = s;
/*  846 */         return true;
/*      */       }
/*  848 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     final void cancelAll()
/*      */     {
/*  855 */       ForkJoinTask.cancelIgnoringExceptions(this.currentJoin);
/*  856 */       ForkJoinTask.cancelIgnoringExceptions(this.currentSteal);
/*  857 */       ForkJoinTask<?> t; while ((t = poll()) != null) {
/*  858 */         ForkJoinTask.cancelIgnoringExceptions(t);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     final void pollAndExecAll()
/*      */     {
/*      */       ForkJoinTask<?> t;
/*      */       
/*  867 */       while ((t = poll()) != null) {
/*  868 */         t.doExec();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     final void runTask(ForkJoinTask<?> task)
/*      */     {
/*  876 */       if ((this.currentSteal = task) != null) {
/*  877 */         task.doExec();
/*  878 */         ForkJoinTask<?>[] a = this.array;
/*  879 */         int md = this.mode;
/*  880 */         this.nsteals += 1;
/*  881 */         this.currentSteal = null;
/*  882 */         if (md != 0) {
/*  883 */           pollAndExecAll();
/*  884 */         } else if (a != null) {
/*  885 */           int m = a.length - 1;
/*  886 */           int s; while ((s = this.top - 1) - this.base >= 0) {
/*  887 */             long i = ((m & s) << ASHIFT) + ABASE;
/*  888 */             ForkJoinTask<?> t = (ForkJoinTask)U.getObject(a, i);
/*  889 */             if (t == null)
/*      */               break;
/*  891 */             if (U.compareAndSwapObject(a, i, t, null)) {
/*  892 */               this.top = s;
/*  893 */               t.doExec();
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     final boolean tryRemoveAndExec(ForkJoinTask<?> task)
/*      */     {
/*      */       ForkJoinTask<?>[] a;
/*      */       
/*      */       int m;
/*      */       int s;
/*      */       int b;
/*      */       int n;
/*      */       boolean stat;
/*  910 */       if ((task != null) && ((a = this.array) != null) && ((m = a.length - 1) >= 0) && ((n = (s = this.top) - (b = this.base)) > 0))
/*      */       {
/*  912 */         boolean removed = false;boolean empty = true;
/*  913 */         boolean stat = true;
/*      */         for (;;) {
/*  915 */           s--;long j = ((s & m) << ASHIFT) + ABASE;
/*  916 */           ForkJoinTask<?> t = (ForkJoinTask)U.getObject(a, j);
/*  917 */           if (t == null)
/*      */             break;
/*  919 */           if (t == task) {
/*  920 */             if (s + 1 == this.top) {
/*  921 */               if (!U.compareAndSwapObject(a, j, task, null))
/*      */                 break;
/*  923 */               this.top = s;
/*  924 */               removed = true; break;
/*      */             }
/*  926 */             if (this.base != b) break;
/*  927 */             removed = U.compareAndSwapObject(a, j, task, new ForkJoinPool.EmptyTask()); break;
/*      */           }
/*      */           
/*      */ 
/*  931 */           if (t.status >= 0) {
/*  932 */             empty = false;
/*  933 */           } else if (s + 1 == this.top) {
/*  934 */             if (!U.compareAndSwapObject(a, j, t, null)) break;
/*  935 */             this.top = s; break;
/*      */           }
/*      */           
/*  938 */           n--; if (n == 0) {
/*  939 */             if ((empty) || (this.base != b)) break;
/*  940 */             stat = false; break;
/*      */           }
/*      */         }
/*      */         
/*  944 */         if (removed) {
/*  945 */           task.doExec();
/*      */         }
/*      */       } else {
/*  948 */         stat = false; }
/*  949 */       return stat;
/*      */     }
/*      */     
/*      */ 
/*      */     final boolean pollAndExecCC(CountedCompleter<?> root)
/*      */     {
/*      */       int b;
/*      */       
/*      */       ForkJoinTask<?>[] a;
/*  958 */       if (((b = this.base) - this.top < 0) && ((a = this.array) != null)) {
/*  959 */         long j = ((a.length - 1 & b) << ASHIFT) + ABASE;
/*  960 */         Object o; if ((o = U.getObjectVolatile(a, j)) == null)
/*  961 */           return true;
/*  962 */         if ((o instanceof CountedCompleter)) {
/*  963 */           CountedCompleter<?> t = (CountedCompleter)o;CountedCompleter<?> r = t;
/*  964 */           for (;;) { if (r == root) {
/*  965 */               if ((this.base == b) && (U.compareAndSwapObject(a, j, t, null)))
/*      */               {
/*  967 */                 U.putOrderedInt(this, QBASE, b + 1);
/*  968 */                 t.doExec();
/*      */               }
/*  970 */               return true;
/*      */             }
/*  972 */             if ((r = r.completer) == null)
/*      */               break;
/*      */           }
/*      */         }
/*      */       }
/*  977 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */     final boolean externalPopAndExecCC(CountedCompleter<?> root)
/*      */     {
/*      */       int s;
/*      */       
/*      */       ForkJoinTask<?>[] a;
/*  986 */       if ((this.base - (s = this.top) < 0) && ((a = this.array) != null)) {
/*  987 */         long j = ((a.length - 1 & s - 1) << ASHIFT) + ABASE;
/*  988 */         Object o; if (((o = U.getObject(a, j)) instanceof CountedCompleter)) {
/*  989 */           CountedCompleter<?> t = (CountedCompleter)o;CountedCompleter<?> r = t;
/*  990 */           for (;;) { if (r == root) {
/*  991 */               if (U.compareAndSwapInt(this, QLOCK, 0, 1))
/*  992 */                 if ((this.top == s) && (this.array == a) && (U.compareAndSwapObject(a, j, t, null)))
/*      */                 {
/*  994 */                   this.top = (s - 1);
/*  995 */                   this.qlock = 0;
/*  996 */                   t.doExec();
/*      */                 }
/*      */                 else {
/*  999 */                   this.qlock = 0;
/*      */                 }
/* 1001 */               return true;
/*      */             }
/* 1003 */             if ((r = r.completer) == null)
/*      */               break;
/*      */           }
/*      */         }
/*      */       }
/* 1008 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */     final boolean internalPopAndExecCC(CountedCompleter<?> root)
/*      */     {
/*      */       int s;
/*      */       ForkJoinTask<?>[] a;
/* 1016 */       if ((this.base - (s = this.top) < 0) && ((a = this.array) != null)) {
/* 1017 */         long j = ((a.length - 1 & s - 1) << ASHIFT) + ABASE;
/* 1018 */         Object o; if (((o = U.getObject(a, j)) instanceof CountedCompleter)) {
/* 1019 */           CountedCompleter<?> t = (CountedCompleter)o;CountedCompleter<?> r = t;
/* 1020 */           for (;;) { if (r == root) {
/* 1021 */               if (U.compareAndSwapObject(a, j, t, null)) {
/* 1022 */                 this.top = (s - 1);
/* 1023 */                 t.doExec();
/*      */               }
/* 1025 */               return true;
/*      */             }
/* 1027 */             if ((r = r.completer) == null)
/*      */               break;
/*      */           }
/*      */         }
/*      */       }
/* 1032 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */     final boolean isApparentlyUnblocked()
/*      */     {
/*      */       Thread wt;
/*      */       Thread.State s;
/* 1040 */       return (this.eventCount >= 0) && ((wt = this.owner) != null) && ((s = wt.getState()) != Thread.State.BLOCKED) && (s != Thread.State.WAITING) && (s != Thread.State.TIMED_WAITING);
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
/*      */     static
/*      */     {
/*      */       try
/*      */       {
/* 1055 */         U = ForkJoinPool.access$000();
/* 1056 */         Class<?> k = WorkQueue.class;
/* 1057 */         Class<?> ak = ForkJoinTask[].class;
/* 1058 */         QBASE = U.objectFieldOffset(k.getDeclaredField("base"));
/*      */         
/* 1060 */         QLOCK = U.objectFieldOffset(k.getDeclaredField("qlock"));
/*      */         
/* 1062 */         ABASE = U.arrayBaseOffset(ak);
/* 1063 */         int scale = U.arrayIndexScale(ak);
/* 1064 */         if ((scale & scale - 1) != 0)
/* 1065 */           throw new Error("data type scale not a power of two");
/* 1066 */         ASHIFT = 31 - Integer.numberOfLeadingZeros(scale);
/*      */       } catch (Exception e) {
/* 1068 */         throw new Error(e);
/*      */       }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final synchronized int nextPoolId()
/*      */   {
/* 1123 */     return ++poolNumberSequence;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int acquirePlock()
/*      */   {
/* 1275 */     int spins = 256;
/*      */     for (;;) { int ps;
/* 1277 */       int nps; if ((((ps = this.plock) & 0x2) == 0) && (U.compareAndSwapInt(this, PLOCK, ps, nps = ps + 2)))
/*      */       {
/* 1279 */         return nps; }
/* 1280 */       if (spins >= 0) {
/* 1281 */         if (ThreadLocalRandom.current().nextInt() >= 0) {
/* 1282 */           spins--;
/*      */         }
/* 1284 */       } else if (U.compareAndSwapInt(this, PLOCK, ps, ps | 0x1)) {
/* 1285 */         synchronized (this) {
/* 1286 */           if ((this.plock & 0x1) != 0) {
/*      */             try {
/* 1288 */               wait();
/*      */             } catch (InterruptedException ie) {
/*      */               try {
/* 1291 */                 Thread.currentThread().interrupt();
/*      */ 
/*      */               }
/*      */               catch (SecurityException ignore) {}
/*      */             }
/*      */           } else {
/* 1297 */             notifyAll();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void releasePlock(int ps)
/*      */   {
/* 1308 */     this.plock = ps;
/* 1309 */     synchronized (this) { notifyAll();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void tryAddWorker()
/*      */   {
/*      */     long c;
/*      */     int u;
/*      */     int e;
/* 1319 */     while (((u = (int)((c = this.ctl) >>> 32)) < 0) && ((u & 0x8000) != 0) && ((e = (int)c) >= 0)) {
/* 1320 */       long nc = (u + 1 & 0xFFFF | u + 65536 & 0xFFFF0000) << 32 | e;
/*      */       
/* 1322 */       if (U.compareAndSwapLong(this, CTL, c, nc))
/*      */       {
/* 1324 */         Throwable ex = null;
/* 1325 */         ForkJoinWorkerThread wt = null;
/*      */         try { ForkJoinWorkerThreadFactory fac;
/* 1327 */           if (((fac = this.factory) != null) && ((wt = fac.newThread(this)) != null))
/*      */           {
/* 1329 */             wt.start();
/* 1330 */             break;
/*      */           }
/*      */         } catch (Throwable rex) {
/* 1333 */           ex = rex;
/*      */         }
/* 1335 */         deregisterWorker(wt, ex);
/* 1336 */         break;
/*      */       }
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
/*      */   final WorkQueue registerWorker(ForkJoinWorkerThread wt)
/*      */   {
/* 1355 */     wt.setDaemon(true);
/* 1356 */     Thread.UncaughtExceptionHandler handler; if ((handler = this.ueh) != null)
/* 1357 */       wt.setUncaughtExceptionHandler(handler);
/*      */     int s;
/* 1359 */     do { s += 1640531527; } while ((!U.compareAndSwapInt(this, INDEXSEED, s = this.indexSeed, s)) || (s == 0));
/*      */     
/* 1361 */     WorkQueue w = new WorkQueue(this, wt, this.mode, s);
/* 1362 */     int ps; if ((((ps = this.plock) & 0x2) != 0) || (!U.compareAndSwapInt(this, PLOCK, , ps)))
/*      */     {
/* 1364 */       ps = acquirePlock(); }
/* 1365 */     int nps = ps & 0x80000000 | ps + 2 & 0x7FFFFFFF;
/*      */     try { WorkQueue[] ws;
/* 1367 */       if ((ws = this.workQueues) != null) {
/* 1368 */         int n = ws.length;int m = n - 1;
/* 1369 */         int r = s << 1 | 0x1;
/* 1370 */         if (ws[(r &= m)] != null) {
/* 1371 */           int probes = 0;
/* 1372 */           int step = n <= 4 ? 2 : (n >>> 1 & 0xFFFE) + 2;
/* 1373 */           while (ws[(r = r + step & m)] != null) {
/* 1374 */             probes++; if (probes >= n) {
/* 1375 */               this.workQueues = (ws = (WorkQueue[])Arrays.copyOf(ws, n <<= 1));
/* 1376 */               m = n - 1;
/* 1377 */               probes = 0;
/*      */             }
/*      */           }
/*      */         }
/* 1381 */         w.poolIndex = ((short)r);
/* 1382 */         w.eventCount = r;
/* 1383 */         ws[r] = w;
/*      */       }
/*      */     } finally {
/* 1386 */       if (!U.compareAndSwapInt(this, PLOCK, ps, nps))
/* 1387 */         releasePlock(nps);
/*      */     }
/* 1389 */     wt.setName(this.workerNamePrefix.concat(Integer.toString(w.poolIndex >>> 1)));
/* 1390 */     return w;
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
/*      */   final void deregisterWorker(ForkJoinWorkerThread wt, Throwable ex)
/*      */   {
/* 1403 */     WorkQueue w = null;
/* 1404 */     if ((wt != null) && ((w = wt.workQueue) != null))
/*      */     {
/* 1406 */       w.qlock = -1;
/* 1407 */       long sc; while (!U.compareAndSwapLong(this, STEALCOUNT, sc = this.stealCount, sc + w.nsteals)) {}
/*      */       
/*      */       int ps;
/* 1410 */       if ((((ps = this.plock) & 0x2) != 0) || (!U.compareAndSwapInt(this, PLOCK, , ps)))
/*      */       {
/* 1412 */         ps = acquirePlock(); }
/* 1413 */       int nps = ps & 0x80000000 | ps + 2 & 0x7FFFFFFF;
/*      */       try {
/* 1415 */         int idx = w.poolIndex;
/* 1416 */         WorkQueue[] ws = this.workQueues;
/* 1417 */         if ((ws != null) && (idx >= 0) && (idx < ws.length) && (ws[idx] == w))
/* 1418 */           ws[idx] = null;
/*      */       } finally {
/* 1420 */         if (!U.compareAndSwapInt(this, PLOCK, ps, nps)) {
/* 1421 */           releasePlock(nps);
/*      */         }
/*      */       }
/*      */     }
/*      */     long c;
/* 1426 */     while (!U.compareAndSwapLong(this, CTL, c = this.ctl, c - 281474976710656L & 0xFFFF000000000000 | c - 4294967296L & 0xFFFF00000000 | c & 0xFFFFFFFF)) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1431 */     if ((!tryTerminate(false, false)) && (w != null) && (w.array != null)) {
/* 1432 */       w.cancelAll();
/*      */       int u;
/* 1434 */       int e; while (((u = (int)((c = this.ctl) >>> 32)) < 0) && ((e = (int)c) >= 0)) {
/* 1435 */         if (e > 0) { WorkQueue[] ws;
/* 1436 */           int i; WorkQueue v; if (((ws = this.workQueues) != null) && ((i = e & 0xFFFF) < ws.length) && ((v = ws[i]) != null))
/*      */           {
/*      */ 
/*      */ 
/* 1440 */             long nc = v.nextWait & 0x7FFFFFFF | u + 65536 << 32;
/*      */             
/* 1442 */             if (v.eventCount == (e | 0x80000000))
/*      */             {
/* 1444 */               if (U.compareAndSwapLong(this, CTL, c, nc)) {
/* 1445 */                 v.eventCount = (e + 65536 & 0x7FFFFFFF);
/* 1446 */                 Thread p; if ((p = v.parker) == null) break;
/* 1447 */                 U.unpark(p);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/* 1452 */         else if ((short)u < 0) {
/* 1453 */           tryAddWorker();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1458 */     if (ex == null) {
/* 1459 */       ForkJoinTask.helpExpungeStaleExceptions();
/*      */     } else {
/* 1461 */       ForkJoinTask.rethrow(ex);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final class Submitter
/*      */   {
/*      */     int seed;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     Submitter(int s)
/*      */     {
/* 1483 */       this.seed = s;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final void externalPush(ForkJoinTask<?> task)
/*      */   {
/* 1495 */     Submitter z = (Submitter)submitters.get();
/*      */     
/* 1497 */     int ps = this.plock;
/* 1498 */     WorkQueue[] ws = this.workQueues;
/* 1499 */     int m; int r; WorkQueue q; if ((z != null) && (ps > 0) && (ws != null) && ((m = ws.length - 1) >= 0) && ((q = ws[(m & (r = z.seed) & 0x7E)]) != null) && (r != 0) && (U.compareAndSwapInt(q, QLOCK, 0, 1))) { ForkJoinTask<?>[] a;
/*      */       int am;
/*      */       int s;
/* 1502 */       int n; if (((a = q.array) != null) && ((am = a.length - 1) > (n = (s = q.top) - q.base)))
/*      */       {
/* 1504 */         int j = ((am & s) << ASHIFT) + ABASE;
/* 1505 */         U.putOrderedObject(a, j, task);
/* 1506 */         q.top = (s + 1);
/* 1507 */         q.qlock = 0;
/* 1508 */         if (n <= 1)
/* 1509 */           signalWork(ws, q);
/* 1510 */         return;
/*      */       }
/* 1512 */       q.qlock = 0;
/*      */     }
/* 1514 */     fullExternalPush(task);
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
/*      */   private void fullExternalPush(ForkJoinTask<?> task)
/*      */   {
/* 1535 */     int r = 0;
/* 1536 */     Submitter z = (Submitter)submitters.get();
/*      */     for (;;) {
/* 1538 */       if (z == null) {
/* 1539 */         r += 1640531527; if ((U.compareAndSwapInt(this, INDEXSEED, r = this.indexSeed, r)) && (r != 0))
/*      */         {
/* 1541 */           submitters.set(z = new Submitter(r));
/*      */         }
/* 1543 */       } else if (r == 0) {
/* 1544 */         r = z.seed;
/* 1545 */         r ^= r << 13;
/* 1546 */         r ^= r >>> 17;
/* 1547 */         z.seed = (r ^= r << 5); }
/*      */       int ps;
/* 1549 */       if ((ps = this.plock) < 0)
/* 1550 */         throw new RejectedExecutionException();
/* 1551 */       WorkQueue[] ws; int m; WorkQueue[] ws; if ((ps == 0) || ((ws = this.workQueues) == null) || ((m = ws.length - 1) < 0))
/*      */       {
/* 1553 */         int p = this.parallelism;
/* 1554 */         int n = p > 1 ? p - 1 : 1;
/* 1555 */         n |= n >>> 1;n |= n >>> 2;n |= n >>> 4;
/* 1556 */         n |= n >>> 8;n |= n >>> 16;n = n + 1 << 1;
/* 1557 */         WorkQueue[] nws = ((ws = this.workQueues) == null) || (ws.length == 0) ? new WorkQueue[n] : null;
/*      */         
/* 1559 */         if ((((ps = this.plock) & 0x2) != 0) || (!U.compareAndSwapInt(this, PLOCK, , ps)))
/*      */         {
/* 1561 */           ps = acquirePlock(); }
/* 1562 */         if ((((ws = this.workQueues) == null) || (ws.length == 0)) && (nws != null))
/* 1563 */           this.workQueues = nws;
/* 1564 */         int nps = ps & 0x80000000 | ps + 2 & 0x7FFFFFFF;
/* 1565 */         if (!U.compareAndSwapInt(this, PLOCK, ps, nps))
/* 1566 */           releasePlock(nps); } else { int m;
/*      */         int k;
/* 1568 */         WorkQueue q; if ((q = ws[(k = r & m & 0x7E)]) != null) {
/* 1569 */           if ((q.qlock == 0) && (U.compareAndSwapInt(q, QLOCK, 0, 1))) {
/* 1570 */             ForkJoinTask<?>[] a = q.array;
/* 1571 */             int s = q.top;
/* 1572 */             boolean submitted = false;
/*      */             try {
/* 1574 */               if (((a != null) && (a.length > s + 1 - q.base)) || ((a = q.growArray()) != null))
/*      */               {
/* 1576 */                 int j = ((a.length - 1 & s) << ASHIFT) + ABASE;
/* 1577 */                 U.putOrderedObject(a, j, task);
/* 1578 */                 q.top = (s + 1);
/* 1579 */                 submitted = true;
/*      */               }
/*      */             } finally {
/* 1582 */               q.qlock = 0;
/*      */             }
/* 1584 */             if (submitted) {
/* 1585 */               signalWork(ws, q);
/* 1586 */               return;
/*      */             }
/*      */           }
/* 1589 */           r = 0;
/*      */         }
/* 1591 */         else if (((ps = this.plock) & 0x2) == 0) {
/* 1592 */           q = new WorkQueue(this, null, -1, r);
/* 1593 */           q.poolIndex = ((short)k);
/* 1594 */           if ((((ps = this.plock) & 0x2) != 0) || (!U.compareAndSwapInt(this, PLOCK, , ps)))
/*      */           {
/* 1596 */             ps = acquirePlock(); }
/* 1597 */           if (((ws = this.workQueues) != null) && (k < ws.length) && (ws[k] == null))
/* 1598 */             ws[k] = q;
/* 1599 */           int nps = ps & 0x80000000 | ps + 2 & 0x7FFFFFFF;
/* 1600 */           if (!U.compareAndSwapInt(this, PLOCK, ps, nps)) {
/* 1601 */             releasePlock(nps);
/*      */           }
/*      */         } else {
/* 1604 */           r = 0;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   final void incrementActiveCount()
/*      */   {
/*      */     long c;
/*      */     
/* 1615 */     while (!U.compareAndSwapLong(this, CTL, c = this.ctl, c & 0xFFFFFFFFFFFF | (c & 0xFFFF000000000000) + 281474976710656L)) {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   final void signalWork(WorkQueue[] ws, WorkQueue q)
/*      */   {
/*      */     long c;
/*      */     
/*      */ 
/*      */     int u;
/*      */     
/*      */ 
/* 1629 */     while ((u = (int)((c = this.ctl) >>> 32)) < 0) {
/*      */       int e;
/* 1631 */       if ((e = (int)c) <= 0) {
/* 1632 */         if ((short)u < 0)
/* 1633 */           tryAddWorker();
/*      */       } else { int i;
/*      */         WorkQueue w;
/* 1636 */         if ((ws != null) && (ws.length > (i = e & 0xFFFF)) && ((w = ws[i]) != null))
/*      */         {
/*      */ 
/* 1639 */           long nc = w.nextWait & 0x7FFFFFFF | u + 65536 << 32;
/*      */           
/* 1641 */           int ne = e + 65536 & 0x7FFFFFFF;
/* 1642 */           if ((w.eventCount == (e | 0x80000000)) && (U.compareAndSwapLong(this, CTL, c, nc)))
/*      */           {
/* 1644 */             w.eventCount = ne;
/* 1645 */             Thread p; if ((p = w.parker) != null) {
/* 1646 */               U.unpark(p);
/*      */             }
/*      */           } else {
/* 1649 */             if ((q != null) && (q.base >= q.top)) {
/*      */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   final void runWorker(WorkQueue w)
/*      */   {
/* 1660 */     w.growArray();
/* 1661 */     for (int r = w.hint; scan(w, r) == 0; 
/* 1662 */         r ^= r << 5) { r ^= r << 13;r ^= r >>> 17;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final int scan(WorkQueue w, int r)
/*      */   {
/* 1690 */     long c = this.ctl;
/* 1691 */     WorkQueue[] ws; int m; if (((ws = this.workQueues) != null) && ((m = ws.length - 1) >= 0) && (w != null)) {
/* 1692 */       int j = m + m + 1;int ec = w.eventCount;
/*      */       for (;;) { WorkQueue q;
/* 1694 */         int b; ForkJoinTask<?>[] a; if (((q = ws[(r - j & m)]) != null) && ((b = q.base) - q.top < 0) && ((a = q.array) != null))
/*      */         {
/* 1696 */           long i = ((a.length - 1 & b) << ASHIFT) + ABASE;
/* 1697 */           ForkJoinTask<?> t; if ((t = (ForkJoinTask)U.getObjectVolatile(a, i)) == null)
/*      */             break;
/* 1699 */           if (ec < 0) {
/* 1700 */             helpRelease(c, ws, w, q, b); break; }
/* 1701 */           if ((q.base != b) || (!U.compareAndSwapObject(a, i, t, null)))
/*      */             break;
/* 1703 */           U.putOrderedInt(q, QBASE, b + 1);
/* 1704 */           if (b + 1 - q.top < 0)
/* 1705 */             signalWork(ws, q);
/* 1706 */           w.runTask(t); break;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1711 */         j--; if (j < 0) { int e;
/* 1712 */           if ((ec | (e = (int)c)) < 0)
/* 1713 */             return awaitWork(w, c, ec);
/* 1714 */           if (this.ctl != c) break;
/* 1715 */           long nc = ec | c - 281474976710656L & 0xFFFFFFFF00000000;
/* 1716 */           w.nextWait = e;
/* 1717 */           w.eventCount = (ec | 0x80000000);
/* 1718 */           if (!U.compareAndSwapLong(this, CTL, c, nc))
/* 1719 */             w.eventCount = ec;
/* 1720 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1725 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final int awaitWork(WorkQueue w, long c, int ec)
/*      */   {
/*      */     int stat;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1744 */     if (((stat = w.qlock) >= 0) && (w.eventCount == ec) && (this.ctl == c) && (!Thread.interrupted()))
/*      */     {
/* 1746 */       int e = (int)c;
/* 1747 */       int u = (int)(c >>> 32);
/* 1748 */       int d = (u >> 16) + this.parallelism;
/*      */       
/* 1750 */       if ((e < 0) || ((d <= 0) && (tryTerminate(false, false)))) {
/* 1751 */         stat = w.qlock = -1; } else { int ns;
/* 1752 */         if ((ns = w.nsteals) != 0)
/*      */         {
/* 1754 */           w.nsteals = 0;
/* 1755 */           long sc; while (!U.compareAndSwapLong(this, STEALCOUNT, sc = this.stealCount, sc + ns)) {}
/*      */         }
/*      */         else
/*      */         {
/* 1759 */           long pc = (d > 0) || (ec != (e | 0x80000000)) ? 0L : w.nextWait & 0x7FFFFFFF | u + 65536 << 32;
/*      */           long deadline;
/*      */           long deadline;
/* 1762 */           long parkTime; if (pc != 0L) {
/* 1763 */             int dc = -(short)(int)(c >>> 32);
/* 1764 */             long parkTime = dc < 0 ? 200000000L : (dc + 1) * 2000000000L;
/*      */             
/* 1766 */             deadline = System.nanoTime() + parkTime - 2000000L;
/*      */           }
/*      */           else {
/* 1769 */             parkTime = deadline = 0L; }
/* 1770 */           if ((w.eventCount == ec) && (this.ctl == c)) {
/* 1771 */             Thread wt = Thread.currentThread();
/* 1772 */             U.putObject(wt, PARKBLOCKER, this);
/* 1773 */             w.parker = wt;
/* 1774 */             if ((w.eventCount == ec) && (this.ctl == c))
/* 1775 */               U.park(false, parkTime);
/* 1776 */             w.parker = null;
/* 1777 */             U.putObject(wt, PARKBLOCKER, null);
/* 1778 */             if ((parkTime != 0L) && (this.ctl == c) && (deadline - System.nanoTime() <= 0L) && (U.compareAndSwapLong(this, CTL, c, pc)))
/*      */             {
/*      */ 
/* 1781 */               stat = w.qlock = -1; }
/*      */           }
/*      */         }
/*      */       } }
/* 1785 */     return stat;
/*      */   }
/*      */   
/*      */ 
/*      */   private final void helpRelease(long c, WorkQueue[] ws, WorkQueue w, WorkQueue q, int b)
/*      */   {
/*      */     int e;
/*      */     
/*      */     int i;
/*      */     
/*      */     WorkQueue v;
/*      */     
/* 1797 */     if ((w != null) && (w.eventCount < 0) && ((e = (int)c) > 0) && (ws != null) && (ws.length > (i = e & 0xFFFF)) && ((v = ws[i]) != null) && (this.ctl == c))
/*      */     {
/*      */ 
/* 1800 */       long nc = v.nextWait & 0x7FFFFFFF | (int)(c >>> 32) + 65536 << 32;
/*      */       
/* 1802 */       int ne = e + 65536 & 0x7FFFFFFF;
/* 1803 */       if ((q != null) && (q.base == b) && (w.eventCount < 0) && (v.eventCount == (e | 0x80000000)) && (U.compareAndSwapLong(this, CTL, c, nc)))
/*      */       {
/*      */ 
/* 1806 */         v.eventCount = ne;
/* 1807 */         Thread p; if ((p = v.parker) != null) {
/* 1808 */           U.unpark(p);
/*      */         }
/*      */       }
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
/*      */ 
/*      */   private int tryHelpStealer(WorkQueue joiner, ForkJoinTask<?> task)
/*      */   {
/* 1832 */     int stat = 0;int steps = 0;
/* 1833 */     if ((task != null) && (joiner != null) && (joiner.base - joiner.top >= 0)) {
/*      */       break label107;
/*      */       label25:
/* 1836 */       ForkJoinTask<?> subtask = task;
/* 1837 */       WorkQueue j = joiner;
/*      */       label107:
/* 1839 */       label471: label472: label474: for (;;) { int s; if ((s = task.status) < 0) {
/* 1840 */           stat = s;
/* 1841 */           return stat; }
/*      */         WorkQueue[] ws;
/* 1843 */         int m; if (((ws = this.workQueues) == null) || ((m = ws.length - 1) <= 0)) return stat;
/*      */         int h;
/* 1845 */         WorkQueue v; if (((v = ws[(h = (j.hint | 0x1) & m)]) == null) || (v.currentSteal != subtask))
/*      */         {
/* 1847 */           int origin = h;
/* 1848 */           if ((((h = h + 2 & m) & 0xF) == 1) && ((subtask.status < 0) || (j.currentJoin != subtask))) {
/*      */             break label25;
/*      */           }
/* 1851 */           if (((v = ws[h]) != null) && (v.currentSteal == subtask))
/*      */           {
/* 1853 */             j.hint = h;
/*      */           }
/*      */           else {
/* 1856 */             if (h != origin) break;
/* 1857 */             return stat;
/*      */           }
/*      */         }
/*      */         for (;;)
/*      */         {
/* 1862 */           if (subtask.status < 0) break label472;
/*      */           int b;
/* 1864 */           ForkJoinTask[] a; if (((b = v.base) - v.top < 0) && ((a = v.array) != null)) {
/* 1865 */             int i = ((a.length - 1 & b) << ASHIFT) + ABASE;
/* 1866 */             ForkJoinTask<?> t = (ForkJoinTask)U.getObjectVolatile(a, i);
/*      */             
/* 1868 */             if ((subtask.status < 0) || (j.currentJoin != subtask) || (v.currentSteal != subtask)) {
/*      */               break;
/*      */             }
/* 1871 */             stat = 1;
/* 1872 */             if (v.base == b) {
/* 1873 */               if (t == null)
/*      */                 return stat;
/* 1875 */               if (U.compareAndSwapObject(a, i, t, null)) {
/* 1876 */                 U.putOrderedInt(v, QBASE, b + 1);
/* 1877 */                 ForkJoinTask<?> ps = joiner.currentSteal;
/* 1878 */                 int jt = joiner.top;
/*      */                 do {
/* 1880 */                   joiner.currentSteal = t;
/* 1881 */                   t.doExec();
/*      */                 }
/* 1883 */                 while ((task.status >= 0) && (joiner.top != jt) && ((t = joiner.pop()) != null));
/*      */                 
/* 1885 */                 joiner.currentSteal = ps;
/* 1886 */                 return stat;
/*      */               }
/*      */             }
/*      */             break label471;
/*      */           }
/* 1891 */           ForkJoinTask<?> next = v.currentJoin;
/* 1892 */           if ((subtask.status < 0) || (j.currentJoin != subtask) || (v.currentSteal != subtask)) {
/*      */             break;
/*      */           }
/* 1895 */           if (next == null) return stat; steps++; if (steps == 64) {
/*      */             return stat;
/*      */           }
/* 1898 */           subtask = next;
/* 1899 */           j = v;
/*      */           
/*      */           break label474;
/*      */         }
/*      */         
/*      */         break label25;
/*      */       }
/*      */     }
/* 1907 */     return stat;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int helpComplete(WorkQueue joiner, CountedCompleter<?> task)
/*      */   {
/* 1918 */     int s = 0;
/* 1919 */     WorkQueue[] ws; int m; if (((ws = this.workQueues) != null) && ((m = ws.length - 1) >= 0) && (joiner != null) && (task != null))
/*      */     {
/* 1921 */       int j = joiner.poolIndex;
/* 1922 */       int scans = m + m + 1;
/* 1923 */       long c = 0L;
/* 1924 */       int k = scans;
/* 1926 */       for (; 
/* 1926 */           (s = task.status) >= 0; j += 2)
/*      */       {
/*      */ 
/*      */ 
/* 1928 */         if (joiner.internalPopAndExecCC(task)) {
/* 1929 */           k = scans;
/* 1930 */         } else { if ((s = task.status) < 0) break;
/*      */           WorkQueue q;
/* 1932 */           if (((q = ws[(j & m)]) != null) && (q.pollAndExecCC(task))) {
/* 1933 */             k = scans;
/* 1934 */           } else { k--; if (k < 0) {
/* 1935 */               if (c == (c = this.ctl))
/*      */                 break;
/* 1937 */               k = scans;
/*      */             }
/*      */           }
/*      */         } } }
/* 1941 */     return s;
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
/*      */   final boolean tryCompensate(long c)
/*      */   {
/* 1954 */     WorkQueue[] ws = this.workQueues;
/* 1955 */     int pc = this.parallelism;int e = (int)c;
/* 1956 */     int m; if ((ws != null) && ((m = ws.length - 1) >= 0) && (e >= 0) && (this.ctl == c)) {
/* 1957 */       WorkQueue w = ws[(e & m)];
/* 1958 */       if ((e != 0) && (w != null))
/*      */       {
/* 1960 */         long nc = w.nextWait & 0x7FFFFFFF | c & 0xFFFFFFFF00000000;
/*      */         
/* 1962 */         int ne = e + 65536 & 0x7FFFFFFF;
/* 1963 */         if ((w.eventCount == (e | 0x80000000)) && (U.compareAndSwapLong(this, CTL, c, nc)))
/*      */         {
/* 1965 */           w.eventCount = ne;
/* 1966 */           Thread p; if ((p = w.parker) != null)
/* 1967 */             U.unpark(p);
/* 1968 */           return true;
/*      */         }
/*      */       } else { int tc;
/* 1971 */         if (((tc = (short)(int)(c >>> 32)) >= 0) && ((int)(c >> 48) + pc > 1))
/*      */         {
/* 1973 */           long nc = c - 281474976710656L & 0xFFFF000000000000 | c & 0xFFFFFFFFFFFF;
/* 1974 */           if (U.compareAndSwapLong(this, CTL, c, nc)) {
/* 1975 */             return true;
/*      */           }
/* 1977 */         } else if (tc + pc < 32767) {
/* 1978 */           long nc = c + 4294967296L & 0xFFFF00000000 | c & 0xFFFF0000FFFFFFFF;
/* 1979 */           if (U.compareAndSwapLong(this, CTL, c, nc))
/*      */           {
/* 1981 */             Throwable ex = null;
/* 1982 */             ForkJoinWorkerThread wt = null;
/*      */             try { ForkJoinWorkerThreadFactory fac;
/* 1984 */               if (((fac = this.factory) != null) && ((wt = fac.newThread(this)) != null))
/*      */               {
/* 1986 */                 wt.start();
/* 1987 */                 return true;
/*      */               }
/*      */             } catch (Throwable rex) {
/* 1990 */               ex = rex;
/*      */             }
/* 1992 */             deregisterWorker(wt, ex);
/*      */           }
/*      */         }
/*      */       } }
/* 1996 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final int awaitJoin(WorkQueue joiner, ForkJoinTask<?> task)
/*      */   {
/* 2007 */     int s = 0;
/* 2008 */     if ((task != null) && ((s = task.status) >= 0) && (joiner != null)) {
/* 2009 */       ForkJoinTask<?> prevJoin = joiner.currentJoin;
/* 2010 */       joiner.currentJoin = task;
/* 2011 */       while ((joiner.tryRemoveAndExec(task)) && ((s = task.status) >= 0)) {}
/*      */       
/* 2013 */       if ((s >= 0) && ((task instanceof CountedCompleter)))
/* 2014 */         s = helpComplete(joiner, (CountedCompleter)task);
/* 2015 */       long cc = 0L;
/* 2016 */       while ((s >= 0) && ((s = task.status) >= 0)) {
/* 2017 */         if (((s = tryHelpStealer(joiner, task)) == 0) && ((s = task.status) >= 0))
/*      */         {
/* 2019 */           if (!tryCompensate(cc)) {
/* 2020 */             cc = this.ctl;
/*      */           } else {
/* 2022 */             if ((task.trySetSignal()) && ((s = task.status) >= 0)) {
/* 2023 */               synchronized (task) {
/* 2024 */                 if (task.status >= 0) {
/*      */                   try {
/* 2026 */                     task.wait();
/*      */ 
/*      */                   }
/*      */                   catch (InterruptedException ie) {}
/*      */                 } else
/* 2031 */                   task.notifyAll();
/*      */               }
/*      */             }
/*      */             long c;
/* 2035 */             while (!U.compareAndSwapLong(this, CTL, c = this.ctl, c & 0xFFFFFFFFFFFF | (c & 0xFFFF000000000000) + 281474976710656L)) {}
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2042 */       joiner.currentJoin = prevJoin;
/*      */     }
/* 2044 */     return s;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final void helpJoinOnce(WorkQueue joiner, ForkJoinTask<?> task)
/*      */   {
/*      */     int s;
/*      */     
/*      */ 
/*      */ 
/* 2057 */     if ((joiner != null) && (task != null) && ((s = task.status) >= 0)) {
/* 2058 */       ForkJoinTask<?> prevJoin = joiner.currentJoin;
/* 2059 */       joiner.currentJoin = task;
/* 2060 */       while ((joiner.tryRemoveAndExec(task)) && ((s = task.status) >= 0)) {}
/*      */       
/* 2062 */       if (s >= 0) {
/* 2063 */         if ((task instanceof CountedCompleter))
/* 2064 */           helpComplete(joiner, (CountedCompleter)task);
/* 2065 */         while ((task.status >= 0) && (tryHelpStealer(joiner, task) > 0)) {}
/*      */       }
/*      */       
/* 2068 */       joiner.currentJoin = prevJoin;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private WorkQueue findNonEmptyStealQueue()
/*      */   {
/* 2078 */     int r = ThreadLocalRandom.current().nextInt();
/*      */     for (;;) {
/* 2080 */       int ps = this.plock;
/* 2081 */       WorkQueue[] ws; int m; if (((ws = this.workQueues) != null) && ((m = ws.length - 1) >= 0)) {
/* 2082 */         for (int j = m + 1 << 2; j >= 0; j--) { WorkQueue q;
/* 2083 */           if (((q = ws[((r - j << 1 | 0x1) & m)]) != null) && (q.base - q.top < 0))
/*      */           {
/* 2085 */             return q; }
/*      */         }
/*      */       }
/* 2088 */       if (this.plock == ps) {
/* 2089 */         return null;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final void helpQuiescePool(WorkQueue w)
/*      */   {
/* 2100 */     ForkJoinTask<?> ps = w.currentSteal;
/* 2101 */     boolean active = true;
/*      */     for (;;) { ForkJoinTask<?> t;
/* 2103 */       if ((t = w.nextLocalTask()) != null) {
/* 2104 */         t.doExec(); } else { WorkQueue q;
/* 2105 */         if ((q = findNonEmptyStealQueue()) != null) {
/* 2106 */           if (!active) {
/* 2107 */             active = true;
/* 2108 */             long c; while (!U.compareAndSwapLong(this, CTL, c = this.ctl, c & 0xFFFFFFFFFFFF | (c & 0xFFFF000000000000) + 281474976710656L)) {}
/*      */           }
/*      */           
/*      */           int b;
/*      */           
/* 2113 */           if (((b = q.base) - q.top < 0) && ((t = q.pollAt(b)) != null)) {
/* 2114 */             (w.currentSteal = t).doExec();
/* 2115 */             w.currentSteal = ps;
/*      */           }
/*      */         }
/* 2118 */         else if (active) { long c;
/* 2119 */           long nc = (c = this.ctl) & 0xFFFFFFFFFFFF | (c & 0xFFFF000000000000) - 281474976710656L;
/* 2120 */           if ((int)(nc >> 48) + this.parallelism == 0)
/*      */             break;
/* 2122 */           if (U.compareAndSwapLong(this, CTL, c, nc))
/* 2123 */             active = false;
/*      */         } else { long c;
/* 2125 */           if (((int)((c = this.ctl) >> 48) + this.parallelism <= 0) && (U.compareAndSwapLong(this, CTL, c, c & 0xFFFFFFFFFFFF | (c & 0xFFFF000000000000) + 281474976710656L))) {
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   final ForkJoinTask<?> nextTaskFor(WorkQueue w)
/*      */   {
/*      */     for (;;)
/*      */     {
/*      */       ForkJoinTask<?> t;
/*      */       
/* 2141 */       if ((t = w.nextLocalTask()) != null)
/* 2142 */         return t;
/* 2143 */       WorkQueue q; if ((q = findNonEmptyStealQueue()) == null)
/* 2144 */         return null;
/* 2145 */       int b; if (((b = q.base) - q.top < 0) && ((t = q.pollAt(b)) != null)) {
/* 2146 */         return t;
/*      */       }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static int getSurplusQueuedTaskCount()
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2198 */     if (((t = Thread.currentThread()) instanceof ForkJoinWorkerThread)) { ForkJoinWorkerThread wt;
/* 2199 */       ForkJoinPool pool; int p = (pool = (wt = (ForkJoinWorkerThread)t).pool).parallelism;
/* 2200 */       WorkQueue q; int n = (q = wt.workQueue).top - q.base;
/* 2201 */       int a = (int)(pool.ctl >> 48) + p;
/* 2202 */       return n - (a > p >>>= 1 ? 4 : a > p >>>= 1 ? 2 : a > p >>>= 1 ? 1 : a > p >>>= 1 ? 0 : 8);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2208 */     return 0;
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
/*      */   private boolean tryTerminate(boolean now, boolean enable)
/*      */   {
/* 2229 */     if (this == common)
/* 2230 */       return false;
/* 2231 */     int ps; if ((ps = this.plock) >= 0) {
/* 2232 */       if (!enable)
/* 2233 */         return false;
/* 2234 */       if (((ps & 0x2) != 0) || (!U.compareAndSwapInt(this, PLOCK, , ps)))
/*      */       {
/* 2236 */         ps = acquirePlock(); }
/* 2237 */       int nps = ps + 2 & 0x7FFFFFFF | 0x80000000;
/* 2238 */       if (!U.compareAndSwapInt(this, PLOCK, ps, nps))
/* 2239 */         releasePlock(nps);
/*      */     }
/*      */     for (;;) { long c;
/* 2242 */       if (((c = this.ctl) & 0x80000000) != 0L) {
/* 2243 */         if ((short)(int)(c >>> 32) + this.parallelism <= 0) {
/* 2244 */           synchronized (this) {
/* 2245 */             notifyAll();
/*      */           }
/*      */         }
/* 2248 */         return true;
/*      */       }
/* 2250 */       if (!now)
/*      */       {
/* 2252 */         if ((int)(c >> 48) + this.parallelism > 0)
/* 2253 */           return false;
/* 2254 */         WorkQueue[] ws; if ((ws = this.workQueues) != null) {
/* 2255 */           for (int i = 0; i < ws.length; i++) { WorkQueue w;
/* 2256 */             if (((w = ws[i]) != null) && ((!w.isEmpty()) || (((i & 0x1) != 0) && (w.eventCount >= 0))))
/*      */             {
/*      */ 
/* 2259 */               signalWork(ws, w);
/* 2260 */               return false;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 2265 */       if (U.compareAndSwapLong(this, CTL, c, c | 0x80000000)) {
/* 2266 */         for (int pass = 0; pass < 3; pass++) {
/*      */           WorkQueue[] ws;
/* 2268 */           if ((ws = this.workQueues) != null) {
/* 2269 */             int n = ws.length;
/* 2270 */             for (int i = 0; i < n; i++) { WorkQueue w;
/* 2271 */               if ((w = ws[i]) != null) {
/* 2272 */                 w.qlock = -1;
/* 2273 */                 if (pass > 0) {
/* 2274 */                   w.cancelAll();
/* 2275 */                   Thread wt; if ((pass > 1) && ((wt = w.owner) != null)) {
/* 2276 */                     if (!wt.isInterrupted()) {
/*      */                       try {
/* 2278 */                         wt.interrupt();
/*      */                       }
/*      */                       catch (Throwable ignore) {}
/*      */                     }
/* 2282 */                     U.unpark(wt);
/*      */                   }
/*      */                 }
/*      */               } }
/*      */             long cc;
/*      */             int e;
/*      */             int i;
/*      */             WorkQueue w;
/* 2290 */             while (((e = (int)(cc = this.ctl) & 0x7FFFFFFF) != 0) && ((i = e & 0xFFFF) < n) && (i >= 0) && ((w = ws[i]) != null))
/*      */             {
/* 2292 */               long nc = w.nextWait & 0x7FFFFFFF | cc + 281474976710656L & 0xFFFF000000000000 | cc & 0xFFFF80000000;
/*      */               
/*      */ 
/* 2295 */               if ((w.eventCount == (e | 0x80000000)) && (U.compareAndSwapLong(this, CTL, cc, nc)))
/*      */               {
/* 2297 */                 w.eventCount = (e + 65536 & 0x7FFFFFFF);
/* 2298 */                 w.qlock = -1;
/* 2299 */                 Thread p; if ((p = w.parker) != null) {
/* 2300 */                   U.unpark(p);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   static WorkQueue commonSubmitterQueue()
/*      */   {
/*      */     Submitter z;
/*      */     ForkJoinPool p;
/*      */     WorkQueue[] ws;
/*      */     int m;
/* 2317 */     return ((z = (Submitter)submitters.get()) != null) && ((p = common) != null) && ((ws = p.workQueues) != null) && ((m = ws.length - 1) >= 0) ? ws[(m & z.seed & 0x7E)] : null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final boolean tryExternalUnpush(ForkJoinTask<?> task)
/*      */   {
/* 2329 */     Submitter z = (Submitter)submitters.get();
/* 2330 */     WorkQueue[] ws = this.workQueues;
/* 2331 */     boolean popped = false;
/* 2332 */     int m; WorkQueue joiner; int s; ForkJoinTask<?>[] a; if ((z != null) && (ws != null) && ((m = ws.length - 1) >= 0) && ((joiner = ws[(z.seed & m & 0x7E)]) != null) && (joiner.base != (s = joiner.top)) && ((a = joiner.array) != null))
/*      */     {
/*      */ 
/*      */ 
/* 2336 */       long j = ((a.length - 1 & s - 1) << ASHIFT) + ABASE;
/* 2337 */       if ((U.getObject(a, j) == task) && (U.compareAndSwapInt(joiner, QLOCK, 0, 1)))
/*      */       {
/* 2339 */         if ((joiner.top == s) && (joiner.array == a) && (U.compareAndSwapObject(a, j, task, null)))
/*      */         {
/* 2341 */           joiner.top = (s - 1);
/* 2342 */           popped = true;
/*      */         }
/* 2344 */         joiner.qlock = 0;
/*      */       }
/*      */     }
/* 2347 */     return popped;
/*      */   }
/*      */   
/*      */   final int externalHelpComplete(CountedCompleter<?> task)
/*      */   {
/* 2352 */     Submitter z = (Submitter)submitters.get();
/* 2353 */     WorkQueue[] ws = this.workQueues;
/* 2354 */     int s = 0;
/* 2355 */     int m; int j; WorkQueue joiner; if ((z != null) && (ws != null) && ((m = ws.length - 1) >= 0) && ((joiner = ws[((j = z.seed) & m & 0x7E)]) != null) && (task != null))
/*      */     {
/* 2357 */       int scans = m + m + 1;
/* 2358 */       long c = 0L;
/* 2359 */       j |= 0x1;
/* 2360 */       int k = scans;
/* 2362 */       for (; 
/* 2362 */           (s = task.status) >= 0; j += 2)
/*      */       {
/*      */ 
/*      */ 
/* 2364 */         if (joiner.externalPopAndExecCC(task)) {
/* 2365 */           k = scans;
/* 2366 */         } else { if ((s = task.status) < 0) break;
/*      */           WorkQueue q;
/* 2368 */           if (((q = ws[(j & m)]) != null) && (q.pollAndExecCC(task))) {
/* 2369 */             k = scans;
/* 2370 */           } else { k--; if (k < 0) {
/* 2371 */               if (c == (c = this.ctl))
/*      */                 break;
/* 2373 */               k = scans;
/*      */             }
/*      */           }
/*      */         } } }
/* 2377 */     return s;
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
/*      */   public ForkJoinPool()
/*      */   {
/* 2396 */     this(Math.min(32767, Runtime.getRuntime().availableProcessors()), defaultForkJoinWorkerThreadFactory, null, false);
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
/*      */   public ForkJoinPool(int parallelism)
/*      */   {
/* 2415 */     this(parallelism, defaultForkJoinWorkerThreadFactory, null, false);
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
/*      */   public ForkJoinPool(int parallelism, ForkJoinWorkerThreadFactory factory, Thread.UncaughtExceptionHandler handler, boolean asyncMode)
/*      */   {
/* 2446 */     this(checkParallelism(parallelism), checkFactory(factory), handler, asyncMode ? 1 : 0, "ForkJoinPool-" + nextPoolId() + "-worker-");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2451 */     checkPermission();
/*      */   }
/*      */   
/*      */   private static int checkParallelism(int parallelism) {
/* 2455 */     if ((parallelism <= 0) || (parallelism > 32767))
/* 2456 */       throw new IllegalArgumentException();
/* 2457 */     return parallelism;
/*      */   }
/*      */   
/*      */   private static ForkJoinWorkerThreadFactory checkFactory(ForkJoinWorkerThreadFactory factory)
/*      */   {
/* 2462 */     if (factory == null)
/* 2463 */       throw new NullPointerException();
/* 2464 */     return factory;
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
/*      */   private ForkJoinPool(int parallelism, ForkJoinWorkerThreadFactory factory, Thread.UncaughtExceptionHandler handler, int mode, String workerNamePrefix)
/*      */   {
/* 2477 */     this.workerNamePrefix = workerNamePrefix;
/* 2478 */     this.factory = factory;
/* 2479 */     this.ueh = handler;
/* 2480 */     this.mode = ((short)mode);
/* 2481 */     this.parallelism = ((short)parallelism);
/* 2482 */     long np = -parallelism;
/* 2483 */     this.ctl = (np << 48 & 0xFFFF000000000000 | np << 32 & 0xFFFF00000000);
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
/*      */   public static ForkJoinPool commonPool()
/*      */   {
/* 2501 */     return common;
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
/*      */   public <T> T invoke(ForkJoinTask<T> task)
/*      */   {
/* 2523 */     if (task == null)
/* 2524 */       throw new NullPointerException();
/* 2525 */     externalPush(task);
/* 2526 */     return (T)task.join();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void execute(ForkJoinTask<?> task)
/*      */   {
/* 2538 */     if (task == null)
/* 2539 */       throw new NullPointerException();
/* 2540 */     externalPush(task);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void execute(Runnable task)
/*      */   {
/* 2551 */     if (task == null)
/* 2552 */       throw new NullPointerException();
/*      */     ForkJoinTask<?> job;
/* 2554 */     ForkJoinTask<?> job; if ((task instanceof ForkJoinTask)) {
/* 2555 */       job = (ForkJoinTask)task;
/*      */     } else
/* 2557 */       job = new ForkJoinTask.RunnableExecuteAction(task);
/* 2558 */     externalPush(job);
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
/*      */   public <T> ForkJoinTask<T> submit(ForkJoinTask<T> task)
/*      */   {
/* 2571 */     if (task == null)
/* 2572 */       throw new NullPointerException();
/* 2573 */     externalPush(task);
/* 2574 */     return task;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> ForkJoinTask<T> submit(Callable<T> task)
/*      */   {
/* 2583 */     ForkJoinTask<T> job = new ForkJoinTask.AdaptedCallable(task);
/* 2584 */     externalPush(job);
/* 2585 */     return job;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> ForkJoinTask<T> submit(Runnable task, T result)
/*      */   {
/* 2594 */     ForkJoinTask<T> job = new ForkJoinTask.AdaptedRunnable(task, result);
/* 2595 */     externalPush(job);
/* 2596 */     return job;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ForkJoinTask<?> submit(Runnable task)
/*      */   {
/* 2605 */     if (task == null)
/* 2606 */       throw new NullPointerException();
/*      */     ForkJoinTask<?> job;
/* 2608 */     ForkJoinTask<?> job; if ((task instanceof ForkJoinTask)) {
/* 2609 */       job = (ForkJoinTask)task;
/*      */     } else
/* 2611 */       job = new ForkJoinTask.AdaptedRunnableAction(task);
/* 2612 */     externalPush(job);
/* 2613 */     return job;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
/*      */   {
/* 2624 */     ArrayList<Future<T>> futures = new ArrayList(tasks.size());
/*      */     
/* 2626 */     boolean done = false;
/*      */     try {
/* 2628 */       for (Callable<T> t : tasks) {
/* 2629 */         ForkJoinTask<T> f = new ForkJoinTask.AdaptedCallable(t);
/* 2630 */         futures.add(f);
/* 2631 */         externalPush(f);
/*      */       }
/* 2633 */       int i = 0; for (int size = futures.size(); i < size; i++)
/* 2634 */         ((ForkJoinTask)futures.get(i)).quietlyJoin();
/* 2635 */       done = true;
/* 2636 */       int i; int size; return futures;
/*      */     } finally {
/* 2638 */       if (!done) {
/* 2639 */         int i = 0; for (int size = futures.size(); i < size; i++) {
/* 2640 */           ((Future)futures.get(i)).cancel(false);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ForkJoinWorkerThreadFactory getFactory()
/*      */   {
/* 2650 */     return this.factory;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Thread.UncaughtExceptionHandler getUncaughtExceptionHandler()
/*      */   {
/* 2660 */     return this.ueh;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getParallelism()
/*      */   {
/*      */     int par;
/*      */     
/*      */ 
/* 2670 */     return (par = this.parallelism) > 0 ? par : 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int getCommonPoolParallelism()
/*      */   {
/* 2680 */     return commonParallelism;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getPoolSize()
/*      */   {
/* 2692 */     return this.parallelism + (short)(int)(this.ctl >>> 32);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getAsyncMode()
/*      */   {
/* 2702 */     return this.mode == 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getRunningThreadCount()
/*      */   {
/* 2714 */     int rc = 0;
/*      */     WorkQueue[] ws;
/* 2716 */     if ((ws = this.workQueues) != null) {
/* 2717 */       for (int i = 1; i < ws.length; i += 2) { WorkQueue w;
/* 2718 */         if (((w = ws[i]) != null) && (w.isApparentlyUnblocked()))
/* 2719 */           rc++;
/*      */       }
/*      */     }
/* 2722 */     return rc;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getActiveThreadCount()
/*      */   {
/* 2733 */     int r = this.parallelism + (int)(this.ctl >> 48);
/* 2734 */     return r <= 0 ? 0 : r;
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
/*      */   public boolean isQuiescent()
/*      */   {
/* 2749 */     return this.parallelism + (int)(this.ctl >> 48) <= 0;
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
/*      */   public long getStealCount()
/*      */   {
/* 2764 */     long count = this.stealCount;
/*      */     WorkQueue[] ws;
/* 2766 */     if ((ws = this.workQueues) != null) {
/* 2767 */       for (int i = 1; i < ws.length; i += 2) { WorkQueue w;
/* 2768 */         if ((w = ws[i]) != null)
/* 2769 */           count += w.nsteals;
/*      */       }
/*      */     }
/* 2772 */     return count;
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
/*      */   public long getQueuedTaskCount()
/*      */   {
/* 2786 */     long count = 0L;
/*      */     WorkQueue[] ws;
/* 2788 */     if ((ws = this.workQueues) != null) {
/* 2789 */       for (int i = 1; i < ws.length; i += 2) { WorkQueue w;
/* 2790 */         if ((w = ws[i]) != null)
/* 2791 */           count += w.queueSize();
/*      */       }
/*      */     }
/* 2794 */     return count;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getQueuedSubmissionCount()
/*      */   {
/* 2805 */     int count = 0;
/*      */     WorkQueue[] ws;
/* 2807 */     if ((ws = this.workQueues) != null) {
/* 2808 */       for (int i = 0; i < ws.length; i += 2) { WorkQueue w;
/* 2809 */         if ((w = ws[i]) != null)
/* 2810 */           count += w.queueSize();
/*      */       }
/*      */     }
/* 2813 */     return count;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasQueuedSubmissions()
/*      */   {
/*      */     WorkQueue[] ws;
/*      */     
/*      */ 
/* 2824 */     if ((ws = this.workQueues) != null) {
/* 2825 */       for (int i = 0; i < ws.length; i += 2) { WorkQueue w;
/* 2826 */         if (((w = ws[i]) != null) && (!w.isEmpty()))
/* 2827 */           return true;
/*      */       }
/*      */     }
/* 2830 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ForkJoinTask<?> pollSubmission()
/*      */   {
/*      */     WorkQueue[] ws;
/*      */     
/*      */ 
/*      */ 
/* 2842 */     if ((ws = this.workQueues) != null) {
/* 2843 */       for (int i = 0; i < ws.length; i += 2) { WorkQueue w;
/* 2844 */         ForkJoinTask<?> t; if (((w = ws[i]) != null) && ((t = w.poll()) != null))
/* 2845 */           return t;
/*      */       }
/*      */     }
/* 2848 */     return null;
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
/*      */   protected int drainTasksTo(Collection<? super ForkJoinTask<?>> c)
/*      */   {
/* 2869 */     int count = 0;
/*      */     WorkQueue[] ws;
/* 2871 */     if ((ws = this.workQueues) != null) {
/* 2872 */       for (int i = 0; i < ws.length; i++) { WorkQueue w;
/* 2873 */         if ((w = ws[i]) != null) { ForkJoinTask<?> t;
/* 2874 */           while ((t = w.poll()) != null) {
/* 2875 */             c.add(t);
/* 2876 */             count++;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 2881 */     return count;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/* 2893 */     long qt = 0L;long qs = 0L;int rc = 0;
/* 2894 */     long st = this.stealCount;
/* 2895 */     long c = this.ctl;
/*      */     WorkQueue[] ws;
/* 2897 */     if ((ws = this.workQueues) != null) {
/* 2898 */       for (int i = 0; i < ws.length; i++) { WorkQueue w;
/* 2899 */         if ((w = ws[i]) != null) {
/* 2900 */           int size = w.queueSize();
/* 2901 */           if ((i & 0x1) == 0) {
/* 2902 */             qs += size;
/*      */           } else {
/* 2904 */             qt += size;
/* 2905 */             st += w.nsteals;
/* 2906 */             if (w.isApparentlyUnblocked())
/* 2907 */               rc++;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 2912 */     int pc = this.parallelism;
/* 2913 */     int tc = pc + (short)(int)(c >>> 32);
/* 2914 */     int ac = pc + (int)(c >> 48);
/* 2915 */     if (ac < 0)
/* 2916 */       ac = 0;
/*      */     String level;
/* 2918 */     String level; if ((c & 0x80000000) != 0L) {
/* 2919 */       level = tc == 0 ? "Terminated" : "Terminating";
/*      */     } else
/* 2921 */       level = this.plock < 0 ? "Shutting down" : "Running";
/* 2922 */     return super.toString() + "[" + level + ", parallelism = " + pc + ", size = " + tc + ", active = " + ac + ", running = " + rc + ", steals = " + st + ", tasks = " + qt + ", submissions = " + qs + "]";
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
/*      */   public void shutdown()
/*      */   {
/* 2949 */     checkPermission();
/* 2950 */     tryTerminate(false, true);
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
/*      */   public List<Runnable> shutdownNow()
/*      */   {
/* 2972 */     checkPermission();
/* 2973 */     tryTerminate(true, true);
/* 2974 */     return Collections.emptyList();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isTerminated()
/*      */   {
/* 2983 */     long c = this.ctl;
/* 2984 */     return ((c & 0x80000000) != 0L) && ((short)(int)(c >>> 32) + this.parallelism <= 0);
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
/*      */   public boolean isTerminating()
/*      */   {
/* 3002 */     long c = this.ctl;
/* 3003 */     return ((c & 0x80000000) != 0L) && ((short)(int)(c >>> 32) + this.parallelism > 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isShutdown()
/*      */   {
/* 3013 */     return this.plock < 0;
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
/*      */   public boolean awaitTermination(long timeout, TimeUnit unit)
/*      */     throws InterruptedException
/*      */   {
/* 3032 */     if (Thread.interrupted())
/* 3033 */       throw new InterruptedException();
/* 3034 */     if (this == common) {
/* 3035 */       awaitQuiescence(timeout, unit);
/* 3036 */       return false;
/*      */     }
/* 3038 */     long nanos = unit.toNanos(timeout);
/* 3039 */     if (isTerminated())
/* 3040 */       return true;
/* 3041 */     if (nanos <= 0L)
/* 3042 */       return false;
/* 3043 */     long deadline = System.nanoTime() + nanos;
/* 3044 */     synchronized (this)
/*      */     {
/* 3046 */       if (isTerminated())
/* 3047 */         return true;
/* 3048 */       if (nanos <= 0L)
/* 3049 */         return false;
/* 3050 */       long millis = TimeUnit.NANOSECONDS.toMillis(nanos);
/* 3051 */       wait(millis > 0L ? millis : 1L);
/* 3052 */       nanos = deadline - System.nanoTime();
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
/*      */   public boolean awaitQuiescence(long timeout, TimeUnit unit)
/*      */   {
/* 3069 */     long nanos = unit.toNanos(timeout);
/*      */     
/* 3071 */     Thread thread = Thread.currentThread();
/* 3072 */     ForkJoinWorkerThread wt; if (((thread instanceof ForkJoinWorkerThread)) && ((wt = (ForkJoinWorkerThread)thread).pool == this))
/*      */     {
/* 3074 */       helpQuiescePool(wt.workQueue);
/* 3075 */       return true;
/*      */     }
/* 3077 */     long startTime = System.nanoTime();
/*      */     
/* 3079 */     int r = 0;
/* 3080 */     boolean found = true;
/* 3081 */     WorkQueue[] ws; int m; while ((!isQuiescent()) && ((ws = this.workQueues) != null) && ((m = ws.length - 1) >= 0))
/*      */     {
/* 3083 */       if (!found) {
/* 3084 */         if (System.nanoTime() - startTime > nanos)
/* 3085 */           return false;
/* 3086 */         Thread.yield();
/*      */       }
/* 3088 */       found = false;
/* 3089 */       for (int j = m + 1 << 2; j >= 0; j--) { WorkQueue q;
/*      */         int b;
/* 3091 */         if (((q = ws[(r++ & m)]) != null) && ((b = q.base) - q.top < 0)) {
/* 3092 */           found = true;
/* 3093 */           ForkJoinTask<?> t; if ((t = q.pollAt(b)) == null) break;
/* 3094 */           t.doExec(); break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3099 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static void quiesceCommonPool()
/*      */   {
/* 3107 */     common.awaitQuiescence(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void managedBlock(ManagedBlocker blocker)
/*      */     throws InterruptedException
/*      */   {
/* 3206 */     Thread t = Thread.currentThread();
/* 3207 */     if ((t instanceof ForkJoinWorkerThread)) {
/* 3208 */       ForkJoinPool p = ((ForkJoinWorkerThread)t).pool;
/* 3209 */       while (!blocker.isReleasable()) {
/* 3210 */         if (p.tryCompensate(p.ctl)) {
/*      */           try {
/* 3212 */             do { if (blocker.isReleasable()) break; } while (!blocker.block());
/*      */           }
/*      */           finally {
/* 3215 */             p.incrementActiveCount();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 3222 */     while ((!blocker.isReleasable()) && (!blocker.block())) {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value)
/*      */   {
/* 3232 */     return new ForkJoinTask.AdaptedRunnable(runnable, value);
/*      */   }
/*      */   
/*      */   protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
/* 3236 */     return new ForkJoinTask.AdaptedCallable(callable);
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
/*      */   static
/*      */   {
/*      */     try
/*      */     {
/* 3254 */       U = getUnsafe();
/* 3255 */       Class<?> k = ForkJoinPool.class;
/* 3256 */       CTL = U.objectFieldOffset(k.getDeclaredField("ctl"));
/*      */       
/* 3258 */       STEALCOUNT = U.objectFieldOffset(k.getDeclaredField("stealCount"));
/*      */       
/* 3260 */       PLOCK = U.objectFieldOffset(k.getDeclaredField("plock"));
/*      */       
/* 3262 */       INDEXSEED = U.objectFieldOffset(k.getDeclaredField("indexSeed"));
/*      */       
/* 3264 */       Class<?> tk = Thread.class;
/* 3265 */       PARKBLOCKER = U.objectFieldOffset(tk.getDeclaredField("parkBlocker"));
/*      */       
/* 3267 */       Class<?> wk = WorkQueue.class;
/* 3268 */       QBASE = U.objectFieldOffset(wk.getDeclaredField("base"));
/*      */       
/* 3270 */       QLOCK = U.objectFieldOffset(wk.getDeclaredField("qlock"));
/*      */       
/* 3272 */       Class<?> ak = ForkJoinTask[].class;
/* 3273 */       ABASE = U.arrayBaseOffset(ak);
/* 3274 */       int scale = U.arrayIndexScale(ak);
/* 3275 */       if ((scale & scale - 1) != 0)
/* 3276 */         throw new Error("data type scale not a power of two");
/* 3277 */       ASHIFT = 31 - Integer.numberOfLeadingZeros(scale);
/*      */     } catch (Exception e) {
/* 3279 */       throw new Error(e);
/*      */     }
/*      */     
/* 3282 */     submitters = new ThreadLocal();
/* 3283 */     defaultForkJoinWorkerThreadFactory = new DefaultForkJoinWorkerThreadFactory();
/*      */     
/* 3285 */     modifyThreadPermission = new RuntimePermission("modifyThread");
/*      */     
/* 3287 */     common = (ForkJoinPool)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/* 3289 */       public ForkJoinPool run() { return ForkJoinPool.access$100(); } });
/* 3290 */     int par = common.parallelism;
/* 3291 */     commonParallelism = par > 0 ? par : 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static ForkJoinPool makeCommonPool()
/*      */   {
/* 3299 */     int parallelism = -1;
/* 3300 */     ForkJoinWorkerThreadFactory factory = defaultForkJoinWorkerThreadFactory;
/*      */     
/* 3302 */     Thread.UncaughtExceptionHandler handler = null;
/*      */     try {
/* 3304 */       String pp = System.getProperty("java.util.concurrent.ForkJoinPool.common.parallelism");
/*      */       
/* 3306 */       String fp = System.getProperty("java.util.concurrent.ForkJoinPool.common.threadFactory");
/*      */       
/* 3308 */       String hp = System.getProperty("java.util.concurrent.ForkJoinPool.common.exceptionHandler");
/*      */       
/* 3310 */       if (pp != null)
/* 3311 */         parallelism = Integer.parseInt(pp);
/* 3312 */       if (fp != null) {
/* 3313 */         factory = (ForkJoinWorkerThreadFactory)ClassLoader.getSystemClassLoader().loadClass(fp).newInstance();
/*      */       }
/* 3315 */       if (hp != null) {
/* 3316 */         handler = (Thread.UncaughtExceptionHandler)ClassLoader.getSystemClassLoader().loadClass(hp).newInstance();
/*      */       }
/*      */     }
/*      */     catch (Exception ignore) {}
/*      */     
/* 3321 */     if ((parallelism < 0) && ((parallelism = Runtime.getRuntime().availableProcessors() - 1) < 0))
/*      */     {
/* 3323 */       parallelism = 0; }
/* 3324 */     if (parallelism > 32767)
/* 3325 */       parallelism = 32767;
/* 3326 */     return new ForkJoinPool(parallelism, factory, handler, 0, "ForkJoinPool.commonPool-worker-");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static Unsafe getUnsafe()
/*      */   {
/*      */     try
/*      */     {
/* 3339 */       return Unsafe.getUnsafe();
/*      */     } catch (SecurityException tryReflectionInstead) {
/*      */       try {
/* 3342 */         (Unsafe)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */         {
/*      */           public Unsafe run() throws Exception {
/* 3345 */             Class<Unsafe> k = Unsafe.class;
/* 3346 */             for (Field f : k.getDeclaredFields()) {
/* 3347 */               f.setAccessible(true);
/* 3348 */               Object x = f.get(null);
/* 3349 */               if (k.isInstance(x))
/* 3350 */                 return (Unsafe)k.cast(x);
/*      */             }
/* 3352 */             throw new NoSuchFieldError("the Unsafe");
/*      */           }
/*      */         });
/* 3355 */       } catch (PrivilegedActionException e) { throw new RuntimeException("Could not initialize intrinsics", e.getCause());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static abstract interface ManagedBlocker
/*      */   {
/*      */     public abstract boolean block()
/*      */       throws InterruptedException;
/*      */     
/*      */     public abstract boolean isReleasable();
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\chmv8\ForkJoinPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */