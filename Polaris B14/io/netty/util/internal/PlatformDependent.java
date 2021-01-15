/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.internal.chmv8.ConcurrentHashMapV8;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
/*     */ import java.util.concurrent.atomic.AtomicLongFieldUpdater;
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PlatformDependent
/*     */ {
/*  56 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(PlatformDependent.class);
/*     */   
/*  58 */   private static final Pattern MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN = Pattern.compile("\\s*-XX:MaxDirectMemorySize\\s*=\\s*([0-9]+)\\s*([kKmMgG]?)\\s*$");
/*     */   
/*     */ 
/*  61 */   private static final boolean IS_ANDROID = isAndroid0();
/*  62 */   private static final boolean IS_WINDOWS = isWindows0();
/*     */   
/*     */   private static volatile Boolean IS_ROOT;
/*  65 */   private static final int JAVA_VERSION = javaVersion0();
/*     */   
/*  67 */   private static final boolean CAN_ENABLE_TCP_NODELAY_BY_DEFAULT = !isAndroid();
/*     */   
/*  69 */   private static final boolean HAS_UNSAFE = hasUnsafe0();
/*  70 */   private static final boolean CAN_USE_CHM_V8 = (HAS_UNSAFE) && (JAVA_VERSION < 8);
/*  71 */   private static final boolean DIRECT_BUFFER_PREFERRED = (HAS_UNSAFE) && (!SystemPropertyUtil.getBoolean("io.netty.noPreferDirect", false));
/*     */   
/*  73 */   private static final long MAX_DIRECT_MEMORY = maxDirectMemory0();
/*     */   
/*  75 */   private static final long ARRAY_BASE_OFFSET = arrayBaseOffset0();
/*     */   
/*  77 */   private static final boolean HAS_JAVASSIST = hasJavassist0();
/*     */   
/*  79 */   private static final File TMPDIR = tmpdir0();
/*     */   
/*  81 */   private static final int BIT_MODE = bitMode0();
/*     */   
/*  83 */   private static final int ADDRESS_SIZE = addressSize0();
/*     */   
/*     */   static {
/*  86 */     if (logger.isDebugEnabled()) {
/*  87 */       logger.debug("-Dio.netty.noPreferDirect: {}", Boolean.valueOf(!DIRECT_BUFFER_PREFERRED));
/*     */     }
/*     */     
/*  90 */     if ((!hasUnsafe()) && (!isAndroid())) {
/*  91 */       logger.info("Your platform does not provide complete low-level API for accessing direct buffers reliably. Unless explicitly requested, heap buffer will always be preferred to avoid potential system unstability.");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isAndroid()
/*     */   {
/* 102 */     return IS_ANDROID;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean isWindows()
/*     */   {
/* 109 */     return IS_WINDOWS;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isRoot()
/*     */   {
/* 117 */     if (IS_ROOT == null) {
/* 118 */       synchronized (PlatformDependent.class) {
/* 119 */         if (IS_ROOT == null) {
/* 120 */           IS_ROOT = Boolean.valueOf(isRoot0());
/*     */         }
/*     */       }
/*     */     }
/* 124 */     return IS_ROOT.booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static int javaVersion()
/*     */   {
/* 131 */     return JAVA_VERSION;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean canEnableTcpNoDelayByDefault()
/*     */   {
/* 138 */     return CAN_ENABLE_TCP_NODELAY_BY_DEFAULT;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean hasUnsafe()
/*     */   {
/* 146 */     return HAS_UNSAFE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean directBufferPreferred()
/*     */   {
/* 154 */     return DIRECT_BUFFER_PREFERRED;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static long maxDirectMemory()
/*     */   {
/* 161 */     return MAX_DIRECT_MEMORY;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean hasJavassist()
/*     */   {
/* 168 */     return HAS_JAVASSIST;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static File tmpdir()
/*     */   {
/* 175 */     return TMPDIR;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static int bitMode()
/*     */   {
/* 182 */     return BIT_MODE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int addressSize()
/*     */   {
/* 190 */     return ADDRESS_SIZE;
/*     */   }
/*     */   
/*     */   public static long allocateMemory(long size) {
/* 194 */     return PlatformDependent0.allocateMemory(size);
/*     */   }
/*     */   
/*     */   public static void freeMemory(long address) {
/* 198 */     PlatformDependent0.freeMemory(address);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void throwException(Throwable t)
/*     */   {
/* 205 */     if (hasUnsafe()) {
/* 206 */       PlatformDependent0.throwException(t);
/*     */     } else {
/* 208 */       throwException0(t);
/*     */     }
/*     */   }
/*     */   
/*     */   private static <E extends Throwable> void throwException0(Throwable t) throws Throwable
/*     */   {
/* 214 */     throw t;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap()
/*     */   {
/* 221 */     if (CAN_USE_CHM_V8) {
/* 222 */       return new ConcurrentHashMapV8();
/*     */     }
/* 224 */     return new ConcurrentHashMap();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(int initialCapacity)
/*     */   {
/* 232 */     if (CAN_USE_CHM_V8) {
/* 233 */       return new ConcurrentHashMapV8(initialCapacity);
/*     */     }
/* 235 */     return new ConcurrentHashMap(initialCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(int initialCapacity, float loadFactor)
/*     */   {
/* 243 */     if (CAN_USE_CHM_V8) {
/* 244 */       return new ConcurrentHashMapV8(initialCapacity, loadFactor);
/*     */     }
/* 246 */     return new ConcurrentHashMap(initialCapacity, loadFactor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(int initialCapacity, float loadFactor, int concurrencyLevel)
/*     */   {
/* 255 */     if (CAN_USE_CHM_V8) {
/* 256 */       return new ConcurrentHashMapV8(initialCapacity, loadFactor, concurrencyLevel);
/*     */     }
/* 258 */     return new ConcurrentHashMap(initialCapacity, loadFactor, concurrencyLevel);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(Map<? extends K, ? extends V> map)
/*     */   {
/* 266 */     if (CAN_USE_CHM_V8) {
/* 267 */       return new ConcurrentHashMapV8(map);
/*     */     }
/* 269 */     return new ConcurrentHashMap(map);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void freeDirectBuffer(ByteBuffer buffer)
/*     */   {
/* 278 */     if ((hasUnsafe()) && (!isAndroid()))
/*     */     {
/*     */ 
/* 281 */       PlatformDependent0.freeDirectBuffer(buffer);
/*     */     }
/*     */   }
/*     */   
/*     */   public static long directBufferAddress(ByteBuffer buffer) {
/* 286 */     return PlatformDependent0.directBufferAddress(buffer);
/*     */   }
/*     */   
/*     */   public static Object getObject(Object object, long fieldOffset) {
/* 290 */     return PlatformDependent0.getObject(object, fieldOffset);
/*     */   }
/*     */   
/*     */   public static Object getObjectVolatile(Object object, long fieldOffset) {
/* 294 */     return PlatformDependent0.getObjectVolatile(object, fieldOffset);
/*     */   }
/*     */   
/*     */   public static int getInt(Object object, long fieldOffset) {
/* 298 */     return PlatformDependent0.getInt(object, fieldOffset);
/*     */   }
/*     */   
/*     */   public static long objectFieldOffset(Field field) {
/* 302 */     return PlatformDependent0.objectFieldOffset(field);
/*     */   }
/*     */   
/*     */   public static byte getByte(long address) {
/* 306 */     return PlatformDependent0.getByte(address);
/*     */   }
/*     */   
/*     */   public static short getShort(long address) {
/* 310 */     return PlatformDependent0.getShort(address);
/*     */   }
/*     */   
/*     */   public static int getInt(long address) {
/* 314 */     return PlatformDependent0.getInt(address);
/*     */   }
/*     */   
/*     */   public static long getLong(long address) {
/* 318 */     return PlatformDependent0.getLong(address);
/*     */   }
/*     */   
/*     */   public static void putOrderedObject(Object object, long address, Object value) {
/* 322 */     PlatformDependent0.putOrderedObject(object, address, value);
/*     */   }
/*     */   
/*     */   public static void putByte(long address, byte value) {
/* 326 */     PlatformDependent0.putByte(address, value);
/*     */   }
/*     */   
/*     */   public static void putShort(long address, short value) {
/* 330 */     PlatformDependent0.putShort(address, value);
/*     */   }
/*     */   
/*     */   public static void putInt(long address, int value) {
/* 334 */     PlatformDependent0.putInt(address, value);
/*     */   }
/*     */   
/*     */   public static void putLong(long address, long value) {
/* 338 */     PlatformDependent0.putLong(address, value);
/*     */   }
/*     */   
/*     */   public static void copyMemory(long srcAddr, long dstAddr, long length) {
/* 342 */     PlatformDependent0.copyMemory(srcAddr, dstAddr, length);
/*     */   }
/*     */   
/*     */   public static void copyMemory(byte[] src, int srcIndex, long dstAddr, long length) {
/* 346 */     PlatformDependent0.copyMemory(src, ARRAY_BASE_OFFSET + srcIndex, null, dstAddr, length);
/*     */   }
/*     */   
/*     */   public static void copyMemory(long srcAddr, byte[] dst, int dstIndex, long length) {
/* 350 */     PlatformDependent0.copyMemory(null, srcAddr, dst, ARRAY_BASE_OFFSET + dstIndex, length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <U, W> AtomicReferenceFieldUpdater<U, W> newAtomicReferenceFieldUpdater(Class<U> tclass, String fieldName)
/*     */   {
/* 360 */     if (hasUnsafe()) {
/*     */       try {
/* 362 */         return PlatformDependent0.newAtomicReferenceFieldUpdater(tclass, fieldName);
/*     */       }
/*     */       catch (Throwable ignore) {}
/*     */     }
/*     */     
/* 367 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> AtomicIntegerFieldUpdater<T> newAtomicIntegerFieldUpdater(Class<?> tclass, String fieldName)
/*     */   {
/* 377 */     if (hasUnsafe()) {
/*     */       try {
/* 379 */         return PlatformDependent0.newAtomicIntegerFieldUpdater(tclass, fieldName);
/*     */       }
/*     */       catch (Throwable ignore) {}
/*     */     }
/*     */     
/* 384 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> AtomicLongFieldUpdater<T> newAtomicLongFieldUpdater(Class<?> tclass, String fieldName)
/*     */   {
/* 394 */     if (hasUnsafe()) {
/*     */       try {
/* 396 */         return PlatformDependent0.newAtomicLongFieldUpdater(tclass, fieldName);
/*     */       }
/*     */       catch (Throwable ignore) {}
/*     */     }
/*     */     
/* 401 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> Queue<T> newMpscQueue()
/*     */   {
/* 409 */     return new MpscLinkedQueue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ClassLoader getClassLoader(Class<?> clazz)
/*     */   {
/* 416 */     return PlatformDependent0.getClassLoader(clazz);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ClassLoader getContextClassLoader()
/*     */   {
/* 423 */     return PlatformDependent0.getContextClassLoader();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ClassLoader getSystemClassLoader()
/*     */   {
/* 430 */     return PlatformDependent0.getSystemClassLoader();
/*     */   }
/*     */   
/*     */   private static boolean isAndroid0() {
/*     */     boolean android;
/*     */     try {
/* 436 */       Class.forName("android.app.Application", false, getSystemClassLoader());
/* 437 */       android = true;
/*     */     }
/*     */     catch (Exception e) {
/* 440 */       android = false;
/*     */     }
/*     */     
/* 443 */     if (android) {
/* 444 */       logger.debug("Platform: Android");
/*     */     }
/* 446 */     return android;
/*     */   }
/*     */   
/*     */   private static boolean isWindows0() {
/* 450 */     boolean windows = SystemPropertyUtil.get("os.name", "").toLowerCase(Locale.US).contains("win");
/* 451 */     if (windows) {
/* 452 */       logger.debug("Platform: Windows");
/*     */     }
/* 454 */     return windows;
/*     */   }
/*     */   
/*     */   private static boolean isRoot0() {
/* 458 */     if (isWindows()) {
/* 459 */       return false;
/*     */     }
/*     */     
/* 462 */     String[] ID_COMMANDS = { "/usr/bin/id", "/bin/id", "/usr/xpg4/bin/id", "id" };
/* 463 */     Pattern UID_PATTERN = Pattern.compile("^(?:0|[1-9][0-9]*)$");
/* 464 */     String idCmd; for (idCmd : ID_COMMANDS) {
/* 465 */       Process p = null;
/* 466 */       BufferedReader in = null;
/* 467 */       String uid = null;
/*     */       try {
/* 469 */         p = Runtime.getRuntime().exec(new String[] { idCmd, "-u" });
/* 470 */         in = new BufferedReader(new InputStreamReader(p.getInputStream(), CharsetUtil.US_ASCII));
/* 471 */         uid = in.readLine();
/* 472 */         in.close();
/*     */         for (;;)
/*     */         {
/*     */           try {
/* 476 */             int exitCode = p.waitFor();
/* 477 */             if (exitCode != 0) {
/* 478 */               uid = null;
/*     */             }
/*     */           }
/*     */           catch (InterruptedException e) {}
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 489 */         if (in != null) {
/*     */           try {
/* 491 */             in.close();
/*     */           }
/*     */           catch (IOException e) {}
/*     */         }
/*     */         
/* 496 */         if (p != null) {
/*     */           try {
/* 498 */             p.destroy();
/*     */           }
/*     */           catch (Exception e) {}
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 505 */         if (uid == null) {
/*     */           continue;
/*     */         }
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 487 */         uid = null;
/*     */       } finally {
/* 489 */         if (in != null) {
/*     */           try {
/* 491 */             in.close();
/*     */           }
/*     */           catch (IOException e) {}
/*     */         }
/*     */         
/* 496 */         if (p != null) {
/*     */           try {
/* 498 */             p.destroy();
/*     */           }
/*     */           catch (Exception e) {}
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 505 */       if (UID_PATTERN.matcher(uid).matches()) {
/* 506 */         logger.debug("UID: {}", uid);
/* 507 */         return "0".equals(uid);
/*     */       }
/*     */     }
/*     */     
/* 511 */     logger.debug("Could not determine the current UID using /usr/bin/id; attempting to bind at privileged ports.");
/*     */     
/* 513 */     Pattern PERMISSION_DENIED = Pattern.compile(".*(?:denied|not.*permitted).*");
/* 514 */     for (int i = 1023; i > 0; i--) {
/* 515 */       ServerSocket ss = null;
/*     */       try {
/* 517 */         ss = new ServerSocket();
/* 518 */         ss.setReuseAddress(true);
/* 519 */         ss.bind(new InetSocketAddress(i));
/* 520 */         if (logger.isDebugEnabled()) {
/* 521 */           logger.debug("UID: 0 (succeded to bind at port {})", Integer.valueOf(i));
/*     */         }
/* 523 */         return 1;
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 527 */         String message = e.getMessage();
/* 528 */         if (message == null) {
/* 529 */           message = "";
/*     */         }
/* 531 */         message = message.toLowerCase();
/* 532 */         if (PERMISSION_DENIED.matcher(message).matches())
/*     */         {
/*     */ 
/*     */ 
/* 536 */           if (ss == null)
/*     */             break;
/* 538 */           try { ss.close();
/*     */           }
/*     */           catch (Exception e) {}
/*     */         }
/*     */       }
/*     */       finally
/*     */       {
/* 536 */         if (ss != null) {
/*     */           try {
/* 538 */             ss.close();
/*     */           }
/*     */           catch (Exception e) {}
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 546 */     logger.debug("UID: non-root (failed to bind at any privileged ports)");
/* 547 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   private static int javaVersion0()
/*     */   {
/*     */     int javaVersion;
/*     */     
/*     */     int javaVersion;
/*     */     
/* 557 */     if (isAndroid()) {
/* 558 */       javaVersion = 6;
/*     */     }
/*     */     else {
/*     */       try
/*     */       {
/* 563 */         Class.forName("java.time.Clock", false, getClassLoader(Object.class));
/* 564 */         javaVersion = 8;
/*     */ 
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/*     */         try
/*     */         {
/* 571 */           Class.forName("java.util.concurrent.LinkedTransferQueue", false, getClassLoader(BlockingQueue.class));
/* 572 */           javaVersion = 7;
/*     */ 
/*     */         }
/*     */         catch (Exception e)
/*     */         {
/*     */ 
/* 578 */           javaVersion = 6;
/*     */         }
/*     */       }
/*     */     }
/* 582 */     if (logger.isDebugEnabled()) {
/* 583 */       logger.debug("Java version: {}", Integer.valueOf(javaVersion));
/*     */     }
/* 585 */     return javaVersion;
/*     */   }
/*     */   
/*     */   private static boolean hasUnsafe0() {
/* 589 */     boolean noUnsafe = SystemPropertyUtil.getBoolean("io.netty.noUnsafe", false);
/* 590 */     logger.debug("-Dio.netty.noUnsafe: {}", Boolean.valueOf(noUnsafe));
/*     */     
/* 592 */     if (isAndroid()) {
/* 593 */       logger.debug("sun.misc.Unsafe: unavailable (Android)");
/* 594 */       return false;
/*     */     }
/*     */     
/* 597 */     if (noUnsafe) {
/* 598 */       logger.debug("sun.misc.Unsafe: unavailable (io.netty.noUnsafe)");
/* 599 */       return false;
/*     */     }
/*     */     
/*     */     boolean tryUnsafe;
/*     */     boolean tryUnsafe;
/* 604 */     if (SystemPropertyUtil.contains("io.netty.tryUnsafe")) {
/* 605 */       tryUnsafe = SystemPropertyUtil.getBoolean("io.netty.tryUnsafe", true);
/*     */     } else {
/* 607 */       tryUnsafe = SystemPropertyUtil.getBoolean("org.jboss.netty.tryUnsafe", true);
/*     */     }
/*     */     
/* 610 */     if (!tryUnsafe) {
/* 611 */       logger.debug("sun.misc.Unsafe: unavailable (io.netty.tryUnsafe/org.jboss.netty.tryUnsafe)");
/* 612 */       return false;
/*     */     }
/*     */     try
/*     */     {
/* 616 */       boolean hasUnsafe = PlatformDependent0.hasUnsafe();
/* 617 */       logger.debug("sun.misc.Unsafe: {}", hasUnsafe ? "available" : "unavailable");
/* 618 */       return hasUnsafe;
/*     */     }
/*     */     catch (Throwable t) {}
/* 621 */     return false;
/*     */   }
/*     */   
/*     */   private static long arrayBaseOffset0()
/*     */   {
/* 626 */     if (!hasUnsafe()) {
/* 627 */       return -1L;
/*     */     }
/*     */     
/* 630 */     return PlatformDependent0.arrayBaseOffset();
/*     */   }
/*     */   
/*     */   private static long maxDirectMemory0() {
/* 634 */     long maxDirectMemory = 0L;
/*     */     try
/*     */     {
/* 637 */       Class<?> vmClass = Class.forName("sun.misc.VM", true, getSystemClassLoader());
/* 638 */       Method m = vmClass.getDeclaredMethod("maxDirectMemory", new Class[0]);
/* 639 */       maxDirectMemory = ((Number)m.invoke(null, new Object[0])).longValue();
/*     */     }
/*     */     catch (Throwable t) {}
/*     */     
/*     */ 
/* 644 */     if (maxDirectMemory > 0L) {
/* 645 */       return maxDirectMemory;
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 651 */       Class<?> mgmtFactoryClass = Class.forName("java.lang.management.ManagementFactory", true, getSystemClassLoader());
/*     */       
/* 653 */       Class<?> runtimeClass = Class.forName("java.lang.management.RuntimeMXBean", true, getSystemClassLoader());
/*     */       
/*     */ 
/* 656 */       Object runtime = mgmtFactoryClass.getDeclaredMethod("getRuntimeMXBean", new Class[0]).invoke(null, new Object[0]);
/*     */       
/*     */ 
/* 659 */       List<String> vmArgs = (List)runtimeClass.getDeclaredMethod("getInputArguments", new Class[0]).invoke(runtime, new Object[0]);
/* 660 */       for (int i = vmArgs.size() - 1; i >= 0; i--) {
/* 661 */         Matcher m = MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN.matcher((CharSequence)vmArgs.get(i));
/* 662 */         if (m.matches())
/*     */         {
/*     */ 
/*     */ 
/* 666 */           maxDirectMemory = Long.parseLong(m.group(1));
/* 667 */           switch (m.group(2).charAt(0)) {
/*     */           case 'K': case 'k': 
/* 669 */             maxDirectMemory *= 1024L;
/* 670 */             break;
/*     */           case 'M': case 'm': 
/* 672 */             maxDirectMemory *= 1048576L;
/* 673 */             break;
/*     */           case 'G': case 'g': 
/* 675 */             maxDirectMemory *= 1073741824L;
/*     */           }
/*     */           
/* 678 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Throwable t) {}
/*     */     
/* 684 */     if (maxDirectMemory <= 0L) {
/* 685 */       maxDirectMemory = Runtime.getRuntime().maxMemory();
/* 686 */       logger.debug("maxDirectMemory: {} bytes (maybe)", Long.valueOf(maxDirectMemory));
/*     */     } else {
/* 688 */       logger.debug("maxDirectMemory: {} bytes", Long.valueOf(maxDirectMemory));
/*     */     }
/*     */     
/* 691 */     return maxDirectMemory;
/*     */   }
/*     */   
/*     */   private static boolean hasJavassist0() {
/* 695 */     if (isAndroid()) {
/* 696 */       return false;
/*     */     }
/*     */     
/* 699 */     boolean noJavassist = SystemPropertyUtil.getBoolean("io.netty.noJavassist", false);
/* 700 */     logger.debug("-Dio.netty.noJavassist: {}", Boolean.valueOf(noJavassist));
/*     */     
/* 702 */     if (noJavassist) {
/* 703 */       logger.debug("Javassist: unavailable (io.netty.noJavassist)");
/* 704 */       return false;
/*     */     }
/*     */     try
/*     */     {
/* 708 */       JavassistTypeParameterMatcherGenerator.generate(Object.class, getClassLoader(PlatformDependent.class));
/* 709 */       logger.debug("Javassist: available");
/* 710 */       return true;
/*     */     }
/*     */     catch (Throwable t) {
/* 713 */       logger.debug("Javassist: unavailable");
/* 714 */       logger.debug("You don't have Javassist in your class path or you don't have enough permission to load dynamically generated classes.  Please check the configuration for better performance.");
/*     */     }
/*     */     
/* 717 */     return false;
/*     */   }
/*     */   
/*     */   private static File tmpdir0()
/*     */   {
/*     */     File f;
/*     */     try {
/* 724 */       f = toDirectory(SystemPropertyUtil.get("io.netty.tmpdir"));
/* 725 */       if (f != null) {
/* 726 */         logger.debug("-Dio.netty.tmpdir: {}", f);
/* 727 */         return f;
/*     */       }
/*     */       
/* 730 */       f = toDirectory(SystemPropertyUtil.get("java.io.tmpdir"));
/* 731 */       if (f != null) {
/* 732 */         logger.debug("-Dio.netty.tmpdir: {} (java.io.tmpdir)", f);
/* 733 */         return f;
/*     */       }
/*     */       
/*     */ 
/* 737 */       if (isWindows()) {
/* 738 */         f = toDirectory(System.getenv("TEMP"));
/* 739 */         if (f != null) {
/* 740 */           logger.debug("-Dio.netty.tmpdir: {} (%TEMP%)", f);
/* 741 */           return f;
/*     */         }
/*     */         
/* 744 */         String userprofile = System.getenv("USERPROFILE");
/* 745 */         if (userprofile != null) {
/* 746 */           f = toDirectory(userprofile + "\\AppData\\Local\\Temp");
/* 747 */           if (f != null) {
/* 748 */             logger.debug("-Dio.netty.tmpdir: {} (%USERPROFILE%\\AppData\\Local\\Temp)", f);
/* 749 */             return f;
/*     */           }
/*     */           
/* 752 */           f = toDirectory(userprofile + "\\Local Settings\\Temp");
/* 753 */           if (f != null) {
/* 754 */             logger.debug("-Dio.netty.tmpdir: {} (%USERPROFILE%\\Local Settings\\Temp)", f);
/* 755 */             return f;
/*     */           }
/*     */         }
/*     */       } else {
/* 759 */         f = toDirectory(System.getenv("TMPDIR"));
/* 760 */         if (f != null) {
/* 761 */           logger.debug("-Dio.netty.tmpdir: {} ($TMPDIR)", f);
/* 762 */           return f;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception ignored) {}
/*     */     
/*     */     File f;
/*     */     
/* 770 */     if (isWindows()) {
/* 771 */       f = new File("C:\\Windows\\Temp");
/*     */     } else {
/* 773 */       f = new File("/tmp");
/*     */     }
/*     */     
/* 776 */     logger.warn("Failed to get the temporary directory; falling back to: {}", f);
/* 777 */     return f;
/*     */   }
/*     */   
/*     */   private static File toDirectory(String path)
/*     */   {
/* 782 */     if (path == null) {
/* 783 */       return null;
/*     */     }
/*     */     
/* 786 */     File f = new File(path);
/* 787 */     f.mkdirs();
/*     */     
/* 789 */     if (!f.isDirectory()) {
/* 790 */       return null;
/*     */     }
/*     */     try
/*     */     {
/* 794 */       return f.getAbsoluteFile();
/*     */     } catch (Exception ignored) {}
/* 796 */     return f;
/*     */   }
/*     */   
/*     */ 
/*     */   private static int bitMode0()
/*     */   {
/* 802 */     int bitMode = SystemPropertyUtil.getInt("io.netty.bitMode", 0);
/* 803 */     if (bitMode > 0) {
/* 804 */       logger.debug("-Dio.netty.bitMode: {}", Integer.valueOf(bitMode));
/* 805 */       return bitMode;
/*     */     }
/*     */     
/*     */ 
/* 809 */     bitMode = SystemPropertyUtil.getInt("sun.arch.data.model", 0);
/* 810 */     if (bitMode > 0) {
/* 811 */       logger.debug("-Dio.netty.bitMode: {} (sun.arch.data.model)", Integer.valueOf(bitMode));
/* 812 */       return bitMode;
/*     */     }
/* 814 */     bitMode = SystemPropertyUtil.getInt("com.ibm.vm.bitmode", 0);
/* 815 */     if (bitMode > 0) {
/* 816 */       logger.debug("-Dio.netty.bitMode: {} (com.ibm.vm.bitmode)", Integer.valueOf(bitMode));
/* 817 */       return bitMode;
/*     */     }
/*     */     
/*     */ 
/* 821 */     String arch = SystemPropertyUtil.get("os.arch", "").toLowerCase(Locale.US).trim();
/* 822 */     if (("amd64".equals(arch)) || ("x86_64".equals(arch))) {
/* 823 */       bitMode = 64;
/* 824 */     } else if (("i386".equals(arch)) || ("i486".equals(arch)) || ("i586".equals(arch)) || ("i686".equals(arch))) {
/* 825 */       bitMode = 32;
/*     */     }
/*     */     
/* 828 */     if (bitMode > 0) {
/* 829 */       logger.debug("-Dio.netty.bitMode: {} (os.arch: {})", Integer.valueOf(bitMode), arch);
/*     */     }
/*     */     
/*     */ 
/* 833 */     String vm = SystemPropertyUtil.get("java.vm.name", "").toLowerCase(Locale.US);
/* 834 */     Pattern BIT_PATTERN = Pattern.compile("([1-9][0-9]+)-?bit");
/* 835 */     Matcher m = BIT_PATTERN.matcher(vm);
/* 836 */     if (m.find()) {
/* 837 */       return Integer.parseInt(m.group(1));
/*     */     }
/* 839 */     return 64;
/*     */   }
/*     */   
/*     */   private static int addressSize0()
/*     */   {
/* 844 */     if (!hasUnsafe()) {
/* 845 */       return -1;
/*     */     }
/* 847 */     return PlatformDependent0.addressSize();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\PlatformDependent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */