package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Beta
public final class Uninterruptibles {
    public static void awaitUninterruptibly(CountDownLatch paramCountDownLatch) {
        int i = 0;
        try {
            paramCountDownLatch.await();
            return;
        } catch (InterruptedException localInterruptedException) {
            for (; ; ) {
                i = 1;
            }
        } finally {
            if (i != 0) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static boolean awaitUninterruptibly(CountDownLatch paramCountDownLatch, long paramLong, java.util.concurrent.TimeUnit paramTimeUnit) {
        // Byte code:
        //   0: iconst_0
        //   1: istore 4
        //   3: aload_3
        //   4: lload_1
        //   5: invokevirtual 33	java/util/concurrent/TimeUnit:toNanos	(J)J
        //   8: lstore 5
        //   10: invokestatic 39	java/lang/System:nanoTime	()J
        //   13: lload 5
        //   15: ladd
        //   16: lstore 7
        //   18: aload_0
        //   19: lload 5
        //   21: getstatic 43	java/util/concurrent/TimeUnit:NANOSECONDS	Ljava/util/concurrent/TimeUnit;
        //   24: invokevirtual 46	java/util/concurrent/CountDownLatch:await	(JLjava/util/concurrent/TimeUnit;)Z
        //   27: istore 9
        //   29: iload 4
        //   31: ifeq +9 -> 40
        //   34: invokestatic 21	java/lang/Thread:currentThread	()Ljava/lang/Thread;
        //   37: invokevirtual 24	java/lang/Thread:interrupt	()V
        //   40: iload 9
        //   42: ireturn
        //   43: astore 9
        //   45: iconst_1
        //   46: istore 4
        //   48: lload 7
        //   50: invokestatic 39	java/lang/System:nanoTime	()J
        //   53: lsub
        //   54: lstore 5
        //   56: goto -38 -> 18
        //   59: astore 10
        //   61: iload 4
        //   63: ifeq +9 -> 72
        //   66: invokestatic 21	java/lang/Thread:currentThread	()Ljava/lang/Thread;
        //   69: invokevirtual 24	java/lang/Thread:interrupt	()V
        //   72: aload 10
        //   74: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	75	0	paramCountDownLatch	CountDownLatch
        //   0	75	1	paramLong	long
        //   0	75	3	paramTimeUnit	java.util.concurrent.TimeUnit
        //   1	61	4	i	int
        //   8	47	5	l1	long
        //   16	33	7	l2	long
        //   27	14	9	bool	boolean
        //   43	1	9	localInterruptedException	InterruptedException
        //   59	14	10	localObject	Object
        // Exception table:
        //   from	to	target	type
        //   18	29	43	java/lang/InterruptedException
        //   3	29	59	finally
        //   43	61	59	finally
    }

    public static void joinUninterruptibly(Thread paramThread) {
        int i = 0;
        try {
            paramThread.join();
            return;
        } catch (InterruptedException localInterruptedException) {
            for (; ; ) {
                i = 1;
            }
        } finally {
            if (i != 0) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static <V> V getUninterruptibly(Future<V> paramFuture)
            throws ExecutionException {
        int i = 0;
        try {
            Object localObject1 = paramFuture.get();
            return (V) localObject1;
        } catch (InterruptedException localInterruptedException) {
            for (; ; ) {
                i = 1;
            }
        } finally {
            if (i != 0) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static <V> V getUninterruptibly(Future<V> paramFuture, long paramLong, java.util.concurrent.TimeUnit paramTimeUnit)
            throws ExecutionException, java.util.concurrent.TimeoutException {
        // Byte code:
        //   0: iconst_0
        //   1: istore 4
        //   3: aload_3
        //   4: lload_1
        //   5: invokevirtual 33	java/util/concurrent/TimeUnit:toNanos	(J)J
        //   8: lstore 5
        //   10: invokestatic 39	java/lang/System:nanoTime	()J
        //   13: lload 5
        //   15: ladd
        //   16: lstore 7
        //   18: aload_0
        //   19: lload 5
        //   21: getstatic 43	java/util/concurrent/TimeUnit:NANOSECONDS	Ljava/util/concurrent/TimeUnit;
        //   24: invokeinterface 67 4 0
        //   29: astore 9
        //   31: iload 4
        //   33: ifeq +9 -> 42
        //   36: invokestatic 21	java/lang/Thread:currentThread	()Ljava/lang/Thread;
        //   39: invokevirtual 24	java/lang/Thread:interrupt	()V
        //   42: aload 9
        //   44: areturn
        //   45: astore 9
        //   47: iconst_1
        //   48: istore 4
        //   50: lload 7
        //   52: invokestatic 39	java/lang/System:nanoTime	()J
        //   55: lsub
        //   56: lstore 5
        //   58: goto -40 -> 18
        //   61: astore 10
        //   63: iload 4
        //   65: ifeq +9 -> 74
        //   68: invokestatic 21	java/lang/Thread:currentThread	()Ljava/lang/Thread;
        //   71: invokevirtual 24	java/lang/Thread:interrupt	()V
        //   74: aload 10
        //   76: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	77	0	paramFuture	Future<V>
        //   0	77	1	paramLong	long
        //   0	77	3	paramTimeUnit	java.util.concurrent.TimeUnit
        //   1	63	4	i	int
        //   8	49	5	l1	long
        //   16	35	7	l2	long
        //   29	14	9	localObject1	Object
        //   45	1	9	localInterruptedException	InterruptedException
        //   61	14	10	localObject2	Object
        // Exception table:
        //   from	to	target	type
        //   18	31	45	java/lang/InterruptedException
        //   3	31	61	finally
        //   45	63	61	finally
    }

    public static void joinUninterruptibly(Thread paramThread, long paramLong, java.util.concurrent.TimeUnit paramTimeUnit) {
        // Byte code:
        //   0: aload_0
        //   1: invokestatic 74	com/google/common/base/Preconditions:checkNotNull	(Ljava/lang/Object;)Ljava/lang/Object;
        //   4: pop
        //   5: iconst_0
        //   6: istore 4
        //   8: aload_3
        //   9: lload_1
        //   10: invokevirtual 33	java/util/concurrent/TimeUnit:toNanos	(J)J
        //   13: lstore 5
        //   15: invokestatic 39	java/lang/System:nanoTime	()J
        //   18: lload 5
        //   20: ladd
        //   21: lstore 7
        //   23: getstatic 43	java/util/concurrent/TimeUnit:NANOSECONDS	Ljava/util/concurrent/TimeUnit;
        //   26: aload_0
        //   27: lload 5
        //   29: invokevirtual 78	java/util/concurrent/TimeUnit:timedJoin	(Ljava/lang/Thread;J)V
        //   32: iload 4
        //   34: ifeq +9 -> 43
        //   37: invokestatic 21	java/lang/Thread:currentThread	()Ljava/lang/Thread;
        //   40: invokevirtual 24	java/lang/Thread:interrupt	()V
        //   43: return
        //   44: astore 9
        //   46: iconst_1
        //   47: istore 4
        //   49: lload 7
        //   51: invokestatic 39	java/lang/System:nanoTime	()J
        //   54: lsub
        //   55: lstore 5
        //   57: goto -34 -> 23
        //   60: astore 10
        //   62: iload 4
        //   64: ifeq +9 -> 73
        //   67: invokestatic 21	java/lang/Thread:currentThread	()Ljava/lang/Thread;
        //   70: invokevirtual 24	java/lang/Thread:interrupt	()V
        //   73: aload 10
        //   75: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	76	0	paramThread	Thread
        //   0	76	1	paramLong	long
        //   0	76	3	paramTimeUnit	java.util.concurrent.TimeUnit
        //   6	57	4	i	int
        //   13	43	5	l1	long
        //   21	29	7	l2	long
        //   44	1	9	localInterruptedException	InterruptedException
        //   60	14	10	localObject	Object
        // Exception table:
        //   from	to	target	type
        //   23	32	44	java/lang/InterruptedException
        //   8	32	60	finally
        //   44	62	60	finally
    }

    public static <E> E takeUninterruptibly(BlockingQueue<E> paramBlockingQueue) {
        int i = 0;
        try {
            Object localObject1 = paramBlockingQueue.take();
            return (E) localObject1;
        } catch (InterruptedException localInterruptedException) {
            for (; ; ) {
                i = 1;
            }
        } finally {
            if (i != 0) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static <E> void putUninterruptibly(BlockingQueue<E> paramBlockingQueue, E paramE) {
        int i = 0;
        try {
            paramBlockingQueue.put(paramE);
            return;
        } catch (InterruptedException localInterruptedException) {
            for (; ; ) {
                i = 1;
            }
        } finally {
            if (i != 0) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void sleepUninterruptibly(long paramLong, java.util.concurrent.TimeUnit paramTimeUnit) {
        // Byte code:
        //   0: iconst_0
        //   1: istore_3
        //   2: aload_2
        //   3: lload_0
        //   4: invokevirtual 33	java/util/concurrent/TimeUnit:toNanos	(J)J
        //   7: lstore 4
        //   9: invokestatic 39	java/lang/System:nanoTime	()J
        //   12: lload 4
        //   14: ladd
        //   15: lstore 6
        //   17: getstatic 43	java/util/concurrent/TimeUnit:NANOSECONDS	Ljava/util/concurrent/TimeUnit;
        //   20: lload 4
        //   22: invokevirtual 97	java/util/concurrent/TimeUnit:sleep	(J)V
        //   25: iload_3
        //   26: ifeq +9 -> 35
        //   29: invokestatic 21	java/lang/Thread:currentThread	()Ljava/lang/Thread;
        //   32: invokevirtual 24	java/lang/Thread:interrupt	()V
        //   35: return
        //   36: astore 8
        //   38: iconst_1
        //   39: istore_3
        //   40: lload 6
        //   42: invokestatic 39	java/lang/System:nanoTime	()J
        //   45: lsub
        //   46: lstore 4
        //   48: goto -31 -> 17
        //   51: astore 9
        //   53: iload_3
        //   54: ifeq +9 -> 63
        //   57: invokestatic 21	java/lang/Thread:currentThread	()Ljava/lang/Thread;
        //   60: invokevirtual 24	java/lang/Thread:interrupt	()V
        //   63: aload 9
        //   65: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	66	0	paramLong	long
        //   0	66	2	paramTimeUnit	java.util.concurrent.TimeUnit
        //   1	53	3	i	int
        //   7	40	4	l1	long
        //   15	26	6	l2	long
        //   36	1	8	localInterruptedException	InterruptedException
        //   51	13	9	localObject	Object
        // Exception table:
        //   from	to	target	type
        //   17	25	36	java/lang/InterruptedException
        //   2	25	51	finally
        //   36	53	51	finally
    }
}




