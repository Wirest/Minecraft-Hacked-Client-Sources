// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class ARBSamplerObjects
{
    public static final int GL_SAMPLER_BINDING = 35097;
    
    private ARBSamplerObjects() {
    }
    
    public static void glGenSamplers(final IntBuffer samplers) {
        GL33.glGenSamplers(samplers);
    }
    
    public static int glGenSamplers() {
        return GL33.glGenSamplers();
    }
    
    public static void glDeleteSamplers(final IntBuffer samplers) {
        GL33.glDeleteSamplers(samplers);
    }
    
    public static void glDeleteSamplers(final int sampler) {
        GL33.glDeleteSamplers(sampler);
    }
    
    public static boolean glIsSampler(final int sampler) {
        return GL33.glIsSampler(sampler);
    }
    
    public static void glBindSampler(final int unit, final int sampler) {
        GL33.glBindSampler(unit, sampler);
    }
    
    public static void glSamplerParameteri(final int sampler, final int pname, final int param) {
        GL33.glSamplerParameteri(sampler, pname, param);
    }
    
    public static void glSamplerParameterf(final int sampler, final int pname, final float param) {
        GL33.glSamplerParameterf(sampler, pname, param);
    }
    
    public static void glSamplerParameter(final int sampler, final int pname, final IntBuffer params) {
        GL33.glSamplerParameter(sampler, pname, params);
    }
    
    public static void glSamplerParameter(final int sampler, final int pname, final FloatBuffer params) {
        GL33.glSamplerParameter(sampler, pname, params);
    }
    
    public static void glSamplerParameterI(final int sampler, final int pname, final IntBuffer params) {
        GL33.glSamplerParameterI(sampler, pname, params);
    }
    
    public static void glSamplerParameterIu(final int sampler, final int pname, final IntBuffer params) {
        GL33.glSamplerParameterIu(sampler, pname, params);
    }
    
    public static void glGetSamplerParameter(final int sampler, final int pname, final IntBuffer params) {
        GL33.glGetSamplerParameter(sampler, pname, params);
    }
    
    public static int glGetSamplerParameteri(final int sampler, final int pname) {
        return GL33.glGetSamplerParameteri(sampler, pname);
    }
    
    public static void glGetSamplerParameter(final int sampler, final int pname, final FloatBuffer params) {
        GL33.glGetSamplerParameter(sampler, pname, params);
    }
    
    public static float glGetSamplerParameterf(final int sampler, final int pname) {
        return GL33.glGetSamplerParameterf(sampler, pname);
    }
    
    public static void glGetSamplerParameterI(final int sampler, final int pname, final IntBuffer params) {
        GL33.glGetSamplerParameterI(sampler, pname, params);
    }
    
    public static int glGetSamplerParameterIi(final int sampler, final int pname) {
        return GL33.glGetSamplerParameterIi(sampler, pname);
    }
    
    public static void glGetSamplerParameterIu(final int sampler, final int pname, final IntBuffer params) {
        GL33.glGetSamplerParameterIu(sampler, pname, params);
    }
    
    public static int glGetSamplerParameterIui(final int sampler, final int pname) {
        return GL33.glGetSamplerParameterIui(sampler, pname);
    }
}
