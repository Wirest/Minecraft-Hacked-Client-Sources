// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executor;
import com.google.common.base.Throwables;
import java.util.logging.Level;
import com.google.common.base.Supplier;
import java.util.logging.Logger;
import com.google.common.annotations.Beta;

@Beta
public abstract class AbstractExecutionThreadService implements Service
{
    private static final Logger logger;
    private final Service delegate;
    
    protected AbstractExecutionThreadService() {
        this.delegate = new AbstractService() {
            @Override
            protected final void doStart() {
                final Executor executor = MoreExecutors.renamingDecorator(AbstractExecutionThreadService.this.executor(), new Supplier<String>() {
                    @Override
                    public String get() {
                        return AbstractExecutionThreadService.this.serviceName();
                    }
                });
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            AbstractExecutionThreadService.this.startUp();
                            AbstractService.this.notifyStarted();
                            if (AbstractService.this.isRunning()) {
                                try {
                                    AbstractExecutionThreadService.this.run();
                                }
                                catch (Throwable t) {
                                    try {
                                        AbstractExecutionThreadService.this.shutDown();
                                    }
                                    catch (Exception ignored) {
                                        AbstractExecutionThreadService.logger.log(Level.WARNING, "Error while attempting to shut down the service after failure.", ignored);
                                    }
                                    throw t;
                                }
                            }
                            AbstractExecutionThreadService.this.shutDown();
                            AbstractService.this.notifyStopped();
                        }
                        catch (Throwable t) {
                            AbstractService.this.notifyFailed(t);
                            throw Throwables.propagate(t);
                        }
                    }
                });
            }
            
            @Override
            protected void doStop() {
                AbstractExecutionThreadService.this.triggerShutdown();
            }
        };
    }
    
    protected void startUp() throws Exception {
    }
    
    protected abstract void run() throws Exception;
    
    protected void shutDown() throws Exception {
    }
    
    protected void triggerShutdown() {
    }
    
    protected Executor executor() {
        return new Executor() {
            @Override
            public void execute(final Runnable command) {
                MoreExecutors.newThread(AbstractExecutionThreadService.this.serviceName(), command).start();
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
    
    static {
        logger = Logger.getLogger(AbstractExecutionThreadService.class.getName());
    }
}
