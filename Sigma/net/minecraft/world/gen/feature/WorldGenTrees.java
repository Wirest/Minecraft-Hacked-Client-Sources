package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenTrees extends WorldGenAbstractTree {
    /**
     * The minimum height of a generated tree.
     */
    private final int minTreeHeight;

    /**
     * True if this tree should grow Vines.
     */
    private final boolean vinesGrow;

    /**
     * The metadata value of the wood to use in tree generation.
     */
    private final int metaWood;

    /**
     * The metadata value of the leaves to use in tree generation.
     */
    private final int metaLeaves;
    private static final String __OBFID = "CL_00000438";

    public WorldGenTrees(boolean p_i2027_1_) {
        this(p_i2027_1_, 4, 0, 0, false);
    }

    public WorldGenTrees(boolean p_i2028_1_, int p_i2028_2_, int p_i2028_3_, int p_i2028_4_, boolean p_i2028_5_) {
        super(p_i2028_1_);
        this.minTreeHeight = p_i2028_2_;
        this.metaWood = p_i2028_3_;
        this.metaLeaves = p_i2028_4_;
        this.vinesGrow = p_i2028_5_;
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_) {
        int var4 = p_180709_2_.nextInt(3) + this.minTreeHeight;
        boolean var5 = true;

        if (p_180709_3_.getY() >= 1 && p_180709_3_.getY() + var4 + 1 <= 256) {
            byte var7;
            int var9;

            for (int var6 = p_180709_3_.getY(); var6 <= p_180709_3_.getY() + 1 + var4; ++var6) {
                var7 = 1;

                if (var6 == p_180709_3_.getY()) {
                    var7 = 0;
                }

                if (var6 >= p_180709_3_.getY() + 1 + var4 - 2) {
                    var7 = 2;
                }

                for (int var8 = p_180709_3_.getX() - var7; var8 <= p_180709_3_.getX() + var7 && var5; ++var8) {
                    for (var9 = p_180709_3_.getZ() - var7; var9 <= p_180709_3_.getZ() + var7 && var5; ++var9) {
                        if (var6 >= 0 && var6 < 256) {
                            if (!this.func_150523_a(worldIn.getBlockState(new BlockPos(var8, var6, var9)).getBlock())) {
                                var5 = false;
                            }
                        } else {
                            var5 = false;
                        }
                    }
                }
            }

            if (!var5) {
                return false;
            } else {
                Block var19 = worldIn.getBlockState(p_180709_3_.offsetDown()).getBlock();

                if ((var19 == Blocks.grass || var19 == Blocks.dirt || var19 == Blocks.farmland) && p_180709_3_.getY() < 256 - var4 - 1) {
                    this.func_175921_a(worldIn, p_180709_3_.offsetDown());
                    var7 = 3;
                    byte var20 = 0;
                    int var10;
                    int var11;
                    int var12;
                    int var13;
                    BlockPos var16;

                    for (var9 = p_180709_3_.getY() - var7 + var4; var9 <= p_180709_3_.getY() + var4; ++var9) {
                        var10 = var9 - (p_180709_3_.getY() + var4);
                        var11 = var20 + 1 - var10 / 2;

                        for (var12 = p_180709_3_.getX() - var11; var12 <= p_180709_3_.getX() + var11; ++var12) {
                            var13 = var12 - p_180709_3_.getX();

                            for (int var14 = p_180709_3_.getZ() - var11; var14 <= p_180709_3_.getZ() + var11; ++var14) {
                                int var15 = var14 - p_180709_3_.getZ();

                                if (Math.abs(var13) != var11 || Math.abs(var15) != var11 || p_180709_2_.nextInt(2) != 0 && var10 != 0) {
                                    var16 = new BlockPos(var12, var9, var14);
                                    Block var17 = worldIn.getBlockState(var16).getBlock();

                                    if (var17.getMaterial() == Material.air || var17.getMaterial() == Material.leaves || var17.getMaterial() == Material.vine) {
                                        this.func_175905_a(worldIn, var16, Blocks.leaves, this.metaLeaves);
                                    }
                                }
                            }
                        }
                    }

                    for (var9 = 0; var9 < var4; ++var9) {
                        Block var21 = worldIn.getBlockState(p_180709_3_.offsetUp(var9)).getBlock();

                        if (var21.getMaterial() == Material.air || var21.getMaterial() == Material.leaves || var21.getMaterial() == Material.vine) {
                            this.func_175905_a(worldIn, p_180709_3_.offsetUp(var9), Blocks.log, this.metaWood);

                            if (this.vinesGrow && var9 > 0) {
                                if (p_180709_2_.nextInt(3) > 0 && worldIn.isAirBlock(p_180709_3_.add(-1, var9, 0))) {
                                    this.func_175905_a(worldIn, p_180709_3_.add(-1, var9, 0), Blocks.vine, BlockVine.field_176275_S);
                                }

                                if (p_180709_2_.nextInt(3) > 0 && worldIn.isAirBlock(p_180709_3_.add(1, var9, 0))) {
                                    this.func_175905_a(worldIn, p_180709_3_.add(1, var9, 0), Blocks.vine, BlockVine.field_176271_T);
                                }

                                if (p_180709_2_.nextInt(3) > 0 && worldIn.isAirBlock(p_180709_3_.add(0, var9, -1))) {
                                    this.func_175905_a(worldIn, p_180709_3_.add(0, var9, -1), Blocks.vine, BlockVine.field_176272_Q);
                                }

                                if (p_180709_2_.nextInt(3) > 0 && worldIn.isAirBlock(p_180709_3_.add(0, var9, 1))) {
                                    this.func_175905_a(worldIn, p_180709_3_.add(0, var9, 1), Blocks.vine, BlockVine.field_176276_R);
                                }
                            }
                        }
                    }

                    if (this.vinesGrow) {
                        for (var9 = p_180709_3_.getY() - 3 + var4; var9 <= p_180709_3_.getY() + var4; ++var9) {
                            var10 = var9 - (p_180709_3_.getY() + var4);
                            var11 = 2 - var10 / 2;

                            for (var12 = p_180709_3_.getX() - var11; var12 <= p_180709_3_.getX() + var11; ++var12) {
                                for (var13 = p_180709_3_.getZ() - var11; var13 <= p_180709_3_.getZ() + var11; ++var13) {
                                    BlockPos var23 = new BlockPos(var12, var9, var13);

                                    if (worldIn.getBlockState(var23).getBlock().getMaterial() == Material.leaves) {
                                        BlockPos var24 = var23.offsetWest();
                                        var16 = var23.offsetEast();
                                        BlockPos var25 = var23.offsetNorth();
                                        BlockPos var18 = var23.offsetSouth();

                                        if (p_180709_2_.nextInt(4) == 0 && worldIn.getBlockState(var24).getBlock().getMaterial() == Material.air) {
                                            this.func_175923_a(worldIn, var24, BlockVine.field_176275_S);
                                        }

                                        if (p_180709_2_.nextInt(4) == 0 && worldIn.getBlockState(var16).getBlock().getMaterial() == Material.air) {
                                            this.func_175923_a(worldIn, var16, BlockVine.field_176271_T);
                                        }

                                        if (p_180709_2_.nextInt(4) == 0 && worldIn.getBlockState(var25).getBlock().getMaterial() == Material.air) {
                                            this.func_175923_a(worldIn, var25, BlockVine.field_176272_Q);
                                        }

                                        if (p_180709_2_.nextInt(4) == 0 && worldIn.getBlockState(var18).getBlock().getMaterial() == Material.air) {
                                            this.func_175923_a(worldIn, var18, BlockVine.field_176276_R);
                                        }
                                    }
                                }
                            }
                        }

                        if (p_180709_2_.nextInt(5) == 0 && var4 > 5) {
                            for (var9 = 0; var9 < 2; ++var9) {
                                for (var10 = 0; var10 < 4; ++var10) {
                                    if (p_180709_2_.nextInt(4 - var9) == 0) {
                                        var11 = p_180709_2_.nextInt(3);
                                        EnumFacing var22 = EnumFacing.getHorizontal(var10).getOpposite();
                                        this.func_175905_a(worldIn, p_180709_3_.add(var22.getFrontOffsetX(), var4 - 5 + var9, var22.getFrontOffsetZ()), Blocks.cocoa, var11 << 2 | EnumFacing.getHorizontal(var10).getHorizontalIndex());
                                    }
                                }
                            }
                        }
                    }

                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    private void func_175923_a(World worldIn, BlockPos p_175923_2_, int p_175923_3_) {
        this.func_175905_a(worldIn, p_175923_2_, Blocks.vine, p_175923_3_);
        int var4 = 4;

        for (p_175923_2_ = p_175923_2_.offsetDown(); worldIn.getBlockState(p_175923_2_).getBlock().getMaterial() == Material.air && var4 > 0; --var4) {
            this.func_175905_a(worldIn, p_175923_2_, Blocks.vine, p_175923_3_);
            p_175923_2_ = p_175923_2_.offsetDown();
        }
    }
}
