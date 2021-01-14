package io.netty.util.concurrent;

import io.netty.util.internal.InternalThreadLocalMap;

public class FastThreadLocalThread
        extends Thread {
    private InternalThreadLocalMap threadLocalMap;

    public FastThreadLocalThread() {
    }

    public FastThreadLocalThread(Runnable paramRunnable) {
        super(paramRunnable);
    }

    public FastThreadLocalThread(ThreadGroup paramThreadGroup, Runnable paramRunnable) {
        super(paramThreadGroup, paramRunnable);
    }

    public FastThreadLocalThread(String paramString) {
        super(paramString);
    }

    public FastThreadLocalThread(ThreadGroup paramThreadGroup, String paramString) {
        super(paramThreadGroup, paramString);
    }

    public FastThreadLocalThread(Runnable paramRunnable, String paramString) {
        super(paramRunnable, paramString);
    }

    public FastThreadLocalThread(ThreadGroup paramThreadGroup, Runnable paramRunnable, String paramString) {
        super(paramThreadGroup, paramRunnable, paramString);
    }

    public FastThreadLocalThread(ThreadGroup paramThreadGroup, Runnable paramRunnable, String paramString, long paramLong) {
        super(paramThreadGroup, paramRunnable, paramString, paramLong);
    }

    public final InternalThreadLocalMap threadLocalMap() {
        return this.threadLocalMap;
    }

    public final void setThreadLocalMap(InternalThreadLocalMap paramInternalThreadLocalMap) {
        this.threadLocalMap = paramInternalThreadLocalMap;
    }
}




