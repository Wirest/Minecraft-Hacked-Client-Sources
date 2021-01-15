package net.minecraft.world.gen.feature;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenBlockBlob extends WorldGenerator
{
    private final Block field_150545_a;
    private final int field_150544_b;

    public WorldGenBlockBlob(Block p_i45450_1_, int p_i45450_2_)
    {
        super(false);
        this.field_150545_a = p_i45450_1_;
        this.field_150544_b = p_i45450_2_;
    }

    @Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        while (true)
        {
            if (position.getY() > 3)
            {
                label47:
                {
                    if (!worldIn.isAirBlock(position.down()))
                    {
                        Block var4 = worldIn.getBlockState(position.down()).getBlock();

                        if (var4 == Blocks.grass || var4 == Blocks.dirt || var4 == Blocks.stone)
                        {
                            break label47;
                        }
                    }

                    position = position.down();
                    continue;
                }
            }

            if (position.getY() <= 3)
            {
                return false;
            }

            int var12 = this.field_150544_b;

            for (int var5 = 0; var12 >= 0 && var5 < 3; ++var5)
            {
                int var6 = var12 + rand.nextInt(2);
                int var7 = var12 + rand.nextInt(2);
                int var8 = var12 + rand.nextInt(2);
                float var9 = (var6 + var7 + var8) * 0.333F + 0.5F;
                Iterator var10 = BlockPos.getAllInBox(position.add(-var6, -var7, -var8), position.add(var6, var7, var8)).iterator();

                while (var10.hasNext())
                {
                    BlockPos var11 = (BlockPos)var10.next();

                    if (var11.distanceSq(position) <= var9 * var9)
                    {
                        worldIn.setBlockState(var11, this.field_150545_a.getDefaultState(), 4);
                    }
                }

                position = position.add(-(var12 + 1) + rand.nextInt(2 + var12 * 2), 0 - rand.nextInt(2), -(var12 + 1) + rand.nextInt(2 + var12 * 2));
            }

            return true;
        }
    }
}
