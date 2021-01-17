// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;

public class GenLayerRiver extends GenLayer
{
    public GenLayerRiver(final long p_i2128_1_, final GenLayer p_i2128_3_) {
        super(p_i2128_1_);
        super.parent = p_i2128_3_;
    }
    
    @Override
    public int[] getInts(final int areaX, final int areaY, final int areaWidth, final int areaHeight) {
        final int i = areaX - 1;
        final int j = areaY - 1;
        final int k = areaWidth + 2;
        final int l = areaHeight + 2;
        final int[] aint = this.parent.getInts(i, j, k, l);
        final int[] aint2 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int i2 = 0; i2 < areaHeight; ++i2) {
            for (int j2 = 0; j2 < areaWidth; ++j2) {
                final int k2 = this.func_151630_c(aint[j2 + 0 + (i2 + 1) * k]);
                final int l2 = this.func_151630_c(aint[j2 + 2 + (i2 + 1) * k]);
                final int i3 = this.func_151630_c(aint[j2 + 1 + (i2 + 0) * k]);
                final int j3 = this.func_151630_c(aint[j2 + 1 + (i2 + 2) * k]);
                final int k3 = this.func_151630_c(aint[j2 + 1 + (i2 + 1) * k]);
                if (k3 == k2 && k3 == i3 && k3 == l2 && k3 == j3) {
                    aint2[j2 + i2 * areaWidth] = -1;
                }
                else {
                    aint2[j2 + i2 * areaWidth] = BiomeGenBase.river.biomeID;
                }
            }
        }
        return aint2;
    }
    
    private int func_151630_c(final int p_151630_1_) {
        return (p_151630_1_ >= 2) ? (2 + (p_151630_1_ & 0x1)) : p_151630_1_;
    }
}
