// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;

public class WorldGenShrub extends WorldGenTrees
{
    private final IBlockState leavesMetadata;
    private final IBlockState woodMetadata;
    
    public WorldGenShrub(final IBlockState p_i46450_1_, final IBlockState p_i46450_2_) {
        super(false);
        this.woodMetadata = p_i46450_1_;
        this.leavesMetadata = p_i46450_2_;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, BlockPos position) {
        Block block;
        while (((block = worldIn.getBlockState(position).getBlock()).getMaterial() == Material.air || block.getMaterial() == Material.leaves) && position.getY() > 0) {
            position = position.down();
        }
        final Block block2 = worldIn.getBlockState(position).getBlock();
        if (block2 == Blocks.dirt || block2 == Blocks.grass) {
            position = position.up();
            this.setBlockAndNotifyAdequately(worldIn, position, this.woodMetadata);
            for (int i = position.getY(); i <= position.getY() + 2; ++i) {
                final int j = i - position.getY();
                for (int k = 2 - j, l = position.getX() - k; l <= position.getX() + k; ++l) {
                    final int i2 = l - position.getX();
                    for (int j2 = position.getZ() - k; j2 <= position.getZ() + k; ++j2) {
                        final int k2 = j2 - position.getZ();
                        if (Math.abs(i2) != k || Math.abs(k2) != k || rand.nextInt(2) != 0) {
                            final BlockPos blockpos = new BlockPos(l, i, j2);
                            if (!worldIn.getBlockState(blockpos).getBlock().isFullBlock()) {
                                this.setBlockAndNotifyAdequately(worldIn, blockpos, this.leavesMetadata);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
