package net.minecraft.world.gen;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class MapGenCavesHell extends MapGenBase
{
    private static final String __OBFID = "CL_00000395";

    protected void func_180705_a(long p_180705_1_, int p_180705_3_, int p_180705_4_, ChunkPrimer p_180705_5_, double p_180705_6_, double p_180705_8_, double p_180705_10_)
    {
        this.func_180704_a(p_180705_1_, p_180705_3_, p_180705_4_, p_180705_5_, p_180705_6_, p_180705_8_, p_180705_10_, 1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
    }

    protected void func_180704_a(long p_180704_1_, int p_180704_3_, int p_180704_4_, ChunkPrimer p_180704_5_, double p_180704_6_, double p_180704_8_, double p_180704_10_, float p_180704_12_, float p_180704_13_, float p_180704_14_, int p_180704_15_, int p_180704_16_, double p_180704_17_)
    {
        double var19 = (double)(p_180704_3_ * 16 + 8);
        double var21 = (double)(p_180704_4_ * 16 + 8);
        float var23 = 0.0F;
        float var24 = 0.0F;
        Random var25 = new Random(p_180704_1_);

        if (p_180704_16_ <= 0)
        {
            int var26 = this.range * 16 - 16;
            p_180704_16_ = var26 - var25.nextInt(var26 / 4);
        }

        boolean var52 = false;

        if (p_180704_15_ == -1)
        {
            p_180704_15_ = p_180704_16_ / 2;
            var52 = true;
        }

        int var27 = var25.nextInt(p_180704_16_ / 2) + p_180704_16_ / 4;

        for (boolean var28 = var25.nextInt(6) == 0; p_180704_15_ < p_180704_16_; ++p_180704_15_)
        {
            double var29 = 1.5D + (double)(MathHelper.sin((float)p_180704_15_ * (float)Math.PI / (float)p_180704_16_) * p_180704_12_ * 1.0F);
            double var31 = var29 * p_180704_17_;
            float var33 = MathHelper.cos(p_180704_14_);
            float var34 = MathHelper.sin(p_180704_14_);
            p_180704_6_ += (double)(MathHelper.cos(p_180704_13_) * var33);
            p_180704_8_ += (double)var34;
            p_180704_10_ += (double)(MathHelper.sin(p_180704_13_) * var33);

            if (var28)
            {
                p_180704_14_ *= 0.92F;
            }
            else
            {
                p_180704_14_ *= 0.7F;
            }

            p_180704_14_ += var24 * 0.1F;
            p_180704_13_ += var23 * 0.1F;
            var24 *= 0.9F;
            var23 *= 0.75F;
            var24 += (var25.nextFloat() - var25.nextFloat()) * var25.nextFloat() * 2.0F;
            var23 += (var25.nextFloat() - var25.nextFloat()) * var25.nextFloat() * 4.0F;

            if (!var52 && p_180704_15_ == var27 && p_180704_12_ > 1.0F)
            {
                this.func_180704_a(var25.nextLong(), p_180704_3_, p_180704_4_, p_180704_5_, p_180704_6_, p_180704_8_, p_180704_10_, var25.nextFloat() * 0.5F + 0.5F, p_180704_13_ - ((float)Math.PI / 2F), p_180704_14_ / 3.0F, p_180704_15_, p_180704_16_, 1.0D);
                this.func_180704_a(var25.nextLong(), p_180704_3_, p_180704_4_, p_180704_5_, p_180704_6_, p_180704_8_, p_180704_10_, var25.nextFloat() * 0.5F + 0.5F, p_180704_13_ + ((float)Math.PI / 2F), p_180704_14_ / 3.0F, p_180704_15_, p_180704_16_, 1.0D);
                return;
            }

            if (var52 || var25.nextInt(4) != 0)
            {
                double var35 = p_180704_6_ - var19;
                double var37 = p_180704_10_ - var21;
                double var39 = (double)(p_180704_16_ - p_180704_15_);
                double var41 = (double)(p_180704_12_ + 2.0F + 16.0F);

                if (var35 * var35 + var37 * var37 - var39 * var39 > var41 * var41)
                {
                    return;
                }

                if (p_180704_6_ >= var19 - 16.0D - var29 * 2.0D && p_180704_10_ >= var21 - 16.0D - var29 * 2.0D && p_180704_6_ <= var19 + 16.0D + var29 * 2.0D && p_180704_10_ <= var21 + 16.0D + var29 * 2.0D)
                {
                    int var53 = MathHelper.floor_double(p_180704_6_ - var29) - p_180704_3_ * 16 - 1;
                    int var36 = MathHelper.floor_double(p_180704_6_ + var29) - p_180704_3_ * 16 + 1;
                    int var54 = MathHelper.floor_double(p_180704_8_ - var31) - 1;
                    int var38 = MathHelper.floor_double(p_180704_8_ + var31) + 1;
                    int var55 = MathHelper.floor_double(p_180704_10_ - var29) - p_180704_4_ * 16 - 1;
                    int var40 = MathHelper.floor_double(p_180704_10_ + var29) - p_180704_4_ * 16 + 1;

                    if (var53 < 0)
                    {
                        var53 = 0;
                    }

                    if (var36 > 16)
                    {
                        var36 = 16;
                    }

                    if (var54 < 1)
                    {
                        var54 = 1;
                    }

                    if (var38 > 120)
                    {
                        var38 = 120;
                    }

                    if (var55 < 0)
                    {
                        var55 = 0;
                    }

                    if (var40 > 16)
                    {
                        var40 = 16;
                    }

                    boolean var56 = false;
                    int var42;

                    for (var42 = var53; !var56 && var42 < var36; ++var42)
                    {
                        for (int var43 = var55; !var56 && var43 < var40; ++var43)
                        {
                            for (int var44 = var38 + 1; !var56 && var44 >= var54 - 1; --var44)
                            {
                                if (var44 >= 0 && var44 < 128)
                                {
                                    IBlockState var45 = p_180704_5_.getBlockState(var42, var44, var43);

                                    if (var45.getBlock() == Blocks.flowing_lava || var45.getBlock() == Blocks.lava)
                                    {
                                        var56 = true;
                                    }

                                    if (var44 != var54 - 1 && var42 != var53 && var42 != var36 - 1 && var43 != var55 && var43 != var40 - 1)
                                    {
                                        var44 = var54;
                                    }
                                }
                            }
                        }
                    }

                    if (!var56)
                    {
                        for (var42 = var53; var42 < var36; ++var42)
                        {
                            double var57 = ((double)(var42 + p_180704_3_ * 16) + 0.5D - p_180704_6_) / var29;

                            for (int var58 = var55; var58 < var40; ++var58)
                            {
                                double var46 = ((double)(var58 + p_180704_4_ * 16) + 0.5D - p_180704_10_) / var29;

                                for (int var48 = var38; var48 > var54; --var48)
                                {
                                    double var49 = ((double)(var48 - 1) + 0.5D - p_180704_8_) / var31;

                                    if (var49 > -0.7D && var57 * var57 + var49 * var49 + var46 * var46 < 1.0D)
                                    {
                                        IBlockState var51 = p_180704_5_.getBlockState(var42, var48, var58);

                                        if (var51.getBlock() == Blocks.netherrack || var51.getBlock() == Blocks.dirt || var51.getBlock() == Blocks.grass)
                                        {
                                            p_180704_5_.setBlockState(var42, var48, var58, Blocks.air.getDefaultState());
                                        }
                                    }
                                }
                            }
                        }

                        if (var52)
                        {
                            break;
                        }
                    }
                }
            }
        }
    }

    protected void func_180701_a(World worldIn, int p_180701_2_, int p_180701_3_, int p_180701_4_, int p_180701_5_, ChunkPrimer p_180701_6_)
    {
        int var7 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(10) + 1) + 1);

        if (this.rand.nextInt(5) != 0)
        {
            var7 = 0;
        }

        for (int var8 = 0; var8 < var7; ++var8)
        {
            double var9 = (double)(p_180701_2_ * 16 + this.rand.nextInt(16));
            double var11 = (double)this.rand.nextInt(128);
            double var13 = (double)(p_180701_3_ * 16 + this.rand.nextInt(16));
            int var15 = 1;

            if (this.rand.nextInt(4) == 0)
            {
                this.func_180705_a(this.rand.nextLong(), p_180701_4_, p_180701_5_, p_180701_6_, var9, var11, var13);
                var15 += this.rand.nextInt(4);
            }

            for (int var16 = 0; var16 < var15; ++var16)
            {
                float var17 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
                float var18 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float var19 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();
                this.func_180704_a(this.rand.nextLong(), p_180701_4_, p_180701_5_, p_180701_6_, var9, var11, var13, var19 * 2.0F, var17, var18, 0, 0, 0.5D);
            }
        }
    }
}
