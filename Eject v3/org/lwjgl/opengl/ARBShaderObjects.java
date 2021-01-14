package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class ARBShaderObjects {
    public static final int GL_PROGRAM_OBJECT_ARB = 35648;
    public static final int GL_OBJECT_TYPE_ARB = 35662;
    public static final int GL_OBJECT_SUBTYPE_ARB = 35663;
    public static final int GL_OBJECT_DELETE_STATUS_ARB = 35712;
    public static final int GL_OBJECT_COMPILE_STATUS_ARB = 35713;
    public static final int GL_OBJECT_LINK_STATUS_ARB = 35714;
    public static final int GL_OBJECT_VALIDATE_STATUS_ARB = 35715;
    public static final int GL_OBJECT_INFO_LOG_LENGTH_ARB = 35716;
    public static final int GL_OBJECT_ATTACHED_OBJECTS_ARB = 35717;
    public static final int GL_OBJECT_ACTIVE_UNIFORMS_ARB = 35718;
    public static final int GL_OBJECT_ACTIVE_UNIFORM_MAX_LENGTH_ARB = 35719;
    public static final int GL_OBJECT_SHADER_SOURCE_LENGTH_ARB = 35720;
    public static final int GL_SHADER_OBJECT_ARB = 35656;
    public static final int GL_FLOAT_VEC2_ARB = 35664;
    public static final int GL_FLOAT_VEC3_ARB = 35665;
    public static final int GL_FLOAT_VEC4_ARB = 35666;
    public static final int GL_INT_VEC2_ARB = 35667;
    public static final int GL_INT_VEC3_ARB = 35668;
    public static final int GL_INT_VEC4_ARB = 35669;
    public static final int GL_BOOL_ARB = 35670;
    public static final int GL_BOOL_VEC2_ARB = 35671;
    public static final int GL_BOOL_VEC3_ARB = 35672;
    public static final int GL_BOOL_VEC4_ARB = 35673;
    public static final int GL_FLOAT_MAT2_ARB = 35674;
    public static final int GL_FLOAT_MAT3_ARB = 35675;
    public static final int GL_FLOAT_MAT4_ARB = 35676;
    public static final int GL_SAMPLER_1D_ARB = 35677;
    public static final int GL_SAMPLER_2D_ARB = 35678;
    public static final int GL_SAMPLER_3D_ARB = 35679;
    public static final int GL_SAMPLER_CUBE_ARB = 35680;
    public static final int GL_SAMPLER_1D_SHADOW_ARB = 35681;
    public static final int GL_SAMPLER_2D_SHADOW_ARB = 35682;
    public static final int GL_SAMPLER_2D_RECT_ARB = 35683;
    public static final int GL_SAMPLER_2D_RECT_SHADOW_ARB = 35684;

    public static void glDeleteObjectARB(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDeleteObjectARB;
        BufferChecks.checkFunctionAddress(l);
        nglDeleteObjectARB(paramInt, l);
    }

    static native void nglDeleteObjectARB(int paramInt, long paramLong);

    public static int glGetHandleARB(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetHandleARB;
        BufferChecks.checkFunctionAddress(l);
        int i = nglGetHandleARB(paramInt, l);
        return i;
    }

    static native int nglGetHandleARB(int paramInt, long paramLong);

    public static void glDetachObjectARB(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDetachObjectARB;
        BufferChecks.checkFunctionAddress(l);
        nglDetachObjectARB(paramInt1, paramInt2, l);
    }

    static native void nglDetachObjectARB(int paramInt1, int paramInt2, long paramLong);

    public static int glCreateShaderObjectARB(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCreateShaderObjectARB;
        BufferChecks.checkFunctionAddress(l);
        int i = nglCreateShaderObjectARB(paramInt, l);
        return i;
    }

    static native int nglCreateShaderObjectARB(int paramInt, long paramLong);

    public static void glShaderSourceARB(int paramInt, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glShaderSourceARB;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramByteBuffer);
        nglShaderSourceARB(paramInt, 1, MemoryUtil.getAddress(paramByteBuffer), paramByteBuffer.remaining(), l);
    }

    static native void nglShaderSourceARB(int paramInt1, int paramInt2, long paramLong1, int paramInt3, long paramLong2);

    public static void glShaderSourceARB(int paramInt, CharSequence paramCharSequence) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glShaderSourceARB;
        BufferChecks.checkFunctionAddress(l);
        nglShaderSourceARB(paramInt, 1, APIUtil.getBuffer(localContextCapabilities, paramCharSequence), paramCharSequence.length(), l);
    }

    public static void glShaderSourceARB(int paramInt, CharSequence[] paramArrayOfCharSequence) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glShaderSourceARB;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkArray(paramArrayOfCharSequence);
        nglShaderSourceARB3(paramInt, paramArrayOfCharSequence.length, APIUtil.getBuffer(localContextCapabilities, paramArrayOfCharSequence), APIUtil.getLengths(localContextCapabilities, paramArrayOfCharSequence), l);
    }

    static native void nglShaderSourceARB3(int paramInt1, int paramInt2, long paramLong1, long paramLong2, long paramLong3);

    public static void glCompileShaderARB(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCompileShaderARB;
        BufferChecks.checkFunctionAddress(l);
        nglCompileShaderARB(paramInt, l);
    }

    static native void nglCompileShaderARB(int paramInt, long paramLong);

    public static int glCreateProgramObjectARB() {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCreateProgramObjectARB;
        BufferChecks.checkFunctionAddress(l);
        int i = nglCreateProgramObjectARB(l);
        return i;
    }

    static native int nglCreateProgramObjectARB(long paramLong);

    public static void glAttachObjectARB(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glAttachObjectARB;
        BufferChecks.checkFunctionAddress(l);
        nglAttachObjectARB(paramInt1, paramInt2, l);
    }

    static native void nglAttachObjectARB(int paramInt1, int paramInt2, long paramLong);

    public static void glLinkProgramARB(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glLinkProgramARB;
        BufferChecks.checkFunctionAddress(l);
        nglLinkProgramARB(paramInt, l);
    }

    static native void nglLinkProgramARB(int paramInt, long paramLong);

    public static void glUseProgramObjectARB(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUseProgramObjectARB;
        BufferChecks.checkFunctionAddress(l);
        nglUseProgramObjectARB(paramInt, l);
    }

    static native void nglUseProgramObjectARB(int paramInt, long paramLong);

    public static void glValidateProgramARB(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glValidateProgramARB;
        BufferChecks.checkFunctionAddress(l);
        nglValidateProgramARB(paramInt, l);
    }

    static native void nglValidateProgramARB(int paramInt, long paramLong);

    public static void glUniform1fARB(int paramInt, float paramFloat) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform1fARB;
        BufferChecks.checkFunctionAddress(l);
        nglUniform1fARB(paramInt, paramFloat, l);
    }

    static native void nglUniform1fARB(int paramInt, float paramFloat, long paramLong);

    public static void glUniform2fARB(int paramInt, float paramFloat1, float paramFloat2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform2fARB;
        BufferChecks.checkFunctionAddress(l);
        nglUniform2fARB(paramInt, paramFloat1, paramFloat2, l);
    }

    static native void nglUniform2fARB(int paramInt, float paramFloat1, float paramFloat2, long paramLong);

    public static void glUniform3fARB(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform3fARB;
        BufferChecks.checkFunctionAddress(l);
        nglUniform3fARB(paramInt, paramFloat1, paramFloat2, paramFloat3, l);
    }

    static native void nglUniform3fARB(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, long paramLong);

    public static void glUniform4fARB(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform4fARB;
        BufferChecks.checkFunctionAddress(l);
        nglUniform4fARB(paramInt, paramFloat1, paramFloat2, paramFloat3, paramFloat4, l);
    }

    static native void nglUniform4fARB(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);

    public static void glUniform1iARB(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform1iARB;
        BufferChecks.checkFunctionAddress(l);
        nglUniform1iARB(paramInt1, paramInt2, l);
    }

    static native void nglUniform1iARB(int paramInt1, int paramInt2, long paramLong);

    public static void glUniform2iARB(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform2iARB;
        BufferChecks.checkFunctionAddress(l);
        nglUniform2iARB(paramInt1, paramInt2, paramInt3, l);
    }

    static native void nglUniform2iARB(int paramInt1, int paramInt2, int paramInt3, long paramLong);

    public static void glUniform3iARB(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform3iARB;
        BufferChecks.checkFunctionAddress(l);
        nglUniform3iARB(paramInt1, paramInt2, paramInt3, paramInt4, l);
    }

    static native void nglUniform3iARB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);

    public static void glUniform4iARB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform4iARB;
        BufferChecks.checkFunctionAddress(l);
        nglUniform4iARB(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, l);
    }

    static native void nglUniform4iARB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);

    public static void glUniform1ARB(int paramInt, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform1fvARB;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramFloatBuffer);
        nglUniform1fvARB(paramInt, paramFloatBuffer.remaining(), MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglUniform1fvARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glUniform2ARB(int paramInt, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform2fvARB;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramFloatBuffer);
        nglUniform2fvARB(paramInt, paramFloatBuffer.remaining() & 0x1, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglUniform2fvARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glUniform3ARB(int paramInt, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform3fvARB;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramFloatBuffer);
        nglUniform3fvARB(paramFloatBuffer.remaining(), -3, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglUniform3fvARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glUniform4ARB(int paramInt, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform4fvARB;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramFloatBuffer);
        nglUniform4fvARB(paramInt, paramFloatBuffer.remaining() & 0x2, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglUniform4fvARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glUniform1ARB(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform1ivARB;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglUniform1ivARB(paramInt, paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglUniform1ivARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glUniform2ARB(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform2ivARB;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglUniform2ivARB(paramInt, paramIntBuffer.remaining() & 0x1, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglUniform2ivARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glUniform3ARB(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform3ivARB;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglUniform3ivARB(paramIntBuffer.remaining(), -3, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglUniform3ivARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glUniform4ARB(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform4ivARB;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglUniform4ivARB(paramInt, paramIntBuffer.remaining() & 0x2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglUniform4ivARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glUniformMatrix2ARB(int paramInt, boolean paramBoolean, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniformMatrix2fvARB;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramFloatBuffer);
        nglUniformMatrix2fvARB(paramInt, paramFloatBuffer.remaining() & 0x2, paramBoolean, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglUniformMatrix2fvARB(int paramInt1, int paramInt2, boolean paramBoolean, long paramLong1, long paramLong2);

    public static void glUniformMatrix3ARB(int paramInt, boolean paramBoolean, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniformMatrix3fvARB;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramFloatBuffer);
        nglUniformMatrix3fvARB(paramFloatBuffer.remaining(), -9, paramBoolean, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglUniformMatrix3fvARB(int paramInt1, int paramInt2, boolean paramBoolean, long paramLong1, long paramLong2);

    public static void glUniformMatrix4ARB(int paramInt, boolean paramBoolean, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniformMatrix4fvARB;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramFloatBuffer);
        nglUniformMatrix4fvARB(paramInt, paramFloatBuffer.remaining() & 0x4, paramBoolean, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglUniformMatrix4fvARB(int paramInt1, int paramInt2, boolean paramBoolean, long paramLong1, long paramLong2);

    public static void glGetObjectParameterARB(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetObjectParameterfvARB;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramFloatBuffer);
        nglGetObjectParameterfvARB(paramInt1, paramInt2, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglGetObjectParameterfvARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static float glGetObjectParameterfARB(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetObjectParameterfvARB;
        BufferChecks.checkFunctionAddress(l);
        FloatBuffer localFloatBuffer = APIUtil.getBufferFloat(localContextCapabilities);
        nglGetObjectParameterfvARB(paramInt1, paramInt2, MemoryUtil.getAddress(localFloatBuffer), l);
        return localFloatBuffer.get(0);
    }

    public static void glGetObjectParameterARB(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetObjectParameterivARB;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglGetObjectParameterivARB(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGetObjectParameterivARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static int glGetObjectParameteriARB(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetObjectParameterivARB;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getBufferInt(localContextCapabilities);
        nglGetObjectParameterivARB(paramInt1, paramInt2, MemoryUtil.getAddress(localIntBuffer), l);
        return localIntBuffer.get(0);
    }

    public static void glGetInfoLogARB(int paramInt, IntBuffer paramIntBuffer, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetInfoLogARB;
        BufferChecks.checkFunctionAddress(l);
        if (paramIntBuffer != null) {
            BufferChecks.checkBuffer(paramIntBuffer, 1);
        }
        BufferChecks.checkDirect(paramByteBuffer);
        nglGetInfoLogARB(paramInt, paramByteBuffer.remaining(), MemoryUtil.getAddressSafe(paramIntBuffer), MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglGetInfoLogARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2, long paramLong3);

    public static String glGetInfoLogARB(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetInfoLogARB;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getLengths(localContextCapabilities);
        ByteBuffer localByteBuffer = APIUtil.getBufferByte(localContextCapabilities, paramInt2);
        nglGetInfoLogARB(paramInt1, paramInt2, MemoryUtil.getAddress0(localIntBuffer), MemoryUtil.getAddress(localByteBuffer), l);
        localByteBuffer.limit(localIntBuffer.get(0));
        return APIUtil.getString(localContextCapabilities, localByteBuffer);
    }

    public static void glGetAttachedObjectsARB(int paramInt, IntBuffer paramIntBuffer1, IntBuffer paramIntBuffer2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetAttachedObjectsARB;
        BufferChecks.checkFunctionAddress(l);
        if (paramIntBuffer1 != null) {
            BufferChecks.checkBuffer(paramIntBuffer1, 1);
        }
        BufferChecks.checkDirect(paramIntBuffer2);
        nglGetAttachedObjectsARB(paramInt, paramIntBuffer2.remaining(), MemoryUtil.getAddressSafe(paramIntBuffer1), MemoryUtil.getAddress(paramIntBuffer2), l);
    }

    static native void nglGetAttachedObjectsARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2, long paramLong3);

    public static int glGetUniformLocationARB(int paramInt, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetUniformLocationARB;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramByteBuffer);
        BufferChecks.checkNullTerminated(paramByteBuffer);
        int i = nglGetUniformLocationARB(paramInt, MemoryUtil.getAddress(paramByteBuffer), l);
        return i;
    }

    static native int nglGetUniformLocationARB(int paramInt, long paramLong1, long paramLong2);

    public static int glGetUniformLocationARB(int paramInt, CharSequence paramCharSequence) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetUniformLocationARB;
        BufferChecks.checkFunctionAddress(l);
        int i = nglGetUniformLocationARB(paramInt, APIUtil.getBufferNT(localContextCapabilities, paramCharSequence), l);
        return i;
    }

    public static void glGetActiveUniformARB(int paramInt1, int paramInt2, IntBuffer paramIntBuffer1, IntBuffer paramIntBuffer2, IntBuffer paramIntBuffer3, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetActiveUniformARB;
        BufferChecks.checkFunctionAddress(l);
        if (paramIntBuffer1 != null) {
            BufferChecks.checkBuffer(paramIntBuffer1, 1);
        }
        BufferChecks.checkBuffer(paramIntBuffer2, 1);
        BufferChecks.checkBuffer(paramIntBuffer3, 1);
        BufferChecks.checkDirect(paramByteBuffer);
        nglGetActiveUniformARB(paramInt1, paramInt2, paramByteBuffer.remaining(), MemoryUtil.getAddressSafe(paramIntBuffer1), MemoryUtil.getAddress(paramIntBuffer2), MemoryUtil.getAddress(paramIntBuffer3), MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglGetActiveUniformARB(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5);

    public static String glGetActiveUniformARB(int paramInt1, int paramInt2, int paramInt3, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetActiveUniformARB;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 2);
        IntBuffer localIntBuffer = APIUtil.getLengths(localContextCapabilities);
        ByteBuffer localByteBuffer = APIUtil.getBufferByte(localContextCapabilities, paramInt3);
        nglGetActiveUniformARB(paramInt1, paramInt2, paramInt3, MemoryUtil.getAddress0(localIntBuffer), MemoryUtil.getAddress(paramIntBuffer), MemoryUtil.getAddress(paramIntBuffer, paramIntBuffer.position() | 0x1), MemoryUtil.getAddress(localByteBuffer), l);
        localByteBuffer.limit(localIntBuffer.get(0));
        return APIUtil.getString(localContextCapabilities, localByteBuffer);
    }

    public static String glGetActiveUniformARB(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetActiveUniformARB;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getLengths(localContextCapabilities);
        ByteBuffer localByteBuffer = APIUtil.getBufferByte(localContextCapabilities, paramInt3);
        nglGetActiveUniformARB(paramInt1, paramInt2, paramInt3, MemoryUtil.getAddress0(localIntBuffer), MemoryUtil.getAddress0(APIUtil.getBufferInt(localContextCapabilities)), MemoryUtil.getAddress(APIUtil.getBufferInt(localContextCapabilities), 1), MemoryUtil.getAddress(localByteBuffer), l);
        localByteBuffer.limit(localIntBuffer.get(0));
        return APIUtil.getString(localContextCapabilities, localByteBuffer);
    }

    public static int glGetActiveUniformSizeARB(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetActiveUniformARB;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getBufferInt(localContextCapabilities);
        nglGetActiveUniformARB(paramInt1, paramInt2, 0, 0L, MemoryUtil.getAddress(localIntBuffer), MemoryUtil.getAddress(localIntBuffer, 1), APIUtil.getBufferByte0(localContextCapabilities), l);
        return localIntBuffer.get(0);
    }

    public static int glGetActiveUniformTypeARB(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetActiveUniformARB;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getBufferInt(localContextCapabilities);
        nglGetActiveUniformARB(paramInt1, paramInt2, 0, 0L, MemoryUtil.getAddress(localIntBuffer, 1), MemoryUtil.getAddress(localIntBuffer), APIUtil.getBufferByte0(localContextCapabilities), l);
        return localIntBuffer.get(0);
    }

    public static void glGetUniformARB(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetUniformfvARB;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramFloatBuffer);
        nglGetUniformfvARB(paramInt1, paramInt2, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglGetUniformfvARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glGetUniformARB(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetUniformivARB;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglGetUniformivARB(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGetUniformivARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glGetShaderSourceARB(int paramInt, IntBuffer paramIntBuffer, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetShaderSourceARB;
        BufferChecks.checkFunctionAddress(l);
        if (paramIntBuffer != null) {
            BufferChecks.checkBuffer(paramIntBuffer, 1);
        }
        BufferChecks.checkDirect(paramByteBuffer);
        nglGetShaderSourceARB(paramInt, paramByteBuffer.remaining(), MemoryUtil.getAddressSafe(paramIntBuffer), MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglGetShaderSourceARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2, long paramLong3);

    public static String glGetShaderSourceARB(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetShaderSourceARB;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getLengths(localContextCapabilities);
        ByteBuffer localByteBuffer = APIUtil.getBufferByte(localContextCapabilities, paramInt2);
        nglGetShaderSourceARB(paramInt1, paramInt2, MemoryUtil.getAddress0(localIntBuffer), MemoryUtil.getAddress(localByteBuffer), l);
        localByteBuffer.limit(localIntBuffer.get(0));
        return APIUtil.getString(localContextCapabilities, localByteBuffer);
    }
}




