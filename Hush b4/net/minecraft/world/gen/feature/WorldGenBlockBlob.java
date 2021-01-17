// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import java.util.Iterator;
import net.minecraft.util.Vec3i;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.Block;

public class WorldGenBlockBlob extends WorldGenerator
{
    private final Block field_150545_a;
    private final int field_150544_b;
    
    public WorldGenBlockBlob(final Block p_i45450_1_, final int p_i45450_2_) {
        super(false);
        this.field_150545_a = p_i45450_1_;
        this.field_150544_b = p_i45450_2_;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, BlockPos position) {
        while (position.getY() > 3) {
            if (!worldIn.isAirBlock(position.down())) {
                final Block block = worldIn.getBlockState(position.down()).getBlock();
                if (block == Blocks.grass || block == Blocks.dirt || block == Blocks.stone) {
                    break;
                }
            }
            position = position.down();
        }
        if (position.getY() <= 3) {
            return false;
        }
        for (int i1 = this.field_150544_b, j = 0; i1 >= 0 && j < 3; ++j) {
            final int k = i1 + rand.nextInt(2);
            final int l = i1 + rand.nextInt(2);
            final int m = i1 + rand.nextInt(2);
            final float f = (k + l + m) * 0.333f + 0.5f;
            for (final BlockPos blockpos : BlockPos.getAllInBox(position.add(-k, -l, -m), position.add(k, l, m))) {
                if (blockpos.distanceSq(position) <= f * f) {
                    worldIn.setBlockState(blockpos, this.field_150545_a.getDefaultState(), 4);
                }
            }
            position = position.add(-(i1 + 1) + rand.nextInt(2 + i1 * 2), 0 - rand.nextInt(2), -(i1 + 1) + rand.nextInt(2 + i1 * 2));
        }
        return true;
    }
}
