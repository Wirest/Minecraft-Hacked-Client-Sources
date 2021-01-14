package com.google.common.util.concurrent;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ExecutionList.RunnableExecutorPair;

import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ExecutionList {
    @VisibleForTesting
    static final Logger log = Logger.getLogger(ExecutionList.class.getName());
    @GuardedBy("this")
    private RunnableExecutorPair runnables;
    @GuardedBy("this")
    private boolean executed;

    private static void executeListener(Runnable paramRunnable, Executor paramExecutor) {
        try {
            paramExecutor.execute(paramRunnable);
        } catch (RuntimeException localRuntimeException) {
            log.log(Level.SEVERE, "RuntimeException while executing runnable " + paramRunnable + " with executor " + paramExecutor, localRuntimeException);
        }
    }

    public void add(Runnable paramRunnable, Executor paramExecutor) {
        Preconditions.checkNotNull(paramRunnable, "Runnable was null.");
        Preconditions.checkNotNull(paramExecutor, "Executor was null.");
        synchronized (this) {
            if (!this.executed) {
                this.runnables = new RunnableExecutorPair(paramRunnable, paramExecutor, this.runnables);
                return;
            }
        }
        executeListener(paramRunnable, paramExecutor);
    }

    public void execute() {
        RunnableExecutorPair localRunnableExecutorPair1;
        synchronized (this) {
            if (this.executed) {
                return;
            }
            this.executed = true;
            localRunnableExecutorPair1 = this.runnables;
            this.runnables = null;
        }
        RunnableExecutorPair localRunnableExecutorPair2;
        for (??? =null;
        localRunnableExecutorPair1 != null; ??? =localRunnableExecutorPair2)
        {
            localRunnableExecutorPair2 = localRunnableExecutorPair1;
            localRunnableExecutorPair1 = localRunnableExecutorPair1.next;
            localRunnableExecutorPair2.next = ((RunnableExecutorPair) ? ??);
        }
        while (??? !=null)
        {
            executeListener(((RunnableExecutorPair) ? ? ?).runnable, ((RunnableExecutorPair) ? ??).executor);
      ??? =((RunnableExecutorPair) ? ??).next;
        }
    }

    private static final class RunnableExecutorPair {
        final Runnable runnable;
        final Executor executor;
        @Nullable
        RunnableExecutorPair next;

        RunnableExecutorPair(Runnable paramRunnable, Executor paramExecutor, RunnableExecutorPair paramRunnableExecutorPair) {
            this.runnable = paramRunnable;
            this.executor = paramExecutor;
            this.next = paramRunnableExecutorPair;
        }
    }
}




