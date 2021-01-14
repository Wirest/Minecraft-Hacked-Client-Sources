package io.netty.util.internal;

import io.netty.util.CharsetUtil;
import io.netty.util.internal.chmv8.ConcurrentHashMapV8;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PlatformDependent {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(PlatformDependent.class);
    private static final Pattern MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN = Pattern.compile("\\s*-XX:MaxDirectMemorySize\\s*=\\s*([0-9]+)\\s*([kKmMgG]?)\\s*$");
    private static final boolean IS_ANDROID = isAndroid0();
    private static final boolean IS_WINDOWS = isWindows0();
    private static final boolean IS_ROOT = isRoot0();
    private static final int JAVA_VERSION = javaVersion0();
    private static final boolean CAN_ENABLE_TCP_NODELAY_BY_DEFAULT = !isAndroid();
    private static final boolean HAS_UNSAFE = hasUnsafe0();
    private static final boolean CAN_USE_CHM_V8 = (HAS_UNSAFE) && (JAVA_VERSION < 8);
    private static final boolean DIRECT_BUFFER_PREFERRED = (HAS_UNSAFE) && (!SystemPropertyUtil.getBoolean("io.netty.noPreferDirect", false));
    private static final long MAX_DIRECT_MEMORY = maxDirectMemory0();
    private static final long ARRAY_BASE_OFFSET = arrayBaseOffset0();
    private static final boolean HAS_JAVASSIST = hasJavassist0();
    private static final File TMPDIR = tmpdir0();
    private static final int BIT_MODE = bitMode0();
    private static final int ADDRESS_SIZE = addressSize0();

    static {
        if (logger.isDebugEnabled()) {
            logger.debug("-Dio.netty.noPreferDirect: {}", Boolean.valueOf(!DIRECT_BUFFER_PREFERRED));
        }
        if ((!hasUnsafe()) && (!isAndroid())) {
            logger.info("Your platform does not provide complete low-level API for accessing direct buffers reliably. Unless explicitly requested, heap buffer will always be preferred to avoid potential system unstability.");
        }
    }

    public static boolean isAndroid() {
        return IS_ANDROID;
    }

    public static boolean isWindows() {
        return IS_WINDOWS;
    }

    public static boolean isRoot() {
        return IS_ROOT;
    }

    public static int javaVersion() {
        return JAVA_VERSION;
    }

    public static boolean canEnableTcpNoDelayByDefault() {
        return CAN_ENABLE_TCP_NODELAY_BY_DEFAULT;
    }

    public static boolean hasUnsafe() {
        return HAS_UNSAFE;
    }

    public static boolean directBufferPreferred() {
        return DIRECT_BUFFER_PREFERRED;
    }

    public static long maxDirectMemory() {
        return MAX_DIRECT_MEMORY;
    }

    public static boolean hasJavassist() {
        return HAS_JAVASSIST;
    }

    public static File tmpdir() {
        return TMPDIR;
    }

    public static int bitMode() {
        return BIT_MODE;
    }

    public static int addressSize() {
        return ADDRESS_SIZE;
    }

    public static long allocateMemory(long paramLong) {
        return PlatformDependent0.allocateMemory(paramLong);
    }

    public static void freeMemory(long paramLong) {
        PlatformDependent0.freeMemory(paramLong);
    }

    public static void throwException(Throwable paramThrowable) {
        if (hasUnsafe()) {
            PlatformDependent0.throwException(paramThrowable);
        } else {
            throwException0(paramThrowable);
        }
    }

    private static <E extends Throwable> void throwException0(Throwable paramThrowable)
            throws Throwable {
        throw paramThrowable;
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap() {
        if (CAN_USE_CHM_V8) {
            return new ConcurrentHashMapV8();
        }
        return new ConcurrentHashMap();
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(int paramInt) {
        if (CAN_USE_CHM_V8) {
            return new ConcurrentHashMapV8(paramInt);
        }
        return new ConcurrentHashMap(paramInt);
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(int paramInt, float paramFloat) {
        if (CAN_USE_CHM_V8) {
            return new ConcurrentHashMapV8(paramInt, paramFloat);
        }
        return new ConcurrentHashMap(paramInt, paramFloat);
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(int paramInt1, float paramFloat, int paramInt2) {
        if (CAN_USE_CHM_V8) {
            return new ConcurrentHashMapV8(paramInt1, paramFloat, paramInt2);
        }
        return new ConcurrentHashMap(paramInt1, paramFloat, paramInt2);
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(Map<? extends K, ? extends V> paramMap) {
        if (CAN_USE_CHM_V8) {
            return new ConcurrentHashMapV8(paramMap);
        }
        return new ConcurrentHashMap(paramMap);
    }

    public static void freeDirectBuffer(ByteBuffer paramByteBuffer) {
        if ((hasUnsafe()) && (!isAndroid())) {
            PlatformDependent0.freeDirectBuffer(paramByteBuffer);
        }
    }

    public static long directBufferAddress(ByteBuffer paramByteBuffer) {
        return PlatformDependent0.directBufferAddress(paramByteBuffer);
    }

    public static Object getObject(Object paramObject, long paramLong) {
        return PlatformDependent0.getObject(paramObject, paramLong);
    }

    public static Object getObjectVolatile(Object paramObject, long paramLong) {
        return PlatformDependent0.getObjectVolatile(paramObject, paramLong);
    }

    public static int getInt(Object paramObject, long paramLong) {
        return PlatformDependent0.getInt(paramObject, paramLong);
    }

    public static long objectFieldOffset(Field paramField) {
        return PlatformDependent0.objectFieldOffset(paramField);
    }

    public static byte getByte(long paramLong) {
        return PlatformDependent0.getByte(paramLong);
    }

    public static short getShort(long paramLong) {
        return PlatformDependent0.getShort(paramLong);
    }

    public static int getInt(long paramLong) {
        return PlatformDependent0.getInt(paramLong);
    }

    public static long getLong(long paramLong) {
        return PlatformDependent0.getLong(paramLong);
    }

    public static void putOrderedObject(Object paramObject1, long paramLong, Object paramObject2) {
        PlatformDependent0.putOrderedObject(paramObject1, paramLong, paramObject2);
    }

    public static void putByte(long paramLong, byte paramByte) {
        PlatformDependent0.putByte(paramLong, paramByte);
    }

    public static void putShort(long paramLong, short paramShort) {
        PlatformDependent0.putShort(paramLong, paramShort);
    }

    public static void putInt(long paramLong, int paramInt) {
        PlatformDependent0.putInt(paramLong, paramInt);
    }

    public static void putLong(long paramLong1, long paramLong2) {
        PlatformDependent0.putLong(paramLong1, paramLong2);
    }

    public static void copyMemory(long paramLong1, long paramLong2, long paramLong3) {
        PlatformDependent0.copyMemory(paramLong1, paramLong2, paramLong3);
    }

    public static void copyMemory(byte[] paramArrayOfByte, int paramInt, long paramLong1, long paramLong2) {
        PlatformDependent0.copyMemory(paramArrayOfByte, ARRAY_BASE_OFFSET + paramInt, null, paramLong1, paramLong2);
    }

    public static void copyMemory(long paramLong1, byte[] paramArrayOfByte, int paramInt, long paramLong2) {
        PlatformDependent0.copyMemory(null, paramLong1, paramArrayOfByte, ARRAY_BASE_OFFSET + paramInt, paramLong2);
    }

    public static <U, W> AtomicReferenceFieldUpdater<U, W> newAtomicReferenceFieldUpdater(Class<U> paramClass, String paramString) {
        if (hasUnsafe()) {
            try {
                return PlatformDependent0.newAtomicReferenceFieldUpdater(paramClass, paramString);
            } catch (Throwable localThrowable) {
            }
        }
        return null;
    }

    public static <T> AtomicIntegerFieldUpdater<T> newAtomicIntegerFieldUpdater(Class<?> paramClass, String paramString) {
        if (hasUnsafe()) {
            try {
                return PlatformDependent0.newAtomicIntegerFieldUpdater(paramClass, paramString);
            } catch (Throwable localThrowable) {
            }
        }
        return null;
    }

    public static <T> AtomicLongFieldUpdater<T> newAtomicLongFieldUpdater(Class<?> paramClass, String paramString) {
        if (hasUnsafe()) {
            try {
                return PlatformDependent0.newAtomicLongFieldUpdater(paramClass, paramString);
            } catch (Throwable localThrowable) {
            }
        }
        return null;
    }

    public static <T> Queue<T> newMpscQueue() {
        return new MpscLinkedQueue();
    }

    public static ClassLoader getClassLoader(Class<?> paramClass) {
        return PlatformDependent0.getClassLoader(paramClass);
    }

    public static ClassLoader getContextClassLoader() {
        return PlatformDependent0.getContextClassLoader();
    }

    public static ClassLoader getSystemClassLoader() {
        return PlatformDependent0.getSystemClassLoader();
    }

    private static boolean isAndroid0() {
        boolean bool;
        try {
            Class.forName("android.app.Application", false, getSystemClassLoader());
            bool = true;
        } catch (Exception localException) {
            bool = false;
        }
        if (bool) {
            logger.debug("Platform: Android");
        }
        return bool;
    }

    private static boolean isWindows0() {
        boolean bool = SystemPropertyUtil.get("os.name", "").toLowerCase(Locale.US).contains("win");
        if (bool) {
            logger.debug("Platform: Windows");
        }
        return bool;
    }

    private static boolean isRoot0() {
        if (isWindows()) {
            return false;
        }
        String[] arrayOfString = {"/usr/bin/id", "/bin/id", "id", "/usr/xpg4/bin/id"};
        Pattern localPattern = Pattern.compile("^(?:0|[1-9][0-9]*)$");
        for (Object localObject2 : arrayOfString) {
            Process localProcess = null;
            BufferedReader localBufferedReader = null;
            String str2 = null;
            try {
                localProcess = Runtime.getRuntime().exec(new String[]{localObject2, "-u"});
                localBufferedReader = new BufferedReader(new InputStreamReader(localProcess.getInputStream(), CharsetUtil.US_ASCII));
                str2 = localBufferedReader.readLine();
                localBufferedReader.close();
                for (; ; ) {
                    try {
                        int k = localProcess.waitFor();
                        if (k != 0) {
                            str2 = null;
                        }
                    } catch (InterruptedException localInterruptedException) {
                    }
                }
                if (localBufferedReader != null) {
                    try {
                        localBufferedReader.close();
                    } catch (IOException localIOException1) {
                    }
                }
                if (localProcess != null) {
                    try {
                        localProcess.destroy();
                    } catch (Exception localException5) {
                    }
                }
                if (str2 == null) {
                    continue;
                }
            } catch (Exception localException6) {
                str2 = null;
            } finally {
                if (localBufferedReader != null) {
                    try {
                        localBufferedReader.close();
                    } catch (IOException localIOException3) {
                    }
                }
                if (localProcess != null) {
                    try {
                        localProcess.destroy();
                    } catch (Exception localException8) {
                    }
                }
            }
            if (localPattern.matcher(str2).matches()) {
                logger.debug("UID: {}", str2);
                return "0".equals(str2);
            }
        }
        logger.debug("Could not determine the current UID using /usr/bin/id; attempting to bind at privileged ports.");
    ??? =Pattern.compile(".*(?:denied|not.*permitted).*");
        for (??? =1023; ??? >0; ???--)
        {
            ServerSocket localServerSocket = null;
            try {
                localServerSocket = new ServerSocket();
                localServerSocket.setReuseAddress(true);
                localServerSocket.bind(new InetSocketAddress( ? ??));
                if (logger.isDebugEnabled()) {
                    logger.debug("UID: 0 (succeded to bind at port {})", Integer.valueOf( ? ??));
                }
                boolean bool = true;
                return bool;
            } catch (Exception localException1) {
                String str1 = localException1.getMessage();
                if (str1 == null) {
                    str1 = "";
                }
                str1 = str1.toLowerCase();
                if (((Pattern) ? ??).matcher(str1).matches())
                {
                    if (localServerSocket == null) {
                        break;
                    }
                    try {
                        localServerSocket.close();
                    } catch (Exception localException4) {
                    }
                }
            } finally {
                if (localServerSocket != null) {
                    try {
                        localServerSocket.close();
                    } catch (Exception localException9) {
                    }
                }
            }
        }
        logger.debug("UID: non-root (failed to bind at any privileged ports)");
        return false;
    }

    private static int javaVersion0() {
        int i;
        if (isAndroid()) {
            i = 6;
        } else {
            try {
                Class.forName("java.time.Clock", false, getClassLoader(Object.class));
                i = 8;
            } catch (Exception localException1) {
                try {
                    Class.forName("java.util.concurrent.LinkedTransferQueue", false, getClassLoader(BlockingQueue.class));
                    i = 7;
                } catch (Exception localException2) {
                    i = 6;
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Java version: {}", Integer.valueOf(i));
        }
        return i;
    }

    private static boolean hasUnsafe0() {
        boolean bool1 = SystemPropertyUtil.getBoolean("io.netty.noUnsafe", false);
        logger.debug("-Dio.netty.noUnsafe: {}", Boolean.valueOf(bool1));
        if (isAndroid()) {
            logger.debug("sun.misc.Unsafe: unavailable (Android)");
            return false;
        }
        if (bool1) {
            logger.debug("sun.misc.Unsafe: unavailable (io.netty.noUnsafe)");
            return false;
        }
        boolean bool2;
        if (SystemPropertyUtil.contains("io.netty.tryUnsafe")) {
            bool2 = SystemPropertyUtil.getBoolean("io.netty.tryUnsafe", true);
        } else {
            bool2 = SystemPropertyUtil.getBoolean("org.jboss.netty.tryUnsafe", true);
        }
        if (!bool2) {
            logger.debug("sun.misc.Unsafe: unavailable (io.netty.tryUnsafe/org.jboss.netty.tryUnsafe)");
            return false;
        }
        try {
            boolean bool3 = PlatformDependent0.hasUnsafe();
            logger.debug("sun.misc.Unsafe: {}", bool3 ? "available" : "unavailable");
            return bool3;
        } catch (Throwable localThrowable) {
        }
        return false;
    }

    private static long arrayBaseOffset0() {
        if (!hasUnsafe()) {
            return -1L;
        }
        return PlatformDependent0.arrayBaseOffset();
    }

    private static long maxDirectMemory0() {
        long l = 0L;
        Object localObject1;
        try {
            Class localClass1 = Class.forName("sun.misc.VM", true, getSystemClassLoader());
            localObject1 = localClass1.getDeclaredMethod("maxDirectMemory", new Class[0]);
            l = ((Number) ((Method) localObject1).invoke(null, new Object[0])).longValue();
        } catch (Throwable localThrowable1) {
        }
        if (l > 0L) {
            return l;
        }
        try {
            Class localClass2 = Class.forName("java.lang.management.ManagementFactory", true, getSystemClassLoader());
            localObject1 = Class.forName("java.lang.management.RuntimeMXBean", true, getSystemClassLoader());
            Object localObject2 = localClass2.getDeclaredMethod("getRuntimeMXBean", new Class[0]).invoke(null, new Object[0]);
            List localList = (List) ((Class) localObject1).getDeclaredMethod("getInputArguments", new Class[0]).invoke(localObject2, new Object[0]);
            for (int i = localList.size() - 1; i >= 0; i--) {
                Matcher localMatcher = MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN.matcher((CharSequence) localList.get(i));
                if (localMatcher.matches()) {
                    l = Long.parseLong(localMatcher.group(1));
                    switch (localMatcher.group(2).charAt(0)) {
                        case 'K':
                        case 'k':
                            l *= 1024L;
                            break;
                        case 'M':
                        case 'm':
                            l *= 1048576L;
                            break;
                        case 'G':
                        case 'g':
                            l *= 1073741824L;
                    }
                    break;
                }
            }
        } catch (Throwable localThrowable2) {
        }
        if (l <= 0L) {
            l = Runtime.getRuntime().maxMemory();
            logger.debug("maxDirectMemory: {} bytes (maybe)", Long.valueOf(l));
        } else {
            logger.debug("maxDirectMemory: {} bytes", Long.valueOf(l));
        }
        return l;
    }

    private static boolean hasJavassist0() {
        if (isAndroid()) {
            return false;
        }
        boolean bool = SystemPropertyUtil.getBoolean("io.netty.noJavassist", false);
        logger.debug("-Dio.netty.noJavassist: {}", Boolean.valueOf(bool));
        if (bool) {
            logger.debug("Javassist: unavailable (io.netty.noJavassist)");
            return false;
        }
        try {
            JavassistTypeParameterMatcherGenerator.generate(Object.class, getClassLoader(PlatformDependent.class));
            logger.debug("Javassist: available");
            return true;
        } catch (Throwable localThrowable) {
            logger.debug("Javassist: unavailable");
            logger.debug("You don't have Javassist in your class path or you don't have enough permission to load dynamically generated classes.  Please check the configuration for better performance.");
        }
        return false;
    }

    private static File tmpdir0() {
        File localFile;
        try {
            localFile = toDirectory(SystemPropertyUtil.get("io.netty.tmpdir"));
            if (localFile != null) {
                logger.debug("-Dio.netty.tmpdir: {}", localFile);
                return localFile;
            }
            localFile = toDirectory(SystemPropertyUtil.get("java.io.tmpdir"));
            if (localFile != null) {
                logger.debug("-Dio.netty.tmpdir: {} (java.io.tmpdir)", localFile);
                return localFile;
            }
            if (isWindows()) {
                localFile = toDirectory(System.getenv("TEMP"));
                if (localFile != null) {
                    logger.debug("-Dio.netty.tmpdir: {} (%TEMP%)", localFile);
                    return localFile;
                }
                String str = System.getenv("USERPROFILE");
                if (str != null) {
                    localFile = toDirectory(str + "\\AppData\\Local\\Temp");
                    if (localFile != null) {
                        logger.debug("-Dio.netty.tmpdir: {} (%USERPROFILE%\\AppData\\Local\\Temp)", localFile);
                        return localFile;
                    }
                    localFile = toDirectory(str + "\\Local Settings\\Temp");
                    if (localFile != null) {
                        logger.debug("-Dio.netty.tmpdir: {} (%USERPROFILE%\\Local Settings\\Temp)", localFile);
                        return localFile;
                    }
                }
            } else {
                localFile = toDirectory(System.getenv("TMPDIR"));
                if (localFile != null) {
                    logger.debug("-Dio.netty.tmpdir: {} ($TMPDIR)", localFile);
                    return localFile;
                }
            }
        } catch (Exception localException) {
        }
        if (isWindows()) {
            localFile = new File("C:\\Windows\\Temp");
        } else {
            localFile = new File("/tmp");
        }
        logger.warn("Failed to get the temporary directory; falling back to: {}", localFile);
        return localFile;
    }

    private static File toDirectory(String paramString) {
        if (paramString == null) {
            return null;
        }
        File localFile = new File(paramString);
        localFile.mkdirs();
        if (!localFile.isDirectory()) {
            return null;
        }
        try {
            return localFile.getAbsoluteFile();
        } catch (Exception localException) {
        }
        return localFile;
    }

    private static int bitMode0() {
        int i = SystemPropertyUtil.getInt("io.netty.bitMode", 0);
        if (i > 0) {
            logger.debug("-Dio.netty.bitMode: {}", Integer.valueOf(i));
            return i;
        }
        i = SystemPropertyUtil.getInt("sun.arch.data.model", 0);
        if (i > 0) {
            logger.debug("-Dio.netty.bitMode: {} (sun.arch.data.model)", Integer.valueOf(i));
            return i;
        }
        i = SystemPropertyUtil.getInt("com.ibm.vm.bitmode", 0);
        if (i > 0) {
            logger.debug("-Dio.netty.bitMode: {} (com.ibm.vm.bitmode)", Integer.valueOf(i));
            return i;
        }
        String str1 = SystemPropertyUtil.get("os.arch", "").toLowerCase(Locale.US).trim();
        if (("amd64".equals(str1)) || ("x86_64".equals(str1))) {
            i = 64;
        } else if (("i386".equals(str1)) || ("i486".equals(str1)) || ("i586".equals(str1)) || ("i686".equals(str1))) {
            i = 32;
        }
        if (i > 0) {
            logger.debug("-Dio.netty.bitMode: {} (os.arch: {})", Integer.valueOf(i), str1);
        }
        String str2 = SystemPropertyUtil.get("java.vm.name", "").toLowerCase(Locale.US);
        Pattern localPattern = Pattern.compile("([1-9][0-9]+)-?bit");
        Matcher localMatcher = localPattern.matcher(str2);
        if (localMatcher.find()) {
            return Integer.parseInt(localMatcher.group(1));
        }
        return 64;
    }

    private static int addressSize0() {
        if (!hasUnsafe()) {
            return -1;
        }
        return PlatformDependent0.addressSize();
    }
}




