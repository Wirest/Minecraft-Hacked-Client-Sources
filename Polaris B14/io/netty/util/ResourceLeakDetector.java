/*     */ package io.netty.util;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.lang.ref.PhantomReference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.EnumSet;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ResourceLeakDetector<T>
/*     */ {
/*     */   private static final String PROP_LEVEL = "io.netty.leakDetectionLevel";
/*  37 */   private static final Level DEFAULT_LEVEL = Level.SIMPLE;
/*     */   
/*     */ 
/*     */   private static Level level;
/*     */   
/*     */ 
/*     */ 
/*     */   public static enum Level
/*     */   {
/*  46 */     DISABLED, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  51 */     SIMPLE, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  56 */     ADVANCED, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  61 */     PARANOID;
/*     */     
/*     */     private Level() {}
/*     */   }
/*     */   
/*  66 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ResourceLeakDetector.class);
/*     */   private static final int DEFAULT_SAMPLING_INTERVAL = 113;
/*     */   
/*  69 */   static { String levelStr = SystemPropertyUtil.get("io.netty.leakDetectionLevel", DEFAULT_LEVEL.name()).trim().toUpperCase();
/*  70 */     Level level = DEFAULT_LEVEL;
/*  71 */     for (Level l : EnumSet.allOf(Level.class)) {
/*  72 */       if ((levelStr.equals(l.name())) || (levelStr.equals(String.valueOf(l.ordinal())))) {
/*  73 */         level = l;
/*     */       }
/*     */     }
/*     */     
/*  77 */     level = level;
/*  78 */     if (logger.isDebugEnabled()) {
/*  79 */       logger.debug("-D{}: {}", "io.netty.leakDetectionLevel", level.name().toLowerCase());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setLevel(Level level)
/*     */   {
/*  89 */     if (level == null) {
/*  90 */       throw new NullPointerException("level");
/*     */     }
/*  92 */     level = level;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static Level getLevel()
/*     */   {
/*  99 */     return level;
/*     */   }
/*     */   
/*     */ 
/* 103 */   private final ResourceLeakDetector<T>.DefaultResourceLeak head = new DefaultResourceLeak(null);
/* 104 */   private final ResourceLeakDetector<T>.DefaultResourceLeak tail = new DefaultResourceLeak(null);
/*     */   
/* 106 */   private final ReferenceQueue<Object> refQueue = new ReferenceQueue();
/* 107 */   private final ConcurrentMap<String, Boolean> reportedLeaks = PlatformDependent.newConcurrentHashMap();
/*     */   
/*     */   private final String resourceType;
/*     */   private final int samplingInterval;
/*     */   private final long maxActive;
/*     */   private long active;
/* 113 */   private final AtomicBoolean loggedTooManyActive = new AtomicBoolean();
/*     */   private long leakCheckCnt;
/*     */   
/*     */   public ResourceLeakDetector(Class<?> resourceType)
/*     */   {
/* 118 */     this(StringUtil.simpleClassName(resourceType));
/*     */   }
/*     */   
/*     */   public ResourceLeakDetector(String resourceType) {
/* 122 */     this(resourceType, 113, Long.MAX_VALUE);
/*     */   }
/*     */   
/*     */   public ResourceLeakDetector(Class<?> resourceType, int samplingInterval, long maxActive) {
/* 126 */     this(StringUtil.simpleClassName(resourceType), samplingInterval, maxActive);
/*     */   }
/*     */   
/*     */   public ResourceLeakDetector(String resourceType, int samplingInterval, long maxActive) {
/* 130 */     if (resourceType == null) {
/* 131 */       throw new NullPointerException("resourceType");
/*     */     }
/* 133 */     if (samplingInterval <= 0) {
/* 134 */       throw new IllegalArgumentException("samplingInterval: " + samplingInterval + " (expected: 1+)");
/*     */     }
/* 136 */     if (maxActive <= 0L) {
/* 137 */       throw new IllegalArgumentException("maxActive: " + maxActive + " (expected: 1+)");
/*     */     }
/*     */     
/* 140 */     this.resourceType = resourceType;
/* 141 */     this.samplingInterval = samplingInterval;
/* 142 */     this.maxActive = maxActive;
/*     */     
/* 144 */     this.head.next = this.tail;
/* 145 */     this.tail.prev = this.head;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResourceLeak open(T obj)
/*     */   {
/* 155 */     Level level = level;
/* 156 */     if (level == Level.DISABLED) {
/* 157 */       return null;
/*     */     }
/*     */     
/* 160 */     if (level.ordinal() < Level.PARANOID.ordinal()) {
/* 161 */       if (this.leakCheckCnt++ % this.samplingInterval == 0L) {
/* 162 */         reportLeak(level);
/* 163 */         return new DefaultResourceLeak(obj);
/*     */       }
/* 165 */       return null;
/*     */     }
/*     */     
/* 168 */     reportLeak(level);
/* 169 */     return new DefaultResourceLeak(obj);
/*     */   }
/*     */   
/*     */   private void reportLeak(Level level)
/*     */   {
/* 174 */     if (!logger.isErrorEnabled())
/*     */     {
/*     */       for (;;) {
/* 177 */         ResourceLeakDetector<T>.DefaultResourceLeak ref = (DefaultResourceLeak)this.refQueue.poll();
/* 178 */         if (ref == null) {
/*     */           break;
/*     */         }
/* 181 */         ref.close();
/*     */       }
/* 183 */       return;
/*     */     }
/*     */     
/*     */ 
/* 187 */     int samplingInterval = level == Level.PARANOID ? 1 : this.samplingInterval;
/* 188 */     if ((this.active * samplingInterval > this.maxActive) && (this.loggedTooManyActive.compareAndSet(false, true))) {
/* 189 */       logger.error("LEAK: You are creating too many " + this.resourceType + " instances.  " + this.resourceType + " is a shared resource that must be reused across the JVM," + "so that only a few instances are created.");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     for (;;)
/*     */     {
/* 197 */       ResourceLeakDetector<T>.DefaultResourceLeak ref = (DefaultResourceLeak)this.refQueue.poll();
/* 198 */       if (ref == null) {
/*     */         break;
/*     */       }
/*     */       
/* 202 */       ref.clear();
/*     */       
/* 204 */       if (ref.close())
/*     */       {
/*     */ 
/*     */ 
/* 208 */         String records = ref.toString();
/* 209 */         if (this.reportedLeaks.putIfAbsent(records, Boolean.TRUE) == null) {
/* 210 */           if (records.isEmpty()) {
/* 211 */             logger.error("LEAK: {}.release() was not called before it's garbage-collected. Enable advanced leak reporting to find out where the leak occurred. To enable advanced leak reporting, specify the JVM option '-D{}={}' or call {}.setLevel() See http://netty.io/wiki/reference-counted-objects.html for more information.", new Object[] { this.resourceType, "io.netty.leakDetectionLevel", Level.ADVANCED.name().toLowerCase(), StringUtil.simpleClassName(this) });
/*     */ 
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/*     */ 
/* 218 */             logger.error("LEAK: {}.release() was not called before it's garbage-collected. See http://netty.io/wiki/reference-counted-objects.html for more information.{}", this.resourceType, records);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private final class DefaultResourceLeak
/*     */     extends PhantomReference<Object>
/*     */     implements ResourceLeak
/*     */   {
/*     */     private static final int MAX_RECORDS = 4;
/*     */     private final String creationRecord;
/* 232 */     private final Deque<String> lastRecords = new ArrayDeque();
/*     */     private final AtomicBoolean freed;
/*     */     private ResourceLeakDetector<T>.DefaultResourceLeak prev;
/*     */     private ResourceLeakDetector<T>.DefaultResourceLeak next;
/*     */     
/*     */     DefaultResourceLeak(Object referent) {
/* 238 */       super(referent != null ? ResourceLeakDetector.this.refQueue : null);
/*     */       ResourceLeakDetector.Level level;
/* 240 */       if (referent != null) {
/* 241 */         level = ResourceLeakDetector.getLevel();
/* 242 */         if (level.ordinal() >= ResourceLeakDetector.Level.ADVANCED.ordinal()) {
/* 243 */           this.creationRecord = ResourceLeakDetector.newRecord(null, 3);
/*     */         } else {
/* 245 */           this.creationRecord = null;
/*     */         }
/*     */         
/*     */ 
/* 249 */         synchronized (ResourceLeakDetector.this.head) {
/* 250 */           this.prev = ResourceLeakDetector.this.head;
/* 251 */           this.next = ResourceLeakDetector.this.head.next;
/* 252 */           ResourceLeakDetector.this.head.next.prev = this;
/* 253 */           ResourceLeakDetector.this.head.next = this;
/* 254 */           ResourceLeakDetector.access$408(ResourceLeakDetector.this);
/*     */         }
/* 256 */         this.freed = new AtomicBoolean();
/*     */       } else {
/* 258 */         this.creationRecord = null;
/* 259 */         this.freed = new AtomicBoolean(true);
/*     */       }
/*     */     }
/*     */     
/*     */     public void record()
/*     */     {
/* 265 */       record0(null, 3);
/*     */     }
/*     */     
/*     */     public void record(Object hint)
/*     */     {
/* 270 */       record0(hint, 3);
/*     */     }
/*     */     
/*     */     private void record0(Object hint, int recordsToSkip) {
/* 274 */       if (this.creationRecord != null) {
/* 275 */         String value = ResourceLeakDetector.newRecord(hint, recordsToSkip);
/*     */         
/* 277 */         synchronized (this.lastRecords) {
/* 278 */           int size = this.lastRecords.size();
/* 279 */           if ((size == 0) || (!((String)this.lastRecords.getLast()).equals(value))) {
/* 280 */             this.lastRecords.add(value);
/*     */           }
/* 282 */           if (size > 4) {
/* 283 */             this.lastRecords.removeFirst();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean close()
/*     */     {
/* 291 */       if (this.freed.compareAndSet(false, true)) {
/* 292 */         synchronized (ResourceLeakDetector.this.head) {
/* 293 */           ResourceLeakDetector.access$410(ResourceLeakDetector.this);
/* 294 */           this.prev.next = this.next;
/* 295 */           this.next.prev = this.prev;
/* 296 */           this.prev = null;
/* 297 */           this.next = null;
/*     */         }
/* 299 */         return true;
/*     */       }
/* 301 */       return false;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 306 */       if (this.creationRecord == null) {
/* 307 */         return "";
/*     */       }
/*     */       
/*     */       Object[] array;
/* 311 */       synchronized (this.lastRecords) {
/* 312 */         array = this.lastRecords.toArray();
/*     */       }
/*     */       
/* 315 */       StringBuilder buf = new StringBuilder(16384).append(StringUtil.NEWLINE).append("Recent access records: ").append(array.length).append(StringUtil.NEWLINE);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 321 */       if (array.length > 0) {
/* 322 */         for (int i = array.length - 1; i >= 0; i--) {
/* 323 */           buf.append('#').append(i + 1).append(':').append(StringUtil.NEWLINE).append(array[i]);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 331 */       buf.append("Created at:").append(StringUtil.NEWLINE).append(this.creationRecord);
/*     */       
/*     */ 
/*     */ 
/* 335 */       buf.setLength(buf.length() - StringUtil.NEWLINE.length());
/* 336 */       return buf.toString();
/*     */     }
/*     */   }
/*     */   
/* 340 */   private static final String[] STACK_TRACE_ELEMENT_EXCLUSIONS = { "io.netty.util.ReferenceCountUtil.touch(", "io.netty.buffer.AdvancedLeakAwareByteBuf.touch(", "io.netty.buffer.AbstractByteBufAllocator.toLeakAwareBuffer(" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static String newRecord(Object hint, int recordsToSkip)
/*     */   {
/* 347 */     StringBuilder buf = new StringBuilder(4096);
/*     */     
/*     */ 
/* 350 */     if (hint != null) {
/* 351 */       buf.append("\tHint: ");
/*     */       
/* 353 */       if ((hint instanceof ResourceLeakHint)) {
/* 354 */         buf.append(((ResourceLeakHint)hint).toHintString());
/*     */       } else {
/* 356 */         buf.append(hint);
/*     */       }
/* 358 */       buf.append(StringUtil.NEWLINE);
/*     */     }
/*     */     
/*     */ 
/* 362 */     StackTraceElement[] array = new Throwable().getStackTrace();
/* 363 */     for (StackTraceElement e : array) {
/* 364 */       if (recordsToSkip > 0) {
/* 365 */         recordsToSkip--;
/*     */       } else {
/* 367 */         String estr = e.toString();
/*     */         
/*     */ 
/* 370 */         boolean excluded = false;
/* 371 */         for (String exclusion : STACK_TRACE_ELEMENT_EXCLUSIONS) {
/* 372 */           if (estr.startsWith(exclusion)) {
/* 373 */             excluded = true;
/* 374 */             break;
/*     */           }
/*     */         }
/*     */         
/* 378 */         if (!excluded) {
/* 379 */           buf.append('\t');
/* 380 */           buf.append(estr);
/* 381 */           buf.append(StringUtil.NEWLINE);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 386 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\ResourceLeakDetector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */