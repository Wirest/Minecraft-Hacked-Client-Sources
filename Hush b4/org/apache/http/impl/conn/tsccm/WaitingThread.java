// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn.tsccm;

import java.util.Date;
import org.apache.http.util.Args;
import java.util.concurrent.locks.Condition;

@Deprecated
public class WaitingThread
{
    private final Condition cond;
    private final RouteSpecificPool pool;
    private Thread waiter;
    private boolean aborted;
    
    public WaitingThread(final Condition cond, final RouteSpecificPool pool) {
        Args.notNull(cond, "Condition");
        this.cond = cond;
        this.pool = pool;
    }
    
    public final Condition getCondition() {
        return this.cond;
    }
    
    public final RouteSpecificPool getPool() {
        return this.pool;
    }
    
    public final Thread getThread() {
        return this.waiter;
    }
    
    public boolean await(final Date deadline) throws InterruptedException {
        if (this.waiter != null) {
            throw new IllegalStateException("A thread is already waiting on this object.\ncaller: " + Thread.currentThread() + "\nwaiter: " + this.waiter);
        }
        if (this.aborted) {
            throw new InterruptedException("Operation interrupted");
        }
        this.waiter = Thread.currentThread();
        boolean success = false;
        try {
            if (deadline != null) {
                success = this.cond.awaitUntil(deadline);
            }
            else {
                this.cond.await();
                success = true;
            }
            if (this.aborted) {
                throw new InterruptedException("Operation interrupted");
            }
        }
        finally {
            this.waiter = null;
        }
        return success;
    }
    
    public void wakeup() {
        if (this.waiter == null) {
            throw new IllegalStateException("Nobody waiting on this object.");
        }
        this.cond.signalAll();
    }
    
    public void interrupt() {
        this.aborted = true;
        this.cond.signalAll();
    }
}
