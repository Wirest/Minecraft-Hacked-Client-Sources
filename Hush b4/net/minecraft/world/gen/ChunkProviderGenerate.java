// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen;

import java.util.List;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.util.BlockPos;
import net.minecraft.block.BlockFalling;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.util.MathHelper;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.StructureOceanMonument;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.block.Block;
import net.minecraft.world.WorldType;
import net.minecraft.world.World;
import java.util.Random;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderGenerate implements IChunkProvider
{
    private Random rand;
    private NoiseGeneratorOctaves field_147431_j;
    private NoiseGeneratorOctaves field_147432_k;
    private NoiseGeneratorOctaves field_147429_l;
    private NoiseGeneratorPerlin field_147430_m;
    public NoiseGeneratorOctaves noiseGen5;
    public NoiseGeneratorOctaves noiseGen6;
    public NoiseGeneratorOctaves mobSpawnerNoise;
    private World worldObj;
    private final boolean mapFeaturesEnabled;
    private WorldType field_177475_o;
    private final double[] field_147434_q;
    private final float[] parabolicField;
    private ChunkProviderSettings settings;
    private Block field_177476_s;
    private double[] stoneNoise;
    private MapGenBase caveGenerator;
    private MapGenStronghold strongholdGenerator;
    private MapGenVillage villageGenerator;
    private MapGenMineshaft mineshaftGenerator;
    private MapGenScatteredFeature scatteredFeatureGenerator;
    private MapGenBase ravineGenerator;
    private StructureOceanMonument oceanMonumentGenerator;
    private BiomeGenBase[] biomesForGeneration;
    double[] field_147427_d;
    double[] field_147428_e;
    double[] field_147425_f;
    double[] field_147426_g;
    
    public ChunkProviderGenerate(final World worldIn, final long p_i45636_2_, final boolean p_i45636_4_, final String p_i45636_5_) {
        this.field_177476_s = Blocks.water;
        this.stoneNoise = new double[256];
        this.caveGenerator = new MapGenCaves();
        this.strongholdGenerator = new MapGenStronghold();
        this.villageGenerator = new MapGenVillage();
        this.mineshaftGenerator = new MapGenMineshaft();
        this.scatteredFeatureGenerator = new MapGenScatteredFeature();
        this.ravineGenerator = new MapGenRavine();
        this.oceanMonumentGenerator = new StructureOceanMonument();
        this.worldObj = worldIn;
        this.mapFeaturesEnabled = p_i45636_4_;
        this.field_177475_o = worldIn.getWorldInfo().getTerrainType();
        this.rand = new Random(p_i45636_2_);
        this.field_147431_j = new NoiseGeneratorOctaves(this.rand, 16);
        this.field_147432_k = new NoiseGeneratorOctaves(this.rand, 16);
        this.field_147429_l = new NoiseGeneratorOctaves(this.rand, 8);
        this.field_147430_m = new NoiseGeneratorPerlin(this.rand, 4);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
        this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
        this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
        this.field_147434_q = new double[825];
        this.parabolicField = new float[25];
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                final float f = 10.0f / MathHelper.sqrt_float(i * i + j * j + 0.2f);
                this.parabolicField[i + 2 + (j + 2) * 5] = f;
            }
        }
        if (p_i45636_5_ != null) {
            this.settings = ChunkProviderSettings.Factory.jsonToFactory(p_i45636_5_).func_177864_b();
            this.field_177476_s = (this.settings.useLavaOceans ? Blocks.lava : Blocks.water);
            worldIn.func_181544_b(this.settings.seaLevel);
        }
    }
    
    public void setBlocksInChunk(final int p_180518_1_, final int p_180518_2_, final ChunkPrimer p_180518_3_) {
        this.biomesForGeneration = this.worldObj.getWorldChunkManager().getBiomesForGeneration(this.biomesForGeneration, p_180518_1_ * 4 - 2, p_180518_2_ * 4 - 2, 10, 10);
        this.func_147423_a(p_180518_1_ * 4, 0, p_180518_2_ * 4);
        for (int i = 0; i < 4; ++i) {
            final int j = i * 5;
            final int k = (i + 1) * 5;
            for (int l = 0; l < 4; ++l) {
                final int i2 = (j + l) * 33;
                final int j2 = (j + l + 1) * 33;
                final int k2 = (k + l) * 33;
                final int l2 = (k + l + 1) * 33;
                for (int i3 = 0; i3 < 32; ++i3) {
                    final double d0 = 0.125;
                    double d2 = this.field_147434_q[i2 + i3];
                    double d3 = this.field_147434_q[j2 + i3];
                    double d4 = this.field_147434_q[k2 + i3];
                    double d5 = this.field_147434_q[l2 + i3];
                    final double d6 = (this.field_147434_q[i2 + i3 + 1] - d2) * d0;
                    final double d7 = (this.field_147434_q[j2 + i3 + 1] - d3) * d0;
                    final double d8 = (this.field_147434_q[k2 + i3 + 1] - d4) * d0;
                    final double d9 = (this.field_147434_q[l2 + i3 + 1] - d5) * d0;
                    for (int j3 = 0; j3 < 8; ++j3) {
                        final double d10 = 0.25;
                        double d11 = d2;
                        double d12 = d3;
                        final double d13 = (d4 - d2) * d10;
                        final double d14 = (d5 - d3) * d10;
                        for (int k3 = 0; k3 < 4; ++k3) {
                            final double d15 = 0.25;
                            final double d16 = (d12 - d11) * d15;
                            double lvt_45_1_ = d11 - d16;
                            for (int l3 = 0; l3 < 4; ++l3) {
                                if ((lvt_45_1_ += d16) > 0.0) {
                                    p_180518_3_.setBlockState(i * 4 + k3, i3 * 8 + j3, l * 4 + l3, Blocks.stone.getDefaultState());
                                }
                                else if (i3 * 8 + j3 < this.settings.seaLevel) {
                                    p_180518_3_.setBlockState(i * 4 + k3, i3 * 8 + j3, l * 4 + l3, this.field_177476_s.getDefaultState());
                                }
                            }
                            d11 += d13;
                            d12 += d14;
                        }
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                        d5 += d9;
                    }
                }
            }
        }
    }
    
    public void replaceBlocksForBiome(final int p_180517_1_, final int p_180517_2_, final ChunkPrimer p_180517_3_, final BiomeGenBase[] p_180517_4_) {
        final double d0 = 0.03125;
        this.stoneNoise = this.field_147430_m.func_151599_a(this.stoneNoise, p_180517_1_ * 16, p_180517_2_ * 16, 16, 16, d0 * 2.0, d0 * 2.0, 1.0);
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                final BiomeGenBase biomegenbase = p_180517_4_[j + i * 16];
                biomegenbase.genTerrainBlocks(this.worldObj, this.rand, p_180517_3_, p_180517_1_ * 16 + i, p_180517_2_ * 16 + j, this.stoneNoise[j + i * 16]);
            }
        }
    }
    
    @Override
    public Chunk provideChunk(final int x, final int z) {
        this.rand.setSeed(x * 341873128712L + z * 132897987541L);
        final ChunkPrimer chunkprimer = new ChunkPrimer();
        this.setBlocksInChunk(x, z, chunkprimer);
        this.replaceBlocksForBiome(x, z, chunkprimer, this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, x * 16, z * 16, 16, 16));
        if (this.settings.useCaves) {
            this.caveGenerator.generate(this, this.worldObj, x, z, chunkprimer);
        }
        if (this.settings.useRavines) {
            this.ravineGenerator.generate(this, this.worldObj, x, z, chunkprimer);
        }
        if (this.settings.useMineShafts && this.mapFeaturesEnabled) {
            this.mineshaftGenerator.generate(this, this.worldObj, x, z, chunkprimer);
        }
        if (this.settings.useVillages && this.mapFeaturesEnabled) {
            this.villageGenerator.generate(this, this.worldObj, x, z, chunkprimer);
        }
        if (this.settings.useStrongholds && this.mapFeaturesEnabled) {
            this.strongholdGenerator.generate(this, this.worldObj, x, z, chunkprimer);
        }
        if (this.settings.useTemples && this.mapFeaturesEnabled) {
            this.scatteredFeatureGenerator.generate(this, this.worldObj, x, z, chunkprimer);
        }
        if (this.settings.useMonuments && this.mapFeaturesEnabled) {
            this.oceanMonumentGenerator.generate(this, this.worldObj, x, z, chunkprimer);
        }
        final Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);
        final byte[] abyte = chunk.getBiomeArray();
        for (int i = 0; i < abyte.length; ++i) {
            abyte[i] = (byte)this.biomesForGeneration[i].biomeID;
        }
        chunk.generateSkylightMap();
        return chunk;
    }
    
    private void func_147423_a(int p_147423_1_, final int p_147423_2_, int p_147423_3_) {
        this.field_147426_g = this.noiseGen6.generateNoiseOctaves(this.field_147426_g, p_147423_1_, p_147423_3_, 5, 5, this.settings.depthNoiseScaleX, this.settings.depthNoiseScaleZ, this.settings.depthNoiseScaleExponent);
        final float f = this.settings.coordinateScale;
        final float f2 = this.settings.heightScale;
        this.field_147427_d = this.field_147429_l.generateNoiseOctaves(this.field_147427_d, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, f / this.settings.mainNoiseScaleX, f2 / this.settings.mainNoiseScaleY, f / this.settings.mainNoiseScaleZ);
        this.field_147428_e = this.field_147431_j.generateNoiseOctaves(this.field_147428_e, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, f, f2, f);
        this.field_147425_f = this.field_147432_k.generateNoiseOctaves(this.field_147425_f, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, f, f2, f);
        p_147423_3_ = 0;
        p_147423_1_ = 0;
        int i = 0;
        int j = 0;
        for (int k = 0; k < 5; ++k) {
            for (int l = 0; l < 5; ++l) {
                float f3 = 0.0f;
                float f4 = 0.0f;
                float f5 = 0.0f;
                final int i2 = 2;
                final BiomeGenBase biomegenbase = this.biomesForGeneration[k + 2 + (l + 2) * 10];
                for (int j2 = -i2; j2 <= i2; ++j2) {
                    for (int k2 = -i2; k2 <= i2; ++k2) {
                        final BiomeGenBase biomegenbase2 = this.biomesForGeneration[k + j2 + 2 + (l + k2 + 2) * 10];
                        float f6 = this.settings.biomeDepthOffSet + biomegenbase2.minHeight * this.settings.biomeDepthWeight;
                        float f7 = this.settings.biomeScaleOffset + biomegenbase2.maxHeight * this.settings.biomeScaleWeight;
                        if (this.field_177475_o == WorldType.AMPLIFIED && f6 > 0.0f) {
                            f6 = 1.0f + f6 * 2.0f;
                            f7 = 1.0f + f7 * 4.0f;
                        }
                        float f8 = this.parabolicField[j2 + 2 + (k2 + 2) * 5] / (f6 + 2.0f);
                        if (biomegenbase2.minHeight > biomegenbase.minHeight) {
                            f8 /= 2.0f;
                        }
                        f3 += f7 * f8;
                        f4 += f6 * f8;
                        f5 += f8;
                    }
                }
                f3 /= f5;
                f4 /= f5;
                f3 = f3 * 0.9f + 0.1f;
                f4 = (f4 * 4.0f - 1.0f) / 8.0f;
                double d7 = this.field_147426_g[j] / 8000.0;
                if (d7 < 0.0) {
                    d7 = -d7 * 0.3;
                }
                d7 = d7 * 3.0 - 2.0;
                if (d7 < 0.0) {
                    d7 /= 2.0;
                    if (d7 < -1.0) {
                        d7 = -1.0;
                    }
                    d7 /= 1.4;
                    d7 /= 2.0;
                }
                else {
                    if (d7 > 1.0) {
                        d7 = 1.0;
                    }
                    d7 /= 8.0;
                }
                ++j;
                double d8 = f4;
                final double d9 = f3;
                d8 += d7 * 0.2;
                d8 = d8 * this.settings.baseSize / 8.0;
                final double d10 = this.settings.baseSize + d8 * 4.0;
                for (int l2 = 0; l2 < 33; ++l2) {
                    double d11 = (l2 - d10) * this.settings.stretchY * 128.0 / 256.0 / d9;
                    if (d11 < 0.0) {
                        d11 *= 4.0;
                    }
                    final double d12 = this.field_147428_e[i] / this.settings.lowerLimitScale;
                    final double d13 = this.field_147425_f[i] / this.settings.upperLimitScale;
                    final double d14 = (this.field_147427_d[i] / 10.0 + 1.0) / 2.0;
                    double d15 = MathHelper.denormalizeClamp(d12, d13, d14) - d11;
                    if (l2 > 29) {
                        final double d16 = (l2 - 29) / 3.0f;
                        d15 = d15 * (1.0 - d16) + -10.0 * d16;
                    }
                    this.field_147434_q[i] = d15;
                    ++i;
                }
            }
        }
    }
    
    @Override
    public boolean chunkExists(final int x, final int z) {
        return true;
    }
    
    @Override
    public void populate(final IChunkProvider p_73153_1_, final int p_73153_2_, final int p_73153_3_) {
        BlockFalling.fallInstantly = true;
        final int i = p_73153_2_ * 16;
        final int j = p_73153_3_ * 16;
        BlockPos blockpos = new BlockPos(i, 0, j);
        final BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(blockpos.add(16, 0, 16));
        this.rand.setSeed(this.worldObj.getSeed());
        final long k = this.rand.nextLong() / 2L * 2L + 1L;
        final long l = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed(p_73153_2_ * k + p_73153_3_ * l ^ this.worldObj.getSeed());
        boolean flag = false;
        final ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(p_73153_2_, p_73153_3_);
        if (this.settings.useMineShafts && this.mapFeaturesEnabled) {
            this.mineshaftGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
        }
        if (this.settings.useVillages && this.mapFeaturesEnabled) {
            flag = this.villageGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
        }
        if (this.settings.useStrongholds && this.mapFeaturesEnabled) {
            this.strongholdGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
        }
        if (this.settings.useTemples && this.mapFeaturesEnabled) {
            this.scatteredFeatureGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
        }
        if (this.settings.useMonuments && this.mapFeaturesEnabled) {
            this.oceanMonumentGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
        }
        if (biomegenbase != BiomeGenBase.desert && biomegenbase != BiomeGenBase.desertHills && this.settings.useWaterLakes && !flag && this.rand.nextInt(this.settings.waterLakeChance) == 0) {
            final int i2 = this.rand.nextInt(16) + 8;
            final int j2 = this.rand.nextInt(256);
            final int k2 = this.rand.nextInt(16) + 8;
            new WorldGenLakes(Blocks.water).generate(this.worldObj, this.rand, blockpos.add(i2, j2, k2));
        }
        if (!flag && this.rand.nextInt(this.settings.lavaLakeChance / 10) == 0 && this.settings.useLavaLakes) {
            final int i3 = this.rand.nextInt(16) + 8;
            final int l2 = this.rand.nextInt(this.rand.nextInt(248) + 8);
            final int k3 = this.rand.nextInt(16) + 8;
            if (l2 < this.worldObj.func_181545_F() || this.rand.nextInt(this.settings.lavaLakeChance / 8) == 0) {
                new WorldGenLakes(Blocks.lava).generate(this.worldObj, this.rand, blockpos.add(i3, l2, k3));
            }
        }
        if (this.settings.useDungeons) {
            for (int j3 = 0; j3 < this.settings.dungeonChance; ++j3) {
                final int i4 = this.rand.nextInt(16) + 8;
                final int l3 = this.rand.nextInt(256);
                final int l4 = this.rand.nextInt(16) + 8;
                new WorldGenDungeons().generate(this.worldObj, this.rand, blockpos.add(i4, l3, l4));
            }
        }
        biomegenbase.decorate(this.worldObj, this.rand, new BlockPos(i, 0, j));
        SpawnerAnimals.performWorldGenSpawning(this.worldObj, biomegenbase, i + 8, j + 8, 16, 16, this.rand);
        blockpos = blockpos.add(8, 0, 8);
        for (int k4 = 0; k4 < 16; ++k4) {
            for (int j4 = 0; j4 < 16; ++j4) {
                final BlockPos blockpos2 = this.worldObj.getPrecipitationHeight(blockpos.add(k4, 0, j4));
                final BlockPos blockpos3 = blockpos2.down();
                if (this.worldObj.canBlockFreezeWater(blockpos3)) {
                    this.worldObj.setBlockState(blockpos3, Blocks.ice.getDefaultState(), 2);
                }
                if (this.worldObj.canSnowAt(blockpos2, true)) {
                    this.worldObj.setBlockState(blockpos2, Blocks.snow_layer.getDefaultState(), 2);
                }
            }
        }
        BlockFalling.fallInstantly = false;
    }
    
    @Override
    public boolean func_177460_a(final IChunkProvider p_177460_1_, final Chunk p_177460_2_, final int p_177460_3_, final int p_177460_4_) {
        boolean flag = false;
        if (this.settings.useMonuments && this.mapFeaturesEnabled && p_177460_2_.getInhabitedTime() < 3600L) {
            flag |= this.oceanMonumentGenerator.generateStructure(this.worldObj, this.rand, new ChunkCoordIntPair(p_177460_3_, p_177460_4_));
        }
        return flag;
    }
    
    @Override
    public boolean saveChunks(final boolean p_73151_1_, final IProgressUpdate progressCallback) {
        return true;
    }
    
    @Override
    public void saveExtraData() {
    }
    
    @Override
    public boolean unloadQueuedChunks() {
        return false;
    }
    
    @Override
    public boolean canSave() {
        return true;
    }
    
    @Override
    public String makeString() {
        return "RandomLevelSource";
    }
    
    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(final EnumCreatureType creatureType, final BlockPos pos) {
        final BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(pos);
        if (this.mapFeaturesEnabled) {
            if (creatureType == EnumCreatureType.MONSTER && this.scatteredFeatureGenerator.func_175798_a(pos)) {
                return this.scatteredFeatureGenerator.getScatteredFeatureSpawnList();
            }
            if (creatureType == EnumCreatureType.MONSTER && this.settings.useMonuments && this.oceanMonumentGenerator.func_175796_a(this.worldObj, pos)) {
                return this.oceanMonumentGenerator.func_175799_b();
            }
        }
        return biomegenbase.getSpawnableList(creatureType);
    }
    
    @Override
    public BlockPos getStrongholdGen(final World worldIn, final String structureName, final BlockPos position) {
        return ("Stronghold".equals(structureName) && this.strongholdGenerator != null) ? this.strongholdGenerator.getClosestStrongholdPos(worldIn, position) : null;
    }
    
    @Override
    public int getLoadedChunkCount() {
        return 0;
    }
    
    @Override
    public void recreateStructures(final Chunk p_180514_1_, final int p_180514_2_, final int p_180514_3_) {
        if (this.settings.useMineShafts && this.mapFeaturesEnabled) {
            this.mineshaftGenerator.generate(this, this.worldObj, p_180514_2_, p_180514_3_, null);
        }
        if (this.settings.useVillages && this.mapFeaturesEnabled) {
            this.villageGenerator.generate(this, this.worldObj, p_180514_2_, p_180514_3_, null);
        }
        if (this.settings.useStrongholds && this.mapFeaturesEnabled) {
            this.strongholdGenerator.generate(this, this.worldObj, p_180514_2_, p_180514_3_, null);
        }
        if (this.settings.useTemples && this.mapFeaturesEnabled) {
            this.scatteredFeatureGenerator.generate(this, this.worldObj, p_180514_2_, p_180514_3_, null);
        }
        if (this.settings.useMonuments && this.mapFeaturesEnabled) {
            this.oceanMonumentGenerator.generate(this, this.worldObj, p_180514_2_, p_180514_3_, null);
        }
    }
    
    @Override
    public Chunk provideChunk(final BlockPos blockPosIn) {
        return this.provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
    }
}
