// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io;

class ThreadMonitor implements Runnable
{
    private final Thread thread;
    private final long timeout;
    
    public static Thread start(final long timeout) {
        return start(Thread.currentThread(), timeout);
    }
    
    public static Thread start(final Thread thread, final long timeout) {
        Thread monitor = null;
        if (timeout > 0L) {
            final ThreadMonitor timout = new ThreadMonitor(thread, timeout);
            monitor = new Thread(timout, ThreadMonitor.class.getSimpleName());
            monitor.setDaemon(true);
            monitor.start();
        }
        return monitor;
    }
    
    public static void stop(final Thread thread) {
        if (thread != null) {
            thread.interrupt();
        }
    }
    
    private ThreadMonitor(final Thread thread, final long timeout) {
        this.thread = thread;
        this.timeout = timeout;
    }
    
    @Override
    public void run() {
        try {
            Thread.sleep(this.timeout);
            this.thread.interrupt();
        }
        catch (InterruptedException ex) {}
    }
}
