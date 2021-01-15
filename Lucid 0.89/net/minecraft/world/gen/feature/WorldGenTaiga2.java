package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenTaiga2 extends WorldGenAbstractTree
{

    public WorldGenTaiga2(boolean p_i2025_1_)
    {
        super(p_i2025_1_);
    }

    @Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        int var4 = rand.nextInt(4) + 6;
        int var5 = 1 + rand.nextInt(2);
        int var6 = var4 - var5;
        int var7 = 2 + rand.nextInt(2);
        boolean var8 = true;

        if (position.getY() >= 1 && position.getY() + var4 + 1 <= 256)
        {
            int var11;
            int var21;

            for (int var9 = position.getY(); var9 <= position.getY() + 1 + var4 && var8; ++var9)
            {
                if (var9 - position.getY() < var5)
                {
                    var21 = 0;
                }
                else
                {
                    var21 = var7;
                }

                for (var11 = position.getX() - var21; var11 <= position.getX() + var21 && var8; ++var11)
                {
                    for (int var12 = position.getZ() - var21; var12 <= position.getZ() + var21 && var8; ++var12)
                    {
                        if (var9 >= 0 && var9 < 256)
                        {
                            Block var13 = worldIn.getBlockState(new BlockPos(var11, var9, var12)).getBlock();

                            if (var13.getMaterial() != Material.air && var13.getMaterial() != Material.leaves)
                            {
                                var8 = false;
                            }
                        }
                        else
                        {
                            var8 = false;
                        }
                    }
                }
            }

            if (!var8)
            {
                return false;
            }
            else
            {
                Block var20 = worldIn.getBlockState(position.down()).getBlock();

                if ((var20 == Blocks.grass || var20 == Blocks.dirt || var20 == Blocks.farmland) && position.getY() < 256 - var4 - 1)
                {
                    this.func_175921_a(worldIn, position.down());
                    var21 = rand.nextInt(2);
                    var11 = 1;
                    byte var22 = 0;
                    int var14;
                    int var23;

                    for (var23 = 0; var23 <= var6; ++var23)
                    {
                        var14 = position.getY() + var4 - var23;

                        for (int var15 = position.getX() - var21; var15 <= position.getX() + var21; ++var15)
                        {
                            int var16 = var15 - position.getX();

                            for (int var17 = position.getZ() - var21; var17 <= position.getZ() + var21; ++var17)
                            {
                                int var18 = var17 - position.getZ();

                                if (Math.abs(var16) != var21 || Math.abs(var18) != var21 || var21 <= 0)
                                {
                                    BlockPos var19 = new BlockPos(var15, var14, var17);

                                    if (!worldIn.getBlockState(var19).getBlock().isFullBlock())
                                    {
                                        this.func_175905_a(worldIn, var19, Blocks.leaves, BlockPlanks.EnumType.SPRUCE.getMetadata());
                                    }
                                }
                            }
                        }

                        if (var21 >= var11)
                        {
                            var21 = var22;
                            var22 = 1;
                            ++var11;

                            if (var11 > var7)
                            {
                                var11 = var7;
                            }
                        }
                        else
                        {
                            ++var21;
                        }
                    }

                    var23 = rand.nextInt(3);

                    for (var14 = 0; var14 < var4 - var23; ++var14)
                    {
                        Block var24 = worldIn.getBlockState(position.up(var14)).getBlock();

                        if (var24.getMaterial() == Material.air || var24.getMaterial() == Material.leaves)
                        {
                            this.func_175905_a(worldIn, position.up(var14), Blocks.log, BlockPlanks.EnumType.SPRUCE.getMetadata());
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
}
