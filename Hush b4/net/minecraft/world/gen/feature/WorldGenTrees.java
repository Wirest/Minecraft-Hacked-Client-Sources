// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.BlockCocoa;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockVine;
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

public class WorldGenTrees extends WorldGenAbstractTree
{
    private static final IBlockState field_181653_a;
    private static final IBlockState field_181654_b;
    private final int minTreeHeight;
    private final boolean vinesGrow;
    private final IBlockState metaWood;
    private final IBlockState metaLeaves;
    
    static {
        field_181653_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
        field_181654_b = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty((IProperty<Comparable>)BlockLeaves.CHECK_DECAY, false);
    }
    
    public WorldGenTrees(final boolean p_i2027_1_) {
        this(p_i2027_1_, 4, WorldGenTrees.field_181653_a, WorldGenTrees.field_181654_b, false);
    }
    
    public WorldGenTrees(final boolean p_i46446_1_, final int p_i46446_2_, final IBlockState p_i46446_3_, final IBlockState p_i46446_4_, final boolean p_i46446_5_) {
        super(p_i46446_1_);
        this.minTreeHeight = p_i46446_2_;
        this.metaWood = p_i46446_3_;
        this.metaLeaves = p_i46446_4_;
        this.vinesGrow = p_i46446_5_;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        final int i = rand.nextInt(3) + this.minTreeHeight;
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
        final Block block1 = worldIn.getBlockState(position.down()).getBlock();
        if ((block1 == Blocks.grass || block1 == Blocks.dirt || block1 == Blocks.farmland) && position.getY() < 256 - i - 1) {
            this.func_175921_a(worldIn, position.down());
            final int k2 = 3;
            final int l2 = 0;
            for (int i3 = position.getY() - k2 + i; i3 <= position.getY() + i; ++i3) {
                final int i4 = i3 - (position.getY() + i);
                for (int j2 = l2 + 1 - i4 / 2, k3 = position.getX() - j2; k3 <= position.getX() + j2; ++k3) {
                    final int l3 = k3 - position.getX();
                    for (int i5 = position.getZ() - j2; i5 <= position.getZ() + j2; ++i5) {
                        final int j3 = i5 - position.getZ();
                        if (Math.abs(l3) != j2 || Math.abs(j3) != j2 || (rand.nextInt(2) != 0 && i4 != 0)) {
                            final BlockPos blockpos = new BlockPos(k3, i3, i5);
                            final Block block2 = worldIn.getBlockState(blockpos).getBlock();
                            if (block2.getMaterial() == Material.air || block2.getMaterial() == Material.leaves || block2.getMaterial() == Material.vine) {
                                this.setBlockAndNotifyAdequately(worldIn, blockpos, this.metaLeaves);
                            }
                        }
                    }
                }
            }
            for (int j4 = 0; j4 < i; ++j4) {
                final Block block3 = worldIn.getBlockState(position.up(j4)).getBlock();
                if (block3.getMaterial() == Material.air || block3.getMaterial() == Material.leaves || block3.getMaterial() == Material.vine) {
                    this.setBlockAndNotifyAdequately(worldIn, position.up(j4), this.metaWood);
                    if (this.vinesGrow && j4 > 0) {
                        if (rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(-1, j4, 0))) {
                            this.func_181651_a(worldIn, position.add(-1, j4, 0), BlockVine.EAST);
                        }
                        if (rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(1, j4, 0))) {
                            this.func_181651_a(worldIn, position.add(1, j4, 0), BlockVine.WEST);
                        }
                        if (rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(0, j4, -1))) {
                            this.func_181651_a(worldIn, position.add(0, j4, -1), BlockVine.SOUTH);
                        }
                        if (rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(0, j4, 1))) {
                            this.func_181651_a(worldIn, position.add(0, j4, 1), BlockVine.NORTH);
                        }
                    }
                }
            }
            if (this.vinesGrow) {
                for (int k4 = position.getY() - 3 + i; k4 <= position.getY() + i; ++k4) {
                    final int j5 = k4 - (position.getY() + i);
                    final int k5 = 2 - j5 / 2;
                    final BlockPos.MutableBlockPos blockpos$mutableblockpos2 = new BlockPos.MutableBlockPos();
                    for (int l4 = position.getX() - k5; l4 <= position.getX() + k5; ++l4) {
                        for (int i6 = position.getZ() - k5; i6 <= position.getZ() + k5; ++i6) {
                            blockpos$mutableblockpos2.func_181079_c(l4, k4, i6);
                            if (worldIn.getBlockState(blockpos$mutableblockpos2).getBlock().getMaterial() == Material.leaves) {
                                final BlockPos blockpos2 = blockpos$mutableblockpos2.west();
                                final BlockPos blockpos3 = blockpos$mutableblockpos2.east();
                                final BlockPos blockpos4 = blockpos$mutableblockpos2.north();
                                final BlockPos blockpos5 = blockpos$mutableblockpos2.south();
                                if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos2).getBlock().getMaterial() == Material.air) {
                                    this.func_181650_b(worldIn, blockpos2, BlockVine.EAST);
                                }
                                if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos3).getBlock().getMaterial() == Material.air) {
                                    this.func_181650_b(worldIn, blockpos3, BlockVine.WEST);
                                }
                                if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos4).getBlock().getMaterial() == Material.air) {
                                    this.func_181650_b(worldIn, blockpos4, BlockVine.SOUTH);
                                }
                                if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos5).getBlock().getMaterial() == Material.air) {
                                    this.func_181650_b(worldIn, blockpos5, BlockVine.NORTH);
                                }
                            }
                        }
                    }
                }
                if (rand.nextInt(5) == 0 && i > 5) {
                    for (int l5 = 0; l5 < 2; ++l5) {
                        for (final Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
                            if (rand.nextInt(4 - l5) == 0) {
                                final EnumFacing enumfacing2 = ((EnumFacing)enumfacing).getOpposite();
                                this.func_181652_a(worldIn, rand.nextInt(3), position.add(enumfacing2.getFrontOffsetX(), i - 5 + l5, enumfacing2.getFrontOffsetZ()), (EnumFacing)enumfacing);
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    private void func_181652_a(final World p_181652_1_, final int p_181652_2_, final BlockPos p_181652_3_, final EnumFacing p_181652_4_) {
        this.setBlockAndNotifyAdequately(p_181652_1_, p_181652_3_, Blocks.cocoa.getDefaultState().withProperty((IProperty<Comparable>)BlockCocoa.AGE, p_181652_2_).withProperty((IProperty<Comparable>)BlockCocoa.FACING, p_181652_4_));
    }
    
    private void func_181651_a(final World p_181651_1_, final BlockPos p_181651_2_, final PropertyBool p_181651_3_) {
        this.setBlockAndNotifyAdequately(p_181651_1_, p_181651_2_, Blocks.vine.getDefaultState().withProperty((IProperty<Comparable>)p_181651_3_, true));
    }
    
    private void func_181650_b(final World p_181650_1_, BlockPos p_181650_2_, final PropertyBool p_181650_3_) {
        this.func_181651_a(p_181650_1_, p_181650_2_, p_181650_3_);
        int i;
        for (i = 4, p_181650_2_ = p_181650_2_.down(); p_181650_1_.getBlockState(p_181650_2_).getBlock().getMaterial() == Material.air && i > 0; p_181650_2_ = p_181650_2_.down(), --i) {
            this.func_181651_a(p_181650_1_, p_181650_2_, p_181650_3_);
        }
    }
}
