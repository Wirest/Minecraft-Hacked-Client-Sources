// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.World;
import net.minecraft.util.MathHelper;
import net.minecraft.block.BlockFlower;
import net.minecraft.util.BlockPos;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import java.util.Random;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenForest;

public class BiomeGenForest extends BiomeGenBase
{
    private int field_150632_aF;
    protected static final WorldGenForest field_150629_aC;
    protected static final WorldGenForest field_150630_aD;
    protected static final WorldGenCanopyTree field_150631_aE;
    
    static {
        field_150629_aC = new WorldGenForest(false, true);
        field_150630_aD = new WorldGenForest(false, false);
        field_150631_aE = new WorldGenCanopyTree(false);
    }
    
    public BiomeGenForest(final int p_i45377_1_, final int p_i45377_2_) {
        super(p_i45377_1_);
        this.field_150632_aF = p_i45377_2_;
        this.theBiomeDecorator.treesPerChunk = 10;
        this.theBiomeDecorator.grassPerChunk = 2;
        if (this.field_150632_aF == 1) {
            this.theBiomeDecorator.treesPerChunk = 6;
            this.theBiomeDecorator.flowersPerChunk = 100;
            this.theBiomeDecorator.grassPerChunk = 1;
        }
        this.setFillerBlockMetadata(5159473);
        this.setTemperatureRainfall(0.7f, 0.8f);
        if (this.field_150632_aF == 2) {
            this.field_150609_ah = 353825;
            this.color = 3175492;
            this.setTemperatureRainfall(0.6f, 0.6f);
        }
        if (this.field_150632_aF == 0) {
            this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 5, 4, 4));
        }
        if (this.field_150632_aF == 3) {
            this.theBiomeDecorator.treesPerChunk = -999;
        }
    }
    
    @Override
    protected BiomeGenBase func_150557_a(final int p_150557_1_, final boolean p_150557_2_) {
        if (this.field_150632_aF == 2) {
            this.field_150609_ah = 353825;
            this.color = p_150557_1_;
            if (p_150557_2_) {
                this.field_150609_ah = (this.field_150609_ah & 0xFEFEFE) >> 1;
            }
            return this;
        }
        return super.func_150557_a(p_150557_1_, p_150557_2_);
    }
    
    @Override
    public WorldGenAbstractTree genBigTreeChance(final Random rand) {
        return (this.field_150632_aF == 3 && rand.nextInt(3) > 0) ? BiomeGenForest.field_150631_aE : ((this.field_150632_aF != 2 && rand.nextInt(5) != 0) ? this.worldGeneratorTrees : BiomeGenForest.field_150630_aD);
    }
    
    @Override
    public BlockFlower.EnumFlowerType pickRandomFlower(final Random rand, final BlockPos pos) {
        if (this.field_150632_aF == 1) {
            final double d0 = MathHelper.clamp_double((1.0 + BiomeGenForest.GRASS_COLOR_NOISE.func_151601_a(pos.getX() / 48.0, pos.getZ() / 48.0)) / 2.0, 0.0, 0.9999);
            final BlockFlower.EnumFlowerType blockflower$enumflowertype = BlockFlower.EnumFlowerType.values()[(int)(d0 * BlockFlower.EnumFlowerType.values().length)];
            return (blockflower$enumflowertype == BlockFlower.EnumFlowerType.BLUE_ORCHID) ? BlockFlower.EnumFlowerType.POPPY : blockflower$enumflowertype;
        }
        return super.pickRandomFlower(rand, pos);
    }
    
    @Override
    public void decorate(final World worldIn, final Random rand, final BlockPos pos) {
        if (this.field_150632_aF == 3) {
            for (int i = 0; i < 4; ++i) {
                for (int j = 0; j < 4; ++j) {
                    final int k = i * 4 + 1 + 8 + rand.nextInt(3);
                    final int l = j * 4 + 1 + 8 + rand.nextInt(3);
                    final BlockPos blockpos = worldIn.getHeight(pos.add(k, 0, l));
                    if (rand.nextInt(20) == 0) {
                        final WorldGenBigMushroom worldgenbigmushroom = new WorldGenBigMushroom();
                        worldgenbigmushroom.generate(worldIn, rand, blockpos);
                    }
                    else {
                        final WorldGenAbstractTree worldgenabstracttree = this.genBigTreeChance(rand);
                        worldgenabstracttree.func_175904_e();
                        if (worldgenabstracttree.generate(worldIn, rand, blockpos)) {
                            worldgenabstracttree.func_180711_a(worldIn, rand, blockpos);
                        }
                    }
                }
            }
        }
        int j2 = rand.nextInt(5) - 3;
        if (this.field_150632_aF == 1) {
            j2 += 2;
        }
        for (int k2 = 0; k2 < j2; ++k2) {
            final int l2 = rand.nextInt(3);
            if (l2 == 0) {
                BiomeGenForest.DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.SYRINGA);
            }
            else if (l2 == 1) {
                BiomeGenForest.DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.ROSE);
            }
            else if (l2 == 2) {
                BiomeGenForest.DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.PAEONIA);
            }
            for (int i2 = 0; i2 < 5; ++i2) {
                final int j3 = rand.nextInt(16) + 8;
                final int k3 = rand.nextInt(16) + 8;
                final int i3 = rand.nextInt(worldIn.getHeight(pos.add(j3, 0, k3)).getY() + 32);
                if (BiomeGenForest.DOUBLE_PLANT_GENERATOR.generate(worldIn, rand, new BlockPos(pos.getX() + j3, i3, pos.getZ() + k3))) {
                    break;
                }
            }
        }
        super.decorate(worldIn, rand, pos);
    }
    
    @Override
    public int getGrassColorAtPos(final BlockPos pos) {
        final int i = super.getGrassColorAtPos(pos);
        return (this.field_150632_aF == 3) ? ((i & 0xFEFEFE) + 2634762 >> 1) : i;
    }
    
    @Override
    protected BiomeGenBase createMutatedBiome(final int p_180277_1_) {
        if (this.biomeID == BiomeGenBase.forest.biomeID) {
            final BiomeGenForest biomegenforest = new BiomeGenForest(p_180277_1_, 1);
            biomegenforest.setHeight(new Height(this.minHeight, this.maxHeight + 0.2f));
            biomegenforest.setBiomeName("Flower Forest");
            biomegenforest.func_150557_a(6976549, true);
            biomegenforest.setFillerBlockMetadata(8233509);
            return biomegenforest;
        }
        return (this.biomeID != BiomeGenBase.birchForest.biomeID && this.biomeID != BiomeGenBase.birchForestHills.biomeID) ? new BiomeGenMutated(p_180277_1_, this) {
            @Override
            public void decorate(final World worldIn, final Random rand, final BlockPos pos) {
                this.baseBiome.decorate(worldIn, rand, pos);
            }
        } : new BiomeGenMutated(p_180277_1_, this) {
            @Override
            public WorldGenAbstractTree genBigTreeChance(final Random rand) {
                return rand.nextBoolean() ? BiomeGenForest.field_150629_aC : BiomeGenForest.field_150630_aD;
            }
        };
    }
}
