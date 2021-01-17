// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import java.util.ArrayList;
import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S24PacketBlockAction;
import net.minecraft.block.BlockEventData;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.Vec3;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.gen.feature.WorldGeneratorBonusChest;
import net.minecraft.world.biome.WorldChunkManager;
import java.util.Random;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.entity.INpc;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.CrashReport;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.block.material.Material;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Collection;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.scoreboard.ScoreboardSaveData;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.storage.MapStorage;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.ISaveHandler;
import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.WeightedRandomChestContent;
import java.util.List;
import net.minecraft.village.VillageSiege;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.entity.Entity;
import java.util.UUID;
import java.util.Map;
import java.util.TreeSet;
import java.util.Set;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.entity.EntityTracker;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;
import net.minecraft.util.IThreadListener;

public class WorldServer extends World implements IThreadListener
{
    private static final Logger logger;
    private final MinecraftServer mcServer;
    private final EntityTracker theEntityTracker;
    private final PlayerManager thePlayerManager;
    private final Set<NextTickListEntry> pendingTickListEntriesHashSet;
    private final TreeSet<NextTickListEntry> pendingTickListEntriesTreeSet;
    private final Map<UUID, Entity> entitiesByUuid;
    public ChunkProviderServer theChunkProviderServer;
    public boolean disableLevelSaving;
    private boolean allPlayersSleeping;
    private int updateEntityTick;
    private final Teleporter worldTeleporter;
    private final SpawnerAnimals mobSpawner;
    protected final VillageSiege villageSiege;
    private ServerBlockEventList[] field_147490_S;
    private int blockEventCacheIndex;
    private static final List<WeightedRandomChestContent> bonusChestContent;
    private List<NextTickListEntry> pendingTickListEntriesThisTick;
    
    static {
        logger = LogManager.getLogger();
        bonusChestContent = Lists.newArrayList(new WeightedRandomChestContent(Items.stick, 0, 1, 3, 10), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.planks), 0, 1, 3, 10), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.log), 0, 1, 3, 10), new WeightedRandomChestContent(Items.stone_axe, 0, 1, 1, 3), new WeightedRandomChestContent(Items.wooden_axe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.stone_pickaxe, 0, 1, 1, 3), new WeightedRandomChestContent(Items.wooden_pickaxe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.apple, 0, 2, 3, 5), new WeightedRandomChestContent(Items.bread, 0, 2, 3, 3), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.log2), 0, 1, 3, 10));
    }
    
    public WorldServer(final MinecraftServer server, final ISaveHandler saveHandlerIn, final WorldInfo info, final int dimensionId, final Profiler profilerIn) {
        super(saveHandlerIn, info, WorldProvider.getProviderForDimension(dimensionId), profilerIn, false);
        this.pendingTickListEntriesHashSet = (Set<NextTickListEntry>)Sets.newHashSet();
        this.pendingTickListEntriesTreeSet = new TreeSet<NextTickListEntry>();
        this.entitiesByUuid = (Map<UUID, Entity>)Maps.newHashMap();
        this.mobSpawner = new SpawnerAnimals();
        this.villageSiege = new VillageSiege(this);
        this.field_147490_S = new ServerBlockEventList[] { new ServerBlockEventList(null), new ServerBlockEventList(null) };
        this.pendingTickListEntriesThisTick = (List<NextTickListEntry>)Lists.newArrayList();
        this.mcServer = server;
        this.theEntityTracker = new EntityTracker(this);
        this.thePlayerManager = new PlayerManager(this);
        this.provider.registerWorld(this);
        this.chunkProvider = this.createChunkProvider();
        this.worldTeleporter = new Teleporter(this);
        this.calculateInitialSkylight();
        this.calculateInitialWeather();
        this.getWorldBorder().setSize(server.getMaxWorldSize());
    }
    
    @Override
    public World init() {
        this.mapStorage = new MapStorage(this.saveHandler);
        final String s = VillageCollection.fileNameForProvider(this.provider);
        final VillageCollection villagecollection = (VillageCollection)this.mapStorage.loadData(VillageCollection.class, s);
        if (villagecollection == null) {
            this.villageCollectionObj = new VillageCollection(this);
            this.mapStorage.setData(s, this.villageCollectionObj);
        }
        else {
            (this.villageCollectionObj = villagecollection).setWorldsForAll(this);
        }
        this.worldScoreboard = new ServerScoreboard(this.mcServer);
        ScoreboardSaveData scoreboardsavedata = (ScoreboardSaveData)this.mapStorage.loadData(ScoreboardSaveData.class, "scoreboard");
        if (scoreboardsavedata == null) {
            scoreboardsavedata = new ScoreboardSaveData();
            this.mapStorage.setData("scoreboard", scoreboardsavedata);
        }
        scoreboardsavedata.setScoreboard(this.worldScoreboard);
        ((ServerScoreboard)this.worldScoreboard).func_96547_a(scoreboardsavedata);
        this.getWorldBorder().setCenter(this.worldInfo.getBorderCenterX(), this.worldInfo.getBorderCenterZ());
        this.getWorldBorder().setDamageAmount(this.worldInfo.getBorderDamagePerBlock());
        this.getWorldBorder().setDamageBuffer(this.worldInfo.getBorderSafeZone());
        this.getWorldBorder().setWarningDistance(this.worldInfo.getBorderWarningDistance());
        this.getWorldBorder().setWarningTime(this.worldInfo.getBorderWarningTime());
        if (this.worldInfo.getBorderLerpTime() > 0L) {
            this.getWorldBorder().setTransition(this.worldInfo.getBorderSize(), this.worldInfo.getBorderLerpTarget(), this.worldInfo.getBorderLerpTime());
        }
        else {
            this.getWorldBorder().setTransition(this.worldInfo.getBorderSize());
        }
        return this;
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.getWorldInfo().isHardcoreModeEnabled() && this.getDifficulty() != EnumDifficulty.HARD) {
            this.getWorldInfo().setDifficulty(EnumDifficulty.HARD);
        }
        this.provider.getWorldChunkManager().cleanupCache();
        if (this.areAllPlayersAsleep()) {
            if (this.getGameRules().getBoolean("doDaylightCycle")) {
                final long i = this.worldInfo.getWorldTime() + 24000L;
                this.worldInfo.setWorldTime(i - i % 24000L);
            }
            this.wakeAllPlayers();
        }
        this.theProfiler.startSection("mobSpawner");
        if (this.getGameRules().getBoolean("doMobSpawning") && this.worldInfo.getTerrainType() != WorldType.DEBUG_WORLD) {
            this.mobSpawner.findChunksForSpawning(this, this.spawnHostileMobs, this.spawnPeacefulMobs, this.worldInfo.getWorldTotalTime() % 400L == 0L);
        }
        this.theProfiler.endStartSection("chunkSource");
        this.chunkProvider.unloadQueuedChunks();
        final int j = this.calculateSkylightSubtracted(1.0f);
        if (j != this.getSkylightSubtracted()) {
            this.setSkylightSubtracted(j);
        }
        this.worldInfo.setWorldTotalTime(this.worldInfo.getWorldTotalTime() + 1L);
        if (this.getGameRules().getBoolean("doDaylightCycle")) {
            this.worldInfo.setWorldTime(this.worldInfo.getWorldTime() + 1L);
        }
        this.theProfiler.endStartSection("tickPending");
        this.tickUpdates(false);
        this.theProfiler.endStartSection("tickBlocks");
        this.updateBlocks();
        this.theProfiler.endStartSection("chunkMap");
        this.thePlayerManager.updatePlayerInstances();
        this.theProfiler.endStartSection("village");
        this.villageCollectionObj.tick();
        this.villageSiege.tick();
        this.theProfiler.endStartSection("portalForcer");
        this.worldTeleporter.removeStalePortalLocations(this.getTotalWorldTime());
        this.theProfiler.endSection();
        this.sendQueuedBlockEvents();
    }
    
    public BiomeGenBase.SpawnListEntry getSpawnListEntryForTypeAt(final EnumCreatureType creatureType, final BlockPos pos) {
        final List<BiomeGenBase.SpawnListEntry> list = this.getChunkProvider().getPossibleCreatures(creatureType, pos);
        return (list != null && !list.isEmpty()) ? WeightedRandom.getRandomItem(this.rand, list) : null;
    }
    
    public boolean canCreatureTypeSpawnHere(final EnumCreatureType creatureType, final BiomeGenBase.SpawnListEntry spawnListEntry, final BlockPos pos) {
        final List<BiomeGenBase.SpawnListEntry> list = this.getChunkProvider().getPossibleCreatures(creatureType, pos);
        return list != null && !list.isEmpty() && list.contains(spawnListEntry);
    }
    
    @Override
    public void updateAllPlayersSleepingFlag() {
        this.allPlayersSleeping = false;
        if (!this.playerEntities.isEmpty()) {
            int i = 0;
            int j = 0;
            for (final EntityPlayer entityplayer : this.playerEntities) {
                if (entityplayer.isSpectator()) {
                    ++i;
                }
                else {
                    if (!entityplayer.isPlayerSleeping()) {
                        continue;
                    }
                    ++j;
                }
            }
            this.allPlayersSleeping = (j > 0 && j >= this.playerEntities.size() - i);
        }
    }
    
    protected void wakeAllPlayers() {
        this.allPlayersSleeping = false;
        for (final EntityPlayer entityplayer : this.playerEntities) {
            if (entityplayer.isPlayerSleeping()) {
                entityplayer.wakeUpPlayer(false, false, true);
            }
        }
        this.resetRainAndThunder();
    }
    
    private void resetRainAndThunder() {
        this.worldInfo.setRainTime(0);
        this.worldInfo.setRaining(false);
        this.worldInfo.setThunderTime(0);
        this.worldInfo.setThundering(false);
    }
    
    public boolean areAllPlayersAsleep() {
        if (this.allPlayersSleeping && !this.isRemote) {
            for (final EntityPlayer entityplayer : this.playerEntities) {
                if (entityplayer.isSpectator() || !entityplayer.isPlayerFullyAsleep()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void setInitialSpawnLocation() {
        if (this.worldInfo.getSpawnY() <= 0) {
            this.worldInfo.setSpawnY(this.func_181545_F() + 1);
        }
        int i = this.worldInfo.getSpawnX();
        int j = this.worldInfo.getSpawnZ();
        int k = 0;
        while (this.getGroundAboveSeaLevel(new BlockPos(i, 0, j)).getMaterial() == Material.air) {
            i += this.rand.nextInt(8) - this.rand.nextInt(8);
            j += this.rand.nextInt(8) - this.rand.nextInt(8);
            if (++k == 10000) {
                break;
            }
        }
        this.worldInfo.setSpawnX(i);
        this.worldInfo.setSpawnZ(j);
    }
    
    @Override
    protected void updateBlocks() {
        super.updateBlocks();
        if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
            for (final ChunkCoordIntPair chunkcoordintpair1 : this.activeChunkSet) {
                this.getChunkFromChunkCoords(chunkcoordintpair1.chunkXPos, chunkcoordintpair1.chunkZPos).func_150804_b(false);
            }
        }
        else {
            int i = 0;
            int j = 0;
            for (final ChunkCoordIntPair chunkcoordintpair2 : this.activeChunkSet) {
                final int k = chunkcoordintpair2.chunkXPos * 16;
                final int l = chunkcoordintpair2.chunkZPos * 16;
                this.theProfiler.startSection("getChunk");
                final Chunk chunk = this.getChunkFromChunkCoords(chunkcoordintpair2.chunkXPos, chunkcoordintpair2.chunkZPos);
                this.playMoodSoundAndCheckLight(k, l, chunk);
                this.theProfiler.endStartSection("tickChunk");
                chunk.func_150804_b(false);
                this.theProfiler.endStartSection("thunder");
                if (this.rand.nextInt(100000) == 0 && this.isRaining() && this.isThundering()) {
                    this.updateLCG = this.updateLCG * 3 + 1013904223;
                    final int i2 = this.updateLCG >> 2;
                    final BlockPos blockpos = this.adjustPosToNearbyEntity(new BlockPos(k + (i2 & 0xF), 0, l + (i2 >> 8 & 0xF)));
                    if (this.canLightningStrike(blockpos)) {
                        this.addWeatherEffect(new EntityLightningBolt(this, blockpos.getX(), blockpos.getY(), blockpos.getZ()));
                    }
                }
                this.theProfiler.endStartSection("iceandsnow");
                if (this.rand.nextInt(16) == 0) {
                    this.updateLCG = this.updateLCG * 3 + 1013904223;
                    final int k2 = this.updateLCG >> 2;
                    final BlockPos blockpos2 = this.getPrecipitationHeight(new BlockPos(k + (k2 & 0xF), 0, l + (k2 >> 8 & 0xF)));
                    final BlockPos blockpos3 = blockpos2.down();
                    if (this.canBlockFreezeNoWater(blockpos3)) {
                        this.setBlockState(blockpos3, Blocks.ice.getDefaultState());
                    }
                    if (this.isRaining() && this.canSnowAt(blockpos2, true)) {
                        this.setBlockState(blockpos2, Blocks.snow_layer.getDefaultState());
                    }
                    if (this.isRaining() && this.getBiomeGenForCoords(blockpos3).canSpawnLightningBolt()) {
                        this.getBlockState(blockpos3).getBlock().fillWithRain(this, blockpos3);
                    }
                }
                this.theProfiler.endStartSection("tickBlocks");
                final int l2 = this.getGameRules().getInt("randomTickSpeed");
                if (l2 > 0) {
                    ExtendedBlockStorage[] blockStorageArray;
                    for (int length = (blockStorageArray = chunk.getBlockStorageArray()).length, n = 0; n < length; ++n) {
                        final ExtendedBlockStorage extendedblockstorage = blockStorageArray[n];
                        if (extendedblockstorage != null && extendedblockstorage.getNeedsRandomTick()) {
                            for (int j2 = 0; j2 < l2; ++j2) {
                                this.updateLCG = this.updateLCG * 3 + 1013904223;
                                final int k3 = this.updateLCG >> 2;
                                final int l3 = k3 & 0xF;
                                final int i3 = k3 >> 8 & 0xF;
                                final int j3 = k3 >> 16 & 0xF;
                                ++j;
                                final IBlockState iblockstate = extendedblockstorage.get(l3, j3, i3);
                                final Block block = iblockstate.getBlock();
                                if (block.getTickRandomly()) {
                                    ++i;
                                    block.randomTick(this, new BlockPos(l3 + k, j3 + extendedblockstorage.getYLocation(), i3 + l), iblockstate, this.rand);
                                }
                            }
                        }
                    }
                }
                this.theProfiler.endSection();
            }
        }
    }
    
    protected BlockPos adjustPosToNearbyEntity(final BlockPos pos) {
        final BlockPos blockpos = this.getPrecipitationHeight(pos);
        final AxisAlignedBB axisalignedbb = new AxisAlignedBB(blockpos, new BlockPos(blockpos.getX(), this.getHeight(), blockpos.getZ())).expand(3.0, 3.0, 3.0);
        final List<EntityLivingBase> list = this.getEntitiesWithinAABB((Class<? extends EntityLivingBase>)EntityLivingBase.class, axisalignedbb, (Predicate<? super EntityLivingBase>)new Predicate<EntityLivingBase>() {
            @Override
            public boolean apply(final EntityLivingBase p_apply_1_) {
                return p_apply_1_ != null && p_apply_1_.isEntityAlive() && WorldServer.this.canSeeSky(p_apply_1_.getPosition());
            }
        });
        return list.isEmpty() ? blockpos : list.get(this.rand.nextInt(list.size())).getPosition();
    }
    
    @Override
    public boolean isBlockTickPending(final BlockPos pos, final Block blockType) {
        final NextTickListEntry nextticklistentry = new NextTickListEntry(pos, blockType);
        return this.pendingTickListEntriesThisTick.contains(nextticklistentry);
    }
    
    @Override
    public void scheduleUpdate(final BlockPos pos, final Block blockIn, final int delay) {
        this.updateBlockTick(pos, blockIn, delay, 0);
    }
    
    @Override
    public void updateBlockTick(final BlockPos pos, final Block blockIn, int delay, final int priority) {
        final NextTickListEntry nextticklistentry = new NextTickListEntry(pos, blockIn);
        int i = 0;
        if (this.scheduledUpdatesAreImmediate && blockIn.getMaterial() != Material.air) {
            if (blockIn.requiresUpdates()) {
                i = 8;
                if (this.isAreaLoaded(nextticklistentry.position.add(-i, -i, -i), nextticklistentry.position.add(i, i, i))) {
                    final IBlockState iblockstate = this.getBlockState(nextticklistentry.position);
                    if (iblockstate.getBlock().getMaterial() != Material.air && iblockstate.getBlock() == nextticklistentry.getBlock()) {
                        iblockstate.getBlock().updateTick(this, nextticklistentry.position, iblockstate, this.rand);
                    }
                }
                return;
            }
            delay = 1;
        }
        if (this.isAreaLoaded(pos.add(-i, -i, -i), pos.add(i, i, i))) {
            if (blockIn.getMaterial() != Material.air) {
                nextticklistentry.setScheduledTime(delay + this.worldInfo.getWorldTotalTime());
                nextticklistentry.setPriority(priority);
            }
            if (!this.pendingTickListEntriesHashSet.contains(nextticklistentry)) {
                this.pendingTickListEntriesHashSet.add(nextticklistentry);
                this.pendingTickListEntriesTreeSet.add(nextticklistentry);
            }
        }
    }
    
    @Override
    public void scheduleBlockUpdate(final BlockPos pos, final Block blockIn, final int delay, final int priority) {
        final NextTickListEntry nextticklistentry = new NextTickListEntry(pos, blockIn);
        nextticklistentry.setPriority(priority);
        if (blockIn.getMaterial() != Material.air) {
            nextticklistentry.setScheduledTime(delay + this.worldInfo.getWorldTotalTime());
        }
        if (!this.pendingTickListEntriesHashSet.contains(nextticklistentry)) {
            this.pendingTickListEntriesHashSet.add(nextticklistentry);
            this.pendingTickListEntriesTreeSet.add(nextticklistentry);
        }
    }
    
    @Override
    public void updateEntities() {
        if (this.playerEntities.isEmpty()) {
            if (this.updateEntityTick++ >= 1200) {
                return;
            }
        }
        else {
            this.resetUpdateEntityTick();
        }
        super.updateEntities();
    }
    
    public void resetUpdateEntityTick() {
        this.updateEntityTick = 0;
    }
    
    @Override
    public boolean tickUpdates(final boolean p_72955_1_) {
        if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
            return false;
        }
        int i = this.pendingTickListEntriesTreeSet.size();
        if (i != this.pendingTickListEntriesHashSet.size()) {
            throw new IllegalStateException("TickNextTick list out of synch");
        }
        if (i > 1000) {
            i = 1000;
        }
        this.theProfiler.startSection("cleaning");
        for (int j = 0; j < i; ++j) {
            final NextTickListEntry nextticklistentry = this.pendingTickListEntriesTreeSet.first();
            if (!p_72955_1_ && nextticklistentry.scheduledTime > this.worldInfo.getWorldTotalTime()) {
                break;
            }
            this.pendingTickListEntriesTreeSet.remove(nextticklistentry);
            this.pendingTickListEntriesHashSet.remove(nextticklistentry);
            this.pendingTickListEntriesThisTick.add(nextticklistentry);
        }
        this.theProfiler.endSection();
        this.theProfiler.startSection("ticking");
        final Iterator<NextTickListEntry> iterator = this.pendingTickListEntriesThisTick.iterator();
        while (iterator.hasNext()) {
            final NextTickListEntry nextticklistentry2 = iterator.next();
            iterator.remove();
            final int k = 0;
            if (this.isAreaLoaded(nextticklistentry2.position.add(-k, -k, -k), nextticklistentry2.position.add(k, k, k))) {
                final IBlockState iblockstate = this.getBlockState(nextticklistentry2.position);
                if (iblockstate.getBlock().getMaterial() == Material.air || !Block.isEqualTo(iblockstate.getBlock(), nextticklistentry2.getBlock())) {
                    continue;
                }
                try {
                    iblockstate.getBlock().updateTick(this, nextticklistentry2.position, iblockstate, this.rand);
                    continue;
                }
                catch (Throwable throwable) {
                    final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while ticking a block");
                    final CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being ticked");
                    CrashReportCategory.addBlockInfo(crashreportcategory, nextticklistentry2.position, iblockstate);
                    throw new ReportedException(crashreport);
                }
            }
            this.scheduleUpdate(nextticklistentry2.position, nextticklistentry2.getBlock(), 0);
        }
        this.theProfiler.endSection();
        this.pendingTickListEntriesThisTick.clear();
        return !this.pendingTickListEntriesTreeSet.isEmpty();
    }
    
    @Override
    public List<NextTickListEntry> getPendingBlockUpdates(final Chunk chunkIn, final boolean p_72920_2_) {
        final ChunkCoordIntPair chunkcoordintpair = chunkIn.getChunkCoordIntPair();
        final int i = (chunkcoordintpair.chunkXPos << 4) - 2;
        final int j = i + 16 + 2;
        final int k = (chunkcoordintpair.chunkZPos << 4) - 2;
        final int l = k + 16 + 2;
        return this.func_175712_a(new StructureBoundingBox(i, 0, k, j, 256, l), p_72920_2_);
    }
    
    @Override
    public List<NextTickListEntry> func_175712_a(final StructureBoundingBox structureBB, final boolean p_175712_2_) {
        List<NextTickListEntry> list = null;
        for (int i = 0; i < 2; ++i) {
            Iterator<NextTickListEntry> iterator;
            if (i == 0) {
                iterator = this.pendingTickListEntriesTreeSet.iterator();
            }
            else {
                iterator = this.pendingTickListEntriesThisTick.iterator();
            }
            while (iterator.hasNext()) {
                final NextTickListEntry nextticklistentry = iterator.next();
                final BlockPos blockpos = nextticklistentry.position;
                if (blockpos.getX() >= structureBB.minX && blockpos.getX() < structureBB.maxX && blockpos.getZ() >= structureBB.minZ && blockpos.getZ() < structureBB.maxZ) {
                    if (p_175712_2_) {
                        this.pendingTickListEntriesHashSet.remove(nextticklistentry);
                        iterator.remove();
                    }
                    if (list == null) {
                        list = (List<NextTickListEntry>)Lists.newArrayList();
                    }
                    list.add(nextticklistentry);
                }
            }
        }
        return list;
    }
    
    @Override
    public void updateEntityWithOptionalForce(final Entity entityIn, final boolean forceUpdate) {
        if (!this.canSpawnAnimals() && (entityIn instanceof EntityAnimal || entityIn instanceof EntityWaterMob)) {
            entityIn.setDead();
        }
        if (!this.canSpawnNPCs() && entityIn instanceof INpc) {
            entityIn.setDead();
        }
        super.updateEntityWithOptionalForce(entityIn, forceUpdate);
    }
    
    private boolean canSpawnNPCs() {
        return this.mcServer.getCanSpawnNPCs();
    }
    
    private boolean canSpawnAnimals() {
        return this.mcServer.getCanSpawnAnimals();
    }
    
    @Override
    protected IChunkProvider createChunkProvider() {
        final IChunkLoader ichunkloader = this.saveHandler.getChunkLoader(this.provider);
        return this.theChunkProviderServer = new ChunkProviderServer(this, ichunkloader, this.provider.createChunkGenerator());
    }
    
    public List<TileEntity> getTileEntitiesIn(final int minX, final int minY, final int minZ, final int maxX, final int maxY, final int maxZ) {
        final List<TileEntity> list = (List<TileEntity>)Lists.newArrayList();
        for (int i = 0; i < this.loadedTileEntityList.size(); ++i) {
            final TileEntity tileentity = this.loadedTileEntityList.get(i);
            final BlockPos blockpos = tileentity.getPos();
            if (blockpos.getX() >= minX && blockpos.getY() >= minY && blockpos.getZ() >= minZ && blockpos.getX() < maxX && blockpos.getY() < maxY && blockpos.getZ() < maxZ) {
                list.add(tileentity);
            }
        }
        return list;
    }
    
    @Override
    public boolean isBlockModifiable(final EntityPlayer player, final BlockPos pos) {
        return !this.mcServer.isBlockProtected(this, pos, player) && this.getWorldBorder().contains(pos);
    }
    
    @Override
    public void initialize(final WorldSettings settings) {
        if (!this.worldInfo.isInitialized()) {
            try {
                this.createSpawnPosition(settings);
                if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
                    this.setDebugWorldSettings();
                }
                super.initialize(settings);
            }
            catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception initializing level");
                try {
                    this.addWorldInfoToCrashReport(crashreport);
                }
                catch (Throwable t) {}
                throw new ReportedException(crashreport);
            }
            this.worldInfo.setServerInitialized(true);
        }
    }
    
    private void setDebugWorldSettings() {
        this.worldInfo.setMapFeaturesEnabled(false);
        this.worldInfo.setAllowCommands(true);
        this.worldInfo.setRaining(false);
        this.worldInfo.setThundering(false);
        this.worldInfo.setCleanWeatherTime(1000000000);
        this.worldInfo.setWorldTime(6000L);
        this.worldInfo.setGameType(WorldSettings.GameType.SPECTATOR);
        this.worldInfo.setHardcore(false);
        this.worldInfo.setDifficulty(EnumDifficulty.PEACEFUL);
        this.worldInfo.setDifficultyLocked(true);
        this.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
    }
    
    private void createSpawnPosition(final WorldSettings p_73052_1_) {
        if (!this.provider.canRespawnHere()) {
            this.worldInfo.setSpawn(BlockPos.ORIGIN.up(this.provider.getAverageGroundLevel()));
        }
        else if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
            this.worldInfo.setSpawn(BlockPos.ORIGIN.up());
        }
        else {
            this.findingSpawnPoint = true;
            final WorldChunkManager worldchunkmanager = this.provider.getWorldChunkManager();
            final List<BiomeGenBase> list = worldchunkmanager.getBiomesToSpawnIn();
            final Random random = new Random(this.getSeed());
            final BlockPos blockpos = worldchunkmanager.findBiomePosition(0, 0, 256, list, random);
            int i = 0;
            final int j = this.provider.getAverageGroundLevel();
            int k = 0;
            if (blockpos != null) {
                i = blockpos.getX();
                k = blockpos.getZ();
            }
            else {
                WorldServer.logger.warn("Unable to find spawn biome");
            }
            int l = 0;
            while (!this.provider.canCoordinateBeSpawn(i, k)) {
                i += random.nextInt(64) - random.nextInt(64);
                k += random.nextInt(64) - random.nextInt(64);
                if (++l == 1000) {
                    break;
                }
            }
            this.worldInfo.setSpawn(new BlockPos(i, j, k));
            this.findingSpawnPoint = false;
            if (p_73052_1_.isBonusChestEnabled()) {
                this.createBonusChest();
            }
        }
    }
    
    protected void createBonusChest() {
        final WorldGeneratorBonusChest worldgeneratorbonuschest = new WorldGeneratorBonusChest(WorldServer.bonusChestContent, 10);
        for (int i = 0; i < 10; ++i) {
            final int j = this.worldInfo.getSpawnX() + this.rand.nextInt(6) - this.rand.nextInt(6);
            final int k = this.worldInfo.getSpawnZ() + this.rand.nextInt(6) - this.rand.nextInt(6);
            final BlockPos blockpos = this.getTopSolidOrLiquidBlock(new BlockPos(j, 0, k)).up();
            if (worldgeneratorbonuschest.generate(this, this.rand, blockpos)) {
                break;
            }
        }
    }
    
    public BlockPos getSpawnCoordinate() {
        return this.provider.getSpawnCoordinate();
    }
    
    public void saveAllChunks(final boolean p_73044_1_, final IProgressUpdate progressCallback) throws MinecraftException {
        if (this.chunkProvider.canSave()) {
            if (progressCallback != null) {
                progressCallback.displaySavingString("Saving level");
            }
            this.saveLevel();
            if (progressCallback != null) {
                progressCallback.displayLoadingString("Saving chunks");
            }
            this.chunkProvider.saveChunks(p_73044_1_, progressCallback);
            for (final Chunk chunk : Lists.newArrayList((Iterable<? extends Chunk>)this.theChunkProviderServer.func_152380_a())) {
                if (chunk != null && !this.thePlayerManager.hasPlayerInstance(chunk.xPosition, chunk.zPosition)) {
                    this.theChunkProviderServer.dropChunk(chunk.xPosition, chunk.zPosition);
                }
            }
        }
    }
    
    public void saveChunkData() {
        if (this.chunkProvider.canSave()) {
            this.chunkProvider.saveExtraData();
        }
    }
    
    protected void saveLevel() throws MinecraftException {
        this.checkSessionLock();
        this.worldInfo.setBorderSize(this.getWorldBorder().getDiameter());
        this.worldInfo.getBorderCenterX(this.getWorldBorder().getCenterX());
        this.worldInfo.getBorderCenterZ(this.getWorldBorder().getCenterZ());
        this.worldInfo.setBorderSafeZone(this.getWorldBorder().getDamageBuffer());
        this.worldInfo.setBorderDamagePerBlock(this.getWorldBorder().getDamageAmount());
        this.worldInfo.setBorderWarningDistance(this.getWorldBorder().getWarningDistance());
        this.worldInfo.setBorderWarningTime(this.getWorldBorder().getWarningTime());
        this.worldInfo.setBorderLerpTarget(this.getWorldBorder().getTargetSize());
        this.worldInfo.setBorderLerpTime(this.getWorldBorder().getTimeUntilTarget());
        this.saveHandler.saveWorldInfoWithPlayer(this.worldInfo, this.mcServer.getConfigurationManager().getHostPlayerData());
        this.mapStorage.saveAllData();
    }
    
    @Override
    protected void onEntityAdded(final Entity entityIn) {
        super.onEntityAdded(entityIn);
        this.entitiesById.addKey(entityIn.getEntityId(), entityIn);
        this.entitiesByUuid.put(entityIn.getUniqueID(), entityIn);
        final Entity[] aentity = entityIn.getParts();
        if (aentity != null) {
            for (int i = 0; i < aentity.length; ++i) {
                this.entitiesById.addKey(aentity[i].getEntityId(), aentity[i]);
            }
        }
    }
    
    @Override
    protected void onEntityRemoved(final Entity entityIn) {
        super.onEntityRemoved(entityIn);
        this.entitiesById.removeObject(entityIn.getEntityId());
        this.entitiesByUuid.remove(entityIn.getUniqueID());
        final Entity[] aentity = entityIn.getParts();
        if (aentity != null) {
            for (int i = 0; i < aentity.length; ++i) {
                this.entitiesById.removeObject(aentity[i].getEntityId());
            }
        }
    }
    
    @Override
    public boolean addWeatherEffect(final Entity entityIn) {
        if (super.addWeatherEffect(entityIn)) {
            this.mcServer.getConfigurationManager().sendToAllNear(entityIn.posX, entityIn.posY, entityIn.posZ, 512.0, this.provider.getDimensionId(), new S2CPacketSpawnGlobalEntity(entityIn));
            return true;
        }
        return false;
    }
    
    @Override
    public void setEntityState(final Entity entityIn, final byte state) {
        this.getEntityTracker().func_151248_b(entityIn, new S19PacketEntityStatus(entityIn, state));
    }
    
    @Override
    public Explosion newExplosion(final Entity entityIn, final double x, final double y, final double z, final float strength, final boolean isFlaming, final boolean isSmoking) {
        final Explosion explosion = new Explosion(this, entityIn, x, y, z, strength, isFlaming, isSmoking);
        explosion.doExplosionA();
        explosion.doExplosionB(false);
        if (!isSmoking) {
            explosion.func_180342_d();
        }
        for (final EntityPlayer entityplayer : this.playerEntities) {
            if (entityplayer.getDistanceSq(x, y, z) < 4096.0) {
                ((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(new S27PacketExplosion(x, y, z, strength, explosion.getAffectedBlockPositions(), explosion.getPlayerKnockbackMap().get(entityplayer)));
            }
        }
        return explosion;
    }
    
    @Override
    public void addBlockEvent(final BlockPos pos, final Block blockIn, final int eventID, final int eventParam) {
        final BlockEventData blockeventdata = new BlockEventData(pos, blockIn, eventID, eventParam);
        for (final BlockEventData blockeventdata2 : this.field_147490_S[this.blockEventCacheIndex]) {
            if (blockeventdata2.equals(blockeventdata)) {
                return;
            }
        }
        this.field_147490_S[this.blockEventCacheIndex].add(blockeventdata);
    }
    
    private void sendQueuedBlockEvents() {
        while (!this.field_147490_S[this.blockEventCacheIndex].isEmpty()) {
            final int i = this.blockEventCacheIndex;
            this.blockEventCacheIndex ^= 0x1;
            for (final BlockEventData blockeventdata : this.field_147490_S[i]) {
                if (this.fireBlockEvent(blockeventdata)) {
                    this.mcServer.getConfigurationManager().sendToAllNear(blockeventdata.getPosition().getX(), blockeventdata.getPosition().getY(), blockeventdata.getPosition().getZ(), 64.0, this.provider.getDimensionId(), new S24PacketBlockAction(blockeventdata.getPosition(), blockeventdata.getBlock(), blockeventdata.getEventID(), blockeventdata.getEventParameter()));
                }
            }
            this.field_147490_S[i].clear();
        }
    }
    
    private boolean fireBlockEvent(final BlockEventData event) {
        final IBlockState iblockstate = this.getBlockState(event.getPosition());
        return iblockstate.getBlock() == event.getBlock() && iblockstate.getBlock().onBlockEventReceived(this, event.getPosition(), iblockstate, event.getEventID(), event.getEventParameter());
    }
    
    public void flush() {
        this.saveHandler.flush();
    }
    
    @Override
    protected void updateWeather() {
        final boolean flag = this.isRaining();
        super.updateWeather();
        if (this.prevRainingStrength != this.rainingStrength) {
            this.mcServer.getConfigurationManager().sendPacketToAllPlayersInDimension(new S2BPacketChangeGameState(7, this.rainingStrength), this.provider.getDimensionId());
        }
        if (this.prevThunderingStrength != this.thunderingStrength) {
            this.mcServer.getConfigurationManager().sendPacketToAllPlayersInDimension(new S2BPacketChangeGameState(8, this.thunderingStrength), this.provider.getDimensionId());
        }
        if (flag != this.isRaining()) {
            if (flag) {
                this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(2, 0.0f));
            }
            else {
                this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(1, 0.0f));
            }
            this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(7, this.rainingStrength));
            this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(8, this.thunderingStrength));
        }
    }
    
    @Override
    protected int getRenderDistanceChunks() {
        return this.mcServer.getConfigurationManager().getViewDistance();
    }
    
    public MinecraftServer getMinecraftServer() {
        return this.mcServer;
    }
    
    public EntityTracker getEntityTracker() {
        return this.theEntityTracker;
    }
    
    public PlayerManager getPlayerManager() {
        return this.thePlayerManager;
    }
    
    public Teleporter getDefaultTeleporter() {
        return this.worldTeleporter;
    }
    
    public void spawnParticle(final EnumParticleTypes particleType, final double xCoord, final double yCoord, final double zCoord, final int numberOfParticles, final double p_175739_9_, final double p_175739_11_, final double p_175739_13_, final double p_175739_15_, final int... p_175739_17_) {
        this.spawnParticle(particleType, false, xCoord, yCoord, zCoord, numberOfParticles, p_175739_9_, p_175739_11_, p_175739_13_, p_175739_15_, p_175739_17_);
    }
    
    public void spawnParticle(final EnumParticleTypes particleType, final boolean longDistance, final double xCoord, final double yCoord, final double zCoord, final int numberOfParticles, final double xOffset, final double yOffset, final double zOffset, final double particleSpeed, final int... p_180505_18_) {
        final Packet packet = new S2APacketParticles(particleType, longDistance, (float)xCoord, (float)yCoord, (float)zCoord, (float)xOffset, (float)yOffset, (float)zOffset, (float)particleSpeed, numberOfParticles, p_180505_18_);
        for (int i = 0; i < this.playerEntities.size(); ++i) {
            final EntityPlayerMP entityplayermp = this.playerEntities.get(i);
            final BlockPos blockpos = entityplayermp.getPosition();
            final double d0 = blockpos.distanceSq(xCoord, yCoord, zCoord);
            if (d0 <= 256.0 || (longDistance && d0 <= 65536.0)) {
                entityplayermp.playerNetServerHandler.sendPacket(packet);
            }
        }
    }
    
    public Entity getEntityFromUuid(final UUID uuid) {
        return this.entitiesByUuid.get(uuid);
    }
    
    @Override
    public ListenableFuture<Object> addScheduledTask(final Runnable runnableToSchedule) {
        return this.mcServer.addScheduledTask(runnableToSchedule);
    }
    
    @Override
    public boolean isCallingFromMinecraftThread() {
        return this.mcServer.isCallingFromMinecraftThread();
    }
    
    static class ServerBlockEventList extends ArrayList<BlockEventData>
    {
        private ServerBlockEventList() {
        }
    }
}
