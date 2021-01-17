// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import java.util.Random;
import java.util.List;
import java.util.Arrays;
import net.minecraft.util.BlockPos;

public class WorldChunkManagerHell extends WorldChunkManager
{
    private BiomeGenBase biomeGenerator;
    private float rainfall;
    
    public WorldChunkManagerHell(final BiomeGenBase p_i45374_1_, final float p_i45374_2_) {
        this.biomeGenerator = p_i45374_1_;
        this.rainfall = p_i45374_2_;
    }
    
    @Override
    public BiomeGenBase getBiomeGenerator(final BlockPos pos) {
        return this.biomeGenerator;
    }
    
    @Override
    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] biomes, final int x, final int z, final int width, final int height) {
        if (biomes == null || biomes.length < width * height) {
            biomes = new BiomeGenBase[width * height];
        }
        Arrays.fill(biomes, 0, width * height, this.biomeGenerator);
        return biomes;
    }
    
    @Override
    public float[] getRainfall(float[] listToReuse, final int x, final int z, final int width, final int length) {
        if (listToReuse == null || listToReuse.length < width * length) {
            listToReuse = new float[width * length];
        }
        Arrays.fill(listToReuse, 0, width * length, this.rainfall);
        return listToReuse;
    }
    
    @Override
    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] oldBiomeList, final int x, final int z, final int width, final int depth) {
        if (oldBiomeList == null || oldBiomeList.length < width * depth) {
            oldBiomeList = new BiomeGenBase[width * depth];
        }
        Arrays.fill(oldBiomeList, 0, width * depth, this.biomeGenerator);
        return oldBiomeList;
    }
    
    @Override
    public BiomeGenBase[] getBiomeGenAt(final BiomeGenBase[] listToReuse, final int x, final int z, final int width, final int length, final boolean cacheFlag) {
        return this.loadBlockGeneratorData(listToReuse, x, z, width, length);
    }
    
    @Override
    public BlockPos findBiomePosition(final int x, final int z, final int range, final List<BiomeGenBase> biomes, final Random random) {
        return biomes.contains(this.biomeGenerator) ? new BlockPos(x - range + random.nextInt(range * 2 + 1), 0, z - range + random.nextInt(range * 2 + 1)) : null;
    }
    
    @Override
    public boolean areBiomesViable(final int p_76940_1_, final int p_76940_2_, final int p_76940_3_, final List<BiomeGenBase> p_76940_4_) {
        return p_76940_4_.contains(this.biomeGenerator);
    }
}
