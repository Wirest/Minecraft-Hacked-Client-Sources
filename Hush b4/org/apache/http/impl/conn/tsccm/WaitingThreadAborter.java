// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn.tsccm;

@Deprecated
public class WaitingThreadAborter
{
    private WaitingThread waitingThread;
    private boolean aborted;
    
    public void abort() {
        this.aborted = true;
        if (this.waitingThread != null) {
            this.waitingThread.interrupt();
        }
    }
    
    public void setWaitingThread(final WaitingThread waitingThread) {
        this.waitingThread = waitingThread;
        if (this.aborted) {
            waitingThread.interrupt();
        }
    }
}
