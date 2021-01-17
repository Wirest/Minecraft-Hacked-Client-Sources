// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.concurrent;

import java.util.Locale;
import io.netty.util.internal.StringUtil;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ThreadFactory;

public class DefaultThreadFactory implements ThreadFactory
{
    private static final AtomicInteger poolId;
    private final AtomicInteger nextId;
    private final String prefix;
    private final boolean daemon;
    private final int priority;
    
    public DefaultThreadFactory(final Class<?> poolType) {
        this(poolType, false, 5);
    }
    
    public DefaultThreadFactory(final String poolName) {
        this(poolName, false, 5);
    }
    
    public DefaultThreadFactory(final Class<?> poolType, final boolean daemon) {
        this(poolType, daemon, 5);
    }
    
    public DefaultThreadFactory(final String poolName, final boolean daemon) {
        this(poolName, daemon, 5);
    }
    
    public DefaultThreadFactory(final Class<?> poolType, final int priority) {
        this(poolType, false, priority);
    }
    
    public DefaultThreadFactory(final String poolName, final int priority) {
        this(poolName, false, priority);
    }
    
    public DefaultThreadFactory(final Class<?> poolType, final boolean daemon, final int priority) {
        this(toPoolName(poolType), daemon, priority);
    }
    
    private static String toPoolName(final Class<?> poolType) {
        if (poolType == null) {
            throw new NullPointerException("poolType");
        }
        final String poolName = StringUtil.simpleClassName(poolType);
        switch (poolName.length()) {
            case 0: {
                return "unknown";
            }
            case 1: {
                return poolName.toLowerCase(Locale.US);
            }
            default: {
                if (Character.isUpperCase(poolName.charAt(0)) && Character.isLowerCase(poolName.charAt(1))) {
                    return Character.toLowerCase(poolName.charAt(0)) + poolName.substring(1);
                }
                return poolName;
            }
        }
    }
    
    public DefaultThreadFactory(final String poolName, final boolean daemon, final int priority) {
        this.nextId = new AtomicInteger();
        if (poolName == null) {
            throw new NullPointerException("poolName");
        }
        if (priority < 1 || priority > 10) {
            throw new IllegalArgumentException("priority: " + priority + " (expected: Thread.MIN_PRIORITY <= priority <= Thread.MAX_PRIORITY)");
        }
        this.prefix = poolName + '-' + DefaultThreadFactory.poolId.incrementAndGet() + '-';
        this.daemon = daemon;
        this.priority = priority;
    }
    
    @Override
    public Thread newThread(final Runnable r) {
        final Thread t = this.newThread(new DefaultRunnableDecorator(r), this.prefix + this.nextId.incrementAndGet());
        try {
            if (t.isDaemon()) {
                if (!this.daemon) {
                    t.setDaemon(false);
                }
            }
            else if (this.daemon) {
                t.setDaemon(true);
            }
            if (t.getPriority() != this.priority) {
                t.setPriority(this.priority);
            }
        }
        catch (Exception ex) {}
        return t;
    }
    
    protected Thread newThread(final Runnable r, final String name) {
        return new FastThreadLocalThread(r, name);
    }
    
    static {
        poolId = new AtomicInteger();
    }
    
    private static final class DefaultRunnableDecorator implements Runnable
    {
        private final Runnable r;
        
        DefaultRunnableDecorator(final Runnable r) {
            this.r = r;
        }
        
        @Override
        public void run() {
            try {
                this.r.run();
            }
            finally {
                FastThreadLocal.removeAll();
            }
        }
    }
}
