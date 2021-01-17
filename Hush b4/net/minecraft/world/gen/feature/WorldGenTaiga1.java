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

public class WorldGenTaiga1 extends WorldGenAbstractTree
{
    private static final IBlockState field_181636_a;
    private static final IBlockState field_181637_b;
    
    static {
        field_181636_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
        field_181637_b = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty((IProperty<Comparable>)BlockLeaves.CHECK_DECAY, false);
    }
    
    public WorldGenTaiga1() {
        super(false);
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        final int i = rand.nextInt(5) + 7;
        final int j = i - rand.nextInt(2) - 3;
        final int k = i - j;
        final int l = 1 + rand.nextInt(k + 1);
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
                        if (!this.func_150523_a(worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(k2, i2, l2)).getBlock())) {
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
        final Block block = worldIn.getBlockState(position.down()).getBlock();
        if ((block == Blocks.grass || block == Blocks.dirt) && position.getY() < 256 - i - 1) {
            this.func_175921_a(worldIn, position.down());
            int k3 = 0;
            for (int l3 = position.getY() + i; l3 >= position.getY() + j; --l3) {
                for (int j3 = position.getX() - k3; j3 <= position.getX() + k3; ++j3) {
                    final int k4 = j3 - position.getX();
                    for (int i3 = position.getZ() - k3; i3 <= position.getZ() + k3; ++i3) {
                        final int j4 = i3 - position.getZ();
                        if (Math.abs(k4) != k3 || Math.abs(j4) != k3 || k3 <= 0) {
                            final BlockPos blockpos = new BlockPos(j3, l3, i3);
                            if (!worldIn.getBlockState(blockpos).getBlock().isFullBlock()) {
                                this.setBlockAndNotifyAdequately(worldIn, blockpos, WorldGenTaiga1.field_181637_b);
                            }
                        }
                    }
                }
                if (k3 >= 1 && l3 == position.getY() + j + 1) {
                    --k3;
                }
                else if (k3 < l) {
                    ++k3;
                }
            }
            for (int i4 = 0; i4 < i - 1; ++i4) {
                final Block block2 = worldIn.getBlockState(position.up(i4)).getBlock();
                if (block2.getMaterial() == Material.air || block2.getMaterial() == Material.leaves) {
                    this.setBlockAndNotifyAdequately(worldIn, position.up(i4), WorldGenTaiga1.field_181636_a);
                }
            }
            return true;
        }
        return false;
    }
}
