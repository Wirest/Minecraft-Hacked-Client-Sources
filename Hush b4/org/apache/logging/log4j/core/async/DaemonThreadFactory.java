// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.async;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ThreadFactory;

public class DaemonThreadFactory implements ThreadFactory
{
    final ThreadGroup group;
    final AtomicInteger threadNumber;
    final String threadNamePrefix;
    
    public DaemonThreadFactory(final String threadNamePrefix) {
        this.threadNumber = new AtomicInteger(1);
        this.threadNamePrefix = threadNamePrefix;
        final SecurityManager securityManager = System.getSecurityManager();
        this.group = ((securityManager != null) ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup());
    }
    
    @Override
    public Thread newThread(final Runnable runnable) {
        final Thread thread = new Thread(this.group, runnable, this.threadNamePrefix + this.threadNumber.getAndIncrement(), 0L);
        if (!thread.isDaemon()) {
            thread.setDaemon(true);
        }
        if (thread.getPriority() != 5) {
            thread.setPriority(5);
        }
        return thread;
    }
}
