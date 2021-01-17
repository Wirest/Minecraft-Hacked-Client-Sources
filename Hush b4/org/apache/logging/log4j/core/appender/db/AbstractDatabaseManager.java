// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db;

import org.apache.logging.log4j.core.appender.ManagerFactory;
import java.util.Iterator;
import org.apache.logging.log4j.core.LogEvent;
import java.util.ArrayList;
import org.apache.logging.log4j.core.appender.AbstractManager;

public abstract class AbstractDatabaseManager extends AbstractManager
{
    private final ArrayList<LogEvent> buffer;
    private final int bufferSize;
    private boolean connected;
    
    protected AbstractDatabaseManager(final String name, final int bufferSize) {
        super(name);
        this.connected = false;
        this.bufferSize = bufferSize;
        this.buffer = new ArrayList<LogEvent>(bufferSize + 1);
    }
    
    protected abstract void connectInternal() throws Exception;
    
    public final synchronized void connect() {
        if (!this.isConnected()) {
            try {
                this.connectInternal();
                this.connected = true;
            }
            catch (Exception e) {
                AbstractDatabaseManager.LOGGER.error("Could not connect to database using logging manager [{}].", this.getName(), e);
            }
        }
    }
    
    protected abstract void disconnectInternal() throws Exception;
    
    public final synchronized void disconnect() {
        this.flush();
        if (this.isConnected()) {
            try {
                this.disconnectInternal();
            }
            catch (Exception e) {
                AbstractDatabaseManager.LOGGER.warn("Error while disconnecting from database using logging manager [{}].", this.getName(), e);
            }
            finally {
                this.connected = false;
            }
        }
    }
    
    public final boolean isConnected() {
        return this.connected;
    }
    
    protected abstract void writeInternal(final LogEvent p0);
    
    public final synchronized void flush() {
        if (this.isConnected() && this.buffer.size() > 0) {
            for (final LogEvent event : this.buffer) {
                this.writeInternal(event);
            }
            this.buffer.clear();
        }
    }
    
    public final synchronized void write(final LogEvent event) {
        if (this.bufferSize > 0) {
            this.buffer.add(event);
            if (this.buffer.size() >= this.bufferSize || event.isEndOfBatch()) {
                this.flush();
            }
        }
        else {
            this.writeInternal(event);
        }
    }
    
    public final void releaseSub() {
        this.disconnect();
    }
    
    @Override
    public final String toString() {
        return this.getName();
    }
    
    protected static <M extends AbstractDatabaseManager, T extends AbstractFactoryData> M getManager(final String name, final T data, final ManagerFactory<M, T> factory) {
        return AbstractManager.getManager(name, factory, data);
    }
    
    protected abstract static class AbstractFactoryData
    {
        private final int bufferSize;
        
        protected AbstractFactoryData(final int bufferSize) {
            this.bufferSize = bufferSize;
        }
        
        public int getBufferSize() {
            return this.bufferSize;
        }
    }
}
