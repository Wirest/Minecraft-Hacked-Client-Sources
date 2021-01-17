// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.layer;

public class GenLayerSmooth extends GenLayer
{
    public GenLayerSmooth(final long p_i2131_1_, final GenLayer p_i2131_3_) {
        super(p_i2131_1_);
        super.parent = p_i2131_3_;
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
                final int k2 = aint[j2 + 0 + (i2 + 1) * k];
                final int l2 = aint[j2 + 2 + (i2 + 1) * k];
                final int i3 = aint[j2 + 1 + (i2 + 0) * k];
                final int j3 = aint[j2 + 1 + (i2 + 2) * k];
                int k3 = aint[j2 + 1 + (i2 + 1) * k];
                if (k2 == l2 && i3 == j3) {
                    this.initChunkSeed(j2 + areaX, i2 + areaY);
                    if (this.nextInt(2) == 0) {
                        k3 = k2;
                    }
                    else {
                        k3 = i3;
                    }
                }
                else {
                    if (k2 == l2) {
                        k3 = k2;
                    }
                    if (i3 == j3) {
                        k3 = i3;
                    }
                }
                aint2[j2 + i2 * areaWidth] = k3;
            }
        }
        return aint2;
    }
}
