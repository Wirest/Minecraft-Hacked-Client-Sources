// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.chunk;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.ChunkCoordIntPair;
import java.util.Random;
import com.google.common.base.Predicate;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import java.util.Collection;
import net.minecraft.util.MathHelper;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.world.gen.ChunkProviderDebug;
import net.minecraft.world.WorldType;
import net.minecraft.crash.CrashReportCategory;
import java.util.concurrent.Callable;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.init.Blocks;
import net.minecraft.world.EnumSkyBlock;
import java.util.Iterator;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import java.util.Arrays;
import com.google.common.collect.Queues;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.entity.Entity;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import java.util.Map;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import org.apache.logging.log4j.Logger;

public class Chunk
{
    private static final Logger logger;
    private final ExtendedBlockStorage[] storageArrays;
    private final byte[] blockBiomeArray;
    private final int[] precipitationHeightMap;
    private final boolean[] updateSkylightColumns;
    private boolean isChunkLoaded;
    private final World worldObj;
    private final int[] heightMap;
    public final int xPosition;
    public final int zPosition;
    private boolean isGapLightingUpdated;
    private final Map<BlockPos, TileEntity> chunkTileEntityMap;
    private final ClassInheritanceMultiMap<Entity>[] entityLists;
    private boolean isTerrainPopulated;
    private boolean isLightPopulated;
    private boolean field_150815_m;
    private boolean isModified;
    private boolean hasEntities;
    private long lastSaveTime;
    private int heightMapMinimum;
    private long inhabitedTime;
    private int queuedLightChecks;
    private ConcurrentLinkedQueue<BlockPos> tileEntityPosQueue;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public Chunk(final World worldIn, final int x, final int z) {
        this.storageArrays = new ExtendedBlockStorage[16];
        this.blockBiomeArray = new byte[256];
        this.precipitationHeightMap = new int[256];
        this.updateSkylightColumns = new boolean[256];
        this.chunkTileEntityMap = (Map<BlockPos, TileEntity>)Maps.newHashMap();
        this.queuedLightChecks = 4096;
        this.tileEntityPosQueue = Queues.newConcurrentLinkedQueue();
        this.entityLists = (ClassInheritanceMultiMap<Entity>[])new ClassInheritanceMultiMap[16];
        this.worldObj = worldIn;
        this.xPosition = x;
        this.zPosition = z;
        this.heightMap = new int[256];
        for (int i = 0; i < this.entityLists.length; ++i) {
            this.entityLists[i] = new ClassInheritanceMultiMap<Entity>(Entity.class);
        }
        Arrays.fill(this.precipitationHeightMap, -999);
        Arrays.fill(this.blockBiomeArray, (byte)(-1));
    }
    
    public Chunk(final World worldIn, final ChunkPrimer primer, final int x, final int z) {
        this(worldIn, x, z);
        final int i = 256;
        final boolean flag = !worldIn.provider.getHasNoSky();
        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                for (int l = 0; l < i; ++l) {
                    final int i2 = j * i * 16 | k * i | l;
                    final IBlockState iblockstate = primer.getBlockState(i2);
                    if (iblockstate.getBlock().getMaterial() != Material.air) {
                        final int j2 = l >> 4;
                        if (this.storageArrays[j2] == null) {
                            this.storageArrays[j2] = new ExtendedBlockStorage(j2 << 4, flag);
                        }
                        this.storageArrays[j2].set(j, l & 0xF, k, iblockstate);
                    }
                }
            }
        }
    }
    
    public boolean isAtLocation(final int x, final int z) {
        return x == this.xPosition && z == this.zPosition;
    }
    
    public int getHeight(final BlockPos pos) {
        return this.getHeightValue(pos.getX() & 0xF, pos.getZ() & 0xF);
    }
    
    public int getHeightValue(final int x, final int z) {
        return this.heightMap[z << 4 | x];
    }
    
    public int getTopFilledSegment() {
        for (int i = this.storageArrays.length - 1; i >= 0; --i) {
            if (this.storageArrays[i] != null) {
                return this.storageArrays[i].getYLocation();
            }
        }
        return 0;
    }
    
    public ExtendedBlockStorage[] getBlockStorageArray() {
        return this.storageArrays;
    }
    
    protected void generateHeightMap() {
        final int i = this.getTopFilledSegment();
        this.heightMapMinimum = Integer.MAX_VALUE;
        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                this.precipitationHeightMap[j + (k << 4)] = -999;
                int l = i + 16;
                while (l > 0) {
                    final Block block = this.getBlock0(j, l - 1, k);
                    if (block.getLightOpacity() != 0) {
                        if ((this.heightMap[k << 4 | j] = l) < this.heightMapMinimum) {
                            this.heightMapMinimum = l;
                            break;
                        }
                        break;
                    }
                    else {
                        --l;
                    }
                }
            }
        }
        this.isModified = true;
    }
    
    public void generateSkylightMap() {
        final int i = this.getTopFilledSegment();
        this.heightMapMinimum = Integer.MAX_VALUE;
        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                this.precipitationHeightMap[j + (k << 4)] = -999;
                int l = i + 16;
                while (l > 0) {
                    if (this.getBlockLightOpacity(j, l - 1, k) != 0) {
                        if ((this.heightMap[k << 4 | j] = l) < this.heightMapMinimum) {
                            this.heightMapMinimum = l;
                            break;
                        }
                        break;
                    }
                    else {
                        --l;
                    }
                }
                if (!this.worldObj.provider.getHasNoSky()) {
                    int k2 = 15;
                    int i2 = i + 16 - 1;
                    do {
                        int j2 = this.getBlockLightOpacity(j, i2, k);
                        if (j2 == 0 && k2 != 15) {
                            j2 = 1;
                        }
                        k2 -= j2;
                        if (k2 > 0) {
                            final ExtendedBlockStorage extendedblockstorage = this.storageArrays[i2 >> 4];
                            if (extendedblockstorage == null) {
                                continue;
                            }
                            extendedblockstorage.setExtSkylightValue(j, i2 & 0xF, k, k2);
                            this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + j, i2, (this.zPosition << 4) + k));
                        }
                    } while (--i2 > 0 && k2 > 0);
                }
            }
        }
        this.isModified = true;
    }
    
    private void propagateSkylightOcclusion(final int x, final int z) {
        this.updateSkylightColumns[x + z * 16] = true;
        this.isGapLightingUpdated = true;
    }
    
    private void recheckGaps(final boolean p_150803_1_) {
        this.worldObj.theProfiler.startSection("recheckGaps");
        if (this.worldObj.isAreaLoaded(new BlockPos(this.xPosition * 16 + 8, 0, this.zPosition * 16 + 8), 16)) {
            for (int i = 0; i < 16; ++i) {
                for (int j = 0; j < 16; ++j) {
                    if (this.updateSkylightColumns[i + j * 16]) {
                        this.updateSkylightColumns[i + j * 16] = false;
                        final int k = this.getHeightValue(i, j);
                        final int l = this.xPosition * 16 + i;
                        final int i2 = this.zPosition * 16 + j;
                        int j2 = Integer.MAX_VALUE;
                        for (final Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
                            j2 = Math.min(j2, this.worldObj.getChunksLowestHorizon(l + ((EnumFacing)enumfacing).getFrontOffsetX(), i2 + ((EnumFacing)enumfacing).getFrontOffsetZ()));
                        }
                        this.checkSkylightNeighborHeight(l, i2, j2);
                        for (final Object enumfacing2 : EnumFacing.Plane.HORIZONTAL) {
                            this.checkSkylightNeighborHeight(l + ((EnumFacing)enumfacing2).getFrontOffsetX(), i2 + ((EnumFacing)enumfacing2).getFrontOffsetZ(), k);
                        }
                        if (p_150803_1_) {
                            this.worldObj.theProfiler.endSection();
                            return;
                        }
                    }
                }
            }
            this.isGapLightingUpdated = false;
        }
        this.worldObj.theProfiler.endSection();
    }
    
    private void checkSkylightNeighborHeight(final int x, final int z, final int maxValue) {
        final int i = this.worldObj.getHeight(new BlockPos(x, 0, z)).getY();
        if (i > maxValue) {
            this.updateSkylightNeighborHeight(x, z, maxValue, i + 1);
        }
        else if (i < maxValue) {
            this.updateSkylightNeighborHeight(x, z, i, maxValue + 1);
        }
    }
    
    private void updateSkylightNeighborHeight(final int x, final int z, final int startY, final int endY) {
        if (endY > startY && this.worldObj.isAreaLoaded(new BlockPos(x, 0, z), 16)) {
            for (int i = startY; i < endY; ++i) {
                this.worldObj.checkLightFor(EnumSkyBlock.SKY, new BlockPos(x, i, z));
            }
            this.isModified = true;
        }
    }
    
    private void relightBlock(final int x, final int y, final int z) {
        int j;
        final int i = j = (this.heightMap[z << 4 | x] & 0xFF);
        if (y > i) {
            j = y;
        }
        while (j > 0 && this.getBlockLightOpacity(x, j - 1, z) == 0) {
            --j;
        }
        if (j != i) {
            this.worldObj.markBlocksDirtyVertical(x + this.xPosition * 16, z + this.zPosition * 16, j, i);
            this.heightMap[z << 4 | x] = j;
            final int k = this.xPosition * 16 + x;
            final int l = this.zPosition * 16 + z;
            if (!this.worldObj.provider.getHasNoSky()) {
                if (j < i) {
                    for (int j2 = j; j2 < i; ++j2) {
                        final ExtendedBlockStorage extendedblockstorage2 = this.storageArrays[j2 >> 4];
                        if (extendedblockstorage2 != null) {
                            extendedblockstorage2.setExtSkylightValue(x, j2 & 0xF, z, 15);
                            this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + x, j2, (this.zPosition << 4) + z));
                        }
                    }
                }
                else {
                    for (int i2 = i; i2 < j; ++i2) {
                        final ExtendedBlockStorage extendedblockstorage3 = this.storageArrays[i2 >> 4];
                        if (extendedblockstorage3 != null) {
                            extendedblockstorage3.setExtSkylightValue(x, i2 & 0xF, z, 0);
                            this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + x, i2, (this.zPosition << 4) + z));
                        }
                    }
                }
                int k2 = 15;
                while (j > 0 && k2 > 0) {
                    --j;
                    int i3 = this.getBlockLightOpacity(x, j, z);
                    if (i3 == 0) {
                        i3 = 1;
                    }
                    k2 -= i3;
                    if (k2 < 0) {
                        k2 = 0;
                    }
                    final ExtendedBlockStorage extendedblockstorage4 = this.storageArrays[j >> 4];
                    if (extendedblockstorage4 != null) {
                        extendedblockstorage4.setExtSkylightValue(x, j & 0xF, z, k2);
                    }
                }
            }
            final int l2 = this.heightMap[z << 4 | x];
            int k3;
            int j3;
            if ((k3 = l2) < (j3 = i)) {
                j3 = l2;
                k3 = i;
            }
            if (l2 < this.heightMapMinimum) {
                this.heightMapMinimum = l2;
            }
            if (!this.worldObj.provider.getHasNoSky()) {
                for (final Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
                    this.updateSkylightNeighborHeight(k + ((EnumFacing)enumfacing).getFrontOffsetX(), l + ((EnumFacing)enumfacing).getFrontOffsetZ(), j3, k3);
                }
                this.updateSkylightNeighborHeight(k, l, j3, k3);
            }
            this.isModified = true;
        }
    }
    
    public int getBlockLightOpacity(final BlockPos pos) {
        return this.getBlock(pos).getLightOpacity();
    }
    
    private int getBlockLightOpacity(final int x, final int y, final int z) {
        return this.getBlock0(x, y, z).getLightOpacity();
    }
    
    private Block getBlock0(final int x, final int y, final int z) {
        Block block = Blocks.air;
        if (y >= 0 && y >> 4 < this.storageArrays.length) {
            final ExtendedBlockStorage extendedblockstorage = this.storageArrays[y >> 4];
            if (extendedblockstorage != null) {
                try {
                    block = extendedblockstorage.getBlockByExtId(x, y & 0xF, z);
                }
                catch (Throwable throwable) {
                    final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting block");
                    throw new ReportedException(crashreport);
                }
            }
        }
        return block;
    }
    
    public Block getBlock(final int x, final int y, final int z) {
        try {
            return this.getBlock0(x & 0xF, y, z & 0xF);
        }
        catch (ReportedException reportedexception) {
            final CrashReportCategory crashreportcategory = reportedexception.getCrashReport().makeCategory("Block being got");
            crashreportcategory.addCrashSectionCallable("Location", new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return CrashReportCategory.getCoordinateInfo(new BlockPos(Chunk.this.xPosition * 16 + x, y, Chunk.this.zPosition * 16 + z));
                }
            });
            throw reportedexception;
        }
    }
    
    public Block getBlock(final BlockPos pos) {
        try {
            return this.getBlock0(pos.getX() & 0xF, pos.getY(), pos.getZ() & 0xF);
        }
        catch (ReportedException reportedexception) {
            final CrashReportCategory crashreportcategory = reportedexception.getCrashReport().makeCategory("Block being got");
            crashreportcategory.addCrashSectionCallable("Location", new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return CrashReportCategory.getCoordinateInfo(pos);
                }
            });
            throw reportedexception;
        }
    }
    
    public IBlockState getBlockState(final BlockPos pos) {
        if (this.worldObj.getWorldType() == WorldType.DEBUG_WORLD) {
            IBlockState iblockstate = null;
            if (pos.getY() == 60) {
                iblockstate = Blocks.barrier.getDefaultState();
            }
            if (pos.getY() == 70) {
                iblockstate = ChunkProviderDebug.func_177461_b(pos.getX(), pos.getZ());
            }
            return (iblockstate == null) ? Blocks.air.getDefaultState() : iblockstate;
        }
        try {
            if (pos.getY() >= 0 && pos.getY() >> 4 < this.storageArrays.length) {
                final ExtendedBlockStorage extendedblockstorage = this.storageArrays[pos.getY() >> 4];
                if (extendedblockstorage != null) {
                    final int j = pos.getX() & 0xF;
                    final int k = pos.getY() & 0xF;
                    final int i = pos.getZ() & 0xF;
                    return extendedblockstorage.get(j, k, i);
                }
            }
            return Blocks.air.getDefaultState();
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting block state");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being got");
            crashreportcategory.addCrashSectionCallable("Location", new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return CrashReportCategory.getCoordinateInfo(pos);
                }
            });
            throw new ReportedException(crashreport);
        }
    }
    
    private int getBlockMetadata(final int x, final int y, final int z) {
        if (y >> 4 >= this.storageArrays.length) {
            return 0;
        }
        final ExtendedBlockStorage extendedblockstorage = this.storageArrays[y >> 4];
        return (extendedblockstorage != null) ? extendedblockstorage.getExtBlockMetadata(x, y & 0xF, z) : 0;
    }
    
    public int getBlockMetadata(final BlockPos pos) {
        return this.getBlockMetadata(pos.getX() & 0xF, pos.getY(), pos.getZ() & 0xF);
    }
    
    public IBlockState setBlockState(final BlockPos pos, final IBlockState state) {
        final int i = pos.getX() & 0xF;
        final int j = pos.getY();
        final int k = pos.getZ() & 0xF;
        final int l = k << 4 | i;
        if (j >= this.precipitationHeightMap[l] - 1) {
            this.precipitationHeightMap[l] = -999;
        }
        final int i2 = this.heightMap[l];
        final IBlockState iblockstate = this.getBlockState(pos);
        if (iblockstate == state) {
            return null;
        }
        final Block block = state.getBlock();
        final Block block2 = iblockstate.getBlock();
        ExtendedBlockStorage extendedblockstorage = this.storageArrays[j >> 4];
        boolean flag = false;
        if (extendedblockstorage == null) {
            if (block == Blocks.air) {
                return null;
            }
            final ExtendedBlockStorage[] storageArrays = this.storageArrays;
            final int n = j >> 4;
            final ExtendedBlockStorage extendedBlockStorage = new ExtendedBlockStorage(j >> 4 << 4, !this.worldObj.provider.getHasNoSky());
            storageArrays[n] = extendedBlockStorage;
            extendedblockstorage = extendedBlockStorage;
            flag = (j >= i2);
        }
        extendedblockstorage.set(i, j & 0xF, k, state);
        if (block2 != block) {
            if (!this.worldObj.isRemote) {
                block2.breakBlock(this.worldObj, pos, iblockstate);
            }
            else if (block2 instanceof ITileEntityProvider) {
                this.worldObj.removeTileEntity(pos);
            }
        }
        if (extendedblockstorage.getBlockByExtId(i, j & 0xF, k) != block) {
            return null;
        }
        if (flag) {
            this.generateSkylightMap();
        }
        else {
            final int j2 = block.getLightOpacity();
            final int k2 = block2.getLightOpacity();
            if (j2 > 0) {
                if (j >= i2) {
                    this.relightBlock(i, j + 1, k);
                }
            }
            else if (j == i2 - 1) {
                this.relightBlock(i, j, k);
            }
            if (j2 != k2 && (j2 < k2 || this.getLightFor(EnumSkyBlock.SKY, pos) > 0 || this.getLightFor(EnumSkyBlock.BLOCK, pos) > 0)) {
                this.propagateSkylightOcclusion(i, k);
            }
        }
        if (block2 instanceof ITileEntityProvider) {
            final TileEntity tileentity = this.getTileEntity(pos, EnumCreateEntityType.CHECK);
            if (tileentity != null) {
                tileentity.updateContainingBlockInfo();
            }
        }
        if (!this.worldObj.isRemote && block2 != block) {
            block.onBlockAdded(this.worldObj, pos, state);
        }
        if (block instanceof ITileEntityProvider) {
            TileEntity tileentity2 = this.getTileEntity(pos, EnumCreateEntityType.CHECK);
            if (tileentity2 == null) {
                tileentity2 = ((ITileEntityProvider)block).createNewTileEntity(this.worldObj, block.getMetaFromState(state));
                this.worldObj.setTileEntity(pos, tileentity2);
            }
            if (tileentity2 != null) {
                tileentity2.updateContainingBlockInfo();
            }
        }
        this.isModified = true;
        return iblockstate;
    }
    
    public int getLightFor(final EnumSkyBlock p_177413_1_, final BlockPos pos) {
        final int i = pos.getX() & 0xF;
        final int j = pos.getY();
        final int k = pos.getZ() & 0xF;
        final ExtendedBlockStorage extendedblockstorage = this.storageArrays[j >> 4];
        return (extendedblockstorage == null) ? (this.canSeeSky(pos) ? p_177413_1_.defaultLightValue : 0) : ((p_177413_1_ == EnumSkyBlock.SKY) ? (this.worldObj.provider.getHasNoSky() ? 0 : extendedblockstorage.getExtSkylightValue(i, j & 0xF, k)) : ((p_177413_1_ == EnumSkyBlock.BLOCK) ? extendedblockstorage.getExtBlocklightValue(i, j & 0xF, k) : p_177413_1_.defaultLightValue));
    }
    
    public void setLightFor(final EnumSkyBlock p_177431_1_, final BlockPos pos, final int value) {
        final int i = pos.getX() & 0xF;
        final int j = pos.getY();
        final int k = pos.getZ() & 0xF;
        ExtendedBlockStorage extendedblockstorage = this.storageArrays[j >> 4];
        if (extendedblockstorage == null) {
            final ExtendedBlockStorage[] storageArrays = this.storageArrays;
            final int n = j >> 4;
            final ExtendedBlockStorage extendedBlockStorage = new ExtendedBlockStorage(j >> 4 << 4, !this.worldObj.provider.getHasNoSky());
            storageArrays[n] = extendedBlockStorage;
            extendedblockstorage = extendedBlockStorage;
            this.generateSkylightMap();
        }
        this.isModified = true;
        if (p_177431_1_ == EnumSkyBlock.SKY) {
            if (!this.worldObj.provider.getHasNoSky()) {
                extendedblockstorage.setExtSkylightValue(i, j & 0xF, k, value);
            }
        }
        else if (p_177431_1_ == EnumSkyBlock.BLOCK) {
            extendedblockstorage.setExtBlocklightValue(i, j & 0xF, k, value);
        }
    }
    
    public int getLightSubtracted(final BlockPos pos, final int amount) {
        final int i = pos.getX() & 0xF;
        final int j = pos.getY();
        final int k = pos.getZ() & 0xF;
        final ExtendedBlockStorage extendedblockstorage = this.storageArrays[j >> 4];
        if (extendedblockstorage == null) {
            return (!this.worldObj.provider.getHasNoSky() && amount < EnumSkyBlock.SKY.defaultLightValue) ? (EnumSkyBlock.SKY.defaultLightValue - amount) : 0;
        }
        int l = this.worldObj.provider.getHasNoSky() ? 0 : extendedblockstorage.getExtSkylightValue(i, j & 0xF, k);
        l -= amount;
        final int i2 = extendedblockstorage.getExtBlocklightValue(i, j & 0xF, k);
        if (i2 > l) {
            l = i2;
        }
        return l;
    }
    
    public void addEntity(final Entity entityIn) {
        this.hasEntities = true;
        final int i = MathHelper.floor_double(entityIn.posX / 16.0);
        final int j = MathHelper.floor_double(entityIn.posZ / 16.0);
        if (i != this.xPosition || j != this.zPosition) {
            Chunk.logger.warn("Wrong location! (" + i + ", " + j + ") should be (" + this.xPosition + ", " + this.zPosition + "), " + entityIn, entityIn);
            entityIn.setDead();
        }
        int k = MathHelper.floor_double(entityIn.posY / 16.0);
        if (k < 0) {
            k = 0;
        }
        if (k >= this.entityLists.length) {
            k = this.entityLists.length - 1;
        }
        entityIn.addedToChunk = true;
        entityIn.chunkCoordX = this.xPosition;
        entityIn.chunkCoordY = k;
        entityIn.chunkCoordZ = this.zPosition;
        this.entityLists[k].add(entityIn);
    }
    
    public void removeEntity(final Entity entityIn) {
        this.removeEntityAtIndex(entityIn, entityIn.chunkCoordY);
    }
    
    public void removeEntityAtIndex(final Entity entityIn, int p_76608_2_) {
        if (p_76608_2_ < 0) {
            p_76608_2_ = 0;
        }
        if (p_76608_2_ >= this.entityLists.length) {
            p_76608_2_ = this.entityLists.length - 1;
        }
        this.entityLists[p_76608_2_].remove(entityIn);
    }
    
    public boolean canSeeSky(final BlockPos pos) {
        final int i = pos.getX() & 0xF;
        final int j = pos.getY();
        final int k = pos.getZ() & 0xF;
        return j >= this.heightMap[k << 4 | i];
    }
    
    private TileEntity createNewTileEntity(final BlockPos pos) {
        final Block block = this.getBlock(pos);
        return block.hasTileEntity() ? ((ITileEntityProvider)block).createNewTileEntity(this.worldObj, this.getBlockMetadata(pos)) : null;
    }
    
    public TileEntity getTileEntity(final BlockPos pos, final EnumCreateEntityType p_177424_2_) {
        TileEntity tileentity = this.chunkTileEntityMap.get(pos);
        if (tileentity == null) {
            if (p_177424_2_ == EnumCreateEntityType.IMMEDIATE) {
                tileentity = this.createNewTileEntity(pos);
                this.worldObj.setTileEntity(pos, tileentity);
            }
            else if (p_177424_2_ == EnumCreateEntityType.QUEUED) {
                this.tileEntityPosQueue.add(pos);
            }
        }
        else if (tileentity.isInvalid()) {
            this.chunkTileEntityMap.remove(pos);
            return null;
        }
        return tileentity;
    }
    
    public void addTileEntity(final TileEntity tileEntityIn) {
        this.addTileEntity(tileEntityIn.getPos(), tileEntityIn);
        if (this.isChunkLoaded) {
            this.worldObj.addTileEntity(tileEntityIn);
        }
    }
    
    public void addTileEntity(final BlockPos pos, final TileEntity tileEntityIn) {
        tileEntityIn.setWorldObj(this.worldObj);
        tileEntityIn.setPos(pos);
        if (this.getBlock(pos) instanceof ITileEntityProvider) {
            if (this.chunkTileEntityMap.containsKey(pos)) {
                this.chunkTileEntityMap.get(pos).invalidate();
            }
            tileEntityIn.validate();
            this.chunkTileEntityMap.put(pos, tileEntityIn);
        }
    }
    
    public void removeTileEntity(final BlockPos pos) {
        if (this.isChunkLoaded) {
            final TileEntity tileentity = this.chunkTileEntityMap.remove(pos);
            if (tileentity != null) {
                tileentity.invalidate();
            }
        }
    }
    
    public void onChunkLoad() {
        this.isChunkLoaded = true;
        this.worldObj.addTileEntities(this.chunkTileEntityMap.values());
        for (int i = 0; i < this.entityLists.length; ++i) {
            for (final Entity entity : this.entityLists[i]) {
                entity.onChunkLoad();
            }
            this.worldObj.loadEntities(this.entityLists[i]);
        }
    }
    
    public void onChunkUnload() {
        this.isChunkLoaded = false;
        for (final TileEntity tileentity : this.chunkTileEntityMap.values()) {
            this.worldObj.markTileEntityForRemoval(tileentity);
        }
        for (int i = 0; i < this.entityLists.length; ++i) {
            this.worldObj.unloadEntities(this.entityLists[i]);
        }
    }
    
    public void setChunkModified() {
        this.isModified = true;
    }
    
    public void getEntitiesWithinAABBForEntity(final Entity entityIn, final AxisAlignedBB aabb, final List<Entity> listToFill, final Predicate<? super Entity> p_177414_4_) {
        int i = MathHelper.floor_double((aabb.minY - 2.0) / 16.0);
        int j = MathHelper.floor_double((aabb.maxY + 2.0) / 16.0);
        i = MathHelper.clamp_int(i, 0, this.entityLists.length - 1);
        j = MathHelper.clamp_int(j, 0, this.entityLists.length - 1);
        for (int k = i; k <= j; ++k) {
            if (!this.entityLists[k].isEmpty()) {
                for (Entity entity : this.entityLists[k]) {
                    if (entity.getEntityBoundingBox().intersectsWith(aabb) && entity != entityIn) {
                        if (p_177414_4_ == null || p_177414_4_.apply(entity)) {
                            listToFill.add(entity);
                        }
                        final Entity[] aentity = entity.getParts();
                        if (aentity == null) {
                            continue;
                        }
                        for (int l = 0; l < aentity.length; ++l) {
                            entity = aentity[l];
                            if (entity != entityIn && entity.getEntityBoundingBox().intersectsWith(aabb) && (p_177414_4_ == null || p_177414_4_.apply(entity))) {
                                listToFill.add(entity);
                            }
                        }
                    }
                }
            }
        }
    }
    
    public <T extends Entity> void getEntitiesOfTypeWithinAAAB(final Class<? extends T> entityClass, final AxisAlignedBB aabb, final List<T> listToFill, final Predicate<? super T> p_177430_4_) {
        int i = MathHelper.floor_double((aabb.minY - 2.0) / 16.0);
        int j = MathHelper.floor_double((aabb.maxY + 2.0) / 16.0);
        i = MathHelper.clamp_int(i, 0, this.entityLists.length - 1);
        j = MathHelper.clamp_int(j, 0, this.entityLists.length - 1);
        for (int k = i; k <= j; ++k) {
            for (final T t : this.entityLists[k].getByClass(entityClass)) {
                if (t.getEntityBoundingBox().intersectsWith(aabb) && (p_177430_4_ == null || p_177430_4_.apply((Object)t))) {
                    listToFill.add(t);
                }
            }
        }
    }
    
    public boolean needsSaving(final boolean p_76601_1_) {
        if (p_76601_1_) {
            if ((this.hasEntities && this.worldObj.getTotalWorldTime() != this.lastSaveTime) || this.isModified) {
                return true;
            }
        }
        else if (this.hasEntities && this.worldObj.getTotalWorldTime() >= this.lastSaveTime + 600L) {
            return true;
        }
        return this.isModified;
    }
    
    public Random getRandomWithSeed(final long seed) {
        return new Random(this.worldObj.getSeed() + this.xPosition * this.xPosition * 4987142 + this.xPosition * 5947611 + this.zPosition * this.zPosition * 4392871L + this.zPosition * 389711 ^ seed);
    }
    
    public boolean isEmpty() {
        return false;
    }
    
    public void populateChunk(final IChunkProvider p_76624_1_, final IChunkProvider p_76624_2_, final int p_76624_3_, final int p_76624_4_) {
        final boolean flag = p_76624_1_.chunkExists(p_76624_3_, p_76624_4_ - 1);
        final boolean flag2 = p_76624_1_.chunkExists(p_76624_3_ + 1, p_76624_4_);
        final boolean flag3 = p_76624_1_.chunkExists(p_76624_3_, p_76624_4_ + 1);
        final boolean flag4 = p_76624_1_.chunkExists(p_76624_3_ - 1, p_76624_4_);
        final boolean flag5 = p_76624_1_.chunkExists(p_76624_3_ - 1, p_76624_4_ - 1);
        final boolean flag6 = p_76624_1_.chunkExists(p_76624_3_ + 1, p_76624_4_ + 1);
        final boolean flag7 = p_76624_1_.chunkExists(p_76624_3_ - 1, p_76624_4_ + 1);
        final boolean flag8 = p_76624_1_.chunkExists(p_76624_3_ + 1, p_76624_4_ - 1);
        if (flag2 && flag3 && flag6) {
            if (!this.isTerrainPopulated) {
                p_76624_1_.populate(p_76624_2_, p_76624_3_, p_76624_4_);
            }
            else {
                p_76624_1_.func_177460_a(p_76624_2_, this, p_76624_3_, p_76624_4_);
            }
        }
        if (flag4 && flag3 && flag7) {
            final Chunk chunk = p_76624_1_.provideChunk(p_76624_3_ - 1, p_76624_4_);
            if (!chunk.isTerrainPopulated) {
                p_76624_1_.populate(p_76624_2_, p_76624_3_ - 1, p_76624_4_);
            }
            else {
                p_76624_1_.func_177460_a(p_76624_2_, chunk, p_76624_3_ - 1, p_76624_4_);
            }
        }
        if (flag && flag2 && flag8) {
            final Chunk chunk2 = p_76624_1_.provideChunk(p_76624_3_, p_76624_4_ - 1);
            if (!chunk2.isTerrainPopulated) {
                p_76624_1_.populate(p_76624_2_, p_76624_3_, p_76624_4_ - 1);
            }
            else {
                p_76624_1_.func_177460_a(p_76624_2_, chunk2, p_76624_3_, p_76624_4_ - 1);
            }
        }
        if (flag5 && flag && flag4) {
            final Chunk chunk3 = p_76624_1_.provideChunk(p_76624_3_ - 1, p_76624_4_ - 1);
            if (!chunk3.isTerrainPopulated) {
                p_76624_1_.populate(p_76624_2_, p_76624_3_ - 1, p_76624_4_ - 1);
            }
            else {
                p_76624_1_.func_177460_a(p_76624_2_, chunk3, p_76624_3_ - 1, p_76624_4_ - 1);
            }
        }
    }
    
    public BlockPos getPrecipitationHeight(final BlockPos pos) {
        final int i = pos.getX() & 0xF;
        final int j = pos.getZ() & 0xF;
        final int k = i | j << 4;
        BlockPos blockpos = new BlockPos(pos.getX(), this.precipitationHeightMap[k], pos.getZ());
        if (blockpos.getY() == -999) {
            final int l = this.getTopFilledSegment() + 15;
            blockpos = new BlockPos(pos.getX(), l, pos.getZ());
            int i2 = -1;
            while (blockpos.getY() > 0 && i2 == -1) {
                final Block block = this.getBlock(blockpos);
                final Material material = block.getMaterial();
                if (!material.blocksMovement() && !material.isLiquid()) {
                    blockpos = blockpos.down();
                }
                else {
                    i2 = blockpos.getY() + 1;
                }
            }
            this.precipitationHeightMap[k] = i2;
        }
        return new BlockPos(pos.getX(), this.precipitationHeightMap[k], pos.getZ());
    }
    
    public void func_150804_b(final boolean p_150804_1_) {
        if (this.isGapLightingUpdated && !this.worldObj.provider.getHasNoSky() && !p_150804_1_) {
            this.recheckGaps(this.worldObj.isRemote);
        }
        this.field_150815_m = true;
        if (!this.isLightPopulated && this.isTerrainPopulated) {
            this.func_150809_p();
        }
        while (!this.tileEntityPosQueue.isEmpty()) {
            final BlockPos blockpos = this.tileEntityPosQueue.poll();
            if (this.getTileEntity(blockpos, EnumCreateEntityType.CHECK) == null && this.getBlock(blockpos).hasTileEntity()) {
                final TileEntity tileentity = this.createNewTileEntity(blockpos);
                this.worldObj.setTileEntity(blockpos, tileentity);
                this.worldObj.markBlockRangeForRenderUpdate(blockpos, blockpos);
            }
        }
    }
    
    public boolean isPopulated() {
        return this.field_150815_m && this.isTerrainPopulated && this.isLightPopulated;
    }
    
    public ChunkCoordIntPair getChunkCoordIntPair() {
        return new ChunkCoordIntPair(this.xPosition, this.zPosition);
    }
    
    public boolean getAreLevelsEmpty(int startY, int endY) {
        if (startY < 0) {
            startY = 0;
        }
        if (endY >= 256) {
            endY = 255;
        }
        for (int i = startY; i <= endY; i += 16) {
            final ExtendedBlockStorage extendedblockstorage = this.storageArrays[i >> 4];
            if (extendedblockstorage != null && !extendedblockstorage.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    public void setStorageArrays(final ExtendedBlockStorage[] newStorageArrays) {
        if (this.storageArrays.length != newStorageArrays.length) {
            Chunk.logger.warn("Could not set level chunk sections, array length is " + newStorageArrays.length + " instead of " + this.storageArrays.length);
        }
        else {
            for (int i = 0; i < this.storageArrays.length; ++i) {
                this.storageArrays[i] = newStorageArrays[i];
            }
        }
    }
    
    public void fillChunk(final byte[] p_177439_1_, final int p_177439_2_, final boolean p_177439_3_) {
        int i = 0;
        final boolean flag = !this.worldObj.provider.getHasNoSky();
        for (int j = 0; j < this.storageArrays.length; ++j) {
            if ((p_177439_2_ & 1 << j) != 0x0) {
                if (this.storageArrays[j] == null) {
                    this.storageArrays[j] = new ExtendedBlockStorage(j << 4, flag);
                }
                final char[] achar = this.storageArrays[j].getData();
                for (int k = 0; k < achar.length; ++k) {
                    achar[k] = (char)((p_177439_1_[i + 1] & 0xFF) << 8 | (p_177439_1_[i] & 0xFF));
                    i += 2;
                }
            }
            else if (p_177439_3_ && this.storageArrays[j] != null) {
                this.storageArrays[j] = null;
            }
        }
        for (int l = 0; l < this.storageArrays.length; ++l) {
            if ((p_177439_2_ & 1 << l) != 0x0 && this.storageArrays[l] != null) {
                final NibbleArray nibblearray = this.storageArrays[l].getBlocklightArray();
                System.arraycopy(p_177439_1_, i, nibblearray.getData(), 0, nibblearray.getData().length);
                i += nibblearray.getData().length;
            }
        }
        if (flag) {
            for (int i2 = 0; i2 < this.storageArrays.length; ++i2) {
                if ((p_177439_2_ & 1 << i2) != 0x0 && this.storageArrays[i2] != null) {
                    final NibbleArray nibblearray2 = this.storageArrays[i2].getSkylightArray();
                    System.arraycopy(p_177439_1_, i, nibblearray2.getData(), 0, nibblearray2.getData().length);
                    i += nibblearray2.getData().length;
                }
            }
        }
        if (p_177439_3_) {
            System.arraycopy(p_177439_1_, i, this.blockBiomeArray, 0, this.blockBiomeArray.length);
            final int n = i + this.blockBiomeArray.length;
        }
        for (int j2 = 0; j2 < this.storageArrays.length; ++j2) {
            if (this.storageArrays[j2] != null && (p_177439_2_ & 1 << j2) != 0x0) {
                this.storageArrays[j2].removeInvalidBlocks();
            }
        }
        this.isLightPopulated = true;
        this.isTerrainPopulated = true;
        this.generateHeightMap();
        for (final TileEntity tileentity : this.chunkTileEntityMap.values()) {
            tileentity.updateContainingBlockInfo();
        }
    }
    
    public BiomeGenBase getBiome(final BlockPos pos, final WorldChunkManager chunkManager) {
        final int i = pos.getX() & 0xF;
        final int j = pos.getZ() & 0xF;
        int k = this.blockBiomeArray[j << 4 | i] & 0xFF;
        if (k == 255) {
            final BiomeGenBase biomegenbase = chunkManager.getBiomeGenerator(pos, BiomeGenBase.plains);
            k = biomegenbase.biomeID;
            this.blockBiomeArray[j << 4 | i] = (byte)(k & 0xFF);
        }
        final BiomeGenBase biomegenbase2 = BiomeGenBase.getBiome(k);
        return (biomegenbase2 == null) ? BiomeGenBase.plains : biomegenbase2;
    }
    
    public byte[] getBiomeArray() {
        return this.blockBiomeArray;
    }
    
    public void setBiomeArray(final byte[] biomeArray) {
        if (this.blockBiomeArray.length != biomeArray.length) {
            Chunk.logger.warn("Could not set level chunk biomes, array length is " + biomeArray.length + " instead of " + this.blockBiomeArray.length);
        }
        else {
            for (int i = 0; i < this.blockBiomeArray.length; ++i) {
                this.blockBiomeArray[i] = biomeArray[i];
            }
        }
    }
    
    public void resetRelightChecks() {
        this.queuedLightChecks = 0;
    }
    
    public void enqueueRelightChecks() {
        final BlockPos blockpos = new BlockPos(this.xPosition << 4, 0, this.zPosition << 4);
        for (int i = 0; i < 8; ++i) {
            if (this.queuedLightChecks >= 4096) {
                return;
            }
            final int j = this.queuedLightChecks % 16;
            final int k = this.queuedLightChecks / 16 % 16;
            final int l = this.queuedLightChecks / 256;
            ++this.queuedLightChecks;
            for (int i2 = 0; i2 < 16; ++i2) {
                final BlockPos blockpos2 = blockpos.add(k, (j << 4) + i2, l);
                final boolean flag = i2 == 0 || i2 == 15 || k == 0 || k == 15 || l == 0 || l == 15;
                if ((this.storageArrays[j] == null && flag) || (this.storageArrays[j] != null && this.storageArrays[j].getBlockByExtId(k, i2, l).getMaterial() == Material.air)) {
                    EnumFacing[] values;
                    for (int length = (values = EnumFacing.values()).length, n = 0; n < length; ++n) {
                        final EnumFacing enumfacing = values[n];
                        final BlockPos blockpos3 = blockpos2.offset(enumfacing);
                        if (this.worldObj.getBlockState(blockpos3).getBlock().getLightValue() > 0) {
                            this.worldObj.checkLight(blockpos3);
                        }
                    }
                    this.worldObj.checkLight(blockpos2);
                }
            }
        }
    }
    
    public void func_150809_p() {
        this.isTerrainPopulated = true;
        this.isLightPopulated = true;
        final BlockPos blockpos = new BlockPos(this.xPosition << 4, 0, this.zPosition << 4);
        if (!this.worldObj.provider.getHasNoSky()) {
            if (this.worldObj.isAreaLoaded(blockpos.add(-1, 0, -1), blockpos.add(16, this.worldObj.func_181545_F(), 16))) {
            Label_0121:
                for (int i = 0; i < 16; ++i) {
                    for (int j = 0; j < 16; ++j) {
                        if (!this.func_150811_f(i, j)) {
                            this.isLightPopulated = false;
                            break Label_0121;
                        }
                    }
                }
                if (this.isLightPopulated) {
                    for (final Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
                        final int k = (((EnumFacing)enumfacing).getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) ? 16 : 1;
                        this.worldObj.getChunkFromBlockCoords(blockpos.offset((EnumFacing)enumfacing, k)).func_180700_a(((EnumFacing)enumfacing).getOpposite());
                    }
                    this.func_177441_y();
                }
            }
            else {
                this.isLightPopulated = false;
            }
        }
    }
    
    private void func_177441_y() {
        for (int i = 0; i < this.updateSkylightColumns.length; ++i) {
            this.updateSkylightColumns[i] = true;
        }
        this.recheckGaps(false);
    }
    
    private void func_180700_a(final EnumFacing p_180700_1_) {
        if (this.isTerrainPopulated) {
            if (p_180700_1_ == EnumFacing.EAST) {
                for (int i = 0; i < 16; ++i) {
                    this.func_150811_f(15, i);
                }
            }
            else if (p_180700_1_ == EnumFacing.WEST) {
                for (int j = 0; j < 16; ++j) {
                    this.func_150811_f(0, j);
                }
            }
            else if (p_180700_1_ == EnumFacing.SOUTH) {
                for (int k = 0; k < 16; ++k) {
                    this.func_150811_f(k, 15);
                }
            }
            else if (p_180700_1_ == EnumFacing.NORTH) {
                for (int l = 0; l < 16; ++l) {
                    this.func_150811_f(l, 0);
                }
            }
        }
    }
    
    private boolean func_150811_f(final int x, final int z) {
        final int i = this.getTopFilledSegment();
        boolean flag = false;
        boolean flag2 = false;
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos((this.xPosition << 4) + x, 0, (this.zPosition << 4) + z);
        for (int j = i + 16 - 1; j > this.worldObj.func_181545_F() || (j > 0 && !flag2); --j) {
            blockpos$mutableblockpos.func_181079_c(blockpos$mutableblockpos.getX(), j, blockpos$mutableblockpos.getZ());
            final int k = this.getBlockLightOpacity(blockpos$mutableblockpos);
            if (k == 255 && blockpos$mutableblockpos.getY() < this.worldObj.func_181545_F()) {
                flag2 = true;
            }
            if (!flag && k > 0) {
                flag = true;
            }
            else if (flag && k == 0 && !this.worldObj.checkLight(blockpos$mutableblockpos)) {
                return false;
            }
        }
        for (int l = blockpos$mutableblockpos.getY(); l > 0; --l) {
            blockpos$mutableblockpos.func_181079_c(blockpos$mutableblockpos.getX(), l, blockpos$mutableblockpos.getZ());
            if (this.getBlock(blockpos$mutableblockpos).getLightValue() > 0) {
                this.worldObj.checkLight(blockpos$mutableblockpos);
            }
        }
        return true;
    }
    
    public boolean isLoaded() {
        return this.isChunkLoaded;
    }
    
    public void setChunkLoaded(final boolean loaded) {
        this.isChunkLoaded = loaded;
    }
    
    public World getWorld() {
        return this.worldObj;
    }
    
    public int[] getHeightMap() {
        return this.heightMap;
    }
    
    public void setHeightMap(final int[] newHeightMap) {
        if (this.heightMap.length != newHeightMap.length) {
            Chunk.logger.warn("Could not set level chunk heightmap, array length is " + newHeightMap.length + " instead of " + this.heightMap.length);
        }
        else {
            for (int i = 0; i < this.heightMap.length; ++i) {
                this.heightMap[i] = newHeightMap[i];
            }
        }
    }
    
    public Map<BlockPos, TileEntity> getTileEntityMap() {
        return this.chunkTileEntityMap;
    }
    
    public ClassInheritanceMultiMap<Entity>[] getEntityLists() {
        return this.entityLists;
    }
    
    public boolean isTerrainPopulated() {
        return this.isTerrainPopulated;
    }
    
    public void setTerrainPopulated(final boolean terrainPopulated) {
        this.isTerrainPopulated = terrainPopulated;
    }
    
    public boolean isLightPopulated() {
        return this.isLightPopulated;
    }
    
    public void setLightPopulated(final boolean lightPopulated) {
        this.isLightPopulated = lightPopulated;
    }
    
    public void setModified(final boolean modified) {
        this.isModified = modified;
    }
    
    public void setHasEntities(final boolean hasEntitiesIn) {
        this.hasEntities = hasEntitiesIn;
    }
    
    public void setLastSaveTime(final long saveTime) {
        this.lastSaveTime = saveTime;
    }
    
    public int getLowestHeight() {
        return this.heightMapMinimum;
    }
    
    public long getInhabitedTime() {
        return this.inhabitedTime;
    }
    
    public void setInhabitedTime(final long newInhabitedTime) {
        this.inhabitedTime = newInhabitedTime;
    }
    
    public enum EnumCreateEntityType
    {
        IMMEDIATE("IMMEDIATE", 0), 
        QUEUED("QUEUED", 1), 
        CHECK("CHECK", 2);
        
        private EnumCreateEntityType(final String name, final int ordinal) {
        }
    }
}
