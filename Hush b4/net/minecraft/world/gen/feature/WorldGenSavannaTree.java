// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockNewLog;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;

public class WorldGenSavannaTree extends WorldGenAbstractTree
{
    private static final IBlockState field_181643_a;
    private static final IBlockState field_181644_b;
    
    static {
        field_181643_a = Blocks.log2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA);
        field_181644_b = Blocks.leaves2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty((IProperty<Comparable>)BlockLeaves.CHECK_DECAY, false);
    }
    
    public WorldGenSavannaTree(final boolean p_i45463_1_) {
        super(p_i45463_1_);
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        final int i = rand.nextInt(3) + rand.nextInt(3) + 5;
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
                k = 2;
            }
            final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l) {
                for (int i2 = position.getZ() - k; i2 <= position.getZ() + k && flag; ++i2) {
                    if (j >= 0 && j < 256) {
                        if (!this.func_150523_a(worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(l, j, i2)).getBlock())) {
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
            final EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(rand);
            final int k2 = i - rand.nextInt(4) - 1;
            int l2 = 3 - rand.nextInt(3);
            int i3 = position.getX();
            int j2 = position.getZ();
            int k3 = 0;
            for (int l3 = 0; l3 < i; ++l3) {
                final int i4 = position.getY() + l3;
                if (l3 >= k2 && l2 > 0) {
                    i3 += enumfacing.getFrontOffsetX();
                    j2 += enumfacing.getFrontOffsetZ();
                    --l2;
                }
                final BlockPos blockpos = new BlockPos(i3, i4, j2);
                final Material material = worldIn.getBlockState(blockpos).getBlock().getMaterial();
                if (material == Material.air || material == Material.leaves) {
                    this.func_181642_b(worldIn, blockpos);
                    k3 = i4;
                }
            }
            BlockPos blockpos2 = new BlockPos(i3, k3, j2);
            for (int j3 = -3; j3 <= 3; ++j3) {
                for (int i5 = -3; i5 <= 3; ++i5) {
                    if (Math.abs(j3) != 3 || Math.abs(i5) != 3) {
                        this.func_175924_b(worldIn, blockpos2.add(j3, 0, i5));
                    }
                }
            }
            blockpos2 = blockpos2.up();
            for (int k4 = -1; k4 <= 1; ++k4) {
                for (int j4 = -1; j4 <= 1; ++j4) {
                    this.func_175924_b(worldIn, blockpos2.add(k4, 0, j4));
                }
            }
            this.func_175924_b(worldIn, blockpos2.east(2));
            this.func_175924_b(worldIn, blockpos2.west(2));
            this.func_175924_b(worldIn, blockpos2.south(2));
            this.func_175924_b(worldIn, blockpos2.north(2));
            i3 = position.getX();
            j2 = position.getZ();
            final EnumFacing enumfacing2 = EnumFacing.Plane.HORIZONTAL.random(rand);
            if (enumfacing2 != enumfacing) {
                final int l4 = k2 - rand.nextInt(2) - 1;
                int k5 = 1 + rand.nextInt(3);
                k3 = 0;
                for (int l5 = l4; l5 < i && k5 > 0; ++l5, --k5) {
                    if (l5 >= 1) {
                        final int j5 = position.getY() + l5;
                        i3 += enumfacing2.getFrontOffsetX();
                        j2 += enumfacing2.getFrontOffsetZ();
                        final BlockPos blockpos3 = new BlockPos(i3, j5, j2);
                        final Material material2 = worldIn.getBlockState(blockpos3).getBlock().getMaterial();
                        if (material2 == Material.air || material2 == Material.leaves) {
                            this.func_181642_b(worldIn, blockpos3);
                            k3 = j5;
                        }
                    }
                }
                if (k3 > 0) {
                    BlockPos blockpos4 = new BlockPos(i3, k3, j2);
                    for (int i6 = -2; i6 <= 2; ++i6) {
                        for (int k6 = -2; k6 <= 2; ++k6) {
                            if (Math.abs(i6) != 2 || Math.abs(k6) != 2) {
                                this.func_175924_b(worldIn, blockpos4.add(i6, 0, k6));
                            }
                        }
                    }
                    blockpos4 = blockpos4.up();
                    for (int j6 = -1; j6 <= 1; ++j6) {
                        for (int l6 = -1; l6 <= 1; ++l6) {
                            this.func_175924_b(worldIn, blockpos4.add(j6, 0, l6));
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    private void func_181642_b(final World p_181642_1_, final BlockPos p_181642_2_) {
        this.setBlockAndNotifyAdequately(p_181642_1_, p_181642_2_, WorldGenSavannaTree.field_181643_a);
    }
    
    private void func_175924_b(final World worldIn, final BlockPos p_175924_2_) {
        final Material material = worldIn.getBlockState(p_175924_2_).getBlock().getMaterial();
        if (material == Material.air || material == Material.leaves) {
            this.setBlockAndNotifyAdequately(worldIn, p_175924_2_, WorldGenSavannaTree.field_181644_b);
        }
    }
}
