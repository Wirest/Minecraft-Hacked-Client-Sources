// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal;

import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteOrder;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import sun.misc.Unsafe;
import io.netty.util.internal.logging.InternalLogger;

final class PlatformDependent0
{
    private static final InternalLogger logger;
    private static final Unsafe UNSAFE;
    private static final boolean BIG_ENDIAN;
    private static final long ADDRESS_FIELD_OFFSET;
    private static final long UNSAFE_COPY_THRESHOLD = 1048576L;
    private static final boolean UNALIGNED;
    
    static boolean hasUnsafe() {
        return PlatformDependent0.UNSAFE != null;
    }
    
    static void throwException(final Throwable t) {
        PlatformDependent0.UNSAFE.throwException(t);
    }
    
    static void freeDirectBuffer(final ByteBuffer buffer) {
        Cleaner0.freeDirectBuffer(buffer);
    }
    
    static long directBufferAddress(final ByteBuffer buffer) {
        return getLong(buffer, PlatformDependent0.ADDRESS_FIELD_OFFSET);
    }
    
    static long arrayBaseOffset() {
        return PlatformDependent0.UNSAFE.arrayBaseOffset(byte[].class);
    }
    
    static Object getObject(final Object object, final long fieldOffset) {
        return PlatformDependent0.UNSAFE.getObject(object, fieldOffset);
    }
    
    static Object getObjectVolatile(final Object object, final long fieldOffset) {
        return PlatformDependent0.UNSAFE.getObjectVolatile(object, fieldOffset);
    }
    
    static int getInt(final Object object, final long fieldOffset) {
        return PlatformDependent0.UNSAFE.getInt(object, fieldOffset);
    }
    
    private static long getLong(final Object object, final long fieldOffset) {
        return PlatformDependent0.UNSAFE.getLong(object, fieldOffset);
    }
    
    static long objectFieldOffset(final Field field) {
        return PlatformDependent0.UNSAFE.objectFieldOffset(field);
    }
    
    static byte getByte(final long address) {
        return PlatformDependent0.UNSAFE.getByte(address);
    }
    
    static short getShort(final long address) {
        if (PlatformDependent0.UNALIGNED) {
            return PlatformDependent0.UNSAFE.getShort(address);
        }
        if (PlatformDependent0.BIG_ENDIAN) {
            return (short)(getByte(address) << 8 | (getByte(address + 1L) & 0xFF));
        }
        return (short)(getByte(address + 1L) << 8 | (getByte(address) & 0xFF));
    }
    
    static int getInt(final long address) {
        if (PlatformDependent0.UNALIGNED) {
            return PlatformDependent0.UNSAFE.getInt(address);
        }
        if (PlatformDependent0.BIG_ENDIAN) {
            return getByte(address) << 24 | (getByte(address + 1L) & 0xFF) << 16 | (getByte(address + 2L) & 0xFF) << 8 | (getByte(address + 3L) & 0xFF);
        }
        return getByte(address + 3L) << 24 | (getByte(address + 2L) & 0xFF) << 16 | (getByte(address + 1L) & 0xFF) << 8 | (getByte(address) & 0xFF);
    }
    
    static long getLong(final long address) {
        if (PlatformDependent0.UNALIGNED) {
            return PlatformDependent0.UNSAFE.getLong(address);
        }
        if (PlatformDependent0.BIG_ENDIAN) {
            return (long)getByte(address) << 56 | ((long)getByte(address + 1L) & 0xFFL) << 48 | ((long)getByte(address + 2L) & 0xFFL) << 40 | ((long)getByte(address + 3L) & 0xFFL) << 32 | ((long)getByte(address + 4L) & 0xFFL) << 24 | ((long)getByte(address + 5L) & 0xFFL) << 16 | ((long)getByte(address + 6L) & 0xFFL) << 8 | ((long)getByte(address + 7L) & 0xFFL);
        }
        return (long)getByte(address + 7L) << 56 | ((long)getByte(address + 6L) & 0xFFL) << 48 | ((long)getByte(address + 5L) & 0xFFL) << 40 | ((long)getByte(address + 4L) & 0xFFL) << 32 | ((long)getByte(address + 3L) & 0xFFL) << 24 | ((long)getByte(address + 2L) & 0xFFL) << 16 | ((long)getByte(address + 1L) & 0xFFL) << 8 | ((long)getByte(address) & 0xFFL);
    }
    
    static void putOrderedObject(final Object object, final long address, final Object value) {
        PlatformDependent0.UNSAFE.putOrderedObject(object, address, value);
    }
    
    static void putByte(final long address, final byte value) {
        PlatformDependent0.UNSAFE.putByte(address, value);
    }
    
    static void putShort(final long address, final short value) {
        if (PlatformDependent0.UNALIGNED) {
            PlatformDependent0.UNSAFE.putShort(address, value);
        }
        else if (PlatformDependent0.BIG_ENDIAN) {
            putByte(address, (byte)(value >>> 8));
            putByte(address + 1L, (byte)value);
        }
        else {
            putByte(address + 1L, (byte)(value >>> 8));
            putByte(address, (byte)value);
        }
    }
    
    static void putInt(final long address, final int value) {
        if (PlatformDependent0.UNALIGNED) {
            PlatformDependent0.UNSAFE.putInt(address, value);
        }
        else if (PlatformDependent0.BIG_ENDIAN) {
            putByte(address, (byte)(value >>> 24));
            putByte(address + 1L, (byte)(value >>> 16));
            putByte(address + 2L, (byte)(value >>> 8));
            putByte(address + 3L, (byte)value);
        }
        else {
            putByte(address + 3L, (byte)(value >>> 24));
            putByte(address + 2L, (byte)(value >>> 16));
            putByte(address + 1L, (byte)(value >>> 8));
            putByte(address, (byte)value);
        }
    }
    
    static void putLong(final long address, final long value) {
        if (PlatformDependent0.UNALIGNED) {
            PlatformDependent0.UNSAFE.putLong(address, value);
        }
        else if (PlatformDependent0.BIG_ENDIAN) {
            putByte(address, (byte)(value >>> 56));
            putByte(address + 1L, (byte)(value >>> 48));
            putByte(address + 2L, (byte)(value >>> 40));
            putByte(address + 3L, (byte)(value >>> 32));
            putByte(address + 4L, (byte)(value >>> 24));
            putByte(address + 5L, (byte)(value >>> 16));
            putByte(address + 6L, (byte)(value >>> 8));
            putByte(address + 7L, (byte)value);
        }
        else {
            putByte(address + 7L, (byte)(value >>> 56));
            putByte(address + 6L, (byte)(value >>> 48));
            putByte(address + 5L, (byte)(value >>> 40));
            putByte(address + 4L, (byte)(value >>> 32));
            putByte(address + 3L, (byte)(value >>> 24));
            putByte(address + 2L, (byte)(value >>> 16));
            putByte(address + 1L, (byte)(value >>> 8));
            putByte(address, (byte)value);
        }
    }
    
    static void copyMemory(long srcAddr, long dstAddr, long length) {
        while (length > 0L) {
            final long size = Math.min(length, 1048576L);
            PlatformDependent0.UNSAFE.copyMemory(srcAddr, dstAddr, size);
            length -= size;
            srcAddr += size;
            dstAddr += size;
        }
    }
    
    static void copyMemory(final Object src, long srcOffset, final Object dst, long dstOffset, long length) {
        while (length > 0L) {
            final long size = Math.min(length, 1048576L);
            PlatformDependent0.UNSAFE.copyMemory(src, srcOffset, dst, dstOffset, size);
            length -= size;
            srcOffset += size;
            dstOffset += size;
        }
    }
    
    static <U, W> AtomicReferenceFieldUpdater<U, W> newAtomicReferenceFieldUpdater(final Class<U> tclass, final String fieldName) throws Exception {
        return new UnsafeAtomicReferenceFieldUpdater<U, W>(PlatformDependent0.UNSAFE, tclass, fieldName);
    }
    
    static <T> AtomicIntegerFieldUpdater<T> newAtomicIntegerFieldUpdater(final Class<?> tclass, final String fieldName) throws Exception {
        return new UnsafeAtomicIntegerFieldUpdater<T>(PlatformDependent0.UNSAFE, tclass, fieldName);
    }
    
    static <T> AtomicLongFieldUpdater<T> newAtomicLongFieldUpdater(final Class<?> tclass, final String fieldName) throws Exception {
        return new UnsafeAtomicLongFieldUpdater<T>(PlatformDependent0.UNSAFE, tclass, fieldName);
    }
    
    static ClassLoader getClassLoader(final Class<?> clazz) {
        if (System.getSecurityManager() == null) {
            return clazz.getClassLoader();
        }
        return AccessController.doPrivileged((PrivilegedAction<ClassLoader>)new PrivilegedAction<ClassLoader>() {
            @Override
            public ClassLoader run() {
                return clazz.getClassLoader();
            }
        });
    }
    
    static ClassLoader getContextClassLoader() {
        if (System.getSecurityManager() == null) {
            return Thread.currentThread().getContextClassLoader();
        }
        return AccessController.doPrivileged((PrivilegedAction<ClassLoader>)new PrivilegedAction<ClassLoader>() {
            @Override
            public ClassLoader run() {
                return Thread.currentThread().getContextClassLoader();
            }
        });
    }
    
    static ClassLoader getSystemClassLoader() {
        if (System.getSecurityManager() == null) {
            return ClassLoader.getSystemClassLoader();
        }
        return AccessController.doPrivileged((PrivilegedAction<ClassLoader>)new PrivilegedAction<ClassLoader>() {
            @Override
            public ClassLoader run() {
                return ClassLoader.getSystemClassLoader();
            }
        });
    }
    
    static int addressSize() {
        return PlatformDependent0.UNSAFE.addressSize();
    }
    
    static long allocateMemory(final long size) {
        return PlatformDependent0.UNSAFE.allocateMemory(size);
    }
    
    static void freeMemory(final long address) {
        PlatformDependent0.UNSAFE.freeMemory(address);
    }
    
    private PlatformDependent0() {
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(PlatformDependent0.class);
        BIG_ENDIAN = (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN);
        final ByteBuffer direct = ByteBuffer.allocateDirect(1);
        Field addressField;
        try {
            addressField = Buffer.class.getDeclaredField("address");
            addressField.setAccessible(true);
            if (addressField.getLong(ByteBuffer.allocate(1)) != 0L) {
                addressField = null;
            }
            else if (addressField.getLong(direct) == 0L) {
                addressField = null;
            }
        }
        catch (Throwable t2) {
            addressField = null;
        }
        PlatformDependent0.logger.debug("java.nio.Buffer.address: {}", (addressField != null) ? "available" : "unavailable");
        Unsafe unsafe;
        if (addressField != null) {
            try {
                final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
                unsafeField.setAccessible(true);
                unsafe = (Unsafe)unsafeField.get(null);
                PlatformDependent0.logger.debug("sun.misc.Unsafe.theUnsafe: {}", (unsafe != null) ? "available" : "unavailable");
                try {
                    if (unsafe != null) {
                        unsafe.getClass().getDeclaredMethod("copyMemory", Object.class, Long.TYPE, Object.class, Long.TYPE, Long.TYPE);
                        PlatformDependent0.logger.debug("sun.misc.Unsafe.copyMemory: available");
                    }
                }
                catch (NoSuchMethodError t) {
                    PlatformDependent0.logger.debug("sun.misc.Unsafe.copyMemory: unavailable");
                    throw t;
                }
                catch (NoSuchMethodException e) {
                    PlatformDependent0.logger.debug("sun.misc.Unsafe.copyMemory: unavailable");
                    throw e;
                }
            }
            catch (Throwable cause) {
                unsafe = null;
            }
        }
        else {
            unsafe = null;
        }
        UNSAFE = unsafe;
        if (unsafe == null) {
            ADDRESS_FIELD_OFFSET = -1L;
            UNALIGNED = false;
        }
        else {
            ADDRESS_FIELD_OFFSET = objectFieldOffset(addressField);
            boolean unaligned;
            try {
                final Class<?> bitsClass = Class.forName("java.nio.Bits", false, ClassLoader.getSystemClassLoader());
                final Method unalignedMethod = bitsClass.getDeclaredMethod("unaligned", (Class<?>[])new Class[0]);
                unalignedMethod.setAccessible(true);
                unaligned = Boolean.TRUE.equals(unalignedMethod.invoke(null, new Object[0]));
            }
            catch (Throwable t3) {
                final String arch = SystemPropertyUtil.get("os.arch", "");
                unaligned = arch.matches("^(i[3-6]86|x86(_64)?|x64|amd64)$");
            }
            UNALIGNED = unaligned;
            PlatformDependent0.logger.debug("java.nio.Bits.unaligned: {}", (Object)PlatformDependent0.UNALIGNED);
        }
    }
}
