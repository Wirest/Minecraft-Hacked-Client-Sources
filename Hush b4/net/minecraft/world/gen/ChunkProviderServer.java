// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.MinecraftException;
import java.io.IOException;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import java.util.Iterator;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.EmptyChunk;
import com.google.common.collect.Lists;
import java.util.Map;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.WorldServer;
import java.util.List;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.chunk.Chunk;
import java.util.Set;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderServer implements IChunkProvider
{
    private static final Logger logger;
    private Set<Long> droppedChunksSet;
    private Chunk dummyChunk;
    private IChunkProvider serverChunkGenerator;
    private IChunkLoader chunkLoader;
    public boolean chunkLoadOverride;
    private LongHashMap id2ChunkMap;
    private List<Chunk> loadedChunks;
    private WorldServer worldObj;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public ChunkProviderServer(final WorldServer p_i1520_1_, final IChunkLoader p_i1520_2_, final IChunkProvider p_i1520_3_) {
        this.droppedChunksSet = Collections.newSetFromMap(new ConcurrentHashMap<Long, Boolean>());
        this.chunkLoadOverride = true;
        this.id2ChunkMap = new LongHashMap();
        this.loadedChunks = (List<Chunk>)Lists.newArrayList();
        this.dummyChunk = new EmptyChunk(p_i1520_1_, 0, 0);
        this.worldObj = p_i1520_1_;
        this.chunkLoader = p_i1520_2_;
        this.serverChunkGenerator = p_i1520_3_;
    }
    
    @Override
    public boolean chunkExists(final int x, final int z) {
        return this.id2ChunkMap.containsItem(ChunkCoordIntPair.chunkXZ2Int(x, z));
    }
    
    public List<Chunk> func_152380_a() {
        return this.loadedChunks;
    }
    
    public void dropChunk(final int p_73241_1_, final int p_73241_2_) {
        if (this.worldObj.provider.canRespawnHere()) {
            if (!this.worldObj.isSpawnChunk(p_73241_1_, p_73241_2_)) {
                this.droppedChunksSet.add(ChunkCoordIntPair.chunkXZ2Int(p_73241_1_, p_73241_2_));
            }
        }
        else {
            this.droppedChunksSet.add(ChunkCoordIntPair.chunkXZ2Int(p_73241_1_, p_73241_2_));
        }
    }
    
    public void unloadAllChunks() {
        for (final Chunk chunk : this.loadedChunks) {
            this.dropChunk(chunk.xPosition, chunk.zPosition);
        }
    }
    
    public Chunk loadChunk(final int p_73158_1_, final int p_73158_2_) {
        final long i = ChunkCoordIntPair.chunkXZ2Int(p_73158_1_, p_73158_2_);
        this.droppedChunksSet.remove(i);
        Chunk chunk = (Chunk)this.id2ChunkMap.getValueByKey(i);
        if (chunk == null) {
            chunk = this.loadChunkFromFile(p_73158_1_, p_73158_2_);
            if (chunk == null) {
                if (this.serverChunkGenerator == null) {
                    chunk = this.dummyChunk;
                }
                else {
                    try {
                        chunk = this.serverChunkGenerator.provideChunk(p_73158_1_, p_73158_2_);
                    }
                    catch (Throwable throwable) {
                        final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception generating new chunk");
                        final CrashReportCategory crashreportcategory = crashreport.makeCategory("Chunk to be generated");
                        crashreportcategory.addCrashSection("Location", String.format("%d,%d", p_73158_1_, p_73158_2_));
                        crashreportcategory.addCrashSection("Position hash", i);
                        crashreportcategory.addCrashSection("Generator", this.serverChunkGenerator.makeString());
                        throw new ReportedException(crashreport);
                    }
                }
            }
            this.id2ChunkMap.add(i, chunk);
            this.loadedChunks.add(chunk);
            chunk.onChunkLoad();
            chunk.populateChunk(this, this, p_73158_1_, p_73158_2_);
        }
        return chunk;
    }
    
    @Override
    public Chunk provideChunk(final int x, final int z) {
        final Chunk chunk = (Chunk)this.id2ChunkMap.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(x, z));
        return (chunk == null) ? ((!this.worldObj.isFindingSpawnPoint() && !this.chunkLoadOverride) ? this.dummyChunk : this.loadChunk(x, z)) : chunk;
    }
    
    private Chunk loadChunkFromFile(final int x, final int z) {
        if (this.chunkLoader == null) {
            return null;
        }
        try {
            final Chunk chunk = this.chunkLoader.loadChunk(this.worldObj, x, z);
            if (chunk != null) {
                chunk.setLastSaveTime(this.worldObj.getTotalWorldTime());
                if (this.serverChunkGenerator != null) {
                    this.serverChunkGenerator.recreateStructures(chunk, x, z);
                }
            }
            return chunk;
        }
        catch (Exception exception) {
            ChunkProviderServer.logger.error("Couldn't load chunk", exception);
            return null;
        }
    }
    
    private void saveChunkExtraData(final Chunk p_73243_1_) {
        if (this.chunkLoader != null) {
            try {
                this.chunkLoader.saveExtraChunkData(this.worldObj, p_73243_1_);
            }
            catch (Exception exception) {
                ChunkProviderServer.logger.error("Couldn't save entities", exception);
            }
        }
    }
    
    private void saveChunkData(final Chunk p_73242_1_) {
        if (this.chunkLoader != null) {
            try {
                p_73242_1_.setLastSaveTime(this.worldObj.getTotalWorldTime());
                this.chunkLoader.saveChunk(this.worldObj, p_73242_1_);
            }
            catch (IOException ioexception) {
                ChunkProviderServer.logger.error("Couldn't save chunk", ioexception);
            }
            catch (MinecraftException minecraftexception) {
                ChunkProviderServer.logger.error("Couldn't save chunk; already in use by another instance of Minecraft?", minecraftexception);
            }
        }
    }
    
    @Override
    public void populate(final IChunkProvider p_73153_1_, final int p_73153_2_, final int p_73153_3_) {
        final Chunk chunk = this.provideChunk(p_73153_2_, p_73153_3_);
        if (!chunk.isTerrainPopulated()) {
            chunk.func_150809_p();
            if (this.serverChunkGenerator != null) {
                this.serverChunkGenerator.populate(p_73153_1_, p_73153_2_, p_73153_3_);
                chunk.setChunkModified();
            }
        }
    }
    
    @Override
    public boolean func_177460_a(final IChunkProvider p_177460_1_, final Chunk p_177460_2_, final int p_177460_3_, final int p_177460_4_) {
        if (this.serverChunkGenerator != null && this.serverChunkGenerator.func_177460_a(p_177460_1_, p_177460_2_, p_177460_3_, p_177460_4_)) {
            final Chunk chunk = this.provideChunk(p_177460_3_, p_177460_4_);
            chunk.setChunkModified();
            return true;
        }
        return false;
    }
    
    @Override
    public boolean saveChunks(final boolean p_73151_1_, final IProgressUpdate progressCallback) {
        int i = 0;
        final List<Chunk> list = (List<Chunk>)Lists.newArrayList((Iterable<?>)this.loadedChunks);
        for (int j = 0; j < list.size(); ++j) {
            final Chunk chunk = list.get(j);
            if (p_73151_1_) {
                this.saveChunkExtraData(chunk);
            }
            if (chunk.needsSaving(p_73151_1_)) {
                this.saveChunkData(chunk);
                chunk.setModified(false);
                if (++i == 24 && !p_73151_1_) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public void saveExtraData() {
        if (this.chunkLoader != null) {
            this.chunkLoader.saveExtraData();
        }
    }
    
    @Override
    public boolean unloadQueuedChunks() {
        if (!this.worldObj.disableLevelSaving) {
            for (int i = 0; i < 100; ++i) {
                if (!this.droppedChunksSet.isEmpty()) {
                    final Long olong = this.droppedChunksSet.iterator().next();
                    final Chunk chunk = (Chunk)this.id2ChunkMap.getValueByKey(olong);
                    if (chunk != null) {
                        chunk.onChunkUnload();
                        this.saveChunkData(chunk);
                        this.saveChunkExtraData(chunk);
                        this.id2ChunkMap.remove(olong);
                        this.loadedChunks.remove(chunk);
                    }
                    this.droppedChunksSet.remove(olong);
                }
            }
            if (this.chunkLoader != null) {
                this.chunkLoader.chunkTick();
            }
        }
        return this.serverChunkGenerator.unloadQueuedChunks();
    }
    
    @Override
    public boolean canSave() {
        return !this.worldObj.disableLevelSaving;
    }
    
    @Override
    public String makeString() {
        return "ServerChunkCache: " + this.id2ChunkMap.getNumHashElements() + " Drop: " + this.droppedChunksSet.size();
    }
    
    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(final EnumCreatureType creatureType, final BlockPos pos) {
        return this.serverChunkGenerator.getPossibleCreatures(creatureType, pos);
    }
    
    @Override
    public BlockPos getStrongholdGen(final World worldIn, final String structureName, final BlockPos position) {
        return this.serverChunkGenerator.getStrongholdGen(worldIn, structureName, position);
    }
    
    @Override
    public int getLoadedChunkCount() {
        return this.id2ChunkMap.getNumHashElements();
    }
    
    @Override
    public void recreateStructures(final Chunk p_180514_1_, final int p_180514_2_, final int p_180514_3_) {
    }
    
    @Override
    public Chunk provideChunk(final BlockPos blockPosIn) {
        return this.provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
    }
}
