// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.layer;

public class GenLayerRiverInit extends GenLayer
{
    public GenLayerRiverInit(final long p_i2127_1_, final GenLayer p_i2127_3_) {
        super(p_i2127_1_);
        this.parent = p_i2127_3_;
    }
    
    @Override
    public int[] getInts(final int areaX, final int areaY, final int areaWidth, final int areaHeight) {
        final int[] aint = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
        final int[] aint2 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int i = 0; i < areaHeight; ++i) {
            for (int j = 0; j < areaWidth; ++j) {
                this.initChunkSeed(j + areaX, i + areaY);
                aint2[j + i * areaWidth] = ((aint[j + i * areaWidth] > 0) ? (this.nextInt(299999) + 2) : 0);
            }
        }
        return aint2;
    }
}
