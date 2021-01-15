package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenSand extends WorldGenerator
{
    private Block field_150517_a;

    /** The maximum radius used when generating a patch of blocks. */
    private int radius;
    private static final String __OBFID = "CL_00000431";

    public WorldGenSand(Block p_i45462_1_, int p_i45462_2_)
    {
        this.field_150517_a = p_i45462_1_;
        this.radius = p_i45462_2_;
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        if (worldIn.getBlockState(p_180709_3_).getBlock().getMaterial() != Material.water)
        {
            return false;
        }
        else
        {
            int var4 = p_180709_2_.nextInt(this.radius - 2) + 2;
            byte var5 = 2;

            for (int var6 = p_180709_3_.getX() - var4; var6 <= p_180709_3_.getX() + var4; ++var6)
            {
                for (int var7 = p_180709_3_.getZ() - var4; var7 <= p_180709_3_.getZ() + var4; ++var7)
                {
                    int var8 = var6 - p_180709_3_.getX();
                    int var9 = var7 - p_180709_3_.getZ();

                    if (var8 * var8 + var9 * var9 <= var4 * var4)
                    {
                        for (int var10 = p_180709_3_.getY() - var5; var10 <= p_180709_3_.getY() + var5; ++var10)
                        {
                            BlockPos var11 = new BlockPos(var6, var10, var7);
                            Block var12 = worldIn.getBlockState(var11).getBlock();

                            if (var12 == Blocks.dirt || var12 == Blocks.grass)
                            {
                                worldIn.setBlockState(var11, this.field_150517_a.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }

            return true;
        }
    }
}
