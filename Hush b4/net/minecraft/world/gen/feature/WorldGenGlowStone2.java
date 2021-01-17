// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;

public class WorldGenGlowStone2 extends WorldGenerator
{
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        if (!worldIn.isAirBlock(position)) {
            return false;
        }
        if (worldIn.getBlockState(position.up()).getBlock() != Blocks.netherrack) {
            return false;
        }
        worldIn.setBlockState(position, Blocks.glowstone.getDefaultState(), 2);
        for (int i = 0; i < 1500; ++i) {
            final BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), -rand.nextInt(12), rand.nextInt(8) - rand.nextInt(8));
            if (worldIn.getBlockState(blockpos).getBlock().getMaterial() == Material.air) {
                int j = 0;
                EnumFacing[] values;
                for (int length = (values = EnumFacing.values()).length, k = 0; k < length; ++k) {
                    final EnumFacing enumfacing = values[k];
                    if (worldIn.getBlockState(blockpos.offset(enumfacing)).getBlock() == Blocks.glowstone) {
                        ++j;
                    }
                    if (j > 1) {
                        break;
                    }
                }
                if (j == 1) {
                    worldIn.setBlockState(blockpos, Blocks.glowstone.getDefaultState(), 2);
                }
            }
        }
        return true;
    }
}
