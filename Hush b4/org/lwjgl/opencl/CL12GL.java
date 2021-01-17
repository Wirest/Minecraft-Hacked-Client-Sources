// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.IntBuffer;

public final class CL12GL
{
    public static final int CL_GL_OBJECT_TEXTURE2D_ARRAY = 8206;
    public static final int CL_GL_OBJECT_TEXTURE1D = 8207;
    public static final int CL_GL_OBJECT_TEXTURE1D_ARRAY = 8208;
    public static final int CL_GL_OBJECT_TEXTURE_BUFFER = 8209;
    
    private CL12GL() {
    }
    
    public static CLMem clCreateFromGLTexture(final CLContext context, final long flags, final int target, final int miplevel, final int texture, final IntBuffer errcode_ret) {
        final long function_pointer = CLCapabilities.clCreateFromGLTexture;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (errcode_ret != null) {
            BufferChecks.checkBuffer(errcode_ret, 1);
        }
        final CLMem __result = new CLMem(nclCreateFromGLTexture(context.getPointer(), flags, target, miplevel, texture, MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
        return __result;
    }
    
    static native long nclCreateFromGLTexture(final long p0, final long p1, final int p2, final int p3, final int p4, final long p5, final long p6);
}
