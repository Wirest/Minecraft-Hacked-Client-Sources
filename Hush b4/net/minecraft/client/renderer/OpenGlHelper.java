// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.EXTBlendFuncSeparate;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.ARBMultitexture;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.GL30;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.ARBBufferObject;
import org.lwjgl.opengl.ARBVertexShader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.ARBShaderObjects;
import oshi.hardware.Processor;
import org.lwjgl.opengl.ContextCapabilities;
import oshi.SystemInfo;
import net.minecraft.client.settings.GameSettings;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import optifine.Config;

public class OpenGlHelper
{
    public static boolean nvidia;
    public static boolean field_181063_b;
    public static int GL_FRAMEBUFFER;
    public static int GL_RENDERBUFFER;
    public static int GL_COLOR_ATTACHMENT0;
    public static int GL_DEPTH_ATTACHMENT;
    public static int GL_FRAMEBUFFER_COMPLETE;
    public static int GL_FB_INCOMPLETE_ATTACHMENT;
    public static int GL_FB_INCOMPLETE_MISS_ATTACH;
    public static int GL_FB_INCOMPLETE_DRAW_BUFFER;
    public static int GL_FB_INCOMPLETE_READ_BUFFER;
    private static int framebufferType;
    public static boolean framebufferSupported;
    private static boolean shadersAvailable;
    private static boolean arbShaders;
    public static int GL_LINK_STATUS;
    public static int GL_COMPILE_STATUS;
    public static int GL_VERTEX_SHADER;
    public static int GL_FRAGMENT_SHADER;
    private static boolean arbMultitexture;
    public static int defaultTexUnit;
    public static int lightmapTexUnit;
    public static int GL_TEXTURE2;
    private static boolean arbTextureEnvCombine;
    public static int GL_COMBINE;
    public static int GL_INTERPOLATE;
    public static int GL_PRIMARY_COLOR;
    public static int GL_CONSTANT;
    public static int GL_PREVIOUS;
    public static int GL_COMBINE_RGB;
    public static int GL_SOURCE0_RGB;
    public static int GL_SOURCE1_RGB;
    public static int GL_SOURCE2_RGB;
    public static int GL_OPERAND0_RGB;
    public static int GL_OPERAND1_RGB;
    public static int GL_OPERAND2_RGB;
    public static int GL_COMBINE_ALPHA;
    public static int GL_SOURCE0_ALPHA;
    public static int GL_SOURCE1_ALPHA;
    public static int GL_SOURCE2_ALPHA;
    public static int GL_OPERAND0_ALPHA;
    public static int GL_OPERAND1_ALPHA;
    public static int GL_OPERAND2_ALPHA;
    private static boolean openGL14;
    public static boolean extBlendFuncSeparate;
    public static boolean openGL21;
    public static boolean shadersSupported;
    private static String logText;
    private static String field_183030_aa;
    public static boolean vboSupported;
    public static boolean field_181062_Q;
    private static boolean arbVbo;
    public static int GL_ARRAY_BUFFER;
    public static int GL_STATIC_DRAW;
    private static final String __OBFID = "CL_00001179";
    public static float lastBrightnessX;
    public static float lastBrightnessY;
    
    static {
        OpenGlHelper.logText = "";
        OpenGlHelper.lastBrightnessX = 0.0f;
        OpenGlHelper.lastBrightnessY = 0.0f;
    }
    
    public static void initializeTextures() {
        Config.initDisplay();
        final ContextCapabilities contextcapabilities = GLContext.getCapabilities();
        OpenGlHelper.arbMultitexture = (contextcapabilities.GL_ARB_multitexture && !contextcapabilities.OpenGL13);
        OpenGlHelper.arbTextureEnvCombine = (contextcapabilities.GL_ARB_texture_env_combine && !contextcapabilities.OpenGL13);
        if (OpenGlHelper.arbMultitexture) {
            OpenGlHelper.logText = String.valueOf(OpenGlHelper.logText) + "Using ARB_multitexture.\n";
            OpenGlHelper.defaultTexUnit = 33984;
            OpenGlHelper.lightmapTexUnit = 33985;
            OpenGlHelper.GL_TEXTURE2 = 33986;
        }
        else {
            OpenGlHelper.logText = String.valueOf(OpenGlHelper.logText) + "Using GL 1.3 multitexturing.\n";
            OpenGlHelper.defaultTexUnit = 33984;
            OpenGlHelper.lightmapTexUnit = 33985;
            OpenGlHelper.GL_TEXTURE2 = 33986;
        }
        if (OpenGlHelper.arbTextureEnvCombine) {
            OpenGlHelper.logText = String.valueOf(OpenGlHelper.logText) + "Using ARB_texture_env_combine.\n";
            OpenGlHelper.GL_COMBINE = 34160;
            OpenGlHelper.GL_INTERPOLATE = 34165;
            OpenGlHelper.GL_PRIMARY_COLOR = 34167;
            OpenGlHelper.GL_CONSTANT = 34166;
            OpenGlHelper.GL_PREVIOUS = 34168;
            OpenGlHelper.GL_COMBINE_RGB = 34161;
            OpenGlHelper.GL_SOURCE0_RGB = 34176;
            OpenGlHelper.GL_SOURCE1_RGB = 34177;
            OpenGlHelper.GL_SOURCE2_RGB = 34178;
            OpenGlHelper.GL_OPERAND0_RGB = 34192;
            OpenGlHelper.GL_OPERAND1_RGB = 34193;
            OpenGlHelper.GL_OPERAND2_RGB = 34194;
            OpenGlHelper.GL_COMBINE_ALPHA = 34162;
            OpenGlHelper.GL_SOURCE0_ALPHA = 34184;
            OpenGlHelper.GL_SOURCE1_ALPHA = 34185;
            OpenGlHelper.GL_SOURCE2_ALPHA = 34186;
            OpenGlHelper.GL_OPERAND0_ALPHA = 34200;
            OpenGlHelper.GL_OPERAND1_ALPHA = 34201;
            OpenGlHelper.GL_OPERAND2_ALPHA = 34202;
        }
        else {
            OpenGlHelper.logText = String.valueOf(OpenGlHelper.logText) + "Using GL 1.3 texture combiners.\n";
            OpenGlHelper.GL_COMBINE = 34160;
            OpenGlHelper.GL_INTERPOLATE = 34165;
            OpenGlHelper.GL_PRIMARY_COLOR = 34167;
            OpenGlHelper.GL_CONSTANT = 34166;
            OpenGlHelper.GL_PREVIOUS = 34168;
            OpenGlHelper.GL_COMBINE_RGB = 34161;
            OpenGlHelper.GL_SOURCE0_RGB = 34176;
            OpenGlHelper.GL_SOURCE1_RGB = 34177;
            OpenGlHelper.GL_SOURCE2_RGB = 34178;
            OpenGlHelper.GL_OPERAND0_RGB = 34192;
            OpenGlHelper.GL_OPERAND1_RGB = 34193;
            OpenGlHelper.GL_OPERAND2_RGB = 34194;
            OpenGlHelper.GL_COMBINE_ALPHA = 34162;
            OpenGlHelper.GL_SOURCE0_ALPHA = 34184;
            OpenGlHelper.GL_SOURCE1_ALPHA = 34185;
            OpenGlHelper.GL_SOURCE2_ALPHA = 34186;
            OpenGlHelper.GL_OPERAND0_ALPHA = 34200;
            OpenGlHelper.GL_OPERAND1_ALPHA = 34201;
            OpenGlHelper.GL_OPERAND2_ALPHA = 34202;
        }
        OpenGlHelper.extBlendFuncSeparate = (contextcapabilities.GL_EXT_blend_func_separate && !contextcapabilities.OpenGL14);
        OpenGlHelper.openGL14 = (contextcapabilities.OpenGL14 || contextcapabilities.GL_EXT_blend_func_separate);
        OpenGlHelper.framebufferSupported = (OpenGlHelper.openGL14 && (contextcapabilities.GL_ARB_framebuffer_object || contextcapabilities.GL_EXT_framebuffer_object || contextcapabilities.OpenGL30));
        if (OpenGlHelper.framebufferSupported) {
            OpenGlHelper.logText = String.valueOf(OpenGlHelper.logText) + "Using framebuffer objects because ";
            if (contextcapabilities.OpenGL30) {
                OpenGlHelper.logText = String.valueOf(OpenGlHelper.logText) + "OpenGL 3.0 is supported and separate blending is supported.\n";
                OpenGlHelper.framebufferType = 0;
                OpenGlHelper.GL_FRAMEBUFFER = 36160;
                OpenGlHelper.GL_RENDERBUFFER = 36161;
                OpenGlHelper.GL_COLOR_ATTACHMENT0 = 36064;
                OpenGlHelper.GL_DEPTH_ATTACHMENT = 36096;
                OpenGlHelper.GL_FRAMEBUFFER_COMPLETE = 36053;
                OpenGlHelper.GL_FB_INCOMPLETE_ATTACHMENT = 36054;
                OpenGlHelper.GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
                OpenGlHelper.GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
                OpenGlHelper.GL_FB_INCOMPLETE_READ_BUFFER = 36060;
            }
            else if (contextcapabilities.GL_ARB_framebuffer_object) {
                OpenGlHelper.logText = String.valueOf(OpenGlHelper.logText) + "ARB_framebuffer_object is supported and separate blending is supported.\n";
                OpenGlHelper.framebufferType = 1;
                OpenGlHelper.GL_FRAMEBUFFER = 36160;
                OpenGlHelper.GL_RENDERBUFFER = 36161;
                OpenGlHelper.GL_COLOR_ATTACHMENT0 = 36064;
                OpenGlHelper.GL_DEPTH_ATTACHMENT = 36096;
                OpenGlHelper.GL_FRAMEBUFFER_COMPLETE = 36053;
                OpenGlHelper.GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
                OpenGlHelper.GL_FB_INCOMPLETE_ATTACHMENT = 36054;
                OpenGlHelper.GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
                OpenGlHelper.GL_FB_INCOMPLETE_READ_BUFFER = 36060;
            }
            else if (contextcapabilities.GL_EXT_framebuffer_object) {
                OpenGlHelper.logText = String.valueOf(OpenGlHelper.logText) + "EXT_framebuffer_object is supported.\n";
                OpenGlHelper.framebufferType = 2;
                OpenGlHelper.GL_FRAMEBUFFER = 36160;
                OpenGlHelper.GL_RENDERBUFFER = 36161;
                OpenGlHelper.GL_COLOR_ATTACHMENT0 = 36064;
                OpenGlHelper.GL_DEPTH_ATTACHMENT = 36096;
                OpenGlHelper.GL_FRAMEBUFFER_COMPLETE = 36053;
                OpenGlHelper.GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
                OpenGlHelper.GL_FB_INCOMPLETE_ATTACHMENT = 36054;
                OpenGlHelper.GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
                OpenGlHelper.GL_FB_INCOMPLETE_READ_BUFFER = 36060;
            }
        }
        else {
            OpenGlHelper.logText = String.valueOf(OpenGlHelper.logText) + "Not using framebuffer objects because ";
            OpenGlHelper.logText = String.valueOf(OpenGlHelper.logText) + "OpenGL 1.4 is " + (contextcapabilities.OpenGL14 ? "" : "not ") + "supported, ";
            OpenGlHelper.logText = String.valueOf(OpenGlHelper.logText) + "EXT_blend_func_separate is " + (contextcapabilities.GL_EXT_blend_func_separate ? "" : "not ") + "supported, ";
            OpenGlHelper.logText = String.valueOf(OpenGlHelper.logText) + "OpenGL 3.0 is " + (contextcapabilities.OpenGL30 ? "" : "not ") + "supported, ";
            OpenGlHelper.logText = String.valueOf(OpenGlHelper.logText) + "ARB_framebuffer_object is " + (contextcapabilities.GL_ARB_framebuffer_object ? "" : "not ") + "supported, and ";
            OpenGlHelper.logText = String.valueOf(OpenGlHelper.logText) + "EXT_framebuffer_object is " + (contextcapabilities.GL_EXT_framebuffer_object ? "" : "not ") + "supported.\n";
        }
        OpenGlHelper.openGL21 = contextcapabilities.OpenGL21;
        OpenGlHelper.shadersAvailable = (OpenGlHelper.openGL21 || (contextcapabilities.GL_ARB_vertex_shader && contextcapabilities.GL_ARB_fragment_shader && contextcapabilities.GL_ARB_shader_objects));
        OpenGlHelper.logText = String.valueOf(OpenGlHelper.logText) + "Shaders are " + (OpenGlHelper.shadersAvailable ? "" : "not ") + "available because ";
        if (OpenGlHelper.shadersAvailable) {
            if (contextcapabilities.OpenGL21) {
                OpenGlHelper.logText = String.valueOf(OpenGlHelper.logText) + "OpenGL 2.1 is supported.\n";
                OpenGlHelper.arbShaders = false;
                OpenGlHelper.GL_LINK_STATUS = 35714;
                OpenGlHelper.GL_COMPILE_STATUS = 35713;
                OpenGlHelper.GL_VERTEX_SHADER = 35633;
                OpenGlHelper.GL_FRAGMENT_SHADER = 35632;
            }
            else {
                OpenGlHelper.logText = String.valueOf(OpenGlHelper.logText) + "ARB_shader_objects, ARB_vertex_shader, and ARB_fragment_shader are supported.\n";
                OpenGlHelper.arbShaders = true;
                OpenGlHelper.GL_LINK_STATUS = 35714;
                OpenGlHelper.GL_COMPILE_STATUS = 35713;
                OpenGlHelper.GL_VERTEX_SHADER = 35633;
                OpenGlHelper.GL_FRAGMENT_SHADER = 35632;
            }
        }
        else {
            OpenGlHelper.logText = String.valueOf(OpenGlHelper.logText) + "OpenGL 2.1 is " + (contextcapabilities.OpenGL21 ? "" : "not ") + "supported, ";
            OpenGlHelper.logText = String.valueOf(OpenGlHelper.logText) + "ARB_shader_objects is " + (contextcapabilities.GL_ARB_shader_objects ? "" : "not ") + "supported, ";
            OpenGlHelper.logText = String.valueOf(OpenGlHelper.logText) + "ARB_vertex_shader is " + (contextcapabilities.GL_ARB_vertex_shader ? "" : "not ") + "supported, and ";
            OpenGlHelper.logText = String.valueOf(OpenGlHelper.logText) + "ARB_fragment_shader is " + (contextcapabilities.GL_ARB_fragment_shader ? "" : "not ") + "supported.\n";
        }
        OpenGlHelper.shadersSupported = (OpenGlHelper.framebufferSupported && OpenGlHelper.shadersAvailable);
        final String s = GL11.glGetString(7936).toLowerCase();
        OpenGlHelper.nvidia = s.contains("nvidia");
        OpenGlHelper.arbVbo = (!contextcapabilities.OpenGL15 && contextcapabilities.GL_ARB_vertex_buffer_object);
        OpenGlHelper.vboSupported = (contextcapabilities.OpenGL15 || OpenGlHelper.arbVbo);
        OpenGlHelper.logText = String.valueOf(OpenGlHelper.logText) + "VBOs are " + (OpenGlHelper.vboSupported ? "" : "not ") + "available because ";
        if (OpenGlHelper.vboSupported) {
            if (OpenGlHelper.arbVbo) {
                OpenGlHelper.logText = String.valueOf(OpenGlHelper.logText) + "ARB_vertex_buffer_object is supported.\n";
                OpenGlHelper.GL_STATIC_DRAW = 35044;
                OpenGlHelper.GL_ARRAY_BUFFER = 34962;
            }
            else {
                OpenGlHelper.logText = String.valueOf(OpenGlHelper.logText) + "OpenGL 1.5 is supported.\n";
                OpenGlHelper.GL_STATIC_DRAW = 35044;
                OpenGlHelper.GL_ARRAY_BUFFER = 34962;
            }
        }
        OpenGlHelper.field_181063_b = s.contains("ati");
        if (OpenGlHelper.field_181063_b) {
            if (OpenGlHelper.vboSupported) {
                OpenGlHelper.field_181062_Q = true;
            }
            else {
                GameSettings.Options.RENDER_DISTANCE.setValueMax(16.0f);
            }
        }
        try {
            final Processor[] aprocessor = new SystemInfo().getHardware().getProcessors();
            OpenGlHelper.field_183030_aa = String.format("%dx %s", aprocessor.length, aprocessor[0]).replaceAll("\\s+", " ");
        }
        catch (Throwable t) {}
    }
    
    public static boolean areShadersSupported() {
        return OpenGlHelper.shadersSupported;
    }
    
    public static String getLogText() {
        return OpenGlHelper.logText;
    }
    
    public static int glGetProgrami(final int program, final int pname) {
        return OpenGlHelper.arbShaders ? ARBShaderObjects.glGetObjectParameteriARB(program, pname) : GL20.glGetProgrami(program, pname);
    }
    
    public static void glAttachShader(final int program, final int shaderIn) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glAttachObjectARB(program, shaderIn);
        }
        else {
            GL20.glAttachShader(program, shaderIn);
        }
    }
    
    public static void glDeleteShader(final int p_153180_0_) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glDeleteObjectARB(p_153180_0_);
        }
        else {
            GL20.glDeleteShader(p_153180_0_);
        }
    }
    
    public static int glCreateShader(final int type) {
        return OpenGlHelper.arbShaders ? ARBShaderObjects.glCreateShaderObjectARB(type) : GL20.glCreateShader(type);
    }
    
    public static void glShaderSource(final int shaderIn, final ByteBuffer string) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glShaderSourceARB(shaderIn, string);
        }
        else {
            GL20.glShaderSource(shaderIn, string);
        }
    }
    
    public static void glCompileShader(final int shaderIn) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glCompileShaderARB(shaderIn);
        }
        else {
            GL20.glCompileShader(shaderIn);
        }
    }
    
    public static int glGetShaderi(final int shaderIn, final int pname) {
        return OpenGlHelper.arbShaders ? ARBShaderObjects.glGetObjectParameteriARB(shaderIn, pname) : GL20.glGetShaderi(shaderIn, pname);
    }
    
    public static String glGetShaderInfoLog(final int shaderIn, final int maxLength) {
        return OpenGlHelper.arbShaders ? ARBShaderObjects.glGetInfoLogARB(shaderIn, maxLength) : GL20.glGetShaderInfoLog(shaderIn, maxLength);
    }
    
    public static String glGetProgramInfoLog(final int program, final int maxLength) {
        return OpenGlHelper.arbShaders ? ARBShaderObjects.glGetInfoLogARB(program, maxLength) : GL20.glGetProgramInfoLog(program, maxLength);
    }
    
    public static void glUseProgram(final int program) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUseProgramObjectARB(program);
        }
        else {
            GL20.glUseProgram(program);
        }
    }
    
    public static int glCreateProgram() {
        return OpenGlHelper.arbShaders ? ARBShaderObjects.glCreateProgramObjectARB() : GL20.glCreateProgram();
    }
    
    public static void glDeleteProgram(final int program) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glDeleteObjectARB(program);
        }
        else {
            GL20.glDeleteProgram(program);
        }
    }
    
    public static void glLinkProgram(final int program) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glLinkProgramARB(program);
        }
        else {
            GL20.glLinkProgram(program);
        }
    }
    
    public static int glGetUniformLocation(final int programObj, final CharSequence name) {
        return OpenGlHelper.arbShaders ? ARBShaderObjects.glGetUniformLocationARB(programObj, name) : GL20.glGetUniformLocation(programObj, name);
    }
    
    public static void glUniform1(final int location, final IntBuffer values) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniform1ARB(location, values);
        }
        else {
            GL20.glUniform1(location, values);
        }
    }
    
    public static void glUniform1i(final int location, final int v0) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniform1iARB(location, v0);
        }
        else {
            GL20.glUniform1i(location, v0);
        }
    }
    
    public static void glUniform1(final int location, final FloatBuffer values) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniform1ARB(location, values);
        }
        else {
            GL20.glUniform1(location, values);
        }
    }
    
    public static void glUniform2(final int location, final IntBuffer values) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniform2ARB(location, values);
        }
        else {
            GL20.glUniform2(location, values);
        }
    }
    
    public static void glUniform2(final int location, final FloatBuffer values) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniform2ARB(location, values);
        }
        else {
            GL20.glUniform2(location, values);
        }
    }
    
    public static void glUniform3(final int location, final IntBuffer values) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniform3ARB(location, values);
        }
        else {
            GL20.glUniform3(location, values);
        }
    }
    
    public static void glUniform3(final int location, final FloatBuffer values) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniform3ARB(location, values);
        }
        else {
            GL20.glUniform3(location, values);
        }
    }
    
    public static void glUniform4(final int location, final IntBuffer values) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniform4ARB(location, values);
        }
        else {
            GL20.glUniform4(location, values);
        }
    }
    
    public static void glUniform4(final int location, final FloatBuffer values) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniform4ARB(location, values);
        }
        else {
            GL20.glUniform4(location, values);
        }
    }
    
    public static void glUniformMatrix2(final int location, final boolean transpose, final FloatBuffer matrices) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniformMatrix2ARB(location, transpose, matrices);
        }
        else {
            GL20.glUniformMatrix2(location, transpose, matrices);
        }
    }
    
    public static void glUniformMatrix3(final int location, final boolean transpose, final FloatBuffer matrices) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniformMatrix3ARB(location, transpose, matrices);
        }
        else {
            GL20.glUniformMatrix3(location, transpose, matrices);
        }
    }
    
    public static void glUniformMatrix4(final int location, final boolean transpose, final FloatBuffer matrices) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniformMatrix4ARB(location, transpose, matrices);
        }
        else {
            GL20.glUniformMatrix4(location, transpose, matrices);
        }
    }
    
    public static int glGetAttribLocation(final int p_153164_0_, final CharSequence p_153164_1_) {
        return OpenGlHelper.arbShaders ? ARBVertexShader.glGetAttribLocationARB(p_153164_0_, p_153164_1_) : GL20.glGetAttribLocation(p_153164_0_, p_153164_1_);
    }
    
    public static int glGenBuffers() {
        return OpenGlHelper.arbVbo ? ARBBufferObject.glGenBuffersARB() : GL15.glGenBuffers();
    }
    
    public static void glBindBuffer(final int target, final int buffer) {
        if (OpenGlHelper.arbVbo) {
            ARBBufferObject.glBindBufferARB(target, buffer);
        }
        else {
            GL15.glBindBuffer(target, buffer);
        }
    }
    
    public static void glBufferData(final int target, final ByteBuffer data, final int usage) {
        if (OpenGlHelper.arbVbo) {
            ARBBufferObject.glBufferDataARB(target, data, usage);
        }
        else {
            GL15.glBufferData(target, data, usage);
        }
    }
    
    public static void glDeleteBuffers(final int buffer) {
        if (OpenGlHelper.arbVbo) {
            ARBBufferObject.glDeleteBuffersARB(buffer);
        }
        else {
            GL15.glDeleteBuffers(buffer);
        }
    }
    
    public static boolean useVbo() {
        return !Config.isMultiTexture() && (OpenGlHelper.vboSupported && Minecraft.getMinecraft().gameSettings.useVbo);
    }
    
    public static void glBindFramebuffer(final int target, final int framebufferIn) {
        if (OpenGlHelper.framebufferSupported) {
            switch (OpenGlHelper.framebufferType) {
                case 0: {
                    GL30.glBindFramebuffer(target, framebufferIn);
                    break;
                }
                case 1: {
                    ARBFramebufferObject.glBindFramebuffer(target, framebufferIn);
                    break;
                }
                case 2: {
                    EXTFramebufferObject.glBindFramebufferEXT(target, framebufferIn);
                    break;
                }
            }
        }
    }
    
    public static void glBindRenderbuffer(final int target, final int renderbuffer) {
        if (OpenGlHelper.framebufferSupported) {
            switch (OpenGlHelper.framebufferType) {
                case 0: {
                    GL30.glBindRenderbuffer(target, renderbuffer);
                    break;
                }
                case 1: {
                    ARBFramebufferObject.glBindRenderbuffer(target, renderbuffer);
                    break;
                }
                case 2: {
                    EXTFramebufferObject.glBindRenderbufferEXT(target, renderbuffer);
                    break;
                }
            }
        }
    }
    
    public static void glDeleteRenderbuffers(final int renderbuffer) {
        if (OpenGlHelper.framebufferSupported) {
            switch (OpenGlHelper.framebufferType) {
                case 0: {
                    GL30.glDeleteRenderbuffers(renderbuffer);
                    break;
                }
                case 1: {
                    ARBFramebufferObject.glDeleteRenderbuffers(renderbuffer);
                    break;
                }
                case 2: {
                    EXTFramebufferObject.glDeleteRenderbuffersEXT(renderbuffer);
                    break;
                }
            }
        }
    }
    
    public static void glDeleteFramebuffers(final int framebufferIn) {
        if (OpenGlHelper.framebufferSupported) {
            switch (OpenGlHelper.framebufferType) {
                case 0: {
                    GL30.glDeleteFramebuffers(framebufferIn);
                    break;
                }
                case 1: {
                    ARBFramebufferObject.glDeleteFramebuffers(framebufferIn);
                    break;
                }
                case 2: {
                    EXTFramebufferObject.glDeleteFramebuffersEXT(framebufferIn);
                    break;
                }
            }
        }
    }
    
    public static int glGenFramebuffers() {
        if (!OpenGlHelper.framebufferSupported) {
            return -1;
        }
        switch (OpenGlHelper.framebufferType) {
            case 0: {
                return GL30.glGenFramebuffers();
            }
            case 1: {
                return ARBFramebufferObject.glGenFramebuffers();
            }
            case 2: {
                return EXTFramebufferObject.glGenFramebuffersEXT();
            }
            default: {
                return -1;
            }
        }
    }
    
    public static int glGenRenderbuffers() {
        if (!OpenGlHelper.framebufferSupported) {
            return -1;
        }
        switch (OpenGlHelper.framebufferType) {
            case 0: {
                return GL30.glGenRenderbuffers();
            }
            case 1: {
                return ARBFramebufferObject.glGenRenderbuffers();
            }
            case 2: {
                return EXTFramebufferObject.glGenRenderbuffersEXT();
            }
            default: {
                return -1;
            }
        }
    }
    
    public static void glRenderbufferStorage(final int target, final int internalFormat, final int width, final int height) {
        if (OpenGlHelper.framebufferSupported) {
            switch (OpenGlHelper.framebufferType) {
                case 0: {
                    GL30.glRenderbufferStorage(target, internalFormat, width, height);
                    break;
                }
                case 1: {
                    ARBFramebufferObject.glRenderbufferStorage(target, internalFormat, width, height);
                    break;
                }
                case 2: {
                    EXTFramebufferObject.glRenderbufferStorageEXT(target, internalFormat, width, height);
                    break;
                }
            }
        }
    }
    
    public static void glFramebufferRenderbuffer(final int target, final int attachment, final int renderBufferTarget, final int renderBuffer) {
        if (OpenGlHelper.framebufferSupported) {
            switch (OpenGlHelper.framebufferType) {
                case 0: {
                    GL30.glFramebufferRenderbuffer(target, attachment, renderBufferTarget, renderBuffer);
                    break;
                }
                case 1: {
                    ARBFramebufferObject.glFramebufferRenderbuffer(target, attachment, renderBufferTarget, renderBuffer);
                    break;
                }
                case 2: {
                    EXTFramebufferObject.glFramebufferRenderbufferEXT(target, attachment, renderBufferTarget, renderBuffer);
                    break;
                }
            }
        }
    }
    
    public static int glCheckFramebufferStatus(final int target) {
        if (!OpenGlHelper.framebufferSupported) {
            return -1;
        }
        switch (OpenGlHelper.framebufferType) {
            case 0: {
                return GL30.glCheckFramebufferStatus(target);
            }
            case 1: {
                return ARBFramebufferObject.glCheckFramebufferStatus(target);
            }
            case 2: {
                return EXTFramebufferObject.glCheckFramebufferStatusEXT(target);
            }
            default: {
                return -1;
            }
        }
    }
    
    public static void glFramebufferTexture2D(final int target, final int attachment, final int textarget, final int texture, final int level) {
        if (OpenGlHelper.framebufferSupported) {
            switch (OpenGlHelper.framebufferType) {
                case 0: {
                    GL30.glFramebufferTexture2D(target, attachment, textarget, texture, level);
                    break;
                }
                case 1: {
                    ARBFramebufferObject.glFramebufferTexture2D(target, attachment, textarget, texture, level);
                    break;
                }
                case 2: {
                    EXTFramebufferObject.glFramebufferTexture2DEXT(target, attachment, textarget, texture, level);
                    break;
                }
            }
        }
    }
    
    public static void setActiveTexture(final int texture) {
        if (OpenGlHelper.arbMultitexture) {
            ARBMultitexture.glActiveTextureARB(texture);
        }
        else {
            GL13.glActiveTexture(texture);
        }
    }
    
    public static void setClientActiveTexture(final int texture) {
        if (OpenGlHelper.arbMultitexture) {
            ARBMultitexture.glClientActiveTextureARB(texture);
        }
        else {
            GL13.glClientActiveTexture(texture);
        }
    }
    
    public static void setLightmapTextureCoords(final int target, final float p_77475_1_, final float p_77475_2_) {
        if (OpenGlHelper.arbMultitexture) {
            ARBMultitexture.glMultiTexCoord2fARB(target, p_77475_1_, p_77475_2_);
        }
        else {
            GL13.glMultiTexCoord2f(target, p_77475_1_, p_77475_2_);
        }
        if (target == OpenGlHelper.lightmapTexUnit) {
            OpenGlHelper.lastBrightnessX = p_77475_1_;
            OpenGlHelper.lastBrightnessY = p_77475_2_;
        }
    }
    
    public static void glBlendFunc(final int sFactorRGB, final int dFactorRGB, final int sfactorAlpha, final int dfactorAlpha) {
        if (OpenGlHelper.openGL14) {
            if (OpenGlHelper.extBlendFuncSeparate) {
                EXTBlendFuncSeparate.glBlendFuncSeparateEXT(sFactorRGB, dFactorRGB, sfactorAlpha, dfactorAlpha);
            }
            else {
                GL14.glBlendFuncSeparate(sFactorRGB, dFactorRGB, sfactorAlpha, dfactorAlpha);
            }
        }
        else {
            GL11.glBlendFunc(sFactorRGB, dFactorRGB);
        }
    }
    
    public static boolean isFramebufferEnabled() {
        return !Config.isFastRender() && !Config.isAntialiasing() && (OpenGlHelper.framebufferSupported && Minecraft.getMinecraft().gameSettings.fboEnable);
    }
    
    public static String func_183029_j() {
        return (OpenGlHelper.field_183030_aa == null) ? "<unknown>" : OpenGlHelper.field_183030_aa;
    }
}
