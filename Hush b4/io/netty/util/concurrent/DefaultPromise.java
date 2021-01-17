// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.concurrent;

import java.util.ArrayDeque;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.InternalThreadLocalMap;
import java.util.concurrent.TimeUnit;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.CancellationException;
import io.netty.util.Signal;
import io.netty.util.internal.logging.InternalLogger;

public class DefaultPromise<V> extends AbstractFuture<V> implements Promise<V>
{
    private static final InternalLogger logger;
    private static final InternalLogger rejectedExecutionLogger;
    private static final int MAX_LISTENER_STACK_DEPTH = 8;
    private static final Signal SUCCESS;
    private static final Signal UNCANCELLABLE;
    private static final CauseHolder CANCELLATION_CAUSE_HOLDER;
    private final EventExecutor executor;
    private volatile Object result;
    private Object listeners;
    private LateListeners lateListeners;
    private short waiters;
    
    public DefaultPromise(final EventExecutor executor) {
        if (executor == null) {
            throw new NullPointerException("executor");
        }
        this.executor = executor;
    }
    
    protected DefaultPromise() {
        this.executor = null;
    }
    
    protected EventExecutor executor() {
        return this.executor;
    }
    
    @Override
    public boolean isCancelled() {
        return isCancelled0(this.result);
    }
    
    private static boolean isCancelled0(final Object result) {
        return result instanceof CauseHolder && ((CauseHolder)result).cause instanceof CancellationException;
    }
    
    @Override
    public boolean isCancellable() {
        return this.result == null;
    }
    
    @Override
    public boolean isDone() {
        return isDone0(this.result);
    }
    
    private static boolean isDone0(final Object result) {
        return result != null && result != DefaultPromise.UNCANCELLABLE;
    }
    
    @Override
    public boolean isSuccess() {
        final Object result = this.result;
        return result != null && result != DefaultPromise.UNCANCELLABLE && !(result instanceof CauseHolder);
    }
    
    @Override
    public Throwable cause() {
        final Object result = this.result;
        if (result instanceof CauseHolder) {
            return ((CauseHolder)result).cause;
        }
        return null;
    }
    
    @Override
    public Promise<V> addListener(final GenericFutureListener<? extends Future<? super V>> listener) {
        if (listener == null) {
            throw new NullPointerException("listener");
        }
        if (this.isDone()) {
            this.notifyLateListener(listener);
            return this;
        }
        synchronized (this) {
            if (!this.isDone()) {
                if (this.listeners == null) {
                    this.listeners = listener;
                }
                else if (this.listeners instanceof DefaultFutureListeners) {
                    ((DefaultFutureListeners)this.listeners).add(listener);
                }
                else {
                    final GenericFutureListener<? extends Future<V>> firstListener = (GenericFutureListener<? extends Future<V>>)this.listeners;
                    this.listeners = new DefaultFutureListeners(firstListener, listener);
                }
                return this;
            }
        }
        this.notifyLateListener(listener);
        return this;
    }
    
    @Override
    public Promise<V> addListeners(final GenericFutureListener<? extends Future<? super V>>... listeners) {
        if (listeners == null) {
            throw new NullPointerException("listeners");
        }
        for (final GenericFutureListener<? extends Future<? super V>> l : listeners) {
            if (l == null) {
                break;
            }
            this.addListener(l);
        }
        return this;
    }
    
    @Override
    public Promise<V> removeListener(final GenericFutureListener<? extends Future<? super V>> listener) {
        if (listener == null) {
            throw new NullPointerException("listener");
        }
        if (this.isDone()) {
            return this;
        }
        synchronized (this) {
            if (!this.isDone()) {
                if (this.listeners instanceof DefaultFutureListeners) {
                    ((DefaultFutureListeners)this.listeners).remove(listener);
                }
                else if (this.listeners == listener) {
                    this.listeners = null;
                }
            }
        }
        return this;
    }
    
    @Override
    public Promise<V> removeListeners(final GenericFutureListener<? extends Future<? super V>>... listeners) {
        if (listeners == null) {
            throw new NullPointerException("listeners");
        }
        for (final GenericFutureListener<? extends Future<? super V>> l : listeners) {
            if (l == null) {
                break;
            }
            this.removeListener(l);
        }
        return this;
    }
    
    @Override
    public Promise<V> sync() throws InterruptedException {
        this.await();
        this.rethrowIfFailed();
        return this;
    }
    
    @Override
    public Promise<V> syncUninterruptibly() {
        this.awaitUninterruptibly();
        this.rethrowIfFailed();
        return this;
    }
    
    private void rethrowIfFailed() {
        final Throwable cause = this.cause();
        if (cause == null) {
            return;
        }
        PlatformDependent.throwException(cause);
    }
    
    @Override
    public Promise<V> await() throws InterruptedException {
        if (this.isDone()) {
            return this;
        }
        if (Thread.interrupted()) {
            throw new InterruptedException(this.toString());
        }
        synchronized (this) {
            while (!this.isDone()) {
                this.checkDeadLock();
                this.incWaiters();
                try {
                    this.wait();
                }
                finally {
                    this.decWaiters();
                }
            }
        }
        return this;
    }
    
    @Override
    public boolean await(final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.await0(unit.toNanos(timeout), true);
    }
    
    @Override
    public boolean await(final long timeoutMillis) throws InterruptedException {
        return this.await0(TimeUnit.MILLISECONDS.toNanos(timeoutMillis), true);
    }
    
    @Override
    public Promise<V> awaitUninterruptibly() {
        if (this.isDone()) {
            return this;
        }
        boolean interrupted = false;
        synchronized (this) {
            while (!this.isDone()) {
                this.checkDeadLock();
                this.incWaiters();
                try {
                    this.wait();
                }
                catch (InterruptedException e) {
                    interrupted = true;
                }
                finally {
                    this.decWaiters();
                }
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
        return this;
    }
    
    @Override
    public boolean awaitUninterruptibly(final long timeout, final TimeUnit unit) {
        try {
            return this.await0(unit.toNanos(timeout), false);
        }
        catch (InterruptedException e) {
            throw new InternalError();
        }
    }
    
    @Override
    public boolean awaitUninterruptibly(final long timeoutMillis) {
        try {
            return this.await0(TimeUnit.MILLISECONDS.toNanos(timeoutMillis), false);
        }
        catch (InterruptedException e) {
            throw new InternalError();
        }
    }
    
    private boolean await0(final long timeoutNanos, final boolean interruptable) throws InterruptedException {
        if (this.isDone()) {
            return true;
        }
        if (timeoutNanos <= 0L) {
            return this.isDone();
        }
        if (interruptable && Thread.interrupted()) {
            throw new InterruptedException(this.toString());
        }
        final long startTime = System.nanoTime();
        long waitTime = timeoutNanos;
        boolean interrupted = false;
        try {
            synchronized (this) {
                if (this.isDone()) {
                    return true;
                }
                if (waitTime <= 0L) {
                    return this.isDone();
                }
                this.checkDeadLock();
                this.incWaiters();
                try {
                    while (true) {
                        try {
                            this.wait(waitTime / 1000000L, (int)(waitTime % 1000000L));
                        }
                        catch (InterruptedException e) {
                            if (interruptable) {
                                throw e;
                            }
                            interrupted = true;
                        }
                        if (this.isDone()) {
                            return true;
                        }
                        waitTime = timeoutNanos - (System.nanoTime() - startTime);
                        if (waitTime <= 0L) {
                            return this.isDone();
                        }
                    }
                }
                finally {
                    this.decWaiters();
                }
            }
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    protected void checkDeadLock() {
        final EventExecutor e = this.executor();
        if (e != null && e.inEventLoop()) {
            throw new BlockingOperationException(this.toString());
        }
    }
    
    @Override
    public Promise<V> setSuccess(final V result) {
        if (this.setSuccess0(result)) {
            this.notifyListeners();
            return this;
        }
        throw new IllegalStateException("complete already: " + this);
    }
    
    @Override
    public boolean trySuccess(final V result) {
        if (this.setSuccess0(result)) {
            this.notifyListeners();
            return true;
        }
        return false;
    }
    
    @Override
    public Promise<V> setFailure(final Throwable cause) {
        if (this.setFailure0(cause)) {
            this.notifyListeners();
            return this;
        }
        throw new IllegalStateException("complete already: " + this, cause);
    }
    
    @Override
    public boolean tryFailure(final Throwable cause) {
        if (this.setFailure0(cause)) {
            this.notifyListeners();
            return true;
        }
        return false;
    }
    
    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        Object result = this.result;
        if (isDone0(result) || result == DefaultPromise.UNCANCELLABLE) {
            return false;
        }
        synchronized (this) {
            result = this.result;
            if (isDone0(result) || result == DefaultPromise.UNCANCELLABLE) {
                return false;
            }
            this.result = DefaultPromise.CANCELLATION_CAUSE_HOLDER;
            if (this.hasWaiters()) {
                this.notifyAll();
            }
        }
        this.notifyListeners();
        return true;
    }
    
    @Override
    public boolean setUncancellable() {
        Object result = this.result;
        if (isDone0(result)) {
            return !isCancelled0(result);
        }
        synchronized (this) {
            result = this.result;
            if (isDone0(result)) {
                return !isCancelled0(result);
            }
            this.result = DefaultPromise.UNCANCELLABLE;
        }
        return true;
    }
    
    private boolean setFailure0(final Throwable cause) {
        if (cause == null) {
            throw new NullPointerException("cause");
        }
        if (this.isDone()) {
            return false;
        }
        synchronized (this) {
            if (this.isDone()) {
                return false;
            }
            this.result = new CauseHolder(cause);
            if (this.hasWaiters()) {
                this.notifyAll();
            }
        }
        return true;
    }
    
    private boolean setSuccess0(final V result) {
        if (this.isDone()) {
            return false;
        }
        synchronized (this) {
            if (this.isDone()) {
                return false;
            }
            if (result == null) {
                this.result = DefaultPromise.SUCCESS;
            }
            else {
                this.result = result;
            }
            if (this.hasWaiters()) {
                this.notifyAll();
            }
        }
        return true;
    }
    
    @Override
    public V getNow() {
        final Object result = this.result;
        if (result instanceof CauseHolder || result == DefaultPromise.SUCCESS) {
            return null;
        }
        return (V)result;
    }
    
    private boolean hasWaiters() {
        return this.waiters > 0;
    }
    
    private void incWaiters() {
        if (this.waiters == 32767) {
            throw new IllegalStateException("too many waiters: " + this);
        }
        ++this.waiters;
    }
    
    private void decWaiters() {
        --this.waiters;
    }
    
    private void notifyListeners() {
        final Object listeners = this.listeners;
        if (listeners == null) {
            return;
        }
        final EventExecutor executor = this.executor();
        if (executor.inEventLoop()) {
            final InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
            final int stackDepth = threadLocals.futureListenerStackDepth();
            if (stackDepth < 8) {
                threadLocals.setFutureListenerStackDepth(stackDepth + 1);
                try {
                    if (listeners instanceof DefaultFutureListeners) {
                        notifyListeners0(this, (DefaultFutureListeners)listeners);
                    }
                    else {
                        final GenericFutureListener<? extends Future<V>> l = (GenericFutureListener<? extends Future<V>>)listeners;
                        notifyListener0(this, l);
                    }
                }
                finally {
                    this.listeners = null;
                    threadLocals.setFutureListenerStackDepth(stackDepth);
                }
                return;
            }
        }
        if (listeners instanceof DefaultFutureListeners) {
            final DefaultFutureListeners dfl = (DefaultFutureListeners)listeners;
            execute(executor, new Runnable() {
                @Override
                public void run() {
                    notifyListeners0(DefaultPromise.this, dfl);
                    DefaultPromise.this.listeners = null;
                }
            });
        }
        else {
            final GenericFutureListener<? extends Future<V>> i = (GenericFutureListener<? extends Future<V>>)listeners;
            execute(executor, new Runnable() {
                @Override
                public void run() {
                    DefaultPromise.notifyListener0(DefaultPromise.this, i);
                    DefaultPromise.this.listeners = null;
                }
            });
        }
    }
    
    private static void notifyListeners0(final Future<?> future, final DefaultFutureListeners listeners) {
        final GenericFutureListener<?>[] a = listeners.listeners();
        for (int size = listeners.size(), i = 0; i < size; ++i) {
            notifyListener0(future, a[i]);
        }
    }
    
    private void notifyLateListener(final GenericFutureListener<?> l) {
        final EventExecutor executor = this.executor();
        if (executor.inEventLoop()) {
            if (this.listeners != null || this.lateListeners != null) {
                LateListeners lateListeners = this.lateListeners;
                if (lateListeners == null) {
                    lateListeners = (this.lateListeners = new LateListeners());
                }
                lateListeners.add(l);
                execute(executor, lateListeners);
                return;
            }
            final InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
            final int stackDepth = threadLocals.futureListenerStackDepth();
            if (stackDepth < 8) {
                threadLocals.setFutureListenerStackDepth(stackDepth + 1);
                try {
                    notifyListener0(this, l);
                }
                finally {
                    threadLocals.setFutureListenerStackDepth(stackDepth);
                }
                return;
            }
        }
        execute(executor, new LateListenerNotifier(l));
    }
    
    protected static void notifyListener(final EventExecutor eventExecutor, final Future<?> future, final GenericFutureListener<?> l) {
        if (eventExecutor.inEventLoop()) {
            final InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
            final int stackDepth = threadLocals.futureListenerStackDepth();
            if (stackDepth < 8) {
                threadLocals.setFutureListenerStackDepth(stackDepth + 1);
                try {
                    notifyListener0(future, l);
                }
                finally {
                    threadLocals.setFutureListenerStackDepth(stackDepth);
                }
                return;
            }
        }
        execute(eventExecutor, new Runnable() {
            @Override
            public void run() {
                DefaultPromise.notifyListener0(future, l);
            }
        });
    }
    
    private static void execute(final EventExecutor executor, final Runnable task) {
        try {
            executor.execute(task);
        }
        catch (Throwable t) {
            DefaultPromise.rejectedExecutionLogger.error("Failed to submit a listener notification task. Event loop shut down?", t);
        }
    }
    
    static void notifyListener0(final Future future, final GenericFutureListener l) {
        try {
            l.operationComplete(future);
        }
        catch (Throwable t) {
            if (DefaultPromise.logger.isWarnEnabled()) {
                DefaultPromise.logger.warn("An exception was thrown by " + l.getClass().getName() + ".operationComplete()", t);
            }
        }
    }
    
    private synchronized Object progressiveListeners() {
        final Object listeners = this.listeners;
        if (listeners == null) {
            return null;
        }
        if (listeners instanceof DefaultFutureListeners) {
            final DefaultFutureListeners dfl = (DefaultFutureListeners)listeners;
            final int progressiveSize = dfl.progressiveSize();
            switch (progressiveSize) {
                case 0: {
                    return null;
                }
                case 1: {
                    for (final GenericFutureListener<?> l : dfl.listeners()) {
                        if (l instanceof GenericProgressiveFutureListener) {
                            return l;
                        }
                    }
                    return null;
                }
                default: {
                    final GenericFutureListener<?>[] array = dfl.listeners();
                    final GenericProgressiveFutureListener<?>[] copy = (GenericProgressiveFutureListener<?>[])new GenericProgressiveFutureListener[progressiveSize];
                    int i = 0;
                    int j = 0;
                    while (j < progressiveSize) {
                        final GenericFutureListener<?> k = array[i];
                        if (k instanceof GenericProgressiveFutureListener) {
                            copy[j++] = (GenericProgressiveFutureListener<?>)(GenericProgressiveFutureListener)k;
                        }
                        ++i;
                    }
                    return copy;
                }
            }
        }
        else {
            if (listeners instanceof GenericProgressiveFutureListener) {
                return listeners;
            }
            return null;
        }
    }
    
    void notifyProgressiveListeners(final long progress, final long total) {
        final Object listeners = this.progressiveListeners();
        if (listeners == null) {
            return;
        }
        final ProgressiveFuture<V> self = (ProgressiveFuture<V>)this;
        final EventExecutor executor = this.executor();
        if (executor.inEventLoop()) {
            if (listeners instanceof GenericProgressiveFutureListener[]) {
                notifyProgressiveListeners0(self, (GenericProgressiveFutureListener<?>[])listeners, progress, total);
            }
            else {
                notifyProgressiveListener0(self, (GenericProgressiveFutureListener)listeners, progress, total);
            }
        }
        else if (listeners instanceof GenericProgressiveFutureListener[]) {
            final GenericProgressiveFutureListener<?>[] array = (GenericProgressiveFutureListener<?>[])listeners;
            execute(executor, new Runnable() {
                @Override
                public void run() {
                    notifyProgressiveListeners0(self, array, progress, total);
                }
            });
        }
        else {
            final GenericProgressiveFutureListener<ProgressiveFuture<V>> l = (GenericProgressiveFutureListener<ProgressiveFuture<V>>)listeners;
            execute(executor, new Runnable() {
                @Override
                public void run() {
                    notifyProgressiveListener0(self, l, progress, total);
                }
            });
        }
    }
    
    private static void notifyProgressiveListeners0(final ProgressiveFuture<?> future, final GenericProgressiveFutureListener<?>[] listeners, final long progress, final long total) {
        for (final GenericProgressiveFutureListener<?> l : listeners) {
            if (l == null) {
                break;
            }
            notifyProgressiveListener0(future, l, progress, total);
        }
    }
    
    private static void notifyProgressiveListener0(final ProgressiveFuture future, final GenericProgressiveFutureListener l, final long progress, final long total) {
        try {
            l.operationProgressed(future, progress, total);
        }
        catch (Throwable t) {
            if (DefaultPromise.logger.isWarnEnabled()) {
                DefaultPromise.logger.warn("An exception was thrown by " + l.getClass().getName() + ".operationProgressed()", t);
            }
        }
    }
    
    @Override
    public String toString() {
        return this.toStringBuilder().toString();
    }
    
    protected StringBuilder toStringBuilder() {
        final StringBuilder buf = new StringBuilder(64);
        buf.append(StringUtil.simpleClassName(this));
        buf.append('@');
        buf.append(Integer.toHexString(this.hashCode()));
        final Object result = this.result;
        if (result == DefaultPromise.SUCCESS) {
            buf.append("(success)");
        }
        else if (result == DefaultPromise.UNCANCELLABLE) {
            buf.append("(uncancellable)");
        }
        else if (result instanceof CauseHolder) {
            buf.append("(failure(");
            buf.append(((CauseHolder)result).cause);
            buf.append(')');
        }
        else {
            buf.append("(incomplete)");
        }
        return buf;
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(DefaultPromise.class);
        rejectedExecutionLogger = InternalLoggerFactory.getInstance(DefaultPromise.class.getName() + ".rejectedExecution");
        SUCCESS = Signal.valueOf(DefaultPromise.class.getName() + ".SUCCESS");
        UNCANCELLABLE = Signal.valueOf(DefaultPromise.class.getName() + ".UNCANCELLABLE");
        CANCELLATION_CAUSE_HOLDER = new CauseHolder(new CancellationException());
        DefaultPromise.CANCELLATION_CAUSE_HOLDER.cause.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
    }
    
    private static final class CauseHolder
    {
        final Throwable cause;
        
        CauseHolder(final Throwable cause) {
            this.cause = cause;
        }
    }
    
    private final class LateListeners extends ArrayDeque<GenericFutureListener<?>> implements Runnable
    {
        private static final long serialVersionUID = -687137418080392244L;
        
        LateListeners() {
            super(2);
        }
        
        @Override
        public void run() {
            if (DefaultPromise.this.listeners == null) {
                while (true) {
                    final GenericFutureListener<?> l = this.poll();
                    if (l == null) {
                        break;
                    }
                    DefaultPromise.notifyListener0(DefaultPromise.this, l);
                }
            }
            else {
                execute(DefaultPromise.this.executor(), this);
            }
        }
    }
    
    private final class LateListenerNotifier implements Runnable
    {
        private GenericFutureListener<?> l;
        
        LateListenerNotifier(final GenericFutureListener<?> l) {
            this.l = l;
        }
        
        @Override
        public void run() {
            LateListeners lateListeners = DefaultPromise.this.lateListeners;
            if (this.l != null) {
                if (lateListeners == null) {
                    DefaultPromise.this.lateListeners = (lateListeners = new LateListeners());
                }
                lateListeners.add(this.l);
                this.l = null;
            }
            lateListeners.run();
        }
    }
}
