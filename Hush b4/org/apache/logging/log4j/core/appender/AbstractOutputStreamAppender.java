// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.LogEvent;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.core.Filter;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public abstract class AbstractOutputStreamAppender extends AbstractAppender
{
    protected final boolean immediateFlush;
    private volatile OutputStreamManager manager;
    private final ReadWriteLock rwLock;
    private final Lock readLock;
    private final Lock writeLock;
    
    protected AbstractOutputStreamAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter, final boolean ignoreExceptions, final boolean immediateFlush, final OutputStreamManager manager) {
        super(name, filter, layout, ignoreExceptions);
        this.rwLock = new ReentrantReadWriteLock();
        this.readLock = this.rwLock.readLock();
        this.writeLock = this.rwLock.writeLock();
        this.manager = manager;
        this.immediateFlush = immediateFlush;
    }
    
    protected OutputStreamManager getManager() {
        return this.manager;
    }
    
    protected void replaceManager(final OutputStreamManager newManager) {
        this.writeLock.lock();
        try {
            final OutputStreamManager old = this.manager;
            this.manager = newManager;
            old.release();
        }
        finally {
            this.writeLock.unlock();
        }
    }
    
    @Override
    public void start() {
        if (this.getLayout() == null) {
            AbstractOutputStreamAppender.LOGGER.error("No layout set for the appender named [" + this.getName() + "].");
        }
        if (this.manager == null) {
            AbstractOutputStreamAppender.LOGGER.error("No OutputStreamManager set for the appender named [" + this.getName() + "].");
        }
        super.start();
    }
    
    @Override
    public void stop() {
        super.stop();
        this.manager.release();
    }
    
    @Override
    public void append(final LogEvent event) {
        this.readLock.lock();
        try {
            final byte[] bytes = this.getLayout().toByteArray(event);
            if (bytes.length > 0) {
                this.manager.write(bytes);
                if (this.immediateFlush || event.isEndOfBatch()) {
                    this.manager.flush();
                }
            }
        }
        catch (AppenderLoggingException ex) {
            this.error("Unable to write to stream " + this.manager.getName() + " for appender " + this.getName());
            throw ex;
        }
        finally {
            this.readLock.unlock();
        }
    }
}
