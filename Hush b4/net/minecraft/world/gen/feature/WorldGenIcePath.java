// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;

public class WorldGenIcePath extends WorldGenerator
{
    private Block block;
    private int basePathWidth;
    
    public WorldGenIcePath(final int p_i45454_1_) {
        this.block = Blocks.packed_ice;
        this.basePathWidth = p_i45454_1_;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, BlockPos position) {
        while (worldIn.isAirBlock(position) && position.getY() > 2) {
            position = position.down();
        }
        if (worldIn.getBlockState(position).getBlock() != Blocks.snow) {
            return false;
        }
        final int i = rand.nextInt(this.basePathWidth - 2) + 2;
        final int j = 1;
        for (int k = position.getX() - i; k <= position.getX() + i; ++k) {
            for (int l = position.getZ() - i; l <= position.getZ() + i; ++l) {
                final int i2 = k - position.getX();
                final int j2 = l - position.getZ();
                if (i2 * i2 + j2 * j2 <= i * i) {
                    for (int k2 = position.getY() - j; k2 <= position.getY() + j; ++k2) {
                        final BlockPos blockpos = new BlockPos(k, k2, l);
                        final Block block = worldIn.getBlockState(blockpos).getBlock();
                        if (block == Blocks.dirt || block == Blocks.snow || block == Blocks.ice) {
                            worldIn.setBlockState(blockpos, this.block.getDefaultState(), 2);
                        }
                    }
                }
            }
        }
        return true;
    }
}
