package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenCanopyTree extends WorldGenAbstractTree
{
    private static final String __OBFID = "CL_00000430";

    public WorldGenCanopyTree(boolean p_i45461_1_)
    {
        super(p_i45461_1_);
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        int var4 = p_180709_2_.nextInt(3) + p_180709_2_.nextInt(2) + 6;
        boolean var5 = true;

        if (p_180709_3_.getY() >= 1 && p_180709_3_.getY() + var4 + 1 <= 256)
        {
            int var8;
            int var9;

            for (int var6 = p_180709_3_.getY(); var6 <= p_180709_3_.getY() + 1 + var4; ++var6)
            {
                byte var7 = 1;

                if (var6 == p_180709_3_.getY())
                {
                    var7 = 0;
                }

                if (var6 >= p_180709_3_.getY() + 1 + var4 - 2)
                {
                    var7 = 2;
                }

                for (var8 = p_180709_3_.getX() - var7; var8 <= p_180709_3_.getX() + var7 && var5; ++var8)
                {
                    for (var9 = p_180709_3_.getZ() - var7; var9 <= p_180709_3_.getZ() + var7 && var5; ++var9)
                    {
                        if (var6 >= 0 && var6 < 256)
                        {
                            if (!this.func_150523_a(worldIn.getBlockState(new BlockPos(var8, var6, var9)).getBlock()))
                            {
                                var5 = false;
                            }
                        }
                        else
                        {
                            var5 = false;
                        }
                    }
                }
            }

            if (!var5)
            {
                return false;
            }
            else
            {
                Block var18 = worldIn.getBlockState(p_180709_3_.offsetDown()).getBlock();

                if ((var18 == Blocks.grass || var18 == Blocks.dirt) && p_180709_3_.getY() < 256 - var4 - 1)
                {
                    this.func_175921_a(worldIn, p_180709_3_.offsetDown());
                    this.func_175921_a(worldIn, p_180709_3_.add(1, -1, 0));
                    this.func_175921_a(worldIn, p_180709_3_.add(1, -1, 1));
                    this.func_175921_a(worldIn, p_180709_3_.add(0, -1, 1));
                    EnumFacing var19 = EnumFacing.Plane.HORIZONTAL.random(p_180709_2_);
                    var8 = var4 - p_180709_2_.nextInt(4);
                    var9 = 2 - p_180709_2_.nextInt(3);
                    int var10 = p_180709_3_.getX();
                    int var11 = p_180709_3_.getZ();
                    int var12 = 0;
                    int var13;
                    int var14;

                    for (var13 = 0; var13 < var4; ++var13)
                    {
                        var14 = p_180709_3_.getY() + var13;

                        if (var13 >= var8 && var9 > 0)
                        {
                            var10 += var19.getFrontOffsetX();
                            var11 += var19.getFrontOffsetZ();
                            --var9;
                        }

                        BlockPos var15 = new BlockPos(var10, var14, var11);
                        Material var16 = worldIn.getBlockState(var15).getBlock().getMaterial();

                        if (var16 == Material.air || var16 == Material.leaves)
                        {
                            this.func_175905_a(worldIn, var15, Blocks.log2, BlockPlanks.EnumType.DARK_OAK.func_176839_a() - 4);
                            this.func_175905_a(worldIn, var15.offsetEast(), Blocks.log2, BlockPlanks.EnumType.DARK_OAK.func_176839_a() - 4);
                            this.func_175905_a(worldIn, var15.offsetSouth(), Blocks.log2, BlockPlanks.EnumType.DARK_OAK.func_176839_a() - 4);
                            this.func_175905_a(worldIn, var15.offsetEast().offsetSouth(), Blocks.log2, BlockPlanks.EnumType.DARK_OAK.func_176839_a() - 4);
                            var12 = var14;
                        }
                    }

                    for (var13 = -2; var13 <= 0; ++var13)
                    {
                        for (var14 = -2; var14 <= 0; ++var14)
                        {
                            byte var20 = -1;
                            this.func_150526_a(worldIn, var10 + var13, var12 + var20, var11 + var14);
                            this.func_150526_a(worldIn, 1 + var10 - var13, var12 + var20, var11 + var14);
                            this.func_150526_a(worldIn, var10 + var13, var12 + var20, 1 + var11 - var14);
                            this.func_150526_a(worldIn, 1 + var10 - var13, var12 + var20, 1 + var11 - var14);

                            if ((var13 > -2 || var14 > -1) && (var13 != -1 || var14 != -2))
                            {
                                byte var21 = 1;
                                this.func_150526_a(worldIn, var10 + var13, var12 + var21, var11 + var14);
                                this.func_150526_a(worldIn, 1 + var10 - var13, var12 + var21, var11 + var14);
                                this.func_150526_a(worldIn, var10 + var13, var12 + var21, 1 + var11 - var14);
                                this.func_150526_a(worldIn, 1 + var10 - var13, var12 + var21, 1 + var11 - var14);
                            }
                        }
                    }

                    if (p_180709_2_.nextBoolean())
                    {
                        this.func_150526_a(worldIn, var10, var12 + 2, var11);
                        this.func_150526_a(worldIn, var10 + 1, var12 + 2, var11);
                        this.func_150526_a(worldIn, var10 + 1, var12 + 2, var11 + 1);
                        this.func_150526_a(worldIn, var10, var12 + 2, var11 + 1);
                    }

                    for (var13 = -3; var13 <= 4; ++var13)
                    {
                        for (var14 = -3; var14 <= 4; ++var14)
                        {
                            if ((var13 != -3 || var14 != -3) && (var13 != -3 || var14 != 4) && (var13 != 4 || var14 != -3) && (var13 != 4 || var14 != 4) && (Math.abs(var13) < 3 || Math.abs(var14) < 3))
                            {
                                this.func_150526_a(worldIn, var10 + var13, var12, var11 + var14);
                            }
                        }
                    }

                    for (var13 = -1; var13 <= 2; ++var13)
                    {
                        for (var14 = -1; var14 <= 2; ++var14)
                        {
                            if ((var13 < 0 || var13 > 1 || var14 < 0 || var14 > 1) && p_180709_2_.nextInt(3) <= 0)
                            {
                                int var22 = p_180709_2_.nextInt(3) + 2;
                                int var23;

                                for (var23 = 0; var23 < var22; ++var23)
                                {
                                    this.func_175905_a(worldIn, new BlockPos(p_180709_3_.getX() + var13, var12 - var23 - 1, p_180709_3_.getZ() + var14), Blocks.log2, BlockPlanks.EnumType.DARK_OAK.func_176839_a() - 4);
                                }

                                int var17;

                                for (var23 = -1; var23 <= 1; ++var23)
                                {
                                    for (var17 = -1; var17 <= 1; ++var17)
                                    {
                                        this.func_150526_a(worldIn, var10 + var13 + var23, var12 - 0, var11 + var14 + var17);
                                    }
                                }

                                for (var23 = -2; var23 <= 2; ++var23)
                                {
                                    for (var17 = -2; var17 <= 2; ++var17)
                                    {
                                        if (Math.abs(var23) != 2 || Math.abs(var17) != 2)
                                        {
                                            this.func_150526_a(worldIn, var10 + var13 + var23, var12 - 1, var11 + var14 + var17);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }
    }

    private void func_150526_a(World worldIn, int p_150526_2_, int p_150526_3_, int p_150526_4_)
    {
        Block var5 = worldIn.getBlockState(new BlockPos(p_150526_2_, p_150526_3_, p_150526_4_)).getBlock();

        if (var5.getMaterial() == Material.air)
        {
            this.func_175905_a(worldIn, new BlockPos(p_150526_2_, p_150526_3_, p_150526_4_), Blocks.leaves2, 1);
        }
    }
}
