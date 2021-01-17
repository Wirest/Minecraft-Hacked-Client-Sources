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

public class WorldGenCanopyTree extends WorldGenAbstractTree
{
    private static final IBlockState field_181640_a;
    private static final IBlockState field_181641_b;
    
    static {
        field_181640_a = Blocks.log2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK);
        field_181641_b = Blocks.leaves2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.DARK_OAK).withProperty((IProperty<Comparable>)BlockLeaves.CHECK_DECAY, false);
    }
    
    public WorldGenCanopyTree(final boolean p_i45461_1_) {
        super(p_i45461_1_);
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        final int i = rand.nextInt(3) + rand.nextInt(2) + 6;
        final int j = position.getX();
        final int k = position.getY();
        final int l = position.getZ();
        if (k < 1 || k + i + 1 >= 256) {
            return false;
        }
        final BlockPos blockpos = position.down();
        final Block block = worldIn.getBlockState(blockpos).getBlock();
        if (block != Blocks.grass && block != Blocks.dirt) {
            return false;
        }
        if (!this.func_181638_a(worldIn, position, i)) {
            return false;
        }
        this.func_175921_a(worldIn, blockpos);
        this.func_175921_a(worldIn, blockpos.east());
        this.func_175921_a(worldIn, blockpos.south());
        this.func_175921_a(worldIn, blockpos.south().east());
        final EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(rand);
        final int i2 = i - rand.nextInt(4);
        int j2 = 2 - rand.nextInt(3);
        int k2 = j;
        int l2 = l;
        final int i3 = k + i - 1;
        for (int j3 = 0; j3 < i; ++j3) {
            if (j3 >= i2 && j2 > 0) {
                k2 += enumfacing.getFrontOffsetX();
                l2 += enumfacing.getFrontOffsetZ();
                --j2;
            }
            final int k3 = k + j3;
            final BlockPos blockpos2 = new BlockPos(k2, k3, l2);
            final Material material = worldIn.getBlockState(blockpos2).getBlock().getMaterial();
            if (material == Material.air || material == Material.leaves) {
                this.func_181639_b(worldIn, blockpos2);
                this.func_181639_b(worldIn, blockpos2.east());
                this.func_181639_b(worldIn, blockpos2.south());
                this.func_181639_b(worldIn, blockpos2.east().south());
            }
        }
        for (int i4 = -2; i4 <= 0; ++i4) {
            for (int l3 = -2; l3 <= 0; ++l3) {
                int k4 = -1;
                this.func_150526_a(worldIn, k2 + i4, i3 + k4, l2 + l3);
                this.func_150526_a(worldIn, 1 + k2 - i4, i3 + k4, l2 + l3);
                this.func_150526_a(worldIn, k2 + i4, i3 + k4, 1 + l2 - l3);
                this.func_150526_a(worldIn, 1 + k2 - i4, i3 + k4, 1 + l2 - l3);
                if ((i4 > -2 || l3 > -1) && (i4 != -1 || l3 != -2)) {
                    k4 = 1;
                    this.func_150526_a(worldIn, k2 + i4, i3 + k4, l2 + l3);
                    this.func_150526_a(worldIn, 1 + k2 - i4, i3 + k4, l2 + l3);
                    this.func_150526_a(worldIn, k2 + i4, i3 + k4, 1 + l2 - l3);
                    this.func_150526_a(worldIn, 1 + k2 - i4, i3 + k4, 1 + l2 - l3);
                }
            }
        }
        if (rand.nextBoolean()) {
            this.func_150526_a(worldIn, k2, i3 + 2, l2);
            this.func_150526_a(worldIn, k2 + 1, i3 + 2, l2);
            this.func_150526_a(worldIn, k2 + 1, i3 + 2, l2 + 1);
            this.func_150526_a(worldIn, k2, i3 + 2, l2 + 1);
        }
        for (int j4 = -3; j4 <= 4; ++j4) {
            for (int i5 = -3; i5 <= 4; ++i5) {
                if ((j4 != -3 || i5 != -3) && (j4 != -3 || i5 != 4) && (j4 != 4 || i5 != -3) && (j4 != 4 || i5 != 4) && (Math.abs(j4) < 3 || Math.abs(i5) < 3)) {
                    this.func_150526_a(worldIn, k2 + j4, i3, l2 + i5);
                }
            }
        }
        for (int k5 = -1; k5 <= 2; ++k5) {
            for (int j5 = -1; j5 <= 2; ++j5) {
                if ((k5 < 0 || k5 > 1 || j5 < 0 || j5 > 1) && rand.nextInt(3) <= 0) {
                    for (int l4 = rand.nextInt(3) + 2, i6 = 0; i6 < l4; ++i6) {
                        this.func_181639_b(worldIn, new BlockPos(j + k5, i3 - i6 - 1, l + j5));
                    }
                    for (int j6 = -1; j6 <= 1; ++j6) {
                        for (int l5 = -1; l5 <= 1; ++l5) {
                            this.func_150526_a(worldIn, k2 + k5 + j6, i3, l2 + j5 + l5);
                        }
                    }
                    for (int k6 = -2; k6 <= 2; ++k6) {
                        for (int l6 = -2; l6 <= 2; ++l6) {
                            if (Math.abs(k6) != 2 || Math.abs(l6) != 2) {
                                this.func_150526_a(worldIn, k2 + k5 + k6, i3 - 1, l2 + j5 + l6);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    
    private boolean func_181638_a(final World p_181638_1_, final BlockPos p_181638_2_, final int p_181638_3_) {
        final int i = p_181638_2_.getX();
        final int j = p_181638_2_.getY();
        final int k = p_181638_2_.getZ();
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int l = 0; l <= p_181638_3_ + 1; ++l) {
            int i2 = 1;
            if (l == 0) {
                i2 = 0;
            }
            if (l >= p_181638_3_ - 1) {
                i2 = 2;
            }
            for (int j2 = -i2; j2 <= i2; ++j2) {
                for (int k2 = -i2; k2 <= i2; ++k2) {
                    if (!this.func_150523_a(p_181638_1_.getBlockState(blockpos$mutableblockpos.func_181079_c(i + j2, j + l, k + k2)).getBlock())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    private void func_181639_b(final World p_181639_1_, final BlockPos p_181639_2_) {
        if (this.func_150523_a(p_181639_1_.getBlockState(p_181639_2_).getBlock())) {
            this.setBlockAndNotifyAdequately(p_181639_1_, p_181639_2_, WorldGenCanopyTree.field_181640_a);
        }
    }
    
    private void func_150526_a(final World worldIn, final int p_150526_2_, final int p_150526_3_, final int p_150526_4_) {
        final BlockPos blockpos = new BlockPos(p_150526_2_, p_150526_3_, p_150526_4_);
        final Block block = worldIn.getBlockState(blockpos).getBlock();
        if (block.getMaterial() == Material.air) {
            this.setBlockAndNotifyAdequately(worldIn, blockpos, WorldGenCanopyTree.field_181641_b);
        }
    }
}
