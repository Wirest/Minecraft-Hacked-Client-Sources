// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.chunk.storage;

import net.minecraft.util.BlockPos;
import net.minecraft.entity.EntityList;
import java.util.List;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.nbt.NBTTagList;
import java.io.DataOutputStream;
import java.io.DataOutput;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraft.world.MinecraftException;
import net.minecraft.nbt.NBTBase;
import java.io.IOException;
import java.io.DataInputStream;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.World;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import java.util.Set;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.storage.IThreadedFileIO;

public class AnvilChunkLoader implements IChunkLoader, IThreadedFileIO
{
    private static final Logger logger;
    private Map<ChunkCoordIntPair, NBTTagCompound> chunksToRemove;
    private Set<ChunkCoordIntPair> pendingAnvilChunksCoordinates;
    private final File chunkSaveLocation;
    private boolean field_183014_e;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public AnvilChunkLoader(final File chunkSaveLocationIn) {
        this.chunksToRemove = new ConcurrentHashMap<ChunkCoordIntPair, NBTTagCompound>();
        this.pendingAnvilChunksCoordinates = Collections.newSetFromMap(new ConcurrentHashMap<ChunkCoordIntPair, Boolean>());
        this.field_183014_e = false;
        this.chunkSaveLocation = chunkSaveLocationIn;
    }
    
    @Override
    public Chunk loadChunk(final World worldIn, final int x, final int z) throws IOException {
        final ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(x, z);
        NBTTagCompound nbttagcompound = this.chunksToRemove.get(chunkcoordintpair);
        if (nbttagcompound == null) {
            final DataInputStream datainputstream = RegionFileCache.getChunkInputStream(this.chunkSaveLocation, x, z);
            if (datainputstream == null) {
                return null;
            }
            nbttagcompound = CompressedStreamTools.read(datainputstream);
        }
        return this.checkedReadChunkFromNBT(worldIn, x, z, nbttagcompound);
    }
    
    protected Chunk checkedReadChunkFromNBT(final World worldIn, final int x, final int z, final NBTTagCompound p_75822_4_) {
        if (!p_75822_4_.hasKey("Level", 10)) {
            AnvilChunkLoader.logger.error("Chunk file at " + x + "," + z + " is missing level data, skipping");
            return null;
        }
        final NBTTagCompound nbttagcompound = p_75822_4_.getCompoundTag("Level");
        if (!nbttagcompound.hasKey("Sections", 9)) {
            AnvilChunkLoader.logger.error("Chunk file at " + x + "," + z + " is missing block data, skipping");
            return null;
        }
        Chunk chunk = this.readChunkFromNBT(worldIn, nbttagcompound);
        if (!chunk.isAtLocation(x, z)) {
            AnvilChunkLoader.logger.error("Chunk file at " + x + "," + z + " is in the wrong location; relocating. (Expected " + x + ", " + z + ", got " + chunk.xPosition + ", " + chunk.zPosition + ")");
            nbttagcompound.setInteger("xPos", x);
            nbttagcompound.setInteger("zPos", z);
            chunk = this.readChunkFromNBT(worldIn, nbttagcompound);
        }
        return chunk;
    }
    
    @Override
    public void saveChunk(final World worldIn, final Chunk chunkIn) throws MinecraftException, IOException {
        worldIn.checkSessionLock();
        try {
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
            nbttagcompound.setTag("Level", nbttagcompound2);
            this.writeChunkToNBT(chunkIn, worldIn, nbttagcompound2);
            this.addChunkToPending(chunkIn.getChunkCoordIntPair(), nbttagcompound);
        }
        catch (Exception exception) {
            AnvilChunkLoader.logger.error("Failed to save chunk", exception);
        }
    }
    
    protected void addChunkToPending(final ChunkCoordIntPair p_75824_1_, final NBTTagCompound p_75824_2_) {
        if (!this.pendingAnvilChunksCoordinates.contains(p_75824_1_)) {
            this.chunksToRemove.put(p_75824_1_, p_75824_2_);
        }
        ThreadedFileIOBase.getThreadedIOInstance().queueIO(this);
    }
    
    @Override
    public boolean writeNextIO() {
        if (this.chunksToRemove.isEmpty()) {
            if (this.field_183014_e) {
                AnvilChunkLoader.logger.info("ThreadedAnvilChunkStorage ({}): All chunks are saved", this.chunkSaveLocation.getName());
            }
            return false;
        }
        final ChunkCoordIntPair chunkcoordintpair = this.chunksToRemove.keySet().iterator().next();
        boolean lvt_3_1_;
        try {
            this.pendingAnvilChunksCoordinates.add(chunkcoordintpair);
            final NBTTagCompound nbttagcompound = this.chunksToRemove.remove(chunkcoordintpair);
            if (nbttagcompound != null) {
                try {
                    this.func_183013_b(chunkcoordintpair, nbttagcompound);
                }
                catch (Exception exception) {
                    AnvilChunkLoader.logger.error("Failed to save chunk", exception);
                }
            }
            lvt_3_1_ = true;
        }
        finally {
            this.pendingAnvilChunksCoordinates.remove(chunkcoordintpair);
        }
        this.pendingAnvilChunksCoordinates.remove(chunkcoordintpair);
        return lvt_3_1_;
    }
    
    private void func_183013_b(final ChunkCoordIntPair p_183013_1_, final NBTTagCompound p_183013_2_) throws IOException {
        final DataOutputStream dataoutputstream = RegionFileCache.getChunkOutputStream(this.chunkSaveLocation, p_183013_1_.chunkXPos, p_183013_1_.chunkZPos);
        CompressedStreamTools.write(p_183013_2_, dataoutputstream);
        dataoutputstream.close();
    }
    
    @Override
    public void saveExtraChunkData(final World worldIn, final Chunk chunkIn) throws IOException {
    }
    
    @Override
    public void chunkTick() {
    }
    
    @Override
    public void saveExtraData() {
        try {
            this.field_183014_e = true;
            while (true) {
                if (this.writeNextIO()) {
                    continue;
                }
            }
        }
        finally {
            this.field_183014_e = false;
        }
    }
    
    private void writeChunkToNBT(final Chunk chunkIn, final World worldIn, final NBTTagCompound p_75820_3_) {
        p_75820_3_.setByte("V", (byte)1);
        p_75820_3_.setInteger("xPos", chunkIn.xPosition);
        p_75820_3_.setInteger("zPos", chunkIn.zPosition);
        p_75820_3_.setLong("LastUpdate", worldIn.getTotalWorldTime());
        p_75820_3_.setIntArray("HeightMap", chunkIn.getHeightMap());
        p_75820_3_.setBoolean("TerrainPopulated", chunkIn.isTerrainPopulated());
        p_75820_3_.setBoolean("LightPopulated", chunkIn.isLightPopulated());
        p_75820_3_.setLong("InhabitedTime", chunkIn.getInhabitedTime());
        final ExtendedBlockStorage[] aextendedblockstorage = chunkIn.getBlockStorageArray();
        final NBTTagList nbttaglist = new NBTTagList();
        final boolean flag = !worldIn.provider.getHasNoSky();
        ExtendedBlockStorage[] array;
        for (int length = (array = aextendedblockstorage).length, n = 0; n < length; ++n) {
            final ExtendedBlockStorage extendedblockstorage = array[n];
            if (extendedblockstorage != null) {
                final NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Y", (byte)(extendedblockstorage.getYLocation() >> 4 & 0xFF));
                final byte[] abyte = new byte[extendedblockstorage.getData().length];
                final NibbleArray nibblearray = new NibbleArray();
                NibbleArray nibblearray2 = null;
                for (int i = 0; i < extendedblockstorage.getData().length; ++i) {
                    final char c0 = extendedblockstorage.getData()[i];
                    final int j = i & 0xF;
                    final int k = i >> 8 & 0xF;
                    final int l = i >> 4 & 0xF;
                    if (c0 >> 12 != 0) {
                        if (nibblearray2 == null) {
                            nibblearray2 = new NibbleArray();
                        }
                        nibblearray2.set(j, k, l, c0 >> 12);
                    }
                    abyte[i] = (byte)(c0 >> 4 & 0xFF);
                    nibblearray.set(j, k, l, c0 & '\u000f');
                }
                nbttagcompound.setByteArray("Blocks", abyte);
                nbttagcompound.setByteArray("Data", nibblearray.getData());
                if (nibblearray2 != null) {
                    nbttagcompound.setByteArray("Add", nibblearray2.getData());
                }
                nbttagcompound.setByteArray("BlockLight", extendedblockstorage.getBlocklightArray().getData());
                if (flag) {
                    nbttagcompound.setByteArray("SkyLight", extendedblockstorage.getSkylightArray().getData());
                }
                else {
                    nbttagcompound.setByteArray("SkyLight", new byte[extendedblockstorage.getBlocklightArray().getData().length]);
                }
                nbttaglist.appendTag(nbttagcompound);
            }
        }
        p_75820_3_.setTag("Sections", nbttaglist);
        p_75820_3_.setByteArray("Biomes", chunkIn.getBiomeArray());
        chunkIn.setHasEntities(false);
        final NBTTagList nbttaglist2 = new NBTTagList();
        for (int i2 = 0; i2 < chunkIn.getEntityLists().length; ++i2) {
            for (final Entity entity : chunkIn.getEntityLists()[i2]) {
                final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                if (entity.writeToNBTOptional(nbttagcompound2)) {
                    chunkIn.setHasEntities(true);
                    nbttaglist2.appendTag(nbttagcompound2);
                }
            }
        }
        p_75820_3_.setTag("Entities", nbttaglist2);
        final NBTTagList nbttaglist3 = new NBTTagList();
        for (final TileEntity tileentity : chunkIn.getTileEntityMap().values()) {
            final NBTTagCompound nbttagcompound3 = new NBTTagCompound();
            tileentity.writeToNBT(nbttagcompound3);
            nbttaglist3.appendTag(nbttagcompound3);
        }
        p_75820_3_.setTag("TileEntities", nbttaglist3);
        final List<NextTickListEntry> list = worldIn.getPendingBlockUpdates(chunkIn, false);
        if (list != null) {
            final long j2 = worldIn.getTotalWorldTime();
            final NBTTagList nbttaglist4 = new NBTTagList();
            for (final NextTickListEntry nextticklistentry : list) {
                final NBTTagCompound nbttagcompound4 = new NBTTagCompound();
                final ResourceLocation resourcelocation = Block.blockRegistry.getNameForObject(nextticklistentry.getBlock());
                nbttagcompound4.setString("i", (resourcelocation == null) ? "" : resourcelocation.toString());
                nbttagcompound4.setInteger("x", nextticklistentry.position.getX());
                nbttagcompound4.setInteger("y", nextticklistentry.position.getY());
                nbttagcompound4.setInteger("z", nextticklistentry.position.getZ());
                nbttagcompound4.setInteger("t", (int)(nextticklistentry.scheduledTime - j2));
                nbttagcompound4.setInteger("p", nextticklistentry.priority);
                nbttaglist4.appendTag(nbttagcompound4);
            }
            p_75820_3_.setTag("TileTicks", nbttaglist4);
        }
    }
    
    private Chunk readChunkFromNBT(final World worldIn, final NBTTagCompound p_75823_2_) {
        final int i = p_75823_2_.getInteger("xPos");
        final int j = p_75823_2_.getInteger("zPos");
        final Chunk chunk = new Chunk(worldIn, i, j);
        chunk.setHeightMap(p_75823_2_.getIntArray("HeightMap"));
        chunk.setTerrainPopulated(p_75823_2_.getBoolean("TerrainPopulated"));
        chunk.setLightPopulated(p_75823_2_.getBoolean("LightPopulated"));
        chunk.setInhabitedTime(p_75823_2_.getLong("InhabitedTime"));
        final NBTTagList nbttaglist = p_75823_2_.getTagList("Sections", 10);
        final int k = 16;
        final ExtendedBlockStorage[] aextendedblockstorage = new ExtendedBlockStorage[k];
        final boolean flag = !worldIn.provider.getHasNoSky();
        for (int l = 0; l < nbttaglist.tagCount(); ++l) {
            final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(l);
            final int i2 = nbttagcompound.getByte("Y");
            final ExtendedBlockStorage extendedblockstorage = new ExtendedBlockStorage(i2 << 4, flag);
            final byte[] abyte = nbttagcompound.getByteArray("Blocks");
            final NibbleArray nibblearray = new NibbleArray(nbttagcompound.getByteArray("Data"));
            final NibbleArray nibblearray2 = nbttagcompound.hasKey("Add", 7) ? new NibbleArray(nbttagcompound.getByteArray("Add")) : null;
            final char[] achar = new char[abyte.length];
            for (int j2 = 0; j2 < achar.length; ++j2) {
                final int k2 = j2 & 0xF;
                final int l2 = j2 >> 8 & 0xF;
                final int i3 = j2 >> 4 & 0xF;
                final int j3 = (nibblearray2 != null) ? nibblearray2.get(k2, l2, i3) : 0;
                achar[j2] = (char)(j3 << 12 | (abyte[j2] & 0xFF) << 4 | nibblearray.get(k2, l2, i3));
            }
            extendedblockstorage.setData(achar);
            extendedblockstorage.setBlocklightArray(new NibbleArray(nbttagcompound.getByteArray("BlockLight")));
            if (flag) {
                extendedblockstorage.setSkylightArray(new NibbleArray(nbttagcompound.getByteArray("SkyLight")));
            }
            extendedblockstorage.removeInvalidBlocks();
            aextendedblockstorage[i2] = extendedblockstorage;
        }
        chunk.setStorageArrays(aextendedblockstorage);
        if (p_75823_2_.hasKey("Biomes", 7)) {
            chunk.setBiomeArray(p_75823_2_.getByteArray("Biomes"));
        }
        final NBTTagList nbttaglist2 = p_75823_2_.getTagList("Entities", 10);
        if (nbttaglist2 != null) {
            for (int k3 = 0; k3 < nbttaglist2.tagCount(); ++k3) {
                final NBTTagCompound nbttagcompound2 = nbttaglist2.getCompoundTagAt(k3);
                final Entity entity = EntityList.createEntityFromNBT(nbttagcompound2, worldIn);
                chunk.setHasEntities(true);
                if (entity != null) {
                    chunk.addEntity(entity);
                    Entity entity2 = entity;
                    for (NBTTagCompound nbttagcompound3 = nbttagcompound2; nbttagcompound3.hasKey("Riding", 10); nbttagcompound3 = nbttagcompound3.getCompoundTag("Riding")) {
                        final Entity entity3 = EntityList.createEntityFromNBT(nbttagcompound3.getCompoundTag("Riding"), worldIn);
                        if (entity3 != null) {
                            chunk.addEntity(entity3);
                            entity2.mountEntity(entity3);
                        }
                        entity2 = entity3;
                    }
                }
            }
        }
        final NBTTagList nbttaglist3 = p_75823_2_.getTagList("TileEntities", 10);
        if (nbttaglist3 != null) {
            for (int l3 = 0; l3 < nbttaglist3.tagCount(); ++l3) {
                final NBTTagCompound nbttagcompound4 = nbttaglist3.getCompoundTagAt(l3);
                final TileEntity tileentity = TileEntity.createAndLoadEntity(nbttagcompound4);
                if (tileentity != null) {
                    chunk.addTileEntity(tileentity);
                }
            }
        }
        if (p_75823_2_.hasKey("TileTicks", 9)) {
            final NBTTagList nbttaglist4 = p_75823_2_.getTagList("TileTicks", 10);
            if (nbttaglist4 != null) {
                for (int i4 = 0; i4 < nbttaglist4.tagCount(); ++i4) {
                    final NBTTagCompound nbttagcompound5 = nbttaglist4.getCompoundTagAt(i4);
                    Block block;
                    if (nbttagcompound5.hasKey("i", 8)) {
                        block = Block.getBlockFromName(nbttagcompound5.getString("i"));
                    }
                    else {
                        block = Block.getBlockById(nbttagcompound5.getInteger("i"));
                    }
                    worldIn.scheduleBlockUpdate(new BlockPos(nbttagcompound5.getInteger("x"), nbttagcompound5.getInteger("y"), nbttagcompound5.getInteger("z")), block, nbttagcompound5.getInteger("t"), nbttagcompound5.getInteger("p"));
                }
            }
        }
        return chunk;
    }
}
