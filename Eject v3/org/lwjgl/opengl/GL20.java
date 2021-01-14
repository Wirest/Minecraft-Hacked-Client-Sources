package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerBuffer;

import java.nio.*;

public final class GL20 {
    public static final int GL_SHADING_LANGUAGE_VERSION = 35724;
    public static final int GL_CURRENT_PROGRAM = 35725;
    public static final int GL_SHADER_TYPE = 35663;
    public static final int GL_DELETE_STATUS = 35712;
    public static final int GL_COMPILE_STATUS = 35713;
    public static final int GL_LINK_STATUS = 35714;
    public static final int GL_VALIDATE_STATUS = 35715;
    public static final int GL_INFO_LOG_LENGTH = 35716;
    public static final int GL_ATTACHED_SHADERS = 35717;
    public static final int GL_ACTIVE_UNIFORMS = 35718;
    public static final int GL_ACTIVE_UNIFORM_MAX_LENGTH = 35719;
    public static final int GL_ACTIVE_ATTRIBUTES = 35721;
    public static final int GL_ACTIVE_ATTRIBUTE_MAX_LENGTH = 35722;
    public static final int GL_SHADER_SOURCE_LENGTH = 35720;
    public static final int GL_SHADER_OBJECT = 35656;
    public static final int GL_FLOAT_VEC2 = 35664;
    public static final int GL_FLOAT_VEC3 = 35665;
    public static final int GL_FLOAT_VEC4 = 35666;
    public static final int GL_INT_VEC2 = 35667;
    public static final int GL_INT_VEC3 = 35668;
    public static final int GL_INT_VEC4 = 35669;
    public static final int GL_BOOL = 35670;
    public static final int GL_BOOL_VEC2 = 35671;
    public static final int GL_BOOL_VEC3 = 35672;
    public static final int GL_BOOL_VEC4 = 35673;
    public static final int GL_FLOAT_MAT2 = 35674;
    public static final int GL_FLOAT_MAT3 = 35675;
    public static final int GL_FLOAT_MAT4 = 35676;
    public static final int GL_SAMPLER_1D = 35677;
    public static final int GL_SAMPLER_2D = 35678;
    public static final int GL_SAMPLER_3D = 35679;
    public static final int GL_SAMPLER_CUBE = 35680;
    public static final int GL_SAMPLER_1D_SHADOW = 35681;
    public static final int GL_SAMPLER_2D_SHADOW = 35682;
    public static final int GL_VERTEX_SHADER = 35633;
    public static final int GL_MAX_VERTEX_UNIFORM_COMPONENTS = 35658;
    public static final int GL_MAX_VARYING_FLOATS = 35659;
    public static final int GL_MAX_VERTEX_ATTRIBS = 34921;
    public static final int GL_MAX_TEXTURE_IMAGE_UNITS = 34930;
    public static final int GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS = 35660;
    public static final int GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS = 35661;
    public static final int GL_MAX_TEXTURE_COORDS = 34929;
    public static final int GL_VERTEX_PROGRAM_POINT_SIZE = 34370;
    public static final int GL_VERTEX_PROGRAM_TWO_SIDE = 34371;
    public static final int GL_VERTEX_ATTRIB_ARRAY_ENABLED = 34338;
    public static final int GL_VERTEX_ATTRIB_ARRAY_SIZE = 34339;
    public static final int GL_VERTEX_ATTRIB_ARRAY_STRIDE = 34340;
    public static final int GL_VERTEX_ATTRIB_ARRAY_TYPE = 34341;
    public static final int GL_VERTEX_ATTRIB_ARRAY_NORMALIZED = 34922;
    public static final int GL_CURRENT_VERTEX_ATTRIB = 34342;
    public static final int GL_VERTEX_ATTRIB_ARRAY_POINTER = 34373;
    public static final int GL_FRAGMENT_SHADER = 35632;
    public static final int GL_MAX_FRAGMENT_UNIFORM_COMPONENTS = 35657;
    public static final int GL_FRAGMENT_SHADER_DERIVATIVE_HINT = 35723;
    public static final int GL_MAX_DRAW_BUFFERS = 34852;
    public static final int GL_DRAW_BUFFER0 = 34853;
    public static final int GL_DRAW_BUFFER1 = 34854;
    public static final int GL_DRAW_BUFFER2 = 34855;
    public static final int GL_DRAW_BUFFER3 = 34856;
    public static final int GL_DRAW_BUFFER4 = 34857;
    public static final int GL_DRAW_BUFFER5 = 34858;
    public static final int GL_DRAW_BUFFER6 = 34859;
    public static final int GL_DRAW_BUFFER7 = 34860;
    public static final int GL_DRAW_BUFFER8 = 34861;
    public static final int GL_DRAW_BUFFER9 = 34862;
    public static final int GL_DRAW_BUFFER10 = 34863;
    public static final int GL_DRAW_BUFFER11 = 34864;
    public static final int GL_DRAW_BUFFER12 = 34865;
    public static final int GL_DRAW_BUFFER13 = 34866;
    public static final int GL_DRAW_BUFFER14 = 34867;
    public static final int GL_DRAW_BUFFER15 = 34868;
    public static final int GL_POINT_SPRITE = 34913;
    public static final int GL_COORD_REPLACE = 34914;
    public static final int GL_POINT_SPRITE_COORD_ORIGIN = 36000;
    public static final int GL_LOWER_LEFT = 36001;
    public static final int GL_UPPER_LEFT = 36002;
    public static final int GL_STENCIL_BACK_FUNC = 34816;
    public static final int GL_STENCIL_BACK_FAIL = 34817;
    public static final int GL_STENCIL_BACK_PASS_DEPTH_FAIL = 34818;
    public static final int GL_STENCIL_BACK_PASS_DEPTH_PASS = 34819;
    public static final int GL_STENCIL_BACK_REF = 36003;
    public static final int GL_STENCIL_BACK_VALUE_MASK = 36004;
    public static final int GL_STENCIL_BACK_WRITEMASK = 36005;
    public static final int GL_BLEND_EQUATION_RGB = 32777;
    public static final int GL_BLEND_EQUATION_ALPHA = 34877;

    public static void glShaderSource(int paramInt, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glShaderSource;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramByteBuffer);
        nglShaderSource(paramInt, 1, MemoryUtil.getAddress(paramByteBuffer), paramByteBuffer.remaining(), l);
    }

    static native void nglShaderSource(int paramInt1, int paramInt2, long paramLong1, int paramInt3, long paramLong2);

    public static void glShaderSource(int paramInt, CharSequence paramCharSequence) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glShaderSource;
        BufferChecks.checkFunctionAddress(l);
        nglShaderSource(paramInt, 1, APIUtil.getBuffer(localContextCapabilities, paramCharSequence), paramCharSequence.length(), l);
    }

    public static void glShaderSource(int paramInt, CharSequence[] paramArrayOfCharSequence) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glShaderSource;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkArray(paramArrayOfCharSequence);
        nglShaderSource3(paramInt, paramArrayOfCharSequence.length, APIUtil.getBuffer(localContextCapabilities, paramArrayOfCharSequence), APIUtil.getLengths(localContextCapabilities, paramArrayOfCharSequence), l);
    }

    static native void nglShaderSource3(int paramInt1, int paramInt2, long paramLong1, long paramLong2, long paramLong3);

    public static int glCreateShader(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCreateShader;
        BufferChecks.checkFunctionAddress(l);
        int i = nglCreateShader(paramInt, l);
        return i;
    }

    static native int nglCreateShader(int paramInt, long paramLong);

    public static boolean glIsShader(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glIsShader;
        BufferChecks.checkFunctionAddress(l);
        boolean bool = nglIsShader(paramInt, l);
        return bool;
    }

    static native boolean nglIsShader(int paramInt, long paramLong);

    public static void glCompileShader(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCompileShader;
        BufferChecks.checkFunctionAddress(l);
        nglCompileShader(paramInt, l);
    }

    static native void nglCompileShader(int paramInt, long paramLong);

    public static void glDeleteShader(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDeleteShader;
        BufferChecks.checkFunctionAddress(l);
        nglDeleteShader(paramInt, l);
    }

    static native void nglDeleteShader(int paramInt, long paramLong);

    public static int glCreateProgram() {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCreateProgram;
        BufferChecks.checkFunctionAddress(l);
        int i = nglCreateProgram(l);
        return i;
    }

    static native int nglCreateProgram(long paramLong);

    public static boolean glIsProgram(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glIsProgram;
        BufferChecks.checkFunctionAddress(l);
        boolean bool = nglIsProgram(paramInt, l);
        return bool;
    }

    static native boolean nglIsProgram(int paramInt, long paramLong);

    public static void glAttachShader(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glAttachShader;
        BufferChecks.checkFunctionAddress(l);
        nglAttachShader(paramInt1, paramInt2, l);
    }

    static native void nglAttachShader(int paramInt1, int paramInt2, long paramLong);

    public static void glDetachShader(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDetachShader;
        BufferChecks.checkFunctionAddress(l);
        nglDetachShader(paramInt1, paramInt2, l);
    }

    static native void nglDetachShader(int paramInt1, int paramInt2, long paramLong);

    public static void glLinkProgram(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glLinkProgram;
        BufferChecks.checkFunctionAddress(l);
        nglLinkProgram(paramInt, l);
    }

    static native void nglLinkProgram(int paramInt, long paramLong);

    public static void glUseProgram(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUseProgram;
        BufferChecks.checkFunctionAddress(l);
        nglUseProgram(paramInt, l);
    }

    static native void nglUseProgram(int paramInt, long paramLong);

    public static void glValidateProgram(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glValidateProgram;
        BufferChecks.checkFunctionAddress(l);
        nglValidateProgram(paramInt, l);
    }

    static native void nglValidateProgram(int paramInt, long paramLong);

    public static void glDeleteProgram(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDeleteProgram;
        BufferChecks.checkFunctionAddress(l);
        nglDeleteProgram(paramInt, l);
    }

    static native void nglDeleteProgram(int paramInt, long paramLong);

    public static void glUniform1f(int paramInt, float paramFloat) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform1f;
        BufferChecks.checkFunctionAddress(l);
        nglUniform1f(paramInt, paramFloat, l);
    }

    static native void nglUniform1f(int paramInt, float paramFloat, long paramLong);

    public static void glUniform2f(int paramInt, float paramFloat1, float paramFloat2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform2f;
        BufferChecks.checkFunctionAddress(l);
        nglUniform2f(paramInt, paramFloat1, paramFloat2, l);
    }

    static native void nglUniform2f(int paramInt, float paramFloat1, float paramFloat2, long paramLong);

    public static void glUniform3f(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform3f;
        BufferChecks.checkFunctionAddress(l);
        nglUniform3f(paramInt, paramFloat1, paramFloat2, paramFloat3, l);
    }

    static native void nglUniform3f(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, long paramLong);

    public static void glUniform4f(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform4f;
        BufferChecks.checkFunctionAddress(l);
        nglUniform4f(paramInt, paramFloat1, paramFloat2, paramFloat3, paramFloat4, l);
    }

    static native void nglUniform4f(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);

    public static void glUniform1i(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform1i;
        BufferChecks.checkFunctionAddress(l);
        nglUniform1i(paramInt1, paramInt2, l);
    }

    static native void nglUniform1i(int paramInt1, int paramInt2, long paramLong);

    public static void glUniform2i(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform2i;
        BufferChecks.checkFunctionAddress(l);
        nglUniform2i(paramInt1, paramInt2, paramInt3, l);
    }

    static native void nglUniform2i(int paramInt1, int paramInt2, int paramInt3, long paramLong);

    public static void glUniform3i(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform3i;
        BufferChecks.checkFunctionAddress(l);
        nglUniform3i(paramInt1, paramInt2, paramInt3, paramInt4, l);
    }

    static native void nglUniform3i(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);

    public static void glUniform4i(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform4i;
        BufferChecks.checkFunctionAddress(l);
        nglUniform4i(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, l);
    }

    static native void nglUniform4i(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);

    public static void glUniform1(int paramInt, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform1fv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramFloatBuffer);
        nglUniform1fv(paramInt, paramFloatBuffer.remaining(), MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglUniform1fv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glUniform2(int paramInt, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform2fv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramFloatBuffer);
        nglUniform2fv(paramInt, paramFloatBuffer.remaining() & 0x1, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglUniform2fv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glUniform3(int paramInt, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform3fv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramFloatBuffer);
        nglUniform3fv(paramFloatBuffer.remaining(), -3, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglUniform3fv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glUniform4(int paramInt, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform4fv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramFloatBuffer);
        nglUniform4fv(paramInt, paramFloatBuffer.remaining() & 0x2, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglUniform4fv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glUniform1(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform1iv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglUniform1iv(paramInt, paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglUniform1iv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glUniform2(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform2iv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglUniform2iv(paramInt, paramIntBuffer.remaining() & 0x1, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglUniform2iv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glUniform3(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform3iv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglUniform3iv(paramIntBuffer.remaining(), -3, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglUniform3iv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glUniform4(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniform4iv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglUniform4iv(paramInt, paramIntBuffer.remaining() & 0x2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglUniform4iv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glUniformMatrix2(int paramInt, boolean paramBoolean, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniformMatrix2fv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramFloatBuffer);
        nglUniformMatrix2fv(paramInt, paramFloatBuffer.remaining() & 0x2, paramBoolean, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglUniformMatrix2fv(int paramInt1, int paramInt2, boolean paramBoolean, long paramLong1, long paramLong2);

    public static void glUniformMatrix3(int paramInt, boolean paramBoolean, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniformMatrix3fv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramFloatBuffer);
        nglUniformMatrix3fv(paramFloatBuffer.remaining(), -9, paramBoolean, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglUniformMatrix3fv(int paramInt1, int paramInt2, boolean paramBoolean, long paramLong1, long paramLong2);

    public static void glUniformMatrix4(int paramInt, boolean paramBoolean, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glUniformMatrix4fv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramFloatBuffer);
        nglUniformMatrix4fv(paramInt, paramFloatBuffer.remaining() & 0x4, paramBoolean, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglUniformMatrix4fv(int paramInt1, int paramInt2, boolean paramBoolean, long paramLong1, long paramLong2);

    public static void glGetShader(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetShaderiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglGetShaderiv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGetShaderiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    @Deprecated
    public static int glGetShader(int paramInt1, int paramInt2) {
        return glGetShaderi(paramInt1, paramInt2);
    }

    public static int glGetShaderi(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetShaderiv;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getBufferInt(localContextCapabilities);
        nglGetShaderiv(paramInt1, paramInt2, MemoryUtil.getAddress(localIntBuffer), l);
        return localIntBuffer.get(0);
    }

    public static void glGetProgram(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetProgramiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglGetProgramiv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGetProgramiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    @Deprecated
    public static int glGetProgram(int paramInt1, int paramInt2) {
        return glGetProgrami(paramInt1, paramInt2);
    }

    public static int glGetProgrami(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetProgramiv;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getBufferInt(localContextCapabilities);
        nglGetProgramiv(paramInt1, paramInt2, MemoryUtil.getAddress(localIntBuffer), l);
        return localIntBuffer.get(0);
    }

    public static void glGetShaderInfoLog(int paramInt, IntBuffer paramIntBuffer, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetShaderInfoLog;
        BufferChecks.checkFunctionAddress(l);
        if (paramIntBuffer != null) {
            BufferChecks.checkBuffer(paramIntBuffer, 1);
        }
        BufferChecks.checkDirect(paramByteBuffer);
        nglGetShaderInfoLog(paramInt, paramByteBuffer.remaining(), MemoryUtil.getAddressSafe(paramIntBuffer), MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglGetShaderInfoLog(int paramInt1, int paramInt2, long paramLong1, long paramLong2, long paramLong3);

    public static String glGetShaderInfoLog(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetShaderInfoLog;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getLengths(localContextCapabilities);
        ByteBuffer localByteBuffer = APIUtil.getBufferByte(localContextCapabilities, paramInt2);
        nglGetShaderInfoLog(paramInt1, paramInt2, MemoryUtil.getAddress0(localIntBuffer), MemoryUtil.getAddress(localByteBuffer), l);
        localByteBuffer.limit(localIntBuffer.get(0));
        return APIUtil.getString(localContextCapabilities, localByteBuffer);
    }

    public static void glGetProgramInfoLog(int paramInt, IntBuffer paramIntBuffer, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetProgramInfoLog;
        BufferChecks.checkFunctionAddress(l);
        if (paramIntBuffer != null) {
            BufferChecks.checkBuffer(paramIntBuffer, 1);
        }
        BufferChecks.checkDirect(paramByteBuffer);
        nglGetProgramInfoLog(paramInt, paramByteBuffer.remaining(), MemoryUtil.getAddressSafe(paramIntBuffer), MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglGetProgramInfoLog(int paramInt1, int paramInt2, long paramLong1, long paramLong2, long paramLong3);

    public static String glGetProgramInfoLog(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetProgramInfoLog;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getLengths(localContextCapabilities);
        ByteBuffer localByteBuffer = APIUtil.getBufferByte(localContextCapabilities, paramInt2);
        nglGetProgramInfoLog(paramInt1, paramInt2, MemoryUtil.getAddress0(localIntBuffer), MemoryUtil.getAddress(localByteBuffer), l);
        localByteBuffer.limit(localIntBuffer.get(0));
        return APIUtil.getString(localContextCapabilities, localByteBuffer);
    }

    public static void glGetAttachedShaders(int paramInt, IntBuffer paramIntBuffer1, IntBuffer paramIntBuffer2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetAttachedShaders;
        BufferChecks.checkFunctionAddress(l);
        if (paramIntBuffer1 != null) {
            BufferChecks.checkBuffer(paramIntBuffer1, 1);
        }
        BufferChecks.checkDirect(paramIntBuffer2);
        nglGetAttachedShaders(paramInt, paramIntBuffer2.remaining(), MemoryUtil.getAddressSafe(paramIntBuffer1), MemoryUtil.getAddress(paramIntBuffer2), l);
    }

    static native void nglGetAttachedShaders(int paramInt1, int paramInt2, long paramLong1, long paramLong2, long paramLong3);

    public static int glGetUniformLocation(int paramInt, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetUniformLocation;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramByteBuffer, 1);
        BufferChecks.checkNullTerminated(paramByteBuffer);
        int i = nglGetUniformLocation(paramInt, MemoryUtil.getAddress(paramByteBuffer), l);
        return i;
    }

    static native int nglGetUniformLocation(int paramInt, long paramLong1, long paramLong2);

    public static int glGetUniformLocation(int paramInt, CharSequence paramCharSequence) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetUniformLocation;
        BufferChecks.checkFunctionAddress(l);
        int i = nglGetUniformLocation(paramInt, APIUtil.getBufferNT(localContextCapabilities, paramCharSequence), l);
        return i;
    }

    public static void glGetActiveUniform(int paramInt1, int paramInt2, IntBuffer paramIntBuffer1, IntBuffer paramIntBuffer2, IntBuffer paramIntBuffer3, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetActiveUniform;
        BufferChecks.checkFunctionAddress(l);
        if (paramIntBuffer1 != null) {
            BufferChecks.checkBuffer(paramIntBuffer1, 1);
        }
        BufferChecks.checkBuffer(paramIntBuffer2, 1);
        BufferChecks.checkBuffer(paramIntBuffer3, 1);
        BufferChecks.checkDirect(paramByteBuffer);
        nglGetActiveUniform(paramInt1, paramInt2, paramByteBuffer.remaining(), MemoryUtil.getAddressSafe(paramIntBuffer1), MemoryUtil.getAddress(paramIntBuffer2), MemoryUtil.getAddress(paramIntBuffer3), MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglGetActiveUniform(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5);

    public static String glGetActiveUniform(int paramInt1, int paramInt2, int paramInt3, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetActiveUniform;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 2);
        IntBuffer localIntBuffer = APIUtil.getLengths(localContextCapabilities);
        ByteBuffer localByteBuffer = APIUtil.getBufferByte(localContextCapabilities, paramInt3);
        nglGetActiveUniform(paramInt1, paramInt2, paramInt3, MemoryUtil.getAddress0(localIntBuffer), MemoryUtil.getAddress(paramIntBuffer), MemoryUtil.getAddress(paramIntBuffer, paramIntBuffer.position() | 0x1), MemoryUtil.getAddress(localByteBuffer), l);
        localByteBuffer.limit(localIntBuffer.get(0));
        return APIUtil.getString(localContextCapabilities, localByteBuffer);
    }

    public static String glGetActiveUniform(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetActiveUniform;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getLengths(localContextCapabilities);
        ByteBuffer localByteBuffer = APIUtil.getBufferByte(localContextCapabilities, paramInt3);
        nglGetActiveUniform(paramInt1, paramInt2, paramInt3, MemoryUtil.getAddress0(localIntBuffer), MemoryUtil.getAddress0(APIUtil.getBufferInt(localContextCapabilities)), MemoryUtil.getAddress(APIUtil.getBufferInt(localContextCapabilities), 1), MemoryUtil.getAddress(localByteBuffer), l);
        localByteBuffer.limit(localIntBuffer.get(0));
        return APIUtil.getString(localContextCapabilities, localByteBuffer);
    }

    public static int glGetActiveUniformSize(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetActiveUniform;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getBufferInt(localContextCapabilities);
        nglGetActiveUniform(paramInt1, paramInt2, 1, 0L, MemoryUtil.getAddress(localIntBuffer), MemoryUtil.getAddress(localIntBuffer, 1), APIUtil.getBufferByte0(localContextCapabilities), l);
        return localIntBuffer.get(0);
    }

    public static int glGetActiveUniformType(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetActiveUniform;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getBufferInt(localContextCapabilities);
        nglGetActiveUniform(paramInt1, paramInt2, 0, 0L, MemoryUtil.getAddress(localIntBuffer, 1), MemoryUtil.getAddress(localIntBuffer), APIUtil.getBufferByte0(localContextCapabilities), l);
        return localIntBuffer.get(0);
    }

    public static void glGetUniform(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetUniformfv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramFloatBuffer);
        nglGetUniformfv(paramInt1, paramInt2, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglGetUniformfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glGetUniform(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetUniformiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglGetUniformiv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGetUniformiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glGetShaderSource(int paramInt, IntBuffer paramIntBuffer, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetShaderSource;
        BufferChecks.checkFunctionAddress(l);
        if (paramIntBuffer != null) {
            BufferChecks.checkBuffer(paramIntBuffer, 1);
        }
        BufferChecks.checkDirect(paramByteBuffer);
        nglGetShaderSource(paramInt, paramByteBuffer.remaining(), MemoryUtil.getAddressSafe(paramIntBuffer), MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglGetShaderSource(int paramInt1, int paramInt2, long paramLong1, long paramLong2, long paramLong3);

    public static String glGetShaderSource(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetShaderSource;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getLengths(localContextCapabilities);
        ByteBuffer localByteBuffer = APIUtil.getBufferByte(localContextCapabilities, paramInt2);
        nglGetShaderSource(paramInt1, paramInt2, MemoryUtil.getAddress0(localIntBuffer), MemoryUtil.getAddress(localByteBuffer), l);
        localByteBuffer.limit(localIntBuffer.get(0));
        return APIUtil.getString(localContextCapabilities, localByteBuffer);
    }

    public static void glVertexAttrib1s(int paramInt, short paramShort) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttrib1s;
        BufferChecks.checkFunctionAddress(l);
        nglVertexAttrib1s(paramInt, paramShort, l);
    }

    static native void nglVertexAttrib1s(int paramInt, short paramShort, long paramLong);

    public static void glVertexAttrib1f(int paramInt, float paramFloat) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttrib1f;
        BufferChecks.checkFunctionAddress(l);
        nglVertexAttrib1f(paramInt, paramFloat, l);
    }

    static native void nglVertexAttrib1f(int paramInt, float paramFloat, long paramLong);

    public static void glVertexAttrib1d(int paramInt, double paramDouble) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttrib1d;
        BufferChecks.checkFunctionAddress(l);
        nglVertexAttrib1d(paramInt, paramDouble, l);
    }

    static native void nglVertexAttrib1d(int paramInt, double paramDouble, long paramLong);

    public static void glVertexAttrib2s(int paramInt, short paramShort1, short paramShort2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttrib2s;
        BufferChecks.checkFunctionAddress(l);
        nglVertexAttrib2s(paramInt, paramShort1, paramShort2, l);
    }

    static native void nglVertexAttrib2s(int paramInt, short paramShort1, short paramShort2, long paramLong);

    public static void glVertexAttrib2f(int paramInt, float paramFloat1, float paramFloat2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttrib2f;
        BufferChecks.checkFunctionAddress(l);
        nglVertexAttrib2f(paramInt, paramFloat1, paramFloat2, l);
    }

    static native void nglVertexAttrib2f(int paramInt, float paramFloat1, float paramFloat2, long paramLong);

    public static void glVertexAttrib2d(int paramInt, double paramDouble1, double paramDouble2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttrib2d;
        BufferChecks.checkFunctionAddress(l);
        nglVertexAttrib2d(paramInt, paramDouble1, paramDouble2, l);
    }

    static native void nglVertexAttrib2d(int paramInt, double paramDouble1, double paramDouble2, long paramLong);

    public static void glVertexAttrib3s(int paramInt, short paramShort1, short paramShort2, short paramShort3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttrib3s;
        BufferChecks.checkFunctionAddress(l);
        nglVertexAttrib3s(paramInt, paramShort1, paramShort2, paramShort3, l);
    }

    static native void nglVertexAttrib3s(int paramInt, short paramShort1, short paramShort2, short paramShort3, long paramLong);

    public static void glVertexAttrib3f(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttrib3f;
        BufferChecks.checkFunctionAddress(l);
        nglVertexAttrib3f(paramInt, paramFloat1, paramFloat2, paramFloat3, l);
    }

    static native void nglVertexAttrib3f(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, long paramLong);

    public static void glVertexAttrib3d(int paramInt, double paramDouble1, double paramDouble2, double paramDouble3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttrib3d;
        BufferChecks.checkFunctionAddress(l);
        nglVertexAttrib3d(paramInt, paramDouble1, paramDouble2, paramDouble3, l);
    }

    static native void nglVertexAttrib3d(int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);

    public static void glVertexAttrib4s(int paramInt, short paramShort1, short paramShort2, short paramShort3, short paramShort4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttrib4s;
        BufferChecks.checkFunctionAddress(l);
        nglVertexAttrib4s(paramInt, paramShort1, paramShort2, paramShort3, paramShort4, l);
    }

    static native void nglVertexAttrib4s(int paramInt, short paramShort1, short paramShort2, short paramShort3, short paramShort4, long paramLong);

    public static void glVertexAttrib4f(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttrib4f;
        BufferChecks.checkFunctionAddress(l);
        nglVertexAttrib4f(paramInt, paramFloat1, paramFloat2, paramFloat3, paramFloat4, l);
    }

    static native void nglVertexAttrib4f(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);

    public static void glVertexAttrib4d(int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttrib4d;
        BufferChecks.checkFunctionAddress(l);
        nglVertexAttrib4d(paramInt, paramDouble1, paramDouble2, paramDouble3, paramDouble4, l);
    }

    static native void nglVertexAttrib4d(int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, long paramLong);

    public static void glVertexAttrib4Nub(int paramInt, byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttrib4Nub;
        BufferChecks.checkFunctionAddress(l);
        nglVertexAttrib4Nub(paramInt, paramByte1, paramByte2, paramByte3, paramByte4, l);
    }

    static native void nglVertexAttrib4Nub(int paramInt, byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4, long paramLong);

    public static void glVertexAttribPointer(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3, DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramDoubleBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).glVertexAttribPointer_buffer[paramInt1] = paramDoubleBuffer;
        }
        nglVertexAttribPointer(paramInt1, paramInt2, 5130, paramBoolean, paramInt3, MemoryUtil.getAddress(paramDoubleBuffer), l);
    }

    public static void glVertexAttribPointer(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramFloatBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).glVertexAttribPointer_buffer[paramInt1] = paramFloatBuffer;
        }
        nglVertexAttribPointer(paramInt1, paramInt2, 5126, paramBoolean, paramInt3, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    public static void glVertexAttribPointer(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2, int paramInt3, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramByteBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).glVertexAttribPointer_buffer[paramInt1] = paramByteBuffer;
        }
        nglVertexAttribPointer(paramInt1, paramInt2, paramBoolean1 ? 5121 : 5120, paramBoolean2, paramInt3, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    public static void glVertexAttribPointer(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2, int paramInt3, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramIntBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).glVertexAttribPointer_buffer[paramInt1] = paramIntBuffer;
        }
        nglVertexAttribPointer(paramInt1, paramInt2, paramBoolean1 ? 5125 : 5124, paramBoolean2, paramInt3, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    public static void glVertexAttribPointer(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2, int paramInt3, ShortBuffer paramShortBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramShortBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).glVertexAttribPointer_buffer[paramInt1] = paramShortBuffer;
        }
        nglVertexAttribPointer(paramInt1, paramInt2, paramBoolean1 ? 5123 : 5122, paramBoolean2, paramInt3, MemoryUtil.getAddress(paramShortBuffer), l);
    }

    static native void nglVertexAttribPointer(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, int paramInt4, long paramLong1, long paramLong2);

    public static void glVertexAttribPointer(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, int paramInt4, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOenabled(localContextCapabilities);
        nglVertexAttribPointerBO(paramInt1, paramInt2, paramInt3, paramBoolean, paramInt4, paramLong, l);
    }

    static native void nglVertexAttribPointerBO(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, int paramInt4, long paramLong1, long paramLong2);

    public static void glVertexAttribPointer(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, int paramInt4, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexAttribPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramByteBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).glVertexAttribPointer_buffer[paramInt1] = paramByteBuffer;
        }
        nglVertexAttribPointer(paramInt1, paramInt2, paramInt3, paramBoolean, paramInt4, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    public static void glEnableVertexAttribArray(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glEnableVertexAttribArray;
        BufferChecks.checkFunctionAddress(l);
        nglEnableVertexAttribArray(paramInt, l);
    }

    static native void nglEnableVertexAttribArray(int paramInt, long paramLong);

    public static void glDisableVertexAttribArray(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDisableVertexAttribArray;
        BufferChecks.checkFunctionAddress(l);
        nglDisableVertexAttribArray(paramInt, l);
    }

    static native void nglDisableVertexAttribArray(int paramInt, long paramLong);

    public static void glGetVertexAttrib(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetVertexAttribfv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramFloatBuffer, 4);
        nglGetVertexAttribfv(paramInt1, paramInt2, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglGetVertexAttribfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glGetVertexAttrib(int paramInt1, int paramInt2, DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetVertexAttribdv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramDoubleBuffer, 4);
        nglGetVertexAttribdv(paramInt1, paramInt2, MemoryUtil.getAddress(paramDoubleBuffer), l);
    }

    static native void nglGetVertexAttribdv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glGetVertexAttrib(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetVertexAttribiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglGetVertexAttribiv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGetVertexAttribiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static ByteBuffer glGetVertexAttribPointer(int paramInt1, int paramInt2, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetVertexAttribPointerv;
        BufferChecks.checkFunctionAddress(l);
        ByteBuffer localByteBuffer = nglGetVertexAttribPointerv(paramInt1, paramInt2, paramLong, l);
        return (LWJGLUtil.CHECKS) && (localByteBuffer == null) ? null : localByteBuffer.order(ByteOrder.nativeOrder());
    }

    static native ByteBuffer nglGetVertexAttribPointerv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glGetVertexAttribPointer(int paramInt1, int paramInt2, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetVertexAttribPointerv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramByteBuffer, PointerBuffer.getPointerSize());
        nglGetVertexAttribPointerv2(paramInt1, paramInt2, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglGetVertexAttribPointerv2(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glBindAttribLocation(int paramInt1, int paramInt2, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glBindAttribLocation;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramByteBuffer);
        BufferChecks.checkNullTerminated(paramByteBuffer);
        nglBindAttribLocation(paramInt1, paramInt2, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglBindAttribLocation(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glBindAttribLocation(int paramInt1, int paramInt2, CharSequence paramCharSequence) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glBindAttribLocation;
        BufferChecks.checkFunctionAddress(l);
        nglBindAttribLocation(paramInt1, paramInt2, APIUtil.getBufferNT(localContextCapabilities, paramCharSequence), l);
    }

    public static void glGetActiveAttrib(int paramInt1, int paramInt2, IntBuffer paramIntBuffer1, IntBuffer paramIntBuffer2, IntBuffer paramIntBuffer3, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetActiveAttrib;
        BufferChecks.checkFunctionAddress(l);
        if (paramIntBuffer1 != null) {
            BufferChecks.checkBuffer(paramIntBuffer1, 1);
        }
        BufferChecks.checkBuffer(paramIntBuffer2, 1);
        BufferChecks.checkBuffer(paramIntBuffer3, 1);
        BufferChecks.checkDirect(paramByteBuffer);
        nglGetActiveAttrib(paramInt1, paramInt2, paramByteBuffer.remaining(), MemoryUtil.getAddressSafe(paramIntBuffer1), MemoryUtil.getAddress(paramIntBuffer2), MemoryUtil.getAddress(paramIntBuffer3), MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglGetActiveAttrib(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5);

    public static String glGetActiveAttrib(int paramInt1, int paramInt2, int paramInt3, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetActiveAttrib;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 2);
        IntBuffer localIntBuffer = APIUtil.getLengths(localContextCapabilities);
        ByteBuffer localByteBuffer = APIUtil.getBufferByte(localContextCapabilities, paramInt3);
        nglGetActiveAttrib(paramInt1, paramInt2, paramInt3, MemoryUtil.getAddress0(localIntBuffer), MemoryUtil.getAddress(paramIntBuffer), MemoryUtil.getAddress(paramIntBuffer, paramIntBuffer.position() | 0x1), MemoryUtil.getAddress(localByteBuffer), l);
        localByteBuffer.limit(localIntBuffer.get(0));
        return APIUtil.getString(localContextCapabilities, localByteBuffer);
    }

    public static String glGetActiveAttrib(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetActiveAttrib;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getLengths(localContextCapabilities);
        ByteBuffer localByteBuffer = APIUtil.getBufferByte(localContextCapabilities, paramInt3);
        nglGetActiveAttrib(paramInt1, paramInt2, paramInt3, MemoryUtil.getAddress0(localIntBuffer), MemoryUtil.getAddress0(APIUtil.getBufferInt(localContextCapabilities)), MemoryUtil.getAddress(APIUtil.getBufferInt(localContextCapabilities), 1), MemoryUtil.getAddress(localByteBuffer), l);
        localByteBuffer.limit(localIntBuffer.get(0));
        return APIUtil.getString(localContextCapabilities, localByteBuffer);
    }

    public static int glGetActiveAttribSize(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetActiveAttrib;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getBufferInt(localContextCapabilities);
        nglGetActiveAttrib(paramInt1, paramInt2, 0, 0L, MemoryUtil.getAddress(localIntBuffer), MemoryUtil.getAddress(localIntBuffer, 1), APIUtil.getBufferByte0(localContextCapabilities), l);
        return localIntBuffer.get(0);
    }

    public static int glGetActiveAttribType(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetActiveAttrib;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getBufferInt(localContextCapabilities);
        nglGetActiveAttrib(paramInt1, paramInt2, 0, 0L, MemoryUtil.getAddress(localIntBuffer, 1), MemoryUtil.getAddress(localIntBuffer), APIUtil.getBufferByte0(localContextCapabilities), l);
        return localIntBuffer.get(0);
    }

    public static int glGetAttribLocation(int paramInt, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetAttribLocation;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramByteBuffer);
        BufferChecks.checkNullTerminated(paramByteBuffer);
        int i = nglGetAttribLocation(paramInt, MemoryUtil.getAddress(paramByteBuffer), l);
        return i;
    }

    static native int nglGetAttribLocation(int paramInt, long paramLong1, long paramLong2);

    public static int glGetAttribLocation(int paramInt, CharSequence paramCharSequence) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetAttribLocation;
        BufferChecks.checkFunctionAddress(l);
        int i = nglGetAttribLocation(paramInt, APIUtil.getBufferNT(localContextCapabilities, paramCharSequence), l);
        return i;
    }

    public static void glDrawBuffers(IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDrawBuffers;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglDrawBuffers(paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglDrawBuffers(int paramInt, long paramLong1, long paramLong2);

    public static void glDrawBuffers(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDrawBuffers;
        BufferChecks.checkFunctionAddress(l);
        nglDrawBuffers(1, APIUtil.getInt(localContextCapabilities, paramInt), l);
    }

    public static void glStencilOpSeparate(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glStencilOpSeparate;
        BufferChecks.checkFunctionAddress(l);
        nglStencilOpSeparate(paramInt1, paramInt2, paramInt3, paramInt4, l);
    }

    static native void nglStencilOpSeparate(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);

    public static void glStencilFuncSeparate(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glStencilFuncSeparate;
        BufferChecks.checkFunctionAddress(l);
        nglStencilFuncSeparate(paramInt1, paramInt2, paramInt3, paramInt4, l);
    }

    static native void nglStencilFuncSeparate(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);

    public static void glStencilMaskSeparate(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glStencilMaskSeparate;
        BufferChecks.checkFunctionAddress(l);
        nglStencilMaskSeparate(paramInt1, paramInt2, l);
    }

    static native void nglStencilMaskSeparate(int paramInt1, int paramInt2, long paramLong);

    public static void glBlendEquationSeparate(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glBlendEquationSeparate;
        BufferChecks.checkFunctionAddress(l);
        nglBlendEquationSeparate(paramInt1, paramInt2, l);
    }

    static native void nglBlendEquationSeparate(int paramInt1, int paramInt2, long paramLong);
}




