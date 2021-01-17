// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.helpers;

import java.util.concurrent.locks.LockSupport;

public final class CachedClock implements Clock
{
    private static final int UPDATE_THRESHOLD = 1023;
    private static CachedClock instance;
    private volatile long millis;
    private volatile short count;
    private final Thread updater;
    
    private CachedClock() {
        this.millis = System.currentTimeMillis();
        this.count = 0;
        (this.updater = new Thread("Clock Updater Thread") {
            @Override
            public void run() {
                while (true) {
                    final long time = System.currentTimeMillis();
                    CachedClock.this.millis = time;
                    LockSupport.parkNanos(1000000L);
                }
            }
        }).setDaemon(true);
        this.updater.start();
    }
    
    public static CachedClock instance() {
        return CachedClock.instance;
    }
    
    @Override
    public long currentTimeMillis() {
        final short count = (short)(this.count + 1);
        this.count = count;
        if ((count & 0x3FF) == 0x3FF) {
            this.millis = System.currentTimeMillis();
        }
        return this.millis;
    }
    
    static {
        CachedClock.instance = new CachedClock();
    }
}
