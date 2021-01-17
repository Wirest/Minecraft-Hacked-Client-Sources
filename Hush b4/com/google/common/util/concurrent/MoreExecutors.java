// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.Delayed;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.RejectedExecutionException;
import java.util.Collections;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Supplier;
import java.lang.reflect.InvocationTargetException;
import com.google.common.base.Throwables;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.Executor;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import com.google.common.collect.Queues;
import com.google.common.collect.Lists;
import com.google.common.base.Preconditions;
import java.util.concurrent.Callable;
import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import com.google.common.annotations.Beta;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor;

public final class MoreExecutors
{
    private MoreExecutors() {
    }
    
    @Beta
    public static ExecutorService getExitingExecutorService(final ThreadPoolExecutor executor, final long terminationTimeout, final TimeUnit timeUnit) {
        return new Application().getExitingExecutorService(executor, terminationTimeout, timeUnit);
    }
    
    @Beta
    public static ScheduledExecutorService getExitingScheduledExecutorService(final ScheduledThreadPoolExecutor executor, final long terminationTimeout, final TimeUnit timeUnit) {
        return new Application().getExitingScheduledExecutorService(executor, terminationTimeout, timeUnit);
    }
    
    @Beta
    public static void addDelayedShutdownHook(final ExecutorService service, final long terminationTimeout, final TimeUnit timeUnit) {
        new Application().addDelayedShutdownHook(service, terminationTimeout, timeUnit);
    }
    
    @Beta
    public static ExecutorService getExitingExecutorService(final ThreadPoolExecutor executor) {
        return new Application().getExitingExecutorService(executor);
    }
    
    @Beta
    public static ScheduledExecutorService getExitingScheduledExecutorService(final ScheduledThreadPoolExecutor executor) {
        return new Application().getExitingScheduledExecutorService(executor);
    }
    
    private static void useDaemonThreadFactory(final ThreadPoolExecutor executor) {
        executor.setThreadFactory(new ThreadFactoryBuilder().setDaemon(true).setThreadFactory(executor.getThreadFactory()).build());
    }
    
    public static ListeningExecutorService sameThreadExecutor() {
        return new SameThreadExecutorService();
    }
    
    public static ListeningExecutorService listeningDecorator(final ExecutorService delegate) {
        return (delegate instanceof ListeningExecutorService) ? ((ListeningExecutorService)delegate) : ((delegate instanceof ScheduledExecutorService) ? new ScheduledListeningDecorator((ScheduledExecutorService)delegate) : new ListeningDecorator(delegate));
    }
    
    public static ListeningScheduledExecutorService listeningDecorator(final ScheduledExecutorService delegate) {
        return (delegate instanceof ListeningScheduledExecutorService) ? ((ListeningScheduledExecutorService)delegate) : new ScheduledListeningDecorator(delegate);
    }
    
    static <T> T invokeAnyImpl(final ListeningExecutorService executorService, final Collection<? extends Callable<T>> tasks, final boolean timed, long nanos) throws InterruptedException, ExecutionException, TimeoutException {
        Preconditions.checkNotNull(executorService);
        int ntasks = tasks.size();
        Preconditions.checkArgument(ntasks > 0);
        final List<Future<T>> futures = (List<Future<T>>)Lists.newArrayListWithCapacity(ntasks);
        final BlockingQueue<Future<T>> futureQueue = (BlockingQueue<Future<T>>)Queues.newLinkedBlockingQueue();
        try {
            ExecutionException ee = null;
            long lastTime = timed ? System.nanoTime() : 0L;
            final Iterator<? extends Callable<T>> it = tasks.iterator();
            futures.add(submitAndAddQueueListener(executorService, (Callable<T>)it.next(), futureQueue));
            --ntasks;
            int active = 1;
            while (true) {
                Future<T> f = futureQueue.poll();
                if (f == null) {
                    if (ntasks > 0) {
                        --ntasks;
                        futures.add(submitAndAddQueueListener(executorService, (Callable<T>)it.next(), futureQueue));
                        ++active;
                    }
                    else {
                        if (active == 0) {
                            if (ee == null) {
                                ee = new ExecutionException((Throwable)null);
                            }
                            throw ee;
                        }
                        if (timed) {
                            f = futureQueue.poll(nanos, TimeUnit.NANOSECONDS);
                            if (f == null) {
                                throw new TimeoutException();
                            }
                            final long now = System.nanoTime();
                            nanos -= now - lastTime;
                            lastTime = now;
                        }
                        else {
                            f = futureQueue.take();
                        }
                    }
                }
                if (f != null) {
                    --active;
                    try {
                        return f.get();
                    }
                    catch (ExecutionException eex) {
                        ee = eex;
                    }
                    catch (RuntimeException rex) {
                        ee = new ExecutionException(rex);
                    }
                }
            }
        }
        finally {
            for (final Future<T> f2 : futures) {
                f2.cancel(true);
            }
        }
    }
    
    private static <T> ListenableFuture<T> submitAndAddQueueListener(final ListeningExecutorService executorService, final Callable<T> task, final BlockingQueue<Future<T>> queue) {
        final ListenableFuture<T> future = executorService.submit(task);
        future.addListener(new Runnable() {
            @Override
            public void run() {
                queue.add(future);
            }
        }, sameThreadExecutor());
        return future;
    }
    
    @Beta
    public static ThreadFactory platformThreadFactory() {
        if (!isAppEngine()) {
            return Executors.defaultThreadFactory();
        }
        try {
            return (ThreadFactory)Class.forName("com.google.appengine.api.ThreadManager").getMethod("currentRequestThreadFactory", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e);
        }
        catch (ClassNotFoundException e2) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e2);
        }
        catch (NoSuchMethodException e3) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e3);
        }
        catch (InvocationTargetException e4) {
            throw Throwables.propagate(e4.getCause());
        }
    }
    
    private static boolean isAppEngine() {
        if (System.getProperty("com.google.appengine.runtime.environment") == null) {
            return false;
        }
        try {
            return Class.forName("com.google.apphosting.api.ApiProxy").getMethod("getCurrentEnvironment", (Class<?>[])new Class[0]).invoke(null, new Object[0]) != null;
        }
        catch (ClassNotFoundException e) {
            return false;
        }
        catch (InvocationTargetException e2) {
            return false;
        }
        catch (IllegalAccessException e3) {
            return false;
        }
        catch (NoSuchMethodException e4) {
            return false;
        }
    }
    
    static Thread newThread(final String name, final Runnable runnable) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(runnable);
        final Thread result = platformThreadFactory().newThread(runnable);
        try {
            result.setName(name);
        }
        catch (SecurityException ex) {}
        return result;
    }
    
    static Executor renamingDecorator(final Executor executor, final Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(executor);
        Preconditions.checkNotNull(nameSupplier);
        if (isAppEngine()) {
            return executor;
        }
        return new Executor() {
            @Override
            public void execute(final Runnable command) {
                executor.execute(Callables.threadRenaming(command, nameSupplier));
            }
        };
    }
    
    static ExecutorService renamingDecorator(final ExecutorService service, final Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(service);
        Preconditions.checkNotNull(nameSupplier);
        if (isAppEngine()) {
            return service;
        }
        return new WrappingExecutorService(service) {
            @Override
            protected <T> Callable<T> wrapTask(final Callable<T> callable) {
                return Callables.threadRenaming(callable, nameSupplier);
            }
            
            @Override
            protected Runnable wrapTask(final Runnable command) {
                return Callables.threadRenaming(command, nameSupplier);
            }
        };
    }
    
    static ScheduledExecutorService renamingDecorator(final ScheduledExecutorService service, final Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(service);
        Preconditions.checkNotNull(nameSupplier);
        if (isAppEngine()) {
            return service;
        }
        return new WrappingScheduledExecutorService(service) {
            @Override
            protected <T> Callable<T> wrapTask(final Callable<T> callable) {
                return Callables.threadRenaming(callable, nameSupplier);
            }
            
            @Override
            protected Runnable wrapTask(final Runnable command) {
                return Callables.threadRenaming(command, nameSupplier);
            }
        };
    }
    
    @Beta
    public static boolean shutdownAndAwaitTermination(final ExecutorService service, final long timeout, final TimeUnit unit) {
        Preconditions.checkNotNull(unit);
        service.shutdown();
        try {
            final long halfTimeoutNanos = TimeUnit.NANOSECONDS.convert(timeout, unit) / 2L;
            if (!service.awaitTermination(halfTimeoutNanos, TimeUnit.NANOSECONDS)) {
                service.shutdownNow();
                service.awaitTermination(halfTimeoutNanos, TimeUnit.NANOSECONDS);
            }
        }
        catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            service.shutdownNow();
        }
        return service.isTerminated();
    }
    
    @VisibleForTesting
    static class Application
    {
        final ExecutorService getExitingExecutorService(final ThreadPoolExecutor executor, final long terminationTimeout, final TimeUnit timeUnit) {
            useDaemonThreadFactory(executor);
            final ExecutorService service = Executors.unconfigurableExecutorService(executor);
            this.addDelayedShutdownHook(service, terminationTimeout, timeUnit);
            return service;
        }
        
        final ScheduledExecutorService getExitingScheduledExecutorService(final ScheduledThreadPoolExecutor executor, final long terminationTimeout, final TimeUnit timeUnit) {
            useDaemonThreadFactory(executor);
            final ScheduledExecutorService service = Executors.unconfigurableScheduledExecutorService(executor);
            this.addDelayedShutdownHook(service, terminationTimeout, timeUnit);
            return service;
        }
        
        final void addDelayedShutdownHook(final ExecutorService service, final long terminationTimeout, final TimeUnit timeUnit) {
            Preconditions.checkNotNull(service);
            Preconditions.checkNotNull(timeUnit);
            this.addShutdownHook(MoreExecutors.newThread("DelayedShutdownHook-for-" + service, new Runnable() {
                @Override
                public void run() {
                    try {
                        service.shutdown();
                        service.awaitTermination(terminationTimeout, timeUnit);
                    }
                    catch (InterruptedException ex) {}
                }
            }));
        }
        
        final ExecutorService getExitingExecutorService(final ThreadPoolExecutor executor) {
            return this.getExitingExecutorService(executor, 120L, TimeUnit.SECONDS);
        }
        
        final ScheduledExecutorService getExitingScheduledExecutorService(final ScheduledThreadPoolExecutor executor) {
            return this.getExitingScheduledExecutorService(executor, 120L, TimeUnit.SECONDS);
        }
        
        @VisibleForTesting
        void addShutdownHook(final Thread hook) {
            Runtime.getRuntime().addShutdownHook(hook);
        }
    }
    
    private static class SameThreadExecutorService extends AbstractListeningExecutorService
    {
        private final Lock lock;
        private final Condition termination;
        private int runningTasks;
        private boolean shutdown;
        
        private SameThreadExecutorService() {
            this.lock = new ReentrantLock();
            this.termination = this.lock.newCondition();
            this.runningTasks = 0;
            this.shutdown = false;
        }
        
        @Override
        public void execute(final Runnable command) {
            this.startTask();
            try {
                command.run();
            }
            finally {
                this.endTask();
            }
        }
        
        @Override
        public boolean isShutdown() {
            this.lock.lock();
            try {
                return this.shutdown;
            }
            finally {
                this.lock.unlock();
            }
        }
        
        @Override
        public void shutdown() {
            this.lock.lock();
            try {
                this.shutdown = true;
            }
            finally {
                this.lock.unlock();
            }
        }
        
        @Override
        public List<Runnable> shutdownNow() {
            this.shutdown();
            return Collections.emptyList();
        }
        
        @Override
        public boolean isTerminated() {
            this.lock.lock();
            try {
                return this.shutdown && this.runningTasks == 0;
            }
            finally {
                this.lock.unlock();
            }
        }
        
        @Override
        public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
            long nanos = unit.toNanos(timeout);
            this.lock.lock();
            try {
                while (!this.isTerminated()) {
                    if (nanos <= 0L) {
                        return false;
                    }
                    nanos = this.termination.awaitNanos(nanos);
                }
                return true;
            }
            finally {
                this.lock.unlock();
            }
        }
        
        private void startTask() {
            this.lock.lock();
            try {
                if (this.isShutdown()) {
                    throw new RejectedExecutionException("Executor already shutdown");
                }
                ++this.runningTasks;
            }
            finally {
                this.lock.unlock();
            }
        }
        
        private void endTask() {
            this.lock.lock();
            try {
                --this.runningTasks;
                if (this.isTerminated()) {
                    this.termination.signalAll();
                }
            }
            finally {
                this.lock.unlock();
            }
        }
    }
    
    private static class ListeningDecorator extends AbstractListeningExecutorService
    {
        private final ExecutorService delegate;
        
        ListeningDecorator(final ExecutorService delegate) {
            this.delegate = Preconditions.checkNotNull(delegate);
        }
        
        @Override
        public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
            return this.delegate.awaitTermination(timeout, unit);
        }
        
        @Override
        public boolean isShutdown() {
            return this.delegate.isShutdown();
        }
        
        @Override
        public boolean isTerminated() {
            return this.delegate.isTerminated();
        }
        
        @Override
        public void shutdown() {
            this.delegate.shutdown();
        }
        
        @Override
        public List<Runnable> shutdownNow() {
            return this.delegate.shutdownNow();
        }
        
        @Override
        public void execute(final Runnable command) {
            this.delegate.execute(command);
        }
    }
    
    private static class ScheduledListeningDecorator extends ListeningDecorator implements ListeningScheduledExecutorService
    {
        final ScheduledExecutorService delegate;
        
        ScheduledListeningDecorator(final ScheduledExecutorService delegate) {
            super(delegate);
            this.delegate = Preconditions.checkNotNull(delegate);
        }
        
        @Override
        public ListenableScheduledFuture<?> schedule(final Runnable command, final long delay, final TimeUnit unit) {
            final ListenableFutureTask<Void> task = ListenableFutureTask.create(command, (Void)null);
            final ScheduledFuture<?> scheduled = this.delegate.schedule(task, delay, unit);
            return new ListenableScheduledTask<Object>(task, scheduled);
        }
        
        @Override
        public <V> ListenableScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit unit) {
            final ListenableFutureTask<V> task = ListenableFutureTask.create(callable);
            final ScheduledFuture<?> scheduled = this.delegate.schedule(task, delay, unit);
            return new ListenableScheduledTask<V>(task, scheduled);
        }
        
        @Override
        public ListenableScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit) {
            final NeverSuccessfulListenableFutureTask task = new NeverSuccessfulListenableFutureTask(command);
            final ScheduledFuture<?> scheduled = this.delegate.scheduleAtFixedRate(task, initialDelay, period, unit);
            return new ListenableScheduledTask<Object>(task, scheduled);
        }
        
        @Override
        public ListenableScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit) {
            final NeverSuccessfulListenableFutureTask task = new NeverSuccessfulListenableFutureTask(command);
            final ScheduledFuture<?> scheduled = this.delegate.scheduleWithFixedDelay(task, initialDelay, delay, unit);
            return new ListenableScheduledTask<Object>(task, scheduled);
        }
        
        private static final class ListenableScheduledTask<V> extends SimpleForwardingListenableFuture<V> implements ListenableScheduledFuture<V>
        {
            private final ScheduledFuture<?> scheduledDelegate;
            
            public ListenableScheduledTask(final ListenableFuture<V> listenableDelegate, final ScheduledFuture<?> scheduledDelegate) {
                super(listenableDelegate);
                this.scheduledDelegate = scheduledDelegate;
            }
            
            @Override
            public boolean cancel(final boolean mayInterruptIfRunning) {
                final boolean cancelled = super.cancel(mayInterruptIfRunning);
                if (cancelled) {
                    this.scheduledDelegate.cancel(mayInterruptIfRunning);
                }
                return cancelled;
            }
            
            @Override
            public long getDelay(final TimeUnit unit) {
                return this.scheduledDelegate.getDelay(unit);
            }
            
            @Override
            public int compareTo(final Delayed other) {
                return this.scheduledDelegate.compareTo(other);
            }
        }
        
        private static final class NeverSuccessfulListenableFutureTask extends AbstractFuture<Void> implements Runnable
        {
            private final Runnable delegate;
            
            public NeverSuccessfulListenableFutureTask(final Runnable delegate) {
                this.delegate = Preconditions.checkNotNull(delegate);
            }
            
            @Override
            public void run() {
                try {
                    this.delegate.run();
                }
                catch (Throwable t) {
                    this.setException(t);
                    throw Throwables.propagate(t);
                }
            }
        }
    }
}
