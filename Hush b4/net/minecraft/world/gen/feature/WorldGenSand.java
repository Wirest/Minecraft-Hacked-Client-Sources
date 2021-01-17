// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.Block;

public class WorldGenSand extends WorldGenerator
{
    private Block block;
    private int radius;
    
    public WorldGenSand(final Block p_i45462_1_, final int p_i45462_2_) {
        this.block = p_i45462_1_;
        this.radius = p_i45462_2_;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        if (worldIn.getBlockState(position).getBlock().getMaterial() != Material.water) {
            return false;
        }
        final int i = rand.nextInt(this.radius - 2) + 2;
        final int j = 2;
        for (int k = position.getX() - i; k <= position.getX() + i; ++k) {
            for (int l = position.getZ() - i; l <= position.getZ() + i; ++l) {
                final int i2 = k - position.getX();
                final int j2 = l - position.getZ();
                if (i2 * i2 + j2 * j2 <= i * i) {
                    for (int k2 = position.getY() - j; k2 <= position.getY() + j; ++k2) {
                        final BlockPos blockpos = new BlockPos(k, k2, l);
                        final Block block = worldIn.getBlockState(blockpos).getBlock();
                        if (block == Blocks.dirt || block == Blocks.grass) {
                            worldIn.setBlockState(blockpos, this.block.getDefaultState(), 2);
                        }
                    }
                }
            }
        }
        return true;
    }
}
