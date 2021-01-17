// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import java.util.UUID;
import java.util.Random;

public class MathHelper
{
    public static final float SQRT_2;
    private static final int SIN_BITS = 12;
    private static final int SIN_MASK = 4095;
    private static final int SIN_COUNT = 4096;
    public static final float PI = 3.1415927f;
    public static final float PI2 = 6.2831855f;
    public static final float PId2 = 1.5707964f;
    private static final float radFull = 6.2831855f;
    private static final float degFull = 360.0f;
    private static final float radToIndex = 651.8986f;
    private static final float degToIndex = 11.377778f;
    public static final float deg2Rad = 0.017453292f;
    private static final float[] SIN_TABLE_FAST;
    public static boolean fastMath;
    private static final float[] SIN_TABLE;
    private static final int[] multiplyDeBruijnBitPosition;
    private static final double field_181163_d;
    private static final double[] field_181164_e;
    private static final double[] field_181165_f;
    private static final String __OBFID = "CL_00001496";
    
    static {
        SQRT_2 = sqrt_float(2.0f);
        SIN_TABLE_FAST = new float[4096];
        MathHelper.fastMath = false;
        SIN_TABLE = new float[65536];
        for (int i = 0; i < 65536; ++i) {
            MathHelper.SIN_TABLE[i] = (float)Math.sin(i * 3.141592653589793 * 2.0 / 65536.0);
        }
        for (int j = 0; j < 4096; ++j) {
            MathHelper.SIN_TABLE_FAST[j] = (float)Math.sin((j + 0.5f) / 4096.0f * 6.2831855f);
        }
        for (int l = 0; l < 360; l += 90) {
            MathHelper.SIN_TABLE_FAST[(int)(l * 11.377778f) & 0xFFF] = (float)Math.sin(l * 0.017453292f);
        }
        multiplyDeBruijnBitPosition = new int[] { 0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9 };
        field_181163_d = Double.longBitsToDouble(4805340802404319232L);
        field_181164_e = new double[257];
        field_181165_f = new double[257];
        for (int k = 0; k < 257; ++k) {
            final double d1 = k / 256.0;
            final double d2 = Math.asin(d1);
            MathHelper.field_181165_f[k] = Math.cos(d2);
            MathHelper.field_181164_e[k] = d2;
        }
    }
    
    public static float sin(final float p_76126_0_) {
        return MathHelper.fastMath ? MathHelper.SIN_TABLE_FAST[(int)(p_76126_0_ * 651.8986f) & 0xFFF] : MathHelper.SIN_TABLE[(int)(p_76126_0_ * 10430.378f) & 0xFFFF];
    }
    
    public static float cos(final float value) {
        return MathHelper.fastMath ? MathHelper.SIN_TABLE_FAST[(int)((value + 1.5707964f) * 651.8986f) & 0xFFF] : MathHelper.SIN_TABLE[(int)(value * 10430.378f + 16384.0f) & 0xFFFF];
    }
    
    public static float sqrt_float(final float value) {
        return (float)Math.sqrt(value);
    }
    
    public static float sqrt_double(final double value) {
        return (float)Math.sqrt(value);
    }
    
    public static int floor_float(final float value) {
        final int i = (int)value;
        return (value < i) ? (i - 1) : i;
    }
    
    public static int truncateDoubleToInt(final double value) {
        return (int)(value + 1024.0) - 1024;
    }
    
    public static int floor_double(final double value) {
        final int i = (int)value;
        return (value < i) ? (i - 1) : i;
    }
    
    public static long floor_double_long(final double value) {
        final long i = (long)value;
        return (value < i) ? (i - 1L) : i;
    }
    
    public static int func_154353_e(final double value) {
        return (int)((value >= 0.0) ? value : (-value + 1.0));
    }
    
    public static float abs(final float value) {
        return (value >= 0.0f) ? value : (-value);
    }
    
    public static int abs_int(final int value) {
        return (value >= 0) ? value : (-value);
    }
    
    public static int ceiling_float_int(final float value) {
        final int i = (int)value;
        return (value > i) ? (i + 1) : i;
    }
    
    public static int ceiling_double_int(final double value) {
        final int i = (int)value;
        return (value > i) ? (i + 1) : i;
    }
    
    public static int clamp_int(final int num, final int min, final int max) {
        return (num < min) ? min : ((num > max) ? max : num);
    }
    
    public static float clamp_float(final float num, final float min, final float max) {
        return (num < min) ? min : ((num > max) ? max : num);
    }
    
    public static double clamp_double(final double num, final double min, final double max) {
        return (num < min) ? min : ((num > max) ? max : num);
    }
    
    public static double denormalizeClamp(final double p_151238_0_, final double p_151238_2_, final double p_151238_4_) {
        return (p_151238_4_ < 0.0) ? p_151238_0_ : ((p_151238_4_ > 1.0) ? p_151238_2_ : (p_151238_0_ + (p_151238_2_ - p_151238_0_) * p_151238_4_));
    }
    
    public static double abs_max(double p_76132_0_, double p_76132_2_) {
        if (p_76132_0_ < 0.0) {
            p_76132_0_ = -p_76132_0_;
        }
        if (p_76132_2_ < 0.0) {
            p_76132_2_ = -p_76132_2_;
        }
        return (p_76132_0_ > p_76132_2_) ? p_76132_0_ : p_76132_2_;
    }
    
    public static int bucketInt(final int p_76137_0_, final int p_76137_1_) {
        return (p_76137_0_ < 0) ? (-((-p_76137_0_ - 1) / p_76137_1_) - 1) : (p_76137_0_ / p_76137_1_);
    }
    
    public static int getRandomIntegerInRange(final Random p_76136_0_, final int p_76136_1_, final int p_76136_2_) {
        return (p_76136_1_ >= p_76136_2_) ? p_76136_1_ : (p_76136_0_.nextInt(p_76136_2_ - p_76136_1_ + 1) + p_76136_1_);
    }
    
    public static float randomFloatClamp(final Random p_151240_0_, final float p_151240_1_, final float p_151240_2_) {
        return (p_151240_1_ >= p_151240_2_) ? p_151240_1_ : (p_151240_0_.nextFloat() * (p_151240_2_ - p_151240_1_) + p_151240_1_);
    }
    
    public static double getRandomDoubleInRange(final Random p_82716_0_, final double p_82716_1_, final double p_82716_3_) {
        return (p_82716_1_ >= p_82716_3_) ? p_82716_1_ : (p_82716_0_.nextDouble() * (p_82716_3_ - p_82716_1_) + p_82716_1_);
    }
    
    public static double average(final long[] values) {
        long i = 0L;
        for (final long j : values) {
            i += j;
        }
        return i / (double)values.length;
    }
    
    public static boolean epsilonEquals(final float p_180185_0_, final float p_180185_1_) {
        return abs(p_180185_1_ - p_180185_0_) < 1.0E-5f;
    }
    
    public static int normalizeAngle(final int p_180184_0_, final int p_180184_1_) {
        return (p_180184_0_ % p_180184_1_ + p_180184_1_) % p_180184_1_;
    }
    
    public static float wrapAngleTo180_float(float value) {
        value %= 360.0f;
        if (value >= 180.0f) {
            value -= 360.0f;
        }
        if (value < -180.0f) {
            value += 360.0f;
        }
        return value;
    }
    
    public static double wrapAngleTo180_double(double value) {
        value %= 360.0;
        if (value >= 180.0) {
            value -= 360.0;
        }
        if (value < -180.0) {
            value += 360.0;
        }
        return value;
    }
    
    public static int parseIntWithDefault(final String p_82715_0_, final int p_82715_1_) {
        try {
            return Integer.parseInt(p_82715_0_);
        }
        catch (Throwable var3) {
            return p_82715_1_;
        }
    }
    
    public static int parseIntWithDefaultAndMax(final String p_82714_0_, final int p_82714_1_, final int p_82714_2_) {
        return Math.max(p_82714_2_, parseIntWithDefault(p_82714_0_, p_82714_1_));
    }
    
    public static double parseDoubleWithDefault(final String p_82712_0_, final double p_82712_1_) {
        try {
            return Double.parseDouble(p_82712_0_);
        }
        catch (Throwable var4) {
            return p_82712_1_;
        }
    }
    
    public static double parseDoubleWithDefaultAndMax(final String p_82713_0_, final double p_82713_1_, final double p_82713_3_) {
        return Math.max(p_82713_3_, parseDoubleWithDefault(p_82713_0_, p_82713_1_));
    }
    
    public static int roundUpToPowerOfTwo(final int value) {
        int i = value - 1;
        i |= i >> 1;
        i |= i >> 2;
        i |= i >> 4;
        i |= i >> 8;
        i |= i >> 16;
        return i + 1;
    }
    
    private static boolean isPowerOfTwo(final int value) {
        return value != 0 && (value & value - 1) == 0x0;
    }
    
    private static int calculateLogBaseTwoDeBruijn(int value) {
        value = (isPowerOfTwo(value) ? value : roundUpToPowerOfTwo(value));
        return MathHelper.multiplyDeBruijnBitPosition[(int)(value * 125613361L >> 27) & 0x1F];
    }
    
    public static int calculateLogBaseTwo(final int value) {
        return calculateLogBaseTwoDeBruijn(value) - (isPowerOfTwo(value) ? 0 : 1);
    }
    
    public static int func_154354_b(final int p_154354_0_, int p_154354_1_) {
        if (p_154354_1_ == 0) {
            return 0;
        }
        if (p_154354_0_ == 0) {
            return p_154354_1_;
        }
        if (p_154354_0_ < 0) {
            p_154354_1_ *= -1;
        }
        final int i = p_154354_0_ % p_154354_1_;
        return (i == 0) ? p_154354_0_ : (p_154354_0_ + p_154354_1_ - i);
    }
    
    public static int func_180183_b(final float p_180183_0_, final float p_180183_1_, final float p_180183_2_) {
        return func_180181_b(floor_float(p_180183_0_ * 255.0f), floor_float(p_180183_1_ * 255.0f), floor_float(p_180183_2_ * 255.0f));
    }
    
    public static int func_180181_b(final int p_180181_0_, final int p_180181_1_, final int p_180181_2_) {
        int i = (p_180181_0_ << 8) + p_180181_1_;
        i = (i << 8) + p_180181_2_;
        return i;
    }
    
    public static int func_180188_d(final int p_180188_0_, final int p_180188_1_) {
        final int i = (p_180188_0_ & 0xFF0000) >> 16;
        final int j = (p_180188_1_ & 0xFF0000) >> 16;
        final int k = (p_180188_0_ & 0xFF00) >> 8;
        final int l = (p_180188_1_ & 0xFF00) >> 8;
        final int i2 = (p_180188_0_ & 0xFF) >> 0;
        final int j2 = (p_180188_1_ & 0xFF) >> 0;
        final int k2 = (int)(i * (float)j / 255.0f);
        final int l2 = (int)(k * (float)l / 255.0f);
        final int i3 = (int)(i2 * (float)j2 / 255.0f);
        return (p_180188_0_ & 0xFF000000) | k2 << 16 | l2 << 8 | i3;
    }
    
    public static double func_181162_h(final double p_181162_0_) {
        return p_181162_0_ - Math.floor(p_181162_0_);
    }
    
    public static long getPositionRandom(final Vec3i pos) {
        return getCoordinateRandom(pos.getX(), pos.getY(), pos.getZ());
    }
    
    public static long getCoordinateRandom(final int x, final int y, final int z) {
        long i = (long)(x * 3129871) ^ z * 116129781L ^ (long)y;
        i = i * i * 42317861L + i * 11L;
        return i;
    }
    
    public static UUID getRandomUuid(final Random rand) {
        final long i = (rand.nextLong() & 0xFFFFFFFFFFFF0FFFL) | 0x4000L;
        final long j = (rand.nextLong() & 0x3FFFFFFFFFFFFFFFL) | Long.MIN_VALUE;
        return new UUID(i, j);
    }
    
    public static double func_181160_c(final double p_181160_0_, final double p_181160_2_, final double p_181160_4_) {
        return (p_181160_0_ - p_181160_2_) / (p_181160_4_ - p_181160_2_);
    }
    
    public static double func_181159_b(double p_181159_0_, double p_181159_2_) {
        final double d0 = p_181159_2_ * p_181159_2_ + p_181159_0_ * p_181159_0_;
        if (Double.isNaN(d0)) {
            return Double.NaN;
        }
        final boolean flag = p_181159_0_ < 0.0;
        if (flag) {
            p_181159_0_ = -p_181159_0_;
        }
        final boolean flag2 = p_181159_2_ < 0.0;
        if (flag2) {
            p_181159_2_ = -p_181159_2_;
        }
        final boolean flag3 = p_181159_0_ > p_181159_2_;
        if (flag3) {
            final double d2 = p_181159_2_;
            p_181159_2_ = p_181159_0_;
            p_181159_0_ = d2;
        }
        final double d3 = func_181161_i(d0);
        p_181159_2_ *= d3;
        p_181159_0_ *= d3;
        final double d4 = MathHelper.field_181163_d + p_181159_0_;
        final int i = (int)Double.doubleToRawLongBits(d4);
        final double d5 = MathHelper.field_181164_e[i];
        final double d6 = MathHelper.field_181165_f[i];
        final double d7 = d4 - MathHelper.field_181163_d;
        final double d8 = p_181159_0_ * d6 - p_181159_2_ * d7;
        final double d9 = (6.0 + d8 * d8) * d8 * 0.16666666666666666;
        double d10 = d5 + d9;
        if (flag3) {
            d10 = 1.5707963267948966 - d10;
        }
        if (flag2) {
            d10 = 3.141592653589793 - d10;
        }
        if (flag) {
            d10 = -d10;
        }
        return d10;
    }
    
    public static double func_181161_i(double p_181161_0_) {
        final double d0 = 0.5 * p_181161_0_;
        long i = Double.doubleToRawLongBits(p_181161_0_);
        i = 6910469410427058090L - (i >> 1);
        p_181161_0_ = Double.longBitsToDouble(i);
        p_181161_0_ *= 1.5 - d0 * p_181161_0_ * p_181161_0_;
        return p_181161_0_;
    }
    
    public static int func_181758_c(final float p_181758_0_, final float p_181758_1_, final float p_181758_2_) {
        final int i = (int)(p_181758_0_ * 6.0f) % 6;
        final float f = p_181758_0_ * 6.0f - i;
        final float f2 = p_181758_2_ * (1.0f - p_181758_1_);
        final float f3 = p_181758_2_ * (1.0f - f * p_181758_1_);
        final float f4 = p_181758_2_ * (1.0f - (1.0f - f) * p_181758_1_);
        float f5 = 0.0f;
        float f6 = 0.0f;
        float f7 = 0.0f;
        switch (i) {
            case 0: {
                f5 = p_181758_2_;
                f6 = f4;
                f7 = f2;
                break;
            }
            case 1: {
                f5 = f3;
                f6 = p_181758_2_;
                f7 = f2;
                break;
            }
            case 2: {
                f5 = f2;
                f6 = p_181758_2_;
                f7 = f4;
                break;
            }
            case 3: {
                f5 = f2;
                f6 = f3;
                f7 = p_181758_2_;
                break;
            }
            case 4: {
                f5 = f4;
                f6 = f2;
                f7 = p_181758_2_;
                break;
            }
            case 5: {
                f5 = p_181758_2_;
                f6 = f2;
                f7 = f3;
                break;
            }
            default: {
                throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + p_181758_0_ + ", " + p_181758_1_ + ", " + p_181758_2_);
            }
        }
        final int j = clamp_int((int)(f5 * 255.0f), 0, 255);
        final int k = clamp_int((int)(f6 * 255.0f), 0, 255);
        final int l = clamp_int((int)(f7 * 255.0f), 0, 255);
        return j << 16 | k << 8 | l;
    }
}
