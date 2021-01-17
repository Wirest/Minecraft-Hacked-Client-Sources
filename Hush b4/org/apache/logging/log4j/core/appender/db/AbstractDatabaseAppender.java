// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db;

import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.LoggingException;
import org.apache.logging.log4j.core.LogEvent;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.Filter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import org.apache.logging.log4j.core.appender.AbstractAppender;

public abstract class AbstractDatabaseAppender<T extends AbstractDatabaseManager> extends AbstractAppender
{
    private final ReadWriteLock lock;
    private final Lock readLock;
    private final Lock writeLock;
    private T manager;
    
    protected AbstractDatabaseAppender(final String name, final Filter filter, final boolean ignoreExceptions, final T manager) {
        super(name, filter, null, ignoreExceptions);
        this.lock = new ReentrantReadWriteLock();
        this.readLock = this.lock.readLock();
        this.writeLock = this.lock.writeLock();
        this.manager = manager;
    }
    
    @Override
    public final Layout<LogEvent> getLayout() {
        return null;
    }
    
    public final T getManager() {
        return this.manager;
    }
    
    @Override
    public final void start() {
        if (this.getManager() == null) {
            AbstractDatabaseAppender.LOGGER.error("No AbstractDatabaseManager set for the appender named [{}].", this.getName());
        }
        super.start();
        if (this.getManager() != null) {
            this.getManager().connect();
        }
    }
    
    @Override
    public final void stop() {
        super.stop();
        if (this.getManager() != null) {
            this.getManager().release();
        }
    }
    
    @Override
    public final void append(final LogEvent event) {
        this.readLock.lock();
        try {
            this.getManager().write(event);
        }
        catch (LoggingException e) {
            AbstractDatabaseAppender.LOGGER.error("Unable to write to database [{}] for appender [{}].", this.getManager().getName(), this.getName(), e);
            throw e;
        }
        catch (Exception e2) {
            AbstractDatabaseAppender.LOGGER.error("Unable to write to database [{}] for appender [{}].", this.getManager().getName(), this.getName(), e2);
            throw new AppenderLoggingException("Unable to write to database in appender: " + e2.getMessage(), e2);
        }
        finally {
            this.readLock.unlock();
        }
    }
    
    protected final void replaceManager(final T manager) {
        this.writeLock.lock();
        try {
            final T old = this.getManager();
            if (!manager.isConnected()) {
                manager.connect();
            }
            this.manager = manager;
            old.release();
        }
        finally {
            this.writeLock.unlock();
        }
    }
}
