// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executor;
import com.google.common.base.Throwables;
import com.google.common.base.Supplier;
import com.google.common.annotations.Beta;

@Beta
public abstract class AbstractIdleService implements Service
{
    private final Supplier<String> threadNameSupplier;
    private final Service delegate;
    
    protected AbstractIdleService() {
        this.threadNameSupplier = new Supplier<String>() {
            @Override
            public String get() {
                return AbstractIdleService.this.serviceName() + " " + AbstractIdleService.this.state();
            }
        };
        this.delegate = new AbstractService() {
            @Override
            protected final void doStart() {
                MoreExecutors.renamingDecorator(AbstractIdleService.this.executor(), AbstractIdleService.this.threadNameSupplier).execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            AbstractIdleService.this.startUp();
                            AbstractService.this.notifyStarted();
                        }
                        catch (Throwable t) {
                            AbstractService.this.notifyFailed(t);
                            throw Throwables.propagate(t);
                        }
                    }
                });
            }
            
            @Override
            protected final void doStop() {
                MoreExecutors.renamingDecorator(AbstractIdleService.this.executor(), AbstractIdleService.this.threadNameSupplier).execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            AbstractIdleService.this.shutDown();
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
    
    protected abstract void startUp() throws Exception;
    
    protected abstract void shutDown() throws Exception;
    
    protected Executor executor() {
        return new Executor() {
            @Override
            public void execute(final Runnable command) {
                MoreExecutors.newThread(AbstractIdleService.this.threadNameSupplier.get(), command).start();
            }
        };
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
    
    protected String serviceName() {
        return this.getClass().getSimpleName();
    }
}
