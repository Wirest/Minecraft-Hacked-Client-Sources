// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import javax.annotation.Nullable;
import java.util.logging.Level;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import javax.annotation.concurrent.GuardedBy;
import com.google.common.annotations.VisibleForTesting;
import java.util.logging.Logger;

public final class ExecutionList
{
    @VisibleForTesting
    static final Logger log;
    @GuardedBy("this")
    private RunnableExecutorPair runnables;
    @GuardedBy("this")
    private boolean executed;
    
    public void add(final Runnable runnable, final Executor executor) {
        Preconditions.checkNotNull(runnable, (Object)"Runnable was null.");
        Preconditions.checkNotNull(executor, (Object)"Executor was null.");
        synchronized (this) {
            if (!this.executed) {
                this.runnables = new RunnableExecutorPair(runnable, executor, this.runnables);
                return;
            }
        }
        executeListener(runnable, executor);
    }
    
    public void execute() {
        RunnableExecutorPair list;
        synchronized (this) {
            if (this.executed) {
                return;
            }
            this.executed = true;
            list = this.runnables;
            this.runnables = null;
        }
        RunnableExecutorPair reversedList;
        RunnableExecutorPair tmp;
        for (reversedList = null; list != null; list = list.next, tmp.next = reversedList, reversedList = tmp) {
            tmp = list;
        }
        while (reversedList != null) {
            executeListener(reversedList.runnable, reversedList.executor);
            reversedList = reversedList.next;
        }
    }
    
    private static void executeListener(final Runnable runnable, final Executor executor) {
        try {
            executor.execute(runnable);
        }
        catch (RuntimeException e) {
            ExecutionList.log.log(Level.SEVERE, "RuntimeException while executing runnable " + runnable + " with executor " + executor, e);
        }
    }
    
    static {
        log = Logger.getLogger(ExecutionList.class.getName());
    }
    
    private static final class RunnableExecutorPair
    {
        final Runnable runnable;
        final Executor executor;
        @Nullable
        RunnableExecutorPair next;
        
        RunnableExecutorPair(final Runnable runnable, final Executor executor, final RunnableExecutorPair next) {
            this.runnable = runnable;
            this.executor = executor;
            this.next = next;
        }
    }
}
