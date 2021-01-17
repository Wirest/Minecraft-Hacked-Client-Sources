// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.properties.IProperty;
import net.minecraft.init.Blocks;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.BlockVine;
import net.minecraft.util.MathHelper;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;

public class WorldGenMegaJungle extends WorldGenHugeTrees
{
    public WorldGenMegaJungle(final boolean p_i46448_1_, final int p_i46448_2_, final int p_i46448_3_, final IBlockState p_i46448_4_, final IBlockState p_i46448_5_) {
        super(p_i46448_1_, p_i46448_2_, p_i46448_3_, p_i46448_4_, p_i46448_5_);
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        final int i = this.func_150533_a(rand);
        if (!this.func_175929_a(worldIn, rand, position, i)) {
            return false;
        }
        this.func_175930_c(worldIn, position.up(i), 2);
        for (int j = position.getY() + i - 2 - rand.nextInt(4); j > position.getY() + i / 2; j -= 2 + rand.nextInt(4)) {
            final float f = rand.nextFloat() * 3.1415927f * 2.0f;
            int k = position.getX() + (int)(0.5f + MathHelper.cos(f) * 4.0f);
            int l = position.getZ() + (int)(0.5f + MathHelper.sin(f) * 4.0f);
            for (int i2 = 0; i2 < 5; ++i2) {
                k = position.getX() + (int)(1.5f + MathHelper.cos(f) * i2);
                l = position.getZ() + (int)(1.5f + MathHelper.sin(f) * i2);
                this.setBlockAndNotifyAdequately(worldIn, new BlockPos(k, j - 3 + i2 / 2, l), this.woodMetadata);
            }
            final int j2 = 1 + rand.nextInt(2);
            for (int j3 = j, k2 = j - j2; k2 <= j3; ++k2) {
                final int l2 = k2 - j3;
                this.func_175928_b(worldIn, new BlockPos(k, k2, l), 1 - l2);
            }
        }
        for (int i3 = 0; i3 < i; ++i3) {
            final BlockPos blockpos = position.up(i3);
            if (this.func_150523_a(worldIn.getBlockState(blockpos).getBlock())) {
                this.setBlockAndNotifyAdequately(worldIn, blockpos, this.woodMetadata);
                if (i3 > 0) {
                    this.func_181632_a(worldIn, rand, blockpos.west(), BlockVine.EAST);
                    this.func_181632_a(worldIn, rand, blockpos.north(), BlockVine.SOUTH);
                }
            }
            if (i3 < i - 1) {
                final BlockPos blockpos2 = blockpos.east();
                if (this.func_150523_a(worldIn.getBlockState(blockpos2).getBlock())) {
                    this.setBlockAndNotifyAdequately(worldIn, blockpos2, this.woodMetadata);
                    if (i3 > 0) {
                        this.func_181632_a(worldIn, rand, blockpos2.east(), BlockVine.WEST);
                        this.func_181632_a(worldIn, rand, blockpos2.north(), BlockVine.SOUTH);
                    }
                }
                final BlockPos blockpos3 = blockpos.south().east();
                if (this.func_150523_a(worldIn.getBlockState(blockpos3).getBlock())) {
                    this.setBlockAndNotifyAdequately(worldIn, blockpos3, this.woodMetadata);
                    if (i3 > 0) {
                        this.func_181632_a(worldIn, rand, blockpos3.east(), BlockVine.WEST);
                        this.func_181632_a(worldIn, rand, blockpos3.south(), BlockVine.NORTH);
                    }
                }
                final BlockPos blockpos4 = blockpos.south();
                if (this.func_150523_a(worldIn.getBlockState(blockpos4).getBlock())) {
                    this.setBlockAndNotifyAdequately(worldIn, blockpos4, this.woodMetadata);
                    if (i3 > 0) {
                        this.func_181632_a(worldIn, rand, blockpos4.west(), BlockVine.EAST);
                        this.func_181632_a(worldIn, rand, blockpos4.south(), BlockVine.NORTH);
                    }
                }
            }
        }
        return true;
    }
    
    private void func_181632_a(final World p_181632_1_, final Random p_181632_2_, final BlockPos p_181632_3_, final PropertyBool p_181632_4_) {
        if (p_181632_2_.nextInt(3) > 0 && p_181632_1_.isAirBlock(p_181632_3_)) {
            this.setBlockAndNotifyAdequately(p_181632_1_, p_181632_3_, Blocks.vine.getDefaultState().withProperty((IProperty<Comparable>)p_181632_4_, true));
        }
    }
    
    private void func_175930_c(final World worldIn, final BlockPos p_175930_2_, final int p_175930_3_) {
        final int i = 2;
        for (int j = -i; j <= 0; ++j) {
            this.func_175925_a(worldIn, p_175930_2_.up(j), p_175930_3_ + 1 - j);
        }
    }
}
