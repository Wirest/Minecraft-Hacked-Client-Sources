// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceReportingEventHandler;
import org.apache.logging.log4j.status.StatusLogger;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.util.Util;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.EventHandler;
import java.util.concurrent.Executor;
import com.lmax.disruptor.dsl.ProducerType;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.core.LogEvent;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.EventFactory;
import java.util.concurrent.ExecutorService;
import com.lmax.disruptor.dsl.Disruptor;
import java.util.concurrent.ThreadFactory;
import org.apache.logging.log4j.Logger;

class AsyncLoggerConfigHelper
{
    private static final int MAX_DRAIN_ATTEMPTS_BEFORE_SHUTDOWN = 20;
    private static final int HALF_A_SECOND = 500;
    private static final int RINGBUFFER_MIN_SIZE = 128;
    private static final int RINGBUFFER_DEFAULT_SIZE = 262144;
    private static final Logger LOGGER;
    private static ThreadFactory threadFactory;
    private static volatile Disruptor<Log4jEventWrapper> disruptor;
    private static ExecutorService executor;
    private static volatile int count;
    private static final EventFactory<Log4jEventWrapper> FACTORY;
    private final EventTranslator<Log4jEventWrapper> translator;
    private final ThreadLocal<LogEvent> currentLogEvent;
    private final AsyncLoggerConfig asyncLoggerConfig;
    
    public AsyncLoggerConfigHelper(final AsyncLoggerConfig asyncLoggerConfig) {
        this.translator = (EventTranslator<Log4jEventWrapper>)new EventTranslator<Log4jEventWrapper>() {
            public void translateTo(final Log4jEventWrapper event, final long sequence) {
                event.event = AsyncLoggerConfigHelper.this.currentLogEvent.get();
                event.loggerConfig = AsyncLoggerConfigHelper.this.asyncLoggerConfig;
            }
        };
        this.currentLogEvent = new ThreadLocal<LogEvent>();
        this.asyncLoggerConfig = asyncLoggerConfig;
        claim();
    }
    
    private static synchronized void initDisruptor() {
        if (AsyncLoggerConfigHelper.disruptor != null) {
            AsyncLoggerConfigHelper.LOGGER.trace("AsyncLoggerConfigHelper not starting new disruptor, using existing object. Ref count is {}.", AsyncLoggerConfigHelper.count);
            return;
        }
        AsyncLoggerConfigHelper.LOGGER.trace("AsyncLoggerConfigHelper creating new disruptor. Ref count is {}.", AsyncLoggerConfigHelper.count);
        final int ringBufferSize = calculateRingBufferSize();
        final WaitStrategy waitStrategy = createWaitStrategy();
        AsyncLoggerConfigHelper.executor = Executors.newSingleThreadExecutor(AsyncLoggerConfigHelper.threadFactory);
        AsyncLoggerConfigHelper.disruptor = (Disruptor<Log4jEventWrapper>)new Disruptor((EventFactory)AsyncLoggerConfigHelper.FACTORY, ringBufferSize, (Executor)AsyncLoggerConfigHelper.executor, ProducerType.MULTI, waitStrategy);
        final EventHandler<Log4jEventWrapper>[] handlers = (EventHandler<Log4jEventWrapper>[])new Log4jEventWrapperHandler[] { new Log4jEventWrapperHandler() };
        final ExceptionHandler errorHandler = getExceptionHandler();
        AsyncLoggerConfigHelper.disruptor.handleExceptionsWith(errorHandler);
        AsyncLoggerConfigHelper.disruptor.handleEventsWith((EventHandler[])handlers);
        AsyncLoggerConfigHelper.LOGGER.debug("Starting AsyncLoggerConfig disruptor with ringbuffer size={}, waitStrategy={}, exceptionHandler={}...", AsyncLoggerConfigHelper.disruptor.getRingBuffer().getBufferSize(), waitStrategy.getClass().getSimpleName(), errorHandler);
        AsyncLoggerConfigHelper.disruptor.start();
    }
    
    private static WaitStrategy createWaitStrategy() {
        final String strategy = System.getProperty("AsyncLoggerConfig.WaitStrategy");
        AsyncLoggerConfigHelper.LOGGER.debug("property AsyncLoggerConfig.WaitStrategy={}", strategy);
        if ("Sleep".equals(strategy)) {
            return (WaitStrategy)new SleepingWaitStrategy();
        }
        if ("Yield".equals(strategy)) {
            return (WaitStrategy)new YieldingWaitStrategy();
        }
        if ("Block".equals(strategy)) {
            return (WaitStrategy)new BlockingWaitStrategy();
        }
        return (WaitStrategy)new SleepingWaitStrategy();
    }
    
    private static int calculateRingBufferSize() {
        int ringBufferSize = 262144;
        final String userPreferredRBSize = System.getProperty("AsyncLoggerConfig.RingBufferSize", String.valueOf(ringBufferSize));
        try {
            int size = Integer.parseInt(userPreferredRBSize);
            if (size < 128) {
                size = 128;
                AsyncLoggerConfigHelper.LOGGER.warn("Invalid RingBufferSize {}, using minimum size {}.", userPreferredRBSize, 128);
            }
            ringBufferSize = size;
        }
        catch (Exception ex) {
            AsyncLoggerConfigHelper.LOGGER.warn("Invalid RingBufferSize {}, using default size {}.", userPreferredRBSize, ringBufferSize);
        }
        return Util.ceilingNextPowerOfTwo(ringBufferSize);
    }
    
    private static ExceptionHandler getExceptionHandler() {
        final String cls = System.getProperty("AsyncLoggerConfig.ExceptionHandler");
        if (cls == null) {
            return null;
        }
        try {
            final Class<? extends ExceptionHandler> klass = (Class<? extends ExceptionHandler>)Class.forName(cls);
            final ExceptionHandler result = (ExceptionHandler)klass.newInstance();
            return result;
        }
        catch (Exception ignored) {
            AsyncLoggerConfigHelper.LOGGER.debug("AsyncLoggerConfig.ExceptionHandler not set: error creating " + cls + ": ", ignored);
            return null;
        }
    }
    
    static synchronized void claim() {
        ++AsyncLoggerConfigHelper.count;
        initDisruptor();
    }
    
    static synchronized void release() {
        if (--AsyncLoggerConfigHelper.count > 0) {
            AsyncLoggerConfigHelper.LOGGER.trace("AsyncLoggerConfigHelper: not shutting down disruptor: ref count is {}.", AsyncLoggerConfigHelper.count);
            return;
        }
        final Disruptor<Log4jEventWrapper> temp = AsyncLoggerConfigHelper.disruptor;
        if (temp == null) {
            AsyncLoggerConfigHelper.LOGGER.trace("AsyncLoggerConfigHelper: disruptor already shut down: ref count is {}.", AsyncLoggerConfigHelper.count);
            return;
        }
        AsyncLoggerConfigHelper.LOGGER.trace("AsyncLoggerConfigHelper: shutting down disruptor: ref count is {}.", AsyncLoggerConfigHelper.count);
        AsyncLoggerConfigHelper.disruptor = null;
        temp.shutdown();
        final RingBuffer<Log4jEventWrapper> ringBuffer = (RingBuffer<Log4jEventWrapper>)temp.getRingBuffer();
        for (int i = 0; i < 20 && !ringBuffer.hasAvailableCapacity(ringBuffer.getBufferSize()); ++i) {
            try {
                Thread.sleep(500L);
            }
            catch (InterruptedException ex) {}
        }
        AsyncLoggerConfigHelper.executor.shutdown();
        AsyncLoggerConfigHelper.executor = null;
    }
    
    public void callAppendersFromAnotherThread(final LogEvent event) {
        this.currentLogEvent.set(event);
        AsyncLoggerConfigHelper.disruptor.publishEvent((EventTranslator)this.translator);
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        AsyncLoggerConfigHelper.threadFactory = new DaemonThreadFactory("AsyncLoggerConfig-");
        AsyncLoggerConfigHelper.count = 0;
        FACTORY = (EventFactory)new EventFactory<Log4jEventWrapper>() {
            public Log4jEventWrapper newInstance() {
                return new Log4jEventWrapper();
            }
        };
    }
    
    private static class Log4jEventWrapper
    {
        private AsyncLoggerConfig loggerConfig;
        private LogEvent event;
        
        public void clear() {
            this.loggerConfig = null;
            this.event = null;
        }
    }
    
    private static class Log4jEventWrapperHandler implements SequenceReportingEventHandler<Log4jEventWrapper>
    {
        private static final int NOTIFY_PROGRESS_THRESHOLD = 50;
        private Sequence sequenceCallback;
        private int counter;
        
        public void setSequenceCallback(final Sequence sequenceCallback) {
            this.sequenceCallback = sequenceCallback;
        }
        
        public void onEvent(final Log4jEventWrapper event, final long sequence, final boolean endOfBatch) throws Exception {
            event.event.setEndOfBatch(endOfBatch);
            event.loggerConfig.asyncCallAppenders(event.event);
            event.clear();
            if (++this.counter > 50) {
                this.sequenceCallback.set(sequence);
                this.counter = 0;
            }
        }
    }
}
