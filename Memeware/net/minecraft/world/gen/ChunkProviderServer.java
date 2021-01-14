package net.minecraft.world.gen;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChunkProviderServer implements IChunkProvider {
    private static final Logger logger = LogManager.getLogger();
    private Set droppedChunksSet = Collections.newSetFromMap(new ConcurrentHashMap());

    /**
     * a dummy chunk, returned in place of an actual chunk.
     */
    private Chunk dummyChunk;

    /**
     * chunk generator object. Calls to load nonexistent chunks are forwarded to this object.
     */
    private IChunkProvider serverChunkGenerator;
    private IChunkLoader chunkLoader;

    /**
     * if set, this flag forces a request to load a chunk to load the chunk rather than defaulting to the dummy if
     * possible
     */
    public boolean chunkLoadOverride = true;

    /**
     * map of chunk Id's to Chunk instances
     */
    private LongHashMap id2ChunkMap = new LongHashMap();
    private List loadedChunks = Lists.newArrayList();
    private WorldServer worldObj;
    private static final String __OBFID = "CL_00001436";

    public ChunkProviderServer(WorldServer p_i1520_1_, IChunkLoader p_i1520_2_, IChunkProvider p_i1520_3_) {
        this.dummyChunk = new EmptyChunk(p_i1520_1_, 0, 0);
        this.worldObj = p_i1520_1_;
        this.chunkLoader = p_i1520_2_;
        this.serverChunkGenerator = p_i1520_3_;
    }

    /**
     * Checks to see if a chunk exists at x, y
     */
    public boolean chunkExists(int p_73149_1_, int p_73149_2_) {
        return this.id2ChunkMap.containsItem(ChunkCoordIntPair.chunkXZ2Int(p_73149_1_, p_73149_2_));
    }

    public List func_152380_a() {
        return this.loadedChunks;
    }

    public void dropChunk(int p_73241_1_, int p_73241_2_) {
        if (this.worldObj.provider.canRespawnHere()) {
            if (!this.worldObj.chunkExists(p_73241_1_, p_73241_2_)) {
                this.droppedChunksSet.add(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(p_73241_1_, p_73241_2_)));
            }
        } else {
            this.droppedChunksSet.add(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(p_73241_1_, p_73241_2_)));
        }
    }

    /**
     * marks all chunks for unload, ignoring those near the spawn
     */
    public void unloadAllChunks() {
        Iterator var1 = this.loadedChunks.iterator();

        while (var1.hasNext()) {
            Chunk var2 = (Chunk) var1.next();
            this.dropChunk(var2.xPosition, var2.zPosition);
        }
    }

    /**
     * loads or generates the chunk at the chunk location specified
     */
    public Chunk loadChunk(int p_73158_1_, int p_73158_2_) {
        long var3 = ChunkCoordIntPair.chunkXZ2Int(p_73158_1_, p_73158_2_);
        this.droppedChunksSet.remove(Long.valueOf(var3));
        Chunk var5 = (Chunk) this.id2ChunkMap.getValueByKey(var3);

        if (var5 == null) {
            var5 = this.loadChunkFromFile(p_73158_1_, p_73158_2_);

            if (var5 == null) {
                if (this.serverChunkGenerator == null) {
                    var5 = this.dummyChunk;
                } else {
                    try {
                        var5 = this.serverChunkGenerator.provideChunk(p_73158_1_, p_73158_2_);
                    } catch (Throwable var9) {
                        CrashReport var7 = CrashReport.makeCrashReport(var9, "Exception generating new chunk");
                        CrashReportCategory var8 = var7.makeCategory("Chunk to be generated");
                        var8.addCrashSection("Location", String.format("%d,%d", new Object[]{Integer.valueOf(p_73158_1_), Integer.valueOf(p_73158_2_)}));
                        var8.addCrashSection("Position hash", Long.valueOf(var3));
                        var8.addCrashSection("Generator", this.serverChunkGenerator.makeString());
                        throw new ReportedException(var7);
                    }
                }
            }

            this.id2ChunkMap.add(var3, var5);
            this.loadedChunks.add(var5);
            var5.onChunkLoad();
            var5.populateChunk(this, this, p_73158_1_, p_73158_2_);
        }

        return var5;
    }

    /**
     * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
     * specified chunk from the map seed and chunk seed
     */
    public Chunk provideChunk(int p_73154_1_, int p_73154_2_) {
        Chunk var3 = (Chunk) this.id2ChunkMap.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(p_73154_1_, p_73154_2_));
        return var3 == null ? (!this.worldObj.isFindingSpawnPoint() && !this.chunkLoadOverride ? this.dummyChunk : this.loadChunk(p_73154_1_, p_73154_2_)) : var3;
    }

    private Chunk loadChunkFromFile(int p_73239_1_, int p_73239_2_) {
        if (this.chunkLoader == null) {
            return null;
        } else {
            try {
                Chunk var3 = this.chunkLoader.loadChunk(this.worldObj, p_73239_1_, p_73239_2_);

                if (var3 != null) {
                    var3.setLastSaveTime(this.worldObj.getTotalWorldTime());

                    if (this.serverChunkGenerator != null) {
                        this.serverChunkGenerator.func_180514_a(var3, p_73239_1_, p_73239_2_);
                    }
                }

                return var3;
            } catch (Exception var4) {
                logger.error("Couldn\'t load chunk", var4);
                return null;
            }
        }
    }

    private void saveChunkExtraData(Chunk p_73243_1_) {
        if (this.chunkLoader != null) {
            try {
                this.chunkLoader.saveExtraChunkData(this.worldObj, p_73243_1_);
            } catch (Exception var3) {
                logger.error("Couldn\'t save entities", var3);
            }
        }
    }

    private void saveChunkData(Chunk p_73242_1_) {
        if (this.chunkLoader != null) {
            try {
                p_73242_1_.setLastSaveTime(this.worldObj.getTotalWorldTime());
                this.chunkLoader.saveChunk(this.worldObj, p_73242_1_);
            } catch (IOException var3) {
                logger.error("Couldn\'t save chunk", var3);
            } catch (MinecraftException var4) {
                logger.error("Couldn\'t save chunk; already in use by another instance of Minecraft?", var4);
            }
        }
    }

    /**
     * Populates chunk with ores etc etc
     */
    public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
        Chunk var4 = this.provideChunk(p_73153_2_, p_73153_3_);

        if (!var4.isTerrainPopulated()) {
            var4.func_150809_p();

            if (this.serverChunkGenerator != null) {
                this.serverChunkGenerator.populate(p_73153_1_, p_73153_2_, p_73153_3_);
                var4.setChunkModified();
            }
        }
    }

    public boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_) {
        if (this.serverChunkGenerator != null && this.serverChunkGenerator.func_177460_a(p_177460_1_, p_177460_2_, p_177460_3_, p_177460_4_)) {
            Chunk var5 = this.provideChunk(p_177460_3_, p_177460_4_);
            var5.setChunkModified();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Two modes of operation: if passed true, save all Chunks in one go.  If passed false, save up to two chunks.
     * Return true if all chunks have been saved.
     */
    public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_) {
        int var3 = 0;

        for (int var4 = 0; var4 < this.loadedChunks.size(); ++var4) {
            Chunk var5 = (Chunk) this.loadedChunks.get(var4);

            if (p_73151_1_) {
                this.saveChunkExtraData(var5);
            }

            if (var5.needsSaving(p_73151_1_)) {
                this.saveChunkData(var5);
                var5.setModified(false);
                ++var3;

                if (var3 == 24 && !p_73151_1_) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Save extra data not associated with any Chunk.  Not saved during autosave, only during world unload.  Currently
     * unimplemented.
     */
    public void saveExtraData() {
        if (this.chunkLoader != null) {
            this.chunkLoader.saveExtraData();
        }
    }

    /**
     * Unloads chunks that are marked to be unloaded. This is not guaranteed to unload every such chunk.
     */
    public boolean unloadQueuedChunks() {
        if (!this.worldObj.disableLevelSaving) {
            for (int var1 = 0; var1 < 100; ++var1) {
                if (!this.droppedChunksSet.isEmpty()) {
                    Long var2 = (Long) this.droppedChunksSet.iterator().next();
                    Chunk var3 = (Chunk) this.id2ChunkMap.getValueByKey(var2.longValue());

                    if (var3 != null) {
                        var3.onChunkUnload();
                        this.saveChunkData(var3);
                        this.saveChunkExtraData(var3);
                        this.id2ChunkMap.remove(var2.longValue());
                        this.loadedChunks.remove(var3);
                    }

                    this.droppedChunksSet.remove(var2);
                }
            }

            if (this.chunkLoader != null) {
                this.chunkLoader.chunkTick();
            }
        }

        return this.serverChunkGenerator.unloadQueuedChunks();
    }

    /**
     * Returns if the IChunkProvider supports saving.
     */
    public boolean canSave() {
        return !this.worldObj.disableLevelSaving;
    }

    /**
     * Converts the instance data to a readable string.
     */
    public String makeString() {
        return "ServerChunkCache: " + this.id2ChunkMap.getNumHashElements() + " Drop: " + this.droppedChunksSet.size();
    }

    public List func_177458_a(EnumCreatureType p_177458_1_, BlockPos p_177458_2_) {
        return this.serverChunkGenerator.func_177458_a(p_177458_1_, p_177458_2_);
    }

    public BlockPos func_180513_a(World worldIn, String p_180513_2_, BlockPos p_180513_3_) {
        return this.serverChunkGenerator.func_180513_a(worldIn, p_180513_2_, p_180513_3_);
    }

    public int getLoadedChunkCount() {
        return this.id2ChunkMap.getNumHashElements();
    }

    public void func_180514_a(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_) {
    }

    public Chunk func_177459_a(BlockPos p_177459_1_) {
        return this.provideChunk(p_177459_1_.getX() >> 4, p_177459_1_.getZ() >> 4);
    }
}
