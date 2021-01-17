// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.world.chunk.ChunkPrimer;
import java.util.Random;
import net.minecraft.world.World;

public class BiomeGenOcean extends BiomeGenBase
{
    public BiomeGenOcean(final int p_i1985_1_) {
        super(p_i1985_1_);
        this.spawnableCreatureList.clear();
    }
    
    @Override
    public TempCategory getTempCategory() {
        return TempCategory.OCEAN;
    }
    
    @Override
    public void genTerrainBlocks(final World worldIn, final Random rand, final ChunkPrimer chunkPrimerIn, final int p_180622_4_, final int p_180622_5_, final double p_180622_6_) {
        super.genTerrainBlocks(worldIn, rand, chunkPrimerIn, p_180622_4_, p_180622_5_, p_180622_6_);
    }
}
