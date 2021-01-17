// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.status;

import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.Level;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.logging.log4j.simple.SimpleLogger;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.spi.AbstractLogger;

public final class StatusLogger extends AbstractLogger
{
    public static final String MAX_STATUS_ENTRIES = "log4j2.status.entries";
    private static final String NOT_AVAIL = "?";
    private static final PropertiesUtil PROPS;
    private static final int MAX_ENTRIES;
    private static final String DEFAULT_STATUS_LEVEL;
    private static final StatusLogger STATUS_LOGGER;
    private final SimpleLogger logger;
    private final CopyOnWriteArrayList<StatusListener> listeners;
    private final ReentrantReadWriteLock listenersLock;
    private final Queue<StatusData> messages;
    private final ReentrantLock msgLock;
    private int listenersLevel;
    
    private StatusLogger() {
        this.listeners = new CopyOnWriteArrayList<StatusListener>();
        this.listenersLock = new ReentrantReadWriteLock();
        this.messages = new BoundedQueue<StatusData>(StatusLogger.MAX_ENTRIES);
        this.msgLock = new ReentrantLock();
        this.logger = new SimpleLogger("StatusLogger", Level.ERROR, false, true, false, false, "", null, StatusLogger.PROPS, System.err);
        this.listenersLevel = Level.toLevel(StatusLogger.DEFAULT_STATUS_LEVEL, Level.WARN).intLevel();
    }
    
    public static StatusLogger getLogger() {
        return StatusLogger.STATUS_LOGGER;
    }
    
    public Level getLevel() {
        return this.logger.getLevel();
    }
    
    public void setLevel(final Level level) {
        this.logger.setLevel(level);
    }
    
    public void registerListener(final StatusListener listener) {
        this.listenersLock.writeLock().lock();
        try {
            this.listeners.add(listener);
            final Level lvl = listener.getStatusLevel();
            if (this.listenersLevel < lvl.intLevel()) {
                this.listenersLevel = lvl.intLevel();
            }
        }
        finally {
            this.listenersLock.writeLock().unlock();
        }
    }
    
    public void removeListener(final StatusListener listener) {
        this.listenersLock.writeLock().lock();
        try {
            this.listeners.remove(listener);
            int lowest = Level.toLevel(StatusLogger.DEFAULT_STATUS_LEVEL, Level.WARN).intLevel();
            for (final StatusListener l : this.listeners) {
                final int level = l.getStatusLevel().intLevel();
                if (lowest < level) {
                    lowest = level;
                }
            }
            this.listenersLevel = lowest;
        }
        finally {
            this.listenersLock.writeLock().unlock();
        }
    }
    
    public Iterator<StatusListener> getListeners() {
        return this.listeners.iterator();
    }
    
    public void reset() {
        this.listeners.clear();
        this.clear();
    }
    
    public List<StatusData> getStatusData() {
        this.msgLock.lock();
        try {
            return new ArrayList<StatusData>(this.messages);
        }
        finally {
            this.msgLock.unlock();
        }
    }
    
    public void clear() {
        this.msgLock.lock();
        try {
            this.messages.clear();
        }
        finally {
            this.msgLock.unlock();
        }
    }
    
    @Override
    public void log(final Marker marker, final String fqcn, final Level level, final Message msg, final Throwable t) {
        StackTraceElement element = null;
        if (fqcn != null) {
            element = this.getStackTraceElement(fqcn, Thread.currentThread().getStackTrace());
        }
        final StatusData data = new StatusData(element, level, msg, t);
        this.msgLock.lock();
        try {
            this.messages.add(data);
        }
        finally {
            this.msgLock.unlock();
        }
        if (this.listeners.size() > 0) {
            for (final StatusListener listener : this.listeners) {
                if (data.getLevel().isAtLeastAsSpecificAs(listener.getStatusLevel())) {
                    listener.log(data);
                }
            }
        }
        else {
            this.logger.log(marker, fqcn, level, msg, t);
        }
    }
    
    private StackTraceElement getStackTraceElement(final String fqcn, final StackTraceElement[] stackTrace) {
        if (fqcn == null) {
            return null;
        }
        boolean next = false;
        for (final StackTraceElement element : stackTrace) {
            if (next) {
                return element;
            }
            final String className = element.getClassName();
            if (fqcn.equals(className)) {
                next = true;
            }
            else if ("?".equals(className)) {
                break;
            }
        }
        return null;
    }
    
    @Override
    protected boolean isEnabled(final Level level, final Marker marker, final String data) {
        return this.isEnabled(level, marker);
    }
    
    @Override
    protected boolean isEnabled(final Level level, final Marker marker, final String data, final Throwable t) {
        return this.isEnabled(level, marker);
    }
    
    @Override
    protected boolean isEnabled(final Level level, final Marker marker, final String data, final Object... p1) {
        return this.isEnabled(level, marker);
    }
    
    @Override
    protected boolean isEnabled(final Level level, final Marker marker, final Object data, final Throwable t) {
        return this.isEnabled(level, marker);
    }
    
    @Override
    protected boolean isEnabled(final Level level, final Marker marker, final Message data, final Throwable t) {
        return this.isEnabled(level, marker);
    }
    
    @Override
    public boolean isEnabled(final Level level, final Marker marker) {
        if (this.listeners.size() > 0) {
            return this.listenersLevel >= level.intLevel();
        }
        switch (level) {
            case FATAL: {
                return this.logger.isFatalEnabled(marker);
            }
            case TRACE: {
                return this.logger.isTraceEnabled(marker);
            }
            case DEBUG: {
                return this.logger.isDebugEnabled(marker);
            }
            case INFO: {
                return this.logger.isInfoEnabled(marker);
            }
            case WARN: {
                return this.logger.isWarnEnabled(marker);
            }
            case ERROR: {
                return this.logger.isErrorEnabled(marker);
            }
            default: {
                return false;
            }
        }
    }
    
    static {
        PROPS = new PropertiesUtil("log4j2.StatusLogger.properties");
        MAX_ENTRIES = StatusLogger.PROPS.getIntegerProperty("log4j2.status.entries", 200);
        DEFAULT_STATUS_LEVEL = StatusLogger.PROPS.getStringProperty("log4j2.StatusLogger.level");
        STATUS_LOGGER = new StatusLogger();
    }
    
    private class BoundedQueue<E> extends ConcurrentLinkedQueue<E>
    {
        private static final long serialVersionUID = -3945953719763255337L;
        private final int size;
        
        public BoundedQueue(final int size) {
            this.size = size;
        }
        
        @Override
        public boolean add(final E object) {
            while (StatusLogger.this.messages.size() > this.size) {
                StatusLogger.this.messages.poll();
            }
            return super.add(object);
        }
    }
}
