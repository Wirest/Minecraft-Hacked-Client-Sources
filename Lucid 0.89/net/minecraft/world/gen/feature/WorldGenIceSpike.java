package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class WorldGenIceSpike extends WorldGenerator
{

    @Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        while (worldIn.isAirBlock(position) && position.getY() > 2)
        {
            position = position.down();
        }

        if (worldIn.getBlockState(position).getBlock() != Blocks.snow)
        {
            return false;
        }
        else
        {
            position = position.up(rand.nextInt(4));
            int var4 = rand.nextInt(4) + 7;
            int var5 = var4 / 4 + rand.nextInt(2);

            if (var5 > 1 && rand.nextInt(60) == 0)
            {
                position = position.up(10 + rand.nextInt(30));
            }

            int var6;
            int var8;

            for (var6 = 0; var6 < var4; ++var6)
            {
                float var7 = (1.0F - (float)var6 / (float)var4) * var5;
                var8 = MathHelper.ceiling_float_int(var7);

                for (int var9 = -var8; var9 <= var8; ++var9)
                {
                    float var10 = MathHelper.abs_int(var9) - 0.25F;

                    for (int var11 = -var8; var11 <= var8; ++var11)
                    {
                        float var12 = MathHelper.abs_int(var11) - 0.25F;

                        if ((var9 == 0 && var11 == 0 || var10 * var10 + var12 * var12 <= var7 * var7) && (var9 != -var8 && var9 != var8 && var11 != -var8 && var11 != var8 || rand.nextFloat() <= 0.75F))
                        {
                            Block var13 = worldIn.getBlockState(position.add(var9, var6, var11)).getBlock();

                            if (var13.getMaterial() == Material.air || var13 == Blocks.dirt || var13 == Blocks.snow || var13 == Blocks.ice)
                            {
                                this.func_175906_a(worldIn, position.add(var9, var6, var11), Blocks.packed_ice);
                            }

                            if (var6 != 0 && var8 > 1)
                            {
                                var13 = worldIn.getBlockState(position.add(var9, -var6, var11)).getBlock();

                                if (var13.getMaterial() == Material.air || var13 == Blocks.dirt || var13 == Blocks.snow || var13 == Blocks.ice)
                                {
                                    this.func_175906_a(worldIn, position.add(var9, -var6, var11), Blocks.packed_ice);
                                }
                            }
                        }
                    }
                }
            }

            var6 = var5 - 1;

            if (var6 < 0)
            {
                var6 = 0;
            }
            else if (var6 > 1)
            {
                var6 = 1;
            }

            for (int var14 = -var6; var14 <= var6; ++var14)
            {
                var8 = -var6;

                while (var8 <= var6)
                {
                    BlockPos var15 = position.add(var14, -1, var8);
                    int var16 = 50;

                    if (Math.abs(var14) == 1 && Math.abs(var8) == 1)
                    {
                        var16 = rand.nextInt(5);
                    }

                    while (true)
                    {
                        if (var15.getY() > 50)
                        {
                            Block var17 = worldIn.getBlockState(var15).getBlock();

                            if (var17.getMaterial() == Material.air || var17 == Blocks.dirt || var17 == Blocks.snow || var17 == Blocks.ice || var17 == Blocks.packed_ice)
                            {
                                this.func_175906_a(worldIn, var15, Blocks.packed_ice);
                                var15 = var15.down();
                                --var16;

                                if (var16 <= 0)
                                {
                                    var15 = var15.down(rand.nextInt(5) + 1);
                                    var16 = rand.nextInt(5);
                                }

                                continue;
                            }
                        }

                        ++var8;
                        break;
                    }
                }
            }

            return true;
        }
    }
}
