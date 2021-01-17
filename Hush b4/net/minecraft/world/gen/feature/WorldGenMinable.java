// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.util.MathHelper;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.init.Blocks;
import com.google.common.base.Predicate;
import net.minecraft.block.state.IBlockState;

public class WorldGenMinable extends WorldGenerator
{
    private final IBlockState oreBlock;
    private final int numberOfBlocks;
    private final Predicate<IBlockState> predicate;
    
    public WorldGenMinable(final IBlockState state, final int blockCount) {
        this(state, blockCount, BlockHelper.forBlock(Blocks.stone));
    }
    
    public WorldGenMinable(final IBlockState state, final int blockCount, final Predicate<IBlockState> p_i45631_3_) {
        this.oreBlock = state;
        this.numberOfBlocks = blockCount;
        this.predicate = p_i45631_3_;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        final float f = rand.nextFloat() * 3.1415927f;
        final double d0 = position.getX() + 8 + MathHelper.sin(f) * this.numberOfBlocks / 8.0f;
        final double d2 = position.getX() + 8 - MathHelper.sin(f) * this.numberOfBlocks / 8.0f;
        final double d3 = position.getZ() + 8 + MathHelper.cos(f) * this.numberOfBlocks / 8.0f;
        final double d4 = position.getZ() + 8 - MathHelper.cos(f) * this.numberOfBlocks / 8.0f;
        final double d5 = position.getY() + rand.nextInt(3) - 2;
        final double d6 = position.getY() + rand.nextInt(3) - 2;
        for (int i = 0; i < this.numberOfBlocks; ++i) {
            final float f2 = i / (float)this.numberOfBlocks;
            final double d7 = d0 + (d2 - d0) * f2;
            final double d8 = d5 + (d6 - d5) * f2;
            final double d9 = d3 + (d4 - d3) * f2;
            final double d10 = rand.nextDouble() * this.numberOfBlocks / 16.0;
            final double d11 = (MathHelper.sin(3.1415927f * f2) + 1.0f) * d10 + 1.0;
            final double d12 = (MathHelper.sin(3.1415927f * f2) + 1.0f) * d10 + 1.0;
            final int j = MathHelper.floor_double(d7 - d11 / 2.0);
            final int k = MathHelper.floor_double(d8 - d12 / 2.0);
            final int l = MathHelper.floor_double(d9 - d11 / 2.0);
            final int i2 = MathHelper.floor_double(d7 + d11 / 2.0);
            final int j2 = MathHelper.floor_double(d8 + d12 / 2.0);
            final int k2 = MathHelper.floor_double(d9 + d11 / 2.0);
            for (int l2 = j; l2 <= i2; ++l2) {
                final double d13 = (l2 + 0.5 - d7) / (d11 / 2.0);
                if (d13 * d13 < 1.0) {
                    for (int i3 = k; i3 <= j2; ++i3) {
                        final double d14 = (i3 + 0.5 - d8) / (d12 / 2.0);
                        if (d13 * d13 + d14 * d14 < 1.0) {
                            for (int j3 = l; j3 <= k2; ++j3) {
                                final double d15 = (j3 + 0.5 - d9) / (d11 / 2.0);
                                if (d13 * d13 + d14 * d14 + d15 * d15 < 1.0) {
                                    final BlockPos blockpos = new BlockPos(l2, i3, j3);
                                    if (this.predicate.apply(worldIn.getBlockState(blockpos))) {
                                        worldIn.setBlockState(blockpos, this.oreBlock, 2);
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
