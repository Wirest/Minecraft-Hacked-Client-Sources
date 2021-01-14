package net.minecraft.world;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEventData;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.INpc;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S24PacketBlockAction;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.ScoreboardSaveData;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.village.VillageCollection;
import net.minecraft.village.VillageSiege;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.feature.WorldGeneratorBonusChest;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldServer extends World implements IThreadListener {
    private static final Logger logger = LogManager.getLogger();
    private final MinecraftServer mcServer;
    private final EntityTracker theEntityTracker;
    private final PlayerManager thePlayerManager;
    private final Set pendingTickListEntriesHashSet = Sets.newHashSet();

    /**
     * All work to do in future ticks.
     */
    private final TreeSet pendingTickListEntriesTreeSet = new TreeSet();
    private final Map entitiesByUuid = Maps.newHashMap();
    public ChunkProviderServer theChunkProviderServer;

    /**
     * Whether level saving is disabled or not
     */
    public boolean disableLevelSaving;

    /**
     * is false if there are no players
     */
    private boolean allPlayersSleeping;
    private int updateEntityTick;

    /**
     * the teleporter to use when the entity is being transferred into the
     * dimension
     */
    private final Teleporter worldTeleporter;
    private final SpawnerAnimals field_175742_R = new SpawnerAnimals();
    protected final VillageSiege villageSiege = new VillageSiege(this);
    private WorldServer.ServerBlockEventList[] field_147490_S = new WorldServer.ServerBlockEventList[]{new WorldServer.ServerBlockEventList(null), new WorldServer.ServerBlockEventList(null)};
    private int blockEventCacheIndex;
    private static final List bonusChestContent = Lists.newArrayList(new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.stick, 0, 1, 3, 10), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.planks), 0, 1, 3, 10), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.log), 0, 1, 3, 10), new WeightedRandomChestContent(Items.stone_axe, 0, 1, 1, 3), new WeightedRandomChestContent(Items.wooden_axe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.stone_pickaxe, 0, 1, 1, 3), new WeightedRandomChestContent(Items.wooden_pickaxe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.apple, 0, 2, 3, 5), new WeightedRandomChestContent(Items.bread, 0, 2, 3, 3), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.log2), 0, 1, 3, 10)});
    private List pendingTickListEntriesThisTick = Lists.newArrayList();
    private static final String __OBFID = "CL_00001437";

    public WorldServer(MinecraftServer server, ISaveHandler saveHandlerIn, WorldInfo info, int dimensionId, Profiler profilerIn) {
        super(saveHandlerIn, info, WorldProvider.getProviderForDimension(dimensionId), profilerIn, false);
        mcServer = server;
        theEntityTracker = new EntityTracker(this);
        thePlayerManager = new PlayerManager(this);
        provider.registerWorld(this);
        chunkProvider = createChunkProvider();
        worldTeleporter = new Teleporter(this);
        calculateInitialSkylight();
        calculateInitialWeather();
        getWorldBorder().setSize(server.getMaxWorldSize());
    }

    @Override
    public World init() {
        mapStorage = new MapStorage(saveHandler);
        String var1 = VillageCollection.func_176062_a(provider);
        VillageCollection var2 = (VillageCollection) mapStorage.loadData(VillageCollection.class, var1);

        if (var2 == null) {
            villageCollectionObj = new VillageCollection(this);
            mapStorage.setData(var1, villageCollectionObj);
        } else {
            villageCollectionObj = var2;
            villageCollectionObj.func_82566_a(this);
        }

        worldScoreboard = new ServerScoreboard(mcServer);
        ScoreboardSaveData var3 = (ScoreboardSaveData) mapStorage.loadData(ScoreboardSaveData.class, "scoreboard");

        if (var3 == null) {
            var3 = new ScoreboardSaveData();
            mapStorage.setData("scoreboard", var3);
        }

        var3.func_96499_a(worldScoreboard);
        ((ServerScoreboard) worldScoreboard).func_96547_a(var3);
        getWorldBorder().setCenter(worldInfo.func_176120_C(), worldInfo.func_176126_D());
        getWorldBorder().func_177744_c(worldInfo.func_176140_I());
        getWorldBorder().setDamageBuffer(worldInfo.func_176138_H());
        getWorldBorder().setWarningDistance(worldInfo.func_176131_J());
        getWorldBorder().setWarningTime(worldInfo.func_176139_K());

        if (worldInfo.func_176134_F() > 0L) {
            getWorldBorder().setTransition(worldInfo.func_176137_E(), worldInfo.func_176132_G(), worldInfo.func_176134_F());
        } else {
            getWorldBorder().setTransition(worldInfo.func_176137_E());
        }

        return this;
    }

    /**
     * Runs a single tick for the world
     */
    @Override
    public void tick() {
        super.tick();

        if (getWorldInfo().isHardcoreModeEnabled() && getDifficulty() != EnumDifficulty.HARD) {
            getWorldInfo().setDifficulty(EnumDifficulty.HARD);
        }

        provider.getWorldChunkManager().cleanupCache();

        if (areAllPlayersAsleep()) {
            if (getGameRules().getGameRuleBooleanValue("doDaylightCycle")) {
                long var1 = worldInfo.getWorldTime() + 24000L;
                worldInfo.setWorldTime(var1 - var1 % 24000L);
            }

            wakeAllPlayers();
        }

        theProfiler.startSection("mobSpawner");

        if (getGameRules().getGameRuleBooleanValue("doMobSpawning") && worldInfo.getTerrainType() != WorldType.DEBUG_WORLD) {
            field_175742_R.findChunksForSpawning(this, spawnHostileMobs, spawnPeacefulMobs, worldInfo.getWorldTotalTime() % 400L == 0L);
        }

        theProfiler.endStartSection("chunkSource");
        chunkProvider.unloadQueuedChunks();
        int var3 = calculateSkylightSubtracted(1.0F);

        if (var3 != getSkylightSubtracted()) {
            setSkylightSubtracted(var3);
        }

        worldInfo.incrementTotalWorldTime(worldInfo.getWorldTotalTime() + 1L);

        if (getGameRules().getGameRuleBooleanValue("doDaylightCycle")) {
            worldInfo.setWorldTime(worldInfo.getWorldTime() + 1L);
        }

        theProfiler.endStartSection("tickPending");
        tickUpdates(false);
        theProfiler.endStartSection("tickBlocks");
        func_147456_g();
        theProfiler.endStartSection("chunkMap");
        thePlayerManager.updatePlayerInstances();
        theProfiler.endStartSection("village");
        villageCollectionObj.tick();
        villageSiege.tick();
        theProfiler.endStartSection("portalForcer");
        worldTeleporter.removeStalePortalLocations(getTotalWorldTime());
        theProfiler.endSection();
        func_147488_Z();
    }

    public BiomeGenBase.SpawnListEntry func_175734_a(EnumCreatureType p_175734_1_, BlockPos p_175734_2_) {
        List var3 = getChunkProvider().func_177458_a(p_175734_1_, p_175734_2_);
        return var3 != null && !var3.isEmpty() ? (BiomeGenBase.SpawnListEntry) WeightedRandom.getRandomItem(rand, var3) : null;
    }

    public boolean func_175732_a(EnumCreatureType p_175732_1_, BiomeGenBase.SpawnListEntry p_175732_2_, BlockPos p_175732_3_) {
        List var4 = getChunkProvider().func_177458_a(p_175732_1_, p_175732_3_);
        return var4 != null && !var4.isEmpty() ? var4.contains(p_175732_2_) : false;
    }

    /**
     * Updates the flag that indicates whether or not all players in the world
     * are sleeping.
     */
    @Override
    public void updateAllPlayersSleepingFlag() {
        allPlayersSleeping = false;

        if (!playerEntities.isEmpty()) {
            int var1 = 0;
            int var2 = 0;
            Iterator var3 = playerEntities.iterator();

            while (var3.hasNext()) {
                EntityPlayer var4 = (EntityPlayer) var3.next();

                if (var4.func_175149_v()) {
                    ++var1;
                } else if (var4.isPlayerSleeping()) {
                    ++var2;
                }
            }

            allPlayersSleeping = var2 > 0 && var2 >= playerEntities.size() - var1;
        }
    }

    protected void wakeAllPlayers() {
        allPlayersSleeping = false;
        Iterator var1 = playerEntities.iterator();

        while (var1.hasNext()) {
            EntityPlayer var2 = (EntityPlayer) var1.next();

            if (var2.isPlayerSleeping()) {
                var2.wakeUpPlayer(false, false, true);
            }
        }

        resetRainAndThunder();
    }

    private void resetRainAndThunder() {
        worldInfo.setRainTime(0);
        worldInfo.setRaining(false);
        worldInfo.setThunderTime(0);
        worldInfo.setThundering(false);
    }

    public boolean areAllPlayersAsleep() {
        if (allPlayersSleeping && !isRemote) {
            Iterator var1 = playerEntities.iterator();
            EntityPlayer var2;

            do {
                if (!var1.hasNext()) {
                    return true;
                }

                var2 = (EntityPlayer) var1.next();
            } while (!var2.func_175149_v() && var2.isPlayerFullyAsleep());

            return false;
        } else {
            return false;
        }
    }

    /**
     * Sets a new spawn location by finding an uncovered block at a random (x,z)
     * location in the chunk.
     */
    @Override
    public void setInitialSpawnLocation() {
        if (worldInfo.getSpawnY() <= 0) {
            worldInfo.setSpawnY(64);
        }

        int var1 = worldInfo.getSpawnX();
        int var2 = worldInfo.getSpawnZ();
        int var3 = 0;

        while (getGroundAboveSeaLevel(new BlockPos(var1, 0, var2)).getMaterial() == Material.air) {
            var1 += rand.nextInt(8) - rand.nextInt(8);
            var2 += rand.nextInt(8) - rand.nextInt(8);
            ++var3;

            if (var3 == 10000) {
                break;
            }
        }

        worldInfo.setSpawnX(var1);
        worldInfo.setSpawnZ(var2);
    }

    @Override
    protected void func_147456_g() {
        super.func_147456_g();

        if (worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
            Iterator var21 = activeChunkSet.iterator();

            while (var21.hasNext()) {
                ChunkCoordIntPair var22 = (ChunkCoordIntPair) var21.next();
                getChunkFromChunkCoords(var22.chunkXPos, var22.chunkZPos).func_150804_b(false);
            }
        } else {
            int var1 = 0;
            int var2 = 0;

            for (Iterator var3 = activeChunkSet.iterator(); var3.hasNext(); theProfiler.endSection()) {
                ChunkCoordIntPair var4 = (ChunkCoordIntPair) var3.next();
                int var5 = var4.chunkXPos * 16;
                int var6 = var4.chunkZPos * 16;
                theProfiler.startSection("getChunk");
                Chunk var7 = getChunkFromChunkCoords(var4.chunkXPos, var4.chunkZPos);
                func_147467_a(var5, var6, var7);
                theProfiler.endStartSection("tickChunk");
                var7.func_150804_b(false);
                theProfiler.endStartSection("thunder");
                int var8;
                BlockPos var9;

                if (rand.nextInt(100000) == 0 && isRaining() && isThundering()) {
                    updateLCG = updateLCG * 3 + 1013904223;
                    var8 = updateLCG >> 2;
                    var9 = func_175736_a(new BlockPos(var5 + (var8 & 15), 0, var6 + (var8 >> 8 & 15)));

                    if (func_175727_C(var9)) {
                        addWeatherEffect(new EntityLightningBolt(this, var9.getX(), var9.getY(), var9.getZ()));
                    }
                }

                theProfiler.endStartSection("iceandsnow");

                if (rand.nextInt(16) == 0) {
                    updateLCG = updateLCG * 3 + 1013904223;
                    var8 = updateLCG >> 2;
                    var9 = func_175725_q(new BlockPos(var5 + (var8 & 15), 0, var6 + (var8 >> 8 & 15)));
                    BlockPos var10 = var9.offsetDown();

                    if (func_175662_w(var10)) {
                        this.setBlockState(var10, Blocks.ice.getDefaultState());
                    }

                    if (isRaining() && func_175708_f(var9, true)) {
                        this.setBlockState(var9, Blocks.snow_layer.getDefaultState());
                    }

                    if (isRaining() && getBiomeGenForCoords(var10).canSpawnLightningBolt()) {
                        getBlockState(var10).getBlock().fillWithRain(this, var10);
                    }
                }

                theProfiler.endStartSection("tickBlocks");
                var8 = getGameRules().getInt("randomTickSpeed");

                if (var8 > 0) {
                    ExtendedBlockStorage[] var23 = var7.getBlockStorageArray();
                    int var24 = var23.length;

                    for (int var11 = 0; var11 < var24; ++var11) {
                        ExtendedBlockStorage var12 = var23[var11];

                        if (var12 != null && var12.getNeedsRandomTick()) {
                            for (int var13 = 0; var13 < var8; ++var13) {
                                updateLCG = updateLCG * 3 + 1013904223;
                                int var14 = updateLCG >> 2;
                                int var15 = var14 & 15;
                                int var16 = var14 >> 8 & 15;
                                int var17 = var14 >> 16 & 15;
                                ++var2;
                                BlockPos var18 = new BlockPos(var15 + var5, var17 + var12.getYLocation(), var16 + var6);
                                IBlockState var19 = var12.get(var15, var17, var16);
                                Block var20 = var19.getBlock();

                                if (var20.getTickRandomly()) {
                                    ++var1;
                                    var20.randomTick(this, var18, var19, rand);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected BlockPos func_175736_a(BlockPos p_175736_1_) {
        BlockPos var2 = func_175725_q(p_175736_1_);
        AxisAlignedBB var3 = (new AxisAlignedBB(var2, new BlockPos(var2.getX(), getHeight(), var2.getZ()))).expand(3.0D, 3.0D, 3.0D);
        List var4 = func_175647_a(EntityLivingBase.class, var3, new Predicate() {
            private static final String __OBFID = "CL_00001889";

            public boolean func_180242_a(EntityLivingBase p_180242_1_) {
                return p_180242_1_ != null && p_180242_1_.isEntityAlive() && WorldServer.this.isAgainstSky(p_180242_1_.getPosition());
            }

            @Override
            public boolean apply(Object p_apply_1_) {
                return func_180242_a((EntityLivingBase) p_apply_1_);
            }
        });
        return !var4.isEmpty() ? ((EntityLivingBase) var4.get(rand.nextInt(var4.size()))).getPosition() : var2;
    }

    @Override
    public boolean isBlockTickPending(BlockPos pos, Block blockType) {
        NextTickListEntry var3 = new NextTickListEntry(pos, blockType);
        return pendingTickListEntriesThisTick.contains(var3);
    }

    @Override
    public void scheduleUpdate(BlockPos pos, Block blockIn, int delay) {
        func_175654_a(pos, blockIn, delay, 0);
    }

    @Override
    public void func_175654_a(BlockPos p_175654_1_, Block p_175654_2_, int p_175654_3_, int p_175654_4_) {
        NextTickListEntry var5 = new NextTickListEntry(p_175654_1_, p_175654_2_);
        byte var6 = 0;

        if (scheduledUpdatesAreImmediate && p_175654_2_.getMaterial() != Material.air) {
            if (p_175654_2_.requiresUpdates()) {
                var6 = 8;

                if (this.isAreaLoaded(var5.field_180282_a.add(-var6, -var6, -var6), var5.field_180282_a.add(var6, var6, var6))) {
                    IBlockState var7 = getBlockState(var5.field_180282_a);

                    if (var7.getBlock().getMaterial() != Material.air && var7.getBlock() == var5.func_151351_a()) {
                        var7.getBlock().updateTick(this, var5.field_180282_a, var7, rand);
                    }
                }

                return;
            }

            p_175654_3_ = 1;
        }

        if (this.isAreaLoaded(p_175654_1_.add(-var6, -var6, -var6), p_175654_1_.add(var6, var6, var6))) {
            if (p_175654_2_.getMaterial() != Material.air) {
                var5.setScheduledTime(p_175654_3_ + worldInfo.getWorldTotalTime());
                var5.setPriority(p_175654_4_);
            }

            if (!pendingTickListEntriesHashSet.contains(var5)) {
                pendingTickListEntriesHashSet.add(var5);
                pendingTickListEntriesTreeSet.add(var5);
            }
        }
    }

    @Override
    public void func_180497_b(BlockPos p_180497_1_, Block p_180497_2_, int p_180497_3_, int p_180497_4_) {
        NextTickListEntry var5 = new NextTickListEntry(p_180497_1_, p_180497_2_);
        var5.setPriority(p_180497_4_);

        if (p_180497_2_.getMaterial() != Material.air) {
            var5.setScheduledTime(p_180497_3_ + worldInfo.getWorldTotalTime());
        }

        if (!pendingTickListEntriesHashSet.contains(var5)) {
            pendingTickListEntriesHashSet.add(var5);
            pendingTickListEntriesTreeSet.add(var5);
        }
    }

    /**
     * Updates (and cleans up) entities and tile entities
     */
    @Override
    public void updateEntities() {
        if (playerEntities.isEmpty()) {
            if (updateEntityTick++ >= 1200) {
                return;
            }
        } else {
            resetUpdateEntityTick();
        }

        super.updateEntities();
    }

    /**
     * Resets the updateEntityTick field to 0
     */
    public void resetUpdateEntityTick() {
        updateEntityTick = 0;
    }

    /**
     * Runs through the list of updates to run and ticks them
     */
    @Override
    public boolean tickUpdates(boolean p_72955_1_) {
        if (worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
            return false;
        } else {
            int var2 = pendingTickListEntriesTreeSet.size();

            if (var2 != pendingTickListEntriesHashSet.size()) {
                throw new IllegalStateException("TickNextTick list out of synch");
            } else {
                if (var2 > 1000) {
                    var2 = 1000;
                }

                theProfiler.startSection("cleaning");
                NextTickListEntry var4;

                for (int var3 = 0; var3 < var2; ++var3) {
                    var4 = (NextTickListEntry) pendingTickListEntriesTreeSet.first();

                    if (!p_72955_1_ && var4.scheduledTime > worldInfo.getWorldTotalTime()) {
                        break;
                    }

                    pendingTickListEntriesTreeSet.remove(var4);
                    pendingTickListEntriesHashSet.remove(var4);
                    pendingTickListEntriesThisTick.add(var4);
                }

                theProfiler.endSection();
                theProfiler.startSection("ticking");
                Iterator var11 = pendingTickListEntriesThisTick.iterator();

                while (var11.hasNext()) {
                    var4 = (NextTickListEntry) var11.next();
                    var11.remove();
                    byte var5 = 0;

                    if (this.isAreaLoaded(var4.field_180282_a.add(-var5, -var5, -var5), var4.field_180282_a.add(var5, var5, var5))) {
                        IBlockState var6 = getBlockState(var4.field_180282_a);

                        if (var6.getBlock().getMaterial() != Material.air && Block.isEqualTo(var6.getBlock(), var4.func_151351_a())) {
                            try {
                                var6.getBlock().updateTick(this, var4.field_180282_a, var6, rand);
                            } catch (Throwable var10) {
                                CrashReport var8 = CrashReport.makeCrashReport(var10, "Exception while ticking a block");
                                CrashReportCategory var9 = var8.makeCategory("Block being ticked");
                                CrashReportCategory.addBlockInfo(var9, var4.field_180282_a, var6);
                                throw new ReportedException(var8);
                            }
                        }
                    } else {
                        scheduleUpdate(var4.field_180282_a, var4.func_151351_a(), 0);
                    }
                }

                theProfiler.endSection();
                pendingTickListEntriesThisTick.clear();
                return !pendingTickListEntriesTreeSet.isEmpty();
            }
        }
    }

    @Override
    public List getPendingBlockUpdates(Chunk p_72920_1_, boolean p_72920_2_) {
        ChunkCoordIntPair var3 = p_72920_1_.getChunkCoordIntPair();
        int var4 = (var3.chunkXPos << 4) - 2;
        int var5 = var4 + 16 + 2;
        int var6 = (var3.chunkZPos << 4) - 2;
        int var7 = var6 + 16 + 2;
        return func_175712_a(new StructureBoundingBox(var4, 0, var6, var5, 256, var7), p_72920_2_);
    }

    @Override
    public List func_175712_a(StructureBoundingBox p_175712_1_, boolean p_175712_2_) {
        ArrayList var3 = null;

        for (int var4 = 0; var4 < 2; ++var4) {
            Iterator var5;

            if (var4 == 0) {
                var5 = pendingTickListEntriesTreeSet.iterator();
            } else {
                var5 = pendingTickListEntriesThisTick.iterator();

                if (!pendingTickListEntriesThisTick.isEmpty()) {
                    WorldServer.logger.debug("toBeTicked = " + pendingTickListEntriesThisTick.size());
                }
            }

            while (var5.hasNext()) {
                NextTickListEntry var6 = (NextTickListEntry) var5.next();
                BlockPos var7 = var6.field_180282_a;

                if (var7.getX() >= p_175712_1_.minX && var7.getX() < p_175712_1_.maxX && var7.getZ() >= p_175712_1_.minZ && var7.getZ() < p_175712_1_.maxZ) {
                    if (p_175712_2_) {
                        pendingTickListEntriesHashSet.remove(var6);
                        var5.remove();
                    }

                    if (var3 == null) {
                        var3 = Lists.newArrayList();
                    }

                    var3.add(var6);
                }
            }
        }

        return var3;
    }

    /**
     * Will update the entity in the world if the chunk the entity is in is
     * currently loaded or its forced to update. Args: entity, forceUpdate
     */
    @Override
    public void updateEntityWithOptionalForce(Entity p_72866_1_, boolean p_72866_2_) {
        if (!func_175735_ai() && (p_72866_1_ instanceof EntityAnimal || p_72866_1_ instanceof EntityWaterMob)) {
            p_72866_1_.setDead();
        }

        if (!func_175738_ah() && p_72866_1_ instanceof INpc) {
            p_72866_1_.setDead();
        }

        super.updateEntityWithOptionalForce(p_72866_1_, p_72866_2_);
    }

    private boolean func_175738_ah() {
        return mcServer.getCanSpawnNPCs();
    }

    private boolean func_175735_ai() {
        return mcServer.getCanSpawnAnimals();
    }

    /**
     * Creates the chunk provider for this world. Called in the constructor.
     * Retrieves provider from worldProvider?
     */
    @Override
    protected IChunkProvider createChunkProvider() {
        IChunkLoader var1 = saveHandler.getChunkLoader(provider);
        theChunkProviderServer = new ChunkProviderServer(this, var1, provider.createChunkGenerator());
        return theChunkProviderServer;
    }

    public List func_147486_a(int p_147486_1_, int p_147486_2_, int p_147486_3_, int p_147486_4_, int p_147486_5_, int p_147486_6_) {
        ArrayList var7 = Lists.newArrayList();

        for (int var8 = 0; var8 < loadedTileEntityList.size(); ++var8) {
            TileEntity var9 = (TileEntity) loadedTileEntityList.get(var8);
            BlockPos var10 = var9.getPos();

            if (var10.getX() >= p_147486_1_ && var10.getY() >= p_147486_2_ && var10.getZ() >= p_147486_3_ && var10.getX() < p_147486_4_ && var10.getY() < p_147486_5_ && var10.getZ() < p_147486_6_) {
                var7.add(var9);
            }
        }

        return var7;
    }

    @Override
    public boolean isBlockModifiable(EntityPlayer p_175660_1_, BlockPos p_175660_2_) {
        return !mcServer.isBlockProtected(this, p_175660_2_, p_175660_1_) && getWorldBorder().contains(p_175660_2_);
    }

    @Override
    public void initialize(WorldSettings settings) {
        if (!worldInfo.isInitialized()) {
            try {
                createSpawnPosition(settings);

                if (worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
                    setDebugWorldSettings();
                }

                super.initialize(settings);
            } catch (Throwable var6) {
                CrashReport var3 = CrashReport.makeCrashReport(var6, "Exception initializing level");

                try {
                    addWorldInfoToCrashReport(var3);
                } catch (Throwable var5) {
                    ;
                }

                throw new ReportedException(var3);
            }

            worldInfo.setServerInitialized(true);
        }
    }

    private void setDebugWorldSettings() {
        worldInfo.setMapFeaturesEnabled(false);
        worldInfo.setAllowCommands(true);
        worldInfo.setRaining(false);
        worldInfo.setThundering(false);
        worldInfo.func_176142_i(1000000000);
        worldInfo.setWorldTime(6000L);
        worldInfo.setGameType(WorldSettings.GameType.SPECTATOR);
        worldInfo.setHardcore(false);
        worldInfo.setDifficulty(EnumDifficulty.PEACEFUL);
        worldInfo.setDifficultyLocked(true);
        getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
    }

    /**
     * creates a spawn position at random within 256 blocks of 0,0
     */
    private void createSpawnPosition(WorldSettings p_73052_1_) {
        if (!provider.canRespawnHere()) {
            worldInfo.setSpawn(BlockPos.ORIGIN.offsetUp(provider.getAverageGroundLevel()));
        } else if (worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
            worldInfo.setSpawn(BlockPos.ORIGIN.offsetUp());
        } else {
            findingSpawnPoint = true;
            WorldChunkManager var2 = provider.getWorldChunkManager();
            List var3 = var2.getBiomesToSpawnIn();
            Random var4 = new Random(getSeed());
            BlockPos var5 = var2.findBiomePosition(0, 0, 256, var3, var4);
            int var6 = 0;
            int var7 = provider.getAverageGroundLevel();
            int var8 = 0;

            if (var5 != null) {
                var6 = var5.getX();
                var8 = var5.getZ();
            } else {
                WorldServer.logger.warn("Unable to find spawn biome");
            }

            int var9 = 0;

            while (!provider.canCoordinateBeSpawn(var6, var8)) {
                var6 += var4.nextInt(64) - var4.nextInt(64);
                var8 += var4.nextInt(64) - var4.nextInt(64);
                ++var9;

                if (var9 == 1000) {
                    break;
                }
            }

            worldInfo.setSpawn(new BlockPos(var6, var7, var8));
            findingSpawnPoint = false;

            if (p_73052_1_.isBonusChestEnabled()) {
                createBonusChest();
            }
        }
    }

    /**
     * Creates the bonus chest in the world.
     */
    protected void createBonusChest() {
        WorldGeneratorBonusChest var1 = new WorldGeneratorBonusChest(WorldServer.bonusChestContent, 10);

        for (int var2 = 0; var2 < 10; ++var2) {
            int var3 = worldInfo.getSpawnX() + rand.nextInt(6) - rand.nextInt(6);
            int var4 = worldInfo.getSpawnZ() + rand.nextInt(6) - rand.nextInt(6);
            BlockPos var5 = func_175672_r(new BlockPos(var3, 0, var4)).offsetUp();

            if (var1.generate(this, rand, var5)) {
                break;
            }
        }
    }

    public BlockPos func_180504_m() {
        return provider.func_177496_h();
    }

    /**
     * Saves all chunks to disk while updating progress bar.
     */
    public void saveAllChunks(boolean p_73044_1_, IProgressUpdate p_73044_2_) throws MinecraftException {
        if (chunkProvider.canSave()) {
            if (p_73044_2_ != null) {
                p_73044_2_.displaySavingString("Saving level");
            }

            saveLevel();

            if (p_73044_2_ != null) {
                p_73044_2_.displayLoadingString("Saving chunks");
            }

            chunkProvider.saveChunks(p_73044_1_, p_73044_2_);
            List var3 = theChunkProviderServer.func_152380_a();
            Iterator var4 = var3.iterator();

            while (var4.hasNext()) {
                Chunk var5 = (Chunk) var4.next();

                if (!thePlayerManager.func_152621_a(var5.xPosition, var5.zPosition)) {
                    theChunkProviderServer.dropChunk(var5.xPosition, var5.zPosition);
                }
            }
        }
    }

    /**
     * saves chunk data - currently only called during execution of the Save All
     * command
     */
    public void saveChunkData() {
        if (chunkProvider.canSave()) {
            chunkProvider.saveExtraData();
        }
    }

    /**
     * Saves the chunks to disk.
     */
    protected void saveLevel() throws MinecraftException {
        checkSessionLock();
        worldInfo.func_176145_a(getWorldBorder().getDiameter());
        worldInfo.func_176124_d(getWorldBorder().getCenterX());
        worldInfo.func_176141_c(getWorldBorder().getCenterZ());
        worldInfo.func_176129_e(getWorldBorder().getDamageBuffer());
        worldInfo.func_176125_f(getWorldBorder().func_177727_n());
        worldInfo.func_176122_j(getWorldBorder().getWarningDistance());
        worldInfo.func_176136_k(getWorldBorder().getWarningTime());
        worldInfo.func_176118_b(getWorldBorder().getTargetSize());
        worldInfo.func_176135_e(getWorldBorder().getTimeUntilTarget());
        saveHandler.saveWorldInfoWithPlayer(worldInfo, mcServer.getConfigurationManager().getHostPlayerData());
        mapStorage.saveAllData();
    }

    @Override
    protected void onEntityAdded(Entity p_72923_1_) {
        super.onEntityAdded(p_72923_1_);
        entitiesById.addKey(p_72923_1_.getEntityId(), p_72923_1_);
        entitiesByUuid.put(p_72923_1_.getUniqueID(), p_72923_1_);
        Entity[] var2 = p_72923_1_.getParts();

        if (var2 != null) {
            for (Entity element : var2) {
                entitiesById.addKey(element.getEntityId(), element);
            }
        }
    }

    @Override
    protected void onEntityRemoved(Entity p_72847_1_) {
        super.onEntityRemoved(p_72847_1_);
        entitiesById.removeObject(p_72847_1_.getEntityId());
        entitiesByUuid.remove(p_72847_1_.getUniqueID());
        Entity[] var2 = p_72847_1_.getParts();

        if (var2 != null) {
            for (Entity element : var2) {
                entitiesById.removeObject(element.getEntityId());
            }
        }
    }

    /**
     * adds a lightning bolt to the list of lightning bolts in this world.
     */
    @Override
    public boolean addWeatherEffect(Entity p_72942_1_) {
        if (super.addWeatherEffect(p_72942_1_)) {
            mcServer.getConfigurationManager().sendToAllNear(p_72942_1_.posX, p_72942_1_.posY, p_72942_1_.posZ, 512.0D, provider.getDimensionId(), new S2CPacketSpawnGlobalEntity(p_72942_1_));
            return true;
        } else {
            return false;
        }
    }

    /**
     * sends a Packet 38 (Entity Status) to all tracked players of that entity
     */
    @Override
    public void setEntityState(Entity entityIn, byte p_72960_2_) {
        getEntityTracker().func_151248_b(entityIn, new S19PacketEntityStatus(entityIn, p_72960_2_));
    }

    /**
     * returns a new explosion. Does initiation (at time of writing Explosion is
     * not finished)
     */
    @Override
    public Explosion newExplosion(Entity p_72885_1_, double p_72885_2_, double p_72885_4_, double p_72885_6_, float p_72885_8_, boolean p_72885_9_, boolean p_72885_10_) {
        Explosion var11 = new Explosion(this, p_72885_1_, p_72885_2_, p_72885_4_, p_72885_6_, p_72885_8_, p_72885_9_, p_72885_10_);
        var11.doExplosionA();
        var11.doExplosionB(false);

        if (!p_72885_10_) {
            var11.func_180342_d();
        }

        Iterator var12 = playerEntities.iterator();

        while (var12.hasNext()) {
            EntityPlayer var13 = (EntityPlayer) var12.next();

            if (var13.getDistanceSq(p_72885_2_, p_72885_4_, p_72885_6_) < 4096.0D) {
                ((EntityPlayerMP) var13).playerNetServerHandler.sendPacket(new S27PacketExplosion(p_72885_2_, p_72885_4_, p_72885_6_, p_72885_8_, var11.func_180343_e(), (Vec3) var11.func_77277_b().get(var13)));
            }
        }

        return var11;
    }

    @Override
    public void addBlockEvent(BlockPos pos, Block blockIn, int eventID, int eventParam) {
        BlockEventData var5 = new BlockEventData(pos, blockIn, eventID, eventParam);
        Iterator var6 = field_147490_S[blockEventCacheIndex].iterator();
        BlockEventData var7;

        do {
            if (!var6.hasNext()) {
                field_147490_S[blockEventCacheIndex].add(var5);
                return;
            }

            var7 = (BlockEventData) var6.next();
        } while (!var7.equals(var5));
    }

    private void func_147488_Z() {
        while (!field_147490_S[blockEventCacheIndex].isEmpty()) {
            int var1 = blockEventCacheIndex;
            blockEventCacheIndex ^= 1;
            Iterator var2 = field_147490_S[var1].iterator();

            while (var2.hasNext()) {
                BlockEventData var3 = (BlockEventData) var2.next();

                if (func_147485_a(var3)) {
                    mcServer.getConfigurationManager().sendToAllNear(var3.func_180328_a().getX(), var3.func_180328_a().getY(), var3.func_180328_a().getZ(), 64.0D, provider.getDimensionId(), new S24PacketBlockAction(var3.func_180328_a(), var3.getBlock(), var3.getEventID(), var3.getEventParameter()));
                }
            }

            field_147490_S[var1].clear();
        }
    }

    private boolean func_147485_a(BlockEventData p_147485_1_) {
        IBlockState var2 = getBlockState(p_147485_1_.func_180328_a());
        return var2.getBlock() == p_147485_1_.getBlock() ? var2.getBlock().onBlockEventReceived(this, p_147485_1_.func_180328_a(), var2, p_147485_1_.getEventID(), p_147485_1_.getEventParameter()) : false;
    }

    /**
     * Syncs all changes to disk and wait for completion.
     */
    public void flush() {
        saveHandler.flush();
    }

    /**
     * Updates all weather states.
     */
    @Override
    protected void updateWeather() {
        boolean var1 = isRaining();
        super.updateWeather();

        if (prevRainingStrength != rainingStrength) {
            mcServer.getConfigurationManager().sendPacketToAllPlayersInDimension(new S2BPacketChangeGameState(7, rainingStrength), provider.getDimensionId());
        }

        if (prevThunderingStrength != thunderingStrength) {
            mcServer.getConfigurationManager().sendPacketToAllPlayersInDimension(new S2BPacketChangeGameState(8, thunderingStrength), provider.getDimensionId());
        }

        if (var1 != isRaining()) {
            if (var1) {
                mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(2, 0.0F));
            } else {
                mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(1, 0.0F));
            }

            mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(7, rainingStrength));
            mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(8, thunderingStrength));
        }
    }

    @Override
    protected int getRenderDistanceChunks() {
        return mcServer.getConfigurationManager().getViewDistance();
    }

    public MinecraftServer func_73046_m() {
        return mcServer;
    }

    /**
     * Gets the EntityTracker
     */
    public EntityTracker getEntityTracker() {
        return theEntityTracker;
    }

    public PlayerManager getPlayerManager() {
        return thePlayerManager;
    }

    public Teleporter getDefaultTeleporter() {
        return worldTeleporter;
    }

    public void func_175739_a(EnumParticleTypes p_175739_1_, double p_175739_2_, double p_175739_4_, double p_175739_6_, int p_175739_8_, double p_175739_9_, double p_175739_11_, double p_175739_13_, double p_175739_15_, int... p_175739_17_) {
        func_180505_a(p_175739_1_, false, p_175739_2_, p_175739_4_, p_175739_6_, p_175739_8_, p_175739_9_, p_175739_11_, p_175739_13_, p_175739_15_, p_175739_17_);
    }

    public void func_180505_a(EnumParticleTypes p_180505_1_, boolean p_180505_2_, double p_180505_3_, double p_180505_5_, double p_180505_7_, int p_180505_9_, double p_180505_10_, double p_180505_12_, double p_180505_14_, double p_180505_16_, int... p_180505_18_) {
        S2APacketParticles var19 = new S2APacketParticles(p_180505_1_, p_180505_2_, (float) p_180505_3_, (float) p_180505_5_, (float) p_180505_7_, (float) p_180505_10_, (float) p_180505_12_, (float) p_180505_14_, (float) p_180505_16_, p_180505_9_, p_180505_18_);

        for (int var20 = 0; var20 < playerEntities.size(); ++var20) {
            EntityPlayerMP var21 = (EntityPlayerMP) playerEntities.get(var20);
            BlockPos var22 = var21.getPosition();
            double var23 = var22.distanceSq(p_180505_3_, p_180505_5_, p_180505_7_);

            if (var23 <= 256.0D || p_180505_2_ && var23 <= 65536.0D) {
                var21.playerNetServerHandler.sendPacket(var19);
            }
        }
    }

    public Entity getEntityFromUuid(UUID uuid) {
        return (Entity) entitiesByUuid.get(uuid);
    }

    @Override
    public ListenableFuture addScheduledTask(Runnable runnableToSchedule) {
        return mcServer.addScheduledTask(runnableToSchedule);
    }

    @Override
    public boolean isCallingFromMinecraftThread() {
        return mcServer.isCallingFromMinecraftThread();
    }

    static class ServerBlockEventList extends ArrayList {
        /**
         *
         */
        private static final long serialVersionUID = 1L;
        private static final String __OBFID = "CL_00001439";

        private ServerBlockEventList() {
        }

        ServerBlockEventList(Object p_i1521_1_) {
            this();
        }
    }
}
