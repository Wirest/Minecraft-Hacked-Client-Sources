// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;

public class GenLayerDeepOcean extends GenLayer
{
    public GenLayerDeepOcean(final long p_i45472_1_, final GenLayer p_i45472_3_) {
        super(p_i45472_1_);
        this.parent = p_i45472_3_;
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
                final int k2 = aint[j2 + 1 + (i2 + 1 - 1) * (areaWidth + 2)];
                final int l2 = aint[j2 + 1 + 1 + (i2 + 1) * (areaWidth + 2)];
                final int i3 = aint[j2 + 1 - 1 + (i2 + 1) * (areaWidth + 2)];
                final int j3 = aint[j2 + 1 + (i2 + 1 + 1) * (areaWidth + 2)];
                final int k3 = aint[j2 + 1 + (i2 + 1) * k];
                int l3 = 0;
                if (k2 == 0) {
                    ++l3;
                }
                if (l2 == 0) {
                    ++l3;
                }
                if (i3 == 0) {
                    ++l3;
                }
                if (j3 == 0) {
                    ++l3;
                }
                if (k3 == 0 && l3 > 3) {
                    aint2[j2 + i2 * areaWidth] = BiomeGenBase.deepOcean.biomeID;
                }
                else {
                    aint2[j2 + i2 * areaWidth] = k3;
                }
            }
        }
        return aint2;
    }
}
