// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.layer;

public class GenLayerVoronoiZoom extends GenLayer
{
    public GenLayerVoronoiZoom(final long p_i2133_1_, final GenLayer p_i2133_3_) {
        super(p_i2133_1_);
        super.parent = p_i2133_3_;
    }
    
    @Override
    public int[] getInts(int areaX, int areaY, final int areaWidth, final int areaHeight) {
        areaX -= 2;
        areaY -= 2;
        final int i = areaX >> 2;
        final int j = areaY >> 2;
        final int k = (areaWidth >> 2) + 2;
        final int l = (areaHeight >> 2) + 2;
        final int[] aint = this.parent.getInts(i, j, k, l);
        final int i2 = k - 1 << 2;
        final int j2 = l - 1 << 2;
        final int[] aint2 = IntCache.getIntCache(i2 * j2);
        for (int k2 = 0; k2 < l - 1; ++k2) {
            int l2 = 0;
            int i3 = aint[l2 + 0 + (k2 + 0) * k];
            int j3 = aint[l2 + 0 + (k2 + 1) * k];
            while (l2 < k - 1) {
                final double d0 = 3.6;
                this.initChunkSeed(l2 + i << 2, k2 + j << 2);
                final double d2 = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6;
                final double d3 = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6;
                this.initChunkSeed(l2 + i + 1 << 2, k2 + j << 2);
                final double d4 = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6 + 4.0;
                final double d5 = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6;
                this.initChunkSeed(l2 + i << 2, k2 + j + 1 << 2);
                final double d6 = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6;
                final double d7 = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6 + 4.0;
                this.initChunkSeed(l2 + i + 1 << 2, k2 + j + 1 << 2);
                final double d8 = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6 + 4.0;
                final double d9 = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6 + 4.0;
                final int k3 = aint[l2 + 1 + (k2 + 0) * k] & 0xFF;
                final int l3 = aint[l2 + 1 + (k2 + 1) * k] & 0xFF;
                for (int i4 = 0; i4 < 4; ++i4) {
                    int j4 = ((k2 << 2) + i4) * i2 + (l2 << 2);
                    for (int k4 = 0; k4 < 4; ++k4) {
                        final double d10 = (i4 - d3) * (i4 - d3) + (k4 - d2) * (k4 - d2);
                        final double d11 = (i4 - d5) * (i4 - d5) + (k4 - d4) * (k4 - d4);
                        final double d12 = (i4 - d7) * (i4 - d7) + (k4 - d6) * (k4 - d6);
                        final double d13 = (i4 - d9) * (i4 - d9) + (k4 - d8) * (k4 - d8);
                        if (d10 < d11 && d10 < d12 && d10 < d13) {
                            aint2[j4++] = i3;
                        }
                        else if (d11 < d10 && d11 < d12 && d11 < d13) {
                            aint2[j4++] = k3;
                        }
                        else if (d12 < d10 && d12 < d11 && d12 < d13) {
                            aint2[j4++] = j3;
                        }
                        else {
                            aint2[j4++] = l3;
                        }
                    }
                }
                i3 = k3;
                j3 = l3;
                ++l2;
            }
        }
        final int[] aint3 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int l4 = 0; l4 < areaHeight; ++l4) {
            System.arraycopy(aint2, (l4 + (areaY & 0x3)) * i2 + (areaX & 0x3), aint3, l4 * areaWidth, areaWidth);
        }
        return aint3;
    }
}
