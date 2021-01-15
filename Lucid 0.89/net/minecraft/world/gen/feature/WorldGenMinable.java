package net.minecraft.world.gen.feature;

import java.util.Random;

import com.google.common.base.Predicate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class WorldGenMinable extends WorldGenerator
{
    private final IBlockState oreBlock;

    /** The number of blocks to generate. */
    private final int numberOfBlocks;
    private final Predicate field_175919_c;

    public WorldGenMinable(IBlockState state, int p_i45630_2_)
    {
        this(state, p_i45630_2_, BlockHelper.forBlock(Blocks.stone));
    }

    public WorldGenMinable(IBlockState state, int blockCount, Predicate p_i45631_3_)
    {
        this.oreBlock = state;
        this.numberOfBlocks = blockCount;
        this.field_175919_c = p_i45631_3_;
    }

    @Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        float var4 = rand.nextFloat() * (float)Math.PI;
        double var5 = position.getX() + 8 + MathHelper.sin(var4) * this.numberOfBlocks / 8.0F;
        double var7 = position.getX() + 8 - MathHelper.sin(var4) * this.numberOfBlocks / 8.0F;
        double var9 = position.getZ() + 8 + MathHelper.cos(var4) * this.numberOfBlocks / 8.0F;
        double var11 = position.getZ() + 8 - MathHelper.cos(var4) * this.numberOfBlocks / 8.0F;
        double var13 = position.getY() + rand.nextInt(3) - 2;
        double var15 = position.getY() + rand.nextInt(3) - 2;

        for (int var17 = 0; var17 < this.numberOfBlocks; ++var17)
        {
            float var18 = (float)var17 / (float)this.numberOfBlocks;
            double var19 = var5 + (var7 - var5) * var18;
            double var21 = var13 + (var15 - var13) * var18;
            double var23 = var9 + (var11 - var9) * var18;
            double var25 = rand.nextDouble() * this.numberOfBlocks / 16.0D;
            double var27 = (MathHelper.sin((float)Math.PI * var18) + 1.0F) * var25 + 1.0D;
            double var29 = (MathHelper.sin((float)Math.PI * var18) + 1.0F) * var25 + 1.0D;
            int var31 = MathHelper.floor_double(var19 - var27 / 2.0D);
            int var32 = MathHelper.floor_double(var21 - var29 / 2.0D);
            int var33 = MathHelper.floor_double(var23 - var27 / 2.0D);
            int var34 = MathHelper.floor_double(var19 + var27 / 2.0D);
            int var35 = MathHelper.floor_double(var21 + var29 / 2.0D);
            int var36 = MathHelper.floor_double(var23 + var27 / 2.0D);

            for (int var37 = var31; var37 <= var34; ++var37)
            {
                double var38 = (var37 + 0.5D - var19) / (var27 / 2.0D);

                if (var38 * var38 < 1.0D)
                {
                    for (int var40 = var32; var40 <= var35; ++var40)
                    {
                        double var41 = (var40 + 0.5D - var21) / (var29 / 2.0D);

                        if (var38 * var38 + var41 * var41 < 1.0D)
                        {
                            for (int var43 = var33; var43 <= var36; ++var43)
                            {
                                double var44 = (var43 + 0.5D - var23) / (var27 / 2.0D);

                                if (var38 * var38 + var41 * var41 + var44 * var44 < 1.0D)
                                {
                                    BlockPos var46 = new BlockPos(var37, var40, var43);

                                    if (this.field_175919_c.apply(worldIn.getBlockState(var46)))
                                    {
                                        worldIn.setBlockState(var46, this.oreBlock, 2);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}
