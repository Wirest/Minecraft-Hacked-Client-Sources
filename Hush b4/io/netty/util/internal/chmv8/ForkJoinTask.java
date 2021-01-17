// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal.chmv8;

import java.util.concurrent.RunnableFuture;
import java.lang.ref.WeakReference;
import java.security.PrivilegedActionException;
import java.security.AccessController;
import java.lang.reflect.Field;
import java.security.PrivilegedExceptionAction;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.List;
import java.util.RandomAccess;
import java.util.Collection;
import java.util.concurrent.CancellationException;
import sun.misc.Unsafe;
import java.lang.ref.ReferenceQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.io.Serializable;
import java.util.concurrent.Future;

public abstract class ForkJoinTask<V> implements Future<V>, Serializable
{
    volatile int status;
    static final int DONE_MASK = -268435456;
    static final int NORMAL = -268435456;
    static final int CANCELLED = -1073741824;
    static final int EXCEPTIONAL = Integer.MIN_VALUE;
    static final int SIGNAL = 65536;
    static final int SMASK = 65535;
    private static final ExceptionNode[] exceptionTable;
    private static final ReentrantLock exceptionTableLock;
    private static final ReferenceQueue<Object> exceptionTableRefQueue;
    private static final int EXCEPTION_MAP_CAPACITY = 32;
    private static final long serialVersionUID = -7721805057305804111L;
    private static final Unsafe U;
    private static final long STATUS;
    
    private int setCompletion(final int completion) {
        int s;
        while ((s = this.status) >= 0) {
            if (ForkJoinTask.U.compareAndSwapInt(this, ForkJoinTask.STATUS, s, s | completion)) {
                if (s >>> 16 != 0) {
                    synchronized (this) {
                        this.notifyAll();
                    }
                }
                return completion;
            }
        }
        return s;
    }
    
    final int doExec() {
        int s;
        if ((s = this.status) >= 0) {
            boolean completed;
            try {
                completed = this.exec();
            }
            catch (Throwable rex) {
                return this.setExceptionalCompletion(rex);
            }
            if (completed) {
                s = this.setCompletion(-268435456);
            }
        }
        return s;
    }
    
    final boolean trySetSignal() {
        final int s = this.status;
        return s >= 0 && ForkJoinTask.U.compareAndSwapInt(this, ForkJoinTask.STATUS, s, s | 0x10000);
    }
    
    private int externalAwaitDone() {
        final ForkJoinPool cp = ForkJoinPool.common;
        int s;
        if ((s = this.status) >= 0) {
            if (cp != null) {
                if (this instanceof CountedCompleter) {
                    s = cp.externalHelpComplete((CountedCompleter<?>)this);
                }
                else if (cp.tryExternalUnpush(this)) {
                    s = this.doExec();
                }
            }
            if (s >= 0 && (s = this.status) >= 0) {
                boolean interrupted = false;
                do {
                    if (ForkJoinTask.U.compareAndSwapInt(this, ForkJoinTask.STATUS, s, s | 0x10000)) {
                        synchronized (this) {
                            if (this.status >= 0) {
                                try {
                                    this.wait();
                                }
                                catch (InterruptedException ie) {
                                    interrupted = true;
                                }
                            }
                            else {
                                this.notifyAll();
                            }
                        }
                    }
                } while ((s = this.status) >= 0);
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return s;
    }
    
    private int externalInterruptibleAwaitDone() throws InterruptedException {
        final ForkJoinPool cp = ForkJoinPool.common;
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        int s;
        if ((s = this.status) >= 0 && cp != null) {
            if (this instanceof CountedCompleter) {
                cp.externalHelpComplete((CountedCompleter<?>)this);
            }
            else if (cp.tryExternalUnpush(this)) {
                this.doExec();
            }
        }
        while ((s = this.status) >= 0) {
            if (ForkJoinTask.U.compareAndSwapInt(this, ForkJoinTask.STATUS, s, s | 0x10000)) {
                synchronized (this) {
                    if (this.status >= 0) {
                        this.wait();
                    }
                    else {
                        this.notifyAll();
                    }
                }
            }
        }
        return s;
    }
    
    private int doJoin() {
        int s;
        Thread t;
        ForkJoinWorkerThread wt;
        ForkJoinPool.WorkQueue w;
        return ((s = this.status) < 0) ? s : (((t = Thread.currentThread()) instanceof ForkJoinWorkerThread) ? (((w = (wt = (ForkJoinWorkerThread)t).workQueue).tryUnpush(this) && (s = this.doExec()) < 0) ? s : wt.pool.awaitJoin(w, this)) : this.externalAwaitDone());
    }
    
    private int doInvoke() {
        final int s;
        Thread t;
        ForkJoinWorkerThread wt;
        return ((s = this.doExec()) < 0) ? s : (((t = Thread.currentThread()) instanceof ForkJoinWorkerThread) ? (wt = (ForkJoinWorkerThread)t).pool.awaitJoin(wt.workQueue, this) : this.externalAwaitDone());
    }
    
    final int recordExceptionalCompletion(final Throwable ex) {
        int s;
        if ((s = this.status) >= 0) {
            final int h = System.identityHashCode(this);
            final ReentrantLock lock = ForkJoinTask.exceptionTableLock;
            lock.lock();
            Label_0115: {
                try {
                    expungeStaleExceptions();
                    final ExceptionNode[] t = ForkJoinTask.exceptionTable;
                    final int i = h & t.length - 1;
                    for (ExceptionNode e = t[i]; e != null; e = e.next) {
                        if (e.get() == this) {
                            break Label_0115;
                        }
                    }
                    t[i] = new ExceptionNode(this, ex, t[i]);
                }
                finally {
                    lock.unlock();
                }
            }
            s = this.setCompletion(Integer.MIN_VALUE);
        }
        return s;
    }
    
    private int setExceptionalCompletion(final Throwable ex) {
        final int s = this.recordExceptionalCompletion(ex);
        if ((s & 0xF0000000) == Integer.MIN_VALUE) {
            this.internalPropagateException(ex);
        }
        return s;
    }
    
    void internalPropagateException(final Throwable ex) {
    }
    
    static final void cancelIgnoringExceptions(final ForkJoinTask<?> t) {
        if (t != null && t.status >= 0) {
            try {
                t.cancel(false);
            }
            catch (Throwable t2) {}
        }
    }
    
    private void clearExceptionalCompletion() {
        final int h = System.identityHashCode(this);
        final ReentrantLock lock = ForkJoinTask.exceptionTableLock;
        lock.lock();
        try {
            final ExceptionNode[] t = ForkJoinTask.exceptionTable;
            final int i = h & t.length - 1;
            ExceptionNode e = t[i];
            ExceptionNode pred = null;
            while (e != null) {
                final ExceptionNode next = e.next;
                if (e.get() == this) {
                    if (pred == null) {
                        t[i] = next;
                        break;
                    }
                    pred.next = next;
                    break;
                }
                else {
                    pred = e;
                    e = next;
                }
            }
            expungeStaleExceptions();
            this.status = 0;
        }
        finally {
            lock.unlock();
        }
    }
    
    private Throwable getThrowableException() {
        if ((this.status & 0xF0000000) != Integer.MIN_VALUE) {
            return null;
        }
        final int h = System.identityHashCode(this);
        final ReentrantLock lock = ForkJoinTask.exceptionTableLock;
        lock.lock();
        ExceptionNode e;
        try {
            expungeStaleExceptions();
            final ExceptionNode[] t = ForkJoinTask.exceptionTable;
            for (e = t[h & t.length - 1]; e != null && e.get() != this; e = e.next) {}
        }
        finally {
            lock.unlock();
        }
        final Throwable ex;
        if (e == null || (ex = e.ex) == null) {
            return null;
        }
        return ex;
    }
    
    private static void expungeStaleExceptions() {
        Object x;
        while ((x = ForkJoinTask.exceptionTableRefQueue.poll()) != null) {
            if (x instanceof ExceptionNode) {
                final ForkJoinTask<?> key = ((ExceptionNode)x).get();
                final ExceptionNode[] t = ForkJoinTask.exceptionTable;
                final int i = System.identityHashCode(key) & t.length - 1;
                ExceptionNode e = t[i];
                ExceptionNode pred = null;
                while (e != null) {
                    final ExceptionNode next = e.next;
                    if (e == x) {
                        if (pred == null) {
                            t[i] = next;
                            break;
                        }
                        pred.next = next;
                        break;
                    }
                    else {
                        pred = e;
                        e = next;
                    }
                }
            }
        }
    }
    
    static final void helpExpungeStaleExceptions() {
        final ReentrantLock lock = ForkJoinTask.exceptionTableLock;
        if (lock.tryLock()) {
            try {
                expungeStaleExceptions();
            }
            finally {
                lock.unlock();
            }
        }
    }
    
    static void rethrow(final Throwable ex) {
        if (ex != null) {
            uncheckedThrow(ex);
        }
    }
    
    static <T extends Throwable> void uncheckedThrow(final Throwable t) throws T, Throwable {
        throw t;
    }
    
    private void reportException(final int s) {
        if (s == -1073741824) {
            throw new CancellationException();
        }
        if (s == Integer.MIN_VALUE) {
            rethrow(this.getThrowableException());
        }
    }
    
    public final ForkJoinTask<V> fork() {
        final Thread t;
        if ((t = Thread.currentThread()) instanceof ForkJoinWorkerThread) {
            ((ForkJoinWorkerThread)t).workQueue.push(this);
        }
        else {
            ForkJoinPool.common.externalPush(this);
        }
        return this;
    }
    
    public final V join() {
        final int s;
        if ((s = (this.doJoin() & 0xF0000000)) != -268435456) {
            this.reportException(s);
        }
        return this.getRawResult();
    }
    
    public final V invoke() {
        final int s;
        if ((s = (this.doInvoke() & 0xF0000000)) != -268435456) {
            this.reportException(s);
        }
        return this.getRawResult();
    }
    
    public static void invokeAll(final ForkJoinTask<?> t1, final ForkJoinTask<?> t2) {
        t2.fork();
        final int s1;
        if ((s1 = (t1.doInvoke() & 0xF0000000)) != -268435456) {
            t1.reportException(s1);
        }
        final int s2;
        if ((s2 = (t2.doJoin() & 0xF0000000)) != -268435456) {
            t2.reportException(s2);
        }
    }
    
    public static void invokeAll(final ForkJoinTask<?>... tasks) {
        Throwable ex = null;
        int i;
        int last;
        for (last = (i = tasks.length - 1); i >= 0; --i) {
            final ForkJoinTask<?> t = tasks[i];
            if (t == null) {
                if (ex == null) {
                    ex = new NullPointerException();
                }
            }
            else if (i != 0) {
                t.fork();
            }
            else if (t.doInvoke() < -268435456 && ex == null) {
                ex = t.getException();
            }
        }
        for (i = 1; i <= last; ++i) {
            final ForkJoinTask<?> t = tasks[i];
            if (t != null) {
                if (ex != null) {
                    t.cancel(false);
                }
                else if (t.doJoin() < -268435456) {
                    ex = t.getException();
                }
            }
        }
        if (ex != null) {
            rethrow(ex);
        }
    }
    
    public static <T extends ForkJoinTask<?>> Collection<T> invokeAll(final Collection<T> tasks) {
        if (!(tasks instanceof RandomAccess) || !(tasks instanceof List)) {
            invokeAll((ForkJoinTask<?>[])tasks.toArray(new ForkJoinTask[tasks.size()]));
            return tasks;
        }
        final List<? extends ForkJoinTask<?>> ts = (List<? extends ForkJoinTask<?>>)(List)tasks;
        Throwable ex = null;
        int i;
        int last;
        for (last = (i = ts.size() - 1); i >= 0; --i) {
            final ForkJoinTask<?> t = (ForkJoinTask<?>)ts.get(i);
            if (t == null) {
                if (ex == null) {
                    ex = new NullPointerException();
                }
            }
            else if (i != 0) {
                t.fork();
            }
            else if (t.doInvoke() < -268435456 && ex == null) {
                ex = t.getException();
            }
        }
        for (i = 1; i <= last; ++i) {
            final ForkJoinTask<?> t = (ForkJoinTask<?>)ts.get(i);
            if (t != null) {
                if (ex != null) {
                    t.cancel(false);
                }
                else if (t.doJoin() < -268435456) {
                    ex = t.getException();
                }
            }
        }
        if (ex != null) {
            rethrow(ex);
        }
        return tasks;
    }
    
    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        return (this.setCompletion(-1073741824) & 0xF0000000) == 0xC0000000;
    }
    
    @Override
    public final boolean isDone() {
        return this.status < 0;
    }
    
    @Override
    public final boolean isCancelled() {
        return (this.status & 0xF0000000) == 0xC0000000;
    }
    
    public final boolean isCompletedAbnormally() {
        return this.status < -268435456;
    }
    
    public final boolean isCompletedNormally() {
        return (this.status & 0xF0000000) == 0xF0000000;
    }
    
    public final Throwable getException() {
        final int s = this.status & 0xF0000000;
        return (s >= -268435456) ? null : ((s == -1073741824) ? new CancellationException() : this.getThrowableException());
    }
    
    public void completeExceptionally(final Throwable ex) {
        this.setExceptionalCompletion((ex instanceof RuntimeException || ex instanceof Error) ? ex : new RuntimeException(ex));
    }
    
    public void complete(final V value) {
        try {
            this.setRawResult(value);
        }
        catch (Throwable rex) {
            this.setExceptionalCompletion(rex);
            return;
        }
        this.setCompletion(-268435456);
    }
    
    public final void quietlyComplete() {
        this.setCompletion(-268435456);
    }
    
    @Override
    public final V get() throws InterruptedException, ExecutionException {
        int s = (Thread.currentThread() instanceof ForkJoinWorkerThread) ? this.doJoin() : this.externalInterruptibleAwaitDone();
        if ((s &= 0xF0000000) == 0xC0000000) {
            throw new CancellationException();
        }
        final Throwable ex;
        if (s == Integer.MIN_VALUE && (ex = this.getThrowableException()) != null) {
            throw new ExecutionException(ex);
        }
        return this.getRawResult();
    }
    
    @Override
    public final V get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        long ns = unit.toNanos(timeout);
        int s;
        if ((s = this.status) >= 0 && ns > 0L) {
            final long deadline = System.nanoTime() + ns;
            ForkJoinPool p = null;
            ForkJoinPool.WorkQueue w = null;
            final Thread t = Thread.currentThread();
            if (t instanceof ForkJoinWorkerThread) {
                final ForkJoinWorkerThread wt = (ForkJoinWorkerThread)t;
                p = wt.pool;
                w = wt.workQueue;
                p.helpJoinOnce(w, this);
            }
            else {
                final ForkJoinPool cp;
                if ((cp = ForkJoinPool.common) != null) {
                    if (this instanceof CountedCompleter) {
                        cp.externalHelpComplete((CountedCompleter<?>)this);
                    }
                    else if (cp.tryExternalUnpush(this)) {
                        this.doExec();
                    }
                }
            }
            boolean canBlock = false;
            boolean interrupted = false;
            try {
                while ((s = this.status) >= 0) {
                    if (w != null && w.qlock < 0) {
                        cancelIgnoringExceptions(this);
                    }
                    else if (!canBlock) {
                        if (p != null && !p.tryCompensate(p.ctl)) {
                            continue;
                        }
                        canBlock = true;
                    }
                    else {
                        final long ms;
                        if ((ms = TimeUnit.NANOSECONDS.toMillis(ns)) > 0L && ForkJoinTask.U.compareAndSwapInt(this, ForkJoinTask.STATUS, s, s | 0x10000)) {
                            synchronized (this) {
                                if (this.status >= 0) {
                                    try {
                                        this.wait(ms);
                                    }
                                    catch (InterruptedException ie) {
                                        if (p == null) {
                                            interrupted = true;
                                        }
                                    }
                                }
                                else {
                                    this.notifyAll();
                                }
                            }
                        }
                        if ((s = this.status) < 0 || interrupted || (ns = deadline - System.nanoTime()) <= 0L) {
                            break;
                        }
                        continue;
                    }
                }
            }
            finally {
                if (p != null && canBlock) {
                    p.incrementActiveCount();
                }
            }
            if (interrupted) {
                throw new InterruptedException();
            }
        }
        if ((s &= 0xF0000000) != 0xF0000000) {
            if (s == -1073741824) {
                throw new CancellationException();
            }
            if (s != Integer.MIN_VALUE) {
                throw new TimeoutException();
            }
            final Throwable ex;
            if ((ex = this.getThrowableException()) != null) {
                throw new ExecutionException(ex);
            }
        }
        return this.getRawResult();
    }
    
    public final void quietlyJoin() {
        this.doJoin();
    }
    
    public final void quietlyInvoke() {
        this.doInvoke();
    }
    
    public static void helpQuiesce() {
        final Thread t;
        if ((t = Thread.currentThread()) instanceof ForkJoinWorkerThread) {
            final ForkJoinWorkerThread wt = (ForkJoinWorkerThread)t;
            wt.pool.helpQuiescePool(wt.workQueue);
        }
        else {
            ForkJoinPool.quiesceCommonPool();
        }
    }
    
    public void reinitialize() {
        if ((this.status & 0xF0000000) == Integer.MIN_VALUE) {
            this.clearExceptionalCompletion();
        }
        else {
            this.status = 0;
        }
    }
    
    public static ForkJoinPool getPool() {
        final Thread t = Thread.currentThread();
        return (t instanceof ForkJoinWorkerThread) ? ((ForkJoinWorkerThread)t).pool : null;
    }
    
    public static boolean inForkJoinPool() {
        return Thread.currentThread() instanceof ForkJoinWorkerThread;
    }
    
    public boolean tryUnfork() {
        final Thread t;
        return ((t = Thread.currentThread()) instanceof ForkJoinWorkerThread) ? ((ForkJoinWorkerThread)t).workQueue.tryUnpush(this) : ForkJoinPool.common.tryExternalUnpush(this);
    }
    
    public static int getQueuedTaskCount() {
        final Thread t;
        ForkJoinPool.WorkQueue q;
        if ((t = Thread.currentThread()) instanceof ForkJoinWorkerThread) {
            q = ((ForkJoinWorkerThread)t).workQueue;
        }
        else {
            q = ForkJoinPool.commonSubmitterQueue();
        }
        return (q == null) ? 0 : q.queueSize();
    }
    
    public static int getSurplusQueuedTaskCount() {
        return ForkJoinPool.getSurplusQueuedTaskCount();
    }
    
    public abstract V getRawResult();
    
    protected abstract void setRawResult(final V p0);
    
    protected abstract boolean exec();
    
    protected static ForkJoinTask<?> peekNextLocalTask() {
        final Thread t;
        ForkJoinPool.WorkQueue q;
        if ((t = Thread.currentThread()) instanceof ForkJoinWorkerThread) {
            q = ((ForkJoinWorkerThread)t).workQueue;
        }
        else {
            q = ForkJoinPool.commonSubmitterQueue();
        }
        return (q == null) ? null : q.peek();
    }
    
    protected static ForkJoinTask<?> pollNextLocalTask() {
        final Thread t;
        return ((t = Thread.currentThread()) instanceof ForkJoinWorkerThread) ? ((ForkJoinWorkerThread)t).workQueue.nextLocalTask() : null;
    }
    
    protected static ForkJoinTask<?> pollTask() {
        final Thread t;
        ForkJoinWorkerThread wt;
        return ((t = Thread.currentThread()) instanceof ForkJoinWorkerThread) ? (wt = (ForkJoinWorkerThread)t).pool.nextTaskFor(wt.workQueue) : null;
    }
    
    public final short getForkJoinTaskTag() {
        return (short)this.status;
    }
    
    public final short setForkJoinTaskTag(final short tag) {
        int s;
        while (!ForkJoinTask.U.compareAndSwapInt(this, ForkJoinTask.STATUS, s = this.status, (s & 0xFFFF0000) | (tag & 0xFFFF))) {}
        return (short)s;
    }
    
    public final boolean compareAndSetForkJoinTaskTag(final short e, final short tag) {
        int s;
        while ((short)(s = this.status) == e) {
            if (ForkJoinTask.U.compareAndSwapInt(this, ForkJoinTask.STATUS, s, (s & 0xFFFF0000) | (tag & 0xFFFF))) {
                return true;
            }
        }
        return false;
    }
    
    public static ForkJoinTask<?> adapt(final Runnable runnable) {
        return new AdaptedRunnableAction(runnable);
    }
    
    public static <T> ForkJoinTask<T> adapt(final Runnable runnable, final T result) {
        return new AdaptedRunnable<T>(runnable, result);
    }
    
    public static <T> ForkJoinTask<T> adapt(final Callable<? extends T> callable) {
        return new AdaptedCallable<T>(callable);
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeObject(this.getException());
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        final Object ex = s.readObject();
        if (ex != null) {
            this.setExceptionalCompletion((Throwable)ex);
        }
    }
    
    private static Unsafe getUnsafe() {
        try {
            return Unsafe.getUnsafe();
        }
        catch (SecurityException tryReflectionInstead) {
            try {
                return AccessController.doPrivileged((PrivilegedExceptionAction<Unsafe>)new PrivilegedExceptionAction<Unsafe>() {
                    @Override
                    public Unsafe run() throws Exception {
                        final Class<Unsafe> k = Unsafe.class;
                        for (final Field f : k.getDeclaredFields()) {
                            f.setAccessible(true);
                            final Object x = f.get(null);
                            if (k.isInstance(x)) {
                                return k.cast(x);
                            }
                        }
                        throw new NoSuchFieldError("the Unsafe");
                    }
                });
            }
            catch (PrivilegedActionException e) {
                throw new RuntimeException("Could not initialize intrinsics", e.getCause());
            }
        }
    }
    
    static {
        exceptionTableLock = new ReentrantLock();
        exceptionTableRefQueue = new ReferenceQueue<Object>();
        exceptionTable = new ExceptionNode[32];
        try {
            U = getUnsafe();
            final Class<?> k = ForkJoinTask.class;
            STATUS = ForkJoinTask.U.objectFieldOffset(k.getDeclaredField("status"));
        }
        catch (Exception e) {
            throw new Error(e);
        }
    }
    
    static final class ExceptionNode extends WeakReference<ForkJoinTask<?>>
    {
        final Throwable ex;
        ExceptionNode next;
        final long thrower;
        
        ExceptionNode(final ForkJoinTask<?> task, final Throwable ex, final ExceptionNode next) {
            super(task, ForkJoinTask.exceptionTableRefQueue);
            this.ex = ex;
            this.next = next;
            this.thrower = Thread.currentThread().getId();
        }
    }
    
    static final class AdaptedRunnable<T> extends ForkJoinTask<T> implements RunnableFuture<T>
    {
        final Runnable runnable;
        T result;
        private static final long serialVersionUID = 5232453952276885070L;
        
        AdaptedRunnable(final Runnable runnable, final T result) {
            if (runnable == null) {
                throw new NullPointerException();
            }
            this.runnable = runnable;
            this.result = result;
        }
        
        @Override
        public final T getRawResult() {
            return this.result;
        }
        
        public final void setRawResult(final T v) {
            this.result = v;
        }
        
        public final boolean exec() {
            this.runnable.run();
            return true;
        }
        
        @Override
        public final void run() {
            this.invoke();
        }
    }
    
    static final class AdaptedRunnableAction extends ForkJoinTask<Void> implements RunnableFuture<Void>
    {
        final Runnable runnable;
        private static final long serialVersionUID = 5232453952276885070L;
        
        AdaptedRunnableAction(final Runnable runnable) {
            if (runnable == null) {
                throw new NullPointerException();
            }
            this.runnable = runnable;
        }
        
        @Override
        public final Void getRawResult() {
            return null;
        }
        
        public final void setRawResult(final Void v) {
        }
        
        public final boolean exec() {
            this.runnable.run();
            return true;
        }
        
        @Override
        public final void run() {
            this.invoke();
        }
    }
    
    static final class RunnableExecuteAction extends ForkJoinTask<Void>
    {
        final Runnable runnable;
        private static final long serialVersionUID = 5232453952276885070L;
        
        RunnableExecuteAction(final Runnable runnable) {
            if (runnable == null) {
                throw new NullPointerException();
            }
            this.runnable = runnable;
        }
        
        @Override
        public final Void getRawResult() {
            return null;
        }
        
        public final void setRawResult(final Void v) {
        }
        
        public final boolean exec() {
            this.runnable.run();
            return true;
        }
        
        @Override
        void internalPropagateException(final Throwable ex) {
            ForkJoinTask.rethrow(ex);
        }
    }
    
    static final class AdaptedCallable<T> extends ForkJoinTask<T> implements RunnableFuture<T>
    {
        final Callable<? extends T> callable;
        T result;
        private static final long serialVersionUID = 2838392045355241008L;
        
        AdaptedCallable(final Callable<? extends T> callable) {
            if (callable == null) {
                throw new NullPointerException();
            }
            this.callable = callable;
        }
        
        @Override
        public final T getRawResult() {
            return this.result;
        }
        
        public final void setRawResult(final T v) {
            this.result = v;
        }
        
        public final boolean exec() {
            try {
                this.result = (T)this.callable.call();
                return true;
            }
            catch (Error err) {
                throw err;
            }
            catch (RuntimeException rex) {
                throw rex;
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        
        @Override
        public final void run() {
            this.invoke();
        }
    }
}
