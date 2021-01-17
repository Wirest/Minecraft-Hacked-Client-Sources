// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen;

import java.util.Random;

public class NoiseGeneratorPerlin extends NoiseGenerator
{
    private NoiseGeneratorSimplex[] field_151603_a;
    private int field_151602_b;
    
    public NoiseGeneratorPerlin(final Random p_i45470_1_, final int p_i45470_2_) {
        this.field_151602_b = p_i45470_2_;
        this.field_151603_a = new NoiseGeneratorSimplex[p_i45470_2_];
        for (int i = 0; i < p_i45470_2_; ++i) {
            this.field_151603_a[i] = new NoiseGeneratorSimplex(p_i45470_1_);
        }
    }
    
    public double func_151601_a(final double p_151601_1_, final double p_151601_3_) {
        double d0 = 0.0;
        double d2 = 1.0;
        for (int i = 0; i < this.field_151602_b; ++i) {
            d0 += this.field_151603_a[i].func_151605_a(p_151601_1_ * d2, p_151601_3_ * d2) / d2;
            d2 /= 2.0;
        }
        return d0;
    }
    
    public double[] func_151599_a(final double[] p_151599_1_, final double p_151599_2_, final double p_151599_4_, final int p_151599_6_, final int p_151599_7_, final double p_151599_8_, final double p_151599_10_, final double p_151599_12_) {
        return this.func_151600_a(p_151599_1_, p_151599_2_, p_151599_4_, p_151599_6_, p_151599_7_, p_151599_8_, p_151599_10_, p_151599_12_, 0.5);
    }
    
    public double[] func_151600_a(double[] p_151600_1_, final double p_151600_2_, final double p_151600_4_, final int p_151600_6_, final int p_151600_7_, final double p_151600_8_, final double p_151600_10_, final double p_151600_12_, final double p_151600_14_) {
        if (p_151600_1_ != null && p_151600_1_.length >= p_151600_6_ * p_151600_7_) {
            for (int i = 0; i < p_151600_1_.length; ++i) {
                p_151600_1_[i] = 0.0;
            }
        }
        else {
            p_151600_1_ = new double[p_151600_6_ * p_151600_7_];
        }
        double d1 = 1.0;
        double d2 = 1.0;
        for (int j = 0; j < this.field_151602_b; ++j) {
            this.field_151603_a[j].func_151606_a(p_151600_1_, p_151600_2_, p_151600_4_, p_151600_6_, p_151600_7_, p_151600_8_ * d2 * d1, p_151600_10_ * d2 * d1, 0.55 / d1);
            d2 *= p_151600_12_;
            d1 *= p_151600_14_;
        }
        return p_151600_1_;
    }
}
