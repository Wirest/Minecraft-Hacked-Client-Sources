// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.lang.ref.PhantomReference;
import java.util.Iterator;
import java.util.EnumSet;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.ConcurrentMap;
import java.lang.ref.ReferenceQueue;
import io.netty.util.internal.logging.InternalLogger;

public final class ResourceLeakDetector<T>
{
    private static final String PROP_LEVEL = "io.netty.leakDetectionLevel";
    private static final Level DEFAULT_LEVEL;
    private static Level level;
    private static final InternalLogger logger;
    private static final int DEFAULT_SAMPLING_INTERVAL = 113;
    private final DefaultResourceLeak head;
    private final DefaultResourceLeak tail;
    private final ReferenceQueue<Object> refQueue;
    private final ConcurrentMap<String, Boolean> reportedLeaks;
    private final String resourceType;
    private final int samplingInterval;
    private final long maxActive;
    private long active;
    private final AtomicBoolean loggedTooManyActive;
    private long leakCheckCnt;
    private static final String[] STACK_TRACE_ELEMENT_EXCLUSIONS;
    
    @Deprecated
    public static void setEnabled(final boolean enabled) {
        setLevel(enabled ? Level.SIMPLE : Level.DISABLED);
    }
    
    public static boolean isEnabled() {
        return getLevel().ordinal() > Level.DISABLED.ordinal();
    }
    
    public static void setLevel(final Level level) {
        if (level == null) {
            throw new NullPointerException("level");
        }
        ResourceLeakDetector.level = level;
    }
    
    public static Level getLevel() {
        return ResourceLeakDetector.level;
    }
    
    public ResourceLeakDetector(final Class<?> resourceType) {
        this(StringUtil.simpleClassName(resourceType));
    }
    
    public ResourceLeakDetector(final String resourceType) {
        this(resourceType, 113, Long.MAX_VALUE);
    }
    
    public ResourceLeakDetector(final Class<?> resourceType, final int samplingInterval, final long maxActive) {
        this(StringUtil.simpleClassName(resourceType), samplingInterval, maxActive);
    }
    
    public ResourceLeakDetector(final String resourceType, final int samplingInterval, final long maxActive) {
        this.head = new DefaultResourceLeak((Object)null);
        this.tail = new DefaultResourceLeak((Object)null);
        this.refQueue = new ReferenceQueue<Object>();
        this.reportedLeaks = PlatformDependent.newConcurrentHashMap();
        this.loggedTooManyActive = new AtomicBoolean();
        if (resourceType == null) {
            throw new NullPointerException("resourceType");
        }
        if (samplingInterval <= 0) {
            throw new IllegalArgumentException("samplingInterval: " + samplingInterval + " (expected: 1+)");
        }
        if (maxActive <= 0L) {
            throw new IllegalArgumentException("maxActive: " + maxActive + " (expected: 1+)");
        }
        this.resourceType = resourceType;
        this.samplingInterval = samplingInterval;
        this.maxActive = maxActive;
        this.head.next = this.tail;
        this.tail.prev = this.head;
    }
    
    public ResourceLeak open(final T obj) {
        final Level level = ResourceLeakDetector.level;
        if (level == Level.DISABLED) {
            return null;
        }
        if (level.ordinal() >= Level.PARANOID.ordinal()) {
            this.reportLeak(level);
            return new DefaultResourceLeak(obj);
        }
        if (this.leakCheckCnt++ % this.samplingInterval == 0L) {
            this.reportLeak(level);
            return new DefaultResourceLeak(obj);
        }
        return null;
    }
    
    private void reportLeak(final Level level) {
        if (!ResourceLeakDetector.logger.isErrorEnabled()) {
            while (true) {
                final DefaultResourceLeak ref = (DefaultResourceLeak)this.refQueue.poll();
                if (ref == null) {
                    break;
                }
                ref.close();
            }
            return;
        }
        final int samplingInterval = (level == Level.PARANOID) ? 1 : this.samplingInterval;
        if (this.active * samplingInterval > this.maxActive && this.loggedTooManyActive.compareAndSet(false, true)) {
            ResourceLeakDetector.logger.error("LEAK: You are creating too many " + this.resourceType + " instances.  " + this.resourceType + " is a shared resource that must be reused across the JVM," + "so that only a few instances are created.");
        }
        while (true) {
            final DefaultResourceLeak ref2 = (DefaultResourceLeak)this.refQueue.poll();
            if (ref2 == null) {
                break;
            }
            ref2.clear();
            if (!ref2.close()) {
                continue;
            }
            final String records = ref2.toString();
            if (this.reportedLeaks.putIfAbsent(records, Boolean.TRUE) != null) {
                continue;
            }
            if (records.isEmpty()) {
                ResourceLeakDetector.logger.error("LEAK: {}.release() was not called before it's garbage-collected. Enable advanced leak reporting to find out where the leak occurred. To enable advanced leak reporting, specify the JVM option '-D{}={}' or call {}.setLevel()", this.resourceType, "io.netty.leakDetectionLevel", Level.ADVANCED.name().toLowerCase(), StringUtil.simpleClassName(this));
            }
            else {
                ResourceLeakDetector.logger.error("LEAK: {}.release() was not called before it's garbage-collected.{}", this.resourceType, records);
            }
        }
    }
    
    static String newRecord(int recordsToSkip) {
        final StringBuilder buf = new StringBuilder(4096);
        final StackTraceElement[] arr$;
        final StackTraceElement[] array = arr$ = new Throwable().getStackTrace();
        for (final StackTraceElement e : arr$) {
            if (recordsToSkip > 0) {
                --recordsToSkip;
            }
            else {
                final String estr = e.toString();
                boolean excluded = false;
                for (final String exclusion : ResourceLeakDetector.STACK_TRACE_ELEMENT_EXCLUSIONS) {
                    if (estr.startsWith(exclusion)) {
                        excluded = true;
                        break;
                    }
                }
                if (!excluded) {
                    buf.append('\t');
                    buf.append(estr);
                    buf.append(StringUtil.NEWLINE);
                }
            }
        }
        return buf.toString();
    }
    
    static {
        DEFAULT_LEVEL = Level.SIMPLE;
        logger = InternalLoggerFactory.getInstance(ResourceLeakDetector.class);
        boolean disabled;
        if (SystemPropertyUtil.get("io.netty.noResourceLeakDetection") != null) {
            disabled = SystemPropertyUtil.getBoolean("io.netty.noResourceLeakDetection", false);
            ResourceLeakDetector.logger.debug("-Dio.netty.noResourceLeakDetection: {}", (Object)disabled);
            ResourceLeakDetector.logger.warn("-Dio.netty.noResourceLeakDetection is deprecated. Use '-D{}={}' instead.", "io.netty.leakDetectionLevel", ResourceLeakDetector.DEFAULT_LEVEL.name().toLowerCase());
        }
        else {
            disabled = false;
        }
        final Level defaultLevel = disabled ? Level.DISABLED : ResourceLeakDetector.DEFAULT_LEVEL;
        final String levelStr = SystemPropertyUtil.get("io.netty.leakDetectionLevel", defaultLevel.name()).trim().toUpperCase();
        Level level = ResourceLeakDetector.DEFAULT_LEVEL;
        for (final Level l : EnumSet.allOf(Level.class)) {
            if (levelStr.equals(l.name()) || levelStr.equals(String.valueOf(l.ordinal()))) {
                level = l;
            }
        }
        ResourceLeakDetector.level = level;
        if (ResourceLeakDetector.logger.isDebugEnabled()) {
            ResourceLeakDetector.logger.debug("-D{}: {}", "io.netty.leakDetectionLevel", level.name().toLowerCase());
        }
        STACK_TRACE_ELEMENT_EXCLUSIONS = new String[] { "io.netty.buffer.AbstractByteBufAllocator.toLeakAwareBuffer(" };
    }
    
    public enum Level
    {
        DISABLED, 
        SIMPLE, 
        ADVANCED, 
        PARANOID;
    }
    
    private final class DefaultResourceLeak extends PhantomReference<Object> implements ResourceLeak
    {
        private static final int MAX_RECORDS = 4;
        private final String creationRecord;
        private final Deque<String> lastRecords;
        private final AtomicBoolean freed;
        private DefaultResourceLeak prev;
        private DefaultResourceLeak next;
        
        DefaultResourceLeak(final Object referent) {
            super(referent, (referent != null) ? ResourceLeakDetector.this.refQueue : null);
            this.lastRecords = new ArrayDeque<String>();
            if (referent != null) {
                final Level level = ResourceLeakDetector.getLevel();
                if (level.ordinal() >= Level.ADVANCED.ordinal()) {
                    this.creationRecord = ResourceLeakDetector.newRecord(3);
                }
                else {
                    this.creationRecord = null;
                }
                synchronized (ResourceLeakDetector.this.head) {
                    this.prev = ResourceLeakDetector.this.head;
                    this.next = ResourceLeakDetector.this.head.next;
                    ResourceLeakDetector.this.head.next.prev = this;
                    ResourceLeakDetector.this.head.next = this;
                    ResourceLeakDetector.this.active++;
                }
                this.freed = new AtomicBoolean();
            }
            else {
                this.creationRecord = null;
                this.freed = new AtomicBoolean(true);
            }
        }
        
        @Override
        public void record() {
            if (this.creationRecord != null) {
                final String value = ResourceLeakDetector.newRecord(2);
                synchronized (this.lastRecords) {
                    final int size = this.lastRecords.size();
                    if (size == 0 || !this.lastRecords.getLast().equals(value)) {
                        this.lastRecords.add(value);
                    }
                    if (size > 4) {
                        this.lastRecords.removeFirst();
                    }
                }
            }
        }
        
        @Override
        public boolean close() {
            if (this.freed.compareAndSet(false, true)) {
                synchronized (ResourceLeakDetector.this.head) {
                    ResourceLeakDetector.this.active--;
                    this.prev.next = this.next;
                    this.next.prev = this.prev;
                    this.prev = null;
                    this.next = null;
                }
                return true;
            }
            return false;
        }
        
        @Override
        public String toString() {
            if (this.creationRecord == null) {
                return "";
            }
            final Object[] array;
            synchronized (this.lastRecords) {
                array = this.lastRecords.toArray();
            }
            final StringBuilder buf = new StringBuilder(16384);
            buf.append(StringUtil.NEWLINE);
            buf.append("Recent access records: ");
            buf.append(array.length);
            buf.append(StringUtil.NEWLINE);
            if (array.length > 0) {
                for (int i = array.length - 1; i >= 0; --i) {
                    buf.append('#');
                    buf.append(i + 1);
                    buf.append(':');
                    buf.append(StringUtil.NEWLINE);
                    buf.append(array[i]);
                }
            }
            buf.append("Created at:");
            buf.append(StringUtil.NEWLINE);
            buf.append(this.creationRecord);
            buf.setLength(buf.length() - StringUtil.NEWLINE.length());
            return buf.toString();
        }
    }
}
