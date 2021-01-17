// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling;

import java.io.FileNotFoundException;
import java.io.BufferedOutputStream;
import java.io.File;
import org.apache.logging.log4j.core.appender.rolling.helper.Action;
import org.apache.logging.log4j.core.appender.rolling.helper.AbstractAction;
import org.apache.logging.log4j.Logger;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.OutputStreamManager;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import java.io.OutputStream;
import java.util.concurrent.Semaphore;
import org.apache.logging.log4j.core.appender.FileManager;

public class RollingFileManager extends FileManager
{
    private static RollingFileManagerFactory factory;
    private long size;
    private long initialTime;
    private final PatternProcessor patternProcessor;
    private final Semaphore semaphore;
    private final TriggeringPolicy policy;
    private final RolloverStrategy strategy;
    
    protected RollingFileManager(final String fileName, final String pattern, final OutputStream os, final boolean append, final long size, final long time, final TriggeringPolicy policy, final RolloverStrategy strategy, final String advertiseURI, final Layout<? extends Serializable> layout) {
        super(fileName, os, append, false, advertiseURI, layout);
        this.semaphore = new Semaphore(1);
        this.size = size;
        this.initialTime = time;
        this.policy = policy;
        this.strategy = strategy;
        this.patternProcessor = new PatternProcessor(pattern);
        policy.initialize(this);
    }
    
    public static RollingFileManager getFileManager(final String fileName, final String pattern, final boolean append, final boolean bufferedIO, final TriggeringPolicy policy, final RolloverStrategy strategy, final String advertiseURI, final Layout<? extends Serializable> layout) {
        return (RollingFileManager)OutputStreamManager.getManager(fileName, new FactoryData(pattern, append, bufferedIO, policy, strategy, advertiseURI, layout), RollingFileManager.factory);
    }
    
    @Override
    protected synchronized void write(final byte[] bytes, final int offset, final int length) {
        this.size += length;
        super.write(bytes, offset, length);
    }
    
    public long getFileSize() {
        return this.size;
    }
    
    public long getFileTime() {
        return this.initialTime;
    }
    
    public synchronized void checkRollover(final LogEvent event) {
        if (this.policy.isTriggeringEvent(event) && this.rollover(this.strategy)) {
            try {
                this.size = 0L;
                this.initialTime = System.currentTimeMillis();
                this.createFileAfterRollover();
            }
            catch (IOException ex) {
                RollingFileManager.LOGGER.error("FileManager (" + this.getFileName() + ") " + ex);
            }
        }
    }
    
    protected void createFileAfterRollover() throws IOException {
        final OutputStream os = new FileOutputStream(this.getFileName(), this.isAppend());
        this.setOutputStream(os);
    }
    
    public PatternProcessor getPatternProcessor() {
        return this.patternProcessor;
    }
    
    private boolean rollover(final RolloverStrategy strategy) {
        try {
            this.semaphore.acquire();
        }
        catch (InterruptedException ie) {
            RollingFileManager.LOGGER.error("Thread interrupted while attempting to check rollover", ie);
            return false;
        }
        boolean success = false;
        Thread thread = null;
        try {
            final RolloverDescription descriptor = strategy.rollover(this);
            if (descriptor != null) {
                this.close();
                if (descriptor.getSynchronous() != null) {
                    try {
                        success = descriptor.getSynchronous().execute();
                    }
                    catch (Exception ex) {
                        RollingFileManager.LOGGER.error("Error in synchronous task", ex);
                    }
                }
                if (success && descriptor.getAsynchronous() != null) {
                    thread = new Thread(new AsyncAction(descriptor.getAsynchronous(), this));
                    thread.start();
                }
                return true;
            }
            return false;
        }
        finally {
            if (thread == null) {
                this.semaphore.release();
            }
        }
    }
    
    static {
        RollingFileManager.factory = new RollingFileManagerFactory();
    }
    
    private static class AsyncAction extends AbstractAction
    {
        private final Action action;
        private final RollingFileManager manager;
        
        public AsyncAction(final Action act, final RollingFileManager manager) {
            this.action = act;
            this.manager = manager;
        }
        
        @Override
        public boolean execute() throws IOException {
            try {
                return this.action.execute();
            }
            finally {
                this.manager.semaphore.release();
            }
        }
        
        @Override
        public void close() {
            this.action.close();
        }
        
        @Override
        public boolean isComplete() {
            return this.action.isComplete();
        }
    }
    
    private static class FactoryData
    {
        private final String pattern;
        private final boolean append;
        private final boolean bufferedIO;
        private final TriggeringPolicy policy;
        private final RolloverStrategy strategy;
        private final String advertiseURI;
        private final Layout<? extends Serializable> layout;
        
        public FactoryData(final String pattern, final boolean append, final boolean bufferedIO, final TriggeringPolicy policy, final RolloverStrategy strategy, final String advertiseURI, final Layout<? extends Serializable> layout) {
            this.pattern = pattern;
            this.append = append;
            this.bufferedIO = bufferedIO;
            this.policy = policy;
            this.strategy = strategy;
            this.advertiseURI = advertiseURI;
            this.layout = layout;
        }
    }
    
    private static class RollingFileManagerFactory implements ManagerFactory<RollingFileManager, FactoryData>
    {
        @Override
        public RollingFileManager createManager(final String name, final FactoryData data) {
            final File file = new File(name);
            final File parent = file.getParentFile();
            if (null != parent && !parent.exists()) {
                parent.mkdirs();
            }
            try {
                file.createNewFile();
            }
            catch (IOException ioe) {
                RollingFileManager.LOGGER.error("Unable to create file " + name, ioe);
                return null;
            }
            final long size = data.append ? file.length() : 0L;
            final long time = file.lastModified();
            try {
                OutputStream os = new FileOutputStream(name, data.append);
                if (data.bufferedIO) {
                    os = new BufferedOutputStream(os);
                }
                return new RollingFileManager(name, data.pattern, os, data.append, size, time, data.policy, data.strategy, data.advertiseURI, data.layout);
            }
            catch (FileNotFoundException ex) {
                RollingFileManager.LOGGER.error("FileManager (" + name + ") " + ex);
                return null;
            }
        }
    }
}
