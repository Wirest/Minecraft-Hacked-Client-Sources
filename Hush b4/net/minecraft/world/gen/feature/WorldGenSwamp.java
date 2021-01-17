// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockOldLog;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;

public class WorldGenSwamp extends WorldGenAbstractTree
{
    private static final IBlockState field_181648_a;
    private static final IBlockState field_181649_b;
    
    static {
        field_181648_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
        field_181649_b = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty((IProperty<Comparable>)BlockOldLeaf.CHECK_DECAY, false);
    }
    
    public WorldGenSwamp() {
        super(false);
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, BlockPos position) {
        final int i = rand.nextInt(4) + 5;
        while (worldIn.getBlockState(position.down()).getBlock().getMaterial() == Material.water) {
            position = position.down();
        }
        boolean flag = true;
        if (position.getY() < 1 || position.getY() + i + 1 > 256) {
            return false;
        }
        for (int j = position.getY(); j <= position.getY() + 1 + i; ++j) {
            int k = 1;
            if (j == position.getY()) {
                k = 0;
            }
            if (j >= position.getY() + 1 + i - 2) {
                k = 3;
            }
            final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l) {
                for (int i2 = position.getZ() - k; i2 <= position.getZ() + k && flag; ++i2) {
                    if (j >= 0 && j < 256) {
                        final Block block = worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(l, j, i2)).getBlock();
                        if (block.getMaterial() != Material.air && block.getMaterial() != Material.leaves) {
                            if (block != Blocks.water && block != Blocks.flowing_water) {
                                flag = false;
                            }
                            else if (j > position.getY()) {
                                flag = false;
                            }
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
        if ((block2 == Blocks.grass || block2 == Blocks.dirt) && position.getY() < 256 - i - 1) {
            this.func_175921_a(worldIn, position.down());
            for (int l2 = position.getY() - 3 + i; l2 <= position.getY() + i; ++l2) {
                final int k2 = l2 - (position.getY() + i);
                for (int i3 = 2 - k2 / 2, k3 = position.getX() - i3; k3 <= position.getX() + i3; ++k3) {
                    final int l3 = k3 - position.getX();
                    for (int j2 = position.getZ() - i3; j2 <= position.getZ() + i3; ++j2) {
                        final int k4 = j2 - position.getZ();
                        if (Math.abs(l3) != i3 || Math.abs(k4) != i3 || (rand.nextInt(2) != 0 && k2 != 0)) {
                            final BlockPos blockpos = new BlockPos(k3, l2, j2);
                            if (!worldIn.getBlockState(blockpos).getBlock().isFullBlock()) {
                                this.setBlockAndNotifyAdequately(worldIn, blockpos, WorldGenSwamp.field_181649_b);
                            }
                        }
                    }
                }
            }
            for (int i4 = 0; i4 < i; ++i4) {
                final Block block3 = worldIn.getBlockState(position.up(i4)).getBlock();
                if (block3.getMaterial() == Material.air || block3.getMaterial() == Material.leaves || block3 == Blocks.flowing_water || block3 == Blocks.water) {
                    this.setBlockAndNotifyAdequately(worldIn, position.up(i4), WorldGenSwamp.field_181648_a);
                }
            }
            for (int j3 = position.getY() - 3 + i; j3 <= position.getY() + i; ++j3) {
                final int l4 = j3 - (position.getY() + i);
                final int j4 = 2 - l4 / 2;
                final BlockPos.MutableBlockPos blockpos$mutableblockpos2 = new BlockPos.MutableBlockPos();
                for (int i5 = position.getX() - j4; i5 <= position.getX() + j4; ++i5) {
                    for (int j5 = position.getZ() - j4; j5 <= position.getZ() + j4; ++j5) {
                        blockpos$mutableblockpos2.func_181079_c(i5, j3, j5);
                        if (worldIn.getBlockState(blockpos$mutableblockpos2).getBlock().getMaterial() == Material.leaves) {
                            final BlockPos blockpos2 = blockpos$mutableblockpos2.west();
                            final BlockPos blockpos3 = blockpos$mutableblockpos2.east();
                            final BlockPos blockpos4 = blockpos$mutableblockpos2.north();
                            final BlockPos blockpos5 = blockpos$mutableblockpos2.south();
                            if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos2).getBlock().getMaterial() == Material.air) {
                                this.func_181647_a(worldIn, blockpos2, BlockVine.EAST);
                            }
                            if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos3).getBlock().getMaterial() == Material.air) {
                                this.func_181647_a(worldIn, blockpos3, BlockVine.WEST);
                            }
                            if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos4).getBlock().getMaterial() == Material.air) {
                                this.func_181647_a(worldIn, blockpos4, BlockVine.SOUTH);
                            }
                            if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos5).getBlock().getMaterial() == Material.air) {
                                this.func_181647_a(worldIn, blockpos5, BlockVine.NORTH);
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    private void func_181647_a(final World p_181647_1_, BlockPos p_181647_2_, final PropertyBool p_181647_3_) {
        final IBlockState iblockstate = Blocks.vine.getDefaultState().withProperty((IProperty<Comparable>)p_181647_3_, true);
        this.setBlockAndNotifyAdequately(p_181647_1_, p_181647_2_, iblockstate);
        int i;
        for (i = 4, p_181647_2_ = p_181647_2_.down(); p_181647_1_.getBlockState(p_181647_2_).getBlock().getMaterial() == Material.air && i > 0; p_181647_2_ = p_181647_2_.down(), --i) {
            this.setBlockAndNotifyAdequately(p_181647_1_, p_181647_2_, iblockstate);
        }
    }
}
