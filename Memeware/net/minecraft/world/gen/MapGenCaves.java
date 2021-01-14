package net.minecraft.world.gen;

import com.google.common.base.Objects;

import java.util.Random;

import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class MapGenCaves extends MapGenBase {
    private static final String __OBFID = "CL_00000393";

    protected void func_180703_a(long p_180703_1_, int p_180703_3_, int p_180703_4_, ChunkPrimer p_180703_5_, double p_180703_6_, double p_180703_8_, double p_180703_10_) {
        this.func_180702_a(p_180703_1_, p_180703_3_, p_180703_4_, p_180703_5_, p_180703_6_, p_180703_8_, p_180703_10_, 1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
    }

    protected void func_180702_a(long p_180702_1_, int p_180702_3_, int p_180702_4_, ChunkPrimer p_180702_5_, double p_180702_6_, double p_180702_8_, double p_180702_10_, float p_180702_12_, float p_180702_13_, float p_180702_14_, int p_180702_15_, int p_180702_16_, double p_180702_17_) {
        double var19 = (double) (p_180702_3_ * 16 + 8);
        double var21 = (double) (p_180702_4_ * 16 + 8);
        float var23 = 0.0F;
        float var24 = 0.0F;
        Random var25 = new Random(p_180702_1_);

        if (p_180702_16_ <= 0) {
            int var26 = this.range * 16 - 16;
            p_180702_16_ = var26 - var25.nextInt(var26 / 4);
        }

        boolean var54 = false;

        if (p_180702_15_ == -1) {
            p_180702_15_ = p_180702_16_ / 2;
            var54 = true;
        }

        int var27 = var25.nextInt(p_180702_16_ / 2) + p_180702_16_ / 4;

        for (boolean var28 = var25.nextInt(6) == 0; p_180702_15_ < p_180702_16_; ++p_180702_15_) {
            double var29 = 1.5D + (double) (MathHelper.sin((float) p_180702_15_ * (float) Math.PI / (float) p_180702_16_) * p_180702_12_ * 1.0F);
            double var31 = var29 * p_180702_17_;
            float var33 = MathHelper.cos(p_180702_14_);
            float var34 = MathHelper.sin(p_180702_14_);
            p_180702_6_ += (double) (MathHelper.cos(p_180702_13_) * var33);
            p_180702_8_ += (double) var34;
            p_180702_10_ += (double) (MathHelper.sin(p_180702_13_) * var33);

            if (var28) {
                p_180702_14_ *= 0.92F;
            } else {
                p_180702_14_ *= 0.7F;
            }

            p_180702_14_ += var24 * 0.1F;
            p_180702_13_ += var23 * 0.1F;
            var24 *= 0.9F;
            var23 *= 0.75F;
            var24 += (var25.nextFloat() - var25.nextFloat()) * var25.nextFloat() * 2.0F;
            var23 += (var25.nextFloat() - var25.nextFloat()) * var25.nextFloat() * 4.0F;

            if (!var54 && p_180702_15_ == var27 && p_180702_12_ > 1.0F && p_180702_16_ > 0) {
                this.func_180702_a(var25.nextLong(), p_180702_3_, p_180702_4_, p_180702_5_, p_180702_6_, p_180702_8_, p_180702_10_, var25.nextFloat() * 0.5F + 0.5F, p_180702_13_ - ((float) Math.PI / 2F), p_180702_14_ / 3.0F, p_180702_15_, p_180702_16_, 1.0D);
                this.func_180702_a(var25.nextLong(), p_180702_3_, p_180702_4_, p_180702_5_, p_180702_6_, p_180702_8_, p_180702_10_, var25.nextFloat() * 0.5F + 0.5F, p_180702_13_ + ((float) Math.PI / 2F), p_180702_14_ / 3.0F, p_180702_15_, p_180702_16_, 1.0D);
                return;
            }

            if (var54 || var25.nextInt(4) != 0) {
                double var35 = p_180702_6_ - var19;
                double var37 = p_180702_10_ - var21;
                double var39 = (double) (p_180702_16_ - p_180702_15_);
                double var41 = (double) (p_180702_12_ + 2.0F + 16.0F);

                if (var35 * var35 + var37 * var37 - var39 * var39 > var41 * var41) {
                    return;
                }

                if (p_180702_6_ >= var19 - 16.0D - var29 * 2.0D && p_180702_10_ >= var21 - 16.0D - var29 * 2.0D && p_180702_6_ <= var19 + 16.0D + var29 * 2.0D && p_180702_10_ <= var21 + 16.0D + var29 * 2.0D) {
                    int var55 = MathHelper.floor_double(p_180702_6_ - var29) - p_180702_3_ * 16 - 1;
                    int var36 = MathHelper.floor_double(p_180702_6_ + var29) - p_180702_3_ * 16 + 1;
                    int var56 = MathHelper.floor_double(p_180702_8_ - var31) - 1;
                    int var38 = MathHelper.floor_double(p_180702_8_ + var31) + 1;
                    int var57 = MathHelper.floor_double(p_180702_10_ - var29) - p_180702_4_ * 16 - 1;
                    int var40 = MathHelper.floor_double(p_180702_10_ + var29) - p_180702_4_ * 16 + 1;

                    if (var55 < 0) {
                        var55 = 0;
                    }

                    if (var36 > 16) {
                        var36 = 16;
                    }

                    if (var56 < 1) {
                        var56 = 1;
                    }

                    if (var38 > 248) {
                        var38 = 248;
                    }

                    if (var57 < 0) {
                        var57 = 0;
                    }

                    if (var40 > 16) {
                        var40 = 16;
                    }

                    boolean var58 = false;
                    int var42;

                    for (var42 = var55; !var58 && var42 < var36; ++var42) {
                        for (int var43 = var57; !var58 && var43 < var40; ++var43) {
                            for (int var44 = var38 + 1; !var58 && var44 >= var56 - 1; --var44) {
                                if (var44 >= 0 && var44 < 256) {
                                    IBlockState var45 = p_180702_5_.getBlockState(var42, var44, var43);

                                    if (var45.getBlock() == Blocks.flowing_water || var45.getBlock() == Blocks.water) {
                                        var58 = true;
                                    }

                                    if (var44 != var56 - 1 && var42 != var55 && var42 != var36 - 1 && var43 != var57 && var43 != var40 - 1) {
                                        var44 = var56;
                                    }
                                }
                            }
                        }
                    }

                    if (!var58) {
                        for (var42 = var55; var42 < var36; ++var42) {
                            double var59 = ((double) (var42 + p_180702_3_ * 16) + 0.5D - p_180702_6_) / var29;

                            for (int var60 = var57; var60 < var40; ++var60) {
                                double var46 = ((double) (var60 + p_180702_4_ * 16) + 0.5D - p_180702_10_) / var29;
                                boolean var48 = false;

                                if (var59 * var59 + var46 * var46 < 1.0D) {
                                    for (int var49 = var38; var49 > var56; --var49) {
                                        double var50 = ((double) (var49 - 1) + 0.5D - p_180702_8_) / var31;

                                        if (var50 > -0.7D && var59 * var59 + var50 * var50 + var46 * var46 < 1.0D) {
                                            IBlockState var52 = p_180702_5_.getBlockState(var42, var49, var60);
                                            IBlockState var53 = (IBlockState) Objects.firstNonNull(p_180702_5_.getBlockState(var42, var49 + 1, var60), Blocks.air.getDefaultState());

                                            if (var52.getBlock() == Blocks.grass || var52.getBlock() == Blocks.mycelium) {
                                                var48 = true;
                                            }

                                            if (this.func_175793_a(var52, var53)) {
                                                if (var49 - 1 < 10) {
                                                    p_180702_5_.setBlockState(var42, var49, var60, Blocks.lava.getDefaultState());
                                                } else {
                                                    p_180702_5_.setBlockState(var42, var49, var60, Blocks.air.getDefaultState());

                                                    if (var53.getBlock() == Blocks.sand) {
                                                        p_180702_5_.setBlockState(var42, var49 + 1, var60, var53.getValue(BlockSand.VARIANT_PROP) == BlockSand.EnumType.RED_SAND ? Blocks.red_sandstone.getDefaultState() : Blocks.sandstone.getDefaultState());
                                                    }

                                                    if (var48 && p_180702_5_.getBlockState(var42, var49 - 1, var60).getBlock() == Blocks.dirt) {
                                                        p_180702_5_.setBlockState(var42, var49 - 1, var60, this.worldObj.getBiomeGenForCoords(new BlockPos(var42 + p_180702_3_ * 16, 0, var60 + p_180702_4_ * 16)).topBlock.getBlock().getDefaultState());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (var54) {
                            break;
                        }
                    }
                }
            }
        }
    }

    protected boolean func_175793_a(IBlockState p_175793_1_, IBlockState p_175793_2_) {
        return p_175793_1_.getBlock() == Blocks.stone ? true : (p_175793_1_.getBlock() == Blocks.dirt ? true : (p_175793_1_.getBlock() == Blocks.grass ? true : (p_175793_1_.getBlock() == Blocks.hardened_clay ? true : (p_175793_1_.getBlock() == Blocks.stained_hardened_clay ? true : (p_175793_1_.getBlock() == Blocks.sandstone ? true : (p_175793_1_.getBlock() == Blocks.red_sandstone ? true : (p_175793_1_.getBlock() == Blocks.mycelium ? true : (p_175793_1_.getBlock() == Blocks.snow_layer ? true : (p_175793_1_.getBlock() == Blocks.sand || p_175793_1_.getBlock() == Blocks.gravel) && p_175793_2_.getBlock().getMaterial() != Material.water))))))));
    }

    protected void func_180701_a(World worldIn, int p_180701_2_, int p_180701_3_, int p_180701_4_, int p_180701_5_, ChunkPrimer p_180701_6_) {
        int var7 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(15) + 1) + 1);

        if (this.rand.nextInt(7) != 0) {
            var7 = 0;
        }

        for (int var8 = 0; var8 < var7; ++var8) {
            double var9 = (double) (p_180701_2_ * 16 + this.rand.nextInt(16));
            double var11 = (double) this.rand.nextInt(this.rand.nextInt(120) + 8);
            double var13 = (double) (p_180701_3_ * 16 + this.rand.nextInt(16));
            int var15 = 1;

            if (this.rand.nextInt(4) == 0) {
                this.func_180703_a(this.rand.nextLong(), p_180701_4_, p_180701_5_, p_180701_6_, var9, var11, var13);
                var15 += this.rand.nextInt(4);
            }

            for (int var16 = 0; var16 < var15; ++var16) {
                float var17 = this.rand.nextFloat() * (float) Math.PI * 2.0F;
                float var18 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float var19 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();

                if (this.rand.nextInt(10) == 0) {
                    var19 *= this.rand.nextFloat() * this.rand.nextFloat() * 3.0F + 1.0F;
                }

                this.func_180702_a(this.rand.nextLong(), p_180701_4_, p_180701_5_, p_180701_6_, var9, var11, var13, var19, var17, var18, 0, 0, 1.0D);
            }
        }
    }
}
