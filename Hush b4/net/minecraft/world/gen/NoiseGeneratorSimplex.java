// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen;

import java.util.Random;

public class NoiseGeneratorSimplex
{
    private static int[][] field_151611_e;
    public static final double field_151614_a;
    private int[] field_151608_f;
    public double field_151612_b;
    public double field_151613_c;
    public double field_151610_d;
    private static final double field_151609_g;
    private static final double field_151615_h;
    
    static {
        NoiseGeneratorSimplex.field_151611_e = new int[][] { { 1, 1, 0 }, { -1, 1, 0 }, { 1, -1, 0 }, { -1, -1, 0 }, { 1, 0, 1 }, { -1, 0, 1 }, { 1, 0, -1 }, { -1, 0, -1 }, { 0, 1, 1 }, { 0, -1, 1 }, { 0, 1, -1 }, { 0, -1, -1 } };
        field_151614_a = Math.sqrt(3.0);
        field_151609_g = 0.5 * (NoiseGeneratorSimplex.field_151614_a - 1.0);
        field_151615_h = (3.0 - NoiseGeneratorSimplex.field_151614_a) / 6.0;
    }
    
    public NoiseGeneratorSimplex() {
        this(new Random());
    }
    
    public NoiseGeneratorSimplex(final Random p_i45471_1_) {
        this.field_151608_f = new int[512];
        this.field_151612_b = p_i45471_1_.nextDouble() * 256.0;
        this.field_151613_c = p_i45471_1_.nextDouble() * 256.0;
        this.field_151610_d = p_i45471_1_.nextDouble() * 256.0;
        for (int i = 0; i < 256; this.field_151608_f[i] = i++) {}
        for (int l = 0; l < 256; ++l) {
            final int j = p_i45471_1_.nextInt(256 - l) + l;
            final int k = this.field_151608_f[l];
            this.field_151608_f[l] = this.field_151608_f[j];
            this.field_151608_f[j] = k;
            this.field_151608_f[l + 256] = this.field_151608_f[l];
        }
    }
    
    private static int func_151607_a(final double p_151607_0_) {
        return (p_151607_0_ > 0.0) ? ((int)p_151607_0_) : ((int)p_151607_0_ - 1);
    }
    
    private static double func_151604_a(final int[] p_151604_0_, final double p_151604_1_, final double p_151604_3_) {
        return p_151604_0_[0] * p_151604_1_ + p_151604_0_[1] * p_151604_3_;
    }
    
    public double func_151605_a(final double p_151605_1_, final double p_151605_3_) {
        final double d3 = 0.5 * (NoiseGeneratorSimplex.field_151614_a - 1.0);
        final double d4 = (p_151605_1_ + p_151605_3_) * d3;
        final int i = func_151607_a(p_151605_1_ + d4);
        final int j = func_151607_a(p_151605_3_ + d4);
        final double d5 = (3.0 - NoiseGeneratorSimplex.field_151614_a) / 6.0;
        final double d6 = (i + j) * d5;
        final double d7 = i - d6;
        final double d8 = j - d6;
        final double d9 = p_151605_1_ - d7;
        final double d10 = p_151605_3_ - d8;
        int k;
        int l;
        if (d9 > d10) {
            k = 1;
            l = 0;
        }
        else {
            k = 0;
            l = 1;
        }
        final double d11 = d9 - k + d5;
        final double d12 = d10 - l + d5;
        final double d13 = d9 - 1.0 + 2.0 * d5;
        final double d14 = d10 - 1.0 + 2.0 * d5;
        final int i2 = i & 0xFF;
        final int j2 = j & 0xFF;
        final int k2 = this.field_151608_f[i2 + this.field_151608_f[j2]] % 12;
        final int l2 = this.field_151608_f[i2 + k + this.field_151608_f[j2 + l]] % 12;
        final int i3 = this.field_151608_f[i2 + 1 + this.field_151608_f[j2 + 1]] % 12;
        double d15 = 0.5 - d9 * d9 - d10 * d10;
        double d16;
        if (d15 < 0.0) {
            d16 = 0.0;
        }
        else {
            d15 *= d15;
            d16 = d15 * d15 * func_151604_a(NoiseGeneratorSimplex.field_151611_e[k2], d9, d10);
        }
        double d17 = 0.5 - d11 * d11 - d12 * d12;
        double d18;
        if (d17 < 0.0) {
            d18 = 0.0;
        }
        else {
            d17 *= d17;
            d18 = d17 * d17 * func_151604_a(NoiseGeneratorSimplex.field_151611_e[l2], d11, d12);
        }
        double d19 = 0.5 - d13 * d13 - d14 * d14;
        double d20;
        if (d19 < 0.0) {
            d20 = 0.0;
        }
        else {
            d19 *= d19;
            d20 = d19 * d19 * func_151604_a(NoiseGeneratorSimplex.field_151611_e[i3], d13, d14);
        }
        return 70.0 * (d16 + d18 + d20);
    }
    
    public void func_151606_a(final double[] p_151606_1_, final double p_151606_2_, final double p_151606_4_, final int p_151606_6_, final int p_151606_7_, final double p_151606_8_, final double p_151606_10_, final double p_151606_12_) {
        int i = 0;
        for (int j = 0; j < p_151606_7_; ++j) {
            final double d0 = (p_151606_4_ + j) * p_151606_10_ + this.field_151613_c;
            for (int k = 0; k < p_151606_6_; ++k) {
                final double d2 = (p_151606_2_ + k) * p_151606_8_ + this.field_151612_b;
                final double d3 = (d2 + d0) * NoiseGeneratorSimplex.field_151609_g;
                final int l = func_151607_a(d2 + d3);
                final int i2 = func_151607_a(d0 + d3);
                final double d4 = (l + i2) * NoiseGeneratorSimplex.field_151615_h;
                final double d5 = l - d4;
                final double d6 = i2 - d4;
                final double d7 = d2 - d5;
                final double d8 = d0 - d6;
                int j2;
                int k2;
                if (d7 > d8) {
                    j2 = 1;
                    k2 = 0;
                }
                else {
                    j2 = 0;
                    k2 = 1;
                }
                final double d9 = d7 - j2 + NoiseGeneratorSimplex.field_151615_h;
                final double d10 = d8 - k2 + NoiseGeneratorSimplex.field_151615_h;
                final double d11 = d7 - 1.0 + 2.0 * NoiseGeneratorSimplex.field_151615_h;
                final double d12 = d8 - 1.0 + 2.0 * NoiseGeneratorSimplex.field_151615_h;
                final int l2 = l & 0xFF;
                final int i3 = i2 & 0xFF;
                final int j3 = this.field_151608_f[l2 + this.field_151608_f[i3]] % 12;
                final int k3 = this.field_151608_f[l2 + j2 + this.field_151608_f[i3 + k2]] % 12;
                final int l3 = this.field_151608_f[l2 + 1 + this.field_151608_f[i3 + 1]] % 12;
                double d13 = 0.5 - d7 * d7 - d8 * d8;
                double d14;
                if (d13 < 0.0) {
                    d14 = 0.0;
                }
                else {
                    d13 *= d13;
                    d14 = d13 * d13 * func_151604_a(NoiseGeneratorSimplex.field_151611_e[j3], d7, d8);
                }
                double d15 = 0.5 - d9 * d9 - d10 * d10;
                double d16;
                if (d15 < 0.0) {
                    d16 = 0.0;
                }
                else {
                    d15 *= d15;
                    d16 = d15 * d15 * func_151604_a(NoiseGeneratorSimplex.field_151611_e[k3], d9, d10);
                }
                double d17 = 0.5 - d11 * d11 - d12 * d12;
                double d18;
                if (d17 < 0.0) {
                    d18 = 0.0;
                }
                else {
                    d17 *= d17;
                    d18 = d17 * d17 * func_151604_a(NoiseGeneratorSimplex.field_151611_e[l3], d11, d12);
                }
                final int n;
                final int i4 = n = i++;
                p_151606_1_[n] += 70.0 * (d14 + d16 + d18) * p_151606_12_;
            }
        }
    }
}
