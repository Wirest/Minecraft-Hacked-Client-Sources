// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.layer;

public class GenLayerZoom extends GenLayer
{
    public GenLayerZoom(final long p_i2134_1_, final GenLayer p_i2134_3_) {
        super(p_i2134_1_);
        super.parent = p_i2134_3_;
    }
    
    @Override
    public int[] getInts(final int areaX, final int areaY, final int areaWidth, final int areaHeight) {
        final int i = areaX >> 1;
        final int j = areaY >> 1;
        final int k = (areaWidth >> 1) + 2;
        final int l = (areaHeight >> 1) + 2;
        final int[] aint = this.parent.getInts(i, j, k, l);
        final int i2 = k - 1 << 1;
        final int j2 = l - 1 << 1;
        final int[] aint2 = IntCache.getIntCache(i2 * j2);
        for (int k2 = 0; k2 < l - 1; ++k2) {
            int l2 = (k2 << 1) * i2;
            int i3 = 0;
            int j3 = aint[i3 + 0 + (k2 + 0) * k];
            int k3 = aint[i3 + 0 + (k2 + 1) * k];
            while (i3 < k - 1) {
                this.initChunkSeed(i3 + i << 1, k2 + j << 1);
                final int l3 = aint[i3 + 1 + (k2 + 0) * k];
                final int i4 = aint[i3 + 1 + (k2 + 1) * k];
                aint2[l2] = j3;
                aint2[l2++ + i2] = this.selectRandom(j3, k3);
                aint2[l2] = this.selectRandom(j3, l3);
                aint2[l2++ + i2] = this.selectModeOrRandom(j3, l3, k3, i4);
                j3 = l3;
                k3 = i4;
                ++i3;
            }
        }
        final int[] aint3 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int j4 = 0; j4 < areaHeight; ++j4) {
            System.arraycopy(aint2, (j4 + (areaY & 0x1)) * i2 + (areaX & 0x1), aint3, j4 * areaWidth, areaWidth);
        }
        return aint3;
    }
    
    public static GenLayer magnify(final long p_75915_0_, final GenLayer p_75915_2_, final int p_75915_3_) {
        GenLayer genlayer = p_75915_2_;
        for (int i = 0; i < p_75915_3_; ++i) {
            genlayer = new GenLayerZoom(p_75915_0_ + i, genlayer);
        }
        return genlayer;
    }
}
