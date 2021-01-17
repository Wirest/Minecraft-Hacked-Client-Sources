// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen;

import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import java.util.Random;
import net.minecraft.world.chunk.ChunkPrimer;

public class MapGenCavesHell extends MapGenBase
{
    protected void func_180705_a(final long p_180705_1_, final int p_180705_3_, final int p_180705_4_, final ChunkPrimer p_180705_5_, final double p_180705_6_, final double p_180705_8_, final double p_180705_10_) {
        this.func_180704_a(p_180705_1_, p_180705_3_, p_180705_4_, p_180705_5_, p_180705_6_, p_180705_8_, p_180705_10_, 1.0f + this.rand.nextFloat() * 6.0f, 0.0f, 0.0f, -1, -1, 0.5);
    }
    
    protected void func_180704_a(final long p_180704_1_, final int p_180704_3_, final int p_180704_4_, final ChunkPrimer p_180704_5_, double p_180704_6_, double p_180704_8_, double p_180704_10_, final float p_180704_12_, float p_180704_13_, float p_180704_14_, int p_180704_15_, int p_180704_16_, final double p_180704_17_) {
        final double d0 = p_180704_3_ * 16 + 8;
        final double d2 = p_180704_4_ * 16 + 8;
        float f = 0.0f;
        float f2 = 0.0f;
        final Random random = new Random(p_180704_1_);
        if (p_180704_16_ <= 0) {
            final int i = this.range * 16 - 16;
            p_180704_16_ = i - random.nextInt(i / 4);
        }
        boolean flag1 = false;
        if (p_180704_15_ == -1) {
            p_180704_15_ = p_180704_16_ / 2;
            flag1 = true;
        }
        final int j = random.nextInt(p_180704_16_ / 2) + p_180704_16_ / 4;
        final boolean flag2 = random.nextInt(6) == 0;
        while (p_180704_15_ < p_180704_16_) {
            final double d3 = 1.5 + MathHelper.sin(p_180704_15_ * 3.1415927f / p_180704_16_) * p_180704_12_ * 1.0f;
            final double d4 = d3 * p_180704_17_;
            final float f3 = MathHelper.cos(p_180704_14_);
            final float f4 = MathHelper.sin(p_180704_14_);
            p_180704_6_ += MathHelper.cos(p_180704_13_) * f3;
            p_180704_8_ += f4;
            p_180704_10_ += MathHelper.sin(p_180704_13_) * f3;
            if (flag2) {
                p_180704_14_ *= 0.92f;
            }
            else {
                p_180704_14_ *= 0.7f;
            }
            p_180704_14_ += f2 * 0.1f;
            p_180704_13_ += f * 0.1f;
            f2 *= 0.9f;
            f *= 0.75f;
            f2 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0f;
            f += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0f;
            if (!flag1 && p_180704_15_ == j && p_180704_12_ > 1.0f) {
                this.func_180704_a(random.nextLong(), p_180704_3_, p_180704_4_, p_180704_5_, p_180704_6_, p_180704_8_, p_180704_10_, random.nextFloat() * 0.5f + 0.5f, p_180704_13_ - 1.5707964f, p_180704_14_ / 3.0f, p_180704_15_, p_180704_16_, 1.0);
                this.func_180704_a(random.nextLong(), p_180704_3_, p_180704_4_, p_180704_5_, p_180704_6_, p_180704_8_, p_180704_10_, random.nextFloat() * 0.5f + 0.5f, p_180704_13_ + 1.5707964f, p_180704_14_ / 3.0f, p_180704_15_, p_180704_16_, 1.0);
                return;
            }
            if (flag1 || random.nextInt(4) != 0) {
                final double d5 = p_180704_6_ - d0;
                final double d6 = p_180704_10_ - d2;
                final double d7 = p_180704_16_ - p_180704_15_;
                final double d8 = p_180704_12_ + 2.0f + 16.0f;
                if (d5 * d5 + d6 * d6 - d7 * d7 > d8 * d8) {
                    return;
                }
                if (p_180704_6_ >= d0 - 16.0 - d3 * 2.0 && p_180704_10_ >= d2 - 16.0 - d3 * 2.0 && p_180704_6_ <= d0 + 16.0 + d3 * 2.0 && p_180704_10_ <= d2 + 16.0 + d3 * 2.0) {
                    int j2 = MathHelper.floor_double(p_180704_6_ - d3) - p_180704_3_ * 16 - 1;
                    int k = MathHelper.floor_double(p_180704_6_ + d3) - p_180704_3_ * 16 + 1;
                    int k2 = MathHelper.floor_double(p_180704_8_ - d4) - 1;
                    int l = MathHelper.floor_double(p_180704_8_ + d4) + 1;
                    int l2 = MathHelper.floor_double(p_180704_10_ - d3) - p_180704_4_ * 16 - 1;
                    int i2 = MathHelper.floor_double(p_180704_10_ + d3) - p_180704_4_ * 16 + 1;
                    if (j2 < 0) {
                        j2 = 0;
                    }
                    if (k > 16) {
                        k = 16;
                    }
                    if (k2 < 1) {
                        k2 = 1;
                    }
                    if (l > 120) {
                        l = 120;
                    }
                    if (l2 < 0) {
                        l2 = 0;
                    }
                    if (i2 > 16) {
                        i2 = 16;
                    }
                    boolean flag3 = false;
                    for (int j3 = j2; !flag3 && j3 < k; ++j3) {
                        for (int k3 = l2; !flag3 && k3 < i2; ++k3) {
                            for (int l3 = l + 1; !flag3 && l3 >= k2 - 1; --l3) {
                                if (l3 >= 0 && l3 < 128) {
                                    final IBlockState iblockstate = p_180704_5_.getBlockState(j3, l3, k3);
                                    if (iblockstate.getBlock() == Blocks.flowing_lava || iblockstate.getBlock() == Blocks.lava) {
                                        flag3 = true;
                                    }
                                    if (l3 != k2 - 1 && j3 != j2 && j3 != k - 1 && k3 != l2 && k3 != i2 - 1) {
                                        l3 = k2;
                                    }
                                }
                            }
                        }
                    }
                    if (!flag3) {
                        for (int i3 = j2; i3 < k; ++i3) {
                            final double d9 = (i3 + p_180704_3_ * 16 + 0.5 - p_180704_6_) / d3;
                            for (int j4 = l2; j4 < i2; ++j4) {
                                final double d10 = (j4 + p_180704_4_ * 16 + 0.5 - p_180704_10_) / d3;
                                for (int i4 = l; i4 > k2; --i4) {
                                    final double d11 = (i4 - 1 + 0.5 - p_180704_8_) / d4;
                                    if (d11 > -0.7 && d9 * d9 + d11 * d11 + d10 * d10 < 1.0) {
                                        final IBlockState iblockstate2 = p_180704_5_.getBlockState(i3, i4, j4);
                                        if (iblockstate2.getBlock() == Blocks.netherrack || iblockstate2.getBlock() == Blocks.dirt || iblockstate2.getBlock() == Blocks.grass) {
                                            p_180704_5_.setBlockState(i3, i4, j4, Blocks.air.getDefaultState());
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
            ++p_180704_15_;
        }
    }
    
    @Override
    protected void recursiveGenerate(final World worldIn, final int chunkX, final int chunkZ, final int p_180701_4_, final int p_180701_5_, final ChunkPrimer chunkPrimerIn) {
        int i = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(10) + 1) + 1);
        if (this.rand.nextInt(5) != 0) {
            i = 0;
        }
        for (int j = 0; j < i; ++j) {
            final double d0 = chunkX * 16 + this.rand.nextInt(16);
            final double d2 = this.rand.nextInt(128);
            final double d3 = chunkZ * 16 + this.rand.nextInt(16);
            int k = 1;
            if (this.rand.nextInt(4) == 0) {
                this.func_180705_a(this.rand.nextLong(), p_180701_4_, p_180701_5_, chunkPrimerIn, d0, d2, d3);
                k += this.rand.nextInt(4);
            }
            for (int l = 0; l < k; ++l) {
                final float f = this.rand.nextFloat() * 3.1415927f * 2.0f;
                final float f2 = (this.rand.nextFloat() - 0.5f) * 2.0f / 8.0f;
                final float f3 = this.rand.nextFloat() * 2.0f + this.rand.nextFloat();
                this.func_180704_a(this.rand.nextLong(), p_180701_4_, p_180701_5_, chunkPrimerIn, d0, d2, d3, f3 * 2.0f, f, f2, 0, 0, 0.5);
            }
        }
    }
}
