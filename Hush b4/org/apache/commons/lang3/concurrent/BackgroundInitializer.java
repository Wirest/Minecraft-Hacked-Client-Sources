// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;

public abstract class BackgroundInitializer<T> implements ConcurrentInitializer<T>
{
    private ExecutorService externalExecutor;
    private ExecutorService executor;
    private Future<T> future;
    
    protected BackgroundInitializer() {
        this(null);
    }
    
    protected BackgroundInitializer(final ExecutorService exec) {
        this.setExternalExecutor(exec);
    }
    
    public final synchronized ExecutorService getExternalExecutor() {
        return this.externalExecutor;
    }
    
    public synchronized boolean isStarted() {
        return this.future != null;
    }
    
    public final synchronized void setExternalExecutor(final ExecutorService externalExecutor) {
        if (this.isStarted()) {
            throw new IllegalStateException("Cannot set ExecutorService after start()!");
        }
        this.externalExecutor = externalExecutor;
    }
    
    public synchronized boolean start() {
        if (!this.isStarted()) {
            this.executor = this.getExternalExecutor();
            ExecutorService tempExec;
            if (this.executor == null) {
                tempExec = (this.executor = this.createExecutor());
            }
            else {
                tempExec = null;
            }
            this.future = this.executor.submit(this.createTask(tempExec));
            return true;
        }
        return false;
    }
    
    @Override
    public T get() throws ConcurrentException {
        try {
            return this.getFuture().get();
        }
        catch (ExecutionException execex) {
            ConcurrentUtils.handleCause(execex);
            return null;
        }
        catch (InterruptedException iex) {
            Thread.currentThread().interrupt();
            throw new ConcurrentException(iex);
        }
    }
    
    public synchronized Future<T> getFuture() {
        if (this.future == null) {
            throw new IllegalStateException("start() must be called first!");
        }
        return this.future;
    }
    
    protected final synchronized ExecutorService getActiveExecutor() {
        return this.executor;
    }
    
    protected int getTaskCount() {
        return 1;
    }
    
    protected abstract T initialize() throws Exception;
    
    private Callable<T> createTask(final ExecutorService execDestroy) {
        return new InitializationTask(execDestroy);
    }
    
    private ExecutorService createExecutor() {
        return Executors.newFixedThreadPool(this.getTaskCount());
    }
    
    private class InitializationTask implements Callable<T>
    {
        private final ExecutorService execFinally;
        
        public InitializationTask(final ExecutorService exec) {
            this.execFinally = exec;
        }
        
        @Override
        public T call() throws Exception {
            try {
                return BackgroundInitializer.this.initialize();
            }
            finally {
                if (this.execFinally != null) {
                    this.execFinally.shutdown();
                }
            }
        }
    }
}
