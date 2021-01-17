// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.MathHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;

public class WorldGenIceSpike extends WorldGenerator
{
    @Override
    public boolean generate(final World worldIn, final Random rand, BlockPos position) {
        while (worldIn.isAirBlock(position) && position.getY() > 2) {
            position = position.down();
        }
        if (worldIn.getBlockState(position).getBlock() != Blocks.snow) {
            return false;
        }
        position = position.up(rand.nextInt(4));
        final int i = rand.nextInt(4) + 7;
        final int j = i / 4 + rand.nextInt(2);
        if (j > 1 && rand.nextInt(60) == 0) {
            position = position.up(10 + rand.nextInt(30));
        }
        for (int k = 0; k < i; ++k) {
            final float f = (1.0f - k / (float)i) * j;
            for (int l = MathHelper.ceiling_float_int(f), i2 = -l; i2 <= l; ++i2) {
                final float f2 = MathHelper.abs_int(i2) - 0.25f;
                for (int j2 = -l; j2 <= l; ++j2) {
                    final float f3 = MathHelper.abs_int(j2) - 0.25f;
                    if (((i2 == 0 && j2 == 0) || f2 * f2 + f3 * f3 <= f * f) && ((i2 != -l && i2 != l && j2 != -l && j2 != l) || rand.nextFloat() <= 0.75f)) {
                        Block block = worldIn.getBlockState(position.add(i2, k, j2)).getBlock();
                        if (block.getMaterial() == Material.air || block == Blocks.dirt || block == Blocks.snow || block == Blocks.ice) {
                            this.setBlockAndNotifyAdequately(worldIn, position.add(i2, k, j2), Blocks.packed_ice.getDefaultState());
                        }
                        if (k != 0 && l > 1) {
                            block = worldIn.getBlockState(position.add(i2, -k, j2)).getBlock();
                            if (block.getMaterial() == Material.air || block == Blocks.dirt || block == Blocks.snow || block == Blocks.ice) {
                                this.setBlockAndNotifyAdequately(worldIn, position.add(i2, -k, j2), Blocks.packed_ice.getDefaultState());
                            }
                        }
                    }
                }
            }
        }
        int k2 = j - 1;
        if (k2 < 0) {
            k2 = 0;
        }
        else if (k2 > 1) {
            k2 = 1;
        }
        for (int l2 = -k2; l2 <= k2; ++l2) {
            for (int i3 = -k2; i3 <= k2; ++i3) {
                BlockPos blockpos = position.add(l2, -1, i3);
                int j3 = 50;
                if (Math.abs(l2) == 1 && Math.abs(i3) == 1) {
                    j3 = rand.nextInt(5);
                }
                while (blockpos.getY() > 50) {
                    final Block block2 = worldIn.getBlockState(blockpos).getBlock();
                    if (block2.getMaterial() != Material.air && block2 != Blocks.dirt && block2 != Blocks.snow && block2 != Blocks.ice && block2 != Blocks.packed_ice) {
                        break;
                    }
                    this.setBlockAndNotifyAdequately(worldIn, blockpos, Blocks.packed_ice.getDefaultState());
                    blockpos = blockpos.down();
                    if (--j3 > 0) {
                        continue;
                    }
                    blockpos = blockpos.down(rand.nextInt(5) + 1);
                    j3 = rand.nextInt(5);
                }
            }
        }
        return true;
    }
}
