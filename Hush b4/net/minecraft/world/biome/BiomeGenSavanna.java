// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockDirt;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import java.util.Random;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;

public class BiomeGenSavanna extends BiomeGenBase
{
    private static final WorldGenSavannaTree field_150627_aC;
    
    static {
        field_150627_aC = new WorldGenSavannaTree(false);
    }
    
    protected BiomeGenSavanna(final int p_i45383_1_) {
        super(p_i45383_1_);
        this.spawnableCreatureList.add(new SpawnListEntry(EntityHorse.class, 1, 2, 6));
        this.theBiomeDecorator.treesPerChunk = 1;
        this.theBiomeDecorator.flowersPerChunk = 4;
        this.theBiomeDecorator.grassPerChunk = 20;
    }
    
    @Override
    public WorldGenAbstractTree genBigTreeChance(final Random rand) {
        return (rand.nextInt(5) > 0) ? BiomeGenSavanna.field_150627_aC : this.worldGeneratorTrees;
    }
    
    @Override
    protected BiomeGenBase createMutatedBiome(final int p_180277_1_) {
        final BiomeGenBase biomegenbase = new Mutated(p_180277_1_, this);
        biomegenbase.temperature = (this.temperature + 1.0f) * 0.5f;
        biomegenbase.minHeight = this.minHeight * 0.5f + 0.3f;
        biomegenbase.maxHeight = this.maxHeight * 0.5f + 1.2f;
        return biomegenbase;
    }
    
    @Override
    public void decorate(final World worldIn, final Random rand, final BlockPos pos) {
        BiomeGenSavanna.DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.GRASS);
        for (int i = 0; i < 7; ++i) {
            final int j = rand.nextInt(16) + 8;
            final int k = rand.nextInt(16) + 8;
            final int l = rand.nextInt(worldIn.getHeight(pos.add(j, 0, k)).getY() + 32);
            BiomeGenSavanna.DOUBLE_PLANT_GENERATOR.generate(worldIn, rand, pos.add(j, l, k));
        }
        super.decorate(worldIn, rand, pos);
    }
    
    public static class Mutated extends BiomeGenMutated
    {
        public Mutated(final int p_i45382_1_, final BiomeGenBase p_i45382_2_) {
            super(p_i45382_1_, p_i45382_2_);
            this.theBiomeDecorator.treesPerChunk = 2;
            this.theBiomeDecorator.flowersPerChunk = 2;
            this.theBiomeDecorator.grassPerChunk = 5;
        }
        
        @Override
        public void genTerrainBlocks(final World worldIn, final Random rand, final ChunkPrimer chunkPrimerIn, final int p_180622_4_, final int p_180622_5_, final double p_180622_6_) {
            this.topBlock = Blocks.grass.getDefaultState();
            this.fillerBlock = Blocks.dirt.getDefaultState();
            if (p_180622_6_ > 1.75) {
                this.topBlock = Blocks.stone.getDefaultState();
                this.fillerBlock = Blocks.stone.getDefaultState();
            }
            else if (p_180622_6_ > -0.5) {
                this.topBlock = Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
            }
            this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, p_180622_4_, p_180622_5_, p_180622_6_);
        }
        
        @Override
        public void decorate(final World worldIn, final Random rand, final BlockPos pos) {
            this.theBiomeDecorator.decorate(worldIn, rand, this, pos);
        }
    }
}
