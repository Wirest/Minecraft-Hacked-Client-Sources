package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenSpikes extends WorldGenerator
{
    private Block field_150520_a;

    public WorldGenSpikes(Block p_i45464_1_)
    {
        this.field_150520_a = p_i45464_1_;
    }

    @Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        if (worldIn.isAirBlock(position) && worldIn.getBlockState(position.down()).getBlock() == this.field_150520_a)
        {
            int var4 = rand.nextInt(32) + 6;
            int var5 = rand.nextInt(4) + 1;
            int var6;
            int var7;
            int var8;
            int var9;

            for (var6 = position.getX() - var5; var6 <= position.getX() + var5; ++var6)
            {
                for (var7 = position.getZ() - var5; var7 <= position.getZ() + var5; ++var7)
                {
                    var8 = var6 - position.getX();
                    var9 = var7 - position.getZ();

                    if (var8 * var8 + var9 * var9 <= var5 * var5 + 1 && worldIn.getBlockState(new BlockPos(var6, position.getY() - 1, var7)).getBlock() != this.field_150520_a)
                    {
                        return false;
                    }
                }
            }

            for (var6 = position.getY(); var6 < position.getY() + var4 && var6 < 256; ++var6)
            {
                for (var7 = position.getX() - var5; var7 <= position.getX() + var5; ++var7)
                {
                    for (var8 = position.getZ() - var5; var8 <= position.getZ() + var5; ++var8)
                    {
                        var9 = var7 - position.getX();
                        int var10 = var8 - position.getZ();

                        if (var9 * var9 + var10 * var10 <= var5 * var5 + 1)
                        {
                            worldIn.setBlockState(new BlockPos(var7, var6, var8), Blocks.obsidian.getDefaultState(), 2);
                        }
                    }
                }
            }

            EntityEnderCrystal var11 = new EntityEnderCrystal(worldIn);
            var11.setLocationAndAngles(position.getX() + 0.5F, position.getY() + var4, position.getZ() + 0.5F, rand.nextFloat() * 360.0F, 0.0F);
            worldIn.spawnEntityInWorld(var11);
            worldIn.setBlockState(position.up(var4), Blocks.bedrock.getDefaultState(), 2);
            return true;
        }
        else
        {
            return false;
        }
    }
}
