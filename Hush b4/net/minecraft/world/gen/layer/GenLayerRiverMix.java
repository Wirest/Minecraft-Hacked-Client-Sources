// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;

public class GenLayerRiverMix extends GenLayer
{
    private GenLayer biomePatternGeneratorChain;
    private GenLayer riverPatternGeneratorChain;
    
    public GenLayerRiverMix(final long p_i2129_1_, final GenLayer p_i2129_3_, final GenLayer p_i2129_4_) {
        super(p_i2129_1_);
        this.biomePatternGeneratorChain = p_i2129_3_;
        this.riverPatternGeneratorChain = p_i2129_4_;
    }
    
    @Override
    public void initWorldGenSeed(final long seed) {
        this.biomePatternGeneratorChain.initWorldGenSeed(seed);
        this.riverPatternGeneratorChain.initWorldGenSeed(seed);
        super.initWorldGenSeed(seed);
    }
    
    @Override
    public int[] getInts(final int areaX, final int areaY, final int areaWidth, final int areaHeight) {
        final int[] aint = this.biomePatternGeneratorChain.getInts(areaX, areaY, areaWidth, areaHeight);
        final int[] aint2 = this.riverPatternGeneratorChain.getInts(areaX, areaY, areaWidth, areaHeight);
        final int[] aint3 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int i = 0; i < areaWidth * areaHeight; ++i) {
            if (aint[i] != BiomeGenBase.ocean.biomeID && aint[i] != BiomeGenBase.deepOcean.biomeID) {
                if (aint2[i] == BiomeGenBase.river.biomeID) {
                    if (aint[i] == BiomeGenBase.icePlains.biomeID) {
                        aint3[i] = BiomeGenBase.frozenRiver.biomeID;
                    }
                    else if (aint[i] != BiomeGenBase.mushroomIsland.biomeID && aint[i] != BiomeGenBase.mushroomIslandShore.biomeID) {
                        aint3[i] = (aint2[i] & 0xFF);
                    }
                    else {
                        aint3[i] = BiomeGenBase.mushroomIslandShore.biomeID;
                    }
                }
                else {
                    aint3[i] = aint[i];
                }
            }
            else {
                aint3[i] = aint[i];
            }
        }
        return aint3;
    }
}
