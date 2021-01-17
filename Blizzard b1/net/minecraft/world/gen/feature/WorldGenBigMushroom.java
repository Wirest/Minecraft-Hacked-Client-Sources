package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenBigMushroom extends WorldGenerator
{
    /** The mushroom type. 0 for brown, 1 for red. */
    private int mushroomType = -1;
    private static final String __OBFID = "CL_00000415";

    public WorldGenBigMushroom(int p_i2017_1_)
    {
        super(true);
        this.mushroomType = p_i2017_1_;
    }

    public WorldGenBigMushroom()
    {
        super(false);
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        int var4 = p_180709_2_.nextInt(2);

        if (this.mushroomType >= 0)
        {
            var4 = this.mushroomType;
        }

        int var5 = p_180709_2_.nextInt(3) + 4;
        boolean var6 = true;

        if (p_180709_3_.getY() >= 1 && p_180709_3_.getY() + var5 + 1 < 256)
        {
            int var9;
            int var10;

            for (int var7 = p_180709_3_.getY(); var7 <= p_180709_3_.getY() + 1 + var5; ++var7)
            {
                byte var8 = 3;

                if (var7 <= p_180709_3_.getY() + 3)
                {
                    var8 = 0;
                }

                for (var9 = p_180709_3_.getX() - var8; var9 <= p_180709_3_.getX() + var8 && var6; ++var9)
                {
                    for (var10 = p_180709_3_.getZ() - var8; var10 <= p_180709_3_.getZ() + var8 && var6; ++var10)
                    {
                        if (var7 >= 0 && var7 < 256)
                        {
                            Block var11 = worldIn.getBlockState(new BlockPos(var9, var7, var10)).getBlock();

                            if (var11.getMaterial() != Material.air && var11.getMaterial() != Material.leaves)
                            {
                                var6 = false;
                            }
                        }
                        else
                        {
                            var6 = false;
                        }
                    }
                }
            }

            if (!var6)
            {
                return false;
            }
            else
            {
                Block var15 = worldIn.getBlockState(p_180709_3_.offsetDown()).getBlock();

                if (var15 != Blocks.dirt && var15 != Blocks.grass && var15 != Blocks.mycelium)
                {
                    return false;
                }
                else
                {
                    int var16 = p_180709_3_.getY() + var5;

                    if (var4 == 1)
                    {
                        var16 = p_180709_3_.getY() + var5 - 3;
                    }

                    for (var9 = var16; var9 <= p_180709_3_.getY() + var5; ++var9)
                    {
                        var10 = 1;

                        if (var9 < p_180709_3_.getY() + var5)
                        {
                            ++var10;
                        }

                        if (var4 == 0)
                        {
                            var10 = 3;
                        }

                        for (int var18 = p_180709_3_.getX() - var10; var18 <= p_180709_3_.getX() + var10; ++var18)
                        {
                            for (int var12 = p_180709_3_.getZ() - var10; var12 <= p_180709_3_.getZ() + var10; ++var12)
                            {
                                int var13 = 5;

                                if (var18 == p_180709_3_.getX() - var10)
                                {
                                    --var13;
                                }

                                if (var18 == p_180709_3_.getX() + var10)
                                {
                                    ++var13;
                                }

                                if (var12 == p_180709_3_.getZ() - var10)
                                {
                                    var13 -= 3;
                                }

                                if (var12 == p_180709_3_.getZ() + var10)
                                {
                                    var13 += 3;
                                }

                                if (var4 == 0 || var9 < p_180709_3_.getY() + var5)
                                {
                                    if ((var18 == p_180709_3_.getX() - var10 || var18 == p_180709_3_.getX() + var10) && (var12 == p_180709_3_.getZ() - var10 || var12 == p_180709_3_.getZ() + var10))
                                    {
                                        continue;
                                    }

                                    if (var18 == p_180709_3_.getX() - (var10 - 1) && var12 == p_180709_3_.getZ() - var10)
                                    {
                                        var13 = 1;
                                    }

                                    if (var18 == p_180709_3_.getX() - var10 && var12 == p_180709_3_.getZ() - (var10 - 1))
                                    {
                                        var13 = 1;
                                    }

                                    if (var18 == p_180709_3_.getX() + (var10 - 1) && var12 == p_180709_3_.getZ() - var10)
                                    {
                                        var13 = 3;
                                    }

                                    if (var18 == p_180709_3_.getX() + var10 && var12 == p_180709_3_.getZ() - (var10 - 1))
                                    {
                                        var13 = 3;
                                    }

                                    if (var18 == p_180709_3_.getX() - (var10 - 1) && var12 == p_180709_3_.getZ() + var10)
                                    {
                                        var13 = 7;
                                    }

                                    if (var18 == p_180709_3_.getX() - var10 && var12 == p_180709_3_.getZ() + (var10 - 1))
                                    {
                                        var13 = 7;
                                    }

                                    if (var18 == p_180709_3_.getX() + (var10 - 1) && var12 == p_180709_3_.getZ() + var10)
                                    {
                                        var13 = 9;
                                    }

                                    if (var18 == p_180709_3_.getX() + var10 && var12 == p_180709_3_.getZ() + (var10 - 1))
                                    {
                                        var13 = 9;
                                    }
                                }

                                if (var13 == 5 && var9 < p_180709_3_.getY() + var5)
                                {
                                    var13 = 0;
                                }

                                if (var13 != 0 || p_180709_3_.getY() >= p_180709_3_.getY() + var5 - 1)
                                {
                                    BlockPos var14 = new BlockPos(var18, var9, var12);

                                    if (!worldIn.getBlockState(var14).getBlock().isFullBlock())
                                    {
                                        this.func_175905_a(worldIn, var14, Block.getBlockById(Block.getIdFromBlock(Blocks.brown_mushroom_block) + var4), var13);
                                    }
                                }
                            }
                        }
                    }

                    for (var9 = 0; var9 < var5; ++var9)
                    {
                        Block var17 = worldIn.getBlockState(p_180709_3_.offsetUp(var9)).getBlock();

                        if (!var17.isFullBlock())
                        {
                            this.func_175905_a(worldIn, p_180709_3_.offsetUp(var9), Block.getBlockById(Block.getIdFromBlock(Blocks.brown_mushroom_block) + var4), 10);
                        }
                    }

                    return true;
                }
            }
        }
        else
        {
            return false;
        }
    }
}
