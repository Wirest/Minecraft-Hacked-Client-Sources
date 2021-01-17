// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;

public class WorldGenWaterlily extends WorldGenerator
{
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        for (int i = 0; i < 10; ++i) {
            final int j = position.getX() + rand.nextInt(8) - rand.nextInt(8);
            final int k = position.getY() + rand.nextInt(4) - rand.nextInt(4);
            final int l = position.getZ() + rand.nextInt(8) - rand.nextInt(8);
            if (worldIn.isAirBlock(new BlockPos(j, k, l)) && Blocks.waterlily.canPlaceBlockAt(worldIn, new BlockPos(j, k, l))) {
                worldIn.setBlockState(new BlockPos(j, k, l), Blocks.waterlily.getDefaultState(), 2);
            }
        }
        return true;
    }
}
