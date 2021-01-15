package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class WorldGenLakes extends WorldGenerator
{
    private Block field_150556_a;

    public WorldGenLakes(Block p_i45455_1_)
    {
        this.field_150556_a = p_i45455_1_;
    }

    @Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        for (position = position.add(-8, 0, -8); position.getY() > 5 && worldIn.isAirBlock(position); position = position.down())
        {
            ;
        }

        if (position.getY() <= 4)
        {
            return false;
        }
        else
        {
            position = position.down(4);
            boolean[] var4 = new boolean[2048];
            int var5 = rand.nextInt(4) + 4;
            int var6;

            for (var6 = 0; var6 < var5; ++var6)
            {
                double var7 = rand.nextDouble() * 6.0D + 3.0D;
                double var9 = rand.nextDouble() * 4.0D + 2.0D;
                double var11 = rand.nextDouble() * 6.0D + 3.0D;
                double var13 = rand.nextDouble() * (16.0D - var7 - 2.0D) + 1.0D + var7 / 2.0D;
                double var15 = rand.nextDouble() * (8.0D - var9 - 4.0D) + 2.0D + var9 / 2.0D;
                double var17 = rand.nextDouble() * (16.0D - var11 - 2.0D) + 1.0D + var11 / 2.0D;

                for (int var19 = 1; var19 < 15; ++var19)
                {
                    for (int var20 = 1; var20 < 15; ++var20)
                    {
                        for (int var21 = 1; var21 < 7; ++var21)
                        {
                            double var22 = (var19 - var13) / (var7 / 2.0D);
                            double var24 = (var21 - var15) / (var9 / 2.0D);
                            double var26 = (var20 - var17) / (var11 / 2.0D);
                            double var28 = var22 * var22 + var24 * var24 + var26 * var26;

                            if (var28 < 1.0D)
                            {
                                var4[(var19 * 16 + var20) * 8 + var21] = true;
                            }
                        }
                    }
                }
            }

            int var8;
            int var30;
            boolean var32;

            for (var6 = 0; var6 < 16; ++var6)
            {
                for (var30 = 0; var30 < 16; ++var30)
                {
                    for (var8 = 0; var8 < 8; ++var8)
                    {
                        var32 = !var4[(var6 * 16 + var30) * 8 + var8] && (var6 < 15 && var4[((var6 + 1) * 16 + var30) * 8 + var8] || var6 > 0 && var4[((var6 - 1) * 16 + var30) * 8 + var8] || var30 < 15 && var4[(var6 * 16 + var30 + 1) * 8 + var8] || var30 > 0 && var4[(var6 * 16 + (var30 - 1)) * 8 + var8] || var8 < 7 && var4[(var6 * 16 + var30) * 8 + var8 + 1] || var8 > 0 && var4[(var6 * 16 + var30) * 8 + (var8 - 1)]);

                        if (var32)
                        {
                            Material var10 = worldIn.getBlockState(position.add(var6, var8, var30)).getBlock().getMaterial();

                            if (var8 >= 4 && var10.isLiquid())
                            {
                                return false;
                            }

                            if (var8 < 4 && !var10.isSolid() && worldIn.getBlockState(position.add(var6, var8, var30)).getBlock() != this.field_150556_a)
                            {
                                return false;
                            }
                        }
                    }
                }
            }

            for (var6 = 0; var6 < 16; ++var6)
            {
                for (var30 = 0; var30 < 16; ++var30)
                {
                    for (var8 = 0; var8 < 8; ++var8)
                    {
                        if (var4[(var6 * 16 + var30) * 8 + var8])
                        {
                            worldIn.setBlockState(position.add(var6, var8, var30), var8 >= 4 ? Blocks.air.getDefaultState() : this.field_150556_a.getDefaultState(), 2);
                        }
                    }
                }
            }

            for (var6 = 0; var6 < 16; ++var6)
            {
                for (var30 = 0; var30 < 16; ++var30)
                {
                    for (var8 = 4; var8 < 8; ++var8)
                    {
                        if (var4[(var6 * 16 + var30) * 8 + var8])
                        {
                            BlockPos var33 = position.add(var6, var8 - 1, var30);

                            if (worldIn.getBlockState(var33).getBlock() == Blocks.dirt && worldIn.getLightFor(EnumSkyBlock.SKY, position.add(var6, var8, var30)) > 0)
                            {
                                BiomeGenBase var34 = worldIn.getBiomeGenForCoords(var33);

                                if (var34.topBlock.getBlock() == Blocks.mycelium)
                                {
                                    worldIn.setBlockState(var33, Blocks.mycelium.getDefaultState(), 2);
                                }
                                else
                                {
                                    worldIn.setBlockState(var33, Blocks.grass.getDefaultState(), 2);
                                }
                            }
                        }
                    }
                }
            }

            if (this.field_150556_a.getMaterial() == Material.lava)
            {
                for (var6 = 0; var6 < 16; ++var6)
                {
                    for (var30 = 0; var30 < 16; ++var30)
                    {
                        for (var8 = 0; var8 < 8; ++var8)
                        {
                            var32 = !var4[(var6 * 16 + var30) * 8 + var8] && (var6 < 15 && var4[((var6 + 1) * 16 + var30) * 8 + var8] || var6 > 0 && var4[((var6 - 1) * 16 + var30) * 8 + var8] || var30 < 15 && var4[(var6 * 16 + var30 + 1) * 8 + var8] || var30 > 0 && var4[(var6 * 16 + (var30 - 1)) * 8 + var8] || var8 < 7 && var4[(var6 * 16 + var30) * 8 + var8 + 1] || var8 > 0 && var4[(var6 * 16 + var30) * 8 + (var8 - 1)]);

                            if (var32 && (var8 < 4 || rand.nextInt(2) != 0) && worldIn.getBlockState(position.add(var6, var8, var30)).getBlock().getMaterial().isSolid())
                            {
                                worldIn.setBlockState(position.add(var6, var8, var30), Blocks.stone.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }

            if (this.field_150556_a.getMaterial() == Material.water)
            {
                for (var6 = 0; var6 < 16; ++var6)
                {
                    for (var30 = 0; var30 < 16; ++var30)
                    {
                        byte var31 = 4;

                        if (worldIn.canBlockFreezeWater(position.add(var6, var31, var30)))
                        {
                            worldIn.setBlockState(position.add(var6, var31, var30), Blocks.ice.getDefaultState(), 2);
                        }
                    }
                }
            }

            return true;
        }
    }
}
