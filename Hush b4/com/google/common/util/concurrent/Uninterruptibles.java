// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.BlockingQueue;
import com.google.common.base.Preconditions;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CountDownLatch;
import com.google.common.annotations.Beta;

@Beta
public final class Uninterruptibles
{
    public static void awaitUninterruptibly(final CountDownLatch latch) {
        boolean interrupted = false;
        while (true) {
            try {
                latch.await();
            }
            catch (InterruptedException e) {
                interrupted = true;
                continue;
            }
            finally {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
            }
            break;
        }
    }
    
    public static boolean awaitUninterruptibly(final CountDownLatch latch, final long timeout, final TimeUnit unit) {
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(timeout);
            final long end = System.nanoTime() + remainingNanos;
            try {
                return latch.await(remainingNanos, TimeUnit.NANOSECONDS);
            }
            catch (InterruptedException e) {
                interrupted = true;
                remainingNanos = end - System.nanoTime();
            }
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public static void joinUninterruptibly(final Thread toJoin) {
        boolean interrupted = false;
        while (true) {
            try {
                toJoin.join();
            }
            catch (InterruptedException e) {
                interrupted = true;
                continue;
            }
            finally {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
            }
            break;
        }
    }
    
    public static <V> V getUninterruptibly(final Future<V> future) throws ExecutionException {
        boolean interrupted = false;
        try {
            return future.get();
        }
        catch (InterruptedException e) {
            interrupted = true;
            return future.get();
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public static <V> V getUninterruptibly(final Future<V> future, final long timeout, final TimeUnit unit) throws ExecutionException, TimeoutException {
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(timeout);
            final long end = System.nanoTime() + remainingNanos;
            try {
                return future.get(remainingNanos, TimeUnit.NANOSECONDS);
            }
            catch (InterruptedException e) {
                interrupted = true;
                remainingNanos = end - System.nanoTime();
            }
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public static void joinUninterruptibly(final Thread toJoin, final long timeout, final TimeUnit unit) {
        Preconditions.checkNotNull(toJoin);
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(timeout);
            final long end = System.nanoTime() + remainingNanos;
            try {
                TimeUnit.NANOSECONDS.timedJoin(toJoin, remainingNanos);
            }
            catch (InterruptedException e) {
                interrupted = true;
                remainingNanos = end - System.nanoTime();
            }
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public static <E> E takeUninterruptibly(final BlockingQueue<E> queue) {
        boolean interrupted = false;
        try {
            return queue.take();
        }
        catch (InterruptedException e) {
            interrupted = true;
            return queue.take();
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public static <E> void putUninterruptibly(final BlockingQueue<E> queue, final E element) {
        boolean interrupted = false;
        while (true) {
            try {
                queue.put(element);
            }
            catch (InterruptedException e) {
                interrupted = true;
                continue;
            }
            finally {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
            }
            break;
        }
    }
    
    public static void sleepUninterruptibly(final long sleepFor, final TimeUnit unit) {
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(sleepFor);
            final long end = System.nanoTime() + remainingNanos;
            try {
                TimeUnit.NANOSECONDS.sleep(remainingNanos);
            }
            catch (InterruptedException e) {
                interrupted = true;
                remainingNanos = end - System.nanoTime();
            }
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    private Uninterruptibles() {
    }
}
