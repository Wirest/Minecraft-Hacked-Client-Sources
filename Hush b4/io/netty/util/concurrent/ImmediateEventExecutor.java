// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.concurrent;

import java.util.concurrent.TimeUnit;

public final class ImmediateEventExecutor extends AbstractEventExecutor
{
    public static final ImmediateEventExecutor INSTANCE;
    private final Future<?> terminationFuture;
    
    private ImmediateEventExecutor() {
        this.terminationFuture = new FailedFuture<Object>(GlobalEventExecutor.INSTANCE, new UnsupportedOperationException());
    }
    
    @Override
    public EventExecutorGroup parent() {
        return null;
    }
    
    @Override
    public boolean inEventLoop() {
        return true;
    }
    
    @Override
    public boolean inEventLoop(final Thread thread) {
        return true;
    }
    
    @Override
    public Future<?> shutdownGracefully(final long quietPeriod, final long timeout, final TimeUnit unit) {
        return this.terminationFuture();
    }
    
    @Override
    public Future<?> terminationFuture() {
        return this.terminationFuture;
    }
    
    @Deprecated
    @Override
    public void shutdown() {
    }
    
    @Override
    public boolean isShuttingDown() {
        return false;
    }
    
    @Override
    public boolean isShutdown() {
        return false;
    }
    
    @Override
    public boolean isTerminated() {
        return false;
    }
    
    @Override
    public boolean awaitTermination(final long timeout, final TimeUnit unit) {
        return false;
    }
    
    @Override
    public void execute(final Runnable command) {
        if (command == null) {
            throw new NullPointerException("command");
        }
        command.run();
    }
    
    @Override
    public <V> Promise<V> newPromise() {
        return new ImmediatePromise<V>(this);
    }
    
    @Override
    public <V> ProgressivePromise<V> newProgressivePromise() {
        return new ImmediateProgressivePromise<V>(this);
    }
    
    static {
        INSTANCE = new ImmediateEventExecutor();
    }
    
    static class ImmediatePromise<V> extends DefaultPromise<V>
    {
        ImmediatePromise(final EventExecutor executor) {
            super(executor);
        }
        
        @Override
        protected void checkDeadLock() {
        }
    }
    
    static class ImmediateProgressivePromise<V> extends DefaultProgressivePromise<V>
    {
        ImmediateProgressivePromise(final EventExecutor executor) {
            super(executor);
        }
        
        @Override
        protected void checkDeadLock() {
        }
    }
}
