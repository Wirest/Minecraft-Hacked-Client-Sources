// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.Buffer;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.MemoryUtil;
import java.nio.ByteBuffer;
import org.lwjgl.BufferChecks;

public final class ARBShaderObjects
{
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
    
    private ARBShaderObjects() {
    }
    
    public static void glDeleteObjectARB(final int obj) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteObjectARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDeleteObjectARB(obj, function_pointer);
    }
    
    static native void nglDeleteObjectARB(final int p0, final long p1);
    
    public static int glGetHandleARB(final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetHandleARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglGetHandleARB(pname, function_pointer);
        return __result;
    }
    
    static native int nglGetHandleARB(final int p0, final long p1);
    
    public static void glDetachObjectARB(final int containerObj, final int attachedObj) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDetachObjectARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDetachObjectARB(containerObj, attachedObj, function_pointer);
    }
    
    static native void nglDetachObjectARB(final int p0, final int p1, final long p2);
    
    public static int glCreateShaderObjectARB(final int shaderType) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateShaderObjectARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglCreateShaderObjectARB(shaderType, function_pointer);
        return __result;
    }
    
    static native int nglCreateShaderObjectARB(final int p0, final long p1);
    
    public static void glShaderSourceARB(final int shader, final ByteBuffer string) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glShaderSourceARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(string);
        nglShaderSourceARB(shader, 1, MemoryUtil.getAddress(string), string.remaining(), function_pointer);
    }
    
    static native void nglShaderSourceARB(final int p0, final int p1, final long p2, final int p3, final long p4);
    
    public static void glShaderSourceARB(final int shader, final CharSequence string) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glShaderSourceARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglShaderSourceARB(shader, 1, APIUtil.getBuffer(caps, string), string.length(), function_pointer);
    }
    
    public static void glShaderSourceARB(final int shader, final CharSequence[] strings) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glShaderSourceARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkArray(strings);
        nglShaderSourceARB3(shader, strings.length, APIUtil.getBuffer(caps, strings), APIUtil.getLengths(caps, strings), function_pointer);
    }
    
    static native void nglShaderSourceARB3(final int p0, final int p1, final long p2, final long p3, final long p4);
    
    public static void glCompileShaderARB(final int shaderObj) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompileShaderARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglCompileShaderARB(shaderObj, function_pointer);
    }
    
    static native void nglCompileShaderARB(final int p0, final long p1);
    
    public static int glCreateProgramObjectARB() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateProgramObjectARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglCreateProgramObjectARB(function_pointer);
        return __result;
    }
    
    static native int nglCreateProgramObjectARB(final long p0);
    
    public static void glAttachObjectARB(final int containerObj, final int obj) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glAttachObjectARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglAttachObjectARB(containerObj, obj, function_pointer);
    }
    
    static native void nglAttachObjectARB(final int p0, final int p1, final long p2);
    
    public static void glLinkProgramARB(final int programObj) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glLinkProgramARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglLinkProgramARB(programObj, function_pointer);
    }
    
    static native void nglLinkProgramARB(final int p0, final long p1);
    
    public static void glUseProgramObjectARB(final int programObj) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUseProgramObjectARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUseProgramObjectARB(programObj, function_pointer);
    }
    
    static native void nglUseProgramObjectARB(final int p0, final long p1);
    
    public static void glValidateProgramARB(final int programObj) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glValidateProgramARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglValidateProgramARB(programObj, function_pointer);
    }
    
    static native void nglValidateProgramARB(final int p0, final long p1);
    
    public static void glUniform1fARB(final int location, final float v0) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform1fARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform1fARB(location, v0, function_pointer);
    }
    
    static native void nglUniform1fARB(final int p0, final float p1, final long p2);
    
    public static void glUniform2fARB(final int location, final float v0, final float v1) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform2fARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform2fARB(location, v0, v1, function_pointer);
    }
    
    static native void nglUniform2fARB(final int p0, final float p1, final float p2, final long p3);
    
    public static void glUniform3fARB(final int location, final float v0, final float v1, final float v2) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform3fARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform3fARB(location, v0, v1, v2, function_pointer);
    }
    
    static native void nglUniform3fARB(final int p0, final float p1, final float p2, final float p3, final long p4);
    
    public static void glUniform4fARB(final int location, final float v0, final float v1, final float v2, final float v3) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform4fARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform4fARB(location, v0, v1, v2, v3, function_pointer);
    }
    
    static native void nglUniform4fARB(final int p0, final float p1, final float p2, final float p3, final float p4, final long p5);
    
    public static void glUniform1iARB(final int location, final int v0) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform1iARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform1iARB(location, v0, function_pointer);
    }
    
    static native void nglUniform1iARB(final int p0, final int p1, final long p2);
    
    public static void glUniform2iARB(final int location, final int v0, final int v1) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform2iARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform2iARB(location, v0, v1, function_pointer);
    }
    
    static native void nglUniform2iARB(final int p0, final int p1, final int p2, final long p3);
    
    public static void glUniform3iARB(final int location, final int v0, final int v1, final int v2) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform3iARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform3iARB(location, v0, v1, v2, function_pointer);
    }
    
    static native void nglUniform3iARB(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glUniform4iARB(final int location, final int v0, final int v1, final int v2, final int v3) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform4iARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform4iARB(location, v0, v1, v2, v3, function_pointer);
    }
    
    static native void nglUniform4iARB(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glUniform1ARB(final int location, final FloatBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform1fvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(values);
        nglUniform1fvARB(location, values.remaining(), MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglUniform1fvARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform2ARB(final int location, final FloatBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform2fvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(values);
        nglUniform2fvARB(location, values.remaining() >> 1, MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglUniform2fvARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform3ARB(final int location, final FloatBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform3fvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(values);
        nglUniform3fvARB(location, values.remaining() / 3, MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglUniform3fvARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform4ARB(final int location, final FloatBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform4fvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(values);
        nglUniform4fvARB(location, values.remaining() >> 2, MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglUniform4fvARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform1ARB(final int location, final IntBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform1ivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(values);
        nglUniform1ivARB(location, values.remaining(), MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglUniform1ivARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform2ARB(final int location, final IntBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform2ivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(values);
        nglUniform2ivARB(location, values.remaining() >> 1, MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglUniform2ivARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform3ARB(final int location, final IntBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform3ivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(values);
        nglUniform3ivARB(location, values.remaining() / 3, MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglUniform3ivARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform4ARB(final int location, final IntBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform4ivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(values);
        nglUniform4ivARB(location, values.remaining() >> 2, MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglUniform4ivARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniformMatrix2ARB(final int location, final boolean transpose, final FloatBuffer matrices) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformMatrix2fvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(matrices);
        nglUniformMatrix2fvARB(location, matrices.remaining() >> 2, transpose, MemoryUtil.getAddress(matrices), function_pointer);
    }
    
    static native void nglUniformMatrix2fvARB(final int p0, final int p1, final boolean p2, final long p3, final long p4);
    
    public static void glUniformMatrix3ARB(final int location, final boolean transpose, final FloatBuffer matrices) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformMatrix3fvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(matrices);
        nglUniformMatrix3fvARB(location, matrices.remaining() / 9, transpose, MemoryUtil.getAddress(matrices), function_pointer);
    }
    
    static native void nglUniformMatrix3fvARB(final int p0, final int p1, final boolean p2, final long p3, final long p4);
    
    public static void glUniformMatrix4ARB(final int location, final boolean transpose, final FloatBuffer matrices) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformMatrix4fvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(matrices);
        nglUniformMatrix4fvARB(location, matrices.remaining() >> 4, transpose, MemoryUtil.getAddress(matrices), function_pointer);
    }
    
    static native void nglUniformMatrix4fvARB(final int p0, final int p1, final boolean p2, final long p3, final long p4);
    
    public static void glGetObjectParameterARB(final int obj, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetObjectParameterfvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglGetObjectParameterfvARB(obj, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetObjectParameterfvARB(final int p0, final int p1, final long p2, final long p3);
    
    public static float glGetObjectParameterfARB(final int obj, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetObjectParameterfvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final FloatBuffer params = APIUtil.getBufferFloat(caps);
        nglGetObjectParameterfvARB(obj, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetObjectParameterARB(final int obj, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetObjectParameterivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglGetObjectParameterivARB(obj, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetObjectParameterivARB(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetObjectParameteriARB(final int obj, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetObjectParameterivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetObjectParameterivARB(obj, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetInfoLogARB(final int obj, final IntBuffer length, final ByteBuffer infoLog) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetInfoLogARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (length != null) {
            BufferChecks.checkBuffer(length, 1);
        }
        BufferChecks.checkDirect(infoLog);
        nglGetInfoLogARB(obj, infoLog.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(infoLog), function_pointer);
    }
    
    static native void nglGetInfoLogARB(final int p0, final int p1, final long p2, final long p3, final long p4);
    
    public static String glGetInfoLogARB(final int obj, final int maxLength) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetInfoLogARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer infoLog_length = APIUtil.getLengths(caps);
        final ByteBuffer infoLog = APIUtil.getBufferByte(caps, maxLength);
        nglGetInfoLogARB(obj, maxLength, MemoryUtil.getAddress0(infoLog_length), MemoryUtil.getAddress(infoLog), function_pointer);
        infoLog.limit(infoLog_length.get(0));
        return APIUtil.getString(caps, infoLog);
    }
    
    public static void glGetAttachedObjectsARB(final int containerObj, final IntBuffer count, final IntBuffer obj) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetAttachedObjectsARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (count != null) {
            BufferChecks.checkBuffer(count, 1);
        }
        BufferChecks.checkDirect(obj);
        nglGetAttachedObjectsARB(containerObj, obj.remaining(), MemoryUtil.getAddressSafe(count), MemoryUtil.getAddress(obj), function_pointer);
    }
    
    static native void nglGetAttachedObjectsARB(final int p0, final int p1, final long p2, final long p3, final long p4);
    
    public static int glGetUniformLocationARB(final int programObj, final ByteBuffer name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetUniformLocationARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(name);
        BufferChecks.checkNullTerminated(name);
        final int __result = nglGetUniformLocationARB(programObj, MemoryUtil.getAddress(name), function_pointer);
        return __result;
    }
    
    static native int nglGetUniformLocationARB(final int p0, final long p1, final long p2);
    
    public static int glGetUniformLocationARB(final int programObj, final CharSequence name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetUniformLocationARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglGetUniformLocationARB(programObj, APIUtil.getBufferNT(caps, name), function_pointer);
        return __result;
    }
    
    public static void glGetActiveUniformARB(final int programObj, final int index, final IntBuffer length, final IntBuffer size, final IntBuffer type, final ByteBuffer name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveUniformARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (length != null) {
            BufferChecks.checkBuffer(length, 1);
        }
        BufferChecks.checkBuffer(size, 1);
        BufferChecks.checkBuffer(type, 1);
        BufferChecks.checkDirect(name);
        nglGetActiveUniformARB(programObj, index, name.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(size), MemoryUtil.getAddress(type), MemoryUtil.getAddress(name), function_pointer);
    }
    
    static native void nglGetActiveUniformARB(final int p0, final int p1, final int p2, final long p3, final long p4, final long p5, final long p6, final long p7);
    
    public static String glGetActiveUniformARB(final int programObj, final int index, final int maxLength, final IntBuffer sizeType) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveUniformARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(sizeType, 2);
        final IntBuffer name_length = APIUtil.getLengths(caps);
        final ByteBuffer name = APIUtil.getBufferByte(caps, maxLength);
        nglGetActiveUniformARB(programObj, index, maxLength, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress(sizeType), MemoryUtil.getAddress(sizeType, sizeType.position() + 1), MemoryUtil.getAddress(name), function_pointer);
        name.limit(name_length.get(0));
        return APIUtil.getString(caps, name);
    }
    
    public static String glGetActiveUniformARB(final int programObj, final int index, final int maxLength) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveUniformARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer name_length = APIUtil.getLengths(caps);
        final ByteBuffer name = APIUtil.getBufferByte(caps, maxLength);
        nglGetActiveUniformARB(programObj, index, maxLength, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress0(APIUtil.getBufferInt(caps)), MemoryUtil.getAddress(APIUtil.getBufferInt(caps), 1), MemoryUtil.getAddress(name), function_pointer);
        name.limit(name_length.get(0));
        return APIUtil.getString(caps, name);
    }
    
    public static int glGetActiveUniformSizeARB(final int programObj, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveUniformARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer size = APIUtil.getBufferInt(caps);
        nglGetActiveUniformARB(programObj, index, 0, 0L, MemoryUtil.getAddress(size), MemoryUtil.getAddress(size, 1), APIUtil.getBufferByte0(caps), function_pointer);
        return size.get(0);
    }
    
    public static int glGetActiveUniformTypeARB(final int programObj, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveUniformARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer type = APIUtil.getBufferInt(caps);
        nglGetActiveUniformARB(programObj, index, 0, 0L, MemoryUtil.getAddress(type, 1), MemoryUtil.getAddress(type), APIUtil.getBufferByte0(caps), function_pointer);
        return type.get(0);
    }
    
    public static void glGetUniformARB(final int programObj, final int location, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetUniformfvARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglGetUniformfvARB(programObj, location, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetUniformfvARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetUniformARB(final int programObj, final int location, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetUniformivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglGetUniformivARB(programObj, location, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetUniformivARB(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetShaderSourceARB(final int obj, final IntBuffer length, final ByteBuffer source) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetShaderSourceARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (length != null) {
            BufferChecks.checkBuffer(length, 1);
        }
        BufferChecks.checkDirect(source);
        nglGetShaderSourceARB(obj, source.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(source), function_pointer);
    }
    
    static native void nglGetShaderSourceARB(final int p0, final int p1, final long p2, final long p3, final long p4);
    
    public static String glGetShaderSourceARB(final int obj, final int maxLength) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetShaderSourceARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer source_length = APIUtil.getLengths(caps);
        final ByteBuffer source = APIUtil.getBufferByte(caps, maxLength);
        nglGetShaderSourceARB(obj, maxLength, MemoryUtil.getAddress0(source_length), MemoryUtil.getAddress(source), function_pointer);
        source.limit(source_length.get(0));
        return APIUtil.getString(caps, source);
    }
}
