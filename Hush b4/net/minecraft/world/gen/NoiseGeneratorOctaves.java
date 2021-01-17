// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen;

import net.minecraft.util.MathHelper;
import java.util.Random;

public class NoiseGeneratorOctaves extends NoiseGenerator
{
    private NoiseGeneratorImproved[] generatorCollection;
    private int octaves;
    
    public NoiseGeneratorOctaves(final Random p_i2111_1_, final int p_i2111_2_) {
        this.octaves = p_i2111_2_;
        this.generatorCollection = new NoiseGeneratorImproved[p_i2111_2_];
        for (int i = 0; i < p_i2111_2_; ++i) {
            this.generatorCollection[i] = new NoiseGeneratorImproved(p_i2111_1_);
        }
    }
    
    public double[] generateNoiseOctaves(double[] p_76304_1_, final int p_76304_2_, final int p_76304_3_, final int p_76304_4_, final int p_76304_5_, final int p_76304_6_, final int p_76304_7_, final double p_76304_8_, final double p_76304_10_, final double p_76304_12_) {
        if (p_76304_1_ == null) {
            p_76304_1_ = new double[p_76304_5_ * p_76304_6_ * p_76304_7_];
        }
        else {
            for (int i = 0; i < p_76304_1_.length; ++i) {
                p_76304_1_[i] = 0.0;
            }
        }
        double d3 = 1.0;
        for (int j = 0; j < this.octaves; ++j) {
            double d4 = p_76304_2_ * d3 * p_76304_8_;
            final double d5 = p_76304_3_ * d3 * p_76304_10_;
            double d6 = p_76304_4_ * d3 * p_76304_12_;
            long k = MathHelper.floor_double_long(d4);
            long l = MathHelper.floor_double_long(d6);
            d4 -= k;
            d6 -= l;
            k %= 16777216L;
            l %= 16777216L;
            d4 += k;
            d6 += l;
            this.generatorCollection[j].populateNoiseArray(p_76304_1_, d4, d5, d6, p_76304_5_, p_76304_6_, p_76304_7_, p_76304_8_ * d3, p_76304_10_ * d3, p_76304_12_ * d3, d3);
            d3 /= 2.0;
        }
        return p_76304_1_;
    }
    
    public double[] generateNoiseOctaves(final double[] p_76305_1_, final int p_76305_2_, final int p_76305_3_, final int p_76305_4_, final int p_76305_5_, final double p_76305_6_, final double p_76305_8_, final double p_76305_10_) {
        return this.generateNoiseOctaves(p_76305_1_, p_76305_2_, 10, p_76305_3_, p_76305_4_, 1, p_76305_5_, p_76305_6_, 1.0, p_76305_8_);
    }
}
