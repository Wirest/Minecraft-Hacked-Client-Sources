package net.minecraft.world.gen;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureOceanMonument;
import net.minecraft.world.gen.structure.WoodlandMansion;

public class ChunkGeneratorOverworld implements IChunkGenerator
{
    protected static final IBlockState STONE = Blocks.STONE.getDefaultState();
    private final Random rand;
    private final NoiseGeneratorOctaves minLimitPerlinNoise;
    private final NoiseGeneratorOctaves maxLimitPerlinNoise;
    private final NoiseGeneratorOctaves mainPerlinNoise;
    private final NoiseGeneratorPerlin surfaceNoise;
    public NoiseGeneratorOctaves scaleNoise;
    public NoiseGeneratorOctaves depthNoise;
    public NoiseGeneratorOctaves forestNoise;
    private final World worldObj;
    private final boolean mapFeaturesEnabled;
    private final WorldType terrainType;
    private final double[] heightMap;
    private final float[] biomeWeights;
    private ChunkGeneratorSettings settings;
    private IBlockState oceanBlock = Blocks.WATER.getDefaultState();
    private double[] depthBuffer = new double[256];
    private final MapGenBase caveGenerator = new MapGenCaves();
    private final MapGenStronghold strongholdGenerator = new MapGenStronghold();
    private final MapGenVillage villageGenerator = new MapGenVillage();
    private final MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();
    private final MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();
    private final MapGenBase ravineGenerator = new MapGenRavine();
    private final StructureOceanMonument oceanMonumentGenerator = new StructureOceanMonument();
    private final WoodlandMansion field_191060_C = new WoodlandMansion(this);
    private Biome[] biomesForGeneration;
    double[] mainNoiseRegion;
    double[] minLimitRegion;
    double[] maxLimitRegion;
    double[] depthRegion;

    public ChunkGeneratorOverworld(World worldIn, long seed, boolean mapFeaturesEnabledIn, String p_i46668_5_)
    {
        this.worldObj = worldIn;
        this.mapFeaturesEnabled = mapFeaturesEnabledIn;
        this.terrainType = worldIn.getWorldInfo().getTerrainType();
        this.rand = new Random(seed);
        this.minLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
        this.maxLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
        this.mainPerlinNoise = new NoiseGeneratorOctaves(this.rand, 8);
        this.surfaceNoise = new NoiseGeneratorPerlin(this.rand, 4);
        this.scaleNoise = new NoiseGeneratorOctaves(this.rand, 10);
        this.depthNoise = new NoiseGeneratorOctaves(this.rand, 16);
        this.forestNoise = new NoiseGeneratorOctaves(this.rand, 8);
        this.heightMap = new double[825];
        this.biomeWeights = new float[25];

        for (int i = -2; i <= 2; ++i)
        {
            for (int j = -2; j <= 2; ++j)
            {
                float f = 10.0F / MathHelper.sqrt((float)(i * i + j * j) + 0.2F);
                this.biomeWeights[i + 2 + (j + 2) * 5] = f;
            }
        }

        if (p_i46668_5_ != null)
        {
            this.settings = ChunkGeneratorSettings.Factory.jsonToFactory(p_i46668_5_).build();
            this.oceanBlock = this.settings.useLavaOceans ? Blocks.LAVA.getDefaultState() : Blocks.WATER.getDefaultState();
            worldIn.setSeaLevel(this.settings.seaLevel);
        }
    }

    public void setBlocksInChunk(int x, int z, ChunkPrimer primer)
    {
        this.biomesForGeneration = this.worldObj.getBiomeProvider().getBiomesForGeneration(this.biomesForGeneration, x * 4 - 2, z * 4 - 2, 10, 10);
        this.generateHeightmap(x * 4, 0, z * 4);

        for (int i = 0; i < 4; ++i)
        {
            int j = i * 5;
            int k = (i + 1) * 5;

            for (int l = 0; l < 4; ++l)
            {
                int i1 = (j + l) * 33;
                int j1 = (j + l + 1) * 33;
                int k1 = (k + l) * 33;
                int l1 = (k + l + 1) * 33;

                for (int i2 = 0; i2 < 32; ++i2)
                {
                    double d0 = 0.125D;
                    double d1 = this.heightMap[i1 + i2];
                    double d2 = this.heightMap[j1 + i2];
                    double d3 = this.heightMap[k1 + i2];
                    double d4 = this.heightMap[l1 + i2];
                    double d5 = (this.heightMap[i1 + i2 + 1] - d1) * 0.125D;
                    double d6 = (this.heightMap[j1 + i2 + 1] - d2) * 0.125D;
                    double d7 = (this.heightMap[k1 + i2 + 1] - d3) * 0.125D;
                    double d8 = (this.heightMap[l1 + i2 + 1] - d4) * 0.125D;

                    for (int j2 = 0; j2 < 8; ++j2)
                    {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * 0.25D;
                        double d13 = (d4 - d2) * 0.25D;

                        for (int k2 = 0; k2 < 4; ++k2)
                        {
                            double d14 = 0.25D;
                            double d16 = (d11 - d10) * 0.25D;
                            double lvt_45_1_ = d10 - d16;

                            for (int l2 = 0; l2 < 4; ++l2)
                            {
                                if ((lvt_45_1_ += d16) > 0.0D)
                                {
                                    primer.setBlockState(i * 4 + k2, i2 * 8 + j2, l * 4 + l2, STONE);
                                }
                                else if (i2 * 8 + j2 < this.settings.seaLevel)
                                {
                                    primer.setBlockState(i * 4 + k2, i2 * 8 + j2, l * 4 + l2, this.oceanBlock);
                                }
                            }

                            d10 += d12;
                            d11 += d13;
                        }

                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }
    }

    public void replaceBiomeBlocks(int x, int z, ChunkPrimer primer, Biome[] biomesIn)
    {
        double d0 = 0.03125D;
        this.depthBuffer = this.surfaceNoise.getRegion(this.depthBuffer, (double)(x * 16), (double)(z * 16), 16, 16, 0.0625D, 0.0625D, 1.0D);

        for (int i = 0; i < 16; ++i)
        {
            for (int j = 0; j < 16; ++j)
            {
                Biome biome = biomesIn[j + i * 16];
                biome.genTerrainBlocks(this.worldObj, this.rand, primer, x * 16 + i, z * 16 + j, this.depthBuffer[j + i * 16]);
            }
        }
    }

    public Chunk provideChunk(int x, int z)
    {
        this.rand.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        this.setBlocksInChunk(x, z, chunkprimer);
        this.biomesForGeneration = this.worldObj.getBiomeProvider().getBiomes(this.biomesForGeneration, x * 16, z * 16, 16, 16);
        this.replaceBiomeBlocks(x, z, chunkprimer, this.biomesForGeneration);

        if (this.settings.useCaves)
        {
            this.caveGenerator.generate(this.worldObj, x, z, chunkprimer);
        }

        if (this.settings.useRavines)
        {
            this.ravineGenerator.generate(this.worldObj, x, z, chunkprimer);
        }

        if (this.mapFeaturesEnabled)
        {
            if (this.settings.useMineShafts)
            {
                this.mineshaftGenerator.generate(this.worldObj, x, z, chunkprimer);
            }

            if (this.settings.useVillages)
            {
                this.villageGenerator.generate(this.worldObj, x, z, chunkprimer);
            }

            if (this.settings.useStrongholds)
            {
                this.strongholdGenerator.generate(this.worldObj, x, z, chunkprimer);
            }

            if (this.settings.useTemples)
            {
                this.scatteredFeatureGenerator.generate(this.worldObj, x, z, chunkprimer);
            }

            if (this.settings.useMonuments)
            {
                this.oceanMonumentGenerator.generate(this.worldObj, x, z, chunkprimer);
            }

            if (this.settings.field_191077_z)
            {
                this.field_191060_C.generate(this.worldObj, x, z, chunkprimer);
            }
        }

        Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);
        byte[] abyte = chunk.getBiomeArray();

        for (int i = 0; i < abyte.length; ++i)
        {
            abyte[i] = (byte)Biome.getIdForBiome(this.biomesForGeneration[i]);
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    private void generateHeightmap(int p_185978_1_, int p_185978_2_, int p_185978_3_)
    {
        this.depthRegion = this.depthNoise.generateNoiseOctaves(this.depthRegion, p_185978_1_, p_185978_3_, 5, 5, (double)this.settings.depthNoiseScaleX, (double)this.settings.depthNoiseScaleZ, (double)this.settings.depthNoiseScaleExponent);
        float f = this.settings.coordinateScale;
        float f1 = this.settings.heightScale;
        this.mainNoiseRegion = this.mainPerlinNoise.generateNoiseOctaves(this.mainNoiseRegion, p_185978_1_, p_185978_2_, p_185978_3_, 5, 33, 5, (double)(f / this.settings.mainNoiseScaleX), (double)(f1 / this.settings.mainNoiseScaleY), (double)(f / this.settings.mainNoiseScaleZ));
        this.minLimitRegion = this.minLimitPerlinNoise.generateNoiseOctaves(this.minLimitRegion, p_185978_1_, p_185978_2_, p_185978_3_, 5, 33, 5, (double)f, (double)f1, (double)f);
        this.maxLimitRegion = this.maxLimitPerlinNoise.generateNoiseOctaves(this.maxLimitRegion, p_185978_1_, p_185978_2_, p_185978_3_, 5, 33, 5, (double)f, (double)f1, (double)f);
        int i = 0;
        int j = 0;

        for (int k = 0; k < 5; ++k)
        {
            for (int l = 0; l < 5; ++l)
            {
                float f2 = 0.0F;
                float f3 = 0.0F;
                float f4 = 0.0F;
                int i1 = 2;
                Biome biome = this.biomesForGeneration[k + 2 + (l + 2) * 10];

                for (int j1 = -2; j1 <= 2; ++j1)
                {
                    for (int k1 = -2; k1 <= 2; ++k1)
                    {
                        Biome biome1 = this.biomesForGeneration[k + j1 + 2 + (l + k1 + 2) * 10];
                        float f5 = this.settings.biomeDepthOffSet + biome1.getBaseHeight() * this.settings.biomeDepthWeight;
                        float f6 = this.settings.biomeScaleOffset + biome1.getHeightVariation() * this.settings.biomeScaleWeight;

                        if (this.terrainType == WorldType.AMPLIFIED && f5 > 0.0F)
                        {
                            f5 = 1.0F + f5 * 2.0F;
                            f6 = 1.0F + f6 * 4.0F;
                        }

                        float f7 = this.biomeWeights[j1 + 2 + (k1 + 2) * 5] / (f5 + 2.0F);

                        if (biome1.getBaseHeight() > biome.getBaseHeight())
                        {
                            f7 /= 2.0F;
                        }

                        f2 += f6 * f7;
                        f3 += f5 * f7;
                        f4 += f7;
                    }
                }

                f2 = f2 / f4;
                f3 = f3 / f4;
                f2 = f2 * 0.9F + 0.1F;
                f3 = (f3 * 4.0F - 1.0F) / 8.0F;
                double d7 = this.depthRegion[j] / 8000.0D;

                if (d7 < 0.0D)
                {
                    d7 = -d7 * 0.3D;
                }

                d7 = d7 * 3.0D - 2.0D;

                if (d7 < 0.0D)
                {
                    d7 = d7 / 2.0D;

                    if (d7 < -1.0D)
                    {
                        d7 = -1.0D;
                    }

                    d7 = d7 / 1.4D;
                    d7 = d7 / 2.0D;
                }
                else
                {
                    if (d7 > 1.0D)
                    {
                        d7 = 1.0D;
                    }

                    d7 = d7 / 8.0D;
                }

                ++j;
                double d8 = (double)f3;
                double d9 = (double)f2;
                d8 = d8 + d7 * 0.2D;
                d8 = d8 * (double)this.settings.baseSize / 8.0D;
                double d0 = (double)this.settings.baseSize + d8 * 4.0D;

                for (int l1 = 0; l1 < 33; ++l1)
                {
                    double d1 = ((double)l1 - d0) * (double)this.settings.stretchY * 128.0D / 256.0D / d9;

                    if (d1 < 0.0D)
                    {
                        d1 *= 4.0D;
                    }

                    double d2 = this.minLimitRegion[i] / (double)this.settings.lowerLimitScale;
                    double d3 = this.maxLimitRegion[i] / (double)this.settings.upperLimitScale;
                    double d4 = (this.mainNoiseRegion[i] / 10.0D + 1.0D) / 2.0D;
                    double d5 = MathHelper.clampedLerp(d2, d3, d4) - d1;

                    if (l1 > 29)
                    {
                        double d6 = (double)((float)(l1 - 29) / 3.0F);
                        d5 = d5 * (1.0D - d6) + -10.0D * d6;
                    }

                    this.heightMap[i] = d5;
                    ++i;
                }
            }
        }
    }

    public void populate(int x, int z)
    {
        BlockFalling.fallInstantly = true;
        int i = x * 16;
        int j = z * 16;
        BlockPos blockpos = new BlockPos(i, 0, j);
        Biome biome = this.worldObj.getBiome(blockpos.add(16, 0, 16));
        this.rand.setSeed(this.worldObj.getSeed());
        long k = this.rand.nextLong() / 2L * 2L + 1L;
        long l = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long)x * k + (long)z * l ^ this.worldObj.getSeed());
        boolean flag = false;
        ChunkPos chunkpos = new ChunkPos(x, z);

        if (this.mapFeaturesEnabled)
        {
            if (this.settings.useMineShafts)
            {
                this.mineshaftGenerator.generateStructure(this.worldObj, this.rand, chunkpos);
            }

            if (this.settings.useVillages)
            {
                flag = this.villageGenerator.generateStructure(this.worldObj, this.rand, chunkpos);
            }

            if (this.settings.useStrongholds)
            {
                this.strongholdGenerator.generateStructure(this.worldObj, this.rand, chunkpos);
            }

            if (this.settings.useTemples)
            {
                this.scatteredFeatureGenerator.generateStructure(this.worldObj, this.rand, chunkpos);
            }

            if (this.settings.useMonuments)
            {
                this.oceanMonumentGenerator.generateStructure(this.worldObj, this.rand, chunkpos);
            }

            if (this.settings.field_191077_z)
            {
                this.field_191060_C.generateStructure(this.worldObj, this.rand, chunkpos);
            }
        }

        if (biome != Biomes.DESERT && biome != Biomes.DESERT_HILLS && this.settings.useWaterLakes && !flag && this.rand.nextInt(this.settings.waterLakeChance) == 0)
        {
            int i1 = this.rand.nextInt(16) + 8;
            int j1 = this.rand.nextInt(256);
            int k1 = this.rand.nextInt(16) + 8;
            (new WorldGenLakes(Blocks.WATER)).generate(this.worldObj, this.rand, blockpos.add(i1, j1, k1));
        }

        if (!flag && this.rand.nextInt(this.settings.lavaLakeChance / 10) == 0 && this.settings.useLavaLakes)
        {
            int i2 = this.rand.nextInt(16) + 8;
            int l2 = this.rand.nextInt(this.rand.nextInt(248) + 8);
            int k3 = this.rand.nextInt(16) + 8;

            if (l2 < this.worldObj.getSeaLevel() || this.rand.nextInt(this.settings.lavaLakeChance / 8) == 0)
            {
                (new WorldGenLakes(Blocks.LAVA)).generate(this.worldObj, this.rand, blockpos.add(i2, l2, k3));
            }
        }

        if (this.settings.useDungeons)
        {
            for (int j2 = 0; j2 < this.settings.dungeonChance; ++j2)
            {
                int i3 = this.rand.nextInt(16) + 8;
                int l3 = this.rand.nextInt(256);
                int l1 = this.rand.nextInt(16) + 8;
                (new WorldGenDungeons()).generate(this.worldObj, this.rand, blockpos.add(i3, l3, l1));
            }
        }

        biome.decorate(this.worldObj, this.rand, new BlockPos(i, 0, j));
        WorldEntitySpawner.performWorldGenSpawning(this.worldObj, biome, i + 8, j + 8, 16, 16, this.rand);
        blockpos = blockpos.add(8, 0, 8);

        for (int k2 = 0; k2 < 16; ++k2)
        {
            for (int j3 = 0; j3 < 16; ++j3)
            {
                BlockPos blockpos1 = this.worldObj.getPrecipitationHeight(blockpos.add(k2, 0, j3));
                BlockPos blockpos2 = blockpos1.down();

                if (this.worldObj.canBlockFreezeWater(blockpos2))
                {
                    this.worldObj.setBlockState(blockpos2, Blocks.ICE.getDefaultState(), 2);
                }

                if (this.worldObj.canSnowAt(blockpos1, true))
                {
                    this.worldObj.setBlockState(blockpos1, Blocks.SNOW_LAYER.getDefaultState(), 2);
                }
            }
        }

        BlockFalling.fallInstantly = false;
    }

    public boolean generateStructures(Chunk chunkIn, int x, int z)
    {
        boolean flag = false;

        if (this.settings.useMonuments && this.mapFeaturesEnabled && chunkIn.getInhabitedTime() < 3600L)
        {
            flag |= this.oceanMonumentGenerator.generateStructure(this.worldObj, this.rand, new ChunkPos(x, z));
        }

        return flag;
    }

    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
    {
        Biome biome = this.worldObj.getBiome(pos);

        if (this.mapFeaturesEnabled)
        {
            if (creatureType == EnumCreatureType.MONSTER && this.scatteredFeatureGenerator.isSwampHut(pos))
            {
                return this.scatteredFeatureGenerator.getScatteredFeatureSpawnList();
            }

            if (creatureType == EnumCreatureType.MONSTER && this.settings.useMonuments && this.oceanMonumentGenerator.isPositionInStructure(this.worldObj, pos))
            {
                return this.oceanMonumentGenerator.getScatteredFeatureSpawnList();
            }
        }

        return biome.getSpawnableList(creatureType);
    }

    public boolean func_193414_a(World p_193414_1_, String p_193414_2_, BlockPos p_193414_3_)
    {
        if (!this.mapFeaturesEnabled)
        {
            return false;
        }
        else if ("Stronghold".equals(p_193414_2_) && this.strongholdGenerator != null)
        {
            return this.strongholdGenerator.isInsideStructure(p_193414_3_);
        }
        else if ("Mansion".equals(p_193414_2_) && this.field_191060_C != null)
        {
            return this.field_191060_C.isInsideStructure(p_193414_3_);
        }
        else if ("Monument".equals(p_193414_2_) && this.oceanMonumentGenerator != null)
        {
            return this.oceanMonumentGenerator.isInsideStructure(p_193414_3_);
        }
        else if ("Village".equals(p_193414_2_) && this.villageGenerator != null)
        {
            return this.villageGenerator.isInsideStructure(p_193414_3_);
        }
        else if ("Mineshaft".equals(p_193414_2_) && this.mineshaftGenerator != null)
        {
            return this.mineshaftGenerator.isInsideStructure(p_193414_3_);
        }
        else
        {
            return "Temple".equals(p_193414_2_) && this.scatteredFeatureGenerator != null ? this.scatteredFeatureGenerator.isInsideStructure(p_193414_3_) : false;
        }
    }

    @Nullable
    public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position, boolean p_180513_4_)
    {
        if (!this.mapFeaturesEnabled)
        {
            return null;
        }
        else if ("Stronghold".equals(structureName) && this.strongholdGenerator != null)
        {
            return this.strongholdGenerator.getClosestStrongholdPos(worldIn, position, p_180513_4_);
        }
        else if ("Mansion".equals(structureName) && this.field_191060_C != null)
        {
            return this.field_191060_C.getClosestStrongholdPos(worldIn, position, p_180513_4_);
        }
        else if ("Monument".equals(structureName) && this.oceanMonumentGenerator != null)
        {
            return this.oceanMonumentGenerator.getClosestStrongholdPos(worldIn, position, p_180513_4_);
        }
        else if ("Village".equals(structureName) && this.villageGenerator != null)
        {
            return this.villageGenerator.getClosestStrongholdPos(worldIn, position, p_180513_4_);
        }
        else if ("Mineshaft".equals(structureName) && this.mineshaftGenerator != null)
        {
            return this.mineshaftGenerator.getClosestStrongholdPos(worldIn, position, p_180513_4_);
        }
        else
        {
            return "Temple".equals(structureName) && this.scatteredFeatureGenerator != null ? this.scatteredFeatureGenerator.getClosestStrongholdPos(worldIn, position, p_180513_4_) : null;
        }
    }

    public void recreateStructures(Chunk chunkIn, int x, int z)
    {
        if (this.mapFeaturesEnabled)
        {
            if (this.settings.useMineShafts)
            {
                this.mineshaftGenerator.generate(this.worldObj, x, z, (ChunkPrimer)null);
            }

            if (this.settings.useVillages)
            {
                this.villageGenerator.generate(this.worldObj, x, z, (ChunkPrimer)null);
            }

            if (this.settings.useStrongholds)
            {
                this.strongholdGenerator.generate(this.worldObj, x, z, (ChunkPrimer)null);
            }

            if (this.settings.useTemples)
            {
                this.scatteredFeatureGenerator.generate(this.worldObj, x, z, (ChunkPrimer)null);
            }

            if (this.settings.useMonuments)
            {
                this.oceanMonumentGenerator.generate(this.worldObj, x, z, (ChunkPrimer)null);
            }

            if (this.settings.field_191077_z)
            {
                this.field_191060_C.generate(this.worldObj, x, z, (ChunkPrimer)null);
            }
        }
    }
}
