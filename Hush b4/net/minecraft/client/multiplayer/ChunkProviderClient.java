// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.multiplayer;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.EnumCreatureType;
import java.util.Iterator;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.EmptyChunk;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.World;
import java.util.List;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderClient implements IChunkProvider
{
    private static final Logger logger;
    private Chunk blankChunk;
    private LongHashMap chunkMapping;
    private List<Chunk> chunkListing;
    private World worldObj;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public ChunkProviderClient(final World worldIn) {
        this.chunkMapping = new LongHashMap();
        this.chunkListing = (List<Chunk>)Lists.newArrayList();
        this.blankChunk = new EmptyChunk(worldIn, 0, 0);
        this.worldObj = worldIn;
    }
    
    @Override
    public boolean chunkExists(final int x, final int z) {
        return true;
    }
    
    public void unloadChunk(final int p_73234_1_, final int p_73234_2_) {
        final Chunk chunk = this.provideChunk(p_73234_1_, p_73234_2_);
        if (!chunk.isEmpty()) {
            chunk.onChunkUnload();
        }
        this.chunkMapping.remove(ChunkCoordIntPair.chunkXZ2Int(p_73234_1_, p_73234_2_));
        this.chunkListing.remove(chunk);
    }
    
    public Chunk loadChunk(final int p_73158_1_, final int p_73158_2_) {
        final Chunk chunk = new Chunk(this.worldObj, p_73158_1_, p_73158_2_);
        this.chunkMapping.add(ChunkCoordIntPair.chunkXZ2Int(p_73158_1_, p_73158_2_), chunk);
        this.chunkListing.add(chunk);
        chunk.setChunkLoaded(true);
        return chunk;
    }
    
    @Override
    public Chunk provideChunk(final int x, final int z) {
        final Chunk chunk = (Chunk)this.chunkMapping.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(x, z));
        return (chunk == null) ? this.blankChunk : chunk;
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
        final long i = System.currentTimeMillis();
        for (final Chunk chunk : this.chunkListing) {
            chunk.func_150804_b(System.currentTimeMillis() - i > 5L);
        }
        if (System.currentTimeMillis() - i > 100L) {
            ChunkProviderClient.logger.info("Warning: Clientside chunk ticking took {} ms", System.currentTimeMillis() - i);
        }
        return false;
    }
    
    @Override
    public boolean canSave() {
        return false;
    }
    
    @Override
    public void populate(final IChunkProvider p_73153_1_, final int p_73153_2_, final int p_73153_3_) {
    }
    
    @Override
    public boolean func_177460_a(final IChunkProvider p_177460_1_, final Chunk p_177460_2_, final int p_177460_3_, final int p_177460_4_) {
        return false;
    }
    
    @Override
    public String makeString() {
        return "MultiplayerChunkCache: " + this.chunkMapping.getNumHashElements() + ", " + this.chunkListing.size();
    }
    
    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(final EnumCreatureType creatureType, final BlockPos pos) {
        return null;
    }
    
    @Override
    public BlockPos getStrongholdGen(final World worldIn, final String structureName, final BlockPos position) {
        return null;
    }
    
    @Override
    public int getLoadedChunkCount() {
        return this.chunkListing.size();
    }
    
    @Override
    public void recreateStructures(final Chunk p_180514_1_, final int p_180514_2_, final int p_180514_3_) {
    }
    
    @Override
    public Chunk provideChunk(final BlockPos blockPosIn) {
        return this.provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
    }
}
