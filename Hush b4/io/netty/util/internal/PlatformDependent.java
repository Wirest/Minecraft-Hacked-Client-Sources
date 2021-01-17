// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal;

import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.regex.Matcher;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import io.netty.util.CharsetUtil;
import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import io.netty.util.internal.chmv8.ConcurrentHashMapV8;
import java.util.concurrent.ConcurrentMap;
import java.io.File;
import java.util.regex.Pattern;
import io.netty.util.internal.logging.InternalLogger;

public final class PlatformDependent
{
    private static final InternalLogger logger;
    private static final Pattern MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN;
    private static final boolean IS_ANDROID;
    private static final boolean IS_WINDOWS;
    private static final boolean IS_ROOT;
    private static final int JAVA_VERSION;
    private static final boolean CAN_ENABLE_TCP_NODELAY_BY_DEFAULT;
    private static final boolean HAS_UNSAFE;
    private static final boolean CAN_USE_CHM_V8;
    private static final boolean DIRECT_BUFFER_PREFERRED;
    private static final long MAX_DIRECT_MEMORY;
    private static final long ARRAY_BASE_OFFSET;
    private static final boolean HAS_JAVASSIST;
    private static final File TMPDIR;
    private static final int BIT_MODE;
    private static final int ADDRESS_SIZE;
    
    public static boolean isAndroid() {
        return PlatformDependent.IS_ANDROID;
    }
    
    public static boolean isWindows() {
        return PlatformDependent.IS_WINDOWS;
    }
    
    public static boolean isRoot() {
        return PlatformDependent.IS_ROOT;
    }
    
    public static int javaVersion() {
        return PlatformDependent.JAVA_VERSION;
    }
    
    public static boolean canEnableTcpNoDelayByDefault() {
        return PlatformDependent.CAN_ENABLE_TCP_NODELAY_BY_DEFAULT;
    }
    
    public static boolean hasUnsafe() {
        return PlatformDependent.HAS_UNSAFE;
    }
    
    public static boolean directBufferPreferred() {
        return PlatformDependent.DIRECT_BUFFER_PREFERRED;
    }
    
    public static long maxDirectMemory() {
        return PlatformDependent.MAX_DIRECT_MEMORY;
    }
    
    public static boolean hasJavassist() {
        return PlatformDependent.HAS_JAVASSIST;
    }
    
    public static File tmpdir() {
        return PlatformDependent.TMPDIR;
    }
    
    public static int bitMode() {
        return PlatformDependent.BIT_MODE;
    }
    
    public static int addressSize() {
        return PlatformDependent.ADDRESS_SIZE;
    }
    
    public static long allocateMemory(final long size) {
        return PlatformDependent0.allocateMemory(size);
    }
    
    public static void freeMemory(final long address) {
        PlatformDependent0.freeMemory(address);
    }
    
    public static void throwException(final Throwable t) {
        if (hasUnsafe()) {
            PlatformDependent0.throwException(t);
        }
        else {
            throwException0(t);
        }
    }
    
    private static <E extends Throwable> void throwException0(final Throwable t) throws E, Throwable {
        throw t;
    }
    
    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap() {
        if (PlatformDependent.CAN_USE_CHM_V8) {
            return new ConcurrentHashMapV8<K, V>();
        }
        return new ConcurrentHashMap<K, V>();
    }
    
    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(final int initialCapacity) {
        if (PlatformDependent.CAN_USE_CHM_V8) {
            return new ConcurrentHashMapV8<K, V>(initialCapacity);
        }
        return new ConcurrentHashMap<K, V>(initialCapacity);
    }
    
    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(final int initialCapacity, final float loadFactor) {
        if (PlatformDependent.CAN_USE_CHM_V8) {
            return new ConcurrentHashMapV8<K, V>(initialCapacity, loadFactor);
        }
        return new ConcurrentHashMap<K, V>(initialCapacity, loadFactor);
    }
    
    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(final int initialCapacity, final float loadFactor, final int concurrencyLevel) {
        if (PlatformDependent.CAN_USE_CHM_V8) {
            return new ConcurrentHashMapV8<K, V>(initialCapacity, loadFactor, concurrencyLevel);
        }
        return new ConcurrentHashMap<K, V>(initialCapacity, loadFactor, concurrencyLevel);
    }
    
    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(final Map<? extends K, ? extends V> map) {
        if (PlatformDependent.CAN_USE_CHM_V8) {
            return new ConcurrentHashMapV8<K, V>(map);
        }
        return new ConcurrentHashMap<K, V>(map);
    }
    
    public static void freeDirectBuffer(final ByteBuffer buffer) {
        if (hasUnsafe() && !isAndroid()) {
            PlatformDependent0.freeDirectBuffer(buffer);
        }
    }
    
    public static long directBufferAddress(final ByteBuffer buffer) {
        return PlatformDependent0.directBufferAddress(buffer);
    }
    
    public static Object getObject(final Object object, final long fieldOffset) {
        return PlatformDependent0.getObject(object, fieldOffset);
    }
    
    public static Object getObjectVolatile(final Object object, final long fieldOffset) {
        return PlatformDependent0.getObjectVolatile(object, fieldOffset);
    }
    
    public static int getInt(final Object object, final long fieldOffset) {
        return PlatformDependent0.getInt(object, fieldOffset);
    }
    
    public static long objectFieldOffset(final Field field) {
        return PlatformDependent0.objectFieldOffset(field);
    }
    
    public static byte getByte(final long address) {
        return PlatformDependent0.getByte(address);
    }
    
    public static short getShort(final long address) {
        return PlatformDependent0.getShort(address);
    }
    
    public static int getInt(final long address) {
        return PlatformDependent0.getInt(address);
    }
    
    public static long getLong(final long address) {
        return PlatformDependent0.getLong(address);
    }
    
    public static void putOrderedObject(final Object object, final long address, final Object value) {
        PlatformDependent0.putOrderedObject(object, address, value);
    }
    
    public static void putByte(final long address, final byte value) {
        PlatformDependent0.putByte(address, value);
    }
    
    public static void putShort(final long address, final short value) {
        PlatformDependent0.putShort(address, value);
    }
    
    public static void putInt(final long address, final int value) {
        PlatformDependent0.putInt(address, value);
    }
    
    public static void putLong(final long address, final long value) {
        PlatformDependent0.putLong(address, value);
    }
    
    public static void copyMemory(final long srcAddr, final long dstAddr, final long length) {
        PlatformDependent0.copyMemory(srcAddr, dstAddr, length);
    }
    
    public static void copyMemory(final byte[] src, final int srcIndex, final long dstAddr, final long length) {
        PlatformDependent0.copyMemory(src, PlatformDependent.ARRAY_BASE_OFFSET + srcIndex, null, dstAddr, length);
    }
    
    public static void copyMemory(final long srcAddr, final byte[] dst, final int dstIndex, final long length) {
        PlatformDependent0.copyMemory(null, srcAddr, dst, PlatformDependent.ARRAY_BASE_OFFSET + dstIndex, length);
    }
    
    public static <U, W> AtomicReferenceFieldUpdater<U, W> newAtomicReferenceFieldUpdater(final Class<U> tclass, final String fieldName) {
        if (hasUnsafe()) {
            try {
                return PlatformDependent0.newAtomicReferenceFieldUpdater(tclass, fieldName);
            }
            catch (Throwable t) {}
        }
        return null;
    }
    
    public static <T> AtomicIntegerFieldUpdater<T> newAtomicIntegerFieldUpdater(final Class<?> tclass, final String fieldName) {
        if (hasUnsafe()) {
            try {
                return PlatformDependent0.newAtomicIntegerFieldUpdater(tclass, fieldName);
            }
            catch (Throwable t) {}
        }
        return null;
    }
    
    public static <T> AtomicLongFieldUpdater<T> newAtomicLongFieldUpdater(final Class<?> tclass, final String fieldName) {
        if (hasUnsafe()) {
            try {
                return PlatformDependent0.newAtomicLongFieldUpdater(tclass, fieldName);
            }
            catch (Throwable t) {}
        }
        return null;
    }
    
    public static <T> Queue<T> newMpscQueue() {
        return new MpscLinkedQueue<T>();
    }
    
    public static ClassLoader getClassLoader(final Class<?> clazz) {
        return PlatformDependent0.getClassLoader(clazz);
    }
    
    public static ClassLoader getContextClassLoader() {
        return PlatformDependent0.getContextClassLoader();
    }
    
    public static ClassLoader getSystemClassLoader() {
        return PlatformDependent0.getSystemClassLoader();
    }
    
    private static boolean isAndroid0() {
        boolean android;
        try {
            Class.forName("android.app.Application", false, getSystemClassLoader());
            android = true;
        }
        catch (Exception e) {
            android = false;
        }
        if (android) {
            PlatformDependent.logger.debug("Platform: Android");
        }
        return android;
    }
    
    private static boolean isWindows0() {
        final boolean windows = SystemPropertyUtil.get("os.name", "").toLowerCase(Locale.US).contains("win");
        if (windows) {
            PlatformDependent.logger.debug("Platform: Windows");
        }
        return windows;
    }
    
    private static boolean isRoot0() {
        if (isWindows()) {
            return false;
        }
        final String[] ID_COMMANDS = { "/usr/bin/id", "/bin/id", "id", "/usr/xpg4/bin/id" };
        final Pattern UID_PATTERN = Pattern.compile("^(?:0|[1-9][0-9]*)$");
        for (final String idCmd : ID_COMMANDS) {
            Process p = null;
            BufferedReader in = null;
            String uid = null;
            try {
                p = Runtime.getRuntime().exec(new String[] { idCmd, "-u" });
                in = new BufferedReader(new InputStreamReader(p.getInputStream(), CharsetUtil.US_ASCII));
                uid = in.readLine();
                in.close();
                while (true) {
                    try {
                        final int exitCode = p.waitFor();
                        if (exitCode != 0) {
                            uid = null;
                        }
                    }
                    catch (InterruptedException e2) {
                        continue;
                    }
                    break;
                }
            }
            catch (Exception e3) {
                uid = null;
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    }
                    catch (IOException ex) {}
                }
                if (p != null) {
                    try {
                        p.destroy();
                    }
                    catch (Exception ex2) {}
                }
            }
            if (uid != null && UID_PATTERN.matcher(uid).matches()) {
                PlatformDependent.logger.debug("UID: {}", uid);
                return "0".equals(uid);
            }
        }
        PlatformDependent.logger.debug("Could not determine the current UID using /usr/bin/id; attempting to bind at privileged ports.");
        final Pattern PERMISSION_DENIED = Pattern.compile(".*(?:denied|not.*permitted).*");
        for (int i = 1023; i > 0; --i) {
            ServerSocket ss = null;
            try {
                ss = new ServerSocket();
                ss.setReuseAddress(true);
                ss.bind(new InetSocketAddress(i));
                if (PlatformDependent.logger.isDebugEnabled()) {
                    PlatformDependent.logger.debug("UID: 0 (succeded to bind at port {})", (Object)i);
                }
                return true;
            }
            catch (Exception e) {
                String message = e.getMessage();
                if (message == null) {
                    message = "";
                }
                message = message.toLowerCase();
                if (PERMISSION_DENIED.matcher(message).matches()) {}
            }
            finally {
                if (ss != null) {
                    try {
                        ss.close();
                    }
                    catch (Exception ex3) {}
                }
            }
        }
        PlatformDependent.logger.debug("UID: non-root (failed to bind at any privileged ports)");
        return false;
    }
    
    private static int javaVersion0() {
        int javaVersion;
        if (isAndroid()) {
            javaVersion = 6;
        }
        else {
            try {
                Class.forName("java.time.Clock", false, getClassLoader(Object.class));
                javaVersion = 8;
            }
            catch (Exception e) {
                try {
                    Class.forName("java.util.concurrent.LinkedTransferQueue", false, getClassLoader(BlockingQueue.class));
                    javaVersion = 7;
                }
                catch (Exception e) {
                    javaVersion = 6;
                }
            }
        }
        if (PlatformDependent.logger.isDebugEnabled()) {
            PlatformDependent.logger.debug("Java version: {}", (Object)javaVersion);
        }
        return javaVersion;
    }
    
    private static boolean hasUnsafe0() {
        final boolean noUnsafe = SystemPropertyUtil.getBoolean("io.netty.noUnsafe", false);
        PlatformDependent.logger.debug("-Dio.netty.noUnsafe: {}", (Object)noUnsafe);
        if (isAndroid()) {
            PlatformDependent.logger.debug("sun.misc.Unsafe: unavailable (Android)");
            return false;
        }
        if (noUnsafe) {
            PlatformDependent.logger.debug("sun.misc.Unsafe: unavailable (io.netty.noUnsafe)");
            return false;
        }
        boolean tryUnsafe;
        if (SystemPropertyUtil.contains("io.netty.tryUnsafe")) {
            tryUnsafe = SystemPropertyUtil.getBoolean("io.netty.tryUnsafe", true);
        }
        else {
            tryUnsafe = SystemPropertyUtil.getBoolean("org.jboss.netty.tryUnsafe", true);
        }
        if (!tryUnsafe) {
            PlatformDependent.logger.debug("sun.misc.Unsafe: unavailable (io.netty.tryUnsafe/org.jboss.netty.tryUnsafe)");
            return false;
        }
        try {
            final boolean hasUnsafe = PlatformDependent0.hasUnsafe();
            PlatformDependent.logger.debug("sun.misc.Unsafe: {}", hasUnsafe ? "available" : "unavailable");
            return hasUnsafe;
        }
        catch (Throwable t) {
            return false;
        }
    }
    
    private static long arrayBaseOffset0() {
        if (!hasUnsafe()) {
            return -1L;
        }
        return PlatformDependent0.arrayBaseOffset();
    }
    
    private static long maxDirectMemory0() {
        long maxDirectMemory = 0L;
        try {
            final Class<?> vmClass = Class.forName("sun.misc.VM", true, getSystemClassLoader());
            final Method m = vmClass.getDeclaredMethod("maxDirectMemory", (Class<?>[])new Class[0]);
            maxDirectMemory = ((Number)m.invoke(null, new Object[0])).longValue();
        }
        catch (Throwable t) {}
        if (maxDirectMemory > 0L) {
            return maxDirectMemory;
        }
        try {
            final Class<?> mgmtFactoryClass = Class.forName("java.lang.management.ManagementFactory", true, getSystemClassLoader());
            final Class<?> runtimeClass = Class.forName("java.lang.management.RuntimeMXBean", true, getSystemClassLoader());
            final Object runtime = mgmtFactoryClass.getDeclaredMethod("getRuntimeMXBean", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
            final List<String> vmArgs = (List<String>)runtimeClass.getDeclaredMethod("getInputArguments", (Class<?>[])new Class[0]).invoke(runtime, new Object[0]);
            for (int i = vmArgs.size() - 1; i >= 0; --i) {
                final Matcher j = PlatformDependent.MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN.matcher(vmArgs.get(i));
                if (j.matches()) {
                    maxDirectMemory = Long.parseLong(j.group(1));
                    switch (j.group(2).charAt(0)) {
                        case 'K':
                        case 'k': {
                            maxDirectMemory *= 1024L;
                            break;
                        }
                        case 'M':
                        case 'm': {
                            maxDirectMemory *= 1048576L;
                            break;
                        }
                        case 'G':
                        case 'g': {
                            maxDirectMemory *= 1073741824L;
                            break;
                        }
                    }
                    break;
                }
            }
        }
        catch (Throwable t2) {}
        if (maxDirectMemory <= 0L) {
            maxDirectMemory = Runtime.getRuntime().maxMemory();
            PlatformDependent.logger.debug("maxDirectMemory: {} bytes (maybe)", (Object)maxDirectMemory);
        }
        else {
            PlatformDependent.logger.debug("maxDirectMemory: {} bytes", (Object)maxDirectMemory);
        }
        return maxDirectMemory;
    }
    
    private static boolean hasJavassist0() {
        if (isAndroid()) {
            return false;
        }
        final boolean noJavassist = SystemPropertyUtil.getBoolean("io.netty.noJavassist", false);
        PlatformDependent.logger.debug("-Dio.netty.noJavassist: {}", (Object)noJavassist);
        if (noJavassist) {
            PlatformDependent.logger.debug("Javassist: unavailable (io.netty.noJavassist)");
            return false;
        }
        try {
            JavassistTypeParameterMatcherGenerator.generate(Object.class, getClassLoader(PlatformDependent.class));
            PlatformDependent.logger.debug("Javassist: available");
            return true;
        }
        catch (Throwable t) {
            PlatformDependent.logger.debug("Javassist: unavailable");
            PlatformDependent.logger.debug("You don't have Javassist in your class path or you don't have enough permission to load dynamically generated classes.  Please check the configuration for better performance.");
            return false;
        }
    }
    
    private static File tmpdir0() {
        try {
            File f = toDirectory(SystemPropertyUtil.get("io.netty.tmpdir"));
            if (f != null) {
                PlatformDependent.logger.debug("-Dio.netty.tmpdir: {}", f);
                return f;
            }
            f = toDirectory(SystemPropertyUtil.get("java.io.tmpdir"));
            if (f != null) {
                PlatformDependent.logger.debug("-Dio.netty.tmpdir: {} (java.io.tmpdir)", f);
                return f;
            }
            if (isWindows()) {
                f = toDirectory(System.getenv("TEMP"));
                if (f != null) {
                    PlatformDependent.logger.debug("-Dio.netty.tmpdir: {} (%TEMP%)", f);
                    return f;
                }
                final String userprofile = System.getenv("USERPROFILE");
                if (userprofile != null) {
                    f = toDirectory(userprofile + "\\AppData\\Local\\Temp");
                    if (f != null) {
                        PlatformDependent.logger.debug("-Dio.netty.tmpdir: {} (%USERPROFILE%\\AppData\\Local\\Temp)", f);
                        return f;
                    }
                    f = toDirectory(userprofile + "\\Local Settings\\Temp");
                    if (f != null) {
                        PlatformDependent.logger.debug("-Dio.netty.tmpdir: {} (%USERPROFILE%\\Local Settings\\Temp)", f);
                        return f;
                    }
                }
            }
            else {
                f = toDirectory(System.getenv("TMPDIR"));
                if (f != null) {
                    PlatformDependent.logger.debug("-Dio.netty.tmpdir: {} ($TMPDIR)", f);
                    return f;
                }
            }
        }
        catch (Exception ex) {}
        File f;
        if (isWindows()) {
            f = new File("C:\\Windows\\Temp");
        }
        else {
            f = new File("/tmp");
        }
        PlatformDependent.logger.warn("Failed to get the temporary directory; falling back to: {}", f);
        return f;
    }
    
    private static File toDirectory(final String path) {
        if (path == null) {
            return null;
        }
        final File f = new File(path);
        f.mkdirs();
        if (!f.isDirectory()) {
            return null;
        }
        try {
            return f.getAbsoluteFile();
        }
        catch (Exception ignored) {
            return f;
        }
    }
    
    private static int bitMode0() {
        int bitMode = SystemPropertyUtil.getInt("io.netty.bitMode", 0);
        if (bitMode > 0) {
            PlatformDependent.logger.debug("-Dio.netty.bitMode: {}", (Object)bitMode);
            return bitMode;
        }
        bitMode = SystemPropertyUtil.getInt("sun.arch.data.model", 0);
        if (bitMode > 0) {
            PlatformDependent.logger.debug("-Dio.netty.bitMode: {} (sun.arch.data.model)", (Object)bitMode);
            return bitMode;
        }
        bitMode = SystemPropertyUtil.getInt("com.ibm.vm.bitmode", 0);
        if (bitMode > 0) {
            PlatformDependent.logger.debug("-Dio.netty.bitMode: {} (com.ibm.vm.bitmode)", (Object)bitMode);
            return bitMode;
        }
        final String arch = SystemPropertyUtil.get("os.arch", "").toLowerCase(Locale.US).trim();
        if ("amd64".equals(arch) || "x86_64".equals(arch)) {
            bitMode = 64;
        }
        else if ("i386".equals(arch) || "i486".equals(arch) || "i586".equals(arch) || "i686".equals(arch)) {
            bitMode = 32;
        }
        if (bitMode > 0) {
            PlatformDependent.logger.debug("-Dio.netty.bitMode: {} (os.arch: {})", (Object)bitMode, arch);
        }
        final String vm = SystemPropertyUtil.get("java.vm.name", "").toLowerCase(Locale.US);
        final Pattern BIT_PATTERN = Pattern.compile("([1-9][0-9]+)-?bit");
        final Matcher m = BIT_PATTERN.matcher(vm);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        return 64;
    }
    
    private static int addressSize0() {
        if (!hasUnsafe()) {
            return -1;
        }
        return PlatformDependent0.addressSize();
    }
    
    private PlatformDependent() {
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(PlatformDependent.class);
        MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN = Pattern.compile("\\s*-XX:MaxDirectMemorySize\\s*=\\s*([0-9]+)\\s*([kKmMgG]?)\\s*$");
        IS_ANDROID = isAndroid0();
        IS_WINDOWS = isWindows0();
        IS_ROOT = isRoot0();
        JAVA_VERSION = javaVersion0();
        CAN_ENABLE_TCP_NODELAY_BY_DEFAULT = !isAndroid();
        HAS_UNSAFE = hasUnsafe0();
        CAN_USE_CHM_V8 = (PlatformDependent.HAS_UNSAFE && PlatformDependent.JAVA_VERSION < 8);
        DIRECT_BUFFER_PREFERRED = (PlatformDependent.HAS_UNSAFE && !SystemPropertyUtil.getBoolean("io.netty.noPreferDirect", false));
        MAX_DIRECT_MEMORY = maxDirectMemory0();
        ARRAY_BASE_OFFSET = arrayBaseOffset0();
        HAS_JAVASSIST = hasJavassist0();
        TMPDIR = tmpdir0();
        BIT_MODE = bitMode0();
        ADDRESS_SIZE = addressSize0();
        if (PlatformDependent.logger.isDebugEnabled()) {
            PlatformDependent.logger.debug("-Dio.netty.noPreferDirect: {}", (Object)!PlatformDependent.DIRECT_BUFFER_PREFERRED);
        }
        if (!hasUnsafe() && !isAndroid()) {
            PlatformDependent.logger.info("Your platform does not provide complete low-level API for accessing direct buffers reliably. Unless explicitly requested, heap buffer will always be preferred to avoid potential system unstability.");
        }
    }
}
