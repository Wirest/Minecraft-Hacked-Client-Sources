// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.realms;

import org.apache.commons.lang3.StringUtils;
import java.util.Random;
import net.minecraft.util.MathHelper;

public class RealmsMth
{
    public static float sin(final float p_sin_0_) {
        return MathHelper.sin(p_sin_0_);
    }
    
    public static double nextDouble(final Random p_nextDouble_0_, final double p_nextDouble_1_, final double p_nextDouble_3_) {
        return MathHelper.getRandomDoubleInRange(p_nextDouble_0_, p_nextDouble_1_, p_nextDouble_3_);
    }
    
    public static int ceil(final float p_ceil_0_) {
        return MathHelper.ceiling_float_int(p_ceil_0_);
    }
    
    public static int floor(final double p_floor_0_) {
        return MathHelper.floor_double(p_floor_0_);
    }
    
    public static int intFloorDiv(final int p_intFloorDiv_0_, final int p_intFloorDiv_1_) {
        return MathHelper.bucketInt(p_intFloorDiv_0_, p_intFloorDiv_1_);
    }
    
    public static float abs(final float p_abs_0_) {
        return MathHelper.abs(p_abs_0_);
    }
    
    public static int clamp(final int p_clamp_0_, final int p_clamp_1_, final int p_clamp_2_) {
        return MathHelper.clamp_int(p_clamp_0_, p_clamp_1_, p_clamp_2_);
    }
    
    public static double clampedLerp(final double p_clampedLerp_0_, final double p_clampedLerp_2_, final double p_clampedLerp_4_) {
        return MathHelper.denormalizeClamp(p_clampedLerp_0_, p_clampedLerp_2_, p_clampedLerp_4_);
    }
    
    public static int ceil(final double p_ceil_0_) {
        return MathHelper.ceiling_double_int(p_ceil_0_);
    }
    
    public static boolean isEmpty(final String p_isEmpty_0_) {
        return StringUtils.isEmpty(p_isEmpty_0_);
    }
    
    public static long lfloor(final double p_lfloor_0_) {
        return MathHelper.floor_double_long(p_lfloor_0_);
    }
    
    public static float sqrt(final double p_sqrt_0_) {
        return MathHelper.sqrt_double(p_sqrt_0_);
    }
    
    public static double clamp(final double p_clamp_0_, final double p_clamp_2_, final double p_clamp_4_) {
        return MathHelper.clamp_double(p_clamp_0_, p_clamp_2_, p_clamp_4_);
    }
    
    public static int getInt(final String p_getInt_0_, final int p_getInt_1_) {
        return MathHelper.parseIntWithDefault(p_getInt_0_, p_getInt_1_);
    }
    
    public static double getDouble(final String p_getDouble_0_, final double p_getDouble_1_) {
        return MathHelper.parseDoubleWithDefault(p_getDouble_0_, p_getDouble_1_);
    }
    
    public static int log2(final int p_log2_0_) {
        return MathHelper.calculateLogBaseTwo(p_log2_0_);
    }
    
    public static int absFloor(final double p_absFloor_0_) {
        return MathHelper.func_154353_e(p_absFloor_0_);
    }
    
    public static int smallestEncompassingPowerOfTwo(final int p_smallestEncompassingPowerOfTwo_0_) {
        return MathHelper.roundUpToPowerOfTwo(p_smallestEncompassingPowerOfTwo_0_);
    }
    
    public static float sqrt(final float p_sqrt_0_) {
        return MathHelper.sqrt_float(p_sqrt_0_);
    }
    
    public static float cos(final float p_cos_0_) {
        return MathHelper.cos(p_cos_0_);
    }
    
    public static int getInt(final String p_getInt_0_, final int p_getInt_1_, final int p_getInt_2_) {
        return MathHelper.parseIntWithDefaultAndMax(p_getInt_0_, p_getInt_1_, p_getInt_2_);
    }
    
    public static int fastFloor(final double p_fastFloor_0_) {
        return MathHelper.truncateDoubleToInt(p_fastFloor_0_);
    }
    
    public static double absMax(final double p_absMax_0_, final double p_absMax_2_) {
        return MathHelper.abs_max(p_absMax_0_, p_absMax_2_);
    }
    
    public static float nextFloat(final Random p_nextFloat_0_, final float p_nextFloat_1_, final float p_nextFloat_2_) {
        return MathHelper.randomFloatClamp(p_nextFloat_0_, p_nextFloat_1_, p_nextFloat_2_);
    }
    
    public static double wrapDegrees(final double p_wrapDegrees_0_) {
        return MathHelper.wrapAngleTo180_double(p_wrapDegrees_0_);
    }
    
    public static float wrapDegrees(final float p_wrapDegrees_0_) {
        return MathHelper.wrapAngleTo180_float(p_wrapDegrees_0_);
    }
    
    public static float clamp(final float p_clamp_0_, final float p_clamp_1_, final float p_clamp_2_) {
        return MathHelper.clamp_float(p_clamp_0_, p_clamp_1_, p_clamp_2_);
    }
    
    public static double getDouble(final String p_getDouble_0_, final double p_getDouble_1_, final double p_getDouble_3_) {
        return MathHelper.parseDoubleWithDefaultAndMax(p_getDouble_0_, p_getDouble_1_, p_getDouble_3_);
    }
    
    public static int roundUp(final int p_roundUp_0_, final int p_roundUp_1_) {
        return MathHelper.func_154354_b(p_roundUp_0_, p_roundUp_1_);
    }
    
    public static double average(final long[] p_average_0_) {
        return MathHelper.average(p_average_0_);
    }
    
    public static int floor(final float p_floor_0_) {
        return MathHelper.floor_float(p_floor_0_);
    }
    
    public static int abs(final int p_abs_0_) {
        return MathHelper.abs_int(p_abs_0_);
    }
    
    public static int nextInt(final Random p_nextInt_0_, final int p_nextInt_1_, final int p_nextInt_2_) {
        return MathHelper.getRandomIntegerInRange(p_nextInt_0_, p_nextInt_1_, p_nextInt_2_);
    }
}
