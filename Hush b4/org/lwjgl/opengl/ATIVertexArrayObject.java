// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import java.nio.DoubleBuffer;
import org.lwjgl.MemoryUtil;
import java.nio.ByteBuffer;
import org.lwjgl.BufferChecks;

public final class ATIVertexArrayObject
{
    public static final int GL_STATIC_ATI = 34656;
    public static final int GL_DYNAMIC_ATI = 34657;
    public static final int GL_PRESERVE_ATI = 34658;
    public static final int GL_DISCARD_ATI = 34659;
    public static final int GL_OBJECT_BUFFER_SIZE_ATI = 34660;
    public static final int GL_OBJECT_BUFFER_USAGE_ATI = 34661;
    public static final int GL_ARRAY_OBJECT_BUFFER_ATI = 34662;
    public static final int GL_ARRAY_OBJECT_OFFSET_ATI = 34663;
    
    private ATIVertexArrayObject() {
    }
    
    public static int glNewObjectBufferATI(final int pPointer_size, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNewObjectBufferATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglNewObjectBufferATI(pPointer_size, 0L, usage, function_pointer);
        return __result;
    }
    
    public static int glNewObjectBufferATI(final ByteBuffer pPointer, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNewObjectBufferATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pPointer);
        final int __result = nglNewObjectBufferATI(pPointer.remaining(), MemoryUtil.getAddress(pPointer), usage, function_pointer);
        return __result;
    }
    
    public static int glNewObjectBufferATI(final DoubleBuffer pPointer, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNewObjectBufferATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pPointer);
        final int __result = nglNewObjectBufferATI(pPointer.remaining() << 3, MemoryUtil.getAddress(pPointer), usage, function_pointer);
        return __result;
    }
    
    public static int glNewObjectBufferATI(final FloatBuffer pPointer, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNewObjectBufferATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pPointer);
        final int __result = nglNewObjectBufferATI(pPointer.remaining() << 2, MemoryUtil.getAddress(pPointer), usage, function_pointer);
        return __result;
    }
    
    public static int glNewObjectBufferATI(final IntBuffer pPointer, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNewObjectBufferATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pPointer);
        final int __result = nglNewObjectBufferATI(pPointer.remaining() << 2, MemoryUtil.getAddress(pPointer), usage, function_pointer);
        return __result;
    }
    
    public static int glNewObjectBufferATI(final ShortBuffer pPointer, final int usage) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNewObjectBufferATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pPointer);
        final int __result = nglNewObjectBufferATI(pPointer.remaining() << 1, MemoryUtil.getAddress(pPointer), usage, function_pointer);
        return __result;
    }
    
    static native int nglNewObjectBufferATI(final int p0, final long p1, final int p2, final long p3);
    
    public static boolean glIsObjectBufferATI(final int buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsObjectBufferATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsObjectBufferATI(buffer, function_pointer);
        return __result;
    }
    
    static native boolean nglIsObjectBufferATI(final int p0, final long p1);
    
    public static void glUpdateObjectBufferATI(final int buffer, final int offset, final ByteBuffer pPointer, final int preserve) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUpdateObjectBufferATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pPointer);
        nglUpdateObjectBufferATI(buffer, offset, pPointer.remaining(), MemoryUtil.getAddress(pPointer), preserve, function_pointer);
    }
    
    public static void glUpdateObjectBufferATI(final int buffer, final int offset, final DoubleBuffer pPointer, final int preserve) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUpdateObjectBufferATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pPointer);
        nglUpdateObjectBufferATI(buffer, offset, pPointer.remaining() << 3, MemoryUtil.getAddress(pPointer), preserve, function_pointer);
    }
    
    public static void glUpdateObjectBufferATI(final int buffer, final int offset, final FloatBuffer pPointer, final int preserve) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUpdateObjectBufferATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pPointer);
        nglUpdateObjectBufferATI(buffer, offset, pPointer.remaining() << 2, MemoryUtil.getAddress(pPointer), preserve, function_pointer);
    }
    
    public static void glUpdateObjectBufferATI(final int buffer, final int offset, final IntBuffer pPointer, final int preserve) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUpdateObjectBufferATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pPointer);
        nglUpdateObjectBufferATI(buffer, offset, pPointer.remaining() << 2, MemoryUtil.getAddress(pPointer), preserve, function_pointer);
    }
    
    public static void glUpdateObjectBufferATI(final int buffer, final int offset, final ShortBuffer pPointer, final int preserve) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUpdateObjectBufferATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pPointer);
        nglUpdateObjectBufferATI(buffer, offset, pPointer.remaining() << 1, MemoryUtil.getAddress(pPointer), preserve, function_pointer);
    }
    
    static native void nglUpdateObjectBufferATI(final int p0, final int p1, final int p2, final long p3, final int p4, final long p5);
    
    public static void glGetObjectBufferATI(final int buffer, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetObjectBufferfvATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglGetObjectBufferfvATI(buffer, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetObjectBufferfvATI(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetObjectBufferATI(final int buffer, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetObjectBufferivATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglGetObjectBufferivATI(buffer, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetObjectBufferivATI(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetObjectBufferiATI(final int buffer, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetObjectBufferivATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetObjectBufferivATI(buffer, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glFreeObjectBufferATI(final int buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFreeObjectBufferATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglFreeObjectBufferATI(buffer, function_pointer);
    }
    
    static native void nglFreeObjectBufferATI(final int p0, final long p1);
    
    public static void glArrayObjectATI(final int array, final int size, final int type, final int stride, final int buffer, final int offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glArrayObjectATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglArrayObjectATI(array, size, type, stride, buffer, offset, function_pointer);
    }
    
    static native void nglArrayObjectATI(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6);
    
    public static void glGetArrayObjectATI(final int array, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetArrayObjectfvATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetArrayObjectfvATI(array, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetArrayObjectfvATI(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetArrayObjectATI(final int array, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetArrayObjectivATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetArrayObjectivATI(array, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetArrayObjectivATI(final int p0, final int p1, final long p2, final long p3);
    
    public static void glVariantArrayObjectATI(final int id, final int type, final int stride, final int buffer, final int offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVariantArrayObjectATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVariantArrayObjectATI(id, type, stride, buffer, offset, function_pointer);
    }
    
    static native void nglVariantArrayObjectATI(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glGetVariantArrayObjectATI(final int id, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVariantArrayObjectfvATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetVariantArrayObjectfvATI(id, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetVariantArrayObjectfvATI(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetVariantArrayObjectATI(final int id, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVariantArrayObjectivATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetVariantArrayObjectivATI(id, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetVariantArrayObjectivATI(final int p0, final int p1, final long p2, final long p3);
}
