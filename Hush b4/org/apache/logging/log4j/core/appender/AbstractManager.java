// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public abstract class AbstractManager
{
    protected static final Logger LOGGER;
    private static final Map<String, AbstractManager> MAP;
    private static final Lock LOCK;
    protected int count;
    private final String name;
    
    protected AbstractManager(final String name) {
        this.name = name;
        AbstractManager.LOGGER.debug("Starting {} {}", this.getClass().getSimpleName(), name);
    }
    
    public static <M extends AbstractManager, T> M getManager(final String name, final ManagerFactory<M, T> factory, final T data) {
        AbstractManager.LOCK.lock();
        try {
            M manager = (M)AbstractManager.MAP.get(name);
            if (manager == null) {
                manager = factory.createManager(name, data);
                if (manager == null) {
                    throw new IllegalStateException("Unable to create a manager");
                }
                AbstractManager.MAP.put(name, manager);
            }
            final AbstractManager abstractManager = manager;
            ++abstractManager.count;
            return manager;
        }
        finally {
            AbstractManager.LOCK.unlock();
        }
    }
    
    public static boolean hasManager(final String name) {
        AbstractManager.LOCK.lock();
        try {
            return AbstractManager.MAP.containsKey(name);
        }
        finally {
            AbstractManager.LOCK.unlock();
        }
    }
    
    protected void releaseSub() {
    }
    
    protected int getCount() {
        return this.count;
    }
    
    public void release() {
        AbstractManager.LOCK.lock();
        try {
            --this.count;
            if (this.count <= 0) {
                AbstractManager.MAP.remove(this.name);
                AbstractManager.LOGGER.debug("Shutting down {} {}", this.getClass().getSimpleName(), this.getName());
                this.releaseSub();
            }
        }
        finally {
            AbstractManager.LOCK.unlock();
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public Map<String, String> getContentFormat() {
        return new HashMap<String, String>();
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        MAP = new HashMap<String, AbstractManager>();
        LOCK = new ReentrantLock();
    }
}
