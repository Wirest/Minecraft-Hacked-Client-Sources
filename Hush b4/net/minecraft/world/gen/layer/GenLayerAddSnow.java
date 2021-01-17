// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.layer;

public class GenLayerAddSnow extends GenLayer
{
    public GenLayerAddSnow(final long p_i2121_1_, final GenLayer p_i2121_3_) {
        super(p_i2121_1_);
        this.parent = p_i2121_3_;
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
                final int k2 = aint[j2 + 1 + (i2 + 1) * k];
                this.initChunkSeed(j2 + areaX, i2 + areaY);
                if (k2 == 0) {
                    aint2[j2 + i2 * areaWidth] = 0;
                }
                else {
                    int l2 = this.nextInt(6);
                    if (l2 == 0) {
                        l2 = 4;
                    }
                    else if (l2 <= 1) {
                        l2 = 3;
                    }
                    else {
                        l2 = 1;
                    }
                    aint2[j2 + i2 * areaWidth] = l2;
                }
            }
        }
        return aint2;
    }
}
