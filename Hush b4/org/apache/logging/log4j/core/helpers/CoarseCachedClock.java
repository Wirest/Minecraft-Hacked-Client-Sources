// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.helpers;

import java.util.concurrent.locks.LockSupport;

public final class CoarseCachedClock implements Clock
{
    private static CoarseCachedClock instance;
    private volatile long millis;
    private final Thread updater;
    
    private CoarseCachedClock() {
        this.millis = System.currentTimeMillis();
        (this.updater = new Thread("Clock Updater Thread") {
            @Override
            public void run() {
                while (true) {
                    final long time = System.currentTimeMillis();
                    CoarseCachedClock.this.millis = time;
                    LockSupport.parkNanos(1000000L);
                }
            }
        }).setDaemon(true);
        this.updater.start();
    }
    
    public static CoarseCachedClock instance() {
        return CoarseCachedClock.instance;
    }
    
    @Override
    public long currentTimeMillis() {
        return this.millis;
    }
    
    static {
        CoarseCachedClock.instance = new CoarseCachedClock();
    }
}
