// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import java.util.List;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import com.google.common.collect.Lists;

public class BiomeGenMutated extends BiomeGenBase
{
    protected BiomeGenBase baseBiome;
    
    public BiomeGenMutated(final int id, final BiomeGenBase biome) {
        super(id);
        this.baseBiome = biome;
        this.func_150557_a(biome.color, true);
        this.biomeName = String.valueOf(biome.biomeName) + " M";
        this.topBlock = biome.topBlock;
        this.fillerBlock = biome.fillerBlock;
        this.fillerBlockMetadata = biome.fillerBlockMetadata;
        this.minHeight = biome.minHeight;
        this.maxHeight = biome.maxHeight;
        this.temperature = biome.temperature;
        this.rainfall = biome.rainfall;
        this.waterColorMultiplier = biome.waterColorMultiplier;
        this.enableSnow = biome.enableSnow;
        this.enableRain = biome.enableRain;
        this.spawnableCreatureList = (List<SpawnListEntry>)Lists.newArrayList((Iterable<?>)biome.spawnableCreatureList);
        this.spawnableMonsterList = (List<SpawnListEntry>)Lists.newArrayList((Iterable<?>)biome.spawnableMonsterList);
        this.spawnableCaveCreatureList = (List<SpawnListEntry>)Lists.newArrayList((Iterable<?>)biome.spawnableCaveCreatureList);
        this.spawnableWaterCreatureList = (List<SpawnListEntry>)Lists.newArrayList((Iterable<?>)biome.spawnableWaterCreatureList);
        this.temperature = biome.temperature;
        this.rainfall = biome.rainfall;
        this.minHeight = biome.minHeight + 0.1f;
        this.maxHeight = biome.maxHeight + 0.2f;
    }
    
    @Override
    public void decorate(final World worldIn, final Random rand, final BlockPos pos) {
        this.baseBiome.theBiomeDecorator.decorate(worldIn, rand, this, pos);
    }
    
    @Override
    public void genTerrainBlocks(final World worldIn, final Random rand, final ChunkPrimer chunkPrimerIn, final int p_180622_4_, final int p_180622_5_, final double p_180622_6_) {
        this.baseBiome.genTerrainBlocks(worldIn, rand, chunkPrimerIn, p_180622_4_, p_180622_5_, p_180622_6_);
    }
    
    @Override
    public float getSpawningChance() {
        return this.baseBiome.getSpawningChance();
    }
    
    @Override
    public WorldGenAbstractTree genBigTreeChance(final Random rand) {
        return this.baseBiome.genBigTreeChance(rand);
    }
    
    @Override
    public int getFoliageColorAtPos(final BlockPos pos) {
        return this.baseBiome.getFoliageColorAtPos(pos);
    }
    
    @Override
    public int getGrassColorAtPos(final BlockPos pos) {
        return this.baseBiome.getGrassColorAtPos(pos);
    }
    
    @Override
    public Class<? extends BiomeGenBase> getBiomeClass() {
        return this.baseBiome.getBiomeClass();
    }
    
    @Override
    public boolean isEqualTo(final BiomeGenBase biome) {
        return this.baseBiome.isEqualTo(biome);
    }
    
    @Override
    public TempCategory getTempCategory() {
        return this.baseBiome.getTempCategory();
    }
}
