// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.Block;

public class WorldGenSpikes extends WorldGenerator
{
    private Block baseBlockRequired;
    
    public WorldGenSpikes(final Block p_i45464_1_) {
        this.baseBlockRequired = p_i45464_1_;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        if (worldIn.isAirBlock(position) && worldIn.getBlockState(position.down()).getBlock() == this.baseBlockRequired) {
            final int i = rand.nextInt(32) + 6;
            final int j = rand.nextInt(4) + 1;
            final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            for (int k = position.getX() - j; k <= position.getX() + j; ++k) {
                for (int l = position.getZ() - j; l <= position.getZ() + j; ++l) {
                    final int i2 = k - position.getX();
                    final int j2 = l - position.getZ();
                    if (i2 * i2 + j2 * j2 <= j * j + 1 && worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(k, position.getY() - 1, l)).getBlock() != this.baseBlockRequired) {
                        return false;
                    }
                }
            }
            for (int l2 = position.getY(); l2 < position.getY() + i && l2 < 256; ++l2) {
                for (int i3 = position.getX() - j; i3 <= position.getX() + j; ++i3) {
                    for (int j3 = position.getZ() - j; j3 <= position.getZ() + j; ++j3) {
                        final int k2 = i3 - position.getX();
                        final int k3 = j3 - position.getZ();
                        if (k2 * k2 + k3 * k3 <= j * j + 1) {
                            worldIn.setBlockState(new BlockPos(i3, l2, j3), Blocks.obsidian.getDefaultState(), 2);
                        }
                    }
                }
            }
            final Entity entity = new EntityEnderCrystal(worldIn);
            entity.setLocationAndAngles(position.getX() + 0.5f, position.getY() + i, position.getZ() + 0.5f, rand.nextFloat() * 360.0f, 0.0f);
            worldIn.spawnEntityInWorld(entity);
            worldIn.setBlockState(position.up(i), Blocks.bedrock.getDefaultState(), 2);
            return true;
        }
        return false;
    }
}
