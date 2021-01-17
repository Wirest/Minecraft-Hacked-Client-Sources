// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.MemoryUtil;
import java.nio.FloatBuffer;
import org.lwjgl.BufferChecks;

public final class ATIFragmentShader
{
    public static final int GL_FRAGMENT_SHADER_ATI = 35104;
    public static final int GL_REG_0_ATI = 35105;
    public static final int GL_REG_1_ATI = 35106;
    public static final int GL_REG_2_ATI = 35107;
    public static final int GL_REG_3_ATI = 35108;
    public static final int GL_REG_4_ATI = 35109;
    public static final int GL_REG_5_ATI = 35110;
    public static final int GL_REG_6_ATI = 35111;
    public static final int GL_REG_7_ATI = 35112;
    public static final int GL_REG_8_ATI = 35113;
    public static final int GL_REG_9_ATI = 35114;
    public static final int GL_REG_10_ATI = 35115;
    public static final int GL_REG_11_ATI = 35116;
    public static final int GL_REG_12_ATI = 35117;
    public static final int GL_REG_13_ATI = 35118;
    public static final int GL_REG_14_ATI = 35119;
    public static final int GL_REG_15_ATI = 35120;
    public static final int GL_REG_16_ATI = 35121;
    public static final int GL_REG_17_ATI = 35122;
    public static final int GL_REG_18_ATI = 35123;
    public static final int GL_REG_19_ATI = 35124;
    public static final int GL_REG_20_ATI = 35125;
    public static final int GL_REG_21_ATI = 35126;
    public static final int GL_REG_22_ATI = 35127;
    public static final int GL_REG_23_ATI = 35128;
    public static final int GL_REG_24_ATI = 35129;
    public static final int GL_REG_25_ATI = 35130;
    public static final int GL_REG_26_ATI = 35131;
    public static final int GL_REG_27_ATI = 35132;
    public static final int GL_REG_28_ATI = 35133;
    public static final int GL_REG_29_ATI = 35134;
    public static final int GL_REG_30_ATI = 35135;
    public static final int GL_REG_31_ATI = 35136;
    public static final int GL_CON_0_ATI = 35137;
    public static final int GL_CON_1_ATI = 35138;
    public static final int GL_CON_2_ATI = 35139;
    public static final int GL_CON_3_ATI = 35140;
    public static final int GL_CON_4_ATI = 35141;
    public static final int GL_CON_5_ATI = 35142;
    public static final int GL_CON_6_ATI = 35143;
    public static final int GL_CON_7_ATI = 35144;
    public static final int GL_CON_8_ATI = 35145;
    public static final int GL_CON_9_ATI = 35146;
    public static final int GL_CON_10_ATI = 35147;
    public static final int GL_CON_11_ATI = 35148;
    public static final int GL_CON_12_ATI = 35149;
    public static final int GL_CON_13_ATI = 35150;
    public static final int GL_CON_14_ATI = 35151;
    public static final int GL_CON_15_ATI = 35152;
    public static final int GL_CON_16_ATI = 35153;
    public static final int GL_CON_17_ATI = 35154;
    public static final int GL_CON_18_ATI = 35155;
    public static final int GL_CON_19_ATI = 35156;
    public static final int GL_CON_20_ATI = 35157;
    public static final int GL_CON_21_ATI = 35158;
    public static final int GL_CON_22_ATI = 35159;
    public static final int GL_CON_23_ATI = 35160;
    public static final int GL_CON_24_ATI = 35161;
    public static final int GL_CON_25_ATI = 35162;
    public static final int GL_CON_26_ATI = 35163;
    public static final int GL_CON_27_ATI = 35164;
    public static final int GL_CON_28_ATI = 35165;
    public static final int GL_CON_29_ATI = 35166;
    public static final int GL_CON_30_ATI = 35167;
    public static final int GL_CON_31_ATI = 35168;
    public static final int GL_MOV_ATI = 35169;
    public static final int GL_ADD_ATI = 35171;
    public static final int GL_MUL_ATI = 35172;
    public static final int GL_SUB_ATI = 35173;
    public static final int GL_DOT3_ATI = 35174;
    public static final int GL_DOT4_ATI = 35175;
    public static final int GL_MAD_ATI = 35176;
    public static final int GL_LERP_ATI = 35177;
    public static final int GL_CND_ATI = 35178;
    public static final int GL_CND0_ATI = 35179;
    public static final int GL_DOT2_ADD_ATI = 35180;
    public static final int GL_SECONDARY_INTERPOLATOR_ATI = 35181;
    public static final int GL_NUM_FRAGMENT_REGISTERS_ATI = 35182;
    public static final int GL_NUM_FRAGMENT_CONSTANTS_ATI = 35183;
    public static final int GL_NUM_PASSES_ATI = 35184;
    public static final int GL_NUM_INSTRUCTIONS_PER_PASS_ATI = 35185;
    public static final int GL_NUM_INSTRUCTIONS_TOTAL_ATI = 35186;
    public static final int GL_NUM_INPUT_INTERPOLATOR_COMPONENTS_ATI = 35187;
    public static final int GL_NUM_LOOPBACK_COMPONENTS_ATI = 35188;
    public static final int GL_COLOR_ALPHA_PAIRING_ATI = 35189;
    public static final int GL_SWIZZLE_STR_ATI = 35190;
    public static final int GL_SWIZZLE_STQ_ATI = 35191;
    public static final int GL_SWIZZLE_STR_DR_ATI = 35192;
    public static final int GL_SWIZZLE_STQ_DQ_ATI = 35193;
    public static final int GL_SWIZZLE_STRQ_ATI = 35194;
    public static final int GL_SWIZZLE_STRQ_DQ_ATI = 35195;
    public static final int GL_RED_BIT_ATI = 1;
    public static final int GL_GREEN_BIT_ATI = 2;
    public static final int GL_BLUE_BIT_ATI = 4;
    public static final int GL_2X_BIT_ATI = 1;
    public static final int GL_4X_BIT_ATI = 2;
    public static final int GL_8X_BIT_ATI = 4;
    public static final int GL_HALF_BIT_ATI = 8;
    public static final int GL_QUARTER_BIT_ATI = 16;
    public static final int GL_EIGHTH_BIT_ATI = 32;
    public static final int GL_SATURATE_BIT_ATI = 64;
    public static final int GL_COMP_BIT_ATI = 2;
    public static final int GL_NEGATE_BIT_ATI = 4;
    public static final int GL_BIAS_BIT_ATI = 8;
    
    private ATIFragmentShader() {
    }
    
    public static int glGenFragmentShadersATI(final int range) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenFragmentShadersATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglGenFragmentShadersATI(range, function_pointer);
        return __result;
    }
    
    static native int nglGenFragmentShadersATI(final int p0, final long p1);
    
    public static void glBindFragmentShaderATI(final int id) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBindFragmentShaderATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBindFragmentShaderATI(id, function_pointer);
    }
    
    static native void nglBindFragmentShaderATI(final int p0, final long p1);
    
    public static void glDeleteFragmentShaderATI(final int id) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeleteFragmentShaderATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDeleteFragmentShaderATI(id, function_pointer);
    }
    
    static native void nglDeleteFragmentShaderATI(final int p0, final long p1);
    
    public static void glBeginFragmentShaderATI() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glBeginFragmentShaderATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglBeginFragmentShaderATI(function_pointer);
    }
    
    static native void nglBeginFragmentShaderATI(final long p0);
    
    public static void glEndFragmentShaderATI() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glEndFragmentShaderATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglEndFragmentShaderATI(function_pointer);
    }
    
    static native void nglEndFragmentShaderATI(final long p0);
    
    public static void glPassTexCoordATI(final int dst, final int coord, final int swizzle) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPassTexCoordATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglPassTexCoordATI(dst, coord, swizzle, function_pointer);
    }
    
    static native void nglPassTexCoordATI(final int p0, final int p1, final int p2, final long p3);
    
    public static void glSampleMapATI(final int dst, final int interp, final int swizzle) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSampleMapATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglSampleMapATI(dst, interp, swizzle, function_pointer);
    }
    
    static native void nglSampleMapATI(final int p0, final int p1, final int p2, final long p3);
    
    public static void glColorFragmentOp1ATI(final int op, final int dst, final int dstMask, final int dstMod, final int arg1, final int arg1Rep, final int arg1Mod) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorFragmentOp1ATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglColorFragmentOp1ATI(op, dst, dstMask, dstMod, arg1, arg1Rep, arg1Mod, function_pointer);
    }
    
    static native void nglColorFragmentOp1ATI(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final long p7);
    
    public static void glColorFragmentOp2ATI(final int op, final int dst, final int dstMask, final int dstMod, final int arg1, final int arg1Rep, final int arg1Mod, final int arg2, final int arg2Rep, final int arg2Mod) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorFragmentOp2ATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglColorFragmentOp2ATI(op, dst, dstMask, dstMod, arg1, arg1Rep, arg1Mod, arg2, arg2Rep, arg2Mod, function_pointer);
    }
    
    static native void nglColorFragmentOp2ATI(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final long p10);
    
    public static void glColorFragmentOp3ATI(final int op, final int dst, final int dstMask, final int dstMod, final int arg1, final int arg1Rep, final int arg1Mod, final int arg2, final int arg2Rep, final int arg2Mod, final int arg3, final int arg3Rep, final int arg3Mod) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glColorFragmentOp3ATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglColorFragmentOp3ATI(op, dst, dstMask, dstMod, arg1, arg1Rep, arg1Mod, arg2, arg2Rep, arg2Mod, arg3, arg3Rep, arg3Mod, function_pointer);
    }
    
    static native void nglColorFragmentOp3ATI(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final int p10, final int p11, final int p12, final long p13);
    
    public static void glAlphaFragmentOp1ATI(final int op, final int dst, final int dstMod, final int arg1, final int arg1Rep, final int arg1Mod) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glAlphaFragmentOp1ATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglAlphaFragmentOp1ATI(op, dst, dstMod, arg1, arg1Rep, arg1Mod, function_pointer);
    }
    
    static native void nglAlphaFragmentOp1ATI(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6);
    
    public static void glAlphaFragmentOp2ATI(final int op, final int dst, final int dstMod, final int arg1, final int arg1Rep, final int arg1Mod, final int arg2, final int arg2Rep, final int arg2Mod) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glAlphaFragmentOp2ATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglAlphaFragmentOp2ATI(op, dst, dstMod, arg1, arg1Rep, arg1Mod, arg2, arg2Rep, arg2Mod, function_pointer);
    }
    
    static native void nglAlphaFragmentOp2ATI(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final long p9);
    
    public static void glAlphaFragmentOp3ATI(final int op, final int dst, final int dstMod, final int arg1, final int arg1Rep, final int arg1Mod, final int arg2, final int arg2Rep, final int arg2Mod, final int arg3, final int arg3Rep, final int arg3Mod) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glAlphaFragmentOp3ATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglAlphaFragmentOp3ATI(op, dst, dstMod, arg1, arg1Rep, arg1Mod, arg2, arg2Rep, arg2Mod, arg3, arg3Rep, arg3Mod, function_pointer);
    }
    
    static native void nglAlphaFragmentOp3ATI(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9, final int p10, final int p11, final long p12);
    
    public static void glSetFragmentShaderConstantATI(final int dst, final FloatBuffer pfValue) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glSetFragmentShaderConstantATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(pfValue, 4);
        nglSetFragmentShaderConstantATI(dst, MemoryUtil.getAddress(pfValue), function_pointer);
    }
    
    static native void nglSetFragmentShaderConstantATI(final int p0, final long p1, final long p2);
}
