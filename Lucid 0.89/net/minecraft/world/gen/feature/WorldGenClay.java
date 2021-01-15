package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenClay extends WorldGenerator
{
    private Block field_150546_a;

    /** The number of blocks to generate. */
    private int numberOfBlocks;

    public WorldGenClay(int p_i2011_1_)
    {
        this.field_150546_a = Blocks.clay;
        this.numberOfBlocks = p_i2011_1_;
    }

    @Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        if (worldIn.getBlockState(position).getBlock().getMaterial() != Material.water)
        {
            return false;
        }
        else
        {
            int var4 = rand.nextInt(this.numberOfBlocks - 2) + 2;
            byte var5 = 1;

            for (int var6 = position.getX() - var4; var6 <= position.getX() + var4; ++var6)
            {
                for (int var7 = position.getZ() - var4; var7 <= position.getZ() + var4; ++var7)
                {
                    int var8 = var6 - position.getX();
                    int var9 = var7 - position.getZ();

                    if (var8 * var8 + var9 * var9 <= var4 * var4)
                    {
                        for (int var10 = position.getY() - var5; var10 <= position.getY() + var5; ++var10)
                        {
                            BlockPos var11 = new BlockPos(var6, var10, var7);
                            Block var12 = worldIn.getBlockState(var11).getBlock();

                            if (var12 == Blocks.dirt || var12 == Blocks.clay)
                            {
                                worldIn.setBlockState(var11, this.field_150546_a.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }

            return true;
        }
    }
}
