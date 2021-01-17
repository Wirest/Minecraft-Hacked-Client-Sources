// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.util.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockOldLog;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;

public class WorldGenMegaPineTree extends WorldGenHugeTrees
{
    private static final IBlockState field_181633_e;
    private static final IBlockState field_181634_f;
    private static final IBlockState field_181635_g;
    private boolean useBaseHeight;
    
    static {
        field_181633_e = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
        field_181634_f = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty((IProperty<Comparable>)BlockLeaves.CHECK_DECAY, false);
        field_181635_g = Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL);
    }
    
    public WorldGenMegaPineTree(final boolean p_i45457_1_, final boolean p_i45457_2_) {
        super(p_i45457_1_, 13, 15, WorldGenMegaPineTree.field_181633_e, WorldGenMegaPineTree.field_181634_f);
        this.useBaseHeight = p_i45457_2_;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        final int i = this.func_150533_a(rand);
        if (!this.func_175929_a(worldIn, rand, position, i)) {
            return false;
        }
        this.func_150541_c(worldIn, position.getX(), position.getZ(), position.getY() + i, 0, rand);
        for (int j = 0; j < i; ++j) {
            Block block = worldIn.getBlockState(position.up(j)).getBlock();
            if (block.getMaterial() == Material.air || block.getMaterial() == Material.leaves) {
                this.setBlockAndNotifyAdequately(worldIn, position.up(j), this.woodMetadata);
            }
            if (j < i - 1) {
                block = worldIn.getBlockState(position.add(1, j, 0)).getBlock();
                if (block.getMaterial() == Material.air || block.getMaterial() == Material.leaves) {
                    this.setBlockAndNotifyAdequately(worldIn, position.add(1, j, 0), this.woodMetadata);
                }
                block = worldIn.getBlockState(position.add(1, j, 1)).getBlock();
                if (block.getMaterial() == Material.air || block.getMaterial() == Material.leaves) {
                    this.setBlockAndNotifyAdequately(worldIn, position.add(1, j, 1), this.woodMetadata);
                }
                block = worldIn.getBlockState(position.add(0, j, 1)).getBlock();
                if (block.getMaterial() == Material.air || block.getMaterial() == Material.leaves) {
                    this.setBlockAndNotifyAdequately(worldIn, position.add(0, j, 1), this.woodMetadata);
                }
            }
        }
        return true;
    }
    
    private void func_150541_c(final World worldIn, final int p_150541_2_, final int p_150541_3_, final int p_150541_4_, final int p_150541_5_, final Random p_150541_6_) {
        final int i = p_150541_6_.nextInt(5) + (this.useBaseHeight ? this.baseHeight : 3);
        int j = 0;
        for (int k = p_150541_4_ - i; k <= p_150541_4_; ++k) {
            final int l = p_150541_4_ - k;
            final int i2 = p_150541_5_ + MathHelper.floor_float(l / (float)i * 3.5f);
            this.func_175925_a(worldIn, new BlockPos(p_150541_2_, k, p_150541_3_), i2 + ((l > 0 && i2 == j && (k & 0x1) == 0x0) ? 1 : 0));
            j = i2;
        }
    }
    
    @Override
    public void func_180711_a(final World worldIn, final Random p_180711_2_, final BlockPos p_180711_3_) {
        this.func_175933_b(worldIn, p_180711_3_.west().north());
        this.func_175933_b(worldIn, p_180711_3_.east(2).north());
        this.func_175933_b(worldIn, p_180711_3_.west().south(2));
        this.func_175933_b(worldIn, p_180711_3_.east(2).south(2));
        for (int i = 0; i < 5; ++i) {
            final int j = p_180711_2_.nextInt(64);
            final int k = j % 8;
            final int l = j / 8;
            if (k == 0 || k == 7 || l == 0 || l == 7) {
                this.func_175933_b(worldIn, p_180711_3_.add(-3 + k, 0, -3 + l));
            }
        }
    }
    
    private void func_175933_b(final World worldIn, final BlockPos p_175933_2_) {
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                if (Math.abs(i) != 2 || Math.abs(j) != 2) {
                    this.func_175934_c(worldIn, p_175933_2_.add(i, 0, j));
                }
            }
        }
    }
    
    private void func_175934_c(final World worldIn, final BlockPos p_175934_2_) {
        for (int i = 2; i >= -3; --i) {
            final BlockPos blockpos = p_175934_2_.up(i);
            final Block block = worldIn.getBlockState(blockpos).getBlock();
            if (block == Blocks.grass || block == Blocks.dirt) {
                this.setBlockAndNotifyAdequately(worldIn, blockpos, WorldGenMegaPineTree.field_181635_g);
                break;
            }
            if (block.getMaterial() != Material.air && i < 0) {
                break;
            }
        }
    }
}
