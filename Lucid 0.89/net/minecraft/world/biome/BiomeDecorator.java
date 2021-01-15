package net.minecraft.world.biome;

import java.util.Random;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraft.world.gen.GeneratorBushFeature;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenCactus;
import net.minecraft.world.gen.feature.WorldGenClay;
import net.minecraft.world.gen.feature.WorldGenDeadBush;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenPumpkin;
import net.minecraft.world.gen.feature.WorldGenReed;
import net.minecraft.world.gen.feature.WorldGenSand;
import net.minecraft.world.gen.feature.WorldGenWaterlily;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeDecorator
{
    /** The world the BiomeDecorator is currently decorating */
    protected World currentWorld;

    /** The Biome Decorator's random number generator. */
    protected Random randomGenerator;
    protected BlockPos field_180294_c;
    protected ChunkProviderSettings chunkProviderSettings;

    /** The clay generator. */
    protected WorldGenerator clayGen = new WorldGenClay(4);

    /** The sand generator. */
    protected WorldGenerator sandGen;

    /** The gravel generator. */
    protected WorldGenerator gravelAsSandGen;

    /** The dirt generator. */
    protected WorldGenerator dirtGen;
    protected WorldGenerator gravelGen;
    protected WorldGenerator graniteGen;
    protected WorldGenerator dioriteGen;
    protected WorldGenerator andesiteGen;
    protected WorldGenerator coalGen;
    protected WorldGenerator ironGen;

    /** Field that holds gold WorldGenMinable */
    protected WorldGenerator goldGen;
    protected WorldGenerator redstoneGen;
    protected WorldGenerator diamondGen;

    /** Field that holds Lapis WorldGenMinable */
    protected WorldGenerator lapisGen;
    protected WorldGenFlowers yellowFlowerGen;

    /** Field that holds mushroomBrown WorldGenFlowers */
    protected WorldGenerator mushroomBrownGen;

    /** Field that holds mushroomRed WorldGenFlowers */
    protected WorldGenerator mushroomRedGen;

    /** Field that holds big mushroom generator */
    protected WorldGenerator bigMushroomGen;

    /** Field that holds WorldGenReed */
    protected WorldGenerator reedGen;

    /** Field that holds WorldGenCactus */
    protected WorldGenerator cactusGen;

    /** The water lily generation! */
    protected WorldGenerator waterlilyGen;

    /** Amount of waterlilys per chunk. */
    protected int waterlilyPerChunk;

    /**
     * The number of trees to attempt to generate per chunk. Up to 10 in forests, none in deserts.
     */
    protected int treesPerChunk;

    /**
     * The number of yellow flower patches to generate per chunk. The game generates much less than this number, since
     * it attempts to generate them at a random altitude.
     */
    protected int flowersPerChunk;

    /** The amount of tall grass to generate per chunk. */
    protected int grassPerChunk;

    /**
     * The number of dead bushes to generate per chunk. Used in deserts and swamps.
     */
    protected int deadBushPerChunk;

    /**
     * The number of extra mushroom patches per chunk. It generates 1/4 this number in brown mushroom patches, and 1/8
     * this number in red mushroom patches. These mushrooms go beyond the default base number of mushrooms.
     */
    protected int mushroomsPerChunk;

    /**
     * The number of reeds to generate per chunk. Reeds won't generate if the randomly selected placement is unsuitable.
     */
    protected int reedsPerChunk;

    /**
     * The number of cactus plants to generate per chunk. Cacti only work on sand.
     */
    protected int cactiPerChunk;

    /**
     * The number of sand patches to generate per chunk. Sand patches only generate when part of it is underwater.
     */
    protected int sandPerChunk;

    /**
     * The number of sand patches to generate per chunk. Sand patches only generate when part of it is underwater. There
     * appear to be two separate fields for this.
     */
    protected int sandPerChunk2;

    /**
     * The number of clay patches to generate per chunk. Only generates when part of it is underwater.
     */
    protected int clayPerChunk;

    /** Amount of big mushrooms per chunk */
    protected int bigMushroomsPerChunk;

    /** True if decorator should generate surface lava & water */
    public boolean generateLakes;

    public BiomeDecorator()
    {
        this.sandGen = new WorldGenSand(Blocks.sand, 7);
        this.gravelAsSandGen = new WorldGenSand(Blocks.gravel, 6);
        this.yellowFlowerGen = new WorldGenFlowers(Blocks.yellow_flower, BlockFlower.EnumFlowerType.DANDELION);
        this.mushroomBrownGen = new GeneratorBushFeature(Blocks.brown_mushroom);
        this.mushroomRedGen = new GeneratorBushFeature(Blocks.red_mushroom);
        this.bigMushroomGen = new WorldGenBigMushroom();
        this.reedGen = new WorldGenReed();
        this.cactusGen = new WorldGenCactus();
        this.waterlilyGen = new WorldGenWaterlily();
        this.flowersPerChunk = 2;
        this.grassPerChunk = 1;
        this.sandPerChunk = 1;
        this.sandPerChunk2 = 3;
        this.clayPerChunk = 1;
        this.generateLakes = true;
    }

    public void decorate(World worldIn, Random p_180292_2_, BiomeGenBase p_180292_3_, BlockPos p_180292_4_)
    {
        if (this.currentWorld != null)
        {
            throw new RuntimeException("Already decorating");
        }
        else
        {
            this.currentWorld = worldIn;
            String var5 = worldIn.getWorldInfo().getGeneratorOptions();

            if (var5 != null)
            {
                this.chunkProviderSettings = ChunkProviderSettings.Factory.func_177865_a(var5).func_177864_b();
            }
            else
            {
                this.chunkProviderSettings = ChunkProviderSettings.Factory.func_177865_a("").func_177864_b();
            }

            this.randomGenerator = p_180292_2_;
            this.field_180294_c = p_180292_4_;
            this.dirtGen = new WorldGenMinable(Blocks.dirt.getDefaultState(), this.chunkProviderSettings.dirtSize);
            this.gravelGen = new WorldGenMinable(Blocks.gravel.getDefaultState(), this.chunkProviderSettings.gravelSize);
            this.graniteGen = new WorldGenMinable(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), this.chunkProviderSettings.graniteSize);
            this.dioriteGen = new WorldGenMinable(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), this.chunkProviderSettings.dioriteSize);
            this.andesiteGen = new WorldGenMinable(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE), this.chunkProviderSettings.andesiteSize);
            this.coalGen = new WorldGenMinable(Blocks.coal_ore.getDefaultState(), this.chunkProviderSettings.coalSize);
            this.ironGen = new WorldGenMinable(Blocks.iron_ore.getDefaultState(), this.chunkProviderSettings.ironSize);
            this.goldGen = new WorldGenMinable(Blocks.gold_ore.getDefaultState(), this.chunkProviderSettings.goldSize);
            this.redstoneGen = new WorldGenMinable(Blocks.redstone_ore.getDefaultState(), this.chunkProviderSettings.redstoneSize);
            this.diamondGen = new WorldGenMinable(Blocks.diamond_ore.getDefaultState(), this.chunkProviderSettings.diamondSize);
            this.lapisGen = new WorldGenMinable(Blocks.lapis_ore.getDefaultState(), this.chunkProviderSettings.lapisSize);
            this.genDecorations(p_180292_3_);
            this.currentWorld = null;
            this.randomGenerator = null;
        }
    }

    protected void genDecorations(BiomeGenBase p_150513_1_)
    {
        this.generateOres();
        int var2;
        int var3;
        int var4;

        for (var2 = 0; var2 < this.sandPerChunk2; ++var2)
        {
            var3 = this.randomGenerator.nextInt(16) + 8;
            var4 = this.randomGenerator.nextInt(16) + 8;
            this.sandGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(var3, 0, var4)));
        }

        for (var2 = 0; var2 < this.clayPerChunk; ++var2)
        {
            var3 = this.randomGenerator.nextInt(16) + 8;
            var4 = this.randomGenerator.nextInt(16) + 8;
            this.clayGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(var3, 0, var4)));
        }

        for (var2 = 0; var2 < this.sandPerChunk; ++var2)
        {
            var3 = this.randomGenerator.nextInt(16) + 8;
            var4 = this.randomGenerator.nextInt(16) + 8;
            this.gravelAsSandGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(var3, 0, var4)));
        }

        var2 = this.treesPerChunk;

        if (this.randomGenerator.nextInt(10) == 0)
        {
            ++var2;
        }

        int var5;
        BlockPos var7;

        for (var3 = 0; var3 < var2; ++var3)
        {
            var4 = this.randomGenerator.nextInt(16) + 8;
            var5 = this.randomGenerator.nextInt(16) + 8;
            WorldGenAbstractTree var6 = p_150513_1_.genBigTreeChance(this.randomGenerator);
            var6.func_175904_e();
            var7 = this.currentWorld.getHeight(this.field_180294_c.add(var4, 0, var5));

            if (var6.generate(this.currentWorld, this.randomGenerator, var7))
            {
                var6.func_180711_a(this.currentWorld, this.randomGenerator, var7);
            }
        }

        for (var3 = 0; var3 < this.bigMushroomsPerChunk; ++var3)
        {
            var4 = this.randomGenerator.nextInt(16) + 8;
            var5 = this.randomGenerator.nextInt(16) + 8;
            this.bigMushroomGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getHeight(this.field_180294_c.add(var4, 0, var5)));
        }

        int var11;

        for (var3 = 0; var3 < this.flowersPerChunk; ++var3)
        {
            var4 = this.randomGenerator.nextInt(16) + 8;
            var5 = this.randomGenerator.nextInt(16) + 8;
            var11 = this.randomGenerator.nextInt(this.currentWorld.getHeight(this.field_180294_c.add(var4, 0, var5)).getY() + 32);
            var7 = this.field_180294_c.add(var4, var11, var5);
            BlockFlower.EnumFlowerType var8 = p_150513_1_.pickRandomFlower(this.randomGenerator, var7);
            BlockFlower var9 = var8.getBlockType().getBlock();

            if (var9.getMaterial() != Material.air)
            {
                this.yellowFlowerGen.setGeneratedBlock(var9, var8);
                this.yellowFlowerGen.generate(this.currentWorld, this.randomGenerator, var7);
            }
        }

        for (var3 = 0; var3 < this.grassPerChunk; ++var3)
        {
            var4 = this.randomGenerator.nextInt(16) + 8;
            var5 = this.randomGenerator.nextInt(16) + 8;
            var11 = this.randomGenerator.nextInt(this.currentWorld.getHeight(this.field_180294_c.add(var4, 0, var5)).getY() * 2);
            p_150513_1_.getRandomWorldGenForGrass(this.randomGenerator).generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(var4, var11, var5));
        }

        for (var3 = 0; var3 < this.deadBushPerChunk; ++var3)
        {
            var4 = this.randomGenerator.nextInt(16) + 8;
            var5 = this.randomGenerator.nextInt(16) + 8;
            var11 = this.randomGenerator.nextInt(this.currentWorld.getHeight(this.field_180294_c.add(var4, 0, var5)).getY() * 2);
            (new WorldGenDeadBush()).generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(var4, var11, var5));
        }

        var3 = 0;

        while (var3 < this.waterlilyPerChunk)
        {
            var4 = this.randomGenerator.nextInt(16) + 8;
            var5 = this.randomGenerator.nextInt(16) + 8;
            var11 = this.randomGenerator.nextInt(this.currentWorld.getHeight(this.field_180294_c.add(var4, 0, var5)).getY() * 2);
            var7 = this.field_180294_c.add(var4, var11, var5);

            while (true)
            {
                if (var7.getY() > 0)
                {
                    BlockPos var13 = var7.down();

                    if (this.currentWorld.isAirBlock(var13))
                    {
                        var7 = var13;
                        continue;
                    }
                }

                this.waterlilyGen.generate(this.currentWorld, this.randomGenerator, var7);
                ++var3;
                break;
            }
        }

        for (var3 = 0; var3 < this.mushroomsPerChunk; ++var3)
        {
            if (this.randomGenerator.nextInt(4) == 0)
            {
                var4 = this.randomGenerator.nextInt(16) + 8;
                var5 = this.randomGenerator.nextInt(16) + 8;
                BlockPos var12 = this.currentWorld.getHeight(this.field_180294_c.add(var4, 0, var5));
                this.mushroomBrownGen.generate(this.currentWorld, this.randomGenerator, var12);
            }

            if (this.randomGenerator.nextInt(8) == 0)
            {
                var4 = this.randomGenerator.nextInt(16) + 8;
                var5 = this.randomGenerator.nextInt(16) + 8;
                var11 = this.randomGenerator.nextInt(this.currentWorld.getHeight(this.field_180294_c.add(var4, 0, var5)).getY() * 2);
                var7 = this.field_180294_c.add(var4, var11, var5);
                this.mushroomRedGen.generate(this.currentWorld, this.randomGenerator, var7);
            }
        }

        if (this.randomGenerator.nextInt(4) == 0)
        {
            var3 = this.randomGenerator.nextInt(16) + 8;
            var4 = this.randomGenerator.nextInt(16) + 8;
            var5 = this.randomGenerator.nextInt(this.currentWorld.getHeight(this.field_180294_c.add(var3, 0, var4)).getY() * 2);
            this.mushroomBrownGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(var3, var5, var4));
        }

        if (this.randomGenerator.nextInt(8) == 0)
        {
            var3 = this.randomGenerator.nextInt(16) + 8;
            var4 = this.randomGenerator.nextInt(16) + 8;
            var5 = this.randomGenerator.nextInt(this.currentWorld.getHeight(this.field_180294_c.add(var3, 0, var4)).getY() * 2);
            this.mushroomRedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(var3, var5, var4));
        }

        for (var3 = 0; var3 < this.reedsPerChunk; ++var3)
        {
            var4 = this.randomGenerator.nextInt(16) + 8;
            var5 = this.randomGenerator.nextInt(16) + 8;
            var11 = this.randomGenerator.nextInt(this.currentWorld.getHeight(this.field_180294_c.add(var4, 0, var5)).getY() * 2);
            this.reedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(var4, var11, var5));
        }

        for (var3 = 0; var3 < 10; ++var3)
        {
            var4 = this.randomGenerator.nextInt(16) + 8;
            var5 = this.randomGenerator.nextInt(16) + 8;
            var11 = this.randomGenerator.nextInt(this.currentWorld.getHeight(this.field_180294_c.add(var4, 0, var5)).getY() * 2);
            this.reedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(var4, var11, var5));
        }

        if (this.randomGenerator.nextInt(32) == 0)
        {
            var3 = this.randomGenerator.nextInt(16) + 8;
            var4 = this.randomGenerator.nextInt(16) + 8;
            var5 = this.randomGenerator.nextInt(this.currentWorld.getHeight(this.field_180294_c.add(var3, 0, var4)).getY() * 2);
            (new WorldGenPumpkin()).generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(var3, var5, var4));
        }

        for (var3 = 0; var3 < this.cactiPerChunk; ++var3)
        {
            var4 = this.randomGenerator.nextInt(16) + 8;
            var5 = this.randomGenerator.nextInt(16) + 8;
            var11 = this.randomGenerator.nextInt(this.currentWorld.getHeight(this.field_180294_c.add(var4, 0, var5)).getY() * 2);
            this.cactusGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(var4, var11, var5));
        }

        if (this.generateLakes)
        {
            BlockPos var10;

            for (var3 = 0; var3 < 50; ++var3)
            {
                var10 = this.field_180294_c.add(this.randomGenerator.nextInt(16) + 8, this.randomGenerator.nextInt(this.randomGenerator.nextInt(248) + 8), this.randomGenerator.nextInt(16) + 8);
                (new WorldGenLiquids(Blocks.flowing_water)).generate(this.currentWorld, this.randomGenerator, var10);
            }

            for (var3 = 0; var3 < 20; ++var3)
            {
                var10 = this.field_180294_c.add(this.randomGenerator.nextInt(16) + 8, this.randomGenerator.nextInt(this.randomGenerator.nextInt(this.randomGenerator.nextInt(240) + 8) + 8), this.randomGenerator.nextInt(16) + 8);
                (new WorldGenLiquids(Blocks.flowing_lava)).generate(this.currentWorld, this.randomGenerator, var10);
            }
        }
    }

    /**
     * Standard ore generation helper. Generates most ores.
     */
    protected void genStandardOre1(int p_76795_1_, WorldGenerator p_76795_2_, int p_76795_3_, int p_76795_4_)
    {
        int var5;

        if (p_76795_4_ < p_76795_3_)
        {
            var5 = p_76795_3_;
            p_76795_3_ = p_76795_4_;
            p_76795_4_ = var5;
        }
        else if (p_76795_4_ == p_76795_3_)
        {
            if (p_76795_3_ < 255)
            {
                ++p_76795_4_;
            }
            else
            {
                --p_76795_3_;
            }
        }

        for (var5 = 0; var5 < p_76795_1_; ++var5)
        {
            BlockPos var6 = this.field_180294_c.add(this.randomGenerator.nextInt(16), this.randomGenerator.nextInt(p_76795_4_ - p_76795_3_) + p_76795_3_, this.randomGenerator.nextInt(16));
            p_76795_2_.generate(this.currentWorld, this.randomGenerator, var6);
        }
    }

    /**
     * Standard ore generation helper. Generates Lapis Lazuli.
     */
    protected void genStandardOre2(int p_76793_1_, WorldGenerator p_76793_2_, int p_76793_3_, int p_76793_4_)
    {
        for (int var5 = 0; var5 < p_76793_1_; ++var5)
        {
            BlockPos var6 = this.field_180294_c.add(this.randomGenerator.nextInt(16), this.randomGenerator.nextInt(p_76793_4_) + this.randomGenerator.nextInt(p_76793_4_) + p_76793_3_ - p_76793_4_, this.randomGenerator.nextInt(16));
            p_76793_2_.generate(this.currentWorld, this.randomGenerator, var6);
        }
    }

    /**
     * Generates ores in the current chunk
     */
    protected void generateOres()
    {
        this.genStandardOre1(this.chunkProviderSettings.dirtCount, this.dirtGen, this.chunkProviderSettings.dirtMinHeight, this.chunkProviderSettings.dirtMaxHeight);
        this.genStandardOre1(this.chunkProviderSettings.gravelCount, this.gravelGen, this.chunkProviderSettings.gravelMinHeight, this.chunkProviderSettings.gravelMaxHeight);
        this.genStandardOre1(this.chunkProviderSettings.dioriteCount, this.dioriteGen, this.chunkProviderSettings.dioriteMinHeight, this.chunkProviderSettings.dioriteMaxHeight);
        this.genStandardOre1(this.chunkProviderSettings.graniteCount, this.graniteGen, this.chunkProviderSettings.graniteMinHeight, this.chunkProviderSettings.graniteMaxHeight);
        this.genStandardOre1(this.chunkProviderSettings.andesiteCount, this.andesiteGen, this.chunkProviderSettings.andesiteMinHeight, this.chunkProviderSettings.andesiteMaxHeight);
        this.genStandardOre1(this.chunkProviderSettings.coalCount, this.coalGen, this.chunkProviderSettings.coalMinHeight, this.chunkProviderSettings.coalMaxHeight);
        this.genStandardOre1(this.chunkProviderSettings.ironCount, this.ironGen, this.chunkProviderSettings.ironMinHeight, this.chunkProviderSettings.ironMaxHeight);
        this.genStandardOre1(this.chunkProviderSettings.goldCount, this.goldGen, this.chunkProviderSettings.goldMinHeight, this.chunkProviderSettings.goldMaxHeight);
        this.genStandardOre1(this.chunkProviderSettings.redstoneCount, this.redstoneGen, this.chunkProviderSettings.redstoneMinHeight, this.chunkProviderSettings.redstoneMaxHeight);
        this.genStandardOre1(this.chunkProviderSettings.diamondCount, this.diamondGen, this.chunkProviderSettings.diamondMinHeight, this.chunkProviderSettings.diamondMaxHeight);
        this.genStandardOre2(this.chunkProviderSettings.lapisCount, this.lapisGen, this.chunkProviderSettings.lapisCenterHeight, this.chunkProviderSettings.lapisSpread);
    }
}
