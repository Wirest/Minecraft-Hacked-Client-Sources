// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;

public final class NVVertexBufferUnifiedMemory
{
    public static final int GL_VERTEX_ATTRIB_ARRAY_UNIFIED_NV = 36638;
    public static final int GL_ELEMENT_ARRAY_UNIFIED_NV = 36639;
    public static final int GL_VERTEX_ATTRIB_ARRAY_ADDRESS_NV = 36640;
    public static final int GL_TEXTURE_COORD_ARRAY_ADDRESS_NV = 36645;
    public static final int GL_VERTEX_ARRAY_ADDRESS_NV = 36641;
    public static final int GL_NORMAL_ARRAY_ADDRESS_NV = 36642;
    public static final int GL_COLOR_ARRAY_ADDRESS_NV = 36643;
    public static final int GL_INDEX_ARRAY_ADDRESS_NV = 36644;
    public static final int GL_EDGE_FLAG_ARRAY_ADDRESS_NV = 36646;
    public static final int GL_SECONDARY_COLOR_ARRAY_ADDRESS_NV = 36647;
    public static final int GL_FOG_COORD_ARRAY_ADDRESS_NV = 36648;
    public static final int GL_ELEMENT_ARRAY_ADDRESS_NV = 36649;
    public static final int GL_VERTEX_ATTRIB_ARRAY_LENGTH_NV = 36650;
    public static final int GL_TEXTURE_COORD_ARRAY_LENGTH_NV = 36655;
    public static final int GL_VERTEX_ARRAY_LENGTH_NV = 36651;
    public static final int GL_NORMAL_ARRAY_LENGTH_NV = 36652;
    public static final int GL_COLOR_ARRAY_LENGTH_NV = 36653;
    public static final int GL_INDEX_ARRAY_LENGTH_NV = 36654;
    public static final int GL_EDGE_FLAG_ARRAY_LENGTH_NV = 36656;
    public static final int GL_SECONDARY_COLOR_ARRAY_LENGTH_NV = 36657;
    public static final int GL_FOG_COORD_ARRAY_LENGTH_NV = 36658;
    public static final int GL_ELEMENT_ARRAY_LENGTH_NV = 36659;
    
    private NVVertexBufferUnifiedMemory() {
    }
    
    public static void glBufferAddressRangeNV(final int pname, final int index, final long address, final long length) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBufferAddressRangeNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBufferAddressRangeNV(pname, index, address, length, function_pointer);
    }
    
    static native void nglBufferAddressRangeNV(final int p0, final int p1, final long p2, final long p3, final long p4);
    
    public static void glVertexFormatNV(final int size, final int type, final int stride) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexFormatNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexFormatNV(size, type, stride, function_pointer);
    }
    
    static native void nglVertexFormatNV(final int p0, final int p1, final int p2, final long p3);
    
    public static void glNormalFormatNV(final int type, final int stride) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glNormalFormatNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglNormalFormatNV(type, stride, function_pointer);
    }
    
    static native void nglNormalFormatNV(final int p0, final int p1, final long p2);
    
    public static void glColorFormatNV(final int size, final int type, final int stride) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorFormatNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglColorFormatNV(size, type, stride, function_pointer);
    }
    
    static native void nglColorFormatNV(final int p0, final int p1, final int p2, final long p3);
    
    public static void glIndexFormatNV(final int type, final int stride) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIndexFormatNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglIndexFormatNV(type, stride, function_pointer);
    }
    
    static native void nglIndexFormatNV(final int p0, final int p1, final long p2);
    
    public static void glTexCoordFormatNV(final int size, final int type, final int stride) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTexCoordFormatNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglTexCoordFormatNV(size, type, stride, function_pointer);
    }
    
    static native void nglTexCoordFormatNV(final int p0, final int p1, final int p2, final long p3);
    
    public static void glEdgeFlagFormatNV(final int stride) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glEdgeFlagFormatNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglEdgeFlagFormatNV(stride, function_pointer);
    }
    
    static native void nglEdgeFlagFormatNV(final int p0, final long p1);
    
    public static void glSecondaryColorFormatNV(final int size, final int type, final int stride) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSecondaryColorFormatNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglSecondaryColorFormatNV(size, type, stride, function_pointer);
    }
    
    static native void nglSecondaryColorFormatNV(final int p0, final int p1, final int p2, final long p3);
    
    public static void glFogCoordFormatNV(final int type, final int stride) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glFogCoordFormatNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglFogCoordFormatNV(type, stride, function_pointer);
    }
    
    static native void nglFogCoordFormatNV(final int p0, final int p1, final long p2);
    
    public static void glVertexAttribFormatNV(final int index, final int size, final int type, final boolean normalized, final int stride) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribFormatNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribFormatNV(index, size, type, normalized, stride, function_pointer);
    }
    
    static native void nglVertexAttribFormatNV(final int p0, final int p1, final int p2, final boolean p3, final int p4, final long p5);
    
    public static void glVertexAttribIFormatNV(final int index, final int size, final int type, final int stride) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribIFormatNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttribIFormatNV(index, size, type, stride, function_pointer);
    }
    
    static native void nglVertexAttribIFormatNV(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glGetIntegeruNV(final int value, final int index, final LongBuffer result) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetIntegerui64i_vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(result, 1);
        nglGetIntegerui64i_vNV(value, index, MemoryUtil.getAddress(result), function_pointer);
    }
    
    static native void nglGetIntegerui64i_vNV(final int p0, final int p1, final long p2, final long p3);
    
    public static long glGetIntegerui64NV(final int value, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetIntegerui64i_vNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final LongBuffer result = APIUtil.getBufferLong(caps);
        nglGetIntegerui64i_vNV(value, index, MemoryUtil.getAddress(result), function_pointer);
        return result.get(0);
    }
}
