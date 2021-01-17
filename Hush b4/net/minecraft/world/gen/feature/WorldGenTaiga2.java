// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockOldLog;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;

public class WorldGenTaiga2 extends WorldGenAbstractTree
{
    private static final IBlockState field_181645_a;
    private static final IBlockState field_181646_b;
    
    static {
        field_181645_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
        field_181646_b = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty((IProperty<Comparable>)BlockLeaves.CHECK_DECAY, false);
    }
    
    public WorldGenTaiga2(final boolean p_i2025_1_) {
        super(p_i2025_1_);
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        final int i = rand.nextInt(4) + 6;
        final int j = 1 + rand.nextInt(2);
        final int k = i - j;
        final int l = 2 + rand.nextInt(2);
        boolean flag = true;
        if (position.getY() < 1 || position.getY() + i + 1 > 256) {
            return false;
        }
        for (int i2 = position.getY(); i2 <= position.getY() + 1 + i && flag; ++i2) {
            int j2 = 1;
            if (i2 - position.getY() < j) {
                j2 = 0;
            }
            else {
                j2 = l;
            }
            final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            for (int k2 = position.getX() - j2; k2 <= position.getX() + j2 && flag; ++k2) {
                for (int l2 = position.getZ() - j2; l2 <= position.getZ() + j2 && flag; ++l2) {
                    if (i2 >= 0 && i2 < 256) {
                        final Block block = worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(k2, i2, l2)).getBlock();
                        if (block.getMaterial() != Material.air && block.getMaterial() != Material.leaves) {
                            flag = false;
                        }
                    }
                    else {
                        flag = false;
                    }
                }
            }
        }
        if (!flag) {
            return false;
        }
        final Block block2 = worldIn.getBlockState(position.down()).getBlock();
        if ((block2 == Blocks.grass || block2 == Blocks.dirt || block2 == Blocks.farmland) && position.getY() < 256 - i - 1) {
            this.func_175921_a(worldIn, position.down());
            int i3 = rand.nextInt(2);
            int j3 = 1;
            int k3 = 0;
            for (int l3 = 0; l3 <= k; ++l3) {
                final int j4 = position.getY() + i - l3;
                for (int i4 = position.getX() - i3; i4 <= position.getX() + i3; ++i4) {
                    final int j5 = i4 - position.getX();
                    for (int k4 = position.getZ() - i3; k4 <= position.getZ() + i3; ++k4) {
                        final int l4 = k4 - position.getZ();
                        if (Math.abs(j5) != i3 || Math.abs(l4) != i3 || i3 <= 0) {
                            final BlockPos blockpos = new BlockPos(i4, j4, k4);
                            if (!worldIn.getBlockState(blockpos).getBlock().isFullBlock()) {
                                this.setBlockAndNotifyAdequately(worldIn, blockpos, WorldGenTaiga2.field_181646_b);
                            }
                        }
                    }
                }
                if (i3 >= j3) {
                    i3 = k3;
                    k3 = 1;
                    if (++j3 > l) {
                        j3 = l;
                    }
                }
                else {
                    ++i3;
                }
            }
            for (int i5 = rand.nextInt(3), k5 = 0; k5 < i - i5; ++k5) {
                final Block block3 = worldIn.getBlockState(position.up(k5)).getBlock();
                if (block3.getMaterial() == Material.air || block3.getMaterial() == Material.leaves) {
                    this.setBlockAndNotifyAdequately(worldIn, position.up(k5), WorldGenTaiga2.field_181645_a);
                }
            }
            return true;
        }
        return false;
    }
}
