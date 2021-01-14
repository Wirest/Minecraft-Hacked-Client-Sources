package io.netty.util;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ResourceLeakDetector<T> {
    private static final String PROP_LEVEL = "io.netty.leakDetectionLevel";
    private static final Level DEFAULT_LEVEL = Level.SIMPLE;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(ResourceLeakDetector.class);
    private static final int DEFAULT_SAMPLING_INTERVAL = 113;
    private static final String[] STACK_TRACE_ELEMENT_EXCLUSIONS = {"io.netty.buffer.AbstractByteBufAllocator.toLeakAwareBuffer("};
    private static Level level;

    static {
        boolean bool;
        if (SystemPropertyUtil.get("io.netty.noResourceLeakDetection") != null) {
            bool = SystemPropertyUtil.getBoolean("io.netty.noResourceLeakDetection", false);
            logger.debug("-Dio.netty.noResourceLeakDetection: {}", Boolean.valueOf(bool));
            logger.warn("-Dio.netty.noResourceLeakDetection is deprecated. Use '-D{}={}' instead.", "io.netty.leakDetectionLevel", DEFAULT_LEVEL.name().toLowerCase());
        } else {
            bool = false;
        }
        Level localLevel1 = bool ? Level.DISABLED : DEFAULT_LEVEL;
        String str = SystemPropertyUtil.get("io.netty.leakDetectionLevel", localLevel1.name()).trim().toUpperCase();
        Object localObject = DEFAULT_LEVEL;
        Iterator localIterator = EnumSet.allOf(Level.class).iterator();
        while (localIterator.hasNext()) {
            Level localLevel2 = (Level) localIterator.next();
            if ((str.equals(localLevel2.name())) || (str.equals(String.valueOf(localLevel2.ordinal())))) {
                localObject = localLevel2;
            }
        }
        level = (Level) localObject;
        if (logger.isDebugEnabled()) {
            logger.debug("-D{}: {}", "io.netty.leakDetectionLevel", ((Level) localObject).name().toLowerCase());
        }
    }

    private final ResourceLeakDetector<T>.DefaultResourceLeak head = new DefaultResourceLeak(null);
    private final ResourceLeakDetector<T>.DefaultResourceLeak tail = new DefaultResourceLeak(null);
    private final ReferenceQueue<Object> refQueue = new ReferenceQueue();
    private final ConcurrentMap<String, Boolean> reportedLeaks = PlatformDependent.newConcurrentHashMap();
    private final String resourceType;
    private final int samplingInterval;
    private final long maxActive;
    private final AtomicBoolean loggedTooManyActive = new AtomicBoolean();
    private long active;
    private long leakCheckCnt;

    public ResourceLeakDetector(Class<?> paramClass) {
        this(StringUtil.simpleClassName(paramClass));
    }

    public ResourceLeakDetector(String paramString) {
        this(paramString, 113, Long.MAX_VALUE);
    }

    public ResourceLeakDetector(Class<?> paramClass, int paramInt, long paramLong) {
        this(StringUtil.simpleClassName(paramClass), paramInt, paramLong);
    }

    public ResourceLeakDetector(String paramString, int paramInt, long paramLong) {
        if (paramString == null) {
            throw new NullPointerException("resourceType");
        }
        if (paramInt <= 0) {
            throw new IllegalArgumentException("samplingInterval: " + paramInt + " (expected: 1+)");
        }
        if (paramLong <= 0L) {
            throw new IllegalArgumentException("maxActive: " + paramLong + " (expected: 1+)");
        }
        this.resourceType = paramString;
        this.samplingInterval = paramInt;
        this.maxActive = paramLong;
        this.head.next = this.tail;
        this.tail.prev = this.head;
    }

    public static boolean isEnabled() {
        return getLevel().ordinal() > Level.DISABLED.ordinal();
    }

    @Deprecated
    public static void setEnabled(boolean paramBoolean) {
        setLevel(paramBoolean ? Level.SIMPLE : Level.DISABLED);
    }

    public static Level getLevel() {
        return level;
    }

    public static void setLevel(Level paramLevel) {
        if (paramLevel == null) {
            throw new NullPointerException("level");
        }
        level = paramLevel;
    }

    static String newRecord(int paramInt) {
        StringBuilder localStringBuilder = new StringBuilder(4096);
        StackTraceElement[] arrayOfStackTraceElement1 = new Throwable().getStackTrace();
        for (StackTraceElement localStackTraceElement : arrayOfStackTraceElement1) {
            if (paramInt > 0) {
                paramInt--;
            } else {
                String str1 = localStackTraceElement.toString();
                int k = 0;
                for (String str2 : STACK_TRACE_ELEMENT_EXCLUSIONS) {
                    if (str1.startsWith(str2)) {
                        k = 1;
                        break;
                    }
                }
                if (k == 0) {
                    localStringBuilder.append('\t');
                    localStringBuilder.append(str1);
                    localStringBuilder.append(StringUtil.NEWLINE);
                }
            }
        }
        return localStringBuilder.toString();
    }

    public ResourceLeak open(T paramT) {
        Level localLevel = level;
        if (localLevel == Level.DISABLED) {
            return null;
        }
        if (localLevel.ordinal() < Level.PARANOID.ordinal()) {
            if (this.leakCheckCnt++ % this.samplingInterval == 0L) {
                reportLeak(localLevel);
                return new DefaultResourceLeak(paramT);
            }
            return null;
        }
        reportLeak(localLevel);
        return new DefaultResourceLeak(paramT);
    }

    private void reportLeak(Level paramLevel) {
        if (!logger.isErrorEnabled()) {
            for (; ; ) {
                DefaultResourceLeak localDefaultResourceLeak1 = (DefaultResourceLeak) this.refQueue.poll();
                if (localDefaultResourceLeak1 == null) {
                    break;
                }
                localDefaultResourceLeak1.close();
            }
            return;
        }
        int i = paramLevel == Level.PARANOID ? 1 : this.samplingInterval;
        if ((this.active * i > this.maxActive) && (this.loggedTooManyActive.compareAndSet(false, true))) {
            logger.error("LEAK: You are creating too many " + this.resourceType + " instances.  " + this.resourceType + " is a shared resource that must be reused across the JVM," + "so that only a few instances are created.");
        }
        for (; ; ) {
            DefaultResourceLeak localDefaultResourceLeak2 = (DefaultResourceLeak) this.refQueue.poll();
            if (localDefaultResourceLeak2 == null) {
                break;
            }
            localDefaultResourceLeak2.clear();
            if (localDefaultResourceLeak2.close()) {
                String str = localDefaultResourceLeak2.toString();
                if (this.reportedLeaks.putIfAbsent(str, Boolean.TRUE) == null) {
                    if (str.isEmpty()) {
                        logger.error("LEAK: {}.release() was not called before it's garbage-collected. Enable advanced leak reporting to find out where the leak occurred. To enable advanced leak reporting, specify the JVM option '-D{}={}' or call {}.setLevel()", new Object[]{this.resourceType, "io.netty.leakDetectionLevel", Level.ADVANCED.name().toLowerCase(), StringUtil.simpleClassName(this)});
                    } else {
                        logger.error("LEAK: {}.release() was not called before it's garbage-collected.{}", this.resourceType, str);
                    }
                }
            }
        }
    }

    public static enum Level {
        DISABLED, SIMPLE, ADVANCED, PARANOID;

        private Level() {
        }
    }

    private final class DefaultResourceLeak
            extends PhantomReference<Object>
            implements ResourceLeak {
        private static final int MAX_RECORDS = 4;
        private final String creationRecord;
        private final Deque<String> lastRecords = new ArrayDeque();
        private final AtomicBoolean freed;
        private ResourceLeakDetector<T>.DefaultResourceLeak prev;
        private ResourceLeakDetector<T>.DefaultResourceLeak next;

        DefaultResourceLeak(Object paramObject) {
            super(paramObject != null ? ResourceLeakDetector.this.refQueue : null);
            if (paramObject != null) {
                ResourceLeakDetector.Level localLevel = ResourceLeakDetector.getLevel();
                if (localLevel.ordinal() >= ResourceLeakDetector.Level.ADVANCED.ordinal()) {
                    this.creationRecord = ResourceLeakDetector.newRecord(3);
                } else {
                    this.creationRecord = null;
                }
                synchronized (ResourceLeakDetector.this.head) {
                    this.prev = ResourceLeakDetector.this.head;
                    this.next = ResourceLeakDetector.this.head.next;
                    ResourceLeakDetector.this.head.next.prev = this;
                    ResourceLeakDetector.this.head.next = this;
                    ResourceLeakDetector.access$408(ResourceLeakDetector.this);
                }
                this.freed = new AtomicBoolean();
            } else {
                this.creationRecord = null;
                this.freed = new AtomicBoolean(true);
            }
        }

        public void record() {
            if (this.creationRecord != null) {
                String str = ResourceLeakDetector.newRecord(2);
                synchronized (this.lastRecords) {
                    int i = this.lastRecords.size();
                    if ((i == 0) || (!((String) this.lastRecords.getLast()).equals(str))) {
                        this.lastRecords.add(str);
                    }
                    if (i > 4) {
                        this.lastRecords.removeFirst();
                    }
                }
            }
        }

        public boolean close() {
            if (this.freed.compareAndSet(false, true)) {
                synchronized (ResourceLeakDetector.this.head) {
                    ResourceLeakDetector.access$410(ResourceLeakDetector.this);
                    this.prev.next = this.next;
                    this.next.prev = this.prev;
                    this.prev = null;
                    this.next = null;
                }
                return true;
            }
            return false;
        }

        public String toString() {
            if (this.creationRecord == null) {
                return "";
            }
            Object[] arrayOfObject;
            synchronized (this.lastRecords) {
                arrayOfObject = this.lastRecords.toArray();
            }
      ??? =new StringBuilder(16384);
            ((StringBuilder) ? ??).append(StringUtil.NEWLINE);
            ((StringBuilder) ? ??).append("Recent access records: ");
            ((StringBuilder) ? ??).append(arrayOfObject.length);
            ((StringBuilder) ? ??).append(StringUtil.NEWLINE);
            if (arrayOfObject.length > 0) {
                for (int i = arrayOfObject.length - 1; i >= 0; i--) {
                    ((StringBuilder) ? ??).append('#');
                    ((StringBuilder) ? ??).append(i | 0x1);
                    ((StringBuilder) ? ??).append(':');
                    ((StringBuilder) ? ??).append(StringUtil.NEWLINE);
                    ((StringBuilder) ? ??).append(arrayOfObject[i]);
                }
            }
            ((StringBuilder) ? ??).append("Created at:");
            ((StringBuilder) ? ??).append(StringUtil.NEWLINE);
            ((StringBuilder) ? ??).append(this.creationRecord);
            ((StringBuilder) ? ??).setLength(((StringBuilder) ? ? ?).length() - StringUtil.NEWLINE.length());
            return ((StringBuilder) ? ??).toString();
        }
    }
}




