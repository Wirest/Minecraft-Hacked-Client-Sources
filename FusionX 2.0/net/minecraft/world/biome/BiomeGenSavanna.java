package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;

public class BiomeGenSavanna extends BiomeGenBase
{
    private static final WorldGenSavannaTree field_150627_aC = new WorldGenSavannaTree(false);
    private static final String __OBFID = "CL_00000182";

    protected BiomeGenSavanna(int p_i45383_1_)
    {
        super(p_i45383_1_);
        this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityHorse.class, 1, 2, 6));
        this.theBiomeDecorator.treesPerChunk = 1;
        this.theBiomeDecorator.flowersPerChunk = 4;
        this.theBiomeDecorator.grassPerChunk = 20;
    }

    public WorldGenAbstractTree genBigTreeChance(Random p_150567_1_)
    {
        return (WorldGenAbstractTree)(p_150567_1_.nextInt(5) > 0 ? field_150627_aC : this.worldGeneratorTrees);
    }

    protected BiomeGenBase createMutatedBiome(int p_180277_1_)
    {
        BiomeGenSavanna.Mutated var2 = new BiomeGenSavanna.Mutated(p_180277_1_, this);
        var2.temperature = (this.temperature + 1.0F) * 0.5F;
        var2.minHeight = this.minHeight * 0.5F + 0.3F;
        var2.maxHeight = this.maxHeight * 0.5F + 1.2F;
        return var2;
    }

    public void func_180624_a(World worldIn, Random p_180624_2_, BlockPos p_180624_3_)
    {
        field_180280_ag.func_180710_a(BlockDoublePlant.EnumPlantType.GRASS);

        for (int var4 = 0; var4 < 7; ++var4)
        {
            int var5 = p_180624_2_.nextInt(16) + 8;
            int var6 = p_180624_2_.nextInt(16) + 8;
            int var7 = p_180624_2_.nextInt(worldIn.getHorizon(p_180624_3_.add(var5, 0, var6)).getY() + 32);
            field_180280_ag.generate(worldIn, p_180624_2_, p_180624_3_.add(var5, var7, var6));
        }

        super.func_180624_a(worldIn, p_180624_2_, p_180624_3_);
    }

    public static class Mutated extends BiomeGenMutated
    {
        private static final String __OBFID = "CL_00000183";

        public Mutated(int p_i45382_1_, BiomeGenBase p_i45382_2_)
        {
            super(p_i45382_1_, p_i45382_2_);
            this.theBiomeDecorator.treesPerChunk = 2;
            this.theBiomeDecorator.flowersPerChunk = 2;
            this.theBiomeDecorator.grassPerChunk = 5;
        }

        public void genTerrainBlocks(World worldIn, Random p_180622_2_, ChunkPrimer p_180622_3_, int p_180622_4_, int p_180622_5_, double p_180622_6_)
        {
            this.topBlock = Blocks.grass.getDefaultState();
            this.fillerBlock = Blocks.dirt.getDefaultState();

            if (p_180622_6_ > 1.75D)
            {
                this.topBlock = Blocks.stone.getDefaultState();
                this.fillerBlock = Blocks.stone.getDefaultState();
            }
            else if (p_180622_6_ > -0.5D)
            {
                this.topBlock = Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
            }

            this.func_180628_b(worldIn, p_180622_2_, p_180622_3_, p_180622_4_, p_180622_5_, p_180622_6_);
        }

        public void func_180624_a(World worldIn, Random p_180624_2_, BlockPos p_180624_3_)
        {
            this.theBiomeDecorator.func_180292_a(worldIn, p_180624_2_, this, p_180624_3_);
        }
    }
}
