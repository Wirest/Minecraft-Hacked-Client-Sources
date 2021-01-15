package net.minecraft.world.gen;

import java.util.Random;

public class NoiseGeneratorSimplex
{
    private static int[][] field_151611_e = new int[][] {{1, 1, 0}, { -1, 1, 0}, {1, -1, 0}, { -1, -1, 0}, {1, 0, 1}, { -1, 0, 1}, {1, 0, -1}, { -1, 0, -1}, {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1}};
    public static final double field_151614_a = Math.sqrt(3.0D);
    private int[] field_151608_f;
    public double field_151612_b;
    public double field_151613_c;
    public double field_151610_d;
    private static final double field_151609_g = 0.5D * (field_151614_a - 1.0D);
    private static final double field_151615_h = (3.0D - field_151614_a) / 6.0D;
    private static final String __OBFID = "CL_00000537";

    public NoiseGeneratorSimplex()
    {
        this(new Random());
    }

    public NoiseGeneratorSimplex(Random p_i45471_1_)
    {
        this.field_151608_f = new int[512];
        this.field_151612_b = p_i45471_1_.nextDouble() * 256.0D;
        this.field_151613_c = p_i45471_1_.nextDouble() * 256.0D;
        this.field_151610_d = p_i45471_1_.nextDouble() * 256.0D;
        int var2;

        for (var2 = 0; var2 < 256; this.field_151608_f[var2] = var2++)
        {
            ;
        }

        for (var2 = 0; var2 < 256; ++var2)
        {
            int var3 = p_i45471_1_.nextInt(256 - var2) + var2;
            int var4 = this.field_151608_f[var2];
            this.field_151608_f[var2] = this.field_151608_f[var3];
            this.field_151608_f[var3] = var4;
            this.field_151608_f[var2 + 256] = this.field_151608_f[var2];
        }
    }

    private static int func_151607_a(double p_151607_0_)
    {
        return p_151607_0_ > 0.0D ? (int)p_151607_0_ : (int)p_151607_0_ - 1;
    }

    private static double func_151604_a(int[] p_151604_0_, double p_151604_1_, double p_151604_3_)
    {
        return (double)p_151604_0_[0] * p_151604_1_ + (double)p_151604_0_[1] * p_151604_3_;
    }

    public double func_151605_a(double p_151605_1_, double p_151605_3_)
    {
        double var11 = 0.5D * (field_151614_a - 1.0D);
        double var13 = (p_151605_1_ + p_151605_3_) * var11;
        int var15 = func_151607_a(p_151605_1_ + var13);
        int var16 = func_151607_a(p_151605_3_ + var13);
        double var17 = (3.0D - field_151614_a) / 6.0D;
        double var19 = (double)(var15 + var16) * var17;
        double var21 = (double)var15 - var19;
        double var23 = (double)var16 - var19;
        double var25 = p_151605_1_ - var21;
        double var27 = p_151605_3_ - var23;
        byte var29;
        byte var30;

        if (var25 > var27)
        {
            var29 = 1;
            var30 = 0;
        }
        else
        {
            var29 = 0;
            var30 = 1;
        }

        double var31 = var25 - (double)var29 + var17;
        double var33 = var27 - (double)var30 + var17;
        double var35 = var25 - 1.0D + 2.0D * var17;
        double var37 = var27 - 1.0D + 2.0D * var17;
        int var39 = var15 & 255;
        int var40 = var16 & 255;
        int var41 = this.field_151608_f[var39 + this.field_151608_f[var40]] % 12;
        int var42 = this.field_151608_f[var39 + var29 + this.field_151608_f[var40 + var30]] % 12;
        int var43 = this.field_151608_f[var39 + 1 + this.field_151608_f[var40 + 1]] % 12;
        double var44 = 0.5D - var25 * var25 - var27 * var27;
        double var5;

        if (var44 < 0.0D)
        {
            var5 = 0.0D;
        }
        else
        {
            var44 *= var44;
            var5 = var44 * var44 * func_151604_a(field_151611_e[var41], var25, var27);
        }

        double var46 = 0.5D - var31 * var31 - var33 * var33;
        double var7;

        if (var46 < 0.0D)
        {
            var7 = 0.0D;
        }
        else
        {
            var46 *= var46;
            var7 = var46 * var46 * func_151604_a(field_151611_e[var42], var31, var33);
        }

        double var48 = 0.5D - var35 * var35 - var37 * var37;
        double var9;

        if (var48 < 0.0D)
        {
            var9 = 0.0D;
        }
        else
        {
            var48 *= var48;
            var9 = var48 * var48 * func_151604_a(field_151611_e[var43], var35, var37);
        }

        return 70.0D * (var5 + var7 + var9);
    }

    public void func_151606_a(double[] p_151606_1_, double p_151606_2_, double p_151606_4_, int p_151606_6_, int p_151606_7_, double p_151606_8_, double p_151606_10_, double p_151606_12_)
    {
        int var14 = 0;

        for (int var15 = 0; var15 < p_151606_7_; ++var15)
        {
            double var16 = (p_151606_4_ + (double)var15) * p_151606_10_ + this.field_151613_c;

            for (int var18 = 0; var18 < p_151606_6_; ++var18)
            {
                double var19 = (p_151606_2_ + (double)var18) * p_151606_8_ + this.field_151612_b;
                double var27 = (var19 + var16) * field_151609_g;
                int var29 = func_151607_a(var19 + var27);
                int var30 = func_151607_a(var16 + var27);
                double var31 = (double)(var29 + var30) * field_151615_h;
                double var33 = (double)var29 - var31;
                double var35 = (double)var30 - var31;
                double var37 = var19 - var33;
                double var39 = var16 - var35;
                byte var41;
                byte var42;

                if (var37 > var39)
                {
                    var41 = 1;
                    var42 = 0;
                }
                else
                {
                    var41 = 0;
                    var42 = 1;
                }

                double var43 = var37 - (double)var41 + field_151615_h;
                double var45 = var39 - (double)var42 + field_151615_h;
                double var47 = var37 - 1.0D + 2.0D * field_151615_h;
                double var49 = var39 - 1.0D + 2.0D * field_151615_h;
                int var51 = var29 & 255;
                int var52 = var30 & 255;
                int var53 = this.field_151608_f[var51 + this.field_151608_f[var52]] % 12;
                int var54 = this.field_151608_f[var51 + var41 + this.field_151608_f[var52 + var42]] % 12;
                int var55 = this.field_151608_f[var51 + 1 + this.field_151608_f[var52 + 1]] % 12;
                double var56 = 0.5D - var37 * var37 - var39 * var39;
                double var21;

                if (var56 < 0.0D)
                {
                    var21 = 0.0D;
                }
                else
                {
                    var56 *= var56;
                    var21 = var56 * var56 * func_151604_a(field_151611_e[var53], var37, var39);
                }

                double var58 = 0.5D - var43 * var43 - var45 * var45;
                double var23;

                if (var58 < 0.0D)
                {
                    var23 = 0.0D;
                }
                else
                {
                    var58 *= var58;
                    var23 = var58 * var58 * func_151604_a(field_151611_e[var54], var43, var45);
                }

                double var60 = 0.5D - var47 * var47 - var49 * var49;
                double var25;

                if (var60 < 0.0D)
                {
                    var25 = 0.0D;
                }
                else
                {
                    var60 *= var60;
                    var25 = var60 * var60 * func_151604_a(field_151611_e[var55], var47, var49);
                }

                int var10001 = var14++;
                p_151606_1_[var10001] += 70.0D * (var21 + var23 + var25) * p_151606_12_;
            }
        }
    }
}
