// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import java.util.UUID;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLiving;
import com.google.common.base.Predicate;
import net.minecraft.util.EntitySelectors;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockLiquid;
import java.util.Iterator;
import net.minecraft.util.ITickable;
import java.util.Collection;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReportCategory;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.util.BlockPos;
import com.google.common.collect.Sets;
import com.google.common.collect.Lists;
import net.minecraft.world.border.WorldBorder;
import java.util.Set;
import net.minecraft.scoreboard.Scoreboard;
import java.util.Calendar;
import net.minecraft.profiler.Profiler;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.chunk.IChunkProvider;
import java.util.Random;
import net.minecraft.util.IntHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.Entity;
import java.util.List;

public abstract class World implements IBlockAccess
{
    private int field_181546_a;
    protected boolean scheduledUpdatesAreImmediate;
    public final List<Entity> loadedEntityList;
    protected final List<Entity> unloadedEntityList;
    public final List<TileEntity> loadedTileEntityList;
    public final List<TileEntity> tickableTileEntities;
    private final List<TileEntity> addedTileEntityList;
    private final List<TileEntity> tileEntitiesToBeRemoved;
    public final List<EntityPlayer> playerEntities;
    public final List<Entity> weatherEffects;
    protected final IntHashMap<Entity> entitiesById;
    private long cloudColour;
    private int skylightSubtracted;
    protected int updateLCG;
    protected final int DIST_HASH_MAGIC = 1013904223;
    protected float prevRainingStrength;
    protected float rainingStrength;
    protected float prevThunderingStrength;
    protected float thunderingStrength;
    private int lastLightningBolt;
    public final Random rand;
    public final WorldProvider provider;
    protected List<IWorldAccess> worldAccesses;
    protected IChunkProvider chunkProvider;
    protected final ISaveHandler saveHandler;
    protected WorldInfo worldInfo;
    protected boolean findingSpawnPoint;
    protected MapStorage mapStorage;
    protected VillageCollection villageCollectionObj;
    public final Profiler theProfiler;
    private final Calendar theCalendar;
    protected Scoreboard worldScoreboard;
    public final boolean isRemote;
    protected Set<ChunkCoordIntPair> activeChunkSet;
    private int ambientTickCountdown;
    protected boolean spawnHostileMobs;
    protected boolean spawnPeacefulMobs;
    private boolean processingLoadedTiles;
    private final WorldBorder worldBorder;
    int[] lightUpdateBlockList;
    
    protected World(final ISaveHandler saveHandlerIn, final WorldInfo info, final WorldProvider providerIn, final Profiler profilerIn, final boolean client) {
        this.field_181546_a = 63;
        this.loadedEntityList = (List<Entity>)Lists.newArrayList();
        this.unloadedEntityList = (List<Entity>)Lists.newArrayList();
        this.loadedTileEntityList = (List<TileEntity>)Lists.newArrayList();
        this.tickableTileEntities = (List<TileEntity>)Lists.newArrayList();
        this.addedTileEntityList = (List<TileEntity>)Lists.newArrayList();
        this.tileEntitiesToBeRemoved = (List<TileEntity>)Lists.newArrayList();
        this.playerEntities = (List<EntityPlayer>)Lists.newArrayList();
        this.weatherEffects = (List<Entity>)Lists.newArrayList();
        this.entitiesById = new IntHashMap<Entity>();
        this.cloudColour = 16777215L;
        this.updateLCG = new Random().nextInt();
        this.rand = new Random();
        this.worldAccesses = (List<IWorldAccess>)Lists.newArrayList();
        this.theCalendar = Calendar.getInstance();
        this.worldScoreboard = new Scoreboard();
        this.activeChunkSet = (Set<ChunkCoordIntPair>)Sets.newHashSet();
        this.ambientTickCountdown = this.rand.nextInt(12000);
        this.spawnHostileMobs = true;
        this.spawnPeacefulMobs = true;
        this.lightUpdateBlockList = new int[32768];
        this.saveHandler = saveHandlerIn;
        this.theProfiler = profilerIn;
        this.worldInfo = info;
        this.provider = providerIn;
        this.isRemote = client;
        this.worldBorder = providerIn.getWorldBorder();
    }
    
    public World init() {
        return this;
    }
    
    @Override
    public BiomeGenBase getBiomeGenForCoords(final BlockPos pos) {
        if (this.isBlockLoaded(pos)) {
            final Chunk chunk = this.getChunkFromBlockCoords(pos);
            try {
                return chunk.getBiome(pos, this.provider.getWorldChunkManager());
            }
            catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting biome");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Coordinates of biome request");
                crashreportcategory.addCrashSectionCallable("Location", new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return CrashReportCategory.getCoordinateInfo(pos);
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
        return this.provider.getWorldChunkManager().getBiomeGenerator(pos, BiomeGenBase.plains);
    }
    
    public WorldChunkManager getWorldChunkManager() {
        return this.provider.getWorldChunkManager();
    }
    
    protected abstract IChunkProvider createChunkProvider();
    
    public void initialize(final WorldSettings settings) {
        this.worldInfo.setServerInitialized(true);
    }
    
    public void setInitialSpawnLocation() {
        this.setSpawnPoint(new BlockPos(8, 64, 8));
    }
    
    public Block getGroundAboveSeaLevel(final BlockPos pos) {
        BlockPos blockpos;
        for (blockpos = new BlockPos(pos.getX(), this.func_181545_F(), pos.getZ()); !this.isAirBlock(blockpos.up()); blockpos = blockpos.up()) {}
        return this.getBlockState(blockpos).getBlock();
    }
    
    private boolean isValid(final BlockPos pos) {
        return pos.getX() >= -30000000 && pos.getZ() >= -30000000 && pos.getX() < 30000000 && pos.getZ() < 30000000 && pos.getY() >= 0 && pos.getY() < 256;
    }
    
    @Override
    public boolean isAirBlock(final BlockPos pos) {
        return this.getBlockState(pos).getBlock().getMaterial() == Material.air;
    }
    
    public boolean isBlockLoaded(final BlockPos pos) {
        return this.isBlockLoaded(pos, true);
    }
    
    public boolean isBlockLoaded(final BlockPos pos, final boolean allowEmpty) {
        return this.isValid(pos) && this.isChunkLoaded(pos.getX() >> 4, pos.getZ() >> 4, allowEmpty);
    }
    
    public boolean isAreaLoaded(final BlockPos center, final int radius) {
        return this.isAreaLoaded(center, radius, true);
    }
    
    public boolean isAreaLoaded(final BlockPos center, final int radius, final boolean allowEmpty) {
        return this.isAreaLoaded(center.getX() - radius, center.getY() - radius, center.getZ() - radius, center.getX() + radius, center.getY() + radius, center.getZ() + radius, allowEmpty);
    }
    
    public boolean isAreaLoaded(final BlockPos from, final BlockPos to) {
        return this.isAreaLoaded(from, to, true);
    }
    
    public boolean isAreaLoaded(final BlockPos from, final BlockPos to, final boolean allowEmpty) {
        return this.isAreaLoaded(from.getX(), from.getY(), from.getZ(), to.getX(), to.getY(), to.getZ(), allowEmpty);
    }
    
    public boolean isAreaLoaded(final StructureBoundingBox box) {
        return this.isAreaLoaded(box, true);
    }
    
    public boolean isAreaLoaded(final StructureBoundingBox box, final boolean allowEmpty) {
        return this.isAreaLoaded(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, allowEmpty);
    }
    
    private boolean isAreaLoaded(int xStart, final int yStart, int zStart, int xEnd, final int yEnd, int zEnd, final boolean allowEmpty) {
        if (yEnd >= 0 && yStart < 256) {
            xStart >>= 4;
            zStart >>= 4;
            xEnd >>= 4;
            zEnd >>= 4;
            for (int i = xStart; i <= xEnd; ++i) {
                for (int j = zStart; j <= zEnd; ++j) {
                    if (!this.isChunkLoaded(i, j, allowEmpty)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    protected boolean isChunkLoaded(final int x, final int z, final boolean allowEmpty) {
        return this.chunkProvider.chunkExists(x, z) && (allowEmpty || !this.chunkProvider.provideChunk(x, z).isEmpty());
    }
    
    public Chunk getChunkFromBlockCoords(final BlockPos pos) {
        return this.getChunkFromChunkCoords(pos.getX() >> 4, pos.getZ() >> 4);
    }
    
    public Chunk getChunkFromChunkCoords(final int chunkX, final int chunkZ) {
        return this.chunkProvider.provideChunk(chunkX, chunkZ);
    }
    
    public boolean setBlockState(final BlockPos pos, final IBlockState newState, final int flags) {
        if (!this.isValid(pos)) {
            return false;
        }
        if (!this.isRemote && this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
            return false;
        }
        final Chunk chunk = this.getChunkFromBlockCoords(pos);
        final Block block = newState.getBlock();
        final IBlockState iblockstate = chunk.setBlockState(pos, newState);
        if (iblockstate == null) {
            return false;
        }
        final Block block2 = iblockstate.getBlock();
        if (block.getLightOpacity() != block2.getLightOpacity() || block.getLightValue() != block2.getLightValue()) {
            this.theProfiler.startSection("checkLight");
            this.checkLight(pos);
            this.theProfiler.endSection();
        }
        if ((flags & 0x2) != 0x0 && (!this.isRemote || (flags & 0x4) == 0x0) && chunk.isPopulated()) {
            this.markBlockForUpdate(pos);
        }
        if (!this.isRemote && (flags & 0x1) != 0x0) {
            this.notifyNeighborsRespectDebug(pos, iblockstate.getBlock());
            if (block.hasComparatorInputOverride()) {
                this.updateComparatorOutputLevel(pos, block);
            }
        }
        return true;
    }
    
    public boolean setBlockToAir(final BlockPos pos) {
        return this.setBlockState(pos, Blocks.air.getDefaultState(), 3);
    }
    
    public boolean destroyBlock(final BlockPos pos, final boolean dropBlock) {
        final IBlockState iblockstate = this.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        if (block.getMaterial() == Material.air) {
            return false;
        }
        this.playAuxSFX(2001, pos, Block.getStateId(iblockstate));
        if (dropBlock) {
            block.dropBlockAsItem(this, pos, iblockstate, 0);
        }
        return this.setBlockState(pos, Blocks.air.getDefaultState(), 3);
    }
    
    public boolean setBlockState(final BlockPos pos, final IBlockState state) {
        return this.setBlockState(pos, state, 3);
    }
    
    public void markBlockForUpdate(final BlockPos pos) {
        for (int i = 0; i < this.worldAccesses.size(); ++i) {
            this.worldAccesses.get(i).markBlockForUpdate(pos);
        }
    }
    
    public void notifyNeighborsRespectDebug(final BlockPos pos, final Block blockType) {
        if (this.worldInfo.getTerrainType() != WorldType.DEBUG_WORLD) {
            this.notifyNeighborsOfStateChange(pos, blockType);
        }
    }
    
    public void markBlocksDirtyVertical(final int x1, final int z1, int x2, int z2) {
        if (x2 > z2) {
            final int i = z2;
            z2 = x2;
            x2 = i;
        }
        if (!this.provider.getHasNoSky()) {
            for (int j = x2; j <= z2; ++j) {
                this.checkLightFor(EnumSkyBlock.SKY, new BlockPos(x1, j, z1));
            }
        }
        this.markBlockRangeForRenderUpdate(x1, x2, z1, x1, z2, z1);
    }
    
    public void markBlockRangeForRenderUpdate(final BlockPos rangeMin, final BlockPos rangeMax) {
        this.markBlockRangeForRenderUpdate(rangeMin.getX(), rangeMin.getY(), rangeMin.getZ(), rangeMax.getX(), rangeMax.getY(), rangeMax.getZ());
    }
    
    public void markBlockRangeForRenderUpdate(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
        for (int i = 0; i < this.worldAccesses.size(); ++i) {
            this.worldAccesses.get(i).markBlockRangeForRenderUpdate(x1, y1, z1, x2, y2, z2);
        }
    }
    
    public void notifyNeighborsOfStateChange(final BlockPos pos, final Block blockType) {
        this.notifyBlockOfStateChange(pos.west(), blockType);
        this.notifyBlockOfStateChange(pos.east(), blockType);
        this.notifyBlockOfStateChange(pos.down(), blockType);
        this.notifyBlockOfStateChange(pos.up(), blockType);
        this.notifyBlockOfStateChange(pos.north(), blockType);
        this.notifyBlockOfStateChange(pos.south(), blockType);
    }
    
    public void notifyNeighborsOfStateExcept(final BlockPos pos, final Block blockType, final EnumFacing skipSide) {
        if (skipSide != EnumFacing.WEST) {
            this.notifyBlockOfStateChange(pos.west(), blockType);
        }
        if (skipSide != EnumFacing.EAST) {
            this.notifyBlockOfStateChange(pos.east(), blockType);
        }
        if (skipSide != EnumFacing.DOWN) {
            this.notifyBlockOfStateChange(pos.down(), blockType);
        }
        if (skipSide != EnumFacing.UP) {
            this.notifyBlockOfStateChange(pos.up(), blockType);
        }
        if (skipSide != EnumFacing.NORTH) {
            this.notifyBlockOfStateChange(pos.north(), blockType);
        }
        if (skipSide != EnumFacing.SOUTH) {
            this.notifyBlockOfStateChange(pos.south(), blockType);
        }
    }
    
    public void notifyBlockOfStateChange(final BlockPos pos, final Block blockIn) {
        if (!this.isRemote) {
            final IBlockState iblockstate = this.getBlockState(pos);
            try {
                iblockstate.getBlock().onNeighborBlockChange(this, pos, iblockstate, blockIn);
            }
            catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while updating neighbours");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being updated");
                crashreportcategory.addCrashSectionCallable("Source block type", new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        try {
                            return String.format("ID #%d (%s // %s)", Block.getIdFromBlock(blockIn), blockIn.getUnlocalizedName(), blockIn.getClass().getCanonicalName());
                        }
                        catch (Throwable var2) {
                            return "ID #" + Block.getIdFromBlock(blockIn);
                        }
                    }
                });
                CrashReportCategory.addBlockInfo(crashreportcategory, pos, iblockstate);
                throw new ReportedException(crashreport);
            }
        }
    }
    
    public boolean isBlockTickPending(final BlockPos pos, final Block blockType) {
        return false;
    }
    
    public boolean canSeeSky(final BlockPos pos) {
        return this.getChunkFromBlockCoords(pos).canSeeSky(pos);
    }
    
    public boolean canBlockSeeSky(final BlockPos pos) {
        if (pos.getY() >= this.func_181545_F()) {
            return this.canSeeSky(pos);
        }
        BlockPos blockpos = new BlockPos(pos.getX(), this.func_181545_F(), pos.getZ());
        if (!this.canSeeSky(blockpos)) {
            return false;
        }
        for (blockpos = blockpos.down(); blockpos.getY() > pos.getY(); blockpos = blockpos.down()) {
            final Block block = this.getBlockState(blockpos).getBlock();
            if (block.getLightOpacity() > 0 && !block.getMaterial().isLiquid()) {
                return false;
            }
        }
        return true;
    }
    
    public int getLight(BlockPos pos) {
        if (pos.getY() < 0) {
            return 0;
        }
        if (pos.getY() >= 256) {
            pos = new BlockPos(pos.getX(), 255, pos.getZ());
        }
        return this.getChunkFromBlockCoords(pos).getLightSubtracted(pos, 0);
    }
    
    public int getLightFromNeighbors(final BlockPos pos) {
        return this.getLight(pos, true);
    }
    
    public int getLight(BlockPos pos, final boolean checkNeighbors) {
        if (pos.getX() < -30000000 || pos.getZ() < -30000000 || pos.getX() >= 30000000 || pos.getZ() >= 30000000) {
            return 15;
        }
        if (checkNeighbors && this.getBlockState(pos).getBlock().getUseNeighborBrightness()) {
            int i1 = this.getLight(pos.up(), false);
            final int j = this.getLight(pos.east(), false);
            final int k = this.getLight(pos.west(), false);
            final int l = this.getLight(pos.south(), false);
            final int m = this.getLight(pos.north(), false);
            if (j > i1) {
                i1 = j;
            }
            if (k > i1) {
                i1 = k;
            }
            if (l > i1) {
                i1 = l;
            }
            if (m > i1) {
                i1 = m;
            }
            return i1;
        }
        if (pos.getY() < 0) {
            return 0;
        }
        if (pos.getY() >= 256) {
            pos = new BlockPos(pos.getX(), 255, pos.getZ());
        }
        final Chunk chunk = this.getChunkFromBlockCoords(pos);
        return chunk.getLightSubtracted(pos, this.skylightSubtracted);
    }
    
    public BlockPos getHeight(final BlockPos pos) {
        int i;
        if (pos.getX() >= -30000000 && pos.getZ() >= -30000000 && pos.getX() < 30000000 && pos.getZ() < 30000000) {
            if (this.isChunkLoaded(pos.getX() >> 4, pos.getZ() >> 4, true)) {
                i = this.getChunkFromChunkCoords(pos.getX() >> 4, pos.getZ() >> 4).getHeightValue(pos.getX() & 0xF, pos.getZ() & 0xF);
            }
            else {
                i = 0;
            }
        }
        else {
            i = this.func_181545_F() + 1;
        }
        return new BlockPos(pos.getX(), i, pos.getZ());
    }
    
    public int getChunksLowestHorizon(final int x, final int z) {
        if (x < -30000000 || z < -30000000 || x >= 30000000 || z >= 30000000) {
            return this.func_181545_F() + 1;
        }
        if (!this.isChunkLoaded(x >> 4, z >> 4, true)) {
            return 0;
        }
        final Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
        return chunk.getLowestHeight();
    }
    
    public int getLightFromNeighborsFor(final EnumSkyBlock type, BlockPos pos) {
        if (this.provider.getHasNoSky() && type == EnumSkyBlock.SKY) {
            return 0;
        }
        if (pos.getY() < 0) {
            pos = new BlockPos(pos.getX(), 0, pos.getZ());
        }
        if (!this.isValid(pos)) {
            return type.defaultLightValue;
        }
        if (!this.isBlockLoaded(pos)) {
            return type.defaultLightValue;
        }
        if (this.getBlockState(pos).getBlock().getUseNeighborBrightness()) {
            int i1 = this.getLightFor(type, pos.up());
            final int j = this.getLightFor(type, pos.east());
            final int k = this.getLightFor(type, pos.west());
            final int l = this.getLightFor(type, pos.south());
            final int m = this.getLightFor(type, pos.north());
            if (j > i1) {
                i1 = j;
            }
            if (k > i1) {
                i1 = k;
            }
            if (l > i1) {
                i1 = l;
            }
            if (m > i1) {
                i1 = m;
            }
            return i1;
        }
        final Chunk chunk = this.getChunkFromBlockCoords(pos);
        return chunk.getLightFor(type, pos);
    }
    
    public int getLightFor(final EnumSkyBlock type, BlockPos pos) {
        if (pos.getY() < 0) {
            pos = new BlockPos(pos.getX(), 0, pos.getZ());
        }
        if (!this.isValid(pos)) {
            return type.defaultLightValue;
        }
        if (!this.isBlockLoaded(pos)) {
            return type.defaultLightValue;
        }
        final Chunk chunk = this.getChunkFromBlockCoords(pos);
        return chunk.getLightFor(type, pos);
    }
    
    public void setLightFor(final EnumSkyBlock type, final BlockPos pos, final int lightValue) {
        if (this.isValid(pos) && this.isBlockLoaded(pos)) {
            final Chunk chunk = this.getChunkFromBlockCoords(pos);
            chunk.setLightFor(type, pos, lightValue);
            this.notifyLightSet(pos);
        }
    }
    
    public void notifyLightSet(final BlockPos pos) {
        for (int i = 0; i < this.worldAccesses.size(); ++i) {
            this.worldAccesses.get(i).notifyLightSet(pos);
        }
    }
    
    @Override
    public int getCombinedLight(final BlockPos pos, final int lightValue) {
        final int i = this.getLightFromNeighborsFor(EnumSkyBlock.SKY, pos);
        int j = this.getLightFromNeighborsFor(EnumSkyBlock.BLOCK, pos);
        if (j < lightValue) {
            j = lightValue;
        }
        return i << 20 | j << 4;
    }
    
    public float getLightBrightness(final BlockPos pos) {
        return this.provider.getLightBrightnessTable()[this.getLightFromNeighbors(pos)];
    }
    
    @Override
    public IBlockState getBlockState(final BlockPos pos) {
        if (!this.isValid(pos)) {
            return Blocks.air.getDefaultState();
        }
        final Chunk chunk = this.getChunkFromBlockCoords(pos);
        return chunk.getBlockState(pos);
    }
    
    public boolean isDaytime() {
        return this.skylightSubtracted < 4;
    }
    
    public MovingObjectPosition rayTraceBlocks(final Vec3 p_72933_1_, final Vec3 p_72933_2_) {
        return this.rayTraceBlocks(p_72933_1_, p_72933_2_, false, false, false);
    }
    
    public MovingObjectPosition rayTraceBlocks(final Vec3 start, final Vec3 end, final boolean stopOnLiquid) {
        return this.rayTraceBlocks(start, end, stopOnLiquid, false, false);
    }
    
    public MovingObjectPosition rayTraceBlocks(Vec3 vec31, final Vec3 vec32, final boolean stopOnLiquid, final boolean ignoreBlockWithoutBoundingBox, final boolean returnLastUncollidableBlock) {
        if (Double.isNaN(vec31.xCoord) || Double.isNaN(vec31.yCoord) || Double.isNaN(vec31.zCoord)) {
            return null;
        }
        if (!Double.isNaN(vec32.xCoord) && !Double.isNaN(vec32.yCoord) && !Double.isNaN(vec32.zCoord)) {
            final int i = MathHelper.floor_double(vec32.xCoord);
            final int j = MathHelper.floor_double(vec32.yCoord);
            final int k = MathHelper.floor_double(vec32.zCoord);
            int l = MathHelper.floor_double(vec31.xCoord);
            int i2 = MathHelper.floor_double(vec31.yCoord);
            int j2 = MathHelper.floor_double(vec31.zCoord);
            BlockPos blockpos = new BlockPos(l, i2, j2);
            final IBlockState iblockstate = this.getBlockState(blockpos);
            final Block block = iblockstate.getBlock();
            if ((!ignoreBlockWithoutBoundingBox || block.getCollisionBoundingBox(this, blockpos, iblockstate) != null) && block.canCollideCheck(iblockstate, stopOnLiquid)) {
                final MovingObjectPosition movingobjectposition = block.collisionRayTrace(this, blockpos, vec31, vec32);
                if (movingobjectposition != null) {
                    return movingobjectposition;
                }
            }
            MovingObjectPosition movingobjectposition2 = null;
            int k2 = 200;
            while (k2-- >= 0) {
                if (Double.isNaN(vec31.xCoord) || Double.isNaN(vec31.yCoord) || Double.isNaN(vec31.zCoord)) {
                    return null;
                }
                if (l == i && i2 == j && j2 == k) {
                    return returnLastUncollidableBlock ? movingobjectposition2 : null;
                }
                boolean flag2 = true;
                boolean flag3 = true;
                boolean flag4 = true;
                double d0 = 999.0;
                double d2 = 999.0;
                double d3 = 999.0;
                if (i > l) {
                    d0 = l + 1.0;
                }
                else if (i < l) {
                    d0 = l + 0.0;
                }
                else {
                    flag2 = false;
                }
                if (j > i2) {
                    d2 = i2 + 1.0;
                }
                else if (j < i2) {
                    d2 = i2 + 0.0;
                }
                else {
                    flag3 = false;
                }
                if (k > j2) {
                    d3 = j2 + 1.0;
                }
                else if (k < j2) {
                    d3 = j2 + 0.0;
                }
                else {
                    flag4 = false;
                }
                double d4 = 999.0;
                double d5 = 999.0;
                double d6 = 999.0;
                final double d7 = vec32.xCoord - vec31.xCoord;
                final double d8 = vec32.yCoord - vec31.yCoord;
                final double d9 = vec32.zCoord - vec31.zCoord;
                if (flag2) {
                    d4 = (d0 - vec31.xCoord) / d7;
                }
                if (flag3) {
                    d5 = (d2 - vec31.yCoord) / d8;
                }
                if (flag4) {
                    d6 = (d3 - vec31.zCoord) / d9;
                }
                if (d4 == -0.0) {
                    d4 = -1.0E-4;
                }
                if (d5 == -0.0) {
                    d5 = -1.0E-4;
                }
                if (d6 == -0.0) {
                    d6 = -1.0E-4;
                }
                EnumFacing enumfacing;
                if (d4 < d5 && d4 < d6) {
                    enumfacing = ((i > l) ? EnumFacing.WEST : EnumFacing.EAST);
                    vec31 = new Vec3(d0, vec31.yCoord + d8 * d4, vec31.zCoord + d9 * d4);
                }
                else if (d5 < d6) {
                    enumfacing = ((j > i2) ? EnumFacing.DOWN : EnumFacing.UP);
                    vec31 = new Vec3(vec31.xCoord + d7 * d5, d2, vec31.zCoord + d9 * d5);
                }
                else {
                    enumfacing = ((k > j2) ? EnumFacing.NORTH : EnumFacing.SOUTH);
                    vec31 = new Vec3(vec31.xCoord + d7 * d6, vec31.yCoord + d8 * d6, d3);
                }
                l = MathHelper.floor_double(vec31.xCoord) - ((enumfacing == EnumFacing.EAST) ? 1 : 0);
                i2 = MathHelper.floor_double(vec31.yCoord) - ((enumfacing == EnumFacing.UP) ? 1 : 0);
                j2 = MathHelper.floor_double(vec31.zCoord) - ((enumfacing == EnumFacing.SOUTH) ? 1 : 0);
                blockpos = new BlockPos(l, i2, j2);
                final IBlockState iblockstate2 = this.getBlockState(blockpos);
                final Block block2 = iblockstate2.getBlock();
                if (ignoreBlockWithoutBoundingBox && block2.getCollisionBoundingBox(this, blockpos, iblockstate2) == null) {
                    continue;
                }
                if (block2.canCollideCheck(iblockstate2, stopOnLiquid)) {
                    final MovingObjectPosition movingobjectposition3 = block2.collisionRayTrace(this, blockpos, vec31, vec32);
                    if (movingobjectposition3 != null) {
                        return movingobjectposition3;
                    }
                    continue;
                }
                else {
                    movingobjectposition2 = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec31, enumfacing, blockpos);
                }
            }
            return returnLastUncollidableBlock ? movingobjectposition2 : null;
        }
        return null;
    }
    
    public void playSoundAtEntity(final Entity entityIn, final String name, final float volume, final float pitch) {
        for (int i = 0; i < this.worldAccesses.size(); ++i) {
            this.worldAccesses.get(i).playSound(name, entityIn.posX, entityIn.posY, entityIn.posZ, volume, pitch);
        }
    }
    
    public void playSoundToNearExcept(final EntityPlayer player, final String name, final float volume, final float pitch) {
        for (int i = 0; i < this.worldAccesses.size(); ++i) {
            this.worldAccesses.get(i).playSoundToNearExcept(player, name, player.posX, player.posY, player.posZ, volume, pitch);
        }
    }
    
    public void playSoundEffect(final double x, final double y, final double z, final String soundName, final float volume, final float pitch) {
        for (int i = 0; i < this.worldAccesses.size(); ++i) {
            this.worldAccesses.get(i).playSound(soundName, x, y, z, volume, pitch);
        }
    }
    
    public void playSound(final double x, final double y, final double z, final String soundName, final float volume, final float pitch, final boolean distanceDelay) {
    }
    
    public void playRecord(final BlockPos pos, final String name) {
        for (int i = 0; i < this.worldAccesses.size(); ++i) {
            this.worldAccesses.get(i).playRecord(name, pos);
        }
    }
    
    public void spawnParticle(final EnumParticleTypes particleType, final double xCoord, final double yCoord, final double zCoord, final double xOffset, final double yOffset, final double zOffset, final int... p_175688_14_) {
        this.spawnParticle(particleType.getParticleID(), particleType.getShouldIgnoreRange(), xCoord, yCoord, zCoord, xOffset, yOffset, zOffset, p_175688_14_);
    }
    
    public void spawnParticle(final EnumParticleTypes particleType, final boolean p_175682_2_, final double xCoord, final double yCoord, final double zCoord, final double xOffset, final double yOffset, final double zOffset, final int... p_175682_15_) {
        this.spawnParticle(particleType.getParticleID(), particleType.getShouldIgnoreRange() | p_175682_2_, xCoord, yCoord, zCoord, xOffset, yOffset, zOffset, p_175682_15_);
    }
    
    private void spawnParticle(final int particleID, final boolean p_175720_2_, final double xCood, final double yCoord, final double zCoord, final double xOffset, final double yOffset, final double zOffset, final int... p_175720_15_) {
        for (int i = 0; i < this.worldAccesses.size(); ++i) {
            this.worldAccesses.get(i).spawnParticle(particleID, p_175720_2_, xCood, yCoord, zCoord, xOffset, yOffset, zOffset, p_175720_15_);
        }
    }
    
    public boolean addWeatherEffect(final Entity entityIn) {
        this.weatherEffects.add(entityIn);
        return true;
    }
    
    public boolean spawnEntityInWorld(final Entity entityIn) {
        final int i = MathHelper.floor_double(entityIn.posX / 16.0);
        final int j = MathHelper.floor_double(entityIn.posZ / 16.0);
        boolean flag = entityIn.forceSpawn;
        if (entityIn instanceof EntityPlayer) {
            flag = true;
        }
        if (!flag && !this.isChunkLoaded(i, j, true)) {
            return false;
        }
        if (entityIn instanceof EntityPlayer) {
            final EntityPlayer entityplayer = (EntityPlayer)entityIn;
            this.playerEntities.add(entityplayer);
            this.updateAllPlayersSleepingFlag();
        }
        this.getChunkFromChunkCoords(i, j).addEntity(entityIn);
        this.loadedEntityList.add(entityIn);
        this.onEntityAdded(entityIn);
        return true;
    }
    
    protected void onEntityAdded(final Entity entityIn) {
        for (int i = 0; i < this.worldAccesses.size(); ++i) {
            this.worldAccesses.get(i).onEntityAdded(entityIn);
        }
    }
    
    protected void onEntityRemoved(final Entity entityIn) {
        for (int i = 0; i < this.worldAccesses.size(); ++i) {
            this.worldAccesses.get(i).onEntityRemoved(entityIn);
        }
    }
    
    public void removeEntity(final Entity entityIn) {
        if (entityIn.riddenByEntity != null) {
            entityIn.riddenByEntity.mountEntity(null);
        }
        if (entityIn.ridingEntity != null) {
            entityIn.mountEntity(null);
        }
        entityIn.setDead();
        if (entityIn instanceof EntityPlayer) {
            this.playerEntities.remove(entityIn);
            this.updateAllPlayersSleepingFlag();
            this.onEntityRemoved(entityIn);
        }
    }
    
    public void removePlayerEntityDangerously(final Entity entityIn) {
        entityIn.setDead();
        if (entityIn instanceof EntityPlayer) {
            this.playerEntities.remove(entityIn);
            this.updateAllPlayersSleepingFlag();
        }
        final int i = entityIn.chunkCoordX;
        final int j = entityIn.chunkCoordZ;
        if (entityIn.addedToChunk && this.isChunkLoaded(i, j, true)) {
            this.getChunkFromChunkCoords(i, j).removeEntity(entityIn);
        }
        this.loadedEntityList.remove(entityIn);
        this.onEntityRemoved(entityIn);
    }
    
    public void addWorldAccess(final IWorldAccess worldAccess) {
        this.worldAccesses.add(worldAccess);
    }
    
    public void removeWorldAccess(final IWorldAccess worldAccess) {
        this.worldAccesses.remove(worldAccess);
    }
    
    public List<AxisAlignedBB> getCollidingBoundingBoxes(final Entity entityIn, final AxisAlignedBB bb) {
        final List<AxisAlignedBB> list = (List<AxisAlignedBB>)Lists.newArrayList();
        final int i = MathHelper.floor_double(bb.minX);
        final int j = MathHelper.floor_double(bb.maxX + 1.0);
        final int k = MathHelper.floor_double(bb.minY);
        final int l = MathHelper.floor_double(bb.maxY + 1.0);
        final int i2 = MathHelper.floor_double(bb.minZ);
        final int j2 = MathHelper.floor_double(bb.maxZ + 1.0);
        final WorldBorder worldborder = this.getWorldBorder();
        final boolean flag = entityIn.isOutsideBorder();
        final boolean flag2 = this.isInsideBorder(worldborder, entityIn);
        final IBlockState iblockstate = Blocks.stone.getDefaultState();
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int k2 = i; k2 < j; ++k2) {
            for (int l2 = i2; l2 < j2; ++l2) {
                if (this.isBlockLoaded(blockpos$mutableblockpos.func_181079_c(k2, 64, l2))) {
                    for (int i3 = k - 1; i3 < l; ++i3) {
                        blockpos$mutableblockpos.func_181079_c(k2, i3, l2);
                        if (flag && flag2) {
                            entityIn.setOutsideBorder(false);
                        }
                        else if (!flag && !flag2) {
                            entityIn.setOutsideBorder(true);
                        }
                        IBlockState iblockstate2 = iblockstate;
                        if (worldborder.contains(blockpos$mutableblockpos) || !flag2) {
                            iblockstate2 = this.getBlockState(blockpos$mutableblockpos);
                        }
                        iblockstate2.getBlock().addCollisionBoxesToList(this, blockpos$mutableblockpos, iblockstate2, bb, list, entityIn);
                    }
                }
            }
        }
        final double d0 = 0.25;
        final List<Entity> list2 = this.getEntitiesWithinAABBExcludingEntity(entityIn, bb.expand(d0, d0, d0));
        for (int j3 = 0; j3 < list2.size(); ++j3) {
            if (entityIn.riddenByEntity != list2 && entityIn.ridingEntity != list2) {
                AxisAlignedBB axisalignedbb = list2.get(j3).getCollisionBoundingBox();
                if (axisalignedbb != null && axisalignedbb.intersectsWith(bb)) {
                    list.add(axisalignedbb);
                }
                axisalignedbb = entityIn.getCollisionBox(list2.get(j3));
                if (axisalignedbb != null && axisalignedbb.intersectsWith(bb)) {
                    list.add(axisalignedbb);
                }
            }
        }
        return list;
    }
    
    public boolean isInsideBorder(final WorldBorder worldBorderIn, final Entity entityIn) {
        double d0 = worldBorderIn.minX();
        double d2 = worldBorderIn.minZ();
        double d3 = worldBorderIn.maxX();
        double d4 = worldBorderIn.maxZ();
        if (entityIn.isOutsideBorder()) {
            ++d0;
            ++d2;
            --d3;
            --d4;
        }
        else {
            --d0;
            --d2;
            ++d3;
            ++d4;
        }
        return entityIn.posX > d0 && entityIn.posX < d3 && entityIn.posZ > d2 && entityIn.posZ < d4;
    }
    
    public List<AxisAlignedBB> func_147461_a(final AxisAlignedBB bb) {
        final List<AxisAlignedBB> list = (List<AxisAlignedBB>)Lists.newArrayList();
        final int i = MathHelper.floor_double(bb.minX);
        final int j = MathHelper.floor_double(bb.maxX + 1.0);
        final int k = MathHelper.floor_double(bb.minY);
        final int l = MathHelper.floor_double(bb.maxY + 1.0);
        final int i2 = MathHelper.floor_double(bb.minZ);
        final int j2 = MathHelper.floor_double(bb.maxZ + 1.0);
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int k2 = i; k2 < j; ++k2) {
            for (int l2 = i2; l2 < j2; ++l2) {
                if (this.isBlockLoaded(blockpos$mutableblockpos.func_181079_c(k2, 64, l2))) {
                    for (int i3 = k - 1; i3 < l; ++i3) {
                        blockpos$mutableblockpos.func_181079_c(k2, i3, l2);
                        IBlockState iblockstate;
                        if (k2 >= -30000000 && k2 < 30000000 && l2 >= -30000000 && l2 < 30000000) {
                            iblockstate = this.getBlockState(blockpos$mutableblockpos);
                        }
                        else {
                            iblockstate = Blocks.bedrock.getDefaultState();
                        }
                        iblockstate.getBlock().addCollisionBoxesToList(this, blockpos$mutableblockpos, iblockstate, bb, list, null);
                    }
                }
            }
        }
        return list;
    }
    
    public int calculateSkylightSubtracted(final float p_72967_1_) {
        final float f = this.getCelestialAngle(p_72967_1_);
        float f2 = 1.0f - (MathHelper.cos(f * 3.1415927f * 2.0f) * 2.0f + 0.5f);
        f2 = MathHelper.clamp_float(f2, 0.0f, 1.0f);
        f2 = 1.0f - f2;
        f2 *= (float)(1.0 - this.getRainStrength(p_72967_1_) * 5.0f / 16.0);
        f2 *= (float)(1.0 - this.getThunderStrength(p_72967_1_) * 5.0f / 16.0);
        f2 = 1.0f - f2;
        return (int)(f2 * 11.0f);
    }
    
    public float getSunBrightness(final float p_72971_1_) {
        final float f = this.getCelestialAngle(p_72971_1_);
        float f2 = 1.0f - (MathHelper.cos(f * 3.1415927f * 2.0f) * 2.0f + 0.2f);
        f2 = MathHelper.clamp_float(f2, 0.0f, 1.0f);
        f2 = 1.0f - f2;
        f2 *= (float)(1.0 - this.getRainStrength(p_72971_1_) * 5.0f / 16.0);
        f2 *= (float)(1.0 - this.getThunderStrength(p_72971_1_) * 5.0f / 16.0);
        return f2 * 0.8f + 0.2f;
    }
    
    public Vec3 getSkyColor(final Entity entityIn, final float partialTicks) {
        final float f = this.getCelestialAngle(partialTicks);
        float f2 = MathHelper.cos(f * 3.1415927f * 2.0f) * 2.0f + 0.5f;
        f2 = MathHelper.clamp_float(f2, 0.0f, 1.0f);
        final int i = MathHelper.floor_double(entityIn.posX);
        final int j = MathHelper.floor_double(entityIn.posY);
        final int k = MathHelper.floor_double(entityIn.posZ);
        final BlockPos blockpos = new BlockPos(i, j, k);
        final BiomeGenBase biomegenbase = this.getBiomeGenForCoords(blockpos);
        final float f3 = biomegenbase.getFloatTemperature(blockpos);
        final int l = biomegenbase.getSkyColorByTemp(f3);
        float f4 = (l >> 16 & 0xFF) / 255.0f;
        float f5 = (l >> 8 & 0xFF) / 255.0f;
        float f6 = (l & 0xFF) / 255.0f;
        f4 *= f2;
        f5 *= f2;
        f6 *= f2;
        final float f7 = this.getRainStrength(partialTicks);
        if (f7 > 0.0f) {
            final float f8 = (f4 * 0.3f + f5 * 0.59f + f6 * 0.11f) * 0.6f;
            final float f9 = 1.0f - f7 * 0.75f;
            f4 = f4 * f9 + f8 * (1.0f - f9);
            f5 = f5 * f9 + f8 * (1.0f - f9);
            f6 = f6 * f9 + f8 * (1.0f - f9);
        }
        final float f10 = this.getThunderStrength(partialTicks);
        if (f10 > 0.0f) {
            final float f11 = (f4 * 0.3f + f5 * 0.59f + f6 * 0.11f) * 0.2f;
            final float f12 = 1.0f - f10 * 0.75f;
            f4 = f4 * f12 + f11 * (1.0f - f12);
            f5 = f5 * f12 + f11 * (1.0f - f12);
            f6 = f6 * f12 + f11 * (1.0f - f12);
        }
        if (this.lastLightningBolt > 0) {
            float f13 = this.lastLightningBolt - partialTicks;
            if (f13 > 1.0f) {
                f13 = 1.0f;
            }
            f13 *= 0.45f;
            f4 = f4 * (1.0f - f13) + 0.8f * f13;
            f5 = f5 * (1.0f - f13) + 0.8f * f13;
            f6 = f6 * (1.0f - f13) + 1.0f * f13;
        }
        return new Vec3(f4, f5, f6);
    }
    
    public float getCelestialAngle(final float partialTicks) {
        return this.provider.calculateCelestialAngle(this.worldInfo.getWorldTime(), partialTicks);
    }
    
    public int getMoonPhase() {
        return this.provider.getMoonPhase(this.worldInfo.getWorldTime());
    }
    
    public float getCurrentMoonPhaseFactor() {
        return WorldProvider.moonPhaseFactors[this.provider.getMoonPhase(this.worldInfo.getWorldTime())];
    }
    
    public float getCelestialAngleRadians(final float partialTicks) {
        final float f = this.getCelestialAngle(partialTicks);
        return f * 3.1415927f * 2.0f;
    }
    
    public Vec3 getCloudColour(final float partialTicks) {
        final float f = this.getCelestialAngle(partialTicks);
        float f2 = MathHelper.cos(f * 3.1415927f * 2.0f) * 2.0f + 0.5f;
        f2 = MathHelper.clamp_float(f2, 0.0f, 1.0f);
        float f3 = (this.cloudColour >> 16 & 0xFFL) / 255.0f;
        float f4 = (this.cloudColour >> 8 & 0xFFL) / 255.0f;
        float f5 = (this.cloudColour & 0xFFL) / 255.0f;
        final float f6 = this.getRainStrength(partialTicks);
        if (f6 > 0.0f) {
            final float f7 = (f3 * 0.3f + f4 * 0.59f + f5 * 0.11f) * 0.6f;
            final float f8 = 1.0f - f6 * 0.95f;
            f3 = f3 * f8 + f7 * (1.0f - f8);
            f4 = f4 * f8 + f7 * (1.0f - f8);
            f5 = f5 * f8 + f7 * (1.0f - f8);
        }
        f3 *= f2 * 0.9f + 0.1f;
        f4 *= f2 * 0.9f + 0.1f;
        f5 *= f2 * 0.85f + 0.15f;
        final float f9 = this.getThunderStrength(partialTicks);
        if (f9 > 0.0f) {
            final float f10 = (f3 * 0.3f + f4 * 0.59f + f5 * 0.11f) * 0.2f;
            final float f11 = 1.0f - f9 * 0.95f;
            f3 = f3 * f11 + f10 * (1.0f - f11);
            f4 = f4 * f11 + f10 * (1.0f - f11);
            f5 = f5 * f11 + f10 * (1.0f - f11);
        }
        return new Vec3(f3, f4, f5);
    }
    
    public Vec3 getFogColor(final float partialTicks) {
        final float f = this.getCelestialAngle(partialTicks);
        return this.provider.getFogColor(f, partialTicks);
    }
    
    public BlockPos getPrecipitationHeight(final BlockPos pos) {
        return this.getChunkFromBlockCoords(pos).getPrecipitationHeight(pos);
    }
    
    public BlockPos getTopSolidOrLiquidBlock(final BlockPos pos) {
        final Chunk chunk = this.getChunkFromBlockCoords(pos);
        BlockPos blockpos;
        BlockPos blockpos2;
        for (blockpos = new BlockPos(pos.getX(), chunk.getTopFilledSegment() + 16, pos.getZ()); blockpos.getY() >= 0; blockpos = blockpos2) {
            blockpos2 = blockpos.down();
            final Material material = chunk.getBlock(blockpos2).getMaterial();
            if (material.blocksMovement() && material != Material.leaves) {
                break;
            }
        }
        return blockpos;
    }
    
    public float getStarBrightness(final float partialTicks) {
        final float f = this.getCelestialAngle(partialTicks);
        float f2 = 1.0f - (MathHelper.cos(f * 3.1415927f * 2.0f) * 2.0f + 0.25f);
        f2 = MathHelper.clamp_float(f2, 0.0f, 1.0f);
        return f2 * f2 * 0.5f;
    }
    
    public void scheduleUpdate(final BlockPos pos, final Block blockIn, final int delay) {
    }
    
    public void updateBlockTick(final BlockPos pos, final Block blockIn, final int delay, final int priority) {
    }
    
    public void scheduleBlockUpdate(final BlockPos pos, final Block blockIn, final int delay, final int priority) {
    }
    
    public void updateEntities() {
        this.theProfiler.startSection("entities");
        this.theProfiler.startSection("global");
        for (int i = 0; i < this.weatherEffects.size(); ++i) {
            final Entity entity = this.weatherEffects.get(i);
            try {
                final Entity entity4 = entity;
                ++entity4.ticksExisted;
                entity.onUpdate();
            }
            catch (Throwable throwable2) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable2, "Ticking entity");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being ticked");
                if (entity == null) {
                    crashreportcategory.addCrashSection("Entity", "~~NULL~~");
                }
                else {
                    entity.addEntityCrashInfo(crashreportcategory);
                }
                throw new ReportedException(crashreport);
            }
            if (entity.isDead) {
                this.weatherEffects.remove(i--);
            }
        }
        this.theProfiler.endStartSection("remove");
        this.loadedEntityList.removeAll(this.unloadedEntityList);
        for (int k = 0; k < this.unloadedEntityList.size(); ++k) {
            final Entity entity2 = this.unloadedEntityList.get(k);
            final int j = entity2.chunkCoordX;
            final int l1 = entity2.chunkCoordZ;
            if (entity2.addedToChunk && this.isChunkLoaded(j, l1, true)) {
                this.getChunkFromChunkCoords(j, l1).removeEntity(entity2);
            }
        }
        for (int m = 0; m < this.unloadedEntityList.size(); ++m) {
            this.onEntityRemoved(this.unloadedEntityList.get(m));
        }
        this.unloadedEntityList.clear();
        this.theProfiler.endStartSection("regular");
        for (int i2 = 0; i2 < this.loadedEntityList.size(); ++i2) {
            final Entity entity3 = this.loadedEntityList.get(i2);
            if (entity3.ridingEntity != null) {
                if (!entity3.ridingEntity.isDead && entity3.ridingEntity.riddenByEntity == entity3) {
                    continue;
                }
                entity3.ridingEntity.riddenByEntity = null;
                entity3.ridingEntity = null;
            }
            this.theProfiler.startSection("tick");
            if (!entity3.isDead) {
                try {
                    this.updateEntity(entity3);
                }
                catch (Throwable throwable3) {
                    final CrashReport crashreport2 = CrashReport.makeCrashReport(throwable3, "Ticking entity");
                    final CrashReportCategory crashreportcategory2 = crashreport2.makeCategory("Entity being ticked");
                    entity3.addEntityCrashInfo(crashreportcategory2);
                    throw new ReportedException(crashreport2);
                }
            }
            this.theProfiler.endSection();
            this.theProfiler.startSection("remove");
            if (entity3.isDead) {
                final int k2 = entity3.chunkCoordX;
                final int i3 = entity3.chunkCoordZ;
                if (entity3.addedToChunk && this.isChunkLoaded(k2, i3, true)) {
                    this.getChunkFromChunkCoords(k2, i3).removeEntity(entity3);
                }
                this.loadedEntityList.remove(i2--);
                this.onEntityRemoved(entity3);
            }
            this.theProfiler.endSection();
        }
        this.theProfiler.endStartSection("blockEntities");
        this.processingLoadedTiles = true;
        final Iterator<TileEntity> iterator = this.tickableTileEntities.iterator();
        while (iterator.hasNext()) {
            final TileEntity tileentity = iterator.next();
            if (!tileentity.isInvalid() && tileentity.hasWorldObj()) {
                final BlockPos blockpos = tileentity.getPos();
                if (this.isBlockLoaded(blockpos) && this.worldBorder.contains(blockpos)) {
                    try {
                        ((ITickable)tileentity).update();
                    }
                    catch (Throwable throwable4) {
                        final CrashReport crashreport3 = CrashReport.makeCrashReport(throwable4, "Ticking block entity");
                        final CrashReportCategory crashreportcategory3 = crashreport3.makeCategory("Block entity being ticked");
                        tileentity.addInfoToCrashReport(crashreportcategory3);
                        throw new ReportedException(crashreport3);
                    }
                }
            }
            if (tileentity.isInvalid()) {
                iterator.remove();
                this.loadedTileEntityList.remove(tileentity);
                if (!this.isBlockLoaded(tileentity.getPos())) {
                    continue;
                }
                this.getChunkFromBlockCoords(tileentity.getPos()).removeTileEntity(tileentity.getPos());
            }
        }
        this.processingLoadedTiles = false;
        if (!this.tileEntitiesToBeRemoved.isEmpty()) {
            this.tickableTileEntities.removeAll(this.tileEntitiesToBeRemoved);
            this.loadedTileEntityList.removeAll(this.tileEntitiesToBeRemoved);
            this.tileEntitiesToBeRemoved.clear();
        }
        this.theProfiler.endStartSection("pendingBlockEntities");
        if (!this.addedTileEntityList.isEmpty()) {
            for (int j2 = 0; j2 < this.addedTileEntityList.size(); ++j2) {
                final TileEntity tileentity2 = this.addedTileEntityList.get(j2);
                if (!tileentity2.isInvalid()) {
                    if (!this.loadedTileEntityList.contains(tileentity2)) {
                        this.addTileEntity(tileentity2);
                    }
                    if (this.isBlockLoaded(tileentity2.getPos())) {
                        this.getChunkFromBlockCoords(tileentity2.getPos()).addTileEntity(tileentity2.getPos(), tileentity2);
                    }
                    this.markBlockForUpdate(tileentity2.getPos());
                }
            }
            this.addedTileEntityList.clear();
        }
        this.theProfiler.endSection();
        this.theProfiler.endSection();
    }
    
    public boolean addTileEntity(final TileEntity tile) {
        final boolean flag = this.loadedTileEntityList.add(tile);
        if (flag && tile instanceof ITickable) {
            this.tickableTileEntities.add(tile);
        }
        return flag;
    }
    
    public void addTileEntities(final Collection<TileEntity> tileEntityCollection) {
        if (this.processingLoadedTiles) {
            this.addedTileEntityList.addAll(tileEntityCollection);
        }
        else {
            for (final TileEntity tileentity : tileEntityCollection) {
                this.loadedTileEntityList.add(tileentity);
                if (tileentity instanceof ITickable) {
                    this.tickableTileEntities.add(tileentity);
                }
            }
        }
    }
    
    public void updateEntity(final Entity ent) {
        this.updateEntityWithOptionalForce(ent, true);
    }
    
    public void updateEntityWithOptionalForce(final Entity entityIn, final boolean forceUpdate) {
        final int i = MathHelper.floor_double(entityIn.posX);
        final int j = MathHelper.floor_double(entityIn.posZ);
        final int k = 32;
        if (!forceUpdate || this.isAreaLoaded(i - k, 0, j - k, i + k, 0, j + k, true)) {
            entityIn.lastTickPosX = entityIn.posX;
            entityIn.lastTickPosY = entityIn.posY;
            entityIn.lastTickPosZ = entityIn.posZ;
            entityIn.prevRotationYaw = entityIn.rotationYaw;
            entityIn.prevRotationPitch = entityIn.rotationPitch;
            if (forceUpdate && entityIn.addedToChunk) {
                ++entityIn.ticksExisted;
                if (entityIn.ridingEntity != null) {
                    entityIn.updateRidden();
                }
                else {
                    entityIn.onUpdate();
                }
            }
            this.theProfiler.startSection("chunkCheck");
            if (Double.isNaN(entityIn.posX) || Double.isInfinite(entityIn.posX)) {
                entityIn.posX = entityIn.lastTickPosX;
            }
            if (Double.isNaN(entityIn.posY) || Double.isInfinite(entityIn.posY)) {
                entityIn.posY = entityIn.lastTickPosY;
            }
            if (Double.isNaN(entityIn.posZ) || Double.isInfinite(entityIn.posZ)) {
                entityIn.posZ = entityIn.lastTickPosZ;
            }
            if (Double.isNaN(entityIn.rotationPitch) || Double.isInfinite(entityIn.rotationPitch)) {
                entityIn.rotationPitch = entityIn.prevRotationPitch;
            }
            if (Double.isNaN(entityIn.rotationYaw) || Double.isInfinite(entityIn.rotationYaw)) {
                entityIn.rotationYaw = entityIn.prevRotationYaw;
            }
            final int l = MathHelper.floor_double(entityIn.posX / 16.0);
            final int i2 = MathHelper.floor_double(entityIn.posY / 16.0);
            final int j2 = MathHelper.floor_double(entityIn.posZ / 16.0);
            if (!entityIn.addedToChunk || entityIn.chunkCoordX != l || entityIn.chunkCoordY != i2 || entityIn.chunkCoordZ != j2) {
                if (entityIn.addedToChunk && this.isChunkLoaded(entityIn.chunkCoordX, entityIn.chunkCoordZ, true)) {
                    this.getChunkFromChunkCoords(entityIn.chunkCoordX, entityIn.chunkCoordZ).removeEntityAtIndex(entityIn, entityIn.chunkCoordY);
                }
                if (this.isChunkLoaded(l, j2, true)) {
                    entityIn.addedToChunk = true;
                    this.getChunkFromChunkCoords(l, j2).addEntity(entityIn);
                }
                else {
                    entityIn.addedToChunk = false;
                }
            }
            this.theProfiler.endSection();
            if (forceUpdate && entityIn.addedToChunk && entityIn.riddenByEntity != null) {
                if (!entityIn.riddenByEntity.isDead && entityIn.riddenByEntity.ridingEntity == entityIn) {
                    this.updateEntity(entityIn.riddenByEntity);
                }
                else {
                    entityIn.riddenByEntity.ridingEntity = null;
                    entityIn.riddenByEntity = null;
                }
            }
        }
    }
    
    public boolean checkNoEntityCollision(final AxisAlignedBB bb) {
        return this.checkNoEntityCollision(bb, null);
    }
    
    public boolean checkNoEntityCollision(final AxisAlignedBB bb, final Entity entityIn) {
        final List<Entity> list = this.getEntitiesWithinAABBExcludingEntity(null, bb);
        for (int i = 0; i < list.size(); ++i) {
            final Entity entity = list.get(i);
            if (!entity.isDead && entity.preventEntitySpawning && entity != entityIn && (entityIn == null || (entityIn.ridingEntity != entity && entityIn.riddenByEntity != entity))) {
                return false;
            }
        }
        return true;
    }
    
    public boolean checkBlockCollision(final AxisAlignedBB bb) {
        final int i = MathHelper.floor_double(bb.minX);
        final int j = MathHelper.floor_double(bb.maxX);
        final int k = MathHelper.floor_double(bb.minY);
        final int l = MathHelper.floor_double(bb.maxY);
        final int i2 = MathHelper.floor_double(bb.minZ);
        final int j2 = MathHelper.floor_double(bb.maxZ);
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int k2 = i; k2 <= j; ++k2) {
            for (int l2 = k; l2 <= l; ++l2) {
                for (int i3 = i2; i3 <= j2; ++i3) {
                    final Block block = this.getBlockState(blockpos$mutableblockpos.func_181079_c(k2, l2, i3)).getBlock();
                    if (block.getMaterial() != Material.air) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean isAnyLiquid(final AxisAlignedBB bb) {
        final int i = MathHelper.floor_double(bb.minX);
        final int j = MathHelper.floor_double(bb.maxX);
        final int k = MathHelper.floor_double(bb.minY);
        final int l = MathHelper.floor_double(bb.maxY);
        final int i2 = MathHelper.floor_double(bb.minZ);
        final int j2 = MathHelper.floor_double(bb.maxZ);
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int k2 = i; k2 <= j; ++k2) {
            for (int l2 = k; l2 <= l; ++l2) {
                for (int i3 = i2; i3 <= j2; ++i3) {
                    final Block block = this.getBlockState(blockpos$mutableblockpos.func_181079_c(k2, l2, i3)).getBlock();
                    if (block.getMaterial().isLiquid()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean isFlammableWithin(final AxisAlignedBB bb) {
        final int i = MathHelper.floor_double(bb.minX);
        final int j = MathHelper.floor_double(bb.maxX + 1.0);
        final int k = MathHelper.floor_double(bb.minY);
        final int l = MathHelper.floor_double(bb.maxY + 1.0);
        final int i2 = MathHelper.floor_double(bb.minZ);
        final int j2 = MathHelper.floor_double(bb.maxZ + 1.0);
        if (this.isAreaLoaded(i, k, i2, j, l, j2, true)) {
            final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            for (int k2 = i; k2 < j; ++k2) {
                for (int l2 = k; l2 < l; ++l2) {
                    for (int i3 = i2; i3 < j2; ++i3) {
                        final Block block = this.getBlockState(blockpos$mutableblockpos.func_181079_c(k2, l2, i3)).getBlock();
                        if (block == Blocks.fire || block == Blocks.flowing_lava || block == Blocks.lava) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public boolean handleMaterialAcceleration(final AxisAlignedBB bb, final Material materialIn, final Entity entityIn) {
        final int i = MathHelper.floor_double(bb.minX);
        final int j = MathHelper.floor_double(bb.maxX + 1.0);
        final int k = MathHelper.floor_double(bb.minY);
        final int l = MathHelper.floor_double(bb.maxY + 1.0);
        final int i2 = MathHelper.floor_double(bb.minZ);
        final int j2 = MathHelper.floor_double(bb.maxZ + 1.0);
        if (!this.isAreaLoaded(i, k, i2, j, l, j2, true)) {
            return false;
        }
        boolean flag = false;
        Vec3 vec3 = new Vec3(0.0, 0.0, 0.0);
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int k2 = i; k2 < j; ++k2) {
            for (int l2 = k; l2 < l; ++l2) {
                for (int i3 = i2; i3 < j2; ++i3) {
                    blockpos$mutableblockpos.func_181079_c(k2, l2, i3);
                    final IBlockState iblockstate = this.getBlockState(blockpos$mutableblockpos);
                    final Block block = iblockstate.getBlock();
                    if (block.getMaterial() == materialIn) {
                        final double d0 = l2 + 1 - BlockLiquid.getLiquidHeightPercent(iblockstate.getValue((IProperty<Integer>)BlockLiquid.LEVEL));
                        if (l >= d0) {
                            flag = true;
                            vec3 = block.modifyAcceleration(this, blockpos$mutableblockpos, entityIn, vec3);
                        }
                    }
                }
            }
        }
        if (vec3.lengthVector() > 0.0 && entityIn.isPushedByWater()) {
            vec3 = vec3.normalize();
            final double d2 = 0.014;
            entityIn.motionX += vec3.xCoord * d2;
            entityIn.motionY += vec3.yCoord * d2;
            entityIn.motionZ += vec3.zCoord * d2;
        }
        return flag;
    }
    
    public boolean isMaterialInBB(final AxisAlignedBB bb, final Material materialIn) {
        final int i = MathHelper.floor_double(bb.minX);
        final int j = MathHelper.floor_double(bb.maxX + 1.0);
        final int k = MathHelper.floor_double(bb.minY);
        final int l = MathHelper.floor_double(bb.maxY + 1.0);
        final int i2 = MathHelper.floor_double(bb.minZ);
        final int j2 = MathHelper.floor_double(bb.maxZ + 1.0);
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int k2 = i; k2 < j; ++k2) {
            for (int l2 = k; l2 < l; ++l2) {
                for (int i3 = i2; i3 < j2; ++i3) {
                    if (this.getBlockState(blockpos$mutableblockpos.func_181079_c(k2, l2, i3)).getBlock().getMaterial() == materialIn) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean isAABBInMaterial(final AxisAlignedBB bb, final Material materialIn) {
        final int i = MathHelper.floor_double(bb.minX);
        final int j = MathHelper.floor_double(bb.maxX + 1.0);
        final int k = MathHelper.floor_double(bb.minY);
        final int l = MathHelper.floor_double(bb.maxY + 1.0);
        final int i2 = MathHelper.floor_double(bb.minZ);
        final int j2 = MathHelper.floor_double(bb.maxZ + 1.0);
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int k2 = i; k2 < j; ++k2) {
            for (int l2 = k; l2 < l; ++l2) {
                for (int i3 = i2; i3 < j2; ++i3) {
                    final IBlockState iblockstate = this.getBlockState(blockpos$mutableblockpos.func_181079_c(k2, l2, i3));
                    final Block block = iblockstate.getBlock();
                    if (block.getMaterial() == materialIn) {
                        final int j3 = iblockstate.getValue((IProperty<Integer>)BlockLiquid.LEVEL);
                        double d0 = l2 + 1;
                        if (j3 < 8) {
                            d0 = l2 + 1 - j3 / 8.0;
                        }
                        if (d0 >= bb.minY) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public Explosion createExplosion(final Entity entityIn, final double x, final double y, final double z, final float strength, final boolean isSmoking) {
        return this.newExplosion(entityIn, x, y, z, strength, false, isSmoking);
    }
    
    public Explosion newExplosion(final Entity entityIn, final double x, final double y, final double z, final float strength, final boolean isFlaming, final boolean isSmoking) {
        final Explosion explosion = new Explosion(this, entityIn, x, y, z, strength, isFlaming, isSmoking);
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        return explosion;
    }
    
    public float getBlockDensity(final Vec3 vec, final AxisAlignedBB bb) {
        final double d0 = 1.0 / ((bb.maxX - bb.minX) * 2.0 + 1.0);
        final double d2 = 1.0 / ((bb.maxY - bb.minY) * 2.0 + 1.0);
        final double d3 = 1.0 / ((bb.maxZ - bb.minZ) * 2.0 + 1.0);
        final double d4 = (1.0 - Math.floor(1.0 / d0) * d0) / 2.0;
        final double d5 = (1.0 - Math.floor(1.0 / d3) * d3) / 2.0;
        if (d0 >= 0.0 && d2 >= 0.0 && d3 >= 0.0) {
            int i = 0;
            int j = 0;
            for (float f = 0.0f; f <= 1.0f; f += (float)d0) {
                for (float f2 = 0.0f; f2 <= 1.0f; f2 += (float)d2) {
                    for (float f3 = 0.0f; f3 <= 1.0f; f3 += (float)d3) {
                        final double d6 = bb.minX + (bb.maxX - bb.minX) * f;
                        final double d7 = bb.minY + (bb.maxY - bb.minY) * f2;
                        final double d8 = bb.minZ + (bb.maxZ - bb.minZ) * f3;
                        if (this.rayTraceBlocks(new Vec3(d6 + d4, d7, d8 + d5), vec) == null) {
                            ++i;
                        }
                        ++j;
                    }
                }
            }
            return i / (float)j;
        }
        return 0.0f;
    }
    
    public boolean extinguishFire(final EntityPlayer player, BlockPos pos, final EnumFacing side) {
        pos = pos.offset(side);
        if (this.getBlockState(pos).getBlock() == Blocks.fire) {
            this.playAuxSFXAtEntity(player, 1004, pos, 0);
            this.setBlockToAir(pos);
            return true;
        }
        return false;
    }
    
    public String getDebugLoadedEntities() {
        return "All: " + this.loadedEntityList.size();
    }
    
    public String getProviderName() {
        return this.chunkProvider.makeString();
    }
    
    @Override
    public TileEntity getTileEntity(final BlockPos pos) {
        if (!this.isValid(pos)) {
            return null;
        }
        TileEntity tileentity = null;
        if (this.processingLoadedTiles) {
            for (int i = 0; i < this.addedTileEntityList.size(); ++i) {
                final TileEntity tileentity2 = this.addedTileEntityList.get(i);
                if (!tileentity2.isInvalid() && tileentity2.getPos().equals(pos)) {
                    tileentity = tileentity2;
                    break;
                }
            }
        }
        if (tileentity == null) {
            tileentity = this.getChunkFromBlockCoords(pos).getTileEntity(pos, Chunk.EnumCreateEntityType.IMMEDIATE);
        }
        if (tileentity == null) {
            for (int j = 0; j < this.addedTileEntityList.size(); ++j) {
                final TileEntity tileentity3 = this.addedTileEntityList.get(j);
                if (!tileentity3.isInvalid() && tileentity3.getPos().equals(pos)) {
                    tileentity = tileentity3;
                    break;
                }
            }
        }
        return tileentity;
    }
    
    public void setTileEntity(final BlockPos pos, final TileEntity tileEntityIn) {
        if (tileEntityIn != null && !tileEntityIn.isInvalid()) {
            if (this.processingLoadedTiles) {
                tileEntityIn.setPos(pos);
                final Iterator<TileEntity> iterator = this.addedTileEntityList.iterator();
                while (iterator.hasNext()) {
                    final TileEntity tileentity = iterator.next();
                    if (tileentity.getPos().equals(pos)) {
                        tileentity.invalidate();
                        iterator.remove();
                    }
                }
                this.addedTileEntityList.add(tileEntityIn);
            }
            else {
                this.addTileEntity(tileEntityIn);
                this.getChunkFromBlockCoords(pos).addTileEntity(pos, tileEntityIn);
            }
        }
    }
    
    public void removeTileEntity(final BlockPos pos) {
        final TileEntity tileentity = this.getTileEntity(pos);
        if (tileentity != null && this.processingLoadedTiles) {
            tileentity.invalidate();
            this.addedTileEntityList.remove(tileentity);
        }
        else {
            if (tileentity != null) {
                this.addedTileEntityList.remove(tileentity);
                this.loadedTileEntityList.remove(tileentity);
                this.tickableTileEntities.remove(tileentity);
            }
            this.getChunkFromBlockCoords(pos).removeTileEntity(pos);
        }
    }
    
    public void markTileEntityForRemoval(final TileEntity tileEntityIn) {
        this.tileEntitiesToBeRemoved.add(tileEntityIn);
    }
    
    public boolean isBlockFullCube(final BlockPos pos) {
        final IBlockState iblockstate = this.getBlockState(pos);
        final AxisAlignedBB axisalignedbb = iblockstate.getBlock().getCollisionBoundingBox(this, pos, iblockstate);
        return axisalignedbb != null && axisalignedbb.getAverageEdgeLength() >= 1.0;
    }
    
    public static boolean doesBlockHaveSolidTopSurface(final IBlockAccess blockAccess, final BlockPos pos) {
        final IBlockState iblockstate = blockAccess.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        return (block.getMaterial().isOpaque() && block.isFullCube()) || ((block instanceof BlockStairs) ? (iblockstate.getValue(BlockStairs.HALF) == BlockStairs.EnumHalf.TOP) : ((block instanceof BlockSlab) ? (iblockstate.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP) : (block instanceof BlockHopper || (block instanceof BlockSnow && iblockstate.getValue((IProperty<Integer>)BlockSnow.LAYERS) == 7))));
    }
    
    public boolean isBlockNormalCube(final BlockPos pos, final boolean _default) {
        if (!this.isValid(pos)) {
            return _default;
        }
        final Chunk chunk = this.chunkProvider.provideChunk(pos);
        if (chunk.isEmpty()) {
            return _default;
        }
        final Block block = this.getBlockState(pos).getBlock();
        return block.getMaterial().isOpaque() && block.isFullCube();
    }
    
    public void calculateInitialSkylight() {
        final int i = this.calculateSkylightSubtracted(1.0f);
        if (i != this.skylightSubtracted) {
            this.skylightSubtracted = i;
        }
    }
    
    public void setAllowedSpawnTypes(final boolean hostile, final boolean peaceful) {
        this.spawnHostileMobs = hostile;
        this.spawnPeacefulMobs = peaceful;
    }
    
    public void tick() {
        this.updateWeather();
    }
    
    protected void calculateInitialWeather() {
        if (this.worldInfo.isRaining()) {
            this.rainingStrength = 1.0f;
            if (this.worldInfo.isThundering()) {
                this.thunderingStrength = 1.0f;
            }
        }
    }
    
    protected void updateWeather() {
        if (!this.provider.getHasNoSky() && !this.isRemote) {
            int i = this.worldInfo.getCleanWeatherTime();
            if (i > 0) {
                --i;
                this.worldInfo.setCleanWeatherTime(i);
                this.worldInfo.setThunderTime(this.worldInfo.isThundering() ? 1 : 2);
                this.worldInfo.setRainTime(this.worldInfo.isRaining() ? 1 : 2);
            }
            int j = this.worldInfo.getThunderTime();
            if (j <= 0) {
                if (this.worldInfo.isThundering()) {
                    this.worldInfo.setThunderTime(this.rand.nextInt(12000) + 3600);
                }
                else {
                    this.worldInfo.setThunderTime(this.rand.nextInt(168000) + 12000);
                }
            }
            else {
                --j;
                this.worldInfo.setThunderTime(j);
                if (j <= 0) {
                    this.worldInfo.setThundering(!this.worldInfo.isThundering());
                }
            }
            this.prevThunderingStrength = this.thunderingStrength;
            if (this.worldInfo.isThundering()) {
                this.thunderingStrength += (float)0.01;
            }
            else {
                this.thunderingStrength -= (float)0.01;
            }
            this.thunderingStrength = MathHelper.clamp_float(this.thunderingStrength, 0.0f, 1.0f);
            int k = this.worldInfo.getRainTime();
            if (k <= 0) {
                if (this.worldInfo.isRaining()) {
                    this.worldInfo.setRainTime(this.rand.nextInt(12000) + 12000);
                }
                else {
                    this.worldInfo.setRainTime(this.rand.nextInt(168000) + 12000);
                }
            }
            else {
                --k;
                this.worldInfo.setRainTime(k);
                if (k <= 0) {
                    this.worldInfo.setRaining(!this.worldInfo.isRaining());
                }
            }
            this.prevRainingStrength = this.rainingStrength;
            if (this.worldInfo.isRaining()) {
                this.rainingStrength += (float)0.01;
            }
            else {
                this.rainingStrength -= (float)0.01;
            }
            this.rainingStrength = MathHelper.clamp_float(this.rainingStrength, 0.0f, 1.0f);
        }
    }
    
    protected void setActivePlayerChunksAndCheckLight() {
        this.activeChunkSet.clear();
        this.theProfiler.startSection("buildList");
        for (int i = 0; i < this.playerEntities.size(); ++i) {
            final EntityPlayer entityplayer = this.playerEntities.get(i);
            final int j = MathHelper.floor_double(entityplayer.posX / 16.0);
            final int k = MathHelper.floor_double(entityplayer.posZ / 16.0);
            for (int l = this.getRenderDistanceChunks(), i2 = -l; i2 <= l; ++i2) {
                for (int j2 = -l; j2 <= l; ++j2) {
                    this.activeChunkSet.add(new ChunkCoordIntPair(i2 + j, j2 + k));
                }
            }
        }
        this.theProfiler.endSection();
        if (this.ambientTickCountdown > 0) {
            --this.ambientTickCountdown;
        }
        this.theProfiler.startSection("playerCheckLight");
        if (!this.playerEntities.isEmpty()) {
            final int k2 = this.rand.nextInt(this.playerEntities.size());
            final EntityPlayer entityplayer2 = this.playerEntities.get(k2);
            final int l2 = MathHelper.floor_double(entityplayer2.posX) + this.rand.nextInt(11) - 5;
            final int i3 = MathHelper.floor_double(entityplayer2.posY) + this.rand.nextInt(11) - 5;
            final int j3 = MathHelper.floor_double(entityplayer2.posZ) + this.rand.nextInt(11) - 5;
            this.checkLight(new BlockPos(l2, i3, j3));
        }
        this.theProfiler.endSection();
    }
    
    protected abstract int getRenderDistanceChunks();
    
    protected void playMoodSoundAndCheckLight(final int p_147467_1_, final int p_147467_2_, final Chunk chunkIn) {
        this.theProfiler.endStartSection("moodSound");
        if (this.ambientTickCountdown == 0 && !this.isRemote) {
            this.updateLCG = this.updateLCG * 3 + 1013904223;
            final int i = this.updateLCG >> 2;
            int j = i & 0xF;
            int k = i >> 8 & 0xF;
            final int l = i >> 16 & 0xFF;
            final BlockPos blockpos = new BlockPos(j, l, k);
            final Block block = chunkIn.getBlock(blockpos);
            j += p_147467_1_;
            k += p_147467_2_;
            if (block.getMaterial() == Material.air && this.getLight(blockpos) <= this.rand.nextInt(8) && this.getLightFor(EnumSkyBlock.SKY, blockpos) <= 0) {
                final EntityPlayer entityplayer = this.getClosestPlayer(j + 0.5, l + 0.5, k + 0.5, 8.0);
                if (entityplayer != null && entityplayer.getDistanceSq(j + 0.5, l + 0.5, k + 0.5) > 4.0) {
                    this.playSoundEffect(j + 0.5, l + 0.5, k + 0.5, "ambient.cave.cave", 0.7f, 0.8f + this.rand.nextFloat() * 0.2f);
                    this.ambientTickCountdown = this.rand.nextInt(12000) + 6000;
                }
            }
        }
        this.theProfiler.endStartSection("checkLight");
        chunkIn.enqueueRelightChecks();
    }
    
    protected void updateBlocks() {
        this.setActivePlayerChunksAndCheckLight();
    }
    
    public void forceBlockUpdateTick(final Block blockType, final BlockPos pos, final Random random) {
        this.scheduledUpdatesAreImmediate = true;
        blockType.updateTick(this, pos, this.getBlockState(pos), random);
        this.scheduledUpdatesAreImmediate = false;
    }
    
    public boolean canBlockFreezeWater(final BlockPos pos) {
        return this.canBlockFreeze(pos, false);
    }
    
    public boolean canBlockFreezeNoWater(final BlockPos pos) {
        return this.canBlockFreeze(pos, true);
    }
    
    public boolean canBlockFreeze(final BlockPos pos, final boolean noWaterAdj) {
        final BiomeGenBase biomegenbase = this.getBiomeGenForCoords(pos);
        final float f = biomegenbase.getFloatTemperature(pos);
        if (f > 0.15f) {
            return false;
        }
        if (pos.getY() >= 0 && pos.getY() < 256 && this.getLightFor(EnumSkyBlock.BLOCK, pos) < 10) {
            final IBlockState iblockstate = this.getBlockState(pos);
            final Block block = iblockstate.getBlock();
            if ((block == Blocks.water || block == Blocks.flowing_water) && iblockstate.getValue((IProperty<Integer>)BlockLiquid.LEVEL) == 0) {
                if (!noWaterAdj) {
                    return true;
                }
                final boolean flag = this.isWater(pos.west()) && this.isWater(pos.east()) && this.isWater(pos.north()) && this.isWater(pos.south());
                if (!flag) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isWater(final BlockPos pos) {
        return this.getBlockState(pos).getBlock().getMaterial() == Material.water;
    }
    
    public boolean canSnowAt(final BlockPos pos, final boolean checkLight) {
        final BiomeGenBase biomegenbase = this.getBiomeGenForCoords(pos);
        final float f = biomegenbase.getFloatTemperature(pos);
        if (f > 0.15f) {
            return false;
        }
        if (!checkLight) {
            return true;
        }
        if (pos.getY() >= 0 && pos.getY() < 256 && this.getLightFor(EnumSkyBlock.BLOCK, pos) < 10) {
            final Block block = this.getBlockState(pos).getBlock();
            if (block.getMaterial() == Material.air && Blocks.snow_layer.canPlaceBlockAt(this, pos)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean checkLight(final BlockPos pos) {
        boolean flag = false;
        if (!this.provider.getHasNoSky()) {
            flag |= this.checkLightFor(EnumSkyBlock.SKY, pos);
        }
        flag |= this.checkLightFor(EnumSkyBlock.BLOCK, pos);
        return flag;
    }
    
    private int getRawLight(final BlockPos pos, final EnumSkyBlock lightType) {
        if (lightType == EnumSkyBlock.SKY && this.canSeeSky(pos)) {
            return 15;
        }
        final Block block = this.getBlockState(pos).getBlock();
        int i = (lightType == EnumSkyBlock.SKY) ? 0 : block.getLightValue();
        int j = block.getLightOpacity();
        if (j >= 15 && block.getLightValue() > 0) {
            j = 1;
        }
        if (j < 1) {
            j = 1;
        }
        if (j >= 15) {
            return 0;
        }
        if (i >= 14) {
            return i;
        }
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, l = 0; l < length; ++l) {
            final EnumFacing enumfacing = values[l];
            final BlockPos blockpos = pos.offset(enumfacing);
            final int k = this.getLightFor(lightType, blockpos) - j;
            if (k > i) {
                i = k;
            }
            if (i >= 14) {
                return i;
            }
        }
        return i;
    }
    
    public boolean checkLightFor(final EnumSkyBlock lightType, final BlockPos pos) {
        if (!this.isAreaLoaded(pos, 17, false)) {
            return false;
        }
        int i = 0;
        int j = 0;
        this.theProfiler.startSection("getBrightness");
        final int k = this.getLightFor(lightType, pos);
        final int l = this.getRawLight(pos, lightType);
        final int i2 = pos.getX();
        final int j2 = pos.getY();
        final int k2 = pos.getZ();
        if (l > k) {
            this.lightUpdateBlockList[j++] = 133152;
        }
        else if (l < k) {
            this.lightUpdateBlockList[j++] = (0x20820 | k << 18);
            while (i < j) {
                final int l2 = this.lightUpdateBlockList[i++];
                final int i3 = (l2 & 0x3F) - 32 + i2;
                final int j3 = (l2 >> 6 & 0x3F) - 32 + j2;
                final int k3 = (l2 >> 12 & 0x3F) - 32 + k2;
                final int l3 = l2 >> 18 & 0xF;
                final BlockPos blockpos = new BlockPos(i3, j3, k3);
                int i4 = this.getLightFor(lightType, blockpos);
                if (i4 == l3) {
                    this.setLightFor(lightType, blockpos, 0);
                    if (l3 <= 0) {
                        continue;
                    }
                    final int j4 = MathHelper.abs_int(i3 - i2);
                    final int k4 = MathHelper.abs_int(j3 - j2);
                    final int l4 = MathHelper.abs_int(k3 - k2);
                    if (j4 + k4 + l4 >= 17) {
                        continue;
                    }
                    final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                    EnumFacing[] values;
                    for (int length = (values = EnumFacing.values()).length, n = 0; n < length; ++n) {
                        final EnumFacing enumfacing = values[n];
                        final int i5 = i3 + enumfacing.getFrontOffsetX();
                        final int j5 = j3 + enumfacing.getFrontOffsetY();
                        final int k5 = k3 + enumfacing.getFrontOffsetZ();
                        blockpos$mutableblockpos.func_181079_c(i5, j5, k5);
                        final int l5 = Math.max(1, this.getBlockState(blockpos$mutableblockpos).getBlock().getLightOpacity());
                        i4 = this.getLightFor(lightType, blockpos$mutableblockpos);
                        if (i4 == l3 - l5 && j < this.lightUpdateBlockList.length) {
                            this.lightUpdateBlockList[j++] = (i5 - i2 + 32 | j5 - j2 + 32 << 6 | k5 - k2 + 32 << 12 | l3 - l5 << 18);
                        }
                    }
                }
            }
            i = 0;
        }
        this.theProfiler.endSection();
        this.theProfiler.startSection("checkedPosition < toCheckCount");
        while (i < j) {
            final int i6 = this.lightUpdateBlockList[i++];
            final int j6 = (i6 & 0x3F) - 32 + i2;
            final int k6 = (i6 >> 6 & 0x3F) - 32 + j2;
            final int l6 = (i6 >> 12 & 0x3F) - 32 + k2;
            final BlockPos blockpos2 = new BlockPos(j6, k6, l6);
            final int i7 = this.getLightFor(lightType, blockpos2);
            final int j7 = this.getRawLight(blockpos2, lightType);
            if (j7 != i7) {
                this.setLightFor(lightType, blockpos2, j7);
                if (j7 <= i7) {
                    continue;
                }
                final int k7 = Math.abs(j6 - i2);
                final int l7 = Math.abs(k6 - j2);
                final int i8 = Math.abs(l6 - k2);
                final boolean flag = j < this.lightUpdateBlockList.length - 6;
                if (k7 + l7 + i8 >= 17 || !flag) {
                    continue;
                }
                if (this.getLightFor(lightType, blockpos2.west()) < j7) {
                    this.lightUpdateBlockList[j++] = j6 - 1 - i2 + 32 + (k6 - j2 + 32 << 6) + (l6 - k2 + 32 << 12);
                }
                if (this.getLightFor(lightType, blockpos2.east()) < j7) {
                    this.lightUpdateBlockList[j++] = j6 + 1 - i2 + 32 + (k6 - j2 + 32 << 6) + (l6 - k2 + 32 << 12);
                }
                if (this.getLightFor(lightType, blockpos2.down()) < j7) {
                    this.lightUpdateBlockList[j++] = j6 - i2 + 32 + (k6 - 1 - j2 + 32 << 6) + (l6 - k2 + 32 << 12);
                }
                if (this.getLightFor(lightType, blockpos2.up()) < j7) {
                    this.lightUpdateBlockList[j++] = j6 - i2 + 32 + (k6 + 1 - j2 + 32 << 6) + (l6 - k2 + 32 << 12);
                }
                if (this.getLightFor(lightType, blockpos2.north()) < j7) {
                    this.lightUpdateBlockList[j++] = j6 - i2 + 32 + (k6 - j2 + 32 << 6) + (l6 - 1 - k2 + 32 << 12);
                }
                if (this.getLightFor(lightType, blockpos2.south()) >= j7) {
                    continue;
                }
                this.lightUpdateBlockList[j++] = j6 - i2 + 32 + (k6 - j2 + 32 << 6) + (l6 + 1 - k2 + 32 << 12);
            }
        }
        this.theProfiler.endSection();
        return true;
    }
    
    public boolean tickUpdates(final boolean p_72955_1_) {
        return false;
    }
    
    public List<NextTickListEntry> getPendingBlockUpdates(final Chunk chunkIn, final boolean p_72920_2_) {
        return null;
    }
    
    public List<NextTickListEntry> func_175712_a(final StructureBoundingBox structureBB, final boolean p_175712_2_) {
        return null;
    }
    
    public List<Entity> getEntitiesWithinAABBExcludingEntity(final Entity entityIn, final AxisAlignedBB bb) {
        return this.getEntitiesInAABBexcluding(entityIn, bb, EntitySelectors.NOT_SPECTATING);
    }
    
    public List<Entity> getEntitiesInAABBexcluding(final Entity entityIn, final AxisAlignedBB boundingBox, final Predicate<? super Entity> predicate) {
        final List<Entity> list = (List<Entity>)Lists.newArrayList();
        final int i = MathHelper.floor_double((boundingBox.minX - 2.0) / 16.0);
        final int j = MathHelper.floor_double((boundingBox.maxX + 2.0) / 16.0);
        final int k = MathHelper.floor_double((boundingBox.minZ - 2.0) / 16.0);
        final int l = MathHelper.floor_double((boundingBox.maxZ + 2.0) / 16.0);
        for (int i2 = i; i2 <= j; ++i2) {
            for (int j2 = k; j2 <= l; ++j2) {
                if (this.isChunkLoaded(i2, j2, true)) {
                    this.getChunkFromChunkCoords(i2, j2).getEntitiesWithinAABBForEntity(entityIn, boundingBox, list, predicate);
                }
            }
        }
        return list;
    }
    
    public <T extends Entity> List<T> getEntities(final Class<? extends T> entityType, final Predicate<? super T> filter) {
        final List<T> list = (List<T>)Lists.newArrayList();
        for (final Entity entity : this.loadedEntityList) {
            if (entityType.isAssignableFrom(entity.getClass()) && filter.apply((Object)entity)) {
                list.add((T)entity);
            }
        }
        return list;
    }
    
    public <T extends Entity> List<T> getPlayers(final Class<? extends T> playerType, final Predicate<? super T> filter) {
        final List<T> list = (List<T>)Lists.newArrayList();
        for (final Entity entity : this.playerEntities) {
            if (playerType.isAssignableFrom(entity.getClass()) && filter.apply((Object)entity)) {
                list.add((T)entity);
            }
        }
        return list;
    }
    
    public <T extends Entity> List<T> getEntitiesWithinAABB(final Class<? extends T> classEntity, final AxisAlignedBB bb) {
        return this.getEntitiesWithinAABB(classEntity, bb, (Predicate<? super T>)EntitySelectors.NOT_SPECTATING);
    }
    
    public <T extends Entity> List<T> getEntitiesWithinAABB(final Class<? extends T> clazz, final AxisAlignedBB aabb, final Predicate<? super T> filter) {
        final int i = MathHelper.floor_double((aabb.minX - 2.0) / 16.0);
        final int j = MathHelper.floor_double((aabb.maxX + 2.0) / 16.0);
        final int k = MathHelper.floor_double((aabb.minZ - 2.0) / 16.0);
        final int l = MathHelper.floor_double((aabb.maxZ + 2.0) / 16.0);
        final List<T> list = (List<T>)Lists.newArrayList();
        for (int i2 = i; i2 <= j; ++i2) {
            for (int j2 = k; j2 <= l; ++j2) {
                if (this.isChunkLoaded(i2, j2, true)) {
                    this.getChunkFromChunkCoords(i2, j2).getEntitiesOfTypeWithinAAAB(clazz, aabb, list, filter);
                }
            }
        }
        return list;
    }
    
    public <T extends Entity> T findNearestEntityWithinAABB(final Class<? extends T> entityType, final AxisAlignedBB aabb, final T closestTo) {
        final List<T> list = this.getEntitiesWithinAABB(entityType, aabb);
        T t = null;
        double d0 = Double.MAX_VALUE;
        for (int i = 0; i < list.size(); ++i) {
            final T t2 = list.get(i);
            if (t2 != closestTo && EntitySelectors.NOT_SPECTATING.apply(t2)) {
                final double d2 = closestTo.getDistanceSqToEntity(t2);
                if (d2 <= d0) {
                    t = t2;
                    d0 = d2;
                }
            }
        }
        return t;
    }
    
    public Entity getEntityByID(final int id) {
        return this.entitiesById.lookup(id);
    }
    
    public List<Entity> getLoadedEntityList() {
        return this.loadedEntityList;
    }
    
    public void markChunkDirty(final BlockPos pos, final TileEntity unusedTileEntity) {
        if (this.isBlockLoaded(pos)) {
            this.getChunkFromBlockCoords(pos).setChunkModified();
        }
    }
    
    public int countEntities(final Class<?> entityType) {
        int i = 0;
        for (final Entity entity : this.loadedEntityList) {
            if ((!(entity instanceof EntityLiving) || !((EntityLiving)entity).isNoDespawnRequired()) && entityType.isAssignableFrom(entity.getClass())) {
                ++i;
            }
        }
        return i;
    }
    
    public void loadEntities(final Collection<Entity> entityCollection) {
        this.loadedEntityList.addAll(entityCollection);
        for (final Entity entity : entityCollection) {
            this.onEntityAdded(entity);
        }
    }
    
    public void unloadEntities(final Collection<Entity> entityCollection) {
        this.unloadedEntityList.addAll(entityCollection);
    }
    
    public boolean canBlockBePlaced(final Block blockIn, final BlockPos pos, final boolean p_175716_3_, final EnumFacing side, final Entity entityIn, final ItemStack itemStackIn) {
        final Block block = this.getBlockState(pos).getBlock();
        final AxisAlignedBB axisalignedbb = p_175716_3_ ? null : blockIn.getCollisionBoundingBox(this, pos, blockIn.getDefaultState());
        return (axisalignedbb == null || this.checkNoEntityCollision(axisalignedbb, entityIn)) && ((block.getMaterial() == Material.circuits && blockIn == Blocks.anvil) || (block.getMaterial().isReplaceable() && blockIn.canReplace(this, pos, side, itemStackIn)));
    }
    
    public int func_181545_F() {
        return this.field_181546_a;
    }
    
    public void func_181544_b(final int p_181544_1_) {
        this.field_181546_a = p_181544_1_;
    }
    
    @Override
    public int getStrongPower(final BlockPos pos, final EnumFacing direction) {
        final IBlockState iblockstate = this.getBlockState(pos);
        return iblockstate.getBlock().getStrongPower(this, pos, iblockstate, direction);
    }
    
    @Override
    public WorldType getWorldType() {
        return this.worldInfo.getTerrainType();
    }
    
    public int getStrongPower(final BlockPos pos) {
        int i = 0;
        i = Math.max(i, this.getStrongPower(pos.down(), EnumFacing.DOWN));
        if (i >= 15) {
            return i;
        }
        i = Math.max(i, this.getStrongPower(pos.up(), EnumFacing.UP));
        if (i >= 15) {
            return i;
        }
        i = Math.max(i, this.getStrongPower(pos.north(), EnumFacing.NORTH));
        if (i >= 15) {
            return i;
        }
        i = Math.max(i, this.getStrongPower(pos.south(), EnumFacing.SOUTH));
        if (i >= 15) {
            return i;
        }
        i = Math.max(i, this.getStrongPower(pos.west(), EnumFacing.WEST));
        if (i >= 15) {
            return i;
        }
        i = Math.max(i, this.getStrongPower(pos.east(), EnumFacing.EAST));
        return (i >= 15) ? i : i;
    }
    
    public boolean isSidePowered(final BlockPos pos, final EnumFacing side) {
        return this.getRedstonePower(pos, side) > 0;
    }
    
    public int getRedstonePower(final BlockPos pos, final EnumFacing facing) {
        final IBlockState iblockstate = this.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        return block.isNormalCube() ? this.getStrongPower(pos) : block.getWeakPower(this, pos, iblockstate, facing);
    }
    
    public boolean isBlockPowered(final BlockPos pos) {
        return this.getRedstonePower(pos.down(), EnumFacing.DOWN) > 0 || this.getRedstonePower(pos.up(), EnumFacing.UP) > 0 || this.getRedstonePower(pos.north(), EnumFacing.NORTH) > 0 || this.getRedstonePower(pos.south(), EnumFacing.SOUTH) > 0 || this.getRedstonePower(pos.west(), EnumFacing.WEST) > 0 || this.getRedstonePower(pos.east(), EnumFacing.EAST) > 0;
    }
    
    public int isBlockIndirectlyGettingPowered(final BlockPos pos) {
        int i = 0;
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, k = 0; k < length; ++k) {
            final EnumFacing enumfacing = values[k];
            final int j = this.getRedstonePower(pos.offset(enumfacing), enumfacing);
            if (j >= 15) {
                return 15;
            }
            if (j > i) {
                i = j;
            }
        }
        return i;
    }
    
    public EntityPlayer getClosestPlayerToEntity(final Entity entityIn, final double distance) {
        return this.getClosestPlayer(entityIn.posX, entityIn.posY, entityIn.posZ, distance);
    }
    
    public EntityPlayer getClosestPlayer(final double x, final double y, final double z, final double distance) {
        double d0 = -1.0;
        EntityPlayer entityplayer = null;
        for (int i = 0; i < this.playerEntities.size(); ++i) {
            final EntityPlayer entityplayer2 = this.playerEntities.get(i);
            if (EntitySelectors.NOT_SPECTATING.apply(entityplayer2)) {
                final double d2 = entityplayer2.getDistanceSq(x, y, z);
                if ((distance < 0.0 || d2 < distance * distance) && (d0 == -1.0 || d2 < d0)) {
                    d0 = d2;
                    entityplayer = entityplayer2;
                }
            }
        }
        return entityplayer;
    }
    
    public boolean isAnyPlayerWithinRangeAt(final double x, final double y, final double z, final double range) {
        for (int i = 0; i < this.playerEntities.size(); ++i) {
            final EntityPlayer entityplayer = this.playerEntities.get(i);
            if (EntitySelectors.NOT_SPECTATING.apply(entityplayer)) {
                final double d0 = entityplayer.getDistanceSq(x, y, z);
                if (range < 0.0 || d0 < range * range) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public EntityPlayer getPlayerEntityByName(final String name) {
        for (int i = 0; i < this.playerEntities.size(); ++i) {
            final EntityPlayer entityplayer = this.playerEntities.get(i);
            if (name.equals(entityplayer.getName())) {
                return entityplayer;
            }
        }
        return null;
    }
    
    public EntityPlayer getPlayerEntityByUUID(final UUID uuid) {
        for (int i = 0; i < this.playerEntities.size(); ++i) {
            final EntityPlayer entityplayer = this.playerEntities.get(i);
            if (uuid.equals(entityplayer.getUniqueID())) {
                return entityplayer;
            }
        }
        return null;
    }
    
    public void sendQuittingDisconnectingPacket() {
    }
    
    public void checkSessionLock() throws MinecraftException {
        this.saveHandler.checkSessionLock();
    }
    
    public void setTotalWorldTime(final long worldTime) {
        this.worldInfo.setWorldTotalTime(worldTime);
    }
    
    public long getSeed() {
        return this.worldInfo.getSeed();
    }
    
    public long getTotalWorldTime() {
        return this.worldInfo.getWorldTotalTime();
    }
    
    public long getWorldTime() {
        return this.worldInfo.getWorldTime();
    }
    
    public void setWorldTime(final long time) {
        this.worldInfo.setWorldTime(time);
    }
    
    public BlockPos getSpawnPoint() {
        BlockPos blockpos = new BlockPos(this.worldInfo.getSpawnX(), this.worldInfo.getSpawnY(), this.worldInfo.getSpawnZ());
        if (!this.getWorldBorder().contains(blockpos)) {
            blockpos = this.getHeight(new BlockPos(this.getWorldBorder().getCenterX(), 0.0, this.getWorldBorder().getCenterZ()));
        }
        return blockpos;
    }
    
    public void setSpawnPoint(final BlockPos pos) {
        this.worldInfo.setSpawn(pos);
    }
    
    public void joinEntityInSurroundings(final Entity entityIn) {
        final int i = MathHelper.floor_double(entityIn.posX / 16.0);
        final int j = MathHelper.floor_double(entityIn.posZ / 16.0);
        for (int k = 2, l = i - k; l <= i + k; ++l) {
            for (int i2 = j - k; i2 <= j + k; ++i2) {
                this.getChunkFromChunkCoords(l, i2);
            }
        }
        if (!this.loadedEntityList.contains(entityIn)) {
            this.loadedEntityList.add(entityIn);
        }
    }
    
    public boolean isBlockModifiable(final EntityPlayer player, final BlockPos pos) {
        return true;
    }
    
    public void setEntityState(final Entity entityIn, final byte state) {
    }
    
    public IChunkProvider getChunkProvider() {
        return this.chunkProvider;
    }
    
    public void addBlockEvent(final BlockPos pos, final Block blockIn, final int eventID, final int eventParam) {
        blockIn.onBlockEventReceived(this, pos, this.getBlockState(pos), eventID, eventParam);
    }
    
    public ISaveHandler getSaveHandler() {
        return this.saveHandler;
    }
    
    public WorldInfo getWorldInfo() {
        return this.worldInfo;
    }
    
    public GameRules getGameRules() {
        return this.worldInfo.getGameRulesInstance();
    }
    
    public void updateAllPlayersSleepingFlag() {
    }
    
    public float getThunderStrength(final float delta) {
        return (this.prevThunderingStrength + (this.thunderingStrength - this.prevThunderingStrength) * delta) * this.getRainStrength(delta);
    }
    
    public void setThunderStrength(final float strength) {
        this.prevThunderingStrength = strength;
        this.thunderingStrength = strength;
    }
    
    public float getRainStrength(final float delta) {
        return this.prevRainingStrength + (this.rainingStrength - this.prevRainingStrength) * delta;
    }
    
    public void setRainStrength(final float strength) {
        this.prevRainingStrength = strength;
        this.rainingStrength = strength;
    }
    
    public boolean isThundering() {
        return this.getThunderStrength(1.0f) > 0.9;
    }
    
    public boolean isRaining() {
        return this.getRainStrength(1.0f) > 0.2;
    }
    
    public boolean canLightningStrike(final BlockPos strikePosition) {
        if (!this.isRaining()) {
            return false;
        }
        if (!this.canSeeSky(strikePosition)) {
            return false;
        }
        if (this.getPrecipitationHeight(strikePosition).getY() > strikePosition.getY()) {
            return false;
        }
        final BiomeGenBase biomegenbase = this.getBiomeGenForCoords(strikePosition);
        return !biomegenbase.getEnableSnow() && !this.canSnowAt(strikePosition, false) && biomegenbase.canSpawnLightningBolt();
    }
    
    public boolean isBlockinHighHumidity(final BlockPos pos) {
        final BiomeGenBase biomegenbase = this.getBiomeGenForCoords(pos);
        return biomegenbase.isHighHumidity();
    }
    
    public MapStorage getMapStorage() {
        return this.mapStorage;
    }
    
    public void setItemData(final String dataID, final WorldSavedData worldSavedDataIn) {
        this.mapStorage.setData(dataID, worldSavedDataIn);
    }
    
    public WorldSavedData loadItemData(final Class<? extends WorldSavedData> clazz, final String dataID) {
        return this.mapStorage.loadData(clazz, dataID);
    }
    
    public int getUniqueDataId(final String key) {
        return this.mapStorage.getUniqueDataId(key);
    }
    
    public void playBroadcastSound(final int p_175669_1_, final BlockPos pos, final int p_175669_3_) {
        for (int i = 0; i < this.worldAccesses.size(); ++i) {
            this.worldAccesses.get(i).broadcastSound(p_175669_1_, pos, p_175669_3_);
        }
    }
    
    public void playAuxSFX(final int p_175718_1_, final BlockPos pos, final int p_175718_3_) {
        this.playAuxSFXAtEntity(null, p_175718_1_, pos, p_175718_3_);
    }
    
    public void playAuxSFXAtEntity(final EntityPlayer player, final int sfxType, final BlockPos pos, final int p_180498_4_) {
        try {
            for (int i = 0; i < this.worldAccesses.size(); ++i) {
                this.worldAccesses.get(i).playAuxSFX(player, sfxType, pos, p_180498_4_);
            }
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Playing level event");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Level event being played");
            crashreportcategory.addCrashSection("Block coordinates", CrashReportCategory.getCoordinateInfo(pos));
            crashreportcategory.addCrashSection("Event source", player);
            crashreportcategory.addCrashSection("Event type", sfxType);
            crashreportcategory.addCrashSection("Event data", p_180498_4_);
            throw new ReportedException(crashreport);
        }
    }
    
    public int getHeight() {
        return 256;
    }
    
    public int getActualHeight() {
        return this.provider.getHasNoSky() ? 128 : 256;
    }
    
    public Random setRandomSeed(final int p_72843_1_, final int p_72843_2_, final int p_72843_3_) {
        final long i = p_72843_1_ * 341873128712L + p_72843_2_ * 132897987541L + this.getWorldInfo().getSeed() + p_72843_3_;
        this.rand.setSeed(i);
        return this.rand;
    }
    
    public BlockPos getStrongholdPos(final String name, final BlockPos pos) {
        return this.getChunkProvider().getStrongholdGen(this, name, pos);
    }
    
    @Override
    public boolean extendedLevelsInChunkCache() {
        return false;
    }
    
    public double getHorizon() {
        return (this.worldInfo.getTerrainType() == WorldType.FLAT) ? 0.0 : 63.0;
    }
    
    public CrashReportCategory addWorldInfoToCrashReport(final CrashReport report) {
        final CrashReportCategory crashreportcategory = report.makeCategoryDepth("Affected level", 1);
        crashreportcategory.addCrashSection("Level name", (this.worldInfo == null) ? "????" : this.worldInfo.getWorldName());
        crashreportcategory.addCrashSectionCallable("All players", new Callable<String>() {
            @Override
            public String call() {
                return String.valueOf(World.this.playerEntities.size()) + " total; " + World.this.playerEntities.toString();
            }
        });
        crashreportcategory.addCrashSectionCallable("Chunk stats", new Callable<String>() {
            @Override
            public String call() {
                return World.this.chunkProvider.makeString();
            }
        });
        try {
            this.worldInfo.addToCrashReport(crashreportcategory);
        }
        catch (Throwable throwable) {
            crashreportcategory.addCrashSectionThrowable("Level Data Unobtainable", throwable);
        }
        return crashreportcategory;
    }
    
    public void sendBlockBreakProgress(final int breakerId, final BlockPos pos, final int progress) {
        for (int i = 0; i < this.worldAccesses.size(); ++i) {
            final IWorldAccess iworldaccess = this.worldAccesses.get(i);
            iworldaccess.sendBlockBreakProgress(breakerId, pos, progress);
        }
    }
    
    public Calendar getCurrentDate() {
        if (this.getTotalWorldTime() % 600L == 0L) {
            this.theCalendar.setTimeInMillis(MinecraftServer.getCurrentTimeMillis());
        }
        return this.theCalendar;
    }
    
    public void makeFireworks(final double x, final double y, final double z, final double motionX, final double motionY, final double motionZ, final NBTTagCompound compund) {
    }
    
    public Scoreboard getScoreboard() {
        return this.worldScoreboard;
    }
    
    public void updateComparatorOutputLevel(final BlockPos pos, final Block blockIn) {
        for (final Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
            BlockPos blockpos = pos.offset((EnumFacing)enumfacing);
            if (this.isBlockLoaded(blockpos)) {
                IBlockState iblockstate = this.getBlockState(blockpos);
                if (Blocks.unpowered_comparator.isAssociated(iblockstate.getBlock())) {
                    iblockstate.getBlock().onNeighborBlockChange(this, blockpos, iblockstate, blockIn);
                }
                else {
                    if (!iblockstate.getBlock().isNormalCube()) {
                        continue;
                    }
                    blockpos = blockpos.offset((EnumFacing)enumfacing);
                    iblockstate = this.getBlockState(blockpos);
                    if (!Blocks.unpowered_comparator.isAssociated(iblockstate.getBlock())) {
                        continue;
                    }
                    iblockstate.getBlock().onNeighborBlockChange(this, blockpos, iblockstate, blockIn);
                }
            }
        }
    }
    
    public DifficultyInstance getDifficultyForLocation(final BlockPos pos) {
        long i = 0L;
        float f = 0.0f;
        if (this.isBlockLoaded(pos)) {
            f = this.getCurrentMoonPhaseFactor();
            i = this.getChunkFromBlockCoords(pos).getInhabitedTime();
        }
        return new DifficultyInstance(this.getDifficulty(), this.getWorldTime(), i, f);
    }
    
    public EnumDifficulty getDifficulty() {
        return this.getWorldInfo().getDifficulty();
    }
    
    public int getSkylightSubtracted() {
        return this.skylightSubtracted;
    }
    
    public void setSkylightSubtracted(final int newSkylightSubtracted) {
        this.skylightSubtracted = newSkylightSubtracted;
    }
    
    public int getLastLightningBolt() {
        return this.lastLightningBolt;
    }
    
    public void setLastLightningBolt(final int lastLightningBoltIn) {
        this.lastLightningBolt = lastLightningBoltIn;
    }
    
    public boolean isFindingSpawnPoint() {
        return this.findingSpawnPoint;
    }
    
    public VillageCollection getVillageCollection() {
        return this.villageCollectionObj;
    }
    
    public WorldBorder getWorldBorder() {
        return this.worldBorder;
    }
    
    public boolean isSpawnChunk(final int x, final int z) {
        final BlockPos blockpos = this.getSpawnPoint();
        final int i = x * 16 + 8 - blockpos.getX();
        final int j = z * 16 + 8 - blockpos.getZ();
        final int k = 128;
        return i >= -k && i <= k && j >= -k && j <= k;
    }
}
