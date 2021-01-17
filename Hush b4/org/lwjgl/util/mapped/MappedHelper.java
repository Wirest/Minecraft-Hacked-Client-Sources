// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.mapped;

import org.lwjgl.MemoryUtil;
import org.lwjgl.LWJGLUtil;
import java.nio.ByteBuffer;

public class MappedHelper
{
    public static void setup(final MappedObject mo, final ByteBuffer buffer, final int align, final int sizeof) {
        if (LWJGLUtil.CHECKS && mo.baseAddress != 0L) {
            throw new IllegalStateException("this method should not be called by user-code");
        }
        if (LWJGLUtil.CHECKS && !buffer.isDirect()) {
            throw new IllegalArgumentException("bytebuffer must be direct");
        }
        mo.preventGC = buffer;
        if (LWJGLUtil.CHECKS && align <= 0) {
            throw new IllegalArgumentException("invalid alignment");
        }
        if (LWJGLUtil.CHECKS && (sizeof <= 0 || sizeof % align != 0)) {
            throw new IllegalStateException("sizeof not a multiple of alignment");
        }
        final long addr = MemoryUtil.getAddress(buffer);
        if (LWJGLUtil.CHECKS && addr % align != 0L) {
            throw new IllegalStateException("buffer address not aligned on " + align + " bytes");
        }
        final long n = addr;
        mo.viewAddress = n;
        mo.baseAddress = n;
    }
    
    public static void checkAddress(final long viewAddress, final MappedObject mapped) {
        mapped.checkAddress(viewAddress);
    }
    
    public static void put_views(final MappedSet2 set, final int view) {
        set.view(view);
    }
    
    public static void put_views(final MappedSet3 set, final int view) {
        set.view(view);
    }
    
    public static void put_views(final MappedSet4 set, final int view) {
        set.view(view);
    }
    
    public static void put_view(final MappedObject mapped, final int view, final int sizeof) {
        mapped.setViewAddress(mapped.baseAddress + view * sizeof);
    }
    
    public static int get_view(final MappedObject mapped, final int sizeof) {
        return (int)(mapped.viewAddress - mapped.baseAddress) / sizeof;
    }
    
    public static void put_view_shift(final MappedObject mapped, final int view, final int sizeof_shift) {
        mapped.setViewAddress(mapped.baseAddress + (view << sizeof_shift));
    }
    
    public static int get_view_shift(final MappedObject mapped, final int sizeof_shift) {
        return (int)(mapped.viewAddress - mapped.baseAddress) >> sizeof_shift;
    }
    
    public static void put_view_next(final MappedObject mapped, final int sizeof) {
        mapped.setViewAddress(mapped.viewAddress + sizeof);
    }
    
    public static MappedObject dup(final MappedObject src, final MappedObject dst) {
        dst.baseAddress = src.baseAddress;
        dst.viewAddress = src.viewAddress;
        dst.preventGC = src.preventGC;
        return dst;
    }
    
    public static MappedObject slice(final MappedObject src, final MappedObject dst) {
        dst.baseAddress = src.viewAddress;
        dst.viewAddress = src.viewAddress;
        dst.preventGC = src.preventGC;
        return dst;
    }
    
    public static void copy(final MappedObject src, final MappedObject dst, final int bytes) {
        if (MappedObject.CHECKS) {
            src.checkRange(bytes);
            dst.checkRange(bytes);
        }
        MappedObjectUnsafe.INSTANCE.copyMemory(src.viewAddress, dst.viewAddress, bytes);
    }
    
    public static ByteBuffer newBuffer(final long address, final int capacity) {
        return MappedObjectUnsafe.newBuffer(address, capacity);
    }
    
    public static void bput(final byte value, final long addr) {
        MappedObjectUnsafe.INSTANCE.putByte(addr, value);
    }
    
    public static void bput(final MappedObject mapped, final byte value, final int fieldOffset) {
        MappedObjectUnsafe.INSTANCE.putByte(mapped.viewAddress + fieldOffset, value);
    }
    
    public static byte bget(final long addr) {
        return MappedObjectUnsafe.INSTANCE.getByte(addr);
    }
    
    public static byte bget(final MappedObject mapped, final int fieldOffset) {
        return MappedObjectUnsafe.INSTANCE.getByte(mapped.viewAddress + fieldOffset);
    }
    
    public static void bvput(final byte value, final long addr) {
        MappedObjectUnsafe.INSTANCE.putByteVolatile(null, addr, value);
    }
    
    public static void bvput(final MappedObject mapped, final byte value, final int fieldOffset) {
        MappedObjectUnsafe.INSTANCE.putByteVolatile(null, mapped.viewAddress + fieldOffset, value);
    }
    
    public static byte bvget(final long addr) {
        return MappedObjectUnsafe.INSTANCE.getByteVolatile(null, addr);
    }
    
    public static byte bvget(final MappedObject mapped, final int fieldOffset) {
        return MappedObjectUnsafe.INSTANCE.getByteVolatile(null, mapped.viewAddress + fieldOffset);
    }
    
    public static void sput(final short value, final long addr) {
        MappedObjectUnsafe.INSTANCE.putShort(addr, value);
    }
    
    public static void sput(final MappedObject mapped, final short value, final int fieldOffset) {
        MappedObjectUnsafe.INSTANCE.putShort(mapped.viewAddress + fieldOffset, value);
    }
    
    public static short sget(final long addr) {
        return MappedObjectUnsafe.INSTANCE.getShort(addr);
    }
    
    public static short sget(final MappedObject mapped, final int fieldOffset) {
        return MappedObjectUnsafe.INSTANCE.getShort(mapped.viewAddress + fieldOffset);
    }
    
    public static void svput(final short value, final long addr) {
        MappedObjectUnsafe.INSTANCE.putShortVolatile(null, addr, value);
    }
    
    public static void svput(final MappedObject mapped, final short value, final int fieldOffset) {
        MappedObjectUnsafe.INSTANCE.putShortVolatile(null, mapped.viewAddress + fieldOffset, value);
    }
    
    public static short svget(final long addr) {
        return MappedObjectUnsafe.INSTANCE.getShortVolatile(null, addr);
    }
    
    public static short svget(final MappedObject mapped, final int fieldOffset) {
        return MappedObjectUnsafe.INSTANCE.getShortVolatile(null, mapped.viewAddress + fieldOffset);
    }
    
    public static void cput(final char value, final long addr) {
        MappedObjectUnsafe.INSTANCE.putChar(addr, value);
    }
    
    public static void cput(final MappedObject mapped, final char value, final int fieldOffset) {
        MappedObjectUnsafe.INSTANCE.putChar(mapped.viewAddress + fieldOffset, value);
    }
    
    public static char cget(final long addr) {
        return MappedObjectUnsafe.INSTANCE.getChar(addr);
    }
    
    public static char cget(final MappedObject mapped, final int fieldOffset) {
        return MappedObjectUnsafe.INSTANCE.getChar(mapped.viewAddress + fieldOffset);
    }
    
    public static void cvput(final char value, final long addr) {
        MappedObjectUnsafe.INSTANCE.putCharVolatile(null, addr, value);
    }
    
    public static void cvput(final MappedObject mapped, final char value, final int fieldOffset) {
        MappedObjectUnsafe.INSTANCE.putCharVolatile(null, mapped.viewAddress + fieldOffset, value);
    }
    
    public static char cvget(final long addr) {
        return MappedObjectUnsafe.INSTANCE.getCharVolatile(null, addr);
    }
    
    public static char cvget(final MappedObject mapped, final int fieldOffset) {
        return MappedObjectUnsafe.INSTANCE.getCharVolatile(null, mapped.viewAddress + fieldOffset);
    }
    
    public static void iput(final int value, final long addr) {
        MappedObjectUnsafe.INSTANCE.putInt(addr, value);
    }
    
    public static void iput(final MappedObject mapped, final int value, final int fieldOffset) {
        MappedObjectUnsafe.INSTANCE.putInt(mapped.viewAddress + fieldOffset, value);
    }
    
    public static int iget(final long address) {
        return MappedObjectUnsafe.INSTANCE.getInt(address);
    }
    
    public static int iget(final MappedObject mapped, final int fieldOffset) {
        return MappedObjectUnsafe.INSTANCE.getInt(mapped.viewAddress + fieldOffset);
    }
    
    public static void ivput(final int value, final long addr) {
        MappedObjectUnsafe.INSTANCE.putIntVolatile(null, addr, value);
    }
    
    public static void ivput(final MappedObject mapped, final int value, final int fieldOffset) {
        MappedObjectUnsafe.INSTANCE.putIntVolatile(null, mapped.viewAddress + fieldOffset, value);
    }
    
    public static int ivget(final long address) {
        return MappedObjectUnsafe.INSTANCE.getIntVolatile(null, address);
    }
    
    public static int ivget(final MappedObject mapped, final int fieldOffset) {
        return MappedObjectUnsafe.INSTANCE.getIntVolatile(null, mapped.viewAddress + fieldOffset);
    }
    
    public static void fput(final float value, final long addr) {
        MappedObjectUnsafe.INSTANCE.putFloat(addr, value);
    }
    
    public static void fput(final MappedObject mapped, final float value, final int fieldOffset) {
        MappedObjectUnsafe.INSTANCE.putFloat(mapped.viewAddress + fieldOffset, value);
    }
    
    public static float fget(final long addr) {
        return MappedObjectUnsafe.INSTANCE.getFloat(addr);
    }
    
    public static float fget(final MappedObject mapped, final int fieldOffset) {
        return MappedObjectUnsafe.INSTANCE.getFloat(mapped.viewAddress + fieldOffset);
    }
    
    public static void fvput(final float value, final long addr) {
        MappedObjectUnsafe.INSTANCE.putFloatVolatile(null, addr, value);
    }
    
    public static void fvput(final MappedObject mapped, final float value, final int fieldOffset) {
        MappedObjectUnsafe.INSTANCE.putFloatVolatile(null, mapped.viewAddress + fieldOffset, value);
    }
    
    public static float fvget(final long addr) {
        return MappedObjectUnsafe.INSTANCE.getFloatVolatile(null, addr);
    }
    
    public static float fvget(final MappedObject mapped, final int fieldOffset) {
        return MappedObjectUnsafe.INSTANCE.getFloatVolatile(null, mapped.viewAddress + fieldOffset);
    }
    
    public static void jput(final long value, final long addr) {
        MappedObjectUnsafe.INSTANCE.putLong(addr, value);
    }
    
    public static void jput(final MappedObject mapped, final long value, final int fieldOffset) {
        MappedObjectUnsafe.INSTANCE.putLong(mapped.viewAddress + fieldOffset, value);
    }
    
    public static long jget(final long addr) {
        return MappedObjectUnsafe.INSTANCE.getLong(addr);
    }
    
    public static long jget(final MappedObject mapped, final int fieldOffset) {
        return MappedObjectUnsafe.INSTANCE.getLong(mapped.viewAddress + fieldOffset);
    }
    
    public static void jvput(final long value, final long addr) {
        MappedObjectUnsafe.INSTANCE.putLongVolatile(null, addr, value);
    }
    
    public static void jvput(final MappedObject mapped, final long value, final int fieldOffset) {
        MappedObjectUnsafe.INSTANCE.putLongVolatile(null, mapped.viewAddress + fieldOffset, value);
    }
    
    public static long jvget(final long addr) {
        return MappedObjectUnsafe.INSTANCE.getLongVolatile(null, addr);
    }
    
    public static long jvget(final MappedObject mapped, final int fieldOffset) {
        return MappedObjectUnsafe.INSTANCE.getLongVolatile(null, mapped.viewAddress + fieldOffset);
    }
    
    public static void aput(final long value, final long addr) {
        MappedObjectUnsafe.INSTANCE.putAddress(addr, value);
    }
    
    public static void aput(final MappedObject mapped, final long value, final int fieldOffset) {
        MappedObjectUnsafe.INSTANCE.putAddress(mapped.viewAddress + fieldOffset, value);
    }
    
    public static long aget(final long addr) {
        return MappedObjectUnsafe.INSTANCE.getAddress(addr);
    }
    
    public static long aget(final MappedObject mapped, final int fieldOffset) {
        return MappedObjectUnsafe.INSTANCE.getAddress(mapped.viewAddress + fieldOffset);
    }
    
    public static void dput(final double value, final long addr) {
        MappedObjectUnsafe.INSTANCE.putDouble(addr, value);
    }
    
    public static void dput(final MappedObject mapped, final double value, final int fieldOffset) {
        MappedObjectUnsafe.INSTANCE.putDouble(mapped.viewAddress + fieldOffset, value);
    }
    
    public static double dget(final long addr) {
        return MappedObjectUnsafe.INSTANCE.getDouble(addr);
    }
    
    public static double dget(final MappedObject mapped, final int fieldOffset) {
        return MappedObjectUnsafe.INSTANCE.getDouble(mapped.viewAddress + fieldOffset);
    }
    
    public static void dvput(final double value, final long addr) {
        MappedObjectUnsafe.INSTANCE.putDoubleVolatile(null, addr, value);
    }
    
    public static void dvput(final MappedObject mapped, final double value, final int fieldOffset) {
        MappedObjectUnsafe.INSTANCE.putDoubleVolatile(null, mapped.viewAddress + fieldOffset, value);
    }
    
    public static double dvget(final long addr) {
        return MappedObjectUnsafe.INSTANCE.getDoubleVolatile(null, addr);
    }
    
    public static double dvget(final MappedObject mapped, final int fieldOffset) {
        return MappedObjectUnsafe.INSTANCE.getDoubleVolatile(null, mapped.viewAddress + fieldOffset);
    }
}
