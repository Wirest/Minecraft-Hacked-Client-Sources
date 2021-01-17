// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.layer;

public class GenLayerAddIsland extends GenLayer
{
    public GenLayerAddIsland(final long p_i2119_1_, final GenLayer p_i2119_3_) {
        super(p_i2119_1_);
        this.parent = p_i2119_3_;
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
                final int k2 = aint[j2 + 0 + (i2 + 0) * k];
                final int l2 = aint[j2 + 2 + (i2 + 0) * k];
                final int i3 = aint[j2 + 0 + (i2 + 2) * k];
                final int j3 = aint[j2 + 2 + (i2 + 2) * k];
                final int k3 = aint[j2 + 1 + (i2 + 1) * k];
                this.initChunkSeed(j2 + areaX, i2 + areaY);
                if (k3 != 0 || (k2 == 0 && l2 == 0 && i3 == 0 && j3 == 0)) {
                    if (k3 > 0 && (k2 == 0 || l2 == 0 || i3 == 0 || j3 == 0)) {
                        if (this.nextInt(5) == 0) {
                            if (k3 == 4) {
                                aint2[j2 + i2 * areaWidth] = 4;
                            }
                            else {
                                aint2[j2 + i2 * areaWidth] = 0;
                            }
                        }
                        else {
                            aint2[j2 + i2 * areaWidth] = k3;
                        }
                    }
                    else {
                        aint2[j2 + i2 * areaWidth] = k3;
                    }
                }
                else {
                    int l3 = 1;
                    int i4 = 1;
                    if (k2 != 0 && this.nextInt(l3++) == 0) {
                        i4 = k2;
                    }
                    if (l2 != 0 && this.nextInt(l3++) == 0) {
                        i4 = l2;
                    }
                    if (i3 != 0 && this.nextInt(l3++) == 0) {
                        i4 = i3;
                    }
                    if (j3 != 0 && this.nextInt(l3++) == 0) {
                        i4 = j3;
                    }
                    if (this.nextInt(3) == 0) {
                        aint2[j2 + i2 * areaWidth] = i4;
                    }
                    else if (i4 == 4) {
                        aint2[j2 + i2 * areaWidth] = 4;
                    }
                    else {
                        aint2[j2 + i2 * areaWidth] = 0;
                    }
                }
            }
        }
        return aint2;
    }
}
