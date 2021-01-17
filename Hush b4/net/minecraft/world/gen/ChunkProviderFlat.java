// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.Chunk;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.structure.StructureOceanMonument;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenVillage;
import java.util.Map;
import com.google.common.collect.Lists;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenStructure;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderFlat implements IChunkProvider
{
    private World worldObj;
    private Random random;
    private final IBlockState[] cachedBlockIDs;
    private final FlatGeneratorInfo flatWorldGenInfo;
    private final List<MapGenStructure> structureGenerators;
    private final boolean hasDecoration;
    private final boolean hasDungeons;
    private WorldGenLakes waterLakeGenerator;
    private WorldGenLakes lavaLakeGenerator;
    
    public ChunkProviderFlat(final World worldIn, final long seed, final boolean generateStructures, final String flatGeneratorSettings) {
        this.cachedBlockIDs = new IBlockState[256];
        this.structureGenerators = (List<MapGenStructure>)Lists.newArrayList();
        this.worldObj = worldIn;
        this.random = new Random(seed);
        this.flatWorldGenInfo = FlatGeneratorInfo.createFlatGeneratorFromString(flatGeneratorSettings);
        if (generateStructures) {
            final Map<String, Map<String, String>> map = this.flatWorldGenInfo.getWorldFeatures();
            if (map.containsKey("village")) {
                final Map<String, String> map2 = map.get("village");
                if (!map2.containsKey("size")) {
                    map2.put("size", "1");
                }
                this.structureGenerators.add(new MapGenVillage(map2));
            }
            if (map.containsKey("biome_1")) {
                this.structureGenerators.add(new MapGenScatteredFeature(map.get("biome_1")));
            }
            if (map.containsKey("mineshaft")) {
                this.structureGenerators.add(new MapGenMineshaft(map.get("mineshaft")));
            }
            if (map.containsKey("stronghold")) {
                this.structureGenerators.add(new MapGenStronghold(map.get("stronghold")));
            }
            if (map.containsKey("oceanmonument")) {
                this.structureGenerators.add(new StructureOceanMonument(map.get("oceanmonument")));
            }
        }
        if (this.flatWorldGenInfo.getWorldFeatures().containsKey("lake")) {
            this.waterLakeGenerator = new WorldGenLakes(Blocks.water);
        }
        if (this.flatWorldGenInfo.getWorldFeatures().containsKey("lava_lake")) {
            this.lavaLakeGenerator = new WorldGenLakes(Blocks.lava);
        }
        this.hasDungeons = this.flatWorldGenInfo.getWorldFeatures().containsKey("dungeon");
        int j = 0;
        int k = 0;
        boolean flag = true;
        for (final FlatLayerInfo flatlayerinfo : this.flatWorldGenInfo.getFlatLayers()) {
            for (int i = flatlayerinfo.getMinY(); i < flatlayerinfo.getMinY() + flatlayerinfo.getLayerCount(); ++i) {
                final IBlockState iblockstate = flatlayerinfo.func_175900_c();
                if (iblockstate.getBlock() != Blocks.air) {
                    flag = false;
                    this.cachedBlockIDs[i] = iblockstate;
                }
            }
            if (flatlayerinfo.func_175900_c().getBlock() == Blocks.air) {
                k += flatlayerinfo.getLayerCount();
            }
            else {
                j += flatlayerinfo.getLayerCount() + k;
                k = 0;
            }
        }
        worldIn.func_181544_b(j);
        this.hasDecoration = (!flag && this.flatWorldGenInfo.getWorldFeatures().containsKey("decoration"));
    }
    
    @Override
    public Chunk provideChunk(final int x, final int z) {
        final ChunkPrimer chunkprimer = new ChunkPrimer();
        for (int i = 0; i < this.cachedBlockIDs.length; ++i) {
            final IBlockState iblockstate = this.cachedBlockIDs[i];
            if (iblockstate != null) {
                for (int j = 0; j < 16; ++j) {
                    for (int k = 0; k < 16; ++k) {
                        chunkprimer.setBlockState(j, i, k, iblockstate);
                    }
                }
            }
        }
        for (final MapGenBase mapgenbase : this.structureGenerators) {
            mapgenbase.generate(this, this.worldObj, x, z, chunkprimer);
        }
        final Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);
        final BiomeGenBase[] abiomegenbase = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(null, x * 16, z * 16, 16, 16);
        final byte[] abyte = chunk.getBiomeArray();
        for (int l = 0; l < abyte.length; ++l) {
            abyte[l] = (byte)abiomegenbase[l].biomeID;
        }
        chunk.generateSkylightMap();
        return chunk;
    }
    
    @Override
    public boolean chunkExists(final int x, final int z) {
        return true;
    }
    
    @Override
    public void populate(final IChunkProvider p_73153_1_, final int p_73153_2_, final int p_73153_3_) {
        final int i = p_73153_2_ * 16;
        final int j = p_73153_3_ * 16;
        final BlockPos blockpos = new BlockPos(i, 0, j);
        final BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(new BlockPos(i + 16, 0, j + 16));
        boolean flag = false;
        this.random.setSeed(this.worldObj.getSeed());
        final long k = this.random.nextLong() / 2L * 2L + 1L;
        final long l = this.random.nextLong() / 2L * 2L + 1L;
        this.random.setSeed(p_73153_2_ * k + p_73153_3_ * l ^ this.worldObj.getSeed());
        final ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(p_73153_2_, p_73153_3_);
        for (final MapGenStructure mapgenstructure : this.structureGenerators) {
            final boolean flag2 = mapgenstructure.generateStructure(this.worldObj, this.random, chunkcoordintpair);
            if (mapgenstructure instanceof MapGenVillage) {
                flag |= flag2;
            }
        }
        if (this.waterLakeGenerator != null && !flag && this.random.nextInt(4) == 0) {
            this.waterLakeGenerator.generate(this.worldObj, this.random, blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(256), this.random.nextInt(16) + 8));
        }
        if (this.lavaLakeGenerator != null && !flag && this.random.nextInt(8) == 0) {
            final BlockPos blockpos2 = blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(this.random.nextInt(248) + 8), this.random.nextInt(16) + 8);
            if (blockpos2.getY() < this.worldObj.func_181545_F() || this.random.nextInt(10) == 0) {
                this.lavaLakeGenerator.generate(this.worldObj, this.random, blockpos2);
            }
        }
        if (this.hasDungeons) {
            for (int i2 = 0; i2 < 8; ++i2) {
                new WorldGenDungeons().generate(this.worldObj, this.random, blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(256), this.random.nextInt(16) + 8));
            }
        }
        if (this.hasDecoration) {
            biomegenbase.decorate(this.worldObj, this.random, blockpos);
        }
    }
    
    @Override
    public boolean func_177460_a(final IChunkProvider p_177460_1_, final Chunk p_177460_2_, final int p_177460_3_, final int p_177460_4_) {
        return false;
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
        return "FlatLevelSource";
    }
    
    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(final EnumCreatureType creatureType, final BlockPos pos) {
        final BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(pos);
        return biomegenbase.getSpawnableList(creatureType);
    }
    
    @Override
    public BlockPos getStrongholdGen(final World worldIn, final String structureName, final BlockPos position) {
        if ("Stronghold".equals(structureName)) {
            for (final MapGenStructure mapgenstructure : this.structureGenerators) {
                if (mapgenstructure instanceof MapGenStronghold) {
                    return mapgenstructure.getClosestStrongholdPos(worldIn, position);
                }
            }
        }
        return null;
    }
    
    @Override
    public int getLoadedChunkCount() {
        return 0;
    }
    
    @Override
    public void recreateStructures(final Chunk p_180514_1_, final int p_180514_2_, final int p_180514_3_) {
        for (final MapGenStructure mapgenstructure : this.structureGenerators) {
            mapgenstructure.generate(this, this.worldObj, p_180514_2_, p_180514_3_, null);
        }
    }
    
    @Override
    public Chunk provideChunk(final BlockPos blockPosIn) {
        return this.provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
    }
}
