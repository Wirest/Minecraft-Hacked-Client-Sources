package net.minecraft.world.gen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureOceanMonument;

public class ChunkGeneratorFlat implements IChunkGenerator
{
    private final World worldObj;
    private final Random random;
    private final IBlockState[] cachedBlockIDs = new IBlockState[256];
    private final FlatGeneratorInfo flatWorldGenInfo;
    private final Map<String, MapGenStructure> structureGenerators = new HashMap<String, MapGenStructure>();
    private final boolean hasDecoration;
    private final boolean hasDungeons;
    private WorldGenLakes waterLakeGenerator;
    private WorldGenLakes lavaLakeGenerator;

    public ChunkGeneratorFlat(World worldIn, long seed, boolean generateStructures, String flatGeneratorSettings)
    {
        this.worldObj = worldIn;
        this.random = new Random(seed);
        this.flatWorldGenInfo = FlatGeneratorInfo.createFlatGeneratorFromString(flatGeneratorSettings);

        if (generateStructures)
        {
            Map<String, Map<String, String>> map = this.flatWorldGenInfo.getWorldFeatures();

            if (map.containsKey("village"))
            {
                Map<String, String> map1 = (Map)map.get("village");

                if (!map1.containsKey("size"))
                {
                    map1.put("size", "1");
                }

                this.structureGenerators.put("Village", new MapGenVillage(map1));
            }

            if (map.containsKey("biome_1"))
            {
                this.structureGenerators.put("Temple", new MapGenScatteredFeature(map.get("biome_1")));
            }

            if (map.containsKey("mineshaft"))
            {
                this.structureGenerators.put("Mineshaft", new MapGenMineshaft(map.get("mineshaft")));
            }

            if (map.containsKey("stronghold"))
            {
                this.structureGenerators.put("Stronghold", new MapGenStronghold(map.get("stronghold")));
            }

            if (map.containsKey("oceanmonument"))
            {
                this.structureGenerators.put("Monument", new StructureOceanMonument(map.get("oceanmonument")));
            }
        }

        if (this.flatWorldGenInfo.getWorldFeatures().containsKey("lake"))
        {
            this.waterLakeGenerator = new WorldGenLakes(Blocks.WATER);
        }

        if (this.flatWorldGenInfo.getWorldFeatures().containsKey("lava_lake"))
        {
            this.lavaLakeGenerator = new WorldGenLakes(Blocks.LAVA);
        }

        this.hasDungeons = this.flatWorldGenInfo.getWorldFeatures().containsKey("dungeon");
        int j = 0;
        int k = 0;
        boolean flag = true;

        for (FlatLayerInfo flatlayerinfo : this.flatWorldGenInfo.getFlatLayers())
        {
            for (int i = flatlayerinfo.getMinY(); i < flatlayerinfo.getMinY() + flatlayerinfo.getLayerCount(); ++i)
            {
                IBlockState iblockstate = flatlayerinfo.getLayerMaterial();

                if (iblockstate.getBlock() != Blocks.AIR)
                {
                    flag = false;
                    this.cachedBlockIDs[i] = iblockstate;
                }
            }

            if (flatlayerinfo.getLayerMaterial().getBlock() == Blocks.AIR)
            {
                k += flatlayerinfo.getLayerCount();
            }
            else
            {
                j += flatlayerinfo.getLayerCount() + k;
                k = 0;
            }
        }

        worldIn.setSeaLevel(j);
        this.hasDecoration = flag && this.flatWorldGenInfo.getBiome() != Biome.getIdForBiome(Biomes.VOID) ? false : this.flatWorldGenInfo.getWorldFeatures().containsKey("decoration");
    }

    public Chunk provideChunk(int x, int z)
    {
        ChunkPrimer chunkprimer = new ChunkPrimer();

        for (int i = 0; i < this.cachedBlockIDs.length; ++i)
        {
            IBlockState iblockstate = this.cachedBlockIDs[i];

            if (iblockstate != null)
            {
                for (int j = 0; j < 16; ++j)
                {
                    for (int k = 0; k < 16; ++k)
                    {
                        chunkprimer.setBlockState(j, i, k, iblockstate);
                    }
                }
            }
        }

        for (MapGenBase mapgenbase : this.structureGenerators.values())
        {
            mapgenbase.generate(this.worldObj, x, z, chunkprimer);
        }

        Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);
        Biome[] abiome = this.worldObj.getBiomeProvider().getBiomes((Biome[])null, x * 16, z * 16, 16, 16);
        byte[] abyte = chunk.getBiomeArray();

        for (int l = 0; l < abyte.length; ++l)
        {
            abyte[l] = (byte)Biome.getIdForBiome(abiome[l]);
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    public void populate(int x, int z)
    {
        int i = x * 16;
        int j = z * 16;
        BlockPos blockpos = new BlockPos(i, 0, j);
        Biome biome = this.worldObj.getBiome(new BlockPos(i + 16, 0, j + 16));
        boolean flag = false;
        this.random.setSeed(this.worldObj.getSeed());
        long k = this.random.nextLong() / 2L * 2L + 1L;
        long l = this.random.nextLong() / 2L * 2L + 1L;
        this.random.setSeed((long)x * k + (long)z * l ^ this.worldObj.getSeed());
        ChunkPos chunkpos = new ChunkPos(x, z);

        for (MapGenStructure mapgenstructure : this.structureGenerators.values())
        {
            boolean flag1 = mapgenstructure.generateStructure(this.worldObj, this.random, chunkpos);

            if (mapgenstructure instanceof MapGenVillage)
            {
                flag |= flag1;
            }
        }

        if (this.waterLakeGenerator != null && !flag && this.random.nextInt(4) == 0)
        {
            this.waterLakeGenerator.generate(this.worldObj, this.random, blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(256), this.random.nextInt(16) + 8));
        }

        if (this.lavaLakeGenerator != null && !flag && this.random.nextInt(8) == 0)
        {
            BlockPos blockpos1 = blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(this.random.nextInt(248) + 8), this.random.nextInt(16) + 8);

            if (blockpos1.getY() < this.worldObj.getSeaLevel() || this.random.nextInt(10) == 0)
            {
                this.lavaLakeGenerator.generate(this.worldObj, this.random, blockpos1);
            }
        }

        if (this.hasDungeons)
        {
            for (int i1 = 0; i1 < 8; ++i1)
            {
                (new WorldGenDungeons()).generate(this.worldObj, this.random, blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(256), this.random.nextInt(16) + 8));
            }
        }

        if (this.hasDecoration)
        {
            biome.decorate(this.worldObj, this.random, blockpos);
        }
    }

    public boolean generateStructures(Chunk chunkIn, int x, int z)
    {
        return false;
    }

    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
    {
        Biome biome = this.worldObj.getBiome(pos);
        return biome.getSpawnableList(creatureType);
    }

    @Nullable
    public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position, boolean p_180513_4_)
    {
        MapGenStructure mapgenstructure = this.structureGenerators.get(structureName);
        return mapgenstructure != null ? mapgenstructure.getClosestStrongholdPos(worldIn, position, p_180513_4_) : null;
    }

    public boolean func_193414_a(World p_193414_1_, String p_193414_2_, BlockPos p_193414_3_)
    {
        MapGenStructure mapgenstructure = this.structureGenerators.get(p_193414_2_);
        return mapgenstructure != null ? mapgenstructure.isInsideStructure(p_193414_3_) : false;
    }

    public void recreateStructures(Chunk chunkIn, int x, int z)
    {
        for (MapGenStructure mapgenstructure : this.structureGenerators.values())
        {
            mapgenstructure.generate(this.worldObj, x, z, (ChunkPrimer)null);
        }
    }
}
