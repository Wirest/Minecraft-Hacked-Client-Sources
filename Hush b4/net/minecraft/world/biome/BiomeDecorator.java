// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraft.world.gen.feature.WorldGenPumpkin;
import net.minecraft.world.gen.feature.WorldGenDeadBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockStone;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenWaterlily;
import net.minecraft.world.gen.feature.WorldGenCactus;
import net.minecraft.world.gen.feature.WorldGenReed;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.GeneratorBushFeature;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.Block;
import net.minecraft.world.gen.feature.WorldGenSand;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenClay;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;

public class BiomeDecorator
{
    protected World currentWorld;
    protected Random randomGenerator;
    protected BlockPos field_180294_c;
    protected ChunkProviderSettings chunkProviderSettings;
    protected WorldGenerator clayGen;
    protected WorldGenerator sandGen;
    protected WorldGenerator gravelAsSandGen;
    protected WorldGenerator dirtGen;
    protected WorldGenerator gravelGen;
    protected WorldGenerator graniteGen;
    protected WorldGenerator dioriteGen;
    protected WorldGenerator andesiteGen;
    protected WorldGenerator coalGen;
    protected WorldGenerator ironGen;
    protected WorldGenerator goldGen;
    protected WorldGenerator redstoneGen;
    protected WorldGenerator diamondGen;
    protected WorldGenerator lapisGen;
    protected WorldGenFlowers yellowFlowerGen;
    protected WorldGenerator mushroomBrownGen;
    protected WorldGenerator mushroomRedGen;
    protected WorldGenerator bigMushroomGen;
    protected WorldGenerator reedGen;
    protected WorldGenerator cactusGen;
    protected WorldGenerator waterlilyGen;
    protected int waterlilyPerChunk;
    protected int treesPerChunk;
    protected int flowersPerChunk;
    protected int grassPerChunk;
    protected int deadBushPerChunk;
    protected int mushroomsPerChunk;
    protected int reedsPerChunk;
    protected int cactiPerChunk;
    protected int sandPerChunk;
    protected int sandPerChunk2;
    protected int clayPerChunk;
    protected int bigMushroomsPerChunk;
    public boolean generateLakes;
    
    public BiomeDecorator() {
        this.clayGen = new WorldGenClay(4);
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
    
    public void decorate(final World worldIn, final Random random, final BiomeGenBase p_180292_3_, final BlockPos p_180292_4_) {
        if (this.currentWorld != null) {
            throw new RuntimeException("Already decorating");
        }
        this.currentWorld = worldIn;
        final String s = worldIn.getWorldInfo().getGeneratorOptions();
        if (s != null) {
            this.chunkProviderSettings = ChunkProviderSettings.Factory.jsonToFactory(s).func_177864_b();
        }
        else {
            this.chunkProviderSettings = ChunkProviderSettings.Factory.jsonToFactory("").func_177864_b();
        }
        this.randomGenerator = random;
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
    
    protected void genDecorations(final BiomeGenBase biomeGenBaseIn) {
        this.generateOres();
        for (int i = 0; i < this.sandPerChunk2; ++i) {
            final int j = this.randomGenerator.nextInt(16) + 8;
            final int k = this.randomGenerator.nextInt(16) + 8;
            this.sandGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(j, 0, k)));
        }
        for (int i2 = 0; i2 < this.clayPerChunk; ++i2) {
            final int l1 = this.randomGenerator.nextInt(16) + 8;
            final int i3 = this.randomGenerator.nextInt(16) + 8;
            this.clayGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(l1, 0, i3)));
        }
        for (int j2 = 0; j2 < this.sandPerChunk; ++j2) {
            final int i4 = this.randomGenerator.nextInt(16) + 8;
            final int j3 = this.randomGenerator.nextInt(16) + 8;
            this.gravelAsSandGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(i4, 0, j3)));
        }
        int k2 = this.treesPerChunk;
        if (this.randomGenerator.nextInt(10) == 0) {
            ++k2;
        }
        for (int j4 = 0; j4 < k2; ++j4) {
            final int k3 = this.randomGenerator.nextInt(16) + 8;
            final int m = this.randomGenerator.nextInt(16) + 8;
            final WorldGenAbstractTree worldgenabstracttree = biomeGenBaseIn.genBigTreeChance(this.randomGenerator);
            worldgenabstracttree.func_175904_e();
            final BlockPos blockpos = this.currentWorld.getHeight(this.field_180294_c.add(k3, 0, m));
            if (worldgenabstracttree.generate(this.currentWorld, this.randomGenerator, blockpos)) {
                worldgenabstracttree.func_180711_a(this.currentWorld, this.randomGenerator, blockpos);
            }
        }
        for (int k4 = 0; k4 < this.bigMushroomsPerChunk; ++k4) {
            final int l2 = this.randomGenerator.nextInt(16) + 8;
            final int k5 = this.randomGenerator.nextInt(16) + 8;
            this.bigMushroomGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getHeight(this.field_180294_c.add(l2, 0, k5)));
        }
        for (int l3 = 0; l3 < this.flowersPerChunk; ++l3) {
            final int i5 = this.randomGenerator.nextInt(16) + 8;
            final int l4 = this.randomGenerator.nextInt(16) + 8;
            final int j5 = this.currentWorld.getHeight(this.field_180294_c.add(i5, 0, l4)).getY() + 32;
            if (j5 > 0) {
                final int k6 = this.randomGenerator.nextInt(j5);
                final BlockPos blockpos2 = this.field_180294_c.add(i5, k6, l4);
                final BlockFlower.EnumFlowerType blockflower$enumflowertype = biomeGenBaseIn.pickRandomFlower(this.randomGenerator, blockpos2);
                final BlockFlower blockflower = blockflower$enumflowertype.getBlockType().getBlock();
                if (blockflower.getMaterial() != Material.air) {
                    this.yellowFlowerGen.setGeneratedBlock(blockflower, blockflower$enumflowertype);
                    this.yellowFlowerGen.generate(this.currentWorld, this.randomGenerator, blockpos2);
                }
            }
        }
        for (int i6 = 0; i6 < this.grassPerChunk; ++i6) {
            final int j6 = this.randomGenerator.nextInt(16) + 8;
            final int i7 = this.randomGenerator.nextInt(16) + 8;
            final int k7 = this.currentWorld.getHeight(this.field_180294_c.add(j6, 0, i7)).getY() * 2;
            if (k7 > 0) {
                final int l5 = this.randomGenerator.nextInt(k7);
                biomeGenBaseIn.getRandomWorldGenForGrass(this.randomGenerator).generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(j6, l5, i7));
            }
        }
        for (int j7 = 0; j7 < this.deadBushPerChunk; ++j7) {
            final int k8 = this.randomGenerator.nextInt(16) + 8;
            final int j8 = this.randomGenerator.nextInt(16) + 8;
            final int l6 = this.currentWorld.getHeight(this.field_180294_c.add(k8, 0, j8)).getY() * 2;
            if (l6 > 0) {
                final int i8 = this.randomGenerator.nextInt(l6);
                new WorldGenDeadBush().generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(k8, i8, j8));
            }
        }
        for (int k9 = 0; k9 < this.waterlilyPerChunk; ++k9) {
            final int l7 = this.randomGenerator.nextInt(16) + 8;
            final int k10 = this.randomGenerator.nextInt(16) + 8;
            final int i9 = this.currentWorld.getHeight(this.field_180294_c.add(l7, 0, k10)).getY() * 2;
            if (i9 > 0) {
                final int j9 = this.randomGenerator.nextInt(i9);
                BlockPos blockpos3;
                BlockPos blockpos4;
                for (blockpos3 = this.field_180294_c.add(l7, j9, k10); blockpos3.getY() > 0; blockpos3 = blockpos4) {
                    blockpos4 = blockpos3.down();
                    if (!this.currentWorld.isAirBlock(blockpos4)) {
                        break;
                    }
                }
                this.waterlilyGen.generate(this.currentWorld, this.randomGenerator, blockpos3);
            }
        }
        for (int l8 = 0; l8 < this.mushroomsPerChunk; ++l8) {
            if (this.randomGenerator.nextInt(4) == 0) {
                final int i10 = this.randomGenerator.nextInt(16) + 8;
                final int l9 = this.randomGenerator.nextInt(16) + 8;
                final BlockPos blockpos5 = this.currentWorld.getHeight(this.field_180294_c.add(i10, 0, l9));
                this.mushroomBrownGen.generate(this.currentWorld, this.randomGenerator, blockpos5);
            }
            if (this.randomGenerator.nextInt(8) == 0) {
                final int j10 = this.randomGenerator.nextInt(16) + 8;
                final int i11 = this.randomGenerator.nextInt(16) + 8;
                final int j11 = this.currentWorld.getHeight(this.field_180294_c.add(j10, 0, i11)).getY() * 2;
                if (j11 > 0) {
                    final int k11 = this.randomGenerator.nextInt(j11);
                    final BlockPos blockpos6 = this.field_180294_c.add(j10, k11, i11);
                    this.mushroomRedGen.generate(this.currentWorld, this.randomGenerator, blockpos6);
                }
            }
        }
        if (this.randomGenerator.nextInt(4) == 0) {
            final int i12 = this.randomGenerator.nextInt(16) + 8;
            final int k12 = this.randomGenerator.nextInt(16) + 8;
            final int j12 = this.currentWorld.getHeight(this.field_180294_c.add(i12, 0, k12)).getY() * 2;
            if (j12 > 0) {
                final int k13 = this.randomGenerator.nextInt(j12);
                this.mushroomBrownGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(i12, k13, k12));
            }
        }
        if (this.randomGenerator.nextInt(8) == 0) {
            final int j13 = this.randomGenerator.nextInt(16) + 8;
            final int l10 = this.randomGenerator.nextInt(16) + 8;
            final int k14 = this.currentWorld.getHeight(this.field_180294_c.add(j13, 0, l10)).getY() * 2;
            if (k14 > 0) {
                final int l11 = this.randomGenerator.nextInt(k14);
                this.mushroomRedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(j13, l11, l10));
            }
        }
        for (int k15 = 0; k15 < this.reedsPerChunk; ++k15) {
            final int i13 = this.randomGenerator.nextInt(16) + 8;
            final int l12 = this.randomGenerator.nextInt(16) + 8;
            final int i14 = this.currentWorld.getHeight(this.field_180294_c.add(i13, 0, l12)).getY() * 2;
            if (i14 > 0) {
                final int l13 = this.randomGenerator.nextInt(i14);
                this.reedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(i13, l13, l12));
            }
        }
        for (int l14 = 0; l14 < 10; ++l14) {
            final int j14 = this.randomGenerator.nextInt(16) + 8;
            final int i15 = this.randomGenerator.nextInt(16) + 8;
            final int j15 = this.currentWorld.getHeight(this.field_180294_c.add(j14, 0, i15)).getY() * 2;
            if (j15 > 0) {
                final int i16 = this.randomGenerator.nextInt(j15);
                this.reedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(j14, i16, i15));
            }
        }
        if (this.randomGenerator.nextInt(32) == 0) {
            final int i17 = this.randomGenerator.nextInt(16) + 8;
            final int k16 = this.randomGenerator.nextInt(16) + 8;
            final int j16 = this.currentWorld.getHeight(this.field_180294_c.add(i17, 0, k16)).getY() * 2;
            if (j16 > 0) {
                final int k17 = this.randomGenerator.nextInt(j16);
                new WorldGenPumpkin().generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(i17, k17, k16));
            }
        }
        for (int j17 = 0; j17 < this.cactiPerChunk; ++j17) {
            final int l15 = this.randomGenerator.nextInt(16) + 8;
            final int k18 = this.randomGenerator.nextInt(16) + 8;
            final int l16 = this.currentWorld.getHeight(this.field_180294_c.add(l15, 0, k18)).getY() * 2;
            if (l16 > 0) {
                final int j18 = this.randomGenerator.nextInt(l16);
                this.cactusGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(l15, j18, k18));
            }
        }
        if (this.generateLakes) {
            for (int k19 = 0; k19 < 50; ++k19) {
                final int i18 = this.randomGenerator.nextInt(16) + 8;
                final int l17 = this.randomGenerator.nextInt(16) + 8;
                final int i19 = this.randomGenerator.nextInt(248) + 8;
                if (i19 > 0) {
                    final int k20 = this.randomGenerator.nextInt(i19);
                    final BlockPos blockpos7 = this.field_180294_c.add(i18, k20, l17);
                    new WorldGenLiquids(Blocks.flowing_water).generate(this.currentWorld, this.randomGenerator, blockpos7);
                }
            }
            for (int l18 = 0; l18 < 20; ++l18) {
                final int j19 = this.randomGenerator.nextInt(16) + 8;
                final int i20 = this.randomGenerator.nextInt(16) + 8;
                final int j20 = this.randomGenerator.nextInt(this.randomGenerator.nextInt(this.randomGenerator.nextInt(240) + 8) + 8);
                final BlockPos blockpos8 = this.field_180294_c.add(j19, j20, i20);
                new WorldGenLiquids(Blocks.flowing_lava).generate(this.currentWorld, this.randomGenerator, blockpos8);
            }
        }
    }
    
    protected void genStandardOre1(final int blockCount, final WorldGenerator generator, int minHeight, int maxHeight) {
        if (maxHeight < minHeight) {
            final int i = minHeight;
            minHeight = maxHeight;
            maxHeight = i;
        }
        else if (maxHeight == minHeight) {
            if (minHeight < 255) {
                ++maxHeight;
            }
            else {
                --minHeight;
            }
        }
        for (int j = 0; j < blockCount; ++j) {
            final BlockPos blockpos = this.field_180294_c.add(this.randomGenerator.nextInt(16), this.randomGenerator.nextInt(maxHeight - minHeight) + minHeight, this.randomGenerator.nextInt(16));
            generator.generate(this.currentWorld, this.randomGenerator, blockpos);
        }
    }
    
    protected void genStandardOre2(final int blockCount, final WorldGenerator generator, final int centerHeight, final int spread) {
        for (int i = 0; i < blockCount; ++i) {
            final BlockPos blockpos = this.field_180294_c.add(this.randomGenerator.nextInt(16), this.randomGenerator.nextInt(spread) + this.randomGenerator.nextInt(spread) + centerHeight - spread, this.randomGenerator.nextInt(16));
            generator.generate(this.currentWorld, this.randomGenerator, blockpos);
        }
    }
    
    protected void generateOres() {
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
