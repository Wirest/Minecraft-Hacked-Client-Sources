// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.mapped;

import org.lwjgl.LWJGLUtil;
import org.lwjgl.PointerBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.LongBuffer;
import java.nio.IntBuffer;
import java.nio.CharBuffer;
import java.nio.ShortBuffer;
import java.nio.ByteOrder;
import org.lwjgl.MemoryUtil;
import java.nio.ByteBuffer;

public final class CacheUtil
{
    private static final int CACHE_LINE_SIZE;
    
    private CacheUtil() {
    }
    
    public static int getCacheLineSize() {
        return CacheUtil.CACHE_LINE_SIZE;
    }
    
    public static ByteBuffer createByteBuffer(final int size) {
        final ByteBuffer buffer = ByteBuffer.allocateDirect(size + CacheUtil.CACHE_LINE_SIZE);
        if (MemoryUtil.getAddress(buffer) % CacheUtil.CACHE_LINE_SIZE != 0L) {
            buffer.position(CacheUtil.CACHE_LINE_SIZE - (int)(MemoryUtil.getAddress(buffer) & (long)(CacheUtil.CACHE_LINE_SIZE - 1)));
        }
        buffer.limit(buffer.position() + size);
        return buffer.slice().order(ByteOrder.nativeOrder());
    }
    
    public static ShortBuffer createShortBuffer(final int size) {
        return createByteBuffer(size << 1).asShortBuffer();
    }
    
    public static CharBuffer createCharBuffer(final int size) {
        return createByteBuffer(size << 1).asCharBuffer();
    }
    
    public static IntBuffer createIntBuffer(final int size) {
        return createByteBuffer(size << 2).asIntBuffer();
    }
    
    public static LongBuffer createLongBuffer(final int size) {
        return createByteBuffer(size << 3).asLongBuffer();
    }
    
    public static FloatBuffer createFloatBuffer(final int size) {
        return createByteBuffer(size << 2).asFloatBuffer();
    }
    
    public static DoubleBuffer createDoubleBuffer(final int size) {
        return createByteBuffer(size << 3).asDoubleBuffer();
    }
    
    public static PointerBuffer createPointerBuffer(final int size) {
        return new PointerBuffer(createByteBuffer(size * PointerBuffer.getPointerSize()));
    }
    
    static {
        final Integer size = LWJGLUtil.getPrivilegedInteger("org.lwjgl.util.mapped.CacheLineSize");
        if (size != null) {
            if (size < 1) {
                throw new IllegalStateException("Invalid CacheLineSize specified: " + size);
            }
            CACHE_LINE_SIZE = size;
        }
        else if (Runtime.getRuntime().availableProcessors() == 1) {
            if (LWJGLUtil.DEBUG) {
                LWJGLUtil.log("Cannot detect cache line size on single-core CPUs, assuming 64 bytes.");
            }
            CACHE_LINE_SIZE = 64;
        }
        else {
            CACHE_LINE_SIZE = CacheLineSize.getCacheLineSize();
        }
    }
}
