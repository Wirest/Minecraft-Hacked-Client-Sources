// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.PointerBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import org.lwjgl.LWJGLUtil;
import java.nio.DoubleBuffer;
import java.nio.Buffer;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.ByteBuffer;

public final class GL20
{
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
    
    private GL20() {
    }
    
    public static void glShaderSource(final int shader, final ByteBuffer string) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glShaderSource;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(string);
        nglShaderSource(shader, 1, MemoryUtil.getAddress(string), string.remaining(), function_pointer);
    }
    
    static native void nglShaderSource(final int p0, final int p1, final long p2, final int p3, final long p4);
    
    public static void glShaderSource(final int shader, final CharSequence string) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glShaderSource;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglShaderSource(shader, 1, APIUtil.getBuffer(caps, string), string.length(), function_pointer);
    }
    
    public static void glShaderSource(final int shader, final CharSequence[] strings) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glShaderSource;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkArray(strings);
        nglShaderSource3(shader, strings.length, APIUtil.getBuffer(caps, strings), APIUtil.getLengths(caps, strings), function_pointer);
    }
    
    static native void nglShaderSource3(final int p0, final int p1, final long p2, final long p3, final long p4);
    
    public static int glCreateShader(final int type) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateShader;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglCreateShader(type, function_pointer);
        return __result;
    }
    
    static native int nglCreateShader(final int p0, final long p1);
    
    public static boolean glIsShader(final int shader) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsShader;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsShader(shader, function_pointer);
        return __result;
    }
    
    static native boolean nglIsShader(final int p0, final long p1);
    
    public static void glCompileShader(final int shader) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCompileShader;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglCompileShader(shader, function_pointer);
    }
    
    static native void nglCompileShader(final int p0, final long p1);
    
    public static void glDeleteShader(final int shader) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteShader;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDeleteShader(shader, function_pointer);
    }
    
    static native void nglDeleteShader(final int p0, final long p1);
    
    public static int glCreateProgram() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateProgram;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglCreateProgram(function_pointer);
        return __result;
    }
    
    static native int nglCreateProgram(final long p0);
    
    public static boolean glIsProgram(final int program) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsProgram;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsProgram(program, function_pointer);
        return __result;
    }
    
    static native boolean nglIsProgram(final int p0, final long p1);
    
    public static void glAttachShader(final int program, final int shader) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glAttachShader;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglAttachShader(program, shader, function_pointer);
    }
    
    static native void nglAttachShader(final int p0, final int p1, final long p2);
    
    public static void glDetachShader(final int program, final int shader) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDetachShader;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDetachShader(program, shader, function_pointer);
    }
    
    static native void nglDetachShader(final int p0, final int p1, final long p2);
    
    public static void glLinkProgram(final int program) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glLinkProgram;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglLinkProgram(program, function_pointer);
    }
    
    static native void nglLinkProgram(final int p0, final long p1);
    
    public static void glUseProgram(final int program) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUseProgram;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUseProgram(program, function_pointer);
    }
    
    static native void nglUseProgram(final int p0, final long p1);
    
    public static void glValidateProgram(final int program) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glValidateProgram;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglValidateProgram(program, function_pointer);
    }
    
    static native void nglValidateProgram(final int p0, final long p1);
    
    public static void glDeleteProgram(final int program) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteProgram;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDeleteProgram(program, function_pointer);
    }
    
    static native void nglDeleteProgram(final int p0, final long p1);
    
    public static void glUniform1f(final int location, final float v0) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform1f;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform1f(location, v0, function_pointer);
    }
    
    static native void nglUniform1f(final int p0, final float p1, final long p2);
    
    public static void glUniform2f(final int location, final float v0, final float v1) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform2f;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform2f(location, v0, v1, function_pointer);
    }
    
    static native void nglUniform2f(final int p0, final float p1, final float p2, final long p3);
    
    public static void glUniform3f(final int location, final float v0, final float v1, final float v2) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform3f;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform3f(location, v0, v1, v2, function_pointer);
    }
    
    static native void nglUniform3f(final int p0, final float p1, final float p2, final float p3, final long p4);
    
    public static void glUniform4f(final int location, final float v0, final float v1, final float v2, final float v3) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform4f;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform4f(location, v0, v1, v2, v3, function_pointer);
    }
    
    static native void nglUniform4f(final int p0, final float p1, final float p2, final float p3, final float p4, final long p5);
    
    public static void glUniform1i(final int location, final int v0) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform1i;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform1i(location, v0, function_pointer);
    }
    
    static native void nglUniform1i(final int p0, final int p1, final long p2);
    
    public static void glUniform2i(final int location, final int v0, final int v1) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform2i;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform2i(location, v0, v1, function_pointer);
    }
    
    static native void nglUniform2i(final int p0, final int p1, final int p2, final long p3);
    
    public static void glUniform3i(final int location, final int v0, final int v1, final int v2) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform3i;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform3i(location, v0, v1, v2, function_pointer);
    }
    
    static native void nglUniform3i(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glUniform4i(final int location, final int v0, final int v1, final int v2, final int v3) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform4i;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglUniform4i(location, v0, v1, v2, v3, function_pointer);
    }
    
    static native void nglUniform4i(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static void glUniform1(final int location, final FloatBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform1fv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(values);
        nglUniform1fv(location, values.remaining(), MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglUniform1fv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform2(final int location, final FloatBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform2fv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(values);
        nglUniform2fv(location, values.remaining() >> 1, MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglUniform2fv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform3(final int location, final FloatBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform3fv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(values);
        nglUniform3fv(location, values.remaining() / 3, MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglUniform3fv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform4(final int location, final FloatBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform4fv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(values);
        nglUniform4fv(location, values.remaining() >> 2, MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglUniform4fv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform1(final int location, final IntBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform1iv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(values);
        nglUniform1iv(location, values.remaining(), MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglUniform1iv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform2(final int location, final IntBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform2iv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(values);
        nglUniform2iv(location, values.remaining() >> 1, MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglUniform2iv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform3(final int location, final IntBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform3iv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(values);
        nglUniform3iv(location, values.remaining() / 3, MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglUniform3iv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniform4(final int location, final IntBuffer values) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniform4iv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(values);
        nglUniform4iv(location, values.remaining() >> 2, MemoryUtil.getAddress(values), function_pointer);
    }
    
    static native void nglUniform4iv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glUniformMatrix2(final int location, final boolean transpose, final FloatBuffer matrices) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformMatrix2fv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(matrices);
        nglUniformMatrix2fv(location, matrices.remaining() >> 2, transpose, MemoryUtil.getAddress(matrices), function_pointer);
    }
    
    static native void nglUniformMatrix2fv(final int p0, final int p1, final boolean p2, final long p3, final long p4);
    
    public static void glUniformMatrix3(final int location, final boolean transpose, final FloatBuffer matrices) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformMatrix3fv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(matrices);
        nglUniformMatrix3fv(location, matrices.remaining() / 9, transpose, MemoryUtil.getAddress(matrices), function_pointer);
    }
    
    static native void nglUniformMatrix3fv(final int p0, final int p1, final boolean p2, final long p3, final long p4);
    
    public static void glUniformMatrix4(final int location, final boolean transpose, final FloatBuffer matrices) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glUniformMatrix4fv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(matrices);
        nglUniformMatrix4fv(location, matrices.remaining() >> 4, transpose, MemoryUtil.getAddress(matrices), function_pointer);
    }
    
    static native void nglUniformMatrix4fv(final int p0, final int p1, final boolean p2, final long p3, final long p4);
    
    public static void glGetShader(final int shader, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetShaderiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglGetShaderiv(shader, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetShaderiv(final int p0, final int p1, final long p2, final long p3);
    
    @Deprecated
    public static int glGetShader(final int shader, final int pname) {
        return glGetShaderi(shader, pname);
    }
    
    public static int glGetShaderi(final int shader, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetShaderiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetShaderiv(shader, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetProgram(final int program, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglGetProgramiv(program, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetProgramiv(final int p0, final int p1, final long p2, final long p3);
    
    @Deprecated
    public static int glGetProgram(final int program, final int pname) {
        return glGetProgrami(program, pname);
    }
    
    public static int glGetProgrami(final int program, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer params = APIUtil.getBufferInt(caps);
        nglGetProgramiv(program, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
    
    public static void glGetShaderInfoLog(final int shader, final IntBuffer length, final ByteBuffer infoLog) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetShaderInfoLog;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (length != null) {
            BufferChecks.checkBuffer(length, 1);
        }
        BufferChecks.checkDirect(infoLog);
        nglGetShaderInfoLog(shader, infoLog.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(infoLog), function_pointer);
    }
    
    static native void nglGetShaderInfoLog(final int p0, final int p1, final long p2, final long p3, final long p4);
    
    public static String glGetShaderInfoLog(final int shader, final int maxLength) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetShaderInfoLog;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer infoLog_length = APIUtil.getLengths(caps);
        final ByteBuffer infoLog = APIUtil.getBufferByte(caps, maxLength);
        nglGetShaderInfoLog(shader, maxLength, MemoryUtil.getAddress0(infoLog_length), MemoryUtil.getAddress(infoLog), function_pointer);
        infoLog.limit(infoLog_length.get(0));
        return APIUtil.getString(caps, infoLog);
    }
    
    public static void glGetProgramInfoLog(final int program, final IntBuffer length, final ByteBuffer infoLog) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramInfoLog;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (length != null) {
            BufferChecks.checkBuffer(length, 1);
        }
        BufferChecks.checkDirect(infoLog);
        nglGetProgramInfoLog(program, infoLog.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(infoLog), function_pointer);
    }
    
    static native void nglGetProgramInfoLog(final int p0, final int p1, final long p2, final long p3, final long p4);
    
    public static String glGetProgramInfoLog(final int program, final int maxLength) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetProgramInfoLog;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer infoLog_length = APIUtil.getLengths(caps);
        final ByteBuffer infoLog = APIUtil.getBufferByte(caps, maxLength);
        nglGetProgramInfoLog(program, maxLength, MemoryUtil.getAddress0(infoLog_length), MemoryUtil.getAddress(infoLog), function_pointer);
        infoLog.limit(infoLog_length.get(0));
        return APIUtil.getString(caps, infoLog);
    }
    
    public static void glGetAttachedShaders(final int program, final IntBuffer count, final IntBuffer shaders) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetAttachedShaders;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (count != null) {
            BufferChecks.checkBuffer(count, 1);
        }
        BufferChecks.checkDirect(shaders);
        nglGetAttachedShaders(program, shaders.remaining(), MemoryUtil.getAddressSafe(count), MemoryUtil.getAddress(shaders), function_pointer);
    }
    
    static native void nglGetAttachedShaders(final int p0, final int p1, final long p2, final long p3, final long p4);
    
    public static int glGetUniformLocation(final int program, final ByteBuffer name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetUniformLocation;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(name, 1);
        BufferChecks.checkNullTerminated(name);
        final int __result = nglGetUniformLocation(program, MemoryUtil.getAddress(name), function_pointer);
        return __result;
    }
    
    static native int nglGetUniformLocation(final int p0, final long p1, final long p2);
    
    public static int glGetUniformLocation(final int program, final CharSequence name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetUniformLocation;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglGetUniformLocation(program, APIUtil.getBufferNT(caps, name), function_pointer);
        return __result;
    }
    
    public static void glGetActiveUniform(final int program, final int index, final IntBuffer length, final IntBuffer size, final IntBuffer type, final ByteBuffer name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveUniform;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (length != null) {
            BufferChecks.checkBuffer(length, 1);
        }
        BufferChecks.checkBuffer(size, 1);
        BufferChecks.checkBuffer(type, 1);
        BufferChecks.checkDirect(name);
        nglGetActiveUniform(program, index, name.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(size), MemoryUtil.getAddress(type), MemoryUtil.getAddress(name), function_pointer);
    }
    
    static native void nglGetActiveUniform(final int p0, final int p1, final int p2, final long p3, final long p4, final long p5, final long p6, final long p7);
    
    public static String glGetActiveUniform(final int program, final int index, final int maxLength, final IntBuffer sizeType) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveUniform;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(sizeType, 2);
        final IntBuffer name_length = APIUtil.getLengths(caps);
        final ByteBuffer name = APIUtil.getBufferByte(caps, maxLength);
        nglGetActiveUniform(program, index, maxLength, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress(sizeType), MemoryUtil.getAddress(sizeType, sizeType.position() + 1), MemoryUtil.getAddress(name), function_pointer);
        name.limit(name_length.get(0));
        return APIUtil.getString(caps, name);
    }
    
    public static String glGetActiveUniform(final int program, final int index, final int maxLength) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveUniform;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer name_length = APIUtil.getLengths(caps);
        final ByteBuffer name = APIUtil.getBufferByte(caps, maxLength);
        nglGetActiveUniform(program, index, maxLength, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress0(APIUtil.getBufferInt(caps)), MemoryUtil.getAddress(APIUtil.getBufferInt(caps), 1), MemoryUtil.getAddress(name), function_pointer);
        name.limit(name_length.get(0));
        return APIUtil.getString(caps, name);
    }
    
    public static int glGetActiveUniformSize(final int program, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveUniform;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer size = APIUtil.getBufferInt(caps);
        nglGetActiveUniform(program, index, 1, 0L, MemoryUtil.getAddress(size), MemoryUtil.getAddress(size, 1), APIUtil.getBufferByte0(caps), function_pointer);
        return size.get(0);
    }
    
    public static int glGetActiveUniformType(final int program, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveUniform;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer type = APIUtil.getBufferInt(caps);
        nglGetActiveUniform(program, index, 0, 0L, MemoryUtil.getAddress(type, 1), MemoryUtil.getAddress(type), APIUtil.getBufferByte0(caps), function_pointer);
        return type.get(0);
    }
    
    public static void glGetUniform(final int program, final int location, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetUniformfv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglGetUniformfv(program, location, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetUniformfv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetUniform(final int program, final int location, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetUniformiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(params);
        nglGetUniformiv(program, location, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetUniformiv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetShaderSource(final int shader, final IntBuffer length, final ByteBuffer source) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetShaderSource;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (length != null) {
            BufferChecks.checkBuffer(length, 1);
        }
        BufferChecks.checkDirect(source);
        nglGetShaderSource(shader, source.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(source), function_pointer);
    }
    
    static native void nglGetShaderSource(final int p0, final int p1, final long p2, final long p3, final long p4);
    
    public static String glGetShaderSource(final int shader, final int maxLength) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetShaderSource;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer source_length = APIUtil.getLengths(caps);
        final ByteBuffer source = APIUtil.getBufferByte(caps, maxLength);
        nglGetShaderSource(shader, maxLength, MemoryUtil.getAddress0(source_length), MemoryUtil.getAddress(source), function_pointer);
        source.limit(source_length.get(0));
        return APIUtil.getString(caps, source);
    }
    
    public static void glVertexAttrib1s(final int index, final short x) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib1s;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib1s(index, x, function_pointer);
    }
    
    static native void nglVertexAttrib1s(final int p0, final short p1, final long p2);
    
    public static void glVertexAttrib1f(final int index, final float x) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib1f;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib1f(index, x, function_pointer);
    }
    
    static native void nglVertexAttrib1f(final int p0, final float p1, final long p2);
    
    public static void glVertexAttrib1d(final int index, final double x) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib1d;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib1d(index, x, function_pointer);
    }
    
    static native void nglVertexAttrib1d(final int p0, final double p1, final long p2);
    
    public static void glVertexAttrib2s(final int index, final short x, final short y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib2s;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib2s(index, x, y, function_pointer);
    }
    
    static native void nglVertexAttrib2s(final int p0, final short p1, final short p2, final long p3);
    
    public static void glVertexAttrib2f(final int index, final float x, final float y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib2f;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib2f(index, x, y, function_pointer);
    }
    
    static native void nglVertexAttrib2f(final int p0, final float p1, final float p2, final long p3);
    
    public static void glVertexAttrib2d(final int index, final double x, final double y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib2d;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib2d(index, x, y, function_pointer);
    }
    
    static native void nglVertexAttrib2d(final int p0, final double p1, final double p2, final long p3);
    
    public static void glVertexAttrib3s(final int index, final short x, final short y, final short z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib3s;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib3s(index, x, y, z, function_pointer);
    }
    
    static native void nglVertexAttrib3s(final int p0, final short p1, final short p2, final short p3, final long p4);
    
    public static void glVertexAttrib3f(final int index, final float x, final float y, final float z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib3f;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib3f(index, x, y, z, function_pointer);
    }
    
    static native void nglVertexAttrib3f(final int p0, final float p1, final float p2, final float p3, final long p4);
    
    public static void glVertexAttrib3d(final int index, final double x, final double y, final double z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib3d;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib3d(index, x, y, z, function_pointer);
    }
    
    static native void nglVertexAttrib3d(final int p0, final double p1, final double p2, final double p3, final long p4);
    
    public static void glVertexAttrib4s(final int index, final short x, final short y, final short z, final short w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib4s;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib4s(index, x, y, z, w, function_pointer);
    }
    
    static native void nglVertexAttrib4s(final int p0, final short p1, final short p2, final short p3, final short p4, final long p5);
    
    public static void glVertexAttrib4f(final int index, final float x, final float y, final float z, final float w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib4f;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib4f(index, x, y, z, w, function_pointer);
    }
    
    static native void nglVertexAttrib4f(final int p0, final float p1, final float p2, final float p3, final float p4, final long p5);
    
    public static void glVertexAttrib4d(final int index, final double x, final double y, final double z, final double w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib4d;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib4d(index, x, y, z, w, function_pointer);
    }
    
    static native void nglVertexAttrib4d(final int p0, final double p1, final double p2, final double p3, final double p4, final long p5);
    
    public static void glVertexAttrib4Nub(final int index, final byte x, final byte y, final byte z, final byte w) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttrib4Nub;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglVertexAttrib4Nub(index, x, y, z, w, function_pointer);
    }
    
    static native void nglVertexAttrib4Nub(final int p0, final byte p1, final byte p2, final byte p3, final byte p4, final long p5);
    
    public static void glVertexAttribPointer(final int index, final int size, final boolean normalized, final int stride, final DoubleBuffer buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribPointer;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(buffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
        }
        nglVertexAttribPointer(index, size, 5130, normalized, stride, MemoryUtil.getAddress(buffer), function_pointer);
    }
    
    public static void glVertexAttribPointer(final int index, final int size, final boolean normalized, final int stride, final FloatBuffer buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribPointer;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(buffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
        }
        nglVertexAttribPointer(index, size, 5126, normalized, stride, MemoryUtil.getAddress(buffer), function_pointer);
    }
    
    public static void glVertexAttribPointer(final int index, final int size, final boolean unsigned, final boolean normalized, final int stride, final ByteBuffer buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribPointer;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(buffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
        }
        nglVertexAttribPointer(index, size, unsigned ? 5121 : 5120, normalized, stride, MemoryUtil.getAddress(buffer), function_pointer);
    }
    
    public static void glVertexAttribPointer(final int index, final int size, final boolean unsigned, final boolean normalized, final int stride, final IntBuffer buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribPointer;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(buffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
        }
        nglVertexAttribPointer(index, size, unsigned ? 5125 : 5124, normalized, stride, MemoryUtil.getAddress(buffer), function_pointer);
    }
    
    public static void glVertexAttribPointer(final int index, final int size, final boolean unsigned, final boolean normalized, final int stride, final ShortBuffer buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribPointer;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(buffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
        }
        nglVertexAttribPointer(index, size, unsigned ? 5123 : 5122, normalized, stride, MemoryUtil.getAddress(buffer), function_pointer);
    }
    
    static native void nglVertexAttribPointer(final int p0, final int p1, final int p2, final boolean p3, final int p4, final long p5, final long p6);
    
    public static void glVertexAttribPointer(final int index, final int size, final int type, final boolean normalized, final int stride, final long buffer_buffer_offset) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribPointer;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOenabled(caps);
        nglVertexAttribPointerBO(index, size, type, normalized, stride, buffer_buffer_offset, function_pointer);
    }
    
    static native void nglVertexAttribPointerBO(final int p0, final int p1, final int p2, final boolean p3, final int p4, final long p5, final long p6);
    
    public static void glVertexAttribPointer(final int index, final int size, final int type, final boolean normalized, final int stride, final ByteBuffer buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glVertexAttribPointer;
        BufferChecks.checkFunctionAddress(function_pointer);
        GLChecks.ensureArrayVBOdisabled(caps);
        BufferChecks.checkDirect(buffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
        }
        nglVertexAttribPointer(index, size, type, normalized, stride, MemoryUtil.getAddress(buffer), function_pointer);
    }
    
    public static void glEnableVertexAttribArray(final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glEnableVertexAttribArray;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglEnableVertexAttribArray(index, function_pointer);
    }
    
    static native void nglEnableVertexAttribArray(final int p0, final long p1);
    
    public static void glDisableVertexAttribArray(final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDisableVertexAttribArray;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDisableVertexAttribArray(index, function_pointer);
    }
    
    static native void nglDisableVertexAttribArray(final int p0, final long p1);
    
    public static void glGetVertexAttrib(final int index, final int pname, final FloatBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexAttribfv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetVertexAttribfv(index, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetVertexAttribfv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetVertexAttrib(final int index, final int pname, final DoubleBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexAttribdv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetVertexAttribdv(index, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetVertexAttribdv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetVertexAttrib(final int index, final int pname, final IntBuffer params) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexAttribiv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 4);
        nglGetVertexAttribiv(index, pname, MemoryUtil.getAddress(params), function_pointer);
    }
    
    static native void nglGetVertexAttribiv(final int p0, final int p1, final long p2, final long p3);
    
    public static ByteBuffer glGetVertexAttribPointer(final int index, final int pname, final long result_size) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexAttribPointerv;
        BufferChecks.checkFunctionAddress(function_pointer);
        final ByteBuffer __result = nglGetVertexAttribPointerv(index, pname, result_size, function_pointer);
        return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
    }
    
    static native ByteBuffer nglGetVertexAttribPointerv(final int p0, final int p1, final long p2, final long p3);
    
    public static void glGetVertexAttribPointer(final int index, final int pname, final ByteBuffer pointer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetVertexAttribPointerv;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(pointer, PointerBuffer.getPointerSize());
        nglGetVertexAttribPointerv2(index, pname, MemoryUtil.getAddress(pointer), function_pointer);
    }
    
    static native void nglGetVertexAttribPointerv2(final int p0, final int p1, final long p2, final long p3);
    
    public static void glBindAttribLocation(final int program, final int index, final ByteBuffer name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindAttribLocation;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(name);
        BufferChecks.checkNullTerminated(name);
        nglBindAttribLocation(program, index, MemoryUtil.getAddress(name), function_pointer);
    }
    
    static native void nglBindAttribLocation(final int p0, final int p1, final long p2, final long p3);
    
    public static void glBindAttribLocation(final int program, final int index, final CharSequence name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindAttribLocation;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBindAttribLocation(program, index, APIUtil.getBufferNT(caps, name), function_pointer);
    }
    
    public static void glGetActiveAttrib(final int program, final int index, final IntBuffer length, final IntBuffer size, final IntBuffer type, final ByteBuffer name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveAttrib;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (length != null) {
            BufferChecks.checkBuffer(length, 1);
        }
        BufferChecks.checkBuffer(size, 1);
        BufferChecks.checkBuffer(type, 1);
        BufferChecks.checkDirect(name);
        nglGetActiveAttrib(program, index, name.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(size), MemoryUtil.getAddress(type), MemoryUtil.getAddress(name), function_pointer);
    }
    
    static native void nglGetActiveAttrib(final int p0, final int p1, final int p2, final long p3, final long p4, final long p5, final long p6, final long p7);
    
    public static String glGetActiveAttrib(final int program, final int index, final int maxLength, final IntBuffer sizeType) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveAttrib;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(sizeType, 2);
        final IntBuffer name_length = APIUtil.getLengths(caps);
        final ByteBuffer name = APIUtil.getBufferByte(caps, maxLength);
        nglGetActiveAttrib(program, index, maxLength, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress(sizeType), MemoryUtil.getAddress(sizeType, sizeType.position() + 1), MemoryUtil.getAddress(name), function_pointer);
        name.limit(name_length.get(0));
        return APIUtil.getString(caps, name);
    }
    
    public static String glGetActiveAttrib(final int program, final int index, final int maxLength) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveAttrib;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer name_length = APIUtil.getLengths(caps);
        final ByteBuffer name = APIUtil.getBufferByte(caps, maxLength);
        nglGetActiveAttrib(program, index, maxLength, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress0(APIUtil.getBufferInt(caps)), MemoryUtil.getAddress(APIUtil.getBufferInt(caps), 1), MemoryUtil.getAddress(name), function_pointer);
        name.limit(name_length.get(0));
        return APIUtil.getString(caps, name);
    }
    
    public static int glGetActiveAttribSize(final int program, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveAttrib;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer size = APIUtil.getBufferInt(caps);
        nglGetActiveAttrib(program, index, 0, 0L, MemoryUtil.getAddress(size), MemoryUtil.getAddress(size, 1), APIUtil.getBufferByte0(caps), function_pointer);
        return size.get(0);
    }
    
    public static int glGetActiveAttribType(final int program, final int index) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetActiveAttrib;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer type = APIUtil.getBufferInt(caps);
        nglGetActiveAttrib(program, index, 0, 0L, MemoryUtil.getAddress(type, 1), MemoryUtil.getAddress(type), APIUtil.getBufferByte0(caps), function_pointer);
        return type.get(0);
    }
    
    public static int glGetAttribLocation(final int program, final ByteBuffer name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetAttribLocation;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(name);
        BufferChecks.checkNullTerminated(name);
        final int __result = nglGetAttribLocation(program, MemoryUtil.getAddress(name), function_pointer);
        return __result;
    }
    
    static native int nglGetAttribLocation(final int p0, final long p1, final long p2);
    
    public static int glGetAttribLocation(final int program, final CharSequence name) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetAttribLocation;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglGetAttribLocation(program, APIUtil.getBufferNT(caps, name), function_pointer);
        return __result;
    }
    
    public static void glDrawBuffers(final IntBuffer buffers) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawBuffers;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(buffers);
        nglDrawBuffers(buffers.remaining(), MemoryUtil.getAddress(buffers), function_pointer);
    }
    
    static native void nglDrawBuffers(final int p0, final long p1, final long p2);
    
    public static void glDrawBuffers(final int buffer) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDrawBuffers;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDrawBuffers(1, APIUtil.getInt(caps, buffer), function_pointer);
    }
    
    public static void glStencilOpSeparate(final int face, final int sfail, final int dpfail, final int dppass) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glStencilOpSeparate;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglStencilOpSeparate(face, sfail, dpfail, dppass, function_pointer);
    }
    
    static native void nglStencilOpSeparate(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glStencilFuncSeparate(final int face, final int func, final int ref, final int mask) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glStencilFuncSeparate;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglStencilFuncSeparate(face, func, ref, mask, function_pointer);
    }
    
    static native void nglStencilFuncSeparate(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static void glStencilMaskSeparate(final int face, final int mask) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glStencilMaskSeparate;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglStencilMaskSeparate(face, mask, function_pointer);
    }
    
    static native void nglStencilMaskSeparate(final int p0, final int p1, final long p2);
    
    public static void glBlendEquationSeparate(final int modeRGB, final int modeAlpha) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBlendEquationSeparate;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBlendEquationSeparate(modeRGB, modeAlpha, function_pointer);
    }
    
    static native void nglBlendEquationSeparate(final int p0, final int p1, final long p2);
}
