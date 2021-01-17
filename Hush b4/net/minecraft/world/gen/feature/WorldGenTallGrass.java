// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;

public class WorldGenTallGrass extends WorldGenerator
{
    private final IBlockState tallGrassState;
    
    public WorldGenTallGrass(final BlockTallGrass.EnumType p_i45629_1_) {
        this.tallGrassState = Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, p_i45629_1_);
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, BlockPos position) {
        Block block;
        while (((block = worldIn.getBlockState(position).getBlock()).getMaterial() == Material.air || block.getMaterial() == Material.leaves) && position.getY() > 0) {
            position = position.down();
        }
        for (int i = 0; i < 128; ++i) {
            final BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
            if (worldIn.isAirBlock(blockpos) && Blocks.tallgrass.canBlockStay(worldIn, blockpos, this.tallGrassState)) {
                worldIn.setBlockState(blockpos, this.tallGrassState, 2);
            }
        }
        return true;
    }
}
