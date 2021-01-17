// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.Block;

public class WorldGenHellLava extends WorldGenerator
{
    private final Block field_150553_a;
    private final boolean field_94524_b;
    
    public WorldGenHellLava(final Block p_i45453_1_, final boolean p_i45453_2_) {
        this.field_150553_a = p_i45453_1_;
        this.field_94524_b = p_i45453_2_;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        if (worldIn.getBlockState(position.up()).getBlock() != Blocks.netherrack) {
            return false;
        }
        if (worldIn.getBlockState(position).getBlock().getMaterial() != Material.air && worldIn.getBlockState(position).getBlock() != Blocks.netherrack) {
            return false;
        }
        int i = 0;
        if (worldIn.getBlockState(position.west()).getBlock() == Blocks.netherrack) {
            ++i;
        }
        if (worldIn.getBlockState(position.east()).getBlock() == Blocks.netherrack) {
            ++i;
        }
        if (worldIn.getBlockState(position.north()).getBlock() == Blocks.netherrack) {
            ++i;
        }
        if (worldIn.getBlockState(position.south()).getBlock() == Blocks.netherrack) {
            ++i;
        }
        if (worldIn.getBlockState(position.down()).getBlock() == Blocks.netherrack) {
            ++i;
        }
        int j = 0;
        if (worldIn.isAirBlock(position.west())) {
            ++j;
        }
        if (worldIn.isAirBlock(position.east())) {
            ++j;
        }
        if (worldIn.isAirBlock(position.north())) {
            ++j;
        }
        if (worldIn.isAirBlock(position.south())) {
            ++j;
        }
        if (worldIn.isAirBlock(position.down())) {
            ++j;
        }
        if ((!this.field_94524_b && i == 4 && j == 1) || i == 5) {
            worldIn.setBlockState(position, this.field_150553_a.getDefaultState(), 2);
            worldIn.forceBlockUpdateTick(this.field_150553_a, position, rand);
        }
        return true;
    }
}
