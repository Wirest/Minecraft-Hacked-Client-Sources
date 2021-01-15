package net.minecraft.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ListenableFuture;

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

public class WorldServer extends World implements IThreadListener
{
    private static final Logger logger = LogManager.getLogger();
    private final MinecraftServer mcServer;
    private final EntityTracker theEntityTracker;
    private final PlayerManager thePlayerManager;
    private final Set pendingTickListEntriesHashSet = Sets.newHashSet();

    /** All work to do in future ticks. */
    private final TreeSet pendingTickListEntriesTreeSet = new TreeSet();
    private final Map entitiesByUuid = Maps.newHashMap();
    public ChunkProviderServer theChunkProviderServer;

    /** Whether level saving is disabled or not */
    public boolean disableLevelSaving;

    /** is false if there are no players */
    private boolean allPlayersSleeping;
    private int updateEntityTick;

    /**
     * the teleporter to use when the entity is being transferred into the dimension
     */
    private final Teleporter worldTeleporter;
    private final SpawnerAnimals mobSpawner = new SpawnerAnimals();
    protected final VillageSiege villageSiege = new VillageSiege(this);
    private WorldServer.ServerBlockEventList[] field_147490_S = new WorldServer.ServerBlockEventList[] {new WorldServer.ServerBlockEventList(null), new WorldServer.ServerBlockEventList(null)};
    private int blockEventCacheIndex;
    private static final List bonusChestContent = Lists.newArrayList(new WeightedRandomChestContent[] {new WeightedRandomChestContent(Items.stick, 0, 1, 3, 10), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.planks), 0, 1, 3, 10), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.log), 0, 1, 3, 10), new WeightedRandomChestContent(Items.stone_axe, 0, 1, 1, 3), new WeightedRandomChestContent(Items.wooden_axe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.stone_pickaxe, 0, 1, 1, 3), new WeightedRandomChestContent(Items.wooden_pickaxe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.apple, 0, 2, 3, 5), new WeightedRandomChestContent(Items.bread, 0, 2, 3, 3), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.log2), 0, 1, 3, 10)});
    private List pendingTickListEntriesThisTick = Lists.newArrayList();

    public WorldServer(MinecraftServer server, ISaveHandler saveHandlerIn, WorldInfo info, int dimensionId, Profiler profilerIn)
    {
        super(saveHandlerIn, info, WorldProvider.getProviderForDimension(dimensionId), profilerIn, false);
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
	public World init()
    {
        this.mapStorage = new MapStorage(this.saveHandler);
        String var1 = VillageCollection.fileNameForProvider(this.provider);
        VillageCollection var2 = (VillageCollection)this.mapStorage.loadData(VillageCollection.class, var1);

        if (var2 == null)
        {
            this.villageCollectionObj = new VillageCollection(this);
            this.mapStorage.setData(var1, this.villageCollectionObj);
        }
        else
        {
            this.villageCollectionObj = var2;
            this.villageCollectionObj.setWorldsForAll(this);
        }

        this.worldScoreboard = new ServerScoreboard(this.mcServer);
        ScoreboardSaveData var3 = (ScoreboardSaveData)this.mapStorage.loadData(ScoreboardSaveData.class, "scoreboard");

        if (var3 == null)
        {
            var3 = new ScoreboardSaveData();
            this.mapStorage.setData("scoreboard", var3);
        }

        var3.setScoreboard(this.worldScoreboard);
        ((ServerScoreboard)this.worldScoreboard).func_96547_a(var3);
        this.getWorldBorder().setCenter(this.worldInfo.getBorderCenterX(), this.worldInfo.getBorderCenterZ());
        this.getWorldBorder().setDamageAmount(this.worldInfo.getBorderDamagePerBlock());
        this.getWorldBorder().setDamageBuffer(this.worldInfo.getBorderSafeZone());
        this.getWorldBorder().setWarningDistance(this.worldInfo.getBorderWarningDistance());
        this.getWorldBorder().setWarningTime(this.worldInfo.getBorderWarningTime());

        if (this.worldInfo.getBorderLerpTime() > 0L)
        {
            this.getWorldBorder().setTransition(this.worldInfo.func_176137_E(), this.worldInfo.getBorderLerpTarget(), this.worldInfo.getBorderLerpTime());
        }
        else
        {
            this.getWorldBorder().setTransition(this.worldInfo.func_176137_E());
        }

        return this;
    }

    /**
     * Runs a single tick for the world
     */
    @Override
	public void tick()
    {
        super.tick();

        if (this.getWorldInfo().isHardcoreModeEnabled() && this.getDifficulty() != EnumDifficulty.HARD)
        {
            this.getWorldInfo().setDifficulty(EnumDifficulty.HARD);
        }

        this.provider.getWorldChunkManager().cleanupCache();

        if (this.areAllPlayersAsleep())
        {
            if (this.getGameRules().getGameRuleBooleanValue("doDaylightCycle"))
            {
                long var1 = this.worldInfo.getWorldTime() + 24000L;
                this.worldInfo.setWorldTime(var1 - var1 % 24000L);
            }

            this.wakeAllPlayers();
        }

        this.theProfiler.startSection("mobSpawner");

        if (this.getGameRules().getGameRuleBooleanValue("doMobSpawning") && this.worldInfo.getTerrainType() != WorldType.DEBUG_WORLD)
        {
            this.mobSpawner.findChunksForSpawning(this, this.spawnHostileMobs, this.spawnPeacefulMobs, this.worldInfo.getWorldTotalTime() % 400L == 0L);
        }

        this.theProfiler.endStartSection("chunkSource");
        this.chunkProvider.unloadQueuedChunks();
        int var3 = this.calculateSkylightSubtracted(1.0F);

        if (var3 != this.getSkylightSubtracted())
        {
            this.setSkylightSubtracted(var3);
        }

        this.worldInfo.incrementTotalWorldTime(this.worldInfo.getWorldTotalTime() + 1L);

        if (this.getGameRules().getGameRuleBooleanValue("doDaylightCycle"))
        {
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

    public BiomeGenBase.SpawnListEntry getSpawnListEntryForTypeAt(EnumCreatureType creatureType, BlockPos pos)
    {
        List var3 = this.getChunkProvider().getPossibleCreatures(creatureType, pos);
        return var3 != null && !var3.isEmpty() ? (BiomeGenBase.SpawnListEntry)WeightedRandom.getRandomItem(this.rand, var3) : null;
    }

    public boolean func_175732_a(EnumCreatureType creatureType, BiomeGenBase.SpawnListEntry spawnListEntry, BlockPos pos)
    {
        List var4 = this.getChunkProvider().getPossibleCreatures(creatureType, pos);
        return var4 != null && !var4.isEmpty() ? var4.contains(spawnListEntry) : false;
    }

    /**
     * Updates the flag that indicates whether or not all players in the world are sleeping.
     */
    @Override
	public void updateAllPlayersSleepingFlag()
    {
        this.allPlayersSleeping = false;

        if (!this.playerEntities.isEmpty())
        {
            int var1 = 0;
            int var2 = 0;
            Iterator var3 = this.playerEntities.iterator();

            while (var3.hasNext())
            {
                EntityPlayer var4 = (EntityPlayer)var3.next();

                if (var4.isSpectator())
                {
                    ++var1;
                }
                else if (var4.isPlayerSleeping())
                {
                    ++var2;
                }
            }

            this.allPlayersSleeping = var2 > 0 && var2 >= this.playerEntities.size() - var1;
        }
    }

    protected void wakeAllPlayers()
    {
        this.allPlayersSleeping = false;
        Iterator var1 = this.playerEntities.iterator();

        while (var1.hasNext())
        {
            EntityPlayer var2 = (EntityPlayer)var1.next();

            if (var2.isPlayerSleeping())
            {
                var2.wakeUpPlayer(false, false, true);
            }
        }

        this.resetRainAndThunder();
    }

    private void resetRainAndThunder()
    {
        this.worldInfo.setRainTime(0);
        this.worldInfo.setRaining(false);
        this.worldInfo.setThunderTime(0);
        this.worldInfo.setThundering(false);
    }

    public boolean areAllPlayersAsleep()
    {
        if (this.allPlayersSleeping && !this.isRemote)
        {
            Iterator var1 = this.playerEntities.iterator();
            EntityPlayer var2;

            do
            {
                if (!var1.hasNext())
                {
                    return true;
                }

                var2 = (EntityPlayer)var1.next();
            }
            while (!var2.isSpectator() && var2.isPlayerFullyAsleep());

            return false;
        }
        else
        {
            return false;
        }
    }

    /**
     * Sets a new spawn location by finding an uncovered block at a random (x,z) location in the chunk.
     */
    @Override
	public void setInitialSpawnLocation()
    {
        if (this.worldInfo.getSpawnY() <= 0)
        {
            this.worldInfo.setSpawnY(64);
        }

        int var1 = this.worldInfo.getSpawnX();
        int var2 = this.worldInfo.getSpawnZ();
        int var3 = 0;

        while (this.getGroundAboveSeaLevel(new BlockPos(var1, 0, var2)).getMaterial() == Material.air)
        {
            var1 += this.rand.nextInt(8) - this.rand.nextInt(8);
            var2 += this.rand.nextInt(8) - this.rand.nextInt(8);
            ++var3;

            if (var3 == 10000)
            {
                break;
            }
        }

        this.worldInfo.setSpawnX(var1);
        this.worldInfo.setSpawnZ(var2);
    }

    @Override
	protected void updateBlocks()
    {
        super.updateBlocks();

        if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD)
        {
            Iterator var21 = this.activeChunkSet.iterator();

            while (var21.hasNext())
            {
                ChunkCoordIntPair var22 = (ChunkCoordIntPair)var21.next();
                this.getChunkFromChunkCoords(var22.chunkXPos, var22.chunkZPos).func_150804_b(false);
            }
        }
        else
        {
            for (Iterator var3 = this.activeChunkSet.iterator(); var3.hasNext(); this.theProfiler.endSection())
            {
                ChunkCoordIntPair var4 = (ChunkCoordIntPair)var3.next();
                int var5 = var4.chunkXPos * 16;
                int var6 = var4.chunkZPos * 16;
                this.theProfiler.startSection("getChunk");
                Chunk var7 = this.getChunkFromChunkCoords(var4.chunkXPos, var4.chunkZPos);
                this.playMoodSoundAndCheckLight(var5, var6, var7);
                this.theProfiler.endStartSection("tickChunk");
                var7.func_150804_b(false);
                this.theProfiler.endStartSection("thunder");
                int var8;
                BlockPos var9;

                if (this.rand.nextInt(100000) == 0 && this.isRaining() && this.isThundering())
                {
                    this.updateLCG = this.updateLCG * 3 + 1013904223;
                    var8 = this.updateLCG >> 2;
                    var9 = this.func_175736_a(new BlockPos(var5 + (var8 & 15), 0, var6 + (var8 >> 8 & 15)));

                    if (this.canLightningStrike(var9))
                    {
                        this.addWeatherEffect(new EntityLightningBolt(this, var9.getX(), var9.getY(), var9.getZ()));
                    }
                }

                this.theProfiler.endStartSection("iceandsnow");

                if (this.rand.nextInt(16) == 0)
                {
                    this.updateLCG = this.updateLCG * 3 + 1013904223;
                    var8 = this.updateLCG >> 2;
                    var9 = this.getPrecipitationHeight(new BlockPos(var5 + (var8 & 15), 0, var6 + (var8 >> 8 & 15)));
                    BlockPos var10 = var9.down();

                    if (this.canBlockFreezeNoWater(var10))
                    {
                        this.setBlockState(var10, Blocks.ice.getDefaultState());
                    }

                    if (this.isRaining() && this.canSnowAt(var9, true))
                    {
                        this.setBlockState(var9, Blocks.snow_layer.getDefaultState());
                    }

                    if (this.isRaining() && this.getBiomeGenForCoords(var10).canSpawnLightningBolt())
                    {
                        this.getBlockState(var10).getBlock().fillWithRain(this, var10);
                    }
                }

                this.theProfiler.endStartSection("tickBlocks");
                var8 = this.getGameRules().getInt("randomTickSpeed");

                if (var8 > 0)
                {
                    ExtendedBlockStorage[] var23 = var7.getBlockStorageArray();
                    int var24 = var23.length;

                    for (int var11 = 0; var11 < var24; ++var11)
                    {
                        ExtendedBlockStorage var12 = var23[var11];

                        if (var12 != null && var12.getNeedsRandomTick())
                        {
                            for (int var13 = 0; var13 < var8; ++var13)
                            {
                                this.updateLCG = this.updateLCG * 3 + 1013904223;
                                int var14 = this.updateLCG >> 2;
                                int var15 = var14 & 15;
                                int var16 = var14 >> 8 & 15;
                                int var17 = var14 >> 16 & 15;
                                BlockPos var18 = new BlockPos(var15 + var5, var17 + var12.getYLocation(), var16 + var6);
                                IBlockState var19 = var12.get(var15, var17, var16);
                                Block var20 = var19.getBlock();

                                if (var20.getTickRandomly())
                                {
                                    var20.randomTick(this, var18, var19, this.rand);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected BlockPos func_175736_a(BlockPos pos)
    {
        BlockPos var2 = this.getPrecipitationHeight(pos);
        AxisAlignedBB var3 = (new AxisAlignedBB(var2, new BlockPos(var2.getX(), this.getHeight(), var2.getZ()))).expand(3.0D, 3.0D, 3.0D);
        List var4 = this.getEntitiesWithinAABB(EntityLivingBase.class, var3, new Predicate()
        {
            public boolean applyLiving(EntityLivingBase living)
            {
                return living != null && living.isEntityAlive() && WorldServer.this.canSeeSky(living.getPosition());
            }
            @Override
			public boolean apply(Object p_apply_1_)
            {
                return this.applyLiving((EntityLivingBase)p_apply_1_);
            }
        });
        return !var4.isEmpty() ? ((EntityLivingBase)var4.get(this.rand.nextInt(var4.size()))).getPosition() : var2;
    }

    @Override
	public boolean isBlockTickPending(BlockPos pos, Block blockType)
    {
        NextTickListEntry var3 = new NextTickListEntry(pos, blockType);
        return this.pendingTickListEntriesThisTick.contains(var3);
    }

    @Override
	public void scheduleUpdate(BlockPos pos, Block blockIn, int delay)
    {
        this.updateBlockTick(pos, blockIn, delay, 0);
    }

    @Override
	public void updateBlockTick(BlockPos pos, Block blockIn, int p_175654_3_, int p_175654_4_)
    {
        NextTickListEntry var5 = new NextTickListEntry(pos, blockIn);
        byte var6 = 0;

        if (this.scheduledUpdatesAreImmediate && blockIn.getMaterial() != Material.air)
        {
            if (blockIn.requiresUpdates())
            {
                var6 = 8;

                if (this.isAreaLoaded(var5.position.add(-var6, -var6, -var6), var5.position.add(var6, var6, var6)))
                {
                    IBlockState var7 = this.getBlockState(var5.position);

                    if (var7.getBlock().getMaterial() != Material.air && var7.getBlock() == var5.getBlock())
                    {
                        var7.getBlock().updateTick(this, var5.position, var7, this.rand);
                    }
                }

                return;
            }

            p_175654_3_ = 1;
        }

        if (this.isAreaLoaded(pos.add(-var6, -var6, -var6), pos.add(var6, var6, var6)))
        {
            if (blockIn.getMaterial() != Material.air)
            {
                var5.setScheduledTime(p_175654_3_ + this.worldInfo.getWorldTotalTime());
                var5.setPriority(p_175654_4_);
            }

            if (!this.pendingTickListEntriesHashSet.contains(var5))
            {
                this.pendingTickListEntriesHashSet.add(var5);
                this.pendingTickListEntriesTreeSet.add(var5);
            }
        }
    }

    @Override
	public void scheduleBlockUpdate(BlockPos pos, Block blockIn, int delay, int priority)
    {
        NextTickListEntry var5 = new NextTickListEntry(pos, blockIn);
        var5.setPriority(priority);

        if (blockIn.getMaterial() != Material.air)
        {
            var5.setScheduledTime(delay + this.worldInfo.getWorldTotalTime());
        }

        if (!this.pendingTickListEntriesHashSet.contains(var5))
        {
            this.pendingTickListEntriesHashSet.add(var5);
            this.pendingTickListEntriesTreeSet.add(var5);
        }
    }

    /**
     * Updates (and cleans up) entities and tile entities
     */
    @Override
	public void updateEntities()
    {
        if (this.playerEntities.isEmpty())
        {
            if (this.updateEntityTick++ >= 1200)
            {
                return;
            }
        }
        else
        {
            this.resetUpdateEntityTick();
        }

        super.updateEntities();
    }

    /**
     * Resets the updateEntityTick field to 0
     */
    public void resetUpdateEntityTick()
    {
        this.updateEntityTick = 0;
    }

    /**
     * Runs through the list of updates to run and ticks them
     */
    @Override
	public boolean tickUpdates(boolean p_72955_1_)
    {
        if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD)
        {
            return false;
        }
        else
        {
            int var2 = this.pendingTickListEntriesTreeSet.size();

            if (var2 != this.pendingTickListEntriesHashSet.size())
            {
                throw new IllegalStateException("TickNextTick list out of synch");
            }
            else
            {
                if (var2 > 1000)
                {
                    var2 = 1000;
                }

                this.theProfiler.startSection("cleaning");
                NextTickListEntry var4;

                for (int var3 = 0; var3 < var2; ++var3)
                {
                    var4 = (NextTickListEntry)this.pendingTickListEntriesTreeSet.first();

                    if (!p_72955_1_ && var4.scheduledTime > this.worldInfo.getWorldTotalTime())
                    {
                        break;
                    }

                    this.pendingTickListEntriesTreeSet.remove(var4);
                    this.pendingTickListEntriesHashSet.remove(var4);
                    this.pendingTickListEntriesThisTick.add(var4);
                }

                this.theProfiler.endSection();
                this.theProfiler.startSection("ticking");
                Iterator var11 = this.pendingTickListEntriesThisTick.iterator();

                while (var11.hasNext())
                {
                    var4 = (NextTickListEntry)var11.next();
                    var11.remove();
                    byte var5 = 0;

                    if (this.isAreaLoaded(var4.position.add(-var5, -var5, -var5), var4.position.add(var5, var5, var5)))
                    {
                        IBlockState var6 = this.getBlockState(var4.position);

                        if (var6.getBlock().getMaterial() != Material.air && Block.isEqualTo(var6.getBlock(), var4.getBlock()))
                        {
                            try
                            {
                                var6.getBlock().updateTick(this, var4.position, var6, this.rand);
                            }
                            catch (Throwable var10)
                            {
                                CrashReport var8 = CrashReport.makeCrashReport(var10, "Exception while ticking a block");
                                CrashReportCategory var9 = var8.makeCategory("Block being ticked");
                                CrashReportCategory.addBlockInfo(var9, var4.position, var6);
                                throw new ReportedException(var8);
                            }
                        }
                    }
                    else
                    {
                        this.scheduleUpdate(var4.position, var4.getBlock(), 0);
                    }
                }

                this.theProfiler.endSection();
                this.pendingTickListEntriesThisTick.clear();
                return !this.pendingTickListEntriesTreeSet.isEmpty();
            }
        }
    }

    @Override
	public List getPendingBlockUpdates(Chunk chunkIn, boolean p_72920_2_)
    {
        ChunkCoordIntPair var3 = chunkIn.getChunkCoordIntPair();
        int var4 = (var3.chunkXPos << 4) - 2;
        int var5 = var4 + 16 + 2;
        int var6 = (var3.chunkZPos << 4) - 2;
        int var7 = var6 + 16 + 2;
        return this.func_175712_a(new StructureBoundingBox(var4, 0, var6, var5, 256, var7), p_72920_2_);
    }

    @Override
	public List func_175712_a(StructureBoundingBox structureBB, boolean p_175712_2_)
    {
        ArrayList var3 = null;

        for (int var4 = 0; var4 < 2; ++var4)
        {
            Iterator var5;

            if (var4 == 0)
            {
                var5 = this.pendingTickListEntriesTreeSet.iterator();
            }
            else
            {
                var5 = this.pendingTickListEntriesThisTick.iterator();

                if (!this.pendingTickListEntriesThisTick.isEmpty())
                {
                    logger.debug("toBeTicked = " + this.pendingTickListEntriesThisTick.size());
                }
            }

            while (var5.hasNext())
            {
                NextTickListEntry var6 = (NextTickListEntry)var5.next();
                BlockPos var7 = var6.position;

                if (var7.getX() >= structureBB.minX && var7.getX() < structureBB.maxX && var7.getZ() >= structureBB.minZ && var7.getZ() < structureBB.maxZ)
                {
                    if (p_175712_2_)
                    {
                        this.pendingTickListEntriesHashSet.remove(var6);
                        var5.remove();
                    }

                    if (var3 == null)
                    {
                        var3 = Lists.newArrayList();
                    }

                    var3.add(var6);
                }
            }
        }

        return var3;
    }

    /**
     * Will update the entity in the world if the chunk the entity is in is currently loaded or its forced to update.
     * Args: entity, forceUpdate
     */
    @Override
	public void updateEntityWithOptionalForce(Entity entityIn, boolean forceUpdate)
    {
        if (!this.func_175735_ai() && (entityIn instanceof EntityAnimal || entityIn instanceof EntityWaterMob))
        {
            entityIn.setDead();
        }

        if (!this.func_175738_ah() && entityIn instanceof INpc)
        {
            entityIn.setDead();
        }

        super.updateEntityWithOptionalForce(entityIn, forceUpdate);
    }

    private boolean func_175738_ah()
    {
        return this.mcServer.getCanSpawnNPCs();
    }

    private boolean func_175735_ai()
    {
        return this.mcServer.getCanSpawnAnimals();
    }

    /**
     * Creates the chunk provider for this world. Called in the constructor. Retrieves provider from worldProvider?
     */
    @Override
	protected IChunkProvider createChunkProvider()
    {
        IChunkLoader var1 = this.saveHandler.getChunkLoader(this.provider);
        this.theChunkProviderServer = new ChunkProviderServer(this, var1, this.provider.createChunkGenerator());
        return this.theChunkProviderServer;
    }

    public List func_147486_a(int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
    {
        ArrayList var7 = Lists.newArrayList();

        for (int var8 = 0; var8 < this.loadedTileEntityList.size(); ++var8)
        {
            TileEntity var9 = (TileEntity)this.loadedTileEntityList.get(var8);
            BlockPos var10 = var9.getPos();

            if (var10.getX() >= minX && var10.getY() >= minY && var10.getZ() >= minZ && var10.getX() < maxX && var10.getY() < maxY && var10.getZ() < maxZ)
            {
                var7.add(var9);
            }
        }

        return var7;
    }

    @Override
	public boolean isBlockModifiable(EntityPlayer player, BlockPos pos)
    {
        return !this.mcServer.isBlockProtected(this, pos, player) && this.getWorldBorder().contains(pos);
    }

    @Override
	public void initialize(WorldSettings settings)
    {
        if (!this.worldInfo.isInitialized())
        {
            try
            {
                this.createSpawnPosition(settings);

                if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD)
                {
                    this.setDebugWorldSettings();
                }

                super.initialize(settings);
            }
            catch (Throwable var6)
            {
                CrashReport var3 = CrashReport.makeCrashReport(var6, "Exception initializing level");

                try
                {
                    this.addWorldInfoToCrashReport(var3);
                }
                catch (Throwable var5)
                {
                    ;
                }

                throw new ReportedException(var3);
            }

            this.worldInfo.setServerInitialized(true);
        }
    }

    private void setDebugWorldSettings()
    {
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

    /**
     * creates a spawn position at random within 256 blocks of 0,0
     */
    private void createSpawnPosition(WorldSettings p_73052_1_)
    {
        if (!this.provider.canRespawnHere())
        {
            this.worldInfo.setSpawn(BlockPos.ORIGIN.up(this.provider.getAverageGroundLevel()));
        }
        else if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD)
        {
            this.worldInfo.setSpawn(BlockPos.ORIGIN.up());
        }
        else
        {
            this.findingSpawnPoint = true;
            WorldChunkManager var2 = this.provider.getWorldChunkManager();
            List var3 = var2.getBiomesToSpawnIn();
            Random var4 = new Random(this.getSeed());
            BlockPos var5 = var2.findBiomePosition(0, 0, 256, var3, var4);
            int var6 = 0;
            int var7 = this.provider.getAverageGroundLevel();
            int var8 = 0;

            if (var5 != null)
            {
                var6 = var5.getX();
                var8 = var5.getZ();
            }
            else
            {
                logger.warn("Unable to find spawn biome");
            }

            int var9 = 0;

            while (!this.provider.canCoordinateBeSpawn(var6, var8))
            {
                var6 += var4.nextInt(64) - var4.nextInt(64);
                var8 += var4.nextInt(64) - var4.nextInt(64);
                ++var9;

                if (var9 == 1000)
                {
                    break;
                }
            }

            this.worldInfo.setSpawn(new BlockPos(var6, var7, var8));
            this.findingSpawnPoint = false;

            if (p_73052_1_.isBonusChestEnabled())
            {
                this.createBonusChest();
            }
        }
    }

    /**
     * Creates the bonus chest in the world.
     */
    protected void createBonusChest()
    {
        WorldGeneratorBonusChest var1 = new WorldGeneratorBonusChest(bonusChestContent, 10);

        for (int var2 = 0; var2 < 10; ++var2)
        {
            int var3 = this.worldInfo.getSpawnX() + this.rand.nextInt(6) - this.rand.nextInt(6);
            int var4 = this.worldInfo.getSpawnZ() + this.rand.nextInt(6) - this.rand.nextInt(6);
            BlockPos var5 = this.getTopSolidOrLiquidBlock(new BlockPos(var3, 0, var4)).up();

            if (var1.generate(this, this.rand, var5))
            {
                break;
            }
        }
    }

    public BlockPos func_180504_m()
    {
        return this.provider.getSpawnCoordinate();
    }

    /**
     * Saves all chunks to disk while updating progress bar.
     */
    public void saveAllChunks(boolean p_73044_1_, IProgressUpdate p_73044_2_) throws MinecraftException
    {
        if (this.chunkProvider.canSave())
        {
            if (p_73044_2_ != null)
            {
                p_73044_2_.displaySavingString("Saving level");
            }

            this.saveLevel();

            if (p_73044_2_ != null)
            {
                p_73044_2_.displayLoadingString("Saving chunks");
            }

            this.chunkProvider.saveChunks(p_73044_1_, p_73044_2_);
            List var3 = this.theChunkProviderServer.func_152380_a();
            Iterator var4 = var3.iterator();

            while (var4.hasNext())
            {
                Chunk var5 = (Chunk)var4.next();

                if (!this.thePlayerManager.hasPlayerInstance(var5.xPosition, var5.zPosition))
                {
                    this.theChunkProviderServer.dropChunk(var5.xPosition, var5.zPosition);
                }
            }
        }
    }

    /**
     * saves chunk data - currently only called during execution of the Save All command
     */
    public void saveChunkData()
    {
        if (this.chunkProvider.canSave())
        {
            this.chunkProvider.saveExtraData();
        }
    }

    /**
     * Saves the chunks to disk.
     */
    protected void saveLevel() throws MinecraftException
    {
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
	protected void onEntityAdded(Entity entityIn)
    {
        super.onEntityAdded(entityIn);
        this.entitiesById.addKey(entityIn.getEntityId(), entityIn);
        this.entitiesByUuid.put(entityIn.getUniqueID(), entityIn);
        Entity[] var2 = entityIn.getParts();

        if (var2 != null)
        {
            for (int var3 = 0; var3 < var2.length; ++var3)
            {
                this.entitiesById.addKey(var2[var3].getEntityId(), var2[var3]);
            }
        }
    }

    @Override
	protected void onEntityRemoved(Entity entityIn)
    {
        super.onEntityRemoved(entityIn);
        this.entitiesById.removeObject(entityIn.getEntityId());
        this.entitiesByUuid.remove(entityIn.getUniqueID());
        Entity[] var2 = entityIn.getParts();

        if (var2 != null)
        {
            for (int var3 = 0; var3 < var2.length; ++var3)
            {
                this.entitiesById.removeObject(var2[var3].getEntityId());
            }
        }
    }

    /**
     * adds a lightning bolt to the list of lightning bolts in this world.
     */
    @Override
	public boolean addWeatherEffect(Entity entityIn)
    {
        if (super.addWeatherEffect(entityIn))
        {
            this.mcServer.getConfigurationManager().sendToAllNear(entityIn.posX, entityIn.posY, entityIn.posZ, 512.0D, this.provider.getDimensionId(), new S2CPacketSpawnGlobalEntity(entityIn));
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * sends a Packet 38 (Entity Status) to all tracked players of that entity
     */
    @Override
	public void setEntityState(Entity entityIn, byte state)
    {
        this.getEntityTracker().func_151248_b(entityIn, new S19PacketEntityStatus(entityIn, state));
    }

    /**
     * returns a new explosion. Does initiation (at time of writing Explosion is not finished)
     */
    @Override
	public Explosion newExplosion(Entity entityIn, double x, double y, double z, float strength, boolean isFlaming, boolean isSmoking)
    {
        Explosion var11 = new Explosion(this, entityIn, x, y, z, strength, isFlaming, isSmoking);
        var11.doExplosionA();
        var11.doExplosionB(false);

        if (!isSmoking)
        {
            var11.func_180342_d();
        }

        Iterator var12 = this.playerEntities.iterator();

        while (var12.hasNext())
        {
            EntityPlayer var13 = (EntityPlayer)var12.next();

            if (var13.getDistanceSq(x, y, z) < 4096.0D)
            {
                ((EntityPlayerMP)var13).playerNetServerHandler.sendPacket(new S27PacketExplosion(x, y, z, strength, var11.func_180343_e(), (Vec3)var11.func_77277_b().get(var13)));
            }
        }

        return var11;
    }

    @Override
	public void addBlockEvent(BlockPos pos, Block blockIn, int eventID, int eventParam)
    {
        BlockEventData var5 = new BlockEventData(pos, blockIn, eventID, eventParam);
        Iterator var6 = this.field_147490_S[this.blockEventCacheIndex].iterator();
        BlockEventData var7;

        do
        {
            if (!var6.hasNext())
            {
                this.field_147490_S[this.blockEventCacheIndex].add(var5);
                return;
            }

            var7 = (BlockEventData)var6.next();
        }
        while (!var7.equals(var5));
    }

    private void sendQueuedBlockEvents()
    {
        while (!this.field_147490_S[this.blockEventCacheIndex].isEmpty())
        {
            int var1 = this.blockEventCacheIndex;
            this.blockEventCacheIndex ^= 1;
            Iterator var2 = this.field_147490_S[var1].iterator();

            while (var2.hasNext())
            {
                BlockEventData var3 = (BlockEventData)var2.next();

                if (this.fireBlockEvent(var3))
                {
                    this.mcServer.getConfigurationManager().sendToAllNear(var3.getPosition().getX(), var3.getPosition().getY(), var3.getPosition().getZ(), 64.0D, this.provider.getDimensionId(), new S24PacketBlockAction(var3.getPosition(), var3.getBlock(), var3.getEventID(), var3.getEventParameter()));
                }
            }

            this.field_147490_S[var1].clear();
        }
    }

    private boolean fireBlockEvent(BlockEventData event)
    {
        IBlockState var2 = this.getBlockState(event.getPosition());
        return var2.getBlock() == event.getBlock() ? var2.getBlock().onBlockEventReceived(this, event.getPosition(), var2, event.getEventID(), event.getEventParameter()) : false;
    }

    /**
     * Syncs all changes to disk and wait for completion.
     */
    public void flush()
    {
        this.saveHandler.flush();
    }

    /**
     * Updates all weather states.
     */
    @Override
	protected void updateWeather()
    {
        boolean var1 = this.isRaining();
        super.updateWeather();

        if (this.prevRainingStrength != this.rainingStrength)
        {
            this.mcServer.getConfigurationManager().sendPacketToAllPlayersInDimension(new S2BPacketChangeGameState(7, this.rainingStrength), this.provider.getDimensionId());
        }

        if (this.prevThunderingStrength != this.thunderingStrength)
        {
            this.mcServer.getConfigurationManager().sendPacketToAllPlayersInDimension(new S2BPacketChangeGameState(8, this.thunderingStrength), this.provider.getDimensionId());
        }

        if (var1 != this.isRaining())
        {
            if (var1)
            {
                this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(2, 0.0F));
            }
            else
            {
                this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(1, 0.0F));
            }

            this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(7, this.rainingStrength));
            this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(8, this.thunderingStrength));
        }
    }

    @Override
	protected int getRenderDistanceChunks()
    {
        return this.mcServer.getConfigurationManager().getViewDistance();
    }

    public MinecraftServer getMinecraftServer()
    {
        return this.mcServer;
    }

    /**
     * Gets the EntityTracker
     */
    public EntityTracker getEntityTracker()
    {
        return this.theEntityTracker;
    }

    public PlayerManager getPlayerManager()
    {
        return this.thePlayerManager;
    }

    public Teleporter getDefaultTeleporter()
    {
        return this.worldTeleporter;
    }

    /**
     * Spawns the desired particle and sends the necessary packets to the relevant connected players.
     */
    public void spawnParticle(EnumParticleTypes particleType, double xCoord, double yCoord, double zCoord, int numberOfParticles, double p_175739_9_, double p_175739_11_, double p_175739_13_, double p_175739_15_, int ... p_175739_17_)
    {
        this.spawnParticle(particleType, false, xCoord, yCoord, zCoord, numberOfParticles, p_175739_9_, p_175739_11_, p_175739_13_, p_175739_15_, p_175739_17_);
    }

    /**
     * Spawns the desired particle and sends the necessary packets to the relevant connected players.
     *  
     * @param xOffset The offset along the x axis to spread the particle across
     * @param yOffset The offset along the y axis to spread the particle across
     * @param zOffset The offset along the z axis to spread the particle across
     */
    public void spawnParticle(EnumParticleTypes particleType, boolean longDistance, double xCoord, double yCoord, double zCoord, int numberOfParticles, double xOffset, double yOffset, double zOffset, double particleSpeed, int ... p_180505_18_)
    {
        S2APacketParticles var19 = new S2APacketParticles(particleType, longDistance, (float)xCoord, (float)yCoord, (float)zCoord, (float)xOffset, (float)yOffset, (float)zOffset, (float)particleSpeed, numberOfParticles, p_180505_18_);

        for (int var20 = 0; var20 < this.playerEntities.size(); ++var20)
        {
            EntityPlayerMP var21 = (EntityPlayerMP)this.playerEntities.get(var20);
            BlockPos var22 = var21.getPosition();
            double var23 = var22.distanceSq(xCoord, yCoord, zCoord);

            if (var23 <= 256.0D || longDistance && var23 <= 65536.0D)
            {
                var21.playerNetServerHandler.sendPacket(var19);
            }
        }
    }

    public Entity getEntityFromUuid(UUID uuid)
    {
        return (Entity)this.entitiesByUuid.get(uuid);
    }

    @Override
	public ListenableFuture addScheduledTask(Runnable runnableToSchedule)
    {
        return this.mcServer.addScheduledTask(runnableToSchedule);
    }

    @Override
	public boolean isCallingFromMinecraftThread()
    {
        return this.mcServer.isCallingFromMinecraftThread();
    }

    static class ServerBlockEventList extends ArrayList
    {

        private ServerBlockEventList() {}

        ServerBlockEventList(Object p_i1521_1_)
        {
            this();
        }
    }
}
