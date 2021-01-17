// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import javax.annotation.concurrent.GuardedBy;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import java.util.logging.Level;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Future;
import java.util.logging.Logger;
import com.google.common.annotations.Beta;

@Beta
public abstract class AbstractScheduledService implements Service
{
    private static final Logger logger;
    private final AbstractService delegate;
    
    protected AbstractScheduledService() {
        this.delegate = new AbstractService() {
            private volatile Future<?> runningTask;
            private volatile ScheduledExecutorService executorService;
            private final ReentrantLock lock = new ReentrantLock();
            private final Runnable task = new Runnable() {
                @Override
                public void run() {
                    AbstractService.this.lock.lock();
                    try {
                        AbstractScheduledService.this.runOneIteration();
                    }
                    catch (Throwable t) {
                        try {
                            AbstractScheduledService.this.shutDown();
                        }
                        catch (Exception ignored) {
                            AbstractScheduledService.logger.log(Level.WARNING, "Error while attempting to shut down the service after failure.", ignored);
                        }
                        AbstractService.this.notifyFailed(t);
                        throw Throwables.propagate(t);
                    }
                    finally {
                        AbstractService.this.lock.unlock();
                    }
                }
            };
            
            @Override
            protected final void doStart() {
                (this.executorService = MoreExecutors.renamingDecorator(AbstractScheduledService.this.executor(), new Supplier<String>() {
                    @Override
                    public String get() {
                        return AbstractScheduledService.this.serviceName() + " " + AbstractService.this.state();
                    }
                })).execute(new Runnable() {
                    @Override
                    public void run() {
                        AbstractService.this.lock.lock();
                        try {
                            AbstractScheduledService.this.startUp();
                            AbstractService.this.runningTask = AbstractScheduledService.this.scheduler().schedule(AbstractScheduledService.this.delegate, AbstractService.this.executorService, AbstractService.this.task);
                            AbstractService.this.notifyStarted();
                        }
                        catch (Throwable t) {
                            AbstractService.this.notifyFailed(t);
                            throw Throwables.propagate(t);
                        }
                        finally {
                            AbstractService.this.lock.unlock();
                        }
                    }
                });
            }
            
            @Override
            protected final void doStop() {
                this.runningTask.cancel(false);
                this.executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            AbstractService.this.lock.lock();
                            try {
                                if (AbstractService.this.state() != State.STOPPING) {
                                    return;
                                }
                                AbstractScheduledService.this.shutDown();
                            }
                            finally {
                                AbstractService.this.lock.unlock();
                            }
                            AbstractService.this.notifyStopped();
                        }
                        catch (Throwable t) {
                            AbstractService.this.notifyFailed(t);
                            throw Throwables.propagate(t);
                        }
                    }
                });
            }
        };
    }
    
    protected abstract void runOneIteration() throws Exception;
    
    protected void startUp() throws Exception {
    }
    
    protected void shutDown() throws Exception {
    }
    
    protected abstract Scheduler scheduler();
    
    protected ScheduledExecutorService executor() {
        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(final Runnable runnable) {
                return MoreExecutors.newThread(AbstractScheduledService.this.serviceName(), runnable);
            }
        });
        this.addListener(new Listener() {
            @Override
            public void terminated(final State from) {
                executor.shutdown();
            }
            
            @Override
            public void failed(final State from, final Throwable failure) {
                executor.shutdown();
            }
        }, MoreExecutors.sameThreadExecutor());
        return executor;
    }
    
    protected String serviceName() {
        return this.getClass().getSimpleName();
    }
    
    @Override
    public String toString() {
        return this.serviceName() + " [" + this.state() + "]";
    }
    
    @Override
    public final boolean isRunning() {
        return this.delegate.isRunning();
    }
    
    @Override
    public final State state() {
        return this.delegate.state();
    }
    
    @Override
    public final void addListener(final Listener listener, final Executor executor) {
        this.delegate.addListener(listener, executor);
    }
    
    @Override
    public final Throwable failureCause() {
        return this.delegate.failureCause();
    }
    
    @Override
    public final Service startAsync() {
        this.delegate.startAsync();
        return this;
    }
    
    @Override
    public final Service stopAsync() {
        this.delegate.stopAsync();
        return this;
    }
    
    @Override
    public final void awaitRunning() {
        this.delegate.awaitRunning();
    }
    
    @Override
    public final void awaitRunning(final long timeout, final TimeUnit unit) throws TimeoutException {
        this.delegate.awaitRunning(timeout, unit);
    }
    
    @Override
    public final void awaitTerminated() {
        this.delegate.awaitTerminated();
    }
    
    @Override
    public final void awaitTerminated(final long timeout, final TimeUnit unit) throws TimeoutException {
        this.delegate.awaitTerminated(timeout, unit);
    }
    
    static {
        logger = Logger.getLogger(AbstractScheduledService.class.getName());
    }
    
    public abstract static class Scheduler
    {
        public static Scheduler newFixedDelaySchedule(final long initialDelay, final long delay, final TimeUnit unit) {
            return new Scheduler() {
                public Future<?> schedule(final AbstractService service, final ScheduledExecutorService executor, final Runnable task) {
                    return executor.scheduleWithFixedDelay(task, initialDelay, delay, unit);
                }
            };
        }
        
        public static Scheduler newFixedRateSchedule(final long initialDelay, final long period, final TimeUnit unit) {
            return new Scheduler() {
                public Future<?> schedule(final AbstractService service, final ScheduledExecutorService executor, final Runnable task) {
                    return executor.scheduleAtFixedRate(task, initialDelay, period, unit);
                }
            };
        }
        
        abstract Future<?> schedule(final AbstractService p0, final ScheduledExecutorService p1, final Runnable p2);
        
        private Scheduler() {
        }
    }
    
    @Beta
    public abstract static class CustomScheduler extends Scheduler
    {
        @Override
        final Future<?> schedule(final AbstractService service, final ScheduledExecutorService executor, final Runnable runnable) {
            final ReschedulableCallable task = new ReschedulableCallable(service, executor, runnable);
            task.reschedule();
            return task;
        }
        
        protected abstract Schedule getNextSchedule() throws Exception;
        
        private class ReschedulableCallable extends ForwardingFuture<Void> implements Callable<Void>
        {
            private final Runnable wrappedRunnable;
            private final ScheduledExecutorService executor;
            private final AbstractService service;
            private final ReentrantLock lock;
            @GuardedBy("lock")
            private Future<Void> currentFuture;
            
            ReschedulableCallable(final AbstractService service, final ScheduledExecutorService executor, final Runnable runnable) {
                this.lock = new ReentrantLock();
                this.wrappedRunnable = runnable;
                this.executor = executor;
                this.service = service;
            }
            
            @Override
            public Void call() throws Exception {
                this.wrappedRunnable.run();
                this.reschedule();
                return null;
            }
            
            public void reschedule() {
                this.lock.lock();
                try {
                    if (this.currentFuture == null || !this.currentFuture.isCancelled()) {
                        final Schedule schedule = CustomScheduler.this.getNextSchedule();
                        this.currentFuture = (Future<Void>)this.executor.schedule((Callable<Object>)this, schedule.delay, schedule.unit);
                    }
                }
                catch (Throwable e) {
                    this.service.notifyFailed(e);
                }
                finally {
                    this.lock.unlock();
                }
            }
            
            @Override
            public boolean cancel(final boolean mayInterruptIfRunning) {
                this.lock.lock();
                try {
                    return this.currentFuture.cancel(mayInterruptIfRunning);
                }
                finally {
                    this.lock.unlock();
                }
            }
            
            @Override
            protected Future<Void> delegate() {
                throw new UnsupportedOperationException("Only cancel is supported by this future");
            }
        }
        
        @Beta
        protected static final class Schedule
        {
            private final long delay;
            private final TimeUnit unit;
            
            public Schedule(final long delay, final TimeUnit unit) {
                this.delay = delay;
                this.unit = Preconditions.checkNotNull(unit);
            }
        }
    }
}
