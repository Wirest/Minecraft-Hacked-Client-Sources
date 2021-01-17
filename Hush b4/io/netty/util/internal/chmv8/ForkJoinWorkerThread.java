// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal.chmv8;

public class ForkJoinWorkerThread extends Thread
{
    final ForkJoinPool pool;
    final ForkJoinPool.WorkQueue workQueue;
    
    protected ForkJoinWorkerThread(final ForkJoinPool pool) {
        super("aForkJoinWorkerThread");
        this.pool = pool;
        this.workQueue = pool.registerWorker(this);
    }
    
    public ForkJoinPool getPool() {
        return this.pool;
    }
    
    public int getPoolIndex() {
        return this.workQueue.poolIndex >>> 1;
    }
    
    protected void onStart() {
    }
    
    protected void onTermination(final Throwable exception) {
    }
    
    @Override
    public void run() {
        Throwable exception = null;
        try {
            this.onStart();
            this.pool.runWorker(this.workQueue);
        }
        catch (Throwable ex) {
            exception = ex;
            try {
                this.onTermination(exception);
            }
            catch (Throwable ex) {
                if (exception == null) {
                    exception = ex;
                }
            }
            finally {
                this.pool.deregisterWorker(this, exception);
            }
        }
        finally {
            try {
                this.onTermination(exception);
            }
            catch (Throwable ex2) {
                if (exception == null) {
                    exception = ex2;
                }
                this.pool.deregisterWorker(this, exception);
            }
            finally {
                this.pool.deregisterWorker(this, exception);
            }
        }
    }
}
