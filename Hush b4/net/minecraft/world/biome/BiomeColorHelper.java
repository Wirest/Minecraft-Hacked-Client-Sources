// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import java.util.Iterator;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.BlockPos;

public class BiomeColorHelper
{
    private static final ColorResolver field_180291_a;
    private static final ColorResolver field_180289_b;
    private static final ColorResolver field_180290_c;
    
    static {
        field_180291_a = new ColorResolver() {
            @Override
            public int getColorAtPos(final BiomeGenBase p_180283_1_, final BlockPos blockPosition) {
                return p_180283_1_.getGrassColorAtPos(blockPosition);
            }
        };
        field_180289_b = new ColorResolver() {
            @Override
            public int getColorAtPos(final BiomeGenBase p_180283_1_, final BlockPos blockPosition) {
                return p_180283_1_.getFoliageColorAtPos(blockPosition);
            }
        };
        field_180290_c = new ColorResolver() {
            @Override
            public int getColorAtPos(final BiomeGenBase p_180283_1_, final BlockPos blockPosition) {
                return p_180283_1_.waterColorMultiplier;
            }
        };
    }
    
    private static int func_180285_a(final IBlockAccess p_180285_0_, final BlockPos p_180285_1_, final ColorResolver p_180285_2_) {
        int i = 0;
        int j = 0;
        int k = 0;
        for (final BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(p_180285_1_.add(-1, 0, -1), p_180285_1_.add(1, 0, 1))) {
            final int l = p_180285_2_.getColorAtPos(p_180285_0_.getBiomeGenForCoords(blockpos$mutableblockpos), blockpos$mutableblockpos);
            i += (l & 0xFF0000) >> 16;
            j += (l & 0xFF00) >> 8;
            k += (l & 0xFF);
        }
        return (i / 9 & 0xFF) << 16 | (j / 9 & 0xFF) << 8 | (k / 9 & 0xFF);
    }
    
    public static int getGrassColorAtPos(final IBlockAccess p_180286_0_, final BlockPos p_180286_1_) {
        return func_180285_a(p_180286_0_, p_180286_1_, BiomeColorHelper.field_180291_a);
    }
    
    public static int getFoliageColorAtPos(final IBlockAccess p_180287_0_, final BlockPos p_180287_1_) {
        return func_180285_a(p_180287_0_, p_180287_1_, BiomeColorHelper.field_180289_b);
    }
    
    public static int getWaterColorAtPos(final IBlockAccess p_180288_0_, final BlockPos p_180288_1_) {
        return func_180285_a(p_180288_0_, p_180288_1_, BiomeColorHelper.field_180290_c);
    }
    
    interface ColorResolver
    {
        int getColorAtPos(final BiomeGenBase p0, final BlockPos p1);
    }
}
