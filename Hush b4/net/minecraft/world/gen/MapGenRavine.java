// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen;

import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import java.util.Random;
import net.minecraft.world.chunk.ChunkPrimer;

public class MapGenRavine extends MapGenBase
{
    private float[] field_75046_d;
    
    public MapGenRavine() {
        this.field_75046_d = new float[1024];
    }
    
    protected void func_180707_a(final long p_180707_1_, final int p_180707_3_, final int p_180707_4_, final ChunkPrimer p_180707_5_, double p_180707_6_, double p_180707_8_, double p_180707_10_, final float p_180707_12_, float p_180707_13_, float p_180707_14_, int p_180707_15_, int p_180707_16_, final double p_180707_17_) {
        final Random random = new Random(p_180707_1_);
        final double d0 = p_180707_3_ * 16 + 8;
        final double d2 = p_180707_4_ * 16 + 8;
        float f = 0.0f;
        float f2 = 0.0f;
        if (p_180707_16_ <= 0) {
            final int i = this.range * 16 - 16;
            p_180707_16_ = i - random.nextInt(i / 4);
        }
        boolean flag1 = false;
        if (p_180707_15_ == -1) {
            p_180707_15_ = p_180707_16_ / 2;
            flag1 = true;
        }
        float f3 = 1.0f;
        for (int j = 0; j < 256; ++j) {
            if (j == 0 || random.nextInt(3) == 0) {
                f3 = 1.0f + random.nextFloat() * random.nextFloat() * 1.0f;
            }
            this.field_75046_d[j] = f3 * f3;
        }
        while (p_180707_15_ < p_180707_16_) {
            double d3 = 1.5 + MathHelper.sin(p_180707_15_ * 3.1415927f / p_180707_16_) * p_180707_12_ * 1.0f;
            double d4 = d3 * p_180707_17_;
            d3 *= random.nextFloat() * 0.25 + 0.75;
            d4 *= random.nextFloat() * 0.25 + 0.75;
            final float f4 = MathHelper.cos(p_180707_14_);
            final float f5 = MathHelper.sin(p_180707_14_);
            p_180707_6_ += MathHelper.cos(p_180707_13_) * f4;
            p_180707_8_ += f5;
            p_180707_10_ += MathHelper.sin(p_180707_13_) * f4;
            p_180707_14_ *= 0.7f;
            p_180707_14_ += f2 * 0.05f;
            p_180707_13_ += f * 0.05f;
            f2 *= 0.8f;
            f *= 0.5f;
            f2 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0f;
            f += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0f;
            if (flag1 || random.nextInt(4) != 0) {
                final double d5 = p_180707_6_ - d0;
                final double d6 = p_180707_10_ - d2;
                final double d7 = p_180707_16_ - p_180707_15_;
                final double d8 = p_180707_12_ + 2.0f + 16.0f;
                if (d5 * d5 + d6 * d6 - d7 * d7 > d8 * d8) {
                    return;
                }
                if (p_180707_6_ >= d0 - 16.0 - d3 * 2.0 && p_180707_10_ >= d2 - 16.0 - d3 * 2.0 && p_180707_6_ <= d0 + 16.0 + d3 * 2.0 && p_180707_10_ <= d2 + 16.0 + d3 * 2.0) {
                    int k2 = MathHelper.floor_double(p_180707_6_ - d3) - p_180707_3_ * 16 - 1;
                    int l = MathHelper.floor_double(p_180707_6_ + d3) - p_180707_3_ * 16 + 1;
                    int l2 = MathHelper.floor_double(p_180707_8_ - d4) - 1;
                    int m = MathHelper.floor_double(p_180707_8_ + d4) + 1;
                    int i2 = MathHelper.floor_double(p_180707_10_ - d3) - p_180707_4_ * 16 - 1;
                    int i3 = MathHelper.floor_double(p_180707_10_ + d3) - p_180707_4_ * 16 + 1;
                    if (k2 < 0) {
                        k2 = 0;
                    }
                    if (l > 16) {
                        l = 16;
                    }
                    if (l2 < 1) {
                        l2 = 1;
                    }
                    if (m > 248) {
                        m = 248;
                    }
                    if (i2 < 0) {
                        i2 = 0;
                    }
                    if (i3 > 16) {
                        i3 = 16;
                    }
                    boolean flag2 = false;
                    for (int j2 = k2; !flag2 && j2 < l; ++j2) {
                        for (int k3 = i2; !flag2 && k3 < i3; ++k3) {
                            for (int l3 = m + 1; !flag2 && l3 >= l2 - 1; --l3) {
                                if (l3 >= 0 && l3 < 256) {
                                    final IBlockState iblockstate = p_180707_5_.getBlockState(j2, l3, k3);
                                    if (iblockstate.getBlock() == Blocks.flowing_water || iblockstate.getBlock() == Blocks.water) {
                                        flag2 = true;
                                    }
                                    if (l3 != l2 - 1 && j2 != k2 && j2 != l - 1 && k3 != i2 && k3 != i3 - 1) {
                                        l3 = l2;
                                    }
                                }
                            }
                        }
                    }
                    if (!flag2) {
                        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                        for (int j3 = k2; j3 < l; ++j3) {
                            final double d9 = (j3 + p_180707_3_ * 16 + 0.5 - p_180707_6_) / d3;
                            for (int i4 = i2; i4 < i3; ++i4) {
                                final double d10 = (i4 + p_180707_4_ * 16 + 0.5 - p_180707_10_) / d3;
                                boolean flag3 = false;
                                if (d9 * d9 + d10 * d10 < 1.0) {
                                    for (int j4 = m; j4 > l2; --j4) {
                                        final double d11 = (j4 - 1 + 0.5 - p_180707_8_) / d4;
                                        if ((d9 * d9 + d10 * d10) * this.field_75046_d[j4 - 1] + d11 * d11 / 6.0 < 1.0) {
                                            final IBlockState iblockstate2 = p_180707_5_.getBlockState(j3, j4, i4);
                                            if (iblockstate2.getBlock() == Blocks.grass) {
                                                flag3 = true;
                                            }
                                            if (iblockstate2.getBlock() == Blocks.stone || iblockstate2.getBlock() == Blocks.dirt || iblockstate2.getBlock() == Blocks.grass) {
                                                if (j4 - 1 < 10) {
                                                    p_180707_5_.setBlockState(j3, j4, i4, Blocks.flowing_lava.getDefaultState());
                                                }
                                                else {
                                                    p_180707_5_.setBlockState(j3, j4, i4, Blocks.air.getDefaultState());
                                                    if (flag3 && p_180707_5_.getBlockState(j3, j4 - 1, i4).getBlock() == Blocks.dirt) {
                                                        blockpos$mutableblockpos.func_181079_c(j3 + p_180707_3_ * 16, 0, i4 + p_180707_4_ * 16);
                                                        p_180707_5_.setBlockState(j3, j4 - 1, i4, this.worldObj.getBiomeGenForCoords(blockpos$mutableblockpos).topBlock);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (flag1) {
                            break;
                        }
                    }
                }
            }
            ++p_180707_15_;
        }
    }
    
    @Override
    protected void recursiveGenerate(final World worldIn, final int chunkX, final int chunkZ, final int p_180701_4_, final int p_180701_5_, final ChunkPrimer chunkPrimerIn) {
        if (this.rand.nextInt(50) == 0) {
            final double d0 = chunkX * 16 + this.rand.nextInt(16);
            final double d2 = this.rand.nextInt(this.rand.nextInt(40) + 8) + 20;
            final double d3 = chunkZ * 16 + this.rand.nextInt(16);
            for (int i = 1, j = 0; j < i; ++j) {
                final float f = this.rand.nextFloat() * 3.1415927f * 2.0f;
                final float f2 = (this.rand.nextFloat() - 0.5f) * 2.0f / 8.0f;
                final float f3 = (this.rand.nextFloat() * 2.0f + this.rand.nextFloat()) * 2.0f;
                this.func_180707_a(this.rand.nextLong(), p_180701_4_, p_180701_5_, chunkPrimerIn, d0, d2, d3, f3, f, f2, 0, 0, 3.0);
            }
        }
    }
}
