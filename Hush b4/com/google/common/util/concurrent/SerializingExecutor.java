// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.logging.Level;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import javax.annotation.concurrent.GuardedBy;
import java.util.Queue;
import java.util.logging.Logger;
import java.util.concurrent.Executor;

final class SerializingExecutor implements Executor
{
    private static final Logger log;
    private final Executor executor;
    @GuardedBy("internalLock")
    private final Queue<Runnable> waitQueue;
    @GuardedBy("internalLock")
    private boolean isThreadScheduled;
    private final TaskRunner taskRunner;
    private final Object internalLock;
    
    public SerializingExecutor(final Executor executor) {
        this.waitQueue = new ArrayDeque<Runnable>();
        this.isThreadScheduled = false;
        this.taskRunner = new TaskRunner();
        this.internalLock = new Object() {
            @Override
            public String toString() {
                return "SerializingExecutor lock: " + super.toString();
            }
        };
        Preconditions.checkNotNull(executor, (Object)"'executor' must not be null.");
        this.executor = executor;
    }
    
    @Override
    public void execute(final Runnable r) {
        Preconditions.checkNotNull(r, (Object)"'r' must not be null.");
        boolean scheduleTaskRunner = false;
        synchronized (this.internalLock) {
            this.waitQueue.add(r);
            if (!this.isThreadScheduled) {
                this.isThreadScheduled = true;
                scheduleTaskRunner = true;
            }
        }
        if (scheduleTaskRunner) {
            boolean threw = true;
            try {
                this.executor.execute(this.taskRunner);
                threw = false;
            }
            finally {
                if (threw) {
                    synchronized (this.internalLock) {
                        this.isThreadScheduled = false;
                    }
                }
            }
        }
    }
    
    static {
        log = Logger.getLogger(SerializingExecutor.class.getName());
    }
    
    private class TaskRunner implements Runnable
    {
        @Override
        public void run() {
            boolean stillRunning = true;
            try {
                while (true) {
                    Preconditions.checkState(SerializingExecutor.this.isThreadScheduled);
                    final Runnable nextToRun;
                    synchronized (SerializingExecutor.this.internalLock) {
                        nextToRun = SerializingExecutor.this.waitQueue.poll();
                        if (nextToRun == null) {
                            SerializingExecutor.this.isThreadScheduled = false;
                            stillRunning = false;
                            break;
                        }
                    }
                    try {
                        nextToRun.run();
                    }
                    catch (RuntimeException e) {
                        SerializingExecutor.log.log(Level.SEVERE, "Exception while executing runnable " + nextToRun, e);
                    }
                }
            }
            finally {
                if (stillRunning) {
                    synchronized (SerializingExecutor.this.internalLock) {
                        SerializingExecutor.this.isThreadScheduled = false;
                    }
                }
            }
        }
    }
}
